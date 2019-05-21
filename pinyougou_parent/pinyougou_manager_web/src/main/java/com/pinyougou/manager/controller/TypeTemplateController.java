package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.sellergoods.service.SpecificationService;
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

import java.util.ArrayList;
import java.util.HashMap;
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
    @Reference
    private SpecificationService specificationService;
    @Reference
    private BrandService brandService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbTypeTemplate> findAll(){
		return typeTemplateService.findAll();
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

	/**
	 * 模板审核
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("updateStatus")
	public Result updateStatus(Long[] ids, Integer status){
		try {
			//审核
			typeTemplateService.updateStatus(ids, status);
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
                TbTypeTemplate tbTypeTemplate = new TbTypeTemplate();
                String templateName = row.getCell(0).getStringCellValue();
                tbTypeTemplate.setName(templateName);//设置模板名称
                String specs = row.getCell(1).getStringCellValue();
                String[] specNames = specs.split(",");
                ArrayList<Map> specIds = new ArrayList<>();

                for (String specName : specNames) {
                    Map map=new HashMap<>();
                    //[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
                    Long specId = specificationService.findOneByName(specName);
                    if (specId == null) {
                        return new Result(false, "导入失败");
                    }
                    map.put("id",specId);
                    map.put("text",specName);
                    specIds.add(map);
                }
                tbTypeTemplate.setSpecIds(JSON.toJSONString(specIds));//设置规格字符串

                String brands = row.getCell(2).getStringCellValue();
                String[] brandNames = brands.split(",");
                ArrayList<Map> brandIds = new ArrayList<>();
                for (String brandName : brandNames) {
                    Map map=new HashMap<>();
                    //[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
                    Long brandId = brandService.findBrandIdByName(brandName);
                    if (brandId == null) {
                        return new Result(false, "导入失败");
                    }
                    map.put("id",brandId);
                    map.put("text",brandName);
                    brandIds.add(map);
                }
                tbTypeTemplate.setBrandIds(JSON.toJSONString(brandIds));//设置品牌字符串


                String cusItems = row.getCell(3).getStringCellValue();
                ArrayList<Map> cusItemsList = new ArrayList<>();
                if ("null".equals(cusItems)) {
                    tbTypeTemplate.setCustomAttributeItems(JSON.toJSONString(cusItemsList));
                }else {
                    String[] cusItemNames = cusItems.split(",");

                    for (String cusItemName : cusItemNames) {
                        Map map=new HashMap<>();
                        //[{"text":"内存大小"},{"text":"颜色"}]
                        map.put("text",cusItemName);
                        cusItemsList.add(map);
                    }
                    tbTypeTemplate.setCustomAttributeItems(JSON.toJSONString(cusItemsList));//设置扩展属性字符串
                }
                tbTypeTemplate.setStatus(1);//设置模板审核状态为通过审核
                typeTemplateService.add(tbTypeTemplate);
			}
			xssfWorkbook.close();
			return new Result(true, "导入成功");
		}catch (RuntimeException e){
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
		catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "导入失败");
		}
	}

    public static void main(String[] args) {
        List<Map> list = new ArrayList<>();
        Map map=new HashMap();
        map.put("id",12);
        map.put("text","我");
        list.add(map);
        map=new HashMap();
        map.put("id",13);
        map.put("text","你");
        list.add(map);
        System.out.println(JSON.toJSONString(list));
        System.out.println(list.toString());
    }
}
