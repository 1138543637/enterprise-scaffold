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

