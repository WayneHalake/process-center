package com.haivera.processcenter.taskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * 任务监听器
 * create: occurs when the task has been created an all task properties are set.
 * assignment: occurs when the task is assigned to somebody. Note: when process execution arrives in a userTask, first an assignment event will be fired, before the create event is fired. This might seem an unnatural order, but the reason is pragmatic: when receiving the create event, we usually want to inspect all properties of the task including the assignee.
 * complete: occurs when the task is completed and just before the task is deleted from the runtime data.
 * delete: occurs just before the task is going to be deleted. Notice that it will also be executed when task is normally finished via completeTask.
 */
@Component
public class TestTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

    }
}
