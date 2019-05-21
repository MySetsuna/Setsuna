package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.cart.controller
 * @date 2019-5-8
 */
@RestController
@RequestMapping("pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;
    @Reference
    private SeckillOrderService seckillOrderService;

    //生成二维码
    @RequestMapping("createNative")
    public Map createNative(){
        /*IdWorker worker = new IdWorker();
        String out_trade_no = worker.nextId() + "";
        return weixinPayService.createNative(out_trade_no,"1");*/

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
        TbSeckillOrder seckillOrder = seckillOrderService.searchOrderFromRedisByUserId(userId);
        String totalFee = ((long)(seckillOrder.getMoney().doubleValue() * 100)) + "";
        return weixinPayService.createNative(seckillOrder.getId() + "", totalFee);
    }

    @RequestMapping("queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //一直查询，直到订单被支付成功，或者订单关闭为止
        Result result = null;
        int x = 1;
        while (true){

            //查询订单逻辑
            Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);
            if(map == null){
                result = new Result(false, "支付失败！");
                break;
            }
            //如果订单被支付成功了
            if("SUCCESS".equals(map.get("trade_state"))){
                result = new Result(true, "支付成功！");

                //更新订单状态
                //orderService.updateOrderStatus(out_trade_no,map.get("transaction_id"));
                seckillOrderService.saveOrderFromRedisToDb(userId,new Long(out_trade_no),map.get("transaction_id"));
                break;
            }

            //如果支付时间超过了5分钟
            if(x >= 3){
                result = new Result(false, "支付已超时");

                //还原库存
                //seckillOrderService.deleteOrderFromRedis(userId,new Long(out_trade_no));

                //1.调用微信的关闭订单接口（学员实现）
                Map<String,String> payresult = weixinPayService.closePay(out_trade_no);
                if( !"SUCCESS".equals(payresult.get("result_code")) ){//如果返回结果是正常关闭
                    //如果订单已支付，我们要正常发货
                    if("ORDERPAID".equals(payresult.get("err_code"))){
                        result=new Result(true, "支付成功");
                        seckillOrderService.saveOrderFromRedisToDb(userId, Long.valueOf(out_trade_no), map.get("transaction_id"));
                    }
                }
                //还原库存
                if(result.isSuccess()==false){
                    //2.调用删除
                    seckillOrderService.deleteOrderFromRedis(userId, Long.valueOf(out_trade_no));
                }


                break;
            }
            //不要太频繁发起请求,3秒发起一次
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x++;
        }
        return result;
    }
}
