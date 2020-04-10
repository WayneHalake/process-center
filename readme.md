# 工作流开发说明
   该工作流应作为一个独立的springboot项目运行，使用nacos作为注册中心，为其他的springboot服务提供接口调用。
   打包为jar的形式进行部署。
   > swagger地址 http://127.0.0.1:8092/swagger-ui.html

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

### 环境准备
  * 安装nacos
    1. nacos [下载](https://github.com/alibaba/nacos/releases/download/1.2.0/nacos-server-1.2.0.tar.gz)
    2. Linux 环境可直接使用一下命令下载 
        > wget https://github.com/alibaba/nacos/releases/download/1.2.0/nacos-server-1.2.0.tar.gz 
    3. 解压安装包  tar  -zxvf nacos-server-1.2.0.tar.gz
  * 启动nacos（已单机的方式启动） 
       >  nohup sh startup.sh -m standalone &
  * nacos默认端口为8848,注意保持服务器上端口开启并不被占用
  * 访问一下地址进入控制台界面
       > * http://{ip}:8848/nacos/index.html 
       > * 账号密码账号:nacos 密码：nacos
  * 如本模块启动成功，可在nacos控制台的服务管理-->服务列表中看到一个服务名为process-center的服务
  * 本地环境可配置服务器上的nacos使用，无需在本地安装nacos，在配置文件中添加以下配置：
    
         spring:
          cloud:
            nacos:
              discovery:
                server-addr: 192.168.1.204:8848
  
### 提供接口
 | 接口 | 参数 | 返回值| 描述 |
 | :---- | :---- | :----: | :----: |
 | post /processCtr/createPro | String processKey | 流程实例id | 创建流程 提供流程的processKey |