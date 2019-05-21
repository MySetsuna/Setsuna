package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
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
public class BrandServiceImpl implements BrandService {

	@Autowired
	private TbBrandMapper brandMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbBrand> findAll() {
		return brandMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbBrand> result = new PageResult<TbBrand>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbBrand> list = brandMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbBrand> info = new PageInfo<TbBrand>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbBrand brand) {
		brandMapper.insertSelective(brand);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbBrand brand){
		brandMapper.updateByPrimaryKeySelective(brand);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbBrand findOne(Long id){
		return brandMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        brandMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
		PageResult<TbBrand> result = new PageResult<TbBrand>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(brand!=null){			
						//如果字段不为空
			if (brand.getName()!=null && brand.getName().length()>0) {
				criteria.andLike("name", "%" + brand.getName() + "%");
			}
			//如果字段不为空
			if (brand.getFirstChar()!=null && brand.getFirstChar().length()>0) {
				criteria.andLike("firstChar", "%" + brand.getFirstChar() + "%");
			}
	
		}

        //查询数据
        List<TbBrand> list = brandMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbBrand> info = new PageInfo<TbBrand>(list);
        result.setTotal(info.getTotal());

		return result;
	}

	//商家的查询另开了一个方法按sellerId查询，不影响运营管理商的全部查询
	@Override
	public List<TbBrand> sellerFindAll(String loginName) {
		//根据商家ID查询，区别于运营管理商的查询结果这里只返回该商家的申请品牌
		TbBrand where = new TbBrand();
		where.setSellerId(loginName);

		return brandMapper.select(where);
	}

	@Override
	public PageResult sellerFindPage(TbBrand brand, int pageNum, int pageSize, String sellerId) {
		PageResult<TbBrand> result = new PageResult<TbBrand>();
		//设置分页条件
		PageHelper.startPage(pageNum, pageSize);

		//构建查询条件
		Example example = new Example(TbBrand.class);
		Example.Criteria criteria = example.createCriteria();

		if(brand!=null){
			//如果字段不为空
			if (brand.getName()!=null && brand.getName().length()>0) {
				criteria.andLike("name", "%" + brand.getName() + "%");
			}
			//如果字段不为空
			if (brand.getFirstChar()!=null && brand.getFirstChar().length()>0) {
				criteria.andLike("firstChar", "%" + brand.getFirstChar() + "%");
			}
			//如果用户名不为空
			if (sellerId !=null && sellerId !="") {
				criteria.andEqualTo("sellerId", sellerId);
			}
		}

		//查询数据
		List<TbBrand> list = brandMapper.selectByExample(example);
		//保存数据列表
		result.setRows(list);

		//获取总记录数
		PageInfo<TbBrand> info = new PageInfo<TbBrand>(list);
		result.setTotal(info.getTotal());

		return result;
	}

	/**
	 * 状态审核
	 * @param ids
	 * @param status
	 */
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		//修改的结果
		TbBrand record = new TbBrand();
		record.setStatus(status);
		//构建修改范围
		Example example = new Example(TbBrand.class);
		Example.Criteria criteria = example.createCriteria();
		List longs = Arrays.asList(ids);
		criteria.andIn("id", longs);
		//开始更新
		brandMapper.updateByExampleSelective(record,example);
	}

    @Override
    public Long findBrandIdByName(String name) {
		TbBrand where=new TbBrand();
		where.setName(name);
        List<TbBrand> tbBrands = brandMapper.select(where);
        if (tbBrands.size()==1){
            for (TbBrand tbBrand : tbBrands) {
                return tbBrand.getId();
            }
        }else {
            throw new RuntimeException("您Excel输入的品牌名:"+name+"有误");
        }
        return null;
    }


}
