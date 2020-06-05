package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.GeneralCommonMap;
import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonBusSer;
import com.haivera.processcenter.service.ICommonProcessSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonBusImpl implements ICommonBusSer {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ICommonProcessSer commonProcessSer;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ICommonTaskSer commonTaskSer;

    @Autowired
    private GeneralCommonMap generalCommonMap;

    @Override
    public ResponseInfo getBaseInfo(String businessKey, String systemId) {
        ResponseInfo resp = new ResponseInfo();
        HashMap<String, Object> map = new HashMap<>();
        map.put("businessKey", businessKey);
        businessKey = IdCombine.combineId(systemId, businessKey);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        HashMap<String, Object> processInfo = new HashMap<>();
        String processId = "";
        if(processInstance != null){
            ExecutionEntityImpl entity = (ExecutionEntityImpl) processInstance;
            processInfo.putAll((Map<? extends String, ?>) entity.getPersistentState());
            processId = processInstance.getProcessInstanceId();
        }else{
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
            HistoricProcessInstanceEntityImpl entity = (HistoricProcessInstanceEntityImpl) historicProcessInstance;
            processInfo.putAll((Map<? extends String, ?>) entity.getPersistentState());
            processId = historicProcessInstance.getId();
        }

        map.put("processId", processId);
        map.put("processInfo", processInfo);

        if(commonProcessSer.isFinishedProcess(processId)){
            map.put("isFinish", true);
        }else {
            map.put("isFinish", false);
            ResponseInfo responseInfo = commonTaskSer.currentTasks(processId);
            map.put("taskInfo", responseInfo.getData());
        }

        resp.doSuccess("获取流程基本信息成功！", map);
        return resp;
    }

    @Override
    public String getProcessId(String businessKey, String systemId) {
        businessKey = IdCombine.combineId(systemId, businessKey);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        if(processInstance != null){
            return processInstance.getProcessInstanceId();
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        return historicProcessInstance.getId();
    }

}
