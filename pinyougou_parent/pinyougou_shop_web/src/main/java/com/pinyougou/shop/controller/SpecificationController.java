package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;
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
@RequestMapping("/specification")
public class SpecificationController {

	@Reference
	private SpecificationService specificationService;

	/**
	 * 返回该商家全部列表
	 *
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSpecification> findAll(String loginName) {
		//前端初始化传值传不过来，在这直接取值覆盖掉
		LoginController loginController = new LoginController();
		loginName =(String) loginController.name().get("loginName");
		return specificationService.sellerFindAll(loginName);
	}


	/**
	 * 返回全部列表
	 *
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows) {
		return specificationService.findPage(page, rows);
	}

	/**
	 * 增加
	 *
	 * @param specification
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Specification specification) {
		try {
			specificationService.add(specification);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 修改
	 *
	 * @param specification
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Specification specification) {
		try {
			//一旦修改，审核状态变更回未审核
			specification.getSpecification().setStatus(0);
			specificationService.update(specification);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 获取实体
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Specification findOne(Long id) {
		return specificationService.findOne(id);
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			specificationService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 *
	 * @param specification
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSpecification specification, int page, int rows) {
		return specificationService.findPage(specification, page, rows);
	}

	/**
	 * 商家品牌查询：查询+分页（区别在于商家查询只返回自己所申请过的品牌）
	 *
	 * @param specification
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/sellerSearch")
	public PageResult sellerSearch(@RequestBody TbSpecification specification, int page, int rows, String sellerId) {
		return specificationService.sellerFindPage(specification, page, rows, sellerId);
	}
	/**
	 * 商家品牌增加:（区别在于商家添加需增加商家及状态信息）
	 * @param specification
	 * @return
	 */
	@RequestMapping("/sellerAdd")
	public Result sellerAdd(@RequestBody Specification specification,String loginName) {
		try {
			//添加加商家及状态信息
			specification.getSpecification().setSellerId(loginName);
			specification.getSpecification().setStatus(0);

			specificationService.add(specification);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}


//end node.
}
