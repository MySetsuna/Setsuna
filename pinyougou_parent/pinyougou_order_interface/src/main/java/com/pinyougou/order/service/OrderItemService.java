package com.pinyougou.order.service;
import java.util.List;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;

import com.pinyougou.pojo.TbSeckillGoods;
import entity.PageResult;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface OrderItemService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbOrderItem> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrderItem orderItem);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrderItem orderItem);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrderItem findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbOrderItem orderItem, int pageNum, int pageSize);

	/**
	 * 根据orderId查找orderItem
	 * @param orderId
	 * @return
	 */
    List<TbOrderItem> findByOrderId(String orderId);



}
