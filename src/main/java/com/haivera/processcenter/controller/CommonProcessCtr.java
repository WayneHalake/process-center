package com.haivera.processcenter.controller;

import com.haivera.processcenter.service.ICommonProcessSer;
import org.activiti.engine.history.HistoricActivityInstance;
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
@RestController
@RequestMapping("/processCtr")
public class CommonProcessCtr {


    @Autowired
    private ICommonProcessSer commonProcessSer;

    /**
     * 创建流程实例
     * @param processKey
     * @return
     */
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
    @GetMapping("/listProcess")
    public List<ProcessInstance> listProcess(String processKey){
        List<ProcessInstance> processInstances = new ArrayList<>();
        try{
            processInstances = commonProcessSer.listProcessInstance(processKey);
        }catch (Exception e){
            e.printStackTrace();
        }
        return processInstances;
    }

    /**
     * 获取流程当前任务节点
     * @param processId
     * @return
     */
    @GetMapping("/currentTask")
    public Task currentTask(String processId){
        Task task = null;
        try {
            task = commonProcessSer.currentTask(processId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return task;
    }

    /**
     * 获取流程当前任务列表
     * @param processId
     * @return
     */
    @GetMapping("/listRunTask")
    public List<Task> listRunTask(String processId){
        List<Task> taskList = new ArrayList<>();
        try{
            taskList = commonProcessSer.listRunTask(processId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * 获取当前用户在processkey流程中任务
     * @param processKey
     * @param systemId
     * @param userId
     * @return
     */
    @GetMapping("/listTask")
    public List<Task> listTask(String processKey, String systemId, String userId){
        List<Task> taskList = new ArrayList<>();
        try{
            taskList = commonProcessSer.listTask(processKey, systemId, userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * 获取所有的流程实例
     * @return
     */
    @GetMapping("/listAllProcessInstance")
    public List<ProcessInstance> listAllProcessInstance(){
        List<ProcessInstance> result = new ArrayList<>();
        try{
            result = commonProcessSer.listAllProcessInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 挂起流程
     * @param processId
     */
    @GetMapping("/suspendProcess")
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
    @GetMapping("/activateProcess")
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
    @GetMapping("/getProcessByProcessId")
    public ProcessInstance getProcessByProcessId(String processId){
        ProcessInstance result = null;
        try{
            result = commonProcessSer.getProcessByProcessId(processId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过任务id获取流程实例
     * @param taskId
     * @return
     */
    @GetMapping("/getProcessByTaskId")
    public ProcessInstance getProcessByTaskId(String taskId){
        ProcessInstance result = null;
        try{
            result = commonProcessSer.getProcessByTaskId(taskId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 完成一个任务
     * @param taskId
     * @param map 任务所需参数
     * @param userId 用户id
     * @param systemId 系统id
     * @return
     */
    @GetMapping("/complete")
    public String completeTask(String taskId, HashMap map, String systemId, String userId){
        String nextTaskId = "";
        try{
            nextTaskId = commonProcessSer.completeTask(taskId, map, systemId, userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return nextTaskId;
    }

    /**
     * 获取流程图
     * @param response
     * @param processId 流程id
     * @param lightFlag 是否高亮当前任务节点
     * @return
     */
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
    @GetMapping("/listHistory")
    public List<HistoricActivityInstance> listHistory(String processId){
        List<HistoricActivityInstance> result = new ArrayList<>();
        try{
            result = commonProcessSer.listHistory(processId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}
