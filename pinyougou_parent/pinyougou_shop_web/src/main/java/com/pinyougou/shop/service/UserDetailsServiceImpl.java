package com.pinyougou.shop.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 扩展权限认证类
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.shop.service
 * @date 2019-4-13
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //构建角色列表
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //查询数据商家的密码
        TbSeller seller = sellerService.findOne(username);
        //如果找到已审核通过商家
        if(seller != null && "1".equals(seller.getStatus())){
            //只要用户登录时输入的密码为123456就放行
            return new User(username,seller.getPassword(),authorities);
        }
        //返回空代表认证失败
        return null;
    }
}
