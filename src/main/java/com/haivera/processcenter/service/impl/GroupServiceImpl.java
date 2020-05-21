package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.service.IGroupService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements IGroupService {

    /**
     * 目的参考 UserServiceImpl
     * @param groupEntity
     * @param group
     */
    private void generalGroup(Group groupEntity, Group group){
        groupEntity.setName(group.getName());
        groupEntity.setType(group.getType());
    }

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

    @Override
    public List<Group> getGroupByUserId(String systemId, String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ACT_ID_GROUP group left join ACT_ID_MEMBERSHIP membership on (group.ID_ == membership.GROUP_ID_ )");
        if(StringUtils.isNotEmpty(systemId)){
            sb.append(" where membership.USER_ID_ = '").append(IdCombine.combineId(systemId, userId)).append("'");
        }
        return identityService.createNativeGroupQuery().sql(sb.toString()).list();
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

    @Override
    public void addGroupList(List<GroupEntityImpl> groupList, String systemId) {
        for(Group group: groupList){
            this.addGroup(group, systemId);
        }
    }

    @Override
    public void addGroup(Group group, String systemId) {
        Group groupEntity = identityService.newGroup(IdCombine.combineId(systemId, group.getId()));
        generalGroup(groupEntity, group);
        identityService.saveGroup(groupEntity);
    }

    @Override
    public void delGroup(Group group, String systemId) {
        identityService.deleteGroup(IdCombine.combineId(systemId, group.getId()));
    }
}
