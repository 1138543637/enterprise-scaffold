<template>
  <div class="metric-page">
    <div class="page-header">
      <div>
        <h2>AIOps 指标采集与模拟数据</h2>
        <p>
          基于 AIOps 资源台账模拟 CPU、内存、磁盘、网络、MySQL、Redis 等指标数据，
          为后续告警中心、根因分析和 AIOps 看板提供数据基础。
        </p>
      </div>

      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button @click="goResources">资源管理</el-button>
        <el-button type="primary" @click="openSimulateDialog">模拟生成指标</el-button>
        <el-button :loading="loading" @click="loadData">刷新</el-button>
      </div>
    </div>

    <el-card class="query-card" shadow="never">
      <div class="query-grid">
        <el-input
          v-model="queryForm.resourceCode"
          clearable
          placeholder="资源编码"
          @keyup.enter="handleSearch"
        />

        <el-input
          v-model="queryForm.resourceName"
          clearable
          placeholder="资源名称"
          @keyup.enter="handleSearch"
        />

        <el-select v-model="queryForm.resourceType" clearable placeholder="资源类型">
          <el-option label="服务器" value="SERVER" />
          <el-option label="数据库" value="DATABASE" />
          <el-option label="中间件" value="MIDDLEWARE" />
          <el-option label="网络设备" value="NETWORK" />
        </el-select>

        <el-input
          v-model="queryForm.ipAddr"
          clearable
          placeholder="IP 地址"
          @keyup.enter="handleSearch"
        />

        <el-select v-model="queryForm.metricType" clearable placeholder="指标类型">
          <el-option label="CPU" value="CPU" />
          <el-option label="内存" value="MEMORY" />
          <el-option label="磁盘" value="DISK" />
          <el-option label="网络" value="NETWORK" />
          <el-option label="MySQL" value="MYSQL" />
          <el-option label="Redis" value="REDIS" />
        </el-select>

        <el-select v-model="queryForm.alarmFlag" clearable placeholder="是否告警">
          <el-option label="未告警" :value="0" />
          <el-option label="已告警" :value="1" />
        </el-select>

        <el-select v-model="queryForm.status" clearable placeholder="指标状态">
          <el-option label="正常" :value="0" />
          <el-option label="异常" :value="1" />
        </el-select>

        <div class="query-actions">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
    </el-card>

    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span>最新指标数据</span>
          <span class="header-tip">按资源 + 指标维度展示最新一条数据</span>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="latestRecords"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="resourceCode" label="资源编码" min-width="160" />
        <el-table-column prop="resourceName" label="资源名称" min-width="180" />
        <el-table-column label="资源类型" min-width="120">
          <template #default="{ row }">
            <el-tag>{{ formatResourceType(row.resourceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddr" label="IP 地址" min-width="130" />
        <el-table-column prop="metricName" label="指标名称" min-width="160" />
        <el-table-column label="指标类型" min-width="100">
          <template #default="{ row }">
            <el-tag type="info">{{ formatMetricType(row.metricType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="指标值" min-width="130">
          <template #default="{ row }">
            <span class="metric-value">
              {{ row.metricValue }} {{ row.metricUnit }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="阈值" min-width="110">
          <template #default="{ row }">
            {{ row.thresholdValue }} {{ row.metricUnit }}
          </template>
        </el-table-column>
        <el-table-column label="告警" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.alarmFlag === 1 ? 'danger' : 'success'">
              {{ row.alarmFlag === 1 ? '已告警' : '未告警' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="collectTime" label="采集时间" min-width="180" />
      </el-table>
    </el-card>

    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span>指标历史数据</span>
          <span class="header-tip">分页展示 aiops_metric_data 历史采集记录</span>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="pageRecords"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="resourceCode" label="资源编码" min-width="160" />
        <el-table-column prop="resourceName" label="资源名称" min-width="180" />
        <el-table-column label="资源类型" min-width="120">
          <template #default="{ row }">
            <el-tag>{{ formatResourceType(row.resourceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddr" label="IP 地址" min-width="130" />
        <el-table-column prop="metricCode" label="指标编码" min-width="160" />
        <el-table-column prop="metricName" label="指标名称" min-width="160" />
        <el-table-column label="指标类型" min-width="100">
          <template #default="{ row }">
            <el-tag type="info">{{ formatMetricType(row.metricType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="指标值" min-width="130">
          <template #default="{ row }">
            <span class="metric-value">
              {{ row.metricValue }} {{ row.metricUnit }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="阈值" min-width="110">
          <template #default="{ row }">
            {{ row.thresholdValue }} {{ row.metricUnit }}
          </template>
        </el-table-column>
        <el-table-column label="告警" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.alarmFlag === 1 ? 'danger' : 'success'">
              {{ row.alarmFlag === 1 ? '已告警' : '未告警' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">
              {{ row.status === 0 ? '正常' : '异常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="collectTime" label="采集时间" min-width="180" />
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :current-page="queryForm.pageNo"
          :page-size="queryForm.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="simulateDialogVisible"
      title="模拟生成 AIOps 指标数据"
      width="520px"
      destroy-on-close
    >
      <el-form label-width="120px">
        <el-form-item label="资源ID">
          <el-input-number
            v-model="simulateForm.resourceId"
            :min="1"
            controls-position="right"
            placeholder="可选，指定资源ID"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="资源类型">
          <el-select v-model="simulateForm.resourceType" clearable placeholder="可选，指定资源类型">
            <el-option label="服务器" value="SERVER" />
            <el-option label="数据库" value="DATABASE" />
            <el-option label="中间件" value="MIDDLEWARE" />
            <el-option label="网络设备" value="NETWORK" />
          </el-select>
        </el-form-item>

        <el-form-item label="指标类型">
          <el-select v-model="simulateForm.metricType" clearable placeholder="可选，指定指标类型">
            <el-option label="CPU" value="CPU" />
            <el-option label="内存" value="MEMORY" />
            <el-option label="磁盘" value="DISK" />
            <el-option label="网络" value="NETWORK" />
            <el-option label="MySQL" value="MYSQL" />
            <el-option label="Redis" value="REDIS" />
          </el-select>
        </el-form-item>

        <el-form-item label="生成轮数">
          <el-input-number
            v-model="simulateForm.count"
            :min="1"
            :max="20"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="备注">
          <el-input
            v-model="simulateForm.remark"
            clearable
            placeholder="例如：A2-03 指标模拟数据"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="simulateDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="simulateLoading" @click="submitSimulate">
          确认生成
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  getAiopsMetricDataLatestApi,
  getAiopsMetricDataPageApi,
  simulateAiopsMetricDataApi,
  type AiopsMetricDataPageQuery,
  type AiopsMetricDataSimulateRequest,
  type AiopsMetricDataVO
} from '../../api/aiops/metricData'

const router = useRouter()

const loading = ref(false)
const simulateLoading = ref(false)
const simulateDialogVisible = ref(false)

const latestRecords = ref<AiopsMetricDataVO[]>([])
const pageRecords = ref<AiopsMetricDataVO[]>([])
const total = ref(0)

const queryForm = reactive<AiopsMetricDataPageQuery>({
  pageNo: 1,
  pageSize: 10,
  resourceCode: '',
  resourceName: '',
  resourceType: '',
  ipAddr: '',
  metricType: '',
  alarmFlag: undefined,
  status: undefined
})

const simulateForm = reactive<AiopsMetricDataSimulateRequest>({
  resourceId: undefined,
  resourceType: '',
  metricType: '',
  count: 1,
  remark: 'A2-03 指标模拟数据'
})

async function loadData() {
  loading.value = true
  try {
    const latestData = await getAiopsMetricDataLatestApi(queryForm)
    latestRecords.value = latestData || []

    const pageData = await getAiopsMetricDataPageApi(queryForm)
    pageRecords.value = pageData.records || []
    total.value = pageData.total || 0
  } catch (error) {
    console.error(error)
    ElMessage.error('AIOps 指标数据加载失败，请先检查 F12 Network、ApiResult 解包和 Docker 后端日志')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryForm.pageNo = 1
  loadData()
}

function handleReset() {
  queryForm.pageNo = 1
  queryForm.pageSize = 10
  queryForm.resourceCode = ''
  queryForm.resourceName = ''
  queryForm.resourceType = ''
  queryForm.ipAddr = ''
  queryForm.metricType = ''
  queryForm.alarmFlag = undefined
  queryForm.status = undefined
  loadData()
}

function handleCurrentChange(pageNo: number) {
  queryForm.pageNo = pageNo
  loadData()
}

function handleSizeChange(pageSize: number) {
  queryForm.pageSize = pageSize
  queryForm.pageNo = 1
  loadData()
}

function openSimulateDialog() {
  simulateDialogVisible.value = true
}

async function submitSimulate() {
  simulateLoading.value = true
  try {
    const result = await simulateAiopsMetricDataApi(simulateForm)
    ElMessage.success(`指标模拟生成成功：${result.length} 条`)
    simulateDialogVisible.value = false
    await loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('指标模拟生成失败，请检查资源是否存在、是否启用采集，以及后端日志')
  } finally {
    simulateLoading.value = false
  }
}

function goDashboard() {
  router.push('/dashboard')
}

function goResources() {
  router.push('/aiops/resources')
}

function formatResourceType(value: string) {
  const map: Record<string, string> = {
    SERVER: '服务器',
    DATABASE: '数据库',
    MIDDLEWARE: '中间件',
    NETWORK: '网络设备'
  }
  return map[value] || value || '-'
}

function formatMetricType(value: string) {
  const map: Record<string, string> = {
    CPU: 'CPU',
    MEMORY: '内存',
    DISK: '磁盘',
    NETWORK: '网络',
    MYSQL: 'MySQL',
    REDIS: 'Redis'
  }
  return map[value] || value || '-'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.metric-page {
  min-height: 100vh;
  padding: 24px;
  background: #f5f7fb;
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
  color: #1f2d3d;
}

.page-header p {
  margin: 0;
  color: #606266;
  line-height: 1.6;
}

.header-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-end;
}

.query-card,
.table-card {
  margin-bottom: 16px;
  border-radius: 10px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 14px;
  align-items: center;
}

.query-actions {
  display: flex;
  gap: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
}

.header-tip {
  font-size: 13px;
  color: #909399;
}

.metric-value {
  font-weight: 600;
  color: #303133;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .metric-page {
    padding: 14px;
  }

  .page-header {
    flex-direction: column;
  }

  .header-actions {
    justify-content: flex-start;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .pagination-wrapper {
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>
