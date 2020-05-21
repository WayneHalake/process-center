package com.haivera.processcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.haivera.processcenter.common.GeneralCommonMap;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonProcessSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 表单相关操作
 */
@RestController
@RequestMapping("/commonForm")
public class CommonFormCtr {

    @Autowired
    private ICommonProcessSer commonProcessSer;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ICommonTaskSer commonTaskSer;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private GeneralCommonMap generalCommonMap;

    /**
     * 获取开始事件上的表单数据
     *
     * @param processKey
     * @return
     */
    @ApiOperation(value = "获取开始事件上的表单", notes = "获取开始事件上的表单")
    @ApiImplicitParam(name = "processKey", value = "流程定义processKey", paramType = "query", required = true, dataType = "string")
    @GetMapping("/getStartFormData")
    public ResponseInfo getStartFormData(String processKey) {
        ResponseInfo resp = new ResponseInfo();
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).singleResult();
            String processDefinitionId = processDefinition.getId();
            FormService formService = processEngine.getFormService();
            String formKey = formService.getStartFormKey(processDefinitionId);
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            List<FormProperty> formPropertyList = startFormData.getFormProperties();

            List<FormPropertyImpl> formPropertyImplList = new ArrayList<>();
            for (FormProperty formProperty : formPropertyList) {
                FormPropertyImpl formPropertyImpl = (FormPropertyImpl) formProperty;
                formPropertyImplList.add(formPropertyImpl);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("formkey", formKey);
            map.put("formPropertyList", formPropertyImplList);
            resp.doSuccess("获取表单成功", map);
        } catch (Exception e) {
            e.printStackTrace();
            resp.doFailed("获取表单失败", e);
        }
        return resp;
    }

    /**
     * 提交流程开始节点表单，并完成开始节点
     *
     * @param processKey
     * @param properties
     * @return
     */
    @ApiOperation(value = "提交流程开始节点表单，并完成开始节点", notes = "提交流程开始节点表单，并完成开始节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程定义processKey", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "properties", value = "任务节点表单数据", paramType = "body", required = true, dataType = "string"),
    })
    @PostMapping("/submitStartFormData")
    public ResponseInfo submitStartFormData(@RequestParam String processKey, @RequestBody JSONObject properties) {
        ResponseInfo resp = new ResponseInfo();
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).singleResult();
            String processDefinitionId = processDefinition.getId();
            FormService formService = processEngine.getFormService();
            HashMap<String, String> propertiesMap = JSONObject.parseObject(properties.toJSONString(), HashMap.class);
            ProcessInstance processInstance = formService.submitStartFormData(processDefinitionId, propertiesMap);

            List<Task> taskList = commonTaskSer.currentTask(processInstance.getId());

            List<Object> datas = new ArrayList<>();
            for(Task task: taskList){
                TaskEntityImpl taskEntity = (TaskEntityImpl)task;
                datas.add(generalCommonMap.taskInfoMap(task.getId(), taskEntity.getPersistentState()));
            }
            resp.doSuccess("提交表单成功", datas);
        } catch (Exception e) {
            e.printStackTrace();
            resp.doFailed("提交表单失败", e);
        }
        return resp;
    }


    /**
     * 获取任务节点上的表单数据
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "获取任务节点上的表单", notes = "获取任务节点上的表单")
    @ApiImplicitParam(name = "taskId", value = "taskId", paramType = "query", required = true, dataType = "string")
    @GetMapping("/getTaskFormData")
    public ResponseInfo getTaskFormData(String taskId) {
        ResponseInfo resp = new ResponseInfo();
        try {
            FormService formService = processEngine.getFormService();
            TaskFormData taskFormData = formService.getTaskFormData(taskId);
            List<FormProperty> formPropertyList = taskFormData.getFormProperties();
            List<FormPropertyImpl> formPropertyImplList = new ArrayList<>();

            for (FormProperty formProperty : formPropertyList) {
                formPropertyImplList.add((FormPropertyImpl) formProperty);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("formKey", taskFormData.getFormKey());
            map.put("formPropertyList", formPropertyImplList);
            resp.doSuccess("获取表单成功", map);
        } catch (Exception e) {
            e.printStackTrace();
            resp.doFailed("获取表单失败", e);
        }
        return resp;
    }

    /**
     * 提交任务节点表单，并完成任务
     *
     * @param taskId
     * @param properties
     * @return
     */
    @ApiOperation(value = "提交任务节点表单，并完成任务", notes = "提交任务节点表单，并完成任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "taskId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "properties", value = "任务节点表单数据", paramType = "body", required = true, dataType = "string"),
    })
    @PostMapping("/submitTaskFormData")
    public ResponseInfo submitTaskFormData(@RequestParam String taskId, @RequestBody JSONObject properties) {
        ResponseInfo resp = new ResponseInfo();
        try {
            FormService formService = processEngine.getFormService();
            HashMap<String, String> propertiesMap = JSONObject.parseObject(properties.toJSONString(), HashMap.class);
            formService.submitTaskFormData(taskId, propertiesMap);
            resp.doSuccess("提交表单成功");
        } catch (Exception e) {
            e.printStackTrace();
            resp.doFailed("提交表单失败", e);
        }
        return resp;
    }

    /**
     * 保存任务节点表单，不会完成任务（不会修改任务状态）
     *
     * @param taskId
     * @param properties
     * @return
     */
    @ApiOperation(value = "保存任务节点表单，不会完成任务（不会修改任务状态）", notes = "保存任务节点表单，不会完成任务（不会修改任务状态）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "taskId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "properties", value = "任务节点表单数据", paramType = "body", required = true, dataType = "string"),
    })
    @PostMapping("/saveFormData")
    public ResponseInfo saveFormData(@RequestParam  String taskId, @RequestBody JSONObject properties) {
        ResponseInfo resp = new ResponseInfo();
        try {
            FormService formService = processEngine.getFormService();
            HashMap<String, String> propertiesMap = JSONObject.parseObject(properties.toJSONString(), HashMap.class);
            formService.saveFormData(taskId, propertiesMap);
            resp.doSuccess("保存表单成功");
        } catch (Exception e) {
            e.printStackTrace();
            resp.doFailed("保存表单失败", e);
        }
        return resp;
    }

    /**
     * 设置任务变量 -- 与 @saveFormData功能一致
     * 任务变量会随着流程的进行，传递到每一个任务节点
     * 任务必须在运行状态
     * @param taskId
     * @param properties
     * @return
     */
    @ApiOperation(value = "设置任务变量", notes = "设置任务变量。任务变量会随着流程的进行，传递到每一个任务节点。任务必须在运行状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "taskId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "properties", value = "任务节点表单数据", paramType = "body", required = true, dataType = "string"),
    })
    @PostMapping("/setVariables")
    public ResponseInfo setVariables(@RequestParam String taskId, @RequestBody JSONObject properties){
        ResponseInfo resp = new ResponseInfo();
        try {
            HashMap<String, String> propertiesMap = JSONObject.parseObject(properties.toJSONString(), HashMap.class);
            taskService.setVariables(taskId, propertiesMap);
            resp.doSuccess("设置任务变量成功");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务变量失败", e);
        }
        return resp;
    }

    /**
     * 获取任务变量（可用于获取提交至任务表单中的数据）
     * 将获取所有的任务变量
     * @param taskId
     * @return
     */
    @ApiOperation(value = "获取任务变量", notes = "获取任务变量（可用于获取提交至任务表单中的数据），将获取所有的任务变量")
    @ApiImplicitParam(name = "taskId", value = "taskId", paramType = "query", required = true, dataType = "string")
    @GetMapping("/getVariables")
    public ResponseInfo getVariables(String taskId){
        ResponseInfo resp = new ResponseInfo();
        try {
            Map<String, Object> map = taskService.getVariables(taskId);
            resp.doSuccess("获取任务变量成功", map);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务变量失败", e);
        }
        return resp;
    }

    /**
     * 设置任务变量
     * 设置的变量仅仅存在当前的任务节点中，不会随着流程的进行传递到后续的任务中
     * @param taskId
     * @param properties
     * @return
     */
    @ApiOperation(value = "设置任务变量", notes = "设置的变量仅仅存在当前的任务节点中，不会随着流程的进行传递到后续的任务中。任务必须在运行状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "taskId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "properties", value = "任务节点表单数据", paramType = "body", required = true, dataType = "string"),
    })
    @PostMapping("/setVariablesLocal")
    public ResponseInfo setVariablesLocal(@RequestParam String taskId, @RequestBody JSONObject properties){
        ResponseInfo resp = new ResponseInfo();
        try {
            HashMap<String, String> propertiesMap = JSONObject.parseObject(properties.toJSONString(), HashMap.class);
            taskService.setVariablesLocal(taskId, propertiesMap);
            resp.doSuccess("设置任务变量成功");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务变量失败", e);
        }
        return resp;
    }

    /**
     * 获取任务变量 仅仅会获取只 @setVariablesLocal 设置的变量
     * @param taskId
     * @return
     */
    @ApiOperation(value = "获取任务变量", notes = "获取任务变量 仅仅会获取只 @setVariablesLocal 设置的变量")
    @ApiImplicitParam(name = "taskId", value = "taskId", paramType = "query", required = true, dataType = "string")
    @GetMapping("/getVariablesLocal")
    public ResponseInfo getVariablesLocal(String taskId){
        ResponseInfo resp = new ResponseInfo();
        try {
            Map<String, Object> map = taskService.getVariablesLocal(taskId);
            resp.doSuccess("获取任务变量成功", map);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("设置任务变量失败", e);
        }
        return resp;
    }

}
