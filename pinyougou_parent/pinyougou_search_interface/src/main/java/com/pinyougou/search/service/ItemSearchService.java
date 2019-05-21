package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

/**
 * 商品搜索业务逻辑接口
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.search.service
 * @date 2019-4-20
 */
public interface ItemSearchService {

    /**
     * 商品搜索业务逻辑
     * @param searchMap 查询条件
     * @return 查询结果：商品列表、品牌、规格、分类等等数据
     */
    public Map search(Map searchMap);

    /**
     * 批量导入数据
     * @param list
     */
    public void importList(List list);

    /**
     * 跟据id列表删除索引
     * @param goodsIdList
     */
    public void deleteByGoodsIds(Long[] goodsIdList);


    /**
     * 点击商品将商品加入到我的足迹
     * @param
     */
    public void addGoodsToFootmark(String userId,Long goodsId);

}
