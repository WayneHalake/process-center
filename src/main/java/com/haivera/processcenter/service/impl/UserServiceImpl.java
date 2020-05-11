package com.haivera.processcenter.service.impl;

import com.haivera.processcenter.common.IdCombine;
import com.haivera.processcenter.service.IGroupService;
import com.haivera.processcenter.service.IUserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    /**
     * 处理数据
     * 1.先调用identityService.newUser()方法获得userEntity；
     * 2.补充userEntity中的数据；
     * 3.最后再调用identityService.saveUser()的方法；
     *
     * *** 注意***
     * *** 解决org.activiti.engine.ActivitiOptimisticLockingException问题 ***
     * 第三步saveUser()的参数必须是第一步中获得的userEntity，否则将会出现乐观锁的问题
     *
     */
    private void generalUser(User userEntity, User user){
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
    }

    @Autowired
    private IdentityService identityService;

    @Autowired
    private IGroupService groupService;

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

    @Override
    public void addUserList(List<UserEntityImpl> users, String systemId) {
        for(User user: users){
            this.addUser(user, systemId);
        }
    }

    @Override
    public void addUser(User user, String systemId) {
        User userEntity = identityService.newUser(IdCombine.combineId(systemId, user.getId()));
        generalUser(userEntity, user);
        identityService.saveUser(userEntity);
    }

    @Override
    public void delUser(User user, String systemId) {
        identityService.deleteUser(IdCombine.combineId(systemId, user.getId()));
    }

    @Override
    public void syncUserAndGroup(User user, Group group, String systemId) {
        this.addUser(user, systemId);
        groupService.addGroup(group, systemId);
        identityService.createMembership(IdCombine.combineId(systemId, user.getId()), IdCombine.combineId(systemId, group.getId()));
    }

    @Override
    public void syncUserAndGroup(HashMap<String, String> userAndGroup, String systemId) {
        for(String userId: userAndGroup.keySet()){
            identityService.createMembership(IdCombine.combineId(systemId, userId), IdCombine.combineId(systemId, userAndGroup.get(userId)));
        }
    }

    @Override
    public void syncUserAndGroup(List<UserEntityImpl> users, List<GroupEntityImpl> groups, HashMap<String, String> userAndGroup, String systemId) {
        this.addUserList(users, systemId);
        groupService.addGroupList(groups, systemId);
        for(String userId: userAndGroup.keySet()){
            identityService.createMembership(IdCombine.combineId(systemId, userId), IdCombine.combineId(systemId, userAndGroup.get(userId)));
        }
    }

}
