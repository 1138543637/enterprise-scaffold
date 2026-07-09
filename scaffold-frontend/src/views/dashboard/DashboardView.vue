<template>
  <div class="dashboard-page">
    <header class="dashboard-header">
      <div>
        <p class="page-subtitle">Enterprise Scaffold</p>
        <h1>企业级项目演示首页</h1>
        <p class="page-desc">
          当前脚手架已经完成系统基础能力、项目一智能矿山平台、项目二云网融合 AIOps 智能运维平台、
          项目三银行实时交易风控与反欺诈平台，并已推进到项目四国企 / 政务数据治理与共享交换平台收尾阶段。
          首页展示顺序固定为：公共脚手架 -> 项目一 -> 项目二 -> 项目三 -> 项目四。
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
        <div class="summary-value">A2 已完成</div>
        <div class="summary-desc">云网融合 AIOps 智能运维平台。</div>
      </el-card>

      <el-card shadow="hover" class="summary-card">
        <div class="summary-title">项目三</div>
        <div class="summary-value">R3-07 收尾验收</div>
        <div class="summary-desc">银行实时交易风控与反欺诈平台。</div>
      </el-card>

      <el-card shadow="hover" class="summary-card">
        <div class="summary-title">项目四</div>
        <div class="summary-value">D4-07 收尾验收</div>
        <div class="summary-desc">国企 / 政务数据治理与共享交换平台。</div>
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

    <section class="project-section risk-section">
      <div class="section-title">
        <h2>项目三：银行实时交易风控与反欺诈平台</h2>
        <p>
          R3-07 收尾阶段用于验收交易模拟、Kafka 实时交易、规则引擎、规则命中、风险评分、人工审核和风控看板完整链路。
          项目三固定放在项目二后面。
        </p>
      </div>

      <div class="entry-grid">
        <el-card
            v-for="entry in riskEntries"
            :key="entry.path"
            shadow="hover"
            class="entry-card risk-card"
        >
          <div class="entry-content">
            <div>
              <div class="entry-title">{{ entry.title }}</div>
              <div class="entry-desc">{{ entry.desc }}</div>
            </div>
            <el-button type="warning" @click="goPage(entry.path)">进入</el-button>
          </div>
        </el-card>
      </div>
    </section>

    <section class="project-section risk-section">
      <div class="section-title">
        <h2>项目三验收链路</h2>
        <p>
          按下面顺序演示，能体现银行风控从交易接入、规则识别、风险评分到人工审核和看板展示的完整闭环。
        </p>
      </div>

      <div class="flow-grid">
        <div v-for="(item, index) in riskFlow" :key="item" class="flow-item risk-flow-item">
          <span class="flow-index risk-flow-index">{{ index + 1 }}</span>
          <span>{{ item }}</span>
        </div>
      </div>
    </section>

    <section class="project-section datahub-section">
      <div class="section-title">
        <h2>项目四：国企 / 政务数据治理与共享交换平台</h2>
        <p>
          D4-07 收尾阶段用于验收数据源管理、元数据采集、数据质量检测、敏感数据识别、脱敏预览、
          API 共享发布和数据治理看板完整链路。项目四固定放在项目三后面。
        </p>
      </div>

      <div class="entry-grid">
        <el-card
            v-for="entry in datahubEntries"
            :key="entry.path"
            shadow="hover"
            class="entry-card datahub-card"
        >
          <div class="entry-content">
            <div>
              <div class="entry-title">{{ entry.title }}</div>
              <div class="entry-desc">{{ entry.desc }}</div>
            </div>
            <el-button type="success" @click="goPage(entry.path)">进入</el-button>
          </div>
        </el-card>
      </div>
    </section>

    <section class="project-section datahub-section">
      <div class="section-title">
        <h2>项目四验收链路</h2>
        <p>
          按下面顺序演示，能体现数据治理从数据源接入、元数据采集、质量检测、敏感识别、脱敏处理、
          API 发布到治理看板展示的完整闭环。
        </p>
      </div>

      <div class="flow-grid">
        <div
            v-for="(item, index) in datahubFlow"
            :key="item"
            class="flow-item datahub-flow-item"
        >
          <span class="flow-index datahub-flow-index">{{ index + 1 }}</span>
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

const riskEntries = [
  {
    title: '银行风控综合看板',
    desc: '交易总览、风险交易、规则命中、人工审核和风险分布图。',
    path: '/risk/dashboard'
  },
  {
    title: '交易流水与 Kafka 模拟',
    desc: 'HTTP 交易模拟、Kafka 批量模拟发布和交易流水分页查询。',
    path: '/risk/transactions'
  },
  {
    title: '规则引擎与规则命中',
    desc: '风控规则分页、规则命中生成和规则命中记录查询。',
    path: '/risk/rules'
  },
  {
    title: '人工审核单',
    desc: '风险评分、审核单生成、审核通过和审核拒绝。',
    path: '/risk/review-orders'
  }
]

const datahubEntries = [
  {
    title: '数据治理综合看板',
    desc: '数据源、元数据、质量结果、敏感字段、脱敏结果和 API 发布综合统计展示。',
    path: '/datahub/dashboard'
  },
  {
    title: '数据源管理',
    desc: '数据源台账、数据源分页查询和连接测试。',
    path: '/datahub/datasources'
  },
  {
    title: '元数据采集',
    desc: '采集数据库表和字段元数据，形成数据资产目录。',
    path: '/datahub/metadata'
  },
  {
    title: '数据质量检测',
    desc: '质量规则分页、质量检测执行和质量结果查询。',
    path: '/datahub/quality'
  },
  {
    title: '敏感数据识别与脱敏',
    desc: '识别手机号、身份证、邮箱等敏感字段，并进行脱敏预览。',
    path: '/datahub/sensitive'
  },
  {
    title: 'API 共享发布',
    desc: '基于元数据表模拟发布共享 API，并支持上线、下线和分页查询。',
    path: '/datahub/apis'
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

const riskFlow = [
  '交易流水 risk_transaction',
  'Kafka Topic risk.transaction.events',
  'Kafka 消费写入 risk_transaction',
  '风控规则 risk_rule',
  '规则命中 risk_rule_hit',
  '交易风险标记 risk_flag',
  '风险评分 risk_score / risk_level',
  '人工审核单 risk_review_order',
  '审核通过 approve',
  '审核拒绝 reject',
  '风控综合看板 /risk/dashboard'
]

const datahubFlow = [
  '数据源台账 datahub_datasource',
  '数据源连接测试 test-connection',
  '元数据表 datahub_metadata_table',
  '元数据字段 datahub_metadata_column',
  '元数据采集日志 datahub_metadata_collect_log',
  '数据质量规则 datahub_quality_rule',
  '数据质量检测结果 datahub_quality_result',
  '敏感字段识别 datahub_sensitive_field',
  '脱敏规则 datahub_mask_rule',
  '脱敏预览结果 datahub_mask_result',
  'API 发布 datahub_api_publish',
  'API 上线 online',
  'API 下线 offline',
  '数据治理综合看板 /datahub/dashboard'
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

.risk-section {
  border: 1px solid #fed7aa;
  background: #fffaf4;
}

.datahub-section {
  border: 1px solid #bbf7d0;
  background: #f7fff9;
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

.risk-card {
  border-color: #fed7aa;
  background: #fffdf8;
}

.datahub-card {
  border-color: #bbf7d0;
  background: #fbfffd;
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

.risk-flow-item {
  border-color: #fed7aa;
  background: #fff7ed;
}

.datahub-flow-item {
  border-color: #bbf7d0;
  background: #f0fdf4;
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

.risk-flow-index {
  background: #f59e0b;
}

.datahub-flow-index {
  background: #16a34a;
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