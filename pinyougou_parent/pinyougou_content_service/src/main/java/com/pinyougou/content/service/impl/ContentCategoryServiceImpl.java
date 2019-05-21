package com.pinyougou.content.service.impl;
import java.util.Arrays;
import java.util.List;

import com.pinyougou.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbContentCategoryMapper;
import com.pinyougou.pojo.TbContentCategory;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContentCategory> findAll() {
		return contentCategoryMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbContentCategory> result = new PageResult<TbContentCategory>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbContentCategory> list = contentCategoryMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbContentCategory> info = new PageInfo<TbContentCategory>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContentCategory contentCategory) {
		contentCategoryMapper.insertSelective(contentCategory);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContentCategory contentCategory){
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContentCategory findOne(Long id){
		return contentCategoryMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbContentCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        contentCategoryMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbContentCategory contentCategory, int pageNum, int pageSize) {
		PageResult<TbContentCategory> result = new PageResult<TbContentCategory>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbContentCategory.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(contentCategory!=null){			
						//如果字段不为空
			if (contentCategory.getName()!=null && contentCategory.getName().length()>0) {
				criteria.andLike("name", "%" + contentCategory.getName() + "%");
			}
	
		}

        //查询数据
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbContentCategory> info = new PageInfo<TbContentCategory>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}
	
}
