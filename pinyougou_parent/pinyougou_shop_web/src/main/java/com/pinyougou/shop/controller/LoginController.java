package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.manager.controller
 * @date 2019-4-13
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @RequestMapping("name")
    public Map<String,Object> name(){
        Map<String, Object> result = new HashMap<>();
        //获取登录名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        result.put("loginName", name);
        return result;
    }
}
