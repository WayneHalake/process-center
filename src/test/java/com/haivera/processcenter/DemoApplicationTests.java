package com.haivera.processcenter;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

//    @Autowired
//    private IdentityService identityService;

    @Test
    public void contextLoads() {
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();

        IdentityService identityService= processEngine.getIdentityService();
        User user= identityService.newUser("zhangsan");
        user.setPassword("123");
        user.setEmail("1123@qq.com");
        identityService.saveUser(user);
    }

}
