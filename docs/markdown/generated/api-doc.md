# process-center RESTful API


<a name="overview"></a>
## 概览
工作流api接口


### 版本信息
*版本* : 1.0


### URI scheme
*域名* : 127.0.0.1:8092  
*基础路径* : /


### 标签

* common-form-ctr : Common Form Ctr
* common-identity-ctr : Common Identity Ctr
* common-process-ctr : Common Process Ctr
* common-task-ctr : Common Task Ctr




<a name="paths"></a>
## 资源

<a name="common-form-ctr_resource"></a>
### Common-form-ctr
Common Form Ctr


<a name="getstartformdatausingget"></a>
#### 获取开始事件上的表单
```
GET /commonForm/getStartFormData
```


##### 说明
获取开始事件上的表单


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processKey**  <br>*必填*|流程定义processKey|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/getStartFormData
```


###### 请求 query
```
json :
{
  "processKey" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="gettaskformdatausingget"></a>
#### 获取任务节点上的表单
```
GET /commonForm/getTaskFormData
```


##### 说明
获取任务节点上的表单


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|taskId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/getTaskFormData
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="getvariablesusingget"></a>
#### 获取任务变量
```
GET /commonForm/getVariables
```


##### 说明
获取任务变量（可用于获取提交至任务表单中的数据），将获取所有的任务变量


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|taskId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/getVariables
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="getvariableslocalusingget"></a>
#### 获取任务变量
```
GET /commonForm/getVariablesLocal
```


##### 说明
获取任务变量 仅仅会获取只 @setVariablesLocal 设置的变量


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|taskId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/getVariablesLocal
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="saveformdatausingpost"></a>
#### 保存任务节点表单，不会完成任务（不会修改任务状态）
```
POST /commonForm/saveFormData
```


##### 说明
保存任务节点表单，不会完成任务（不会修改任务状态）


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|taskId|string|
|**Body**|**properties**  <br>*必填*|任务节点表单数据|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/saveFormData
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="setvariablesusingpost"></a>
#### 设置任务变量
```
POST /commonForm/setVariables
```


##### 说明
设置任务变量。任务变量会随着流程的进行，传递到每一个任务节点。任务必须在运行状态


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|taskId|string|
|**Body**|**properties**  <br>*必填*|任务节点表单数据|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/setVariables
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="setvariableslocalusingpost"></a>
#### 设置任务变量
```
POST /commonForm/setVariablesLocal
```


##### 说明
设置的变量仅仅存在当前的任务节点中，不会随着流程的进行传递到后续的任务中。任务必须在运行状态


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|taskId|string|
|**Body**|**properties**  <br>*必填*|任务节点表单数据|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/setVariablesLocal
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="submitstartformdatausingpost"></a>
#### 提交流程开始节点表单，并完成开始节点
```
POST /commonForm/submitStartFormData
```


##### 说明
提交流程开始节点表单，并完成开始节点


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processKey**  <br>*必填*|流程定义processKey|string|
|**Body**|**properties**  <br>*必填*|任务节点表单数据|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/submitStartFormData
```


###### 请求 query
```
json :
{
  "processKey" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="submittaskformdatausingpost"></a>
#### 提交任务节点表单，并完成任务
```
POST /commonForm/submitTaskFormData
```


##### 说明
提交任务节点表单，并完成任务


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|taskId|string|
|**Body**|**properties**  <br>*必填*|任务节点表单数据|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/commonForm/submitTaskFormData
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="common-identity-ctr_resource"></a>
### Common-identity-ctr
Common Identity Ctr


<a name="synuserandgroupmembershipusingpost"></a>
#### 同步用户与用户组（角色）关系
```
POST /identityCtr/synUserAndGroupMembership
```


##### 说明
同步用户与用户组（角色）关系


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统id|string|
|**Body**|**object**  <br>*必填*|用户与用户组（角色）关系|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/identityCtr/synUserAndGroupMembership
```


###### 请求 query
```
json :
{
  "systemId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="synusersandgroupsusingpost"></a>
#### 同步用户、用户组（角色）关系和用户与用户组（角色）关系
```
POST /identityCtr/synUsersAndGroups
```


##### 说明
同步用户、用户组（角色）关系和用户与用户组（角色）关系


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统id|string|
|**Body**|**object**  <br>*必填*|用户、用户组（角色）和用户与用户组（角色）关系|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/identityCtr/synUsersAndGroups
```


###### 请求 query
```
json :
{
  "systemId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="syncgroupusingpost"></a>
#### 同步用户组（角色）信息
```
POST /identityCtr/syncGroup
```


##### 说明
同步用户组（角色）信息


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统id|string|
|**Body**|**group**  <br>*必填*|用户组（角色）信息|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/identityCtr/syncGroup
```


###### 请求 query
```
json :
{
  "systemId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="syncgrouplistusingpost"></a>
#### 同步用户组（角色）信息(列表)
```
POST /identityCtr/syncGroupList
```


##### 说明
同步用户组（角色）信息(列表)


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统id|string|
|**Body**|**groups**  <br>*必填*|用户组（角色）信息|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/identityCtr/syncGroupList
```


###### 请求 query
```
json :
{
  "systemId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="syncuserusingpost"></a>
#### 同步用户信息
```
POST /identityCtr/syncUser
```


##### 说明
同步用户信息


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统id|string|
|**Body**|**user**  <br>*必填*|用户信息|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/identityCtr/syncUser
```


###### 请求 query
```
json :
{
  "systemId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="syncuserandgroupusingpost"></a>
#### 同步用户、用户组（角色）信息
```
POST /identityCtr/syncUserAndGroup
```


##### 说明
同步用户、用户组（角色）信息，默认当前用户与用户组（角色）关联


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统id|string|
|**Body**|**object**  <br>*必填*|同步用户、用户组（角色）信息|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/identityCtr/syncUserAndGroup
```


###### 请求 query
```
json :
{
  "systemId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="syncuserlistusingpost"></a>
#### 同步用户信息(列表)
```
POST /identityCtr/syncUserList
```


##### 说明
同步用户信息(列表)


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统id|string|
|**Body**|**users**  <br>*必填*|用户信息|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/identityCtr/syncUserList
```


###### 请求 query
```
json :
{
  "systemId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="common-process-ctr_resource"></a>
### Common-process-ctr
Common Process Ctr


<a name="activateprocessusingpost"></a>
#### 启动挂起的流程
```
POST /processCtr/activateProcess
```


##### 说明
通过流程实例processId启动挂起的流程


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processId**  <br>*必填*|流程实例processId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|无内容|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/activateProcess
```


###### 请求 query
```
json :
{
  "processId" : "string"
}
```


<a name="createprousingpost_1"></a>
#### 创建流程
```
POST /processCtr/createPro
```


##### 说明
通过processKey创建流程


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processKey**  <br>*必填*|流程图processKey|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/createPro
```


###### 请求 query
```
json :
{
  "processKey" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="createprousingpost"></a>
#### 创建流程
```
POST /processCtr/createPro4bus
```


##### 说明
通过processKey创建流程,需指定busCode、busType


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**busCode**  <br>*必填*|业务编码|string|
|**Query**|**busType**  <br>*必填*|业务类型|string|
|**Query**|**processKey**  <br>*必填*|流程图processKey|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/createPro4bus
```


###### 请求 query
```
json :
{
  "busCode" : "string",
  "busType" : "string",
  "processKey" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="deploymentprocessusingpost"></a>
#### 手动发布流程
```
POST /processCtr/deploymentProcess
```


##### 说明
手动发布流程


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**FormData**|**file**  <br>*必填*|file|file|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `multipart/form-data`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/deploymentProcess
```


###### 请求 formData
```
json :
"file"
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="getimageusingget"></a>
#### 获取流程图
```
GET /processCtr/getImage
```


##### 说明
获取流程图


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**lightFlag**  <br>*必填*|是否高亮当前任务节点|boolean|
|**Query**|**processId**  <br>*必填*|流程实例processId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/getImage
```


###### 请求 query
```
json :
{
  "lightFlag" : true,
  "processId" : "string"
}
```


<a name="getprocessbyprocessidusingget"></a>
#### 通过流程id获取流程实例
```
GET /processCtr/getProcessByProcessId
```


##### 说明
通过流程流程实例processId获取流程实例


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processId**  <br>*必填*|流程实例processId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/getProcessByProcessId
```


###### 请求 query
```
json :
{
  "processId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="getprocessbytaskidusingget"></a>
#### 通过任务id获取流程实例
```
GET /processCtr/getProcessByTaskId
```


##### 说明
通过任务taskId获取流程实例


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/getProcessByTaskId
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="listallprocessinstanceusingget"></a>
#### 获取所有正在运行的流程实例
```
GET /processCtr/listAllProcessInstance
```


##### 说明
获取所有正在运行的流程实例


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/listAllProcessInstance
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="listhistoryusingget"></a>
#### 获取流程的历史记录
```
GET /processCtr/listHistory
```


##### 说明
获取流程的历史记录


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processId**  <br>*必填*|流程实例processId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/listHistory
```


###### 请求 query
```
json :
{
  "processId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="listprocessusingget"></a>
#### 流程实例列表
```
GET /processCtr/listProcess
```


##### 说明
通过processKey查找流程实例列表


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processKey**  <br>*必填*|流程图processKey|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/listProcess
```


###### 请求 query
```
json :
{
  "processKey" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="suspendprocessusingpost"></a>
#### 挂起流程
```
POST /processCtr/suspendProcess
```


##### 说明
通过流程实例processId挂起流程


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processId**  <br>*必填*|流程实例processId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|无内容|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/processCtr/suspendProcess
```


###### 请求 query
```
json :
{
  "processId" : "string"
}
```


<a name="common-task-ctr_resource"></a>
### Common-task-ctr
Common Task Ctr


<a name="addcandidategroupusingpost"></a>
#### 设置任务候选用户组
```
POST /taskCtr/addCandidateGroup
```


##### 说明
设置候选任务用户组，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**groupId**  <br>*必填*|用户组id|string|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/addCandidateGroup
```


###### 请求 query
```
json :
{
  "groupId" : "string",
  "systemId" : "string",
  "taskId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="addcandidategroupusingpost_1"></a>
#### 设置任务候选用户组列表
```
POST /taskCtr/addCandidateGroups
```


##### 说明
设置候选任务用户组，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**groupIds**  <br>*必填*|用户组id列表|string|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/addCandidateGroups
```


###### 请求 query
```
json :
{
  "groupIds" : "string",
  "systemId" : "string",
  "taskId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="addcandidateuserusingpost"></a>
#### 设置任务候选处理人
```
POST /taskCtr/addCandidateUser
```


##### 说明
设置候选任务处理人，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|
|**Query**|**userId**  <br>*必填*|用户id|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/addCandidateUser
```


###### 请求 query
```
json :
{
  "systemId" : "string",
  "taskId" : "string",
  "userId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="addcandidateuserusingpost_1"></a>
#### 设置任务候选处理人列表
```
POST /taskCtr/addCandidateUsers
```


##### 说明
设置候选任务处理人，多个候选人中只要有一个候选人操作过该节点，及完成该节点的操作


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|
|**Query**|**userIds**  <br>*必填*|用户id列表|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/addCandidateUsers
```


###### 请求 query
```
json :
{
  "systemId" : "string",
  "taskId" : "string",
  "userIds" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="completetaskusingpost"></a>
#### 完成一个任务(非委托任务)
```
POST /taskCtr/complete
```


##### 说明
完成一个任务(非委托任务)


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|
|**Query**|**userId**  <br>*必填*|用户id|string|
|**Body**|**varAndTransientVar**  <br>*可选*|任务所需参数|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/complete
```


###### 请求 query
```
json :
{
  "systemId" : "string",
  "taskId" : "string",
  "userId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="currenttaskusingget"></a>
#### 流程实例所在节点
```
GET /taskCtr/currentTask
```


##### 说明
通过processId查找流程实例所在节点


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processId**  <br>*必填*|流程实例processId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/currentTask
```


###### 请求 query
```
json :
{
  "processId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="delegatetaskusingpost"></a>
#### 设置任务委托人
```
POST /taskCtr/delegateTask
```


##### 说明
设置任务委托人


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|
|**Query**|**userId**  <br>*必填*|用户id|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/delegateTask
```


###### 请求 query
```
json :
{
  "systemId" : "string",
  "taskId" : "string",
  "userId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="getidentitylinksfortaskusingget"></a>
#### 获取任务处理候选用户
```
GET /taskCtr/getIdentityLinksForTask
```


##### 说明
获取任务处理候选用户


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/getIdentityLinksForTask
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="gettaskbyidusingget"></a>
#### 通过任务id获取任务
```
GET /taskCtr/getTaskById
```


##### 说明
通过任务id获取任务


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|任务id|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/getTaskById
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="listruntaskusingget"></a>
#### 流程实例所在节点--返回多个节点
```
GET /taskCtr/listRunTask
```


##### 说明
通过processId查找流程实例所在节点


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processId**  <br>*必填*|流程实例processId|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/listRunTask
```


###### 请求 query
```
json :
{
  "processId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="listtaskusingget"></a>
#### 获取当前用户在processkey流程中任务
```
GET /taskCtr/listTaskByProcessKey
```


##### 说明
获取当前用户在processkey流程中任务


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**processKey**  <br>*必填*|流程processKey|string|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**userId**  <br>*必填*|用户id|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/listTaskByProcessKey
```


###### 请求 query
```
json :
{
  "processKey" : "string",
  "systemId" : "string",
  "userId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="listtaskusingget_1"></a>
#### 获取当前用户所有任务
```
GET /taskCtr/listTaskByUserId
```


##### 说明
获取当前用户所有任务


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**userId**  <br>*必填*|用户id|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/listTaskByUserId
```


###### 请求 query
```
json :
{
  "systemId" : "string",
  "userId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="resolvetaskusingpost"></a>
#### 处理委托任务
```
POST /taskCtr/resolveTask
```


##### 说明
处理委托任务


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**taskId**  <br>*必填*|任务taskId|string|
|**Body**|**varAndTransientVar**  <br>*可选*|任务所需参数|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/resolveTask
```


###### 请求 query
```
json :
{
  "taskId" : "string"
}
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```


<a name="setassignerusingpost"></a>
#### 设置任务处理人
```
POST /taskCtr/setAssigner
```


##### 说明
设置任务处理人


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**systemId**  <br>*必填*|系统标识id|string|
|**Query**|**taskId**  <br>*必填*|任务id|string|
|**Query**|**userId**  <br>*必填*|用户id|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[ResponseInfo](#responseinfo)|
|**201**|Created|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|
|**404**|Not Found|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/taskCtr/setAssigner
```


###### 请求 query
```
json :
{
  "systemId" : "string",
  "taskId" : "string",
  "userId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "data" : "object",
  "rtnCode" : "string",
  "rtnMsg" : "string"
}
```




<a name="definitions"></a>
## 定义

<a name="bytearrayentity"></a>
### ByteArrayEntity

|名称|说明|类型|
|---|---|---|
|**bytes**  <br>*可选*|**模式** : `"^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==\|[A-Za-z0-9+/]{3}=)?$"`**样例** : `"string"`|string (byte)|
|**deleted**  <br>*可选*|**样例** : `true`|boolean|
|**deploymentId**  <br>*可选*|**样例** : `"string"`|string|
|**id**  <br>*可选*|**样例** : `"string"`|string|
|**inserted**  <br>*可选*|**样例** : `true`|boolean|
|**name**  <br>*可选*|**样例** : `"string"`|string|
|**persistentState**  <br>*可选*|**样例** : `"object"`|object|
|**revision**  <br>*可选*|**样例** : `0`|integer (int32)|
|**revisionNext**  <br>*可选*|**样例** : `0`|integer (int32)|
|**updated**  <br>*可选*|**样例** : `true`|boolean|


<a name="bytearrayref"></a>
### ByteArrayRef

|名称|说明|类型|
|---|---|---|
|**bytes**  <br>*可选*|**模式** : `"^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==\|[A-Za-z0-9+/]{3}=)?$"`**样例** : `"string"`|string (byte)|
|**deleted**  <br>*可选*|**样例** : `true`|boolean|
|**entity**  <br>*可选*|**样例** : `"[bytearrayentity](#bytearrayentity)"`|[ByteArrayEntity](#bytearrayentity)|
|**id**  <br>*可选*|**样例** : `"string"`|string|
|**name**  <br>*可选*|**样例** : `"string"`|string|


<a name="groupentityimpl"></a>
### GroupEntityImpl

|名称|说明|类型|
|---|---|---|
|**deleted**  <br>*可选*|**样例** : `true`|boolean|
|**id**  <br>*可选*|**样例** : `"string"`|string|
|**inserted**  <br>*可选*|**样例** : `true`|boolean|
|**name**  <br>*可选*|**样例** : `"string"`|string|
|**persistentState**  <br>*可选*|**样例** : `"object"`|object|
|**revision**  <br>*可选*|**样例** : `0`|integer (int32)|
|**revisionNext**  <br>*可选*|**样例** : `0`|integer (int32)|
|**type**  <br>*可选*|**样例** : `"string"`|string|
|**updated**  <br>*可选*|**样例** : `true`|boolean|


<a name="inputstream"></a>
### InputStream
*类型* : object


<a name="picture"></a>
### Picture

|名称|说明|类型|
|---|---|---|
|**bytes**  <br>*可选*|**模式** : `"^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==\|[A-Za-z0-9+/]{3}=)?$"`**样例** : `"string"`|string (byte)|
|**inputStream**  <br>*可选*|**样例** : `"[inputstream](#inputstream)"`|[InputStream](#inputstream)|
|**mimeType**  <br>*可选*|**样例** : `"string"`|string|


<a name="responseinfo"></a>
### ResponseInfo

|名称|说明|类型|
|---|---|---|
|**data**  <br>*可选*|**样例** : `"object"`|object|
|**rtnCode**  <br>*可选*|**样例** : `"string"`|string|
|**rtnMsg**  <br>*可选*|**样例** : `"string"`|string|


<a name="userentityimpl"></a>
### UserEntityImpl

|名称|说明|类型|
|---|---|---|
|**deleted**  <br>*可选*|**样例** : `true`|boolean|
|**email**  <br>*可选*|**样例** : `"string"`|string|
|**firstName**  <br>*可选*|**样例** : `"string"`|string|
|**id**  <br>*可选*|**样例** : `"string"`|string|
|**inserted**  <br>*可选*|**样例** : `true`|boolean|
|**lastName**  <br>*可选*|**样例** : `"string"`|string|
|**password**  <br>*可选*|**样例** : `"string"`|string|
|**persistentState**  <br>*可选*|**样例** : `"object"`|object|
|**picture**  <br>*可选*|**样例** : `"[picture](#picture)"`|[Picture](#picture)|
|**pictureByteArrayRef**  <br>*可选*|**样例** : `"[bytearrayref](#bytearrayref)"`|[ByteArrayRef](#bytearrayref)|
|**pictureSet**  <br>*可选*|**样例** : `true`|boolean|
|**revision**  <br>*可选*|**样例** : `0`|integer (int32)|
|**revisionNext**  <br>*可选*|**样例** : `0`|integer (int32)|
|**updated**  <br>*可选*|**样例** : `true`|boolean|





