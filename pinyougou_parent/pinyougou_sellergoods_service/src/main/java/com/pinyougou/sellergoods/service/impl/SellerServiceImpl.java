package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSellerMapper;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service(timeout = 5000)
public class SellerServiceImpl implements SellerService {

	@Autowired
	private TbSellerMapper sellerMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeller> findAll() {
		return sellerMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbSeller> result = new PageResult<TbSeller>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbSeller> list = sellerMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSeller> info = new PageInfo<TbSeller>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeller seller) {
		//初始化数据
		seller.setStatus("0");  //新增商家-未审核状态
		seller.setCreateTime(new Date());
		sellerMapper.insertSelective(seller);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSeller seller){
		sellerMapper.updateByPrimaryKeySelective(seller);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSeller findOne(String id){
		return sellerMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbSeller.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        sellerMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbSeller seller, int pageNum, int pageSize) {
		PageResult<TbSeller> result = new PageResult<TbSeller>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbSeller.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(seller!=null){			
						//如果字段不为空
			if (seller.getSellerId()!=null && seller.getSellerId().length()>0) {
				criteria.andLike("sellerId", "%" + seller.getSellerId() + "%");
			}
			//如果字段不为空
			if (seller.getName()!=null && seller.getName().length()>0) {
				criteria.andLike("name", "%" + seller.getName() + "%");
			}
			//如果字段不为空
			if (seller.getNickName()!=null && seller.getNickName().length()>0) {
				criteria.andLike("nickName", "%" + seller.getNickName() + "%");
			}
			//如果字段不为空
			if (seller.getPassword()!=null && seller.getPassword().length()>0) {
				criteria.andLike("password", "%" + seller.getPassword() + "%");
			}
			//如果字段不为空
			if (seller.getEmail()!=null && seller.getEmail().length()>0) {
				criteria.andLike("email", "%" + seller.getEmail() + "%");
			}
			//如果字段不为空
			if (seller.getMobile()!=null && seller.getMobile().length()>0) {
				criteria.andLike("mobile", "%" + seller.getMobile() + "%");
			}
			//如果字段不为空
			if (seller.getTelephone()!=null && seller.getTelephone().length()>0) {
				criteria.andLike("telephone", "%" + seller.getTelephone() + "%");
			}
			//如果字段不为空
			if (seller.getStatus()!=null && seller.getStatus().length()>0) {
				criteria.andLike("status", "%" + seller.getStatus() + "%");
			}
			//如果字段不为空
			if (seller.getAddressDetail()!=null && seller.getAddressDetail().length()>0) {
				criteria.andLike("addressDetail", "%" + seller.getAddressDetail() + "%");
			}
			//如果字段不为空
			if (seller.getLinkmanName()!=null && seller.getLinkmanName().length()>0) {
				criteria.andLike("linkmanName", "%" + seller.getLinkmanName() + "%");
			}
			//如果字段不为空
			if (seller.getLinkmanQq()!=null && seller.getLinkmanQq().length()>0) {
				criteria.andLike("linkmanQq", "%" + seller.getLinkmanQq() + "%");
			}
			//如果字段不为空
			if (seller.getLinkmanMobile()!=null && seller.getLinkmanMobile().length()>0) {
				criteria.andLike("linkmanMobile", "%" + seller.getLinkmanMobile() + "%");
			}
			//如果字段不为空
			if (seller.getLinkmanEmail()!=null && seller.getLinkmanEmail().length()>0) {
				criteria.andLike("linkmanEmail", "%" + seller.getLinkmanEmail() + "%");
			}
			//如果字段不为空
			if (seller.getLicenseNumber()!=null && seller.getLicenseNumber().length()>0) {
				criteria.andLike("licenseNumber", "%" + seller.getLicenseNumber() + "%");
			}
			//如果字段不为空
			if (seller.getTaxNumber()!=null && seller.getTaxNumber().length()>0) {
				criteria.andLike("taxNumber", "%" + seller.getTaxNumber() + "%");
			}
			//如果字段不为空
			if (seller.getOrgNumber()!=null && seller.getOrgNumber().length()>0) {
				criteria.andLike("orgNumber", "%" + seller.getOrgNumber() + "%");
			}
			//如果字段不为空
			if (seller.getLogoPic()!=null && seller.getLogoPic().length()>0) {
				criteria.andLike("logoPic", "%" + seller.getLogoPic() + "%");
			}
			//如果字段不为空
			if (seller.getBrief()!=null && seller.getBrief().length()>0) {
				criteria.andLike("brief", "%" + seller.getBrief() + "%");
			}
			//如果字段不为空
			if (seller.getLegalPerson()!=null && seller.getLegalPerson().length()>0) {
				criteria.andLike("legalPerson", "%" + seller.getLegalPerson() + "%");
			}
			//如果字段不为空
			if (seller.getLegalPersonCardId()!=null && seller.getLegalPersonCardId().length()>0) {
				criteria.andLike("legalPersonCardId", "%" + seller.getLegalPersonCardId() + "%");
			}
			//如果字段不为空
			if (seller.getBankUser()!=null && seller.getBankUser().length()>0) {
				criteria.andLike("bankUser", "%" + seller.getBankUser() + "%");
			}
			//如果字段不为空
			if (seller.getBankName()!=null && seller.getBankName().length()>0) {
				criteria.andLike("bankName", "%" + seller.getBankName() + "%");
			}
	
		}

        //查询数据
        List<TbSeller> list = sellerMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbSeller> info = new PageInfo<TbSeller>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}
	
}
