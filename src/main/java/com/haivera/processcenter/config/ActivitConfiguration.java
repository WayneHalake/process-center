package com.haivera.processcenter.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.haivera.processcenter.eventListener.TestEventListener;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
public class ActivitConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DruidDataSource dataSource() {
        DruidDataSource ds = new DruidDataSource();
        return ds;
    }

    @Autowired
    TestEventListener testEventListener;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(){
        SpringProcessEngineConfiguration spec = new SpringProcessEngineConfiguration();
        spec.setDataSource(dataSource());
        spec.setTransactionManager(platformTransactionManager);
        spec.setDatabaseSchemaUpdate("true");
        Resource[] resources = null;
        // 启动自动部署流程
        try {
            resources = new PathMatchingResourcePatternResolver().getResources("classpath*:processes/**/*.bpmn");
        } catch (IOException e) {
            e.printStackTrace();
        }
        spec.setDeploymentResources(resources);

        // 调度事件的顺序取决于侦听器的添加顺序
        // 全局监听器
        List<ActivitiEventListener> eventListeners = new ArrayList<>();
//        eventListeners.add(testEventListener); //添加事件监听
        spec.setEventListeners(eventListeners);

        //某些类型的事件时得到通知  eg: key="JOB_EXECUTION_SUCCESS,JOB_EXECUTION_FAILURE" 作业执行成功或失败
        HashMap<String, List<ActivitiEventListener>> typeEventListeners = new HashMap<>();
        //typeEventListeners.put(key, null); todo  添加事件监听器
        spec.setTypedEventListeners(typeEventListeners);
        return spec;
    }

    @Bean
    public ProcessEngineFactoryBean processEngine(){
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration());
        return processEngineFactoryBean;
    }


    @Bean
    public RepositoryService repositoryService() throws Exception{
        return processEngine().getObject().getRepositoryService();
    }
    @Bean
    public RuntimeService runtimeService() throws Exception{
        return processEngine().getObject().getRuntimeService();
    }
    @Bean
    public TaskService taskService() throws Exception{
        return processEngine().getObject().getTaskService();
    }
    @Bean
    public HistoryService historyService() throws Exception{
        return processEngine().getObject().getHistoryService();
    }


}
