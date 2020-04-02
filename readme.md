# 工作流开发说明
    该工作流应作为一个独立的springboot项目运行。
	使用nacos作为注册中心，为其他的springboot服务提供接口调用。
	添加feign相关依赖，可使用feign实现接口化调用
    打包为jar的形式进行部署。

### 流程开发注意事项
* 首先在activiti-admin中定义好流程图，然后导出成dpmn文件；
* 添加到resource/processes文件夹的相关文件夹下面（eg:insurance-admin的流程对应的流程图，添加至insuranceAdmin文件夹）；
* 在controller中添加相关流程的业务逻辑（eg:insurance-admin的流程对应的流程图，添加至controller.insuranceadmin包）；
* controller中提供了公共的流程接口，无特殊流程的情况下可优先调用CommonProcessCtr中的接口；
* 系统调用controller接口时，应提供相应的系统id（eg:insurance-admin系统的系统id为insuranceadmin）；

### 流程用户与用户组相关
* 同步相关系统的用户id和用户组（角色）以系统推送的方式（eg:insurance-admin系统的用户和角色推送到process-center）；
* 用户id在process-center中保存的方式为：系统id_用户id（eg:insurance-admin系统中的用户id：Wkbzoq31，在process-center中对应的用户id为：insuranceadmin_Wkbzoq31）；
* 用户组对应为系统的角色，用户组在process-center中的保存方式为：系统id_角色id（eg：insurance-admin系统中的角色id：c9bb3，在process-center中对应的用户组id：insuranceadmin_c9bb3）；
