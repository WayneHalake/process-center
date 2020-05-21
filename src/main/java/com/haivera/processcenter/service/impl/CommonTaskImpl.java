package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonProcessSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import org.activiti.engine.*;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public String completeTask(String taskId, HashMap<String, Object> variables, String systemId, String userId) throws Exception {
        return completeTask(taskId, variables, null, systemId, userId);
    }

    @Override
    public String completeTask(String taskId, HashMap<String, Object> variables, HashMap<String, Object> transientVariables, String systemId, String userId) throws Exception {
        Task task = this.getTaskByTaskId(taskId);
        if(task == null){
            return "任务不存在！";
        }
        //判断流程是否结束
        if(commonProcessSer.isFinishedProcess(task.getProcessInstanceId())){
            return "流程已结束";
        }

        //校验流程处理人
        boolean checkAssignFlag = checkAssignee(taskId, systemId, userId);
        boolean checkOwnerFlag = checkOwner(taskId, systemId, userId);
        if(!checkAssignFlag && !checkOwnerFlag){
          return "流程处理人错误！";
        }

        taskService.complete(taskId, variables, transientVariables);

        List<Task> nextTasks = currentTask(task.getProcessInstanceId());
        if(nextTasks.size() > 1){ //并行会签任务情况
            return "";
        }
        return nextTasks.get(0).getId();
    }

    @Override
    public ResponseInfo delegateTask(String taskId, String systemId, String userId) {
        ResponseInfo resp = new ResponseInfo();
        String assigner = IdCombine.combineId(systemId, userId);
        //判断委托人是否在任务候选处理人列表中
        if(!checkCandidateUser(taskId, systemId, userId)){
            resp.doFailed("任务委托失败，任务委托人不在候选人列表中。");
            return resp;
        }
        taskService.delegateTask(taskId, assigner);
        resp.doSuccess("任务委托成功");
        return resp;
    }

    @Override
    public void resolveTask(String taskId, Map<String, Object> variables) {
        resolveTask(taskId, variables, null);
    }

    @Override
    public void resolveTask(String taskId, Map<String, Object> variables, Map<String, Object> transientVariables) {
        Task task = getTaskByTaskId(taskId);
        DelegationState delegationState = task.getDelegationState();
        if(delegationState.equals("RESOLVED")){ //委托已处理
            return;
        }else if(delegationState.equals("PENDING")){ //委托未处理
            taskService.resolveTask(taskId, variables, transientVariables);
        }
    }

    @Override
    public List<Task> currentTask(String processId) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processId).list();
        return taskList;
    }

    @Override
    public List<Task> listTask(String processKey, String systemId, String userId) {
        List<Task> result = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(processKey).active();

        String assigner = IdCombine.combineId(systemId, userId);
        if(StringUtils.isNotEmpty(assigner)){
            taskQuery.taskAssignee(assigner);
        }
        result = taskQuery.orderByTaskCreateTime().desc().list();
        return result;
    }

    @Override
    public List<Task> listTask(String systemId, String userId) {
        List<Task> result = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery().active();

        String assigner = IdCombine.combineId(systemId, userId);
        if(StringUtils.isNotEmpty(assigner)){
            taskQuery.taskAssignee(assigner);
        }
        result = taskQuery.orderByTaskCreateTime().desc().list();
        return result;
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
        for(String userId: userIds){
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
        for(String groupId: groupIds){
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
        if(task == null){
            return false;
        }

        if(!assignee.equals(task.getAssignee())){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCandidateUser(String taskId, String systemId, String userId) {
        String candidateUser = IdCombine.combineId(systemId, userId);
        Task task = getTaskByTaskId(taskId);
        if(task == null){
            return false;
        }
        List<IdentityLink> identityLinks = getIdentityLinksForTask(taskId);
        for(IdentityLink identityLink: identityLinks){
            if(candidateUser.equals(identityLink.getUserId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkOwner(String taskId, String systemId, String userId) {
        String owner = IdCombine.combineId(systemId, userId);
        Task task = getTaskByTaskId(taskId);
        if(task == null){
            return false;
        }
        if(!owner.equals(task.getOwner())){
            return false;
        }
        return true;
    }

}
