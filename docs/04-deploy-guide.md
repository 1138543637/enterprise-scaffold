## S0-07 前端本地开发与启动

S0-07 完成 Vue3 前端初始化，并实现前端登录页面与后端 JWT 登录接口联调。

### 1. 当前前后端目录

项目根目录：

```text
D:\Code\enterprise-scaffold
```

后端目录：

```text
D:\Code\enterprise-scaffold\scaffold-backend
```

前端目录：

```text
D:\Code\enterprise-scaffold\scaffold-frontend
```

---

### 2. 前端技术栈

```text
Vue3
Vite
TypeScript
Vue Router
Pinia
Axios
Element Plus
pnpm
```

---

### 3. 初始化前端项目

进入项目根目录：

```cmd
cd /d D:\Code\enterprise-scaffold
```

删除空目录占位文件：

```cmd
if exist scaffold-frontend\.gitkeep del scaffold-frontend\.gitkeep
```

进入前端目录：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-frontend
```

创建 Vue3 + TypeScript 项目：

```cmd
pnpm create vite@latest . -- --template vue-ts
```

如果命令进入交互选择界面，选择：

```text
Framework: Vue
Variant: TypeScript
```

安装依赖：

```cmd
pnpm install
```

安装项目需要的前端依赖：

```cmd
pnpm add vue-router pinia axios element-plus
```

---

### 4. 前端开发代理配置

文件路径：

```text
D:\Code\enterprise-scaffold\scaffold-frontend\vite.config.ts
```

当前开发环境代理配置：

```ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0',
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

代理含义：

```text
前端访问：
http://localhost:5173/api/auth/login

会被 Vite 转发到：
http://localhost:8080/api/auth/login
```

这样本地开发时前端不需要单独处理跨域问题。

---

### 5. 启动后端

打开第一个 CMD 窗口。

进入后端目录：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
```

设置环境变量：

```cmd
set MYSQL_PASSWORD=你的MySQL密码
set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32
```

启动后端：

```cmd
mvn spring-boot:run
```

后端启动成功后不要关闭这个窗口。

后端地址：

```text
http://localhost:8080
```

健康检查地址：

```text
http://localhost:8080/api/health
```

数据库健康检查地址：

```text
http://localhost:8080/api/health/db
```

后端启动成功标志：

```text
Tomcat started on port 8080
Started ScaffoldBackendApplication
```

---

### 6. 启动前端

打开第二个 CMD 窗口。

进入前端目录：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-frontend
```

启动前端：

```cmd
pnpm dev
```

前端启动成功后会显示类似：

```text
Local: http://localhost:5173/
```

浏览器访问：

```text
http://localhost:5173
```

---

### 7. 前端页面访问路径

登录页：

```text
http://localhost:5173/login
```

登录后首页：

```text
http://localhost:5173/dashboard
```

默认账号：

```text
username: admin
password: admin123
```

---

### 8. 前端构建检查

进入前端目录：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-frontend
```

执行构建：

```cmd
pnpm build
```

构建成功后，会生成：

```text
scaffold-frontend/dist
```

本阶段只是本地开发和构建检查，暂时不部署 `dist` 目录。

---

### 9. S0-07 验收标准

S0-07 通过标准：

```text
1. scaffold-frontend 目录下存在 package.json
2. scaffold-frontend 目录下存在 vite.config.ts
3. scaffold-frontend/src/main.ts 已引入 Element Plus、Pinia、Router
4. scaffold-frontend/src/api/request.ts 已封装 axios
5. scaffold-frontend/src/api/system/auth.ts 已封装登录和当前用户接口
6. scaffold-frontend/src/stores/auth.ts 已保存 token 和用户信息
7. scaffold-frontend/src/router/index.ts 已配置 /login 和 /dashboard
8. scaffold-frontend/src/views/login/LoginView.vue 登录页可打开
9. scaffold-frontend/src/views/dashboard/DashboardView.vue 登录后首页可打开
10. pnpm dev 能启动
11. pnpm build 能成功
12. 浏览器访问 http://localhost:5173 能看到登录页
13. 使用 admin / admin123 能登录成功
14. 登录成功后能跳转到 /dashboard
15. 首页能显示当前用户：系统管理员（admin）
16. 点击退出登录能回到 /login
```

---

### 10. 常见问题

#### 10.1 前端提示后端服务异常

先检查后端是否启动：

```text
http://localhost:8080/api/health
```

如果后端没启动，先启动后端：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
set MYSQL_PASSWORD=你的MySQL密码
set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32
mvn spring-boot:run
```

---

#### 10.2 登录失败

先用 Apifox 测试后端登录接口：

```text
POST http://localhost:8080/api/auth/login
```

请求体：

```json
{
  "username": "admin",
  "password": "admin123"
}
```

如果 Apifox 也登录失败，说明问题在后端账号、密码哈希或数据库数据。

如果 Apifox 登录成功，但前端登录失败，优先检查：

```text
scaffold-frontend/vite.config.ts
scaffold-frontend/src/api/request.ts
scaffold-frontend/src/api/system/auth.ts
```

---

#### 10.3 页面空白

打开浏览器开发者工具：

```text
F12
Console
```

查看红色报错。

常见原因：

```text
1. 文件路径写错
2. import 路径写错
3. 组件文件名大小写不一致
4. pnpm install 没执行
5. Element Plus 没安装
6. Vue Router 没安装
7. Pinia 没安装
```

---

#### 10.4 5173 端口被占用

如果看到：

```text
Port 5173 is already in use
```

先关闭之前启动前端的 CMD 窗口。

如果仍然不行，可以在任务管理器中结束 Node.js 进程，然后重新执行：

```cmd
pnpm dev
```

---

### 11. S0-07 Git 提交

确认前端能启动、能登录、能构建后，在项目根目录执行：

```cmd
cd /d D:\Code\enterprise-scaffold

git status

git add .

git commit -m "feat: init vue3 frontend scaffold"

git push
```
## S0-08 日志功能本地验证

### 启动后端

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
set MYSQL_PASSWORD=你的MySQL密码
set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32
mvn spring-boot:run
```

## 本地启动脚本

项目新增 3 个 Windows 批处理启动脚本：

- scripts/start-mysql.bat：检查并启动 MySQL，默认检查 3306 端口
- scripts/start-backend.bat：检查 MySQL、8080 端口、Maven 后启动 Spring Boot 后端
- scripts/start-frontend.bat：检查 Node、pnpm、5173 端口后启动 Vue3 前端

推荐启动顺序：

1. start-mysql.bat
2. start-backend.bat
3. start-frontend.bat

## S0-10：Docker Compose 一键部署指南

### 1. 前提条件

本地需要已安装并启动：

- Docker Desktop
- Docker Compose
- Git

检查命令：

```cmd
docker --version
docker compose version
```

## M1-01：智能矿山模块部署与验证

### 本阶段部署变化

M1-01 不新增 Docker Compose 服务，不新增环境变量，不新增端口，不修改 Nginx 配置。

继续使用 S0-10 已完成的部署方式：

- MySQL 容器：`enterprise-scaffold-mysql`
- 后端容器：`enterprise-scaffold-backend`
- 前端容器：`enterprise-scaffold-frontend`

端口继续保持：

- MySQL：`3306`
- 后端：`8080`
- 前端：`5173 -> 容器内 80`

### 本地后端启动

执行目录：

`D:\Code\enterprise-scaffold\scaffold-backend`

启动命令：

`cd /d D:\Code\enterprise-scaffold\scaffold-backend`

`set MYSQL_PASSWORD=你的MySQL密码`

`set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32`

`set LOCAL_UPLOAD_PATH=D:\Code\enterprise-scaffold\uploads`

`mvn spring-boot:run`

### 本地接口验证

登录接口：

`POST http://localhost:8080/api/auth/login`

智能矿山模块健康检查：

`GET http://localhost:8080/api/mine/health`

请求头：

`Authorization: Bearer <token>`

预期返回：

{
"code": 0,
"msg": "success",
"data": "enterprise-scaffold mine module running"
}

### Docker Compose 重新构建

执行目录：

`D:\Code\enterprise-scaffold\scaffold-docker`

启动并重新构建：

`docker compose --env-file .env up -d --build`

查看状态：

`docker compose ps`

查看后端日志：

`docker compose logs -f enterprise-scaffold-backend`

### Docker 环境接口验证

后端直连：

`GET http://localhost:8080/api/mine/health`

前端 Nginx 代理：

`GET http://localhost:5173/api/mine/health`

以上两个地址都需要请求头：

`Authorization: Bearer <token>`

## M1-02：数据库升级

执行：
mysql -u root -p < scaffold-sql/m1_02_mine_device_sensor.sql

本阶段不改：
- Docker
- 端口
- Nginx
- 环境变量

接口：
/api/mine/devices/page
/api/mine/sensors/page
## M1-03 数据库升级说明

M1-03 新增 SQL 文件：

scaffold-sql/m1_03_mine_sensor_data.sql

如果是本地 MySQL，执行：

cd /d D:\Code\enterprise-scaffold
mysql -u root -p < scaffold-sql\m1_03_mine_sensor_data.sql

如果是 Docker Compose 已经启动过的 MySQL，因为 Docker volume 已经存在，enterprise_scaffold_init.sql 不会自动重新执行，需要手动执行 M1-03 SQL：

cd /d D:\Code\enterprise-scaffold
mysql -h 127.0.0.1 -P 3306 -u root -p < scaffold-sql\m1_03_mine_sensor_data.sql

或者：

docker exec -i enterprise-scaffold-mysql mysql -u root -p enterprise_scaffold < scaffold-sql\m1_03_mine_sensor_data.sql

执行完成后重启后端：

cd /d D:\Code\enterprise-scaffold\scaffold-docker
docker compose restart enterprise-scaffold-backend

M1-03 验收接口：

POST http://localhost:8080/api/mine/sensor-data/simulate
GET  http://localhost:8080/api/mine/sensor-data/latest
GET  http://localhost:8080/api/mine/sensor-data/page?pageNo=1&pageSize=10

以上接口均需要：

Authorization: Bearer <token>

## M1-04：告警规则和告警事件部署说明

M1-04 新增数据库表和后端接口，不新增前端页面，不新增 Docker 服务，不修改端口，不新增环境变量。

### 1. 新增 SQL 文件

SQL 文件路径：

```text
scaffold-sql/m1_04_mine_alarm_rule_event.sql
```

该 SQL 文件第一行必须是：

```sql
SET NAMES utf8mb4;
```

固定开头：

```sql
SET NAMES utf8mb4;

USE enterprise_scaffold;
```

### 2. 本地 MySQL 执行 SQL

命令前提：

```text
1. MySQL 已启动
2. enterprise_scaffold 数据库已存在
3. 当前目录是 D:\Code\enterprise-scaffold
4. 已经创建 scaffold-sql\m1_04_mine_alarm_rule_event.sql
```

执行目录：

```cmd
cd /d D:\Code\enterprise-scaffold
```

执行命令：

```cmd
mysql -u root -p < scaffold-sql\m1_04_mine_alarm_rule_event.sql
```

执行后检查：

```cmd
mysql -u root -p enterprise_scaffold
```

进入 MySQL 后执行：

```sql
SHOW TABLES LIKE 'mine_alarm%';

SELECT * FROM mine_alarm_rule;

SELECT COUNT(*) FROM mine_alarm_event;
```

预期：

```text
能看到 mine_alarm_rule
能看到 mine_alarm_event
mine_alarm_rule 至少有 3 条初始化规则
mine_alarm_event 初始可以是 0 条
```

### 3. Docker MySQL 已启动时执行 SQL

命令前提：

```text
1. Docker Desktop 已启动
2. enterprise-scaffold-mysql 容器正在运行
3. 宿主机 3306 端口可以访问 MySQL
4. 当前目录是 D:\Code\enterprise-scaffold
```

执行目录：

```cmd
cd /d D:\Code\enterprise-scaffold
```

执行命令：

```cmd
mysql -h 127.0.0.1 -P 3306 -u root -p < scaffold-sql\m1_04_mine_alarm_rule_event.sql
```

### 4. 启动后端

执行目录：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
```

命令前提：

```text
1. MySQL 已启动
2. 已执行 m1_04_mine_alarm_rule_event.sql
3. MYSQL_PASSWORD 要改成自己的 MySQL 密码
4. LOCAL_UPLOAD_PATH 保持固定路径 D:\Code\enterprise-scaffold\uploads
```

设置环境变量：

```cmd
set MYSQL_PASSWORD=你的MySQL密码
set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32
set LOCAL_UPLOAD_PATH=D:\Code\enterprise-scaffold\uploads
```

启动：

```cmd
mvn spring-boot:run
```

预期：

```text
后端启动成功
端口仍然是 8080
不出现 Java 编译错误
不出现 Mapper 找不到错误
不出现 SQL 表不存在错误
```

### 5. 获取 token

请求地址：

```text
POST http://localhost:8080/api/auth/login
```

请求头：

```text
Content-Type: application/json
```

Body：

```json
{
  "username": "admin",
  "password": "admin123"
}
```

成功后复制：

```text
data.token
```

后续请求头固定使用：

```text
Authorization: Bearer <token>
```

### 6. M1-04 接口验收地址

告警规则分页：

```text
GET http://localhost:8080/api/mine/alarm-rules/page?pageNo=1&pageSize=10
```

生成传感器数据：

```text
POST http://localhost:8080/api/mine/sensor-data/simulate
```

生成告警事件：

```text
POST http://localhost:8080/api/mine/alarm-events/generate
```

告警事件分页：

```text
GET http://localhost:8080/api/mine/alarm-events/page?pageNo=1&pageSize=10
```

### 7. 操作日志验收

查询告警规则操作日志：

```text
GET http://localhost:8080/api/system/oper-logs/page?pageNo=1&pageSize=10&title=智能矿山-告警规则
```

查询告警事件操作日志：

```text
GET http://localhost:8080/api/system/oper-logs/page?pageNo=1&pageSize=10&title=智能矿山-告警事件
```

预期：

```text
records 中能看到对应接口访问记录
status = 0
requestMethod = GET 或 POST
```

### 8. 构建验收

后端构建：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
mvn clean package -DskipTests
```

预期：

```text
BUILD SUCCESS
```

前端本阶段没有修改，但可以继续执行构建：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-frontend
pnpm build
```

预期：

```text
构建成功
```

### 9. Docker Compose 说明

M1-04 不新增 Docker 服务，不修改 Docker Compose 配置。

固定容器名继续为：

```text
enterprise-scaffold-mysql
enterprise-scaffold-backend
enterprise-scaffold-frontend
```

固定端口继续为：

```text
MySQL：3306
后端：8080
前端：5173 -> 容器内 80
```

固定上传目录挂载继续为：

```text
../uploads:/app/uploads
```

固定环境变量继续为：

```text
MYSQL_PASSWORD
JWT_SECRET
LOCAL_UPLOAD_PATH
SPRING_DATASOURCE_URL
```


## M1-05 数据库升级和接口验收

M1-05 新增 SQL 文件 scaffold-sql/m1_05_mine_work_order.sql，用于创建 mine_work_order 工单表。该 SQL 文件第一行必须是 SET NAMES utf8mb4;，并且固定使用 USE enterprise_scaffold; 指定数据库。

本地 MySQL 执行方式：先进入项目根目录 D:\Code\enterprise-scaffold，然后执行 mysql -u root -p < scaffold-sql\m1_05_mine_work_order.sql。

如果 Docker MySQL 已经启动，可以在项目根目录 D:\Code\enterprise-scaffold 执行 mysql -h 127.0.0.1 -P 3306 -u root -p < scaffold-sql\m1_05_mine_work_order.sql。也可以执行 docker exec -i enterprise-scaffold-mysql mysql -u root -p enterprise_scaffold < scaffold-sql\m1_05_mine_work_order.sql。

注意：如果 Docker MySQL 数据卷已经存在，enterprise_scaffold_init.sql 不会自动重新执行。这种情况下必须手动执行 scaffold-sql/m1_05_mine_work_order.sql。

M1-05 后端启动方式保持不变。进入 D:\Code\enterprise-scaffold\scaffold-backend，设置 MYSQL_PASSWORD、JWT_SECRET、LOCAL_UPLOAD_PATH 环境变量后，执行 mvn spring-boot:run 启动后端。

M1-05 验收接口包括 GET http://localhost:8080/api/mine/work-orders/page?pageNo=1&pageSize=10、POST http://localhost:8080/api/mine/work-orders/create-from-alarm、POST http://localhost:8080/api/mine/work-orders/{id}/handle、POST http://localhost:8080/api/mine/work-orders/{id}/close。所有接口都需要请求头 Authorization: Bearer <token>。

验收流程为：先登录 POST http://localhost:8080/api/auth/login 获取 token；再调用 POST http://localhost:8080/api/mine/sensor-data/simulate 生成传感器数据；再调用 POST http://localhost:8080/api/mine/alarm-events/generate 生成告警事件；再查询 GET http://localhost:8080/api/mine/alarm-events/page?pageNo=1&pageSize=10 获取告警事件 id；然后调用 POST http://localhost:8080/api/mine/work-orders/create-from-alarm 生成工单；最后依次调用工单处理接口和工单关闭接口完成闭环。


## M1-06：智能矿山看板部署和验证

M1-06 不新增 Docker 服务，不新增环境变量，不修改 Docker Compose 配置。

后端启动方式保持不变：

cd /d D:\Code\enterprise-scaffold\scaffold-backend
set MYSQL_PASSWORD=你的MySQL密码
set JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32
set LOCAL_UPLOAD_PATH=D:\Code\enterprise-scaffold\uploads
mvn spring-boot:run

前端需要新增 ECharts 依赖：

cd /d D:\Code\enterprise-scaffold\scaffold-frontend
pnpm add echarts
pnpm dev

前端访问地址：

http://localhost:5173/mine/dashboard

后端验收接口：

GET http://localhost:8080/api/mine/dashboard/summary
GET http://localhost:8080/api/mine/dashboard/alarm-level-stats
GET http://localhost:8080/api/mine/dashboard/sensor-type-stats
GET http://localhost:8080/api/mine/dashboard/work-order-status-stats
GET http://localhost:8080/api/mine/dashboard/recent-alarms
GET http://localhost:8080/api/mine/dashboard/recent-work-orders

## M1-07：Docker Compose 接入 EMQX

M1-07 新增 EMQX 作为 MQTT Broker，用于接收智能矿山传感器上报消息。

Docker Compose 新增服务名和容器名均固定为：

```text
enterprise-scaffold-emqx
```

镜像使用：

```text
emqx/emqx:5.8.8
```

固定端口：

| 端口 | 用途 |
|---|---|
| 1883 | MQTT TCP 连接端口 |
| 18083 | EMQX Dashboard 管理端口 |

后端容器通过环境变量连接 EMQX。

固定配置为：

```yaml
MINE_MQTT_ENABLED: true
MINE_MQTT_BROKER_URL: tcp://enterprise-scaffold-emqx:1883
MINE_MQTT_CLIENT_ID: enterprise-scaffold-mine-subscriber
MINE_MQTT_TOPIC: mine/sensor/data
MINE_MQTT_QOS: 1
```

如果 `backend` 使用 `depends_on`，需要在原有 MySQL 健康检查依赖基础上增加 EMQX 依赖。

示例：

```yaml
depends_on:
  enterprise-scaffold-mysql:
    condition: service_healthy
  enterprise-scaffold-emqx:
    condition: service_started
```

启动命令在以下目录执行：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-docker
docker compose up -d --build
```

EMQX Dashboard 访问地址为：

```text
http://localhost:18083
```

默认登录账号：

```text
admin
```

默认登录密码：

```text
public
```

M1-07 验收标准：

```text
1. enterprise-scaffold-emqx 容器状态为 Up
2. 1883 和 18083 端口映射正常
3. 后端容器可以通过 tcp://enterprise-scaffold-emqx:1883 连接 EMQX
4. 调用 /api/mine/mqtt/simulate-publish 后，mine_sensor_data 新增记录
5. 超过阈值的数据可以生成 mine_alarm_event
6. M1-06 看板统计可以看到数据变化
```

## M1-08：MQTT 数据模拟增强部署说明

M1-08 不新增 Docker 服务，继续复用 M1-07 已新增的 EMQX 服务 `enterprise-scaffold-emqx`。

MQTT Topic 继续固定为 `mine/sensor/data`。

本阶段不需要修改 `.env.example`，不需要新增环境变量。

重新构建并启动时，在 `D:\Code\enterprise-scaffold\scaffold-docker` 目录执行 `docker compose --env-file .env up -d --build`。

启动后执行 `docker compose ps` 查看容器状态。

验收时需要确认以下容器均正常运行：

- `enterprise-scaffold-mysql`
- `enterprise-scaffold-backend`
- `enterprise-scaffold-frontend`
- `enterprise-scaffold-emqx`

后端验收接口：

- `POST http://localhost:8080/api/mine/mqtt/simulate-batch`
- `GET http://localhost:8080/api/mine/sensor-data/page?pageNo=1&pageSize=10&sensorType=GAS`
- `GET http://localhost:8080/api/mine/alarm-events/page?pageNo=1&pageSize=10&sensorType=GAS`
- `GET http://localhost:8080/api/mine/dashboard/summary`


## M1-09：实时数据展示增强部署说明

M1-09 不新增 Docker 服务，不新增环境变量，不新增 SQL 文件。本阶段修改前端页面和前端 API 文件，因此需要重新构建前端。

前端本地构建命令：先进入 `D:\Code\enterprise-scaffold\scaffold-frontend`，再执行 `pnpm build`。如果使用本地 Vite 开发模式，执行目录仍然是 `D:\Code\enterprise-scaffold\scaffold-frontend`，启动命令为 `pnpm dev`，浏览器访问 `http://localhost:5173/mine/dashboard`。

如果使用 Docker Compose 访问前端页面，需要重新构建前端容器。执行目录为 `D:\Code\enterprise-scaffold\scaffold-docker`，执行命令为 `docker compose --env-file .env up -d --build enterprise-scaffold-frontend`。如果希望后端和前端一起重建，可以在同一目录执行 `docker compose --env-file .env up -d --build`。

M1-09 验收地址为 `http://localhost:5173/mine/dashboard`。验收标准：页面可以正常打开；统计卡片显示真实数据；最近传感器数据表格能显示 `mine_sensor_data` 最新记录；点击“刷新看板”后数据刷新；打开“自动刷新”后每 5 秒刷新一次；点击“批量模拟 MQTT 数据”后调用 `POST /api/mine/mqtt/simulate-batch`；批量模拟成功后采集数据总数和最近传感器数据发生变化；离开页面后自动刷新定时器能正常清理；页面布局正常，统计卡片、图表、表格不会一列铺满。

## M1-10：设备健康评分部署和验收

M1-10 不新增 Docker 服务。

M1-10 不新增环境变量。

M1-10 不新增 SQL 文件。

后端编译：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
mvn -DskipTests compile
```

前端构建：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-frontend
pnpm build
```

Docker Compose 重建：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-docker
docker compose --env-file .env up -d --build
docker compose ps
```

接口验收：

```text
GET http://localhost:8080/api/mine/device-health/page?pageNo=1&pageSize=10
GET http://localhost:8080/api/mine/device-health/summary
```

前端验收：

```text
http://localhost:5173/mine/device-health
```

预期：

```text
1. 页面能显示设备健康汇总卡片
2. 页面能分页展示每台设备健康评分
3. 页面能展示健康、关注、风险、高危等级
4. 查询条件能按设备编码、设备名称、设备类型、区域、风险等级、设备状态筛选
5. 告警增多后设备健康分下降
6. 工单关闭后未关闭工单数量减少
```


## M1-11 部署与验收

1. 执行新增 SQL：
   cd /d D:\Code\enterprise-scaffold
   mysql -u root -p < scaffold-sql\m1_11_mine_maintenance_task.sql

如果 MySQL 在 Docker 容器中运行，可在 scaffold-docker 目录执行：
cd /d D:\Code\enterprise-scaffold\scaffold-docker
docker exec -i enterprise-scaffold-mysql mysql -u root -p enterprise_scaffold < ..\scaffold-sql\m1_11_mine_maintenance_task.sql

2. 后端编译：
   cd /d D:\Code\enterprise-scaffold\scaffold-backend
   mvn -DskipTests compile

3. 前端构建：
   cd /d D:\Code\enterprise-scaffold\scaffold-frontend
   pnpm build

4. Docker Compose 重建：
   cd /d D:\Code\enterprise-scaffold\scaffold-docker
   docker compose --env-file .env up -d --build

5. 接口验收：
   GET http://localhost:8080/api/mine/maintenance-tasks/page?pageNo=1&pageSize=10
   GET http://localhost:8080/api/mine/maintenance-tasks/summary
   POST http://localhost:8080/api/mine/maintenance-tasks/create-from-device-health
   POST http://localhost:8080/api/mine/maintenance-tasks/{id}/plan
   POST http://localhost:8080/api/mine/maintenance-tasks/{id}/handle
   POST http://localhost:8080/api/mine/maintenance-tasks/{id}/close

6. 前端验收：
   http://localhost:5173/mine/maintenance-tasks



## M1-12：维护看板与风险趋势分析部署验收

M1-12 不新增数据库表，不需要执行 SQL 文件。

后端编译：

```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-backend
mvn -DskipTests compile
```
前端构建：
```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-frontend
pnpm build
```
Docker Compose 重建：
```cmd
cd /d D:\Code\enterprise-scaffold\scaffold-docker
docker compose --env-file .env up -d --build
```
接口验收：
```cmd
GET http://localhost:8080/api/mine/maintenance-dashboard/summary
GET http://localhost:8080/api/mine/maintenance-dashboard/task-status-stats
GET http://localhost:8080/api/mine/maintenance-dashboard/priority-stats
GET http://localhost:8080/api/mine/maintenance-dashboard/risk-level-stats
GET http://localhost:8080/api/mine/maintenance-dashboard/risk-trend
GET http://localhost:8080/api/mine/maintenance-dashboard/recent-tasks
GET http://localhost:8080/api/mine/maintenance-dashboard/high-risk-devices
```
前端页面验收：
```cmd
http://localhost:5173/mine/maintenance-dashboard
```
预期：

-维护统计卡片正常展示
-风险趋势图正常展示
-任务状态、优先级、风险等级图正常展示
-高风险设备任务和最近维护任务表格正常展示
-页面布局使用 CSS Grid，桌面端一行多张卡片

## M1-13：项目一总体验收部署说明

M1-13 不新增 Docker 服务。

M1-13 不修改以下 Docker 文件：

- `scaffold-docker/docker-compose.yml`
- `scaffold-docker/.env.example`
- `scaffold-backend/Dockerfile`
- `scaffold-frontend/Dockerfile`
- `scaffold-frontend/nginx.conf`

Docker Compose 服务继续固定为：

- `enterprise-scaffold-mysql`
- `enterprise-scaffold-backend`
- `enterprise-scaffold-frontend`
- `enterprise-scaffold-emqx`

M1-13 即使不修改 Docker 配置，也必须进行 Docker Compose 验收。

### 后端编译验收

执行目录：

`D:\Code\enterprise-scaffold\scaffold-backend`

执行命令：

`mvn -DskipTests compile`

预期结果：

`BUILD SUCCESS`

### 前端构建验收

执行目录：

`D:\Code\enterprise-scaffold\scaffold-frontend`

执行命令：

`pnpm build`

预期结果：

- 构建成功。
- 没有 TypeScript 报错。
- 没有 `AxiosResponse<T>` 和 `T` 类型不匹配报错。

### Docker Compose 验收

执行目录：

`D:\Code\enterprise-scaffold\scaffold-docker`

启动命令：

`docker compose --env-file .env up -d --build`

查看容器命令：

`docker compose ps`

预期看到：

- `enterprise-scaffold-mysql`：running 或 healthy
- `enterprise-scaffold-backend`：running
- `enterprise-scaffold-frontend`：running
- `enterprise-scaffold-emqx`：running

### Docker 日志排错命令

后端日志：

`docker logs -f enterprise-scaffold-backend`

前端日志：

`docker logs -f enterprise-scaffold-frontend`

EMQX 日志：

`docker logs -f enterprise-scaffold-emqx`

### Docker 健康检查地址

后端健康检查：

`http://localhost:8080/api/health`

前端 Nginx 代理健康检查：

`http://localhost:5173/api/health`

预期返回：

- `code = 0`
- `msg = success`

### 前端页面验收地址

- `http://localhost:5173`
- `http://localhost:5173/dashboard`
- `http://localhost:5173/mine/dashboard`
- `http://localhost:5173/mine/device-health`
- `http://localhost:5173/mine/maintenance-tasks`
- `http://localhost:5173/mine/maintenance-dashboard`

### 页面加载失败排查顺序

如果页面提示加载失败，排查顺序固定为：

1. 先查 F12 Network。
2. 如果接口是 401，重新登录 `admin / admin123`。
3. 如果接口是 404，检查路径是否缺少 `/api`。
4. 如果接口 `code = 0` 但页面报错，检查前端 ApiResult 解包。
5. 如果接口是 500，执行 `docker logs -f enterprise-scaffold-backend`。
6. 如果前端还是旧页面，重新执行 `docker compose --env-file .env up -d --build enterprise-scaffold-frontend`。
7. 浏览器按 Ctrl + F5 强制刷新。








###########################################################################################################################
## A2-01 部署与验收说明

A2-01 不新增 Docker 服务，不修改 `docker-compose.yml`，但新增了后端代码，因此 Docker Compose 验收时必须重新构建后端镜像。

本地后端编译：

cd /d D:\Code\enterprise-scaffold\scaffold-backend
mvn -DskipTests compile

Docker Compose 重建：

cd /d D:\Code\enterprise-scaffold\scaffold-docker
docker compose --env-file .env up -d --build
docker compose ps
docker logs -f enterprise-scaffold-backend

验收接口：

GET http://localhost:8080/api/aiops/health

该接口需要 JWT。未登录访问应返回 401，带 token 访问应返回 `enterprise-scaffold aiops module running`。


## A2-02：AIOps 资源管理部署与验收

A2-02 新增 SQL 文件 `scaffold-sql/a2_02_aiops_resource.sql`，该 SQL 文件第一行必须是 `SET NAMES utf8mb4;`，第二段固定使用 `USE enterprise_scaffold;`。已有数据库需要手动执行该 SQL 文件。执行目录为 `D:\Code\enterprise-scaffold`，执行命令为 `mysql -u root -p < scaffold-sql\a2_02_aiops_resource.sql`。如果使用 Docker MySQL，可以执行 `mysql -h 127.0.0.1 -P 3306 -u root -p < scaffold-sql\a2_02_aiops_resource.sql`。同时必须将 `aiops_resource` 建表 SQL 追加到 `scaffold-sql/enterprise_scaffold_init.sql`，确保 Docker MySQL 空数据卷全新初始化时也能自动创建该表。

后端编译验收在 `D:\Code\enterprise-scaffold\scaffold-backend` 目录执行，命令为 `mvn -DskipTests compile`，预期结果为 `BUILD SUCCESS`。前端构建验收在 `D:\Code\enterprise-scaffold\scaffold-frontend` 目录执行，命令为 `pnpm build`，预期结果为构建成功。接口验收前先调用 `POST http://localhost:8080/api/auth/login` 使用 `admin / admin123` 登录获取 token，然后带请求头 `Authorization: Bearer <token>` 访问 `GET http://localhost:8080/api/aiops/resources/page?pageNo=1&pageSize=10`。预期返回 `code = 0`，`msg = success`，`data.records` 中能看到初始化资源数据。

A2-02 不新增 Docker 服务，不修改 `scaffold-docker/docker-compose.yml`，不修改 `.env.example`，不修改后端 Dockerfile，不修改前端 Dockerfile，也不修改 Nginx 配置。但是 A2-02 新增了后端 Java 代码和前端 Vue 页面，所以必须重新 build 后端和前端镜像。Docker Compose 验收执行目录为 `D:\Code\enterprise-scaffold\scaffold-docker`，执行命令为 `docker compose --env-file .env up -d --build`，然后执行 `docker compose ps` 查看容器状态，并执行 `docker logs -f enterprise-scaffold-backend` 查看后端日志。预期 `enterprise-scaffold-mysql`、`enterprise-scaffold-backend`、`enterprise-scaffold-frontend`、`enterprise-scaffold-emqx` 均正常运行。

前端页面验收地址为 `http://localhost:5173/aiops/resources`。预期页面可以正常打开，资源列表可以正常显示，查询条件可以正常筛选，F12 Network 中接口路径为 `/api/aiops/resources/page`，页面不出现 `undefined`，不提示“加载失败”。如果页面提示加载失败，先查 F12 Network，再查 ApiResult 解包，再查 `docker logs -f enterprise-scaffold-backend`。

## A2-03：指标采集与模拟数据部署与验收

A2-03 新增了后端代码、前端代码和 SQL 文件，不新增 Docker 服务，不修改 Docker Compose 配置，但必须重新 build 后端和前端镜像。

先在 `D:\Code\enterprise-scaffold` 目录执行 SQL：`mysql -u root -p < scaffold-sql\a2_03_aiops_metric_data.sql`。如果使用 Docker MySQL，可以执行：`mysql -h 127.0.0.1 -P 3306 -u root -p < scaffold-sql\a2_03_aiops_metric_data.sql`。

后端编译验收在 `D:\Code\enterprise-scaffold\scaffold-backend` 目录执行：`mvn -DskipTests compile`。前端构建验收在 `D:\Code\enterprise-scaffold\scaffold-frontend` 目录执行：`pnpm build`。

Docker Compose 验收不能省略，在 `D:\Code\enterprise-scaffold\scaffold-docker` 目录执行：`docker compose --env-file .env up -d --build`，然后执行 `docker compose ps`，并使用 `docker logs -f enterprise-scaffold-backend` 查看后端日志。预期容器包括 `enterprise-scaffold-mysql`、`enterprise-scaffold-backend`、`enterprise-scaffold-frontend`、`enterprise-scaffold-emqx`，均应正常运行。

接口验收地址包括 `GET http://localhost:8080/api/health`、`GET http://localhost:8080/api/health/db`、`POST http://localhost:8080/api/aiops/metric-data/simulate`、`GET http://localhost:8080/api/aiops/metric-data/latest`、`GET http://localhost:8080/api/aiops/metric-data/page?pageNo=1&pageSize=10`。AIOps 指标接口都需要 `Authorization: Bearer <token>`。

前端页面验收地址为 `http://localhost:5173/aiops/metrics`，页面应能展示最新指标数据、指标历史数据，支持筛选和模拟生成指标数据。

## A2-04：告警规则、告警事件、运维工单闭环部署验收

A2-04 新增 SQL 文件 `scaffold-sql/a2_04_aiops_alert_work_order.sql`，用于创建 `aiops_alert_rule`、`aiops_alert_event`、`aiops_work_order` 三张表并插入初始化告警规则。该 SQL 文件第一行必须为 `SET NAMES utf8mb4;`，第二段固定为 `USE enterprise_scaffold;`。

如果是已有数据库升级，需要在项目根目录 `D:\Code\enterprise-scaffold` 执行 `mysql -u root -p < scaffold-sql\a2_04_aiops_alert_work_order.sql`。如果使用 Docker MySQL，也可以执行 `mysql -h 127.0.0.1 -P 3306 -u root -p < scaffold-sql\a2_04_aiops_alert_work_order.sql`。

SQL 执行完成后，进入 MySQL 执行 `USE enterprise_scaffold;`，再执行 `SHOW TABLES LIKE 'aiops_alert_rule';`、`SHOW TABLES LIKE 'aiops_alert_event';`、`SHOW TABLES LIKE 'aiops_work_order';` 验证三张表是否存在。再执行 `SELECT rule_code, rule_name, resource_type, metric_type, threshold_value, alert_level FROM aiops_alert_rule;`，预期能看到 CPU、内存、磁盘、MySQL、Redis、网络等初始化告警规则。

后端编译验收在 `D:\Code\enterprise-scaffold\scaffold-backend` 目录执行 `mvn -DskipTests compile`，预期出现 `BUILD SUCCESS`。前端构建验收在 `D:\Code\enterprise-scaffold\scaffold-frontend` 目录执行 `pnpm build`，预期构建成功。

A2-04 不新增 Docker 服务，不修改 `scaffold-docker/docker-compose.yml`，不修改 `.env.example`，不修改后端 Dockerfile 和前端 Dockerfile。但是 A2-04 新增了后端代码、前端代码和数据库表，因此 Docker Compose 验收不能省略，必须在 `D:\Code\enterprise-scaffold\scaffold-docker` 目录执行 `docker compose --env-file .env up -d --build`，然后执行 `docker compose ps` 查看容器状态，最后执行 `docker logs -f enterprise-scaffold-backend` 查看后端日志。预期 `enterprise-scaffold-mysql`、`enterprise-scaffold-backend`、`enterprise-scaffold-frontend`、`enterprise-scaffold-emqx` 都处于 running 状态，其中 MySQL 应为 healthy。

接口验收需要先调用 `POST http://localhost:8080/api/auth/login` 使用 `admin / admin123` 登录获取 token，然后在请求头中加入 `Authorization: Bearer <token>`。需要验收的接口包括：`GET http://localhost:8080/api/aiops/alert-rules/page?pageNo=1&pageSize=10`、`POST http://localhost:8080/api/aiops/alert-events/generate`、`GET http://localhost:8080/api/aiops/alert-events/page?pageNo=1&pageSize=10`、`GET http://localhost:8080/api/aiops/work-orders/page?pageNo=1&pageSize=10`、`POST http://localhost:8080/api/aiops/work-orders/create-from-alert`、`POST http://localhost:8080/api/aiops/work-orders/{id}/handle`、`POST http://localhost:8080/api/aiops/work-orders/{id}/close`。

前端页面验收地址为 `http://localhost:5173/aiops/alerts` 和 `http://localhost:5173/aiops/work-orders`。预期告警中心页面能显示告警规则和告警事件，运维工单页面能显示工单列表，并支持告警转工单、工单处理和工单关闭。页面不应出现 `undefined`，不应提示“加载失败”，F12 Network 中接口路径必须带 `/api/aiops/**` 前缀。

如果页面提示“加载失败”，排查顺序固定为：先查 F12 Network，看请求路径是否为 `/api/aiops/**`；再看后端返回是否为 `code = 0`；如果返回 401，重新登录；如果返回 404，检查前端路径是否缺少 `/api` 或 Docker 是否未重新 build；如果返回 500，执行 `docker logs -f enterprise-scaffold-backend` 查看后端日志；如果后端返回正常但页面仍失败，检查前端 API 文件是否正确解包 `ApiResult`。

## A2-05：根因分析简化版部署与验收

A2-05 新增 SQL 文件 scaffold-sql/a2_05_aiops_root_cause.sql。

执行 SQL 前进入项目根目录 D:\Code\enterprise-scaffold。

本地 MySQL 执行命令：

mysql -u root -p < scaffold-sql\a2_05_aiops_root_cause.sql

如果使用 Docker MySQL，可以执行：

mysql -h 127.0.0.1 -P 3306 -u root -p < scaffold-sql\a2_05_aiops_root_cause.sql

SQL 执行后进入 MySQL，执行以下语句验收表是否创建成功：

USE enterprise_scaffold;
SHOW TABLES LIKE 'aiops_root_cause_record';
DESC aiops_root_cause_record;

后端编译命令：

cd /d D:\Code\enterprise-scaffold\scaffold-backend
mvn -DskipTests compile

预期结果：

BUILD SUCCESS

前端构建命令：

cd /d D:\Code\enterprise-scaffold\scaffold-frontend
pnpm build

预期结果：

构建成功

A2-05 不新增 Docker 服务，
也不修改 docker-compose.yml、.env.example、后端 Dockerfile、前端 Dockerfile 和 nginx.conf。

但是本阶段新增后端代码和前端代码，所以必须重新构建镜像。

Docker Compose 验收命令：

cd /d D:\Code\enterprise-scaffold\scaffold-docker
docker compose --env-file .env up -d --build
docker compose ps
docker logs -f enterprise-scaffold-backend

预期 enterprise-scaffold-mysql、enterprise-scaffold-backend、
enterprise-scaffold-frontend、enterprise-scaffold-emqx 均正常运行。

验收地址包括：

http://localhost:8080/api/health

http://localhost:8080/api/health/db

http://localhost:8080/api/aiops/root-causes/page?pageNo=1&pageSize=10

http://localhost:5173/aiops/root-causes

根因分析接口需要 JWT。

如果不带 token 返回 401，说明安全拦截正常。

如果带 token 返回 code = 0，说明接口正常。

前端页面不应出现 undefined，不应提示加载失败。

F12 Network 中接口路径必须是 /api/aiops/root-causes/**。

### A2-06：AIOps 综合看板部署与验收

A2-06 不新增 Docker 服务，不修改 `scaffold-docker/docker-compose.yml`，不修改 `scaffold-docker/.env.example`，不新增 Docker 环境变量，不新增 Docker volume。

Docker 服务仍固定为：

- `enterprise-scaffold-mysql`
- `enterprise-scaffold-backend`
- `enterprise-scaffold-frontend`
- `enterprise-scaffold-emqx`

但是本阶段新增了后端接口和前端页面，所以必须重新构建后端和前端镜像。

后端编译执行目录：

`D:\Code\enterprise-scaffold\scaffold-backend`

后端编译命令：

`mvn -DskipTests compile`

预期结果：

`BUILD SUCCESS`

前端构建执行目录：

`D:\Code\enterprise-scaffold\scaffold-frontend`

前端构建命令：

`pnpm build`

预期结果：

构建成功。

Docker Compose 验收执行目录：

`D:\Code\enterprise-scaffold\scaffold-docker`

Docker Compose 验收命令：

`docker compose --env-file .env up -d --build`

`docker compose ps`

`docker logs -f enterprise-scaffold-backend`

预期容器状态：

- `enterprise-scaffold-mysql` running / healthy
- `enterprise-scaffold-backend` running
- `enterprise-scaffold-frontend` running
- `enterprise-scaffold-emqx` running

接口验收需要先通过 `POST http://localhost:8080/api/auth/login` 使用 `admin / admin123` 登录获取 token。

登录后请求头携带：

`Authorization: Bearer <token>`

需要验收的接口包括：

- `GET http://localhost:8080/api/aiops/dashboard/summary`
- `GET http://localhost:8080/api/aiops/dashboard/resource-type-stats`
- `GET http://localhost:8080/api/aiops/dashboard/alert-level-stats`
- `GET http://localhost:8080/api/aiops/dashboard/work-order-status-stats`
- `GET http://localhost:8080/api/aiops/dashboard/metric-trend`
- `GET http://localhost:8080/api/aiops/dashboard/recent-alerts`
- `GET http://localhost:8080/api/aiops/dashboard/recent-work-orders`
- `GET http://localhost:8080/api/aiops/dashboard/recent-root-causes`

预期接口返回：

`code = 0`

`msg = success`

`data` 不为 null。

前端页面验收地址：

`http://localhost:5173/aiops/dashboard`

预期页面正常打开，统计卡片一行多张显示，ECharts 图表正常展示，最近告警事件、最近运维工单、最近根因分析表格正常展示，页面不出现 `undefined`，不提示 `AIOps 综合看板加载失败`。

如果页面加载失败，先查 F12 Network，再查前端 `ApiResult` 解包，再查 Docker 后端日志：

`docker logs -f enterprise-scaffold-backend`

## A2-07：Prometheus / Grafana Docker Compose 部署说明

A2-07 新增 Prometheus 和 Grafana Docker Compose 服务。

本阶段需要修改 `scaffold-backend/pom.xml`、`scaffold-backend/src/main/resources/application.yml`、`scaffold-backend/src/main/java/cn/sxu/enterprise/common/security/config/SecurityConfig.java`、`scaffold-docker/docker-compose.yml`、`scaffold-docker/.env.example` 和本地 `scaffold-docker/.env`，并新增 `scaffold-docker/prometheus/prometheus.yml`。

本阶段新增容器名固定为 `enterprise-scaffold-prometheus` 和 `enterprise-scaffold-grafana`。Prometheus 端口固定为 `9090`，Grafana 端口固定为 `3000`。新增 Docker volume 固定为 `enterprise-scaffold-prometheus-data` 和 `enterprise-scaffold-grafana-data`。

部署验收时，进入目录 `D:\Code\enterprise-scaffold\scaffold-docker`，执行 `docker compose --env-file .env up -d --build`，然后执行 `docker compose ps` 查看容器状态，最后执行 `docker logs -f enterprise-scaffold-backend` 查看后端日志。

预期容器包括 `enterprise-scaffold-mysql`、`enterprise-scaffold-backend`、`enterprise-scaffold-frontend`、`enterprise-scaffold-emqx`、`enterprise-scaffold-prometheus`、`enterprise-scaffold-grafana`。

验收地址包括 `http://localhost:8080/actuator/health`、`http://localhost:8080/actuator/prometheus`、`http://localhost:9090`、`http://localhost:3000`。

Prometheus 的 `Status -> Targets` 中 `enterprise-scaffold-backend` 应显示 `UP`。Grafana 使用 `.env` 中的 `GRAFANA_ADMIN_USER` 和 `GRAFANA_ADMIN_PASSWORD` 登录，并在 Explore 中选择 `enterprise-scaffold-prometheus` 数据源，查询 `up`、`up{job="enterprise-scaffold-backend"}`、`jvm_memory_used_bytes` 等指标验证监控链路。


## A2-08：A2 项目总体验收部署说明

A2-08 不新增 Docker 服务，不修改 `docker-compose.yml`，不修改 `.env.example`，不修改后端 Dockerfile，不修改前端 Dockerfile，不修改 Nginx 配置。但本阶段修改了前端首页 `/dashboard`，因此仍然必须重新构建前端镜像，并统一执行 Docker Compose 重建验收。

后端编译命令：进入 `D:\Code\enterprise-scaffold\scaffold-backend`，执行 `mvn -DskipTests compile`，预期结果是 `BUILD SUCCESS`。

前端构建命令：进入 `D:\Code\enterprise-scaffold\scaffold-frontend`，执行 `pnpm build`，预期结果是构建成功。

Docker Compose 验收命令：进入 `D:\Code\enterprise-scaffold\scaffold-docker`，依次执行 `docker compose --env-file .env up -d --build`、`docker compose ps`、`docker logs -f enterprise-scaffold-backend`。预期容器包括 `enterprise-scaffold-mysql`、`enterprise-scaffold-backend`、`enterprise-scaffold-frontend`、`enterprise-scaffold-emqx`、`enterprise-scaffold-prometheus`、`enterprise-scaffold-grafana`，并且都应处于 running 状态，其中 MySQL 应为 healthy。

A2-08 需要验收的页面包括 `http://localhost:5173/dashboard`、`http://localhost:5173/aiops/dashboard`、`http://localhost:5173/aiops/resources`、`http://localhost:5173/aiops/metrics`、`http://localhost:5173/aiops/alerts`、`http://localhost:5173/aiops/work-orders`、`http://localhost:5173/aiops/root-causes`、`http://localhost:9090`、`http://localhost:3000`。如果页面提示加载失败，先查 F12 Network，再查 ApiResult 解包，再查 `docker logs -f enterprise-scaffold-backend`。


















#######################################################################################################################################################################################


## R3-01 部署与验收说明

R3-01 新增了后端 Java 代码，因此需要重新编译后端并重建 enterprise-scaffold-backend 镜像。本阶段不新增 Docker 服务，不修改 docker-compose.yml，不修改 .env.example，不新增 volume，也不新增端口。后端本地编译命令为 cd /d D:\Code\enterprise-scaffold\scaffold-backend，然后执行 mvn -DskipTests compile。Docker Compose 验收仍然不能省略，必须执行 cd /d D:\Code\enterprise-scaffold\scaffold-docker，docker compose --env-file .env up -d --build，docker compose ps，docker logs -f enterprise-scaffold-backend。验收时确认 enterprise-scaffold-mysql、enterprise-scaffold-backend、enterprise-scaffold-frontend、enterprise-scaffold-emqx、enterprise-scaffold-prometheus、enterprise-scaffold-grafana 正常运行。R3-01 不新增 enterprise-scaffold-kafka，Kafka 将在 R3-05 接入。


### R3-02：部署与验收

R3-02 新增 SQL、后端代码和前端页面，不新增 Docker 服务，不修改 Docker Compose 配置。由于新增了后端 Java 代码和前端 Vue 页面，必须重建 `enterprise-scaffold-backend` 和 `enterprise-scaffold-frontend` 镜像。为了避免漏构建，统一执行完整 Docker Compose 重建。

先在 MySQL 中执行 `scaffold-sql/r3_02_risk_transaction.sql`，并确认 `enterprise_scaffold` 数据库中已经存在 `risk_transaction` 表。后端本地编译命令为 `cd /d D:\Code\enterprise-scaffold\scaffold-backend`，然后执行 `mvn -DskipTests compile`。前端本地构建命令为 `cd /d D:\Code\enterprise-scaffold\scaffold-frontend`，然后执行 `pnpm build`。

Docker Compose 验收固定执行：`cd /d D:\Code\enterprise-scaffold\scaffold-docker`，然后执行 `docker compose --env-file .env up -d --build`、`docker compose ps`、`docker logs -f enterprise-scaffold-backend`。预期 MySQL、后端、前端、EMQX、Prometheus、Grafana 容器正常运行。本阶段不应该新增 Kafka 容器。



