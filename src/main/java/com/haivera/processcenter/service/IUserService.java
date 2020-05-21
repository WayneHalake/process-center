package com.haivera.processcenter.service;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 获取用户信息
 * 用户id格式：系统id_用户id
 */
public interface IUserService {

    /**
     * 获取用户信息
     * @param systemId 系统id
     * @param userId 用户id
     * @return
     */
    User getUserById(String systemId, String userId);

    /**
     * 获取用户信息列表
     * @param systemId 系统id
     * @return
     */
    List<User> getUserList(String systemId);


    /**
     * 添加系统用户列表
     * @param users
     */
    void addUserList(List<UserEntityImpl> users, String systemId);

    /**
     * 添加用户信息
     * @param user
     */
    void addUser(User user, String systemId);

    /**
     * 删除用户信息
     * @param user
     */
    void delUser(User user, String systemId);

    /**
     * 同步用户和用户组关系
     * 用户和用户组关系默认关联
     * @param user
     * @param group
     */
    void syncUserAndGroup(User user, Group group, String systemId);

    /**
     * 用户与用户组关系
     * @param userAndGroup Map<userId, groupId> 用户与用户组关联关系
     */
    void syncUserAndGroup(HashMap<String, String> userAndGroup, String systemId);

    /**
     * 同步用户和用户组关系
     * 用户与用户组关系通过userAndGroup关联
     * @param users
     * @param groups
     * @param userAndGroup Map<userId, groupId> 用户与用户组关联关系
     */
    void syncUserAndGroup(List<UserEntityImpl> users, List<GroupEntityImpl> groups, HashMap<String, String> userAndGroup, String systemId);

    /**
     * 通过用户组获取用户列表
     * @param groupId
     * @param systemId
     * @return
     */
    List<User> getUserListByGroup(String groupId, String systemId);

}
