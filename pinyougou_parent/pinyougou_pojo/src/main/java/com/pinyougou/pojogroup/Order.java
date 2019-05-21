package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Order implements Serializable {
    private TbOrder tbOrder;//订单

    private List<OrderItem> orderItemList;//订单清单

    private HashMap propertyMap;

    private HashMap dateMap;

    public HashMap getDateMap() {
        return dateMap;
    }

    public void setDateMap(HashMap dateMap) {
        this.dateMap = dateMap;
    }

    public HashMap getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(HashMap propertyMap) {
        this.propertyMap = propertyMap;
    }

    public TbOrder getTbOrder() {
        return tbOrder;
    }

    public void setTbOrder(TbOrder tbOrder) {
        this.tbOrder = tbOrder;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
