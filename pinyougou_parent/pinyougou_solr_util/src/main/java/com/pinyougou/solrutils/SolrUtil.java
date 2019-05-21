package com.pinyougou.solrutils;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import entity.SolrItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.solrutils
 * @date 2019-4-20
 */
@Component
public class SolrUtil {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 导入数据到索引库
     */
    public void importData(){
        //从数据库中把所有商品信息查出来
        TbItem where = new TbItem();
        where.setStatus("1");  //只查询已启用的商品信息
        List<TbItem> tbItemList = itemMapper.select(where);
        System.out.println("总共查询到：" + tbItemList.size() + "条记录!\n开始组装数据.");
        List<SolrItem> solrItemList = new ArrayList<>();
        for (TbItem item : tbItemList) {
            SolrItem solrItem = new SolrItem();
            //复制所有属性
            BeanUtils.copyProperties(item, solrItem);
            //设置规格信息
            Map specMap = JSON.parseObject(item.getSpec(), Map.class);
            solrItem.setSpecMap(specMap);

            solrItemList.add(solrItem);
        }
        System.out.println("组装数据完毕,准备导入数据到索引库..");
        //保存到索引库
        solrTemplate.saveBeans(solrItemList);
        solrTemplate.commit();
        System.out.println("索引库导入完毕...");

    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        SolrUtil solrUtil = context.getBean(SolrUtil.class);
        solrUtil.importData();
    }
}
