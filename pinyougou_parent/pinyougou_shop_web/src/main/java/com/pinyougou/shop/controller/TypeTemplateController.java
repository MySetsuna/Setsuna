package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 请求处理器
 * @author Steven
 *
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	@Reference
	private TypeTemplateService typeTemplateService;
	
	/**
	 * 返回该商家全部列表——替换了原返回全部列表的方法，反正这个模块也调不到
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbTypeTemplate> findAll(String loginName){
		//前端初始化传值传不过来，在这直接取值覆盖掉
		LoginController loginController = new LoginController();
		loginName =(String) loginController.name().get("loginName");

		return typeTemplateService.sellerFindAll(loginName);
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return typeTemplateService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbTypeTemplate typeTemplate){
		try {
			typeTemplateService.add(typeTemplate);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbTypeTemplate typeTemplate){
		try {
			//一旦修改，审核状态变更回未审核
			typeTemplate.setStatus(0);
			typeTemplateService.update(typeTemplate);
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
	public TbTypeTemplate findOne(Long id){
		return typeTemplateService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			typeTemplateService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/**
	 * 查询+分页
	 * @param typeTemplate
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbTypeTemplate typeTemplate, int page, int rows  ){
		return typeTemplateService.findPage(typeTemplate, page, rows);		
	}

	@RequestMapping("findSpecList")
	public List<Map> findSpecList(Long id){
		return typeTemplateService.findSpecList(id);
	}

	/**zeke
	 * 商家品牌查询：查询+分页（区别在于商家查询只返回自己所申请过的品牌）
	 */
	@RequestMapping("/sellerSearch")
	public PageResult sellerSearch(@RequestBody TbTypeTemplate typeTemplate, int page, int rows, String sellerId) {
		return typeTemplateService.sellerFindPage(typeTemplate, page, rows, sellerId);
	}
	/**
	 * 商家品牌增加:（区别在于商家添加需增加商家及状态信息）
	 */
	@RequestMapping("/sellerAdd")
	public Result sellerAdd(@RequestBody TbTypeTemplate typeTemplate, String loginName) {
		try {
			//添加加商家及状态信息
			typeTemplate.setSellerId(loginName);
			typeTemplate.setStatus(0);

			typeTemplateService.add(typeTemplate);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
}
