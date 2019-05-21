package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
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
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private BrandService brandService;
	
	/**
	 * 返回该商家全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbBrand> sellerFindAll(String loginName){
		//前端初始化传值传不过来，在这直接取值覆盖掉
		LoginController loginController = new LoginController();
		loginName =(String) loginController.name().get("loginName");

		return brandService.sellerFindAll(loginName);
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return brandService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param brand
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand){
		try {
			brandService.add(brand);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	
	/**
	 * 修改
	 * @param brand
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand){
		try {
			//一旦修改，审核状态变更回未审核
			brand.setStatus(0);
			brandService.update(brand);
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
	public TbBrand findOne(Long id){
		return brandService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			brandService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/**
	 * 运营管理商：查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand brand, int page, int rows  ){
		return brandService.findPage(brand, page, rows);		
	}
	/**
	 * 商家品牌查询：查询+分页（区别在于商家查询只返回自己所申请过的品牌）
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/sellerSearch")
	public PageResult sellerSearch(@RequestBody TbBrand brand, int page, int rows,String sellerId  ){
		return brandService.sellerFindPage(brand, page, rows,sellerId);
	}
	/**
	 * 商家品牌增加:（区别在于商家添加需增加商家及状态信息）
	 * @param brand
	 * @return
	 */
	@RequestMapping("/sellerAdd")
	public Result sellerAdd(@RequestBody TbBrand brand,String loginName){
		try {
			//添加加商家及状态信息
			brand.setSellerId(loginName);
			brand.setStatus(0);
			brandService.add(brand);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

}
