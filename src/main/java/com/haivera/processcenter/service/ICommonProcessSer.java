package com.haivera.processcenter.service;

import com.haivera.processcenter.common.util.ResponseInfo;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;

import javax.servlet.http.HttpServletResponse;
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
     * 创建并开始流程
     * @param processDefinitionKey 流程key
     * @param busCode 业务编码
     * @param busType  业务类型
     * @return
     */
    String startProcessInstanceByKey(String processDefinitionKey, String busCode, String busType, String userId, String systemId);
    /**
     * 按流程key查找运行中的流程实例
     * @param processKey
     * @return
     * @throws Exception
     */
    ResponseInfo listProcessInstance(String processKey) throws Exception;

    /**
     * 通过流程发起人查找流程实例
     * @param systemId
     * @param userId
     * @return
     */
    ResponseInfo listProcessInstanceStartBy(String systemId, String userId);

    /**
     * 获取所有的正在运行的流程实例
     * @return
     * @throws Exception
     */
    ResponseInfo listAllProcessInstance() throws Exception;

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
    boolean isFinishedProcess(String processId);

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
     * 通过流程id获取子流程
     * @param processId
     * @return
     */
    ResponseInfo getSubProcessByProcessId(String processId);
}
