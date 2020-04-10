package com.haivera.processcenter.taskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.FixedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(TestTaskListener.class);

    //添加任务监听器时添加的FieldName
    private FixedValue testField;

    @Override
    public void notify(DelegateTask delegateTask) {
        Object value = testField.getValue(delegateTask); //获取FieldName对应的value
        logger.info("value:{}", value.toString());
    }

    public FixedValue getTestField() {
        return testField;
    }

    public void setTestField(FixedValue testField) {
        this.testField = testField;
    }
}
