<template>
  <div class="dashboard-page">
    <el-container class="layout">
      <el-header class="header">
        <div class="logo">Enterprise Scaffold</div>

        <div class="user-area">
          <span v-if="authStore.user">
            当前用户：{{ authStore.user.nickname }}（{{ authStore.user.username }}）
          </span>

          <el-button type="danger" plain @click="handleLogout">
            退出登录
          </el-button>
        </div>
      </el-header>

      <el-main class="main">
        <el-card>
          <template #header>
            <span>S0-07 前端初始化完成</span>
          </template>

          <el-descriptions :column="1" border>
            <el-descriptions-item label="项目名称">
              Enterprise Scaffold
            </el-descriptions-item>

            <el-descriptions-item label="当前阶段">
              S0-07：初始化 Vue3 前端
            </el-descriptions-item>

            <el-descriptions-item label="前端技术">
              Vue3 + Vite + TypeScript + Element Plus
            </el-descriptions-item>

            <el-descriptions-item label="后端接口">
              POST /api/auth/login，GET /api/auth/me
            </el-descriptions-item>

            <el-descriptions-item label="登录状态">
              已登录
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

onMounted(async () => {
  if (!authStore.user) {
    try {
      await authStore.loadMe()
    } catch {
      authStore.logout()
      await router.push('/login')
    }
  }
})

async function handleLogout() {
  authStore.logout()
  await router.push('/login')
}
</script>

<style scoped>
.dashboard-page {
  width: 100%;
  height: 100%;
}

.layout {
  min-height: 100vh;
}

.header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #1f2d3d;
  color: #ffffff;
}

.logo {
  font-size: 20px;
  font-weight: 700;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 16px;
}

.main {
  padding: 24px;
}
</style>