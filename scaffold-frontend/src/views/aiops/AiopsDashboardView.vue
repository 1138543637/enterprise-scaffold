<template>
  <div class="aiops-dashboard-page">
    <div class="page-header">
      <div>
        <h2>AIOps 综合看板</h2>
        <p>展示资源、指标、告警、工单和根因分析的综合运行状态。</p>
      </div>

      <div class="header-actions">
        <el-button @click="goHome">返回首页</el-button>
        <el-button type="primary" :loading="loading" @click="loadDashboard">刷新看板</el-button>
      </div>
    </div>

    <div class="summary-grid">
      <div class="metric-card">
        <div class="metric-title">资源总数</div>
        <div class="metric-value">{{ summary.resourceTotal }}</div>
        <div class="metric-desc">AIOps 纳管资源</div>
      </div>

      <div class="metric-card danger">
        <div class="metric-title">异常资源</div>
        <div class="metric-value">{{ summary.abnormalResourceTotal }}</div>
        <div class="metric-desc">status = 2</div>
      </div>

      <div class="metric-card">
        <div class="metric-title">指标数据</div>
        <div class="metric-value">{{ summary.metricDataTotal }}</div>
        <div class="metric-desc">aiops_metric_data</div>
      </div>

      <div class="metric-card warning">
        <div class="metric-title">异常指标</div>
        <div class="metric-value">{{ summary.alertMetricTotal }}</div>
        <div class="metric-desc">alarm_flag = 1</div>
      </div>

      <div class="metric-card">
        <div class="metric-title">告警规则</div>
        <div class="metric-value">{{ summary.alertRuleTotal }}</div>
        <div class="metric-desc">aiops_alert_rule</div>
      </div>

      <div class="metric-card danger">
        <div class="metric-title">告警事件</div>
        <div class="metric-value">{{ summary.alertEventTotal }}</div>
        <div class="metric-desc">aiops_alert_event</div>
      </div>

      <div class="metric-card warning">
        <div class="metric-title">未处理告警</div>
        <div class="metric-value">{{ summary.unhandledAlertTotal }}</div>
        <div class="metric-desc">handle_status = 0</div>
      </div>

      <div class="metric-card">
        <div class="metric-title">运维工单</div>
        <div class="metric-value">{{ summary.workOrderTotal }}</div>
        <div class="metric-desc">aiops_work_order</div>
      </div>

      <div class="metric-card warning">
        <div class="metric-title">待处理工单</div>
        <div class="metric-value">{{ summary.pendingWorkOrderTotal }}</div>
        <div class="metric-desc">order_status = 0</div>
      </div>

      <div class="metric-card">
        <div class="metric-title">根因分析</div>
        <div class="metric-value">{{ summary.rootCauseTotal }}</div>
        <div class="metric-desc">aiops_root_cause_record</div>
      </div>

      <div class="metric-card success">
        <div class="metric-title">高置信根因</div>
        <div class="metric-value">{{ summary.highConfidenceRootCauseTotal }}</div>
        <div class="metric-desc">confidence_score ≥ 80</div>
      </div>
    </div>

    <div class="chart-grid">
      <el-card shadow="never">
        <template #header>最近 7 天指标趋势</template>
        <div ref="metricTrendChartRef" class="chart-box"></div>
      </el-card>

      <el-card shadow="never">
        <template #header>资源类型分布</template>
        <div ref="resourceTypeChartRef" class="chart-box"></div>
      </el-card>

      <el-card shadow="never">
        <template #header>告警级别分布</template>
        <div ref="alertLevelChartRef" class="chart-box"></div>
      </el-card>

      <el-card shadow="never">
        <template #header>工单状态分布</template>
        <div ref="workOrderStatusChartRef" class="chart-box"></div>
      </el-card>
    </div>

    <div class="table-grid">
      <el-card shadow="never">
        <template #header>最近告警事件</template>
        <el-table :data="recentAlerts" border stripe class="data-table">
          <el-table-column prop="eventCode" label="告警编码" min-width="170" />
          <el-table-column prop="resourceName" label="资源名称" min-width="150" />
          <el-table-column prop="ipAddr" label="IP 地址" min-width="130" />
          <el-table-column prop="metricType" label="指标类型" min-width="100" />
          <el-table-column prop="metricValue" label="指标值" min-width="100" />
          <el-table-column prop="thresholdValue" label="阈值" min-width="100" />
          <el-table-column label="级别" min-width="90">
            <template #default="{ row }">
              <el-tag :type="alertLevelTag(row.alertLevel)">
                {{ row.alertLevelName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="处理状态" min-width="100">
            <template #default="{ row }">
              <el-tag :type="handleStatusTag(row.handleStatus)">
                {{ row.handleStatusName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="alertTime" label="告警时间" min-width="180" />
        </el-table>
      </el-card>

      <el-card shadow="never">
        <template #header>最近运维工单</template>
        <el-table :data="recentWorkOrders" border stripe class="data-table">
          <el-table-column prop="workOrderCode" label="工单编码" min-width="170" />
          <el-table-column prop="eventCode" label="告警编码" min-width="170" />
          <el-table-column prop="resourceName" label="资源名称" min-width="150" />
          <el-table-column prop="metricType" label="指标类型" min-width="100" />
          <el-table-column label="级别" min-width="90">
            <template #default="{ row }">
              <el-tag :type="alertLevelTag(row.alertLevel)">
                {{ row.alertLevelName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="工单状态" min-width="100">
            <template #default="{ row }">
              <el-tag :type="orderStatusTag(row.orderStatus)">
                {{ row.orderStatusName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" min-width="180" />
        </el-table>
      </el-card>

      <el-card shadow="never" class="full-table-card">
        <template #header>最近根因分析</template>
        <el-table :data="recentRootCauses" border stripe class="data-table">
          <el-table-column prop="analysisCode" label="分析编码" min-width="180" />
          <el-table-column prop="eventCode" label="告警编码" min-width="170" />
          <el-table-column prop="resourceName" label="资源名称" min-width="150" />
          <el-table-column prop="ipAddr" label="IP 地址" min-width="130" />
          <el-table-column prop="rootCauseTypeName" label="根因类型" min-width="150" />
          <el-table-column label="置信度" min-width="140">
            <template #default="{ row }">
              <el-progress
                :percentage="row.confidenceScore || 0"
                :stroke-width="10"
              />
            </template>
          </el-table-column>
          <el-table-column prop="analysisTime" label="分析时间" min-width="180" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { nextTick, onBeforeUnmount, onMounted, reactive, ref, shallowRef } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import {
  getAiopsAlertLevelStatsApi,
  getAiopsDashboardSummaryApi,
  getAiopsMetricTrendApi,
  getAiopsRecentAlertsApi,
  getAiopsRecentRootCausesApi,
  getAiopsRecentWorkOrdersApi,
  getAiopsResourceTypeStatsApi,
  getAiopsWorkOrderStatusStatsApi,
  type AiopsAlertLevelStatVO,
  type AiopsDashboardSummaryVO,
  type AiopsMetricTrendVO,
  type AiopsRecentAlertVO,
  type AiopsRecentRootCauseVO,
  type AiopsRecentWorkOrderVO,
  type AiopsResourceTypeStatVO,
  type AiopsWorkOrderStatusStatVO
} from '../../api/aiops/dashboard'

const router = useRouter()
const loading = ref(false)

const summary = reactive<AiopsDashboardSummaryVO>({
  resourceTotal: 0,
  abnormalResourceTotal: 0,
  metricDataTotal: 0,
  alertMetricTotal: 0,
  alertRuleTotal: 0,
  alertEventTotal: 0,
  unhandledAlertTotal: 0,
  workOrderTotal: 0,
  pendingWorkOrderTotal: 0,
  rootCauseTotal: 0,
  highConfidenceRootCauseTotal: 0
})

const resourceTypeStats = ref<AiopsResourceTypeStatVO[]>([])
const alertLevelStats = ref<AiopsAlertLevelStatVO[]>([])
const workOrderStatusStats = ref<AiopsWorkOrderStatusStatVO[]>([])
const metricTrend = ref<AiopsMetricTrendVO[]>([])
const recentAlerts = ref<AiopsRecentAlertVO[]>([])
const recentWorkOrders = ref<AiopsRecentWorkOrderVO[]>([])
const recentRootCauses = ref<AiopsRecentRootCauseVO[]>([])

const metricTrendChartRef = ref<HTMLDivElement | null>(null)
const resourceTypeChartRef = ref<HTMLDivElement | null>(null)
const alertLevelChartRef = ref<HTMLDivElement | null>(null)
const workOrderStatusChartRef = ref<HTMLDivElement | null>(null)

const metricTrendChart = shallowRef<ECharts | null>(null)
const resourceTypeChart = shallowRef<ECharts | null>(null)
const alertLevelChart = shallowRef<ECharts | null>(null)
const workOrderStatusChart = shallowRef<ECharts | null>(null)

async function loadDashboard() {
  loading.value = true

  try {
    const [
      summaryData,
      resourceTypeData,
      alertLevelData,
      workOrderStatusData,
      metricTrendData,
      recentAlertData,
      recentWorkOrderData,
      recentRootCauseData
    ] = await Promise.all([
      getAiopsDashboardSummaryApi(),
      getAiopsResourceTypeStatsApi(),
      getAiopsAlertLevelStatsApi(),
      getAiopsWorkOrderStatusStatsApi(),
      getAiopsMetricTrendApi(),
      getAiopsRecentAlertsApi(),
      getAiopsRecentWorkOrdersApi(),
      getAiopsRecentRootCausesApi()
    ])

    Object.assign(summary, summaryData || {})
    resourceTypeStats.value = resourceTypeData || []
    alertLevelStats.value = alertLevelData || []
    workOrderStatusStats.value = workOrderStatusData || []
    metricTrend.value = metricTrendData || []
    recentAlerts.value = recentAlertData || []
    recentWorkOrders.value = recentWorkOrderData || []
    recentRootCauses.value = recentRootCauseData || []

    await nextTick()
    renderCharts()
  } catch (error) {
    console.error(error)
    ElMessage.error('AIOps 综合看板加载失败')
  } finally {
    loading.value = false
  }
}

function renderCharts() {
  renderMetricTrendChart()
  renderResourceTypeChart()
  renderAlertLevelChart()
  renderWorkOrderStatusChart()
}

function renderMetricTrendChart() {
  if (!metricTrendChartRef.value) {
    return
  }

  if (!metricTrendChart.value) {
    metricTrendChart.value = echarts.init(metricTrendChartRef.value)
  }

  metricTrendChart.value.setOption({
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      top: 0
    },
    grid: {
      left: 40,
      right: 24,
      top: 45,
      bottom: 35
    },
    xAxis: {
      type: 'category',
      data: metricTrend.value.map((item) => item.statDate)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '指标数量',
        type: 'line',
        smooth: true,
        data: metricTrend.value.map((item) => item.metricTotal)
      },
      {
        name: '异常指标',
        type: 'line',
        smooth: true,
        data: metricTrend.value.map((item) => item.alertMetricTotal)
      }
    ]
  })
}

function renderResourceTypeChart() {
  if (!resourceTypeChartRef.value) {
    return
  }

  if (!resourceTypeChart.value) {
    resourceTypeChart.value = echarts.init(resourceTypeChartRef.value)
  }

  resourceTypeChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '资源类型',
        type: 'pie',
        radius: ['45%', '70%'],
        data: resourceTypeStats.value.map((item) => ({
          name: item.resourceTypeName,
          value: item.total
        }))
      }
    ]
  })
}

function renderAlertLevelChart() {
  if (!alertLevelChartRef.value) {
    return
  }

  if (!alertLevelChart.value) {
    alertLevelChart.value = echarts.init(alertLevelChartRef.value)
  }

  alertLevelChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '告警级别',
        type: 'pie',
        radius: ['45%', '70%'],
        data: alertLevelStats.value.map((item) => ({
          name: item.alertLevelName,
          value: item.total
        }))
      }
    ]
  })
}

function renderWorkOrderStatusChart() {
  if (!workOrderStatusChartRef.value) {
    return
  }

  if (!workOrderStatusChart.value) {
    workOrderStatusChart.value = echarts.init(workOrderStatusChartRef.value)
  }

  workOrderStatusChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '工单状态',
        type: 'pie',
        radius: ['45%', '70%'],
        data: workOrderStatusStats.value.map((item) => ({
          name: item.orderStatusName,
          value: item.total
        }))
      }
    ]
  })
}

function resizeCharts() {
  metricTrendChart.value?.resize()
  resourceTypeChart.value?.resize()
  alertLevelChart.value?.resize()
  workOrderStatusChart.value?.resize()
}

function disposeCharts() {
  metricTrendChart.value?.dispose()
  resourceTypeChart.value?.dispose()
  alertLevelChart.value?.dispose()
  workOrderStatusChart.value?.dispose()

  metricTrendChart.value = null
  resourceTypeChart.value = null
  alertLevelChart.value = null
  workOrderStatusChart.value = null
}

function alertLevelTag(alertLevel: number) {
  if (alertLevel === 3) {
    return 'danger'
  }
  if (alertLevel === 2) {
    return 'warning'
  }
  return 'info'
}

function handleStatusTag(handleStatus: number) {
  if (handleStatus === 0) {
    return 'danger'
  }
  if (handleStatus === 1) {
    return 'warning'
  }
  if (handleStatus === 2) {
    return 'success'
  }
  return 'info'
}

function orderStatusTag(orderStatus: number) {
  if (orderStatus === 0) {
    return 'danger'
  }
  if (orderStatus === 1) {
    return 'warning'
  }
  if (orderStatus === 2) {
    return 'primary'
  }
  if (orderStatus === 3) {
    return 'success'
  }
  return 'info'
}

function goHome() {
  router.push('/dashboard')
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
.aiops-dashboard-page {
  min-height: 100vh;
  padding: 24px;
  background: #f5f7fa;
  box-sizing: border-box;
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
  color: #6b7280;
}

.header-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
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
  margin-top: 10px;
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
  grid-template-columns: minmax(0, 2fr) minmax(320px, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.chart-grid :deep(.el-card) {
  min-width: 0;
}

.chart-box {
  width: 100%;
  height: 320px;
}

.table-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.full-table-card {
  grid-column: 1 / -1;
}

.data-table {
  width: 100%;
}

@media (max-width: 1100px) {
  .chart-grid,
  .table-grid {
    grid-template-columns: 1fr;
  }

  .full-table-card {
    grid-column: auto;
  }
}

@media (max-width: 768px) {
  .aiops-dashboard-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
  }

  .header-actions {
    width: 100%;
  }
}
</style>
