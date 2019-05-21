package com.pinyougou.user.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Order;
import com.pinyougou.pojogroup.OrderItem;
import com.pinyougou.user.service.UserService;
import entity.PageResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.util.*;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbItemMapper itemMapper;
    @Autowired
    private TbGoodsMapper goodsMapper;
	@Autowired
	private TbFavoriteMapper favoriteMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbUser> findAll() {
		return userMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbUser> result = new PageResult<TbUser>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbUser> list = userMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbUser> info = new PageInfo<TbUser>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbUser user) {
		//密码加密
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		//时间
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		userMapper.insertSelective(user);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbUser user){

		userMapper.updateByPrimaryKeySelective(user);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbUser findOne(Long id){
		return userMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        userMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbUser user, int pageNum, int pageSize) {
		PageResult<TbUser> result = new PageResult<TbUser>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbUser.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(user!=null){			
						//如果字段不为空
			if (user.getUsername()!=null && user.getUsername().length()>0) {
				criteria.andLike("username", "%" + user.getUsername() + "%");
			}
			//如果字段不为空
			if (user.getPassword()!=null && user.getPassword().length()>0) {
				criteria.andLike("password", "%" + user.getPassword() + "%");
			}
			//如果字段不为空
			if (user.getPhone()!=null && user.getPhone().length()>0) {
				criteria.andLike("phone", "%" + user.getPhone() + "%");
			}
			//如果字段不为空
			if (user.getEmail()!=null && user.getEmail().length()>0) {
				criteria.andLike("email", "%" + user.getEmail() + "%");
			}
			//如果字段不为空
			if (user.getSourceType()!=null && user.getSourceType().length()>0) {
				criteria.andLike("sourceType", "%" + user.getSourceType() + "%");
			}
			//如果字段不为空
			if (user.getNickName()!=null && user.getNickName().length()>0) {
				criteria.andLike("nickName", "%" + user.getNickName() + "%");
			}
			//如果字段不为空
			if (user.getName()!=null && user.getName().length()>0) {
				criteria.andLike("name", "%" + user.getName() + "%");
			}
			//如果字段不为空
			if (user.getStatus()!=null && user.getStatus().length()>0) {
				criteria.andLike("status", "%" + user.getStatus() + "%");
			}
			//如果字段不为空
			if (user.getHeadPic()!=null && user.getHeadPic().length()>0) {
				criteria.andLike("headPic", "%" + user.getHeadPic() + "%");
			}
			//如果字段不为空
			if (user.getQq()!=null && user.getQq().length()>0) {
				criteria.andLike("qq", "%" + user.getQq() + "%");
			}
			//如果字段不为空
			if (user.getIsMobileCheck()!=null && user.getIsMobileCheck().length()>0) {
				criteria.andLike("isMobileCheck", "%" + user.getIsMobileCheck() + "%");
			}
			//如果字段不为空
			if (user.getIsEmailCheck()!=null && user.getIsEmailCheck().length()>0) {
				criteria.andLike("isEmailCheck", "%" + user.getIsEmailCheck() + "%");
			}
			//如果字段不为空
			if (user.getSex()!=null && user.getSex().length()>0) {
				criteria.andLike("sex", "%" + user.getSex() + "%");
			}
	
		}

        //查询数据
        List<TbUser> list = userMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbUser> info = new PageInfo<TbUser>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}

	/*public static void main(String[] args) {
		System.out.println((long)(Math.random() * 1000000));
	}*/

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Destination queueSmsDestination;


	@Override
	public void createSmsCode(String phone) {
		//1、生成验证码
		String code = (long)(Math.random() * 1000000) + "";
		System.out.println("验证码："+code);
		//2、保存验证码到Redis
		redisTemplate.boundHashOps("smsCodes").put(phone, code);
		//3、发送手机验证码
		jmsTemplate.send(queueSmsDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("mobile", phone);//手机号
				mapMessage.setString("template_code", "SMS_135802012");//模板编号
				mapMessage.setString("sign_name", "黑马");//签名
				Map m=new HashMap<>();
				m.put("number", code);
				mapMessage.setString("param", JSON.toJSONString(m));//参数
				return mapMessage;
			}
		});
	}

	@Override
	public boolean checkSmsCode(String phone, String code) {
		String smsCodes = (String) redisTemplate.boundHashOps("smsCodes").get(phone);
		return code.equals(smsCodes);
	}

	@Override
	public List<Order> findOrderByUserId(String userId,String status) {
	     List<Order> orderList = new ArrayList<>();
		TbOrder where = new TbOrder();
		where.setUserId(userId);
		if (status!=null&&!status.equals("")) {
			where.setStatus(status);
		}
		List<TbOrder> orders = orderMapper.select(where);
		for (TbOrder tbOrder : orders) {
			Order order = new Order();
			//设置order的Tborder
			order.setTbOrder(tbOrder);

			TbOrderItem tbOrderItem = new TbOrderItem();
			tbOrderItem.setOrderId(tbOrder.getOrderId());
			List<TbOrderItem> tbOrderItems = orderItemMapper.select(tbOrderItem);

			//new一个OrderItem的集合
			List<OrderItem> orderItemList = new ArrayList<>();
			for (TbOrderItem tbOrderItem1 : tbOrderItems) {
				//new一个orderItem
				OrderItem orderItem = new OrderItem();

				//设置orderItem的OrderItem
				orderItem.setOrderItem(tbOrderItem1);
				//设置orderItem的item
				TbItem item = itemMapper.selectByPrimaryKey(tbOrderItem1.getItemId());
				orderItem.setItem(item);
				//设置order的OrderItemList
				orderItemList.add(orderItem);
			}
			//设置order的OrderItemList
			order.setOrderItemList(orderItemList);

			orderList.add(order);
		}
		return orderList;
	}

	@Override
	public TbUser findUserByUserId(String userName) {
		TbUser where = new TbUser();
		where.setUsername(userName);
		TbUser user = userMapper.selectOne(where);
		return user;
	}

	@Override
	public List<TbItem> findUserFavoriteByUserId(String userName) {
		TbFavorite where = new TbFavorite();
		where.setUserId(userName);
		List<TbFavorite> favoriteList = favoriteMapper.select(where);
		List<TbItem> itemList = new ArrayList<>();
		for (TbFavorite tbFavorite : favoriteList) {
			TbItem tbItem = itemMapper.selectByPrimaryKey(tbFavorite.getItemId());
			itemList.add(tbItem);
		}
		return itemList;
	}

	@Override
	public void updateOrderStatus(Long orderId,String status) {
		TbOrder where = new TbOrder();
		where.setOrderId(orderId);
		where.setStatus(status);
		orderMapper.updateByPrimaryKeySelective(where);
	}

    @Override
    public List<TbGoods> findPersonFootmark(String userId) {
		Set<Long> goodsIds = (Set<Long>) redisTemplate.boundHashOps("footMarkList").get(userId);
        List<Object> goodIdList = new ArrayList<>(goodsIds);
        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",goodIdList);
        List<TbGoods> tbGoodsList = goodsMapper.selectByExample(example);
        return tbGoodsList;
    }

}
