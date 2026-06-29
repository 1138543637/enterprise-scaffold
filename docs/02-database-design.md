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

