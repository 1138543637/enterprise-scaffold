<template>
  <div class="iam-rate-limit-page">
    <section class="page-header">
      <div>
        <p class="page-subtitle">IAM Security Audit</p>
        <h1>接口限流规则</h1>
        <p class="page-desc">
          当前阶段只实现限流规则管理和模拟检测，不做真实 Filter / Interceptor 强制限流。
        </p>
      </div>
      <div class="header-actions">
        <el-button @click="handleRefresh">刷新</el-button>
        <el-button type="primary" @click="openSimulateDialog">模拟检测</el-button>
      </div>
    </section>

    <section class="stat-grid">
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">规则总数</span>
        <strong>{{ total }}</strong>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">当前页启用</span>
        <strong>{{ enabledCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">当前页停用</span>
        <strong>{{ disabledCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">当前页拒绝动作</span>
        <strong>{{ rejectCount }}</strong>
      </el-card>
    </section>

    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" label-position="top">
        <div class="query-grid">
          <el-form-item label="规则编码">
            <el-input v-model="queryForm.ruleCode" clearable placeholder="例如 RL-IAM" />
          </el-form-item>
          <el-form-item label="规则名称">
            <el-input v-model="queryForm.ruleName" clearable placeholder="例如 异常登录检测" />
          </el-form-item>
          <el-form-item label="接口路径">
            <el-input v-model="queryForm.requestUri" clearable placeholder="/api/iam/**" />
          </el-form-item>
          <el-form-item label="请求方法">
            <el-select v-model="queryForm.requestMethod" clearable placeholder="全部">
              <el-option label="GET" value="GET" />
              <el-option label="POST" value="POST" />
              <el-option label="PUT" value="PUT" />
              <el-option label="DELETE" value="DELETE" />
            </el-select>
          </el-form-item>
          <el-form-item label="限流维度">
            <el-select v-model="queryForm.limitDimension" clearable placeholder="全部">
              <el-option label="全局" value="GLOBAL" />
              <el-option label="用户" value="USER" />
              <el-option label="客户端 IP" value="IP" />
            </el-select>
          </el-form-item>
          <el-form-item label="启用状态">
            <el-select v-model="queryForm.enabled" clearable placeholder="全部">
              <el-option label="启用" :value="1" />
              <el-option label="停用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="createTimeRange"
              type="datetimerange"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              value-format="YYYY-MM-DDTHH:mm:ss"
              clearable
            />
          </el-form-item>
        </div>
        <div class="query-actions">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="records" border>
        <el-table-column prop="ruleCode" label="规则编码" min-width="190" show-overflow-tooltip />
        <el-table-column prop="ruleName" label="规则名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="requestUri" label="接口路径" min-width="260" show-overflow-tooltip />
        <el-table-column prop="requestMethod" label="方法" width="90" />
        <el-table-column prop="limitDimensionName" label="维度" width="110" />
        <el-table-column label="窗口/次数" width="130">
          <template #default="{ row }">
            {{ row.limitWindowSeconds }} 秒 / {{ row.maxRequests }} 次
          </template>
        </el-table-column>
        <el-table-column prop="limitActionName" label="动作" width="110" />
        <el-table-column label="启用状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.enabled === 1 ? 'success' : 'info'">
              {{ row.enabledName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="remark" label="备注" min-width="220" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.enabled === 0"
              link
              type="success"
              @click="handleEnable(row.id)"
            >
              启用
            </el-button>
            <el-button
              v-else
              link
              type="warning"
              @click="handleDisable(row.id)"
            >
              停用
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-row">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          v-model:current-page="queryForm.pageNo"
          v-model:page-size="queryForm.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="fetchList"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="simulateDialogVisible" title="接口限流模拟检测" width="720px">
      <el-form :model="simulateForm" label-position="top">
        <div class="simulate-grid">
          <el-form-item label="接口路径">
            <el-input v-model="simulateForm.requestUri" placeholder="/api/iam/login-risks/detect" />
          </el-form-item>
          <el-form-item label="请求方法">
            <el-select v-model="simulateForm.requestMethod">
              <el-option label="GET" value="GET" />
              <el-option label="POST" value="POST" />
              <el-option label="PUT" value="PUT" />
              <el-option label="DELETE" value="DELETE" />
            </el-select>
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="simulateForm.username" placeholder="admin" />
          </el-form-item>
          <el-form-item label="客户端 IP">
            <el-input v-model="simulateForm.clientIp" placeholder="127.0.0.1" />
          </el-form-item>
          <el-form-item label="当前窗口请求数">
            <el-input-number v-model="simulateForm.currentRequests" :min="0" :max="9999" />
          </el-form-item>
        </div>
      </el-form>

      <el-alert
        v-if="simulateResult"
        class="simulate-result"
        :type="simulateResult.allowed ? 'success' : 'warning'"
        :title="simulateResult.message"
        show-icon
        :closable="false"
      />

      <template #footer>
        <el-button @click="simulateDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="simulateLoading" @click="handleSimulate">
          执行模拟
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  disableIamRateLimitRule,
  enableIamRateLimitRule,
  getIamRateLimitRulePage,
  simulateIamRateLimitRule,
  type IamRateLimitRulePageQuery,
  type IamRateLimitRulePageVO,
  type IamRateLimitRuleSimulateRequest,
  type IamRateLimitRuleSimulateVO
} from '../../api/iam/rateLimitRule'

const loading = ref(false)
const simulateLoading = ref(false)
const records = ref<IamRateLimitRulePageVO[]>([])
const total = ref(0)
const createTimeRange = ref<string[]>([])
const simulateDialogVisible = ref(false)
const simulateResult = ref<IamRateLimitRuleSimulateVO | null>(null)

const queryForm = reactive<IamRateLimitRulePageQuery>({
  pageNo: 1,
  pageSize: 10,
  ruleCode: '',
  ruleName: '',
  requestUri: '',
  requestMethod: '',
  limitDimension: '',
  enabled: undefined,
  beginTime: '',
  endTime: ''
})

const simulateForm = reactive<IamRateLimitRuleSimulateRequest>({
  requestUri: '/api/iam/login-risks/detect',
  requestMethod: 'POST',
  username: 'admin',
  clientIp: '127.0.0.1',
  currentRequests: 1
})

const enabledCount = computed(() =>
  records.value.filter((item) => item.enabled === 1).length
)

const disabledCount = computed(() =>
  records.value.filter((item) => item.enabled === 0).length
)

const rejectCount = computed(() =>
  records.value.filter((item) => item.limitAction === 'REJECT').length
)

function fillTimeRange() {
  queryForm.beginTime = Array.isArray(createTimeRange.value) ? createTimeRange.value[0] || '' : ''
  queryForm.endTime = Array.isArray(createTimeRange.value) ? createTimeRange.value[1] || '' : ''
}

async function fetchList() {
  loading.value = true
  try {
    fillTimeRange()
    const page = await getIamRateLimitRulePage(queryForm)
    records.value = Array.isArray(page.records) ? page.records : []
    total.value = Number(page.total || 0)
  } catch (error) {
    console.error(error)
    records.value = []
    total.value = 0
    ElMessage.error('接口限流规则加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志。')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryForm.pageNo = 1
  fetchList()
}

function handleReset() {
  queryForm.pageNo = 1
  queryForm.pageSize = 10
  queryForm.ruleCode = ''
  queryForm.ruleName = ''
  queryForm.requestUri = ''
  queryForm.requestMethod = ''
  queryForm.limitDimension = ''
  queryForm.enabled = undefined
  queryForm.beginTime = ''
  queryForm.endTime = ''
  createTimeRange.value = []
  fetchList()
}

function handleRefresh() {
  fetchList()
}

function handleSizeChange() {
  queryForm.pageNo = 1
  fetchList()
}

async function handleEnable(id: number) {
  try {
    await enableIamRateLimitRule(id)
    ElMessage.success('启用成功')
    fetchList()
  } catch (error) {
    console.error(error)
    ElMessage.error('启用失败，请检查 Network、ApiResult 解包和后端日志。')
  }
}

async function handleDisable(id: number) {
  try {
    await disableIamRateLimitRule(id)
    ElMessage.success('停用成功')
    fetchList()
  } catch (error) {
    console.error(error)
    ElMessage.error('停用失败，请检查 Network、ApiResult 解包和后端日志。')
  }
}

function openSimulateDialog() {
  simulateResult.value = null
  simulateDialogVisible.value = true
}

async function handleSimulate() {
  simulateLoading.value = true
  try {
    simulateResult.value = await simulateIamRateLimitRule(simulateForm)
    ElMessage.success(simulateResult.value.allowed ? '模拟通过' : '模拟命中限流')
  } catch (error) {
    console.error(error)
    simulateResult.value = null
    ElMessage.error('模拟检测失败，请检查 requestUri、requestMethod、Network 和后端日志。')
  } finally {
    simulateLoading.value = false
  }
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.iam-rate-limit-page {
  padding: 24px;
  background: #f5f7fb;
  min-height: 100vh;
}

.page-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 16px;
  align-items: center;
  margin-bottom: 18px;
}

.page-subtitle {
  margin: 0 0 6px;
  color: #64748b;
  font-size: 13px;
}

.page-header h1 {
  margin: 0;
  font-size: 26px;
  color: #1f2937;
}

.page-desc {
  margin: 8px 0 0;
  color: #64748b;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card :deep(.el-card__body) {
  display: grid;
  gap: 8px;
}

.stat-label {
  color: #64748b;
  font-size: 14px;
}

.stat-card strong {
  font-size: 28px;
  color: #1f2937;
}

.query-card,
.table-card {
  margin-bottom: 16px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 12px 16px;
  align-items: end;
}

.query-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.pagination-row {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.simulate-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 12px 16px;
}

.simulate-result {
  margin-top: 12px;
}

@media (max-width: 768px) {
  .iam-rate-limit-page {
    padding: 14px;
  }

  .page-header {
    grid-template-columns: 1fr;
  }

  .header-actions,
  .query-actions,
  .pagination-row {
    justify-content: flex-start;
    flex-wrap: wrap;
  }
}
</style>
