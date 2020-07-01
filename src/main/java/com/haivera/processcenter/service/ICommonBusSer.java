package com.haivera.processcenter.service;

import com.haivera.processcenter.common.util.ResponseInfo;

/**
 * businessKey相关操作
 */
public interface ICommonBusSer {

    /**
     * 获取基本信息
     * 流程实例信息、当前任务所在任务节点等
     * ***
     * *** 如存在多个流程的businessKey相同时，仅仅会返回流程id最大的流程信息
     * ***
     * @param businessKey
     * @return
     */
    ResponseInfo getBaseInfo(String businessKey, String systemId);

    /**
     * 通过businessKey获取流程实例id
     *  * ***
     *  * *** 如存在多个流程的businessKey相同时，仅仅会返回流程id最大的流程信息
     *  * ***
     * @param businessKey
     * @param systemId
     * @return
     */
    String getProcessId(String businessKey, String systemId);

    /**
     * 修改流程中的businessKey
     * @param businessKey
     * @param systemId
     * @param processId
     * @return
     */
    ResponseInfo updateBusinessKey(String businessKey, String systemId, String processId);


}
