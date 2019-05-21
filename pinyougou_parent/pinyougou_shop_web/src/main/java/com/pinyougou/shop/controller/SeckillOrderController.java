package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.sellergoods.service.SeckillOrderService;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 请求处理器
 * @author Steven
 *
 */
@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

	@Reference
	private SeckillOrderService seckillOrderService;

	/** zeke
	 *提取登录用户名的方法
	 */
	public String getSellerName(){
		LoginController loginController = new LoginController();
		return (String) loginController.name().get("loginName");
	}
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeckillOrder> findAll(){
		String sellerId = getSellerName();
		return seckillOrderService.findAll(sellerId);
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return seckillOrderService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param seckillOrder
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSeckillOrder seckillOrder){
		try {
			seckillOrderService.add(seckillOrder);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seckillOrder
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSeckillOrder seckillOrder){
		try {
			seckillOrderService.update(seckillOrder);
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
	public TbSeckillOrder findOne(Long id){
		return seckillOrderService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			seckillOrderService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/**
	 * 查询+分页
	 * @param seckillOrder
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSeckillOrder seckillOrder, int page, int rows  ){
		return seckillOrderService.findPage(seckillOrder, page, rows);		
	}

	@RequestMapping("submitOrder")
	public Result submitOrder(Long seckillId){
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		if ("anonymousUser".equals(userId)) {
			return new Result(false, "请先登录！");
		}
		try {
			//开始下单
			seckillOrderService.submitOrder(seckillId, userId);

			return new Result(true, "恭喜抢购成功，请5分钟内完成支付！");
		}catch (RuntimeException e) {
			return new Result(false, e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(false, "抢购失败，请稍后再试！");
	}
	
}
