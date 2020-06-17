package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.GeneralCommonMap;
import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonStatisSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CommonStatisImpl implements ICommonStatisSer {

    @Autowired
    private ICommonTaskSer commonTaskSer;

    @Autowired
    private TaskService taskService;

    @Autowired
    private GeneralCommonMap generalCommonMap;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public ResponseInfo countUpTodoTask(String systemId, String userId) {
        return countUpTodoTask(systemId, userId, null);
    }

    @Override
    public ResponseInfo countUpTodoTask(String systemId, String userId, String processKey) {
        ResponseInfo resp = new ResponseInfo();
        TaskQuery taskQuery = taskService.createTaskQuery().active();

        String assignee = IdCombine.combineId(systemId, userId);
        if(StringUtils.isNotEmpty(assignee)){
            taskQuery.taskAssignee(assignee);
        }

        if(StringUtils.isNotEmpty(processKey)){
            taskQuery.processDefinitionKey(processKey);
        }
        List<Task> taskList = taskQuery.orderByTaskName().asc().list();
        HashMap<String, Long> taskCount = new HashMap<>();//<任务名称，数量>
        HashMap<String, List<Object>> taskInfoMap = new HashMap<>(); //<任务名称，任务信息>  --> 任务信息：[任务详细信息]
        for(Task task: taskList){
            List<Object> taskInfos = new ArrayList<>();
            if(taskCount.containsKey(task.getName())){
                taskCount.put(task.getName(), taskCount.get(task.getName()) + 1);
                taskInfos = taskInfoMap.get(task.getName());
            }else{
                taskCount.put(task.getName(), 1L);
                taskInfos = new ArrayList<>();
            }
            taskInfos.add(generalCommonMap.taskInfoMap(task));
            taskInfoMap.put(task.getName(), taskInfos);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("taskCount", taskCount);
        result.put("taskInfoMap", taskInfoMap);
        resp.doSuccess("获取待处理任务成功！", result);
        return resp;
    }

    @Override
    public ResponseInfo countUpDoneTask(String systemId, String userId) {
        return countUpTodoTask(systemId, userId, null);
    }

    @Override
    public ResponseInfo countUpDoneTask(String systemId, String userId, String processKey) {
        ResponseInfo resp = new ResponseInfo();
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery().finished();

        String assignee = IdCombine.combineId(systemId, userId);
        if(StringUtils.isNotEmpty(assignee)){
            taskInstanceQuery.taskAssignee(assignee);
        }

        if(StringUtils.isNotEmpty(processKey)){
            taskInstanceQuery.processDefinitionKey(processKey);
        }

        List<HistoricTaskInstance> taskInstanceList = taskInstanceQuery.orderByTaskName().asc().list();
        HashMap<String, Long> hisTaskCount = new HashMap<>();//<任务名称，数量>
        HashMap<String, List<Object>> hisTaskInfoMap = new HashMap<>();    //<任务名称，任务信息>  --> 任务信息：[任务详细信息]
        for(HistoricTaskInstance historicTaskInstance: taskInstanceList){
            if(hisTaskCount.containsKey(historicTaskInstance.getName())){
                hisTaskCount.put(historicTaskInstance.getName(), hisTaskCount.get(historicTaskInstance.getName() + 1));
                List<Object> hisTaskInfos = hisTaskInfoMap.get(historicTaskInstance.getName());
                hisTaskInfos.add(generalCommonMap.hisTaskInfoMap(historicTaskInstance));
                hisTaskInfoMap.put(historicTaskInstance.getName(), hisTaskInfos);
            }else{
                hisTaskCount.put(historicTaskInstance.getName(), 1l);
                List<Object> hisTaskInfos = new ArrayList<>();
                hisTaskInfos.add(generalCommonMap.hisTaskInfoMap(historicTaskInstance));
                hisTaskInfoMap.put(historicTaskInstance.getName(), hisTaskInfos);
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("hisTaskCount", hisTaskCount);
        result.put("hisTaskInfoMap", hisTaskInfoMap);
        resp.doSuccess("获取已处理任务成功！", result);
        return resp;
    }

    /**
     * 我发起的 正在运行的流程
     * @param systemId
     * @param userId
     * @return
     */
    @Override
    public ResponseInfo countUpActProcess(String systemId, String userId) {
        ResponseInfo resp = new ResponseInfo();
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        String assignee = IdCombine.combineId(systemId, userId);
        if(StringUtils.isNotEmpty(assignee) && StringUtils.isNotEmpty(userId)){
            processInstanceQuery.startedBy(assignee);
        }

        List<ProcessInstance> processInstances = processInstanceQuery.orderByProcessDefinitionKey().asc().list();
        HashMap<String, Long> countProcess = new HashMap<>();
        HashMap<String, List<Object>> processInfoMap = new HashMap<>();
        for(ProcessInstance processInstance: processInstances){
            if(countProcess.containsKey(processInstance.getName())){
                countProcess.put(processInstance.getName(), countProcess.get(processInstance.getName()) + 1);
                List<Object> processInfos = processInfoMap.get(processInstance.getName());
                processInfos.add(generalCommonMap.processInfoMap(processInstance));
                processInfoMap.put(processInstance.getName(), processInfos);
            }else{
                countProcess.put(processInstance.getName(), 1l);
                List<Object> processInfos = new ArrayList<>();
                processInfos.add(generalCommonMap.processInfoMap(processInstance));
                processInfoMap.put(processInstance.getName(), processInfos);
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("countProcess", countProcess);
        result.put("processInfoMap", processInfoMap);
        resp.doSuccess("获取正在云心的流程成功！", result);
        return resp;
    }

    /**
     * 我发起的 已结束的流程
     * @param systemId
     * @param userId
     * @return
     */
    @Override
    public ResponseInfo countUpFinishProcess(String systemId, String userId) {
        ResponseInfo resp = new ResponseInfo();
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().finished();
        String assignee = IdCombine.combineId(systemId, userId);
        if(StringUtils.isNotEmpty(assignee) && StringUtils.isNotEmpty(userId)){
            historicProcessInstanceQuery.startedBy(assignee);
        }
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.orderByProcessInstanceId().asc().list();
        HashMap<String, Long> countHisProcess = new HashMap<>();
        HashMap<String, List<Object>> hisProcessInfoMap = new HashMap<>();
        for (HistoricProcessInstance historicProcessInstance: historicProcessInstances){
            if(countHisProcess.containsKey(historicProcessInstance.getName())){
                countHisProcess.put(historicProcessInstance.getName(), countHisProcess.get(historicProcessInstance.getName()) + 1);
                List<Object> hisProcessInfos = hisProcessInfoMap.get(historicProcessInstance.getName());
                hisProcessInfos.add(generalCommonMap.hisProcessInfoMap(historicProcessInstance));
                hisProcessInfoMap.put(historicProcessInstance.getName(), hisProcessInfos);
            }else{
                countHisProcess.put(historicProcessInstance.getName(), 1l);
                List<Object> hisProcessInfos = new ArrayList<>();
                hisProcessInfos.add(generalCommonMap.hisProcessInfoMap(historicProcessInstance));
                hisProcessInfoMap.put(historicProcessInstance.getName(), hisProcessInfos);
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("countHisProcess", countHisProcess);
        result.put("hisProcessInfoMap", hisProcessInfoMap);
        resp.doSuccess("获取正在云心的流程成功！", result);
        return resp;
    }

    @Override
    public ResponseInfo countUpAllActProcess(String systemId) {
        return null;
    }

    @Override
    public ResponseInfo countUpAllFinishProcess(String systemId) {
        return null;
    }
}
