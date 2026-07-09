<template>
  <div class="sensitive-page">
    <div class="page-header">
      <div>
        <h2>敏感数据识别和脱敏</h2>
        <p>基于元数据字段识别手机号、身份证、邮箱、银行卡、姓名、地址等敏感字段，并进行脱敏预览。</p>
      </div>
      <el-button @click="goDashboard">返回首页</el-button>
      <el-button type="primary" :loading="scanLoading" @click="handleScan">开始识别</el-button>
    </div>

    <div class="summary-grid">
      <el-card shadow="hover">
        <div class="summary-title">敏感字段数</div>
        <div class="summary-value">{{ sensitivePage.total }}</div>
      </el-card>
      <el-card shadow="hover">
        <div class="summary-title">脱敏规则数</div>
        <div class="summary-value">{{ maskRulePage.total }}</div>
      </el-card>
      <el-card shadow="hover">
        <div class="summary-title">脱敏预览数</div>
        <div class="summary-value">{{ maskResultPage.total }}</div>
      </el-card>
      <el-card shadow="hover">
        <div class="summary-title">当前识别结果</div>
        <div class="summary-value">{{ lastScanCount }}</div>
      </el-card>
    </div>

    <el-card shadow="never" class="query-card">
      <template #header>识别条件</template>
      <div class="query-grid">
        <el-input v-model="scanForm.targetTableId" placeholder="元数据表ID，可不填" clearable />
        <el-select v-model="scanForm.sensitiveType" placeholder="敏感类型，可不选" clearable>
          <el-option label="手机号" value="PHONE" />
          <el-option label="身份证" value="ID_CARD" />
          <el-option label="邮箱" value="EMAIL" />
          <el-option label="银行卡" value="BANK_CARD" />
          <el-option label="姓名" value="NAME" />
          <el-option label="地址" value="ADDRESS" />
        </el-select>
        <el-input v-model="scanForm.limit" placeholder="最多扫描字段数，默认500" clearable />
      </div>
    </el-card>

    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span>敏感字段识别结果</span>
          <el-button size="small" @click="loadSensitiveFields">刷新</el-button>
        </div>
      </template>

      <div class="query-grid">
        <el-input v-model="sensitiveQuery.datasourceName" placeholder="数据源名称" clearable @keyup.enter="loadSensitiveFields" />
        <el-input v-model="sensitiveQuery.tableName" placeholder="表名" clearable @keyup.enter="loadSensitiveFields" />
        <el-input v-model="sensitiveQuery.columnName" placeholder="字段名" clearable @keyup.enter="loadSensitiveFields" />
        <el-select v-model="sensitiveQuery.sensitiveType" placeholder="敏感类型" clearable @change="loadSensitiveFields">
          <el-option label="手机号" value="PHONE" />
          <el-option label="身份证" value="ID_CARD" />
          <el-option label="邮箱" value="EMAIL" />
          <el-option label="银行卡" value="BANK_CARD" />
          <el-option label="姓名" value="NAME" />
          <el-option label="地址" value="ADDRESS" />
        </el-select>
      </div>

      <el-table :data="sensitiveRecords" border stripe v-loading="sensitiveLoading">
        <el-table-column prop="fieldCode" label="字段编码" width="180" />
        <el-table-column prop="datasourceName" label="数据源" min-width="140" />
        <el-table-column prop="tableName" label="表名" min-width="150" />
        <el-table-column prop="columnName" label="字段名" min-width="150" />
        <el-table-column prop="columnComment" label="字段注释" min-width="160" />
        <el-table-column prop="dataType" label="数据类型" width="110" />
        <el-table-column prop="sensitiveType" label="敏感类型" width="120">
          <template #default="{ row }">
            <el-tag type="danger">{{ sensitiveTypeName(row.sensitiveType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sensitiveLevel" label="级别" width="80">
          <template #default="{ row }">
            <el-tag :type="levelTagType(row.sensitiveLevel)">{{ levelName(row.sensitiveLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="maskType" label="默认脱敏" width="110" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openPreview(row)">脱敏预览</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination
          layout="prev, pager, next, total"
          :total="sensitivePage.total"
          :page-size="sensitiveQuery.pageSize"
          v-model:current-page="sensitiveQuery.pageNo"
          @current-change="loadSensitiveFields"
        />
      </div>
    </el-card>

    <div class="two-column-grid">
      <el-card shadow="never">
        <template #header>
          <div class="card-header">
            <span>脱敏规则</span>
            <el-button size="small" @click="loadMaskRules">刷新</el-button>
          </div>
        </template>

        <el-table :data="maskRuleRecords" border stripe v-loading="maskRuleLoading">
          <el-table-column prop="ruleCode" label="规则编码" min-width="160" />
          <el-table-column prop="ruleName" label="规则名称" min-width="160" />
          <el-table-column prop="sensitiveType" label="敏感类型" width="110" />
          <el-table-column prop="maskMethod" label="方式" width="90" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 0 ? 'success' : 'info'">
                {{ row.status === 0 ? '启用' : '停用' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="card-header">
            <span>脱敏预览结果</span>
            <el-button size="small" @click="loadMaskResults">刷新</el-button>
          </div>
        </template>

        <el-table :data="maskResultRecords" border stripe v-loading="maskResultLoading">
          <el-table-column prop="resultCode" label="结果编码" min-width="170" />
          <el-table-column prop="columnName" label="字段名" min-width="120" />
          <el-table-column prop="sensitiveType" label="敏感类型" width="110" />
          <el-table-column prop="sampleBefore" label="脱敏前" min-width="150" />
          <el-table-column prop="sampleAfter" label="脱敏后" min-width="150" />
          <el-table-column prop="maskMethod" label="方式" width="90" />
        </el-table>
      </el-card>
    </div>

    <el-dialog v-model="previewDialogVisible" title="脱敏预览" width="520px">
      <el-form label-width="100px">
        <el-form-item label="字段">
          <span>{{ currentField?.tableName }}.{{ currentField?.columnName }}</span>
        </el-form-item>
        <el-form-item label="敏感类型">
          <el-tag type="danger">{{ sensitiveTypeName(currentField?.sensitiveType) }}</el-tag>
        </el-form-item>
        <el-form-item label="示例值">
          <el-input v-model="previewForm.rawValue" placeholder="可不填，不填则后端生成示例值" />
        </el-form-item>
        <el-form-item v-if="previewResult" label="脱敏前">
          <el-input :model-value="previewResult.sampleBefore" readonly />
        </el-form-item>
        <el-form-item v-if="previewResult" label="脱敏后">
          <el-input :model-value="previewResult.sampleAfter" readonly />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="previewLoading" @click="handlePreview">执行预览</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getMaskResultPage,
  getMaskRulePage,
  getSensitiveFieldPage,
  previewMask,
  scanSensitiveFields,
  type DatahubMaskResultPageVO,
  type DatahubMaskRulePageVO,
  type DatahubSensitiveFieldPageVO
} from '../../api/datahub/sensitive'
import {useRouter} from "vue-router";
const router = useRouter()
const scanLoading = ref(false)
const sensitiveLoading = ref(false)
const maskRuleLoading = ref(false)
const maskResultLoading = ref(false)
const previewLoading = ref(false)
const lastScanCount = ref(0)

const scanForm = reactive({
  targetTableId: '',
  sensitiveType: '',
  limit: '500'
})

const sensitiveQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  datasourceName: '',
  tableName: '',
  columnName: '',
  sensitiveType: ''
})

const maskRuleQuery = reactive({
  pageNo: 1,
  pageSize: 50
})

const maskResultQuery = reactive({
  pageNo: 1,
  pageSize: 10
})

const sensitivePage = reactive({
  total: 0,
  records: [] as DatahubSensitiveFieldPageVO[]
})

const maskRulePage = reactive({
  total: 0,
  records: [] as DatahubMaskRulePageVO[]
})

const maskResultPage = reactive({
  total: 0,
  records: [] as DatahubMaskResultPageVO[]
})

const sensitiveRecords = computed(() => Array.isArray(sensitivePage.records) ? sensitivePage.records : [])
const maskRuleRecords = computed(() => Array.isArray(maskRulePage.records) ? maskRulePage.records : [])
const maskResultRecords = computed(() => Array.isArray(maskResultPage.records) ? maskResultPage.records : [])

const previewDialogVisible = ref(false)
const currentField = ref<DatahubSensitiveFieldPageVO>()
const previewResult = ref<DatahubMaskResultPageVO>()
const previewForm = reactive({
  rawValue: ''
})
function goDashboard() {
  router.push('/dashboard')
}
async function handleScan() {
  scanLoading.value = true
  try {
    const data = await scanSensitiveFields({
      targetTableId: scanForm.targetTableId ? Number(scanForm.targetTableId) : undefined,
      sensitiveType: scanForm.sensitiveType || undefined,
      limit: scanForm.limit ? Number(scanForm.limit) : 500
    })
    lastScanCount.value = Array.isArray(data) ? data.length : 0
    ElMessage.success(`识别完成：新增或更新 ${lastScanCount.value} 个敏感字段`)
    await loadSensitiveFields()
  } catch (error) {
    console.error(error)
    ElMessage.error('敏感字段识别失败，请先查看 F12 Network，再查看 Docker 后端日志')
  } finally {
    scanLoading.value = false
  }
}

async function loadSensitiveFields() {
  sensitiveLoading.value = true
  try {
    const data = await getSensitiveFieldPage({ ...sensitiveQuery })
    sensitivePage.total = data?.total ?? 0
    sensitivePage.records = Array.isArray(data?.records) ? data.records : []
  } catch (error) {
    console.error(error)
    ElMessage.error('加载敏感字段失败')
  } finally {
    sensitiveLoading.value = false
  }
}

async function loadMaskRules() {
  maskRuleLoading.value = true
  try {
    const data = await getMaskRulePage({ ...maskRuleQuery })
    maskRulePage.total = data?.total ?? 0
    maskRulePage.records = Array.isArray(data?.records) ? data.records : []
  } catch (error) {
    console.error(error)
    ElMessage.error('加载脱敏规则失败')
  } finally {
    maskRuleLoading.value = false
  }
}

async function loadMaskResults() {
  maskResultLoading.value = true
  try {
    const data = await getMaskResultPage({ ...maskResultQuery })
    maskResultPage.total = data?.total ?? 0
    maskResultPage.records = Array.isArray(data?.records) ? data.records : []
  } catch (error) {
    console.error(error)
    ElMessage.error('加载脱敏结果失败')
  } finally {
    maskResultLoading.value = false
  }
}

function openPreview(row: DatahubSensitiveFieldPageVO) {
  currentField.value = row
  previewResult.value = undefined
  previewForm.rawValue = ''
  previewDialogVisible.value = true
}

async function handlePreview() {
  if (!currentField.value) {
    return
  }
  previewLoading.value = true
  try {
    const data = await previewMask({
      fieldId: currentField.value.id,
      rawValue: previewForm.rawValue || undefined
    })
    previewResult.value = data
    ElMessage.success('脱敏预览成功')
    await loadMaskResults()
  } catch (error) {
    console.error(error)
    ElMessage.error('脱敏预览失败')
  } finally {
    previewLoading.value = false
  }
}

function sensitiveTypeName(type?: string) {
  const map: Record<string, string> = {
    PHONE: '手机号',
    ID_CARD: '身份证',
    EMAIL: '邮箱',
    BANK_CARD: '银行卡',
    NAME: '姓名',
    ADDRESS: '地址'
  }
  return type ? (map[type] || type) : '-'
}

function levelName(level?: number) {
  if (level === 3) return '严重'
  if (level === 2) return '重要'
  return '一般'
}

function levelTagType(level?: number) {
  if (level === 3) return 'danger'
  if (level === 2) return 'warning'
  return 'info'
}

onMounted(async () => {
  await Promise.all([
    loadSensitiveFields(),
    loadMaskRules(),
    loadMaskResults()
  ])
})
</script>

<style scoped>
.sensitive-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: center;
  margin-bottom: 16px;
}

.page-header h2 {
  margin: 0 0 8px;
}

.page-header p {
  margin: 0;
  color: #666;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.summary-title {
  color: #666;
  font-size: 14px;
}

.summary-value {
  margin-top: 8px;
  font-size: 28px;
  font-weight: 700;
}

.query-card,
.table-card {
  margin-bottom: 16px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 14px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.two-column-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  gap: 16px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 14px;
}

@media (max-width: 1200px) {
  .summary-grid,
  .query-grid,
  .two-column-grid {
    grid-template-columns: 1fr;
  }

  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
