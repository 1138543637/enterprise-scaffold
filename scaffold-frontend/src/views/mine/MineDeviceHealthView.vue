<template>
  <div class="device-health-page">
    <div class="page-header">
      <div>
        <h1>设备健康评分与风险等级</h1>
        <p>基于告警事件、工单状态和传感器上报时间实时计算设备健康状态</p>
      </div>

      <div class="header-actions">
        <el-button @click="goMineDashboard">返回矿山看板</el-button>
        <el-button :loading="loading" type="primary" @click="loadData">刷新数据</el-button>
      </div>
    </div>

    <div class="metric-grid" v-loading="summaryLoading">
      <div v-for="item in summaryCards" :key="item.title" class="metric-card">
        <div class="metric-label">{{ item.title }}</div>
        <div class="metric-value">{{ item.value }}</div>
        <div class="metric-desc">{{ item.desc }}</div>
      </div>
    </div>

    <div class="panel">
      <div class="panel-title">查询条件</div>

      <el-form :model="query" label-width="90px" class="query-form">
        <div class="query-grid">
          <el-form-item label="设备编码">
            <el-input v-model="query.deviceCode" clearable placeholder="例如 DEV-FAN-001" />
          </el-form-item>

          <el-form-item label="设备名称">
            <el-input v-model="query.deviceName" clearable placeholder="请输入设备名称" />
          </el-form-item>

          <el-form-item label="设备类型">
            <el-select v-model="query.deviceType" clearable placeholder="请选择设备类型">
              <el-option label="通风机" value="VENTILATION_FAN" />
              <el-option label="水泵" value="WATER_PUMP" />
              <el-option label="运输皮带" value="BELT_CONVEYOR" />
            </el-select>
          </el-form-item>

          <el-form-item label="风险等级">
            <el-select v-model="query.riskLevel" clearable placeholder="请选择风险等级">
              <el-option label="健康" :value="0" />
              <el-option label="关注" :value="1" />
              <el-option label="风险" :value="2" />
              <el-option label="高危" :value="3" />
            </el-select>
          </el-form-item>

          <el-form-item label="所属区域">
            <el-input v-model="query.areaName" clearable placeholder="请输入区域" />
          </el-form-item>

          <el-form-item label="设备状态">
            <el-select v-model="query.status" clearable placeholder="请选择设备状态">
              <el-option label="正常" :value="0" />
              <el-option label="停用" :value="1" />
              <el-option label="维修" :value="2" />
              <el-option label="故障" :value="3" />
            </el-select>
          </el-form-item>

          <el-form-item label="操作" class="query-actions">
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>

    <div class="panel">
      <div class="panel-header">
        <div>
          <div class="panel-title no-margin">设备健康列表</div>
          <div class="panel-subtitle">
            评分规则：基础分 100，最近告警、未处理严重告警、未关闭工单、传感器离线会扣分
          </div>
        </div>
      </div>

      <div class="table-wrapper">
        <el-table :data="records" border stripe v-loading="loading">
          <el-table-column prop="deviceCode" label="设备编码" min-width="140" />
          <el-table-column prop="deviceName" label="设备名称" min-width="160" />
          <el-table-column prop="deviceType" label="设备类型" min-width="140" />
          <el-table-column prop="areaName" label="区域" min-width="110" />
          <el-table-column label="健康分" width="130">
            <template #default="{ row }">
              <el-progress
                  :percentage="Number(row.healthScore ?? 0)"
                  :stroke-width="10"
                  :status="healthProgressStatus(row.healthScore ?? 0)"
              />
            </template>
          </el-table-column>
          <el-table-column label="风险等级" width="100">
            <template #default="{ row }">
              <el-tag :type="riskLevelTagType(row.riskLevel)">
                {{ row.riskLevelName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="alarmCount24h" label="24小时告警" width="110" />
          <el-table-column prop="severeUnhandledAlarmCount" label="未处理严重告警" width="130" />
          <el-table-column prop="unclosedWorkOrderCount" label="未关闭工单" width="110" />
          <el-table-column prop="sensorTotal" label="传感器数" width="100" />
          <el-table-column prop="offlineSensorCount" label="离线传感器" width="110" />
          <el-table-column prop="lastReportTime" label="最后上报时间" min-width="170" />
          <el-table-column label="操作" fixed="right" width="130">
            <template #default="{ row }">
              <el-button
                  size="small"
                  type="primary"
                  :disabled="row.riskLevel === 0"
                  @click="createMaintenanceTask(row)"
              >
                生成维护任务
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="pagination-wrapper">
        <el-pagination
            v-model:current-page="query.pageNo"
            v-model:page-size="query.pageSize"
            :total="total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="loadPage"
            @current-change="loadPage"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  getMineDeviceHealthPageApi,
  getMineDeviceHealthSummaryApi,
  type MineDeviceHealthSummaryVO,
  type MineDeviceHealthVO
} from '../../api/mine/deviceHealth'
import { ElMessage } from 'element-plus'
import { createMineMaintenanceTaskFromDeviceHealthApi } from '../../api/mine/maintenanceTask'

const router = useRouter()

const loading = ref(false)
const summaryLoading = ref(false)
const records = ref<MineDeviceHealthVO[]>([])
const total = ref(0)
const summary = ref<MineDeviceHealthSummaryVO | null>(null)

const query = reactive({
  pageNo: 1,
  pageSize: 10,
  deviceCode: '',
  deviceName: '',
  deviceType: undefined as string | undefined,
  areaName: '',
  riskLevel: undefined as number | undefined,
  status: undefined as number | undefined
})

const summaryCards = computed(() => {
  const data = summary.value

  return [
    { title: '设备总数', value: data?.deviceTotal ?? 0, desc: 'mine_device 设备数量' },
    { title: '平均健康分', value: data?.averageHealthScore ?? 0, desc: '所有设备健康分平均值' },
    { title: '健康设备', value: data?.healthyTotal ?? 0, desc: '健康分 90-100' },
    { title: '关注设备', value: data?.attentionTotal ?? 0, desc: '健康分 70-89' },
    { title: '风险设备', value: data?.riskTotal ?? 0, desc: '健康分 50-69' },
    { title: '高危设备', value: data?.highRiskTotal ?? 0, desc: '健康分 0-49' },
    {
      title: '未处理严重告警',
      value: data?.severeUnhandledAlarmTotal ?? 0,
      desc: 'alarm_level = 3 且 handle_status = 0'
    },
    { title: '未关闭工单', value: data?.unclosedWorkOrderTotal ?? 0, desc: 'order_status = 0/1/2' }
  ]
})

async function loadSummary() {
  summaryLoading.value = true

  try {
    summary.value = await getMineDeviceHealthSummaryApi()
  } finally {
    summaryLoading.value = false
  }
}

async function loadPage() {
  loading.value = true

  try {
    const data = await getMineDeviceHealthPageApi({
      pageNo: query.pageNo,
      pageSize: query.pageSize,
      deviceCode: query.deviceCode || undefined,
      deviceName: query.deviceName || undefined,
      deviceType: query.deviceType,
      areaName: query.areaName || undefined,
      riskLevel: query.riskLevel,
      status: query.status
    })

    records.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

async function loadData() {
  await Promise.all([loadSummary(), loadPage()])
}

async function createMaintenanceTask(row: MineDeviceHealthVO) {
  await createMineMaintenanceTaskFromDeviceHealthApi({
    deviceId: row.id,
    remark: '由设备健康风险生成预测性维护任务'
  })
  ElMessage.success('预测性维护任务已生成')
}

function handleSearch() {
  query.pageNo = 1
  loadPage()
}

function handleReset() {
  query.pageNo = 1
  query.pageSize = 10
  query.deviceCode = ''
  query.deviceName = ''
  query.deviceType = undefined
  query.areaName = ''
  query.riskLevel = undefined
  query.status = undefined
  loadPage()
}

function riskLevelTagType(riskLevel: number) {
  if (riskLevel === 3) {
    return 'danger'
  }
  if (riskLevel === 2) {
    return 'warning'
  }
  if (riskLevel === 1) {
    return 'info'
  }
  return 'success'
}

function healthProgressStatus(score: number) {
  if (score < 50) {
    return 'exception'
  }
  if (score < 70) {
    return 'warning'
  }
  return 'success'
}

function goMineDashboard() {
  router.push('/mine/dashboard')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.device-health-page {
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

.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.metric-card {
  min-height: 118px;
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

.panel {
  margin-bottom: 16px;
  padding: 18px;
  border: 1px solid #e4e7ec;
  border-radius: 8px;
  background: #ffffff;
}

.panel-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 14px;
}

.panel-title {
  margin-bottom: 14px;
  font-size: 16px;
  font-weight: 700;
  color: #101828;
}

.no-margin {
  margin-bottom: 0;
}

.panel-subtitle {
  margin-top: 4px;
  color: #667085;
  font-size: 13px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(220px, 1fr));
  column-gap: 16px;
  row-gap: 4px;
}

.query-form :deep(.el-form-item) {
  margin-bottom: 16px;
}

.query-form :deep(.el-select) {
  width: 100%;
}

.query-actions :deep(.el-form-item__content) {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.table-wrapper {
  width: 100%;
  overflow-x: auto;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 1200px) {
  .metric-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .query-grid {
    grid-template-columns: repeat(3, minmax(220px, 1fr));
  }
}

@media (max-width: 900px) {
  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .query-grid {
    grid-template-columns: repeat(2, minmax(220px, 1fr));
  }
}

@media (max-width: 768px) {
  .device-health-page {
    padding: 16px;
  }

  .page-header {
    display: block;
  }

  .header-actions {
    justify-content: flex-start;
    margin-top: 16px;
  }

  .panel-header {
    display: block;
  }

  .pagination-wrapper {
    justify-content: flex-start;
    overflow-x: auto;
  }
}

@media (max-width: 560px) {
  .metric-grid,
  .query-grid {
    grid-template-columns: 1fr;
  }
}
</style>