package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationOptionService;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class SpecificationOptionServiceImpl implements SpecificationOptionService {

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecificationOption> findAll() {
		return specificationOptionMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbSpecificationOption> result = new PageResult<TbSpecificationOption>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbSpecificationOption> list = specificationOptionMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSpecificationOption> info = new PageInfo<TbSpecificationOption>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSpecificationOption specificationOption) {
		specificationOptionMapper.insertSelective(specificationOption);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSpecificationOption specificationOption){
		specificationOptionMapper.updateByPrimaryKeySelective(specificationOption);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSpecificationOption findOne(Long id){
		return specificationOptionMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbSpecificationOption.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        specificationOptionMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbSpecificationOption specificationOption, int pageNum, int pageSize) {
		PageResult<TbSpecificationOption> result = new PageResult<TbSpecificationOption>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbSpecificationOption.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(specificationOption!=null){			
						//如果字段不为空
			if (specificationOption.getOptionName()!=null && specificationOption.getOptionName().length()>0) {
				criteria.andLike("optionName", "%" + specificationOption.getOptionName() + "%");
			}
	
		}

        //查询数据
        List<TbSpecificationOption> list = specificationOptionMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSpecificationOption> info = new PageInfo<TbSpecificationOption>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}
	
}
