package com.haivera.processcenter.controller;

import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonTaskSer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通用的task接口
 */
@Api("流程操作接口")
@RestController
@RequestMapping("/taskCtr")
public class CommonTaskCtr {
    @Autowired
    private ICommonTaskSer commonTaskSer;

    /**
     * 获取流程当前任务节点
     * @param processId
     * @return
     */
    @ApiOperation(value = "流程实例所在节点--返回单个节点", notes = "通过processId查找流程实例所在节点")
    @ApiImplicitParam(name = "processId", value = "流程实例processId", paramType="query", required = true, dataType = "string")
    @GetMapping("/currentTask")
    public ResponseInfo currentTask(String processId){
        ResponseInfo resp = new ResponseInfo();
        try {
            Task task = commonTaskSer.currentTask(processId);
            TaskEntityImpl taskEntity = (TaskEntityImpl) task;
            resp.doSuccess("查询当前任务节点成功！", taskEntity.getPersistentState());
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("查询当前任务节点失败！", e);
        }
        return resp;
    }

    /**
     * 获取流程当前任务列表
     * @param processId
     * @return
     */
    @ApiOperation(value = "流程实例所在节点--返回多个节点", notes = "通过processId查找流程实例所在节点")
    @ApiImplicitParam(name = "processId", value = "流程实例processId", paramType="query", required = true, dataType = "string")
    @GetMapping("/listRunTask")
    public ResponseInfo listRunTask(String processId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<Object> datas = new ArrayList<>();
            List<Task> taskList = commonTaskSer.listRunTask(processId);
            for(Task task: taskList){
                TaskEntityImpl taskEntity = (TaskEntityImpl)task;
                datas.add(taskEntity.getPersistentState());
            }
            resp.doSuccess("获取流程当前任务列表成功！", datas);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程当前任务列表失败！", e);
        }
        return resp;
    }

    /**
     * 获取当前用户在processkey流程中任务
     * @param processKey
     * @param systemId
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取当前用户在processkey流程中任务", notes = "获取当前用户在processkey流程中任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程processKey", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType="query", required = true, dataType = "string")
    })
    @GetMapping("/listTask")
    public ResponseInfo listTask(String processKey, String systemId, String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<Task> taskList = commonTaskSer.listTask(processKey, systemId, userId);
            List<Object> datas = new ArrayList<>();
            for(Task task: taskList){
                TaskEntityImpl taskEntity = (TaskEntityImpl)task;
                datas.add(taskEntity.getPersistentState());
            }
            resp.doSuccess("获取当前用户的任务成功！",datas);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取当前用户的任务失败!", e);
        }
        return resp;
    }


    /**
     * 完成一个任务
     * @param taskId
     * @param map 任务所需参数
     * @param userId 用户id
     * @param systemId 系统id
     * @return
     */
    @ApiOperation(value = "完成一个任务", notes = "完成一个任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "map", value = "任务所需参数", paramType="query", dataType = "HashMap"),
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType="query", required = true, dataType = "string")
    })
    @PostMapping("/complete")
    public String completeTask(String taskId, HashMap map, String systemId, String userId){
        String nextTaskId = "";
        try{
            nextTaskId = commonTaskSer.completeTask(taskId, map, systemId, userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return nextTaskId;
    }

    @ApiOperation(value = "设置任务候选处理人", notes = "设置候选任务处理人，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType="query", required = true, dataType = "string")
    })
    @PostMapping("/addCandidateUser")
    public ResponseInfo addCandidateUser(String taskId, String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            commonTaskSer.addCandidateUser(taskId, userId);
            resp.doSuccess("设置任务候选处理人成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务候选处理人失败！");
        }
        return resp;
    }

    @ApiOperation(value = "设置任务候选处理人列表", notes = "设置候选任务处理人，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userIds", value = "用户id列表", paramType="query", required = true)
    })
    @PostMapping("/addCandidateUsers")
    public ResponseInfo addCandidateUser(String taskId, List<String> userIds){
        ResponseInfo resp = new ResponseInfo();
        try{
            commonTaskSer.addCandidateUser(taskId, userIds);
            resp.doSuccess("设置任务候选处理人列表成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务候选处理人列表失败！");
        }
        return resp;
    }

    @ApiOperation(value = "设置任务候选用户组", notes = "设置候选任务用户组，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "groupId", value = "用户组id", paramType="query", required = true, dataType = "string")
    })
    @PostMapping("/addCandidateGroup")
    public ResponseInfo addCandidateGroup(String taskId, String groupId){
        ResponseInfo resp = new ResponseInfo();
        try{
            commonTaskSer.addCandidateGroup(taskId, groupId);
            resp.doSuccess("设置任务候选用户组成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务候选用户组失败！");
        }
        return resp;
    }

    @ApiOperation(value = "设置任务候选用户组列表", notes = "设置候选任务用户组，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "groupIds", value = "用户组id列表", paramType="query", required = true)
    })
    @PostMapping("/addCandidateGroups")
    public ResponseInfo addCandidateGroup(String taskId, List<String> groupIds){
        ResponseInfo resp = new ResponseInfo();
        try{
            commonTaskSer.addCandidateGroup(taskId, groupIds);
            resp.doSuccess("设置任务候选用户组列表成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务候选用户组列表失败！");
        }
        return resp;
    }
}
