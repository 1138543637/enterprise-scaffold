## S0-07 前端初始化参考

S0-07 初始化 Vue3 前端，参考了 Vue3 中后台项目的常见目录组织方式，但没有复制任何开源项目代码。

### 1. 本阶段参考项目

| 项目                | 地址                                              | 参考内容                              |
| ----------------- | ----------------------------------------------- | --------------------------------- |
| RuoYi-Vue3        | https://github.com/yangzongzhuan/RuoYi-Vue3     | Vue3、Vite、Element Plus 后台管理项目前端结构 |
| vue-element-admin | https://github.com/PanJiaChen/vue-element-admin | 后台管理系统登录页、布局、权限路由设计思路             |
| Vue Vben Admin    | https://github.com/vbenjs/vue-vben-admin        | Vue3 + TypeScript 中后台工程化目录设计      |
| Vite              | https://github.com/vitejs/vite                  | 前端项目构建工具和开发服务器                    |
| Vue Router        | https://github.com/vuejs/router                 | Vue3 官方路由方案                       |
| Pinia             | https://github.com/vuejs/pinia                  | Vue3 状态管理方案                       |
| Element Plus      | https://github.com/element-plus/element-plus    | Vue3 UI 组件库                       |
| Axios             | https://github.com/axios/axios                  | HTTP 请求封装                         |

---

### 2. 本阶段实际使用方式

本项目没有直接复制上述开源项目代码，而是只参考以下思路：

```text
1. 前端项目使用 Vue3 + Vite + TypeScript
2. 使用 Element Plus 搭建基础登录页和首页
3. 使用 Vue Router 管理 /login 和 /dashboard 页面
4. 使用 Pinia 保存 token 和当前用户信息
5. 使用 Axios 统一请求后端接口
6. 使用 Vite proxy 将 /api 请求转发到后端 http://localhost:8080
7. 前端目录按照 api、router、stores、types、views 分层
```

---

### 3. 本项目 S0-07 前端目录

```text
scaffold-frontend
├── index.html
├── package.json
├── pnpm-lock.yaml
├── tsconfig.json
├── tsconfig.app.json
├── tsconfig.node.json
├── vite.config.ts
└── src
    ├── api
    │   ├── request.ts
    │   └── system
    │       └── auth.ts
    ├── router
    │   └── index.ts
    ├── stores
    │   └── auth.ts
    ├── types
    │   └── api.ts
    ├── views
    │   ├── dashboard
    │   │   └── DashboardView.vue
    │   └── login
    │       └── LoginView.vue
    ├── App.vue
    ├── main.ts
    ├── style.css
    └── vite-env.d.ts
```

---

### 4. 本阶段没有照搬的内容

S0-07 没有照搬以下内容：

```text
1. 没有复制若依前端完整代码
2. 没有复制若依动态路由代码
3. 没有复制若依权限指令代码
4. 没有复制 Vben Admin 的复杂工程封装
5. 没有复制 vue-element-admin 的 Vue2 旧版实现
6. 没有引入复杂主题配置
7. 没有引入复杂菜单权限渲染
```

---

### 5. 后续可继续参考的方向

后续阶段可以逐步参考这些设计，但仍然要自己实现：

```text
1. 动态菜单渲染
2. 基于按钮权限的页面按钮显示 / 隐藏
3. 系统管理布局
4. 用户管理页面
5. 角色管理页面
6. 菜单管理页面
7. 登录过期处理
8. 前端权限指令
9. 后端权限注解
```

## S0-10：Docker Compose 部署参考说明

### 参考方向

S0-10 主要参考企业后台项目的部署组织方式，包括：

- 后端 Dockerfile
- 前端 Dockerfile
- Nginx 托管前端静态资源
- Nginx 反向代理 `/api/**`
- Docker Compose 编排 MySQL、后端、前端
- 使用 `.env` 管理本地环境变量
- 使用 volume 持久化数据库数据
- 使用 bind mount 持久化上传文件

### 可参考项目

| 项目 | 参考点 |
|---|---|
| RuoYi-Vue | 企业后台部署目录、后端服务和前端服务拆分思路 |
| RuoYi-Vue3 | Vue3 前端打包和 Nginx 部署思路 |
| Docker 官方文档 | Docker Compose、Dockerfile、多阶段构建、环境变量、服务依赖、健康检查 |

### 正确参考方式

- 参考部署文件组织方式
- 参考前后端分离部署思路
- 参考 Nginx 代理 `/api` 的思路
- 参考 Docker Compose 管理多服务的方式

### 不允许的方式

- 不复制若依的业务代码
- 不替换本项目包名 `cn.sxu.enterprise`
- 不替换本项目数据库名 `enterprise_scaffold`
- 不替换本项目已有表结构
- 不替换本项目已有接口路径
- 不提交真实密码、真实密钥、`.env` 文件

## M1-01：智能矿山模块骨架参考说明

### 本阶段参考项目

#### RuoYi-Vue-Pro

地址：`https://github.com/YunaiV/ruoyi-vue-pro`

参考点：

- 企业后台整体结构
- 业务模块拆分方式
- Controller / Service / Mapper / VO 分层组织方式

本项目只参考模块组织思想，不复制代码。

#### RuoYi-Vue

地址：`https://github.com/yangzongzhuan/RuoYi-Vue`

参考点：

- Spring Security + JWT 后台接口保护
- 操作日志注解与切面记录思路
- 系统模块与业务模块分离方式

本项目已经有自己的 `@OperLog` 和 `OperLogAspect`，M1-01 继续复用已有实现，不复制 RuoYi 代码。

### 本项目自己的实现

M1-01 新增业务包：`cn.sxu.enterprise.module.mine`

新增接口：`GET /api/mine/health`

新增控制器：`cn.sxu.enterprise.module.mine.controller.MineHealthController`

### 正确参考方式

- 参考目录分层
- 参考接口组织方式
- 参考命名规范
- 参考模块拆分思想
- 自己编写核心代码
- 自己维护接口文档和部署文档

### 错误方式

- 不要复制若依代码直接改名
- 不要 fork 其他项目只改 logo
- 不要引入当前阶段不需要的大量依赖
- 不要在 M1-01 提前复制完整智能矿山项目

## M1-02

参考：
- RuoYi-Vue 分页结构
- MyBatis-Plus BaseMapper + Page + Wrapper

本项目：
- mine_device
- mine_sensor
- MineDeviceController
- MineSensorController
- ApiResult + PageResult
- JWT + @OperLog

## M1-03 参考说明

M1-03 参考 RuoYi-Vue、RuoYi-Vue-Pro 和 MyBatis-Plus 的分层思想。

参考内容：

1. 参考 RuoYi 系列 Controller / Service / Mapper / Entity 分层组织方式。
2. 参考 RuoYi 系列操作日志注解思想。
3. 参考 MyBatis-Plus BaseMapper、Page、LambdaQueryWrapper 的分页查询方式。
4. 参考企业后台项目中业务数据表与台账表分离的设计思路。

本项目自己的实现是：

cn.sxu.enterprise.module.mine
mine_sensor_data
MineSensorData
MineSensorDataMapper
MineSensorDataService
MineSensorDataServiceImpl
MineSensorDataController
POST /api/mine/sensor-data/simulate
GET /api/mine/sensor-data/latest
GET /api/mine/sensor-data/page

本阶段不复制 RuoYi 代码，不引入 RuoYi 代码生成器，不引入新的权限框架，不提前接入 MQTT / EMQX。


## M1-04：告警规则和告警事件参考说明

M1-04 参考项目：

```text
RuoYi-Vue
RuoYi-Vue-Pro
MyBatis-Plus
```

参考内容：

```text
1. 参考 RuoYi 系列后台管理系统的 Controller / Service / Mapper / Entity 分层方式。
2. 参考 RuoYi 系列操作日志注解思想。
3. 参考 MyBatis-Plus BaseMapper、Page、LambdaQueryWrapper 的分页查询方式。
4. 参考企业后台项目中“规则表 + 事件表”的业务建模方式。
```

本项目自己的实现是：

```text
cn.sxu.enterprise.module.mine
mine_alarm_rule
mine_alarm_event
MineAlarmRule
MineAlarmEvent
MineAlarmRuleMapper
MineAlarmEventMapper
MineAlarmRuleService
MineAlarmEventService
MineAlarmRuleServiceImpl
MineAlarmEventServiceImpl
MineAlarmRuleController
MineAlarmEventController
MineAlarmGenerateRequest
MineAlarmRulePageQuery
MineAlarmRulePageVO
MineAlarmEventPageQuery
MineAlarmEventPageVO
GET /api/mine/alarm-rules/page
GET /api/mine/alarm-events/page
POST /api/mine/alarm-events/generate
ApiResult
PageResult
JWT
@OperLog
```

本阶段参考方式：

```text
1. 只参考企业后台项目的分层思想。
2. 只参考操作日志注解的设计思想。
3. 只参考分页查询接口的组织方式。
4. 只参考“规则配置 + 事件记录”的业务建模方式。
5. 代码仍然按照本项目已有包名、类名、接口路径和返回结构自己实现。
```

不能做：

```text
1. 不能复制若依代码。
2. 不能引入若依代码生成器。
3. 不能引入新的权限框架。
4. 不能修改现有 JWT 登录认证方式。
5. 不能修改 ApiResult 和 PageResult 返回结构。
6. 不能修改已有 mine_device、mine_sensor、mine_sensor_data 表名。
7. 不能把 MineAlarmRule 改成 AlarmRule。
8. 不能把 MineAlarmEvent 改成 AlarmEvent。
9. 不能提前引入 MQTT / EMQX。
10. 不能提前做前端页面。
```

M1-04 的价值：

```text
M1-04 将 M1-03 的传感器数据进一步转换为业务告警事件，为后续 M1-05 工单闭环提供基础。
```



## M1-05 工单闭环参考说明

M1-05 参考 RuoYi-Vue、RuoYi-Vue-Pro 和 MyBatis-Plus。主要参考方向包括：Controller、Service、Mapper、Entity 分层方式，操作日志注解思想，MyBatis-Plus BaseMapper、Page、LambdaQueryWrapper 的分页查询方式，以及企业后台中告警、工单和处理状态流转的基础建模思路。

本项目自己的实现为 cn.sxu.enterprise.module.mine 包下的智能矿山工单闭环能力。新增 mine_work_order 表，新增 MineWorkOrder、MineWorkOrderMapper、MineWorkOrderService、MineWorkOrderServiceImpl、MineWorkOrderController、MineWorkOrderCreateRequest、MineWorkOrderHandleRequest、MineWorkOrderCloseRequest、MineWorkOrderPageQuery、MineWorkOrderPageVO。

本阶段新增接口包括 GET /api/mine/work-orders/page、POST /api/mine/work-orders/create-from-alarm、POST /api/mine/work-orders/{id}/handle、POST /api/mine/work-orders/{id}/close。接口继续使用 ApiResult、PageResult、JWT 和 @OperLog。

本阶段不能复制若依代码，不能引入若依代码生成器，不能引入新的权限框架，不能提前引入复杂流程引擎。只参考企业后台的分层组织方式、日志思想和工单状态流转建模思路，核心代码仍然按照本项目已有命名和结构独立实现。


## M1-06：智能矿山看板参考说明

M1-06 参考 RuoYi-Vue、RuoYi-Vue3、Vue Vben Admin 和 MyBatis-Plus。

参考内容：

1. 参考后台首页统计卡片布局。
2. 参考 Vue3 + Element Plus 表格和页面组织方式。
3. 参考 ECharts 图表展示方式。
4. 参考 MyBatis-Plus BaseMapper、LambdaQueryWrapper 和 QueryWrapper 统计查询写法。

本项目自己的实现是：

- MineDashboardController
- MineDashboardService
- MineDashboardServiceImpl
- MineDashboardSummaryVO
- MineAlarmLevelStatVO
- MineSensorTypeStatVO
- MineWorkOrderStatusStatVO
- MineRecentAlarmVO
- MineRecentWorkOrderVO
- /api/mine/dashboard/summary
- /api/mine/dashboard/alarm-level-stats
- /api/mine/dashboard/sensor-type-stats
- /api/mine/dashboard/work-order-status-stats
- /api/mine/dashboard/recent-alarms
- /api/mine/dashboard/recent-work-orders
- scaffold-frontend/src/views/mine/MineDashboardView.vue

不能复制若依代码，不能引入若依代码生成器，不能修改已有项目命名契约。

## M1-07：MQTT / EMQX 参考说明

M1-07 参考 Spring Integration MQTT 官方文档、EMQX 官方 Docker 部署方式以及 MQTT Topic 订阅和消息发布的基础实践。

本阶段参考内容只用于学习 MQTT Broker 与 Spring Boot 后端之间的连接方式、Spring 后端如何订阅 MQTT Topic、如何将 MQTT 消息转换为业务 DTO、如何通过 Docker Compose 启动 EMQX，以及如何使用固定 Topic 接入传感器数据。

本阶段没有复制第三方业务代码，没有引入新的权限框架，没有改变已有系统架构。

M1-07 继续沿用本项目已有命名。

已有业务包名：

```text
cn.sxu.enterprise.module.mine
```

已有实体和服务：

```text
MineSensor
MineSensorData
MineAlarmEventService
```

已有数据库表：

```text
mine_sensor
mine_sensor_data
mine_alarm_event
```

本阶段新增类名固定为：

```text
MineMqttProperties
MineMqttConfig
MineSensorMqttMessage
MineSensorMqttListener
MineMqttController
```

## M1-08：MQTT 数据模拟增强参考说明

M1-08 主要参考 Spring Integration MQTT 的消息发布模式，以及工业互联网场景中传感器批量上报数据的常见模拟方式。

本阶段只参考实现思路：

- 通过 HTTP 接口触发批量模拟
- 将模拟数据发布到 MQTT Topic
- 由后端 MQTT 订阅监听器统一消费消息
- 消息消费后进入业务入库和告警生成流程

本阶段没有复制外部项目代码，没有引入新的权限框架，没有引入 Kafka、WebSocket、Prometheus 或 Grafana。

M1-08 继续保持项目已有命名规范，继续使用 `MineMqttController`、`MineSensorMqttMessage`、`MineSensorMqttListener` 和 Topic `mine/sensor/data`。

