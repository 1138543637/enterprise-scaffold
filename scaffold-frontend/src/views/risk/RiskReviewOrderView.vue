<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  approveRiskReviewOrderApi,
  createRiskReviewOrderFromTransactionApi,
  getRiskReviewOrderPageApi,
  getRiskReviewSummaryApi,
  rejectRiskReviewOrderApi,
  type RiskReviewOrderPageQuery,
  type RiskReviewOrderPageVO,
  type RiskReviewSummaryVO,
} from '../../api/risk/reviewOrder'

const loading = ref(false)
const generateLoading = ref(false)
const approveDialogVisible = ref(false)
const rejectDialogVisible = ref(false)
const currentOrder = ref<RiskReviewOrderPageVO | null>(null)
const records = ref<RiskReviewOrderPageVO[]>([])
const total = ref(0)

const query = reactive<RiskReviewOrderPageQuery>({
  pageNo: 1,
  pageSize: 10,
  reviewOrderCode: '',
  transactionNo: '',
  accountNo: '',
  customerName: '',
  riskLevel: undefined,
  riskResult: '',
  reviewStatus: undefined,
})

const generateForm = reactive({
  transactionId: undefined as number | undefined,
  limit: 50,
})

const reviewForm = reactive({
  reviewerUserId: 1,
  reviewerUsername: 'admin',
  reviewResult: '',
})

const summary = ref<RiskReviewSummaryVO>({
  totalOrderCount: 0,
  pendingCount: 0,
  approvedCount: 0,
  rejectedCount: 0,
  lowRiskCount: 0,
  mediumRiskCount: 0,
  highRiskCount: 0,
  reviewResultReviewCount: 0,
  reviewResultRejectCount: 0,
  todayOrderCount: 0,
  averageScore: 0,
})

const safeRecords = computed(() => (Array.isArray(records.value) ? records.value : []))

const pendingTotal = computed(() =>
  safeRecords.value.filter((item) => item.reviewStatus === 0).length,
)

const highRiskTotal = computed(() =>
  safeRecords.value.filter((item) => item.riskLevel === 3).length,
)

function riskLevelText(level?: number) {
  if (level === 3) {
    return '高风险'
  }
  if (level === 2) {
    return '中风险'
  }
  if (level === 1) {
    return '低风险'
  }
  return '未知'
}

function riskLevelTag(level?: number) {
  if (level === 3) {
    return 'danger'
  }
  if (level === 2) {
    return 'warning'
  }
  return 'success'
}

function riskResultText(result?: string) {
  if (result === 'PASS') {
    return '放行'
  }
  if (result === 'REVIEW') {
    return '人工审核'
  }
  if (result === 'REJECT') {
    return '拒绝'
  }
  return '未知'
}

function riskResultTag(result?: string) {
  if (result === 'REJECT') {
    return 'danger'
  }
  if (result === 'REVIEW') {
    return 'warning'
  }
  return 'success'
}

function reviewStatusText(status?: number) {
  if (status === 0) {
    return '待审核'
  }
  if (status === 1) {
    return '审核通过'
  }
  if (status === 2) {
    return '审核拒绝'
  }
  return '未知'
}

function reviewStatusTag(status?: number) {
  if (status === 0) {
    return 'warning'
  }
  if (status === 1) {
    return 'success'
  }
  if (status === 2) {
    return 'danger'
  }
  return 'info'
}

async function loadSummary() {
  const data = await getRiskReviewSummaryApi()
  summary.value = data || summary.value
}

async function loadPage() {
  loading.value = true
  try {
    const page = await getRiskReviewOrderPageApi(query)
    records.value = Array.isArray(page.records) ? page.records : []
    total.value = Number(page.total || 0)
  } catch (error) {
    console.error(error)
    ElMessage.error('加载人工审核单失败，请先查 F12 Console 和 Network')
  } finally {
    loading.value = false
  }
}

async function refreshAll() {
  await Promise.all([loadSummary(), loadPage()])
}

function handleSearch() {
  query.pageNo = 1
  refreshAll()
}

function handleReset() {
  query.pageNo = 1
  query.pageSize = 10
  query.reviewOrderCode = ''
  query.transactionNo = ''
  query.accountNo = ''
  query.customerName = ''
  query.riskLevel = undefined
  query.riskResult = ''
  query.reviewStatus = undefined
  refreshAll()
}

async function handleGenerate() {
  generateLoading.value = true
  try {
    const data = await createRiskReviewOrderFromTransactionApi({
      transactionId: generateForm.transactionId,
      limit: generateForm.limit,
    })
    ElMessage.success(`生成完成，本次新增 ${Array.isArray(data) ? data.length : 0} 条审核单`)
    await refreshAll()
  } catch (error) {
    console.error(error)
    ElMessage.error('生成审核单失败，请确认已生成规则命中记录，并查看 Docker 后端日志')
  } finally {
    generateLoading.value = false
  }
}

function openApproveDialog(row: RiskReviewOrderPageVO) {
  currentOrder.value = row
  reviewForm.reviewResult = '经人工复核，交易可放行。'
  approveDialogVisible.value = true
}

function openRejectDialog(row: RiskReviewOrderPageVO) {
  currentOrder.value = row
  reviewForm.reviewResult = '经人工复核，交易风险较高，拒绝通过。'
  rejectDialogVisible.value = true
}

async function submitApprove() {
  if (!currentOrder.value) {
    return
  }
  if (!reviewForm.reviewResult.trim()) {
    ElMessage.warning('请填写审核意见')
    return
  }
  await approveRiskReviewOrderApi(currentOrder.value.id, { ...reviewForm })
  ElMessage.success('审核通过成功')
  approveDialogVisible.value = false
  await refreshAll()
}

async function submitReject() {
  if (!currentOrder.value) {
    return
  }
  if (!reviewForm.reviewResult.trim()) {
    ElMessage.warning('请填写审核意见')
    return
  }
  await ElMessageBox.confirm('确认拒绝这笔交易审核单吗？', '审核拒绝确认', {
    type: 'warning',
  })
  await rejectRiskReviewOrderApi(currentOrder.value.id, { ...reviewForm })
  ElMessage.success('审核拒绝成功')
  rejectDialogVisible.value = false
  await refreshAll()
}

function handleSizeChange(size: number) {
  query.pageSize = size
  query.pageNo = 1
  loadPage()
}

function handleCurrentChange(pageNo: number) {
  query.pageNo = pageNo
  loadPage()
}

onMounted(() => {
  refreshAll()
})
</script>

<template>
  <main class="risk-review-page">
    <section class="page-header">
      <div>
        <p class="eyebrow">R3-04 银行实时交易风控</p>
        <h1>风险评分与人工审核</h1>
      </div>
      <el-button type="primary" :loading="loading" @click="refreshAll">刷新</el-button>
    </section>

    <section class="summary-grid">
      <div class="summary-card">
        <span>审核单总数</span>
        <strong>{{ summary.totalOrderCount }}</strong>
      </div>
      <div class="summary-card warning">
        <span>待审核</span>
        <strong>{{ summary.pendingCount }}</strong>
      </div>
      <div class="summary-card success">
        <span>审核通过</span>
        <strong>{{ summary.approvedCount }}</strong>
      </div>
      <div class="summary-card danger">
        <span>审核拒绝</span>
        <strong>{{ summary.rejectedCount }}</strong>
      </div>
      <div class="summary-card">
        <span>高风险单</span>
        <strong>{{ summary.highRiskCount }}</strong>
      </div>
      <div class="summary-card">
        <span>平均分</span>
        <strong>{{ summary.averageScore }}</strong>
      </div>
    </section>

    <section class="operate-panel">
      <div class="panel-title">生成审核单</div>
      <div class="operate-grid">
        <el-input-number
          v-model="generateForm.transactionId"
          :min="1"
          :controls="false"
          placeholder="指定交易ID，可不填"
          class="grid-control"
        />
        <el-input-number v-model="generateForm.limit" :min="1" :max="200" class="grid-control" />
        <el-button type="primary" :loading="generateLoading" @click="handleGenerate">
          从交易和规则命中生成
        </el-button>
      </div>
    </section>

    <section class="query-panel">
      <div class="query-grid">
        <el-input v-model="query.reviewOrderCode" placeholder="审核单编号" clearable />
        <el-input v-model="query.transactionNo" placeholder="交易流水号" clearable />
        <el-input v-model="query.accountNo" placeholder="账户号" clearable />
        <el-input v-model="query.customerName" placeholder="客户姓名" clearable />
        <el-select v-model="query.riskLevel" placeholder="风险等级" clearable>
          <el-option label="低风险" :value="1" />
          <el-option label="中风险" :value="2" />
          <el-option label="高风险" :value="3" />
        </el-select>
        <el-select v-model="query.riskResult" placeholder="风险结果" clearable>
          <el-option label="放行" value="PASS" />
          <el-option label="人工审核" value="REVIEW" />
          <el-option label="拒绝" value="REJECT" />
        </el-select>
        <el-select v-model="query.reviewStatus" placeholder="审核状态" clearable>
          <el-option label="待审核" :value="0" />
          <el-option label="审核通过" :value="1" />
          <el-option label="审核拒绝" :value="2" />
        </el-select>
        <div class="query-actions">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
    </section>

    <section class="table-panel">
      <div class="table-toolbar">
        <span>当前页待审核：{{ pendingTotal }}</span>
        <span>当前页高风险：{{ highRiskTotal }}</span>
      </div>
      <el-table v-loading="loading" :data="safeRecords" border stripe>
        <el-table-column prop="reviewOrderCode" label="审核单编号" min-width="170" />
        <el-table-column prop="transactionNo" label="交易流水号" min-width="170" />
        <el-table-column prop="customerName" label="客户" min-width="120" />
        <el-table-column prop="merchantName" label="商户" min-width="140" />
        <el-table-column prop="amount" label="金额" width="120" />
        <el-table-column prop="totalScore" label="风险分" width="100" />
        <el-table-column label="风险等级" width="110">
          <template #default="{ row }">
            <el-tag :type="riskLevelTag(row.riskLevel)">
              {{ riskLevelText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险结果" width="120">
          <template #default="{ row }">
            <el-tag :type="riskResultTag(row.riskResult)">
              {{ riskResultText(row.riskResult) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审核状态" width="120">
          <template #default="{ row }">
            <el-tag :type="reviewStatusTag(row.reviewStatus)">
              {{ reviewStatusText(row.reviewStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" />
        <el-table-column fixed="right" label="操作" width="180">
          <template #default="{ row }">
            <el-button
              link
              type="success"
              :disabled="row.reviewStatus !== 0"
              @click="openApproveDialog(row)"
            >
              通过
            </el-button>
            <el-button
              link
              type="danger"
              :disabled="row.reviewStatus !== 0"
              @click="openRejectDialog(row)"
            >
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :current-page="query.pageNo"
          :page-size="query.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </section>

    <el-dialog v-model="approveDialogVisible" title="审核通过" width="520px">
      <el-form label-width="96px">
        <el-form-item label="审核人">
          <el-input v-model="reviewForm.reviewerUsername" />
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="reviewForm.reviewResult" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApprove">确认通过</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectDialogVisible" title="审核拒绝" width="520px">
      <el-form label-width="96px">
        <el-form-item label="审核人">
          <el-input v-model="reviewForm.reviewerUsername" />
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="reviewForm.reviewResult" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="submitReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </main>
</template>

<style scoped>
.risk-review-page {
  padding: 24px;
  color: #1f2937;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 20px;
}

.eyebrow {
  margin: 0 0 6px;
  color: #64748b;
  font-size: 13px;
}

h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 14px;
  margin-bottom: 18px;
}

.summary-card {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  background: #ffffff;
}

.summary-card span {
  display: block;
  color: #64748b;
  font-size: 13px;
  margin-bottom: 8px;
}

.summary-card strong {
  font-size: 26px;
  line-height: 1;
}

.summary-card.warning strong {
  color: #d97706;
}

.summary-card.success strong {
  color: #16a34a;
}

.summary-card.danger strong {
  color: #dc2626;
}

.operate-panel,
.query-panel,
.table-panel {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  background: #ffffff;
  margin-bottom: 18px;
}

.panel-title {
  font-weight: 600;
  margin-bottom: 12px;
}

.operate-grid,
.query-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
  align-items: center;
}

.grid-control {
  width: 100%;
}

.query-actions {
  display: flex;
  gap: 10px;
}

.table-toolbar {
  display: flex;
  gap: 18px;
  color: #64748b;
  margin-bottom: 12px;
  font-size: 13px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 720px) {
  .risk-review-page {
    padding: 16px;
  }

  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .query-actions {
    justify-content: flex-start;
  }
}
</style>
