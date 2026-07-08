<template>
  <div class="quality-page">
    <div class="page-header">
      <div>
        <h2>D4-04 数据质量检测</h2>
        <p>基于 D4-03 已采集的元数据，为表和字段配置质量规则，并生成检测结果。</p>
      </div>
      <el-button type="primary" :loading="checking" @click="handleCheckSelected">
        执行检测
      </el-button>
    </div>

    <div class="summary-grid">
      <el-card shadow="hover" class="summary-card">
        <div class="summary-label">当前规则数</div>
        <div class="summary-value">{{ rulePage.total }}</div>
      </el-card>
      <el-card shadow="hover" class="summary-card">
        <div class="summary-label">当前结果数</div>
        <div class="summary-value">{{ resultPage.total }}</div>
      </el-card>
      <el-card shadow="hover" class="summary-card danger">
        <div class="summary-label">本页不通过</div>
        <div class="summary-value">{{ failedResultCount }}</div>
      </el-card>
      <el-card shadow="hover" class="summary-card success">
        <div class="summary-label">本页通过</div>
        <div class="summary-value">{{ passedResultCount }}</div>
      </el-card>
    </div>

    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-header">
          <span>质量检测操作</span>
          <span class="header-tip">选择一条规则或一张元数据表执行检测</span>
        </div>
      </template>

      <div class="check-grid">
        <el-select
          v-model="checkForm.targetTableId"
          filterable
          clearable
          placeholder="选择元数据表"
          @change="handleCheckTableChange"
        >
          <el-option
            v-for="item in tableOptions"
            :key="item.id"
            :label="formatTableOption(item)"
            :value="item.id"
          />
        </el-select>

        <el-select
          v-model="checkForm.ruleId"
          filterable
          clearable
          placeholder="可选：只检测某一条规则"
        >
          <el-option
            v-for="item in ruleOptions"
            :key="item.id"
            :label="`${item.ruleCode}｜${item.ruleName}`"
            :value="item.id"
          />
        </el-select>

        <el-button type="primary" :loading="checking" @click="handleCheckSelected">
          开始检测
        </el-button>
        <el-button @click="resetCheckForm">重置</el-button>
      </div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-header">
          <span>质量规则列表</span>
          <el-button size="small" @click="loadRules">刷新规则</el-button>
        </div>
      </template>

      <div class="rule-search-grid">
        <el-input v-model="ruleQuery.ruleName" clearable placeholder="规则名称" />
        <el-select v-model="ruleQuery.ruleType" clearable placeholder="规则类型">
          <el-option label="非空 NOT_NULL" value="NOT_NULL" />
          <el-option label="唯一 UNIQUE" value="UNIQUE" />
          <el-option label="范围 RANGE" value="RANGE" />
          <el-option label="格式 FORMAT" value="FORMAT" />
          <el-option label="枚举 ENUM" value="ENUM" />
        </el-select>
        <el-select v-model="ruleQuery.status" clearable placeholder="启停状态">
          <el-option label="启用" :value="0" />
          <el-option label="停用" :value="1" />
        </el-select>
        <el-button type="primary" @click="handleRuleSearch">查询</el-button>
        <el-button @click="resetRuleSearch">重置</el-button>
      </div>

      <el-table v-loading="ruleLoading" :data="safeRuleRecords" border stripe class="data-table">
        <el-table-column prop="ruleCode" label="规则编码" min-width="170" show-overflow-tooltip />
        <el-table-column prop="ruleName" label="规则名称" min-width="220" show-overflow-tooltip />
        <el-table-column prop="ruleType" label="规则类型" width="120" />
        <el-table-column prop="targetType" label="对象类型" width="100" />
        <el-table-column prop="targetTableName" label="目标表" min-width="160" show-overflow-tooltip />
        <el-table-column prop="targetColumnName" label="目标字段" min-width="140" show-overflow-tooltip />
        <el-table-column prop="qualityLevel" label="等级" width="90">
          <template #default="scope">
            <el-tag :type="qualityLevelType(scope.row.qualityLevel)">
              {{ qualityLevelText(scope.row.qualityLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.status === 0 ? 'success' : 'info'">
              {{ scope.row.status === 0 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="120">
          <template #default="scope">
            <el-button type="primary" link @click="handleCheckRule(scope.row)">检测</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-wrapper">
        <el-pagination
          v-model:current-page="ruleQuery.pageNo"
          v-model:page-size="ruleQuery.pageSize"
          :total="rulePage.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadRules"
          @current-change="loadRules"
        />
      </div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-header">
          <span>质量检测结果</span>
          <el-button size="small" @click="loadResults">刷新结果</el-button>
        </div>
      </template>

      <div class="result-search-grid">
        <el-input v-model="resultQuery.ruleName" clearable placeholder="规则名称" />
        <el-input v-model="resultQuery.tableCode" clearable placeholder="表名" />
        <el-input v-model="resultQuery.columnName" clearable placeholder="字段名" />
        <el-select v-model="resultQuery.checkStatus" clearable placeholder="检测状态">
          <el-option label="通过" :value="0" />
          <el-option label="不通过" :value="1" />
        </el-select>
        <el-button type="primary" @click="handleResultSearch">查询</el-button>
        <el-button @click="resetResultSearch">重置</el-button>
      </div>

      <el-table v-loading="resultLoading" :data="safeResultRecords" border stripe class="data-table">
        <el-table-column prop="resultCode" label="结果编码" min-width="190" show-overflow-tooltip />
        <el-table-column prop="ruleName" label="规则名称" min-width="220" show-overflow-tooltip />
        <el-table-column prop="tableCode" label="表名" min-width="160" show-overflow-tooltip />
        <el-table-column prop="columnName" label="字段名" min-width="140" show-overflow-tooltip />
        <el-table-column prop="checkTotal" label="检测总数" width="110" />
        <el-table-column prop="errorTotal" label="异常数" width="100" />
        <el-table-column prop="errorRate" label="异常率" width="110">
          <template #default="scope">{{ toPercent(scope.row.errorRate) }}</template>
        </el-table-column>
        <el-table-column prop="checkStatus" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.checkStatus === 0 ? 'success' : 'danger'">
              {{ scope.row.checkStatus === 0 ? '通过' : '不通过' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkTime" label="检测时间" min-width="170" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" min-width="220" show-overflow-tooltip />
      </el-table>

      <div class="pager-wrapper">
        <el-pagination
          v-model:current-page="resultQuery.pageNo"
          v-model:page-size="resultQuery.pageSize"
          :total="resultPage.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadResults"
          @current-change="loadResults"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  checkQualityResults,
  getMetadataTablesForQuality,
  getQualityResultsPage,
  getQualityRulesPage,
  type DatahubMetadataTableOption,
  type DatahubQualityResultPageQuery,
  type DatahubQualityResultPageVO,
  type DatahubQualityRulePageQuery,
  type DatahubQualityRulePageVO,
  type PageResult
} from '../../api/datahub/quality'

type UnknownResponse = unknown

const ruleLoading = ref(false)
const resultLoading = ref(false)
const checking = ref(false)

const ruleQuery = reactive<DatahubQualityRulePageQuery>({
  pageNo: 1,
  pageSize: 10,
  ruleName: '',
  ruleType: '',
  status: undefined
})

const resultQuery = reactive<DatahubQualityResultPageQuery>({
  pageNo: 1,
  pageSize: 10,
  ruleName: '',
  tableCode: '',
  columnName: '',
  checkStatus: undefined
})

const checkForm = reactive({
  targetTableId: undefined as number | undefined,
  ruleId: undefined as number | undefined
})

const rulePage = reactive<PageResult<DatahubQualityRulePageVO>>({
  pageNo: 1,
  pageSize: 10,
  total: 0,
  pages: 0,
  records: []
})

const resultPage = reactive<PageResult<DatahubQualityResultPageVO>>({
  pageNo: 1,
  pageSize: 10,
  total: 0,
  pages: 0,
  records: []
})

const tableOptions = ref<DatahubMetadataTableOption[]>([])

const safeRuleRecords = computed(() => (Array.isArray(rulePage.records) ? rulePage.records : []))
const safeResultRecords = computed(() => (Array.isArray(resultPage.records) ? resultPage.records : []))

const ruleOptions = computed(() => {
  const list = safeRuleRecords.value
  if (!checkForm.targetTableId) {
    return list
  }
  return list.filter((item) => item.targetTableId === checkForm.targetTableId)
})

const failedResultCount = computed(() => safeResultRecords.value.filter((item) => item.checkStatus === 1).length)
const passedResultCount = computed(() => safeResultRecords.value.filter((item) => item.checkStatus === 0).length)

function unwrapApiData<T>(response: UnknownResponse): T {
  const anyResponse = response as any
  return (anyResponse?.data?.data ?? anyResponse?.data ?? anyResponse ?? {}) as T
}

function unwrapPage<T>(response: UnknownResponse): PageResult<T> {
  const data = unwrapApiData<PageResult<T>>(response)
  return {
    pageNo: Number(data?.pageNo ?? 1),
    pageSize: Number(data?.pageSize ?? 10),
    total: Number(data?.total ?? 0),
    pages: Number(data?.pages ?? 0),
    records: Array.isArray(data?.records) ? data.records : []
  }
}

function assignPage<T>(target: PageResult<T>, source: PageResult<T>) {
  target.pageNo = source.pageNo
  target.pageSize = source.pageSize
  target.total = source.total
  target.pages = source.pages
  target.records = Array.isArray(source.records) ? source.records : []
}

async function loadRules() {
  ruleLoading.value = true
  try {
    const response = await getQualityRulesPage(ruleQuery)
    const page = unwrapPage<DatahubQualityRulePageVO>(response)
    assignPage(rulePage, page)
  } catch (error) {
    console.error(error)
    ElMessage.error('质量规则加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    ruleLoading.value = false
  }
}

async function loadResults() {
  resultLoading.value = true
  try {
    const response = await getQualityResultsPage(resultQuery)
    const page = unwrapPage<DatahubQualityResultPageVO>(response)
    assignPage(resultPage, page)
  } catch (error) {
    console.error(error)
    ElMessage.error('质量结果加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    resultLoading.value = false
  }
}

async function loadTableOptions() {
  try {
    const response = await getMetadataTablesForQuality({ pageNo: 1, pageSize: 100 })
    const page = unwrapPage<DatahubMetadataTableOption>(response)
    tableOptions.value = Array.isArray(page.records) ? page.records : []
  } catch (error) {
    console.error(error)
    tableOptions.value = []
    ElMessage.warning('元数据表选项加载失败，请确认 D4-03 元数据采集已完成')
  }
}

function handleRuleSearch() {
  ruleQuery.pageNo = 1
  loadRules()
}

function resetRuleSearch() {
  ruleQuery.pageNo = 1
  ruleQuery.pageSize = 10
  ruleQuery.ruleName = ''
  ruleQuery.ruleType = ''
  ruleQuery.status = undefined
  loadRules()
}

function handleResultSearch() {
  resultQuery.pageNo = 1
  loadResults()
}

function resetResultSearch() {
  resultQuery.pageNo = 1
  resultQuery.pageSize = 10
  resultQuery.ruleName = ''
  resultQuery.tableCode = ''
  resultQuery.columnName = ''
  resultQuery.checkStatus = undefined
  loadResults()
}

function resetCheckForm() {
  checkForm.targetTableId = undefined
  checkForm.ruleId = undefined
}

function handleCheckTableChange() {
  checkForm.ruleId = undefined
}

async function handleCheckRule(row: DatahubQualityRulePageVO) {
  checkForm.ruleId = row.id
  checkForm.targetTableId = row.targetTableId
  await handleCheckSelected()
}

async function handleCheckSelected() {
  if (!checkForm.ruleId && !checkForm.targetTableId) {
    ElMessage.warning('请至少选择一条规则，或选择一张元数据表')
    return
  }

  checking.value = true
  try {
    const response = await checkQualityResults({
      ruleId: checkForm.ruleId,
      targetTableId: checkForm.ruleId ? undefined : checkForm.targetTableId
    })
    const data = unwrapApiData<DatahubQualityResultPageVO[]>(response)
    const list = Array.isArray(data) ? data : []
    const failed = list.filter((item) => item.checkStatus === 1).length
    ElMessage.success(`检测完成：共生成 ${list.length} 条结果，其中不通过 ${failed} 条`)
    await loadResults()
  } catch (error) {
    console.error(error)
    ElMessage.error('质量检测失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    checking.value = false
  }
}

function formatTableOption(item: DatahubMetadataTableOption) {
  const comment = item.tableComment ? `（${item.tableComment}）` : ''
  return `${item.tableName}${comment}`
}

function qualityLevelText(level?: number) {
  if (level === 3) return '严重'
  if (level === 2) return '重要'
  return '一般'
}

function qualityLevelType(level?: number) {
  if (level === 3) return 'danger'
  if (level === 2) return 'warning'
  return 'info'
}

function toPercent(value?: number) {
  const num = Number(value ?? 0)
  return `${num.toFixed(2)}%`
}

onMounted(async () => {
  await Promise.all([loadRules(), loadResults(), loadTableOptions()])
})
</script>

<style scoped>
.quality-page {
  padding: 20px;
  background: #f5f7fb;
  min-height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.page-header h2 {
  margin: 0 0 6px;
  font-size: 22px;
  font-weight: 700;
  color: #1f2d3d;
}

.page-header p {
  margin: 0;
  color: #6b7280;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.summary-card {
  border-radius: 12px;
}

.summary-label {
  color: #6b7280;
  font-size: 14px;
  margin-bottom: 8px;
}

.summary-value {
  color: #111827;
  font-size: 28px;
  font-weight: 700;
}

.summary-card.danger .summary-value {
  color: #f56c6c;
}

.summary-card.success .summary-value {
  color: #67c23a;
}

.section-card {
  margin-bottom: 16px;
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  font-weight: 600;
}

.header-tip {
  color: #909399;
  font-size: 13px;
  font-weight: 400;
}

.check-grid {
  display: grid;
  grid-template-columns: minmax(220px, 1fr) minmax(260px, 1.2fr) auto auto;
  gap: 12px;
  align-items: center;
}

.rule-search-grid,
.result-search-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(180px, 1fr)) auto auto;
  gap: 12px;
  align-items: center;
  margin-bottom: 14px;
}

.result-search-grid {
  grid-template-columns: repeat(4, minmax(160px, 1fr)) auto auto;
}

.data-table {
  width: 100%;
}

.pager-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 14px;
}

@media (max-width: 1200px) {
  .summary-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .check-grid,
  .rule-search-grid,
  .result-search-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .summary-grid,
  .check-grid,
  .rule-search-grid,
  .result-search-grid {
    grid-template-columns: 1fr;
  }

  .pager-wrapper {
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>
