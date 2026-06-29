<template>
  <div class="maintenance-dashboard-page">
    <div class="page-header">
      <div>
        <h2>预测性维护看板</h2>
        <p>维护任务统计、风险趋势分析、高风险设备维护任务展示</p>
      </div>

      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button @click="goDeviceHealth">设备健康</el-button>
        <el-button @click="goMaintenanceTasks">维护任务</el-button>
        <el-button type="primary" :loading="loading" @click="loadDashboard">刷新看板</el-button>
      </div>
    </div>

    <div class="summary-grid">
      <div class="metric-card">
        <div class="metric-label">维护任务总数</div>
        <div class="metric-value">{{ summary.taskTotal }}</div>
        <div class="metric-desc">全部有效维护任务</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">未关闭任务</div>
        <div class="metric-value danger">{{ summary.unclosedTaskTotal }}</div>
        <div class="metric-desc">待安排 / 待执行 / 处理中</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">待安排</div>
        <div class="metric-value warning">{{ summary.pendingTotal }}</div>
        <div class="metric-desc">task_status = 0</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">待执行</div>
        <div class="metric-value primary">{{ summary.plannedTotal }}</div>
        <div class="metric-desc">task_status = 1</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">处理中</div>
        <div class="metric-value primary">{{ summary.processingTotal }}</div>
        <div class="metric-desc">task_status = 2</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">已关闭</div>
        <div class="metric-value success">{{ summary.closedTotal }}</div>
        <div class="metric-desc">task_status = 3</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">高危任务</div>
        <div class="metric-value danger">{{ summary.highRiskTaskTotal }}</div>
        <div class="metric-desc">risk_level = 3</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">紧急任务</div>
        <div class="metric-value danger">{{ summary.urgentTotal }}</div>
        <div class="metric-desc">priority = 3</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">今日新增</div>
        <div class="metric-value">{{ summary.todayNewTaskTotal }}</div>
        <div class="metric-desc">今日新建维护任务</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">7天告警</div>
        <div class="metric-value warning">{{ summary.alarmTotal7d }}</div>
        <div class="metric-desc">最近 7 天告警事件</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">7天工单</div>
        <div class="metric-value primary">{{ summary.workOrderTotal7d }}</div>
        <div class="metric-desc">最近 7 天工单记录</div>
      </div>
    </div>

    <div class="chart-grid">
      <el-card shadow="never">
        <template #header>
          <span>最近 7 天风险趋势</span>
        </template>
        <div ref="riskTrendChartRef" class="chart-box"></div>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <span>维护任务状态分布</span>
        </template>
        <div ref="taskStatusChartRef" class="chart-box"></div>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <span>维护任务优先级分布</span>
        </template>
        <div ref="priorityChartRef" class="chart-box"></div>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <span>风险等级分布</span>
        </template>
        <div ref="riskLevelChartRef" class="chart-box"></div>
      </el-card>
    </div>

    <div class="table-grid">
      <el-card shadow="never">
        <template #header>
          <span>高风险设备维护任务</span>
        </template>

        <div class="table-wrapper">
          <el-table :data="highRiskDevices" border stripe>
            <el-table-column prop="taskCode" label="任务编码" min-width="160" />
            <el-table-column prop="deviceCode" label="设备编码" min-width="140" />
            <el-table-column prop="deviceName" label="设备名称" min-width="160" />
            <el-table-column prop="areaName" label="区域" min-width="120" />
            <el-table-column prop="healthScore" label="健康分" min-width="90" />
            <el-table-column label="风险等级" min-width="100">
              <template #default="{ row }">
                <el-tag :type="riskTagType(row.riskLevel)">
                  {{ row.riskLevelName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="优先级" min-width="100">
              <template #default="{ row }">
                <el-tag :type="priorityTagType(row.priority)">
                  {{ row.priorityName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="任务状态" min-width="110">
              <template #default="{ row }">
                <el-tag :type="taskStatusTagType(row.taskStatus)">
                  {{ row.taskStatusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" min-width="180" />
          </el-table>
        </div>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <span>最近维护任务</span>
        </template>

        <div class="table-wrapper">
          <el-table :data="recentTasks" border stripe>
            <el-table-column prop="taskCode" label="任务编码" min-width="160" />
            <el-table-column prop="deviceCode" label="设备编码" min-width="140" />
            <el-table-column prop="deviceName" label="设备名称" min-width="160" />
            <el-table-column prop="areaName" label="区域" min-width="120" />
            <el-table-column prop="healthScore" label="健康分" min-width="90" />
            <el-table-column label="风险等级" min-width="100">
              <template #default="{ row }">
                <el-tag :type="riskTagType(row.riskLevel)">
                  {{ row.riskLevelName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="优先级" min-width="100">
              <template #default="{ row }">
                <el-tag :type="priorityTagType(row.priority)">
                  {{ row.priorityName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="任务状态" min-width="110">
              <template #default="{ row }">
                <el-tag :type="taskStatusTagType(row.taskStatus)">
                  {{ row.taskStatusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" min-width="180" />
          </el-table>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted, reactive, ref, shallowRef } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import { ElMessage } from 'element-plus'
import {
  getMaintenanceDashboardSummaryApi,
  getMaintenanceHighRiskDevicesApi,
  getMaintenancePriorityStatsApi,
  getMaintenanceRecentTasksApi,
  getMaintenanceRiskLevelStatsApi,
  getMaintenanceRiskTrendApi,
  getMaintenanceTaskStatusStatsApi,
  type MineMaintenanceDashboardSummaryVO,
  type MineMaintenanceHighRiskDeviceVO,
  type MineMaintenancePriorityStatVO,
  type MineMaintenanceRecentTaskVO,
  type MineMaintenanceRiskLevelStatVO,
  type MineMaintenanceRiskTrendVO,
  type MineMaintenanceTaskStatusStatVO
} from '../../api/mine/maintenanceDashboard'

const router = useRouter()
const loading = ref(false)

const summary = reactive<MineMaintenanceDashboardSummaryVO>({
  taskTotal: 0,
  unclosedTaskTotal: 0,
  pendingTotal: 0,
  plannedTotal: 0,
  processingTotal: 0,
  closedTotal: 0,
  highRiskTaskTotal: 0,
  urgentTotal: 0,
  todayNewTaskTotal: 0,
  alarmTotal7d: 0,
  workOrderTotal7d: 0
})

const taskStatusStats = ref<MineMaintenanceTaskStatusStatVO[]>([])
const priorityStats = ref<MineMaintenancePriorityStatVO[]>([])
const riskLevelStats = ref<MineMaintenanceRiskLevelStatVO[]>([])
const riskTrend = ref<MineMaintenanceRiskTrendVO[]>([])
const recentTasks = ref<MineMaintenanceRecentTaskVO[]>([])
const highRiskDevices = ref<MineMaintenanceHighRiskDeviceVO[]>([])

const riskTrendChartRef = ref<HTMLDivElement | null>(null)
const taskStatusChartRef = ref<HTMLDivElement | null>(null)
const priorityChartRef = ref<HTMLDivElement | null>(null)
const riskLevelChartRef = ref<HTMLDivElement | null>(null)

const riskTrendChart = shallowRef<ECharts | null>(null)
const taskStatusChart = shallowRef<ECharts | null>(null)
const priorityChart = shallowRef<ECharts | null>(null)
const riskLevelChart = shallowRef<ECharts | null>(null)

const loadDashboard = async () => {
  loading.value = true
  try {
    const [
      summaryData,
      taskStatusData,
      priorityData,
      riskLevelData,
      riskTrendData,
      recentTaskData,
      highRiskDeviceData
    ] = await Promise.all([
      getMaintenanceDashboardSummaryApi(),
      getMaintenanceTaskStatusStatsApi(),
      getMaintenancePriorityStatsApi(),
      getMaintenanceRiskLevelStatsApi(),
      getMaintenanceRiskTrendApi(),
      getMaintenanceRecentTasksApi(),
      getMaintenanceHighRiskDevicesApi()
    ])

    Object.assign(summary, summaryData || {})
    taskStatusStats.value = taskStatusData || []
    priorityStats.value = priorityData || []
    riskLevelStats.value = riskLevelData || []
    riskTrend.value = riskTrendData || []
    recentTasks.value = recentTaskData || []
    highRiskDevices.value = highRiskDeviceData || []

    await nextTick()
    renderCharts()
  } catch (error) {
    console.error(error)
    ElMessage.error('维护看板加载失败')
  } finally {
    loading.value = false
  }
}

const renderCharts = () => {
  renderRiskTrendChart()
  renderTaskStatusChart()
  renderPriorityChart()
  renderRiskLevelChart()
}

const renderRiskTrendChart = () => {
  if (!riskTrendChartRef.value) {
    return
  }

  if (!riskTrendChart.value) {
    riskTrendChart.value = echarts.init(riskTrendChartRef.value)
  }

  const dates = riskTrend.value.map((item) => item.statDate)
  riskTrendChart.value.setOption({
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      top: 0
    },
    grid: {
      top: 48,
      left: 40,
      right: 20,
      bottom: 32
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '告警数量',
        type: 'line',
        smooth: true,
        data: riskTrend.value.map((item) => item.alarmTotal)
      },
      {
        name: '工单数量',
        type: 'line',
        smooth: true,
        data: riskTrend.value.map((item) => item.workOrderTotal)
      },
      {
        name: '维护任务',
        type: 'line',
        smooth: true,
        data: riskTrend.value.map((item) => item.maintenanceTaskTotal)
      },
      {
        name: '高危任务',
        type: 'line',
        smooth: true,
        data: riskTrend.value.map((item) => item.highRiskTaskTotal)
      }
    ]
  })
}

const renderTaskStatusChart = () => {
  if (!taskStatusChartRef.value) {
    return
  }

  if (!taskStatusChart.value) {
    taskStatusChart.value = echarts.init(taskStatusChartRef.value)
  }

  taskStatusChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: 0
    },
    series: [
      {
        name: '任务状态',
        type: 'pie',
        radius: ['35%', '62%'],
        center: ['50%', '45%'],
        data: taskStatusStats.value.map((item) => ({
          name: item.taskStatusName,
          value: item.total
        }))
      }
    ]
  })
}

const renderPriorityChart = () => {
  if (!priorityChartRef.value) {
    return
  }

  if (!priorityChart.value) {
    priorityChart.value = echarts.init(priorityChartRef.value)
  }

  priorityChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: 0
    },
    series: [
      {
        name: '优先级',
        type: 'pie',
        radius: ['35%', '62%'],
        center: ['50%', '45%'],
        data: priorityStats.value.map((item) => ({
          name: item.priorityName,
          value: item.total
        }))
      }
    ]
  })
}

const renderRiskLevelChart = () => {
  if (!riskLevelChartRef.value) {
    return
  }

  if (!riskLevelChart.value) {
    riskLevelChart.value = echarts.init(riskLevelChartRef.value)
  }

  riskLevelChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: 0
    },
    series: [
      {
        name: '风险等级',
        type: 'pie',
        radius: ['35%', '62%'],
        center: ['50%', '45%'],
        data: riskLevelStats.value.map((item) => ({
          name: item.riskLevelName,
          value: item.total
        }))
      }
    ]
  })
}

const resizeCharts = () => {
  riskTrendChart.value?.resize()
  taskStatusChart.value?.resize()
  priorityChart.value?.resize()
  riskLevelChart.value?.resize()
}

const disposeCharts = () => {
  riskTrendChart.value?.dispose()
  taskStatusChart.value?.dispose()
  priorityChart.value?.dispose()
  riskLevelChart.value?.dispose()

  riskTrendChart.value = null
  taskStatusChart.value = null
  priorityChart.value = null
  riskLevelChart.value = null
}

const riskTagType = (riskLevel: number) => {
  if (riskLevel === 3) {
    return 'danger'
  }
  if (riskLevel === 2) {
    return 'warning'
  }
  if (riskLevel === 1) {
    return 'primary'
  }
  return 'success'
}

const priorityTagType = (priority: number) => {
  if (priority === 3) {
    return 'danger'
  }
  if (priority === 2) {
    return 'warning'
  }
  if (priority === 1) {
    return 'primary'
  }
  return 'info'
}

const taskStatusTagType = (taskStatus: number) => {
  if (taskStatus === 3) {
    return 'success'
  }
  if (taskStatus === 2) {
    return 'warning'
  }
  if (taskStatus === 1) {
    return 'primary'
  }
  return 'info'
}

const goDashboard = () => {
  router.push('/dashboard')
}

const goDeviceHealth = () => {
  router.push('/mine/device-health')
}

const goMaintenanceTasks = () => {
  router.push('/mine/maintenance-tasks')
}

onMounted(async () => {
  await loadDashboard()
  window.addEventListener('resize', resizeCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  disposeCharts()
})
</script>

<style scoped>
.maintenance-dashboard-page {
  min-height: 100vh;
  padding: 24px;
  background: #f5f7fa;
}

.page-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 16px;
}

.page-header h2 {
  margin: 0 0 8px;
  font-size: 24px;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
}

.header-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.metric-card {
  min-height: 118px;
  padding: 18px;
  border: 1px solid #e4e7ec;
  border-radius: 8px;
  background: #ffffff;
  box-sizing: border-box;
}

.metric-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 12px;
}

.metric-value {
  font-size: 30px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 8px;
}

.metric-value.primary {
  color: #409eff;
}

.metric-value.success {
  color: #67c23a;
}

.metric-value.warning {
  color: #e6a23c;
}

.metric-value.danger {
  color: #f56c6c;
}

.metric-desc {
  font-size: 12px;
  color: #909399;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(360px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.chart-box {
  width: 100%;
  height: 320px;
}

.table-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(520px, 1fr));
  gap: 16px;
}

.table-wrapper {
  width: 100%;
  overflow-x: auto;
}

@media (max-width: 768px) {
  .maintenance-dashboard-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
  }

  .header-actions {
    justify-content: flex-start;
  }

  .chart-grid,
  .table-grid {
    grid-template-columns: 1fr;
  }

  .chart-box {
    height: 280px;
  }
}
</style>
