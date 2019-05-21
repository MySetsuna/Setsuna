package com.pinyougou.order.service;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojogroup.Order;
import entity.PageResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface OrderService {

	/**
	 * 返回全部已付款订单
	 * @return
	 */
	public List<TbOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);


	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrder findOne(Long id);
	
	
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
	public PageResult findPage(Order order, int pageNum, int pageSize);

	/**
	 * 根据用户查询payLog
	 * @param userId
	 * @return
	 */
	public TbPayLog searchPayLogFromRedis(String userId);

	/**
	 * 修改订单状态
	 * @param out_trade_no 支付订单号
	 * @param transaction_id 微信返回的交易流水号
	 */
	public void updateOrderStatus(String out_trade_no,String transaction_id);

	/**查询订单和订单里的商品详情
	 * @return
	 * @param border
	 */
	public List<TbOrder> findOrderAndOrderItem(Order border);


	/**
	 * 商家后台订单查询
	 *
	 */
	public  List<TbOrder>  findOrdersBySellId (String selleId);


	/**
	 * 查询数据id
	 */
	public  TbOrder  findByQueryId (Long orderId);


	/**
	 *修改数据
	 */
	public  void modification (TbOrder TbOrder);

	/**
	 * 订单统计
	 * @param order
	 * @return
	 */
	List<TbItem> findCount(Order order);

	/**
	 * 每天的销售量
	 * @param start
	 * @param end
	 * @return
	 */
    Map searchDaySale(Date start, Date end);

    void updateStatus(String[] ids, String status);

	PageResult findOrderPage(TbOrder tbOrder, int page, int rows);

	/**
	 * 时间段的每个商品的销售额
	 * @param start
	 * @param end
	 * @return
	 */
	List<TbItem> searchDayGoodsSale(Date start, Date end);

    /**
     * 生成饼图
     * @param order
     * @return
     */
	public List<TbItem> createPie(Order order);
}
