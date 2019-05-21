package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private TbOrderItem orderItem;
    private TbItem item;

    public TbOrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(TbOrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public TbItem getItem() {
        return item;
    }

    public void setItem(TbItem item) {
        this.item = item;
    }
}
