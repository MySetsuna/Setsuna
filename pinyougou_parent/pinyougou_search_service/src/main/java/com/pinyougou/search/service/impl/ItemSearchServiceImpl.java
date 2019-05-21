package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.search.service.ItemSearchService;
import entity.SolrItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.search.service.impl
 * @date 2019-4-20
 */
@Service(timeout = 5000, interfaceClass = ItemSearchService.class)
@Transactional
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map search(Map searchMap) {
        Map map = new HashMap();
        //组装查询条件
        if(searchMap != null){
            //1、查询商品列表
            searchList(searchMap, map);

            //2、分组查询分类列表
            searchCategoryList(searchMap, map);

            //3、查询品牌与规格列表
            String category = searchMap.get("category") == null ? "" : searchMap.get("category").toString();
            //如果用户没有选择了商品分类条件
            if(category.trim().length() < 1){
                if (map.get("categoryList") != null) {
                    //默认读取第一个分类
                    List<String> categoryList = (List<String>) map.get("categoryList");
                    category = categoryList.get(0);
                }
            }
            searchBrandAndSpecList(category,map);
        }
        return map;
    }

    @Override
    public void importList(List list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodsIds(Long[] goodsIdList) {
        //构建删除条件
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(goodsIdList);
        query.addCriteria(criteria);
        //执行删除
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    @Override
    public void addGoodsToFootmark(String userId,Long goodsId) {
//        Map<String,List<Long>> footMarkMap = (Map<String, List<Long>>) redisTemplate.boundHashOps("footMarkList").get(userId);
//        if (footMarkMap == null) {
//            footMarkMap = new HashMap<String, List<Long>>();
//            List<Long> goodIds = new ArrayList<>();
//            goodIds.add(goodsId);
//            footMarkMap.put(userId, goodIds);
//        } else {
//            footMarkMap.get(userId).add(goodsId);
//        }
//        redisTemplate.boundHashOps("footMarkList").put(userId,footMarkMap);

        //使用二级map加上set
//        Map<String,Set> footMarkMap = (Map<String, Set>) redisTemplate.boundHashOps("footMarkList").get(userId);
//        if (footMarkMap == null) {
//            footMarkMap = new HashMap<String, Set>();
//            HashSet<Long> goodsIdSet = new HashSet<>();
//            goodsIdSet.add(goodsId);
//            footMarkMap.put(userId, goodsIdSet);
//        } else {
//            footMarkMap.get(userId).add(goodsId);
//        }
//        redisTemplate.boundHashOps("footMarkList").put(userId,footMarkMap);

        //map里加set集合
        Set<Long> footMarkSet = (Set<Long>) redisTemplate.boundHashOps("footMarkList").get(userId);
        if (footMarkSet == null) {
            footMarkSet = new HashSet<Long>();
            footMarkSet.add(goodsId);
        } else {
            footMarkSet.add(goodsId);
        }
        redisTemplate.boundHashOps("footMarkList").put(userId,footMarkSet);
    }

    /**
     * 根据分类名称-查询品牌与规格列表
     * @param category 分类名称
     * @param map 查询到的结果
     */
    private void searchBrandAndSpecList(String category, Map map) {
        //查询品牌列表
        //先根据分类名称查询模板id
        Long typeId = (Long) redisTemplate.boundHashOps("itemCats").get(category);
        if(typeId != null){
            //查询品牌列表
            List<Map> brandIds = (List<Map>) redisTemplate.boundHashOps("brandIds").get(typeId);
            map.put("brandIds", brandIds);
            //查询规格列表
            List<Map> specIds = (List<Map>) redisTemplate.boundHashOps("specIds").get(typeId);
            map.put("specIds", specIds);
        }
    }

    /**
     * 根据查询条件-分组查询商品分类列表
     * @param searchMap 查询条件
     * @param map 查询结果
     */
    private void searchCategoryList(Map searchMap, Map map) {
        //1.创建查询条件对象query = new SimpleQuery()
        Query query = new SimpleQuery();
        //2.复制之前的Criteria组装查询条件的代码
        //关键字探索
        String keywords = searchMap.get("keywords") == null ? "" : searchMap.get("keywords").toString();
        if(keywords.trim().length() > 0){
            Criteria criteria = new Criteria("item_keywords").is(keywords);
            query.addCriteria(criteria);
        }
        //3.创建分组选项对象new GroupOptions().addGroupByField(域名)
        GroupOptions gOptions = new GroupOptions().addGroupByField("item_category");
        //4.设置分组对象query.setGroupOptions
        query.setGroupOptions(gOptions);
        //5.得到分组页对象page = solrTemplate.queryForGroupPage
        GroupPage<SolrItem> page = solrTemplate.queryForGroupPage(query, SolrItem.class);
        //6.得到分组结果集groupResult = page.getGroupResult(域名)
        GroupResult<SolrItem> groupResult = page.getGroupResult("item_category");
        //7.得到分组结果入口groupEntries = groupResult.getGroupEntries()
        Page<GroupEntry<SolrItem>> groupEntries = groupResult.getGroupEntries();
        //8.得到分组入口集合content = groupEntries.getContent()
        List<GroupEntry<SolrItem>> content = groupEntries.getContent();
        //当前查询结果中的商品分类列表
        List<String> categoryList = new ArrayList<>();
        //9.遍历分组入口集合content.for(entry)，记录结果entry.getGroupValue()
        for (GroupEntry<SolrItem> entry : content) {
            //每一行分类名字
            categoryList.add(entry.getGroupValue());
        }
        //返回结果
        map.put("categoryList", categoryList);
    }

    /**
     * 根据查询条件-搜索商品列表
     * @param searchMap 查找条件
     * @param map 查询结果
     */
    private void searchList(Map searchMap, Map map) {
        //1.调用solrTemplate.queryForHighlightPage(query,class)方法，高亮查询数据
        //2.构建query高亮查询对象new SimpleHighlightQuery
        HighlightQuery query = new SimpleHighlightQuery();
        //3.复制之前的Criteria组装查询条件的代码
        //3.1关键字查询
        String keywords = searchMap.get("keywords") == null ? "" : searchMap.get("keywords").toString();
        //去掉空格
        keywords = keywords.replaceAll(" ", "");
        //记得这里要重新放回searchMap中，因为后续要使用这个变量
        searchMap.put("keywords", keywords);
        if(keywords.trim().length() > 0){
            Criteria criteria = new Criteria("item_keywords").is(keywords);
            query.addCriteria(criteria);
        }
        //3.2商品分类过滤
        String category = searchMap.get("category") == null ? "" : searchMap.get("category").toString();
        if(category.trim().length() > 0){
            Criteria criteria = new Criteria("item_category").is(category);
            FilterQuery filterQuery = new SimpleFilterQuery(criteria);
            query.addFilterQuery(filterQuery);
        }
        //3.3品牌过滤
        String brand = searchMap.get("brand") == null ? "" : searchMap.get("brand").toString();
        if(brand.trim().length() > 0){
            Criteria criteria = new Criteria("item_brand").is(brand);
            FilterQuery filterQuery = new SimpleFilterQuery(criteria);
            query.addFilterQuery(filterQuery);
        }
        //3.4规格过滤
        String spec = searchMap.get("spec") == null ? "" : searchMap.get("spec").toString();
        if(spec.trim().length() > 0){
            Map<String,String> specMap = JSON.parseObject(spec, Map.class);
            for (String key : specMap.keySet()) {
                Criteria criteria = new Criteria("item_spec_" + key).is(specMap.get(key));
                FilterQuery filterQuery = new SimpleFilterQuery(criteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //3.5价格区间过滤，前端传过来的数据为：0-500,500-1000.....3000-*
        String price = searchMap.get("price") == null ? "" : searchMap.get("price").toString();
        if(price.trim().length() > 0){
            /* 方式一：使用between，存在*报错问题，解决方案是，有*把split[1]设置一个很大的值
            String[] split = price.split("-");
            Criteria criteria = new Criteria("item_price").between(split[0],split[1]);
            FilterQuery filterQuery = new SimpleFilterQuery(criteria);
            query.addFilterQuery(filterQuery);*/

            //方式二：使用大于与小于
            String[] split = price.split("-");
            if(!split[0].equals("0")) {
                Criteria criteria = new Criteria("item_price").greaterThanEqual(split[0]);
                FilterQuery filterQuery = new SimpleFilterQuery(criteria);
                query.addFilterQuery(filterQuery);
            }
            if(!split[1].equals("*")) {
                Criteria criteria = new Criteria("item_price").lessThanEqual(split[1]);
                FilterQuery filterQuery = new SimpleFilterQuery(criteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //3.6分页查询
        //当前页
        Integer pageNo = searchMap.get("pageNo") == null ? 1 : new Integer(searchMap.get("pageNo").toString());
        //每页查询的记录数
        Integer pageSize = searchMap.get("pageSize") == null ? 20 : new Integer(searchMap.get("pageSize").toString());
        //设置分页条件
        query.setOffset((pageNo - 1) * pageSize);
        query.setRows(pageSize);
        //3.7排序查询
        //排序的域名,前端传过来时少了item_
        String sortField = searchMap.get("sortField") == null ? "" : searchMap.get("sortField").toString();
        //排序方式asc|desc
        String sortStr = searchMap.get("sort") == null ? "" : searchMap.get("sort").toString();
        if(sortField.trim().length() > 0){
            if(sortStr.toUpperCase().equals("ASC")) {
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
                query.addSort(sort);
            }
            if(sortStr.toUpperCase().equals("DESC")) {
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(sort);
            }
        }

        //4.调用query.setHighlightOptions()方法，
        // 构建高亮数据三步曲：1、new HighlightOptions().
        HighlightOptions hOptions = new HighlightOptions();
        // 3.2、addField(高亮业务域)，
        hOptions.addField("item_title");
        // 3.3、.setSimpleP..(前缀)，.setSimpleP..(后缀)
        hOptions.setSimplePrefix("<em style=\"color:red;\">");
        hOptions.setSimplePostfix("</em>");
        query.setHighlightOptions(hOptions);
        // 5.接收solrTemplate.queryForHighlightPage的返回数据，定义page变量
        HighlightPage<SolrItem> page = solrTemplate.queryForHighlightPage(query, SolrItem.class);
        //6.遍历解析page对象，page.getHighlighted().for，
        // item = h.getEntity()，item.setTitle(h.getHighlights().get(0).getSnipplets().get(0))，
        // 在设置高亮之前最好判断一下;
        for (HighlightEntry<SolrItem> h : page.getHighlighted()) {
            SolrItem item = h.getEntity();
            //如果我们查询结果里面有高亮数据
            if(h.getHighlights() != null && h.getHighlights().size() > 0 &&
                    h.getHighlights().get(0).getSnipplets() != null &&
                    h.getHighlights().get(0).getSnipplets().size() > 0) {
                //修改item_itle
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
            }
        }
        //7.在循环完成外map.put("rows", page.getContent())返回数据列表
        map.put("rows", page.getContent());
        //返回分页数据
        map.put("total", page.getTotalElements());
        map.put("totalPages", page.getTotalPages());
    }
}
