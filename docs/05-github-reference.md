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

## M1-09：实时数据展示增强参考说明

M1-09 主要参考企业后台看板和运维监控平台中的实时刷新展示方式。本阶段只参考实现思路，不复制外部项目代码。

本阶段参考点包括：看板页面通过按钮手动刷新数据；看板页面通过定时器自动刷新数据；页面卸载时清理定时器；复用已有后端接口，避免额外增加后端复杂度；使用 CSS Grid 保证统计卡片、图表和表格在不同屏幕下稳定布局。

本项目自己的实现是修改 `scaffold-frontend/src/api/mine/dashboard.ts` 和 `scaffold-frontend/src/views/mine/MineDashboardView.vue`，复用 `GET /api/mine/sensor-data/page`、`POST /api/mine/mqtt/simulate-batch` 以及已有 `/api/mine/dashboard/**` 看板接口。M1-09 暂不引入 WebSocket，避免在当前阶段增加后端连接管理和消息推送复杂度。后续如果需要更强实时性，可以再扩展 WebSocket 或 SSE。

## M1-10：设备健康评分参考说明

M1-10 主要参考企业设备运维系统、AIOps 平台和工业互联网平台中的健康评分设计思路。

本阶段只参考思路，不复制外部项目代码。

参考点：

- 设备健康评分通常由多项运行指标综合计算
- 风险等级用于辅助运维人员快速识别重点设备
- 告警、工单、传感器上报时间可以作为设备健康状态的重要输入
- 评分规则先采用可解释的规则模型，便于面试讲解和后续扩展

本项目自己的实现是：

- `MineDeviceHealthController`
- `MineDeviceHealthService`
- `MineDeviceHealthServiceImpl`
- `MineDeviceHealthPageQuery`
- `MineDeviceHealthVO`
- `MineDeviceHealthSummaryVO`
- `GET /api/mine/device-health/page`
- `GET /api/mine/device-health/summary`
- 前端页面 `/mine/device-health`

本阶段不引入机器学习，不引入新的权限框架，不引入新的数据库表。



## M1-11 GitHub 参考

推荐 commit：
feat: implement mine predictive maintenance tasks

本次提交应包含：
1. scaffold-sql/m1_11_mine_maintenance_task.sql
2. scaffold-sql/enterprise_scaffold_init.sql 追加 mine_maintenance_task 建表语句
3. MineMaintenanceTask Entity / Mapper / Service / ServiceImpl / Controller
4. MineMaintenanceTaskCreateRequest / PlanRequest / HandleRequest / CloseRequest
5. MineMaintenanceTaskPageQuery / PageVO / SummaryVO
6. scaffold-frontend/src/api/mine/maintenanceTask.ts
7. scaffold-frontend/src/views/mine/MineMaintenanceTaskView.vue
8. scaffold-frontend/src/router/index.ts 新增 /mine/maintenance-tasks 路由
9. docs 和 README 更新


docs/05-github-reference.md 增加：

```md
## M1-12：维护看板与风险趋势分析参考说明

M1-12 继续参考以下项目：

- RuoYi-Vue-Pro：参考企业后台模块划分、统计接口组织方式
- RuoYi-Vue：参考 Controller / Service / Mapper 分层和操作日志思想
- RuoYi-Vue3：参考 Vue3 + Element Plus 后台页面写法
- Vue Vben Admin：参考中后台看板布局和卡片设计
- MyBatis-Plus：参考 LambdaQueryWrapper、QueryWrapper、selectMaps 分组统计
- ECharts：参考折线图、饼图展示方式

本项目自己的实现为：

- `MineMaintenanceDashboardController`
- `MineMaintenanceDashboardService`
- `MineMaintenanceDashboardServiceImpl`
- `/api/mine/maintenance-dashboard/**`
- `/mine/maintenance-dashboard`

本阶段不复制第三方项目代码，只参考结构、命名习惯和页面组织方式。
```


## M1-13：GitHub 参考说明

M1-13 主要参考企业级后台项目的收尾方式、首页入口组织方式、README 总结方式和文档验收方式。

参考项目包括：

- RuoYi-Vue
- RuoYi-Vue3
- Vue Vben Admin
- vue-element-admin

参考内容包括：

- 后台首页如何放置业务模块入口。
- README 如何按功能模块总结项目能力。
- 文档如何补充部署说明。
- 文档如何补充接口说明。
- 文档如何补充数据库说明。
- 文档如何补充技术栈总结。
- 如何把业务链路整理成可演示、可答辩、可写简历的表达。

本项目自己的实现继续保持：

- 业务包名：`cn.sxu.enterprise.module.mine`
- 接口路径：`/api/mine/**`
- 设备台账表：`mine_device`
- 传感器台账表：`mine_sensor`
- 传感器数据表：`mine_sensor_data`
- 告警规则表：`mine_alarm_rule`
- 告警事件表：`mine_alarm_event`
- 工单表：`mine_work_order`
- 预测性维护任务表：`mine_maintenance_task`
- 统一返回结构：`ApiResult`
- 统一分页结构：`PageResult`
- 认证方式：JWT
- 操作日志：`@OperLog`
- 部署方式：Docker Compose
- MySQL 容器名：`enterprise-scaffold-mysql`
- 后端容器名：`enterprise-scaffold-backend`
- 前端容器名：`enterprise-scaffold-frontend`
- EMQX 容器名：`enterprise-scaffold-emqx`

M1-13 不复制参考项目代码，不引入新的权限框架，不改变已有项目结构，不改变数据库名 `enterprise_scaffold`，不改变 Docker 容器名、端口、环境变量、volume 和上传目录挂载规则。

M1-13 的重点是把项目一整理成完整可展示项目，用于面向能源企业、省属国企、运营商、工业互联网、政务数字化和企业信息化岗位的求职展示。









#############################################################################################################################################################
## A2-01 GitHub 参考说明

A2-01 参考 RuoYi-Vue、RuoYi-Vue-Pro 和 MyBatis-Plus 的模块化分层思想。可以参考其 Controller、Service、Mapper、Entity 的目录组织方式，以及统一返回结构和操作日志设计思想。本项目自己的实现固定为 `cn.sxu.enterprise.module.aiops`、`AiopsHealthController` 和 `GET /api/aiops/health`。不能复制若依源码，不能引入若依代码生成器，不能改变当前项目已固定的技术路线和命名契约。


## A2-02：AIOps 资源管理参考说明

A2-02 参考 RuoYi-Vue、RuoYi-Vue-Pro、RuoYi-Vue3 和 MyBatis-Plus 的分层组织方式、分页查询接口设计、后台列表页结构、操作日志注解思想和 MyBatis-Plus 分页查询写法。可以参考这些项目中 Controller、Service、Mapper、Entity、VO 的组织方式，也可以参考后台管理系统中列表查询、查询条件、分页组件、状态标签的页面设计方式。

本项目不能直接复制若依代码，不能引入若依代码生成器，不能改成若依的包名和目录结构，不能改变本项目固定的 `cn.sxu.enterprise.module.aiops` 包名，不能改变 `/api/aiops/**` 接口前缀，不能改变 `aiops_` 数据库表前缀，不能改变 `ApiResult` 和 `PageResult` 返回结构，也不能引入新的权限框架。本项目继续复用已有 JWT、Spring Security、MyBatis-Plus、@OperLog、ApiResult、PageResult、Vue3、Element Plus、Axios 和 Docker Compose 基础能力。

A2-02 自己实现的内容是 `aiops_resource`、`AiopsResource`、`AiopsResourceMapper`、`AiopsResourceService`、`AiopsResourceServiceImpl`、`AiopsResourceController`、`AiopsResourcePageQuery`、`AiopsResourcePageVO`、`GET /api/aiops/resources/page`、`scaffold-frontend/src/api/aiops/resource.ts`、`scaffold-frontend/src/views/aiops/AiopsResourceView.vue` 和前端路由 `/aiops/resources`。本阶段的参考重点是企业后台资源台账建模、分页查询分层、前后端列表联调和 Docker Compose 验收流程。

## A2-03：指标采集与模拟数据参考说明

A2-03 参考 RuoYi-Vue、RuoYi-Vue-Pro、RuoYi-Vue3、Vue Vben Admin 和 MyBatis-Plus 的设计思想。

参考内容包括企业后台 Controller / Service / Mapper / Entity 分层方式，分页查询接口组织方式，操作日志注解思想，MyBatis-Plus `BaseMapper`、`Page`、`LambdaQueryWrapper` 条件查询写法，以及 Vue3 + Element Plus 的后台列表页面和筛选表单布局。

本项目自己的实现是：后端包名 `cn.sxu.enterprise.module.aiops`，数据库表 `aiops_metric_data`，后端类 `AiopsMetricData`、`AiopsMetricDataMapper`、`AiopsMetricDataService`、`AiopsMetricDataServiceImpl`、`AiopsMetricDataController`，接口 `POST /api/aiops/metric-data/simulate`、`GET /api/aiops/metric-data/latest`、`GET /api/aiops/metric-data/page`，前端页面 `/aiops/metrics`。

本阶段不能直接复制若依代码，不能引入若依代码生成器，不能改变已有 `ApiResult`、`PageResult`、JWT、`@OperLog` 和 Docker Compose 契约。

## A2-04：GitHub 参考说明

A2-04 继续参考 RuoYi-Vue、RuoYi-Vue-Pro、RuoYi-Vue3、Vue Vben Admin 和 MyBatis-Plus 的工程组织方式。参考重点包括企业后台项目中的 Controller / Service / Mapper / Entity 分层方式、分页查询接口设计、操作日志注解思想、MyBatis-Plus BaseMapper、Page、LambdaQueryWrapper 用法，以及前端中后台页面的查询表单、表格、分页和操作按钮布局。

A2-04 可以参考 RuoYi 系列项目的模块分层思想、操作日志思想、分页查询习惯和前后端接口组织方式，但不能直接复制若依代码，不能引入若依代码生成器，不能把包名改成若依包名，不能把接口路径改成若依系统模块路径。本项目 A2 模块必须继续保持 `cn.sxu.enterprise.module.aiops` 包名、`/api/aiops/**` 接口路径、`aiops_` 数据库表前缀和 `/aiops/**` 前端路由前缀。

A2-04 还复用了项目一 M1 阶段已经验证过的“规则表 + 事件表 + 工单表”建模思想，以及告警事件和工单状态联动、`@Transactional` 事务控制、`ApiResult` 统一返回、`PageResult` 分页返回、`@OperLog` 操作日志审计等工程经验。但 AIOps 模块不能直接复用 `mine_alarm_event` 或 `mine_work_order`，因为这些表具有智能矿山业务语义。A2-04 必须使用独立的 `aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order`，保证 AIOps 模块边界清晰。

MyBatis-Plus 参考重点是 `BaseMapper`、`Page`、`LambdaQueryWrapper` 和条件查询写法。前端参考重点是 Vue3、TypeScript、Element Plus 表格页面和 CSS Grid 布局。新增前端 API 文件时继续使用 `unwrapApiResult` 或 `request.get<any, T>()` 防止 `AxiosResponse<T>` 和 `T` 类型不匹配。

## A2-05：根因分析简化版参考说明

A2-05 继续参考 RuoYi-Vue、RuoYi-Vue-Pro、RuoYi-Vue3、Vue Vben Admin 和 MyBatis-Plus。

参考内容包括 Controller / Service / Mapper / Entity 分层方式、
分页查询接口组织方式、操作日志注解思想、Vue3 + Element Plus 后台列表页写法、
MyBatis-Plus BaseMapper、Page、LambdaQueryWrapper 条件查询方式。

本项目自己的实现固定为 cn.sxu.enterprise.module.aiops 包下的
AiopsRootCauseRecord、AiopsRootCauseRecordMapper、AiopsRootCauseService、
AiopsRootCauseServiceImpl、AiopsRootCauseController、AiopsRootCauseAnalyzeRequest、
AiopsRootCausePageQuery、AiopsRootCauseVO，
以及 /api/aiops/root-causes/** 接口和 /aiops/root-causes 前端页面。

A2-05 只能参考开源项目的工程组织和后台管理思想，不能复制若依源码，
不能引入若依代码生成器，不能引入新的权限框架。

不能改变已有 aiops_resource、aiops_metric_data、aiops_alert_rule、
aiops_alert_event、aiops_work_order 表和接口命名。

也不能把根因分析接口改成 /api/aiops/root-cause/** 或 /api/aiops/rca/**。

本阶段重点是用自己的代码实现可解释的简化根因分析能力，
并保持与当前 enterprise-scaffold 工程命名契约一致。

### A2-06：AIOps 综合看板 GitHub 参考说明

A2-06 参考 RuoYi-Vue-Pro、RuoYi-Vue、RuoYi-Vue3、Vue Vben Admin、MyBatis-Plus 和 ECharts 的设计思想。

RuoYi 系列项目主要参考后台管理系统的 Controller、Service、Mapper 分层方式、操作日志设计思想和后台首页统计接口组织方式。

MyBatis-Plus 主要参考 `BaseMapper`、`LambdaQueryWrapper`、`QueryWrapper`、`selectMaps` 的聚合统计写法。

Vue3 中后台项目主要参考看板页面的统计卡片、图表区域和表格区域布局方式。

ECharts 主要参考折线图和饼图的初始化、数据更新、窗口 resize 和组件销毁方式。

本项目自己的实现固定为后端包名 `cn.sxu.enterprise.module.aiops`，接口路径 `/api/aiops/dashboard/**`，前端路由 `/aiops/dashboard`。

数据库继续复用 `aiops_resource`、`aiops_metric_data`、`aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order`、`aiops_root_cause_record`。

返回结构继续使用 `ApiResult`。

前端 API 文件继续使用 `unwrapApiResult` 兼容 `AxiosResponse<ApiResult<T>>`、`ApiResult<T>` 和 `T` 三种返回情况。

前端统计卡片、图表区和表格区继续使用 CSS Grid。

A2-06 不能直接复制开源项目代码，不能引入新的权限框架，不能替换现有 JWT、`ApiResult`、`PageResult`，不能重命名已有 A2 表、类和接口，不能把 `/api/aiops/dashboard/**` 改成 `/api/ops/**` 或其他路径。

## A2-07：Prometheus / Grafana 接入参考说明

A2-07 参考 Spring Boot Actuator、Micrometer、Prometheus 和 Grafana 的官方实践，同时继续保持本项目自己的工程结构和命名契约。

本阶段可以参考 Spring Boot 官方文档中 Actuator 和 Prometheus 指标暴露方式，参考 Prometheus 官方 `scrape_configs` 和 `static_configs` 配置方式，参考 Grafana 官方 Docker 镜像、数据源配置和环境变量配置方式。

也可以参考 RuoYi-Vue、RuoYi-Vue-Pro 等企业后台项目中对监控、日志、权限和模块化的组织思想。

本项目不能直接复制其他项目的完整 `docker-compose.yml`，不能修改 `enterprise-scaffold-backend`、`enterprise-scaffold-frontend`、`enterprise-scaffold-mysql`、`enterprise-scaffold-emqx` 等已有容器名，不能改变 `/api/aiops/**`、`/api/mine/**`、`aiops_*`、`mine_*` 等已固定命名。

本阶段只接入轻量级 Prometheus / Grafana 监控链路，不引入 Kubernetes、复杂告警规则、云服务器部署和生产级权限配置。


## A2-08：GitHub 参考说明

A2-08 主要参考 RuoYi-Vue、RuoYi-Vue3、RuoYi-Vue-Pro 和 Vue Vben Admin 的后台首页入口组织思路，用于在 `/dashboard` 中增加项目演示入口。本项目只参考模块入口、后台导航、管理系统首页组织方式，不复制开源项目代码，不引入新的后台模板，不改变现有 Vue3、Vite、TypeScript、Element Plus 技术路线。

A2-08 继续保持本项目自己的实现：公共脚手架为 `enterprise-scaffold`，AIOps 后端包名为 `cn.sxu.enterprise.module.aiops`，接口路径为 `/api/aiops/**`，前端路由为 `/aiops/**`，监控组件使用 Prometheus 和 Grafana 原生页面验收。Prometheus 页面固定为 `http://localhost:9090`，Grafana 页面固定为 `http://localhost:3000`。本阶段的 GitHub 提交建议为 `feat: finish aiops project acceptance wrap-up`，用于表示 A2 项目完成总体验收、首页入口、文档总结和部署验收收尾。



















#######################################################################################################



## R3-01 GitHub 参考说明

R3-01 参考 RuoYi-Vue-Pro、RuoYi-Vue、RuoYi-Vue3、vue-element-admin、Vue Vben Admin 和 MyBatis-Plus 的企业后台组织方式，但只参考模块拆分、Controller 命名、统一返回、操作日志和后台项目结构，不复制业务代码，不复制完整权限系统，不复制代码生成器，不把其他项目改名后作为本项目内容。R3-01 的实际代码必须沿用 enterprise-scaffold 已有 MineHealthController、AiopsHealthController、ApiResult 和 @OperLog 的写法，保持包名 cn.sxu.enterprise.module.risk、接口路径 /api/risk/** 和返回结构一致。



### R3-02：GitHub 参考说明

R3-02 继续参考 RuoYi-Vue、RuoYi-Vue-Pro、RuoYi-Vue3、vue-element-admin、Vue Vben Admin 和 MyBatis-Plus。参考点仅限企业后台目录组织方式、Controller / Service / Mapper / Entity 分层方式、分页查询接口组织方式、操作日志思想、Vue3 + Element Plus 页面组织方式和 MyBatis-Plus 的 `BaseMapper`、`Page`、`LambdaQueryWrapper` 用法。

本项目自己的实现固定为 `cn.sxu.enterprise.module.risk`、`risk_transaction`、`RiskTransaction*` 类、`/api/risk/transactions/**` 接口和 `/risk/transactions` 前端页面。不能复制若依代码，不能引入若依代码生成器，不能改变已有 JWT、ApiResult、PageResult、@OperLog、Docker Compose 和上传目录挂载规则。


## R3-03：规则引擎 GitHub 参考项目

R3-03 可以参考 GitHub 上的 Java 风控和规则引擎项目，但只能参考分层思路、规则抽象方式、规则匹配流程和页面组织，不能照搬包名、表名、接口路径、业务命名和返回结构。

可参考 GitHub Topics：`fraud-detection` Java 项目列表，用于了解反欺诈项目常见的交易、规则、告警、评分和看板组织方式。

可参考 GitHub Topics：`risk-analysis` 项目列表，其中 `wfh45678/radar` 是实时风控引擎项目，可参考“规则引擎服务独立于业务入口”的思路，但不要引入它的脚本规则系统。

可参考 `j-easy/easy-rules` 或相关 Easy Rules fork，理解轻量规则引擎通常由“规则条件 + 规则动作 + 规则执行器”组成。本项目 R3-03 不引入 Easy Rules 依赖，只用普通 Java 代码实现第一版规则匹配。

可参考 `gitshishirkarki/spring-SpEL-rule-engine` 的规则表达式思路，但 R3-03 不使用 SpEL 动态表达式，避免新手阶段引入额外复杂度。

本项目必须继续保持固定命名：后端包名 `cn.sxu.enterprise.module.risk`，接口路径 `/api/risk/**`，表名前缀 `risk_`，前端路由 `/risk/**`，返回结构 `ApiResult` 和 `PageResult`。


