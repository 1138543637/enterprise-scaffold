<template>
  <div class="iam-security-dashboard-page">
    <section class="page-header">
      <div>
        <p class="page-subtitle">Enterprise Scaffold / IAM</p>
        <h1>IAM 安全看板增强</h1>
        <p class="page-desc">
          汇总接口访问日志、异常登录风险、接口限流规则、安全策略配置和权限审计记录，形成统一身份认证与安全审计总览。
        </p>
      </div>
      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button type="primary" :loading="loading" @click="loadDashboard">刷新看板</el-button>
      </div>
    </section>

    <section class="summary-grid">
      <el-card shadow="hover" class="summary-card">
        <span>今日访问量</span>
        <strong>{{ dashboard.summary.todayAccessCount }}</strong>
        <small>iam_access_log 今日访问记录</small>
      </el-card>
      <el-card shadow="hover" class="summary-card danger-card">
        <span>今日失败访问</span>
        <strong>{{ dashboard.summary.todayFailedAccessCount }}</strong>
        <small>access_status = 1</small>
      </el-card>
      <el-card shadow="hover" class="summary-card warning-card">
        <span>未处理登录风险</span>
        <strong>{{ dashboard.summary.unhandledLoginRiskCount }}</strong>
        <small>iam_login_risk handle_status = 0</small>
      </el-card>
      <el-card shadow="hover" class="summary-card danger-card">
        <span>高风险权限变更</span>
        <strong>{{ dashboard.summary.highRiskPermissionAuditCount }}</strong>
        <small>iam_permission_audit risk_level = HIGH</small>
      </el-card>
      <el-card shadow="hover" class="summary-card warning-card">
        <span>待复核审计</span>
        <strong>{{ dashboard.summary.pendingAuditCount }}</strong>
        <small>review_status = PENDING</small>
      </el-card>
      <el-card shadow="hover" class="summary-card success-card">
        <span>启用安全能力</span>
        <strong>{{ dashboard.summary.enabledSecurityPolicyCount + dashboard.summary.enabledRateLimitRuleCount }}</strong>
        <small>启用策略 + 启用限流规则</small>
      </el-card>
    </section>

    <section class="dashboard-grid">
      <el-card shadow="hover" class="dashboard-panel">
        <template #header>
          <div class="panel-header">
            <span>风险分布</span>
            <small>登录风险 + 权限审计</small>
          </div>
        </template>
        <div class="stat-list">
          <div v-for="item in safeRiskDistributions" :key="item.riskLevel" class="stat-row">
            <div class="stat-row-main">
              <el-tag :type="riskTagType(item.riskLevel)">{{ item.riskLevelName }}</el-tag>
              <span>合计 {{ item.totalCount }}</span>
            </div>
            <div class="bar-wrap">
              <div class="bar-inner" :style="{ width: percent(item.totalCount, maxRiskTotal) + '%' }"></div>
            </div>
            <div class="stat-row-sub">
              <span>异常登录：{{ item.loginRiskCount }}</span>
              <span>权限审计：{{ item.permissionAuditCount }}</span>
            </div>
          </div>
        </div>
      </el-card>

      <el-card shadow="hover" class="dashboard-panel">
        <template #header>
          <div class="panel-header">
            <span>权限审计复核状态</span>
            <small>iam_permission_audit</small>
          </div>
        </template>
        <div class="stat-list">
          <div v-for="item in safeReviewStatusStats" :key="item.reviewStatus" class="stat-row compact-row">
            <span>{{ item.reviewStatusName }}</span>
            <strong>{{ item.totalCount }}</strong>
          </div>
        </div>
      </el-card>

      <el-card shadow="hover" class="dashboard-panel">
        <template #header>
          <div class="panel-header">
            <span>安全能力启停统计</span>
            <small>安全策略 + 限流规则</small>
          </div>
        </template>
        <div class="stat-list">
          <div v-for="item in safePolicyStatusStats" :key="item.enabled" class="stat-row">
            <div class="stat-row-main">
              <el-tag :type="item.enabled === 1 ? 'success' : 'info'">{{ item.enabledName }}</el-tag>
              <span>合计 {{ item.totalCount }}</span>
            </div>
            <div class="stat-row-sub">
              <span>安全策略：{{ item.securityPolicyCount }}</span>
              <span>限流规则：{{ item.rateLimitRuleCount }}</span>
            </div>
          </div>
        </div>
      </el-card>
    </section>

    <el-card shadow="hover" class="event-card">
      <template #header>
        <div class="panel-header">
          <span>最近安全事件</span>
          <small>最近异常登录和权限审计记录</small>
        </div>
      </template>
      <el-table v-loading="loading" :data="safeRecentEvents" border stripe>
        <el-table-column prop="eventTypeName" label="事件类型" width="120" />
        <el-table-column prop="eventCode" label="事件编码" min-width="180" show-overflow-tooltip />
        <el-table-column prop="eventTitle" label="事件标题" min-width="160" show-overflow-tooltip />
        <el-table-column prop="eventDetail" label="事件说明" min-width="300" show-overflow-tooltip />
        <el-table-column label="风险等级" width="110">
          <template #default="scope">
            <el-tag :type="riskTagType(scope.row.riskLevel)">{{ scope.row.riskLevelName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statusText" label="处理状态" width="110" />
        <el-table-column prop="operatorName" label="用户/操作人" width="130" show-overflow-tooltip />
        <el-table-column prop="requestIp" label="IP" width="130" show-overflow-tooltip />
        <el-table-column label="事件时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.eventTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getIamSecurityDashboard,
  type IamSecurityDashboardVO
} from '../../api/iam/securityDashboard'
import {useRouter} from "vue-router";
const router = useRouter()
const loading = ref(false)

const dashboard = reactive<IamSecurityDashboardVO>({
  summary: {
    todayAccessCount: 0,
    todayFailedAccessCount: 0,
    unhandledLoginRiskCount: 0,
    highRiskPermissionAuditCount: 0,
    pendingAuditCount: 0,
    enabledSecurityPolicyCount: 0,
    enabledRateLimitRuleCount: 0
  },
  riskDistributions: [],
  reviewStatusStats: [],
  policyStatusStats: [],
  recentEvents: []
})

const safeRiskDistributions = computed(() => Array.isArray(dashboard.riskDistributions) ? dashboard.riskDistributions : [])
const safeReviewStatusStats = computed(() => Array.isArray(dashboard.reviewStatusStats) ? dashboard.reviewStatusStats : [])
const safePolicyStatusStats = computed(() => Array.isArray(dashboard.policyStatusStats) ? dashboard.policyStatusStats : [])
const safeRecentEvents = computed(() => Array.isArray(dashboard.recentEvents) ? dashboard.recentEvents : [])
const maxRiskTotal = computed(() => Math.max(...safeRiskDistributions.value.map(item => Number(item.totalCount || 0)), 1))
function goDashboard() {
  router.push('/dashboard')
}
const loadDashboard = async () => {
  loading.value = true
  try {
    const data = await getIamSecurityDashboard()
    dashboard.summary = data.summary
    dashboard.riskDistributions = Array.isArray(data.riskDistributions) ? data.riskDistributions : []
    dashboard.reviewStatusStats = Array.isArray(data.reviewStatusStats) ? data.reviewStatusStats : []
    dashboard.policyStatusStats = Array.isArray(data.policyStatusStats) ? data.policyStatusStats : []
    dashboard.recentEvents = Array.isArray(data.recentEvents) ? data.recentEvents : []
  } catch (error) {
    console.error(error)
    ElMessage.error('IAM 安全看板加载失败，请按顺序检查 F12 Network、ApiResult 解包、Docker 后端日志。')
  } finally {
    loading.value = false
  }
}

const percent = (value: number, max: number) => {
  if (!max || max <= 0) {
    return 0
  }
  return Math.round((Number(value || 0) / max) * 100)
}

const riskTagType = (riskLevel: string | number | undefined) => {
  const value = String(riskLevel ?? '')
  if (value === 'HIGH' || value === '3') {
    return 'danger'
  }
  if (value === 'MEDIUM' || value === '2') {
    return 'warning'
  }
  if (value === 'LOW' || value === '1') {
    return 'success'
  }
  return 'info'
}

const formatTime = (value: string | undefined) => {
  if (!value) {
    return '-'
  }
  return value.replace('T', ' ')
}

onMounted(() => {
  loadDashboard()
})
</script>

<style scoped>
.iam-security-dashboard-page {
  padding: 24px;
  display: grid;
  gap: 18px;
}

.page-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 16px;
  align-items: center;
  padding: 22px;
  border-radius: 14px;
  background: #fff;
  box-shadow: 0 8px 26px rgba(15, 23, 42, 0.08);
}

.page-subtitle {
  margin: 0 0 6px;
  color: #64748b;
  font-size: 13px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  color: #0f172a;
}

.page-desc {
  margin: 8px 0 0;
  color: #64748b;
  line-height: 1.7;
}

.header-actions {
  display: flex;
  justify-content: flex-end;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 16px;
}

.summary-card :deep(.el-card__body) {
  min-height: 112px;
  display: grid;
  gap: 8px;
}

.summary-card span {
  color: #64748b;
  font-size: 14px;
}

.summary-card strong {
  font-size: 30px;
  line-height: 1;
  color: #0f172a;
}

.summary-card small {
  color: #94a3b8;
}

.danger-card strong {
  color: #dc2626;
}

.warning-card strong {
  color: #d97706;
}

.success-card strong {
  color: #16a34a;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}

.dashboard-panel,
.event-card {
  border-radius: 14px;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.panel-header span {
  font-weight: 700;
  color: #0f172a;
}

.panel-header small {
  color: #94a3b8;
}

.stat-list {
  display: grid;
  gap: 14px;
}

.stat-row {
  display: grid;
  gap: 8px;
  padding: 12px;
  border-radius: 12px;
  background: #f8fafc;
}

.stat-row-main,
.stat-row-sub,
.compact-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.stat-row-sub {
  color: #64748b;
  font-size: 13px;
}

.compact-row strong {
  font-size: 22px;
}

.bar-wrap {
  width: 100%;
  height: 8px;
  overflow: hidden;
  border-radius: 999px;
  background: #e2e8f0;
}

.bar-inner {
  height: 100%;
  border-radius: 999px;
  background: #2563eb;
}

@media (max-width: 768px) {
  .iam-security-dashboard-page {
    padding: 14px;
  }

  .page-header {
    grid-template-columns: 1fr;
  }

  .header-actions {
    justify-content: flex-start;
  }
}
</style>
