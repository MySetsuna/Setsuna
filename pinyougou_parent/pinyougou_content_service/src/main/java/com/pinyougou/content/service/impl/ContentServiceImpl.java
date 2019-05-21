package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbItemCat;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbContent> result = new PageResult<TbContent>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbContent> list = contentMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbContent> info = new PageInfo<TbContent>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insertSelective(content);
		//清空缓存
		redisTemplate.boundHashOps("contents").delete(content.getCategoryId());
	}

	/*public static void main(String[] args) {
		Long a = 128l;
		Long b = 128l;
		System.out.println(a.longValue() == b.longValue());
		Integer c = 128;
		Integer d = 128;
		System.out.println(c == d);
	}*/
	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		//必须要更新之前先把原来的数据查询出来
		TbContent beUpdate = contentMapper.selectByPrimaryKey(content.getId());
		//识别广告分类有没有被修改
		if(beUpdate.getCategoryId().longValue() != content.getCategoryId().longValue()){
			//清空缓存
			redisTemplate.boundHashOps("contents").delete(beUpdate.getCategoryId());
		}
		contentMapper.updateByPrimaryKeySelective(content);
		//清空缓存
		redisTemplate.boundHashOps("contents").delete(content.getCategoryId());
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

		//将要删除的数据列表，一定要先查询出来
		List<TbContent> contentList = contentMapper.selectByExample(example);
		for (TbContent content : contentList) {
			redisTemplate.boundHashOps("contents").delete(content.getCategoryId());
		}

        //跟据查询条件删除数据
        contentMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageResult<TbContent> result = new PageResult<TbContent>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						//如果字段不为空
			if (content.getTitle()!=null && content.getTitle().length()>0) {
				criteria.andLike("title", "%" + content.getTitle() + "%");
			}
			//如果字段不为空
			if (content.getUrl()!=null && content.getUrl().length()>0) {
				criteria.andLike("url", "%" + content.getUrl() + "%");
			}
			//如果字段不为空
			if (content.getPic()!=null && content.getPic().length()>0) {
				criteria.andLike("pic", "%" + content.getPic() + "%");
			}
			//如果字段不为空
			if (content.getStatus()!=null && content.getStatus().length()>0) {
				criteria.andLike("status", "%" + content.getStatus() + "%");
			}
	
		}

        //查询数据
        List<TbContent> list = contentMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbContent> info = new PageInfo<TbContent>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}

    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {
		//从缓存中读取当前的广告列表
		List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("contents").get(categoryId);
		//缓存中没有数据
		if (contentList == null || contentList.size() < 1) {
			Example example = new Example(TbContent.class);
			//组装查询条件
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("categoryId", categoryId);

			//设置排序方式(属性名 asc|desc,属性名 asc|desc)
			//多个字段排序，以","分隔
			example.setOrderByClause("sortOrder asc");
			//查询数据
			contentList = contentMapper.selectByExample(example);
			//把数据放入缓存
			redisTemplate.boundHashOps("contents").put(categoryId, contentList);
		}else{
			System.out.println("从缓存中读取了广告列表");
		}
		return contentList;
    }

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public Map<String, List<TbItemCat>> findAllCats() {

		Map<String, List<TbItemCat>> map =(Map<String, List<TbItemCat>>) redisTemplate.boundHashOps("cats").get("map");
		if (map == null ){
			map= new HashMap<>();
			TbItemCat where = new TbItemCat();
			where.setParentId(new Long(0));
			List<TbItemCat> itemCats1 = itemCatMapper.select(where);
			//放好1级
			map.put("0", itemCats1);
			for (TbItemCat cat1: itemCats1) {
				where.setParentId(cat1.getId());
				List<TbItemCat> itemCats2 = itemCatMapper.select(where);
				//放好2级
				map.put(cat1.getId()+"", itemCats2);
				for (TbItemCat cat2 : itemCats2) {
					where.setParentId(cat2.getId());
					List<TbItemCat> itemCats3= itemCatMapper.select(where);
					//放好2级
					map.put(cat2.getId()+"", itemCats3);
				}
			}
			System.out.println("将分类信息存入缓存！");
			redisTemplate.boundHashOps("cats").put("map", map);
		}else {
			System.out.println("从缓存中查找了分类信息！");
		}

		return map;
	}






}
