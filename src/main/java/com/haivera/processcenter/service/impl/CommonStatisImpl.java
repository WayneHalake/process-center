package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.GeneralCommonMap;
import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonProcessSer;
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

    @Autowired
    private ICommonProcessSer commonProcessSer;

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
        //todo 分页相关参数后续修改
        List<Task> taskList = taskQuery.orderByTaskCreateTime().desc().listPage(0, 6);
        HashMap<String, Long> taskCount = new HashMap<>();//<任务名称，数量>
        HashMap<String, List<Object>> taskInfoMap = new HashMap<>(); //<任务名称，任务信息>  --> 任务信息：[任务详细信息]
        List<Object> taskInfoList = new ArrayList<>();
        for(Task task: taskList){
            List<Object> taskInfos = new ArrayList<>();
            if(taskCount.containsKey(task.getName())){
                taskCount.put(task.getName(), taskCount.get(task.getName()) + 1);
                taskInfos = taskInfoMap.get(task.getName());
            }else{
                taskCount.put(task.getName(), 1L);
                taskInfos = new ArrayList<>();
            }
            HashMap<String, Object> map = generalCommonMap.taskInfoMap(task);
            taskInfos.add(map);
            taskInfoList.add(map);
            taskInfoMap.put(task.getName(), taskInfos);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("taskCount", taskCount);
        result.put("taskInfoMap", taskInfoMap);
        result.put("taskInfoList", taskInfoList);
        resp.doSuccess("获取待处理任务成功！", result);
        return resp;
    }

    @Override
    public ResponseInfo countUpDoneTask(String systemId, String userId) {
        return countUpDoneTask(systemId, userId, null);
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

        //todo 分页相关参数后续修改
        List<HistoricTaskInstance> taskInstanceList = taskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc().listPage(0, 6);
        HashMap<String, Long> hisTaskCount = new HashMap<>();//<任务名称，数量>
        HashMap<String, List<Object>> hisTaskInfoMap = new HashMap<>();    //<任务名称，任务信息>  --> 任务信息：[任务详细信息]
        List<Object> hisTaskInfoList = new ArrayList<>();
        for(HistoricTaskInstance historicTaskInstance: taskInstanceList){
            List<Object> hisTaskInfos = null;
            if(hisTaskCount.containsKey(historicTaskInstance.getName())){
                hisTaskCount.put(historicTaskInstance.getName(), hisTaskCount.get(historicTaskInstance.getName() + 1));
                hisTaskInfos = hisTaskInfoMap.get(historicTaskInstance.getName());
            }else{
                hisTaskCount.put(historicTaskInstance.getName(), 1l);
                hisTaskInfos = new ArrayList<>();
            }
            HashMap<String, Object> taskInfoMap = generalCommonMap.hisTaskInfoMap(historicTaskInstance);
            hisTaskInfoList.add(taskInfoMap);
            hisTaskInfos.add(taskInfoMap);
            hisTaskInfoMap.put(historicTaskInstance.getName(), hisTaskInfos);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("hisTaskCount", hisTaskCount);
        result.put("hisTaskInfoMap", hisTaskInfoMap);
        result.put("hisTaskInfoList", hisTaskInfoList);
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

        //todo 分页相关参数后续修改
        List<ProcessInstance> processInstances = processInstanceQuery.orderByProcessInstanceId().desc().listPage(0, 6);
        HashMap<String, Long> countProcess = new HashMap<>();
        HashMap<String, List<Object>> processInfoMap = new HashMap<>();
        List<Object> processInfoList = new ArrayList<>();
        for(ProcessInstance processInstance: processInstances){
            //如果流程是子流程则不计入
            if(commonProcessSer.isSubProcess(processInstance.getId())){
                continue;
            }

            String processName = processInstance.getName() == null ? "": processInstance.getName();
            List<Object> processInfos = null;
            if(countProcess.containsKey(processName)){
                countProcess.put(processName, countProcess.get(processName) + 1);
                processInfos = processInfoMap.get(processName);
            }else{
                countProcess.put(processName, 1l);
                processInfos = new ArrayList<>();
            }
            HashMap<String, Object> map = generalCommonMap.processInfoMap(processInstance);
            processInfos.add(map);
            processInfoMap.put(processName, processInfos);
            processInfoList.add(map);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("countProcess", countProcess);
        result.put("processInfoMap", processInfoMap);
        result.put("processInfoList", processInfoList);
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
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.orderByProcessInstanceId().desc().list();
        HashMap<String, Long> countHisProcess = new HashMap<>();
        HashMap<String, List<Object>> hisProcessInfoMap = new HashMap<>();
        List<Object> hisProcessInfoList = new ArrayList<>();
        for (HistoricProcessInstance historicProcessInstance: historicProcessInstances){
            List<Object> hisProcessInfos = null;
            if(countHisProcess.containsKey(historicProcessInstance.getName())){
                countHisProcess.put(historicProcessInstance.getName(), countHisProcess.get(historicProcessInstance.getName()) + 1);
                hisProcessInfos = hisProcessInfoMap.get(historicProcessInstance.getName());
            }else{
                countHisProcess.put(historicProcessInstance.getName(), 1l);
                hisProcessInfos = new ArrayList<>();
            }
            HashMap<String, Object> map = generalCommonMap.hisProcessInfoMap(historicProcessInstance);
            hisProcessInfoList.add(map);
            hisProcessInfos.add(map);
            hisProcessInfoMap.put(historicProcessInstance.getName(), hisProcessInfos);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("countHisProcess", countHisProcess);
        result.put("hisProcessInfoMap", hisProcessInfoMap);
        result.put("hisProcessInfoList", hisProcessInfoList);
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
