<template>
  <div class="iam-security-policy-page">
    <section class="page-header">
      <div>
        <p class="page-subtitle">IAM Security</p>
        <h1>安全策略配置</h1>
        <p class="page-desc">
          当前页面用于管理 IAM 安全策略，包括登录失败阈值、接口限流默认值、风险处理时限和审计日志保留周期。
          本阶段只做策略配置和管理，不重写登录流程，不修改 JWT，不做真实拦截。
        </p>
      </div>
      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button type="primary" :loading="loading" @click="loadData">刷新</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </div>
    </section>

    <section class="stat-grid">
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">策略总数</span>
        <strong>{{ page.total }}</strong>
        <em>当前查询范围</em>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">启用策略</span>
        <strong>{{ enabledCount }}</strong>
        <em>enabled = 1</em>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">停用策略</span>
        <strong>{{ disabledCount }}</strong>
        <em>enabled = 0</em>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">高等级策略</span>
        <strong>{{ highLevelCount }}</strong>
        <em>policyLevel = 3</em>
      </el-card>
    </section>

    <el-card shadow="never" class="query-card">
      <div class="query-grid">
        <el-form-item label="策略编码">
          <el-input v-model="query.policyCode" placeholder="例如 SP-LOGIN" clearable />
        </el-form-item>
        <el-form-item label="策略名称">
          <el-input v-model="query.policyName" placeholder="例如 登录失败" clearable />
        </el-form-item>
        <el-form-item label="策略类型">
          <el-select v-model="query.policyType" placeholder="全部" clearable>
            <el-option label="登录安全" value="LOGIN" />
            <el-option label="接口限流" value="RATE_LIMIT" />
            <el-option label="风险处理" value="RISK" />
            <el-option label="审计保留" value="AUDIT" />
          </el-select>
        </el-form-item>
        <el-form-item label="策略等级">
          <el-select v-model="query.policyLevel" placeholder="全部" clearable>
            <el-option label="低" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="高" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="生效范围">
          <el-select v-model="query.effectiveScope" placeholder="全部" clearable>
            <el-option label="全局" value="GLOBAL" />
            <el-option label="用户" value="USER" />
            <el-option label="客户端 IP" value="IP" />
            <el-option label="接口" value="API" />
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-select v-model="query.enabled" placeholder="全部" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务状态">
          <el-select v-model="query.status" placeholder="全部" clearable>
            <el-option label="正常" :value="0" />
            <el-option label="禁用" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DDTHH:mm:ss"
            clearable
          />
        </el-form-item>
      </div>
      <div class="query-actions">
        <el-button type="primary" :loading="loading" @click="handleSearch">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </div>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="policyCode" label="策略编码" min-width="190" show-overflow-tooltip />
        <el-table-column prop="policyName" label="策略名称" min-width="170" show-overflow-tooltip />
        <el-table-column label="策略类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.policyTypeName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="策略等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.policyLevel)">{{ row.policyLevelName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="policyValue" label="策略值" width="100" />
        <el-table-column prop="policyUnitName" label="单位" width="90" />
        <el-table-column prop="effectiveScopeName" label="生效范围" width="120" />
        <el-table-column label="启用状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled === 1 ? 'success' : 'info'">{{ row.enabledName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="业务状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.statusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="170" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" min-width="240" show-overflow-tooltip />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" text @click="openEditDialog(row)">配置</el-button>
            <el-button
              v-if="row.enabled === 0"
              size="small"
              type="success"
              text
              @click="handleEnable(row)"
            >启用</el-button>
            <el-button
              v-if="row.enabled === 1"
              size="small"
              type="warning"
              text
              @click="handleDisable(row)"
            >停用</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-row">
        <el-pagination
          v-model:current-page="query.pageNo"
          v-model:page-size="query.pageSize"
          :total="page.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <el-dialog v-model="editDialogVisible" title="配置安全策略" width="640px">
      <el-form label-width="110px" class="edit-form">
        <el-form-item label="策略编码">
          <el-input v-model="editForm.policyCode" disabled />
        </el-form-item>
        <el-form-item label="策略名称">
          <el-input v-model="editForm.policyName" disabled />
        </el-form-item>
        <div class="dialog-grid">
          <el-form-item label="策略值">
            <el-input v-model="editForm.policyValue" placeholder="请输入策略值" />
          </el-form-item>
          <el-form-item label="策略单位">
            <el-select v-model="editForm.policyUnit" placeholder="请选择单位">
              <el-option label="次" value="COUNT" />
              <el-option label="秒" value="SECOND" />
              <el-option label="天" value="DAY" />
              <el-option label="等级" value="LEVEL" />
            </el-select>
          </el-form-item>
          <el-form-item label="生效范围">
            <el-select v-model="editForm.effectiveScope" placeholder="请选择范围">
              <el-option label="全局" value="GLOBAL" />
              <el-option label="用户" value="USER" />
              <el-option label="客户端 IP" value="IP" />
              <el-option label="接口" value="API" />
            </el-select>
          </el-form-item>
          <el-form-item label="是否启用">
            <el-select v-model="editForm.enabled" placeholder="请选择状态">
              <el-option label="启用" :value="1" />
              <el-option label="停用" :value="0" />
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="备注">
          <el-input v-model="editForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleUpdate">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  disableIamSecurityPolicy,
  enableIamSecurityPolicy,
  getIamSecurityPolicyPage,
  updateIamSecurityPolicy,
  type IamSecurityPolicyPageQuery,
  type IamSecurityPolicyPageVO
} from '../../api/iam/securityPolicy'
import {useRouter} from "vue-router";
const router = useRouter()
const loading = ref(false)
const saving = ref(false)
const records = ref<IamSecurityPolicyPageVO[]>([])
const timeRange = ref<string[]>([])
const editDialogVisible = ref(false)

const query = reactive<IamSecurityPolicyPageQuery>({
  pageNo: 1,
  pageSize: 10,
  policyCode: '',
  policyName: '',
  policyType: '',
  policyLevel: undefined,
  effectiveScope: '',
  enabled: undefined,
  status: undefined,
  beginTime: '',
  endTime: ''
})

const page = reactive({
  total: 0,
  pages: 0
})

const editForm = reactive({
  id: 0,
  policyCode: '',
  policyName: '',
  policyValue: '',
  policyUnit: '',
  effectiveScope: '',
  enabled: 1,
  remark: ''
})

const enabledCount = computed(() => records.value.filter((item) => item.enabled === 1).length)
const disabledCount = computed(() => records.value.filter((item) => item.enabled === 0).length)
const highLevelCount = computed(() => records.value.filter((item) => item.policyLevel === 3).length)

function buildQueryParams(): IamSecurityPolicyPageQuery {
  const params: IamSecurityPolicyPageQuery = {
    ...query,
    pageNo: query.pageNo || 1,
    pageSize: query.pageSize || 10
  }
  if (Array.isArray(timeRange.value) && timeRange.value.length === 2) {
    params.beginTime = timeRange.value[0]
    params.endTime = timeRange.value[1]
  } else {
    params.beginTime = ''
    params.endTime = ''
  }
  return params
}
function goDashboard() {
  router.push('/dashboard')
}
async function loadData() {
  loading.value = true
  try {
    const result = await getIamSecurityPolicyPage(buildQueryParams())
    records.value = Array.isArray(result.records) ? result.records : []
    page.total = Number(result.total || 0)
    page.pages = Number(result.pages || 0)
  } catch (error) {
    records.value = []
    page.total = 0
    page.pages = 0
    console.error(error)
    ElMessage.error('安全策略配置加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.pageNo = 1
  loadData()
}

function resetQuery() {
  query.pageNo = 1
  query.pageSize = 10
  query.policyCode = ''
  query.policyName = ''
  query.policyType = ''
  query.policyLevel = undefined
  query.effectiveScope = ''
  query.enabled = undefined
  query.status = undefined
  query.beginTime = ''
  query.endTime = ''
  timeRange.value = []
  loadData()
}

function openEditDialog(row: IamSecurityPolicyPageVO) {
  editForm.id = row.id
  editForm.policyCode = row.policyCode
  editForm.policyName = row.policyName
  editForm.policyValue = row.policyValue
  editForm.policyUnit = row.policyUnit
  editForm.effectiveScope = row.effectiveScope
  editForm.enabled = row.enabled
  editForm.remark = row.remark || ''
  editDialogVisible.value = true
}

async function handleUpdate() {
  if (!editForm.id) {
    ElMessage.warning('请选择要配置的安全策略')
    return
  }
  if (!editForm.policyValue) {
    ElMessage.warning('策略值不能为空')
    return
  }
  saving.value = true
  try {
    await updateIamSecurityPolicy(editForm.id, {
      policyValue: editForm.policyValue,
      policyUnit: editForm.policyUnit,
      effectiveScope: editForm.effectiveScope,
      enabled: editForm.enabled,
      remark: editForm.remark
    })
    ElMessage.success('安全策略配置已保存')
    editDialogVisible.value = false
    await loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('保存失败，请检查 F12 Network、ApiResult 解包和 Docker 后端日志')
  } finally {
    saving.value = false
  }
}

async function handleEnable(row: IamSecurityPolicyPageVO) {
  await ElMessageBox.confirm(`确认启用策略“${row.policyName}”吗？`, '启用策略', { type: 'warning' })
  await enableIamSecurityPolicy(row.id)
  ElMessage.success('启用成功')
  await loadData()
}

async function handleDisable(row: IamSecurityPolicyPageVO) {
  await ElMessageBox.confirm(`确认停用策略“${row.policyName}”吗？`, '停用策略', { type: 'warning' })
  await disableIamSecurityPolicy(row.id)
  ElMessage.success('停用成功')
  await loadData()
}

function getLevelTagType(level: number) {
  if (level === 3) {
    return 'danger'
  }
  if (level === 2) {
    return 'warning'
  }
  return 'success'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.iam-security-policy-page {
  padding: 24px;
  background: #f5f7fb;
  min-height: 100vh;
}

.page-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 20px;
  align-items: start;
  margin-bottom: 18px;
}

.page-subtitle {
  margin: 0 0 4px;
  color: #64748b;
  font-size: 13px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.page-header h1 {
  margin: 0;
  color: #111827;
  font-size: 26px;
}

.page-desc {
  margin: 8px 0 0;
  max-width: 980px;
  color: #64748b;
  line-height: 1.7;
}

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 14px;
  margin-bottom: 16px;
}

.stat-card :deep(.el-card__body) {
  display: grid;
  gap: 6px;
}

.stat-label {
  color: #64748b;
  font-size: 13px;
}

.stat-card strong {
  color: #111827;
  font-size: 30px;
  line-height: 1;
}

.stat-card em {
  color: #94a3b8;
  font-style: normal;
  font-size: 12px;
}

.query-card,
.table-card {
  margin-bottom: 16px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px 16px;
}

.query-grid :deep(.el-form-item) {
  margin-bottom: 0;
}

.query-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 16px;
}

.pagination-row {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.edit-form {
  display: grid;
  gap: 8px;
}

.dialog-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 0 12px;
}

@media (max-width: 768px) {
  .iam-security-policy-page {
    padding: 16px;
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
