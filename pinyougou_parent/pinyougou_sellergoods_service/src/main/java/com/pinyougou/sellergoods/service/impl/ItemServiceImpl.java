package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.sellergoods.service.ItemService;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbItem> findAll() {
		return itemMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbItem> result = new PageResult<TbItem>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbItem> list = itemMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbItem> info = new PageInfo<TbItem>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbItem item) {
		itemMapper.insertSelective(item);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbItem item){
		itemMapper.updateByPrimaryKeySelective(item);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbItem findOne(Long id){
		return itemMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        itemMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbItem item, int pageNum, int pageSize) {
		PageResult<TbItem> result = new PageResult<TbItem>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbItem.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(item!=null){			
						//如果字段不为空
			if (item.getTitle()!=null && item.getTitle().length()>0) {
				criteria.andLike("title", "%" + item.getTitle() + "%");
			}
			//如果字段不为空
			if (item.getSellPoint()!=null && item.getSellPoint().length()>0) {
				criteria.andLike("sellPoint", "%" + item.getSellPoint() + "%");
			}
			//如果字段不为空
			if (item.getBarcode()!=null && item.getBarcode().length()>0) {
				criteria.andLike("barcode", "%" + item.getBarcode() + "%");
			}
			//如果字段不为空
			if (item.getImage()!=null && item.getImage().length()>0) {
				criteria.andLike("image", "%" + item.getImage() + "%");
			}
			//如果字段不为空
			if (item.getStatus()!=null && item.getStatus().length()>0) {
				criteria.andLike("status", "%" + item.getStatus() + "%");
			}
			//如果字段不为空
			if (item.getItemSn()!=null && item.getItemSn().length()>0) {
				criteria.andLike("itemSn", "%" + item.getItemSn() + "%");
			}
			//如果字段不为空
			if (item.getIsDefault()!=null && item.getIsDefault().length()>0) {
				criteria.andLike("isDefault", "%" + item.getIsDefault() + "%");
			}
			//如果字段不为空
			if (item.getSellerId()!=null && item.getSellerId().length()>0) {
				criteria.andLike("sellerId", "%" + item.getSellerId() + "%");
			}
			//如果字段不为空
			if (item.getCartThumbnail()!=null && item.getCartThumbnail().length()>0) {
				criteria.andLike("cartThumbnail", "%" + item.getCartThumbnail() + "%");
			}
			//如果字段不为空
			if (item.getCategory()!=null && item.getCategory().length()>0) {
				criteria.andLike("category", "%" + item.getCategory() + "%");
			}
			//如果字段不为空
			if (item.getBrand()!=null && item.getBrand().length()>0) {
				criteria.andLike("brand", "%" + item.getBrand() + "%");
			}
			//如果字段不为空
			if (item.getSpec()!=null && item.getSpec().length()>0) {
				criteria.andLike("spec", "%" + item.getSpec() + "%");
			}
			//如果字段不为空
			if (item.getSeller()!=null && item.getSeller().length()>0) {
				criteria.andLike("seller", "%" + item.getSeller() + "%");
			}
	
		}

        //查询数据
        List<TbItem> list = itemMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbItem> info = new PageInfo<TbItem>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}
	
}
