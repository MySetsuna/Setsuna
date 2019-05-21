package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.utils.HttpClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.pay.service.impl
 * @date 2019-5-8
 */
@Service(timeout = 5000)
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;
    @Value("${notifyurl}")
    private String notifyurl;

    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        try {
            //1、准备接口入参，Map包装
            Map map = new HashMap();
            map.put("appid", appid);  //公众号id
            map.put("mch_id", partner);  //公众号id
            map.put("nonce_str", WXPayUtil.generateNonceStr());  //随机字符串
            map.put("body", "品优购");  //商品描述，用户在扫码时能看到的信息
            map.put("out_trade_no", out_trade_no);  //商户支付订单
            map.put("total_fee", total_fee);  //支付金额(分)
            map.put("spbill_create_ip", "127.0.0.1");  //正常来说是用户支付时的ip(request可取)，这里简单点
            map.put("notify_url", notifyurl);  //回调地址
            map.put("trade_type", "NATIVE");  //支付类型-我们用的是扫码支付
            //签名不用单独设置，WXPayUtil中有现成方法可以生成....
            //generateSignedXml(要转换的map对象,支付密匙)
            String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
            System.out.println("准备发起微信统一下单接口，入参为：" + xmlParam);
            //2、调起微信统一下单接口-HttpClient工具
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);  //设置使用安全协议
            httpClient.setXmlParam(xmlParam);  //设置请求提交的参数
            httpClient.post();  //发起post请求
            String content = httpClient.getContent();//获取请求响应的结果
            System.out.println("成功发起微信统一下单接口，响应的结果为：" + content);
            //3、接收微信响应的参数并解析返回
            Map<String, String> responseMap = WXPayUtil.xmlToMap(content);
            //包装返回参数
            Map<String, String> result = new HashMap<>();
            result.put("out_trade_no", out_trade_no); //订单号
            result.put("total_fee", total_fee); //支付金额
            result.put("code_url", responseMap.get("code_url")); //二维码连接
            //返回结果
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //请求失败返回null
        return null;
    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        try {
            //1、准备接口入参，Map包装
            Map map = new HashMap();
            map.put("appid", appid);  //公众号id
            map.put("mch_id", partner);  //公众号id
            map.put("out_trade_no", out_trade_no);  //商户支付订单
            map.put("nonce_str", WXPayUtil.generateNonceStr());  //随机字符串
            //签名不用单独设置，WXPayUtil中有现成方法可以生成....
            //generateSignedXml(要转换的map对象,支付密匙)
            String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
            System.out.println("准备发起微信查询订单接口，入参为：" + xmlParam);
            //2、调起微信统一下单接口-HttpClient工具
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);  //设置使用安全协议
            httpClient.setXmlParam(xmlParam);  //设置请求提交的参数
            httpClient.post();  //发起post请求
            String content = httpClient.getContent();//获取请求响应的结果
            System.out.println("成功发起微信查询订单接口，响应的结果为：" + content);
            //3、接收微信响应的参数并解析返回
            Map<String, String> responseMap = WXPayUtil.xmlToMap(content);
           //直接返回结果
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //请求失败返回null
        return null;
    }

    @Override
    public Map closePay(String out_trade_no) {
        try {
            //1、准备接口入参，Map包装
            Map map = new HashMap();
            map.put("appid", appid);  //公众号id
            map.put("mch_id", partner);  //公众号id
            map.put("out_trade_no", out_trade_no);  //商户支付订单
            map.put("nonce_str", WXPayUtil.generateNonceStr());  //随机字符串
            //签名不用单独设置，WXPayUtil中有现成方法可以生成....
            //generateSignedXml(要转换的map对象,支付密匙)
            String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
            System.out.println("准备发起微信关闭订单接口，入参为：" + xmlParam);
            //2、调起微信统一下单接口-HttpClient工具
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
            httpClient.setHttps(true);  //设置使用安全协议
            httpClient.setXmlParam(xmlParam);  //设置请求提交的参数
            httpClient.post();  //发起post请求
            String content = httpClient.getContent();//获取请求响应的结果
            System.out.println("成功发起微信关闭订单接口，响应的结果为：" + content);
            //3、接收微信响应的参数并解析返回
            Map<String, String> responseMap = WXPayUtil.xmlToMap(content);
            //直接返回结果
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //请求失败返回null
        return null;
    }
}
