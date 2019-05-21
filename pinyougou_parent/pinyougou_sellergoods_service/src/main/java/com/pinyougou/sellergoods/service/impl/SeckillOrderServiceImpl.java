package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.sellergoods.service.SeckillOrderService;
import com.pinyougou.utils.IdWorker;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

	@Autowired
	private TbSeckillOrderMapper seckillOrderMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillOrder> findAll(String sellerId) {
		TbSeckillOrder where = new TbSeckillOrder();
		where.setSellerId(sellerId);
		return seckillOrderMapper.select(where);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbSeckillOrder> result = new PageResult<TbSeckillOrder>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbSeckillOrder> list = seckillOrderMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSeckillOrder> info = new PageInfo<TbSeckillOrder>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillOrder seckillOrder) {
		seckillOrderMapper.insertSelective(seckillOrder);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillOrder seckillOrder){
		seckillOrderMapper.updateByPrimaryKeySelective(seckillOrder);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillOrder findOne(Long id){
		return seckillOrderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbSeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        seckillOrderMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
		PageResult<TbSeckillOrder> result = new PageResult<TbSeckillOrder>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbSeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(seckillOrder!=null){			
						//如果字段不为空
			if (seckillOrder.getUserId()!=null && seckillOrder.getUserId().length()>0) {
				criteria.andLike("userId", "%" + seckillOrder.getUserId() + "%");
			}
			//如果字段不为空
			if (seckillOrder.getSellerId()!=null && seckillOrder.getSellerId().length()>0) {
				criteria.andLike("sellerId", "%" + seckillOrder.getSellerId() + "%");
			}
			//如果字段不为空
			if (seckillOrder.getStatus()!=null && seckillOrder.getStatus().length()>0) {
				criteria.andLike("status", "%" + seckillOrder.getStatus() + "%");
			}
			//如果字段不为空
			if (seckillOrder.getReceiverAddress()!=null && seckillOrder.getReceiverAddress().length()>0) {
				criteria.andLike("receiverAddress", "%" + seckillOrder.getReceiverAddress() + "%");
			}
			//如果字段不为空
			if (seckillOrder.getReceiverMobile()!=null && seckillOrder.getReceiverMobile().length()>0) {
				criteria.andLike("receiverMobile", "%" + seckillOrder.getReceiverMobile() + "%");
			}
			//如果字段不为空
			if (seckillOrder.getReceiver()!=null && seckillOrder.getReceiver().length()>0) {
				criteria.andLike("receiver", "%" + seckillOrder.getReceiver() + "%");
			}
			//如果字段不为空
			if (seckillOrder.getTransactionId()!=null && seckillOrder.getTransactionId().length()>0) {
				criteria.andLike("transactionId", "%" + seckillOrder.getTransactionId() + "%");
			}
	
		}

        //查询数据
        List<TbSeckillOrder> list = seckillOrderMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSeckillOrder> info = new PageInfo<TbSeckillOrder>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private IdWorker worker ;
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;

    @Override
    public synchronized void submitOrder(Long seckillId, String userId) {
		TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoodses").get(seckillId);
		if(seckillGoods == null){
			throw new RuntimeException("商品信息不存在，或者商品已下架");
		}else{
			if(seckillGoods.getStockCount() < 1){
				throw new RuntimeException("抱歉，您的来晚了一步，商品已被抢购一空！");
			}
			//先预占库存
			seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
			redisTemplate.boundHashOps("seckillGoodses").put(seckillId, seckillGoods);

			//启动新线程完后续的操作,目的尽快释放这个方法锁
			new Thread(){
				@Override
				public void run() {
					//如果最后一个商品被抢购了
					if(seckillGoods.getStockCount() == 0){
						//把商品信息同步到数据库中
						seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
						//删除redis中的商品
						//redisTemplate.boundHashOps("seckillGoodses").delete(seckillId);
					}
					//下单-订单保存在redis中
					long orderId = worker.nextId();
					TbSeckillOrder seckillOrder = new TbSeckillOrder();
					seckillOrder.setId(orderId);
					seckillOrder.setCreateTime(new Date());
					seckillOrder.setMoney(seckillGoods.getCostPrice());//秒杀价格
					seckillOrder.setSeckillId(seckillId);
					seckillOrder.setSellerId(seckillGoods.getSellerId());
					seckillOrder.setUserId(userId);//设置用户ID
					seckillOrder.setStatus("0");//状态 -未支付
					//把订单保存在Redis中
					redisTemplate.boundHashOps("seckillOrders").put(userId, seckillOrder);
					super.run();
				}
			}.start();
		}
    }

	@Override
	public TbSeckillOrder searchOrderFromRedisByUserId(String userId) {
		TbSeckillOrder seckillOrder = (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrders").get(userId);
		return seckillOrder;
	}

	@Override
	public void saveOrderFromRedisToDb(String userId, Long orderId, String transactionId) {
		TbSeckillOrder seckillOrder = (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrders").get(userId);
		if(seckillOrder == null){
			throw new RuntimeException("支付的订单不存在！");
		}
		//防止用户多点下单，支付订单不相同的情况
		if(orderId.longValue() != seckillOrder.getId().longValue()){
			throw new RuntimeException("支付的订单存在异常，请进入订单中心从新发起支付！");
		}

		//保存订单到数据库,同时修改订单状态为已支付
		seckillOrder.setStatus("1");  //已支付
		seckillOrder.setPayTime(new Date());  //支付时间
		seckillOrder.setTransactionId(transactionId);  //交易流水号-微信
		seckillOrderMapper.insertSelective(seckillOrder);
		//删除redis中的订单信息
		redisTemplate.boundHashOps("seckillOrders").delete(userId);
	}

	@Override
	public void deleteOrderFromRedis(String userId, Long orderId) {
		//删除用户在Redis中的订单
		TbSeckillOrder seckillOrder = (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrders").get(userId);
		if(seckillOrder.getId().longValue() == orderId.longValue()){
			redisTemplate.boundHashOps("seckillOrders").delete(orderId);
		}
		//还原商品的库存
		TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoodses").get(seckillOrder.getSeckillId());
		//还原库存
		seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);
		//从新把商品放入redis
		redisTemplate.boundHashOps("seckillGoodses").put(seckillGoods.getId(), seckillGoods);
	}
}
