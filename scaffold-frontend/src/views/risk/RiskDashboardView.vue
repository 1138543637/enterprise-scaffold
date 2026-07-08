<template>
  <div class="risk-dashboard-page">
    <div class="page-header">
      <div>
        <h2>银行实时交易风控看板</h2>
        <p>汇总展示交易流水、规则命中、风险审核和 Kafka 实时交易接入后的风控数据。</p>
      </div>

      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button @click="goTransactions">交易流水</el-button>
        <el-button @click="goRules">规则引擎</el-button>
        <el-button @click="goReviewOrders">人工审核</el-button>
        <el-button type="primary" :loading="loading" @click="loadDashboard">刷新看板</el-button>
      </div>
    </div>

    <div class="summary-grid">
      <div class="metric-card">
        <div class="metric-title">交易总数</div>
        <div class="metric-value">{{ summary.transactionTotal || 0 }}</div>
        <div class="metric-desc">risk_transaction</div>
      </div>

      <div class="metric-card danger">
        <div class="metric-title">风险交易</div>
        <div class="metric-value">{{ summary.riskTransactionTotal || 0 }}</div>
        <div class="metric-desc">risk_flag = 1</div>
      </div>

      <div class="metric-card">
        <div class="metric-title">风控规则</div>
        <div class="metric-value">{{ summary.ruleTotal || 0 }}</div>
        <div class="metric-desc">risk_rule</div>
      </div>

      <div class="metric-card warning">
        <div class="metric-title">规则命中</div>
        <div class="metric-value">{{ summary.ruleHitTotal || 0 }}</div>
        <div class="metric-desc">risk_rule_hit</div>
      </div>

      <div class="metric-card warning">
        <div class="metric-title">待审核</div>
        <div class="metric-value">{{ summary.pendingReviewTotal || 0 }}</div>
        <div class="metric-desc">review_status = 0</div>
      </div>

      <div class="metric-card success">
        <div class="metric-title">审核通过</div>
        <div class="metric-value">{{ summary.approvedReviewTotal || 0 }}</div>
        <div class="metric-desc">review_status = 1</div>
      </div>

      <div class="metric-card danger">
        <div class="metric-title">审核拒绝</div>
        <div class="metric-value">{{ summary.rejectedReviewTotal || 0 }}</div>
        <div class="metric-desc">review_status = 2</div>
      </div>
    </div>

    <div class="chart-grid">
      <el-card>
        <template #header>交易渠道分布</template>
        <div ref="channelChartRef" class="chart-box"></div>
      </el-card>

      <el-card>
        <template #header>交易类型分布</template>
        <div ref="typeChartRef" class="chart-box"></div>
      </el-card>

      <el-card>
        <template #header>风险等级分布</template>
        <div ref="riskLevelChartRef" class="chart-box"></div>
      </el-card>
    </div>

    <div class="table-grid">
      <el-card>
        <template #header>最近交易流水</template>
        <el-table :data="safeRecentTransactions" border stripe class="table-wrapper">
          <el-table-column prop="transactionNo" label="交易流水号" min-width="190" />
          <el-table-column prop="customerName" label="客户" min-width="120" />
          <el-table-column prop="transactionTypeName" label="交易类型" width="100" />
          <el-table-column prop="channelName" label="渠道" width="100" />
          <el-table-column prop="amount" label="金额" width="120" />
          <el-table-column prop="location" label="地点" min-width="120" />
          <el-table-column label="风险标记" width="110">
            <template #default="{ row }">
              <el-tag :type="row.riskFlag === 1 ? 'danger' : 'success'">
                {{ row.riskFlagName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="transactionTime" label="交易时间" min-width="170" />
        </el-table>
      </el-card>

      <el-card>
        <template #header>最近规则命中</template>
        <el-table :data="safeRecentRuleHits" border stripe class="table-wrapper">
          <el-table-column prop="hitCode" label="命中编号" min-width="170" />
          <el-table-column prop="transactionNo" label="交易流水号" min-width="190" />
          <el-table-column prop="ruleName" label="规则名称" min-width="160" />
          <el-table-column prop="ruleTypeName" label="规则类型" width="120" />
          <el-table-column label="风险等级" width="110">
            <template #default="{ row }">
              <el-tag :type="riskLevelTagType(row.riskLevel)">
                {{ row.riskLevelName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="riskScore" label="风险分" width="90" />
          <el-table-column prop="hitTime" label="命中时间" min-width="170" />
        </el-table>
      </el-card>
    </div>

    <el-card>
      <template #header>最近人工审核单</template>
      <el-table :data="safeRecentReviewOrders" border stripe class="table-wrapper">
        <el-table-column prop="reviewOrderCode" label="审核单号" min-width="190" />
        <el-table-column prop="transactionNo" label="交易流水号" min-width="190" />
        <el-table-column prop="customerName" label="客户" min-width="120" />
        <el-table-column prop="amount" label="金额" width="120" />
        <el-table-column prop="totalScore" label="总分" width="90" />
        <el-table-column label="风险等级" width="110">
          <template #default="{ row }">
            <el-tag :type="riskLevelTagType(row.riskLevel)">
              {{ row.riskLevelName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险结果" width="110">
          <template #default="{ row }">
            <el-tag :type="riskResultTagType(row.riskResult)">
              {{ row.riskResultName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审核状态" width="110">
          <template #default="{ row }">
            <el-tag :type="reviewStatusTagType(row.reviewStatus)">
              {{ row.reviewStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref, shallowRef } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import {
  getRiskChannelStatsApi,
  getRiskDashboardSummaryApi,
  getRiskLevelStatsApi,
  getRiskRecentReviewOrdersApi,
  getRiskRecentRuleHitsApi,
  getRiskRecentTransactionsApi,
  getRiskTransactionTypeStatsApi,
  type RiskChannelStatVO,
  type RiskDashboardSummaryVO,
  type RiskLevelStatVO,
  type RiskRecentReviewOrderVO,
  type RiskRecentRuleHitVO,
  type RiskRecentTransactionVO,
  type RiskTransactionTypeStatVO,
} from '../../api/risk/dashboard'

const router = useRouter()

const loading = ref(false)

const summary = ref<RiskDashboardSummaryVO>({
  transactionTotal: 0,
  riskTransactionTotal: 0,
  ruleTotal: 0,
  ruleHitTotal: 0,
  pendingReviewTotal: 0,
  approvedReviewTotal: 0,
  rejectedReviewTotal: 0,
})

const channelStats = ref<RiskChannelStatVO[]>([])
const transactionTypeStats = ref<RiskTransactionTypeStatVO[]>([])
const riskLevelStats = ref<RiskLevelStatVO[]>([])
const recentTransactions = ref<RiskRecentTransactionVO[]>([])
const recentRuleHits = ref<RiskRecentRuleHitVO[]>([])
const recentReviewOrders = ref<RiskRecentReviewOrderVO[]>([])

const channelChartRef = ref<HTMLDivElement | null>(null)
const typeChartRef = ref<HTMLDivElement | null>(null)
const riskLevelChartRef = ref<HTMLDivElement | null>(null)

const channelChart = shallowRef<echarts.ECharts | null>(null)
const typeChart = shallowRef<echarts.ECharts | null>(null)
const riskLevelChart = shallowRef<echarts.ECharts | null>(null)

const safeRecentTransactions = computed(() =>
  Array.isArray(recentTransactions.value) ? recentTransactions.value : [],
)

const safeRecentRuleHits = computed(() =>
  Array.isArray(recentRuleHits.value) ? recentRuleHits.value : [],
)

const safeRecentReviewOrders = computed(() =>
  Array.isArray(recentReviewOrders.value) ? recentReviewOrders.value : [],
)

async function loadDashboard() {
  loading.value = true
  try {
    const [
      summaryData,
      channelData,
      typeData,
      riskLevelData,
      transactionData,
      ruleHitData,
      reviewOrderData,
    ] = await Promise.all([
      getRiskDashboardSummaryApi(),
      getRiskChannelStatsApi(),
      getRiskTransactionTypeStatsApi(),
      getRiskLevelStatsApi(),
      getRiskRecentTransactionsApi(),
      getRiskRecentRuleHitsApi(),
      getRiskRecentReviewOrdersApi(),
    ])

    summary.value = summaryData || summary.value
    channelStats.value = Array.isArray(channelData) ? channelData : []
    transactionTypeStats.value = Array.isArray(typeData) ? typeData : []
    riskLevelStats.value = Array.isArray(riskLevelData) ? riskLevelData : []
    recentTransactions.value = Array.isArray(transactionData) ? transactionData : []
    recentRuleHits.value = Array.isArray(ruleHitData) ? ruleHitData : []
    recentReviewOrders.value = Array.isArray(reviewOrderData) ? reviewOrderData : []

    await nextTick()
    renderCharts()
  } catch (error) {
    console.error(error)
    ElMessage.error('风控看板加载失败')
  } finally {
    loading.value = false
  }
}

function renderCharts() {
  renderChannelChart()
  renderTransactionTypeChart()
  renderRiskLevelChart()
}

function renderChannelChart() {
  if (!channelChartRef.value) {
    return
  }

  if (!channelChart.value) {
    channelChart.value = echarts.init(channelChartRef.value)
  }

  channelChart.value.setOption({
    tooltip: {
      trigger: 'item',
    },
    legend: {
      bottom: 0,
    },
    series: [
      {
        name: '交易渠道',
        type: 'pie',
        radius: ['45%', '70%'],
        data: channelStats.value.map((item) => ({
          name: item.channelName || item.channel || '未知',
          value: Number(item.total || 0),
        })),
      },
    ],
  })
}

function renderTransactionTypeChart() {
  if (!typeChartRef.value) {
    return
  }

  if (!typeChart.value) {
    typeChart.value = echarts.init(typeChartRef.value)
  }

  typeChart.value.setOption({
    tooltip: {
      trigger: 'axis',
    },
    grid: {
      left: 40,
      right: 20,
      top: 30,
      bottom: 40,
    },
    xAxis: {
      type: 'category',
      data: transactionTypeStats.value.map((item) => item.transactionTypeName || item.transactionType || '未知'),
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        name: '交易数量',
        type: 'bar',
        data: transactionTypeStats.value.map((item) => Number(item.total || 0)),
      },
      {
        name: '风险交易',
        type: 'bar',
        data: transactionTypeStats.value.map((item) => Number(item.riskTotal || 0)),
      },
    ],
  })
}

function renderRiskLevelChart() {
  if (!riskLevelChartRef.value) {
    return
  }

  if (!riskLevelChart.value) {
    riskLevelChart.value = echarts.init(riskLevelChartRef.value)
  }

  riskLevelChart.value.setOption({
    tooltip: {
      trigger: 'item',
    },
    legend: {
      bottom: 0,
    },
    series: [
      {
        name: '风险等级',
        type: 'pie',
        radius: '65%',
        data: riskLevelStats.value.map((item) => ({
          name: item.riskLevelName || '未知',
          value: Number(item.total || 0),
        })),
      },
    ],
  })
}

function resizeCharts() {
  channelChart.value?.resize()
  typeChart.value?.resize()
  riskLevelChart.value?.resize()
}

function disposeCharts() {
  channelChart.value?.dispose()
  typeChart.value?.dispose()
  riskLevelChart.value?.dispose()
  channelChart.value = null
  typeChart.value = null
  riskLevelChart.value = null
}

function riskLevelTagType(riskLevel: number) {
  if (riskLevel === 3) {
    return 'danger'
  }
  if (riskLevel === 2) {
    return 'warning'
  }
  if (riskLevel === 1) {
    return 'success'
  }
  return 'info'
}

function riskResultTagType(riskResult: string) {
  if (riskResult === 'REJECT') {
    return 'danger'
  }
  if (riskResult === 'REVIEW') {
    return 'warning'
  }
  if (riskResult === 'PASS') {
    return 'success'
  }
  return 'info'
}

function reviewStatusTagType(reviewStatus: number) {
  if (reviewStatus === 2) {
    return 'danger'
  }
  if (reviewStatus === 1) {
    return 'success'
  }
  if (reviewStatus === 0) {
    return 'warning'
  }
  return 'info'
}

function goDashboard() {
  router.push('/dashboard')
}

function goTransactions() {
  router.push('/risk/transactions')
}

function goRules() {
  router.push('/risk/rules')
}

function goReviewOrders() {
  router.push('/risk/review-orders')
}

onMounted(() => {
  loadDashboard()
  window.addEventListener('resize', resizeCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  disposeCharts()
})
</script>

<style scoped>
.risk-dashboard-page {
  min-height: 100vh;
  padding: 24px;
  background: #f5f7fb;
}

.page-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 18px;
}

.page-header h2 {
  margin: 0 0 8px;
  font-size: 24px;
  color: #1f2d3d;
}

.page-header p {
  margin: 0;
  color: #606266;
}

.header-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-end;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.metric-card {
  min-height: 118px;
  padding: 18px;
  border: 1px solid #e4e7ec;
  border-radius: 10px;
  background: #ffffff;
  box-sizing: border-box;
}

.metric-card.warning {
  border-color: #f3d19e;
}

.metric-card.danger {
  border-color: #fab6b6;
}

.metric-card.success {
  border-color: #b3e19d;
}

.metric-title {
  font-size: 14px;
  color: #606266;
}

.metric-value {
  margin-top: 12px;
  font-size: 30px;
  font-weight: 700;
  color: #1f2d3d;
}

.metric-desc {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.chart-box {
  height: 320px;
}

.table-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(520px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.table-wrapper {
  width: 100%;
}

@media (max-width: 768px) {
  .risk-dashboard-page {
    padding: 14px;
  }

  .page-header {
    flex-direction: column;
  }

  .header-actions {
    justify-content: flex-start;
  }

  .table-grid {
    grid-template-columns: 1fr;
  }
}
</style>
