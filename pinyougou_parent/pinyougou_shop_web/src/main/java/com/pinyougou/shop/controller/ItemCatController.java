package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.PageResult;
import entity.Result;
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
@RequestMapping("/itemCat")
public class ItemCatController {

	@Reference
	private ItemCatService itemCatService;
	
	/**
	 * 返回该商家全部列表——替换了原返回全部列表的方法，反正这个模块也调不到
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbItemCat> findAll(String loginName){
		//前端初始化传值传不过来，在这直接取值覆盖掉
		LoginController loginController = new LoginController();
		loginName =(String) loginController.name().get("loginName");

		return itemCatService.sellerFindAll(loginName);
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return itemCatService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param itemCat
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbItemCat itemCat){
		try {
			itemCatService.add(itemCat);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param itemCat
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbItemCat itemCat){
		try {
			//一旦修改，审核状态变更回未审核
			itemCat.setStatus(0);
			itemCatService.update(itemCat);
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
	public TbItemCat findOne(Long id){
		return itemCatService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			itemCatService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/**
	 * 查询+分页
	 * @param itemCat
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbItemCat itemCat, int page, int rows  ){
		return itemCatService.findPage(itemCat, page, rows);		
	}

	@RequestMapping("findByParentId")
	public List<TbItemCat> findByParentId(Long parentId){
		return itemCatService.findByParentId(parentId);
	}
	/** zeke
	 *提取登录用户名的方法
	 */
	public String getSellerName(){
		LoginController loginController = new LoginController();
		return (String) loginController.name().get("loginName");
	}
	/**zeke
	 * 商家品牌查询：查询+分页（区别在于商家查询只返回自己所申请过的品牌）
	 */
	@RequestMapping("/sellerFindByParentId")
	public List<TbItemCat>sellerFindByParentId(Long parentId){
		//添加加商家及状态信息
		String sellerId = getSellerName();
		return itemCatService.sellerFindByParentId(parentId,sellerId);
	}
	/**
	 * 商家品牌增加:（区别在于商家添加需增加商家及状态信息）
	 */
	@RequestMapping("/sellerAdd")
	public Result sellerAdd(@RequestBody TbItemCat itemCat){
		try {
			//添加加商家及状态信息
			String sellerId = getSellerName();
			itemCat.setSellerId(sellerId);
			itemCat.setStatus(0);
			itemCatService.add(itemCat);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

}
