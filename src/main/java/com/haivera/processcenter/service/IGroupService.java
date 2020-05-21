package com.haivera.processcenter.service;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;

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
     * 通过用户id获取用户组信息
     * @param systemId
     * @param userId
     * @return
     */
    List<Group> getGroupByUserId(String systemId, String userId);

    /**
     * 获取用户组信息列表
     * @param systemId 系统id
     * @return
     */
    List<Group> getGroupList(String systemId);

    /**
     * 添加用户组列表信息
     * @param groupList
     */
    void addGroupList(List<GroupEntityImpl> groupList, String systemId);

    /**
     * 添加用户组信息
     * @param group
     */
    void addGroup(Group group, String systemId);

    /**
     * 删除
     * @param group
     */
    void delGroup(Group group, String systemId);

}
