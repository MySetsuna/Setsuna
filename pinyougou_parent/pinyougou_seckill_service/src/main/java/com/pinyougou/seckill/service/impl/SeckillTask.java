package com.pinyougou.seckill.service.impl;

import com.github.abel533.entity.Example;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SeckillTask {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Scheduled(cron = "* * * * * ?")
    public void refreshSeckillGoods() {
        System.out.println("执行了任务调度" + new Date());

        //查询所有的秒杀商品键集合

        List ids = new ArrayList(redisTemplate.boundHashOps("seckillGoodses").keys());

        //查询正在秒杀的商品列表      


        Example example=new Example(TbSeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("status","1");//审核通过

        criteria.andGreaterThan("stockCount",0);//剩余库存大于0

        Date now = new Date();
        criteria.andLessThanOrEqualTo("startTime", now);
        criteria.andGreaterThanOrEqualTo("endTime", now);

        criteria.andNotIn("id",ids);//排除缓存中已经有的商品     

        List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
        if (seckillGoodsList != null && seckillGoodsList.size()>0) {
            //装入缓存

            for (TbSeckillGoods seckill : seckillGoodsList) {

                redisTemplate.boundHashOps("seckillGoodses").put(seckill.getId(), seckill);

            }

            System.out.println("将" + seckillGoodsList.size() + "条商品装入缓存");
        }

    }
}
