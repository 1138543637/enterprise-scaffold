<template>
  <div class="page-container">
    <div class="page-header">
      <div>
        <h2>AIOps 根因分析</h2>
        <p>根据告警事件回看指标、同资源告警、同系统告警和工单记录，生成可解释的疑似根因。</p>
      </div>
      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button type="primary" :loading="loading" @click="loadPage">刷新</el-button>
      </div>
    </div>

    <el-card class="section-card">
      <template #header>
        <span>执行根因分析</span>
      </template>

      <div class="analyze-grid">
        <el-input-number
          v-model="analyzeForm.alertEventId"
          :min="1"
          controls-position="right"
          placeholder="告警事件ID"
          class="full-width"
        />
        <el-input-number
          v-model="analyzeForm.lookbackMinutes"
          :min="5"
          :max="1440"
          controls-position="right"
          placeholder="回看分钟数"
          class="full-width"
        />
        <el-input
          v-model="analyzeForm.remark"
          clearable
          placeholder="备注，例如：由告警中心触发分析"
        />
        <el-button type="success" :loading="analyzeLoading" @click="handleAnalyze">
          执行分析
        </el-button>
      </div>
    </el-card>

    <el-card class="section-card">
      <template #header>
        <span>查询条件</span>
      </template>

      <div class="query-grid">
        <el-input v-model="queryForm.analysisCode" clearable placeholder="分析编码" />
        <el-input v-model="queryForm.eventCode" clearable placeholder="告警事件编码" />
        <el-input v-model="queryForm.resourceCode" clearable placeholder="资源编码" />
        <el-input v-model="queryForm.resourceName" clearable placeholder="资源名称" />
        <el-select v-model="queryForm.resourceType" clearable placeholder="资源类型">
          <el-option label="服务器" value="SERVER" />
          <el-option label="数据库" value="DATABASE" />
          <el-option label="中间件" value="MIDDLEWARE" />
          <el-option label="网络设备" value="NETWORK" />
        </el-select>
        <el-input v-model="queryForm.ipAddr" clearable placeholder="IP 地址" />
        <el-select v-model="queryForm.rootCauseType" clearable placeholder="根因类型">
          <el-option label="CPU 过高" value="CPU_HIGH" />
          <el-option label="内存过高" value="MEMORY_HIGH" />
          <el-option label="磁盘空间不足" value="DISK_FULL" />
          <el-option label="网络异常" value="NETWORK_ABNORMAL" />
          <el-option label="数据库慢" value="DATABASE_SLOW" />
          <el-option label="未知" value="UNKNOWN" />
        </el-select>
        <el-select v-model="queryForm.status" clearable placeholder="状态">
          <el-option label="有效" :value="0" />
          <el-option label="无效" :value="1" />
        </el-select>
        <div class="query-actions">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
    </el-card>

    <el-card class="section-card">
      <template #header>
        <span>根因分析记录</span>
      </template>

      <div class="table-wrapper">
        <el-table
          v-loading="loading"
          :data="records"
          row-key="id"
          border
          style="width: 100%"
        >
          <el-table-column prop="analysisCode" label="分析编码" min-width="190" />
          <el-table-column prop="eventCode" label="告警事件" min-width="160" />
          <el-table-column prop="resourceCode" label="资源编码" min-width="160" />
          <el-table-column prop="resourceName" label="资源名称" min-width="180" />
          <el-table-column prop="ipAddr" label="IP 地址" min-width="130" />
          <el-table-column prop="rootCauseType" label="根因类型" min-width="150">
            <template #default="{ row }">
              <el-tag :type="rootCauseTagType(row.rootCauseType)">
                {{ rootCauseTypeName(row.rootCauseType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="confidenceScore" label="置信度" min-width="150">
            <template #default="{ row }">
              <el-progress
                :percentage="row.confidenceScore || 0"
                :stroke-width="10"
                :show-text="true"
              />
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" min-width="90">
            <template #default="{ row }">
              <el-tag :type="row.status === 0 ? 'success' : 'info'">
                {{ row.status === 0 ? '有效' : '无效' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="analysisTime" label="分析时间" min-width="170" />
          <el-table-column fixed="right" label="操作" width="110">
            <template #default="{ row }">
              <el-button link type="primary" @click="openDetail(row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryForm.pageNo"
          v-model:page-size="queryForm.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-drawer
      v-model="detailVisible"
      title="根因分析详情"
      size="520px"
      destroy-on-close
    >
      <template v-if="detailRecord">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="分析编码">
            {{ detailRecord.analysisCode }}
          </el-descriptions-item>
          <el-descriptions-item label="告警事件">
            {{ detailRecord.eventCode }}
          </el-descriptions-item>
          <el-descriptions-item label="资源">
            {{ detailRecord.resourceName }}（{{ detailRecord.resourceCode }}）
          </el-descriptions-item>
          <el-descriptions-item label="IP 地址">
            {{ detailRecord.ipAddr }}
          </el-descriptions-item>
          <el-descriptions-item label="根因类型">
            {{ rootCauseTypeName(detailRecord.rootCauseType) }}
          </el-descriptions-item>
          <el-descriptions-item label="置信度">
            {{ detailRecord.confidenceScore }}%
          </el-descriptions-item>
          <el-descriptions-item label="根因描述">
            {{ detailRecord.rootCauseDesc }}
          </el-descriptions-item>
          <el-descriptions-item label="处理建议">
            {{ detailRecord.suggestion }}
          </el-descriptions-item>
          <el-descriptions-item label="备注">
            {{ detailRecord.remark || '-' }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="detail-block">
          <h4>分析证据</h4>
          <pre>{{ detailRecord.evidence }}</pre>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  analyzeAiopsRootCauseApi,
  getAiopsRootCauseDetailApi,
  getAiopsRootCausePageApi,
  type AiopsRootCauseAnalyzeRequest,
  type AiopsRootCausePageQuery,
  type AiopsRootCauseVO
} from '../../api/aiops/rootCause'

const router = useRouter()

const loading = ref(false)
const analyzeLoading = ref(false)
const records = ref<AiopsRootCauseVO[]>([])
const total = ref(0)
const detailVisible = ref(false)
const detailRecord = ref<AiopsRootCauseVO | null>(null)

const analyzeForm = reactive<AiopsRootCauseAnalyzeRequest>({
  alertEventId: undefined,
  lookbackMinutes: 30,
  remark: ''
})

const queryForm = reactive<AiopsRootCausePageQuery>({
  pageNo: 1,
  pageSize: 10,
  analysisCode: '',
  eventCode: '',
  resourceCode: '',
  resourceName: '',
  resourceType: '',
  ipAddr: '',
  rootCauseType: '',
  status: undefined
})

function goDashboard() {
  router.push('/dashboard')
}

async function loadPage() {
  loading.value = true
  try {
    const pageData = await getAiopsRootCausePageApi(queryForm)
    records.value = pageData.records || []
    total.value = pageData.total || 0
  } catch (error) {
    console.error(error)
    ElMessage.error('AIOps 根因分析记录加载失败')
  } finally {
    loading.value = false
  }
}

async function handleAnalyze() {
  if (!analyzeForm.alertEventId) {
    ElMessage.warning('请先输入告警事件ID')
    return
  }

  analyzeLoading.value = true
  try {
    const result = await analyzeAiopsRootCauseApi(analyzeForm)
    ElMessage.success(`根因分析完成：${result.analysisCode || '已生成'}`)
    detailRecord.value = result
    detailVisible.value = true
    await loadPage()
  } catch (error) {
    console.error(error)
    ElMessage.error('根因分析失败，请先检查告警事件ID是否存在')
  } finally {
    analyzeLoading.value = false
  }
}

function handleSearch() {
  queryForm.pageNo = 1
  loadPage()
}

function handleReset() {
  queryForm.pageNo = 1
  queryForm.pageSize = 10
  queryForm.analysisCode = ''
  queryForm.eventCode = ''
  queryForm.resourceCode = ''
  queryForm.resourceName = ''
  queryForm.resourceType = ''
  queryForm.ipAddr = ''
  queryForm.rootCauseType = ''
  queryForm.status = undefined
  loadPage()
}

function handleSizeChange(size: number) {
  queryForm.pageSize = size
  queryForm.pageNo = 1
  loadPage()
}

function handleCurrentChange(pageNo: number) {
  queryForm.pageNo = pageNo
  loadPage()
}

async function openDetail(row: AiopsRootCauseVO) {
  try {
    detailRecord.value = await getAiopsRootCauseDetailApi(row.id)
    detailVisible.value = true
  } catch (error) {
    console.error(error)
    ElMessage.error('根因分析详情加载失败')
  }
}

function rootCauseTypeName(type: string) {
  const map: Record<string, string> = {
    CPU_HIGH: 'CPU 过高',
    MEMORY_HIGH: '内存过高',
    DISK_FULL: '磁盘空间不足',
    NETWORK_ABNORMAL: '网络异常',
    DATABASE_SLOW: '数据库慢',
    UNKNOWN: '未知'
  }
  return map[type] || type || '-'
}

function rootCauseTagType(type: string) {
  if (type === 'UNKNOWN') {
    return 'info'
  }
  if (type === 'CPU_HIGH' || type === 'MEMORY_HIGH') {
    return 'danger'
  }
  if (type === 'DISK_FULL' || type === 'DATABASE_SLOW') {
    return 'warning'
  }
  return 'primary'
}

onMounted(() => {
  loadPage()
})
</script>

<style scoped>
.page-container {
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
  color: #667085;
}

.header-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.section-card {
  margin-bottom: 16px;
}

.analyze-grid {
  display: grid;
  grid-template-columns: minmax(180px, 240px) minmax(180px, 240px) minmax(240px, 1fr) 120px;
  gap: 14px;
  align-items: center;
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
  flex-wrap: wrap;
}

.full-width {
  width: 100%;
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

.detail-block {
  margin-top: 18px;
}

.detail-block h4 {
  margin: 0 0 8px;
  color: #1f2d3d;
}

.detail-block pre {
  padding: 12px;
  white-space: pre-wrap;
  word-break: break-word;
  background: #f5f7fb;
  border: 1px solid #e4e7ec;
  border-radius: 6px;
  line-height: 1.7;
  color: #344054;
}

@media (max-width: 900px) {
  .page-header {
    flex-direction: column;
  }

  .analyze-grid {
    grid-template-columns: 1fr;
  }
}
</style>
