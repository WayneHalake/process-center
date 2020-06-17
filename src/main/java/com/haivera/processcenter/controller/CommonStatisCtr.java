package com.haivera.processcenter.controller;

import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonStatisSer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("流程和任务相关数量统计")
@RestController
@RequestMapping("/commonStatisCtr")
public class CommonStatisCtr {

    @Autowired
    private ICommonStatisSer commonStatisSer;

    //统计当前人员待处理的任务数量
    @ApiOperation(value = "统计当前人员待处理的任务数量", notes = "统计当前人员待处理的任务数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/countUpTodoTask")
    public ResponseInfo countUpTodoTask(String systemId, String userId){
        ResponseInfo responseInfo = new ResponseInfo();
        try{
            responseInfo = commonStatisSer.countUpTodoTask(systemId, userId);
        }catch (Exception e){
            responseInfo.doSuccess("统计当前人员待处理任务数量失败！", e);
        }
        return responseInfo;
    }

    //统计当前人员待处理的任务数量
    @ApiOperation(value = "统计当前人员待处理的任务数量", notes = "统计当前人员待处理的任务数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "processKey", value = "流程processKey", paramType = "query", required = true, dataType = "string"),
    })
    @GetMapping("/countUpTodoTaskByProcessKey")
    public ResponseInfo countUpTodoTaskByProcessKey(String systemId, String userId, String processKey){
        ResponseInfo responseInfo = new ResponseInfo();
        try{
            responseInfo = commonStatisSer.countUpTodoTask(systemId, userId, processKey);
        }catch (Exception e){
            responseInfo.doSuccess("统计当前人员待处理任务数量失败！", e);
        }
        return responseInfo;
    }

    @ApiOperation(value = "统计当前人员已处理的任务数量", notes = "统计当前人员已处理的任务数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/countUpDoneTask")
    public ResponseInfo countUpDoneTask(String systemId, String userId){
        ResponseInfo responseInfo = new ResponseInfo();
        try{
            responseInfo = commonStatisSer.countUpDoneTask(systemId, userId);
        }catch (Exception e){
            responseInfo.doSuccess("统计当前人员已处理任务数量失败！", e);
        }
        return responseInfo;
    }

    @ApiOperation(value = "统计当前人员已处理的任务数量", notes = "统计当前人员已处理的任务数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "processKey", value = "流程processKey", paramType = "query", required = true, dataType = "string"),
    })
    @GetMapping("/countUpDoneTaskByProcessKey")
    public ResponseInfo countUpDoneTaskByProcessKey(String systemId, String userId, String processKey){
        ResponseInfo responseInfo = new ResponseInfo();
        try{
            responseInfo = commonStatisSer.countUpDoneTask(systemId, userId, processKey);
        }catch (Exception e){
            responseInfo.doSuccess("统计当前人员已处理任务数量失败！", e);
        }
        return responseInfo;
    }

    @ApiOperation(value = "统计我发起的正在运行的流程", notes = "统计我发起的正在运行的流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/countUpActProcess")
    public ResponseInfo countUpActProcess(String systemId, String userId){
        ResponseInfo responseInfo = new ResponseInfo();
        try{
            responseInfo = commonStatisSer.countUpActProcess(systemId, userId);
        }catch (Exception e){
            responseInfo.doSuccess("统计我发起的正在运行的流程失败！", e);
        }
        return responseInfo;
    }

    @ApiOperation(value = "统计我发起的已结束的流程", notes = "统计我发起的已结束的流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemId", value = "系统标识id", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/countUpFinishProcess")
    public ResponseInfo countUpFinishProcess(String systemId, String userId){
        ResponseInfo responseInfo = new ResponseInfo();
        try{
            responseInfo = commonStatisSer.countUpFinishProcess(systemId, userId);
        }catch (Exception e){
            responseInfo.doSuccess("统计我发起的已结束的流程失败！", e);
        }
        return responseInfo;
    }

}
