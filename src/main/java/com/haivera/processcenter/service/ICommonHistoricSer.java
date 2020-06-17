package com.haivera.processcenter.service;


import com.haivera.processcenter.common.util.ResponseInfo;

public interface ICommonHistoricSer {

    /**
     * 流程实例历史记录
     * @param processKey
     * @return
     */
    ResponseInfo listHisProcessInstance(String processKey, boolean finishFlag);

    ResponseInfo listHisProcessInstance(String systemId, String userId, boolean finishFlag);

    /**
     *
     * @param processKey
     * @param systemId
     * @param userId
     * @param finishFlag 流程是否结束
     * @return
     */
    ResponseInfo listHisProcessInstance(String processKey, String systemId, String userId, boolean finishFlag);

    /**
     * 获取processkey的历史流程的所有活动
     * @param processKey
     * @return
     */
    ResponseInfo listHisProcess(String processKey);

    /**
     * 获取用户所有的历史流程的所有活动
     * @param systemId
     * @param userId
     * @return
     */
    ResponseInfo listHisProcess(String systemId, String userId);

    /**
     * 获取用户的processkey的历史流程的所有活动
     * @param processKey
     * @param systemId
     * @param userId
     * @return
     */
    ResponseInfo listHisProcess(String processKey, String systemId, String userId);


    /**
     * 获取processId流程的所有历史任务节点
     * @param processId
     * @return
     */
    ResponseInfo listHisTask(String processId);

    ResponseInfo listHisTask(String processId, String taskName);

    /**
     * 获取用户处理的流程（processId）历史任务节点
     * @param processId
     * @param systemId
     * @param userId
     * @return
     */
    ResponseInfo listHisTask(String processId, String systemId, String userId);

    ResponseInfo listHisTask(String processId, String systemId, String userId, String taskName);

    /**
     * 获取用户所有的历史任务节点
     * @param systemId
     * @param userId
     * @return
     */
    ResponseInfo listHisTaskByAssignee(String systemId, String userId);

    /**
     * 获取历史变量信息
     * @param processId
     * @return
     */
    ResponseInfo listHisVariable(String processId);

    ResponseInfo listHisVariable(String processId, String taskId);

    ResponseInfo listHisVariableByTaskId(String taskId);

}
