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

