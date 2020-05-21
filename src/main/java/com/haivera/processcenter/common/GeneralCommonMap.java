package com.haivera.processcenter.common;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GeneralCommonMap {

    @Autowired
    private TaskService taskService;

    public HashMap taskInfoMap(String taskId, Object object){
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("taskEntity", object);
        map.putAll(taskService.getVariables(taskId));
        return map;
    }

}
