package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper optionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbSpecification> result = new PageResult<TbSpecification>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbSpecification> list = specificationMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSpecification> info = new PageInfo<TbSpecification>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		//保存规格名称信息
		specificationMapper.insertSelective(specification.getSpecification());

		//保存规格选项列表
		for (TbSpecificationOption option : specification.getSpecificationOptionList()) {
			option.setSpecId(specification.getSpecification().getId());
			optionMapper.insertSelective(option);
		}
	}/**
	 * 增加
	 */
	@Override
	public void add(TbSpecification specification) {
		//保存规格名称信息
		specificationMapper.insertSelective(specification);
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		//更新规格名称信息
		specificationMapper.updateByPrimaryKeySelective(specification.getSpecification());

		//更新规格选项列表
		//先删除所有选项列表
		TbSpecificationOption where = new TbSpecificationOption();
		where.setSpecId(specification.getSpecification().getId());
		optionMapper.delete(where);
		//再重新保存列表
		//保存规格选项列表
		for (TbSpecificationOption option : specification.getSpecificationOptionList()) {
			option.setSpecId(specification.getSpecification().getId());
			optionMapper.insertSelective(option);
		}
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		Specification specification = new Specification();
		//查询规格名称信息
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		specification.setSpecification(tbSpecification);
		//查询选项列表
		TbSpecificationOption where = new TbSpecificationOption();
		where.setSpecId(id);
		List<TbSpecificationOption> options = optionMapper.select(where);
		specification.setSpecificationOptionList(options);

		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);
        //跟据查询条件删除数据
        specificationMapper.deleteByExample(example);

        //删除规格选项列表
		Example example2 = new Example(TbSpecificationOption.class);
		Example.Criteria criteria1 = example2.createCriteria();
		criteria1.andIn("specId", longs);
		optionMapper.deleteByExample(example2);
	}
	
	
	@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageResult<TbSpecification> result = new PageResult<TbSpecification>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);
        //构建查询条件
        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();
		if(specification!=null){			
						//如果字段不为空
			if (specification.getSpecName()!=null && specification.getSpecName().length()>0) {
				criteria.andLike("specName", "%" + specification.getSpecName() + "%");
			}
		}
        //查询数据
        List<TbSpecification> list = specificationMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);
        //获取总记录数
        PageInfo<TbSpecification> info = new PageInfo<TbSpecification>(list);
        result.setTotal(info.getTotal());

		return result;
	}

	//商家的查询另开了一个方法按sellerId查询，不影响运营管理商的全部查询
	@Override
	public List<TbSpecification> sellerFindAll(String loginName) {
		//根据商家ID查询，区别于运营管理商的查询结果这里只返回该商家的申请规格
		TbSpecification where = new TbSpecification();
		where.setSellerId(loginName);
		return specificationMapper.select(where);
	}


	@Override
	public PageResult sellerFindPage(TbSpecification specification, int pageNum, int pageSize, String sellerId) {
		PageResult<TbSpecification> result = new PageResult<TbSpecification>();
		//设置分页条件
		PageHelper.startPage(pageNum, pageSize);
		//构建查询条件
		Example example = new Example(TbSpecification.class);
		Example.Criteria criteria = example.createCriteria();
		if(specification!=null){
			//如果字段不为空
			if (specification.getSpecName()!=null && specification.getSpecName().length()>0) {
				criteria.andLike("specName", "%" + specification.getSpecName() + "%");
			}
			//如果用户名不为空
			if (sellerId !=null && sellerId !="") {
				criteria.andEqualTo("sellerId", sellerId);
			}
		}
		//查询数据
		List<TbSpecification> list = specificationMapper.selectByExample(example);
		//保存数据列表
		result.setRows(list);
		//获取总记录数
		PageInfo<TbSpecification> info = new PageInfo<TbSpecification>(list);
		result.setTotal(info.getTotal());

		return result;
	}

	/**通过规格名称查找规格ID
	 * @param specName
	 * @return
	 */
	@Override
	public Long findOneByName(String specName) {
		TbSpecification where=new TbSpecification();
		where.setSpecName(specName);
        List<TbSpecification> tbSpecifications = specificationMapper.select(where);
        if (tbSpecifications.size()==1){
            for (TbSpecification tbSpecification : tbSpecifications) {
                return tbSpecification.getId();
            }
        }else {
            throw new RuntimeException("您输入的规格名:"+specName+"有误");
        }
        return null;
	}

	/**
	 * 状态审核
	 * @param ids
	 * @param status
	 */
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		//修改的结果
		TbSpecification record = new TbSpecification();
		record.setStatus(status);
		//构建修改范围
		Example example = new Example(TbSpecification.class);
		Example.Criteria criteria = example.createCriteria();
		List longs = Arrays.asList(ids);
		criteria.andIn("id", longs);
		//开始更新
		specificationMapper.updateByExampleSelective(record,example);
	}


}
