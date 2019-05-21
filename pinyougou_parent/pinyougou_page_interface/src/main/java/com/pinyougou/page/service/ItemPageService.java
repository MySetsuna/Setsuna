package com.pinyougou.page.service;

/**
 * 商品详细页接口
 * @author Steven
 *
 */
public interface ItemPageService {
    /**
    * 生成商品详细页
    * @param goodsId spuID
    */
   public boolean genItemHtml(Long goodsId);
}
