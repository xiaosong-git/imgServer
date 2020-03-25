package com.xdream.goldccm.service.inAndOutService.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class IInAndOutSerivceImplTest {
    @Autowired
    private TaskExecutor taskExecutor;
    @Test
    public void save() {
        System.out.println("test1");
    }

}