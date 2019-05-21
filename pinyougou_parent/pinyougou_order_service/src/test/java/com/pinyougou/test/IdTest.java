package com.pinyougou.test;

import com.pinyougou.utils.IdWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.test
 * @date 2019-5-9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class IdTest {

    @Autowired
    private IdWorker worker;

    @Test
    public void testGetId(){
        System.out.println("----------------------------------------------");
        System.out.println(worker.nextId());
        System.out.println("----------------------------------------------");
    }
}
