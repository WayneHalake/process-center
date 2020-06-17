package com.haivera.processcenter.common;

import com.haivera.processcenter.service.ICommonProcessSer;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GeneralCommonMap {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ICommonProcessSer commonProcessSer;

    public HashMap taskInfoMap(Task task){
        TaskEntityImpl entity = (TaskEntityImpl) task;
        ProcessInstance processInstance = commonProcessSer.getProcessByProcessId(task.getProcessInstanceId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskId", task.getId());
        map.put("taskName", task.getName());
        map.put("processName", processInstance.getName());
        map.put("businessKey", processInstance.getBusinessKey());
        map.put("processId", task.getProcessInstanceId());
        map.putAll((Map<? extends String, ?>) entity.getPersistentState());
        map.putAll(taskService.getVariables(task.getId()));
        return map;
    }


    /**
     * 历史任务记录信息
     * @param taskInstance
     * @return
     */
    public HashMap hisTaskInfoMap(HistoricTaskInstance taskInstance){
        HistoricTaskInstanceEntityImpl entity = (HistoricTaskInstanceEntityImpl) taskInstance;
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskId", taskInstance.getId());
        map.put("processId", taskInstance.getProcessInstanceId());
        map.putAll((Map<? extends String, ?>) entity.getPersistentState());
        map.putAll(taskService.getVariables(taskInstance.getId()));
        return map;
    }

    public HashMap processInfoMap(ProcessInstance processInstance){
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) processInstance;
        HashMap<String, Object> map = new HashMap<>();
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
