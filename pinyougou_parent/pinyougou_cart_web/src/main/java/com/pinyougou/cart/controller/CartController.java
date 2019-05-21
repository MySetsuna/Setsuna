package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojogroup.Cart;
import com.pinyougou.utils.CookieUtil;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.cart.controller
 * @date 2019-5-5
 */
@RestController
@RequestMapping("cart")
public class CartController {
    @Reference
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    //查询当前购物车列表
    /*合并购物车前的逻辑
    @RequestMapping("findCartList")
    public List<Cart> findCartList(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //如果原来没有购物车，准备一个空集合
        List<Cart> cartList =  new ArrayList<>();

        //未登录，查询cookie
        if("anonymousUser".equals(userName)) {
            String cartListStr = CookieUtil.getCookieValue(request, "cartList", true);
            if (cartListStr != null && cartListStr.length() > 1) {
                cartList = JSON.parseArray(cartListStr, Cart.class);
            }
            System.out.println("从cookie中获取了购物车数据...");
        }else{
            //查询redis数据
            cartList = cartService.findCartListFromRedis(userName);
            System.out.println("从Redis中获取了购物车数据...");
        }
        return cartList;
    }*/

    //合并后的购物车查询逻辑
    @RequestMapping("findCartList")
    public List<Cart> findCartList(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //如果原来没有购物车，准备一个空集合
        List<Cart> cartList =  new ArrayList<>();
        //不管有没有登录，先查询cookie中的购物车
        String cartListStr = CookieUtil.getCookieValue(request, "cartList", true);
        if (cartListStr != null && cartListStr.length() > 1) {
            cartList = JSON.parseArray(cartListStr, Cart.class);
        }
        //未登录，查询cookie
        if("anonymousUser".equals(userName)) {
            System.out.println("从cookie中获取了购物车数据...");
        }else{
            //查询redis数据
            List<Cart> redisList = cartService.findCartListFromRedis(userName);
            System.out.println("从Redis中获取了购物车数据...");
            //合并购物车
            //如果cookies中有购物车数据
            if(cartList.size() > 0){
                cartList = cartService.mergeCartList(cartList, redisList);
                //删除cookie购物车
                CookieUtil.deleteCookie(request,response,"cartList");
                //保存最新的购物车信息到redis中
                cartService.saveCartListToRedis(userName, cartList);
                System.out.println("合并了购物车的数据...");
            }else{
                cartList = redisList;
            }
        }
        return cartList;
    }

    //添加购物车
    @RequestMapping("addGoodsToCartList")
    //springMVC对跨域支持的注解，其中allowCredentials可以省略，默认值就是true
    @CrossOrigin(origins = "http://localhost:8085")
    public Result addGoodsToCartList(Long itemId,Integer num){
        //Access-Control-Allow-Origin的value，可以是具体允许的某个站点，也可以允许所有站点*
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:8085");
        // 并且Access-Control-Allow-Origin不能设置为*，因为cookies操作需要域名
        //response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //先查询原来的购物车列表
            List<Cart> cartList = this.findCartList();
            //添加购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            //未登录
            if("anonymousUser".equals(userName)) {
                //把添加的购物车保存到Cookie中
                String cartListStr = JSON.toJSONString(cartList);
                //注意这里的有效时间，根据实现业务场景决定，我们这里暂时设置1天
                CookieUtil.setCookie(request, response, "cartList", cartListStr, 60 * 60 * 24, true);
                System.out.println("操作了cookie中的购物车数据...");
            }else{
                //把数据保存到Redis中
//                cartService.addGoodsToMyFavorite(userName, itemId);
                cartService.saveCartListToRedis(userName,cartList);
                System.out.println("操作了Redis中的购物车数据...");
            }
            return new Result(true, "购物操作成功！");
        } catch (RuntimeException e) {
            //返回自定义消息
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "购物添加失败！");
    }


    /**
     * 返回全部列表
     * @return
     */
    @RequestMapping("/addGoodsToMyFavorite")
    public Result addGoodsToMyFavorite(Long itemId){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            cartService.addGoodsToMyFavorite(name,itemId);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "添加失敗");
    }
}

