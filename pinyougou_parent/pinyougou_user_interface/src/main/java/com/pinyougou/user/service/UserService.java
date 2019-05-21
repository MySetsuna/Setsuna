package com.pinyougou.user.service;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbUser;

import com.pinyougou.pojogroup.Order;
import entity.PageResult;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface UserService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbUser> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbUser user);
	
	
	/**
	 * 修改
	 */
	public void update(TbUser user);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbUser findOne(Long id);
	
	
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
	public PageResult findPage(TbUser user, int pageNum, int pageSize);

	/**
	 * 生成短信验证码
	 * @return
	 */
	public void createSmsCode(String phone);

	/**
	 * 判断短信验证码是否存在
	 * @param phone
	 * @return
	 */
	public boolean  checkSmsCode(String phone,String code);

	/**
	 * 查询用户订单
	 * @return
	 */
	public List<Order> findOrderByUserId(String userId,String Status);


	/**
	 * 查询用户信息
	 * @return
	 */
	public TbUser findUserByUserId(String userName);

    /**查询用户收藏
     * @param userName
     * @return
     */
    public List<TbItem> findUserFavoriteByUserId(String userName);


    /**
     * 修改订单状态
     */
    public void updateOrderStatus(Long orderId,String status);

	/**
	 * 查询用户id
	 */
	public List<TbGoods> findPersonFootmark(String userId);

}
