# 工作流开发说明

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

### 提供接口
   > * processKey 流程key，在创建流程图时，可指定流程的processKey
   > * processId 流程实例id，流程实例化时，将提供对应的processId
   
   接口详情参考swagger地址
   > swagger地址 http://127.0.0.1:8092/swagger-ui.html
##### CommonProcessCtr
    流程处理接口
    
 | 请求类型 | 接口 | 参数 | 返回值| 描述 |
 | :--- | :---- | :---- | :----: | :----: |
 | post |/processCtr/createPro | String processKey | 流程实例id String | 创建流程 |
 | get |/processCtr/listProcess | String processKey | 流程实例列表 List<ExecutionEntityImpl> | 查找流程实例列表 |
 | get |/processCtr/listAllProcessInstance |  | 流程实例列表 List<ExecutionEntityImpl> | 获取所有正在运行的流程实例 |
 | post |/processCtr/suspendProcess | String processId |  | 挂起流程 |
 | post |/processCtr/activateProcess | String processId |  | 启动流程 |
 | get |/processCtr/getProcessByProcessId | String processId | 流程实例 ExecutionEntityImpl  | 通过processId获取流程实例 |
 | get |/processCtr/listHistory | String processId | 流程历史记录 List<HistoricActivityInstance> | 获取流程的历史记录 | 
 | get |/processCtr/getProcessByTaskId | String taskId | 流程实例 ExecutionEntityImpl | 通过任务id获取流程实例 |
 | get |/processCtr/getImage | HttpServletResponse response, String processId, boolean lightFlag |  | 获取流程图 |

##### CommonIdentityCtr
    用户、用户组（角色）、用户与用户组（角色）绑定关系
   > * systemId 对应系统id，目的：作为区分不同系统的标识
   > * 参数中的对象，请使用JSON数据 
   
| 请求类型 | 接口 | 参数 | 返回值| 描述 |
| :--- | :---- | :---- | :----: | :----: |
| post |/identityCtr/syncUser | UserEntityImpl user,String systemId |  | 同步用户信息 |
| post |/identityCtr/syncUserList | JSONObject users,String systemId |  | 同步用户信息（列表） |
| post |/identityCtr/syncGroup | GroupEntityImpl group,String systemId |  | 同步用户组（角色）信息 |
| post |/identityCtr/syncGroupList | JSONObject groups,String systemId |  | 同步用户组（角色）信息(列表) |
| post |/identityCtr/syncUserAndGroup | JSONObject object,String systemId |  | 同步用户、用户组（角色）信息，默认当前用户与用户组（角色）关联 |
| post |/identityCtr/synUserAndGroupMembership | JSONObject object,String systemId |  | 同步用户与用户组（角色）关系 |
| post |/identityCtr/synUsersAndGroups | JSONObject object,String systemId |  | 同步用户、用户组（角色）关系和用户与用户组（角色）关系 |

##### CommonFormCtr
    任务表单处理
   > * 参数中的对象，请使用JSON数据 
   
| 请求类型 | 接口 | 参数 | 返回值| 描述 |
| :--- | :---- | :---- | :----: | :----: |
| get |/commonForm/getStartFormData | String processKey |  | 获取开始事件上的表单数据 |
| get |/commonForm/getTaskFormData | String taskId |  | 获取任务节点上的表单数据 |
| post |/commonForm/saveFormData | String taskId, JSONObject properties |  | 保存任务节点表单，不会完成任务（不会修改任务状态） |
| post |/commonForm/submitStartFormData | String processKey, JSONObject properties |  | 提交流程开始节点表单，并完成开始节点 |
| post |/commonForm/submitTaskFormData | String taskId, JSONObject properties |  | 提交任务节点表单，并完成任务 |


##### CommonTaskCtr
    任务处理
   > * systemId 对应系统id，目的：作为区分不同系统的标识
   > * 参数中的对象，请使用JSON数据 
   
| 接口 | 参数 | 返回值| 描述 |
| :---- | :---- | :----: | :----: |
|POST /taskCtr/setAssigner | String taskId,String systemId,String userId |  | 设置任务处理人 |
|POST /taskCtr/complete | String taskId, JSONObject variables, JSONObject transientVariables, String systemId, String userId | 下一任务Id | 完成一个任务(非委托任务) |
|POST /taskCtr/delegateTask | String taskId,String systemId,String userId |  | 设置任务委托人 |
|POST /taskCtr/resolveTask | String taskId, JSONObject variables, JSONObject transientVariables, |  | 处理委托任务 |
|GET /taskCtr/currentTask | String processId | 任务节点信息 | 通过processId查找流程实例所在节点 |
|GET /taskCtr/getTaskById | String taskId | 任务节点信息 | 通过任务id获取任务 |
|GET /taskCtr/listRunTask | String processId | 任务列表 List<TaskEntityImlp> | 流程实例所在节点--返回个多节点 |
|GET /taskCtr/listTaskByProcessKey| String processKey, String systemId, String userId | 任务列表 List<TaskEntityImlp> | 获取当前用户在processkey流程中任务 |
|GET /taskCtr/listTaskByUserId | String taskId, HashMap map, String systemId, String userId | 下一个任务Id String | 完成一个任务 |
|POST /taskCtr/addCandidateGroup | String taskId,String systemId,String groupId |  | 设置候选任务用户组 |
|POST /taskCtr/addCandidateGroups | String taskId,String systemId,List<String> groupIds  |  | 设置候选任务用户组(列表) |
|POST /taskCtr/addCandidateUser | String taskId,String systemId,String userId  |  | 设置候选任务处理人 |
|POST /taskCtr/addCandidateUsers | String taskId,String systemId,List<String> userId |  | 设置候选任务处理人(列表) |
|GET /taskCtr/getIdentityLinksForTask | String taskId | 任务处理候选用户信息 | 获取任务处理候选用户 |



> 项目间歇性更新
