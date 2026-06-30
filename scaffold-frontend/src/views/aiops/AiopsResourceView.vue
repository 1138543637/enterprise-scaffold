<template>
  <div class="aiops-resource-page">
    <div class="page-header">
      <div>
        <h2>AIOps 资源管理</h2>
        <p>管理云主机、数据库、中间件、网络设备等运维资源台账。</p>
      </div>
      <el-button @click="goDashboard">返回首页</el-button>
    </div>

    <el-card class="query-card" shadow="never">
      <div class="query-grid">
        <el-input
          v-model="query.resourceCode"
          clearable
          placeholder="资源编码"
          @keyup.enter="handleSearch"
        />
        <el-input
          v-model="query.resourceName"
          clearable
          placeholder="资源名称"
          @keyup.enter="handleSearch"
        />
        <el-select v-model="query.resourceType" clearable placeholder="资源类型">
          <el-option label="服务器" value="SERVER" />
          <el-option label="数据库" value="DATABASE" />
          <el-option label="中间件" value="MIDDLEWARE" />
          <el-option label="网络设备" value="NETWORK" />
        </el-select>
        <el-input
          v-model="query.ipAddr"
          clearable
          placeholder="IP 地址"
          @keyup.enter="handleSearch"
        />
        <el-select v-model="query.envType" clearable placeholder="环境">
          <el-option label="开发" value="DEV" />
          <el-option label="测试" value="TEST" />
          <el-option label="生产" value="PROD" />
        </el-select>
        <el-input
          v-model="query.systemName"
          clearable
          placeholder="所属系统"
          @keyup.enter="handleSearch"
        />
        <el-input
          v-model="query.ownerName"
          clearable
          placeholder="负责人"
          @keyup.enter="handleSearch"
        />
        <el-select v-model="query.collectEnabled" clearable placeholder="采集状态">
          <el-option label="启用采集" :value="0" />
          <el-option label="停止采集" :value="1" />
        </el-select>
        <el-select v-model="query.status" clearable placeholder="资源状态">
          <el-option label="正常" :value="0" />
          <el-option label="停用" :value="1" />
          <el-option label="异常" :value="2" />
        </el-select>
        <div class="query-actions">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
    </el-card>

    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="table-header">
          <span>资源列表</span>
          <el-button type="primary" plain @click="loadData">刷新</el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="records" border class="resource-table">
        <el-table-column prop="resourceCode" label="资源编码" min-width="160" fixed="left" />
        <el-table-column prop="resourceName" label="资源名称" min-width="180" />
        <el-table-column label="资源类型" min-width="120">
          <template #default="{ row }">
            <el-tag>{{ formatResourceType(row.resourceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddr" label="IP 地址" min-width="140" />
        <el-table-column prop="port" label="端口" min-width="90" />
        <el-table-column label="环境" min-width="100">
          <template #default="{ row }">
            <el-tag :type="envTagType(row.envType)">
              {{ formatEnvType(row.envType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="systemName" label="所属系统" min-width="180" />
        <el-table-column prop="ownerName" label="负责人" min-width="130" />
        <el-table-column label="采集状态" min-width="110">
          <template #default="{ row }">
            <el-tag :type="row.collectEnabled === 0 ? 'success' : 'info'">
              {{ row.collectEnabled === 0 ? '启用采集' : '停止采集' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="资源状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">
              {{ formatStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastCollectTime" label="最后采集时间" min-width="180" />
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column prop="remark" label="备注" min-width="220" />
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="query.pageNo"
          v-model:page-size="query.pageSize"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
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
  getAiopsResourcePageApi,
  type AiopsResourcePageQuery,
  type AiopsResourcePageVO
} from '../../api/aiops/resource'

const router = useRouter()

const loading = ref(false)
const total = ref(0)
const records = ref<AiopsResourcePageVO[]>([])

const query = reactive<AiopsResourcePageQuery>({
  pageNo: 1,
  pageSize: 10,
  resourceCode: '',
  resourceName: '',
  resourceType: '',
  ipAddr: '',
  envType: '',
  systemName: '',
  ownerName: '',
  collectEnabled: undefined,
  status: undefined
})

async function loadData() {
  loading.value = true
  try {
    const pageData = await getAiopsResourcePageApi(query)
    records.value = pageData.records || []
    total.value = pageData.total || 0
  } catch (error) {
    console.error(error)
    ElMessage.error('AIOps 资源列表加载失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.pageNo = 1
  loadData()
}

function handleReset() {
  query.pageNo = 1
  query.pageSize = 10
  query.resourceCode = ''
  query.resourceName = ''
  query.resourceType = ''
  query.ipAddr = ''
  query.envType = ''
  query.systemName = ''
  query.ownerName = ''
  query.collectEnabled = undefined
  query.status = undefined
  loadData()
}

function handleSizeChange(size: number) {
  query.pageSize = size
  query.pageNo = 1
  loadData()
}

function handleCurrentChange(page: number) {
  query.pageNo = page
  loadData()
}

function goDashboard() {
  router.push('/dashboard')
}

function formatResourceType(value: string) {
  const map: Record<string, string> = {
    SERVER: '服务器',
    DATABASE: '数据库',
    MIDDLEWARE: '中间件',
    NETWORK: '网络设备'
  }
  return map[value] || value || '-'
}

function formatEnvType(value: string) {
  const map: Record<string, string> = {
    DEV: '开发',
    TEST: '测试',
    PROD: '生产'
  }
  return map[value] || value || '-'
}

function envTagType(value: string) {
  if (value === 'PROD') return 'danger'
  if (value === 'TEST') return 'warning'
  if (value === 'DEV') return 'info'
  return ''
}

function formatStatus(value: number) {
  const map: Record<number, string> = {
    0: '正常',
    1: '停用',
    2: '异常'
  }
  return map[value] || '-'
}

function statusTagType(value: number) {
  if (value === 0) return 'success'
  if (value === 1) return 'info'
  if (value === 2) return 'danger'
  return ''
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.aiops-resource-page {
  min-height: 100vh;
  padding: 24px;
  background: #f5f7fa;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 16px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #1f2937;
}

.page-header p {
  margin: 8px 0 0;
  color: #6b7280;
}

.query-card,
.table-card {
  margin-bottom: 16px;
}

.query-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 14px;
  align-items: center;
}

.query-actions {
  display: flex;
  gap: 10px;
}

.table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.resource-table {
  width: 100%;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .aiops-resource-page {
    padding: 16px;
  }

  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .pagination-wrapper {
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>
