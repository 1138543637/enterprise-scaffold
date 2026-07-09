<template>
  <div class="iam-login-risk-page">
    <section class="page-header">
      <div>
        <p class="page-subtitle">Enterprise Scaffold / IAM</p>
        <h1>异常登录检测</h1>
        <p class="page-desc">
          基于系统登录日志识别登录失败、异常 IP、短时间多次失败等风险行为，为权限审计和数据安全分析提供基础。
        </p>
      </div>
      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button type="primary" :loading="detecting" @click="handleDetect">
          执行风险检测
        </el-button>
        <el-button @click="fetchPage">刷新</el-button>
      </div>
    </section>

    <section class="summary-grid">
      <el-card shadow="never" class="summary-card">
        <span class="summary-label">风险总数</span>
        <strong>{{ total }}</strong>
      </el-card>
      <el-card shadow="never" class="summary-card">
        <span class="summary-label">高风险</span>
        <strong>{{ highRiskCount }}</strong>
      </el-card>
      <el-card shadow="never" class="summary-card">
        <span class="summary-label">中风险</span>
        <strong>{{ middleRiskCount }}</strong>
      </el-card>
      <el-card shadow="never" class="summary-card">
        <span class="summary-label">未处理</span>
        <strong>{{ pendingCount }}</strong>
      </el-card>
    </section>

    <el-card shadow="never" class="search-card">
      <div class="search-grid">
        <el-input v-model="queryForm.riskCode" clearable placeholder="风险编码" />
        <el-input v-model="queryForm.username" clearable placeholder="用户名" />
        <el-input v-model="queryForm.clientIp" clearable placeholder="客户端 IP" />
        <el-select v-model="queryForm.riskType" clearable placeholder="风险类型">
          <el-option label="登录失败" value="LOGIN_FAILED" />
          <el-option label="短时间多次失败" value="SHORT_TIME_FAILED" />
          <el-option label="异常 IP" value="ABNORMAL_IP" />
        </el-select>
        <el-select v-model="queryForm.riskLevel" clearable placeholder="风险等级">
          <el-option label="低风险" :value="1" />
          <el-option label="中风险" :value="2" />
          <el-option label="高风险" :value="3" />
        </el-select>
        <el-select v-model="queryForm.handleStatus" clearable placeholder="处理状态">
          <el-option label="未处理" :value="0" />
          <el-option label="已确认" :value="1" />
          <el-option label="已忽略" :value="2" />
        </el-select>
        <el-date-picker
          v-model="timeRange"
          type="datetimerange"
          start-placeholder="开始检测时间"
          end-placeholder="结束检测时间"
          value-format="YYYY-MM-DDTHH:mm:ss"
          class="time-range"
        />
        <div class="search-actions">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="records"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="riskCode" label="风险编码" min-width="190" />
        <el-table-column prop="username" label="用户名" min-width="110" />
        <el-table-column prop="clientIp" label="客户端 IP" min-width="130" />
        <el-table-column prop="riskTypeName" label="风险类型" min-width="130">
          <template #default="{ row }">
            <el-tag>{{ row.riskTypeName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevelName" label="风险等级" min-width="100">
          <template #default="{ row }">
            <el-tag :type="riskLevelTagType(row.riskLevel)">
              {{ row.riskLevelName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="failCount" label="失败次数" min-width="90" />
        <el-table-column prop="firstFailTime" label="首次失败时间" min-width="170" />
        <el-table-column prop="lastFailTime" label="最近失败时间" min-width="170" />
        <el-table-column prop="detectTime" label="检测时间" min-width="170" />
        <el-table-column prop="handleStatusName" label="处理状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.handleStatus === 0 ? 'warning' : 'success'">
              {{ row.handleStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="260" show-overflow-tooltip />
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="queryForm.pageNo"
          v-model:page-size="queryForm.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="fetchPage"
          @current-change="fetchPage"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  detectIamLoginRisks,
  getIamLoginRiskPage,
  type IamLoginRiskPageQuery,
  type IamLoginRiskPageVO
} from '../../api/iam/loginRisk'
import {useRouter} from "vue-router";
const router = useRouter()
const loading = ref(false)
const detecting = ref(false)
const records = ref<IamLoginRiskPageVO[]>([])
const total = ref(0)
const timeRange = ref<string[]>([])

const queryForm = reactive<IamLoginRiskPageQuery>({
  pageNo: 1,
  pageSize: 10,
  riskCode: '',
  username: '',
  clientIp: '',
  riskType: '',
  riskLevel: '',
  handleStatus: '',
  beginTime: '',
  endTime: ''
})

const highRiskCount = computed(() =>
  records.value.filter((item) => item.riskLevel === 3).length
)

const middleRiskCount = computed(() =>
  records.value.filter((item) => item.riskLevel === 2).length
)

const pendingCount = computed(() =>
  records.value.filter((item) => item.handleStatus === 0).length
)

function buildQueryParams(): IamLoginRiskPageQuery {
  const [beginTime, endTime] = Array.isArray(timeRange.value) ? timeRange.value : []
  return {
    ...queryForm,
    beginTime: beginTime || '',
    endTime: endTime || ''
  }
}

async function fetchPage() {
  loading.value = true
  try {
    const page = await getIamLoginRiskPage(buildQueryParams())
    records.value = Array.isArray(page.records) ? page.records : []
    total.value = Number(page.total || 0)
  } catch (error) {
    records.value = []
    total.value = 0
    ElMessage.error('异常登录风险加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    loading.value = false
  }
}
function goDashboard() {
  router.push('/dashboard')
}
async function handleDetect() {
  detecting.value = true
  try {
    const list = await detectIamLoginRisks()
    ElMessage.success(`异常登录检测完成，新增 ${Array.isArray(list) ? list.length : 0} 条风险记录`)
    await fetchPage()
  } catch (error) {
    ElMessage.error('异常登录检测失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    detecting.value = false
  }
}

function handleSearch() {
  queryForm.pageNo = 1
  fetchPage()
}

function handleReset() {
  queryForm.pageNo = 1
  queryForm.pageSize = 10
  queryForm.riskCode = ''
  queryForm.username = ''
  queryForm.clientIp = ''
  queryForm.riskType = ''
  queryForm.riskLevel = ''
  queryForm.handleStatus = ''
  queryForm.beginTime = ''
  queryForm.endTime = ''
  timeRange.value = []
  fetchPage()
}

function riskLevelTagType(level: number) {
  if (level === 3) {
    return 'danger'
  }
  if (level === 2) {
    return 'warning'
  }
  return 'success'
}

onMounted(() => {
  fetchPage()
})
</script>

<style scoped>
.iam-login-risk-page {
  padding: 24px;
  background: #f5f7fb;
  min-height: 100vh;
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
  color: #6b7280;
  font-size: 13px;
}

.page-header h1 {
  margin: 0;
  font-size: 26px;
  color: #111827;
}

.page-desc {
  margin: 8px 0 0;
  color: #6b7280;
  line-height: 1.7;
}

.header-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.summary-card {
  border-radius: 12px;
}

.summary-label {
  display: block;
  color: #6b7280;
  font-size: 13px;
  margin-bottom: 8px;
}

.summary-card strong {
  font-size: 26px;
  color: #111827;
}

.search-card,
.table-card {
  border-radius: 12px;
  margin-bottom: 16px;
}

.search-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 12px;
  align-items: center;
}

.time-range {
  width: 100%;
}

.search-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .iam-login-risk-page {
    padding: 16px;
  }

  .page-header {
    grid-template-columns: 1fr;
  }

  .header-actions,
  .search-actions,
  .pagination-wrap {
    justify-content: flex-start;
  }
}
</style>
