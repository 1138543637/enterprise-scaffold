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

