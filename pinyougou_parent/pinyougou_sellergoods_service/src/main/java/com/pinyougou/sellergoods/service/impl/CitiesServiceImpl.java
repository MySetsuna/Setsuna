package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbCitiesMapper;
import com.pinyougou.pojo.TbCities;
import com.pinyougou.sellergoods.service.CitiesService;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class CitiesServiceImpl implements CitiesService {

	@Autowired
	private TbCitiesMapper citiesMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbCities> findAll() {
		return citiesMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbCities> result = new PageResult<TbCities>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbCities> list = citiesMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbCities> info = new PageInfo<TbCities>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbCities cities) {
		citiesMapper.insertSelective(cities);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbCities cities){
		citiesMapper.updateByPrimaryKeySelective(cities);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbCities findOne(Long id){
		return citiesMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbCities.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        citiesMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbCities cities, int pageNum, int pageSize) {
		PageResult<TbCities> result = new PageResult<TbCities>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbCities.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(cities!=null){			
						//如果字段不为空
			if (cities.getCityid()!=null && cities.getCityid().length()>0) {
				criteria.andLike("cityid", "%" + cities.getCityid() + "%");
			}
			//如果字段不为空
			if (cities.getCity()!=null && cities.getCity().length()>0) {
				criteria.andLike("city", "%" + cities.getCity() + "%");
			}
			//如果字段不为空
			if (cities.getProvinceid()!=null && cities.getProvinceid().length()>0) {
				criteria.andLike("provinceid", "%" + cities.getProvinceid() + "%");
			}
	
		}

        //查询数据
        List<TbCities> list = citiesMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbCities> info = new PageInfo<TbCities>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}
	
}
