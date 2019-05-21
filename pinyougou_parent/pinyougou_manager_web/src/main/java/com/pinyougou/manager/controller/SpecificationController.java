package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSpecification> findAll(){			
		return specificationService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return specificationService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param specification
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Specification specification){
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
	 * @param specification
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Specification specification){
		try {
			specificationService.update(specification);
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
	public Specification findOne(Long id){
		return specificationService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
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
	 * @param specification
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSpecification specification, int page, int rows  ){
		return specificationService.findPage(specification, page, rows);		
	}

	/**
	 * 规格审核
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("updateStatus")
	public Result updateStatus(Long[] ids, Integer status){
		try {
			//审核
			specificationService.updateStatus(ids, status);
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
            Map<String,List<TbSpecificationOption>> map=new HashMap<>();
            //遍历excel每行
			for (Row row : sheet) {
			    //跳过第一行
                if (row.getRowNum() == 0) continue;
                Specification specification = new Specification();

                TbSpecification tbSpecification=null;



                List<TbSpecificationOption> tbSpecificationOptions = map.get(row.getCell(0).getStringCellValue());
                //如果从map中取出的集合为不为空,或者长度大于0
                if (tbSpecificationOptions == null) {
                    tbSpecificationOptions=new ArrayList<>();
                    map.put(row.getCell(0).getStringCellValue(),tbSpecificationOptions);
                }
                TbSpecificationOption tbSpecificationOption=new TbSpecificationOption();
                tbSpecificationOption.setOptionName(row.getCell(1).getStringCellValue());
                double cellValue = row.getCell(2).getNumericCellValue();
                int orderValue=(int) cellValue;
                tbSpecificationOption.setOrders(orderValue);

                tbSpecificationOptions.add(tbSpecificationOption);
                /*tbSpecification = new TbSpecification();
                tbSpecification.setSpecName(row.getCell(0).getStringCellValue());//name*/
                map.put(row.getCell(0).getStringCellValue(),tbSpecificationOptions);
            }
            //所有行遍历完,之后取出map的key
            Set<String> specSet = map.keySet();
            for (String specName : specSet) {
                TbSpecification tbSpecification = new TbSpecification();
                tbSpecification.setSpecName(specName);
                Specification specification = new Specification();
                specification.setSpecification(tbSpecification);
                List<TbSpecificationOption> tbSpecificationOptions = map.get(specName);
                specification.setSpecificationOptionList(tbSpecificationOptions);
                specificationService.add(specification);
            }
            xssfWorkbook.close();
			return new Result(true, "导入成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "导入失败");
		}
	}
	
}
