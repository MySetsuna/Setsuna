package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service(timeout = 15000)
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbItemCat> findAll() {
		return itemCatMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbItemCat> result = new PageResult<TbItemCat>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbItemCat> list = itemCatMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbItemCat> info = new PageInfo<TbItemCat>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public Long add(TbItemCat itemCat) {
		itemCatMapper.insertSelective(itemCat);
        List<TbItemCat> tbItemCats = itemCatMapper.select(itemCat);
        for (TbItemCat tbItemCat : tbItemCats) {
            return tbItemCat.getId();
        }
        return null;
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbItemCat itemCat){
		itemCatMapper.updateByPrimaryKeySelective(itemCat);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbItemCat findOne(Long id){
		return itemCatMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
		List longs = new ArrayList();
		for (Long id : ids) {
			selectAllId(longs,id);
		}
		//构建查询条件
        Example example = new Example(TbItemCat.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        itemCatMapper.deleteByExample(example);
	}

	/**
	 * 递归查询所有要删除的子节点列表
	 * @param list 记录每次查询结果的集合
	 * @param id 当前的父节点
	 */
	private void selectAllId(List list,Long id){
		//记录要删除的id
		list.add(id);
		//检测当前节点有没有子节点
		List<TbItemCat> itemCats = findByParentId(id);
		if(itemCats != null && itemCats.size() > 0){
			for (TbItemCat itemCat : itemCats) {
				//调用本身查询所有子节点
				selectAllId(list,itemCat.getId());
			}
		}
	}
	
	
	@Override
	public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
		PageResult<TbItemCat> result = new PageResult<TbItemCat>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbItemCat.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(itemCat!=null){			
						//如果字段不为空
			if (itemCat.getName()!=null && itemCat.getName().length()>0) {
				criteria.andLike("name", "%" + itemCat.getName() + "%");
			}
	
		}

        //查询数据
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbItemCat> info = new PageInfo<TbItemCat>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}

    @Override
    public List<TbItemCat> findByParentId(Long patentId) {
		TbItemCat where = new TbItemCat();
		where.setParentId(patentId);
		List<TbItemCat> itemCats = itemCatMapper.select(where);

		//统一做缓存更新
		List<TbItemCat> all = this.findAll();
		for (TbItemCat itemCat : all) {
			redisTemplate.boundHashOps("itemCats").put(itemCat.getName(), itemCat.getTypeId());
		}
		return itemCats;
    }

	/**
	 * 分类审核
	 * @param ids
	 * @param status
	 */
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		//修改的结果
		TbItemCat record = new TbItemCat();
		record.setStatus(status);
		//构建修改范围
		Example example = new Example(TbItemCat.class);
		Example.Criteria criteria = example.createCriteria();
		//遍历子节点
		List idList = new ArrayList();
		for (Long id : ids) {
			selectAllId(idList,id);
		}

		criteria.andIn("id", idList);
		//开始更新
		itemCatMapper.updateByExampleSelective(record,example);

	}

	/**zeke
	 * 商家的查询另开了两个个方法按sellerId查询，不影响运营管理商的全部查询
	 * @param loginName 当前商家名
//	 * @param sellerId 当前商家名
	 *——原谅下我上面这两个都是商家名，前端页面回显我entity取名用的loginName（登录名），后端数据库字段用的是sellerId（商家名），但其实从前端传参过来这两个就是一样的，就是我service.js传参时起名一下子脑抽混乱了，复制粘贴太多又不想改回来，备注让大家知悉下
	 * @return
	 */
	@Override
	public List<TbItemCat> sellerFindAll(String loginName) {
		//根据商家ID查询，区别于运营管理商的查询结果这里只返回该商家的申请规格
		TbItemCat where = new TbItemCat();
		where.setSellerId(loginName);
		return itemCatMapper.select(where);
	}

	@Override
	public PageResult sellerFindPage(TbItemCat itemCat, int pageNum, int pageSize, String sellerId) {
		PageResult<TbItemCat> result = new PageResult<TbItemCat>();
		//设置分页条件
		PageHelper.startPage(pageNum, pageSize);

		//构建查询条件
		Example example = new Example(TbItemCat.class);
		Example.Criteria criteria = example.createCriteria();

		if(itemCat!=null){
			//如果字段不为空
			if (itemCat.getName()!=null && itemCat.getName().length()>0) {
				criteria.andLike("name", "%" + itemCat.getName() + "%");
			}
			//如果用户名不为空
			if (sellerId !=null && sellerId !="") {
				criteria.andEqualTo("sellerId", sellerId);
			}
		}
		//查询数据
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//保存数据列表
		result.setRows(list);
		//获取总记录数
		PageInfo<TbItemCat> info = new PageInfo<TbItemCat>(list);
		result.setTotal(info.getTotal());

		return result;
	}

	@Override
	public List<TbItemCat> sellerFindByParentId(Long patentId, String sellerId) {
		TbItemCat where = new TbItemCat();
		where.setParentId(patentId);
		where.setSellerId(sellerId);
		List<TbItemCat> itemCats = itemCatMapper.select(where);

		//统一做缓存更新
		List<TbItemCat> all = this.findAll();
		for (TbItemCat itemCat : all) {
			redisTemplate.boundHashOps("itemCats").put(itemCat.getName(), itemCat.getTypeId());
		}
		return itemCats;
	}

    /**通过分类名称获取对应的Id
     * @param itemCatname
     * @return
     */
    @Override
    public Long findItemCatIdByName(String itemCatname) {
        TbItemCat where=new TbItemCat();
        where.setName(itemCatname);
        List<TbItemCat> tbItemCatList = itemCatMapper.select(where);
        for (TbItemCat tbItemCat : tbItemCatList) {
            return tbItemCat.getId();
        }
        return null;
    }

}
