package com.haivera.processcenter.serviceTask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateHelper;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * 通用的serviceTask
 */
@Component
public class CommonServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        //do nothing
    }

}
