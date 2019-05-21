package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import entity.SolrItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.search.service.impl
 * @date 2019-4-26
 */
@Component
public class ItemSearchListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        try {
            //接收消息
            TextMessage msg = (TextMessage) message;
            String jsonItems = msg.getText();
            //导入索引库
            List<TbItem> itemList = JSON.parseArray(jsonItems, TbItem.class);

            //组装数据
            List<SolrItem> solrItemList = new ArrayList<>();
            for (TbItem item : itemList) {
                SolrItem solrItem = new SolrItem();
                //复制所有属性
                BeanUtils.copyProperties(item, solrItem);
                //设置规格信息
                Map specMap = JSON.parseObject(item.getSpec(), Map.class);
                solrItem.setSpecMap(specMap);

                solrItemList.add(solrItem);
            }
            itemSearchService.importList(solrItemList);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
