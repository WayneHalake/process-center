package com.haivera.processcenter.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.IGroupService;
import com.haivera.processcenter.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户、用户组（角色）、用户与用户组（角色）绑定关系
 */
@Api("用户、用户组（角色）、用户与用户组（角色）绑定关系")
@RequestMapping("/identityCtr")
@RestController
public class CommonIdentityCtr {

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IUserService userService;

    /**
     * 通过systemId获取用户列表
     * @param systemId
     * @return
     */
    @ApiOperation(value = "通过systemId获取用户列表", notes = "通过systemId获取用户列表")
    @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string")
    @GetMapping("/getUserListBySystemId")
    public ResponseInfo getUserListBySystemId(String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<User> userList = userService.getUserList(systemId);
            List<Object> datas = new ArrayList<>();
            for(User user: userList){
                UserEntityImpl userEntity = (UserEntityImpl) user;
                datas.add(userEntity.getPersistentState());
            }
            resp.doSuccess("获取用户列表成功", datas);
        }catch (Exception e){
            resp.doFailed("获取用户列表失败");
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * 通过systemId获取用户组列表
     * @param systemId
     * @return
     */
    @ApiOperation(value = "通过systemId获取用户组列表", notes = "通过systemId获取用户组列表")
    @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string")
    @GetMapping("/getGroupListBySystemId")
    public ResponseInfo getGroupListBySystemId(String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<Group> groupList = groupService.getGroupList(systemId);
            List<Object> datas = new ArrayList<>();
            for(Group group: groupList){
                GroupEntityImpl groupEntity = (GroupEntityImpl) group;
                datas.add(groupEntity);
            }
            resp.doSuccess("获取用户组列表成功", datas);
        }catch (Exception e){
            resp.doFailed("获取用户组列表失败");
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * 通过用户组获取用户列表
     * @param systemId
     * @param groupId
     * @return
     */
    @ApiOperation(value = "通过用户组获取用户列表", notes = "通过用户组获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "用户组id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/getUserListByGroupId")
    public ResponseInfo getUserListByGroupId(String groupId, String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<User> userList = userService.getUserListByGroup(groupId, systemId);
            List<Object> datas = new ArrayList<>();
            for(User user: userList){
                UserEntityImpl userEntity = (UserEntityImpl) user;
                datas.add(userEntity.getPersistentState());
            }
            resp.doSuccess("获取用户列表成功", datas);
        }catch (Exception e){
            resp.doFailed("获取用户列表失败");
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * 通过用户获取 用户所在的用户组信息
     * @param systemId
     * @param userId
     * @return
     */
    @ApiOperation(value = "通过用户获取 用户所在的用户组信息", notes = "通过用户获取 用户所在的用户组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/getGroupListByUserId")
    public ResponseInfo getGroupListByUserId(String userId, String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<Group> groupList = groupService.getGroupByUserId(systemId, userId);
            List<Object> datas = new ArrayList<>();
            for(Group group: groupList){
                GroupEntityImpl groupEntity = (GroupEntityImpl) group;
                datas.add(groupEntity);
            }
            resp.doSuccess("获取用户组列表成功", datas);
        }catch (Exception e){
            resp.doFailed("获取用户列表失败");
            e.printStackTrace();
        }
        return resp;
    }


    @ApiOperation(value = "同步用户信息", notes = "同步用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user", value = "用户信息", paramType = "body", required = true, dataType = "string"),
        @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @PostMapping("/syncUser")
    public ResponseInfo syncUser(@RequestBody UserEntityImpl user, @RequestParam("systemId") String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            userService.addUser(user, systemId);
            resp.doSuccess("同步用户信息成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("同步用户信息失败！", e);
        }
        return resp;
    }

    @ApiOperation(value = "同步用户信息(列表)", notes = "同步用户信息(列表)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "users", value = "用户信息", paramType = "body", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @PostMapping("/syncUserList")
    public ResponseInfo syncUserList(@RequestBody JSONObject users, @RequestParam("systemId") String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<UserEntityImpl> userList = JSONArray.parseArray(users.toJSONString(), UserEntityImpl.class);
            userService.addUserList(userList, systemId);
            resp.doSuccess("同步用户信息成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("同步用户信息失败！", e);
        }
        return resp;
    }

    @ApiOperation(value = "同步用户组（角色）信息", notes = "同步用户组（角色）信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group", value = "用户组（角色）信息", paramType = "body", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @PostMapping("/syncGroup")
    public ResponseInfo syncGroup(@RequestBody GroupEntityImpl group, @RequestParam("systemId") String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            groupService.addGroup(group, systemId);
            resp.doSuccess("同步用户组（角色）信息成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("同步用户组（角色）信息失败！", e);
        }
        return resp;
    }

    @ApiOperation(value = "同步用户组（角色）信息(列表)", notes = "同步用户组（角色）信息(列表)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groups", value = "用户组（角色）信息", paramType = "body", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @PostMapping("/syncGroupList")
    public ResponseInfo syncGroupList(@RequestBody JSONObject groups, @RequestParam("systemId") String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<GroupEntityImpl> groupList = JSONArray.parseArray(groups.toJSONString(), GroupEntityImpl.class);
            groupService.addGroupList(groupList, systemId);
            resp.doSuccess("同步用户组（角色）信息成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("同步用户组（角色）信息失败！", e);
        }
        return resp;
    }

    @ApiOperation(value = "同步用户、用户组（角色）信息", notes = "同步用户、用户组（角色）信息，默认当前用户与用户组（角色）关联")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "object", value = "同步用户、用户组（角色）信息", paramType = "body", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @PostMapping("/syncUserAndGroup")
    public ResponseInfo syncUserAndGroup(@RequestBody JSONObject object, @RequestParam("systemId") String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            User user = JSONObject.parseObject(object.get("user").toString(), UserEntityImpl.class);
            Group group = JSONObject.parseObject(object.get("group").toString(), GroupEntityImpl.class);
            userService.syncUserAndGroup(user, group, systemId);
            resp.doSuccess("同步用户、用户组（角色）信息成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("同步用户、用户组（角色）信息失败！", e);
        }
        return resp;
    }

    @ApiOperation(value = "同步用户与用户组（角色）关系", notes = "同步用户与用户组（角色）关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "object", value = "用户与用户组（角色）关系", paramType = "body", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @PostMapping("/synUserAndGroupMembership")
    public ResponseInfo synUserAndGroupMembership(@RequestBody JSONObject object, @RequestParam("systemId") String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            HashMap map = JSONObject.parseObject(object.toJSONString(), HashMap.class);
            userService.syncUserAndGroup(map, systemId);
            resp.doSuccess("同步用户与用户组（角色）关系成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("同步用户与用户组（角色）关系失败！", e);
        }
        return resp;
    }

    @ApiOperation(value = "同步用户、用户组（角色）关系和用户与用户组（角色）关系", notes = "同步用户、用户组（角色）关系和用户与用户组（角色）关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "object", value = "用户、用户组（角色）和用户与用户组（角色）关系", paramType = "body", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string")
    })
    @PostMapping("/synUsersAndGroups")
    public ResponseInfo synUsersAndGroups(@RequestBody JSONObject object, @RequestParam("systemId") String systemId){
        ResponseInfo resp = new ResponseInfo();
        try{
            List<UserEntityImpl> users = JSONArray.parseArray(object.get("users").toString(), UserEntityImpl.class);
            List<GroupEntityImpl> groups = JSONArray.parseArray(object.get("groups").toString(), GroupEntityImpl.class);
            HashMap map = JSONObject.parseObject(object.get("userAndGroup").toString(), HashMap.class);
            userService.syncUserAndGroup(users, groups, map, systemId);
            resp.doSuccess("同步用户、用户组（角色）关系和用户与用户组（角色）关系成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("同步用户、用户组（角色）关系和用户与用户组（角色）关系失败！", e);
        }
        return resp;
    }

}
