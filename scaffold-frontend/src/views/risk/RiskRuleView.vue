<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  generateRiskRuleHitsApi,
  getRiskRuleHitPageApi,
  getRiskRulePageApi,
  type RiskRuleHitPageVO,
  type RiskRulePageVO,
} from '../../api/risk/rule'

const ruleLoading = ref(false)
const hitLoading = ref(false)
const generateLoading = ref(false)

const ruleRecords = ref<RiskRulePageVO[]>([])
const hitRecords = ref<RiskRuleHitPageVO[]>([])

const ruleTotal = ref(0)
const hitTotal = ref(0)

const ruleQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  ruleCode: '',
  ruleName: '',
  ruleType: '',
  riskLevel: undefined as number | undefined,
  status: undefined as number | undefined,
})

const hitQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  transactionNo: '',
  accountNo: '',
  customerName: '',
  ruleType: '',
  riskLevel: undefined as number | undefined,
  status: undefined as number | undefined,
})

const safeRuleRecords = computed(() => (Array.isArray(ruleRecords.value) ? ruleRecords.value : []))
const safeHitRecords = computed(() => (Array.isArray(hitRecords.value) ? hitRecords.value : []))

const enabledRuleTotal = computed(() => safeRuleRecords.value.filter((item) => item.status === 0).length)
const highRiskRuleTotal = computed(() => safeRuleRecords.value.filter((item) => item.riskLevel === 3).length)
const highRiskHitTotal = computed(() => safeHitRecords.value.filter((item) => item.riskLevel === 3).length)
const pendingHitTotal = computed(() => safeHitRecords.value.filter((item) => item.status === 0).length)

function riskLevelText(level?: number) {
  if (level === 3) return '高风险'
  if (level === 2) return '中风险'
  if (level === 1) return '低风险'
  return '-'
}

function riskLevelType(level?: number) {
  if (level === 3) return 'danger'
  if (level === 2) return 'warning'
  if (level === 1) return 'success'
  return 'info'
}

function ruleTypeText(type?: string) {
  const map: Record<string, string> = {
    AMOUNT: '大额交易',
    FREQUENCY: '高频交易',
    LOCATION: '异地交易',
    DEVICE: '异常设备',
    TIME: '夜间交易',
    BLACKLIST: '黑名单',
  }
  return type ? map[type] || type : '-'
}

async function loadRules() {
  ruleLoading.value = true
  try {
    const page = await getRiskRulePageApi(ruleQuery)
    ruleRecords.value = Array.isArray(page?.records) ? page.records : []
    ruleTotal.value = Number(page?.total || 0)
  } catch (error) {
    console.error(error)
    ElMessage.error('风控规则加载失败')
  } finally {
    ruleLoading.value = false
  }
}

async function loadHits() {
  hitLoading.value = true
  try {
    const page = await getRiskRuleHitPageApi(hitQuery)
    hitRecords.value = Array.isArray(page?.records) ? page.records : []
    hitTotal.value = Number(page?.total || 0)
  } catch (error) {
    console.error(error)
    ElMessage.error('规则命中记录加载失败')
  } finally {
    hitLoading.value = false
  }
}

async function handleGenerateHits() {
  generateLoading.value = true
  try {
    const hits = await generateRiskRuleHitsApi({ limit: 50 })
    ElMessage.success(`规则引擎执行完成，本次新增 ${Array.isArray(hits) ? hits.length : 0} 条命中记录`)
    await Promise.all([loadRules(), loadHits()])
  } catch (error) {
    console.error(error)
    ElMessage.error('规则引擎执行失败')
  } finally {
    generateLoading.value = false
  }
}

function handleRuleSearch() {
  ruleQuery.pageNo = 1
  loadRules()
}

function handleRuleReset() {
  ruleQuery.pageNo = 1
  ruleQuery.ruleCode = ''
  ruleQuery.ruleName = ''
  ruleQuery.ruleType = ''
  ruleQuery.riskLevel = undefined
  ruleQuery.status = undefined
  loadRules()
}

function handleHitSearch() {
  hitQuery.pageNo = 1
  loadHits()
}

function handleHitReset() {
  hitQuery.pageNo = 1
  hitQuery.transactionNo = ''
  hitQuery.accountNo = ''
  hitQuery.customerName = ''
  hitQuery.ruleType = ''
  hitQuery.riskLevel = undefined
  hitQuery.status = undefined
  loadHits()
}

function handleRulePageChange(pageNo: number) {
  ruleQuery.pageNo = pageNo
  loadRules()
}

function handleHitPageChange(pageNo: number) {
  hitQuery.pageNo = pageNo
  loadHits()
}

onMounted(() => {
  loadRules()
  loadHits()
})
</script>

<template>
  <div class="risk-rule-page">
    <div class="page-header">
      <div>
        <h2>风控规则引擎</h2>
        <p>基于交易流水执行大额交易、高频交易、异地交易、异常设备、夜间交易和黑名单规则。</p>
      </div>
      <el-button type="primary" :loading="generateLoading" @click="handleGenerateHits">
        生成规则命中
      </el-button>
    </div>

    <div class="stat-grid">
      <div class="stat-item">
        <span class="stat-label">规则总数</span>
        <strong>{{ ruleTotal }}</strong>
      </div>
      <div class="stat-item">
        <span class="stat-label">启用规则</span>
        <strong>{{ enabledRuleTotal }}</strong>
      </div>
      <div class="stat-item">
        <span class="stat-label">高风险规则</span>
        <strong>{{ highRiskRuleTotal }}</strong>
      </div>
      <div class="stat-item">
        <span class="stat-label">命中记录</span>
        <strong>{{ hitTotal }}</strong>
      </div>
      <div class="stat-item">
        <span class="stat-label">高风险命中</span>
        <strong>{{ highRiskHitTotal }}</strong>
      </div>
      <div class="stat-item">
        <span class="stat-label">未处理命中</span>
        <strong>{{ pendingHitTotal }}</strong>
      </div>
    </div>

    <section class="section-panel">
      <div class="section-title">
        <h3>规则列表</h3>
      </div>

      <div class="query-grid">
        <el-input v-model="ruleQuery.ruleCode" clearable placeholder="规则编码" />
        <el-input v-model="ruleQuery.ruleName" clearable placeholder="规则名称" />
        <el-select v-model="ruleQuery.ruleType" clearable placeholder="规则类型">
          <el-option label="大额交易" value="AMOUNT" />
          <el-option label="高频交易" value="FREQUENCY" />
          <el-option label="异地交易" value="LOCATION" />
          <el-option label="异常设备" value="DEVICE" />
          <el-option label="夜间交易" value="TIME" />
          <el-option label="黑名单" value="BLACKLIST" />
        </el-select>
        <el-select v-model="ruleQuery.riskLevel" clearable placeholder="风险等级">
          <el-option label="低风险" :value="1" />
          <el-option label="中风险" :value="2" />
          <el-option label="高风险" :value="3" />
        </el-select>
        <el-select v-model="ruleQuery.status" clearable placeholder="状态">
          <el-option label="启用" :value="0" />
          <el-option label="停用" :value="1" />
        </el-select>
        <div class="query-actions">
          <el-button type="primary" @click="handleRuleSearch">查询</el-button>
          <el-button @click="handleRuleReset">重置</el-button>
        </div>
      </div>

      <el-table v-loading="ruleLoading" :data="safeRuleRecords" border>
        <el-table-column prop="ruleCode" label="规则编码" min-width="170" />
        <el-table-column prop="ruleName" label="规则名称" min-width="160" />
        <el-table-column label="规则类型" min-width="120">
          <template #default="{ row }">
            {{ ruleTypeText(row.ruleType) }}
          </template>
        </el-table-column>
        <el-table-column prop="compareOperator" label="比较符" width="90" />
        <el-table-column prop="thresholdValue" label="阈值" width="110" />
        <el-table-column label="风险等级" width="110">
          <template #default="{ row }">
            <el-tag :type="riskLevelType(row.riskLevel)">
              {{ riskLevelText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险分" width="90" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'info'">
              {{ row.status === 0 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ruleContent" label="规则内容" min-width="220" show-overflow-tooltip />
      </el-table>

      <div class="pagination-row">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :current-page="ruleQuery.pageNo"
          :page-size="ruleQuery.pageSize"
          :total="ruleTotal"
          @current-change="handleRulePageChange"
        />
      </div>
    </section>

    <section class="section-panel">
      <div class="section-title">
        <h3>规则命中记录</h3>
      </div>

      <div class="query-grid">
        <el-input v-model="hitQuery.transactionNo" clearable placeholder="交易流水号" />
        <el-input v-model="hitQuery.accountNo" clearable placeholder="账号" />
        <el-input v-model="hitQuery.customerName" clearable placeholder="客户姓名" />
        <el-select v-model="hitQuery.ruleType" clearable placeholder="规则类型">
          <el-option label="大额交易" value="AMOUNT" />
          <el-option label="高频交易" value="FREQUENCY" />
          <el-option label="异地交易" value="LOCATION" />
          <el-option label="异常设备" value="DEVICE" />
          <el-option label="夜间交易" value="TIME" />
          <el-option label="黑名单" value="BLACKLIST" />
        </el-select>
        <el-select v-model="hitQuery.riskLevel" clearable placeholder="风险等级">
          <el-option label="低风险" :value="1" />
          <el-option label="中风险" :value="2" />
          <el-option label="高风险" :value="3" />
        </el-select>
        <el-select v-model="hitQuery.status" clearable placeholder="处理状态">
          <el-option label="未处理" :value="0" />
          <el-option label="已处理" :value="1" />
        </el-select>
        <div class="query-actions">
          <el-button type="primary" @click="handleHitSearch">查询</el-button>
          <el-button @click="handleHitReset">重置</el-button>
        </div>
      </div>

      <el-table v-loading="hitLoading" :data="safeHitRecords" border>
        <el-table-column prop="hitCode" label="命中编码" min-width="170" />
        <el-table-column prop="transactionNo" label="交易流水号" min-width="180" />
        <el-table-column prop="accountNo" label="账号" min-width="140" />
        <el-table-column prop="customerName" label="客户姓名" min-width="120" />
        <el-table-column label="规则类型" min-width="120">
          <template #default="{ row }">
            {{ ruleTypeText(row.ruleType) }}
          </template>
        </el-table-column>
        <el-table-column prop="ruleName" label="规则名称" min-width="160" />
        <el-table-column prop="hitValue" label="命中值" min-width="100" />
        <el-table-column label="风险等级" width="110">
          <template #default="{ row }">
            <el-tag :type="riskLevelType(row.riskLevel)">
              {{ riskLevelText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险分" width="90" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'warning' : 'success'">
              {{ row.status === 0 ? '未处理' : '已处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="hitTime" label="命中时间" min-width="170" />
      </el-table>

      <div class="pagination-row">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :current-page="hitQuery.pageNo"
          :page-size="hitQuery.pageSize"
          :total="hitTotal"
          @current-change="handleHitPageChange"
        />
      </div>
    </section>
  </div>
</template>

<style scoped>
.risk-rule-page {
  display: flex;
  flex-direction: column;
  gap: 18px;
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.page-header h2,
.section-title h3 {
  margin: 0;
}

.page-header p {
  margin: 8px 0 0;
  color: #606266;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 12px;
}

.stat-item {
  display: flex;
  min-height: 76px;
  flex-direction: column;
  justify-content: center;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background: #fff;
  padding: 14px 16px;
}

.stat-label {
  color: #606266;
  font-size: 13px;
}

.stat-item strong {
  margin-top: 8px;
  color: #303133;
  font-size: 26px;
}

.section-panel {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background: #fff;
  padding: 16px;
}

.section-title {
  margin-bottom: 14px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
  margin-bottom: 14px;
}

.query-actions {
  display: flex;
  gap: 8px;
}

.pagination-row {
  display: flex;
  justify-content: flex-end;
  margin-top: 14px;
}

@media (max-width: 768px) {
  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .query-actions {
    width: 100%;
  }
}
</style>
