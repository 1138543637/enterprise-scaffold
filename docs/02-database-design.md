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