## S0-04 用户分页查询

### 接口名称

用户分页查询

### 请求方式

GET

### 请求路径

/api/system/users/page

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| pageNo | number | 否 | 当前页，默认 1 |
| pageSize | number | 否 | 每页条数，默认 10，最大 100 |
| username | string | 否 | 登录账号，模糊查询 |
| nickname | string | 否 | 用户昵称，模糊查询 |
| status | number | 否 | 用户状态：0正常 1停用 |

### 返回字段

| 字段 | 说明 |
|---|---|
| pageNo | 当前页 |
| pageSize | 每页条数 |
| total | 总记录数 |
| pages | 总页数 |
| records | 用户列表 |

### 说明

本接口用于系统管理模块的用户列表页。当前阶段不接入登录认证，后续 S0-05、S0-06 会增加 JWT 和权限控制。

## S0-05 登录认证 JWT

### 1. 用户登录

请求方式：

POST

请求路径：

/api/auth/login

请求体：

```json
{
  "username": "admin",
  "password": "admin123"
}