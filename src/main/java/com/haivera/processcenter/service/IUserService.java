package com.haivera.processcenter.service;

import org.activiti.engine.identity.User;

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
}
