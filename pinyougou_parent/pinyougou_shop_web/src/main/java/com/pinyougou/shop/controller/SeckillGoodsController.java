package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.sellergoods.service.SeckillGoodsService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 请求处理器
 * @author Steven
 *
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

	@Reference
	private SeckillGoodsService seckillGoodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeckillGoods> findAll(){			
		return seckillGoodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return seckillGoodsService.findPage(page, rows);
	}

	
	/**
	 * 增加
	 * @param seckillGoods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSeckillGoods seckillGoods){
		try {
			//设置商家名
			LoginController loginController = new LoginController();
			String loginName =(String) loginController.name().get("loginName");
			seckillGoods.setSellerId(loginName);
			//设置创建时间
			seckillGoods.setCreateTime(new Date());
			//设置初始状态
			seckillGoods.setStatus("0");
			//设置商品goodsId的生成需要ID生成器，就放到service层去，因为那边有过依赖不用在controller层也导一遍
			seckillGoodsService.add(seckillGoods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seckillGoods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSeckillGoods seckillGoods){
		try {
			//设置商家名
			LoginController loginController = new LoginController();
			String loginName =(String) loginController.name().get("loginName");
			seckillGoods.setSellerId(loginName);
			seckillGoodsService.update(seckillGoods);
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
	public TbSeckillGoods findOne(Long id){
		return seckillGoodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			seckillGoodsService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/**
	 * 查询+分页
	 * @param seckillGoods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSeckillGoods seckillGoods, int page, int rows  ){
		return seckillGoodsService.findPage(seckillGoods, page, rows);		
	}

	@RequestMapping("findList")
	public List<TbSeckillGoods> findList(){
		return seckillGoodsService.findList();
	}

	@RequestMapping("findOneFromRedis")
	public TbSeckillGoods findOneFromRedis(Long id){
		return seckillGoodsService.findOneFromRedis(id);
	}

	/**
	 * 秒杀商品审核
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("updateStatus")
	public Result updateStatus(Long[] ids, String status){
		try {
			//审核
			seckillGoodsService.updateStatus(ids, status);
			return new Result(true, "审核操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(false, "审核操作失败！");
	}
}
