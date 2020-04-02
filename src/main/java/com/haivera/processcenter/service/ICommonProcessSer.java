package com.haivera.processcenter.service;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 流程公共接口
 */
public interface ICommonProcessSer {

    /**
     * 创建并开始流程
     * @param processKey 流程key 创建bpmn文件时流程的processId
     * @return 流程创建之后的id
     */
    String StartAndCreateProcess(String processKey) throws Exception;

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
     * 按流程key查找运行中的流程实例
     * @param processKey
     * @return
     * @throws Exception
     */
    List<ProcessInstance> listProcessInstance(String processKey) throws Exception;

    /**
     * 获取所有的正在运行的流程实例
     * @return
     * @throws Exception
     */
    List<ProcessInstance> listAllProcessInstance() throws Exception;

    /**
     * 按流程id查询该流程的历史记录
     * @param processId
     * @return
     */
    List<HistoricActivityInstance> listHistory(String processId) throws Exception;

    /**
     * 获取流程图
     * 当前节点高亮
     * @param response
     * @param processId 流程id
     * @param lightFlag 是否高亮当前节点
     */
    void getWorkFlowImage(HttpServletResponse response, String processId, boolean lightFlag) throws Exception;

    /**
     * 挂起流程
     * @param processId
     * @throws Exception
     */
    void suspendProcess(String processId) throws Exception;

    /**
     * 将挂起流程激活
     * @param processId
     * @throws Exception
     */
    void activateProcess(String processId) throws Exception;


    /**
     * 判断流程是否结束
     * @param processId
     * @return
     * @throws Exception
     */
    boolean isFinishedProcess(String processId) throws Exception;

    /**
     * 通过流程id获取流程
     * @param processId
     * @return
     * @throws Exception
     */
    ProcessInstance getProcessByProcessId(String processId) throws Exception;

    /**
     * 通过任务id获取流程
     * @param taskId
     * @return
     * @throws Exception
     */
    ProcessInstance getProcessByTaskId(String taskId) throws Exception;

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

}
