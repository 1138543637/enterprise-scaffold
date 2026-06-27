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