package com.haivera.processcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.haivera.processcenter.common.GeneralCommonMap;
import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonProcessSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private ICommonProcessSer commonProcessSer;

    @Autowired
    private TaskService taskService;

    @Autowired
    private GeneralCommonMap generalCommonMap;
    /**
     * 获取流程当前任务节点
     * @param processId
     * @return
     */
    @ApiOperation(value = "流程实例所在节点", notes = "通过processId查找流程实例所在节点")
    @ApiImplicitParam(name = "processId", value = "流程实例processId", paramType="query", required = true, dataType = "string")
    @GetMapping("/currentTask")
    public ResponseInfo currentTask(String processId){
        ResponseInfo resp = new ResponseInfo();
        try {
            //判断流程是否结束
            if(commonProcessSer.isFinishedProcess(processId)){
                resp.doSuccess("流程已结束！");
            }
            List<Object> datas = new ArrayList<>();
            List<Task> taskList = commonTaskSer.currentTask(processId);

            for(Task task: taskList){
                TaskEntityImpl taskEntity = (TaskEntityImpl)task;
                datas.add(generalCommonMap.taskInfoMap(task.getId(), taskEntity.getPersistentState()));
            }
            resp.doSuccess("查询当前任务节点成功！", datas);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("查询当前任务节点失败！", e);
        }
        return resp;
    }

    @ApiOperation(value = "通过任务id获取任务", notes = "通过任务id获取任务")
    @ApiImplicitParam(name = "taskId", value = "任务id", paramType="query", required = true, dataType = "string")
    @GetMapping("/getTaskById")
    public ResponseInfo getTaskById(String taskId){
        ResponseInfo resp = new ResponseInfo();
        try {
            Task task = commonTaskSer.getTaskByTaskId(taskId);
            TaskEntityImpl taskEntity = (TaskEntityImpl) task;
            resp.doSuccess("获取任务成功", generalCommonMap.taskInfoMap(task.getId(), taskEntity.getPersistentState()));
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取任务失败", e);
        }
        return resp;
    }


    @ApiOperation(value = "设置任务处理人", notes = "设置任务处理人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType="query", required = true, dataType = "string")
    })
    @PostMapping("/setAssigner")
    public ResponseInfo setAssigner(@RequestParam String taskId, @RequestParam String systemId, @RequestParam String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            String assigner = IdCombine.combineId(systemId, userId);
            commonTaskSer.setAssigner(taskId, assigner);
            resp.doSuccess("设置任务处理人成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务处理人失败！", e);
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
                datas.add(generalCommonMap.taskInfoMap(task.getId(), taskEntity.getPersistentState()));
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
    @GetMapping("/listTaskByProcessKey")
    public ResponseInfo listTask(String processKey, String systemId, String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<Task> taskList = commonTaskSer.listTask(processKey, systemId, userId);
            List<Object> datas = new ArrayList<>();
            for(Task task: taskList){
                TaskEntityImpl taskEntity = (TaskEntityImpl)task;
                datas.add(generalCommonMap.taskInfoMap(task.getId(), taskEntity.getPersistentState()));
            }
            resp.doSuccess("获取当前用户的任务成功！",datas);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取当前用户的任务失败!", e);
        }
        return resp;
    }

    /**
     * 获取当前用户所有任务
     * @param systemId
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取当前用户所有任务", notes = "获取当前用户所有任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType="query", required = true, dataType = "string")
    })
    @GetMapping("/listTaskByUserId")
    public ResponseInfo listTask(String systemId, String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<Task> taskList = commonTaskSer.listTask(systemId, userId);
            List<Object> datas = new ArrayList<>();
            for(Task task: taskList){
                TaskEntityImpl taskEntity = (TaskEntityImpl)task;
                datas.add(generalCommonMap.taskInfoMap(task.getId(), taskEntity.getPersistentState()));
            }
            resp.doSuccess("获取当前用户的任务成功！",datas);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取当前用户的任务失败!", e);
        }
        return resp;
    }

    /**
     * 完成一个任务(非委托任务)
     * @param taskId
     * @param varAndTransientVar 任务所需参数 --包括两部分variables、transientVariables
     * @param userId 用户id
     * @param systemId 系统id
     * @return
     */
    @ApiOperation(value = "完成一个任务(非委托任务)", notes = "完成一个任务(非委托任务)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "varAndTransientVar", value = "任务所需参数", paramType="body", dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType="query", required = true, dataType = "string")
    })
    @PostMapping("/complete")
    public ResponseInfo completeTask(@RequestParam String taskId,
                               @RequestBody JSONObject varAndTransientVar,
                               @RequestParam String systemId,
                               @RequestParam String userId){
        ResponseInfo resp = new ResponseInfo();
        String nextTaskId = "";
        try{
            JSONObject variables = varAndTransientVar.getJSONObject("variables");
            JSONObject transientVariables = varAndTransientVar.getJSONObject("transientVariables");
            HashMap<String, Object> variablesMap = null;
            if(variables != null){
                variablesMap = JSONObject.parseObject(variables.toJSONString(), HashMap.class);
            }
            HashMap<String, Object> transientVariablesMap = null;
            if(transientVariables != null){
                transientVariablesMap = JSONObject.parseObject(transientVariables.toJSONString(), HashMap.class);
            }
            nextTaskId = commonTaskSer.completeTask(taskId, variablesMap, transientVariablesMap, systemId, userId);
            resp.doSuccess("任务已处理", nextTaskId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("任务处理失败");
        }
        return resp;
    }

    @ApiOperation(value = "设置任务委托人", notes = "设置任务委托人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType="query", required = true, dataType = "string")
    })
    @PostMapping("/delegateTask")
    public ResponseInfo delegateTask(@RequestParam String taskId, @RequestParam String systemId, @RequestParam String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = commonTaskSer.delegateTask(taskId, systemId, userId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务委托人失败！");
        }
        return resp;
    }

    @ApiOperation(value = "处理委托任务", notes = "处理委托任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "varAndTransientVar", value = "任务所需参数", paramType="body", dataType = "string"),
    })
    @PostMapping("/resolveTask")
    public ResponseInfo resolveTask(@RequestParam String taskId,
                                    @RequestBody JSONObject varAndTransientVar
                                    ){
        ResponseInfo resp = new ResponseInfo();
        try{
            JSONObject variables = varAndTransientVar.getJSONObject("variables");
            JSONObject transientVariables = varAndTransientVar.getJSONObject("transientVariables");
            HashMap<String, Object> variablesMap = null;
            if(variables != null){
                variablesMap = JSONObject.parseObject(variables.toJSONString(), HashMap.class);
            }
            HashMap<String, Object> transientVariablesMap = null;
            if(transientVariables != null){
                transientVariablesMap = JSONObject.parseObject(transientVariables.toJSONString(), HashMap.class);
            }
            commonTaskSer.resolveTask(taskId, variablesMap, transientVariablesMap);
            resp.doSuccess("处理委托任务成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("处理委托任务失败！");
        }
        return resp;
    }

    @ApiOperation(value = "设置任务候选处理人", notes = "设置候选任务处理人，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType="query", required = true, dataType = "string")
    })
    @PostMapping("/addCandidateUser")
    public ResponseInfo addCandidateUser(@RequestParam String taskId, @RequestParam String systemId, @RequestParam String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            commonTaskSer.addCandidateUser(taskId, systemId, userId);
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
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userIds", value = "用户id列表", paramType="query", required = true)
    })
    @PostMapping("/addCandidateUsers")
    public ResponseInfo addCandidateUser(@RequestParam String taskId, @RequestParam String systemId, @RequestParam List<String> userIds){
        ResponseInfo resp = new ResponseInfo();
        try{
            commonTaskSer.addCandidateUser(taskId, systemId, userIds);
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
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "groupId", value = "用户组id", paramType="query", required = true, dataType = "string")
    })
    @PostMapping("/addCandidateGroup")
    public ResponseInfo addCandidateGroup(@RequestParam String taskId, @RequestParam String systemId, @RequestParam String groupId){
        ResponseInfo resp = new ResponseInfo();
        try{
            commonTaskSer.addCandidateGroup(taskId, systemId, groupId);
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
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "groupIds", value = "用户组id列表", paramType="query", required = true)
    })
    @PostMapping("/addCandidateGroups")
    public ResponseInfo addCandidateGroup(@RequestParam String taskId, @RequestParam String systemId, @RequestParam List<String> groupIds){
        ResponseInfo resp = new ResponseInfo();
        try{
            commonTaskSer.addCandidateGroup(taskId, systemId, groupIds);
            resp.doSuccess("设置任务候选用户组列表成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务候选用户组列表失败！");
        }
        return resp;
    }

    @ApiOperation(value = "获取任务处理候选用户", notes = "获取任务处理候选用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string"),
    })
    @GetMapping("/getIdentityLinksForTask")
    public ResponseInfo getIdentityLinksForTask(String taskId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<IdentityLink> identityList = commonTaskSer.getIdentityLinksForTask(taskId);
            List<Object> result = new ArrayList<>();
            for(IdentityLink identityLink: identityList){
                IdentityLinkEntityImpl identityLinkEntity = (IdentityLinkEntityImpl) identityLink;
                result.add(identityLinkEntity.getPersistentState());
            }
            resp.doSuccess("获取任务处理候选用户成功！", result);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取任务处理候选用户失败！", e);
        }
        return resp;
    }
}
