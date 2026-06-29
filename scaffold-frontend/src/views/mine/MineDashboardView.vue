<template>
  <div class="mine-dashboard-page">
    <div class="page-header">
      <div>
        <h1>智能矿山安全生产看板</h1>
        <p>展示设备、传感器、告警事件和工单闭环的核心运行状态</p>
      </div>

      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button :loading="actionLoading" type="warning" @click="handleSimulate">
          生成模拟数据
        </el-button>
        <el-button :loading="actionLoading" type="danger" @click="handleGenerateAlarms">
          生成告警事件
        </el-button>
        <el-button :loading="loading" type="primary" @click="loadDashboard">
          刷新看板
        </el-button>
      </div>
    </div>

    <el-row :gutter="16" v-loading="loading">
      <el-col
        v-for="item in metricCards"
        :key="item.title"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <div class="metric-card">
          <div class="metric-label">{{ item.title }}</div>
          <div class="metric-value">{{ item.value }}</div>
          <div class="metric-desc">{{ item.desc }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :md="8">
        <div class="panel">
          <div class="panel-title">告警级别分布</div>
          <div ref="alarmLevelChartRef" class="chart"></div>
        </div>
      </el-col>

      <el-col :xs="24" :md="8">
        <div class="panel">
          <div class="panel-title">传感器类型分布</div>
          <div ref="sensorTypeChartRef" class="chart"></div>
        </div>
      </el-col>

      <el-col :xs="24" :md="8">
        <div class="panel">
          <div class="panel-title">工单状态分布</div>
          <div ref="workOrderStatusChartRef" class="chart"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="table-row">
      <el-col :xs="24" :lg="12">
        <div class="panel">
          <div class="panel-title">最近告警事件</div>

          <el-table :data="recentAlarms" border stripe>
            <el-table-column prop="eventCode" label="告警编号" min-width="150" />
            <el-table-column prop="sensorName" label="传感器" min-width="160" />
            <el-table-column prop="areaName" label="区域" min-width="100" />
            <el-table-column label="级别" width="90">
              <template #default="{ row }">
                <el-tag :type="alarmLevelTagType(row.alarmLevel)">
                  {{ row.alarmLevelName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="handleStatusTagType(row.handleStatus)">
                  {{ row.handleStatusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="alarmTime" label="告警时间" min-width="170" />
          </el-table>
        </div>
      </el-col>

      <el-col :xs="24" :lg="12">
        <div class="panel">
          <div class="panel-title">最近工单记录</div>

          <el-table :data="recentWorkOrders" border stripe>
            <el-table-column prop="workOrderCode" label="工单编号" min-width="150" />
            <el-table-column prop="sensorName" label="传感器" min-width="160" />
            <el-table-column prop="areaName" label="区域" min-width="100" />
            <el-table-column label="级别" width="90">
              <template #default="{ row }">
                <el-tag :type="alarmLevelTagType(row.alarmLevel)">
                  {{ row.alarmLevelName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="orderStatusTagType(row.orderStatus)">
                  {{ row.orderStatusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" min-width="170" />
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import {
  generateMineAlarmEventsApi,
  getMineAlarmLevelStatsApi,
  getMineDashboardSummaryApi,
  getMineRecentAlarmsApi,
  getMineRecentWorkOrdersApi,
  getMineSensorTypeStatsApi,
  getMineWorkOrderStatusStatsApi,
  simulateMineSensorDataApi,
  type MineAlarmLevelStatVO,
  type MineDashboardSummaryVO,
  type MineRecentAlarmVO,
  type MineRecentWorkOrderVO,
  type MineSensorTypeStatVO,
  type MineWorkOrderStatusStatVO
} from '../../api/mine/dashboard'

const router = useRouter()

const loading = ref(false)
const actionLoading = ref(false)

const summary = ref<MineDashboardSummaryVO | null>(null)
const alarmLevelStats = ref<MineAlarmLevelStatVO[]>([])
const sensorTypeStats = ref<MineSensorTypeStatVO[]>([])
const workOrderStatusStats = ref<MineWorkOrderStatusStatVO[]>([])
const recentAlarms = ref<MineRecentAlarmVO[]>([])
const recentWorkOrders = ref<MineRecentWorkOrderVO[]>([])

const alarmLevelChartRef = ref<HTMLDivElement | null>(null)
const sensorTypeChartRef = ref<HTMLDivElement | null>(null)
const workOrderStatusChartRef = ref<HTMLDivElement | null>(null)

const alarmLevelChart = ref<echarts.ECharts | null>(null)
const sensorTypeChart = ref<echarts.ECharts | null>(null)
const workOrderStatusChart = ref<echarts.ECharts | null>(null)

const metricCards = computed(() => {
  const data = summary.value

  return [
    {
      title: '设备总数',
      value: data?.deviceTotal ?? 0,
      desc: 'mine_device 台账数量'
    },
    {
      title: '传感器总数',
      value: data?.sensorTotal ?? 0,
      desc: 'mine_sensor 台账数量'
    },
    {
      title: '采集数据总数',
      value: data?.sensorDataTotal ?? 0,
      desc: 'mine_sensor_data 记录数量'
    },
    {
      title: '告警规则总数',
      value: data?.alarmRuleTotal ?? 0,
      desc: 'mine_alarm_rule 规则数量'
    },
    {
      title: '告警事件总数',
      value: data?.alarmEventTotal ?? 0,
      desc: 'mine_alarm_event 事件数量'
    },
    {
      title: '未处理告警',
      value: data?.unhandledAlarmTotal ?? 0,
      desc: 'handle_status = 0'
    },
    {
      title: '工单总数',
      value: data?.workOrderTotal ?? 0,
      desc: 'mine_work_order 记录数量'
    },
    {
      title: '待处理工单',
      value: data?.pendingWorkOrderTotal ?? 0,
      desc: 'order_status = 0'
    },
    {
      title: '已关闭工单',
      value: data?.closedWorkOrderTotal ?? 0,
      desc: 'order_status = 3'
    }
  ]
})

async function loadDashboard() {
  loading.value = true

  try {
    const [
      summaryData,
      alarmLevelData,
      sensorTypeData,
      workOrderStatusData,
      recentAlarmData,
      recentWorkOrderData
    ] = await Promise.all([
      getMineDashboardSummaryApi(),
      getMineAlarmLevelStatsApi(),
      getMineSensorTypeStatsApi(),
      getMineWorkOrderStatusStatsApi(),
      getMineRecentAlarmsApi(),
      getMineRecentWorkOrdersApi()
    ])

    summary.value = summaryData
    alarmLevelStats.value = alarmLevelData
    sensorTypeStats.value = sensorTypeData
    workOrderStatusStats.value = workOrderStatusData
    recentAlarms.value = recentAlarmData
    recentWorkOrders.value = recentWorkOrderData

    await nextTick()
    renderCharts()
  } finally {
    loading.value = false
  }
}

async function handleSimulate() {
  actionLoading.value = true

  try {
    const data = await simulateMineSensorDataApi({
      count: 5
    })

    ElMessage.success(`已生成 ${data.length} 条模拟传感器数据`)
    await loadDashboard()
  } finally {
    actionLoading.value = false
  }
}

async function handleGenerateAlarms() {
  actionLoading.value = true

  try {
    const data = await generateMineAlarmEventsApi({
      limit: 100
    })

    ElMessage.success(`已生成 ${data.length} 条告警事件`)
    await loadDashboard()
  } finally {
    actionLoading.value = false
  }
}

function renderCharts() {
  renderPieChart(
    alarmLevelChartRef.value,
    alarmLevelChart,
    alarmLevelStats.value.map((item) => ({
      name: item.alarmLevelName,
      value: item.total
    }))
  )

  renderPieChart(
    sensorTypeChartRef.value,
    sensorTypeChart,
    sensorTypeStats.value.map((item) => ({
      name: item.sensorTypeName,
      value: item.total
    }))
  )

  renderPieChart(
    workOrderStatusChartRef.value,
    workOrderStatusChart,
    workOrderStatusStats.value.map((item) => ({
      name: item.orderStatusName,
      value: item.total
    }))
  )
}

function renderPieChart(
  dom: HTMLDivElement | null,
  chartRef: { value: echarts.ECharts | null },
  data: Array<{ name: string; value: number }>
) {
  if (!dom) {
    return
  }

  if (chartRef.value) {
    chartRef.value.dispose()
  }

  chartRef.value = echarts.init(dom)
  chartRef.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: 0
    },
    series: [
      {
        type: 'pie',
        radius: ['42%', '68%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: true,
        data
      }
    ]
  })
}

function resizeCharts() {
  alarmLevelChart.value?.resize()
  sensorTypeChart.value?.resize()
  workOrderStatusChart.value?.resize()
}

function alarmLevelTagType(alarmLevel: number) {
  if (alarmLevel === 3) {
    return 'danger'
  }
  if (alarmLevel === 2) {
    return 'warning'
  }
  return 'info'
}

function handleStatusTagType(handleStatus: number) {
  if (handleStatus === 0) {
    return 'danger'
  }
  if (handleStatus === 1) {
    return 'warning'
  }
  return 'success'
}

function orderStatusTagType(orderStatus: number) {
  if (orderStatus === 0) {
    return 'danger'
  }
  if (orderStatus === 1) {
    return 'warning'
  }
  if (orderStatus === 2) {
    return 'primary'
  }
  return 'success'
}

function goDashboard() {
  router.push('/dashboard')
}

onMounted(() => {
  loadDashboard()
  window.addEventListener('resize', resizeCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  alarmLevelChart.value?.dispose()
  sensorTypeChart.value?.dispose()
  workOrderStatusChart.value?.dispose()
})
</script>

<style scoped>
.mine-dashboard-page {
  min-height: 100vh;
  padding: 24px;
  background: #f5f7fb;
  color: #1f2937;
}

.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}

.page-header p {
  margin: 8px 0 0;
  color: #667085;
}

.header-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 8px;
}

.metric-card {
  min-height: 118px;
  margin-bottom: 16px;
  padding: 18px;
  border: 1px solid #e4e7ec;
  border-radius: 8px;
  background: #ffffff;
}

.metric-label {
  color: #667085;
  font-size: 14px;
}

.metric-value {
  margin-top: 10px;
  font-size: 30px;
  font-weight: 700;
  color: #101828;
}

.metric-desc {
  margin-top: 8px;
  font-size: 13px;
  color: #98a2b3;
}

.chart-row,
.table-row {
  margin-top: 4px;
}

.panel {
  margin-bottom: 16px;
  padding: 18px;
  border: 1px solid #e4e7ec;
  border-radius: 8px;
  background: #ffffff;
}

.panel-title {
  margin-bottom: 14px;
  font-size: 16px;
  font-weight: 700;
  color: #101828;
}

.chart {
  width: 100%;
  height: 320px;
}

@media (max-width: 768px) {
  .mine-dashboard-page {
    padding: 16px;
  }

  .page-header {
    display: block;
  }

  .header-actions {
    justify-content: flex-start;
    margin-top: 16px;
  }
}
</style>
