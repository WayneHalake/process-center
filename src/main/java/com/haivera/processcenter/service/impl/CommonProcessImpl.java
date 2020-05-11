package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.service.ICommonProcessSer;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class CommonProcessImpl implements ICommonProcessSer {

    private static Logger logger = LoggerFactory.getLogger(CommonProcessImpl.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private CommonTaskImpl commonTaskImpl;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    @Autowired
    ProcessEngine processEngine;

    @Override
    public String StartAndCreateProcess(String processKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        String processInsId = processInstance.getId();
        logger.info("创建流程成功，流程key["+processKey+"]，流程实例化Id["+processInsId+"]");
        return processInsId;
    }

    @Override
    public List<ProcessInstance> listProcessInstance(String processKey) {
        return runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).list();
    }

    @Override
    public List<ProcessInstance> listAllProcessInstance() {
        return runtimeService.createProcessInstanceQuery().list();
    }

    @Override
    public List<HistoricActivityInstance> listHistory(String processId) {
        return historyService.createHistoricActivityInstanceQuery().processInstanceId(processId).list();
    }

    @Override
    public void getWorkFlowImage(HttpServletResponse response, String processId, boolean lightFlag) {
        //获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();

        //当前正在运行的任务高亮
        if(lightFlag) {
            if(!this.isFinishedProcess(processId)){
                List<Task> runTaskList = commonTaskImpl.listRunTask(processId);
                for(Task task: runTaskList){
                    highLightedActivitis.add(task.getTaskDefinitionKey());
                }
            }else{
                highLightedActivitis.add(processInstance.getEndActivityId());
            }

        }
        //生成图片
        byte[] b = new byte[1024];
        int len;
        try(InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "png",
                highLightedActivitis, Collections.emptyList(),
                "WenQuanYi Micro Hei", "WenQuanYi Micro Hei",
                "WenQuanYi Micro Hei", null, 1.0);
            OutputStream outputStream = response.getOutputStream()
        ) {
            while((len= inputStream.read(b, 0, 1024)) != -1){
                outputStream.write(b, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void suspendProcess(String processId) {
        repositoryService.suspendProcessDefinitionById(processId);
        logger.info("挂起流程成功,流程id[{}]", processId);
    }

    @Override
    public void activateProcess(String processId) {
        repositoryService.activateProcessDefinitionById(processId);
        logger.info("激活流程成功,流程id[{}]", processId);
    }

    @Override
    public boolean isFinishedProcess(String processId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        if(processInstance == null){
            return true;
        }
        return false;
    }

    @Override
    public ProcessInstance getProcessByProcessId(String processId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
    }

    @Override
    public ProcessInstance getProcessByTaskId(String taskId) {
        Task task = commonTaskImpl.getTaskByTaskId(taskId);
        if(task == null){
            return null;
        }
        return getProcessByProcessId(task.getProcessInstanceId());
    }

}
