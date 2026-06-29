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

