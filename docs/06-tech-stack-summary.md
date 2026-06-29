# Enterprise Scaffold 技术栈总结文档

当前版本：S0-08  
维护要求：每完成一个阶段，必须同步更新本文档。  
建议仓库路径：`docs/06-tech-stack-summary.md`

---

# 0. 文档用途

本文档用于长期记录 `enterprise-scaffold` 项目的技术栈、使用位置、解决的问题、对应代码文件、接口路径和后续扩展方向。

这个文档不是简单列技术名，而是帮助后续做三件事：

1. 项目完成后快速回顾每个技术为什么用；
2. 写简历、准备面试时可以直接提炼项目亮点；
3. 新阶段开发时避免忘记已经用过的包名、类名、接口路径和配置方式。

每完成一个阶段，例如 S0-09、S0-10、M1-01，都必须更新本文档。

---

# 1. 项目总体定位

项目名称：

```text
Enterprise Scaffold
```

项目定位：

```text
面向央国企、能源、运营商、银行科技、政务数字化场景的企业级后台脚手架。
```

项目作用：

```text
作为后续五个业务项目的公共基础底座：
1. 智能矿山安全生产与设备预测性维护平台
2. 云网融合 AIOps 智能运维平台
3. 银行实时交易风控与反欺诈平台
4. 国企 / 政务数据治理与共享交换平台
5. 统一身份认证、权限审计与数据安全平台
```

当前阶段：

```text
已完成到 S0-08：操作日志和登录日志
下一步：S0-09：字典管理和文件上传
```

---

# 2. 总体技术栈一览

## 2.1 后端技术栈

| 技术 | 当前使用状态 | 主要作用 |
|---|---|---|
| Java 17 | 已使用 | 后端主开发语言 |
| Spring Boot 3 | 已使用 | 后端应用框架 |
| Maven | 已使用 | 后端依赖管理和构建 |
| Spring Web | 已使用 | REST API 接口开发 |
| Validation | 已使用 | 登录参数等请求参数校验 |
| Spring Security | 已使用 | 登录认证、安全过滤器、接口保护 |
| JWT | 已使用 | 无状态登录 Token |
| BCrypt | 已使用 | 密码哈希校验 |
| MyBatis-Plus | 已使用 | ORM、基础 CRUD、分页查询 |
| MySQL | 已使用 | 业务数据存储 |
| JdbcTemplate | 已使用 | 数据库健康检查 |
| Spring AOP | 已使用 | 操作日志切面 |
| Redis | 未开始 | 后续缓存、验证码、权限缓存、限流 |
| Docker Compose | 未开始 | 后续一键部署 |

## 2.2 前端技术栈

| 技术 | 当前使用状态 | 主要作用 |
|---|---|---|
| Vue3 | 已使用 | 前端主框架 |
| Vite | 已使用 | 前端构建工具和开发服务器 |
| TypeScript | 已使用 | 前端类型约束 |
| Element Plus | 已使用 | 后台管理 UI 组件库 |
| Vue Router | 已使用 | 页面路由 |
| Pinia | 已使用 | 登录状态和用户信息管理 |
| Axios | 已使用 | 调用后端接口 |
| ECharts | 未开始 | 后续看板图表 |
| MQTT 前端可视化 | 未开始 | 后续智能矿山传感器数据展示 |

## 2.3 数据库与存储技术

| 技术 | 当前使用状态 | 主要作用 |
|---|---|---|
| MySQL | 已使用 | 保存用户、角色、菜单、日志、字典、文件记录 |
| 本地文件存储 | 未开始，S0-09 使用 | 文件上传初版 |
| MinIO | 未开始 | 后续对象存储增强 |
| Redis | 未开始 | 后续缓存和安全能力 |

## 2.4 运维与部署技术

| 技术 | 当前使用状态 | 主要作用 |
|---|---|---|
| Git | 已使用 | 版本管理 |
| GitHub | 已使用 | 远程仓库 |
| Docker Desktop | 已安装，未正式接入 | 后续 Docker Compose 部署 |
| Docker Compose | 未开始，S0-10 使用 | 后端、前端、MySQL、Redis 一键启动 |
| Linux | 未开始 | 后续部署演示 |
| Prometheus / Grafana | 未开始 | 后续 AIOps 指标监控 |

---

# 3. 后端技术栈详细总结

## 3.1 Java 17

使用阶段：

```text
S0-02 开始
```

项目中的作用：

```text
后端主开发语言。
```

对应目录：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise
```

当前重点类：

```text
ScaffoldBackendApplication.java
ApiResult.java
PageResult.java
BusinessException.java
GlobalExceptionHandler.java
```

总结表达：

```text
项目后端基于 Java 17 开发，使用 Spring Boot 3 构建企业级后台服务。
```

## 3.2 Spring Boot 3

使用阶段：

```text
S0-02 开始
```

项目中的作用：

```text
作为后端应用基础框架，负责应用启动、依赖注入、Web 接口、配置管理等。
```

启动类路径：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/ScaffoldBackendApplication.java
```

配置文件路径：

```text
scaffold-backend/src/main/resources/application.yml
```

当前启动命令：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
set MYSQL_PASSWORD=你的MySQL密码
set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32
mvn spring-boot:run
```

总结表达：

```text
使用 Spring Boot 3 搭建后端服务，统一管理 Controller、Service、Mapper、Security、AOP 等组件。
```

## 3.3 Spring Web

使用阶段：

```text
S0-02 开始
```

项目中的作用：

```text
提供 REST API 能力。
```

已完成 Controller：

```text
HealthController
DatabaseHealthController
AuthController
SysUserController
SysRoleController
SysMenuController
SysPermissionController
SysLoginLogController
SysOperLogController
```

接口示例：

```text
GET  /api/health
POST /api/auth/login
GET  /api/system/users/page
GET  /api/system/oper-logs/page
```

总结表达：

```text
使用 Spring Web 对外提供 RESTful 风格接口，统一返回 ApiResult 结构。
```

## 3.4 Validation

使用阶段：

```text
S0-05 开始
```

项目中的作用：

```text
对登录请求参数进行基础校验。
```

典型文件：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/vo/LoginRequest.java
```

字段校验：

```java
@NotBlank(message = "用户名不能为空")
@NotBlank(message = "密码不能为空") 
```

总结表达：

```text
使用 Validation 对请求参数进行约束，减少 Controller 中手写参数判断。
```

## 3.5 MySQL

使用阶段：

```text
S0-03 开始
```

数据库名：

```text
enterprise_scaffold
```

字符集：

```text
utf8mb4
```

排序规则：

```text
utf8mb4_unicode_ci
```

当前已建基础表：

```text
sys_dept
sys_user
sys_role
sys_menu
sys_post
sys_user_role
sys_role_menu
sys_user_post
sys_dict_type
sys_dict_data
sys_login_log
sys_oper_log
sys_file
```

核心作用：

```text
保存系统用户、部门、角色、菜单、权限、日志、字典、文件等企业后台基础数据。
```

总结表达：

```text
项目使用 MySQL 存储系统基础数据，表结构围绕企业后台常见的用户、角色、菜单、日志、字典、文件模块设计。
```

## 3.6 MyBatis-Plus

使用阶段：

```text
S0-03 / S0-04 开始
```

项目中的作用：

```text
简化数据库 CRUD 和分页查询。
```

配置类：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/common/mybatis/config/MybatisPlusConfig.java
```

重要配置：

```text
map-underscore-to-camel-case: true
logic-delete-field: deleted
logic-delete-value: 1
logic-not-delete-value: 0
```

已使用 Mapper：

```text
SysUserMapper
SysRoleMapper
SysMenuMapper
SysUserRoleMapper
SysRoleMenuMapper
SysLoginLogMapper
SysOperLogMapper
```

分页返回统一使用：

```text
PageResult<T>
```

总结表达：

```text
使用 MyBatis-Plus 实现实体与数据库表映射，通过 BaseMapper 和 ServiceImpl 简化 CRUD，并使用分页插件统一处理分页查询。
```

## 3.7 Spring Security

使用阶段：

```text
S0-05 开始
```

配置类：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/common/security/config/SecurityConfig.java
```

当前作用：

```text
1. 禁用 csrf、formLogin、httpBasic、logout
2. 使用无状态 Session
3. 放行健康检查和登录接口
4. 其他接口都需要登录
5. 注册 JwtAuthenticationFilter
6. 未登录返回统一 401 JSON
```

放行接口：

```text
/api/health
/api/health/db
/api/auth/login
```

需要登录接口示例：

```text
/api/auth/me
/api/system/users/page
/api/system/roles/page
/api/system/menus/tree
/api/system/login-logs/page
/api/system/oper-logs/page
```

总结表达：

```text
项目使用 Spring Security 作为安全框架，结合 JWT 实现前后端分离场景下的无状态认证。
```

## 3.8 JWT

使用阶段：

```text
S0-05 开始
```

核心类：

```text
JwtProperties
JwtTokenProvider
JwtAuthenticationFilter
LoginUser
```

配置前缀：

```text
enterprise.security.jwt
```

配置项：

```text
issuer
secret
expireMinutes
```

认证请求头：

```text
Authorization: Bearer <token>
```

Token 中保存：

```text
issuer
subject = username
userId
nickname
issuedAt
expiration
```

总结表达：

```text
使用 JWT 保存登录态，后端不依赖 Session，适合前后端分离和后续微服务扩展。
```

## 3.9 BCrypt

使用阶段：

```text
S0-05 开始
```

项目中的作用：

```text
对用户密码进行安全哈希存储和校验。
```

默认账号：

```text
username: admin
password: admin123
```

数据库字段：

```text
sys_user.password_hash
```

校验代码位置：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/service/AuthService.java
```

总结表达：

```text
项目使用 BCrypt 对密码进行哈希校验，避免明文存储密码。
```

## 3.10 RBAC 权限模型

使用阶段：

```text
S0-06 开始
```

涉及表：

```text
sys_user
sys_role
sys_menu
sys_user_role
sys_role_menu
```

关系：

```text
用户 -> 用户角色关联 -> 角色 -> 角色菜单关联 -> 菜单 / 按钮权限
```

已完成接口：

```text
GET /api/system/roles/page
GET /api/system/menus/tree
GET /api/system/users/{userId}/permissions
```

当前策略：

```text
S0-06 只做权限查询
暂时不做 @PreAuthorize
暂时不做按钮级后端拦截
```

总结表达：

```text
项目实现了基础 RBAC 权限模型，支持查询用户角色、权限标识和菜单树，为后续动态路由和按钮权限控制打基础。
```

## 3.11 Spring AOP

使用阶段：

```text
S0-08 开始
```

项目中的作用：

```text
实现操作日志自动记录。
```

核心文件：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/common/web/annotation/OperLog.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/system/aspect/OperLogAspect.java
```

日志表：

```text
sys_oper_log
```

使用方式：

```java
@OperLog(title = "用户管理", businessType = "分页查询")
@GetMapping("/api/system/users/page")
public ApiResult<PageResult<SysUserPageVO>> pageUsers(SysUserPageQuery query) {
    return ApiResult.success(sysUserService.pageUsers(query));
}
```

记录内容：

```text
模块标题
业务类型
请求方法
操作人员
请求地址
请求 IP
请求参数
响应结果
操作状态
错误信息
耗时
操作时间
```

总结表达：

```text
项目使用 Spring AOP + 自定义 @OperLog 注解实现操作日志，业务代码只需加注解即可记录审计信息。
```

## 3.12 登录日志

使用阶段：

```text
S0-08 开始
```

日志表：

```text
sys_login_log
```

核心类：

```text
SysLoginLog
SysLoginLogMapper
SysLoginLogService
SysLoginLogServiceImpl
SysLoginLogController
```

写入位置：

```text
AuthService.login()
```

记录规则：

```text
登录成功：status = 0，msg = 登录成功
登录失败：status = 1，msg = 失败原因
```

查询接口：

```text
GET /api/system/login-logs/page
```

总结表达：

```text
项目实现登录成功和失败日志记录，便于后续安全审计、异常登录检测和 IAM 模块扩展。
```

## 3.13 操作日志

使用阶段：

```text
S0-08 开始
```

日志表：

```text
sys_oper_log
```

核心类：

```text
SysOperLog
SysOperLogMapper
SysOperLogService
SysOperLogServiceImpl
SysOperLogController
OperLog
OperLogAspect
```

查询接口：

```text
GET /api/system/oper-logs/page
```

当前已加日志注解的接口：

```text
GET /api/system/users/page
GET /api/system/roles/page
GET /api/system/menus/tree
GET /api/system/users/{userId}/permissions
GET /api/system/login-logs/page
GET /api/system/oper-logs/page
```

总结表达：

```text
项目实现了基于注解的操作日志能力，可以记录用户访问接口的行为，为企业系统审计和问题追踪提供基础。
```

---

# 4. 前端技术栈详细总结

## 4.1 Vue3

使用阶段：

```text
S0-07 开始
```

前端目录：

```text
scaffold-frontend
```

入口文件：

```text
scaffold-frontend/src/main.ts
```

根组件：

```text
scaffold-frontend/src/App.vue
```

总结表达：

```text
前端使用 Vue3 构建后台管理界面，当前已完成登录页和 Dashboard 首页。
```

## 4.2 Vite

使用阶段：

```text
S0-07 开始
```

配置文件：

```text
scaffold-frontend/vite.config.ts
```

开发端口：

```text
5173
```

开发代理：

```text
/api -> http://localhost:8080
```

启动命令：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-frontend
pnpm dev
```

构建命令：

```cmd
pnpm build
```

总结表达：

```text
使用 Vite 作为前端开发和构建工具，开发环境通过 proxy 将 /api 请求转发到后端。
```

## 4.3 TypeScript

使用阶段：

```text
S0-07 开始
```

当前类型文件：

```text
scaffold-frontend/src/types/api.ts
scaffold-frontend/src/api/system/auth.ts
```

已定义类型：

```text
ApiResult<T>
LoginRequest
LoginResponse
LoginUser
```

总结表达：

```text
前端使用 TypeScript 定义接口返回结构和请求参数类型，减少前后端字段不一致的问题。
```

## 4.4 Element Plus

使用阶段：

```text
S0-07 开始
```

引入位置：

```text
scaffold-frontend/src/main.ts
```

当前使用页面：

```text
LoginView.vue
DashboardView.vue
```

当前使用组件：

```text
el-form
el-form-item
el-input
el-button
el-container
el-header
el-main
el-card
el-descriptions
ElMessage
```

总结表达：

```text
前端使用 Element Plus 快速搭建后台页面，保证界面风格统一。
```

## 4.5 Vue Router

使用阶段：

```text
S0-07 开始
```

路由文件：

```text
scaffold-frontend/src/router/index.ts
```

当前路由：

```text
/          -> /dashboard
/login     -> LoginView.vue
/dashboard -> DashboardView.vue
```

路由守卫：

```text
未登录访问 /dashboard，跳转 /login
已登录访问 /login，跳转 /dashboard
```

总结表达：

```text
使用 Vue Router 管理前端页面，并通过路由守卫实现基础登录拦截。
```

## 4.6 Pinia

使用阶段：

```text
S0-07 开始
```

Store 文件：

```text
scaffold-frontend/src/stores/auth.ts
```

Store 名：

```text
auth
```

保存内容：

```text
token
user
```

方法：

```text
login(data)
loadMe()
logout()
```

总结表达：

```text
使用 Pinia 管理登录状态和当前用户信息，刷新页面后可从 localStorage 恢复 token。
```

## 4.7 Axios

使用阶段：

```text
S0-07 开始
```

统一封装文件：

```text
scaffold-frontend/src/api/request.ts
```

业务接口文件：

```text
scaffold-frontend/src/api/system/auth.ts
```

Token Key：

```text
enterprise_scaffold_token
```

当前封装能力：

```text
请求前自动添加 Authorization: Bearer <token>
响应后统一判断 ApiResult.code
code !== 0 时弹出错误
401 时清除 token
```

总结表达：

```text
使用 Axios 封装前端请求，统一处理 Token、响应结构和错误提示。
```

---

# 5. 已完成阶段技术栈变化记录

## 5.1 P-01：开发环境

新增技术和工具：

```text
Java 17
Maven
Git
Node.js
pnpm
Docker
MySQL 客户端
Apifox
IntelliJ IDEA
```

价值：

```text
完成 Java 后端、Vue 前端、数据库、接口测试和后续 Docker 部署所需的开发环境。
```

## 5.2 P-02：GitHub 仓库

新增技术和工具：

```text
Git
GitHub
```

仓库：

```text
https://github.com/1138543637/enterprise-scaffold.git
```

价值：

```text
建立版本管理和远程代码托管能力。
```

## 5.3 S0-01：基础目录

新增内容：

```text
scaffold-backend
scaffold-frontend
scaffold-sql
scaffold-docker
docs
README.md
```

价值：

```text
形成前后端分离、SQL 独立管理、Docker 独立管理、文档独立管理的工程结构。
```

## 5.4 S0-02：Spring Boot 后端初始化

新增技术：

```text
Spring Boot 3
Spring Web
ApiResult
```

新增接口：

```text
GET /api/health
```

价值：

```text
跑通后端服务启动和统一返回结构。
```

## 5.5 S0-03：MySQL 与基础表

新增技术：

```text
MySQL
MyBatis-Plus
JdbcTemplate
```

新增内容：

```text
enterprise_scaffold 数据库
13 张系统基础表
GET /api/health/db
```

价值：

```text
建立企业后台系统的数据库基础。
```

## 5.6 S0-04：用户分页

新增技术：

```text
MyBatis-Plus 分页插件
LambdaQueryWrapper
PageResult
```

新增接口：

```text
GET /api/system/users/page
```

价值：

```text
实现系统中第一个正式业务分页接口，形成 Controller / Service / Mapper / Entity / VO 分层样板。
```

## 5.7 S0-05：JWT 登录认证

新增技术：

```text
Spring Security
JWT
BCrypt
Validation
```

新增接口：

```text
POST /api/auth/login
GET  /api/auth/me
```

价值：

```text
实现前后端分离项目的登录认证基础，所有系统接口可以基于 token 保护。
```

## 5.8 S0-06：RBAC 权限基础查询

新增技术 / 设计：

```text
RBAC
用户-角色-菜单权限模型
菜单树构建
权限标识 permissions
```

新增接口：

```text
GET /api/system/roles/page
GET /api/system/menus/tree
GET /api/system/users/{userId}/permissions
```

价值：

```text
系统从“只能登录”升级为“能查询用户拥有哪些角色、菜单和按钮权限”。
```

## 5.9 S0-07：Vue3 前端初始化

新增技术：

```text
Vue3
Vite
TypeScript
Element Plus
Vue Router
Pinia
Axios
```

新增页面：

```text
/login
/dashboard
```

价值：

```text
项目从纯后端接口阶段进入前后端联调阶段，实现浏览器登录和首页展示。
```

## 5.10 S0-08：操作日志和登录日志

新增技术：

```text
Spring AOP
自定义注解 @OperLog
操作日志切面
登录日志记录
```

新增接口：

```text
GET /api/system/login-logs/page
GET /api/system/oper-logs/page
```

价值：

```text
为企业级系统补齐安全审计能力，可以追踪登录行为和接口访问行为。
```

---

# 6. 当前可写进简历的技术点

当前项目还没有全部完成，但到 S0-08 已经可以提炼这些技术点。

## 6.1 后端方向

```text
基于 Spring Boot 3 + MyBatis-Plus + MySQL 搭建企业级后台脚手架，完成用户、角色、菜单、权限、登录日志、操作日志等系统基础模块。
```

```text
使用 Spring Security + JWT 实现前后端分离登录认证，使用 BCrypt 完成密码哈希校验，统一处理未登录 401 返回。
```

```text
设计并实现 RBAC 权限模型，基于 sys_user、sys_role、sys_menu、sys_user_role、sys_role_menu 表完成角色分页、菜单树和用户权限查询。
```

```text
使用 Spring AOP + 自定义 @OperLog 注解实现操作日志，记录请求方法、操作人员、请求 URL、请求参数、响应结果、异常信息和耗时。
```

```text
使用 MyBatis-Plus 实现分页查询和逻辑删除，统一封装 PageResult 分页返回结构。
```

## 6.2 前端方向

```text
基于 Vue3 + Vite + TypeScript + Element Plus 初始化后台管理前端，实现登录页、首页、路由守卫和后端接口联调。
```

```text
使用 Pinia 管理登录 token 和当前用户信息，使用 Axios 拦截器统一添加 Authorization 请求头并处理 ApiResult 响应结构。
```

## 6.3 企业信息化方向

```text
项目面向央国企信息化场景，围绕统一认证、权限管理、操作审计、日志追踪等后台系统基础能力构建，为后续智能矿山、AIOps、银行风控和数据治理模块提供公共底座。
```

---

# 7. 面试讲解模板

## 7.1 一分钟项目介绍

```text
Enterprise Scaffold 是我从零搭建的企业级后台脚手架，定位是服务于央国企、能源、运营商、银行科技和政务数字化场景。项目采用 Spring Boot 3、MyBatis-Plus、MySQL、Spring Security、JWT、Vue3、Vite、Element Plus 等技术，已经完成了用户分页、JWT 登录认证、RBAC 权限查询、Vue3 前端登录联调、登录日志和操作日志等基础能力。后续会在这个脚手架上扩展智能矿山、AIOps、银行风控、数据治理和统一身份认证等业务模块。
```

## 7.2 登录认证怎么实现

```text
登录时前端调用 POST /api/auth/login，后端根据 username 查询 sys_user，判断用户是否存在、账号是否停用，再用 BCrypt 校验密码。校验通过后由 JwtTokenProvider 生成 JWT，返回 token、tokenType、expiresIn 和用户基础信息。后续请求由前端 Axios 拦截器自动添加 Authorization: Bearer token，后端 JwtAuthenticationFilter 解析 token，并把 LoginUser 放入 SecurityContext。
```

## 7.3 RBAC 怎么实现

```text
RBAC 使用 sys_user、sys_role、sys_menu、sys_user_role、sys_role_menu 五张表。用户和角色通过 sys_user_role 关联，角色和菜单权限通过 sys_role_menu 关联。系统提供角色分页、菜单树查询和用户权限查询接口，用户权限接口会返回 roleKeys、permissions 和 menus。当前阶段先实现权限查询，后续会继续扩展按钮权限拦截和前端动态菜单。
```

## 7.4 操作日志怎么实现

```text
操作日志使用 Spring AOP 和自定义 @OperLog 注解实现。Controller 方法上加 @OperLog 后，OperLogAspect 会拦截方法调用，记录模块标题、业务类型、请求方法、操作人、请求 URL、请求 IP、请求参数、返回结果、异常信息和耗时，最后保存到 sys_oper_log 表。日志记录被 try-catch 包裹，即使日志失败也不会影响主业务接口。
```

## 7.5 前端登录怎么实现

```text
前端使用 Vue3 + Vite + TypeScript + Element Plus。登录页调用 auth.ts 中的 loginApi，请求后端 /api/auth/login。登录成功后 Pinia 保存 token，并写入 localStorage。Axios 请求拦截器会自动读取 enterprise_scaffold_token，并添加 Authorization 请求头。Vue Router 路由守卫会判断 token，未登录访问 /dashboard 会跳回 /login。
```

---

# 8. 后续技术栈扩展计划

## 8.1 S0-09：字典管理和文件上传

预计新增 / 使用：

```text
sys_dict_type
sys_dict_data
sys_file
MultipartFile
本地文件存储 LOCAL
文件上传接口
字典查询接口
```

预计接口：

```text
GET  /api/system/dict-types/page
GET  /api/system/dict-data/page
GET  /api/system/dict-data/type/{dictType}
POST /api/system/files/upload
GET  /api/system/files/page
```

完成后本文档需要补充：

```text
字典管理技术总结
文件上传技术总结
MultipartFile 使用位置
本地文件存储路径配置
文件表字段说明
```

## 8.2 S0-10：Docker Compose 一键部署

预计新增 / 使用：

```text
Dockerfile
Docker Compose
MySQL 容器
Redis 容器
后端容器
前端 Nginx 容器
```

完成后本文档需要补充：

```text
Docker Compose 架构
容器端口
环境变量
部署命令
常见问题
```

## 8.3 M1：智能矿山平台

预计新增 / 使用：

```text
module/mine
设备台账
传感器台账
告警规则
告警事件
工单闭环
MQTT
EMQX
ECharts
```

完成后本文档需要补充：

```text
智能矿山业务技术栈
MQTT 数据流
告警规则设计
工单闭环设计
看板图表设计
```

## 8.4 A2：AIOps 智能运维平台

预计新增 / 使用：

```text
Prometheus
Grafana
指标采集
日志检索
告警中心
根因分析
```

完成后本文档需要补充：

```text
AIOps 技术栈
指标数据模型
告警流转设计
根因分析简化实现
```

## 8.5 R3：银行实时交易风控平台

预计新增 / 使用：

```text
Kafka
交易模拟
规则引擎
风险评分
人工审核
风控看板
```

完成后本文档需要补充：

```text
风控技术栈
交易消息流
规则引擎设计
风险评分设计
```

## 8.6 D4：数据治理与共享交换平台

预计新增 / 使用：

```text
数据源管理
元数据采集
数据质量检测
敏感数据识别
脱敏
API 共享
数据血缘
```

完成后本文档需要补充：

```text
数据治理技术栈
元数据模型
数据质量规则
脱敏策略
共享接口设计
```

## 8.7 I5：统一身份认证与安全审计

预计新增 / 使用：

```text
IAM
权限审计
接口访问日志
异常登录检测
接口限流
SSO 简化版
```

完成后本文档需要补充：

```text
IAM 技术栈
SSO 设计
异常登录检测逻辑
接口限流策略
审计日志增强
```

---

# 9. 每阶段更新本文档的固定规则

每完成一个阶段，都必须更新本文档。

## 9.1 必须更新的位置

每次至少更新：

```text
# 2. 总体技术栈一览
# 5. 已完成阶段技术栈变化记录
# 6. 当前可写进简历的技术点
# 8. 后续技术栈扩展计划
```

如果新增了一个重要技术，例如 Redis、Kafka、MQTT、Docker、MinIO，还必须新增一个详细小节，例如：

```text
# 3.x Redis
# 3.x Kafka
# 3.x MQTT
# 3.x Docker Compose
```

或者如果是前端技术，则新增：

```text
# 4.x ECharts
# 4.x 动态路由
```

## 9.2 每个阶段更新时必须记录

每个阶段完成后，必须补充：

```text
1. 本阶段新增了什么技术
2. 这个技术解决了什么问题
3. 对应的后端文件路径
4. 对应的前端文件路径
5. 对应的数据库表
6. 对应的接口路径
7. 如何启动和验证
8. 可以写进简历的一句话
9. 面试时怎么解释
10. 后续还能怎么增强
```

## 9.3 提交要求

每次更新本文档后，建议和该阶段代码一起提交。

示例：

```cmd
cd /d D:\Code\enterprise-scaffold

git add .

git commit -m "feat: implement dictionary and file upload api"

git push
```

如果只是单独更新技术栈文档，可以提交：

```cmd
git add docs/06-tech-stack-summary.md

git commit -m "docs: update tech stack summary"

git push
```

---

# 10. 永久提醒

后续每完成一个阶段，都要同时检查并更新：

```text
docs/03-api-design.md
docs/04-deploy-guide.md
docs/05-github-reference.md
docs/06-tech-stack-summary.md
```

如果涉及数据库变化，还要更新：

```text
docs/02-database-design.md
```

如果进入新的业务模块，还要更新：

```text
README.md
docs/01-project-overview.md
```

本文档 `docs/06-tech-stack-summary.md` 是长期维护文件，不能只在最后项目完成时才写。
## S0-09：字典管理和文件上传技术栈总结

### 1. 本阶段新增技术

S0-09 新增或正式使用：

- Spring MultipartFile
- 本地文件存储
- Spring MVC 静态资源映射
- 文件 MD5 计算
- MyBatis-Plus 多表基础查询
- 字典数据接口封装
- 文件元数据管理

### 2. 解决了什么问题

本阶段解决两个企业后台常见问题：

1. 字典管理  
   用于统一维护状态、类型、枚举类数据，例如用户状态、性别、是否默认、业务类型等。

2. 文件上传  
   用于上传附件、图片、证明材料、日志文件、数据文件等，为后续智能矿山、AIOps、银行风控、数据治理模块提供公共文件能力。

### 3. 后端文件路径

配置文件：

```text
scaffold-backend/src/main/resources/application.yml
```

## S0-10：Docker Compose 一键部署技术总结

### 1. 本阶段新增技术

| 技术 | 用途 |
|---|---|
| Dockerfile | 构建后端和前端镜像 |
| Docker Compose | 编排 MySQL、后端、前端多个容器 |
| MySQL Docker 镜像 | 容器化运行 MySQL 数据库 |
| Nginx | 托管 Vue3 前端静态资源并代理接口 |
| Docker volume | 持久化 MySQL 数据 |
| bind mount | 持久化本地上传文件 uploads |
| .env | 管理本地环境变量 |

### 2. 解决的问题

S0-10 解决了项目从“本机手动启动”到“容器化一键部署”的问题。

原来需要分别启动：

```text
本机 MySQL
mvn spring-boot:run
pnpm dev
```
## M1-01：智能矿山业务模块骨架

### 1. 本阶段新增了什么

本阶段新增智能矿山业务模块包：`cn.sxu.enterprise.module.mine`。

新增模块健康检查接口：`GET /api/mine/health`。

本阶段没有新增第三方依赖，没有引入 MQTT、EMQX、Kafka、Redis、Prometheus 或 Grafana。

### 2. 这个技术解决了什么问题

M1-01 解决的是公共脚手架向业务项目扩展的问题。

项目不再只是系统管理后台，而是开始进入第一个业务项目：智能矿山安全生产与设备预测性维护平台。

通过新增 `module/mine`，后续设备台账、传感器数据、告警规则、工单闭环和智能矿山看板都可以放在同一个业务模块下持续扩展。

### 3. 对应后端文件路径

新增控制器：

`scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/controller/MineHealthController.java`

模块目录：

- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/controller`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/entity`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/mapper`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/impl`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo`
- `scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/dto`

### 4. 对应前端文件路径

本阶段不新增前端页面。

当前前端仍然保持：

- `scaffold-frontend/src/views/login/LoginView.vue`
- `scaffold-frontend/src/views/dashboard/DashboardView.vue`

后续 M1-06 智能矿山看板阶段再新增前端页面。

### 5. 对应数据库表

本阶段不新增业务表。

本阶段复用已有操作日志表：`sys_oper_log`。

原因是 `MineHealthController` 使用：

`@OperLog(title = "智能矿山", businessType = "模块健康检查")`

### 6. 对应接口路径

接口：`GET /api/mine/health`

认证方式：`Authorization: Bearer <token>`

成功返回：

{
"code": 0,
"msg": "success",
"data": "enterprise-scaffold mine module running"
}

### 7. 如何启动和验证

本地启动后端：

`cd /d D:\Code\enterprise-scaffold\scaffold-backend`

`set MYSQL_PASSWORD=你的MySQL密码`

`set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32`

`set LOCAL_UPLOAD_PATH=D:\Code\enterprise-scaffold\uploads`

`mvn spring-boot:run`

登录获取 token：

`POST http://localhost:8080/api/auth/login`

访问智能矿山模块健康检查：

`GET http://localhost:8080/api/mine/health`

查询操作日志：

`GET http://localhost:8080/api/system/oper-logs/page?pageNo=1&pageSize=10&title=智能矿山`

### 8. 可以写进简历的一句话

在企业级 Spring Boot 3 后台脚手架基础上，按模块化分层方式扩展智能矿山业务模块，并复用 JWT 认证、统一返回结构和操作日志审计能力。

### 9. 面试时怎么解释

我先完成了公共后台脚手架，包括登录认证、RBAC 基础查询、操作日志、文件上传和 Docker Compose 部署。随后在不破坏公共能力的前提下，新建 `module/mine` 智能矿山业务模块，通过一个受 JWT 保护的健康检查接口验证模块扫描、认证拦截和操作日志切面是否生效。这样后续设备台账、传感器数据、告警规则和工单闭环都可以在同一业务模块下持续扩展。

### 10. 后续还能怎么增强

- M1-02：新增设备台账和传感器台账
- M1-03：新增模拟传感器数据
- M1-04：新增告警规则和告警事件
- M1-05：新增工单闭环
- M1-06：新增智能矿山看板
- M1-07：接入 MQTT / EMQX


## M1-02

新增能力：
设备台账 + 传感器台账分页查询

技术：
Spring Boot
MyBatis-Plus
JWT
MySQL
AOP日志

接口：
/api/mine/devices/page
/api/mine/sensors/page

## M1-03：智能矿山传感器模拟数据

本阶段新增技术与能力：

1. 智能矿山传感器数据表设计。
2. 基于 mine_sensor 台账生成模拟传感器数据。
3. 使用 MyBatis-Plus BaseMapper 写入业务数据。
4. 使用 LambdaQueryWrapper 实现条件查询。
5. 使用 MyBatis-Plus Page 实现历史数据分页。
6. 使用 Java ThreadLocalRandom 生成模拟采集值。
7. 使用 BigDecimal 保存传感器采集数值。
8. 使用 @OperLog 记录模拟生成、最新数据查询、历史数据分页查询操作日志。
9. 使用 JWT 保护智能矿山业务接口。

解决的问题：

M1-02 已经完成设备台账和传感器台账，但还没有传感器运行数据。M1-03 补齐了“传感器产生数据”的基础能力，使后续告警规则、告警事件、工单闭环和看板展示有数据来源。

后端文件路径：

scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/entity/MineSensorData.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/mapper/MineSensorDataMapper.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/MineSensorDataService.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/impl/MineSensorDataServiceImpl.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/controller/MineSensorDataController.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/dto/MineSensorDataSimulateRequest.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineSensorDataPageQuery.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineSensorDataVO.java

数据库表：

mine_sensor_data

SQL 文件：

scaffold-sql/m1_03_mine_sensor_data.sql

接口路径：

POST /api/mine/sensor-data/simulate
GET /api/mine/sensor-data/latest
GET /api/mine/sensor-data/page

启动和验证：

1. 执行 scaffold-sql/m1_03_mine_sensor_data.sql。
2. 启动后端。
3. 登录获取 JWT token。
4. 调用 POST /api/mine/sensor-data/simulate 生成模拟数据。
5. 调用 GET /api/mine/sensor-data/latest 查询最新数据。
6. 调用 GET /api/mine/sensor-data/page 分页查询历史数据。
7. 查询 sys_oper_log 验证操作日志是否记录成功。

简历表达：

在智能矿山安全生产与设备预测性维护平台中，设计并实现传感器数据模拟模块，基于传感器台账动态生成实时采集数据，支持最新数据查询、历史数据分页查询和操作日志审计，为后续告警规则和设备预测性维护提供数据基础。

面试解释：

M1-03 主要解决智能矿山模块“只有台账、没有运行数据”的问题。系统读取 mine_sensor 中正常状态的传感器，根据传感器类型、上下限和告警阈值生成模拟采集值，写入 mine_sensor_data 表。如果采集值达到阈值，则标记为告警数据。接口层继续使用 JWT 认证、ApiResult 统一返回和 @OperLog 操作日志，保持企业后台项目的一致性。

后续增强：

1. M1-04 基于 mine_sensor_data 实现告警规则和告警事件。
2. M1-06 基于最新数据接口实现智能矿山看板。
3. M1-07 接入 MQTT / EMQX，将模拟接口替换为真实消息接入。
4. 后续可加入定时任务自动生成传感器数据。

## M1-04：智能矿山告警规则和告警事件

### 1. 本阶段新增技术点

```text
告警规则建模
告警事件建模
规则匹配
事件生成
告警事件分页查询
规则事件去重
MyBatis-Plus 条件查询
MyBatis-Plus 分页查询
Spring 事务 @Transactional
```

### 2. 这个技术解决了什么问题

M1-03 已经能够生成传感器模拟数据，并写入 `mine_sensor_data`。

但 M1-03 只解决了“采集数据”的问题，还没有形成“业务告警”。

M1-04 通过新增：

```text
mine_alarm_rule
mine_alarm_event
```

实现了：

```text
传感器数据 -> 告警规则判断 -> 告警事件生成
```

这样后续 M1-05 就可以继续实现：

```text
告警事件 -> 工单 -> 处理闭环
```

### 3. 对应后端文件路径

Entity：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/entity/MineAlarmRule.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/entity/MineAlarmEvent.java
```

Mapper：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/mapper/MineAlarmRuleMapper.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/mapper/MineAlarmEventMapper.java
```

Service：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/MineAlarmRuleService.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/MineAlarmEventService.java
```

ServiceImpl：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/impl/MineAlarmRuleServiceImpl.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/impl/MineAlarmEventServiceImpl.java
```

Controller：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/controller/MineAlarmRuleController.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/controller/MineAlarmEventController.java
```

DTO：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/dto/MineAlarmGenerateRequest.java
```

VO / Query：

```text
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineAlarmRulePageQuery.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineAlarmRulePageVO.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineAlarmEventPageQuery.java
scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineAlarmEventPageVO.java
```

### 4. 对应前端文件路径

M1-04 暂不新增前端页面。

前端目录保持不变：

```text
scaffold-frontend/src/views
scaffold-frontend/src/router/index.ts
```

### 5. 对应数据库表

```text
mine_alarm_rule
mine_alarm_event
```

SQL 文件：

```text
scaffold-sql/m1_04_mine_alarm_rule_event.sql
```

SQL 文件第一行：

```sql
SET NAMES utf8mb4;
```

### 6. 对应接口路径

告警规则分页查询：

```text
GET /api/mine/alarm-rules/page
```

告警事件分页查询：

```text
GET /api/mine/alarm-events/page
```

根据传感器数据生成告警事件：

```text
POST /api/mine/alarm-events/generate
```

### 7. 如何启动和验证

执行 SQL：

```cmd
cd /d D:\Code\enterprise-scaffold
mysql -u root -p < scaffold-sql\m1_04_mine_alarm_rule_event.sql
```

启动后端：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
set MYSQL_PASSWORD=你的MySQL密码
set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32
set LOCAL_UPLOAD_PATH=D:\Code\enterprise-scaffold\uploads
mvn spring-boot:run
```

登录获取 token：

```text
POST http://localhost:8080/api/auth/login
```

Body：

```json
{
  "username": "admin",
  "password": "admin123"
}
```

验证告警规则分页：

```text
GET http://localhost:8080/api/mine/alarm-rules/page?pageNo=1&pageSize=10
```

先生成传感器数据：

```text
POST http://localhost:8080/api/mine/sensor-data/simulate
```

Body：

```json
{
  "count": 10
}
```

生成告警事件：

```text
POST http://localhost:8080/api/mine/alarm-events/generate
```

Body：

```json
{
  "limit": 100
}
```

查询告警事件：

```text
GET http://localhost:8080/api/mine/alarm-events/page?pageNo=1&pageSize=10
```

所有受保护接口都需要请求头：

```text
Authorization: Bearer <token>
```

### 8. 可以写进简历的一句话

基于 Spring Boot 3、MyBatis-Plus 和 MySQL 设计并实现智能矿山告警规则与告警事件模块，支持传感器数据规则匹配、告警事件自动生成、分页查询、重复事件去重和操作日志审计。

### 9. 面试时怎么解释

可以这样说：

```text
在智能矿山模块中，我先通过 mine_sensor_data 保存传感器采集数据，然后通过 mine_alarm_rule 配置不同传感器类型的告警规则，例如瓦斯浓度超限、温度过高、振动过高等。系统扫描传感器数据后，根据规则中的 compare_operator 和 threshold_value 判断是否触发告警。如果触发，就生成 mine_alarm_event 告警事件。

为了避免重复生成告警事件，我在 mine_alarm_event 表中增加了 rule_id + sensor_data_id 的唯一约束，保证同一条传感器数据对同一条规则只生成一次告警。
```

### 10. 后续还能怎么增强

```text
1. 增加告警确认接口
2. 增加告警关闭接口
3. 增加告警转工单能力
4. 增加告警等级统计接口
5. 增加告警趋势统计接口
6. 前端增加告警规则页面
7. 前端增加告警事件页面
8. MQTT 实时数据接入后自动触发告警
9. 告警通知，例如短信、邮件、站内信
```


## M1-05：智能矿山工单闭环

本阶段新增智能矿山工单闭环能力，包括告警转工单、工单处理、工单关闭、工单状态流转和告警事件 handle_status 联动更新。

M1-04 已经可以根据传感器数据生成告警事件，但告警事件还没有人工处置闭环。M1-05 新增 mine_work_order 工单表和工单接口，让告警事件可以进入处理流程，形成“传感器数据 -> 告警事件 -> 工单处理 -> 工单关闭”的业务闭环。

本阶段新增数据库表 mine_work_order。新增后端路径包括 cn.sxu.enterprise.module.mine.entity.MineWorkOrder、cn.sxu.enterprise.module.mine.mapper.MineWorkOrderMapper、cn.sxu.enterprise.module.mine.service.MineWorkOrderService、cn.sxu.enterprise.module.mine.service.impl.MineWorkOrderServiceImpl、cn.sxu.enterprise.module.mine.controller.MineWorkOrderController、cn.sxu.enterprise.module.mine.dto.MineWorkOrderCreateRequest、cn.sxu.enterprise.module.mine.dto.MineWorkOrderHandleRequest、cn.sxu.enterprise.module.mine.dto.MineWorkOrderCloseRequest、cn.sxu.enterprise.module.mine.vo.MineWorkOrderPageQuery、cn.sxu.enterprise.module.mine.vo.MineWorkOrderPageVO。

本阶段新增接口包括 GET /api/mine/work-orders/page、POST /api/mine/work-orders/create-from-alarm、POST /api/mine/work-orders/{id}/handle、POST /api/mine/work-orders/{id}/close。接口继续使用 JWT 认证、ApiResult 统一返回、PageResult 分页结构和 @OperLog 操作日志。

本阶段使用的技术包括 Spring Boot Controller、Spring Service、MyBatis-Plus BaseMapper、MyBatis-Plus Page、LambdaQueryWrapper、Spring @Transactional、JWT 认证、SecurityContext 当前登录用户、ApiResult、PageResult 和 @OperLog 操作日志。

核心业务规则包括：一个告警事件只能生成一个工单；mine_work_order.alarm_event_id 使用唯一约束防止重复转工单；创建工单后，mine_alarm_event.handle_status 更新为 1；处理工单后，mine_work_order.order_status 更新为 2；关闭工单后，mine_work_order.order_status 更新为 3；关闭工单后，mine_alarm_event.handle_status 更新为 2。

可以写进简历的一句话：实现智能矿山告警工单闭环模块，支持告警事件转工单、工单处理、工单关闭和状态联动更新，并基于 MyBatis-Plus、JWT、操作日志和事务机制保证接口安全性、可追踪性和业务一致性。

面试时可以这样解释：项目中传感器数据触发告警后，会先生成告警事件。为了让告警不是停留在展示层，我设计了 mine_work_order 工单表，通过 alarm_event_id 和告警事件关联。告警转工单时用唯一约束避免重复生成；工单处理和关闭时会同步更新告警事件 handle_status，并使用 @Transactional 保证工单和告警事件状态的一致性。

后续增强方向包括：增加工单指派、增加工单附件、增加工单处理图片上传、增加前端工单管理页面、增加工单超时统计、增加短信邮件站内信通知，后续也可以扩展简化版流程审批。

## M1-06：智能矿山看板

本阶段新增智能矿山看板能力。

新增技术：

- ECharts
- Vue3 看板页面
- Element Plus 表格和按钮
- MyBatis-Plus 统计查询
- QueryWrapper 分组统计
- LambdaQueryWrapper 最近数据查询

解决的问题：

M1-06 将 M1-02 到 M1-05 已经完成的设备、传感器、传感器数据、告警事件和工单闭环能力可视化展示出来，让项目具备可演示的智能矿山业务看板。

后端文件路径：

- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/controller/MineDashboardController.java
- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/MineDashboardService.java
- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/service/impl/MineDashboardServiceImpl.java
- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineDashboardSummaryVO.java
- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineAlarmLevelStatVO.java
- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineSensorTypeStatVO.java
- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineWorkOrderStatusStatVO.java
- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineRecentAlarmVO.java
- scaffold-backend/src/main/java/cn/sxu/enterprise/module/mine/vo/MineRecentWorkOrderVO.java

前端文件路径：

- scaffold-frontend/src/api/mine/dashboard.ts
- scaffold-frontend/src/views/mine/MineDashboardView.vue
- scaffold-frontend/src/router/index.ts

数据库表：

- mine_device
- mine_sensor
- mine_sensor_data
- mine_alarm_rule
- mine_alarm_event
- mine_work_order

接口路径：

- GET /api/mine/dashboard/summary
- GET /api/mine/dashboard/alarm-level-stats
- GET /api/mine/dashboard/sensor-type-stats
- GET /api/mine/dashboard/work-order-status-stats
- GET /api/mine/dashboard/recent-alarms
- GET /api/mine/dashboard/recent-work-orders

验证方式：

- 后端执行 mvn clean package -DskipTests
- 前端执行 pnpm build
- 浏览器访问 http://localhost:5173/mine/dashboard

简历表达：

基于 Spring Boot 3、MyBatis-Plus、Vue3、Element Plus 和 ECharts 实现智能矿山安全生产看板，完成设备、传感器、告警事件、工单状态等核心指标统计，并支持最近告警和最近工单展示，形成从传感器模拟数据到告警事件再到工单闭环的可视化业务链路。

面试解释：

M1-06 的核心是把后端业务闭环通过统计接口和图表页面展示出来。后端通过 MyBatis-Plus 查询已有业务表，返回统一 ApiResult；前端通过 Axios 调用接口，用 Element Plus 展示统计卡片和表格，用 ECharts 展示分布图。

后续增强：

- M1-07 接入 MQTT / EMQX，实现传感器数据实时上报
- 后续可引入 WebSocket 实现看板实时刷新
- 后续可接入 Prometheus / Grafana 做系统监控


## M1-07：MQTT / EMQX 消息接入

M1-07 新增智能矿山 MQTT 数据接入能力，使平台具备接收传感器实时上报消息的基础能力。

本阶段新增技术点包括：

```text
MQTT
EMQX
Spring Integration MQTT
Eclipse Paho MQTT Client
Topic 订阅
JSON 消息解析
MQTT 消息落库
告警规则复用
Docker Compose 新增中间件服务
```

固定 MQTT Topic 为：

```text
mine/sensor/data
```

新增后端配置项包括：

```text
enterprise.mine.mqtt.enabled
enterprise.mine.mqtt.broker-url
enterprise.mine.mqtt.client-id
enterprise.mine.mqtt.username
enterprise.mine.mqtt.password
enterprise.mine.mqtt.topic
enterprise.mine.mqtt.qos
enterprise.mine.mqtt.completion-timeout
```

新增后端文件包括：

```text
cn.sxu.enterprise.module.mine.config.MineMqttProperties
cn.sxu.enterprise.module.mine.config.MineMqttConfig
cn.sxu.enterprise.module.mine.dto.MineSensorMqttMessage
cn.sxu.enterprise.module.mine.listener.MineSensorMqttListener
cn.sxu.enterprise.module.mine.controller.MineMqttController
```

新增接口为：

```text
POST /api/mine/mqtt/simulate-publish
```

M1-07 数据处理链路为：

```text
1. EMQX 接收 MQTT 消息
2. 后端订阅 mine/sensor/data
3. 后端将 JSON 解析为 MineSensorMqttMessage
4. 根据 sensorCode 查询 mine_sensor
5. 写入 mine_sensor_data
6. 更新 mine_sensor.last_report_time
7. 调用 MineAlarmEventService.generate(...)
8. 根据已有告警规则生成 mine_alarm_event
9. M1-06 看板继续统计最新数据
```

M1-07 简历表达：

```text
接入 EMQX 作为 MQTT Broker，基于 Spring Integration MQTT 实现智能矿山传感器数据订阅、解析、落库和告警联动，完成从设备消息上报到业务告警生成的端到端数据链路。
```

