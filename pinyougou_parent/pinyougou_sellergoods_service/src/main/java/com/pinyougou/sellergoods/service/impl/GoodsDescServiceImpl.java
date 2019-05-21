package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.sellergoods.service.GoodsDescService;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class GoodsDescServiceImpl implements GoodsDescService {

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoodsDesc> findAll() {
		return goodsDescMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbGoodsDesc> result = new PageResult<TbGoodsDesc>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbGoodsDesc> list = goodsDescMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbGoodsDesc> info = new PageInfo<TbGoodsDesc>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbGoodsDesc goodsDesc) {
		goodsDescMapper.insertSelective(goodsDesc);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoodsDesc goodsDesc){
		goodsDescMapper.updateByPrimaryKeySelective(goodsDesc);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoodsDesc findOne(Long id){
		return goodsDescMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbGoodsDesc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        goodsDescMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbGoodsDesc goodsDesc, int pageNum, int pageSize) {
		PageResult<TbGoodsDesc> result = new PageResult<TbGoodsDesc>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbGoodsDesc.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(goodsDesc!=null){			
						//如果字段不为空
			if (goodsDesc.getIntroduction()!=null && goodsDesc.getIntroduction().length()>0) {
				criteria.andLike("introduction", "%" + goodsDesc.getIntroduction() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getSpecificationItems()!=null && goodsDesc.getSpecificationItems().length()>0) {
				criteria.andLike("specificationItems", "%" + goodsDesc.getSpecificationItems() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getCustomAttributeItems()!=null && goodsDesc.getCustomAttributeItems().length()>0) {
				criteria.andLike("customAttributeItems", "%" + goodsDesc.getCustomAttributeItems() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getItemImages()!=null && goodsDesc.getItemImages().length()>0) {
				criteria.andLike("itemImages", "%" + goodsDesc.getItemImages() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getPackageList()!=null && goodsDesc.getPackageList().length()>0) {
				criteria.andLike("packageList", "%" + goodsDesc.getPackageList() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getSaleService()!=null && goodsDesc.getSaleService().length()>0) {
				criteria.andLike("saleService", "%" + goodsDesc.getSaleService() + "%");
			}
	
		}

        //查询数据
        List<TbGoodsDesc> list = goodsDescMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbGoodsDesc> info = new PageInfo<TbGoodsDesc>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}
	
}
