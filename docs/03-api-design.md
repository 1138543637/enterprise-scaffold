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


## M1-06：智能矿山看板接口

### 汇总统计

GET /api/mine/dashboard/summary

认证：需要 JWT。

返回：

ApiResult<MineDashboardSummaryVO>

### 告警级别统计

GET /api/mine/dashboard/alarm-level-stats

认证：需要 JWT。

返回：

ApiResult<List<MineAlarmLevelStatVO>>

### 传感器类型统计

GET /api/mine/dashboard/sensor-type-stats

认证：需要 JWT。

返回：

ApiResult<List<MineSensorTypeStatVO>>

### 工单状态统计

GET /api/mine/dashboard/work-order-status-stats

认证：需要 JWT。

返回：

ApiResult<List<MineWorkOrderStatusStatVO>>

### 最近告警事件

GET /api/mine/dashboard/recent-alarms

认证：需要 JWT。

返回：

ApiResult<List<MineRecentAlarmVO>>

### 最近工单记录

GET /api/mine/dashboard/recent-work-orders

认证：需要 JWT。

返回：

ApiResult<List<MineRecentWorkOrderVO>>

## M1-07：MQTT 模拟发布接口

M1-07 新增 MQTT 模拟发布接口，用于开发和验收阶段模拟传感器设备向 EMQX 发布 MQTT 消息。

接口路径为：

```text
POST /api/mine/mqtt/simulate-publish
```

认证方式为 JWT，调用时需要在请求头中携带：

```text
Authorization: Bearer <token>
Content-Type: application/json
```

请求体示例：

```json
{
  "sensorCode": "SEN-GAS-001",
  "dataValue": 1.68,
  "remark": "M1-07 MQTT模拟上报"
}
```

字段说明：

| 字段 | 类型 | 是否必填 | 说明 |
|---|---|---|---|
| sensorCode | String | 是 | 传感器编码，必须能在 `mine_sensor` 中查到 |
| dataValue | BigDecimal | 是 | 传感器采集值 |
| collectTime | String | 否 | 采集时间，格式为 `yyyy-MM-dd HH:mm:ss`，不传则使用当前时间 |
| remark | String | 否 | 备注 |

成功返回结构继续使用 `ApiResult`。

返回示例：

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "sensorCode": "SEN-GAS-001",
    "dataValue": 1.68,
    "collectTime": "2026-06-29 20:30:00",
    "remark": "M1-07 MQTT模拟上报"
  }
}
```

该接口收到请求后，会将 JSON 消息发布到 EMQX 的 `mine/sensor/data` Topic。

后端 MQTT 订阅器再接收该消息，并写入 `mine_sensor_data`。

验收接口包括：

```text
GET /api/mine/sensor-data/page?pageNo=1&pageSize=10&sensorCode=SEN-GAS-001
GET /api/mine/alarm-events/page?pageNo=1&pageSize=10&sensorCode=SEN-GAS-001
GET /api/mine/dashboard/summary
```

## M1-08：MQTT 批量模拟发布接口

接口地址：`POST /api/mine/mqtt/simulate-batch`

认证方式：需要 JWT。

请求头：

- `Authorization: Bearer <token>`
- `Content-Type: application/json`

请求 Body 示例：

- `sensorType`：`GAS`
- `count`：`5`
- `intervalMillis`：`200`
- `remark`：`M1-08批量MQTT模拟`

字段说明：

- `sensorType`：可选，表示传感器类型，例如 `GAS`、`TEMPERATURE`、`VIBRATION`。不传则从全部正常传感器中随机选择。
- `count`：可选，表示批量模拟条数，默认 `10`，范围 `1` 到 `100`。
- `intervalMillis`：可选，表示每条 MQTT 消息发送间隔，默认 `0`，范围 `0` 到 `1000`，单位毫秒。
- `remark`：可选，表示备注。

返回结构：`ApiResult<List<MineSensorMqttMessage>>`

验收接口：

- `POST /api/mine/mqtt/simulate-batch`
- `GET /api/mine/sensor-data/page?pageNo=1&pageSize=10&sensorType=GAS`
- `GET /api/mine/alarm-events/page?pageNo=1&pageSize=10&sensorType=GAS`
- `GET /api/mine/dashboard/summary`

操作日志注解：`@OperLog(title = "智能矿山-MQTT", businessType = "批量模拟发布")`

## M1-09：实时数据展示增强接口复用说明

M1-09 不新增后端接口，全部复用已有接口。

在 `/mine/dashboard` 页面展示最近传感器数据时，复用 `GET /api/mine/sensor-data/page?pageNo=1&pageSize=10`。该接口需要 JWT 认证，请求头为 `Authorization: Bearer <token>`，返回结构为 `ApiResult<PageResult<MineSensorDataVO>>`，页面读取分页结果中的 `records` 展示最新 10 条传感器数据。

在 `/mine/dashboard` 页面点击“批量模拟 MQTT 数据”时，复用 `POST /api/mine/mqtt/simulate-batch`。该接口需要 JWT 认证，请求头为 `Authorization: Bearer <token>` 和 `Content-Type: application/json`。请求 Body 示例为：`{"count":10,"intervalMillis":100,"remark":"M1-09实时看板批量模拟"}`。返回结构为 `ApiResult<List<MineSensorMqttMessage>>`。

M1-09 继续复用已有看板接口：`GET /api/mine/dashboard/summary`、`GET /api/mine/dashboard/alarm-level-stats`、`GET /api/mine/dashboard/sensor-type-stats`、`GET /api/mine/dashboard/work-order-status-stats`、`GET /api/mine/dashboard/recent-alarms`、`GET /api/mine/dashboard/recent-work-orders`。前端通过手动刷新和 5 秒自动刷新，让这些接口返回的数据在页面上持续更新。

## M1-10：设备健康评分接口

### 设备健康分页查询

接口地址：

```text
GET /api/mine/device-health/page
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
deviceCode
deviceName
deviceType
areaName
riskLevel
status
```

返回结构：

```text
ApiResult<PageResult<MineDeviceHealthVO>>
```

风险等级：

```text
0 = 健康
1 = 关注
2 = 风险
3 = 高危
```

### 设备健康汇总统计

接口地址：

```text
GET /api/mine/device-health/summary
```

认证：

```text
需要 JWT
Authorization: Bearer <token>
```

返回结构：

```text
ApiResult<MineDeviceHealthSummaryVO>
```

返回字段：

```text
deviceTotal
healthyTotal
attentionTotal
riskTotal
highRiskTotal
averageHealthScore
severeUnhandledAlarmTotal
unclosedWorkOrderTotal
```


## M1-11：预测性维护任务接口

所有接口都需要 JWT：
Authorization: Bearer <token>

1. 分页查询
   GET /api/mine/maintenance-tasks/page
   返回：ApiResult<PageResult<MineMaintenanceTaskPageVO>>

2. 汇总统计
   GET /api/mine/maintenance-tasks/summary
   返回：ApiResult<MineMaintenanceTaskSummaryVO>

3. 基于设备健康风险生成维护任务
   POST /api/mine/maintenance-tasks/create-from-device-health
   请求体：MineMaintenanceTaskCreateRequest
   返回：ApiResult<MineMaintenanceTaskPageVO>

4. 安排维护任务
   POST /api/mine/maintenance-tasks/{id}/plan
   请求体：MineMaintenanceTaskPlanRequest
   返回：ApiResult<MineMaintenanceTaskPageVO>

5. 处理维护任务
   POST /api/mine/maintenance-tasks/{id}/handle
   请求体：MineMaintenanceTaskHandleRequest
   返回：ApiResult<MineMaintenanceTaskPageVO>

6. 关闭维护任务
   POST /api/mine/maintenance-tasks/{id}/close
   请求体：MineMaintenanceTaskCloseRequest
   返回：ApiResult<MineMaintenanceTaskPageVO>

操作日志：
@OperLog(title = "智能矿山-预测性维护", businessType = "分页查询")
@OperLog(title = "智能矿山-预测性维护", businessType = "汇总统计")
@OperLog(title = "智能矿山-预测性维护", businessType = "健康风险生成任务")
@OperLog(title = "智能矿山-预测性维护", businessType = "任务安排")
@OperLog(title = "智能矿山-预测性维护", businessType = "任务处理")
@OperLog(title = "智能矿山-预测性维护", businessType = "任务关闭")


## M1-12：维护看板与风险趋势分析接口

### 1. 维护看板汇总统计

`GET /api/mine/maintenance-dashboard/summary`

认证：需要 JWT。

返回：

`ApiResult<MineMaintenanceDashboardSummaryVO>`

字段：

- `taskTotal`
- `unclosedTaskTotal`
- `pendingTotal`
- `plannedTotal`
- `processingTotal`
- `closedTotal`
- `highRiskTaskTotal`
- `urgentTotal`
- `todayNewTaskTotal`
- `alarmTotal7d`
- `workOrderTotal7d`

### 2. 任务状态统计

`GET /api/mine/maintenance-dashboard/task-status-stats`

返回：

`ApiResult<List<MineMaintenanceTaskStatusStatVO>>`

### 3. 优先级统计

`GET /api/mine/maintenance-dashboard/priority-stats`

返回：

`ApiResult<List<MineMaintenancePriorityStatVO>>`

### 4. 风险等级统计

`GET /api/mine/maintenance-dashboard/risk-level-stats`

返回：

`ApiResult<List<MineMaintenanceRiskLevelStatVO>>`

### 5. 最近 7 天风险趋势

`GET /api/mine/maintenance-dashboard/risk-trend`

返回：

`ApiResult<List<MineMaintenanceRiskTrendVO>>`

### 6. 最近维护任务

`GET /api/mine/maintenance-dashboard/recent-tasks`

返回：

`ApiResult<List<MineMaintenanceRecentTaskVO>>`

### 7. 高风险设备维护任务

`GET /api/mine/maintenance-dashboard/high-risk-devices`

返回：

`ApiResult<List<MineMaintenanceHighRiskDeviceVO>>`

以上接口均需要请求头：

`Authorization: Bearer <token>`

## M1-13：项目一接口总体验收

M1-13 不新增后端接口，复用项目一已有接口进行总体验收。

所有智能矿山业务接口均需要 JWT 认证。

请求头固定为：

`Authorization: Bearer <token>`

所有成功响应继续使用统一结构：

`ApiResult`

分页接口继续使用统一结构：

`PageResult`

登录接口：

`POST /api/auth/login`

请求体：

`{ "username": "admin", "password": "admin123" }`

返回成功后，从 `data.token` 中复制 token。

### 智能矿山基础接口

- `GET /api/mine/health`
- `GET /api/mine/devices/page?pageNo=1&pageSize=10`
- `GET /api/mine/sensors/page?pageNo=1&pageSize=10`

### 传感器数据接口

- `POST /api/mine/sensor-data/simulate`
- `GET /api/mine/sensor-data/latest`
- `GET /api/mine/sensor-data/page?pageNo=1&pageSize=10`

### MQTT 模拟接口

- `POST /api/mine/mqtt/simulate-publish`
- `POST /api/mine/mqtt/simulate-batch`

单条模拟发布请求体示例：

`{ "sensorCode": "SEN-GAS-001", "dataValue": 1.68, "remark": "M1-13总体验收：MQTT瓦斯高值模拟上报" }`

批量模拟发布请求体示例：

`{ "sensorType": "GAS", "count": 5, "intervalMillis": 200, "remark": "M1-13总体验收：批量MQTT模拟上报" }`

### 告警规则和告警事件接口

- `GET /api/mine/alarm-rules/page?pageNo=1&pageSize=10`
- `GET /api/mine/alarm-events/page?pageNo=1&pageSize=10`
- `POST /api/mine/alarm-events/generate`

### 工单闭环接口

- `GET /api/mine/work-orders/page?pageNo=1&pageSize=10`
- `POST /api/mine/work-orders/create-from-alarm`
- `POST /api/mine/work-orders/{id}/handle`
- `POST /api/mine/work-orders/{id}/close`

### 智能矿山综合看板接口

- `GET /api/mine/dashboard/summary`
- `GET /api/mine/dashboard/alarm-level-stats`
- `GET /api/mine/dashboard/sensor-type-stats`
- `GET /api/mine/dashboard/work-order-status-stats`
- `GET /api/mine/dashboard/recent-alarms`
- `GET /api/mine/dashboard/recent-work-orders`

### 设备健康评分接口

- `GET /api/mine/device-health/page?pageNo=1&pageSize=10`
- `GET /api/mine/device-health/summary`

### 预测性维护任务接口

- `GET /api/mine/maintenance-tasks/page?pageNo=1&pageSize=10`
- `GET /api/mine/maintenance-tasks/summary`
- `POST /api/mine/maintenance-tasks/create-from-device-health`
- `POST /api/mine/maintenance-tasks/{id}/plan`
- `POST /api/mine/maintenance-tasks/{id}/handle`
- `POST /api/mine/maintenance-tasks/{id}/close`

### 维护看板与风险趋势接口

- `GET /api/mine/maintenance-dashboard/summary`
- `GET /api/mine/maintenance-dashboard/task-status-stats`
- `GET /api/mine/maintenance-dashboard/priority-stats`
- `GET /api/mine/maintenance-dashboard/risk-level-stats`
- `GET /api/mine/maintenance-dashboard/risk-trend`
- `GET /api/mine/maintenance-dashboard/recent-tasks`
- `GET /api/mine/maintenance-dashboard/high-risk-devices`

M1-13 接口验收标准：

- 所有核心接口带 token 请求后返回 `code = 0`。
- 所有核心接口带 token 请求后返回 `msg = success`。
- 分页接口返回 `pageNo`、`pageSize`、`total`、`pages`、`records`。
- 看板统计接口返回 `data` 不为 null。

操作日志验收接口：

`GET /api/system/oper-logs/page?pageNo=1&pageSize=20&title=智能矿山`

预期能看到智能矿山基础、设备台账、传感器台账、传感器数据、告警规则、告警事件、工单、看板、MQTT、设备健康、预测性维护、维护看板相关操作记录。







##############################################################################################################################################
## A2-01 AIOps 模块健康检查接口

接口地址：`GET /api/aiops/health`

认证方式：需要 JWT，请求头固定为 `Authorization: Bearer <token>`。

成功返回：

{
"code": 0,
"msg": "success",
"data": "enterprise-scaffold aiops module running"
}

该接口用于验证 AIOps 模块已经成功接入后端工程。接口继续使用 `ApiResult` 统一返回结构，并使用 `@OperLog(title = "AIOps智能运维", businessType = "模块健康检查")` 记录操作日志。


## A2-02：AIOps 资源管理接口

A2-02 新增资源分页查询接口 `GET /api/aiops/resources/page`，用于查询 AIOps 资源台账列表。该接口属于项目二“云网融合 AIOps 智能运维平台”的资源管理能力，接口路径固定使用 `/api/aiops/resources/page`，不能改成 `/api/aiops/resource/page`，也不能改成 `/api/ops/resources/page` 或 `/api/monitor/resources/page`。

该接口需要 JWT 认证，请求头固定为 `Authorization: Bearer <token>`。不带 token 访问时应返回 `code = 401`，提示“请先登录或登录已过期”。带 token 正常访问时返回 `ApiResult<PageResult<AiopsResourcePageVO>>`。分页结构继续使用项目统一 `PageResult`，字段包括 `pageNo`、`pageSize`、`total`、`pages`、`records`。成功响应继续使用项目统一 `ApiResult`，字段包括 `code`、`msg`、`data`。

接口支持 query 参数 `pageNo`、`pageSize`、`resourceCode`、`resourceName`、`resourceType`、`ipAddr`、`envType`、`systemName`、`ownerName`、`collectEnabled`、`status`。其中 `resourceCode`、`resourceName`、`ipAddr`、`systemName`、`ownerName` 使用模糊查询，`resourceType`、`envType`、`collectEnabled`、`status` 使用精确查询。默认按照 `createTime` 倒序、`id` 倒序排序。

返回对象 `AiopsResourcePageVO` 字段包括 `id`、`resourceCode`、`resourceName`、`resourceType`、`ipAddr`、`port`、`envType`、`systemName`、`ownerName`、`collectEnabled`、`lastCollectTime`、`status`、`createTime`、`remark`。接口使用操作日志注解 `@OperLog(title = "AIOps资源管理", businessType = "分页查询")`，访问成功后可在 `GET /api/system/oper-logs/page?pageNo=1&pageSize=20&title=AIOps资源管理` 中查询到对应操作日志。

## A2-03：AIOps 指标采集与模拟数据接口

A2-03 新增三个接口，统一使用 `/api/aiops/metric-data` 前缀，所有接口都需要 JWT 认证，并继续使用 `ApiResult` 统一返回结构。

`POST /api/aiops/metric-data/simulate` 用于基于 `aiops_resource` 中启用采集且状态正常的资源，模拟生成 CPU、内存、磁盘、网络、MySQL、Redis 等指标数据，并写入 `aiops_metric_data`。请求体字段包括 `resourceId`、`resourceType`、`metricType`、`count`、`remark`。其中 `resourceId` 可选，用于指定某个资源；`resourceType` 可选，用于指定资源类型；`metricType` 可选，用于指定指标类型；`count` 表示每个资源生成几轮指标数据，默认 1，最大 20；`remark` 为备注。返回结构为 `ApiResult<List<AiopsMetricDataVO>>`。

`GET /api/aiops/metric-data/latest` 用于查询每个资源每类指标的最新一条数据，支持查询参数 `resourceId`、`resourceCode`、`resourceName`、`resourceType`、`ipAddr`、`metricCode`、`metricType`、`alarmFlag`、`status`，返回结构为 `ApiResult<List<AiopsMetricDataVO>>`。

`GET /api/aiops/metric-data/page` 用于分页查询 AIOps 指标历史数据，支持查询参数 `pageNo`、`pageSize`、`resourceId`、`resourceCode`、`resourceName`、`resourceType`、`ipAddr`、`metricCode`、`metricType`、`alarmFlag`、`status`，返回结构为 `ApiResult<PageResult<AiopsMetricDataVO>>`。

本阶段 Controller 方法统一使用 `@OperLog(title = "AIOps指标数据", businessType = "...")` 记录操作日志。

## A2-04：AIOps 告警规则、告警事件、运维工单接口

A2-04 新增 AIOps 告警中心和运维工单闭环接口，所有接口都需要 JWT 认证，请求头固定使用 `Authorization: Bearer <token>`。所有接口继续使用 `ApiResult` 统一返回结构，分页接口继续使用 `PageResult` 分页结构，Controller 方法继续使用 `@OperLog` 记录操作日志。

`GET /api/aiops/alert-rules/page` 用于分页查询 AIOps 告警规则，返回结构为 `ApiResult<PageResult<AiopsAlertRulePageVO>>`。支持查询参数：`pageNo`、`pageSize`、`ruleCode`、`ruleName`、`resourceType`、`metricType`、`alertLevel`、`status`。其中 `ruleCode` 和 `ruleName` 为模糊查询，`resourceType`、`metricType`、`alertLevel`、`status` 为精确查询。操作日志固定为 `@OperLog(title = "AIOps告警规则", businessType = "分页查询")`。

`GET /api/aiops/alert-events/page` 用于分页查询 AIOps 告警事件，返回结构为 `ApiResult<PageResult<AiopsAlertEventPageVO>>`。支持查询参数：`pageNo`、`pageSize`、`eventCode`、`ruleCode`、`resourceCode`、`resourceName`、`resourceType`、`ipAddr`、`metricCode`、`metricType`、`alertLevel`、`handleStatus`、`status`。其中编码和名称类字段为模糊查询，类型、级别和状态类字段为精确查询。排序规则固定为 `alertTime` 倒序、`id` 倒序。操作日志固定为 `@OperLog(title = "AIOps告警事件", businessType = "分页查询")`。

`POST /api/aiops/alert-events/generate` 用于根据 AIOps 指标数据生成告警事件，返回结构为 `ApiResult<List<AiopsAlertEventPageVO>>`。请求体字段包括 `metricDataId`、`resourceType`、`metricType`、`limit`。生成逻辑为：查询最近的 `aiops_metric_data` 指标数据，查询启用状态的 `aiops_alert_rule` 告警规则，根据 `resourceType` 和 `metricType` 匹配规则，再根据 `compareOperator` 和 `thresholdValue` 判断是否触发告警，触发后写入 `aiops_alert_event`。系统使用 `rule_id + metric_data_id` 去重，避免重复生成同一告警事件。操作日志固定为 `@OperLog(title = "AIOps告警事件", businessType = "生成告警事件")`。

`GET /api/aiops/work-orders/page` 用于分页查询 AIOps 运维工单，返回结构为 `ApiResult<PageResult<AiopsWorkOrderPageVO>>`。支持查询参数：`pageNo`、`pageSize`、`workOrderCode`、`eventCode`、`alertLevel`、`resourceCode`、`resourceName`、`resourceType`、`ipAddr`、`metricType`、`orderStatus`、`status`。排序规则固定为 `createTime` 倒序、`id` 倒序。操作日志固定为 `@OperLog(title = "AIOps运维工单", businessType = "分页查询")`。

`POST /api/aiops/work-orders/create-from-alert` 用于将告警事件生成运维工单，返回结构为 `ApiResult<AiopsWorkOrderPageVO>`。请求体字段包括 `alertEventId` 和 `remark`。生成逻辑为：根据 `alertEventId` 查询 `aiops_alert_event`，如果告警事件不存在则抛出业务异常，如果该告警事件已经关闭则不能生成工单，如果该告警事件已经生成过工单则直接返回已有工单，否则创建 `aiops_work_order`，并将 `aiops_alert_event.handle_status` 更新为 `1 = 已派单`。操作日志固定为 `@OperLog(title = "AIOps运维工单", businessType = "告警转工单")`。

`POST /api/aiops/work-orders/{id}/handle` 用于处理 AIOps 运维工单，返回结构为 `ApiResult<AiopsWorkOrderPageVO>`。请求体字段包括 `handleResult` 和 `remark`。处理成功后 `aiops_work_order.order_status` 更新为 `2 = 已处理`，并记录处理人和处理时间。操作日志固定为 `@OperLog(title = "AIOps运维工单", businessType = "工单处理")`。

`POST /api/aiops/work-orders/{id}/close` 用于关闭 AIOps 运维工单，返回结构为 `ApiResult<AiopsWorkOrderPageVO>`。请求体字段包括 `closeResult` 和 `remark`。关闭成功后 `aiops_work_order.order_status` 更新为 `3 = 已关闭`，同时将对应 `aiops_alert_event.handle_status` 更新为 `2 = 已关闭`，形成告警事件和运维工单状态联动。操作日志固定为 `@OperLog(title = "AIOps运维工单", businessType = "工单关闭")`。

## A2-05：AIOps 根因分析接口

A2-05 新增 AIOps 根因分析接口，接口统一使用 /api/aiops/root-causes 前缀。

POST /api/aiops/root-causes/analyze 用于根据告警事件执行根因分析。

请求体包含 alertEventId、lookbackMinutes、remark。

其中 alertEventId 为必填字段。

lookbackMinutes 表示向前回看的分钟数，默认 30，最小 5，最大 1440。

接口返回 ApiResult<AiopsRootCauseVO>。

GET /api/aiops/root-causes/page 用于分页查询根因分析记录。

Query 参数包含 pageNo、pageSize、analysisCode、eventCode、resourceCode、resourceName、
resourceType、ipAddr、rootCauseType、status。

接口返回 ApiResult<PageResult<AiopsRootCauseVO>>。

GET /api/aiops/root-causes/{id} 用于查询根因分析详情。

接口返回 ApiResult<AiopsRootCauseVO>。

所有 A2-05 接口都需要 JWT 认证，请求头使用 Authorization: Bearer <token>。

Controller 方法继续使用 @OperLog 记录操作日志。

操作日志 title 固定为 AIOps根因分析，
businessType 分别为执行分析、分页查询、详情查询。

成功响应继续使用 ApiResult，分页响应继续使用 PageResult，
不允许改变统一返回结构。

### A2-06：AIOps 综合看板接口

A2-06 新增 AIOps 综合看板接口，统一接口前缀为 `/api/aiops/dashboard`。

所有接口都需要 JWT 认证，请求头固定为 `Authorization: Bearer <token>`。

所有接口继续使用 `ApiResult` 统一返回结构。

Controller 方法继续使用 `@OperLog` 记录操作日志。

#### 汇总统计

`GET /api/aiops/dashboard/summary`

用于获取资源总数、异常资源数、指标数据总数、异常指标数、告警规则数、告警事件数、未处理告警数、运维工单数、待处理工单数、根因分析数和高置信根因分析数。

返回结构：

`ApiResult<AiopsDashboardSummaryVO>`

操作日志：

`@OperLog(title = "AIOps综合看板", businessType = "汇总统计")`

#### 资源类型统计

`GET /api/aiops/dashboard/resource-type-stats`

用于按资源类型统计资源数量。

返回结构：

`ApiResult<List<AiopsResourceTypeStatVO>>`

操作日志：

`@OperLog(title = "AIOps综合看板", businessType = "资源类型统计")`

#### 告警级别统计

`GET /api/aiops/dashboard/alert-level-stats`

用于按告警级别统计告警事件数量。

返回结构：

`ApiResult<List<AiopsAlertLevelStatVO>>`

操作日志：

`@OperLog(title = "AIOps综合看板", businessType = "告警级别统计")`

#### 工单状态统计

`GET /api/aiops/dashboard/work-order-status-stats`

用于按工单状态统计运维工单数量。

返回结构：

`ApiResult<List<AiopsWorkOrderStatusStatVO>>`

操作日志：

`@OperLog(title = "AIOps综合看板", businessType = "工单状态统计")`

#### 最近 7 天指标趋势

`GET /api/aiops/dashboard/metric-trend`

用于统计最近 7 天指标数量、异常指标数量和平均指标值。

返回结构：

`ApiResult<List<AiopsMetricTrendVO>>`

操作日志：

`@OperLog(title = "AIOps综合看板", businessType = "指标趋势分析")`

#### 最近告警事件

`GET /api/aiops/dashboard/recent-alerts`

用于查询最近 10 条告警事件。

返回结构：

`ApiResult<List<AiopsRecentAlertVO>>`

操作日志：

`@OperLog(title = "AIOps综合看板", businessType = "最近告警查询")`

#### 最近运维工单

`GET /api/aiops/dashboard/recent-work-orders`

用于查询最近 10 条运维工单。

返回结构：

`ApiResult<List<AiopsRecentWorkOrderVO>>`

操作日志：

`@OperLog(title = "AIOps综合看板", businessType = "最近工单查询")`

#### 最近根因分析

`GET /api/aiops/dashboard/recent-root-causes`

用于查询最近 10 条根因分析记录。

返回结构：

`ApiResult<List<AiopsRecentRootCauseVO>>`

操作日志：

`@OperLog(title = "AIOps综合看板", businessType = "最近根因分析查询")`

## A2-07：Prometheus / Grafana 接入接口说明

A2-07 新增的是 Spring Boot Actuator 端点，不新增自定义业务 Controller 接口。

本阶段新增可访问端点 `GET /actuator/health` 和 `GET /actuator/prometheus`。

`GET /actuator/health` 用于检查后端服务健康状态。本阶段在 `SecurityConfig` 中放行该路径，不需要 JWT。成功时返回服务健康状态，例如 `status = UP`。

`GET /actuator/prometheus` 用于向 Prometheus 暴露后端应用指标。本阶段在 `SecurityConfig` 中放行该路径，不需要 JWT。Prometheus 通过该端点抓取 JVM、HTTP 请求、进程、线程、HikariCP 数据库连接池等运行指标。

A2 业务接口继续使用 `/api/aiops/**` 前缀，继续需要 JWT 认证，继续使用 `ApiResult` 和 `PageResult`。

不要把 Actuator 端点改成 `/api/aiops/prometheus`，也不要改动已经完成的 `/api/aiops/dashboard/**` 接口。

## A2-08：A2 项目接口总体验收

A2-08 不新增后端业务接口，只对 A2 已有接口进行总体验收。A2 业务接口继续统一使用 `/api/aiops/**` 前缀，继续使用 JWT 认证，继续使用 `ApiResult` 返回结构，分页接口继续使用 `PageResult`。

A2 项目需要验收的核心接口包括：`GET /api/aiops/health`、`GET /api/aiops/resources/page`、`POST /api/aiops/metric-data/simulate`、`GET /api/aiops/metric-data/latest`、`GET /api/aiops/metric-data/page`、`GET /api/aiops/alert-rules/page`、`GET /api/aiops/alert-events/page`、`POST /api/aiops/alert-events/generate`、`GET /api/aiops/work-orders/page`、`POST /api/aiops/work-orders/create-from-alert`、`POST /api/aiops/work-orders/{id}/handle`、`POST /api/aiops/work-orders/{id}/close`、`POST /api/aiops/root-causes/analyze`、`GET /api/aiops/root-causes/page`、`GET /api/aiops/root-causes/{id}`、`GET /api/aiops/dashboard/summary`、`GET /api/aiops/dashboard/resource-type-stats`、`GET /api/aiops/dashboard/alert-level-stats`、`GET /api/aiops/dashboard/work-order-status-stats`、`GET /api/aiops/dashboard/metric-trend`、`GET /api/aiops/dashboard/recent-alerts`、`GET /api/aiops/dashboard/recent-work-orders`、`GET /api/aiops/dashboard/recent-root-causes`。

A2-07 已接入 Spring Boot Actuator 和 Micrometer Prometheus Registry，需要继续验收 `GET /actuator/health` 和 `GET /actuator/prometheus`。这两个端点不需要 JWT，用于 Prometheus 抓取后端运行指标。验收标准是业务接口带 token 后返回 `code = 0`，分页接口返回 `records` 数组，Actuator 指标端点可以被 Prometheus 正常抓取。

































######################################################################################################################


## R3-01 接口设计

R3-01 新增银行风控模块健康检查接口 GET /api/risk/health。该接口需要 JWT 认证，不加入 SecurityConfig 放行列表。请求时需要携带 Authorization: Bearer token。接口成功时继续使用 ApiResult 统一返回结构，code 为 0，msg 为 success，data 为 enterprise-scaffold risk module running。该接口由 cn.sxu.enterprise.module.risk.controller.RiskHealthController 提供，并使用 @OperLog(title = "银行风控", businessType = "模块健康检查") 记录操作日志。本阶段不新增分页接口，因此不涉及 PageResult。


### R3-02：交易模拟接口

R3-02 新增交易流水接口，所有接口都需要 JWT，继续使用 `ApiResult` 统一返回，分页接口继续使用 `PageResult`，Controller 方法继续使用 `@OperLog`。

`POST /api/risk/transactions/simulate` 用于模拟生成交易流水，请求体使用 `RiskTransactionSimulateRequest`，可传 `count`、`accountNo`、`customerName`、`transactionType`、`channel`、`minAmount`、`maxAmount`、`riskFlag`、`remark`。返回 `ApiResult<List<RiskTransactionVO>>`。

`GET /api/risk/transactions/latest` 用于查询最近 10 条交易流水，返回 `ApiResult<List<RiskTransactionVO>>`。

`GET /api/risk/transactions/page` 用于分页查询交易流水，查询参数使用 `RiskTransactionPageQuery`，支持 `pageNo`、`pageSize`、`transactionNo`、`accountNo`、`customerName`、`merchantName`、`transactionType`、`channel`、`transactionStatus`、`riskFlag`、`status`、`beginTime`、`endTime`。返回 `ApiResult<PageResult<RiskTransactionPageVO>>`。


## R3-03：规则引擎接口设计

R3-03 新增接口继续使用 `/api/risk/**` 前缀，继续需要 JWT，继续返回 `ApiResult`，分页接口继续返回 `PageResult`。

### GET /api/risk/rules/page

作用：分页查询风控规则。

请求参数：`pageNo`、`pageSize`、`ruleCode`、`ruleName`、`ruleType`、`riskLevel`、`status`。

返回结构：`ApiResult<PageResult<RiskRulePageVO>>`。

用途：前端 `/risk/rules` 页面展示规则列表和规则统计。

### POST /api/risk/rule-hits/generate

作用：执行规则引擎，基于 `risk_transaction` 交易流水和启用状态的 `risk_rule` 生成 `risk_rule_hit` 命中记录。

请求体：`RiskRuleHitGenerateRequest`。请求体可以传 `{}`，也可以传 `transactionId` 只处理指定交易，或传 `limit` 控制批量处理最近多少条交易。

返回结构：`ApiResult<List<RiskRuleHitPageVO>>`。

注意：前端调用必须传 JSON 请求体，至少传 `{}`，并设置 `Content-Type: application/json`，避免空 body 导致参数绑定异常。

### GET /api/risk/rule-hits/page

作用：分页查询规则命中记录。

请求参数：`pageNo`、`pageSize`、`hitCode`、`transactionNo`、`accountNo`、`customerName`、`ruleCode`、`ruleName`、`ruleType`、`riskLevel`、`status`、`beginTime`、`endTime`。

返回结构：`ApiResult<PageResult<RiskRuleHitPageVO>>`。

用途：前端 `/risk/rules` 页面展示规则命中记录。



## R3-04：风险评分和人工审核接口

R3-04 新增接口全部位于 `/api/risk/review-orders` 下，继续需要 JWT。`GET /api/risk/review-orders/page` 用于分页查询人工审核单，返回 `ApiResult<PageResult<RiskReviewOrderPageVO>>`。`GET /api/risk/review-orders/summary` 用于查询审核单汇总统计，返回 `ApiResult<RiskReviewSummaryVO>`。`POST /api/risk/review-orders/create-from-transaction` 用于基于 `risk_transaction` 和 `risk_rule_hit` 生成审核单，请求体为 `RiskReviewOrderCreateRequest`，返回 `ApiResult<List<RiskReviewOrderPageVO>>`。`POST /api/risk/review-orders/{id}/approve` 用于审核通过，请求体为 `RiskReviewApproveRequest`。`POST /api/risk/review-orders/{id}/reject` 用于审核拒绝，请求体为 `RiskReviewRejectRequest`。所有 Controller 方法继续使用 `@OperLog` 记录操作日志。


## R3-05：Kafka 接入接口设计

R3-05 新增 Kafka 单条模拟发布接口 `POST /api/risk/kafka/simulate-publish`。

该接口需要 JWT 认证，请求头必须携带 `Authorization: Bearer <token>`，请求体为 `RiskTransactionKafkaMessage`，返回结构为 `ApiResult<RiskTransactionKafkaMessage>`。

该接口用于向 Kafka Topic `risk.transaction.events` 发送一条模拟交易消息，后端会自动补齐缺失的交易流水号、账户号、客户信息、商户信息、交易类型、渠道、金额、币种、IP、设备号、地点、交易时间、交易状态和备注。

R3-05 新增 Kafka 批量模拟发布接口 `POST /api/risk/kafka/simulate-batch`。

该接口需要 JWT 认证，请求头必须携带 `Authorization: Bearer <token>`，请求体为 `RiskKafkaBatchSimulateRequest`，返回结构为 `ApiResult<Integer>`。

该接口用于批量向 Kafka Topic `risk.transaction.events` 发送模拟交易消息，支持 `count`、`transactionType`、`channel`、`minAmount`、`maxAmount`、`location`、`remark` 等参数。

`count` 最小为 1，最大为 100。

两个接口都继续使用 `@OperLog` 记录操作日志，操作日志标题固定为“银行风控-Kafka”，业务类型分别为“模拟发布”和“批量模拟发布”。

Kafka 消费后的交易数据继续通过已有 `GET /api/risk/transactions/page` 查询。


## R3-06：风控看板接口

### 风控看板汇总统计

接口：`GET /api/risk/dashboard/summary`

认证：需要 JWT。

返回：`ApiResult<RiskDashboardSummaryVO>`

统计字段：

- `transactionTotal`：交易总数
- `riskTransactionTotal`：风险交易数
- `ruleTotal`：风控规则数
- `ruleHitTotal`：规则命中数
- `pendingReviewTotal`：待审核数
- `approvedReviewTotal`：审核通过数
- `rejectedReviewTotal`：审核拒绝数

### 交易渠道统计

接口：`GET /api/risk/dashboard/channel-stats`

返回：`ApiResult<List<RiskChannelStatVO>>`

### 交易类型统计

接口：`GET /api/risk/dashboard/transaction-type-stats`

返回：`ApiResult<List<RiskTransactionTypeStatVO>>`

### 风险等级统计

接口：`GET /api/risk/dashboard/risk-level-stats`

返回：`ApiResult<List<RiskLevelStatVO>>`

### 最近交易流水

接口：`GET /api/risk/dashboard/recent-transactions`

返回：`ApiResult<List<RiskRecentTransactionVO>>`

### 最近规则命中

接口：`GET /api/risk/dashboard/recent-rule-hits`

返回：`ApiResult<List<RiskRecentRuleHitVO>>`

### 最近人工审核单

接口：`GET /api/risk/dashboard/recent-review-orders`

返回：`ApiResult<List<RiskRecentReviewOrderVO>>`

所有接口继续使用 `/api/risk/**` 路径，继续需要 JWT 认证，继续使用 `ApiResult` 统一返回，Controller 方法继续使用 `@OperLog` 记录操作日志。

## R3-07：项目三总体验收接口说明

R3-07 不新增后端业务接口。本阶段对项目三银行实时交易风控模块已有接口进行总体验收。

项目三核心接口包括：

- `GET /api/risk/health`：银行风控模块健康检查。
- `POST /api/risk/transactions/simulate`：模拟生成银行交易流水。
- `GET /api/risk/transactions/latest`：查询最新交易流水。
- `GET /api/risk/transactions/page`：分页查询交易流水。
- `GET /api/risk/rules/page`：分页查询风控规则。
- `POST /api/risk/rule-hits/generate`：基于交易流水生成规则命中记录。
- `GET /api/risk/rule-hits/page`：分页查询规则命中记录。
- `GET /api/risk/review-orders/page`：分页查询人工审核单。
- `GET /api/risk/review-orders/summary`：查询人工审核单汇总统计。
- `POST /api/risk/review-orders/create-from-transaction`：基于交易和规则命中生成审核单。
- `POST /api/risk/review-orders/{id}/approve`：人工审核通过。
- `POST /api/risk/review-orders/{id}/reject`：人工审核拒绝。
- `POST /api/risk/kafka/simulate-publish`：模拟发布单条 Kafka 交易消息。
- `POST /api/risk/kafka/simulate-batch`：批量模拟发布 Kafka 交易消息。
- `GET /api/risk/dashboard/summary`：风控看板汇总统计。
- `GET /api/risk/dashboard/channel-stats`：交易渠道统计。
- `GET /api/risk/dashboard/transaction-type-stats`：交易类型统计。
- `GET /api/risk/dashboard/risk-level-stats`：风险等级统计。
- `GET /api/risk/dashboard/recent-transactions`：最近交易流水。
- `GET /api/risk/dashboard/recent-rule-hits`：最近规则命中。
- `GET /api/risk/dashboard/recent-review-orders`：最近人工审核单。

以上接口除公开健康检查规则外，均继续使用 JWT 认证，继续返回 `ApiResult` 统一结构，分页接口继续返回 `PageResult` 结构，Controller 方法继续使用 `@OperLog` 记录操作日志。

************************************************************************************************************



## D4-01：数据治理模块健康检查接口

新增接口：`GET /api/datahub/health`。

该接口用于验证国企 / 政务数据治理与共享交换平台 `datahub` 模块是否成功接入后端工程。

接口需要 JWT 认证，请求头必须携带 `Authorization: Bearer <token>`。

接口成功时返回 `ApiResult<String>`，`data` 固定为 `enterprise-scaffold datahub module running`。

接口继续使用 `@OperLog(title = "数据治理", businessType = "模块健康检查")` 记录操作日志。

该接口不加入 `SecurityConfig` 放行列表，未登录访问时应返回 `code = 401`。



## D4-02：数据源管理接口

D4-02 新增 GET /api/datahub/datasources/page 数据源分页查询接口，认证方式为 Authorization: Bearer <token>。请求参数包括 pageNo、pageSize、datasourceCode、datasourceName、datasourceType、host、databaseName、envType、ownerName、testStatus、status。返回结构为 ApiResult<PageResult<DatahubDatasourcePageVO>>。D4-02 新增 POST /api/datahub/datasources/test-connection 数据源连接测试接口，认证方式为 Authorization: Bearer <token>，请求体包括 datasourceType、jdbcUrl、username、password，返回结构为 ApiResult<DatahubDatasourceTestVO>。两个接口继续使用 @OperLog 记录操作日志，title 固定为 数据治理-数据源管理。


# 03 API Design

## 接口统一规则

当前项目后端接口统一使用 REST 风格，Datahub 模块接口统一使用：

```text
/api/datahub/**
```

D4-03 元数据采集接口统一使用：

```text
/api/datahub/metadata/**
```

统一返回结构为 `ApiResult`，典型结构如下：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

分页接口统一返回 `PageResult`，典型结构如下：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "pageNo": 1,
    "pageSize": 10,
    "total": 100,
    "pages": 10,
    "records": []
  }
}
```

前端调用接口时要注意 ApiResult 解包。若 Axios 封装层没有自动解包，则实际数据在：

```ts
response.data.data
```

若 Axios 封装层已经解包，则实际数据可能直接在：

```ts
response
```

因此前端推荐统一使用：

```ts
function unwrapApiData<T>(response: unknown): T {
  const anyResponse = response as any
  return (anyResponse?.data?.data ?? anyResponse?.data ?? anyResponse ?? {}) as T
}
```

## D4-03 执行元数据采集接口

### 接口路径

```http
POST /api/datahub/metadata/collect
```

### 请求体

```json
{
  "dataSourceId": 1
}
```

### 请求字段说明

| 字段名 | 类型 | 必填 | 说明 |
|---|---|---|---|
| dataSourceId | number | 是 | D4-02 数据源管理中已有的数据源 ID |

### 后端处理逻辑

后端根据 `dataSourceId` 查询：

```sql
SELECT * FROM datahub_datasource WHERE id = ? AND deleted = 0;
```

然后读取：

```text
datasource_type
datasource_name
jdbc_url
host
port
database_name
username
password
status
deleted
```

当 `datasource_type` 为 `MYSQL` 时，继续连接目标数据库并采集元数据。当前 D4-03 仅支持 MySQL。

### 成功响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "collectBatchNo": "META202607081200001234",
    "dataSourceId": 1,
    "dataSourceName": "本地MySQL",
    "tableTotal": 12,
    "columnTotal": 98,
    "costTime": 1200
  }
}
```

### 失败响应示例

数据源 ID 为空：

```json
{
  "code": 500,
  "msg": "数据源ID不能为空",
  "data": null
}
```

数据源不是 MySQL：

```json
{
  "code": 500,
  "msg": "D4-03 当前仅支持 MySQL 数据源元数据采集，当前数据源类型为：xxx",
  "data": null
}
```

密码为空：

```json
{
  "code": 500,
  "msg": "元数据采集失败：Access denied for user 'root'@'172.18.0.5' (using password: NO)",
  "data": null
}
```

连接失败：

```json
{
  "code": 500,
  "msg": "元数据采集失败：Communications link failure",
  "data": null
}
```

## D4-03 元数据表分页接口

### 接口路径

```http
GET /api/datahub/metadata/tables/page
```

或按项目实际 Controller 保持为：

```http
GET /api/datahub/metadata/table/page
```

接口路径必须以前端 `getMetadataTablePage()` 中的真实路径为准，文档与代码需保持一致。

### 查询参数

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "dataSourceId": 1,
  "schemaName": "",
  "tableName": "",
  "tableType": "",
  "status": null
}
```

### 返回示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "pageNo": 1,
    "pageSize": 10,
    "total": 12,
    "pages": 2,
    "records": [
      {
        "id": 1,
        "dataSourceId": 1,
        "dataSourceName": "本地MySQL",
        "schemaName": "enterprise_scaffold",
        "tableName": "datahub_datasource",
        "tableComment": "数据源表",
        "tableType": "TABLE",
        "rowCount": 1,
        "collectTime": "2026-07-08 18:30:00"
      }
    ]
  }
}
```

## D4-03 字段元数据分页接口

### 接口路径

```http
GET /api/datahub/metadata/columns/page
```

或按项目实际 Controller 保持为：

```http
GET /api/datahub/metadata/column/page
```

接口路径必须以前端 `getMetadataColumnPage()` 中的真实路径为准。

### 查询参数

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "tableId": 1,
  "dataSourceId": 1,
  "tableName": "datahub_datasource",
  "columnName": "",
  "dataType": "",
  "primaryKeyFlag": null
}
```

### 返回示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "pageNo": 1,
    "pageSize": 10,
    "total": 10,
    "pages": 1,
    "records": [
      {
        "id": 1,
        "tableId": 1,
        "dataSourceId": 1,
        "dataSourceName": "本地MySQL",
        "schemaName": "enterprise_scaffold",
        "tableName": "datahub_datasource",
        "columnName": "datasource_type",
        "columnComment": "数据源类型",
        "dataType": "VARCHAR",
        "columnType": "VARCHAR(50)",
        "nullableFlag": 0,
        "primaryKeyFlag": 0,
        "ordinalPosition": 2
      }
    ]
  }
}
```

## D4-03 采集日志分页接口

### 接口路径

```http
GET /api/datahub/metadata/collect-logs/page
```

或按项目实际 Controller 保持为：

```http
GET /api/datahub/metadata/logs/page
```

接口路径必须以前端 `getMetadataCollectLogPage()` 中的真实路径为准。

### 查询参数

```json
{
  "pageNo": 1,
  "pageSize": 5,
  "dataSourceId": 1,
  "collectBatchNo": "",
  "collectStatus": null
}
```

### 返回示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "pageNo": 1,
    "pageSize": 5,
    "total": 1,
    "pages": 1,
    "records": [
      {
        "id": 1,
        "collectBatchNo": "META202607081200001234",
        "dataSourceId": 1,
        "dataSourceName": "本地MySQL",
        "collectStatus": 0,
        "tableTotal": 12,
        "columnTotal": 98,
        "costTime": 1200,
        "startTime": "2026-07-08 18:30:00",
        "endTime": "2026-07-08 18:30:01",
        "errorMsg": null
      }
    ]
  }
}
```

## 前端字段约定

采集成功后，前端提示应优先读取：

```text
tableTotal
columnTotal
```

兼容读取：

```text
tableCount
tableNum
columnCount
columnNum
```

推荐写法：

```ts
const response = await collectMetadata({
  dataSourceId: collectForm.dataSourceId
})

const collectResult = unwrapApiData<CollectResultLike>(response)

const collectedTableTotal = toNumber(
  collectResult.tableTotal ?? collectResult.tableCount ?? collectResult.tableNum
)

const collectedColumnTotal = toNumber(
  collectResult.columnTotal ?? collectResult.columnCount ?? collectResult.columnNum
)

ElMessage.success(`采集成功：表 ${collectedTableTotal} 张，字段 ${collectedColumnTotal} 个`)
```

## D4-03 接口验收标准

1. `POST /api/datahub/metadata/collect` 能返回 `code = 200`。
2. 返回 `data.tableTotal` 和 `data.columnTotal`。
3. 前端不显示 `undefined`。
4. 表分页接口能返回 `records` 和 `total`。
5. 字段分页接口能根据表 ID 或数据源 ID 返回字段。
6. 采集日志分页接口能显示成功或失败记录。
7. 所有接口均保持 `ApiResult` 返回结构。
8. 所有分页接口均保持 `PageResult` 返回结构。

## D4-04 数据质量检测接口

D4-04 新增接口继续统一使用 `ApiResult` 返回结构，分页接口继续使用 `ApiResult<PageResult<XXXPageVO>>`。新增 `GET /api/datahub/quality-rules/page`，用于分页查询质量规则，支持 `pageNo`、`pageSize`、`ruleName`、`ruleType`、`targetType`、`targetTableId`、`targetColumnId`、`status` 查询参数。新增 `POST /api/datahub/quality-results/check`，用于执行质量检测，请求体可以传 `{ "ruleId": 1 }` 表示只检测单条规则，也可以传 `{ "targetTableId": 1 }` 表示检测某张元数据表下全部启用规则，返回本次生成的检测结果列表。新增 `GET /api/datahub/quality-results/page`，用于分页查询质量检测结果，支持 `pageNo`、`pageSize`、`ruleName`、`ruleCode`、`tableCode`、`columnName`、`checkStatus`、`tableId`、`columnId` 查询参数。接口路径必须保持 `/api/datahub/quality-rules/page`、`/api/datahub/quality-results/check`、`/api/datahub/quality-results/page`，不能改成 `/api/datahub/quality/rules/page` 或 `/api/datahub/quality/check`。


## D4-05 接口设计：敏感数据识别和脱敏

D4-05 新增敏感数据识别和脱敏接口，所有接口继续使用 JWT 认证，成功响应继续使用 `ApiResult`，分页接口继续使用 `ApiResult<PageResult<XXXPageVO>>`。新增接口包括：`POST /api/datahub/sensitive-fields/scan`，用于基于 `datahub_metadata_column` 扫描敏感字段；`GET /api/datahub/sensitive-fields/page`，用于分页查询敏感字段识别结果；`GET /api/datahub/mask-rules/page`，用于分页查询脱敏规则；`POST /api/datahub/mask-results/preview`，用于对指定敏感字段进行脱敏预览；`GET /api/datahub/mask-results/page`，用于分页查询脱敏预览结果。本阶段 Controller 为 `DatahubSensitiveController`，Service 为 `DatahubSensitiveService` 和 `DatahubSensitiveServiceImpl`，Mapper 包括 `DatahubSensitiveFieldMapper`、`DatahubMaskRuleMapper`、`DatahubMaskResultMapper`。


## D4-06 接口设计：API 共享发布与数据治理看板

D4-06 新增 API 共享发布接口和数据治理看板接口，所有接口继续使用 JWT 认证，返回结构继续使用 `ApiResult`，分页结构继续使用 `PageResult`。API 共享发布接口包括：`GET /api/datahub/api-publishes/page` 用于分页查询 API 发布记录，支持 `apiCode`、`apiName`、`datasourceName`、`tableName`、`requestMethod`、`onlineStatus`、`status` 等查询条件；`POST /api/datahub/api-publishes/publish-from-table` 用于基于 `datahub_metadata_table.id` 发布 API；`POST /api/datahub/api-publishes/{id}/online` 用于将 API 上线；`POST /api/datahub/api-publishes/{id}/offline` 用于将 API 下线。数据治理看板接口包括：`GET /api/datahub/dashboard/summary` 汇总统计数据源、元数据表、元数据字段、质量规则、质量结果、敏感字段、脱敏规则、脱敏结果、API 发布数量和已上线 API 数量；`GET /api/datahub/dashboard/datasource-type-stats` 统计数据源类型分布；`GET /api/datahub/dashboard/quality-result-stats` 统计质量检测通过和不通过数量；`GET /api/datahub/dashboard/sensitive-type-stats` 统计敏感字段类型分布；`GET /api/datahub/dashboard/recent-quality-results` 查询最近质量检测结果；`GET /api/datahub/dashboard/recent-apis` 查询最近 API 发布记录。

## D4-07：项目四接口验收说明

D4-07 不新增后端业务接口。本阶段只验收项目四已经完成的接口是否仍然可用，所有接口继续使用 `Authorization: Bearer <token>` 认证方式，成功返回继续使用 `ApiResult`，分页返回继续使用 `PageResult`。固定验收接口包括：`GET /api/datahub/datasources/page`、`POST /api/datahub/datasources/test-connection`、`POST /api/datahub/metadata/collect`、`GET /api/datahub/metadata/tables/page`、`GET /api/datahub/metadata/columns/page`、`GET /api/datahub/metadata/collect-logs/page`、`GET /api/datahub/quality-rules/page`、`POST /api/datahub/quality-results/check`、`GET /api/datahub/quality-results/page`、`POST /api/datahub/sensitive-fields/scan`、`GET /api/datahub/sensitive-fields/page`、`GET /api/datahub/mask-rules/page`、`POST /api/datahub/mask-results/preview`、`GET /api/datahub/mask-results/page`、`GET /api/datahub/api-publishes/page`、`POST /api/datahub/api-publishes/publish-from-table`、`POST /api/datahub/api-publishes/{id}/online`、`POST /api/datahub/api-publishes/{id}/offline`、`GET /api/datahub/dashboard/summary`、`GET /api/datahub/dashboard/datasource-type-stats`、`GET /api/datahub/dashboard/quality-result-stats`、`GET /api/datahub/dashboard/sensitive-type-stats`、`GET /api/datahub/dashboard/recent-quality-results`、`GET /api/datahub/dashboard/recent-apis`。特别注意，D4-06 实际 API 发布后端路径是 `/api/datahub/api-publishes/**`，不是旧规划里的 `/api/datahub/apis/**`。

******************************************************************************************************

## I5-01：IAM 模块健康检查接口

I5-01 新增 IAM 模块健康检查接口：`GET /api/iam/health`。该接口用于验证 `cn.sxu.enterprise.module.iam` 模块是否已被 Spring Boot 正常扫描和加载。接口继续使用 JWT 认证，不加入 SecurityConfig 放行列表；继续使用 `ApiResult<String>` 统一返回结构；Controller 方法继续使用 `@OperLog(title = "统一身份认证与安全审计", businessType = "模块健康检查")` 记录操作日志。成功返回示例为 `{"code":0,"msg":"success","data":"enterprise-scaffold iam module running"}`。未登录或 token 失效时，继续沿用系统已有 401 返回规则。


## I5-02：接口访问日志接口

I5-02 新增 IAM 接口访问日志分页查询接口：`GET /api/iam/access-logs/page`。该接口继续使用 JWT 认证，不加入放行列表，Controller 方法继续使用 `@OperLog`，返回结构继续使用 `ApiResult<PageResult<IamAccessLogPageVO>>`。查询参数包括 `pageNo`、`pageSize`、`requestUri`、`requestMethod`、`username`、`clientIp`、`accessStatus`、`beginTime` 和 `endTime`。分页结果字段包括接口路径、请求方法、访问用户、访问 IP、访问状态、响应码、耗时、访问时间、操作名称和备注。该接口属于项目五 IAM 模块，接口路径继续使用 `/api/iam/**`，不影响 D4 的 `/api/datahub/**` 接口，也不修改已有 `/api/auth/login` 和 JWT 认证逻辑。


### I5-03：异常登录检测接口

I5-03 新增 IAM 异常登录风险分页查询接口 `GET /api/iam/login-risks/page` 和异常登录检测接口 `POST /api/iam/login-risks/detect`。两个接口继续使用 JWT 认证，不加入放行列表，Controller 方法继续使用 `@OperLog`，返回结构继续使用 `ApiResult` 和 `PageResult`。分页查询参数包括 `pageNo`、`pageSize`、`riskCode`、`username`、`clientIp`、`riskType`、`riskLevel`、`handleStatus`、`beginTime`、`endTime`。检测接口基于 `sys_login_log` 中登录失败记录生成 `iam_login_risk` 风险数据，不修改登录主流程，不重写 `/api/auth/login`，不影响 D4 的 `/api/datahub/**` 接口和 I5-02 的 `/api/iam/access-logs/page` 接口。

### I5-04
I5-04 新增 IAM 接口限流规则分页查询接口 GET /api/iam/rate-limit-rules/page、规则启用接口 POST /api/iam/rate-limit-rules/{id}/enable、规则停用接口 POST /api/iam/rate-limit-rules/{id}/disable、模拟检测接口 POST /api/iam/rate-limit-rules/simulate。接口继续使用 JWT 认证，不加入放行列表，Controller 方法继续使用 @OperLog，返回结构继续使用 ApiResult 和 PageResult。分页查询参数包括 pageNo、pageSize、ruleCode、ruleName、requestUri、requestMethod、limitDimension、enabled、beginTime、endTime。模拟检测请求参数包括 requestUri、requestMethod、username、clientIp、currentRequests。模拟检测只根据 iam_rate_limit_rule 中已启用规则判断是否命中，不修改登录主流程，不重写 /api/auth/login，不修改 SecurityConfig，不实现真实 Filter / Interceptor 强制限流。

### I5-05:
I5-05 新增 IAM 安全策略配置分页查询接口 GET /api/iam/security-policies/page、策略启用接口 POST /api/iam/security-policies/{id}/enable、策略停用接口 POST /api/iam/security-policies/{id}/disable、策略配置更新接口 POST /api/iam/security-policies/{id}/update-config。接口继续使用 JWT 认证，不加入放行列表，Controller 方法继续使用 @OperLog，返回结构继续使用 ApiResult 和 PageResult。分页查询参数包括 pageNo、pageSize、policyCode、policyName、policyType、policyLevel、effectiveScope、enabled、status、beginTime、endTime。更新配置请求参数包括 policyValue、policyUnit、effectiveScope、enabled、remark。本阶段只管理安全策略配置，不修改登录主流程，不重写 /api/auth/login，不修改 SecurityConfig，不实现真实 Filter / Interceptor 强制限流。


## I5-06：权限审计增强接口

I5-06 新增 IAM 权限审计分页查询接口 `GET /api/iam/permission-audits/page`、模拟生成审计记录接口 `POST /api/iam/permission-audits/simulate` 和复核审计记录接口 `POST /api/iam/permission-audits/{id}/review`。接口继续使用 JWT 认证，不加入放行列表，Controller 方法继续使用 `@OperLog`，返回结构继续使用 `ApiResult` 和 `PageResult`。分页查询参数包括 `pageNo`、`pageSize`、`auditCode`、`auditType`、`targetType`、`targetName`、`changeAction`、`riskLevel`、`reviewStatus`、`operatorName`、`beginTime`、`endTime`。模拟生成请求参数包括 `auditType`、`targetType`、`targetId`、`targetName`、`changeAction`、`beforeValue`、`afterValue`、`riskLevel`、`requestIp`、`operatorName`、`remark`。复核请求参数包括 `reviewStatus`、`reviewBy`、`remark`。本阶段只做审计记录管理和复核闭环，不修改登录主流程，不修改 SecurityConfig，不实现真实权限变更拦截。


## I5-07：IAM 安全看板接口

I5-07 新增 IAM 安全看板总览接口：`GET /api/iam/security-dashboard/overview`。该接口继续使用 JWT 认证，不加入 SecurityConfig 放行列表，Controller 方法继续使用 `@OperLog`，返回结构继续使用 `ApiResult<IamSecurityDashboardVO>`。`IamSecurityDashboardVO` 包含 `summary`、`riskDistributions`、`reviewStatusStats`、`policyStatusStats`、`recentEvents` 五部分数据。`summary` 展示今日访问量、今日失败访问、未处理登录风险、高风险权限变更、待复核审计、启用安全策略和启用限流规则；`riskDistributions` 展示异常登录风险和权限审计风险的低、中、高风险分布；`reviewStatusStats` 展示权限审计待复核、已复核、已忽略统计；`policyStatusStats` 展示安全策略和限流规则启停统计；`recentEvents` 汇总最近异常登录和权限审计事件。






