package com.haivera.processcenter.service;

import com.haivera.processcenter.common.util.ResponseInfo;

/**
 * 流程和任务相关数量统计
 *
 */
public interface ICommonStatisSer {

    //统计当前人员待处理的任务数量
    //<任务名称，数量>
    //<任务名称，任务信息>  --> 任务信息：[任务详细信息]
    ResponseInfo countUpTodoTask(String systemId, String userId);

    ResponseInfo countUpTodoTask(String systemId, String userId, String processKey);

    //统计当前人员已处理的任务数量
    //<任务名称，数量>
    //<任务名称，任务信息>  --> 任务信息：[任务详细信息]
    ResponseInfo countUpDoneTask(String systemId, String userId);

    ResponseInfo countUpDoneTask(String systemId, String userId, String processKey);

    //统计我发起的 正在运行的流程
    //<流程名称，数量>
    //<流程名称，流程信息>  --> 流程信息：[流程详细信息]
    ResponseInfo countUpActProcess(String systemId, String userId);

    //统计我发起的 已结束的流程
    ResponseInfo countUpFinishProcess(String systemId, String userId);


    //统计所有正在处理的流程 todo
    ResponseInfo countUpAllActProcess(String systemId);

    //统计所有已处理的流程 todo
    ResponseInfo countUpAllFinishProcess(String systemId);

}
