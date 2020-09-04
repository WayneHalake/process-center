package com.haivera.processcenter;

import com.haivera.processcenter.common.util.ResponseInfo;
import com.haivera.processcenter.service.ICommonProcessSer;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.rmi.log.LogInputStream;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TextApplication {

    @Autowired
    private ICommonProcessSer commonProcessSer;

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void delProcess() throws Exception {
        ResponseInfo<List<HashMap>> resp = commonProcessSer.listAllProcessInstance();
        List<HashMap> datas = resp.getData();
        for(HashMap map: datas){
            try{
                String processId = map.get("processId").toString();
                runtimeService.deleteProcessInstance(processId, "");
            }catch (ActivitiObjectNotFoundException e){
                continue;
            }

        }
    }

    @Test
    public void activateProcess() throws Exception{
        commonProcessSer.activateProcess("subProcessA:3:3255");
    }


}
