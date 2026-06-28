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