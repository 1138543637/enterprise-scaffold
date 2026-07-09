<template>
  <div class="iam-permission-audit-page">
    <section class="page-header">
      <div>
        <p class="page-subtitle">Enterprise Scaffold / IAM</p>
        <h1>权限审计增强</h1>
        <p class="page-desc">
          基于 iam_permission_audit 表记录用户、角色、菜单、数据范围等权限变化，支持分页查询、模拟审计记录和复核处理。
        </p>
      </div>
      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button @click="handleReset">重置</el-button>
        <el-button type="primary" @click="openSimulateDialog">模拟审计记录</el-button>
      </div>
    </section>

    <section class="stat-grid">
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">本页记录</span>
        <strong>{{ records.length }}</strong>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">待复核</span>
        <strong>{{ pendingCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">高风险</span>
        <strong>{{ highRiskCount }}</strong>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <span class="stat-label">已复核</span>
        <strong>{{ reviewedCount }}</strong>
      </el-card>
    </section>

    <el-card shadow="never" class="query-card">
      <div class="query-grid">
        <el-input v-model="queryForm.auditCode" clearable placeholder="审计编码" />
        <el-select v-model="queryForm.auditType" clearable placeholder="审计类型">
          <el-option label="用户角色变更" value="USER_ROLE" />
          <el-option label="角色菜单变更" value="ROLE_MENU" />
          <el-option label="用户状态变更" value="USER_STATUS" />
          <el-option label="菜单权限变更" value="MENU_PERMISSION" />
          <el-option label="数据范围变更" value="DATA_SCOPE" />
        </el-select>
        <el-select v-model="queryForm.targetType" clearable placeholder="对象类型">
          <el-option label="用户" value="USER" />
          <el-option label="角色" value="ROLE" />
          <el-option label="菜单" value="MENU" />
          <el-option label="接口" value="API" />
        </el-select>
        <el-input v-model="queryForm.targetName" clearable placeholder="对象名称" />
        <el-select v-model="queryForm.changeAction" clearable placeholder="变更动作">
          <el-option label="授权" value="GRANT" />
          <el-option label="撤销" value="REVOKE" />
          <el-option label="修改" value="UPDATE" />
          <el-option label="启用" value="ENABLE" />
          <el-option label="停用" value="DISABLE" />
        </el-select>
        <el-select v-model="queryForm.riskLevel" clearable placeholder="风险等级">
          <el-option label="低" value="LOW" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="高" value="HIGH" />
        </el-select>
        <el-select v-model="queryForm.reviewStatus" clearable placeholder="复核状态">
          <el-option label="待复核" value="PENDING" />
          <el-option label="已复核" value="REVIEWED" />
          <el-option label="已忽略" value="IGNORED" />
        </el-select>
        <el-input v-model="queryForm.operatorName" clearable placeholder="操作人" />
      </div>
      <div class="query-actions">
        <el-button type="primary" :loading="loading" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="records" border>
        <el-table-column prop="auditCode" label="审计编码" min-width="180" show-overflow-tooltip />
        <el-table-column prop="auditTypeName" label="审计类型" min-width="130" />
        <el-table-column prop="targetTypeName" label="对象类型" width="100" />
        <el-table-column prop="targetName" label="对象名称" min-width="130" show-overflow-tooltip />
        <el-table-column prop="changeActionName" label="变更动作" width="100" />
        <el-table-column label="风险等级" width="100">
          <template #default="{ row }">
            <el-tag :type="riskTagType(row.riskLevel)">{{ row.riskLevelName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="复核状态" width="110">
          <template #default="{ row }">
            <el-tag :type="reviewTagType(row.reviewStatus)">{{ row.reviewStatusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operatorName" label="操作人" width="110" />
        <el-table-column prop="requestIp" label="请求IP" min-width="130" />
        <el-table-column prop="createTime" label="创建时间" min-width="170" />
        <el-table-column label="操作" fixed="right" width="180">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetailDialog(row)">详情</el-button>
            <el-button link type="success" @click="openReviewDialog(row)">复核</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-row">
        <el-pagination
          v-model:current-page="queryForm.pageNo"
          v-model:page-size="queryForm.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <el-dialog v-model="simulateDialogVisible" title="模拟新增权限审计记录" width="760px">
      <div class="dialog-grid">
        <el-select v-model="simulateForm.auditType" placeholder="审计类型">
          <el-option label="用户角色变更" value="USER_ROLE" />
          <el-option label="角色菜单变更" value="ROLE_MENU" />
          <el-option label="用户状态变更" value="USER_STATUS" />
          <el-option label="菜单权限变更" value="MENU_PERMISSION" />
          <el-option label="数据范围变更" value="DATA_SCOPE" />
        </el-select>
        <el-select v-model="simulateForm.targetType" placeholder="对象类型">
          <el-option label="用户" value="USER" />
          <el-option label="角色" value="ROLE" />
          <el-option label="菜单" value="MENU" />
          <el-option label="接口" value="API" />
        </el-select>
        <el-input-number v-model="simulateForm.targetId" :min="1" placeholder="对象ID" class="full-width" />
        <el-input v-model="simulateForm.targetName" placeholder="对象名称" />
        <el-select v-model="simulateForm.changeAction" placeholder="变更动作">
          <el-option label="授权" value="GRANT" />
          <el-option label="撤销" value="REVOKE" />
          <el-option label="修改" value="UPDATE" />
          <el-option label="启用" value="ENABLE" />
          <el-option label="停用" value="DISABLE" />
        </el-select>
        <el-select v-model="simulateForm.riskLevel" placeholder="风险等级">
          <el-option label="低" value="LOW" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="高" value="HIGH" />
        </el-select>
        <el-input v-model="simulateForm.operatorName" placeholder="操作人" />
        <el-input v-model="simulateForm.requestIp" placeholder="请求IP" />
      </div>
      <el-input v-model="simulateForm.beforeValue" class="mt-12" type="textarea" :rows="3" placeholder="变更前内容" />
      <el-input v-model="simulateForm.afterValue" class="mt-12" type="textarea" :rows="3" placeholder="变更后内容" />
      <el-input v-model="simulateForm.remark" class="mt-12" type="textarea" :rows="2" placeholder="备注" />
      <template #footer>
        <el-button @click="simulateDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSimulate">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="reviewDialogVisible" title="复核权限审计记录" width="560px">
      <div class="dialog-grid">
        <el-select v-model="reviewForm.reviewStatus" placeholder="复核状态">
          <el-option label="已复核" value="REVIEWED" />
          <el-option label="已忽略" value="IGNORED" />
          <el-option label="待复核" value="PENDING" />
        </el-select>
        <el-input v-model="reviewForm.reviewBy" placeholder="复核人" />
      </div>
      <el-input v-model="reviewForm.remark" class="mt-12" type="textarea" :rows="3" placeholder="复核备注" />
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleReview">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="权限审计详情" width="760px">
      <el-descriptions v-if="currentRow" :column="1" border>
        <el-descriptions-item label="审计编码">{{ currentRow.auditCode }}</el-descriptions-item>
        <el-descriptions-item label="变更前内容">{{ currentRow.beforeValue || '-' }}</el-descriptions-item>
        <el-descriptions-item label="变更后内容">{{ currentRow.afterValue || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ currentRow.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getIamPermissionAuditPage,
  reviewIamPermissionAudit,
  simulateIamPermissionAudit,
  type IamPermissionAuditPageQuery,
  type IamPermissionAuditPageVO,
  type IamPermissionAuditReviewRequest,
  type IamPermissionAuditSimulateRequest
} from '../../api/iam/permissionAudit'
import {useRouter} from "vue-router";
const router = useRouter()
const loading = ref(false)
const saving = ref(false)
const total = ref(0)
const records = ref<IamPermissionAuditPageVO[]>([])

const simulateDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentRow = ref<IamPermissionAuditPageVO | null>(null)

const queryForm = reactive<IamPermissionAuditPageQuery>({
  pageNo: 1,
  pageSize: 10,
  auditCode: '',
  auditType: '',
  targetType: '',
  targetName: '',
  changeAction: '',
  riskLevel: '',
  reviewStatus: '',
  operatorName: ''
})

const simulateForm = reactive<IamPermissionAuditSimulateRequest>({
  auditType: 'USER_ROLE',
  targetType: 'USER',
  targetId: 1,
  targetName: 'demo_user',
  changeAction: 'GRANT',
  beforeValue: 'roles=[普通用户]',
  afterValue: 'roles=[普通用户,审计员]',
  riskLevel: 'HIGH',
  requestIp: '127.0.0.1',
  operatorName: 'admin',
  remark: '前端模拟生成权限审计记录'
})

const reviewForm = reactive<IamPermissionAuditReviewRequest>({
  reviewStatus: 'REVIEWED',
  reviewBy: 'admin',
  remark: ''
})

const pendingCount = computed(() => records.value.filter((item) => item.reviewStatus === 'PENDING').length)
const reviewedCount = computed(() => records.value.filter((item) => item.reviewStatus === 'REVIEWED').length)
const highRiskCount = computed(() => records.value.filter((item) => item.riskLevel === 'HIGH').length)

function riskTagType(level?: string) {
  if (level === 'HIGH') return 'danger'
  if (level === 'MEDIUM') return 'warning'
  return 'info'
}

function reviewTagType(status?: string) {
  if (status === 'REVIEWED') return 'success'
  if (status === 'IGNORED') return 'info'
  return 'warning'
}

async function loadData() {
  loading.value = true
  try {
    const page = await getIamPermissionAuditPage(queryForm)
    records.value = Array.isArray(page.records) ? page.records : []
    total.value = Number(page.total || 0)
  } catch (error) {
    records.value = []
    total.value = 0
    ElMessage.error('权限审计加载失败：请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryForm.pageNo = 1
  loadData()
}
function goDashboard() {
  router.push('/dashboard')
}
function handleReset() {
  queryForm.pageNo = 1
  queryForm.pageSize = 10
  queryForm.auditCode = ''
  queryForm.auditType = ''
  queryForm.targetType = ''
  queryForm.targetName = ''
  queryForm.changeAction = ''
  queryForm.riskLevel = ''
  queryForm.reviewStatus = ''
  queryForm.operatorName = ''
  loadData()
}

function openSimulateDialog() {
  simulateDialogVisible.value = true
}

async function handleSimulate() {
  saving.value = true
  try {
    await simulateIamPermissionAudit(simulateForm)
    ElMessage.success('权限审计记录已生成')
    simulateDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('生成失败：请检查 F12 Network、ApiResult 解包和后端日志')
  } finally {
    saving.value = false
  }
}

function openReviewDialog(row: IamPermissionAuditPageVO) {
  currentRow.value = row
  reviewForm.reviewStatus = row.reviewStatus === 'PENDING' ? 'REVIEWED' : row.reviewStatus
  reviewForm.reviewBy = 'admin'
  reviewForm.remark = row.remark || ''
  reviewDialogVisible.value = true
}

async function handleReview() {
  if (!currentRow.value?.id) {
    ElMessage.error('请选择要复核的记录')
    return
  }
  saving.value = true
  try {
    await reviewIamPermissionAudit(currentRow.value.id, reviewForm)
    ElMessage.success('复核成功')
    reviewDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('复核失败：请检查 F12 Network、ApiResult 解包和后端日志')
  } finally {
    saving.value = false
  }
}

function openDetailDialog(row: IamPermissionAuditPageVO) {
  currentRow.value = row
  detailDialogVisible.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.iam-permission-audit-page {
  padding: 24px;
}

.page-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 16px;
  align-items: center;
  margin-bottom: 16px;
}

.page-subtitle {
  margin: 0 0 6px;
  color: #909399;
  font-size: 13px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.page-desc {
  margin: 8px 0 0;
  color: #606266;
  line-height: 1.7;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card :deep(.el-card__body) {
  display: grid;
  gap: 8px;
}

.stat-label {
  color: #909399;
  font-size: 13px;
}

.stat-card strong {
  font-size: 24px;
  color: #303133;
}

.query-card {
  margin-bottom: 16px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 12px;
}

.query-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
  margin-top: 12px;
}

.table-card {
  margin-bottom: 16px;
}

.pagination-row {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.dialog-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 12px;
}

.full-width {
  width: 100%;
}

.mt-12 {
  margin-top: 12px;
}

@media (max-width: 768px) {
  .iam-permission-audit-page {
    padding: 12px;
  }

  .page-header {
    grid-template-columns: 1fr;
  }

  .header-actions {
    justify-content: flex-start;
  }
}
</style>
