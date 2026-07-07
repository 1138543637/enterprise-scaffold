<template>
  <div class="risk-transaction-page">
    <div class="page-header">
      <div>
        <h1>银行交易模拟</h1>
        <p>模拟银行交易流水，写入 risk_transaction，为后续规则命中、风险评分和人工审核提供数据基础</p>
      </div>

      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button :loading="loading" type="primary" @click="loadData">刷新</el-button>
      </div>
    </div>

    <div class="metric-grid" v-loading="loading">
      <div class="metric-card">
        <div class="metric-label">交易总数</div>
        <div class="metric-value">{{ total }}</div>
        <div class="metric-desc">当前查询条件下的交易流水数量</div>
      </div>
      <div class="metric-card">
        <div class="metric-label">最新交易</div>
        <div class="metric-value">{{ latestRecords.length }}</div>
        <div class="metric-desc">GET /api/risk/transactions/latest</div>
      </div>
      <div class="metric-card">
        <div class="metric-label">最新风险交易</div>
        <div class="metric-value danger">{{ latestRiskTotal }}</div>
        <div class="metric-desc">latest 中 risk_flag = 1 的交易</div>
      </div>
      <div class="metric-card">
        <div class="metric-label">最新成功交易</div>
        <div class="metric-value success">{{ latestSuccessTotal }}</div>
        <div class="metric-desc">latest 中 transaction_status = 0 的交易</div>
      </div>
    </div>

    <div class="panel">
      <div class="panel-header">
        <div>
          <div class="panel-title">模拟交易</div>
          <div class="panel-subtitle">不填账户、客户、交易类型和渠道时，后端会自动生成随机交易</div>
        </div>
      </div>

      <el-form :model="simulateForm" label-width="90px">
        <div class="form-grid">
          <el-form-item label="模拟条数">
            <el-input-number v-model="simulateForm.count" :min="1" :max="100" />
          </el-form-item>

          <el-form-item label="账户号">
            <el-input v-model="simulateForm.accountNo" clearable placeholder="可不填，后端自动生成" />
          </el-form-item>

          <el-form-item label="客户姓名">
            <el-input v-model="simulateForm.customerName" clearable placeholder="可不填，后端自动生成" />
          </el-form-item>

          <el-form-item label="交易类型">
            <el-select v-model="simulateForm.transactionType" clearable placeholder="随机">
              <el-option label="支付 PAYMENT" value="PAYMENT" />
              <el-option label="转账 TRANSFER" value="TRANSFER" />
              <el-option label="取现 WITHDRAW" value="WITHDRAW" />
              <el-option label="消费 CONSUME" value="CONSUME" />
            </el-select>
          </el-form-item>

          <el-form-item label="渠道">
            <el-select v-model="simulateForm.channel" clearable placeholder="随机">
              <el-option label="手机银行 APP" value="APP" />
              <el-option label="网上银行 WEB" value="WEB" />
              <el-option label="ATM" value="ATM" />
              <el-option label="POS" value="POS" />
            </el-select>
          </el-form-item>

          <el-form-item label="金额下限">
            <el-input-number v-model="simulateForm.minAmount" :min="0.01" :precision="2" :step="100" />
          </el-form-item>

          <el-form-item label="金额上限">
            <el-input-number v-model="simulateForm.maxAmount" :min="0.01" :precision="2" :step="100" />
          </el-form-item>

          <el-form-item label="风险标记">
            <el-select v-model="simulateForm.riskFlag" clearable placeholder="后端自动判断">
              <el-option label="未命中风险" :value="0" />
              <el-option label="命中风险" :value="1" />
            </el-select>
          </el-form-item>

          <el-form-item label="操作">
            <el-button :loading="simulateLoading" type="primary" @click="handleSimulate">
              生成交易
            </el-button>
            <el-button @click="resetSimulateForm">重置</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>

    <div class="panel">
      <div class="panel-title">查询条件</div>

      <el-form :model="query" label-width="90px">
        <div class="form-grid">
          <el-form-item label="流水号">
            <el-input v-model="query.transactionNo" clearable placeholder="请输入流水号" />
          </el-form-item>

          <el-form-item label="账户号">
            <el-input v-model="query.accountNo" clearable placeholder="请输入账户号" />
          </el-form-item>

          <el-form-item label="客户姓名">
            <el-input v-model="query.customerName" clearable placeholder="请输入客户姓名" />
          </el-form-item>

          <el-form-item label="商户名称">
            <el-input v-model="query.merchantName" clearable placeholder="请输入商户名称" />
          </el-form-item>

          <el-form-item label="交易类型">
            <el-select v-model="query.transactionType" clearable placeholder="全部">
              <el-option label="支付 PAYMENT" value="PAYMENT" />
              <el-option label="转账 TRANSFER" value="TRANSFER" />
              <el-option label="取现 WITHDRAW" value="WITHDRAW" />
              <el-option label="消费 CONSUME" value="CONSUME" />
            </el-select>
          </el-form-item>

          <el-form-item label="渠道">
            <el-select v-model="query.channel" clearable placeholder="全部">
              <el-option label="APP" value="APP" />
              <el-option label="WEB" value="WEB" />
              <el-option label="ATM" value="ATM" />
              <el-option label="POS" value="POS" />
            </el-select>
          </el-form-item>

          <el-form-item label="交易状态">
            <el-select v-model="query.transactionStatus" clearable placeholder="全部">
              <el-option label="成功" :value="0" />
              <el-option label="失败" :value="1" />
              <el-option label="处理中" :value="2" />
            </el-select>
          </el-form-item>

          <el-form-item label="风险标记">
            <el-select v-model="query.riskFlag" clearable placeholder="全部">
              <el-option label="未命中风险" :value="0" />
              <el-option label="命中风险" :value="1" />
            </el-select>
          </el-form-item>

          <el-form-item label="交易时间" class="range-item">
            <el-date-picker
              v-model="dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>

          <el-form-item label="操作">
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>

    <div class="panel">
      <div class="panel-header">
        <div>
          <div class="panel-title no-margin">交易流水列表</div>
          <div class="panel-subtitle">接口：GET /api/risk/transactions/page，返回 ApiResult + PageResult</div>
        </div>
      </div>

      <el-table :data="records" border stripe v-loading="loading">
        <el-table-column prop="transactionNo" label="流水号" min-width="210" />
        <el-table-column prop="accountNo" label="账户号" min-width="170" />
        <el-table-column prop="customerName" label="客户" min-width="100" />
        <el-table-column prop="merchantName" label="商户" min-width="150" />
        <el-table-column label="交易类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.transactionTypeName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="channel" label="渠道" width="90" />
        <el-table-column label="金额" width="120" align="right">
          <template #default="{ row }">
            {{ row.amount?.toFixed ? row.amount.toFixed(2) : row.amount }} {{ row.currency }}
          </template>
        </el-table-column>
        <el-table-column label="交易状态" width="100">
          <template #default="{ row }">
            <el-tag :type="transactionStatusTagType(row.transactionStatus)">
              {{ row.transactionStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险标记" width="120">
          <template #default="{ row }">
            <el-tag :type="row.riskFlag === 1 ? 'danger' : 'success'">
              {{ row.riskFlagName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="地点" min-width="110" />
        <el-table-column prop="ipAddr" label="IP" min-width="130" />
        <el-table-column prop="transactionTime" label="交易时间" min-width="170" />
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
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
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  getRiskTransactionLatestApi,
  getRiskTransactionPageApi,
  simulateRiskTransactionsApi,
  type RiskTransactionVO
} from '../../api/risk/transaction'

const router = useRouter()

const loading = ref(false)
const simulateLoading = ref(false)
const records = ref<RiskTransactionVO[]>([])
const latestRecords = ref<RiskTransactionVO[]>([])
const total = ref(0)
const dateRange = ref<string[]>([])

const query = reactive({
  pageNo: 1,
  pageSize: 10,
  transactionNo: '',
  accountNo: '',
  customerName: '',
  merchantName: '',
  transactionType: undefined as string | undefined,
  channel: undefined as string | undefined,
  transactionStatus: undefined as number | undefined,
  riskFlag: undefined as number | undefined
})

const simulateForm = reactive({
  count: 10,
  accountNo: '',
  customerName: '',
  transactionType: undefined as string | undefined,
  channel: undefined as string | undefined,
  minAmount: 10,
  maxAmount: 50000,
  riskFlag: undefined as number | undefined
})

function unwrapApiData<T>(response: unknown): T {
  let current = response as any

  if (
      current &&
      typeof current === 'object' &&
      'data' in current &&
      ('status' in current || 'headers' in current || 'config' in current)
  ) {
    current = current.data
  }

  if (
      current &&
      typeof current === 'object' &&
      'data' in current &&
      ('code' in current || 'msg' in current)
  ) {
    current = current.data
  }

  return current as T
}

function toArray<T>(response: unknown): T[] {
  const data = unwrapApiData<unknown>(response)

  if (Array.isArray(data)) {
    return data as T[]
  }

  if (data && typeof data === 'object' && Array.isArray((data as any).records)) {
    return (data as any).records as T[]
  }

  return []
}

const safeLatestRecords = computed(() =>
    Array.isArray(latestRecords.value) ? latestRecords.value : []
)

const latestRiskTotal = computed(() =>
    safeLatestRecords.value.filter((item) => item.riskFlag === 1).length
)

const latestSuccessTotal = computed(() =>
    safeLatestRecords.value.filter((item) => item.transactionStatus === 0).length
)

async function loadLatest() {
  const response = await getRiskTransactionLatestApi()
  latestRecords.value = toArray<RiskTransactionVO>(response)
}

async function loadPage() {
  loading.value = true

  try {
    const response = await getRiskTransactionPageApi({
      pageNo: query.pageNo,
      pageSize: query.pageSize,
      transactionNo: query.transactionNo || undefined,
      accountNo: query.accountNo || undefined,
      customerName: query.customerName || undefined,
      merchantName: query.merchantName || undefined,
      transactionType: query.transactionType,
      channel: query.channel,
      transactionStatus: query.transactionStatus,
      riskFlag: query.riskFlag,
      beginTime: dateRange.value[0] || undefined,
      endTime: dateRange.value[1] || undefined
    })

    const page = unwrapApiData<any>(response)
    records.value = Array.isArray(page?.records) ? page.records : []
    total.value = Number(page?.total || 0)
  } finally {
    loading.value = false
  }
}

async function loadData() {
  await Promise.all([loadLatest(), loadPage()])
}

async function handleSimulate() {
  simulateLoading.value = true

  try {
    const response = await simulateRiskTransactionsApi({
      count: simulateForm.count,
      accountNo: simulateForm.accountNo || undefined,
      customerName: simulateForm.customerName || undefined,
      transactionType: simulateForm.transactionType,
      channel: simulateForm.channel,
      minAmount: simulateForm.minAmount,
      maxAmount: simulateForm.maxAmount,
      riskFlag: simulateForm.riskFlag,
      remark: 'R3-02前端页面模拟生成'
    })

    const result = toArray<RiskTransactionVO>(response)
    ElMessage.success(`已生成 ${result.length} 条交易流水`)
    query.pageNo = 1
    await loadData()
  } finally {
    simulateLoading.value = false
  }
}

function handleSearch() {
  query.pageNo = 1
  loadPage()
}

function handleReset() {
  query.pageNo = 1
  query.pageSize = 10
  query.transactionNo = ''
  query.accountNo = ''
  query.customerName = ''
  query.merchantName = ''
  query.transactionType = undefined
  query.channel = undefined
  query.transactionStatus = undefined
  query.riskFlag = undefined
  dateRange.value = []
  loadPage()
}

function resetSimulateForm() {
  simulateForm.count = 10
  simulateForm.accountNo = ''
  simulateForm.customerName = ''
  simulateForm.transactionType = undefined
  simulateForm.channel = undefined
  simulateForm.minAmount = 10
  simulateForm.maxAmount = 50000
  simulateForm.riskFlag = undefined
}

function transactionStatusTagType(status: number) {
  if (status === 0) {
    return 'success'
  }
  if (status === 2) {
    return 'warning'
  }
  return 'danger'
}

function goDashboard() {
  router.push('/dashboard')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.risk-transaction-page {
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

.metric-value.danger {
  color: #d92d20;
}

.metric-value.success {
  color: #039855;
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
  gap: 12px;
  margin-bottom: 16px;
}

.panel-title {
  margin-bottom: 16px;
  font-size: 18px;
  font-weight: 700;
}

.panel-title.no-margin {
  margin-bottom: 0;
}

.panel-subtitle {
  margin-top: 6px;
  color: #667085;
  font-size: 13px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 0 16px;
}

.range-item {
  grid-column: span 2;
}

.range-item :deep(.el-date-editor) {
  width: 100%;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 1200px) {
  .metric-grid,
  .form-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 768px) {
  .risk-transaction-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
  }

  .header-actions {
    width: 100%;
    justify-content: flex-start;
  }

  .metric-grid,
  .form-grid {
    grid-template-columns: 1fr;
  }

  .range-item {
    grid-column: auto;
  }
}
</style>
