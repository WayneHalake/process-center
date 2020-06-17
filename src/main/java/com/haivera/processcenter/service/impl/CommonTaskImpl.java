package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.GeneralCommonMap;
import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.common.util.ResponseUtil;
import com.haivera.processcenter.service.ICommonProcessSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommonTaskImpl implements ICommonTaskSer {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ICommonProcessSer commonProcessSer;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private GeneralCommonMap generalCommonMap;

    @Override
    public ResponseInfo completeTask(String taskId, HashMap<String, Object> variables, String systemId, String userId, boolean checkAssignee) throws Exception {
        return completeTask(taskId, variables, null, systemId, userId, checkAssignee);
    }

    @Override
    @Transactional
    public ResponseInfo completeTask(String taskId, HashMap<String, Object> variables, HashMap<String, Object> transientVariables, String systemId, String userId, boolean checkAssignee) throws Exception {
        ResponseInfo resp = new ResponseInfo();
        Task task = this.getTaskByTaskId(taskId);
        if (task == null) {
            resp.doFailed("任务不存在！");
            return resp;
        }
        //任务处理之前判断流程是否结束
        if (commonProcessSer.isFinishedProcess(task.getProcessInstanceId())) {
            resp.doFinish("流程已结束！");
            return resp;
        }

        //判断流程是否为子流程
        boolean subProcessFlag = commonProcessSer.isSubProcess(task.getProcessInstanceId());
        ResponseInfo subResp = new ResponseInfo();
        if(subProcessFlag){ //获取父流程信息
            subResp = commonProcessSer.getRootProcessByProcessId(task.getProcessInstanceId());
        }
        //校验流程处理人
        if (checkAssignee) {
            boolean checkAssignFlag = checkAssignee(taskId, systemId, userId);
            boolean checkOwnerFlag = checkOwner(taskId, systemId, userId);
            if (!checkAssignFlag && !checkOwnerFlag) {
                resp.doFailed("流程处理人错误！");
                return resp;
            }
        }else{//不校验处理人的情况 将userId设置为当前任务处理人
            setAssigner(taskId, IdCombine.combineId(systemId, userId));
        }

        taskService.complete(taskId, variables, transientVariables);

        //任务处理之后判断流程是否结束
        if (commonProcessSer.isFinishedProcess(task.getProcessInstanceId())) {
            if(subProcessFlag){//当前流程为子流程
                //获取父流程的当前任务节点
                HashMap<String, Object> tempData = (HashMap<String, Object>) subResp.getData();
                String rootProcessId = (String) tempData.get("processId");

                //判断父流程是否结束
                if(commonProcessSer.isFinishedProcess(rootProcessId)){
                    resp.doFinish("流程已结束！");
                    return resp;
                }

                //获取父流程中的当前任务节点（包含后续的子流程中的任务节点）
                ResponseInfo currentTask = currentTasks(rootProcessId);
                List<Object> tasks = (List<Object>) currentTask.getData();
                if(tasks == null || tasks.size() == 0){
                    resp.doFailed("查询流程的当前任务失败");
                    return resp;
                }
                HashMap<String, String> taskMap = (HashMap<String, String>) tasks.get(0);
                HashMap<String, Object> map = new HashMap<>();
                map.put("assignee", taskMap.get("assignee"));
                map.put("processId", taskMap.get("processId"));
                map.put("nextTaskId", taskMap.get("taskId"));
                resp.doSuccess("任务处理成功", map);
                return resp;
            }
            resp.doFinish("流程已结束！");
            return resp;
        }

        List<Task> nextTasks = currentTask(task.getProcessInstanceId());
        if (nextTasks.size() > 1) { //并行会签任务情况
            Map<String, Object> map = new HashMap<>();
            List<String> assigneeList = new ArrayList<>();
            List<String> nextTaskIds = new ArrayList<>();
            for(Task tempTask: nextTasks){
                assigneeList.add(tempTask.getAssignee());
                nextTaskIds.add(tempTask.getId());
            }
            map.put("assignees", assigneeList);
            map.put("nextTaskIds", nextTaskIds);
            map.put("processId", task.getProcessInstanceId());
            resp.doSuccess("任务处理成功！", map);
            return resp;
        }

        if(nextTasks == null || nextTasks.size() == 0){ //流程进入子流程的情况
            ResponseInfo subProcessInfo = commonProcessSer.getSubProcessByProcessId(task.getProcessInstanceId());

            List<HashMap<String, Object>> datas = (List<HashMap<String, Object>>) subProcessInfo.getData();
            HashMap<String, Object> data = datas.get(0);
            HashMap<String, Object> taskInfoMap = (HashMap<String, Object>) data.get("taskInfo");

            HashMap<String, Object> map = new HashMap();
            map.put("subProcessInfo", subProcessInfo.getData());
            map.put("assignee", taskInfoMap.get("assignee"));
            map.put("processId", data.get("processId"));
            map.put("nextTaskId", taskInfoMap.get("taskId"));
            resp.doSuccess("任务处理成功！当前流程在子流程中。", map);
            return resp;
        }

        String nextTaskId = nextTasks.get(0).getId();
        Map<String, String> map = new HashMap<>();
        map.put("assignee", nextTasks.get(0).getAssignee());
        map.put("nextTaskId", nextTaskId);
        map.put("processId", task.getProcessInstanceId());
        resp.doSuccess("任务处理成功！", map);
        return resp;
    }

    @Override
    public ResponseInfo completeTaskWithNextUserId(String taskId, HashMap<String, Object> variables, String systemId, String userId, String nextUserId, boolean checkAssignee) throws Exception {
        ResponseInfo resp = new ResponseInfo();
        resp = completeTask(taskId, variables, systemId, userId, checkAssignee);
        if (ResponseUtil.FAILED_CODE.equals(resp.getRtnCode())) {
            return resp;
        }

        Map<String, String> map = (Map<String, String>) resp.getData();
        if(map == null){
            return resp;
        }
        String nextTaskId = map.get("nextTaskId");
        if (nextTaskId == null) {
            return resp;
        }
        //设置下一任务处理人
        String assignee = IdCombine.combineId(systemId, nextUserId);
        if(StringUtils.isNotEmpty(assignee)){
            setAssigner(nextTaskId, assignee);
        }
        return resp;
    }


    @Override
    public ResponseInfo delegateTask(String taskId, String systemId, String userId) {
        ResponseInfo resp = new ResponseInfo();
        String assigner = IdCombine.combineId(systemId, userId);
        //判断委托人是否在任务候选处理人列表中
        if (!checkCandidateUser(taskId, systemId, userId)) {
            resp.doFailed("任务委托失败，任务委托人不在候选人列表中。");
            return resp;
        }
        taskService.delegateTask(taskId, assigner);
        resp.doSuccess("任务委托成功");
        return resp;
    }

    @Override
    public ResponseInfo resolveTask(String taskId, Map<String, Object> variables, String systemId, String userId) {
        return resolveTask(taskId, variables, null, systemId, userId);
    }

    @Override
    public ResponseInfo resolveTask(String taskId, Map<String, Object> variables, Map<String, Object> transientVariables, String systemId, String userId) {
        ResponseInfo resp = new ResponseInfo();
        Task task = getTaskByTaskId(taskId);
        DelegationState delegationState = task.getDelegationState();
        if (delegationState.equals("RESOLVED")) { //委托已处理
            resp.doFailed("委托已被处理");
            return resp;
        }
        if (!delegationState.equals("PENDING")) { //非委托任务
            resp.doFailed("非委托任务不能处理");
            return resp;
        }
        String assignee = task.getAssignee();
        if (!assignee.equals(IdCombine.combineId(systemId, userId))) {
            resp.doFailed("委托人不正确");
            return resp;
        }
        taskService.resolveTask(taskId, variables, transientVariables);
        return resp;
    }

    @Override
    public List<Task> currentTask(String processId) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processId).orderByTaskId().desc().list();
        return taskList;
    }

    @Override
    public ResponseInfo currentTasks(String processId) {
        ResponseInfo resp = new ResponseInfo();
        List<Object> datas = new ArrayList<>();

        List<Task> taskList = currentTask(processId);
        if(taskList == null || taskList.size() == 0){
            ResponseInfo subProcessInfo = commonProcessSer.getSubProcessByProcessId(processId);
            resp.doSuccess("当前流程在子流程中。", subProcessInfo.getData());
            return resp;
        }

        for (Task task : taskList) {
            datas.add(generalCommonMap.taskInfoMap(task));
        }
        resp.doSuccess("查询当前任务成功！", datas);
        return resp;
    }

    /**
     * ***注意***
     * 如果在网关之后存在多条SequenceFlow时，需要至少有一条SequenceFlow的name为通过
     * ***注意***
     */
    @Override
    public List<FlowElement> getNextTask(String taskId) throws Exception {
        Task countTask = getTaskByTaskId(taskId);
        ProcessInstance processInstance = commonProcessSer.getProcessByTaskId(taskId);

        //获取流程图--指定ProcessDefinitionId的情况一般只有一个
        List<Process> processList = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId()).getProcesses();
        if(processList.size() == 0){
            return null;
        }
        List<FlowElement> result = new ArrayList<>();
        //获取流程图中元素
        Process process = processList.get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        for (FlowElement flowElement : flowElements) {//循环流程图中的元素
            if (!(flowElement instanceof UserTask)) {
                continue;
            }
            if (!countTask.getName().equals(flowElement.getName())) { //当前任务节点
                continue;
            }
            //获取当前任务节点的下一个任务节点
            List<SequenceFlow> sequenceFlows = ((UserTask) flowElement).getOutgoingFlows();
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                String targetRef = sequenceFlow.getTargetRef();
                FlowElement ref = process.getFlowElement(targetRef);
                if (ref instanceof UserTask) { //任务
                    result.add(ref); //结果
                    continue;
                }
                if(ref instanceof ExclusiveGateway) { //网关
                    //获取网关的SequenceFlow
                    List<SequenceFlow> temps = ((ExclusiveGateway) ref).getOutgoingFlows();

                    /**
                     * 循环网关上的SequenceFlow
                     * 获取SequenceFlow的name为"通过"的flow之后的UserTask
                     * ***注意***
                     * 如果在网关之后存在多条SequenceFlow时，需要至少有一条SequenceFlow的name为通过
                     * ***注意***
                     */
                    for(SequenceFlow tempFlow : temps){
                        if(!tempFlow.getName().equals("通过")){
                            continue;
                        }
                        String targetRef1 = tempFlow.getTargetRef();
                        FlowElement ref1 = process.getFlowElement(targetRef1);
                        if(ref1 instanceof UserTask){
                            result.add(ref1);
                        }
                    }
                }
            }
            break;
        }
        return result;
    }

    @Override
    public ResponseInfo listTask(String processKey, String systemId, String userId) {
        ResponseInfo resp = new ResponseInfo();
        TaskQuery taskQuery = taskService.createTaskQuery().active();

        if(StringUtils.isNotEmpty(processKey)){
            taskQuery.processDefinitionKey(processKey);
        }

        String assigner = IdCombine.combineId(systemId, userId);
        if (StringUtils.isNotEmpty(assigner)) {
            taskQuery.taskAssignee(assigner);
        }
        List<Task> result = taskQuery.orderByTaskCreateTime().desc().list();

        List<Object> datas = new ArrayList<>();
        for (Task task : result) {
            ProcessInstance processInstance = commonProcessSer.getProcessByTaskId(task.getId());
            HashMap<String, Object> map = new HashMap<>();
            map.put("processId", processInstance.getId());
            map.put("processName", processInstance.getProcessDefinitionName());
            map.put("businessKey", processInstance.getBusinessKey());
            map.putAll(generalCommonMap.taskInfoMap(task));
            datas.add(map);
        }
        resp.doSuccess("获取任务信息成功！", datas);
        return resp;
    }

    @Override
    public ResponseInfo listTask(String systemId, String userId) {
        return listTask(null, systemId, userId);
    }

    @Override
    public List<Task> listRunTask(String processId) {
        return taskService.createTaskQuery().active().processInstanceId(processId).list();
    }

    @Override
    public Task getTaskByTaskId(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public void claimTask(String taskId, String assigner) {
        taskService.claim(taskId, assigner);
    }

    @Override
    public void setAssigner(String taskId, String assigner) {
        taskService.setAssignee(taskId, assigner);
    }

    @Override
    public void addCandidateUser(String taskId, String systemId, String userId) {
        String candidateUser = IdCombine.combineId(systemId, userId);
        taskService.addCandidateUser(taskId, candidateUser);
    }

    @Override
    public void addCandidateUser(String taskId, String systemId, List<String> userIds) {
        for (String userId : userIds) {
            addCandidateUser(taskId, systemId, userId);
        }
    }

    @Override
    public void addCandidateGroup(String taskId, String systemId, String groupId) {
        String candidateGroup = IdCombine.combineId(systemId, groupId);
        taskService.addCandidateGroup(taskId, candidateGroup);
    }

    @Override
    public void addCandidateGroup(String taskId, String systemId, List<String> groupIds) {
        for (String groupId : groupIds) {
            addCandidateGroup(taskId, systemId, groupId);
        }
    }

    @Override
    public List<IdentityLink> getIdentityLinksForTask(String taskId) {
        return taskService.getIdentityLinksForTask(taskId);
    }

    @Override
    public boolean checkAssignee(String taskId, String systemId, String userId) {
        String assignee = IdCombine.combineId(systemId, userId);
        Task task = getTaskByTaskId(taskId);
        if (task == null) {
            return false;
        }

        if (!assignee.equals(task.getAssignee())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCandidateUser(String taskId, String systemId, String userId) {
        String candidateUser = IdCombine.combineId(systemId, userId);
        Task task = getTaskByTaskId(taskId);
        if (task == null) {
            return false;
        }
        List<IdentityLink> identityLinks = getIdentityLinksForTask(taskId);
        for (IdentityLink identityLink : identityLinks) {
            if (candidateUser.equals(identityLink.getUserId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkOwner(String taskId, String systemId, String userId) {
        String owner = IdCombine.combineId(systemId, userId);
        Task task = getTaskByTaskId(taskId);
        if (task == null) {
            return false;
        }
        if (!owner.equals(task.getOwner())) {
            return false;
        }
        return true;
    }

}
