<template>
  <div class="dashboard-page">
    <header class="dashboard-header">
      <div>
        <p class="page-subtitle">Enterprise Scaffold</p>
        <h1>企业级项目演示首页</h1>
        <p class="page-desc">
          当前脚手架已经完成系统基础能力、项目一智能矿山平台，并已推进项目二云网融合 AIOps 智能运维平台。
          首页展示顺序固定为：公共脚手架 -> 项目一 -> 项目二。
        </p>
      </div>

      <div class="user-panel">
        <div class="user-info">
          <span class="user-label">当前用户</span>
          <strong>{{ displayName }}</strong>
        </div>
        <el-button type="danger" plain @click="handleLogout">退出登录</el-button>
      </div>
    </header>

    <section class="summary-grid">
      <el-card shadow="hover" class="summary-card">
        <div class="summary-title">公共脚手架</div>
        <div class="summary-value">S0 已完成</div>
        <div class="summary-desc">登录认证、RBAC、日志、字典、文件上传、Docker Compose。</div>
      </el-card>

      <el-card shadow="hover" class="summary-card">
        <div class="summary-title">项目一</div>
        <div class="summary-value">M1 已完成</div>
        <div class="summary-desc">智能矿山安全生产与设备预测性维护平台。</div>
      </el-card>

      <el-card shadow="hover" class="summary-card">
        <div class="summary-title">项目二</div>
        <div class="summary-value">A2-08 收尾验收</div>
        <div class="summary-desc">云网融合 AIOps 智能运维平台。</div>
      </el-card>
    </section>

    <section class="project-section">
      <div class="section-title">
        <h2>项目一：智能矿山安全生产与设备预测性维护平台</h2>
        <p>
          项目一已经完成，应放在项目二上方，作为第一个完整项目演示入口。
        </p>
      </div>

      <div class="entry-grid">
        <el-card
          v-for="entry in mineEntries"
          :key="entry.path"
          shadow="hover"
          class="entry-card"
        >
          <div class="entry-content">
            <div>
              <div class="entry-title">{{ entry.title }}</div>
              <div class="entry-desc">{{ entry.desc }}</div>
            </div>
            <el-button type="primary" plain @click="goPage(entry.path)">进入</el-button>
          </div>
        </el-card>
      </div>
    </section>

    <section class="project-section">
      <div class="section-title">
        <h2>项目一验收链路</h2>
        <p>
          按下面顺序演示，能体现智能矿山从设备、传感器、MQTT、告警、工单到预测性维护的完整闭环。
        </p>
      </div>

      <div class="flow-grid">
        <div v-for="(item, index) in mineFlow" :key="item" class="flow-item">
          <span class="flow-index">{{ index + 1 }}</span>
          <span>{{ item }}</span>
        </div>
      </div>
    </section>

    <section class="project-section">
      <div class="section-title">
        <h2>项目二：云网融合 AIOps 智能运维平台</h2>
        <p>
          A2-08 收尾阶段用于验收资源台账、指标采集、告警事件、运维工单、根因分析、综合看板、Prometheus 和 Grafana。
        </p>
      </div>

      <div class="entry-grid">
        <el-card
          v-for="entry in aiopsEntries"
          :key="entry.path"
          shadow="hover"
          class="entry-card"
        >
          <div class="entry-content">
            <div>
              <div class="entry-title">{{ entry.title }}</div>
              <div class="entry-desc">{{ entry.desc }}</div>
            </div>
            <el-button type="primary" @click="goPage(entry.path)">进入</el-button>
          </div>
        </el-card>

        <el-card shadow="hover" class="entry-card monitor-card">
          <div class="entry-content">
            <div>
              <div class="entry-title">Prometheus</div>
              <div class="entry-desc">查看 Targets 是否 UP，验证后端 /actuator/prometheus 指标抓取。</div>
            </div>
            <el-button type="success" @click="openExternal('http://localhost:9090')">
              打开
            </el-button>
          </div>
        </el-card>

        <el-card shadow="hover" class="entry-card monitor-card">
          <div class="entry-content">
            <div>
              <div class="entry-title">Grafana</div>
              <div class="entry-desc">验证 Prometheus 数据源，使用 Explore 查询 up、jvm_memory_used_bytes 等指标。</div>
            </div>
            <el-button type="success" @click="openExternal('http://localhost:3000')">
              打开
            </el-button>
          </div>
        </el-card>
      </div>
    </section>

    <section class="project-section">
      <div class="section-title">
        <h2>项目二验收链路</h2>
        <p>
          按下面顺序演示，能体现 AIOps 从资源、指标、告警、工单、根因到监控组件的完整闭环。
        </p>
      </div>

      <div class="flow-grid">
        <div v-for="(item, index) in aiopsFlow" :key="item" class="flow-item">
          <span class="flow-index">{{ index + 1 }}</span>
          <span>{{ item }}</span>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const displayName = computed(() => {
  const user = authStore.user as { nickname?: string; username?: string } | null

  if (user?.nickname && user?.username) {
    return `${user.nickname}（${user.username}）`
  }

  if (user?.username) {
    return user.username
  }

  return '系统管理员（admin）'
})

const mineEntries = [
  {
    title: '智能矿山综合看板',
    desc: '设备、传感器、采集数据、告警、工单综合展示。',
    path: '/mine/dashboard'
  },
  {
    title: '设备健康评分',
    desc: '设备健康分、风险等级和异常识别。',
    path: '/mine/device-health'
  },
  {
    title: '预测性维护任务',
    desc: '维护任务生成、安排、处理、关闭。',
    path: '/mine/maintenance-tasks'
  },
  {
    title: '维护看板与风险趋势',
    desc: '维护任务统计和最近 7 天风险趋势。',
    path: '/mine/maintenance-dashboard'
  }
]

const aiopsEntries = [
  {
    title: 'AIOps 综合看板',
    desc: '资源、指标、告警、工单、根因分析综合统计展示。',
    path: '/aiops/dashboard'
  },
  {
    title: '资源管理',
    desc: '服务器、数据库、中间件、网络设备统一台账。',
    path: '/aiops/resources'
  },
  {
    title: '指标采集',
    desc: 'CPU、内存、磁盘、网络、MySQL、Redis 指标模拟采集。',
    path: '/aiops/metrics'
  },
  {
    title: '告警中心',
    desc: '告警规则、告警事件和指标异常识别。',
    path: '/aiops/alerts'
  },
  {
    title: '运维工单',
    desc: '告警转工单、工单处理、工单关闭闭环。',
    path: '/aiops/work-orders'
  },
  {
    title: '根因分析',
    desc: '基于告警、指标、工单生成疑似根因和处理建议。',
    path: '/aiops/root-causes'
  }
]

const mineFlow = [
  '设备台账 mine_device',
  '传感器台账 mine_sensor',
  'MQTT 数据上报 mine/sensor/data',
  '数据入库 mine_sensor_data',
  '告警生成 mine_alarm_event',
  '工单闭环 mine_work_order',
  '智能矿山综合看板 /mine/dashboard',
  '设备健康评分 /mine/device-health',
  '预测性维护任务 mine_maintenance_task',
  '维护看板 /mine/maintenance-dashboard',
  '最近 7 天风险趋势分析'
]

const aiopsFlow = [
  '资源台账 aiops_resource',
  '指标采集 aiops_metric_data',
  '指标异常识别 alarm_flag',
  '告警事件 aiops_alert_event',
  '告警转运维工单 aiops_work_order',
  '工单处理与关闭',
  '根因分析 aiops_root_cause_record',
  'AIOps 综合看板 /aiops/dashboard',
  'Actuator 指标 /actuator/prometheus',
  'Prometheus Targets UP',
  'Grafana 数据源与 Explore 查询'
]

const goPage = (path: string) => {
  router.push(path)
}

const openExternal = (url: string) => {
  window.open(url, '_blank')
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  padding: 28px;
  background: #f5f7fb;
  color: #1f2937;
}

.dashboard-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 24px;
  align-items: center;
  margin-bottom: 20px;
  padding: 28px;
  border-radius: 14px;
  background: linear-gradient(135deg, #ffffff 0%, #eef5ff 100%);
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.06);
}

.page-subtitle {
  margin: 0 0 8px;
  color: #2563eb;
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.dashboard-header h1 {
  margin: 0;
  font-size: 30px;
  color: #111827;
}

.page-desc {
  margin: 12px 0 0;
  color: #4b5563;
  line-height: 1.7;
}

.user-panel {
  display: flex;
  gap: 14px;
  align-items: center;
  justify-content: flex-end;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  text-align: right;
}

.user-label {
  color: #6b7280;
  font-size: 13px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.summary-card {
  border-radius: 12px;
}

.summary-title {
  color: #6b7280;
  font-size: 14px;
}

.summary-value {
  margin-top: 10px;
  color: #111827;
  font-size: 24px;
  font-weight: 800;
}

.summary-desc {
  margin-top: 8px;
  color: #4b5563;
  line-height: 1.6;
}

.project-section {
  margin-bottom: 20px;
  padding: 24px;
  border-radius: 14px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.05);
}

.section-title {
  margin-bottom: 18px;
}

.section-title h2 {
  margin: 0;
  font-size: 22px;
  color: #111827;
}

.section-title p {
  margin: 8px 0 0;
  color: #6b7280;
  line-height: 1.7;
}

.entry-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
}

.entry-card {
  border-radius: 12px;
}

.entry-content {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 14px;
  align-items: center;
}

.entry-title {
  margin-bottom: 8px;
  color: #111827;
  font-size: 17px;
  font-weight: 700;
}

.entry-desc {
  color: #6b7280;
  line-height: 1.6;
}

.monitor-card {
  background: #fbfffb;
}

.flow-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
}

.flow-item {
  display: flex;
  gap: 10px;
  align-items: center;
  padding: 12px 14px;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  background: #f9fafb;
  color: #374151;
  line-height: 1.5;
}

.flow-index {
  display: inline-flex;
  width: 26px;
  height: 26px;
  flex: 0 0 26px;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: #2563eb;
  color: #ffffff;
  font-size: 13px;
  font-weight: 700;
}

@media (max-width: 768px) {
  .dashboard-page {
    padding: 16px;
  }

  .dashboard-header {
    grid-template-columns: 1fr;
  }

  .user-panel {
    justify-content: flex-start;
  }

  .user-info {
    text-align: left;
  }

  .entry-content {
    grid-template-columns: 1fr;
  }
}
</style>
