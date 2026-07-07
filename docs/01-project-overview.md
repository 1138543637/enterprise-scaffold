## S0-09：字典管理和文件上传

本阶段在公共企业级脚手架中新增字典管理和本地文件上传能力。

已完成能力：

- 字典类型分页查询
- 字典数据分页查询
- 根据 dictType 查询启用状态的字典数据
- 本地文件上传
- 文件元数据入库
- 文件分页查询
- 本地静态文件访问
- 文件上传接口接入 JWT 认证
- 字典和文件接口接入操作日志

本阶段继续复用已有系统基础表：

- sys_dict_type
- sys_dict_data
- sys_file

本阶段新增后端类：

- SysDictType
- SysDictData
- SysFile
- SysDictTypeMapper
- SysDictDataMapper
- SysFileMapper
- SysDictTypeService
- SysDictDataService
- SysFileService
- SysDictTypeServiceImpl
- SysDictDataServiceImpl
- SysFileServiceImpl
- SysDictTypeController
- SysDictDataController
- SysFileController

本阶段新增接口：

- GET /api/system/dict-types/page
- GET /api/system/dict-data/page
- GET /api/system/dict-data/type/{dictType}
- POST /api/system/files/upload
- GET /api/system/files/page

本阶段暂不实现：

- 字典新增
- 字典修改
- 字典删除
- MinIO 对象存储
- 复杂按钮级权限拦截

当前 S0 进度：

- S0-01 已完成：基础目录
- S0-02 已完成：后端健康检查
- S0-03 已完成：MySQL 与基础表
- S0-04 已完成：用户分页
- S0-05 已完成：JWT 登录认证
- S0-06 已完成：RBAC 基础查询
- S0-07 已完成：Vue3 前端初始化
- S0-08 已完成：登录日志和操作日志
- S0-09 已完成：字典管理和文件上传
- S0-10 未开始：Docker Compose 一键部署
## S0-10：Docker Compose 一键部署

### 阶段目标

S0-10 完成了 Enterprise Scaffold 的基础容器化部署能力，使用 Docker Compose 一键启动 MySQL、后端和前端服务。

### 本阶段新增能力

- 使用 Docker Compose 编排多容器服务
- 使用 MySQL 容器运行 `enterprise_scaffold` 数据库
- 使用后端 Dockerfile 构建 Spring Boot 3 后端镜像
- 使用前端 Dockerfile 构建 Vue3 前端镜像
- 使用 Nginx 托管前端静态资源
- 使用 Nginx 代理 `/api/**` 和 `/files/**` 到后端容器
- 使用 `.env` 管理本地环境变量
- 使用 Docker volume 持久化 MySQL 数据
- 使用 bind mount 持久化本地上传文件 `uploads`

### 容器名称

| 服务 | 容器名 | 端口 |
|---|---|---|
| MySQL | enterprise-scaffold-mysql | 3306 |
| 后端 | enterprise-scaffold-backend | 8080 |
| 前端 | enterprise-scaffold-frontend | 5173 |

### 不变的项目契约

S0-10 不改变已有业务代码：

- 后端包名继续使用 `cn.sxu.enterprise`
- 数据库名继续使用 `enterprise_scaffold`
- 接口路径继续使用 `/api/...`
- 文件访问前缀继续使用 `/files`
- 统一返回结构继续使用 `ApiResult`
- 分页返回结构继续使用 `PageResult`

## M1-01：新增智能矿山模块骨架

M1-01 是项目一“智能矿山安全生产与设备预测性维护平台”的起始步骤。

本阶段在已有公共企业级脚手架基础上，新增智能矿山业务模块包：`cn.sxu.enterprise.module.mine`。

新增后端目录：

- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/controller`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/entity`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/mapper`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/impl`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/dto`

新增控制器：`cn.sxu.enterprise.module.mine.controller.MineHealthController`。

新增接口：`GET /api/mine/health`。

该接口继续复用已有公共能力：

- 统一返回结构：`ApiResult`
- JWT 登录认证
- 操作日志注解：`@OperLog`
- 操作日志表：`sys_oper_log`

本阶段暂不实现：

- 不新增数据库表
- 不新增 SQL 文件
- 不新增前端页面
- 不引入 MQTT
- 不引入 EMQX
- 不做设备台账
- 不做传感器数据
- 不做告警规则
- 不做工单闭环

M1-01 的作用是验证智能矿山业务模块已经能被 Spring Boot 扫描，并且可以复用公共脚手架中的认证、统一返回和操作日志能力。
## M1-02：设备台账和传感器台账

本阶段新增设备台账与传感器台账能力，用于支撑智能矿山基础数据。

新增：
- mine_device 设备台账表
- mine_sensor 传感器台账表
- GET /api/mine/devices/page
- GET /api/mine/sensors/page

特点：
- JWT 认证
- ApiResult + PageResult
- @OperLog 操作日志
- 仅分页查询，不含增删改

## M1-03：模拟传感器数据

本阶段在智能矿山模块中新增传感器数据模拟能力，基于 M1-02 已完成的 mine_sensor 传感器台账，生成传感器实时模拟数据，并写入 mine_sensor_data 表。

本阶段实现了从“传感器台账”到“传感器数据采集记录”的基础链路，为后续 M1-04 告警规则和告警事件、M1-06 智能矿山看板、M1-07 MQTT 接入提供数据基础。

新增能力包括：

1. 新增 mine_sensor_data 传感器数据表。
2. 新增模拟传感器数据生成接口。
3. 新增传感器最新数据查询接口。
4. 新增传感器历史数据分页查询接口。
5. 继续使用 JWT 认证、ApiResult 统一返回、PageResult 统一分页结构。
6. 继续使用 @OperLog 记录操作日志。

## M1-04：告警规则和告警事件

本阶段在智能矿山模块中新增告警规则和告警事件能力。

M1-04 基于 M1-03 已完成的传感器模拟数据表 `mine_sensor_data`，新增告警规则表和告警事件表，实现从“传感器数据异常”到“业务告警事件”的转换。

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

本阶段新增后端能力：

```text
1. 支持告警规则分页查询
2. 支持告警事件分页查询
3. 支持根据传感器数据生成告警事件
4. 支持 rule_id + sensor_data_id 去重，避免同一条传感器数据重复生成同一规则的告警事件
5. 支持按传感器类型、告警等级、处理状态等条件查询告警事件
```

本阶段继续沿用：

```text
后端包名：cn.sxu.enterprise.module.mine
数据库名：enterprise_scaffold
统一返回结构：ApiResult
统一分页结构：PageResult
认证方式：JWT
请求头：Authorization: Bearer <token>
操作日志注解：@OperLog
ORM：MyBatis-Plus
分页：MyBatis-Plus Page
条件查询：LambdaQueryWrapper
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

## M1-05：工单闭环

本阶段完成智能矿山告警后的工单闭环能力。系统在 M1-04 已经具备告警规则和告警事件能力，本阶段继续基于 mine_alarm_event 扩展 mine_work_order 工单表，实现“告警事件 -> 生成工单 -> 处理工单 -> 关闭工单”的基础业务闭环。

本阶段新增工单表 mine_work_order，用于记录由告警事件生成的处置工单。新增工单分页查询接口 GET /api/mine/work-orders/page，新增告警事件转工单接口 POST /api/mine/work-orders/create-from-alarm，新增工单处理接口 POST /api/mine/work-orders/{id}/handle，新增工单关闭接口 POST /api/mine/work-orders/{id}/close。

本阶段实现一个告警事件只允许生成一个工单。创建工单后，关联告警事件的 handle_status 更新为 1；工单处理后，工单 order_status 更新为 2；工单关闭后，工单 order_status 更新为 3，同时关联告警事件的 handle_status 更新为 2。本阶段所有接口继续使用 JWT 认证、ApiResult 统一返回、PageResult 分页结构和 @OperLog 操作日志。

M1-05 暂不做前端页面，暂不接复杂流程引擎，暂不做多人审批，暂不接短信、邮件和站内信。

## M1-06：智能矿山看板

M1-06 完成智能矿山安全生产看板能力，在已有设备台账、传感器台账、传感器数据、告警规则、告警事件和工单闭环基础上，新增看板统计接口和前端可视化页面。

本阶段新增设备数量、传感器数量、采集数据数量、告警规则数量、告警事件数量、未处理告警数量、工单数量、待处理工单数量和已关闭工单数量统计。

本阶段新增告警级别分布、传感器类型分布、工单状态分布、最近告警事件和最近工单记录展示。

本阶段前端新增 ECharts 图表能力，用于展示智能矿山业务运行状态。

M1-06 不新增数据库表，不新增 SQL 文件，不接入 MQTT / EMQX。

## M1-07：智能矿山 MQTT 数据接入

M1-07 在智能矿山模块中新增 MQTT 数据接入能力，用于模拟真实矿山传感器通过物联网消息协议上报采集数据。

本阶段使用 EMQX 作为 MQTT Broker，后端通过 Spring Integration MQTT 订阅固定 Topic `mine/sensor/data`。

传感器上报消息采用 JSON 格式，后端接收到消息后，根据 `sensorCode` 查询已有传感器台账 `mine_sensor`，并将采集值写入传感器历史数据表 `mine_sensor_data`。

写入数据后，系统继续复用已有 `MineAlarmEventService.generate(...)` 告警生成逻辑，根据 `mine_alarm_rule` 判断是否需要生成 `mine_alarm_event`。

M1-07 不新增前端页面，看板继续复用 M1-06 已完成的 `/mine/dashboard` 页面。

MQTT 数据写入后，已有看板统计接口可以继续统计设备、传感器、告警事件、工单等数据。

本阶段完成后，智能矿山平台的数据来源从“接口手动模拟生成”进一步扩展为“MQTT 消息接入”，更接近工业互联网、智能矿山和物联网平台中的真实数据采集链路。

## M1-08：MQTT 数据模拟增强

M1-08 在 M1-07 已接入 MQTT / EMQX 的基础上，新增了智能矿山传感器数据的批量 MQTT 模拟发布能力。

本阶段新增接口 `POST /api/mine/mqtt/simulate-batch`，支持按传感器类型 `sensorType` 批量生成 MQTT 消息，支持通过 `count` 控制模拟条数，支持通过 `intervalMillis` 控制每条 MQTT 消息的发送间隔。

M1-08 不直接写入数据库，而是继续将消息发布到固定 Topic `mine/sensor/data`。

已有 `MineSensorMqttListener` 监听消息后继续写入 `mine_sensor_data`，并复用已有告警规则生成 `mine_alarm_event`。

本阶段不新增数据库表，不新增数据库字段，不新增 SQL 文件，不修改前端页面，不引入 WebSocket、Kafka、Prometheus 或 Grafana。


## M1-09：实时数据展示增强

M1-09 在 M1-06 智能矿山看板、M1-07 MQTT 接入、M1-08 MQTT 批量模拟发布的基础上，增强了前端实时数据展示能力。本阶段在 `/mine/dashboard` 页面新增最近传感器数据表格，用于展示 `mine_sensor_data` 最新 10 条采集数据；新增“刷新看板”手动刷新能力；新增“自动刷新”开关，自动刷新间隔固定为 5 秒；新增“批量模拟 MQTT 数据”按钮，前端调用 `POST /api/mine/mqtt/simulate-batch`，批量模拟成功后自动刷新看板数据。

本阶段页面卸载时会清除定时器，避免内存泄漏；同时使用 CSS Grid 优化统计卡片、图表和表格布局，避免在部分环境中 `el-row` / `el-col` 栅格失效导致页面一列铺满。

M1-09 不新增数据库表，不新增数据库字段，不新增 SQL 文件，不新增后端接口，不新增 Docker 服务，不引入 WebSocket。M1-09 让智能矿山看板能够更直观地展示 MQTT 上报后的数据变化，为后续设备健康评分和预测性维护提供展示基础。


## M1-10：设备健康评分与风险等级

M1-10 在已有智能矿山设备台账、传感器数据、告警事件和工单闭环基础上，新增设备健康评分与风险等级能力。

本阶段不新增数据库表，不新增 SQL 文件，不新增 Docker 服务。

设备健康评分通过实时计算得到，数据来源包括：

- `mine_device`
- `mine_sensor`
- `mine_sensor_data`
- `mine_alarm_event`
- `mine_work_order`

评分规则采用规则模型：

- 基础分 100 分
- 最近 24 小时每条告警扣 5 分，最多扣 50 分
- 存在未处理严重告警扣 20 分
- 存在未关闭工单扣 10 分
- 存在长时间未上报或异常传感器扣 10 分

风险等级：

- 90-100：健康
- 70-89：关注
- 50-69：风险
- 0-49：高危

本阶段新增前端页面 `/mine/device-health`，用于展示设备健康评分、风险等级、24 小时告警数量、未处理严重告警、未关闭工单和传感器离线情况。

## M1-11：预测性维护任务闭环

本阶段在智能矿山模块中新增预测性维护任务能力。系统基于 M1-10 设备健康评分和风险等级，针对关注、风险、高危设备生成 mine_maintenance_task 维护任务，并支持任务安排、任务处理、任务关闭的完整闭环。

本阶段新增能力：
1. 新增 mine_maintenance_task 预测性维护任务表。
2. 新增预测性维护任务分页查询接口。
3. 新增维护任务汇总统计接口。
4. 新增基于设备健康风险生成维护任务接口。
5. 新增维护任务安排、处理、关闭接口。
6. 新增前端预测性维护任务页面 /mine/maintenance-tasks。


## M1-12：维护看板与风险趋势分析

本阶段完成智能矿山预测性维护看板与风险趋势分析能力。

M1-12 基于 M1-11 已完成的 `mine_maintenance_task` 预测性维护任务数据，结合 `mine_alarm_event` 告警事件和 `mine_work_order` 工单数据，实现维护任务总览、任务状态统计、优先级统计、风险等级统计、最近 7 天风险趋势、高风险设备任务和最近维护任务展示。

本阶段新增前端页面：

- `/mine/maintenance-dashboard`

本阶段新增后端接口：

- `GET /api/mine/maintenance-dashboard/summary`
- `GET /api/mine/maintenance-dashboard/task-status-stats`
- `GET /api/mine/maintenance-dashboard/priority-stats`
- `GET /api/mine/maintenance-dashboard/risk-level-stats`
- `GET /api/mine/maintenance-dashboard/risk-trend`
- `GET /api/mine/maintenance-dashboard/recent-tasks`
- `GET /api/mine/maintenance-dashboard/high-risk-devices`

M1-12 不新增数据库表，不新增 SQL 文件，不新增 Docker 服务。

## M1-13：项目一总体验收与收尾

M1-13 完成了项目一“智能矿山安全生产与设备预测性维护平台”的总体验收与收尾。

本阶段不新增数据库表，不新增数据库字段，不新增 SQL 文件，不新增后端业务接口，不新增 Docker 服务。

本阶段重点是对项目一已有能力进行完整验收、入口收尾、文档总结和简历表达整理。

项目一当前已经形成完整业务链路：

设备台账 → 传感器台账 → MQTT 数据上报 → 传感器数据入库 → 告警事件生成 → 工单闭环 → 设备健康评分 → 预测性维护任务 → 维护看板与风险趋势分析。

M1-13 对以下前端页面进行总体验收：

- `/dashboard`：首页项目入口
- `/mine/dashboard`：智能矿山综合看板
- `/mine/device-health`：设备健康评分
- `/mine/maintenance-tasks`：预测性维护任务
- `/mine/maintenance-dashboard`：维护看板与风险趋势分析

M1-13 对以下核心能力进行总体验收：

- JWT 登录认证
- 智能矿山基础接口
- MQTT 模拟发布
- 批量 MQTT 模拟发布
- 传感器数据入库
- 告警事件生成
- 告警转工单
- 工单处理
- 工单关闭
- 设备健康评分
- 基于设备健康风险生成维护任务
- 维护任务安排
- 维护任务处理
- 维护任务关闭
- 维护看板统计
- 最近 7 天风险趋势分析

M1-13 建议在首页 `/dashboard` 增加项目一入口导航，方便演示智能矿山完整功能。

项目一完成后，可以作为面向山西能源企业、省属国企、运营商、工业互联网、政务数字化和企业信息化岗位的完整求职项目。




###############################################################################################################
## A2-01 新增 AIOps 智能运维模块骨架

A2-01 开始进入项目二“云网融合 AIOps 智能运维平台”的开发。本阶段新增后端业务包 `cn.sxu.enterprise.module.aiops`，用于承载后续资源台账、指标采集、告警中心、运维工单、根因分析、AIOps 综合看板以及 Prometheus / Grafana 接入等能力。本阶段只新增模块骨架和健康检查接口，不新增数据库表、不新增 SQL 文件、不新增前端页面、不新增 Docker 服务。

## A2-02：AIOps 资源管理

A2-02 完成了项目二“云网融合 AIOps 智能运维平台”的资源台账基础能力。本阶段新增 AIOps 资源表 `aiops_resource`，用于记录云主机、数据库、中间件、网络设备等基础运维资源信息。资源台账是 AIOps 后续指标采集、告警识别、运维工单、根因分析和综合看板的基础数据来源。

本阶段后端继续在固定业务包 `cn.sxu.enterprise.module.aiops` 下开发，新增 `AiopsResource`、`AiopsResourceMapper`、`AiopsResourceService`、`AiopsResourceServiceImpl`、`AiopsResourceController`、`AiopsResourcePageQuery` 和 `AiopsResourcePageVO`。新增接口 `GET /api/aiops/resources/page`，用于分页查询资源台账。该接口需要 JWT 认证，继续使用 `ApiResult` 统一返回结构和 `PageResult` 分页结构，并通过 `@OperLog(title = "AIOps资源管理", businessType = "分页查询")` 写入操作日志。

本阶段前端新增 `scaffold-frontend/src/api/aiops/resource.ts` 和 `scaffold-frontend/src/views/aiops/AiopsResourceView.vue`，新增路由 `/aiops/resources`。页面支持按资源编码、资源名称、资源类型、IP 地址、环境类型、所属系统、负责人、采集状态和资源状态进行筛选。前端接口路径固定使用 `/api/aiops/resources/page`，不能写成 `/aiops/resources/page`。页面查询条件和关键布局使用 CSS Grid，避免桌面端布局变成一列铺满。

A2-02 不新增 Docker 服务，不修改 `docker-compose.yml`，但由于新增了后端和前端代码，必须通过 Docker Compose 重新 build 后端和前端镜像完成验收。A2-02 完成后，AIOps 模块已经具备资源台账基础能力，下一步可以进入 A2-03：指标采集与模拟数据。

## A2-03：指标采集与模拟数据

A2-03 完成了云网融合 AIOps 智能运维平台的指标采集与模拟数据能力。本阶段基于 A2-02 已完成的 AIOps 资源台账 `aiops_resource`，新增指标数据表 `aiops_metric_data`，用于记录服务器、数据库、中间件、网络设备等资源的 CPU、内存、磁盘、网络、MySQL、Redis 等指标数据。

本阶段新增了指标模拟生成能力、最新指标查询能力和历史指标分页查询能力。指标数据会记录资源编码、资源名称、资源类型、IP 地址、指标编码、指标名称、指标类型、指标值、指标单位、阈值、是否告警、采集时间等信息，为后续 A2-04 告警规则、告警事件、运维工单闭环，以及 A2-06 AIOps 综合看板提供数据基础。

A2-03 继续保持 A2 固定命名规则：后端包名使用 `cn.sxu.enterprise.module.aiops`，接口路径使用 `/api/aiops/**`，数据库表使用 `aiops_` 前缀，前端路由使用 `/aiops/**`。本阶段不新增 Docker 服务，但新增了后端和前端代码，因此必须进行 Docker Compose 重建镜像验收。

## A2-04：告警规则、告警事件、运维工单闭环

A2-04 是项目二“云网融合 AIOps 智能运维平台”的告警中心与运维处置闭环阶段。本阶段基于 A2-02 已完成的 `aiops_resource` 资源台账，以及 A2-03 已完成的 `aiops_metric_data` 指标采集与模拟数据，新增 `aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order` 三张业务表，实现从指标异常识别、告警规则匹配、告警事件生成、告警转运维工单、工单处理到工单关闭的完整基础闭环。

本阶段继续沿用 A2 固定技术路线：后端包名使用 `cn.sxu.enterprise.module.aiops`，接口路径使用 `/api/aiops/**`，数据库表使用 `aiops_` 前缀，前端路由使用 `/aiops/**`。所有接口继续接入 JWT 认证，返回结构继续使用 `ApiResult`，分页接口继续使用 `PageResult`，Controller 方法继续使用 `@OperLog` 写入操作日志。

A2-04 的业务链路为：`aiops_metric_data` 指标数据经过 `aiops_alert_rule` 告警规则匹配后生成 `aiops_alert_event` 告警事件，再由告警事件生成 `aiops_work_order` 运维工单，最后通过工单处理和工单关闭形成运维处置闭环。该阶段让 AIOps 项目从“资源和指标展示”进入“异常发现、告警派发、工单闭环”的核心业务阶段。

A2-04 不新增 Docker 服务，不修改 Docker Compose 配置，但新增了数据库表、后端代码和前端页面，因此需要执行 SQL 升级、后端编译、前端构建和 Docker Compose 重新 build 验收。

## A2-05：根因分析简化版

A2-05 完成了云网融合 AIOps 智能运维平台的根因分析简化版能力。

本阶段基于 A2-04 已完成的告警规则、告警事件、指标数据、资源台账和运维工单闭环，
新增根因分析记录表，用于保存每次根因分析生成的疑似根因、分析证据、处理建议、置信度和分析时间。

A2-05 不引入复杂 AI 模型，也不引入机器学习算法，
而是采用规则化、可解释、适合工程落地的分析方式。

系统会根据某个告警事件，向前回看一段时间内的指标异常、同资源告警、同系统告警和未关闭工单，
生成 CPU 过高、内存过高、磁盘空间不足、网络异常、数据库慢或未知类型的根因判断。

本阶段新增后端接口 POST /api/aiops/root-causes/analyze、
GET /api/aiops/root-causes/page、
GET /api/aiops/root-causes/{id}，
新增前端页面 /aiops/root-causes。

A2-05 使 AIOps 项目从“发现告警、生成工单”进一步扩展到
“解释告警原因、沉淀分析记录、辅助运维处置”的能力，
为后续 A2-06 AIOps 综合看板提供根因分析数据基础。

### A2-06：AIOps 综合看板

A2-06 完成了云网融合 AIOps 智能运维平台的综合看板能力。

本阶段基于已经完成的 AIOps 资源台账、指标数据、告警规则、告警事件、运维工单和根因分析记录，新增综合统计接口和前端看板页面，用于统一展示资源总数、异常资源、指标数据总数、异常指标数量、告警规则数量、告警事件数量、未处理告警数量、运维工单数量、待处理工单数量、根因分析记录数量和高置信根因分析数量。

后端新增 `AiopsDashboardController`、`AiopsDashboardService`、`AiopsDashboardServiceImpl`，并新增 `AiopsDashboardSummaryVO`、`AiopsResourceTypeStatVO`、`AiopsAlertLevelStatVO`、`AiopsWorkOrderStatusStatVO`、`AiopsMetricTrendVO`、`AiopsRecentAlertVO`、`AiopsRecentWorkOrderVO`、`AiopsRecentRootCauseVO` 等返回对象。

前端新增 `scaffold-frontend/src/api/aiops/dashboard.ts` 和 `scaffold-frontend/src/views/aiops/AiopsDashboardView.vue`，新增访问路由 `/aiops/dashboard`。

页面使用 Vue3、TypeScript、Element Plus、ECharts 和 CSS Grid 展示统计卡片、最近 7 天指标趋势图、资源类型分布图、告警级别分布图、工单状态分布图、最近告警事件表格、最近运维工单表格和最近根因分析表格。

A2-06 不新增数据库表、不新增数据库字段、不新增 SQL 文件、不新增 Docker 服务，继续复用 `aiops_resource`、`aiops_metric_data`、`aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order`、`aiops_root_cause_record`。

本阶段完成后，AIOps 模块形成了从资源管理、指标采集、告警事件、运维工单、根因分析到综合看板的完整展示链路，为后续 A2-07 Prometheus / Grafana 接入提供可视化基础。



## A2-07：Prometheus / Grafana 接入

A2-07 完成了云网融合 AIOps 智能运维平台的监控组件接入能力。

本阶段在后端引入 Spring Boot Actuator 和 Micrometer Prometheus Registry，通过 `/actuator/health` 提供服务健康检查，通过 `/actuator/prometheus` 提供 Prometheus 可抓取的指标数据。

Docker Compose 中新增 Prometheus 和 Grafana 服务。Prometheus 用于抓取 `enterprise-scaffold-backend` 后端服务指标，Grafana 用于后续可视化展示监控数据。

本阶段新增容器名固定为 `enterprise-scaffold-prometheus` 和 `enterprise-scaffold-grafana`。Prometheus 端口固定为 `9090`，Grafana 端口固定为 `3000`。

A2-07 不新增业务数据库表，不新增业务 Controller、Service、Mapper，也不新增前端页面。本阶段重点是让 AIOps 项目具备真实监控体系接入能力，为 A2-08 项目二总体验收提供监控能力基础。


## A2-08：A2 项目总体验收与收尾

A2-08 完成项目二“云网融合 AIOps 智能运维平台”的总体验收与收尾。本阶段不新增数据库表、不新增 SQL 文件、不新增后端业务接口、不新增 Docker 服务，重点是把 AIOps 项目整理成可以演示、可以部署、可以写入简历、可以在面试中讲清楚的完整项目。

A2 项目完整业务链路为：资源台账 -> 指标采集 -> 指标异常识别 -> 告警事件生成 -> 告警转运维工单 -> 工单处理 -> 工单关闭 -> 根因分析 -> AIOps 综合看板 -> Spring Boot Actuator 指标暴露 -> Prometheus 指标抓取 -> Grafana 指标查询与可视化基础。

本阶段修改首页 `/dashboard`，首页展示顺序固定为：公共脚手架概览、项目一智能矿山入口、项目一验收链路、项目二 AIOps 入口、A2 项目验收链路。这样可以先展示已经完整完成的项目一，再展示当前收尾的项目二，符合项目推进顺序，也方便面试演示。A2 页面入口包括 `/aiops/dashboard`、`/aiops/resources`、`/aiops/metrics`、`/aiops/alerts`、`/aiops/work-orders`、`/aiops/root-causes`，监控组件入口包括 Prometheus `http://localhost:9090` 和 Grafana `http://localhost:3000`。




















############################################################################################


## R3-01：银行实时交易风控与反欺诈平台模块骨架

R3-01 新增项目三“银行实时交易风控与反欺诈平台”的后端模块骨架。该模块继续基于 enterprise-scaffold 公共脚手架扩展，不新建仓库，不改变已有 M1 智能矿山模块和 A2 AIOps 模块。后端包名固定为 cn.sxu.enterprise.module.risk，接口路径固定使用 /api/risk/**，数据库表前缀固定使用 risk_，前端路由前缀固定使用 /risk/**。本阶段新增 RiskHealthController 和 GET /api/risk/health 健康检查接口，用于验证银行风控模块已经被 Spring Boot 正常扫描，并继续复用 JWT 认证、ApiResult 统一返回结构和 @OperLog 操作日志。本阶段不新增 SQL 文件，不新增数据库表，不新增 Docker 服务，不修改 Docker 配置。


### R3-02：银行交易模拟

R3-02 在项目三“银行实时交易风控与反欺诈平台”中新增交易流水模拟能力。本阶段继续沿用公共脚手架，不新建系统，不改变已有 M1 和 A2 模块。后端固定使用 `cn.sxu.enterprise.module.risk` 包，接口固定使用 `/api/risk/**`，数据库表固定使用 `risk_` 前缀，前端路由固定使用 `/risk/**`。

本阶段新增 `risk_transaction` 交易流水表，用于保存模拟出来的银行交易数据。交易流水会作为后续 R3-03 风控规则、规则命中记录、风险评分、人工审核和 Kafka 实时交易接入的基础数据。新增接口包括 `POST /api/risk/transactions/simulate`、`GET /api/risk/transactions/latest`、`GET /api/risk/transactions/page`。新增前端页面为 `/risk/transactions`，用于模拟交易、查看最新交易和分页查询交易流水。

