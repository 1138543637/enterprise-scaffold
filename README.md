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