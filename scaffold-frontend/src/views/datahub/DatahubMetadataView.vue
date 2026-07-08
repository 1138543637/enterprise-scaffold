<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  collectMetadata,
  getMetadataCollectLogPage,
  getMetadataColumnPage,
  getMetadataTablePage,
  type DatahubMetadataCollectLogPageVO,
  type DatahubMetadataColumnPageVO,
  type DatahubMetadataTablePageVO
} from '../../api/datahub/metadata'

type PageLike<T> = {
  records?: T[]
  total?: number
}

type CollectResultLike = {
  collectBatchNo?: string
  dataSourceId?: number
  dataSourceName?: string
  tableTotal?: number
  columnTotal?: number

  // 兼容旧字段，避免以后后端字段名变化导致 undefined
  tableCount?: number
  tableNum?: number
  columnCount?: number
  columnNum?: number
}

function unwrapApiData<T>(response: unknown): T {
  const anyResponse = response as any
  return (anyResponse?.data?.data ?? anyResponse?.data ?? anyResponse ?? {}) as T
}

function toNumber(value: unknown): number {
  const num = Number(value)
  return Number.isFinite(num) ? num : 0
}

const collectForm = reactive({
  dataSourceId: 1
})

const tableQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  dataSourceId: undefined as number | undefined,
  schemaName: '',
  tableName: '',
  tableType: '',
  status: undefined as number | undefined
})

const columnQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  tableId: undefined as number | undefined,
  dataSourceId: undefined as number | undefined,
  tableName: '',
  columnName: '',
  dataType: '',
  primaryKeyFlag: undefined as number | undefined
})

const logQuery = reactive({
  pageNo: 1,
  pageSize: 5,
  dataSourceId: undefined as number | undefined,
  collectBatchNo: '',
  collectStatus: undefined as number | undefined
})

const tableLoading = ref(false)
const columnLoading = ref(false)
const logLoading = ref(false)
const collectLoading = ref(false)

const tableRecords = ref<DatahubMetadataTablePageVO[]>([])
const tableTotal = ref(0)

const columnRecords = ref<DatahubMetadataColumnPageVO[]>([])
const columnTotal = ref(0)

const logRecords = ref<DatahubMetadataCollectLogPageVO[]>([])
const logTotal = ref(0)

const selectedTable = ref<DatahubMetadataTablePageVO | null>(null)

const tableTotalText = computed(() => tableTotal.value || 0)
const columnTotalText = computed(() => columnTotal.value || 0)
const logTotalText = computed(() => logTotal.value || 0)

function tableTypeName(type: string) {
  if (type === 'TABLE') return '数据表'
  if (type === 'VIEW') return '视图'
  return type || '-'
}

function collectStatusName(status: number) {
  if (status === 0) return '成功'
  if (status === 1) return '失败'
  return '未知'
}

function collectStatusTag(status: number) {
  if (status === 0) return 'success'
  if (status === 1) return 'danger'
  return 'info'
}

function yesNoName(value: number) {
  if (value === 1) return '是'
  if (value === 0) return '否'
  return '-'
}

async function loadTables() {
  tableLoading.value = true
  try {
    const response = await getMetadataTablePage(tableQuery)
    const result = unwrapApiData<PageLike<DatahubMetadataTablePageVO>>(response)

    tableRecords.value = Array.isArray(result.records) ? result.records : []
    tableTotal.value = toNumber(result.total)
  } catch (error) {
    console.error(error)
    ElMessage.error('元数据表加载失败，请检查 F12 Network 和 Docker 后端日志')
  } finally {
    tableLoading.value = false
  }
}

async function loadColumns() {
  columnLoading.value = true
  try {
    const response = await getMetadataColumnPage(columnQuery)
    const result = unwrapApiData<PageLike<DatahubMetadataColumnPageVO>>(response)

    columnRecords.value = Array.isArray(result.records) ? result.records : []
    columnTotal.value = toNumber(result.total)
  } catch (error) {
    console.error(error)
    ElMessage.error('字段元数据加载失败，请检查 F12 Network 和 Docker 后端日志')
  } finally {
    columnLoading.value = false
  }
}

async function loadLogs() {
  logLoading.value = true
  try {
    const response = await getMetadataCollectLogPage(logQuery)
    const result = unwrapApiData<PageLike<DatahubMetadataCollectLogPageVO>>(response)

    logRecords.value = Array.isArray(result.records) ? result.records : []
    logTotal.value = toNumber(result.total)
  } catch (error) {
    console.error(error)
    ElMessage.error('采集日志加载失败，请检查 F12 Network 和 Docker 后端日志')
  } finally {
    logLoading.value = false
  }
}

async function handleCollect() {
  if (!collectForm.dataSourceId) {
    ElMessage.warning('请填写数据源ID')
    return
  }

  collectLoading.value = true
  try {
    const response = await collectMetadata({
      dataSourceId: collectForm.dataSourceId
    })

    const collectResult = unwrapApiData<CollectResultLike>(response)

    const collectedTableTotal = toNumber(
        collectResult.tableTotal ?? collectResult.tableCount ?? collectResult.tableNum
    )

    const collectedColumnTotal = toNumber(
        collectResult.columnTotal ?? collectResult.columnCount ?? collectResult.columnNum
    )

    ElMessage.success(`采集成功：表 ${collectedTableTotal} 张，字段 ${collectedColumnTotal} 个`)

    tableQuery.pageNo = 1
    tableQuery.dataSourceId = collectForm.dataSourceId

    columnQuery.pageNo = 1
    columnQuery.tableId = undefined
    columnQuery.dataSourceId = collectForm.dataSourceId
    columnQuery.tableName = ''
    columnQuery.columnName = ''
    columnQuery.dataType = ''
    columnQuery.primaryKeyFlag = undefined

    logQuery.pageNo = 1
    logQuery.dataSourceId = collectForm.dataSourceId

    selectedTable.value = null

    await loadTables()
    await loadColumns()
    await loadLogs()
  } catch (error) {
    console.error(error)
    ElMessage.error('元数据采集失败，请检查数据源配置、F12 Network 和后端日志')
  } finally {
    collectLoading.value = false
  }
}

function handleSearchTables() {
  tableQuery.pageNo = 1
  loadTables()
}

function handleResetTables() {
  tableQuery.pageNo = 1
  tableQuery.pageSize = 10
  tableQuery.dataSourceId = undefined
  tableQuery.schemaName = ''
  tableQuery.tableName = ''
  tableQuery.tableType = ''
  tableQuery.status = undefined

  selectedTable.value = null

  columnQuery.pageNo = 1
  columnQuery.tableId = undefined
  columnQuery.dataSourceId = undefined
  columnQuery.tableName = ''
  columnQuery.columnName = ''
  columnQuery.dataType = ''
  columnQuery.primaryKeyFlag = undefined

  columnRecords.value = []
  columnTotal.value = 0

  loadTables()
}

function handleSelectTable(row: DatahubMetadataTablePageVO) {
  selectedTable.value = row

  columnQuery.pageNo = 1
  columnQuery.tableId = row.id
  columnQuery.dataSourceId = row.dataSourceId
  columnQuery.tableName = row.tableName
  columnQuery.columnName = ''
  columnQuery.dataType = ''
  columnQuery.primaryKeyFlag = undefined

  loadColumns()
}

function handleSearchColumns() {
  columnQuery.pageNo = 1
  loadColumns()
}

function handleResetColumns() {
  columnQuery.pageNo = 1
  columnQuery.pageSize = 10
  columnQuery.tableId = selectedTable.value?.id
  columnQuery.dataSourceId = selectedTable.value?.dataSourceId ?? collectForm.dataSourceId
  columnQuery.tableName = selectedTable.value?.tableName || ''
  columnQuery.columnName = ''
  columnQuery.dataType = ''
  columnQuery.primaryKeyFlag = undefined

  loadColumns()
}

function handleSearchLogs() {
  logQuery.pageNo = 1
  loadLogs()
}

onMounted(() => {
  loadTables()
  loadLogs()
})
</script>

<template>
  <div class="metadata-page">
    <div class="page-header">
      <div>
        <h2>数据治理 / 元数据采集</h2>
        <p>D4-03：连接数据源，自动采集数据库表和字段元数据。</p>
      </div>
    </div>

    <div class="summary-grid">
      <el-card shadow="never" class="summary-card">
        <div class="summary-label">已采集表数量</div>
        <div class="summary-value">{{ tableTotalText }}</div>
      </el-card>

      <el-card shadow="never" class="summary-card">
        <div class="summary-label">当前字段数量</div>
        <div class="summary-value">{{ columnTotalText }}</div>
      </el-card>

      <el-card shadow="never" class="summary-card">
        <div class="summary-label">采集日志数量</div>
        <div class="summary-value">{{ logTotalText }}</div>
      </el-card>
    </div>

    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-header">
          <span>执行元数据采集</span>
        </div>
      </template>

      <div class="collect-grid">
        <el-form label-width="100px">
          <el-form-item label="数据源ID">
            <el-input-number v-model="collectForm.dataSourceId" :min="1" />
          </el-form-item>
        </el-form>

        <div class="collect-actions">
          <el-button type="primary" :loading="collectLoading" @click="handleCollect">
            开始采集
          </el-button>
          <span class="tip-text">
            这里填写的是 D4-02 数据源管理中已有的数据源 ID。
          </span>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-header">
          <span>元数据表清单</span>
          <el-button size="small" @click="loadTables">刷新</el-button>
        </div>
      </template>

      <el-form class="search-grid" label-width="90px">
        <el-form-item label="数据源ID">
          <el-input-number v-model="tableQuery.dataSourceId" :min="1" clearable />
        </el-form-item>

        <el-form-item label="库名">
          <el-input v-model="tableQuery.schemaName" placeholder="请输入库名" clearable />
        </el-form-item>

        <el-form-item label="表名">
          <el-input v-model="tableQuery.tableName" placeholder="请输入表名" clearable />
        </el-form-item>

        <el-form-item label="表类型">
          <el-select v-model="tableQuery.tableType" placeholder="请选择" clearable>
            <el-option label="数据表" value="TABLE" />
            <el-option label="视图" value="VIEW" />
          </el-select>
        </el-form-item>

        <div class="search-actions">
          <el-button type="primary" @click="handleSearchTables">查询</el-button>
          <el-button @click="handleResetTables">重置</el-button>
        </div>
      </el-form>

      <el-table
        v-loading="tableLoading"
        :data="tableRecords"
        border
        stripe
        class="data-table"
        @row-click="handleSelectTable"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="dataSourceName" label="数据源" min-width="140" />
        <el-table-column prop="schemaName" label="库名" min-width="140" />
        <el-table-column prop="tableName" label="表名" min-width="180" />
        <el-table-column prop="tableComment" label="表注释" min-width="220" show-overflow-tooltip />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ tableTypeName(row.tableType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rowCount" label="行数" width="100" />
        <el-table-column prop="collectTime" label="采集时间" min-width="170" />
        <el-table-column label="操作" width="110" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click.stop="handleSelectTable(row)">
              查看字段
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="tableQuery.pageNo"
          v-model:page-size="tableQuery.pageSize"
          :total="tableTotal"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadTables"
          @current-change="loadTables"
        />
      </div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-header">
          <span>
            字段元数据
            <span v-if="selectedTable" class="selected-table">
              当前表：{{ selectedTable.tableName }}
            </span>
          </span>
          <el-button size="small" @click="loadColumns">刷新</el-button>
        </div>
      </template>

      <el-form class="search-grid" label-width="90px">
        <el-form-item label="字段名">
          <el-input v-model="columnQuery.columnName" placeholder="请输入字段名" clearable />
        </el-form-item>

        <el-form-item label="数据类型">
          <el-input v-model="columnQuery.dataType" placeholder="例如 VARCHAR" clearable />
        </el-form-item>

        <el-form-item label="是否主键">
          <el-select v-model="columnQuery.primaryKeyFlag" placeholder="请选择" clearable>
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
        </el-form-item>

        <div class="search-actions">
          <el-button type="primary" @click="handleSearchColumns">查询</el-button>
          <el-button @click="handleResetColumns">重置</el-button>
        </div>
      </el-form>

      <el-table
        v-loading="columnLoading"
        :data="columnRecords"
        border
        stripe
        class="data-table"
      >
        <el-table-column prop="ordinalPosition" label="顺序" width="80" />
        <el-table-column prop="columnName" label="字段名" min-width="160" />
        <el-table-column prop="columnComment" label="字段注释" min-width="200" show-overflow-tooltip />
        <el-table-column prop="columnType" label="字段类型" min-width="140" />
        <el-table-column prop="dataType" label="基础类型" min-width="120" />
        <el-table-column label="主键" width="90">
          <template #default="{ row }">
            <el-tag :type="row.primaryKeyFlag === 1 ? 'success' : 'info'">
              {{ yesNoName(row.primaryKeyFlag) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="可为空" width="90">
          <template #default="{ row }">
            <el-tag :type="row.nullableFlag === 1 ? 'warning' : 'info'">
              {{ yesNoName(row.nullableFlag) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="columnDefault" label="默认值" min-width="120" show-overflow-tooltip />
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="columnQuery.pageNo"
          v-model:page-size="columnQuery.pageSize"
          :total="columnTotal"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadColumns"
          @current-change="loadColumns"
        />
      </div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-header">
          <span>采集日志</span>
          <el-button size="small" @click="loadLogs">刷新</el-button>
        </div>
      </template>

      <el-form class="search-grid" label-width="100px">
        <el-form-item label="数据源ID">
          <el-input-number v-model="logQuery.dataSourceId" :min="1" clearable />
        </el-form-item>

        <el-form-item label="采集批次">
          <el-input v-model="logQuery.collectBatchNo" placeholder="请输入批次号" clearable />
        </el-form-item>

        <el-form-item label="采集状态">
          <el-select v-model="logQuery.collectStatus" placeholder="请选择" clearable>
            <el-option label="成功" :value="0" />
            <el-option label="失败" :value="1" />
          </el-select>
        </el-form-item>

        <div class="search-actions">
          <el-button type="primary" @click="handleSearchLogs">查询</el-button>
        </div>
      </el-form>

      <el-table
        v-loading="logLoading"
        :data="logRecords"
        border
        stripe
        class="data-table"
      >
        <el-table-column prop="collectBatchNo" label="采集批次" min-width="180" />
        <el-table-column prop="dataSourceName" label="数据源" min-width="140" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="collectStatusTag(row.collectStatus)">
              {{ collectStatusName(row.collectStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="tableTotal" label="表数量" width="100" />
        <el-table-column prop="columnTotal" label="字段数量" width="100" />
        <el-table-column prop="costTime" label="耗时ms" width="110" />
        <el-table-column prop="startTime" label="开始时间" min-width="170" />
        <el-table-column prop="errorMsg" label="错误信息" min-width="220" show-overflow-tooltip />
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="logQuery.pageNo"
          v-model:page-size="logQuery.pageSize"
          :total="logTotal"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadLogs"
          @current-change="loadLogs"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.metadata-page {
  padding: 20px;
  background: #f5f7fb;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}

.page-header h2 {
  margin: 0;
  font-size: 22px;
  color: #1f2f3d;
}

.page-header p {
  margin: 8px 0 0;
  color: #6b7280;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.summary-card {
  border-radius: 12px;
}

.summary-label {
  color: #6b7280;
  font-size: 14px;
}

.summary-value {
  margin-top: 8px;
  font-size: 28px;
  font-weight: 700;
  color: #1f2f3d;
}

.section-card {
  margin-bottom: 16px;
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.collect-grid {
  display: grid;
  grid-template-columns: minmax(260px, 420px) 1fr;
  gap: 16px;
  align-items: center;
}

.collect-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.tip-text {
  color: #6b7280;
  font-size: 13px;
}

.search-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(180px, 1fr));
  gap: 8px 16px;
  margin-bottom: 12px;
  align-items: center;
}

.search-actions {
  display: flex;
  gap: 8px;
  align-items: center;
  padding-left: 4px;
}

.data-table {
  width: 100%;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.selected-table {
  margin-left: 12px;
  font-size: 13px;
  color: #409eff;
}

@media (max-width: 1200px) {
  .summary-grid {
    grid-template-columns: repeat(2, minmax(180px, 1fr));
  }

  .search-grid {
    grid-template-columns: repeat(2, minmax(180px, 1fr));
  }

  .collect-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .summary-grid {
    grid-template-columns: 1fr;
  }

  .search-grid {
    grid-template-columns: 1fr;
  }
}
</style>
