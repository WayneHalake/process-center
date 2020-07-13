package com.haivera.processcenter.common;

import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonHistoricSer;
import com.haivera.processcenter.service.ICommonProcessSer;
import com.haivera.processcenter.service.ICommonTaskSer;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GeneralCommonMap {
    private static final Logger logger = LoggerFactory.getLogger(GeneralCommonMap.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private ICommonProcessSer commonProcessSer;

    @Autowired
    private ICommonHistoricSer commonHistoricSer;

    @Autowired
    private ICommonTaskSer commonTaskSer;

    public HashMap taskInfoMap(Task task){
        TaskEntityImpl entity = (TaskEntityImpl) task;
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskId", task.getId());
        map.put("taskName", task.getName());
        try {
            ProcessInstance processInstance = commonProcessSer.getProcessByProcessId(task.getProcessInstanceId());
            map.put("processName", processInstance.getName());
            map.put("startBy", processInstance.getStartUserId());
            map.put("businessKey", processInstance.getBusinessKey());
        }catch (ActivitiObjectNotFoundException e){
            //流程实例已结束或是不存在
            logger.info("{}流程已结束或是存在！", task.getProcessInstanceId());
        }
        map.put("processId", task.getProcessInstanceId());
        map.putAll((Map<? extends String, ?>) entity.getPersistentState());
        map.put("variables", taskService.getVariables(task.getId()));
        map.put("variablesLocal",taskService.getVariablesLocal(task.getId()));
        return map;
    }


    /**
     * 历史任务记录信息
     * @param taskInstance
     * @return
     */
    public HashMap hisTaskInfoMap(HistoricTaskInstance taskInstance){
        HistoricTaskInstanceEntityImpl entity = (HistoricTaskInstanceEntityImpl) taskInstance;
        HistoricProcessInstance historicProcessInstance = commonHistoricSer.getHisProcessInstance(taskInstance.getProcessInstanceId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskId", taskInstance.getId());
        map.put("processId", taskInstance.getProcessInstanceId());
        map.put("taskName", taskInstance.getName());
        map.put("processName", historicProcessInstance.getName());
        map.put("startBy", historicProcessInstance.getStartUserId());
        map.put("businessKey", historicProcessInstance.getBusinessKey());

        map.putAll((Map<? extends String, ?>) entity.getPersistentState());
        try {
            //流程的全局变量
            map.put("variables", commonHistoricSer.listHisVariable(historicProcessInstance.getId()).getData());
            //任务的局部变量 包括审批结果 审批意见 审批人
            map.put("variablesLocal", commonHistoricSer.listHisVariableByTaskId(taskInstance.getId()).getData());
        }catch (ActivitiObjectNotFoundException exception){
            logger.info("{}任务不存在或已结束！", taskInstance.getId());
        }
        return map;
    }

    public HashMap processInfoMap(ProcessInstance processInstance){
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) processInstance;
        ResponseInfo resp = commonTaskSer.currentTasks(processInstance.getId());

        HashMap<String, Object> map = new HashMap<>();
        if(resp.getData() != null){
            List<Map<? extends String, Object>> temp = (List<Map<? extends String, Object>>) resp.getData();
            if(temp.get(0).get("isSubProcess")!= null && (boolean) temp.get(0).get("isSubProcess")){
                map.put("taskInfo", temp.get(0).get("taskInfo"));
            }else{
                map.put("taskInfo",temp.get(0));
            }
        }
        map.put("processId", processInstance.getProcessInstanceId());
        map.putAll((Map<? extends String, ?>) executionEntity.getPersistentState());
        return map;
    }

    public HashMap hisProcessInfoMap(HistoricProcessInstance historicProcessInstance){
        HistoricProcessInstanceEntityImpl entity = (HistoricProcessInstanceEntityImpl) historicProcessInstance;
        HashMap<String, Object> map = new HashMap<>();
        map.put("processId", entity.getProcessInstanceId());
        map.putAll((Map<? extends String, ?>) entity.getPersistentState());
        return map;
    }

}
