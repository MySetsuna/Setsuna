package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbTypeTemplate;
import entity.PageResult;

import java.util.List;
import java.util.Map;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface TypeTemplateService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbTypeTemplate> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbTypeTemplate typeTemplate);
	
	
	/**
	 * 修改
	 */
	public void update(TbTypeTemplate typeTemplate);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbTypeTemplate findOne(Long id);
	
	
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
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize);

	/**
	 * 跟据模板id查询规格与选项列表
	 * @param id 模板id
	 * @return 规格与选项列表
	 */
	public List<Map> findSpecList(Long id);

    void updateStatus(Long[] ids, Integer status);

	/**zeke
	 * 商家模板查询：查询+分页（区别在于商家查询只返回自己所申请过的模板）
	 * @param loginName 当前商家名
	 * @return
	 */
	public List<TbTypeTemplate> sellerFindAll(String loginName);

	/**zeke
	 * 商家模板查询：查询+分页（区别在于商家查询只返回自己所申请过的模板）
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult sellerFindPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize, String sellerId);

	/**通过模板名查模板Id
	 * @param tempName
	 * @return
	 */
    Long findTempIdByTempName(String tempName);

}
