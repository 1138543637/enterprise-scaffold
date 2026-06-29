## S0-04 用户分页查询

### 接口名称

用户分页查询

### 请求方式

GET

### 请求路径

/api/system/users/page

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| pageNo | number | 否 | 当前页，默认 1 |
| pageSize | number | 否 | 每页条数，默认 10，最大 100 |
| username | string | 否 | 登录账号，模糊查询 |
| nickname | string | 否 | 用户昵称，模糊查询 |
| status | number | 否 | 用户状态：0正常 1停用 |

### 返回字段

| 字段 | 说明 |
|---|---|
| pageNo | 当前页 |
| pageSize | 每页条数 |
| total | 总记录数 |
| pages | 总页数 |
| records | 用户列表 |

### 说明

本接口用于系统管理模块的用户列表页。当前阶段不接入登录认证，后续 S0-05、S0-06 会增加 JWT 和权限控制。

## S0-05 登录认证 JWT

### 1. 用户登录

请求方式：

POST

请求路径：

/api/auth/login

请求体：

```json
{
  "username": "admin",
  "password": "admin123"
}
```
## S0-06 RBAC 基础查询接口

S0-06 实现角色、菜单、按钮权限的基础查询能力。

本阶段只做查询接口，不做新增、修改、删除，不做 `@PreAuthorize` 注解式权限拦截。

### 1. 角色分页查询

#### 接口地址

```text
GET /api/system/roles/page
```

#### 认证要求

需要登录，需要请求头携带 JWT Token。

```text
Authorization: Bearer <token>
```

#### Query 参数

| 参数       | 类型      | 必填 | 说明                |
| -------- | ------- | -- | ----------------- |
| pageNo   | Long    | 否  | 当前页，默认 1          |
| pageSize | Long    | 否  | 每页条数，默认 10，最大 100 |
| roleName | String  | 否  | 角色名称，模糊查询         |
| roleKey  | String  | 否  | 角色权限字符串，模糊查询      |
| status   | Integer | 否  | 状态：0 正常，1 停用      |

#### 请求示例

```text
GET http://localhost:8080/api/system/roles/page?pageNo=1&pageSize=10
Authorization: Bearer <token>
```

#### 返回结构

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "pageNo": 1,
    "pageSize": 10,
    "total": 1,
    "pages": 1,
    "records": [
      {
        "id": 1,
        "roleName": "超级管理员",
        "roleKey": "admin",
        "roleSort": 1,
        "dataScope": 1,
        "status": 0,
        "createTime": "2026-06-26T00:00:00",
        "remark": "超级管理员"
      }
    ]
  }
}
```

#### 后端文件

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/controller/SysRoleController.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/service/SysRoleService.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/service/impl/SysRoleServiceImpl.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/mapper/SysRoleMapper.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/entity/SysRole.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/vo/SysRolePageQuery.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/vo/SysRolePageVO.java
```

---

### 2. 菜单树查询

#### 接口地址

```text
GET /api/system/menus/tree
```

#### 认证要求

需要登录，需要请求头携带 JWT Token。

```text
Authorization: Bearer <token>
```

#### 请求参数

无。

#### 请求示例

```text
GET http://localhost:8080/api/system/menus/tree
Authorization: Bearer <token>
```

#### 返回结构

```json
{
  "code": 0,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "parentId": 0,
      "menuName": "系统管理",
      "menuType": "M",
      "path": "/system",
      "component": null,
      "perms": null,
      "icon": "system",
      "orderNum": 1,
      "visible": 0,
      "status": 0,
      "isFrame": 1,
      "isCache": 0,
      "children": [
        {
          "id": 2,
          "parentId": 1,
          "menuName": "用户管理",
          "menuType": "C",
          "path": "/system/user",
          "component": "system/user/index",
          "perms": "system:user:list",
          "icon": "user",
          "orderNum": 1,
          "visible": 0,
          "status": 0,
          "isFrame": 1,
          "isCache": 0,
          "children": []
        }
      ]
    }
  ]
}
```

#### 后端文件

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/controller/SysMenuController.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/service/SysMenuService.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/service/impl/SysMenuServiceImpl.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/mapper/SysMenuMapper.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/entity/SysMenu.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/vo/SysMenuTreeVO.java
```

---

### 3. 用户权限查询

#### 接口地址

```text
GET /api/system/users/{userId}/permissions
```

#### 认证要求

需要登录，需要请求头携带 JWT Token。

```text
Authorization: Bearer <token>
```

#### Path 参数

| 参数     | 类型   | 必填 | 说明    |
| ------ | ---- | -- | ----- |
| userId | Long | 是  | 用户 ID |

#### 请求示例

```text
GET http://localhost:8080/api/system/users/1/permissions
Authorization: Bearer <token>
```

#### 返回结构

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "userId": 1,
    "roleKeys": [
      "admin"
    ],
    "permissions": [
      "system:user:list",
      "system:user:query",
      "system:user:add",
      "system:user:edit",
      "system:user:delete",
      "system:role:list",
      "system:menu:list"
    ],
    "menus": [
      {
        "id": 1,
        "parentId": 0,
        "menuName": "系统管理",
        "menuType": "M",
        "path": "/system",
        "component": null,
        "perms": null,
        "icon": "system",
        "orderNum": 1,
        "visible": 0,
        "status": 0,
        "isFrame": 1,
        "isCache": 0,
        "children": []
      }
    ]
  }
}
```

#### 后端文件

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/controller/SysPermissionController.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/service/SysPermissionService.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/service/impl/SysPermissionServiceImpl.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/vo/SysPermissionVO.java
```

---

## S0-07 前端已联调接口

S0-07 初始化 Vue3 前端，并完成登录接口和当前用户接口的前后端联调。

本阶段前端只调用已有后端接口，不新增后端接口。

### 1. 登录接口

#### 接口地址

```text
POST /api/auth/login
```

#### 认证要求

无需登录。

#### 请求头

```text
Content-Type: application/json
```

#### 请求体

```json
{
  "username": "admin",
  "password": "admin123"
}
```

#### 返回结构

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "userId": 1,
    "username": "admin",
    "nickname": "系统管理员"
  }
}
```

#### 前端调用文件

```text
scaffold-frontend/src/api/system/auth.ts
```

#### 前端方法

```ts
loginApi(data)
```

---

### 2. 获取当前用户接口

#### 接口地址

```text
GET /api/auth/me
```

#### 认证要求

需要登录，需要请求头携带 JWT Token。

```text
Authorization: Bearer <token>
```

#### 请求参数

无。

#### 返回结构

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "userId": 1,
    "username": "admin",
    "nickname": "系统管理员"
  }
}
```

#### 前端调用文件

```text
scaffold-frontend/src/api/system/auth.ts
```

#### 前端方法

```ts
getMeApi()
```

---

### 3. 前端统一请求封装

#### 文件路径

```text
scaffold-frontend/src/api/request.ts
```

#### 作用

```text
1. 统一创建 axios 实例
2. 统一设置请求超时时间
3. 统一从 localStorage 读取 token
4. 统一给请求头添加 Authorization
5. 统一处理后端 ApiResult 返回结构
6. 遇到 401 时清除本地 token
```

#### Token 本地存储 Key

```text
enterprise_scaffold_token
```

---

### 4. 前端登录状态管理

#### 文件路径

```text
scaffold-frontend/src/stores/auth.ts
```

#### 作用

```text
1. 保存 token
2. 保存当前登录用户 user
3. 登录成功后写入 localStorage
4. 页面刷新后从 localStorage 恢复 token
5. 退出登录时清除 token 和 user
```

---

### 5. 前端路由

#### 文件路径

```text
scaffold-frontend/src/router/index.ts
```

#### 当前路由

| 路径         | 页面                | 说明    |
| ---------- | ----------------- | ----- |
| /          | 重定向到 /dashboard   | 默认首页  |
| /login     | LoginView.vue     | 登录页   |
| /dashboard | DashboardView.vue | 登录后首页 |

#### 路由守卫规则

```text
1. 未登录访问 /dashboard，自动跳转到 /login
2. 已登录访问 /login，自动跳转到 /dashboard
3. 访问 /，自动跳转到 /dashboard
```

---

### 6. 前端页面文件

```text
scaffold-frontend/src/views/login/LoginView.vue
scaffold-frontend/src/views/dashboard/DashboardView.vue
```

#### 登录页功能

```text
1. 展示项目名称 Enterprise Scaffold
2. 展示用户名输入框
3. 展示密码输入框
4. 默认填入 admin / admin123
5. 点击登录后调用 POST /api/auth/login
6. 登录成功后跳转到 /dashboard
```

#### 首页功能

```text
1. 展示当前阶段：S0-07 初始化 Vue3 前端
2. 展示当前用户昵称和用户名
3. 展示前端技术栈
4. 支持退出登录
```
## S0-08 登录日志和操作日志接口

### 登录日志分页查询

GET /api/system/login-logs/page

认证：需要 token

Query 参数：

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| pageNo | Long | 否 | 当前页，默认 1 |
| pageSize | Long | 否 | 每页条数，默认 10，最大 100 |
| username | String | 否 | 登录账号，模糊查询 |
| status | Integer | 否 | 状态：0 成功，1 失败 |

返回字段：

- id
- username
- ipaddr
- loginLocation
- browser
- os
- status
- msg
- loginTime

### 操作日志分页查询

GET /api/system/oper-logs/page

认证：需要 token

Query 参数：

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| pageNo | Long | 否 | 当前页，默认 1 |
| pageSize | Long | 否 | 每页条数，默认 10，最大 100 |
| title | String | 否 | 模块标题，模糊查询 |
| businessType | String | 否 | 业务类型，模糊查询 |
| operName | String | 否 | 操作人员，模糊查询 |
| status | Integer | 否 | 状态：0 正常，1 异常 |

返回字段：

- id
- title
- businessType
- method
- requestMethod
- operatorType
- operName
- operUrl
- operIp
- status
- errorMsg
- operTime
- costTime

## S0-09：字典管理和文件上传接口

所有接口成功响应继续使用统一结构：

```json
{
  "code": 0,
  "msg": "success",
  "data": {}
}
```
## S0-10：Docker Compose 部署后的接口验收

S0-10 不新增业务接口，只验证已有接口在容器化环境下是否正常工作。

### 后端基础地址

```text
http://localhost:8080
```
## M1-01：智能矿山模块接口

### 智能矿山模块健康检查

接口地址：`GET /api/mine/health`

认证要求：需要 JWT token。

请求头：

- `Authorization: Bearer <token>`

该接口不加入 `SecurityConfig` 放行列表。

请求参数：无。

成功响应：

{
"code": 0,
"msg": "success",
"data": "enterprise-scaffold mine module running"
}

未登录响应：

{
"code": 401,
"msg": "请先登录或登录已过期",
"data": null
}

后端类路径：

`cn.sxu.enterprise.module.mine.controller.MineHealthController`

操作日志注解：

`@OperLog(title = "智能矿山", businessType = "模块健康检查")`

Apifox 测试步骤：

1. 登录获取 token

请求地址：`POST http://localhost:8080/api/auth/login`

请求体：

{
"username": "admin",
"password": "admin123"
}

2. 不带 token 访问

请求地址：`GET http://localhost:8080/api/mine/health`

预期返回：`code = 401`。

3. 带 token 访问

请求地址：`GET http://localhost:8080/api/mine/health`

请求头：`Authorization: Bearer <token>`

预期返回：`code = 0`，`data = enterprise-scaffold mine module running`。

4. 查询操作日志

请求地址：`GET http://localhost:8080/api/system/oper-logs/page?pageNo=1&pageSize=10&title=智能矿山`

预期能看到：

- `title = 智能矿山`
- `businessType = 模块健康检查`
- `operUrl = /api/mine/health`
- `requestMethod = GET`
- `status = 0`


## M1-02：设备与传感器分页接口

### 设备分页
GET /api/mine/devices/page

参数：
pageNo, pageSize, deviceCode, deviceName, deviceType, areaName, status

### 传感器分页
GET /api/mine/sensors/page

参数：
pageNo, pageSize, sensorCode, sensorName, sensorType, deviceId, areaName, status

返回：
ApiResult + PageResult

权限：
JWT

日志：
@OperLog

## M1-03：传感器模拟数据接口

### 生成模拟传感器数据

接口：

POST /api/mine/sensor-data/simulate

认证：

需要 JWT token。

请求头：

Authorization: Bearer <token>

请求体示例：

{
"sensorType": "GAS",
"count": 5
}

参数说明：

sensorId：可选，指定某个传感器生成数据。
sensorType：可选，按传感器类型生成数据。
count：可选，每个传感器生成几条数据，默认 1，最大 20。

返回：

ApiResult<List<MineSensorDataVO>>

说明：

接口基于 mine_sensor 中 status = 0 的传感器生成模拟数据，并写入 mine_sensor_data 表。如果 data_value 大于等于 alarm_threshold，则 alarm_flag = 1，status = 1。

### 查询传感器最新数据

接口：

GET /api/mine/sensor-data/latest

认证：

需要 JWT token。

请求参数：

sensorId
sensorCode
sensorName
sensorType
areaName
alarmFlag
status

返回：

ApiResult<List<MineSensorDataVO>>

说明：

按传感器维度返回最新一条数据。

### 分页查询传感器历史数据

接口：

GET /api/mine/sensor-data/page

认证：

需要 JWT token。

请求参数：

pageNo
pageSize
sensorId
sensorCode
sensorName
sensorType
areaName
alarmFlag
status

返回：

ApiResult<PageResult<MineSensorDataVO>>

说明：

按照 collect_time 和 id 倒序分页查询传感器历史数据。

## M1-04：告警规则和告警事件接口

M1-04 新增智能矿山告警规则和告警事件接口。

本阶段新增接口：

```text
GET  /api/mine/alarm-rules/page
GET  /api/mine/alarm-events/page
POST /api/mine/alarm-events/generate
```

所有接口都需要 JWT 认证。

请求头固定：

```text
Authorization: Bearer <token>
```

所有成功响应继续使用统一结构：

```json
{
  "code": 0,
  "msg": "success",
  "data": {}
}
```

分页接口继续使用统一分页结构：

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "total": 0,
  "pages": 0,
  "records": []
}
```

### 1. 告警规则分页查询

接口：

```text
GET /api/mine/alarm-rules/page
```

认证：

```text
需要 JWT
Authorization: Bearer <token>
```

Query 参数：

```text
pageNo
pageSize
ruleCode
ruleName
sensorType
alarmLevel
status
```

参数说明：

```text
pageNo：页码，默认 1
pageSize：每页条数，默认 10
ruleCode：规则编码，模糊查询
ruleName：规则名称，模糊查询
sensorType：传感器类型，精确查询
alarmLevel：告警级别，精确查询
status：状态，精确查询
```

请求示例：

```text
GET http://localhost:8080/api/mine/alarm-rules/page?pageNo=1&pageSize=10
GET http://localhost:8080/api/mine/alarm-rules/page?pageNo=1&pageSize=10&sensorType=GAS
GET http://localhost:8080/api/mine/alarm-rules/page?pageNo=1&pageSize=10&alarmLevel=3
```

返回：

```text
ApiResult<PageResult<MineAlarmRulePageVO>>
```

MineAlarmRulePageVO 字段：

```text
id
ruleCode
ruleName
sensorType
compareOperator
thresholdValue
alarmLevel
alarmTitle
alarmContent
status
createTime
remark
```

操作日志：

```java
@OperLog(title = "智能矿山-告警规则", businessType = "分页查询")
```

### 2. 告警事件分页查询

接口：

```text
GET /api/mine/alarm-events/page
```

认证：

```text
需要 JWT
Authorization: Bearer <token>
```

Query 参数：

```text
pageNo
pageSize
eventCode
ruleCode
sensorCode
sensorName
sensorType
areaName
alarmLevel
handleStatus
status
```

参数说明：

```text
pageNo：页码，默认 1
pageSize：每页条数，默认 10
eventCode：告警事件编码，模糊查询
ruleCode：规则编码，模糊查询
sensorCode：传感器编码，模糊查询
sensorName：传感器名称，模糊查询
sensorType：传感器类型，精确查询
areaName：所属区域，模糊查询
alarmLevel：告警级别，精确查询
handleStatus：处理状态，精确查询
status：状态，精确查询
```

请求示例：

```text
GET http://localhost:8080/api/mine/alarm-events/page?pageNo=1&pageSize=10
GET http://localhost:8080/api/mine/alarm-events/page?pageNo=1&pageSize=10&sensorType=GAS
GET http://localhost:8080/api/mine/alarm-events/page?pageNo=1&pageSize=10&alarmLevel=3
GET http://localhost:8080/api/mine/alarm-events/page?pageNo=1&pageSize=10&handleStatus=0
```

返回：

```text
ApiResult<PageResult<MineAlarmEventPageVO>>
```

MineAlarmEventPageVO 字段：

```text
id
eventCode
ruleId
ruleCode
ruleName
sensorDataId
sensorId
sensorCode
sensorName
sensorType
deviceId
areaName
location
dataValue
thresholdValue
compareOperator
alarmLevel
alarmTitle
alarmContent
alarmTime
handleStatus
status
createTime
remark
```

排序规则：

```text
alarmTime 倒序
id 倒序
```

操作日志：

```java
@OperLog(title = "智能矿山-告警事件", businessType = "分页查询")
```

### 3. 根据传感器数据生成告警事件

接口：

```text
POST /api/mine/alarm-events/generate
```

认证：

```text
需要 JWT
Authorization: Bearer <token>
Content-Type: application/json
```

Body 参数：

```text
sensorDataId
sensorType
limit
```

参数说明：

```text
sensorDataId：可选，只根据某一条传感器数据生成告警事件
sensorType：可选，只处理某一类传感器数据，例如 GAS
limit：可选，最多扫描最近多少条传感器数据，默认 100，最大 1000
```

Body 示例 1：扫描最近 100 条传感器数据生成告警事件

```json
{
  "limit": 100
}
```

Body 示例 2：只扫描瓦斯传感器数据

```json
{
  "sensorType": "GAS",
  "limit": 100
}
```

Body 示例 3：只处理某条传感器数据

```json
{
  "sensorDataId": 1,
  "limit": 1
}
```

返回：

```text
ApiResult<List<MineAlarmEventPageVO>>
```

生成规则：

```text
1. 查询 mine_sensor_data 中的传感器数据。
2. 查询 mine_alarm_rule 中 status = 0 的启用规则。
3. 根据 sensor_type 匹配对应规则。
4. 根据 compare_operator 和 threshold_value 判断 data_value 是否触发告警。
5. 如果触发告警，写入 mine_alarm_event。
6. 同一个 rule_id + sensor_data_id 不重复生成告警事件。
```

操作日志：

```java
@OperLog(title = "智能矿山-告警事件", businessType = "生成告警事件")
```

### 4. 401 未登录返回

不带 token 访问 M1-04 接口时返回：

```json
{
  "code": 401,
  "msg": "请先登录或登录已过期",
  "data": null
}
```

## M1-05 工单闭环接口

本阶段新增智能矿山工单闭环接口，统一路径前缀为 /api/mine/work-orders。所有接口都需要 JWT 认证，请求头固定使用 Authorization: Bearer <token>。所有接口继续使用 ApiResult 统一返回结构，分页接口继续使用 PageResult 分页结构。

工单分页查询接口：GET /api/mine/work-orders/page。支持 Query 参数 pageNo、pageSize、workOrderCode、eventCode、alarmLevel、sensorCode、sensorName、sensorType、areaName、orderStatus、status。返回结构为 ApiResult<PageResult<MineWorkOrderPageVO>>。该接口用于分页查询工单列表，支持按工单编码、告警事件编码、告警级别、传感器编码、传感器名称、传感器类型、区域、工单状态和数据状态过滤。

告警转工单接口：POST /api/mine/work-orders/create-from-alarm。请求 Body 示例为 {"alarmEventId":1,"remark":"由告警事件生成检修工单"}。返回结构为 ApiResult<MineWorkOrderPageVO>。该接口根据 mine_alarm_event 中的告警事件生成 mine_work_order 工单。一个告警事件只允许生成一个工单，重复调用同一个 alarmEventId 时不重复创建，而是返回已经存在的工单。

工单处理接口：POST /api/mine/work-orders/{id}/handle。请求 Body 示例为 {"handleResult":"已安排检修人员完成现场处置。","remark":"工单已处理"}。返回结构为 ApiResult<MineWorkOrderPageVO>。处理成功后，mine_work_order.order_status 更新为 2，并记录 handler_user_id、handler_username、handle_time、handle_result 等处理信息。



工单关闭接口：POST /api/mine/work-orders/{id}/close。请求 Body 示例为 {"closeResult":"现场复核正常，工单关闭。","remark":"闭环完成"}。返回结构为 ApiResult<MineWorkOrderPageVO>。关闭成功后，mine_work_order.order_status 更新为 3，并记录 close_user_id、close_username、close_time、close_result 等关闭信息，同时关联的 mine_alarm_event.handle_status 更新为 2。