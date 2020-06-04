package com.haivera.processcenter.vo;

import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl;

import java.util.HashMap;

//todo 功能迁移至GeneralCommonMap

public class HistoricActivityInstanceEntity {

    public static Object getPersistentState(HistoricActivityInstanceEntityImpl activityInstanceEntity) {
        HashMap<String, Object> map = (HashMap<String, Object>) activityInstanceEntity.getPersistentState();
        map.put("activityId", activityInstanceEntity.getActivityId());
        map.put("activityName", activityInstanceEntity.getActivityName());
        map.put("activityType", activityInstanceEntity.getActivityType());
        map.put("executionId", activityInstanceEntity.getExecutionId());
        map.put("assignee", activityInstanceEntity.getAssignee());
        map.put("taskId", activityInstanceEntity.getTaskId());
        map.put("calledProcessInstanceId", activityInstanceEntity.getCalledProcessInstanceId());
        return map;
    }
}
