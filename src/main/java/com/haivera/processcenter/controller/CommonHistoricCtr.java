package com.haivera.processcenter.controller;

import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonHistoricSer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("历史记录相关操作接口")
@RestController
@RequestMapping("/commonHistoricCtr")
public class CommonHistoricCtr {

    @Autowired
    private ICommonHistoricSer ICommonHistoricSer;

    /**
     * 获取流程实例记录
     * @param processKey
     * @return
     */
    @ApiOperation(value = "通过processKey获取流程实例历史记录", notes = "通过processKey获取流程实例历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程定义key", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "finishFlag", value = "流程是否结束", paramType = "query", required = true, dataType = "string"),
    })
    @GetMapping("/listHisProcessInstanceByKey")
    public ResponseInfo listHisProcessInstanceByKey(String processKey, boolean finishFlag){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisProcessInstance(processKey, finishFlag);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程实例历史记录失败", e);
        }
        return resp;
    }

    @ApiOperation(value = "通过流程发起人获取流程实例历史记录", notes = "通过流程发起人获取流程实例历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "userId用户id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "finishFlag", value = "流程是否结束", paramType = "query", required = true, dataType = "string"),
    })
    @GetMapping("/listHisProcessInstanceByUser")
    public ResponseInfo listHisProcessInstanceByUser(String systemId, String userId, boolean finishFlag){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisProcessInstance(systemId, userId, finishFlag);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程实例历史记录失败", e);
        }
        return resp;
    }


    @ApiOperation(value = "通过流程processKey、发起人获取流程实例历史记录", notes = "通过流程processKey、发起人获取流程实例历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程定义key", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "userId用户id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "finishFlag", value = "流程是否结束", paramType = "query", required = true, dataType = "string"),
    })
    @GetMapping("/listHisProcessInstanceByKeyAndUser")
    public ResponseInfo listHisProcessInstanceByKeyAndUser(String processKey, String systemId, String userId, boolean finishFlag){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisProcessInstance(processKey, systemId, userId, finishFlag);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程实例历史记录失败", e);
        }
        return resp;
    }

    /**
     * 获取流程活动历史记录
     * @param processKey
     * @return
     */
    @ApiOperation(value = "通过processKey获取流程活动历史记录", notes = "通过processKey获取流程活动（流程的所有记录）历史记录")
    @ApiImplicitParam(name = "processKey", value = "流程定义key", paramType = "query", required = true, dataType = "string")
    @GetMapping("/listHisProcessByKey")
    public ResponseInfo listHisProcessByKey(String processKey){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisProcess(processKey);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程活动历史记录失败", e);
        }
        return resp;
    }


    @ApiOperation(value = "通过流程发起人获取流程活动历史记录", notes = "通过流程发起人获取流程活动（流程的所有记录）历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "userId用户id", paramType = "query", required = true, dataType = "string"),
    })
    @GetMapping("/listHisProcessByUser")
    public ResponseInfo listHisProcessByUser(String systemId, String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisProcess(systemId, userId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程活动历史记录失败", e);
        }
        return resp;
    }

    @ApiOperation(value = "通过流程processKey、任务处理人获取流程活动历史记录", notes = "通过流程processKey、任务处理人获取流程活动历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程定义key", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "userId用户id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/listHisProcessByKeyAndUser")
    public ResponseInfo listHisProcess(String processKey, String systemId, String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisProcess(processKey, systemId, userId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程活动历史记录失败", e);
        }
        return resp;
    }

    /**
     * 获取任务历史记录
     * @param processId
     * @return
     */
    @ApiOperation(value = "通过processId获取流程的任务历史记录", notes = "通过processId获取流程的任务历史记录")
    @ApiImplicitParam(name = "processId", value = "流程实例id", paramType = "query", required = true, dataType = "string")
    @GetMapping("/listHisTaskById")
    public ResponseInfo listHisTask(String processId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisTask(processId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程的任务历史记录失败", e);
        }
        return resp;
    }

    @ApiOperation(value = "通过流程processId、任务名称获取流程任务历史记录", notes = "通过流程processId、任务名称获取流程任务历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "taskName", value = "任务节点名称", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/listHisTaskByIdAndTaskName")
    public ResponseInfo listHisTask(String processId, String taskName){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisTask(processId, taskName);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程的任务历史记录失败", e);
        }
        return resp;
    }

    @ApiOperation(value = "通过流程processId、任务处理人获取流程任务历史记录", notes = "通过流程processId、任务处理人获取流程任务历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "userId用户id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/listHisTaskByIdAndUser")
    public ResponseInfo listHisTask(String processId, String systemId, String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisTask(processId, systemId, userId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程的任务历史记录失败", e);
        }
        return resp;
    }

    @ApiOperation(value = "通过流程任务处理人获取流程任务历史记录", notes = "通过流程任务处理人获取流程任务历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "userId用户id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/listHisTaskByUser")
    public ResponseInfo listHisTaskByUser(String systemId, String userId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisTaskByAssignee(systemId, userId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程的任务历史记录失败", e);
        }
        return resp;
    }


    @ApiOperation(value = "通过流程processId、任务处理人、任务名称获取流程任务历史记录", notes = "通过流程processId、任务处理人、任务名称获取流程任务历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "systemId", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "userId用户id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "taskName", value = "任务节点名称", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/listHisTask")
    public ResponseInfo listHisTask(String processId, String systemId, String userId, String taskName){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisTask(processId, systemId, userId, taskName);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程的任务历史记录失败", e);
        }
        return resp;
    }

    /**
     * 获取流程变量历史记录
     * @param processId
     * @return
     */
    @ApiOperation(value = "通过processId获取流程变量历史记录", notes = "通过processId获取流程变量历史记录")
    @ApiImplicitParam(name = "processId", value = "流程实例id", paramType = "query", required = true, dataType = "string")
    @GetMapping("/listHisVariableById")
    public ResponseInfo listHisVariable(String processId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisVariable(processId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程变量历史记录失败", e);
        }
        return resp;
    }

    /**
     * 传入taskId参数之后仅会查出variablesLocal
     * @param processId
     * @param taskId
     * @return
     */
    @ApiOperation(value = "通过流程processId、任务id获取流程变量历史记录", notes = "通过流程processId、任务id获取流程变量历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "taskId", value = "任务id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/listHisVariableByIdAndTaskId")
    public ResponseInfo listHisVariable(String processId, String taskId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisVariable(processId, taskId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程变量历史记录失败", e);
        }
        return resp;
    }

    /**
     * 仅查询variablesLocal
     * @param taskId
     * @return
     */
    @ApiOperation(value = "通过taskId获取流程变量历史记录", notes = "通过taskId获取流程变量历史记录")
    @ApiImplicitParam(name = "taskId", value = "任务Id", paramType = "query", required = true, dataType = "string")
    @GetMapping("/listHisVariableByTaskId")
    public ResponseInfo listHisVariableByTaskId(String taskId){
        ResponseInfo resp = new ResponseInfo();
        try{
            resp = ICommonHistoricSer.listHisVariableByTaskId(taskId);
        }catch (Exception e){
            e.printStackTrace();
            resp.doFailed("获取流程变量历史记录失败", e);
        }
        return resp;
    }

}
