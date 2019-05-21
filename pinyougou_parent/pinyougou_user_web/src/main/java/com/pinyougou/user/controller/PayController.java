package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbSeckillOrder;

import com.pinyougou.pojogroup.Order;
import com.pinyougou.user.service.UserService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
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
    private UserService userService;



    @RequestMapping("submitOrder")
    public Map<String,Object> submitOrder(@RequestBody TbOrder order){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        HashMap<String, Object> map = new HashMap<>();
        if (userId==null&&userId.equals("")) {
            map.put("success", false);
            return map;
        }
        try {
            //开始下单
            Map<String,Object> weixinMap = weixinPayService.createNative(order.getOrderIdStr(),"1");
            weixinMap.put("success", true);
            return weixinMap;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        map.put("success", false);
        return map;
    }


    //查询订单状态
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

                //更新订单状态out_trade_no订单号
                userService.updateOrderStatus(Long.valueOf(out_trade_no), "2");
//                seckillOrderService.saveOrderFromRedisToDb(userId,new Long(out_trade_no),map.get("transaction_id"));
                break;
            }

            //如果支付时间超过了5分钟
            if(x >= 10){
                result = new Result(false, "支付已超时");

                //还原库存
                //seckillOrderService.deleteOrderFromRedis(userId,new Long(out_trade_no));

                //1.调用微信的关闭订单接口（学员实现）
                Map<String,String> payresult = weixinPayService.closePay(out_trade_no);
                if( !"SUCCESS".equals(payresult.get("result_code")) ){//如果返回结果是正常关闭
                    //如果订单已支付，我们要正常发货
                    if("ORDERPAID".equals(payresult.get("err_code"))){
                        result=new Result(true, "支付成功");
//                        seckillOrderService.saveOrderFromRedisToDb(userId, Long.valueOf(out_trade_no), map.get("transaction_id"));
                        userService.updateOrderStatus(Long.valueOf(out_trade_no), "2");
                    }
                }
                //还原库存
//                if(result.isSuccess()==false){
//                    //2.调用删除
//                    seckillOrderService.deleteOrderFromRedis(userId, Long.valueOf(out_trade_no));
//                }


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
