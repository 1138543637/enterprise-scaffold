## S0-09：字典管理和文件上传相关表

S0-09 复用 S0-03 已创建的 3 张表：

- sys_dict_type：字典类型表
- sys_dict_data：字典数据表
- sys_file：文件表

### sys_dict_type：字典类型表

| 字段 | Java 字段 | 说明 |
|---|---|---|
| id | id | 字典类型ID，主键，自增 |
| dict_name | dictName | 字典名称 |
| dict_type | dictType | 字典类型编码，唯一 |
| status | status | 状态：0正常 1停用 |
| create_by | createBy | 创建者 |
| create_time | createTime | 创建时间 |
| update_by | updateBy | 更新者 |
| update_time | updateTime | 更新时间 |
| deleted | deleted | 逻辑删除：0未删除 1已删除 |
| remark | remark | 备注 |

重要约束：

- uk_dict_type(dict_type)

### sys_dict_data：字典数据表

| 字段 | Java 字段 | 说明 |
|---|---|---|
| id | id | 字典数据ID，主键，自增 |
| dict_type | dictType | 字典类型编码 |
| dict_label | dictLabel | 字典标签 |
| dict_value | dictValue | 字典值 |
| dict_sort | dictSort | 字典排序 |
| css_class | cssClass | 样式属性 |
| list_class | listClass | 回显样式 |
| is_default | isDefault | 是否默认：Y是 N否 |
| status | status | 状态：0正常 1停用 |
| create_by | createBy | 创建者 |
| create_time | createTime | 创建时间 |
| update_by | updateBy | 更新者 |
| update_time | updateTime | 更新时间 |
| deleted | deleted | 逻辑删除：0未删除 1已删除 |
| remark | remark | 备注 |

重要索引：

- idx_dict_type(dict_type)
- idx_status(status)

### sys_file：文件表

| 字段 | Java 字段 | 说明 |
|---|---|---|
| id | id | 文件ID，主键，自增 |
| file_name | fileName | 存储后的文件名 |
| original_name | originalName | 上传时的原始文件名 |
| file_type | fileType | 文件扩展名 |
| file_size | fileSize | 文件大小，单位字节 |
| storage_type | storageType | 存储类型，S0-09 固定为 LOCAL |
| bucket_name | bucketName | 对象存储桶名称，S0-09 暂为空 |
| object_name | objectName | 文件对象路径 |
| url | url | 文件访问地址 |
| md5 | md5 | 文件 MD5 |
| upload_user_id | uploadUserId | 上传用户ID |
| status | status | 状态：0正常 1停用 |
| create_by | createBy | 创建者 |
| create_time | createTime | 创建时间 |
| update_by | updateBy | 更新者 |
| update_time | updateTime | 更新时间 |
| deleted | deleted | 逻辑删除：0未删除 1已删除 |
| remark | remark | 备注 |

重要索引：

- idx_upload_user_id(upload_user_id)
- idx_md5(md5)
- idx_status(status)

### S0-09 文件上传入库规则

文件上传成功后：

- storage_type 固定为 LOCAL
- status 固定为 0
- deleted 固定为 0
- upload_user_id 从当前 JWT 登录用户中获取
- create_by 从当前 JWT 登录用户名中获取
- object_name 使用日期目录加 UUID 文件名
- url 使用 /files 前缀

## S0-10：Docker Compose 数据库部署说明

### 数据库部署方式

S0-10 将 MySQL 从本机手动运行扩展为 Docker Compose 容器化运行。

数据库名继续固定为：utf8mb4

排序规则继续固定为：utf8mb4_unicode_ci
```sql
enterprise_scaffold
```

## M1-01：智能矿山模块数据库说明

M1-01 不新增数据库表，不新增 SQL 文件，不修改已有表结构。

原因：M1-01 只新增 `module/mine` 模块骨架和 `MineHealthController`，`GET /api/mine/health` 只返回模块运行状态，不需要查询业务表。

本阶段复用已有系统表：`sys_oper_log`。

复用原因：`GET /api/mine/health` 使用 `@OperLog` 记录操作日志，访问该接口后，操作记录会写入 `sys_oper_log`。

操作日志字段预期：

- `title = 智能矿山`
- `business_type = 模块健康检查`
- `request_method = GET`
- `oper_url = /api/mine/health`
- `status = 0`

本阶段暂不创建以下业务表：

- `mine_device`
- `mine_sensor`
- `mine_sensor_data`
- `mine_alarm_rule`
- `mine_alarm_event`
- `mine_work_order`

这些表将在后续 M1 阶段逐步设计：

- M1-02：设备台账和传感器台账
- M1-03：模拟传感器数据
- M1-04：告警规则和告警事件
- M1-05：工单闭环

## M1-02：mine_device 与 mine_sensor

新增两表：

mine_device：设备基础信息表
mine_sensor：传感器基础信息表

关系：
mine_sensor.device_id -> mine_device.id（逻辑关联）

索引：
- mine_device：device_code唯一，device_type，area_name，status
- mine_sensor：sensor_code唯一，device_id，sensor_type，area_name，status

说明：
不建外键，仅业务关联。

## mine_sensor_data：传感器数据表

表名：mine_sensor_data

用途：记录智能矿山传感器每次模拟上报的数据，包括传感器编码、传感器类型、采集值、单位、告警阈值、是否告警、采集时间等。

字段包括：

id：主键ID。
sensor_id：传感器ID，逻辑关联 mine_sensor.id。
sensor_code：传感器编码。
sensor_name：传感器名称。
sensor_type：传感器类型，例如 GAS、TEMPERATURE、VIBRATION。
device_id：所属设备ID，逻辑关联 mine_device.id。
area_name：所属区域。
location：安装位置。
data_value：采集数值。
unit：单位。
alarm_threshold：告警阈值。
alarm_flag：是否告警，0否，1是。
collect_time：采集时间。
status：数据状态，0正常，1异常。
create_by：创建者。
create_time：创建时间。
update_by：更新者。
update_time：更新时间。
deleted：逻辑删除，0未删除，1已删除。
remark：备注。

索引包括：

idx_sensor_id(sensor_id)
idx_sensor_code(sensor_code)
idx_sensor_type(sensor_type)
idx_area_name(area_name)
idx_collect_time(collect_time)
idx_alarm_flag(alarm_flag)
idx_status(status)

说明：

mine_sensor_data.sensor_id 逻辑关联 mine_sensor.id。
mine_sensor_data.device_id 逻辑关联 mine_device.id。
本阶段不设置数据库外键，避免测试数据导入、删除和 Docker 初始化时受外键约束影响。

## M1-04：告警规则和告警事件表

M1-04 新增两张智能矿山业务表：

```text
mine_alarm_rule
mine_alarm_event
```

这两张表用于实现：

```text
传感器数据 -> 告警规则判断 -> 告警事件生成
```

### 1. mine_alarm_rule：智能矿山告警规则表

表名：

```text
mine_alarm_rule
```

用途：

```text
记录智能矿山平台中不同传感器类型对应的告警规则，例如瓦斯浓度超限、温度过高、设备振动过高等。
```

字段：

```text
id
rule_code
rule_name
sensor_type
compare_operator
threshold_value
alarm_level
alarm_title
alarm_content
status
create_by
create_time
update_by
update_time
deleted
remark
```

字段说明：

```text
id：主键ID
rule_code：规则编码
rule_name：规则名称
sensor_type：传感器类型，例如 GAS、TEMPERATURE、VIBRATION
compare_operator：比较符号，GT大于，GE大于等于，LT小于，LE小于等于，EQ等于
threshold_value：规则阈值
alarm_level：告警级别，1一般，2重要，3严重
alarm_title：告警标题
alarm_content：告警内容模板
status：状态，0启用，1停用
create_by：创建者
create_time：创建时间
update_by：更新者
update_time：更新时间
deleted：逻辑删除，0未删除，1已删除
remark：备注
```

状态说明：

```text
status = 0：启用
status = 1：停用
```

告警级别说明：

```text
alarm_level = 1：一般告警
alarm_level = 2：重要告警
alarm_level = 3：严重告警
```

比较符号说明：

```text
GT：大于
GE：大于等于
LT：小于
LE：小于等于
EQ：等于
```

索引：

```text
uk_rule_code(rule_code)
idx_sensor_type(sensor_type)
idx_alarm_level(alarm_level)
idx_status(status)
```

初始化规则：

```text
ALM-RULE-GAS-001：瓦斯浓度超限告警，sensor_type = GAS，compare_operator = GE，threshold_value = 1.0000，alarm_level = 3
ALM-RULE-TEMP-001：温度过高告警，sensor_type = TEMPERATURE，compare_operator = GE，threshold_value = 40.0000，alarm_level = 2
ALM-RULE-VIB-001：振动过高告警，sensor_type = VIBRATION，compare_operator = GE，threshold_value = 8.0000，alarm_level = 2
```

### 2. mine_alarm_event：智能矿山告警事件表

表名：

```text
mine_alarm_event
```

用途：

```text
记录根据传感器数据和告警规则生成的告警事件。
```

字段：

```text
id
event_code
rule_id
rule_code
rule_name
sensor_data_id
sensor_id
sensor_code
sensor_name
sensor_type
device_id
area_name
location
data_value
threshold_value
compare_operator
alarm_level
alarm_title
alarm_content
alarm_time
handle_status
status
create_by
create_time
update_by
update_time
deleted
remark
```

字段说明：

```text
id：主键ID
event_code：告警事件编码
rule_id：告警规则ID，逻辑关联 mine_alarm_rule.id
rule_code：告警规则编码
rule_name：告警规则名称
sensor_data_id：传感器数据ID，逻辑关联 mine_sensor_data.id
sensor_id：传感器ID，逻辑关联 mine_sensor.id
sensor_code：传感器编码
sensor_name：传感器名称
sensor_type：传感器类型
device_id：所属设备ID，逻辑关联 mine_device.id
area_name：所属区域
location：安装位置
data_value：触发告警的采集值
threshold_value：触发告警的规则阈值
compare_operator：比较符号
alarm_level：告警级别，1一般，2重要，3严重
alarm_title：告警标题
alarm_content：告警内容
alarm_time：告警时间
handle_status：处理状态，0未处理，1已确认，2已关闭
status：状态，0有效，1无效
create_by：创建者
create_time：创建时间
update_by：更新者
update_time：更新时间
deleted：逻辑删除，0未删除，1已删除
remark：备注
```

处理状态说明：

```text
handle_status = 0：未处理
handle_status = 1：已确认
handle_status = 2：已关闭
```

状态说明：

```text
status = 0：有效
status = 1：无效
```

索引：

```text
uk_event_code(event_code)
uk_rule_data(rule_id, sensor_data_id)
idx_rule_id(rule_id)
idx_sensor_data_id(sensor_data_id)
idx_sensor_id(sensor_id)
idx_sensor_code(sensor_code)
idx_sensor_type(sensor_type)
idx_area_name(area_name)
idx_alarm_level(alarm_level)
idx_alarm_time(alarm_time)
idx_handle_status(handle_status)
idx_status(status)
```

关联关系：

```text
mine_alarm_event.rule_id 逻辑关联 mine_alarm_rule.id
mine_alarm_event.sensor_data_id 逻辑关联 mine_sensor_data.id
mine_alarm_event.sensor_id 逻辑关联 mine_sensor.id
mine_alarm_event.device_id 逻辑关联 mine_device.id
```

说明：

```text
M1-04 暂不设置数据库外键。
继续使用逻辑关联，避免测试数据导入、删除和 Docker 初始化时受外键约束影响。
同一个 rule_id + sensor_data_id 不允许重复生成告警事件。
```

### 3. M1-04 SQL 文件

SQL 文件路径：

```text
scaffold-sql/m1_04_mine_alarm_rule_event.sql
```

第一行必须是：

```sql
SET NAMES utf8mb4;
```

固定开头：

```sql
SET NAMES utf8mb4;

USE enterprise_scaffold;
```

执行命令：

```cmd
cd /d D:\Code\enterprise-scaffold
mysql -u root -p < scaffold-sql\m1_04_mine_alarm_rule_event.sql
```

## mine_work_order：智能矿山工单表

表名：mine_work_order。

用途：记录由智能矿山告警事件生成的处置工单，用于完成告警确认、处理和关闭闭环。

核心字段包括：id、work_order_code、alarm_event_id、event_code、alarm_level、work_order_title、work_order_content、device_id、sensor_id、sensor_code、sensor_name、sensor_type、area_name、location、order_status、handler_user_id、handler_username、handle_time、handle_result、close_user_id、close_username、close_time、close_result、status、create_by、create_time、update_by、update_time、deleted、remark。

工单状态 order_status 固定含义：0 表示待处理，1 表示处理中，2 表示已处理，3 表示已关闭。数据状态 status 固定含义：0 表示有效，1 表示无效。逻辑删除 deleted 固定含义：0 表示未删除，1 表示已删除。

唯一约束包括 uk_work_order_code(work_order_code) 和 uk_alarm_event_id(alarm_event_id)。其中 uk_alarm_event_id 用于保证一个告警事件只能生成一个工单，避免重复转工单。

普通索引包括 idx_event_code(event_code)、idx_sensor_code(sensor_code)、idx_sensor_type(sensor_type)、idx_area_name(area_name)、idx_alarm_level(alarm_level)、idx_order_status(order_status)、idx_status(status)、idx_create_time(create_time)。

逻辑关联关系为：mine_work_order.alarm_event_id 逻辑关联 mine_alarm_event.id，mine_work_order.sensor_id 逻辑关联 mine_sensor.id，mine_work_order.device_id 逻辑关联 mine_device.id。本阶段不设置数据库外键，避免测试数据导入、删除和 Docker 初始化时受外键约束影响。

## M1-06：智能矿山看板

M1-06 完成智能矿山安全生产看板能力，在已有设备台账、传感器台账、传感器数据、告警规则、告警事件和工单闭环基础上，新增看板统计接口和前端可视化页面。

本阶段新增设备数量、传感器数量、采集数据数量、告警规则数量、告警事件数量、未处理告警数量、工单数量、待处理工单数量和已关闭工单数量统计。

本阶段新增告警级别分布、传感器类型分布、工单状态分布、最近告警事件和最近工单记录展示。

本阶段前端新增 ECharts 图表能力，用于展示智能矿山业务运行状态。

M1-06 不新增数据库表，不新增 SQL 文件，不接入 MQTT / EMQX。


## M1-07：MQTT 接入数据库说明

M1-07 不新增数据库表，不新增数据库字段，不修改已有表结构。

MQTT 上报数据继续复用已有智能矿山业务表：`mine_sensor`、`mine_sensor_data`、`mine_alarm_rule`、`mine_alarm_event`。

数据写入流程为：后端订阅 MQTT Topic `mine/sensor/data`，接收传感器 JSON 消息，根据 `sensorCode` 查询 `mine_sensor`，如果传感器存在且状态为正常，则写入 `mine_sensor_data`。

写入时继续沿用 M1-03 的规则，根据 `data_value >= alarm_threshold` 判断 `alarm_flag`。

其中 `alarm_flag = 0` 表示未告警，`alarm_flag = 1` 表示触发告警。

写入成功后调用已有 `MineAlarmEventService.generate(...)` 告警生成逻辑，符合 `mine_alarm_rule` 的数据会生成 `mine_alarm_event`。

MQTT 消息不会直接写入告警表，而是先落入 `mine_sensor_data`，再复用已有告警规则生成告警事件。

本阶段继续保持“不设置数据库外键”的设计，避免 Docker 初始化、测试数据导入和逻辑删除时受到外键约束影响。


## M1-08：MQTT 数据模拟增强数据库说明

M1-08 不新增数据库表，不新增数据库字段，不新增 SQL 文件。

批量 MQTT 模拟发布后的数据继续写入已有表 `mine_sensor_data` 和 `mine_alarm_event`。

其中，`mine_sensor_data` 由 `MineSensorMqttListener` 消费 MQTT 消息后写入。

当采集值达到告警阈值时，继续复用已有 `MineAlarmEventService.generate(...)` 生成告警事件，并写入 `mine_alarm_event`。

本阶段继续复用已有 `mine_sensor` 表查询正常传感器，按 `sensorType` 过滤传感器类型，按 `status = 0` 过滤正常传感器。

## M1-09：实时数据展示增强数据库说明

M1-09 不新增数据库表，不新增数据库字段，不新增 SQL 文件。本阶段只增强前端展示能力，继续复用已有智能矿山业务表。

复用表包括 `mine_sensor_data`、`mine_alarm_event`、`mine_work_order`、`mine_device`、`mine_sensor`、`mine_alarm_rule`。其中最近传感器数据表格读取 `mine_sensor_data` 最新记录；告警统计继续读取 `mine_alarm_event`；工单统计继续读取 `mine_work_order`；设备、传感器和告警规则数量继续复用原有看板统计逻辑。

批量模拟 MQTT 数据后，数据仍然通过已有链路写入数据库：`mine/sensor/data` Topic -> `MineSensorMqttListener` -> `mine_sensor_data` -> `MineAlarmEventService.generate(...)` -> `mine_alarm_event`。M1-09 不改变已有数据库结构，也不改变已有表字段含义。

## M1-10：设备健康评分数据库说明

M1-10 不新增数据库表。

M1-10 不新增数据库字段。

M1-10 不新增 SQL 文件。

本阶段复用已有表实时计算设备健康评分：

- `mine_device`：设备基础信息
- `mine_sensor`：设备关联传感器及最后上报时间
- `mine_alarm_event`：设备关联告警事件
- `mine_work_order`：设备关联工单状态

健康评分不落库，每次访问接口时由后端根据当前业务数据实时计算。

这样做的好处是避免冗余字段和数据同步问题，后续如果需要保留历史评分趋势，可以在后续阶段新增评分快照表。


## mine_maintenance_task：智能矿山预测性维护任务表

表名：mine_maintenance_task

用途：记录由设备健康评分和风险等级生成的预测性维护任务，用于完成维护计划、现场处理、复核关闭的业务闭环。

核心字段：
id、task_code、device_id、device_code、device_name、device_type、area_name、location、health_score、risk_level、risk_level_name、task_title、task_content、task_type、task_source、task_status、priority、plan_start_time、plan_end_time、maintainer_user_id、maintainer_username、handle_time、handle_result、close_time、close_result、status、create_by、create_time、update_by、update_time、deleted、remark。

任务状态：
task_status = 0：待安排
task_status = 1：待执行
task_status = 2：处理中
task_status = 3：已关闭

优先级：
priority = 0：低
priority = 1：中
priority = 2：高
priority = 3：紧急

关联关系：
mine_maintenance_task.device_id 逻辑关联 mine_device.id。

说明：
M1-11 不设置数据库外键，继续保持历史阶段“逻辑关联、不加物理外键”的规则。
新增 SQL 文件为 scaffold-sql/m1_11_mine_maintenance_task.sql，第一行必须是 SET NAMES utf8mb4;。
同时需要将 mine_maintenance_task 建表语句追加到 scaffold-sql/enterprise_scaffold_init.sql，保证全新 Docker 初始化数据库时表结构完整。


## M1-12：维护看板与风险趋势分析数据库说明

M1-12 不新增数据库表，不新增数据库字段，不新增 SQL 文件。

本阶段复用已有业务表：

- `mine_maintenance_task`：维护任务状态、优先级、风险等级、最近维护任务、高风险设备维护任务
- `mine_alarm_event`：最近 7 天告警趋势
- `mine_work_order`：最近 7 天工单趋势
- `mine_device`：设备基础信息

统计规则：

- 维护任务状态统计基于 `mine_maintenance_task.task_status`
- 维护任务优先级统计基于 `mine_maintenance_task.priority`
- 风险等级统计基于 `mine_maintenance_task.risk_level`
- 最近 7 天风险趋势基于：
    - `mine_alarm_event.alarm_time`
    - `mine_work_order.create_time`
    - `mine_maintenance_task.create_time`
- 高风险设备任务基于 `mine_maintenance_task.risk_level = 3` 且 `task_status != 3`


## M1-13：数据库总体验收说明

M1-13 不新增数据库表，不新增数据库字段，不新增 SQL 文件。

本阶段不创建 `scaffold-sql/m1_13_xxx.sql`。

本阶段复用项目一已有智能矿山业务表：

- `mine_device`：设备台账表
- `mine_sensor`：传感器台账表
- `mine_sensor_data`：传感器采集数据表
- `mine_alarm_rule`：告警规则表
- `mine_alarm_event`：告警事件表
- `mine_work_order`：智能矿山工单表
- `mine_maintenance_task`：预测性维护任务表

M1-13 数据库验收重点：

- 确认所有 `mine_%` 表存在。
- 确认 `mine_device` 和 `mine_sensor` 有初始化测试数据。
- 确认 `mine_alarm_rule` 有 GAS、TEMPERATURE、VIBRATION 三类告警规则。
- 确认 MQTT 模拟上报后 `mine_sensor_data` 可以产生数据。
- 确认告警生成后 `mine_alarm_event` 可以产生数据。
- 确认告警转工单后 `mine_work_order` 可以产生数据。
- 确认设备健康风险生成维护任务后 `mine_maintenance_task` 可以产生数据。

Docker MySQL 验收执行目录：

`D:\Code\enterprise-scaffold\scaffold-docker`

进入 MySQL 容器：

`docker exec -it enterprise-scaffold-mysql mysql -u root -p enterprise_scaffold`

进入数据库后执行：

`SHOW TABLES LIKE 'mine_%';`

预期至少看到以下表：

- `mine_device`
- `mine_sensor`
- `mine_sensor_data`
- `mine_alarm_rule`
- `mine_alarm_event`
- `mine_work_order`
- `mine_maintenance_task`

继续执行以下统计 SQL，用于确认项目一完整链路数据是否存在：

- `SELECT COUNT(*) AS device_count FROM mine_device WHERE deleted = 0;`
- `SELECT COUNT(*) AS sensor_count FROM mine_sensor WHERE deleted = 0;`
- `SELECT COUNT(*) AS sensor_data_count FROM mine_sensor_data WHERE deleted = 0;`
- `SELECT COUNT(*) AS alarm_rule_count FROM mine_alarm_rule WHERE deleted = 0;`
- `SELECT COUNT(*) AS alarm_event_count FROM mine_alarm_event WHERE deleted = 0;`
- `SELECT COUNT(*) AS work_order_count FROM mine_work_order WHERE deleted = 0;`
- `SELECT COUNT(*) AS maintenance_task_count FROM mine_maintenance_task WHERE deleted = 0;`

后续如果新增 SQL 文件，第一行仍然必须是：

`SET NAMES utf8mb4;`

第二段固定使用：

`USE enterprise_scaffold;`





#############################################################################################################
## A2-01 数据库说明

A2-01 不新增数据库表，不新增数据库字段，不新增 SQL 文件。本阶段只新增 `cn.sxu.enterprise.module.aiops` 后端模块骨架和 `GET /api/aiops/health` 健康检查接口。A2 后续数据库表统一使用 `aiops_` 前缀，数据库名继续固定为 `enterprise_scaffold`。


## A2-02：aiops_resource AIOps 资源台账表

A2-02 新增数据库表 `aiops_resource`，用于记录 AIOps 智能运维平台中的服务器、数据库、中间件、网络设备等资源台账。该表属于项目二“云网融合 AIOps 智能运维平台”的基础业务表，后续 `aiops_metric_data`、`aiops_alert_event`、`aiops_work_order`、`aiops_root_cause_record` 都会围绕资源台账进行关联和扩展。

表名固定为 `aiops_resource`。字段固定包括 `id`、`resource_code`、`resource_name`、`resource_type`、`ip_addr`、`port`、`env_type`、`system_name`、`owner_name`、`collect_enabled`、`last_collect_time`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。其中 `resource_code` 表示资源编码，`resource_name` 表示资源名称，`resource_type` 表示资源类型，`ip_addr` 表示 IP 地址，`port` 表示端口，`env_type` 表示环境类型，`system_name` 表示所属系统，`owner_name` 表示负责人，`collect_enabled` 表示是否启用采集，`last_collect_time` 表示最后采集时间，`status` 表示资源状态，`deleted` 表示逻辑删除。

`resource_type` 固定取值为 `SERVER`、`DATABASE`、`MIDDLEWARE`、`NETWORK`，分别表示服务器、数据库、中间件、网络设备。`env_type` 固定取值为 `DEV`、`TEST`、`PROD`，分别表示开发环境、测试环境、生产环境。`collect_enabled` 固定含义为 `0 = 启用采集`，`1 = 停止采集`。`status` 固定含义为 `0 = 正常`，`1 = 停用`，`2 = 异常`。`deleted` 继续沿用项目统一逻辑删除规则，`0 = 未删除`，`1 = 已删除`。

`aiops_resource` 表固定使用唯一索引 `uk_resource_code(resource_code)`，避免资源编码重复。查询索引包括 `idx_resource_type(resource_type)`、`idx_ip_addr(ip_addr)`、`idx_env_type(env_type)`、`idx_system_name(system_name)`、`idx_collect_enabled(collect_enabled)`、`idx_status(status)`。A2-02 暂不设置数据库外键，避免测试数据导入、Docker 初始化和后续演示时受到外键约束影响。后续如果需要资源与指标、告警、工单关联，优先使用逻辑关联字段，例如 `resource_id`、`resource_code`、`resource_name`。

## A2-03：aiops_metric_data 指标数据表

A2-03 新增数据库表 `aiops_metric_data`，用于记录 AIOps 智能运维平台中的资源指标采集数据。表名固定为 `aiops_metric_data`。

字段包括 `id`、`resource_id`、`resource_code`、`resource_name`、`resource_type`、`ip_addr`、`metric_code`、`metric_name`、`metric_type`、`metric_value`、`metric_unit`、`threshold_value`、`alarm_flag`、`collect_time`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。

其中 `resource_id` 逻辑关联 `aiops_resource.id`；`resource_code`、`resource_name`、`resource_type`、`ip_addr` 为资源冗余信息；`metric_code` 表示指标编码，例如 `CPU_USAGE`；`metric_name` 表示指标名称，例如 CPU 使用率；`metric_type` 表示指标类型，包括 `CPU`、`MEMORY`、`DISK`、`NETWORK`、`MYSQL`、`REDIS`；`metric_value` 表示指标值；`metric_unit` 表示指标单位；`threshold_value` 表示告警阈值；`alarm_flag` 表示是否触发告警，0 为未告警，1 为已告警；`collect_time` 表示采集时间；`status` 表示指标状态，0 为正常，1 为异常；`deleted` 为逻辑删除字段。

A2-03 暂不设置数据库外键，后续通过 `resource_id` 和 `resource_code` 与 `aiops_resource` 进行逻辑关联。新增 SQL 文件为 `scaffold-sql/a2_03_aiops_metric_data.sql`，文件第一行固定为 `SET NAMES utf8mb4;`，第二段固定为 `USE enterprise_scaffold;`。同时，`aiops_metric_data` 建表 SQL 已追加到 `scaffold-sql/enterprise_scaffold_init.sql`，保证 Docker MySQL 空数据卷初始化时不会缺表。

## A2-04：AIOps 告警规则、告警事件、运维工单表

A2-04 新增三张数据库表：`aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order`。这三张表用于支撑 AIOps 智能运维平台的告警中心与运维工单闭环。

`aiops_alert_rule` 是 AIOps 告警规则表，用于保存 CPU、内存、磁盘、网络、MySQL、Redis 等指标的告警判断规则。固定字段包括：`id`、`rule_code`、`rule_name`、`resource_type`、`metric_type`、`compare_operator`、`threshold_value`、`alert_level`、`alert_title`、`alert_content`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。

其中 `resource_type` 沿用 A2-02 的资源类型，固定为 `SERVER`、`DATABASE`、`MIDDLEWARE`、`NETWORK`；`metric_type` 沿用 A2-03 的指标类型，固定为 `CPU`、`MEMORY`、`DISK`、`NETWORK`、`MYSQL`、`REDIS`；`compare_operator` 固定为 `GT`、`GE`、`LT`、`LE`、`EQ`；`alert_level` 固定为 `1 = 一般`、`2 = 重要`、`3 = 严重`；`status` 固定为 `0 = 启用`、`1 = 停用`。

`aiops_alert_event` 是 AIOps 告警事件表，用于保存根据 `aiops_metric_data` 指标数据和 `aiops_alert_rule` 告警规则生成的告警事件。固定字段包括：`id`、`event_code`、`rule_id`、`rule_code`、`rule_name`、`metric_data_id`、`resource_id`、`resource_code`、`resource_name`、`resource_type`、`ip_addr`、`metric_code`、`metric_name`、`metric_type`、`metric_value`、`threshold_value`、`compare_operator`、`alert_level`、`alert_title`、`alert_content`、`alert_time`、`handle_status`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。

其中 `handle_status` 固定为 `0 = 未处理`、`1 = 已派单`、`2 = 已关闭`；`status` 固定为 `0 = 有效`、`1 = 无效`。表中通过 `rule_id + metric_data_id` 唯一约束避免同一条指标数据和同一条规则重复生成告警事件。

`aiops_work_order` 是 AIOps 运维工单表，用于保存由 AIOps 告警事件生成的运维处置工单。固定字段包括：`id`、`work_order_code`、`alert_event_id`、`event_code`、`alert_level`、`work_order_title`、`work_order_content`、`resource_id`、`resource_code`、`resource_name`、`resource_type`、`ip_addr`、`metric_code`、`metric_name`、`metric_type`、`order_status`、`handler_user_id`、`handler_username`、`handle_time`、`handle_result`、`close_user_id`、`close_username`、`close_time`、`close_result`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。

其中 `order_status` 固定为 `0 = 待处理`、`1 = 处理中`、`2 = 已处理`、`3 = 已关闭`；`status` 固定为 `0 = 有效`、`1 = 无效`。表中通过 `alert_event_id` 唯一约束避免同一个告警事件重复生成多个工单。

A2-04 暂不设置数据库外键，继续采用逻辑关联方式。`aiops_alert_event.rule_id` 逻辑关联 `aiops_alert_rule.id`，`aiops_alert_event.metric_data_id` 逻辑关联 `aiops_metric_data.id`，`aiops_alert_event.resource_id` 逻辑关联 `aiops_resource.id`，`aiops_work_order.alert_event_id` 逻辑关联 `aiops_alert_event.id`，`aiops_work_order.resource_id` 逻辑关联 `aiops_resource.id`。不设置数据库外键的原因是避免测试数据导入、删除和 Docker 初始化时受到外键约束影响。

## A2-05：aiops_root_cause_record 根因分析记录表

A2-05 新增 aiops_root_cause_record 表，用于保存 AIOps 根因分析记录。

该表记录某个告警事件对应的分析编码、告警事件信息、资源信息、根因类型、根因描述、
分析证据、处理建议、置信度、分析时间和状态信息。

字段包括 id、analysis_code、alert_event_id、event_code、resource_id、resource_code、
resource_name、resource_type、ip_addr、root_cause_type、root_cause_desc、evidence、
suggestion、confidence_score、analysis_time、status、create_by、create_time、update_by、
update_time、deleted、remark。

root_cause_type 固定支持 CPU_HIGH、MEMORY_HIGH、DISK_FULL、NETWORK_ABNORMAL、
DATABASE_SLOW、UNKNOWN。

confidence_score 表示根因分析置信度，范围为 0 到 100。

status = 0 表示有效，status = 1 表示无效。

deleted = 0 表示未删除，deleted = 1 表示已删除。

aiops_root_cause_record.alert_event_id 逻辑关联 aiops_alert_event.id。

aiops_root_cause_record.resource_id 逻辑关联 aiops_resource.id。

aiops_root_cause_record.resource_code 逻辑关联 aiops_resource.resource_code。

本阶段暂不设置数据库外键，继续采用逻辑关联方式，
避免测试数据导入、删除和 Docker 初始化时受外键约束影响。

A2-05 新增 SQL 文件 scaffold-sql/a2_05_aiops_root_cause.sql，
第一行固定为 SET NAMES utf8mb4;，
第二段固定使用 USE enterprise_scaffold;，
并且必须将 aiops_root_cause_record 建表语句同步追加到 scaffold-sql/enterprise_scaffold_init.sql。


### A2-06：AIOps 综合看板数据库说明

A2-06 不新增数据库表，不新增数据库字段，不新增 SQL 文件。

本阶段只做看板统计和可视化展示，继续复用 A2-02 至 A2-05 已经完成的 AIOps 业务表。

`aiops_resource` 用于统计资源总数、异常资源数量和资源类型分布。

`aiops_metric_data` 用于统计指标数据总数、异常指标数量和最近 7 天指标趋势。

`aiops_alert_rule` 用于统计告警规则数量。

`aiops_alert_event` 用于统计告警事件数量、未处理告警数量、告警级别分布和最近告警事件。

`aiops_work_order` 用于统计运维工单数量、待处理工单数量、工单状态分布和最近运维工单。

`aiops_root_cause_record` 用于统计根因分析记录数量、高置信根因分析数量和最近根因分析记录。

本阶段所有统计均基于已有表的查询和聚合完成，不改变任何表结构，不新增外键，不新增索引，不改变已有字段含义。

A2-06 继续遵守数据库名固定为 `enterprise_scaffold`、A2 表名前缀固定为 `aiops_`、逻辑删除字段固定为 `deleted`、统一状态字段固定为 `status` 的工程约定。

后续如需接入 Prometheus / Grafana 或 Spring Boot Actuator，将在 A2-07 中单独处理部署和监控配置。

## A2-07：Prometheus / Grafana 接入数据库说明

A2-07 不新增数据库表，不新增数据库字段，不新增 SQL 文件，也不修改 `enterprise_scaffold_init.sql`。

本阶段接入 Prometheus 和 Grafana。Prometheus 自身负责存储抓取到的时序指标数据，Grafana 使用自己的数据目录保存登录信息、数据源配置和后续仪表盘配置，因此不需要在 `enterprise_scaffold` 业务数据库中新增 `aiops_prometheus`、`aiops_grafana` 或其他监控配置表。

A2-07 继续复用已有 AIOps 业务表：`aiops_resource`、`aiops_metric_data`、`aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order`、`aiops_root_cause_record`。

后续如果新增 SQL 文件，第一行仍必须是 `SET NAMES utf8mb4;`，第二段仍必须是 `USE enterprise_scaffold;`。





## A2-08：数据库设计说明

A2-08 不新增数据库表，不新增数据库字段，不新增 SQL 文件，不修改 `enterprise_scaffold_init.sql`。本阶段只做 A2 项目总体验收与收尾，继续复用 A2 已有业务表。

A2 项目当前数据库表包括：`aiops_resource`、`aiops_metric_data`、`aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order`、`aiops_root_cause_record`。

其中，`aiops_resource` 记录资源台账，`aiops_metric_data` 记录指标数据，`aiops_alert_rule` 记录告警规则，`aiops_alert_event` 记录告警事件，`aiops_work_order` 记录运维工单，`aiops_root_cause_record` 记录根因分析结果。Prometheus 和 Grafana 的数据不写入 `enterprise_scaffold` 业务数据库，Prometheus 使用自身时序数据目录，Grafana 使用自身 Docker volume 保存数据源和面板配置。A2-08 阶段的验收重点是确认这些表已经能支撑资源、指标、告警、工单、根因分析和综合看板完整链路，不再额外扩展数据库结构。




















#############################################################################################################################################################

## R3-01 数据库设计说明

R3-01 不新增数据库表，不新增数据库字段，不新增 SQL 文件，也不修改 scaffold-sql/enterprise_scaffold_init.sql。原因是本阶段只新增银行风控模块骨架和健康检查接口，不涉及交易流水、规则命中、风险评分或人工审核等业务数据持久化。R3 后续数据库表前缀固定为 risk_，后续阶段规划新增 risk_transaction、risk_rule、risk_rule_hit、risk_review_order。后续新增 SQL 文件时，第一行必须是 SET NAMES utf8mb4;，第二段必须使用 USE enterprise_scaffold;，并且新增 SQL 必须同步追加到 scaffold-sql/enterprise_scaffold_init.sql。



### R3-02：risk_transaction 交易流水表

R3-02 新增 SQL 文件 `scaffold-sql/r3_02_risk_transaction.sql`，第一行固定为 `SET NAMES utf8mb4;`，第二段固定为 `USE enterprise_scaffold;`。同时需要把建表语句追加到 `scaffold-sql/enterprise_scaffold_init.sql`。

新增表 `risk_transaction`，字段固定为 `id`、`transaction_no`、`account_no`、`customer_id`、`customer_name`、`merchant_id`、`merchant_name`、`transaction_type`、`channel`、`amount`、`currency`、`ip_addr`、`device_id`、`location`、`transaction_time`、`transaction_status`、`risk_flag`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。

其中 `transaction_type` 固定为 `PAYMENT / TRANSFER / WITHDRAW / CONSUME`，`channel` 固定为 `APP / WEB / ATM / POS`，`transaction_status` 固定为 `0成功、1失败、2处理中`，`risk_flag` 固定为 `0未命中风险、1命中风险`，`status` 固定为 `0正常、1异常`。本表使用 `transaction_no` 唯一约束，并为账户号、客户ID、商户ID、交易类型、渠道、交易时间、交易状态、风险标记和状态建立索引。


## R3-03：规则引擎数据表设计

R3-03 新增 SQL 文件为 `scaffold-sql/r3_03_risk_rule_hit.sql`。该 SQL 文件第一行固定为 `SET NAMES utf8mb4;`，第二段固定为 `USE enterprise_scaffold;`，并且 SQL 内容必须同步追加到 `scaffold-sql/enterprise_scaffold_init.sql`。

### risk_rule 风控规则表

| 字段名 | 含义 |
| --- | --- |
| id | 主键ID |
| rule_code | 规则编码，唯一 |
| rule_name | 规则名称 |
| rule_type | 规则类型，固定为 AMOUNT、FREQUENCY、LOCATION、DEVICE、TIME、BLACKLIST |
| condition_type | 条件类型 |
| compare_operator | 比较操作符，支持 GT、GTE、LT、LTE、EQ、CONTAINS、NIGHT |
| threshold_value | 规则阈值 |
| risk_level | 风险等级，1低风险，2中风险，3高风险 |
| risk_score | 风险分 |
| rule_content | 规则内容 |
| status | 状态，0启用，1停用 |
| create_by | 创建人 |
| create_time | 创建时间 |
| update_by | 更新人 |
| update_time | 更新时间 |
| deleted | 逻辑删除，0未删除，1已删除 |
| remark | 备注 |

### risk_rule_hit 规则命中表

| 字段名 | 含义 |
| --- | --- |
| id | 主键ID |
| hit_code | 命中编码，唯一 |
| transaction_id | 交易ID |
| transaction_no | 交易流水号 |
| account_no | 账号 |
| customer_id | 客户ID |
| customer_name | 客户姓名 |
| rule_id | 规则ID |
| rule_code | 规则编码 |
| rule_name | 规则名称 |
| rule_type | 规则类型 |
| hit_value | 命中值 |
| threshold_value | 命中阈值 |
| risk_level | 风险等级，1低风险，2中风险，3高风险 |
| risk_score | 风险分 |
| hit_time | 命中时间 |
| status | 状态，0未处理，1已处理 |
| create_by | 创建人 |
| create_time | 创建时间 |
| update_by | 更新人 |
| update_time | 更新时间 |
| deleted | 逻辑删除，0未删除，1已删除 |
| remark | 备注 |

`risk_rule_hit` 使用 `transaction_id + rule_id` 唯一约束，避免同一笔交易对同一条规则重复生成命中记录。生成命中记录后，后端会把对应 `risk_transaction.risk_flag` 更新为 1，并把 `risk_transaction.status` 更新为 1。



## R3-04：risk_review_order 人工审核单表

R3-04 新增表 `risk_review_order`，用于保存风险评分后的人工审核单。字段包括：`id`、`review_order_code`、`transaction_id`、`transaction_no`、`account_no`、`customer_id`、`customer_name`、`merchant_id`、`merchant_name`、`transaction_type`、`channel`、`amount`、`currency`、`total_score`、`risk_level`、`risk_result`、`review_status`、`reviewer_user_id`、`reviewer_username`、`review_time`、`review_result`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。风险等级固定为：`1` 低风险、`2` 中风险、`3` 高风险。风险结果固定为：`PASS` 放行、`REVIEW` 人工审核、`REJECT` 拒绝。审核状态固定为：`0` 待审核、`1` 审核通过、`2` 审核拒绝。表中 `review_order_code` 唯一，`transaction_id` 唯一，避免同一笔交易重复生成审核单。


## R3-05：Kafka 接入数据库设计说明

R3-05 不新增数据库表、不新增数据库字段、不新增 SQL 文件，因此不创建 `scaffold-sql/r3_05_xxx.sql`。

本阶段 Kafka 消费后的交易消息继续写入已有 `risk_transaction` 表，交易流水表仍然作为银行风控平台的核心数据入口。

Kafka 消息中的 `transactionNo`、`accountNo`、`customerId`、`customerName`、`merchantId`、`merchantName`、`transactionType`、`channel`、`amount`、`currency`、`ipAddr`、`deviceId`、`location`、`transactionTime`、`transactionStatus`、`remark` 等字段会映射到 `risk_transaction` 对应字段。

写入时继续保持 `riskFlag = 0`、`status = 0`、`deleted = 0`，并使用 `transaction_no` 做重复交易判断，避免同一 Kafka 消息重复消费后重复入库。

R3-05 后，`risk_transaction` 同时支持原有 HTTP 模拟交易写入和 Kafka 消息消费写入。

后续 `risk_rule`、`risk_rule_hit`、`risk_review_order` 继续围绕 `risk_transaction` 完成规则命中、风险评分和人工审核。


## R3-06：风控看板数据库说明

R3-06 不新增数据库表，不新增数据库字段，不新增 SQL 文件。

本阶段复用以下已有表进行统计：

- `risk_transaction`：统计交易总数、风险交易数、交易渠道分布、交易类型分布、最近交易流水。
- `risk_rule`：统计风控规则总数。
- `risk_rule_hit`：统计规则命中总数、最近规则命中记录。
- `risk_review_order`：统计待审核数量、审核通过数量、审核拒绝数量、风险等级分布、最近人工审核单。

本阶段继续保持数据库名 `enterprise_scaffold` 不变，继续保持 R3 表名前缀 `risk_` 不变，继续保持逻辑删除字段 `deleted` 不变。

## R3-07：项目三总体验收数据库说明

R3-07 不新增数据库表，不新增数据库字段，不新增 SQL 文件。

本阶段继续复用项目三已经完成的风控业务表：

- `risk_transaction`：银行交易流水表，用于保存 HTTP 模拟交易和 Kafka 消费后的实时交易。
- `risk_rule`：风控规则表，用于保存大额交易、高频交易、异地交易、异常设备、夜间交易、黑名单等规则。
- `risk_rule_hit`：规则命中表，用于保存每次交易命中的风控规则记录。
- `risk_review_order`：人工审核单表，用于保存风险评分后生成的人工审核任务。

R3-07 的数据库验收重点是确认以上表均存在，且能够支撑完整链路：交易写入 `risk_transaction`，规则命中写入 `risk_rule_hit`，风险评分生成 `risk_review_order`，风控看板基于以上表做汇总统计和最近记录展示。

R3-07 不创建 `scaffold-sql/r3_07_xxx.sql`。后续如果新增 SQL 文件，第一行必须继续使用 `SET NAMES utf8mb4;`，第二段必须继续使用 `USE enterprise_scaffold;`。

************************************************************************************************************



## D4-01：数据治理模块骨架数据库说明

D4-01 不新增数据库表，不新增数据库字段，不新增 SQL 文件，也不修改 `enterprise_scaffold_init.sql`。

原因是本阶段只做国企 / 政务数据治理与共享交换平台的模块骨架和健康检查接口，不涉及业务数据持久化。

后续 D4-02 开始进入数据源管理阶段时，才会新增 `datahub_datasource` 表。

D4 后续数据库表统一使用 `datahub_` 前缀，数据库名继续固定为 `enterprise_scaffold`，逻辑删除字段继续使用 `deleted`，通用状态字段继续使用 `status`。


## D4-02：datahub_datasource 数据源管理表

D4-02 新增 datahub_datasource 表，用于记录数据治理平台中的数据源基础信息。字段包括 id、datasource_code、datasource_name、datasource_type、jdbc_url、host、port、database_name、username、password、owner_name、env_type、test_status、last_test_time、status、create_by、create_time、update_by、update_time、deleted、remark。datasource_type 表示数据源类型，例如 MYSQL、POSTGRESQL、ORACLE、SQLSERVER、API。env_type 表示环境类型，DEV 为开发环境，TEST 为测试环境，PROD 为生产环境。test_status 表示最近连接测试状态，0 为未测试，1 为成功，2 为失败。status 表示数据源状态，0 为正常，1 为停用。本阶段暂不设置数据库外键，后续 datahub_metadata_table、datahub_metadata_column、datahub_quality_result 等表通过 datasource_id 和 datasource_code 与 datahub_datasource 做逻辑关联。

# 01 Project Overview

## 项目名称

Enterprise Scaffold 工程级企业后台脚手架项目。

## 当前阶段

当前已经推进到 `D4-03 元数据采集` 阶段，并已完成该阶段的主要后端与前端联调。

D4-03 的目标是在 D4-02 数据源管理基础上，通过已有数据源 ID 自动连接数据库，并采集数据库表和字段元数据。

当前阶段不重新设计项目，不重新选择技术路线，不回退到 P-01、S0-01 或 D4-02。后续继续在现有工程结构、现有 Docker 环境、现有 `datahub_` 表体系和现有前后端接口规范上迭代。

## 当前已完成内容

截至 D4-03，项目已经完成：

1. 基础工程结构搭建。
2. Docker Compose 本地开发环境。
3. 后端 Spring Boot 基础接口体系。
4. 前端 Vue3 + Element Plus 基础页面体系。
5. D4-02 数据源管理基础能力。
6. D4-03 元数据采集能力。
7. D4-03 前后端联调。
8. D4-03 Docker 环境下 MySQL 连接问题修复。
9. D4-03 前端采集成功提示 undefined 问题修复。
10. D4-03 前端 TypeScript 构建错误修复。

## D4-03 阶段目标

D4-03 阶段实现的是“元数据采集”，即后端根据前端传入的数据源 ID，读取 D4-02 中已经保存的数据源连接信息，然后连接目标 MySQL 数据库，自动采集数据库中的表、视图、字段、字段类型、字段注释、是否主键、是否可为空、默认值、行数、采集批次和采集日志。

核心接口为：

```http
POST /api/datahub/metadata/collect
```

请求体为：

```json
{
  "dataSourceId": 1
}
```

## 当前真实数据源表

当前项目真实数据源表名是：

```text
datahub_datasource
```

不是：

```text
datahub_data_source
```

后续所有 D4-03 代码、SQL、文档、排查说明中，均必须使用 `datahub_datasource`。

## 当前真实数据源字段

当前 D4-02 数据源表中的关键字段包括：

```text
id
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

其中：

```text
datasource_type = MYSQL
```

用于判断数据源类型。后端不能只读取 `db_type` 或 `database_type`，否则会出现“D4-03 当前仅支持 MySQL 数据源元数据采集”的错误。

## 当前 Docker 规则

当前 Docker 环境中 MySQL 容器名为：

```text
enterprise-scaffold-mysql
```

后端容器访问 MySQL 时不能使用：

```text
localhost
127.0.0.1
```

因为在 Docker 容器内部，`localhost` 指向的是当前容器自己，不是 MySQL 容器。

正确 MySQL 连接地址应为：

```text
enterprise-scaffold-mysql
```

正确 JDBC URL 示例：

```text
jdbc:mysql://enterprise-scaffold-mysql:3306/enterprise_scaffold?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
```

## 当前已确认问题记录

D4-03 调试中已经确认以下问题，并作为后续永久排查规则：

1. 数据源真实表名是 `datahub_datasource`，不是 `datahub_data_source`。
2. 数据源类型真实字段是 `datasource_type`，不是 `db_type`。
3. 数据源名称真实字段是 `datasource_name`。
4. Docker 环境中 JDBC URL 不能使用 `localhost`。
5. `Access denied using password:NO` 表示 `datahub_datasource.password` 为空。
6. 密码应写入 `.env` 中 `MYSQL_ROOT_PASSWORD` 对应的值。
7. 前端“采集成功：表 undefined 张，字段 undefined 个”是返回字段或 ApiResult 解包不一致导致。
8. 前端应读取 `tableTotal` 和 `columnTotal`，并兼容 ApiResult 包装结构。
9. 前端 TypeScript 错误曾由重复声明 `const result`、错误使用未定义 `res`、访问类型不存在的 `tableCount/tableNum/columnCount/columnNum` 导致。
10. 修复前端时应使用 `response`、`collectResult` 等清晰变量名，避免变量重复声明。

## 当前下一步

D4-03 已完成基本采集闭环后，下一步应继续进入后续数据治理能力建设，例如：

1. 元数据详情页。
2. 字段搜索和筛选增强。
3. 数据血缘或数据质量模块。
4. 数据源连接测试能力增强。
5. 元数据定时采集。
6. 元数据变更对比。

后续不能重新推荐项目，不能重新规划技术路线，不能从已经完成的旧阶段重新开始。

## D4-04 数据质量检测表设计

D4-04 新增 SQL 文件 `scaffold-sql/d4_04_datahub_quality.sql`，文件第一行固定为 `SET NAMES utf8mb4;`，第二段固定为 `USE enterprise_scaffold;`。本阶段新增两张表：`datahub_quality_rule` 和 `datahub_quality_result`。`datahub_quality_rule` 用于保存质量检测规则，核心字段包括 `id`、`rule_code`、`rule_name`、`rule_type`、`target_type`、`target_table_id`、`target_column_id`、`check_expression`、`quality_level`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。其中 `rule_type` 固定支持 `NOT_NULL`、`UNIQUE`、`RANGE`、`FORMAT`、`ENUM`，`target_type` 固定支持 `TABLE` 和 `COLUMN`，`quality_level` 固定为 1 一般、2 重要、3 严重，`status` 固定为 0 启用、1 停用。`datahub_quality_result` 用于保存检测结果，核心字段包括 `id`、`result_code`、`rule_id`、`rule_code`、`rule_name`、`datasource_id`、`datasource_code`、`table_id`、`table_code`、`column_id`、`column_name`、`check_total`、`error_total`、`error_rate`、`check_status`、`check_time`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。其中 `check_status` 固定为 0 通过、1 不通过。D4-04 不修改 D4-02 已固定表 `datahub_datasource`，也不修改 D4-03 已固定表 `datahub_metadata_table`、`datahub_metadata_column`、`datahub_metadata_collect_log`。


## D4-05 数据库设计：敏感字段与脱敏

D4-05 新增 SQL 文件 `scaffold-sql/d4_05_datahub_sensitive_mask.sql`，第一行固定为 `SET NAMES utf8mb4;`，第二段固定使用 `USE enterprise_scaffold;`。本阶段新增三张表：`datahub_sensitive_field`、`datahub_mask_rule`、`datahub_mask_result`。`datahub_sensitive_field` 用于保存基于元数据字段识别出的敏感字段，核心字段包括 `field_code`、`datasource_id`、`datasource_name`、`table_id`、`schema_name`、`table_name`、`column_id`、`column_name`、`column_comment`、`data_type`、`sensitive_type`、`sensitive_level`、`detect_rule`、`confidence`、`confirm_status`、`mask_type`、`status`、`deleted`。`datahub_mask_rule` 用于保存脱敏规则，核心字段包括 `rule_code`、`rule_name`、`sensitive_type`、`mask_method`、`keep_prefix`、`keep_suffix`、`mask_char`、`status`、`deleted`。`datahub_mask_result` 用于保存脱敏预览结果，核心字段包括 `result_code`、`field_id`、`rule_id`、`datasource_id`、`datasource_name`、`table_id`、`table_name`、`column_id`、`column_name`、`sensitive_type`、`mask_method`、`sample_before`、`sample_after`、`mask_status`、`mask_time`、`status`、`deleted`。本阶段还给 `datahub_metadata_column` 补充 `sensitive_flag`、`sensitive_type`、`mask_type` 三个字段，用于在字段元数据中直接标记敏感识别结果。


## D4-06 数据库设计：API 共享发布与调用日志

D4-06 新增 SQL 文件 `scaffold-sql/d4_06_datahub_api_dashboard.sql`，SQL 第一行固定为 `SET NAMES utf8mb4;`，第二段固定使用 `USE enterprise_scaffold;`。本阶段新增 `datahub_api_publish` 和 `datahub_api_call_log` 两张表。`datahub_api_publish` 用于保存从元数据表发布出来的共享 API，核心字段包括 `api_code`、`api_name`、`datasource_id`、`datasource_code`、`datasource_name`、`table_id`、`table_code`、`table_name`、`api_path`、`request_method`、`publish_type`、`online_status`、`auth_required`、`owner_name`、`publish_time`、`last_online_time`、`last_offline_time`、`status`、`deleted` 等，其中 `online_status = 0` 表示下线，`online_status = 1` 表示上线。`datahub_api_call_log` 用于后续记录 API 调用流水，核心字段包括 `call_code`、`api_id`、`api_code`、`api_name`、`request_path`、`request_method`、`caller_ip`、`call_status`、`error_msg`、`cost_time`、`call_time`、`status`、`deleted` 等。本阶段暂不实现真实动态数据开放接口，只先完成 API 发布记录、上下线管理和看板统计，为后续继续扩展真实 API 调用、调用日志写入和权限控制保留表结构。

## D4-07：项目四数据库验收说明

D4-07 不新增 SQL 文件，不新增数据库表，不新增数据库字段。本阶段只对项目四已经完成的数据治理表进行总体验收和文档收尾。项目四当前固定数据库为 `enterprise_scaffold`，数据治理相关表继续使用 `datahub_` 前缀。验收链路涉及 `datahub_datasource`、`datahub_metadata_table`、`datahub_metadata_column`、`datahub_metadata_collect_log`、`datahub_quality_rule`、`datahub_quality_result`、`datahub_sensitive_field`、`datahub_mask_rule`、`datahub_mask_result`、`datahub_api_publish`、`datahub_api_call_log`。其中 `datahub_api_publish.table_id` 关联 `datahub_metadata_table.id`，API 上线状态字段固定为 `online_status`，不要写成旧规划里的 `publish_status`。D4-07 的数据库验收重点是确认这些表已经存在、字段命名与后端实体一致、逻辑删除字段 `deleted` 可用、初始化 SQL 已经包含 D4-06 以前所有数据治理建表内容。

******************************************************************************************************

## I5-01：抽象 IAM 模块数据库说明

I5-01 不新增 SQL 文件，不新增数据库表，不新增数据库字段，也不修改 `enterprise_scaffold_init.sql`。本阶段只创建 IAM 后端模块骨架和健康检查接口，暂不涉及业务数据持久化。项目五后续如果进入 I5-02、I5-03、I5-04、I5-05 等需要新增数据库表的阶段，IAM 相关业务表统一使用 `iam_` 前缀；新增 SQL 文件第一行必须固定为 `SET NAMES utf8mb4;`，第二段必须固定使用 `USE enterprise_scaffold;`。


## I5-02：IAM 接口访问日志表

I5-02 新增 SQL 文件 `scaffold-sql/i5_02_iam_access_log.sql`，SQL 第一行固定为 `SET NAMES utf8mb4;`，第二段固定使用 `USE enterprise_scaffold;`。本阶段新增表 `iam_access_log`，该表属于项目五 IAM 模块，继续使用 `iam_` 前缀。表字段包括 `id`、`trace_id`、`request_uri`、`request_method`、`module_name`、`operation_name`、`user_id`、`username`、`client_ip`、`user_agent`、`request_params`、`response_code`、`response_msg`、`access_status`、`cost_ms`、`access_time`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted` 和 `remark`。其中 `access_status` 用于区分访问成功和失败，`cost_ms` 用于记录接口耗时，`access_time` 用于记录访问发生时间，`deleted` 继续作为逻辑删除字段。本阶段不新增 Docker 服务、不修改 Docker volume、不修改上传目录挂载规则。


### I5-03：iam_login_risk 异常登录风险表

I5-03 新增 SQL 文件 `scaffold-sql/i5_03_iam_login_risk.sql`，SQL 第一行固定为 `SET NAMES utf8mb4;`，第二段固定使用 `USE enterprise_scaffold;`。本阶段新增表 `iam_login_risk`，字段包括 `id`、`risk_code`、`username`、`client_ip`、`risk_type`、`risk_level`、`fail_count`、`first_fail_time`、`last_fail_time`、`detect_time`、`handle_status`、`status`、`create_by`、`create_time`、`update_by`、`update_time`、`deleted`、`remark`。其中 `risk_type` 表示风险类型，当前包括 `LOGIN_FAILED`、`SHORT_TIME_FAILED`、`ABNORMAL_IP`；`risk_level` 表示风险等级，1 为低风险，2 为中风险，3 为高风险；`handle_status` 表示处理状态，0 为未处理，1 为已确认，2 为已忽略。该表继续使用 `iam_` 前缀，`deleted` 字段继续作为逻辑删除字段。

### I5-04
I5-04 新增 SQL 文件 scaffold-sql/i5_04_iam_rate_limit_rule.sql，SQL 第一行固定为 SET NAMES utf8mb4;，第二段固定使用 USE enterprise_scaffold;。本阶段新增表 iam_rate_limit_rule，字段包括 id、rule_code、rule_name、request_uri、request_method、limit_dimension、limit_window_seconds、max_requests、limit_action、enabled、status、create_by、create_time、update_by、update_time、deleted、remark。其中 limit_dimension 表示限流维度，当前包括 GLOBAL、USER、IP；limit_window_seconds 表示限流时间窗口，单位秒；max_requests 表示窗口内最大请求次数；limit_action 表示命中动作，当前包括 WARN 和 REJECT；enabled 表示规则是否启用，0 为停用，1 为启用。该表继续使用 iam_ 前缀，deleted 字段继续作为逻辑删除字段。

### I5-05:
I5-05 新增 SQL 文件 scaffold-sql/i5_05_iam_security_policy.sql，SQL 第一行固定为 SET NAMES utf8mb4;，第二段固定使用 USE enterprise_scaffold;。本阶段新增表 iam_security_policy，字段包括 id、policy_code、policy_name、policy_type、policy_level、policy_value、policy_unit、effective_scope、enabled、status、create_by、create_time、update_by、update_time、deleted、remark。其中 policy_type 表示策略类型，当前包括 LOGIN、RATE_LIMIT、RISK、AUDIT；policy_level 表示策略等级，1 为低，2 为中，3 为高；policy_value 表示策略值；policy_unit 表示策略单位，当前包括 COUNT、SECOND、DAY、LEVEL；effective_scope 表示生效范围，当前包括 GLOBAL、USER、IP、API；enabled 表示是否启用，0 为停用，1 为启用。该表继续使用 iam_ 前缀，deleted 字段继续作为逻辑删除字段。








