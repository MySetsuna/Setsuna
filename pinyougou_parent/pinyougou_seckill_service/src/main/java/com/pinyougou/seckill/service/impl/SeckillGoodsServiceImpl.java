package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillGoods> findAll() {
		return seckillGoodsMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbSeckillGoods> result = new PageResult<TbSeckillGoods>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbSeckillGoods> list = seckillGoodsMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSeckillGoods> info = new PageInfo<TbSeckillGoods>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillGoods seckillGoods) {
		seckillGoodsMapper.insertSelective(seckillGoods);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillGoods seckillGoods){
		seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillGoods findOne(Long id){
		return seckillGoodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbSeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        seckillGoodsMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbSeckillGoods seckillGoods, int pageNum, int pageSize) {
		PageResult<TbSeckillGoods> result = new PageResult<TbSeckillGoods>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbSeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(seckillGoods!=null){			
						//如果字段不为空
			if (seckillGoods.getTitle()!=null && seckillGoods.getTitle().length()>0) {
				criteria.andLike("title", "%" + seckillGoods.getTitle() + "%");
			}
			//如果字段不为空
			if (seckillGoods.getSmallPic()!=null && seckillGoods.getSmallPic().length()>0) {
				criteria.andLike("smallPic", "%" + seckillGoods.getSmallPic() + "%");
			}
			//如果字段不为空
			if (seckillGoods.getSellerId()!=null && seckillGoods.getSellerId().length()>0) {
				criteria.andLike("sellerId", "%" + seckillGoods.getSellerId() + "%");
			}
			//如果字段不为空
			if (seckillGoods.getStatus()!=null && seckillGoods.getStatus().length()>0) {
				criteria.andLike("status", "%" + seckillGoods.getStatus() + "%");
			}
			//如果字段不为空
			if (seckillGoods.getIntroduction()!=null && seckillGoods.getIntroduction().length()>0) {
				criteria.andLike("introduction", "%" + seckillGoods.getIntroduction() + "%");
			}
	
		}

        //查询数据
        List<TbSeckillGoods> list = seckillGoodsMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSeckillGoods> info = new PageInfo<TbSeckillGoods>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}

	@Autowired
	private RedisTemplate redisTemplate;

    @Override
    public List<TbSeckillGoods> findList() {
    	//先查询缓存数据
		List<TbSeckillGoods> seckillGoodses = redisTemplate.boundHashOps("seckillGoodses").values();
		if(seckillGoodses == null || seckillGoodses.size() < 1) {
			//查询库存大于0、时间有效期内、状态审核通过的
			Example example = new Example(TbSeckillGoods.class);
			Example.Criteria criteria = example.createCriteria();
			//库存大于0
			criteria.andGreaterThan("stockCount", 0);
			//状态审核通过
			criteria.andEqualTo("status", "1");
			// startTime <= 当前时间 <= endTime
			Date now = new Date();
			criteria.andLessThanOrEqualTo("startTime", now);
			criteria.andGreaterThanOrEqualTo("endTime", now);
			//查询数据
			seckillGoodses = seckillGoodsMapper.selectByExample(example);

			//把商品数据放入缓存
			for (TbSeckillGoods goods : seckillGoodses) {
				redisTemplate.boundHashOps("seckillGoodses").put(goods.getId(),goods);
			}
		}else {
			System.out.println("从缓存中获取了商品列表...");
		}
		return seckillGoodses;
    }

	@Override
	public TbSeckillGoods findOneFromRedis(Long id) {
		TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoodses").get(id);
		return seckillGoods;
	}

	/**
	 * 状态审核
	 * @param ids
	 * @param status
	 */
	@Override
	public void updateStatus(Long[] ids, String status) {
		//修改的结果
		TbSeckillGoods record = new TbSeckillGoods();
		record.setStatus(status);
		record.setCheckTime(new Date());
		//构建修改范围
		Example example = new Example(TbSeckillGoods.class);
		Example.Criteria criteria = example.createCriteria();
		List longs = Arrays.asList(ids);
		criteria.andIn("id", longs);
		//开始更新
		seckillGoodsMapper.updateByExampleSelective(record,example);
	}

}
