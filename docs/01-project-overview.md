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