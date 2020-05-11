package com.haivera.processcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonProcessSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通用的process接口
 */
@Api("流程操作接口")
@RestController
@RequestMapping("/processCtr")
public class CommonProcessCtr {

    @Autowired
    private ICommonProcessSer commonProcessSer;

    @Autowired
    private ICommonTaskSer commonTaskSer;

    /**
     * 创建流程实例
     * @param processKey
     * @return
     */
    @ApiOperation(value = "创建流程", notes = "通过processKey创建流程")
    @ApiImplicitParam(name = "processKey", value = "流程图processKey", paramType="query", required = true, dataType = "string")
    @PostMapping("/createPro")
    public String createPro(@RequestParam("processKey") String processKey){
        String processId = "";
        try{
            processId = commonProcessSer.StartAndCreateProcess(processKey);
        }catch (Exception e){
            e.printStackTrace();
        }
        return processId;
    }

    /**
     * 流程实例列表
     * @param processKey
     * @return
     */
    @ApiOperation(value = "流程实例列表", notes = "通过processKey查找流程实例列表")
    @ApiImplicitParam(name = "processKey", value = "流程图processKey", paramType="query", required = true, dataType = "string")
    @GetMapping("/listProcess")
    public ResponseInfo listProcess(String processKey){
        ResponseInfo responseInfo = new ResponseInfo();
        List<Object> datas = new ArrayList<>();
        try{
            List<ProcessInstance> processInstances = commonProcessSer.listProcessInstance(processKey);
            for(ProcessInstance processInstance: processInstances){
                ExecutionEntityImpl executionEntity = (ExecutionEntityImpl)processInstance;
                datas.add(executionEntity.getPersistentState());
            }
            responseInfo.doSuccess("流程实例列表查询成功！", datas);
        }catch (Exception e){
            e.printStackTrace();
            responseInfo.doFailed("流程实例列表查询失败！", e);
        }
        return responseInfo;
    }

    /**
     * 获取所有的流程实例
     * @return
     */
    @ApiOperation(value = "获取所有正在运行的流程实例", notes = "获取所有正在运行的流程实例")
    @GetMapping("/listAllProcessInstance")
    public ResponseInfo listAllProcessInstance(){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<Object> datas = new ArrayList<>();
            List<ProcessInstance> result = commonProcessSer.listAllProcessInstance();
            for(ProcessInstance processInstance: result){
                ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) processInstance;
                datas.add(executionEntity.getPersistentState());
            }
            resp.doSuccess("获取所有流程实例成功！", datas);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取所有流程实例成功！", e);
        }
        return resp;
    }

    /**
     * 挂起流程
     * @param processId
     */
    @ApiOperation(value = "挂起流程", notes = "通过流程实例processId挂起流程")
    @ApiImplicitParam(name = "processId", value = "流程实例processId", paramType="query", required = true, dataType = "string")
    @PostMapping("/suspendProcess")
    public void suspendProcess(String processId){
        try{
            commonProcessSer.suspendProcess(processId);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 启动挂起的流程
     * @param processId
     */
    @ApiOperation(value = "启动挂起的流程", notes = "通过流程实例processId启动挂起的流程")
    @ApiImplicitParam(name = "processId", value = "流程实例processId", paramType="query", required = true, dataType = "string")
    @PostMapping("/activateProcess")
    public void activateProcess(String processId){
        try{
            commonProcessSer.activateProcess(processId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过流程id获取流程实例
     * @param processId
     * @return
     */
    @ApiOperation(value = "通过流程id获取流程实例", notes = "通过流程流程实例processId获取流程实例")
    @ApiImplicitParam(name = "processId", value = "流程实例processId", paramType="query", required = true, dataType = "string")
    @GetMapping("/getProcessByProcessId")
    public ResponseInfo getProcessByProcessId(String processId){
        ResponseInfo resp = new ResponseInfo();
        try{
            ProcessInstance result = commonProcessSer.getProcessByProcessId(processId);
            ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) result;
            resp.doSuccess("获取流程实例成功！", executionEntity.getPersistentState());
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程实例失败！", e);
        }
        return resp;
    }

    /**
     * 通过任务id获取流程实例
     * @param taskId
     * @return
     */
    @ApiOperation(value = "通过任务id获取流程实例", notes = "通过任务taskId获取流程实例")
    @ApiImplicitParam(name = "taskId", value = "任务taskId", paramType="query", required = true, dataType = "string")
    @GetMapping("/getProcessByTaskId")
    public ResponseInfo getProcessByTaskId(String taskId){
        ResponseInfo resp = new ResponseInfo();
        try{
            ProcessInstance result = commonProcessSer.getProcessByTaskId(taskId);
            ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) result;
            resp.doSuccess("获取流程实例成功！", executionEntity.getPersistentState());
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程实例失败！", e);
        }
        return resp;
    }

    /**
     * 获取流程图
     * @param response
     * @param processId 流程id
     * @param lightFlag 是否高亮当前任务节点
     * @return
     */

    @ApiOperation(value = "获取流程图", notes = "获取流程图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例processId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "lightFlag", value = "是否高亮当前任务节点", paramType="query", required = true, dataType = "boolean"),
    })
    @GetMapping("/getImage")
    public void getImage(HttpServletResponse response, String processId, boolean lightFlag){
        try{
            commonProcessSer.getWorkFlowImage(response, processId, lightFlag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取流程的历史记录
     * @param processId
     * @return
     */
    @ApiOperation(value = "获取流程的历史记录", notes = "获取流程的历史记录")
    @ApiImplicitParam(name = "processId", value = "流程实例processId", paramType="query", required = true, dataType = "string")
    @GetMapping("/listHistory")
    public ResponseInfo listHistory(String processId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<HistoricActivityInstance> result = commonProcessSer.listHistory(processId);
            List<Object> datas = new ArrayList<>();
            for(HistoricActivityInstance historicActivityInstance : result){
                HistoricActivityInstanceEntityImpl entity = (HistoricActivityInstanceEntityImpl) historicActivityInstance;
                datas.add(entity.getPersistentState());
            }
            resp.doSuccess("获取流程历史记录成功！", datas);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程历史记录失败！", e);
        }
        return resp;
    }


}
