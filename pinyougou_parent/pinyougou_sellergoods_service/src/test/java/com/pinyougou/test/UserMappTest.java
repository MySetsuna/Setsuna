package com.pinyougou.test;

import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.test
 * @date 2019-4-8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class UserMappTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetById(){
        User user = userMapper.selectByPrimaryKey(24);
        System.out.println(user);
    }

    @Test
    public void testGetAll(){
        List<User> userList = userMapper.select(null);
        for (User user : userList) {
            System.out.println(user);
        }
    }
}
