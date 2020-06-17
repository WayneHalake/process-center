package com.haivera.processcenter.service;

import com.haivera.processcenter.common.util.ResponseInfo;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务节点相关操作
 */
public interface ICommonTaskSer {

    /**
     * 完成一个任务(非委托任务)
     * @param taskId 任务id
     * @param variables 任务所需的参数
     * @param systemId 系统id
     * @param userId 任务执行人
     * @param checkAssignee 是否检查任务处理人
     * @return 下一个任务的id
     */
    ResponseInfo completeTask(String taskId, HashMap<String, Object> variables, String systemId, String userId, boolean checkAssignee) throws Exception;

    /**
     * 完成一个任务（非委托任务）
     * @param taskId
     * @param variables 任务所需的参数
     * @param transientVariables transient所需的参数
     * @param systemId
     * @param userId
     * @param checkAssignee 是否检查任务处理人
     * @return
     * @throws Exception
     */
    ResponseInfo completeTask(String taskId, HashMap<String, Object> variables, HashMap<String, Object> transientVariables, String systemId, String userId, boolean checkAssignee) throws Exception;

    /**
     * 完成一个任务（非委托任务）
     * @param taskId
     * @param variables 任务所需的参数
     * @param systemId 系统id
     * @param userId 任务执行人
     * @param nextUserId 下一任务节点的处理人
     * @param checkAssignee 是否检查任务处理人
     * @return
     * @throws Exception
     */
    ResponseInfo completeTaskWithNextUserId(String taskId, HashMap<String, Object> variables, String systemId, String userId, String nextUserId, boolean checkAssignee) throws Exception;

    /**
     * 委托任务给userId
     * @param taskId
     * @param systemId
     * @param userId
     */
    ResponseInfo delegateTask(String taskId, String systemId, String userId);

    /**
     * 处理委托任务，不会完成任务
     * @param taskId
     * @param variables
     */
    ResponseInfo resolveTask(String taskId, Map<String, Object> variables, String systemId, String userId);

    /**
     * 处理委托任务 不会完成任务
     * @param taskId
     * @param variables
     * @param transientVariables
     */
    ResponseInfo resolveTask(String taskId, Map<String, Object> variables, Map<String, Object> transientVariables, String systemId, String userId);

    /**
     * 查询流程的当前
     * @param processId 流程id 流程创建(实例化)之后的id
     * @return 当前运行的任务
     */
    List<Task> currentTask(String processId);

    /**
     * 查询流程当前任务
     * @param processId
     * @return
     */
    ResponseInfo currentTasks(String processId);

    /**
     * 获取当前任务的下一任务信息
     *
     *      * ***注意***
     *      * 如果在网关之后存在多条SequenceFlow时，需要至少有一条SequenceFlow的name为"通过"，否则获取为空
     *      * ***注意***
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    List<FlowElement> getNextTask(String taskId) throws Exception;

    /**
     * 按流程key查询用户的任务列表
     * @param processKey 流程key 创建bpmn文件时流程的processId
     * @param systemId
     * @param userId
     * @return
     */
    ResponseInfo listTask(String processKey, String systemId, String userId) throws Exception;

    /**
     * 查询用户的所有任务列表
     * @param systemId
     * @param userId
     * @return
     * @throws Exception
     */
    ResponseInfo listTask(String systemId, String userId) throws Exception;

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
     * 设置任务处理人 (通常用于候选人员签收任务)
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
    void addCandidateUser(String taskId, String systemId, String userId);

    /**
     * 设置候选任务处理人，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作
     * @param taskId
     * @param userIds
     */
    void addCandidateUser(String taskId, String systemId, List<String> userIds);

    /**
     * 设置候选任务用户组，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作
     * @param taskId
     * @param groupId
     */
    void addCandidateGroup(String taskId, String systemId, String groupId);

    /**
     * 设置候选任务用户组，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作
     * @param taskId
     * @param groupIds
     */
    void addCandidateGroup(String taskId, String systemId, List<String> groupIds);

    /**
     * 获取任务候选用户
     * @param taskId
     * @return
     */
    List<IdentityLink> getIdentityLinksForTask(String taskId);

    /**
     * 校验任务处理人
     * @param taskId
     * @param systemId
     * @param userId
     * @return
     */
    boolean checkAssignee(String taskId, String systemId, String userId);

    /**
     * 校验任务处理候选人
     * @param taskId
     * @param systemId
     * @param userId
     * @return
     */
    boolean checkCandidateUser(String taskId, String systemId, String userId);

    /**
     * 校验任务拥有人
     * @param taskId
     * @param systemId
     * @param userId
     * @return
     */
    boolean checkOwner(String taskId, String systemId, String userId);

}
