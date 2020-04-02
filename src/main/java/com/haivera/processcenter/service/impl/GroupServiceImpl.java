package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.service.IGroupService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GroupServiceImpl implements IGroupService {

    @Autowired
    private IdentityService identityService;

    /**
     * 获取用户组信息
     * @param systemId 系统id
     * @param groupId 系统角色id
     * @return
     */
    @Override
    public Group getGroupById(String systemId, String groupId) {
        return identityService.createGroupQuery().groupId(IdCombine.combineId(systemId, groupId)).singleResult();
    }

    /**
     * 获取用户组列表
     * @param systemId 系统id
     * @return
     */
    @Override
    public List<Group> getGroupList(String systemId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ACT_ID_GROUP");
        if(StringUtils.isNotEmpty(systemId)){
            sb.append(" where ID_ like ").append(systemId).append("_%");
        }
        return identityService.createNativeGroupQuery().sql(sb.toString()).list();
    }
}
