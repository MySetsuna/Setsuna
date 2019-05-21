package com.pinyougou.user.listener;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;
import java.util.Enumeration;

public class UserSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent event) {

//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println(userName);

        HttpSession session = event.getSession();
        System.out.println("已创建session-创建时间:" + new Date());
        System.out.println(session.getId());


    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();

        System.out.println(session.getId());
        String userName = null;
        try {
            userName = SecurityContextHolder.getContext().getAuthentication().getName();
        }catch (Exception e){
            System.out.println("未获取到");
        }
        System.out.println(userName);
        if (userName != null && !"".equals(userName)) {
            System.out.println("调用service层方法");
            System.out.println("session已失效-失效时间:" + new Date());

        }


    }
}
