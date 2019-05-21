package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbFavoriteMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbFavorite;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车业务逻辑实现
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.cart.service.impl
 * @date 2019-5-5
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbFavoriteMapper favoriteMapper;

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //1.根据商品SKU ID查询SKU商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if(item == null){
            throw new RuntimeException("商品信息不存在,或者商品已下架！");
        }
        //2.获取商家ID
        String sellerId = item.getSellerId();
        //3.根据商家ID判断购物车列表中是否存在该商家的购物车
        Cart cart = searchCartBySellerId(cartList,sellerId);
        //4.如果购物车列表中不存在该商家的购物车
        if(cart == null){
            //4.1 新建购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);  //商家id
            cart.setSellerName(item.getSeller());  //商家名字
            //构建一个商品列表
            List<TbOrderItem> orderItemList = new ArrayList<>();
            //构建商品信息
            TbOrderItem orderItem = createOrderItem(item,num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //4.2 将新建的购物车对象添加到购物车列表
            cartList.add(cart);
        }else{//5.如果购物车列表中存在该商家的购物车
            // 查询购物车明细列表中是否存在该商品
            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(),itemId);
            //5.1. 如果没有，新增购物车明细
            if(orderItem == null){
                //构建新的商品信息
                orderItem = createOrderItem(item,num);
                //向商家商品列表中追加一个商品
                cart.getOrderItemList().add(orderItem);
            }else{
                //5.2. 如果有，在原购物车明细上添加数量，更改金额
                orderItem.setNum(orderItem.getNum() + num);
                //计算小计
                double totalFee = item.getPrice().doubleValue() * orderItem.getNum();
                orderItem.setTotalFee(new BigDecimal(totalFee));

                //特殊情况
                //修改了商品数量后，如果当前要购买的数量不足1
                if(orderItem.getNum() < 1){
                    //删除当前商品
                    cart.getOrderItemList().remove(orderItem);
                }
                //如果删除商品后，我们已经没有要添加的商品了
                if(cart.getOrderItemList().size() < 1){
                    //删除当前整个购物车节点
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> findCartListFromRedis(String username) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if(cartList == null){
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        redisTemplate.boundHashOps("cartList").put(username, cartList);
    }

    @Override
    public List<Cart> mergeCartList(List<Cart> cookieList, List<Cart> redisList) {
        for (Cart cart : cookieList) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                //合并购物车
                this.addGoodsToCartList(redisList, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return redisList;
    }

    /**将购物车添加到我的收藏
     * @param userName
     * @param itemId
     */
    @Override
    public void addGoodsToMyFavorite(String userName, Long itemId) {
        TbFavorite tbFavorite = new TbFavorite();
        tbFavorite.setItemId(itemId);
        tbFavorite.setUserId(userName);
        favoriteMapper.insertSelective(tbFavorite);
    }

    /**
     * 跟据商品skuId查询当前商家中有没有相应商品信息
     * @param orderItemList 当前商家的商品列表
     * @param itemId 要查询的商品Id
     * @return 查找的结果,null-表示找不到
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            //如果找到了相应商品信息
            if(orderItem.getItemId().longValue() == itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }

    /**
     * 构建购物商商品信息
     * @param item 当前要购买的sku信息
     * @param num 购买的数量
     * @return 购物车商品对象
     */
    private TbOrderItem createOrderItem(TbItem item, Integer num) {
        if(num < 1){
            throw new RuntimeException("请输入正确的购买数量！");
        }
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setItemId(item.getId());  //skuId
        orderItem.setGoodsId(item.getGoodsId());  //spuId
        orderItem.setNum(num);  //购买数量
        orderItem.setPicPath(item.getImage()); //商品图片
        orderItem.setPrice(item.getPrice());  //单价
        orderItem.setSellerId(item.getSellerId());  //商家id
        orderItem.setTitle(item.getTitle());  //商品标题
        //计算小计
        double totalFee = item.getPrice().doubleValue() * num;
        orderItem.setTotalFee(new BigDecimal(totalFee));
        return orderItem;
    }

    /**
     * 跟据商家id查询当前用户的购物车信息
     * @param cartList  原来的购物车列表
     * @param sellerId 商家id
     * @return 当前商家购物车对象
     */
    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            //如果找到了该商家的购物车信息
            if(sellerId.equals(cart.getSellerId())){
                //返回购物车对象
                return cart;
            }
        }
        //返回null，标识没有找到
        return null;
    }
}
