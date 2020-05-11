package com.haivera.processcenter.service;

import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;

/**
 * 任务节点相关操作
 */
public interface ICommonTaskSer {

    /**
     * 完成一个任务
     * @param taskId 任务id
     * @param map 下一次任务所需的参数
     * @param userId 任务执行人
     * @param systemId 系统id
     * @return 下一个任务的id
     */
    String completeTask(String taskId, HashMap<String, Object> map, String systemId, String userId) throws Exception;

    /**
     * 查询流程的当前
     * @param processId 流程id 流程创建(实例化)之后的id
     * @return 当前运行的任务
     */
    Task currentTask(String processId) throws Exception;

    /**
     * 按流程key查询用户的任务列表
     * @param processKey 流程key 创建bpmn文件时流程的processId
     * @param systemId
     * @param userId
     * @return
     */
    List<Task> listTask(String processKey, String systemId, String userId) throws Exception;


    /**
     * 按流程id查询任务列表
     * 在运行中的任务(可以用来查询一条流程当前进行的任务)
     * @param processId
     * @return
     */
    List<Task> listRunTask(String processId) throws Exception;

    /**
     * 通过任务id获取任务
     * @param taskId
     * @return
     * @throws Exception
     */
    Task getTaskByTaskId(String taskId) throws Exception;

    /**
     * 设置任务处理人
     * 不会覆盖已存在的任务处理人，仅会对没有设置任务处理人的任务设置任务处理人
     * @param taskId
     * @param assigner
     * @throws Exception
     */
    void claimTask(String taskId, String assigner) throws Exception;

    /**
     * 设置任务处理人（建议使用）
     * 会覆盖已存在的任务处理人
     * @throws Exception
     */
    void setAssigner(String taskId, String assigner) throws Exception;

    /**
     * 设置候选任务处理人，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作
     * @param taskId
     * @param userId
     */
    void addCandidateUser(String taskId, String userId);

    /**
     * 设置候选任务处理人，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作
     * @param taskId
     * @param userIds
     */
    void addCandidateUser(String taskId, List<String> userIds);

    /**
     * 设置候选任务用户组，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作
     * @param taskId
     * @param groupId
     */
    void addCandidateGroup(String taskId, String groupId);

    /**
     * 设置候选任务用户组，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作
     * @param taskId
     * @param groupIds
     */
    void addCandidateGroup(String taskId, List<String> groupIds);

}
