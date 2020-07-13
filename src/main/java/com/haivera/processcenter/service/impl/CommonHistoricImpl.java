package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonHistoricSer;
import com.haivera.processcenter.service.ICommonProcessSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import com.haivera.processcenter.vo.HistoricActivityInstanceEntity;
import com.haivera.processcenter.vo.HistoricVariableInstanceEntity;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.HistoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntityImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CommonHistoricImpl extends HistoryServiceImpl implements ICommonHistoricSer {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ICommonTaskSer commonTaskSer;

    @Autowired
    private ICommonProcessSer commonProcessSer;

    @Override
    public HistoricProcessInstance getHisProcessInstance(String processId) {
        HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        processInstanceQuery.processInstanceId(processId);
        HistoricProcessInstance processInstances = processInstanceQuery.singleResult();
        return processInstances;
    }

    @Override
    public ResponseInfo listHisProcessInstance(String processKey, boolean finishFlag) {
        return listHisProcessInstance(processKey, null, finishFlag);
    }

    @Override
    public ResponseInfo listHisProcessInstance(String systemId, String userId, boolean finishFlag) {
        return listHisProcessInstance(null, systemId, userId, finishFlag);
    }

    @Override
    public ResponseInfo listHisProcessInstance(String processKey, String systemId, String userId, boolean finishFlag) {
        ResponseInfo resp = new ResponseInfo();
        HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery();

        String assignee = IdCombine.combineId(systemId, userId);
        if (StringUtils.isNotEmpty(assignee)) {
            processInstanceQuery.startedBy(assignee);
        }

        if (StringUtils.isNotEmpty(processKey)) {
            processInstanceQuery.processDefinitionKey(processKey);
        }

        if (finishFlag) {
            processInstanceQuery.finished();
            processInstanceQuery.orderByProcessInstanceEndTime().desc();
        } else {
            processInstanceQuery.unfinished();
            processInstanceQuery.orderByProcessInstanceStartTime().desc();
        }
        List<HistoricProcessInstance> processInstances = processInstanceQuery.listPage(0, 6);
        List<Object> datas = new ArrayList<>();
        for (HistoricProcessInstance processInstance : processInstances) {
            HistoricProcessInstanceEntityImpl entity = (HistoricProcessInstanceEntityImpl) processInstance;
            HashMap<String, Object> tempResult = (HashMap<String, Object>) entity.getPersistentState();
            tempResult.put("startTime", entity.getStartTime());
            tempResult.put("processInstanceId", entity.getProcessInstanceId());
            boolean isFinish = commonProcessSer.isFinishedProcess(entity.getProcessInstanceId());
            tempResult.put("isFinish", isFinish);
            if (!isFinish) {
                List<Task> taskList = commonTaskSer.currentTask(entity.getProcessInstanceId());
                //正常情况下只有数据一条(会签情况除外)
                if(taskList != null && taskList.size() > 0){
                    Task task = taskList.get(0);
                    tempResult.put("taskId", task.getId());
                    tempResult.put("taskName", task.getName());
                    tempResult.put("assignee", task.getAssignee());
                }
            }
            datas.add(tempResult);
        }

        resp.doSuccess("查找流程历史记录成功", datas);
        return resp;
    }

    @Override
    public ResponseInfo listHisProcess(String processKey) {
        return listHisProcess(processKey, null, null);
    }

    @Override
    public ResponseInfo listHisProcess(String systemId, String userId) {
        return listHisProcess(null, systemId, userId);
    }

    @Override
    public ResponseInfo listHisProcess(String processKey, String systemId, String userId) {
        ResponseInfo resp = new ResponseInfo();

        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery().finished();

        if (StringUtils.isNotEmpty(processKey)) {
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processKey).finished().singleResult();
            historicActivityInstanceQuery.processDefinitionId(processInstance.getProcessDefinitionId());
        }

        String assignee = IdCombine.combineId(systemId, userId);
        if (StringUtils.isNotEmpty(assignee)) {
            historicActivityInstanceQuery.taskAssignee(assignee);
        }
        List<HistoricActivityInstance> activityInstances = historicActivityInstanceQuery.orderByHistoricActivityInstanceEndTime().desc().list();
        List<Object> datas = new ArrayList<>();
        for (HistoricActivityInstance activityInstance : activityInstances) {
            HistoricActivityInstanceEntityImpl entity = (HistoricActivityInstanceEntityImpl) activityInstance;

            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(entity.getTaskId()).singleResult();
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId())
                    .singleResult();

            HashMap<String, Object> map = new HashMap<>();
            map.put("historyProcess", HistoricActivityInstanceEntity.getPersistentState(entity));
            map.put("processName", processInstance != null ? processInstance.getProcessDefinitionName() : "");
            map.put("businessKey", processInstance != null ? processInstance.getBusinessKey() : "");
            datas.add(map);
        }
        resp.doSuccess("查询活动历史记录成功", datas);
        return resp;
    }

    @Override
    public ResponseInfo listHisTask(String processId) {
        return listHisTask(processId, null);
    }

    @Override
    public ResponseInfo listHisTask(String processId, String taskName) {
        return listHisTask(processId, taskName, null, null);
    }

    @Override
    public ResponseInfo listHisTask(String processId, String systemId, String userId) {
        return listHisTask(processId, systemId, userId, null);
    }

    @Override
    public ResponseInfo listHisTask(String processId, String systemId, String userId, String taskName) {
        ResponseInfo resp = new ResponseInfo();
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery().finished();

        if (StringUtils.isNotEmpty(processId)) {
            taskInstanceQuery.processInstanceId(processId);
        }

        if (StringUtils.isNotEmpty(taskName)) {
            taskInstanceQuery.taskNameLike(taskName);
        }

        String assignee = IdCombine.combineId(systemId, userId);
        if (StringUtils.isNotEmpty(assignee)) {
            taskInstanceQuery.taskAssignee(assignee);
        }

        List<HistoricTaskInstance> taskInstances = taskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc().list();
        List<Object> datas = new ArrayList<>();
        for (HistoricTaskInstance taskInstance : taskInstances) {
            HashMap<String, Object> map = new HashMap<>();
            HistoricTaskInstanceEntityImpl entity = (HistoricTaskInstanceEntityImpl) taskInstance;
            map.put("taskInfo", entity);
            datas.add(map);
        }
        resp.doSuccess("查询任务历史记录成功", datas);
        return resp;
    }

    @Override
    public ResponseInfo listHisTaskByAssignee(String systemId, String userId) {
        return listHisTask(null, systemId, userId);
    }

    @Override
    public ResponseInfo listHisVariable(String processId) {
        return listHisVariable(processId, null);
    }

    @Override
    public ResponseInfo listHisVariable(String processId, String taskId) {
        ResponseInfo resp = new ResponseInfo();
        HistoricVariableInstanceQuery variableInstanceQuery = historyService.createHistoricVariableInstanceQuery();
        if (StringUtils.isNotEmpty(processId)) {
            variableInstanceQuery.processInstanceId(processId);
        }

        if (StringUtils.isNotEmpty(taskId)) {
            variableInstanceQuery.taskId(taskId);
        }

        List<HistoricVariableInstance> variableInstances = variableInstanceQuery.orderByProcessInstanceId().desc().list();
        HashMap<String, Object> datas = new HashMap<>();
        for (HistoricVariableInstance variableInstance : variableInstances) {
            HistoricVariableInstanceEntityImpl entity = (HistoricVariableInstanceEntityImpl) variableInstance;
            datas.put(entity.getName(), entity.getTextValue());
        }
        resp.doSuccess("查询变量历史记录成功", datas);
        return resp;
    }

    @Override
    public ResponseInfo listHisVariableByTaskId(String taskId) {
        return listHisVariable(null, taskId);
    }
}
