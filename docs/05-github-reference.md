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


## R3-04：GitHub 参考项目

R3-04 可以参考 GitHub 上 Java 风控、反欺诈、风险评分和审核流相关项目的分层思想。例如 `yasinkabboura/FraudDetectionBased` 可参考 Kafka 交易流和欺诈识别的整体链路，GitHub `fraud-detection` Java 主题可参考规则评分、告警分诊和管理后台的常见组织方式，Redis 的 Transaction Risk Scoring 示例可参考交易风险评分的概念和阈值思想。只能参考业务拆分、交易风险评分、审核流、列表页面和看板思路，不能照搬它们的包名、表名、字段名、接口路径、模型依赖或前端结构。本项目继续固定使用 `cn.sxu.enterprise.module.risk`、`/api/risk/**`、`risk_` 表前缀、`ApiResult`、`PageResult` 和 JWT。


## R3-05：Kafka 接入 GitHub 参考说明

R3-05 参考 Spring Kafka 的 `KafkaTemplate`、`@KafkaListener`、`ConcurrentKafkaListenerContainerFactory`、`KafkaAdmin` 和 Topic 创建方式。

R3-05 参考 Apache Kafka 的 Producer、Consumer、Topic、Broker 和 KRaft 单节点运行模式。

R3-05 参考 RuoYi-Vue 和 RuoYi-Vue-Pro 的企业后台 Controller / Service / Mapper 分层、统一返回结构和操作日志设计思想。

本项目没有直接复制外部项目业务代码，而是在已有 `cn.sxu.enterprise.module.risk` 模块中新增 `RiskKafkaProperties`、`RiskKafkaConfig`、`RiskTransactionKafkaMessage`、`RiskKafkaBatchSimulateRequest`、`RiskTransactionKafkaProducer`、`RiskTransactionKafkaListener` 和 `RiskKafkaController`。

本阶段继续保持 `/api/risk/**` 接口前缀、`risk_` 数据库表前缀、`ApiResult`、`PageResult`、JWT 认证和 `@OperLog` 操作日志契约不变。

Docker Compose 层面参考 Kafka 官方 Docker 镜像运行方式，最终保持项目固定容器命名 `enterprise-scaffold-kafka`、固定端口 `9092` 和固定 volume `enterprise-scaffold-kafka-data`，避免引入额外的独立项目或破坏已有 M1、A2、R3 模块结构。


## R3-06：风控看板 GitHub 参考说明

R3-06 主要参考企业后台项目中的看板统计接口、分层结构和前端图表展示方式。

可参考项目：

- RuoYi-Vue-Pro：参考模块化 Controller / Service / Mapper 分层、后台统计接口组织方式。
- RuoYi-Vue：参考操作日志、统一返回结构、后台管理页面组织方式。
- RuoYi-Vue3：参考 Vue3 + Element Plus 页面结构。
- Vue Vben Admin：参考中后台看板布局和统计卡片设计。
- MyBatis-Plus：参考 QueryWrapper、LambdaQueryWrapper、selectMaps 分组统计写法。

本项目自己的实现固定为：

- 后端包名：`cn.sxu.enterprise.module.risk`
- 接口路径：`/api/risk/dashboard/**`
- 前端 API：`scaffold-frontend/src/api/risk/dashboard.ts`
- 前端页面：`scaffold-frontend/src/views/risk/RiskDashboardView.vue`
- 前端路由：`/risk/dashboard`

不能复制若依代码，不能更换项目包名，不能把 `/api/risk/dashboard/**` 改成其他路径。

## R3-07：项目三总体验收 GitHub 参考说明

R3-07 继续参考以下开源项目的工程组织思想，但不复制其代码：

- RuoYi-Vue：参考企业后台 Controller / Service / Mapper 分层、操作日志、登录认证和后台管理页面组织方式。
- RuoYi-Vue-Pro：参考多业务模块拆分、统一返回结构、权限体系和后台业务模块扩展方式。
- RuoYi-Vue3：参考 Vue3 + Vite + Element Plus 后台页面组织方式。
- Vue Vben Admin：参考中后台首页入口、卡片布局和模块导航设计思路。
- MyBatis-Plus：参考 BaseMapper、Page、LambdaQueryWrapper、QueryWrapper 和 selectMaps 分组统计方式。
- Kafka 官方示例：参考 Topic、Producer、Consumer、Listener 和 Docker Compose 场景下的消息链路设计思路。

本项目自己的实现固定为 `cn.sxu.enterprise.module.risk`、`/api/risk/**`、`risk_` 表前缀、`/risk/**` 前端路由。R3-07 只做项目三收尾、首页入口、验收链路和文档总结，不复制任何开源项目业务代码。

************************************************************************************************************


## D4-01：数据治理模块骨架参考说明

D4-01 参考 `RuoYi-Vue`、`RuoYi-Vue-Pro` 和 `MyBatis-Plus` 的模块化分层思想。

本阶段只参考 Controller、Service、Mapper、Entity 分层方式和操作日志注解思想。

不要复制若依源码，不要引入若依代码生成器，不要更换权限框架。

本项目自己的实现固定为 `cn.sxu.enterprise.module.datahub`、`DatahubHealthController` 和 `GET /api/datahub/health`。

D4 是国企 / 政务数据治理与共享交换平台，不能改成普通报表系统、BI 系统或数据分析系统。


## D4-02：GitHub 参考说明

D4-02 参考 RuoYi-Vue、RuoYi-Vue-Pro、MyBatis-Plus 和 Vue Vben Admin 的后台管理分层思想、分页查询组织方式、操作日志思想和前端列表页面结构。本项目只参考这些项目的架构思想，不复制源码，不引入若依代码生成器，不改变当前项目的认证、返回结构、包名、接口路径和 Docker 命名。D4-02 的自有实现固定为 cn.sxu.enterprise.module.datahub、datahub_datasource、DatahubDatasourceController、GET /api/datahub/datasources/page、POST /api/datahub/datasources/test-connection、scaffold-frontend/src/api/datahub/datasource.ts 和 scaffold-frontend/src/views/datahub/DatahubDatasourceView.vue。


# 05 GitHub Reference

## 本项目参考方向

Enterprise Scaffold 是一个工程级企业后台脚手架项目，当前阶段重点是 Datahub 数据治理模块。D4-03 元数据采集阶段可参考以下类型的开源项目设计思路，但不能直接照搬项目结构。

当前项目已经形成固定技术路线，不重新选择技术栈，不替换框架，不引入过重的外部平台。

## 可参考的项目类型

### 1. 后台管理系统脚手架

可参考内容：

```text
用户、角色、菜单、权限、字典、日志、统一返回、分页封装、前后端分离工程结构
```

参考意义：

```text
用于理解企业后台的基础结构，而不是替换当前项目。
```

当前项目已经有自己的工程结构，因此只参考设计思想，不迁移代码。

### 2. 数据源管理系统

可参考内容：

```text
数据源类型
数据源连接信息
数据源连接测试
数据源状态
数据源列表
数据源详情
```

D4-02 已经实现数据源管理，D4-03 继续基于已有 `datahub_datasource` 表进行元数据采集。

当前项目真实字段为：

```text
datasource_name
datasource_type
jdbc_url
host
port
database_name
username
password
status
deleted
```

参考项目中如果使用 `db_type`、`database_type`、`data_source_name`，不能直接照搬到当前项目。

### 3. 元数据管理系统

可参考内容：

```text
数据库表采集
字段采集
字段类型识别
字段注释读取
主键识别
采集批次
采集日志
```

D4-03 当前已经实现 MySQL 元数据采集，后续可以继续参考开源数据目录或元数据平台的设计方式，但当前阶段不引入复杂血缘引擎、不引入 Elasticsearch、不引入 Kafka、不引入 Atlas/DataHub 等大型平台。

### 4. 数据治理平台

可参考内容：

```text
元数据目录
数据质量规则
数据标准
数据血缘
数据资产检索
数据权限
```

当前项目后续可以逐步扩展，但 D4-03 只完成“连接已有数据源并采集表字段元数据”。

## GitHub 参考关键词

搜索 GitHub 时可使用以下关键词：

```text
spring boot vue admin scaffold
spring boot vue3 element plus admin
spring boot metadata management
spring boot datasource management
database metadata jdbc
mysql database metadata
data catalog spring boot
data governance platform
```

## 可借鉴但不照搬的设计点

### 数据源连接测试

后续可增加：

```text
POST /api/datahub/datasource/test
```

用于在保存或采集前测试数据源连接是否可用。

### 元数据定时采集

后续可增加定时任务：

```text
每天凌晨自动采集
手动触发采集
保留历史采集批次
对比字段变化
```

### 采集日志增强

当前已有：

```text
collect_batch_no
collect_status
table_total
column_total
error_msg
start_time
end_time
cost_time
```

后续可增加：

```text
trigger_type
operator_id
operator_name
source_ip
diff_status
```

### 元数据变更对比

后续可基于 `collect_batch_no` 对比不同批次：

```text
新增表
删除表
新增字段
删除字段
字段类型变化
字段注释变化
```

## 当前项目不能照搬的点

### 不能照搬其他项目的数据源表名

其他项目可能使用：

```text
data_source
datahub_data_source
datasource
sys_datasource
```

但当前项目真实表名是：

```text
datahub_datasource
```

### 不能照搬其他项目的数据源类型字段

其他项目可能使用：

```text
db_type
database_type
type
source_type
```

但当前项目真实字段是：

```text
datasource_type
```

### 不能照搬 localhost 配置

很多本地项目示例会写：

```text
jdbc:mysql://localhost:3306/xxx
```

但当前项目运行在 Docker 中，后端容器访问 MySQL 必须使用：

```text
enterprise-scaffold-mysql
```

### 不能引入过重组件

当前阶段不引入：

```text
Apache Atlas
LinkedIn DataHub
OpenMetadata
Elasticsearch
Kafka
Flink
Airflow
```

这些平台可以作为产品设计参考，但不能在当前阶段直接加入工程。

## 当前阶段推荐参考方式

当前阶段最适合参考的是 JDBC 原生元数据能力：

```java
DatabaseMetaData metaData = connection.getMetaData();
metaData.getTables(...);
metaData.getColumns(...);
metaData.getPrimaryKeys(...);
```

这与当前 D4-03 的轻量化目标一致。

## 当前 D4-03 已沉淀规则

1. 以当前项目真实表结构为准。
2. 以当前 Docker Compose 容器名为准。
3. 以现有 `datahub_` 前缀为准。
4. 以当前 `ApiResult` 和 `PageResult` 返回结构为准。
5. 参考项目只看设计，不直接复制表名、字段名和接口路径。
6. 当前阶段只做 MySQL 元数据采集，不扩展多数据库。
7. 遇到报错先查 Network，再查 Docker 后端日志，再查数据库真实字段。


## D4-04 GitHub 参考项目

D4-04 数据质量检测可参考 Apache Griffin、AWS Deequ 和 Great Expectations 的设计思想。Apache Griffin 可参考其“质量规则、执行、报告”的模型驱动数据质量平台思路；AWS Deequ 可参考其“约束定义、指标计算、质量验证”的规则检测思路；Great Expectations 可参考其“期望规则、验证结果、可读报告”的数据质量验证思路。以上项目只能参考思想，不能照搬代码，因为当前项目固定使用 Java 17、Spring Boot 3、MyBatis-Plus、MySQL、Vue3、Element Plus 和 `/api/datahub/**` 接口路径；当前项目也必须继续使用 `datahub_datasource`、`datahub_metadata_table`、`datahub_metadata_column`、`datahub_quality_rule`、`datahub_quality_result` 等固定表名，不能改成外部项目中的数据模型。

## D4-05 GitHub 参考项目说明

D4-05 的敏感数据识别和脱敏设计可以参考 Apache Atlas、DataHub、Amundsen、Apache Ranger、Microsoft Presidio 等项目的思想。可以参考它们对“元数据、敏感标签、数据分类、脱敏策略、审计记录”的建模方式，但不能照搬其源码、包名、表名和复杂架构。本项目保持求职项目可控范围，只基于 `datahub_metadata_column` 做字段级敏感识别，基于 `datahub_mask_rule` 做规则化脱敏预览，继续保持 Spring Boot 3 + MyBatis-Plus + MySQL + Vue3 + Element Plus 的轻量实现方式。

## D4-06 GitHub 参考项目说明

D4-06 可以参考 LinkedIn DataHub、Apache Atlas、Amundsen、Apache Ranger、Kong、Apache APISIX、Spring Boot 企业后台项目和 RuoYi 系列项目的设计思想。DataHub、Atlas、Amundsen 主要用于参考“数据目录、元数据、数据资产看板”的建模方式；Ranger 主要用于参考“数据安全与策略治理”的思想；Kong 和 APISIX 主要用于理解“API 发布、API 上线下线、API 调用网关”的业务概念；RuoYi 系列项目只参考 Controller、Service、Mapper、Entity、VO、DTO 的分层组织方式和后台管理页面布局。不能照搬这些项目的源码、包名、表名、权限框架、网关架构或代码生成器。本项目自己的实现继续保持 `cn.sxu.enterprise.module.datahub` 包名、`datahub_` 表前缀、`/api/datahub/**` 接口路径、`/datahub/**` 前端路由、`ApiResult` 和 `PageResult` 统一返回结构。


## D4-07：项目四收尾阶段 GitHub 参考

D4-07 主要参考方向是数据治理平台、元数据管理平台、数据目录和治理看板的收尾表达。可以参考 Apache Atlas、DataHub、Amundsen 等开源项目的数据资产、元数据、分类、搜索、治理看板和文档组织思路；也可以参考 qData 这类整合元数据、数据质量、数据资产和 API 服务的数据治理平台。注意只能参考项目分层、治理链路、文档表达和看板入口设计，不能照搬对方代码、包名、数据库字段、接口路径和 UI。当前项目必须继续保持 `cn.sxu.enterprise.module.datahub` 包名、`/api/datahub/**` 接口路径、`datahub_` 表前缀、`/datahub/**` 前端路由和 `ApiResult / PageResult` 返回结构。D4-07 推荐提交命令：`git add .`、`git commit -m "feat: finish datahub project acceptance wrap-up"`、`git push`。

******************************************************************************************************

## I5-01：GitHub 参考项目说明

I5-01 可以继续参考 RuoYi-Vue、RuoYi-Vue-Pro、MyBatis-Plus 官方示例和 Spring Boot REST Controller 示例中的模块分层方式、Controller 写法、统一返回结构思想和操作日志注解思想。只能参考目录组织、Controller 分层、统一返回和注解式日志的设计思路，不能照搬若依代码，不能引入若依代码生成器，不能更换现有权限框架，不能重写 `/api/auth/login`，不能重写 JWT，也不能修改 `sys_user`、`sys_role`、`sys_menu`。本项目自己的实现固定为 `cn.sxu.enterprise.module.iam`、`IamHealthController`、`GET /api/iam/health`、`ApiResult<String>`、JWT 认证和 `@OperLog` 操作日志。


## I5-02：接口访问日志参考与提交记录

I5-02 可以参考 RuoYi-Vue / RuoYi-Vue3 中操作日志、登录日志和权限审计相关模块的分层思路，也可以参考 MyBatis-Plus 官方示例中的 `BaseMapper`、`Page` 和 `LambdaQueryWrapper` 分页查询写法。参考时只学习分层结构、日志字段设计、分页查询和前端列表页组织方式，不能照搬包名、表名、接口路径和菜单体系。本项目必须继续使用 `cn.sxu.enterprise.module.iam`、`/api/iam/**`、`iam_` 表前缀、`ApiResult`、`PageResult` 和现有 Docker Compose 契约。推荐提交命令：`git add .`，`git commit -m "feat: implement iam access log"`，`git push`。


### I5-03：异常登录检测参考说明

I5-03 可参考 RuoYi-Vue / RuoYi-Vue3 的登录日志、操作日志、安全审计和风险记录模块分层思路，也可以参考常见后台管理系统中的登录失败记录、异常 IP 识别、短时间多次失败检测等实现方式。参考时只学习 Entity、Mapper、Service、ServiceImpl、Controller、Query、VO、前端 API、前端页面的分层方式，不能照搬包名、表名、接口路径、菜单体系或返回结构。本项目固定使用 `cn.sxu.enterprise.module.iam`、`/api/iam/**`、`iam_` 表前缀、`ApiResult`、`PageResult`、`@OperLog`、JWT 和现有 Docker Compose 契约。推荐提交命令为 `git add .`、`git commit -m "feat: implement iam login risk detection"`、`git push`。

### I5-04
I5-04 可参考 RuoYi-Vue / RuoYi-Vue3 的系统日志、登录日志、操作日志、安全审计和后台管理模块分层思路，也可以参考 Spring Boot 限流示例项目中的限流规则、限流窗口、最大请求次数、Redis 或内存计数等概念。参考时只学习 Entity、Mapper、Service、ServiceImpl、Controller、Query、VO、前端 API、前端页面的分层方式和限流概念，不能照搬包名、表名、接口路径、菜单体系或返回结构。本项目固定使用 cn.sxu.enterprise.module.iam、/api/iam/**、iam_ 表前缀、ApiResult、PageResult、@OperLog、JWT 和现有 Docker Compose 契约。本阶段不做真实网关级限流，不新增 Redis 依赖，不新增 Docker 服务。

### I5-05:
I5-05 可参考 RuoYi-Vue / RuoYi-Vue3 的参数配置、系统日志、登录日志、操作日志、安全审计和后台管理模块分层思路，也可以参考 Sa-Token 等 Java 权限认证项目中的认证、授权、会话、安全策略设计思想。参考时只学习 Entity、Mapper、Service、ServiceImpl、Controller、Query、VO、前端 API、前端页面的分层方式和安全策略抽象方式，不能照搬包名、表名、接口路径、菜单体系、返回结构或权限框架。本项目固定使用 cn.sxu.enterprise.module.iam、/api/iam/**、iam_ 表前缀、ApiResult、PageResult、@OperLog、JWT 和现有 Docker Compose 契约。本阶段不新增权限框架，不引入 Sa-Token，不新增 Redis，不新增 Docker 服务。


## I5-06：GitHub 参考项目与提交记录

I5-06 可参考 RuoYi / RuoYi-Vue / RuoYi-Vue3 的用户管理、角色管理、菜单管理、操作日志、登录日志和后台管理分层思路，也可以参考 Sa-Token 等 Java 权限认证项目中的认证、授权、会话和安全策略设计思想。参考时只学习 Entity、Mapper、Service、ServiceImpl、Controller、Query、VO、前端 API、前端页面的分层方式和权限审计抽象方式，不能照搬包名、表名、接口路径、菜单体系、返回结构或权限框架。本项目固定使用 `cn.sxu.enterprise.module.iam`、`/api/iam/**`、`iam_` 表前缀、`ApiResult`、`PageResult`、`@OperLog`、JWT 和现有 Docker Compose 契约。本阶段不引入 Sa-Token，不新增 Redis，不新增 Docker 服务。推荐提交命令为 `git add .`、`git commit -m "feat: implement iam permission audit"`、`git push`。







