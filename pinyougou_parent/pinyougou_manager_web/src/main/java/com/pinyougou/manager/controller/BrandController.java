package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){			
		return brandService.findAll();
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
	 * 查询+分页
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
	 * 品牌审核
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("updateStatus")
	public Result updateStatus(Long[] ids, Integer status){
		try {
			//审核
			brandService.updateStatus(ids, status);
			return new Result(true, "审核操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(false, "审核操作失败！");
	}

	@RequestMapping("upload")
	public Result upload(MultipartFile file){
		try {
			//解析.xlsx文件
			//1.加载Excel文件对象,xlsx对应XSSFWorkbook,xls对应HSSFWorkbook
			//如果是File类
//          HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(brandFile));
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
			//2.读取第一个sheet
			XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
			//3.读取每一行,对应一个Brand对象
			for (Row row : sheet) {
				//跳过第一行的表头
			    if (row.getRowNum() == 0) continue;
                TbBrand tbBrand = new TbBrand();
                tbBrand.setName(row.getCell(0).getStringCellValue());//name
                tbBrand.setFirstChar(row.getCell(1).getStringCellValue());//firstChar
                brandService.add(tbBrand);
			}
			xssfWorkbook.close();
			return new Result(true, "导入成功");
		} catch (Exception e) {
			e.printStackTrace();
            return new Result(false, "导入失败");
		}
	}
	
}
