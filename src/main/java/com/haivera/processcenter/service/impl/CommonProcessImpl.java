package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.GeneralCommonMap;
import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonHistoricSer;
import com.haivera.processcenter.service.ICommonProcessSer;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

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

    @Autowired
    IdentityService identityService;

    @Autowired
    ICommonHistoricSer ICommonHistoricSer;

    @Autowired
    GeneralCommonMap generalCommonMap;

    @Override
    public String StartAndCreateProcess(String processKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        String processInsId = processInstance.getId();
        logger.info("创建流程成功，流程key["+processKey+"]，流程实例化Id["+processInsId+"]");
        return processInsId;
    }

    @Override
    public String startProcessInstanceByKey(String processKey, String busCode, String busType, String userId, String systemId) {
        logger.info("开始创建流程，业务编码[{}]，业务类型[{}]", busCode, busType);
        Map<String, Object> variables = new HashMap<>();
        String startUserId = IdCombine.combineId(systemId, userId);
        String businessKey = IdCombine.combineId(systemId, busCode);

        variables.put("busCode", busCode);
        variables.put("busType", busType);
        variables.put("processName", busType);
        variables.put("processCode", processKey);
        variables.put("businessKey", businessKey);

        identityService.setAuthenticatedUserId(startUserId); //设置流程发起人
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        String processInsId = processInstance.getId();
        logger.info("创建流程成功，流程key[{}]，流程实例化Id[{}]", processKey, processInsId);
        return processInsId;
    }

    @Override
    public ResponseInfo listProcessInstanceStartBy(String systemId, String userId) {
        return ICommonHistoricSer.listHisProcessInstance(null, systemId, userId, false);
    }

    @Override
    public ResponseInfo listProcessInstance(String processKey) {
        ResponseInfo resp = new ResponseInfo();
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        if(StringUtils.isNotEmpty(processKey)){
            processInstanceQuery = processInstanceQuery.processDefinitionKey(processKey);
        }
        List<ProcessInstance> processInstances = processInstanceQuery.list();
        List<Object> datas = new ArrayList<>();
        for(ProcessInstance processInstance: processInstances){
            ExecutionEntityImpl executionEntity = (ExecutionEntityImpl)processInstance;
            HashMap<String, Object> map = new HashMap<>();
            map.putAll((Map<? extends String, ?>) executionEntity.getPersistentState());
            map.put("processId", executionEntity.getProcessInstanceId());

            //子流程信息
            ResponseInfo subProcessInfo = getSubProcessByProcessId(executionEntity.getProcessInstanceId());
            map.put("subProcessInfo", subProcessInfo.getData());
            datas.add(map);
        }
        resp.doSuccess("获取流程列表成功！", datas);
        return resp;
    }

    @Override
    public ResponseInfo listAllProcessInstance() {
        return listProcessInstance(null);
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
        ProcessInstance processInstance = getProcessByProcessId(processId);
        repositoryService.suspendProcessDefinitionById(processInstance.getProcessDefinitionId());
        logger.info("挂起流程成功,流程id[{}]", processId);
    }

    @Override
    public void activateProcess(String processId) {
        ProcessInstance processInstance = getProcessByProcessId(processId);
        repositoryService.activateProcessDefinitionById(processInstance.getProcessDefinitionId());
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

    @Override
    public ResponseInfo getSubProcessByProcessId(String processId) {
        ResponseInfo resp = new ResponseInfo();
        List<Object> datas = new ArrayList<>();

        List<Execution> executionList = runtimeService.createExecutionQuery().rootProcessInstanceId(processId).onlySubProcessExecutions().list();
        for (Execution execution: executionList ){
            ExecutionEntityImpl entity = (ExecutionEntityImpl)execution;
            HashMap<String, Object> map = new HashMap<>();
            map.put("processId", execution.getProcessInstanceId());
            map.put("processInfo", entity.getPersistentState());
            List<Task> taskList = commonTaskImpl.currentTask(execution.getProcessInstanceId());
            if(taskList!=null && taskList.size()>0){
                map.put("taskId", taskList.get(0).getId());
                map.put("taskInfo", generalCommonMap.taskInfoMap(taskList.get(0)));
            }
            datas.add(map);
        }
        resp.doSuccess("获取子流程信息成功", datas);
        return resp;
    }

    @Override
    public boolean isSubProcess(String processId) {
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processId).list();
        for(Execution execution: executions){
            if(!execution.getProcessInstanceId().equals(execution.getRootProcessInstanceId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseInfo getRootProcessByProcessId(String processId) {
        ResponseInfo resp = new ResponseInfo();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processId).list();
        if(executions == null){
            resp.doSuccess("获取父流程为空");
            return resp;
        }
        String rootProcessId = executions.get(0).getRootProcessInstanceId();
        if(StringUtils.isEmpty(rootProcessId)){
            resp.doSuccess("获取父流程为空");
            return resp;
        }

        ProcessInstance processInstance = getProcessByProcessId(rootProcessId);
        if(processInstance == null){
            resp.doSuccess("获取父流程为空");
            return resp;
        }
        resp.doSuccess("获取父流程成功！", generalCommonMap.processInfoMap(processInstance));
        return resp;
    }
}
