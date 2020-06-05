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
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

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

    @Autowired
    private RepositoryService repositoryService;



    @ApiOperation(value = "创建流程", notes = "通过processKey创建流程,需指定busCode、busType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程图processKey", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "busCode", value = "业务编码", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "busType", value = "业务类型", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "userId", paramType="query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "systemId", paramType="query", required = true, dataType = "string"),
        })
    @PostMapping("/createPro4bus")
    public ResponseInfo createPro(@RequestParam("processKey") String processKey,
                                  @RequestParam String busCode, @RequestParam String busType,
                                  @RequestParam String userId, @RequestParam String systemId){
        ResponseInfo resp = new ResponseInfo();
        String processId = "";
        try{
            processId = commonProcessSer.startProcessInstanceByKey(processKey, busCode, busType, userId, systemId);
            List<Task> tasks = commonTaskSer.currentTask(processId);
            List<String> taskIds = new ArrayList<>();
            for (Task task : tasks) {
                taskIds.add(task.getId());
            }
            HashMap<String, Object> map = new HashMap();
            map.put("processId", processId);
            map.put("currentTaskId", taskIds.get(0));
            resp.doSuccess("创建流程成功", map);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("创建流程失败");
        }
        return resp;
    }


    /**
     * 创建流程实例
     * @param processKey
     * @return
     */
    @ApiOperation(value = "创建流程", notes = "通过processKey创建流程")
    @ApiImplicitParam(name = "processKey", value = "流程图processKey", paramType="query", required = true, dataType = "string")
    @PostMapping("/createPro")
    public ResponseInfo createPro(@RequestParam("processKey") String processKey){
        ResponseInfo resp = new ResponseInfo();
        String processId = "";
        try{
            processId = commonProcessSer.StartAndCreateProcess(processKey);
            List<Task> tasks = commonTaskSer.currentTask(processId);
            List<String> taskIds = new ArrayList<>();
            for (Task task : tasks) {
                taskIds.add(task.getId());
            }
            HashMap<String, Object> map = new HashMap();
            map.put("processId", processId);
            map.put("currentTaskId", taskIds.get(0));
            resp.doSuccess("创建流程成功", map);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("创建流程失败");
        }
        return resp;
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
        try{
            responseInfo = commonProcessSer.listProcessInstance(processKey);
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
            resp = commonProcessSer.listAllProcessInstance();
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
     * 包括子流程
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
            ResponseInfo subInfo = commonProcessSer.getSubProcessByProcessId(processId);
            ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) result;
            HashMap map = new HashMap();
            map.put("processInfo", executionEntity.getPersistentState());
            map.put("subProcessInfo", subInfo.getData());
            resp.doSuccess("获取流程实例成功！", map);
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
     * 手动发布流程，
     * 可上传zip压缩文件（用于发布多个流程）
     * 上传xml文件，用于发布单个流程
     * @return
     */
    @ApiOperation(value = "手动发布流程", notes = "手动发布流程，可上传zip压缩文件（用于发布多个流程）。上传xml文件（用于发布单个流程）。")
    @PostMapping("/deploymentProcess")
    public ResponseInfo deploymentProcess(@RequestParam MultipartFile file){
        ResponseInfo resp = new ResponseInfo();
        try{
            String fileName = file.getOriginalFilename();
            if(file.isEmpty()){
                resp.doFailed("发布文件为空");
                return resp;
            }
            //xml文件
            if("text/xml".equals(file.getContentType())){
                InputStream inputStream = file.getInputStream();
                Deployment deployment = repositoryService.createDeployment().addInputStream(fileName, inputStream).deploy();
                resp.doSuccess("发布成功！", deployment.getId());
                return resp;
            }
            //zip压缩包格式文件 --多流程发布
            if("application/x-zip-compressed".equals(file.getContentType())){
                ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
                Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
                resp.doSuccess("发布成功", deployment.getId());
                return resp;
            }
            resp.doFailed("文件格式不对，发布失败");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("发布流程失败！", e);
        }
        return resp;
    }
}
