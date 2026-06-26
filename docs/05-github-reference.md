## S0-07 前端初始化参考

S0-07 初始化 Vue3 前端，参考了 Vue3 中后台项目的常见目录组织方式，但没有复制任何开源项目代码。

### 1. 本阶段参考项目

| 项目                | 地址                                              | 参考内容                              |
| ----------------- | ----------------------------------------------- | --------------------------------- |
| RuoYi-Vue3        | https://github.com/yangzongzhuan/RuoYi-Vue3     | Vue3、Vite、Element Plus 后台管理项目前端结构 |
| vue-element-admin | https://github.com/PanJiaChen/vue-element-admin | 后台管理系统登录页、布局、权限路由设计思路             |
| Vue Vben Admin    | https://github.com/vbenjs/vue-vben-admin        | Vue3 + TypeScript 中后台工程化目录设计      |
| Vite              | https://github.com/vitejs/vite                  | 前端项目构建工具和开发服务器                    |
| Vue Router        | https://github.com/vuejs/router                 | Vue3 官方路由方案                       |
| Pinia             | https://github.com/vuejs/pinia                  | Vue3 状态管理方案                       |
| Element Plus      | https://github.com/element-plus/element-plus    | Vue3 UI 组件库                       |
| Axios             | https://github.com/axios/axios                  | HTTP 请求封装                         |

---

### 2. 本阶段实际使用方式

本项目没有直接复制上述开源项目代码，而是只参考以下思路：

```text
1. 前端项目使用 Vue3 + Vite + TypeScript
2. 使用 Element Plus 搭建基础登录页和首页
3. 使用 Vue Router 管理 /login 和 /dashboard 页面
4. 使用 Pinia 保存 token 和当前用户信息
5. 使用 Axios 统一请求后端接口
6. 使用 Vite proxy 将 /api 请求转发到后端 http://localhost:8080
7. 前端目录按照 api、router、stores、types、views 分层
```

---

### 3. 本项目 S0-07 前端目录

```text
scaffold-frontend
├── index.html
├── package.json
├── pnpm-lock.yaml
├── tsconfig.json
├── tsconfig.app.json
├── tsconfig.node.json
├── vite.config.ts
└── src
    ├── api
    │   ├── request.ts
    │   └── system
    │       └── auth.ts
    ├── router
    │   └── index.ts
    ├── stores
    │   └── auth.ts
    ├── types
    │   └── api.ts
    ├── views
    │   ├── dashboard
    │   │   └── DashboardView.vue
    │   └── login
    │       └── LoginView.vue
    ├── App.vue
    ├── main.ts
    ├── style.css
    └── vite-env.d.ts
```

---

### 4. 本阶段没有照搬的内容

S0-07 没有照搬以下内容：

```text
1. 没有复制若依前端完整代码
2. 没有复制若依动态路由代码
3. 没有复制若依权限指令代码
4. 没有复制 Vben Admin 的复杂工程封装
5. 没有复制 vue-element-admin 的 Vue2 旧版实现
6. 没有引入复杂主题配置
7. 没有引入复杂菜单权限渲染
```

---

### 5. 后续可继续参考的方向

后续阶段可以逐步参考这些设计，但仍然要自己实现：

```text
1. 动态菜单渲染
2. 基于按钮权限的页面按钮显示 / 隐藏
3. 系统管理布局
4. 用户管理页面
5. 角色管理页面
6. 菜单管理页面
7. 登录过期处理
8. 前端权限指令
9. 后端权限注解
```
