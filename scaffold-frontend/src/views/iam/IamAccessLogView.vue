<template>
  <div class="iam-access-log-page">
    <div class="page-header">
      <div>
        <p class="page-subtitle">Enterprise Scaffold / IAM</p>
        <h1>接口访问日志</h1>
        <p class="page-desc">
          本页面用于查询 IAM 模块接口访问日志，展示接口路径、请求方法、访问用户、访问 IP、访问状态、耗时和访问时间。
        </p>
      </div>

      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button type="primary" :loading="loading" @click="loadPage">刷新</el-button>
      </div>
    </div>

    <el-card shadow="never" class="query-card">
      <div class="query-grid">
        <el-input
          v-model="queryForm.requestUri"
          clearable
          placeholder="接口路径，例如 /api/iam/health"
        />
        <el-select v-model="queryForm.requestMethod" clearable placeholder="请求方法">
          <el-option label="GET" value="GET" />
          <el-option label="POST" value="POST" />
          <el-option label="PUT" value="PUT" />
          <el-option label="DELETE" value="DELETE" />
        </el-select>
        <el-input v-model="queryForm.username" clearable placeholder="访问用户名" />
        <el-input v-model="queryForm.clientIp" clearable placeholder="访问 IP" />
        <el-select v-model="queryForm.accessStatus" clearable placeholder="访问状态">
          <el-option label="成功" :value="0" />
          <el-option label="失败" :value="1" />
        </el-select>
        <el-date-picker
          v-model="timeRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DDTHH:mm:ss"
          class="time-range"
        />
      </div>

      <div class="query-actions">
        <el-button type="primary" :loading="loading" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="records"
        border
        stripe
        empty-text="暂无接口访问日志"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="requestUri" label="接口路径" min-width="220" show-overflow-tooltip />
        <el-table-column prop="requestMethod" label="方法" width="90" />
        <el-table-column prop="username" label="用户" width="120" show-overflow-tooltip />
        <el-table-column prop="clientIp" label="访问 IP" width="140" show-overflow-tooltip />
        <el-table-column prop="accessStatusName" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.accessStatus === 0 ? 'success' : 'danger'">
              {{ row.accessStatusName || statusText(row.accessStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="responseCode" label="响应码" width="100" />
        <el-table-column prop="costMs" label="耗时(ms)" width="110" />
        <el-table-column prop="accessTime" label="访问时间" width="180" />
        <el-table-column prop="operationName" label="操作名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="queryForm.pageNo"
          v-model:page-size="queryForm.pageSize"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="loadPage"
          @current-change="loadPage"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  getIamAccessLogPage,
  type IamAccessLogPageQuery,
  type IamAccessLogPageVO
} from '../../api/iam/accessLog'

const router = useRouter()

const loading = ref(false)
const records = ref<IamAccessLogPageVO[]>([])
const total = ref(0)
const timeRange = ref<string[]>([])

const queryForm = reactive<IamAccessLogPageQuery>({
  pageNo: 1,
  pageSize: 10,
  requestUri: '',
  requestMethod: '',
  username: '',
  clientIp: '',
  accessStatus: '',
  beginTime: '',
  endTime: ''
})

function applyTimeRange() {
  if (Array.isArray(timeRange.value) && timeRange.value.length === 2) {
    queryForm.beginTime = timeRange.value[0]
    queryForm.endTime = timeRange.value[1]
  } else {
    queryForm.beginTime = ''
    queryForm.endTime = ''
  }
}

async function loadPage() {
  loading.value = true
  try {
    applyTimeRange()
    const page = await getIamAccessLogPage(queryForm)
    records.value = Array.isArray(page.records) ? page.records : []
    total.value = Number(page.total || 0)
  } catch (error) {
    console.error(error)
    ElMessage.error('接口访问日志加载失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
    records.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryForm.pageNo = 1
  loadPage()
}

function handleReset() {
  queryForm.pageNo = 1
  queryForm.pageSize = 10
  queryForm.requestUri = ''
  queryForm.requestMethod = ''
  queryForm.username = ''
  queryForm.clientIp = ''
  queryForm.accessStatus = ''
  queryForm.beginTime = ''
  queryForm.endTime = ''
  timeRange.value = []
  loadPage()
}

function statusText(accessStatus: number) {
  if (accessStatus === 0) {
    return '成功'
  }
  if (accessStatus === 1) {
    return '失败'
  }
  return '未知'
}

function goDashboard() {
  router.push('/dashboard')
}

onMounted(() => {
  loadPage()
})
</script>

<style scoped>
.iam-access-log-page {
  padding: 24px;
}

.page-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 16px;
  align-items: center;
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
}

.page-desc {
  margin: 8px 0 0;
  color: #64748b;
  line-height: 1.7;
}

.header-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.query-card {
  margin-bottom: 18px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 14px;
  align-items: center;
}

.time-range {
  width: 100%;
}

.query-actions {
  display: flex;
  gap: 10px;
  margin-top: 14px;
  flex-wrap: wrap;
}

.table-card {
  margin-bottom: 18px;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .iam-access-log-page {
    padding: 16px;
  }

  .page-header {
    grid-template-columns: 1fr;
  }

  .header-actions {
    justify-content: flex-start;
  }

  .pagination-wrap {
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>
