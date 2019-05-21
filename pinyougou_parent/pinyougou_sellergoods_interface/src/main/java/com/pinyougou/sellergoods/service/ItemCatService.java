package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbItemCat;
import entity.PageResult;

import java.util.List;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface ItemCatService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbItemCat> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public Long add(TbItemCat itemCat);
	
	
	/**
	 * 修改
	 */
	public void update(TbItemCat itemCat);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbItemCat findOne(Long id);
	
	
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
	public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize);

	/**
	 * 跟据父节点查询子节点列表
	 * @param patentId
	 * @return
	 */
	public List<TbItemCat> findByParentId(Long patentId);

    void updateStatus(Long[] ids, Integer status);

	/**zeke
	 * 商家分类查询：查询+分页（区别在于商家查询只返回自己所申请过的分类）
	 * @param loginName 当前商家名，等同于下面的sellerId
	 * @return
	 */
	public List<TbItemCat> sellerFindAll(String loginName);

	/**zeke
	 * 商家分类查询：查询+分页（区别在于商家查询只返回自己所申请过的分类）
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult sellerFindPage(TbItemCat itemCat, int pageNum, int pageSize, String sellerId);

	/**zeke
	 * 商家分类查询：查询+分页（区别在于商家查询只返回自己所申请过的分类）
	 * @return
	 */
	public List<TbItemCat> sellerFindByParentId(Long patentId,String sellerId);

	Long findItemCatIdByName(String itemCatname);

}
