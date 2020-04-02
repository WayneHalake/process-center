package com.haivera.processcenter.service;

import org.activiti.engine.identity.Group;

import java.util.List;

/**
 * 获取用户组信息
 * 用户组id格式：系统id_系统角色id
 */
public interface IGroupService {

    /**
     * 获取用户组信息
     * @param systemId 系统id
     * @param groupId 系统角色id
     * @return
     */
    Group getGroupById(String systemId, String groupId);

    /**
     * 获取用户组信息列表
     * @param systemId 系统id
     * @return
     */
    List<Group> getGroupList(String systemId);
}
