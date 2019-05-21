package com.pinyougou.user.controller;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.user.service.AddressService;
import com.pinyougou.user.service.UserService;
import com.pinyougou.utils.PhoneFormatCheckUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbUser;

import entity.PageResult;
import entity.Result;
/**
 * 请求处理器
 * @author Steven
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference(timeout = 5000)
	private UserService userService;
	@Reference(timeout = 5000)
	private AddressService addressService;
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String code){
		try {
		    //先判断验证码是否正确
            boolean flag = userService.checkSmsCode(user.getPhone(), code);
            if(flag) {
                userService.add(user);
                return new Result(true, "增加成功");
            }else{
                return new Result(false, "验证码输入错误！");
            }
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			userService.update(user);
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
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/**
	 * 查询+分页
	 * @param user
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}

	@RequestMapping("sendSmsCode")
    public Result sendSmsCode(String phone){
        try {
            //先验证手机机是否合法
            if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
                return new Result(false, "请输入正确的手机号!");
            }
            userService.createSmsCode(phone);
            return new Result(true, "验证码已发送!");
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        return new Result(false, "验证码发送失败!");
    }

	/**
	 * 查询用户信息
	 * @param
	 * @return
	 */
	@RequestMapping("/findUserByUserId")
	public TbUser findUserByUserId(){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findUserByUserId(userName);
	}
	/**
	 * 查询用户收藏
	 * @param
	 * @return
	 */
	@RequestMapping("/findUserFavoriteByUserId")
	public List<TbItem> findUserFavoriteByUserId(){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findUserFavoriteByUserId(userName);
	}
	/**
	 * 查询用户足迹
	 * @param
	 * @return
	 */
	@RequestMapping("/findPersonFootmark")
	public List<TbGoods> findPersonFootmark(){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findPersonFootmark(userName);
	}


}
