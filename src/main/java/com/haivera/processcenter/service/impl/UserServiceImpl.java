package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.service.IUserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceImpl implements IUserService {

    @Autowired
    private IdentityService identityService;

    /**
     * 获取用户信息
     * @param systemId 系统id
     * @param userId 用户id
     * @return
     */
    @Override
    public User getUserById(String systemId, String userId) {
        return identityService.createUserQuery().userId(IdCombine.combineId(systemId, userId)).singleResult();
    }

    /**
     * 获取用户信息列表
     * @param systemId 系统id
     * @return
     */
    @Override
    public List<User> getUserList(String systemId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ACT_ID_USER");
        if(StringUtils.isNotEmpty(systemId)){
            sb.append(" where ID_ like ").append(systemId).append("_%");
        }
        return identityService.createNativeUserQuery().sql(sb.toString()).list();
    }
}
