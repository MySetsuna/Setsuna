package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.sellergoods.service.PayLogService;
import entity.PageResult;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class PayLogServiceImpl implements PayLogService {

	@Autowired
	private TbPayLogMapper payLogMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbPayLog> findAll() {
		return payLogMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageResult<TbPayLog> result = new PageResult<TbPayLog>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //查询数据
        List<TbPayLog> list = payLogMapper.select(null);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbPayLog> info = new PageInfo<TbPayLog>(list);
        result.setTotal(info.getTotal());
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbPayLog payLog) {
		payLogMapper.insertSelective(payLog);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbPayLog payLog){
		payLogMapper.updateByPrimaryKeySelective(payLog);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbPayLog findOne(Long id){
		return payLogMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbPayLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        payLogMapper.deleteByExample(example);
	}
	
	
	@Override
	public PageResult findPage(TbPayLog payLog, int pageNum, int pageSize) {
		PageResult<TbPayLog> result = new PageResult<TbPayLog>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbPayLog.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(payLog!=null){			
						//如果字段不为空
			if (payLog.getOutTradeNo()!=null && payLog.getOutTradeNo().length()>0) {
				criteria.andLike("outTradeNo", "%" + payLog.getOutTradeNo() + "%");
			}
			//如果字段不为空
			if (payLog.getUserId()!=null && payLog.getUserId().length()>0) {
				criteria.andLike("userId", "%" + payLog.getUserId() + "%");
			}
			//如果字段不为空
			if (payLog.getTransactionId()!=null && payLog.getTransactionId().length()>0) {
				criteria.andLike("transactionId", "%" + payLog.getTransactionId() + "%");
			}
			//如果字段不为空
			if (payLog.getTradeState()!=null && payLog.getTradeState().length()>0) {
				criteria.andLike("tradeState", "%" + payLog.getTradeState() + "%");
			}
			//如果字段不为空
			if (payLog.getOrderList()!=null && payLog.getOrderList().length()>0) {
				criteria.andLike("orderList", "%" + payLog.getOrderList() + "%");
			}
			//如果字段不为空
			if (payLog.getPayType()!=null && payLog.getPayType().length()>0) {
				criteria.andLike("payType", "%" + payLog.getPayType() + "%");
			}
	
		}

        //查询数据
        List<TbPayLog> list = payLogMapper.selectByExample(example);
        //保存数据列表
        result.setRows(list);

        //获取总记录数
        PageInfo<TbPayLog> info = new PageInfo<TbPayLog>(list);
        result.setTotal(info.getTotal());
		
		return result;
	}
	
}
