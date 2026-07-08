<template>
  <div class="page-container">
    <div class="page-header">
      <div>
        <h2>数据源管理</h2>
        <p>维护国企 / 政务数据治理平台中的 MySQL、Oracle、PostgreSQL 等数据源，为后续元数据采集和数据质量检测做准备。</p>
      </div>
      <div class="header-actions">
        <el-button @click="goDashboard">返回首页</el-button>
        <el-button type="primary" @click="loadData">刷新</el-button>
        <el-button type="success" @click="openTestDialog">连接测试</el-button>
      </div>
    </div>

    <el-card class="query-card" shadow="never">
      <div class="query-grid">
        <el-input v-model="query.datasourceCode" placeholder="数据源编码" clearable />
        <el-input v-model="query.datasourceName" placeholder="数据源名称" clearable />
        <el-select v-model="query.datasourceType" placeholder="数据源类型" clearable>
          <el-option label="MySQL" value="MYSQL" />
          <el-option label="PostgreSQL" value="POSTGRESQL" />
          <el-option label="Oracle" value="ORACLE" />
          <el-option label="SQL Server" value="SQLSERVER" />
          <el-option label="API" value="API" />
        </el-select>
        <el-input v-model="query.host" placeholder="主机地址" clearable />
        <el-input v-model="query.databaseName" placeholder="数据库名" clearable />
        <el-select v-model="query.envType" placeholder="环境类型" clearable>
          <el-option label="开发环境" value="DEV" />
          <el-option label="测试环境" value="TEST" />
          <el-option label="生产环境" value="PROD" />
        </el-select>
        <el-input v-model="query.ownerName" placeholder="负责人" clearable />
        <el-select v-model="query.testStatus" placeholder="测试状态" clearable>
          <el-option label="未测试" :value="0" />
          <el-option label="成功" :value="1" />
          <el-option label="失败" :value="2" />
        </el-select>
        <el-select v-model="query.status" placeholder="状态" clearable>
          <el-option label="正常" :value="0" />
          <el-option label="停用" :value="1" />
        </el-select>
        <div class="query-actions">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never">
      <el-table v-loading="loading" :data="records" border style="width: 100%">
        <el-table-column prop="datasourceCode" label="数据源编码" min-width="170" />
        <el-table-column prop="datasourceName" label="数据源名称" min-width="190" />
        <el-table-column prop="datasourceType" label="类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ formatDatasourceType(row.datasourceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="host" label="主机" min-width="140" />
        <el-table-column prop="port" label="端口" width="90" />
        <el-table-column prop="databaseName" label="数据库名" min-width="140" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="ownerName" label="负责人" min-width="120" />
        <el-table-column prop="envType" label="环境" width="110">
          <template #default="{ row }">
            <el-tag :type="envTagType(row.envType)">{{ formatEnvType(row.envType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="testStatus" label="测试状态" width="120">
          <template #default="{ row }">
            <el-tag :type="testStatusTagType(row.testStatus)">
              {{ formatTestStatus(row.testStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'info'">
              {{ row.status === 0 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastTestTime" label="最近测试时间" min-width="180" />
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column prop="remark" label="备注" min-width="220" show-overflow-tooltip />
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="query.pageNo"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <el-dialog v-model="testDialogVisible" title="数据源连接测试" width="720px">
      <el-form label-width="120px">
        <el-form-item label="数据源类型">
          <el-select v-model="testForm.datasourceType" placeholder="请选择数据源类型">
            <el-option label="MySQL" value="MYSQL" />
            <el-option label="PostgreSQL" value="POSTGRESQL" />
            <el-option label="Oracle" value="ORACLE" />
            <el-option label="SQL Server" value="SQLSERVER" />
          </el-select>
        </el-form-item>
        <el-form-item label="JDBC 地址">
          <el-input
            v-model="testForm.jdbcUrl"
            type="textarea"
            :rows="3"
            placeholder="例如：jdbc:mysql://localhost:3306/enterprise_scaffold?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true"
          />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="testForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="testForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
      </el-form>

      <el-alert
        v-if="testResult"
        :title="testResult.testMessage"
        :type="testResult.testStatus === 1 ? 'success' : 'error'"
        show-icon
        :closable="false"
      >
        <template #default>
          <div class="test-result">
            <div>耗时：{{ testResult.costTime }} ms</div>
            <div v-if="testResult.databaseProductName">数据库：{{ testResult.databaseProductName }}</div>
            <div v-if="testResult.databaseProductVersion">版本：{{ testResult.databaseProductVersion }}</div>
            <div>测试时间：{{ testResult.testTime }}</div>
          </div>
        </template>
      </el-alert>

      <template #footer>
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="testing" @click="handleTestConnection">开始测试</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  getDatahubDatasourcePageApi,
  testDatahubDatasourceConnectionApi,
  type DatahubDatasourcePageQuery,
  type DatahubDatasourcePageVO,
  type DatahubDatasourceTestRequest,
  type DatahubDatasourceTestVO
} from '../../api/datahub/datasource'

const router = useRouter()

const loading = ref(false)
const testing = ref(false)
const records = ref<DatahubDatasourcePageVO[]>([])
const total = ref(0)
const testDialogVisible = ref(false)
const testResult = ref<DatahubDatasourceTestVO | null>(null)

const query = reactive<DatahubDatasourcePageQuery>({
  pageNo: 1,
  pageSize: 10,
  datasourceCode: '',
  datasourceName: '',
  datasourceType: '',
  host: '',
  databaseName: '',
  envType: '',
  ownerName: '',
  testStatus: undefined,
  status: undefined
})

const testForm = reactive<DatahubDatasourceTestRequest>({
  datasourceType: 'MYSQL',
  jdbcUrl: 'jdbc:mysql://localhost:3306/enterprise_scaffold?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true',
  username: 'root',
  password: ''
})

function goDashboard() {
  router.push('/dashboard')
}

async function loadData() {
  loading.value = true
  try {
    const page = await getDatahubDatasourcePageApi(query)
    records.value = Array.isArray(page.records) ? page.records : []
    total.value = page.total || 0
  } catch (error) {
    console.error(error)
    ElMessage.error('数据源列表加载失败')
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
  query.datasourceCode = ''
  query.datasourceName = ''
  query.datasourceType = ''
  query.host = ''
  query.databaseName = ''
  query.envType = ''
  query.ownerName = ''
  query.testStatus = undefined
  query.status = undefined
  loadData()
}

function openTestDialog() {
  testResult.value = null
  testDialogVisible.value = true
}

async function handleTestConnection() {
  if (!testForm.datasourceType || !testForm.jdbcUrl) {
    ElMessage.warning('请先填写数据源类型和 JDBC 地址')
    return
  }

  testing.value = true
  try {
    testResult.value = await testDatahubDatasourceConnectionApi(testForm)
    if (testResult.value.testStatus === 1) {
      ElMessage.success('连接测试成功')
    } else {
      ElMessage.error('连接测试失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('连接测试请求失败')
  } finally {
    testing.value = false
  }
}

function formatDatasourceType(type: string) {
  const map: Record<string, string> = {
    MYSQL: 'MySQL',
    POSTGRESQL: 'PostgreSQL',
    ORACLE: 'Oracle',
    SQLSERVER: 'SQL Server',
    API: 'API'
  }
  return map[type] || type || '-'
}

function formatEnvType(type: string) {
  const map: Record<string, string> = {
    DEV: '开发',
    TEST: '测试',
    PROD: '生产'
  }
  return map[type] || type || '-'
}

function envTagType(type: string) {
  if (type === 'PROD') return 'danger'
  if (type === 'TEST') return 'warning'
  return 'info'
}

function formatTestStatus(status: number) {
  const map: Record<number, string> = {
    0: '未测试',
    1: '成功',
    2: '失败'
  }
  return map[status] || '未知'
}

function testStatusTagType(status: number) {
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  return 'info'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 16px;
}

.page-header h2 {
  margin: 0 0 8px;
  font-size: 24px;
  color: #1f2937;
}

.page-header p {
  margin: 0;
  color: #6b7280;
}

.header-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.query-card {
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

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.test-result {
  line-height: 1.8;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
  }

  .header-actions {
    width: 100%;
  }
}
</style>
