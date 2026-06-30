\# Enterprise Scaffold



面向央国企信息化场景的企业级后台脚手架。



\## 项目定位



本项目用于支撑智能矿山、AIOps 智能运维、银行实时交易风控、数据治理与共享交换、统一身份认证与安全审计等业务模块开发。



\## 求职方向



\- 山西能源企业信息化岗

\- 山西省属国企软件开发岗

\- 三大运营商云网运维与开发岗

\- 国有大行金融科技岗

\- 政务数字化与数据治理相关岗位



\## 技术栈规划



\- Java 17

\- Spring Boot 3

\- MyBatis-Plus

\- MySQL

\- Redis

\- Vue3

\- Element Plus

\- Docker Compose



\## 模块规划



\- system：系统管理模块

\- mine：智能矿山安全生产与设备预测性维护模块

\- aiops：云网融合 AIOps 智能运维模块

\- risk：银行实时交易风控与反欺诈模块

\- datahub：国企/政务数据治理与共享交换模块

\- iam：统一身份认证、权限审计与数据安全模块

## Docker Compose 本地一键部署

进入部署目录：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-docker
```

## 当前进度

已完成第 0 阶段公共企业级脚手架，包括后端基础接口、MySQL、MyBatis-Plus、JWT 登录认证、RBAC 基础查询、操作日志、字典管理、文件上传、Vue3 前端登录页和 Docker Compose 一键部署。

当前已进入项目一“智能矿山安全生产与设备预测性维护平台”的开发阶段。

### M1-01：智能矿山模块骨架

已新增智能矿山业务模块：`cn.sxu.enterprise.module.mine`。

已新增模块健康检查接口：`GET /api/mine/health`。

该接口需要 JWT 认证，并复用统一返回结构 `ApiResult` 和操作日志注解 `@OperLog`。

## M1-02

新增：
mine_device
mine_sensor

接口：
GET /api/mine/devices/page
GET /api/mine/sensors/page

特点：
JWT认证 + 分页查询 + 操作日志

## 当前进度：M1-03 模拟传感器数据

项目一“智能矿山安全生产与设备预测性维护平台”已完成传感器模拟数据能力。

本阶段新增：

- mine_sensor_data 传感器数据表
- MineSensorData 实体
- MineSensorDataMapper
- MineSensorDataService
- MineSensorDataServiceImpl
- MineSensorDataController
- MineSensorDataSimulateRequest
- MineSensorDataPageQuery
- MineSensorDataVO

新增接口：

- POST /api/mine/sensor-data/simulate
- GET /api/mine/sensor-data/latest
- GET /api/mine/sensor-data/page

接口说明：

- 模拟数据生成接口基于 mine_sensor 中的正常传感器生成数据。
- 最新数据接口按传感器返回最新一条采集数据。
- 历史数据接口支持分页查询。
- 所有接口均需要 JWT 认证。
- 所有接口继续使用 ApiResult 统一返回结构。
- 分页接口继续使用 PageResult。
- 接口继续使用 @OperLog 记录操作日志。

M1-03 为后续 M1-04 告警规则和告警事件提供数据基础。


## M1-04：智能矿山告警规则和告警事件

当前项目一“智能矿山安全生产与设备预测性维护平台”已完成告警规则和告警事件基础能力。

本阶段基于 M1-03 的传感器模拟数据能力，新增告警规则表和告警事件表，实现从传感器数据到告警事件的转换。

本阶段新增数据库表：

```text
mine_alarm_rule
mine_alarm_event
```

本阶段新增接口：

```text
GET  /api/mine/alarm-rules/page
GET  /api/mine/alarm-events/page
POST /api/mine/alarm-events/generate
```

本阶段实现能力：

```text
1. 支持配置不同传感器类型的告警规则
2. 支持告警规则分页查询
3. 支持根据传感器数据生成告警事件
4. 支持告警事件分页查询
5. 支持按传感器类型、告警等级、处理状态等条件查询告警事件
6. 支持 rule_id + sensor_data_id 去重，避免重复生成告警事件
7. 接口需要 JWT 认证
8. 接口使用 ApiResult 统一返回
9. 分页接口使用 PageResult
10. 接口使用 @OperLog 记录操作日志
```

本阶段新增后端类：

```text
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
```

本阶段暂不实现：

```text
告警确认
告警关闭
告警转工单
前端页面
MQTT / EMQX 接入
短信 / 邮件 / 站内信通知
```

M1-04 为后续 M1-05 工单闭环提供告警事件基础。


## M1-05：智能矿山工单闭环

当前项目一“智能矿山安全生产与设备预测性维护平台”已完成工单闭环基础能力。

本阶段新增 mine_work_order 工单表，新增 GET /api/mine/work-orders/page、POST /api/mine/work-orders/create-from-alarm、POST /api/mine/work-orders/{id}/handle、POST /api/mine/work-orders/{id}/close 接口。

M1-05 实现告警事件转工单、工单分页查询、工单处理、工单关闭、工单状态流转、告警事件 handle_status 联动更新、操作日志审计和 JWT 认证保护。

本阶段完成后，智能矿山模块已经形成“传感器数据 -> 告警事件 -> 工单处理 -> 工单关闭”的基础业务闭环。

## M1-06：智能矿山看板

当前项目一“智能矿山安全生产与设备预测性维护平台”已完成智能矿山看板能力。

已新增后端看板统计接口：

- GET /api/mine/dashboard/summary
- GET /api/mine/dashboard/alarm-level-stats
- GET /api/mine/dashboard/sensor-type-stats
- GET /api/mine/dashboard/work-order-status-stats
- GET /api/mine/dashboard/recent-alarms
- GET /api/mine/dashboard/recent-work-orders

已新增前端页面：

- /mine/dashboard

已新增前端能力：

- ECharts 图表
- 智能矿山统计卡片
- 告警级别分布图
- 传感器类型分布图
- 工单状态分布图
- 最近告警事件表格
- 最近工单记录表格

M1-06 不新增数据库表，不新增 Docker 服务，不接入 MQTT / EMQX。

## M1-07：MQTT 数据接入能力

当前项目已支持智能矿山传感器 MQTT 数据接入。

M1-07 使用 EMQX 作为 MQTT Broker，后端通过 Spring Integration MQTT 订阅 `mine/sensor/data` Topic。

传感器上报 JSON 消息后，系统会根据 `sensorCode` 查询传感器台账，并将采集数据写入 `mine_sensor_data`。

如果采集值超过告警阈值，系统会复用已有告警规则生成 `mine_alarm_event`，并继续在智能矿山看板中展示统计结果。

本阶段新增能力包括：

```text
EMQX Docker Compose 服务
MQTT Topic 订阅
传感器 MQTT 消息 DTO
MQTT 消息写入传感器历史数据
MQTT 数据触发告警事件生成
MQTT 模拟发布接口
```

测试接口为：

```text
POST /api/mine/mqtt/simulate-publish
```

测试 Topic 为：

```text
mine/sensor/data
```

EMQX Dashboard 地址为：

```text
http://localhost:18083
```

## M1-08：MQTT 数据模拟增强

当前项目一“智能矿山安全生产与设备预测性维护平台”已完成 MQTT 数据模拟增强能力。

本阶段新增接口 `POST /api/mine/mqtt/simulate-batch`。

该接口支持按传感器类型 `sensorType` 批量模拟 MQTT 数据，支持通过 `count` 控制模拟条数，支持通过 `intervalMillis` 控制发送间隔。

批量模拟数据继续发布到 Topic `mine/sensor/data`。

后端继续由 `MineSensorMqttListener` 消费消息，继续写入 `mine_sensor_data`，继续触发 `mine_alarm_event` 告警事件生成。

M1-08 不新增数据库表，不新增数据库字段，不新增 SQL 文件，不新增 Docker 服务，不修改前端页面。


## M1-09：实时数据展示增强

当前项目一“智能矿山安全生产与设备预测性维护平台”已完成实时数据展示增强能力。

本阶段在 `/mine/dashboard` 页面新增最近传感器数据展示、手动刷新看板、自动刷新开关、5 秒定时刷新、批量模拟 MQTT 数据按钮、CSS Grid 响应式布局优化。

本阶段复用接口：`GET /api/mine/sensor-data/page`、`POST /api/mine/mqtt/simulate-batch`、`GET /api/mine/dashboard/summary`、`GET /api/mine/dashboard/alarm-level-stats`、`GET /api/mine/dashboard/sensor-type-stats`、`GET /api/mine/dashboard/work-order-status-stats`、`GET /api/mine/dashboard/recent-alarms`、`GET /api/mine/dashboard/recent-work-orders`。

本阶段不新增数据库表，不新增 SQL 文件，不新增 Docker 服务，不引入 WebSocket。M1-09 让智能矿山看板能够实时体现 MQTT 上报后的数据变化，为后续 M1-10 设备健康评分与风险等级提供展示基础。

## M1-10：设备健康评分与风险等级

当前项目一“智能矿山安全生产与设备预测性维护平台”已完成设备健康评分与风险等级能力。

本阶段新增：

```text
GET /api/mine/device-health/page
GET /api/mine/device-health/summary
/mine/device-health
```

本阶段新增后端类：

```text
MineDeviceHealthController
MineDeviceHealthService
MineDeviceHealthServiceImpl
MineDeviceHealthPageQuery
MineDeviceHealthVO
MineDeviceHealthSummaryVO
```

本阶段新增前端文件：

```text
scaffold-frontend/src/api/mine/deviceHealth.ts
scaffold-frontend/src/views/mine/MineDeviceHealthView.vue
```

本阶段不新增数据库表，不新增 SQL 文件，不新增 Docker 服务。

设备健康评分复用已有：

```text
mine_device
mine_sensor
mine_sensor_data
mine_alarm_event
mine_work_order
```

M1-10 让项目从“监控告警系统”进一步增强为“预测性维护平台”的基础版本。


## M1-11：预测性维护任务闭环

当前智能矿山模块已支持预测性维护任务闭环：
1. 基于设备健康评分生成维护任务。
2. 支持维护任务分页查询和汇总统计。
3. 支持维护任务安排、处理、关闭。
4. 前端新增 /mine/maintenance-tasks 页面。

新增接口：
GET /api/mine/maintenance-tasks/page
GET /api/mine/maintenance-tasks/summary
POST /api/mine/maintenance-tasks/create-from-device-health
POST /api/mine/maintenance-tasks/{id}/plan
POST /api/mine/maintenance-tasks/{id}/handle
POST /api/mine/maintenance-tasks/{id}/close


README.md 增加：

```md
## M1-12：维护看板与风险趋势分析

当前项目一“智能矿山安全生产与设备预测性维护平台”已完成维护看板与风险趋势分析能力。

已新增后端接口：

- `GET /api/mine/maintenance-dashboard/summary`
- `GET /api/mine/maintenance-dashboard/task-status-stats`
- `GET /api/mine/maintenance-dashboard/priority-stats`
- `GET /api/mine/maintenance-dashboard/risk-level-stats`
- `GET /api/mine/maintenance-dashboard/risk-trend`
- `GET /api/mine/maintenance-dashboard/recent-tasks`
- `GET /api/mine/maintenance-dashboard/high-risk-devices`

已新增前端页面：

- `/mine/maintenance-dashboard`

本阶段基于 `mine_maintenance_task`、`mine_alarm_event`、`mine_work_order` 实现维护任务总览、任务状态统计、优先级统计、风险等级统计、最近 7 天风险趋势、高风险设备任务和最近维护任务展示。

M1-12 不新增数据库表，不新增 SQL 文件，不新增 Docker 服务。
```


## 项目一：智能矿山安全生产与设备预测性维护平台

当前项目一已经完成总体验收与收尾。

项目一完整业务链路为：

设备台账 → 传感器台账 → MQTT 数据上报 → 传感器数据入库 → 告警事件生成 → 工单闭环 → 设备健康评分 → 预测性维护任务 → 维护看板与风险趋势分析。

### 已完成能力

- 智能矿山业务模块 `cn.sxu.enterprise.module.mine`
- 设备台账 `mine_device`
- 传感器台账 `mine_sensor`
- 传感器数据 `mine_sensor_data`
- 告警规则 `mine_alarm_rule`
- 告警事件 `mine_alarm_event`
- 工单闭环 `mine_work_order`
- 预测性维护任务 `mine_maintenance_task`
- MQTT 数据接入
- EMQX Docker Compose 服务
- 智能矿山综合看板 `/mine/dashboard`
- 设备健康评分页面 `/mine/device-health`
- 预测性维护任务页面 `/mine/maintenance-tasks`
- 维护看板与风险趋势页面 `/mine/maintenance-dashboard`

### Docker Compose 服务

- `enterprise-scaffold-mysql`
- `enterprise-scaffold-backend`
- `enterprise-scaffold-frontend`
- `enterprise-scaffold-emqx`

### 项目一适合展示的能力

- Java 后端开发
- 企业信息化开发
- 智能矿山信息化
- 工业互联网数据接入
- 告警规则和工单闭环
- 设备健康评分
- 预测性维护
- Vue3 可视化看板
- Docker Compose 一键部署

### 项目一总体验收命令

后端编译：

执行目录：`scaffold-backend`

执行命令：`mvn -DskipTests compile`

前端构建：

执行目录：`scaffold-frontend`

执行命令：`pnpm build`

Docker Compose 验收：

执行目录：`scaffold-docker`

执行命令：`docker compose --env-file .env up -d --build`

查看容器：

`docker compose ps`

### 项目一验收页面

- `http://localhost:5173/dashboard`
- `http://localhost:5173/mine/dashboard`
- `http://localhost:5173/mine/device-health`
- `http://localhost:5173/mine/maintenance-tasks`
- `http://localhost:5173/mine/maintenance-dashboard`








###################################################################################################
## A2-01 云网融合 AIOps 智能运维平台启动

项目一“智能矿山安全生产与设备预测性维护平台”已完成。当前开始项目二“云网融合 AIOps 智能运维平台”。

A2-01 已新增后端业务模块：

- `cn.sxu.enterprise.module.aiops`

新增健康检查接口：

- `GET /api/aiops/health`

该接口需要 JWT 认证，继续使用 `ApiResult` 统一返回结构，并通过 `@OperLog` 记录操作日志。本阶段不新增数据库表、不新增 SQL 文件、不新增前端页面、不新增 Docker 服务。


## A2-02：AIOps 资源管理

当前项目二“云网融合 AIOps 智能运维平台”已完成资源管理基础能力。已新增 `aiops_resource` 资源台账表，已新增 `GET /api/aiops/resources/page` 资源分页查询接口，已新增前端页面 `/aiops/resources`。资源类型覆盖 `SERVER`、`DATABASE`、`MIDDLEWARE`、`NETWORK`，环境类型覆盖 `DEV`、`TEST`、`PROD`。接口需要 JWT 认证，继续使用 `ApiResult + PageResult` 返回结构，并通过 `@OperLog` 写入操作日志。

A2-02 继续保持 A2 固定开发规则：后端包名使用 `cn.sxu.enterprise.module.aiops`，接口路径使用 `/api/aiops/**`，数据库表使用 `aiops_` 前缀，前端路由使用 `/aiops/**`。本阶段不新增 Docker 服务，但新增了后端和前端代码，因此需要通过 Docker Compose 重新 build 后端和前端镜像完成验收。A2-02 为后续 A2-03 指标采集与模拟数据提供资源台账基础。

## A2-03：AIOps 指标采集与模拟数据

当前项目二“云网融合 AIOps 智能运维平台”已完成指标采集与模拟数据能力。

本阶段新增 `aiops_metric_data` 指标数据表，新增 `POST /api/aiops/metric-data/simulate`、`GET /api/aiops/metric-data/latest`、`GET /api/aiops/metric-data/page` 三个接口，支持基于 AIOps 资源台账模拟生成 CPU、内存、磁盘、网络、MySQL、Redis 等指标数据。

本阶段新增前端页面 `/aiops/metrics`，用于展示最新指标数据、指标历史数据，并支持模拟生成指标数据。接口继续需要 JWT 认证，继续使用 `ApiResult` 统一返回，分页接口继续使用 `PageResult`，Controller 方法继续使用 `@OperLog` 记录操作日志。

A2-03 为后续 A2-04 告警规则、告警事件、运维工单闭环，以及 A2-06 AIOps 综合看板提供指标数据基础。

## A2-04：AIOps 告警规则、告警事件、运维工单闭环

当前项目二“云网融合 AIOps 智能运维平台”已完成告警规则、告警事件和运维工单闭环能力。

本阶段新增 `aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order` 三张数据库表，基于 A2-03 已完成的 `aiops_metric_data` 指标数据，实现从指标异常识别、告警规则匹配、告警事件生成、告警转运维工单、工单处理到工单关闭的完整基础闭环。

本阶段新增后端接口：

- `GET /api/aiops/alert-rules/page`
- `GET /api/aiops/alert-events/page`
- `POST /api/aiops/alert-events/generate`
- `GET /api/aiops/work-orders/page`
- `POST /api/aiops/work-orders/create-from-alert`
- `POST /api/aiops/work-orders/{id}/handle`
- `POST /api/aiops/work-orders/{id}/close`

本阶段新增前端页面：

- `/aiops/alerts`
- `/aiops/work-orders`

所有 A2-04 接口继续需要 JWT 认证，继续使用 `ApiResult` 统一返回结构，分页接口继续使用 `PageResult`，Controller 方法继续使用 `@OperLog` 记录操作日志。

A2-04 不新增 Docker 服务，不修改 Docker Compose 配置，但新增了后端代码、前端代码和数据库表，因此需要执行 SQL 升级、后端编译、前端构建和 Docker Compose 重建镜像验收。

本阶段完成后，AIOps 智能运维平台已经具备以下链路：

`资源台账 -> 指标采集与模拟数据 -> 告警规则 -> 告警事件 -> 运维工单 -> 工单处理 -> 工单关闭`

