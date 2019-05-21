package com.pinyougou.sellergoods.service;
import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface BrandService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbBrand> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbBrand brand);
	
	
	/**
	 * 修改
	 */
	public void update(TbBrand brand);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize);

	/**
	 * 商家品牌查询：查询+分页（区别在于商家查询只返回自己所申请过的品牌）
	 * @param loginName 当前商家名
	 * @return
	 */
	public List<TbBrand> sellerFindAll(String loginName);

	/**
	 * 商家品牌查询：查询+分页（区别在于商家查询只返回自己所申请过的品牌）
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult sellerFindPage(TbBrand brand, int pageNum, int pageSize,String sellerId);



	/**
	 * 审核状态
	 * @param ids
	 * @param status
	 */
    void updateStatus(Long[] ids, Integer status);

	/**通过name找品牌的id
	 * @param name
	 * @return
	 */
    Long findBrandIdByName(String name);
}
