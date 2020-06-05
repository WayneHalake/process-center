package com.haivera.processcenter.executionListener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 初始化A模型子流程
 */
@Component
public class SubProcessAInit implements ExecutionListener {

    /**
     * 流程名称
     */
    private Expression processName;

    /**
     * 流程编码
     */
    private Expression processCode;

    /**
     * businessKey
     */
    private Expression businessKey;

    @Override
    public void notify(DelegateExecution execution) {
        String processId = execution.getProcessInstanceId();
        String processNameValue = getProcessName(execution);
        String processCodeValue = getProcessCode(execution);
        String businessKeyValue = getBusinessKey(execution);

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        if(StringUtils.isNotEmpty(processNameValue)){
            runtimeService.setProcessInstanceName(processId, processNameValue);
        }

        if(StringUtils.isNotEmpty(businessKeyValue)){
            if(StringUtils.isNotEmpty(processCodeValue)){
                businessKeyValue = processCodeValue.concat("_").concat(businessKeyValue);
            }
            runtimeService.updateBusinessKey(processId, businessKeyValue);
        }

    }

    public String getProcessName(DelegateExecution execution) {
        if(processName == null){
            return null;
        }
        return processName.getValue(execution).toString();
    }

    public void setProcessName(Expression processName) {
        this.processName = processName;
    }

    public String getBusinessKey(DelegateExecution execution) {
        if(businessKey == null){
            return null;
        }
        return businessKey.getValue(execution).toString();
    }

    public void setBusinessKey(Expression businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessCode(DelegateExecution execution) {
        if(processCode == null){
            return null;
        }
        return processCode.getValue(execution).toString();
    }

    public void setProcessCode(Expression processCode) {
        this.processCode = processCode;
    }
}
