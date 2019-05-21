package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.sellergoods.service.TypeTemplateService;
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
@RequestMapping("/itemCat")
public class ItemCatController {

	@Reference
	private ItemCatService itemCatService;
	@Reference
    private TypeTemplateService typeTemplateService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbItemCat> findAll(){			
		return itemCatService.findAll();
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


	/**
	 * 分类审核
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("updateStatus")
	public Result updateStatus(Long[] ids, Integer status){
		try {
			//审核
			itemCatService.updateStatus(ids, status);
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
                String parentCateName = row.getCell(0).getStringCellValue();
                List<TbItemCat> itemCatList=  itemCatService.findAll();
                Long parentCateId=null;
                //遍历所有分类
                for (TbItemCat tbItemCat : itemCatList) {
                    if (parentCateName.equals(tbItemCat.getName())){
                        parentCateId = tbItemCat.getParentId();
                    }
                }
                //遍历完所有一级分类,如果parentCateId还是mnull,代表不存在当前excel里的父分类,需要新建父分类
                if (parentCateId==null){
                    TbItemCat tbItemCat = new TbItemCat();
                    tbItemCat.setParentId(new Long(0));
                    tbItemCat.setName(parentCateName);
                    Long tempId=typeTemplateService.findTempIdByTempName(row.getCell(1).getStringCellValue());
                    tbItemCat.setTypeId(tempId);
                    tbItemCat.setStatus(1);
                    parentCateId = itemCatService.add(tbItemCat);
                }

                TbItemCat tbItemCat = new TbItemCat();
                tbItemCat.setParentId(parentCateId);
                tbItemCat.setName(row.getCell(2).getStringCellValue());
                Long tempId=typeTemplateService.findTempIdByTempName(row.getCell(3).getStringCellValue());
                tbItemCat.setTypeId(tempId);
                itemCatService.add(tbItemCat);
			}
			xssfWorkbook.close();
			return new Result(true, "导入成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "导入失败");
		}
	}
}
