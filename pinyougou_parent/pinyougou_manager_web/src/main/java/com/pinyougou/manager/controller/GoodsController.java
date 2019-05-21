package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.PageResult;
import entity.Result;
import entity.SolrItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;
/**
 * 请求处理器
 * @author Steven
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	@Autowired
	private JmsTemplate jmsTemplate;

    @Autowired
    private Destination queueSolrDestination;  //索引库更新队列
    @Autowired
    private Destination queueSolrDeleteDestination;  //索引库删除队列
    @Autowired
    private Destination topicPageDestination;  //索引库删除队列
    @Autowired
    private Destination topicPageDeleteDestination;  //索引库删除队列

	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			//删除索引库
			//itemSearchService.deleteByGoodsIds(ids);
            /*jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(ids);
                }
            });*/

            //删除商品详情页
			/*jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});*/

			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}

	@RequestMapping("updateStatus")
	public Result updateStatus(Long[] ids, String status){
		try {
			//先审核
			goodsService.updateStatus(ids, status);

			//只有审核通过是商品，才能导入索引库
			/*if("1".equals(status)){
				//先查询所有商品sku列表
				List<TbItem> itemList = goodsService.findItemListByGoodsIdsAndStatus(ids, status);
				//组装导入对象,这一步相当关键
				*//*List<SolrItem> solrItemList = new ArrayList<>();
				for (TbItem item : itemList) {
					SolrItem solrItem = new SolrItem();
					//复制所有属性
					BeanUtils.copyProperties(item, solrItem);
					//设置规格信息
					Map specMap = JSON.parseObject(item.getSpec(), Map.class);
					solrItem.setSpecMap(specMap);

					solrItemList.add(solrItem);
				}*//*
				// /导入索引库
				//itemSearchService.importList(solrItemList);

				//发送消息到MQ
				String jsonItem = JSON.toJSONString(itemList);
				jmsTemplate.send(queueSolrDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(jsonItem);
					}
				});

				//生成商品静态页
				*//*for (Long id : ids) {
					itemPageService.genItemHtml(id);
				}*//*
				//发消息生成商品详情页
				jmsTemplate.send(topicPageDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(ids);
					}
				});
			}*/

			return new Result(true, "审核操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(false, "审核操作失败！");
	}

	/*@RequestMapping("genHtml")
	public boolean genHtml(Long goodsId){
		return itemPageService.genItemHtml(goodsId);
	}*/
}
