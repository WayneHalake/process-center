package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.service.ICommonTaskSer;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CommonTaskImpl implements ICommonTaskSer {

    @Autowired
    private TaskService taskService;

    @Override
    public String completeTask(String taskId, HashMap<String, Object> map, String systemId, String userId) {
        Task task = this.getTaskByTaskId(taskId);
        if(task != null){
            //设置任务处理人
            String assigner = IdCombine.combineId(systemId, userId);
            this.setAssigner(taskId, assigner);
            taskService.complete(taskId, map);
        }else{
            return "任务不存在！";
        }
        return this.currentTask(task.getProcessInstanceId()).getId();
    }

    @Override
    public Task currentTask(String processId) {
        return taskService.createTaskQuery().processInstanceId(processId).singleResult();
    }

    @Override
    public List<Task> listTask(String processKey, String systemId, String userId) {
        List<Task> result = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(processKey);

        String assigner = IdCombine.combineId(systemId, userId);
        if(StringUtils.isNotEmpty(assigner)){
            taskQuery.taskAssignee(assigner);
        }
        result = taskQuery.orderByTaskCreateTime().desc().list();
        return result;
    }

    @Override
    public List<Task> listRunTask(String processId) {
        return taskService.createTaskQuery().processInstanceId(processId).list();
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
    public void addCandidateUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
    }

    @Override
    public void addCandidateUser(String taskId, List<String> userIds) {
        for(String userId: userIds){
            addCandidateUser(taskId, userId);
        }
    }

    @Override
    public void addCandidateGroup(String taskId, String groupId) {
        taskService.addCandidateGroup(taskId, groupId);
    }

    @Override
    public void addCandidateGroup(String taskId, List<String> groupIds) {
        for(String groupId: groupIds){
            addCandidateGroup(taskId, groupId);
        }
    }
}
