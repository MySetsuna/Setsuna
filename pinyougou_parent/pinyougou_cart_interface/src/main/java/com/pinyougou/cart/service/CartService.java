package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;

/**
 * 购物车业务逻辑接口
 * @author Steven
 */
public interface CartService {
    /**
    * 添加商品到购物车
    * @param cartList 原来购物车列表
    * @param itemId 购买商品skuId
    * @param num 购买数量
    * @return 添加后购物车列表
    */
   public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num );

    /**
     * 从redis中查询购物车
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车保存到redis
     */
    public void saveCartListToRedis(String username,List<Cart> cartList);

    /**
     * 合并购物车
     * @param cookieList
     * @param redisList
     * @return 合并后的购物车
     */
    public List<Cart> mergeCartList(List<Cart> cookieList,List<Cart> redisList);


    /**添加到我的收藏
     * @param userName
     * @param itemId
     */
    public void addGoodsToMyFavorite(String userName, Long itemId);
}
