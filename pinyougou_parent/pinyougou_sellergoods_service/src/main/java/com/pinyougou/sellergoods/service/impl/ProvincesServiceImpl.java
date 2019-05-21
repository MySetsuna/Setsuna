package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbProvincesMapper;
import com.pinyougou.pojo.TbProvinces;
import com.pinyougou.sellergoods.service.ProvincesService;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class ProvincesServiceImpl implements ProvincesService {

	@Autowired
	private TbProvincesMapper provincesMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbProvinces> findAll() {
		return provincesMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbProvinces> result = new PageResult<TbProvinces>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbProvinces> list = provincesMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbProvinces> info = new PageInfo<TbProvinces>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbProvinces provinces) {
		provincesMapper.insertSelective(provinces);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbProvinces provinces){
		provincesMapper.updateByPrimaryKeySelective(provinces);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbProvinces findOne(Long id){
		return provincesMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbProvinces.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        provincesMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbProvinces provinces, int pageNum, int pageSize) {
		PageResult<TbProvinces> result = new PageResult<TbProvinces>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbProvinces.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(provinces!=null){			
						//如果字段不为空
			if (provinces.getProvinceid()!=null && provinces.getProvinceid().length()>0) {
				criteria.andLike("provinceid", "%" + provinces.getProvinceid() + "%");
			}
			//如果字段不为空
			if (provinces.getProvince()!=null && provinces.getProvince().length()>0) {
				criteria.andLike("province", "%" + provinces.getProvince() + "%");
			}
	
		}

        //查询数据
        List<TbProvinces> list = provincesMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbProvinces> info = new PageInfo<TbProvinces>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}
	
}
