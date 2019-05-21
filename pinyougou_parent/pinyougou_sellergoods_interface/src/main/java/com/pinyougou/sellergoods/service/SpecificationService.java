package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojogroup.Specification;
import entity.PageResult;

import java.util.List;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface SpecificationService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSpecification> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Specification specification);
	/**
	 * 增加
	*/
	public void add(TbSpecification specification);
	
	
	/**
	 * 修改
	 */
	public void update(Specification specification);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Specification findOne(Long id);
	
	
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
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize);

    void updateStatus(Long[] ids, Integer status);

	/**
	 * 商家规格查询：查询+分页（区别在于商家查询只返回自己所申请过的规格）
	 * @param loginName 当前商家名
	 * @return
	 */
	public List<TbSpecification> sellerFindAll(String loginName);
	/**
	 * 商家品牌查询：查询+分页（区别在于商家查询只返回自己所申请过的品牌）
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult sellerFindPage(TbSpecification specification, int pageNum, int pageSize, String sellerId);

	Long findOneByName(String specName);

}
