package com.haivera.processcenter.vo;

import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntityImpl;

import java.util.HashMap;

//todo 功能迁移至GeneralCommonMap

public class HistoricVariableInstanceEntity {

    public static Object getPersistentState(HistoricVariableInstanceEntityImpl entity){
        HashMap<String, Object> map = (HashMap<String, Object>) entity.getPersistentState();
        map.put("name", entity.getName());
        map.put("taskId", entity.getTaskId());
        map.put("processInstanceId", entity.getProcessInstanceId());
        map.put("executionId", entity.getExecutionId());
        return map;
    }
}
