<!--<template>-->
<!--  <div class="dashboard-page">-->
<!--    <el-container class="layout">-->
<!--      <el-header class="header">-->
<!--        <div class="logo">Enterprise Scaffold</div>-->

<!--        <div class="user-area">-->
<!--          <span v-if="authStore.user">-->
<!--            当前用户：{{ authStore.user.nickname }}（{{ authStore.user.username }}）-->
<!--          </span>-->

<!--          <el-button type="danger" plain @click="handleLogout">-->
<!--            退出登录-->
<!--          </el-button>-->
<!--        </div>-->
<!--      </el-header>-->

<!--      <el-main class="main">-->
<!--        <el-card>-->
<!--          <template #header>-->
<!--            <span>S0-07 前端初始化完成</span>-->
<!--          </template>-->

<!--          <el-descriptions :column="1" border>-->
<!--            <el-descriptions-item label="项目名称">-->
<!--              Enterprise Scaffold-->
<!--            </el-descriptions-item>-->

<!--            <el-descriptions-item label="当前阶段">-->
<!--              S0-07：初始化 Vue3 前端-->
<!--            </el-descriptions-item>-->

<!--            <el-descriptions-item label="前端技术">-->
<!--              Vue3 + Vite + TypeScript + Element Plus-->
<!--            </el-descriptions-item>-->

<!--            <el-descriptions-item label="后端接口">-->
<!--              POST /api/auth/login，GET /api/auth/me-->
<!--            </el-descriptions-item>-->

<!--            <el-descriptions-item label="登录状态">-->
<!--              已登录-->
<!--            </el-descriptions-item>-->
<!--          </el-descriptions>-->
<!--        </el-card>-->
<!--      </el-main>-->
<!--    </el-container>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--import { onMounted } from 'vue'-->
<!--import { useRouter } from 'vue-router'-->
<!--import { useAuthStore } from '../../stores/auth'-->

<!--const router = useRouter()-->
<!--const authStore = useAuthStore()-->

<!--onMounted(async () => {-->
<!--  if (!authStore.user) {-->
<!--    try {-->
<!--      await authStore.loadMe()-->
<!--    } catch {-->
<!--      authStore.logout()-->
<!--      await router.push('/login')-->
<!--    }-->
<!--  }-->
<!--})-->

<!--async function handleLogout() {-->
<!--  authStore.logout()-->
<!--  await router.push('/login')-->
<!--}-->
<!--</script>-->

<!--<style scoped>-->
<!--.dashboard-page {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--}-->

<!--.layout {-->
<!--  min-height: 100vh;-->
<!--}-->

<!--.header {-->
<!--  height: 60px;-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: space-between;-->
<!--  background: #1f2d3d;-->
<!--  color: #ffffff;-->
<!--}-->

<!--.logo {-->
<!--  font-size: 20px;-->
<!--  font-weight: 700;-->
<!--}-->

<!--.user-area {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  gap: 16px;-->
<!--}-->

<!--.main {-->
<!--  padding: 24px;-->
<!--}-->
<!--</style>-->

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const userText = computed(() => {
  if (!authStore.user) {
    return '未登录'
  }
  return `${authStore.user.nickname}（${authStore.user.username}）`
})

const mineEntries = [
  {
    title: '智能矿山综合看板',
    path: '/mine/dashboard',
    stage: 'M1-06 / M1-09',
    desc: '展示设备、传感器、采集数据、告警事件、工单状态和最近实时数据。'
  },
  {
    title: '设备健康评分',
    path: '/mine/device-health',
    stage: 'M1-10',
    desc: '根据告警、工单、传感器状态实时计算设备健康分和风险等级。'
  },
  {
    title: '预测性维护任务',
    path: '/mine/maintenance-tasks',
    stage: 'M1-11',
    desc: '基于设备健康风险生成维护任务，并完成安排、处理、关闭闭环。'
  },
  {
    title: '维护看板与风险趋势',
    path: '/mine/maintenance-dashboard',
    stage: 'M1-12',
    desc: '展示维护任务统计、优先级分布、风险等级分布和最近 7 天风险趋势。'
  }
]

const goPage = (path: string) => {
  router.push(path)
}

const logout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="dashboard-page">
    <el-card class="welcome-card" shadow="never">
      <div class="welcome-content">
        <div>
          <h2>Enterprise Scaffold</h2>
          <p>当前用户：{{ userText }}</p>
          <p class="sub-title">
            面向央国企信息化场景的企业级后台脚手架，当前已完成项目一智能矿山模块。
          </p>
        </div>

        <div class="welcome-actions">
          <el-button type="primary" @click="goPage('/mine/dashboard')">
            进入智能矿山看板
          </el-button>
          <el-button @click="logout">
            退出登录
          </el-button>
        </div>
      </div>
    </el-card>

    <el-card class="project-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span>项目一：智能矿山安全生产与设备预测性维护平台</span>
          <el-tag type="success">M1 已进入总体验收</el-tag>
        </div>
      </template>

      <div class="entry-grid">
        <div
            v-for="entry in mineEntries"
            :key="entry.path"
            class="entry-card"
            @click="goPage(entry.path)"
        >
          <div class="entry-title">
            {{ entry.title }}
          </div>
          <div class="entry-stage">
            {{ entry.stage }}
          </div>
          <div class="entry-desc">
            {{ entry.desc }}
          </div>
          <el-button class="entry-button" type="primary" plain>
            进入页面
          </el-button>
        </div>
      </div>
    </el-card>

    <el-card class="summary-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span>项目一完整业务链路</span>
        </div>
      </template>

      <div class="chain-box">
        设备台账 → 传感器台账 → MQTT 数据上报 → 传感器数据入库 →
        告警事件生成 → 工单闭环 → 设备健康评分 → 预测性维护任务 →
        维护看板与风险趋势分析
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  padding: 24px;
  background: #f5f7fa;
}

.welcome-card,
.project-card,
.summary-card {
  margin-bottom: 16px;
}

.welcome-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.welcome-content h2 {
  margin: 0 0 8px;
  font-size: 24px;
  color: #1f2d3d;
}

.welcome-content p {
  margin: 0 0 6px;
  color: #606266;
}

.sub-title {
  font-size: 14px;
}

.welcome-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  font-weight: 600;
}

.entry-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
}

.entry-card {
  min-height: 170px;
  padding: 18px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #ffffff;
  cursor: pointer;
  transition: all 0.2s ease;
}

.entry-card:hover {
  border-color: #409eff;
  box-shadow: 0 8px 20px rgb(0 0 0 / 8%);
  transform: translateY(-2px);
}

.entry-title {
  margin-bottom: 8px;
  font-size: 17px;
  font-weight: 600;
  color: #303133;
}

.entry-stage {
  margin-bottom: 10px;
  font-size: 13px;
  color: #409eff;
}

.entry-desc {
  min-height: 48px;
  margin-bottom: 16px;
  line-height: 1.6;
  color: #606266;
}

.entry-button {
  width: 100%;
}

.chain-box {
  line-height: 1.8;
  color: #303133;
}

@media (max-width: 768px) {
  .dashboard-page {
    padding: 12px;
  }

  .welcome-content {
    align-items: flex-start;
    flex-direction: column;
  }

  .welcome-actions {
    width: 100%;
  }

  .welcome-actions .el-button {
    flex: 1;
  }
}
</style>