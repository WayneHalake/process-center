package com.haivera.processcenter.controller;

import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonBusSer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("通过businessKey获取流程基本信息")
@RestController
@RequestMapping("/commonBusCtr")
public class CommonBusCtr {

    @Autowired
    private ICommonBusSer commonBusSer;

    @ApiOperation(value = "通过businessKey获取流程基本信息", notes = "通过businessKey获取流程基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessKey", value = "业务编号", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string"),
    })
    @GetMapping("/getBaseInfo")
    public ResponseInfo getBaseInfo(String businessKey, String systemId){
        return commonBusSer.getBaseInfo(businessKey, systemId);
    }

    @ApiOperation(value = "通过businessKey获取流程id", notes = "通过businessKey获取流程id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessKey", value = "业务编号", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "systemId", value = "系统id", paramType = "query", required = true, dataType = "string"),
    })
    @GetMapping("/getProcessId")
    public String getProcessId(String businessKey, String systemId){
        return commonBusSer.getProcessId(businessKey, systemId);
    }

}
