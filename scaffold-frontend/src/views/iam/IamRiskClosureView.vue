<template>
  <div class="iam-risk-closure-page">
    <div class="page-header">
      <div>
        <p class="page-subtitle">Enterprise Scaffold / IAM</p>
        <h1>IAM 风险闭环处理</h1>
        <p class="page-desc">
          本页面用于处理 I5-03 异常登录风险和 I5-06 权限审计记录，形成“发现风险 -> 人工处理 -> 留痕复核 -> 看板汇总”的闭环。
        </p>
      </div>
      <el-button @click="goDashboard">返回首页</el-button>
      <el-button type="primary" :loading="loading" @click="loadAll">刷新数据</el-button>
    </div>

    <section class="summary-grid">
      <el-card shadow="hover" class="summary-card">
        <span class="summary-label">未处理登录风险</span>
        <strong>{{ summary.unhandledLoginRiskCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="summary-card">
        <span class="summary-label">已确认登录风险</span>
        <strong>{{ summary.confirmedLoginRiskCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="summary-card">
        <span class="summary-label">已忽略登录风险</span>
        <strong>{{ summary.ignoredLoginRiskCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="summary-card">
        <span class="summary-label">已关闭登录风险</span>
        <strong>{{ summary.closedLoginRiskCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="summary-card">
        <span class="summary-label">待复核权限审计</span>
        <strong>{{ summary.pendingAuditCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="summary-card">
        <span class="summary-label">已复核权限审计</span>
        <strong>{{ summary.reviewedAuditCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="summary-card">
        <span class="summary-label">已忽略权限审计</span>
        <strong>{{ summary.ignoredAuditCount }}</strong>
      </el-card>
    </section>

    <el-tabs v-model="activeTab" class="risk-tabs">
      <el-tab-pane label="异常登录风险闭环" name="loginRisk">
        <el-card shadow="never">
          <div class="search-grid">
            <el-input v-model="loginQuery.riskCode" clearable placeholder="风险编码" />
            <el-input v-model="loginQuery.username" clearable placeholder="用户名" />
            <el-input v-model="loginQuery.clientIp" clearable placeholder="客户端 IP" />
            <el-select v-model="loginQuery.riskType" clearable placeholder="风险类型">
              <el-option label="登录失败" value="LOGIN_FAILED" />
              <el-option label="短时间多次失败" value="SHORT_TIME_FAILED" />
              <el-option label="异常 IP" value="ABNORMAL_IP" />
            </el-select>
            <el-select v-model="loginQuery.riskLevel" clearable placeholder="风险等级">
              <el-option label="低风险" :value="1" />
              <el-option label="中风险" :value="2" />
              <el-option label="高风险" :value="3" />
            </el-select>
            <el-select v-model="loginQuery.handleStatus" clearable placeholder="处理状态">
              <el-option label="未处理" :value="0" />
              <el-option label="已确认" :value="1" />
              <el-option label="已忽略" :value="2" />
              <el-option label="已关闭" :value="3" />
            </el-select>
            <div class="search-actions">
              <el-button type="primary" @click="loadLoginRisks">查询</el-button>
              <el-button @click="resetLoginQuery">重置</el-button>
            </div>
          </div>

          <el-table :data="loginRiskRecords" v-loading="loading" border stripe class="data-table">
            <el-table-column prop="riskCode" label="风险编码" min-width="170" />
            <el-table-column prop="username" label="用户名" min-width="120" />
            <el-table-column prop="clientIp" label="客户端 IP" min-width="130" />
            <el-table-column prop="riskTypeName" label="风险类型" min-width="140" />
            <el-table-column label="风险等级" min-width="110">
              <template #default="scope">
                <el-tag :type="getLoginRiskLevelTag(scope.row.riskLevel)">{{ scope.row.riskLevelName || '-' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="failCount" label="失败次数" min-width="90" />
            <el-table-column prop="detectTime" label="检测时间" min-width="170" />
            <el-table-column label="处理状态" min-width="110">
              <template #default="scope">
                <el-tag :type="getLoginHandleStatusTag(scope.row.handleStatus)">{{ getLoginHandleStatusName(scope.row) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="handleBy" label="处理人" min-width="110" />
            <el-table-column prop="handleTime" label="处理时间" min-width="170" />
            <el-table-column label="操作" width="260" fixed="right">
              <template #default="scope">
                <el-button size="small" type="success" plain @click="openLoginHandleDialog(scope.row, 'confirm')">确认</el-button>
                <el-button size="small" type="warning" plain @click="openLoginHandleDialog(scope.row, 'ignore')">忽略</el-button>
                <el-button size="small" type="danger" plain @click="openLoginHandleDialog(scope.row, 'close')">关闭</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="loginQuery.pageNo"
              v-model:page-size="loginQuery.pageSize"
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="loginTotal"
              @size-change="loadLoginRisks"
              @current-change="loadLoginRisks"
            />
          </div>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="权限审计复核闭环" name="permissionAudit">
        <el-card shadow="never">
          <div class="search-grid">
            <el-input v-model="auditQuery.auditCode" clearable placeholder="审计编码" />
            <el-input v-model="auditQuery.targetName" clearable placeholder="对象名称" />
            <el-input v-model="auditQuery.operatorName" clearable placeholder="操作人" />
            <el-select v-model="auditQuery.auditType" clearable placeholder="审计类型">
              <el-option label="用户角色变更" value="USER_ROLE" />
              <el-option label="角色菜单变更" value="ROLE_MENU" />
              <el-option label="用户状态变更" value="USER_STATUS" />
              <el-option label="菜单权限变更" value="MENU_PERMISSION" />
              <el-option label="数据范围变更" value="DATA_SCOPE" />
            </el-select>
            <el-select v-model="auditQuery.riskLevel" clearable placeholder="风险等级">
              <el-option label="低" value="LOW" />
              <el-option label="中" value="MEDIUM" />
              <el-option label="高" value="HIGH" />
            </el-select>
            <el-select v-model="auditQuery.reviewStatus" clearable placeholder="复核状态">
              <el-option label="待复核" value="PENDING" />
              <el-option label="已复核" value="REVIEWED" />
              <el-option label="已忽略" value="IGNORED" />
            </el-select>
            <div class="search-actions">
              <el-button type="primary" @click="loadPermissionAudits">查询</el-button>
              <el-button @click="resetAuditQuery">重置</el-button>
            </div>
          </div>

          <el-table :data="auditRecords" v-loading="loading" border stripe class="data-table">
            <el-table-column prop="auditCode" label="审计编码" min-width="170" />
            <el-table-column prop="auditTypeName" label="审计类型" min-width="140" />
            <el-table-column prop="targetName" label="对象名称" min-width="140" />
            <el-table-column prop="changeActionName" label="变更动作" min-width="110" />
            <el-table-column label="风险等级" min-width="90">
              <template #default="scope">
                <el-tag :type="getAuditRiskLevelTag(scope.row.riskLevel)">{{ scope.row.riskLevelName || '-' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="复核状态" min-width="110">
              <template #default="scope">
                <el-tag :type="getAuditReviewStatusTag(scope.row.reviewStatus)">{{ scope.row.reviewStatusName || '-' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reviewBy" label="复核人" min-width="110" />
            <el-table-column prop="reviewTime" label="复核时间" min-width="170" />
            <el-table-column prop="createTime" label="创建时间" min-width="170" />
            <el-table-column label="操作" width="190" fixed="right">
              <template #default="scope">
                <el-button size="small" type="success" plain @click="openAuditHandleDialog(scope.row, 'review')">复核</el-button>
                <el-button size="small" type="warning" plain @click="openAuditHandleDialog(scope.row, 'ignore')">忽略</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="auditQuery.pageNo"
              v-model:page-size="auditQuery.pageSize"
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="auditTotal"
              @size-change="loadPermissionAudits"
              @current-change="loadPermissionAudits"
            />
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="loginDialog.visible" :title="loginDialog.title" width="560px">
      <el-form label-width="90px">
        <el-form-item label="风险编码">
          <el-input :model-value="loginDialog.row?.riskCode || '-'" disabled />
        </el-form-item>
        <el-form-item label="处理人">
          <el-input v-model="loginDialog.form.handleBy" placeholder="例如：admin" />
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input v-model="loginDialog.form.handleRemark" type="textarea" :rows="4" placeholder="请输入处理说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="loginDialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitLoginHandle">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="auditDialog.visible" :title="auditDialog.title" width="560px">
      <el-form label-width="90px">
        <el-form-item label="审计编码">
          <el-input :model-value="auditDialog.row?.auditCode || '-'" disabled />
        </el-form-item>
        <el-form-item label="复核人">
          <el-input v-model="auditDialog.form.reviewBy" placeholder="例如：admin" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="auditDialog.form.remark" type="textarea" :rows="4" placeholder="请输入复核说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitAuditHandle">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  closeIamLoginRisk,
  confirmIamLoginRisk,
  getIamLoginRiskPage,
  getIamPermissionAuditPage,
  getIamRiskClosureSummary,
  ignoreIamLoginRisk,
  ignoreIamPermissionAudit,
  reviewIamPermissionAudit,
  type IamLoginRiskPageVO,
  type IamPermissionAuditPageVO,
  type IamRiskClosureSummaryVO
} from '../../api/iam/riskClosure'
import {useRouter} from "vue-router";
const router = useRouter()
const loading = ref(false)
const saving = ref(false)
const activeTab = ref('loginRisk')

const summary = reactive<IamRiskClosureSummaryVO>({
  unhandledLoginRiskCount: 0,
  confirmedLoginRiskCount: 0,
  ignoredLoginRiskCount: 0,
  closedLoginRiskCount: 0,
  pendingAuditCount: 0,
  reviewedAuditCount: 0,
  ignoredAuditCount: 0
})

const loginQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  riskCode: '',
  username: '',
  clientIp: '',
  riskType: '',
  riskLevel: '' as number | string,
  handleStatus: '' as number | string
})

const auditQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  auditCode: '',
  auditType: '',
  targetName: '',
  riskLevel: '',
  reviewStatus: '',
  operatorName: ''
})

const loginRiskRecords = ref<IamLoginRiskPageVO[]>([])
const auditRecords = ref<IamPermissionAuditPageVO[]>([])
const loginTotal = ref(0)
const auditTotal = ref(0)

const loginDialog = reactive({
  visible: false,
  action: 'confirm',
  title: '处理登录风险',
  row: null as IamLoginRiskPageVO | null,
  form: {
    handleBy: 'admin',
    handleRemark: ''
  }
})

const auditDialog = reactive({
  visible: false,
  action: 'review',
  title: '处理权限审计',
  row: null as IamPermissionAuditPageVO | null,
  form: {
    reviewBy: 'admin',
    remark: ''
  }
})

async function loadSummary() {
  const data = await getIamRiskClosureSummary()
  Object.assign(summary, data)
}

async function loadLoginRisks() {
  loading.value = true
  try {
    const page = await getIamLoginRiskPage({ ...loginQuery })
    loginRiskRecords.value = Array.isArray(page.records) ? page.records : []
    loginTotal.value = Number(page.total || 0)
  } catch (error) {
    ElMessage.error('异常登录风险加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    loading.value = false
  }
}

async function loadPermissionAudits() {
  loading.value = true
  try {
    const page = await getIamPermissionAuditPage({ ...auditQuery })
    auditRecords.value = Array.isArray(page.records) ? page.records : []
    auditTotal.value = Number(page.total || 0)
  } catch (error) {
    ElMessage.error('权限审计记录加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    loading.value = false
  }
}
function goDashboard() {
  router.push('/dashboard')
}
async function loadAll() {
  loading.value = true
  try {
    await Promise.all([loadSummary(), loadLoginRisks(), loadPermissionAudits()])
  } catch (error) {
    ElMessage.error('风险闭环页面加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    loading.value = false
  }
}

function resetLoginQuery() {
  loginQuery.pageNo = 1
  loginQuery.pageSize = 10
  loginQuery.riskCode = ''
  loginQuery.username = ''
  loginQuery.clientIp = ''
  loginQuery.riskType = ''
  loginQuery.riskLevel = ''
  loginQuery.handleStatus = ''
  loadLoginRisks()
}

function resetAuditQuery() {
  auditQuery.pageNo = 1
  auditQuery.pageSize = 10
  auditQuery.auditCode = ''
  auditQuery.auditType = ''
  auditQuery.targetName = ''
  auditQuery.riskLevel = ''
  auditQuery.reviewStatus = ''
  auditQuery.operatorName = ''
  loadPermissionAudits()
}

function openLoginHandleDialog(row: IamLoginRiskPageVO, action: 'confirm' | 'ignore' | 'close') {
  loginDialog.row = row
  loginDialog.action = action
  loginDialog.title = action === 'confirm' ? '确认登录风险' : action === 'ignore' ? '忽略登录风险' : '关闭登录风险'
  loginDialog.form.handleBy = 'admin'
  loginDialog.form.handleRemark = ''
  loginDialog.visible = true
}

function openAuditHandleDialog(row: IamPermissionAuditPageVO, action: 'review' | 'ignore') {
  auditDialog.row = row
  auditDialog.action = action
  auditDialog.title = action === 'review' ? '复核权限审计' : '忽略权限审计'
  auditDialog.form.reviewBy = 'admin'
  auditDialog.form.remark = ''
  auditDialog.visible = true
}

async function submitLoginHandle() {
  if (!loginDialog.row?.id) {
    ElMessage.error('请选择需要处理的登录风险')
    return
  }
  saving.value = true
  try {
    const payload = { ...loginDialog.form }
    if (loginDialog.action === 'confirm') {
      await confirmIamLoginRisk(loginDialog.row.id, payload)
    } else if (loginDialog.action === 'ignore') {
      await ignoreIamLoginRisk(loginDialog.row.id, payload)
    } else {
      await closeIamLoginRisk(loginDialog.row.id, payload)
    }
    ElMessage.success('登录风险处理成功')
    loginDialog.visible = false
    await Promise.all([loadSummary(), loadLoginRisks()])
  } catch (error) {
    ElMessage.error('登录风险处理失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    saving.value = false
  }
}

async function submitAuditHandle() {
  if (!auditDialog.row?.id) {
    ElMessage.error('请选择需要处理的权限审计记录')
    return
  }
  saving.value = true
  try {
    const payload = { ...auditDialog.form }
    if (auditDialog.action === 'review') {
      await reviewIamPermissionAudit(auditDialog.row.id, payload)
    } else {
      await ignoreIamPermissionAudit(auditDialog.row.id, payload)
    }
    ElMessage.success('权限审计处理成功')
    auditDialog.visible = false
    await Promise.all([loadSummary(), loadPermissionAudits()])
  } catch (error) {
    ElMessage.error('权限审计处理失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    saving.value = false
  }
}

function getLoginRiskLevelTag(level?: number) {
  if (level === 3) return 'danger'
  if (level === 2) return 'warning'
  if (level === 1) return 'success'
  return 'info'
}

function getLoginHandleStatusTag(status?: number) {
  if (status === 1) return 'success'
  if (status === 2) return 'warning'
  if (status === 3) return 'info'
  return 'danger'
}

function getLoginHandleStatusName(row: IamLoginRiskPageVO) {
  if (row.handleStatusName) return row.handleStatusName
  if (row.handleStatus === 1) return '已确认'
  if (row.handleStatus === 2) return '已忽略'
  if (row.handleStatus === 3) return '已关闭'
  return '未处理'
}

function getAuditRiskLevelTag(level?: string) {
  if (level === 'HIGH') return 'danger'
  if (level === 'MEDIUM') return 'warning'
  if (level === 'LOW') return 'success'
  return 'info'
}

function getAuditReviewStatusTag(status?: string) {
  if (status === 'REVIEWED') return 'success'
  if (status === 'IGNORED') return 'warning'
  return 'danger'
}

onMounted(() => {
  loadAll()
})
</script>

<style scoped>
.iam-risk-closure-page {
  padding: 24px;
}

.page-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 16px;
  align-items: start;
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
  color: #0f172a;
}

.page-desc {
  margin: 8px 0 0;
  color: #64748b;
  line-height: 1.7;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 14px;
  margin-bottom: 18px;
}

.summary-card :deep(.el-card__body) {
  display: grid;
  gap: 10px;
}

.summary-label {
  color: #64748b;
  font-size: 13px;
}

.summary-card strong {
  font-size: 28px;
  color: #0f172a;
}

.risk-tabs {
  display: grid;
  gap: 14px;
}

.search-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
  margin-bottom: 16px;
}

.search-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.data-table {
  width: 100%;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .iam-risk-closure-page {
    padding: 14px;
  }

  .page-header {
    grid-template-columns: 1fr;
  }

  .pagination-wrapper {
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>
