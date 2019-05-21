package com.pinyougou.user.service.impl;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.pinyougou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbAddressMapper;
import com.pinyougou.pojo.TbAddress;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private TbAddressMapper addressMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbAddress> findAll() {
		return addressMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbAddress> result = new PageResult<TbAddress>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbAddress> list = addressMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbAddress> info = new PageInfo<TbAddress>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbAddress address) {
		address.setCreateDate(new Date());
		addressMapper.insertSelective(address);
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbAddress address){
		addressMapper.updateByPrimaryKeySelective(address);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbAddress findOne(Long id){ return addressMapper.selectByPrimaryKey(id); }

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        addressMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbAddress address, int pageNum, int pageSize) {
		PageResult<TbAddress> result = new PageResult<TbAddress>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbAddress.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(address!=null){			
						//如果字段不为空
			if (address.getUserId()!=null && address.getUserId().length()>0) {
				criteria.andLike("userId", "%" + address.getUserId() + "%");
			}
			//如果字段不为空
			if (address.getProvinceId()!=null && address.getProvinceId().length()>0) {
				criteria.andLike("provinceId", "%" + address.getProvinceId() + "%");
			}
			//如果字段不为空
			if (address.getCityId()!=null && address.getCityId().length()>0) {
				criteria.andLike("cityId", "%" + address.getCityId() + "%");
			}
			//如果字段不为空
			if (address.getTownId()!=null && address.getTownId().length()>0) {
				criteria.andLike("townId", "%" + address.getTownId() + "%");
			}
			//如果字段不为空
			if (address.getMobile()!=null && address.getMobile().length()>0) {
				criteria.andLike("mobile", "%" + address.getMobile() + "%");
			}
			//如果字段不为空
			if (address.getAddress()!=null && address.getAddress().length()>0) {
				criteria.andLike("address", "%" + address.getAddress() + "%");
			}
			//如果字段不为空
			if (address.getContact()!=null && address.getContact().length()>0) {
				criteria.andLike("contact", "%" + address.getContact() + "%");
			}
			//如果字段不为空
			if (address.getIsDefault()!=null && address.getIsDefault().length()>0) {
				criteria.andLike("isDefault", "%" + address.getIsDefault() + "%");
			}
			//如果字段不为空
			if (address.getNotes()!=null && address.getNotes().length()>0) {
				criteria.andLike("notes", "%" + address.getNotes() + "%");
			}
			//如果字段不为空
			if (address.getAlias()!=null && address.getAlias().length()>0) {
				criteria.andLike("alias", "%" + address.getAlias() + "%");
			}
	
		}

        //查询数据
        List<TbAddress> list = addressMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbAddress> info = new PageInfo<TbAddress>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}

    @Override
    public List<TbAddress> findListByUserId(String userId) {
		TbAddress where = new TbAddress();
		where.setUserId(userId);
		return addressMapper.select(where);
    }

	@Override
	public void deleteOne(Long id) {
		//跟据查询条件删除数据
		addressMapper.deleteByPrimaryKey(id);
	}

}
