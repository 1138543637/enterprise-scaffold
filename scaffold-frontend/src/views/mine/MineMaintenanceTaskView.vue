<template>
  <div class="maintenance-task-page">
    <div class="page-header">
      <div>
        <h1>预测性维护任务闭环</h1>
        <p>基于设备健康评分生成维护任务，并完成安排、处理、关闭流程</p>
      </div>

      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button @click="goDeviceHealth">查看设备健康</el-button>
        <el-button type="primary" @click="openCreateDialog">生成维护任务</el-button>
        <el-button :loading="loading" @click="loadData">刷新数据</el-button>
      </div>
    </div>

    <div class="summary-grid" v-loading="summaryLoading">
      <div v-for="item in summaryCards" :key="item.title" class="metric-card">
        <div class="metric-label">{{ item.title }}</div>
        <div class="metric-value">{{ item.value }}</div>
        <div class="metric-desc">{{ item.desc }}</div>
      </div>
    </div>

    <div class="panel">
      <div class="panel-title">查询条件</div>

      <el-form :model="query" label-width="90px" class="query-form">
        <el-row :gutter="16">
          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="任务编码">
              <el-input v-model="query.taskCode" clearable placeholder="请输入任务编码" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="设备编码">
              <el-input v-model="query.deviceCode" clearable placeholder="例如 DEV-FAN-001" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="设备名称">
              <el-input v-model="query.deviceName" clearable placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="设备类型">
              <el-select v-model="query.deviceType" clearable placeholder="请选择设备类型">
                <el-option label="通风机" value="VENTILATION_FAN" />
                <el-option label="水泵" value="WATER_PUMP" />
                <el-option label="运输皮带" value="BELT_CONVEYOR" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="所属区域">
              <el-input v-model="query.areaName" clearable placeholder="请输入区域" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="风险等级">
              <el-select v-model="query.riskLevel" clearable placeholder="请选择风险等级">
                <el-option label="关注" :value="1" />
                <el-option label="风险" :value="2" />
                <el-option label="高危" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="任务状态">
              <el-select v-model="query.taskStatus" clearable placeholder="请选择任务状态">
                <el-option label="待安排" :value="0" />
                <el-option label="待执行" :value="1" />
                <el-option label="处理中" :value="2" />
                <el-option label="已关闭" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="优先级">
              <el-select v-model="query.priority" clearable placeholder="请选择优先级">
                <el-option label="中" :value="1" />
                <el-option label="高" :value="2" />
                <el-option label="紧急" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="有效状态">
              <el-select v-model="query.status" clearable placeholder="请选择有效状态">
                <el-option label="有效" :value="0" />
                <el-option label="无效" :value="1" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :md="8" :lg="6">
            <el-form-item label="操作">
              <el-button type="primary" @click="handleSearch">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <div class="panel">
      <div class="panel-header">
        <div>
          <div class="panel-title no-margin">维护任务列表</div>
          <div class="panel-subtitle">任务状态流转：待安排 -> 待执行 -> 处理中 -> 已关闭</div>
        </div>
      </div>

      <el-table :data="records" border stripe v-loading="loading">
        <el-table-column prop="taskCode" label="任务编码" min-width="170" />
        <el-table-column prop="taskTitle" label="任务标题" min-width="220" />
        <el-table-column prop="deviceCode" label="设备编码" min-width="140" />
        <el-table-column prop="deviceName" label="设备名称" min-width="150" />
        <el-table-column prop="areaName" label="区域" min-width="110" />
        <el-table-column label="健康分" width="90">
          <template #default="{ row }">
            <span>{{ row.healthScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="100">
          <template #default="{ row }">
            <el-tag :type="riskLevelTagType(row.riskLevel)">
              {{ row.riskLevelName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="优先级" width="90">
          <template #default="{ row }">
            <el-tag :type="priorityTagType(row.priority)">
              {{ row.priorityName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="任务状态" width="100">
          <template #default="{ row }">
            <el-tag :type="taskStatusTagType(row.taskStatus)">
              {{ row.taskStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="maintainerUsername" label="维护人员" min-width="110" />
        <el-table-column prop="planStartTime" label="计划开始" min-width="170" />
        <el-table-column prop="planEndTime" label="计划结束" min-width="170" />
        <el-table-column prop="createTime" label="创建时间" min-width="170" />
        <el-table-column label="操作" fixed="right" width="230">
          <template #default="{ row }">
            <el-button size="small" :disabled="row.taskStatus === 3" @click="openPlanDialog(row)">
              安排
            </el-button>
            <el-button size="small" type="warning" :disabled="row.taskStatus === 3" @click="openHandleDialog(row)">
              处理
            </el-button>
            <el-button size="small" type="success" :disabled="row.taskStatus === 3" @click="openCloseDialog(row)">
              关闭
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <el-dialog v-model="createDialogVisible" title="生成预测性维护任务" width="520px">
      <el-form :model="createForm" label-width="110px">
        <el-form-item label="设备ID" required>
          <el-input-number v-model="createForm.deviceId" :min="1" :precision="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="createForm.remark" type="textarea" :rows="3" placeholder="例如：由设备健康风险生成维护任务" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitCreate">生成</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="planDialogVisible" title="安排维护任务" width="560px">
      <el-form :model="planForm" label-width="110px">
        <el-form-item label="计划开始" required>
          <el-date-picker
              v-model="planForm.planStartTime"
              type="datetime"
              value-format="YYYY-MM-DDTHH:mm:ss"
              placeholder="选择计划开始时间"
          />
        </el-form-item>
        <el-form-item label="计划结束" required>
          <el-date-picker
              v-model="planForm.planEndTime"
              type="datetime"
              value-format="YYYY-MM-DDTHH:mm:ss"
              placeholder="选择计划结束时间"
          />
        </el-form-item>
        <el-form-item label="维护人员" required>
          <el-input v-model="planForm.maintainerUsername" placeholder="例如 zhangsan" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="planForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="planDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitPlan">保存安排</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="handleDialogVisible" title="处理维护任务" width="560px">
      <el-form :model="handleForm" label-width="110px">
        <el-form-item label="处理结果" required>
          <el-input v-model="handleForm.handleResult" type="textarea" :rows="4" placeholder="请输入现场处理结果" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="handleForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitHandle">保存处理</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="closeDialogVisible" title="关闭维护任务" width="560px">
      <el-form :model="closeForm" label-width="110px">
        <el-form-item label="关闭结果" required>
          <el-input v-model="closeForm.closeResult" type="textarea" :rows="4" placeholder="请输入关闭结果" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="closeForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="closeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitClose">确认关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  closeMineMaintenanceTaskApi,
  createMineMaintenanceTaskFromDeviceHealthApi,
  getMineMaintenanceTaskPageApi,
  getMineMaintenanceTaskSummaryApi,
  handleMineMaintenanceTaskApi,
  planMineMaintenanceTaskApi,
  type MineMaintenanceTaskSummaryVO,
  type MineMaintenanceTaskVO
} from '../../api/mine/maintenanceTask'

const router = useRouter()

const loading = ref(false)
const summaryLoading = ref(false)
const submitLoading = ref(false)
const records = ref<MineMaintenanceTaskVO[]>([])
const total = ref(0)
const summary = ref<MineMaintenanceTaskSummaryVO | null>(null)
const currentTask = ref<MineMaintenanceTaskVO | null>(null)

const createDialogVisible = ref(false)
const planDialogVisible = ref(false)
const handleDialogVisible = ref(false)
const closeDialogVisible = ref(false)

const query = reactive({
  pageNo: 1,
  pageSize: 10,
  taskCode: '',
  deviceCode: '',
  deviceName: '',
  deviceType: undefined as string | undefined,
  areaName: '',
  riskLevel: undefined as number | undefined,
  taskStatus: undefined as number | undefined,
  priority: undefined as number | undefined,
  status: 0 as number | undefined
})

const createForm = reactive({
  deviceId: 1,
  remark: '由设备健康风险生成预测性维护任务'
})

const planForm = reactive({
  planStartTime: '',
  planEndTime: '',
  maintainerUsername: '',
  remark: ''
})

const handleForm = reactive({
  handleResult: '',
  remark: ''
})

const closeForm = reactive({
  closeResult: '',
  remark: ''
})

const summaryCards = computed(() => {
  const data = summary.value

  return [
    { title: '任务总数', value: data?.taskTotal ?? 0, desc: 'mine_maintenance_task 数量' },
    { title: '待安排', value: data?.pendingTotal ?? 0, desc: 'task_status = 0' },
    { title: '待执行', value: data?.plannedTotal ?? 0, desc: 'task_status = 1' },
    { title: '处理中', value: data?.processingTotal ?? 0, desc: 'task_status = 2' },
    { title: '已关闭', value: data?.closedTotal ?? 0, desc: 'task_status = 3' },
    { title: '高危任务', value: data?.highRiskTaskTotal ?? 0, desc: 'risk_level = 3' },
    { title: '紧急任务', value: data?.urgentTotal ?? 0, desc: 'priority = 3' }
  ]
})

async function loadSummary() {
  summaryLoading.value = true

  try {
    summary.value = await getMineMaintenanceTaskSummaryApi()
  } finally {
    summaryLoading.value = false
  }
}

async function loadPage() {
  loading.value = true

  try {
    const data = await getMineMaintenanceTaskPageApi({
      pageNo: query.pageNo,
      pageSize: query.pageSize,
      taskCode: query.taskCode || undefined,
      deviceCode: query.deviceCode || undefined,
      deviceName: query.deviceName || undefined,
      deviceType: query.deviceType,
      areaName: query.areaName || undefined,
      riskLevel: query.riskLevel,
      taskStatus: query.taskStatus,
      priority: query.priority,
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

function handleSearch() {
  query.pageNo = 1
  loadPage()
}

function handleReset() {
  query.pageNo = 1
  query.pageSize = 10
  query.taskCode = ''
  query.deviceCode = ''
  query.deviceName = ''
  query.deviceType = undefined
  query.areaName = ''
  query.riskLevel = undefined
  query.taskStatus = undefined
  query.priority = undefined
  query.status = 0
  loadPage()
}

function openCreateDialog() {
  createForm.deviceId = 1
  createForm.remark = '由设备健康风险生成预测性维护任务'
  createDialogVisible.value = true
}

function openPlanDialog(row: MineMaintenanceTaskVO) {
  currentTask.value = row
  planForm.planStartTime = ''
  planForm.planEndTime = ''
  planForm.maintainerUsername = row.maintainerUsername || ''
  planForm.remark = ''
  planDialogVisible.value = true
}

function openHandleDialog(row: MineMaintenanceTaskVO) {
  currentTask.value = row
  handleForm.handleResult = ''
  handleForm.remark = ''
  handleDialogVisible.value = true
}

function openCloseDialog(row: MineMaintenanceTaskVO) {
  currentTask.value = row
  closeForm.closeResult = ''
  closeForm.remark = ''
  closeDialogVisible.value = true
}

async function submitCreate() {
  submitLoading.value = true

  try {
    const task = await createMineMaintenanceTaskFromDeviceHealthApi({
      deviceId: createForm.deviceId,
      remark: createForm.remark || undefined
    })

    ElMessage.success(`预测性维护任务已生成：${task.taskCode || '已生成'}`)
    createDialogVisible.value = false
    await loadData()
  } catch (error: any) {
    ElMessage.error(
        error?.response?.data?.msg ||
        error?.message ||
        '生成维护任务失败，请检查设备ID和设备风险等级'
    )
  } finally {
    submitLoading.value = false
  }
}

async function submitPlan() {
  if (!currentTask.value) {
    return
  }

  submitLoading.value = true

  try {
    await planMineMaintenanceTaskApi(currentTask.value.id, {
      planStartTime: planForm.planStartTime,
      planEndTime: planForm.planEndTime,
      maintainerUsername: planForm.maintainerUsername,
      remark: planForm.remark || undefined
    })
    ElMessage.success('维护任务已安排')
    planDialogVisible.value = false
    await loadData()
  } finally {
    submitLoading.value = false
  }
}

async function submitHandle() {
  if (!currentTask.value) {
    return
  }

  submitLoading.value = true

  try {
    await handleMineMaintenanceTaskApi(currentTask.value.id, {
      handleResult: handleForm.handleResult,
      remark: handleForm.remark || undefined
    })
    ElMessage.success('维护任务已处理')
    handleDialogVisible.value = false
    await loadData()
  } finally {
    submitLoading.value = false
  }
}

async function submitClose() {
  if (!currentTask.value) {
    return
  }

  submitLoading.value = true

  try {
    await closeMineMaintenanceTaskApi(currentTask.value.id, {
      closeResult: closeForm.closeResult,
      remark: closeForm.remark || undefined
    })
    ElMessage.success('维护任务已关闭')
    closeDialogVisible.value = false
    await loadData()
  } finally {
    submitLoading.value = false
  }
}

function riskLevelTagType(riskLevel: number) {
  if (riskLevel === 3) {
    return 'danger'
  }
  if (riskLevel === 2) {
    return 'warning'
  }
  return 'info'
}

function priorityTagType(priority: number) {
  if (priority === 3) {
    return 'danger'
  }
  if (priority === 2) {
    return 'warning'
  }
  return 'info'
}

function taskStatusTagType(taskStatus: number) {
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
function goDashboard() {
  router.push('/dashboard')
}
function goDeviceHealth() {
  router.push('/mine/device-health')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.maintenance-task-page {
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

.query-form :deep(.el-select) {
  width: 100%;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .maintenance-task-page {
    padding: 16px;
  }

  .page-header {
    display: block;
  }

  .header-actions {
    justify-content: flex-start;
    margin-top: 16px;
  }

  .pagination-wrapper {
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>