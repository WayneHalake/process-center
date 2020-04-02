package com.haivera.processcenter.config;

import com.haivera.processcenter.eventListener.TestEventListener;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Event listener 配置
 */
@Configuration
public class ActivitiEventListenerConfiguration {

    @Autowired
    TestEventListener testEventListener;

    @Bean
    public ProcessEngineConfigurationImpl getProcessEngineConfigurationImpl(){
        StandaloneProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration();

        // 调度事件的顺序取决于侦听器的添加顺序
        // 全局监听器
        List<ActivitiEventListener> eventListeners = new ArrayList<>();
        eventListeners.add(testEventListener); //添加事件监听
        processEngineConfiguration.setEventListeners(eventListeners);

        //某些类型的事件时得到通知  eg: key="JOB_EXECUTION_SUCCESS,JOB_EXECUTION_FAILURE" 作业执行成功或失败
        HashMap<String, List<ActivitiEventListener>> typeEventListeners = new HashMap<>();
        //typeEventListeners.put(key, null); todo  添加事件监听器
        processEngineConfiguration.setTypedEventListeners(typeEventListeners);
        return processEngineConfiguration;
    }
}
