<template>
  <div class="api-publish-page">
    <div class="page-header">
      <div>
        <h2>API 共享发布</h2>
        <p>把已采集的元数据表发布为可管理的共享 API，并支持上线、下线和分页查询。</p>
      </div>
      <el-button type="primary" @click="openPublishDialog">从元数据表发布 API</el-button>
    </div>

    <el-card shadow="never" class="query-card">
      <el-form :model="query" label-width="90px" class="query-form">
        <el-form-item label="API编码">
          <el-input v-model="query.apiCode" clearable placeholder="请输入 API 编码" />
        </el-form-item>
        <el-form-item label="API名称">
          <el-input v-model="query.apiName" clearable placeholder="请输入 API 名称" />
        </el-form-item>
        <el-form-item label="数据源">
          <el-input v-model="query.datasourceName" clearable placeholder="请输入数据源名称" />
        </el-form-item>
        <el-form-item label="表名">
          <el-input v-model="query.tableName" clearable placeholder="请输入表名" />
        </el-form-item>
        <el-form-item label="上线状态">
          <el-select v-model="query.onlineStatus" clearable placeholder="请选择">
            <el-option label="下线" :value="0" />
            <el-option label="上线" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item class="query-actions">
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="apiCode" label="API编码" min-width="170" />
        <el-table-column prop="apiName" label="API名称" min-width="180" />
        <el-table-column prop="datasourceName" label="数据源" min-width="140" />
        <el-table-column prop="tableName" label="表名" min-width="140" />
        <el-table-column prop="apiPath" label="API路径" min-width="230" />
        <el-table-column prop="requestMethod" label="请求方式" width="100" />
        <el-table-column label="上线状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.onlineStatus === 1 ? 'success' : 'info'">
              {{ scope.row.onlineStatus === 1 ? '上线' : '下线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ownerName" label="负责人" width="110" />
        <el-table-column prop="publishTime" label="发布时间" min-width="170" />
        <el-table-column label="操作" fixed="right" width="180">
          <template #default="scope">
            <el-button v-if="scope.row.onlineStatus !== 1" link type="success" @click="handleOnline(scope.row.id)">
              上线
            </el-button>
            <el-button v-else link type="warning" @click="handleOffline(scope.row.id)">
              下线
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-wrap">
        <el-pagination
          v-model:current-page="query.pageNo"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="从元数据表发布 API" width="640px">
      <el-alert
        title="先在 /datahub/metadata 页面确认已有元数据表，再复制对应 tableId 到这里发布。"
        type="info"
        show-icon
        :closable="false"
        class="dialog-alert"
      />
      <el-form :model="form" label-width="110px">
        <el-form-item label="元数据表ID" required>
          <el-input-number v-model="form.tableId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="API名称">
          <el-input v-model="form.apiName" clearable placeholder="不填则自动生成：表名分页查询API" />
        </el-form-item>
        <el-form-item label="API路径">
          <el-input v-model="form.apiPath" clearable placeholder="不填则自动生成 /openapi/datahub/{table}/page" />
        </el-form-item>
        <el-form-item label="请求方式">
          <el-select v-model="form.requestMethod" placeholder="请选择请求方式">
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.ownerName" clearable placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitPublish">确认发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import {
  offlineApiPublish,
  onlineApiPublish,
  pageApiPublishes,
  publishApiFromTable,
  type DatahubApiPublishCreateRequest,
  type DatahubApiPublishPageQuery,
  type DatahubApiPublishPageVO
} from '../../api/datahub/apiPublish'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const records = ref<DatahubApiPublishPageVO[]>([])
const total = ref(0)

const query = reactive<DatahubApiPublishPageQuery>({
  pageNo: 1,
  pageSize: 10,
  apiCode: '',
  apiName: '',
  datasourceName: '',
  tableName: '',
  onlineStatus: undefined,
  status: undefined
})

const form = reactive<DatahubApiPublishCreateRequest>({
  tableId: 1,
  apiName: '',
  apiPath: '',
  requestMethod: 'GET',
  ownerName: '',
  remark: ''
})

async function loadData() {
  loading.value = true
  try {
    const page = await pageApiPublishes(query)
    records.value = Array.isArray(page.records) ? page.records : []
    total.value = Number(page.total || 0)
  } catch (error) {
    console.error(error)
    ElMessage.error('加载 API 发布列表失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.pageNo = 1
  query.pageSize = 10
  query.apiCode = ''
  query.apiName = ''
  query.datasourceName = ''
  query.tableName = ''
  query.onlineStatus = undefined
  query.status = undefined
  loadData()
}

function openPublishDialog() {
  dialogVisible.value = true
}

async function submitPublish() {
  if (!form.tableId) {
    ElMessage.warning('请填写元数据表ID')
    return
  }
  submitLoading.value = true
  try {
    const result = await publishApiFromTable(form)
    ElMessage.success(`发布成功：${result.apiCode}`)
    dialogVisible.value = false
    await loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('发布失败，请确认 tableId 是否存在，并查看后端日志')
  } finally {
    submitLoading.value = false
  }
}

async function handleOnline(id: number) {
  try {
    await ElMessageBox.confirm('确认上线这个 API 吗？', '提示', { type: 'warning' })
  } catch {
    return
  }
  try {
    await onlineApiPublish(id)
    ElMessage.success('上线成功')
    await loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('上线失败，请查看后端日志')
  }
}

async function handleOffline(id: number) {
  try {
    await ElMessageBox.confirm('确认下线这个 API 吗？', '提示', { type: 'warning' })
  } catch {
    return
  }
  try {
    await offlineApiPublish(id)
    ElMessage.success('下线成功')
    await loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('下线失败，请查看后端日志')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.api-publish-page {
  padding: 20px;
}

.page-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
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

.query-card {
  margin-bottom: 16px;
}

.query-form {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 0 16px;
}

.query-actions {
  display: flex;
  align-items: flex-end;
}

.pager-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.dialog-alert {
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .page-header {
    grid-template-columns: 1fr;
  }
}
</style>
