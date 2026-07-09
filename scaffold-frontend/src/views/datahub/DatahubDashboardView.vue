<template>
  <div class="datahub-dashboard-page">
    <div class="page-header">
      <div>
        <h2>数据治理看板</h2>
        <p>汇总数据源、元数据、质量检测、敏感字段、脱敏和 API 发布情况。</p>
      </div>
      <el-button @click="goDashboard">返回首页</el-button>
      <el-button type="primary" @click="loadDashboard">刷新看板</el-button>
    </div>

    <div class="stat-grid">
      <el-card v-for="item in statCards" :key="item.label" shadow="never" class="stat-card">
        <div class="stat-value">{{ item.value }}</div>
        <div class="stat-label">{{ item.label }}</div>
      </el-card>
    </div>

    <div class="content-grid">
      <el-card shadow="never">
        <template #header>数据源类型分布</template>
        <el-table :data="datasourceTypeStats" border stripe height="260">
          <el-table-column prop="typeName" label="类型" />
          <el-table-column prop="count" label="数量" width="100" />
        </el-table>
      </el-card>

      <el-card shadow="never">
        <template #header>质量检测结果</template>
        <el-table :data="qualityResultStats" border stripe height="260">
          <el-table-column prop="statusName" label="结果" />
          <el-table-column prop="count" label="数量" width="100" />
        </el-table>
      </el-card>

      <el-card shadow="never">
        <template #header>敏感类型分布</template>
        <el-table :data="sensitiveTypeStats" border stripe height="260">
          <el-table-column prop="typeName" label="敏感类型" />
          <el-table-column prop="count" label="数量" width="100" />
        </el-table>
      </el-card>
    </div>

    <div class="table-grid">
      <el-card shadow="never">
        <template #header>最近质量检测结果</template>
        <el-table v-loading="loading" :data="recentQualityResults" border stripe>
          <el-table-column prop="resultCode" label="结果编码" min-width="170" />
          <el-table-column prop="ruleName" label="规则名称" min-width="160" />
          <el-table-column prop="tableCode" label="表编码" min-width="130" />
          <el-table-column prop="columnName" label="字段" min-width="110" />
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.checkStatus === 0 ? 'success' : 'danger'">
                {{ scope.row.checkStatus === 0 ? '通过' : '不通过' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="checkTime" label="检测时间" min-width="170" />
        </el-table>
      </el-card>

      <el-card shadow="never">
        <template #header>最近 API 发布</template>
        <el-table v-loading="loading" :data="recentApis" border stripe>
          <el-table-column prop="apiCode" label="API编码" min-width="170" />
          <el-table-column prop="apiName" label="API名称" min-width="160" />
          <el-table-column prop="tableName" label="表名" min-width="120" />
          <el-table-column prop="apiPath" label="路径" min-width="220" />
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.onlineStatus === 1 ? 'success' : 'info'">
                {{ scope.row.onlineStatus === 1 ? '上线' : '下线' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, ref } from 'vue'
import {
  getDatahubDashboardSummary,
  getDatasourceTypeStats,
  getQualityResultStats,
  getRecentApis,
  getRecentQualityResults,
  getSensitiveTypeStats,
  type DatahubDashboardSummaryVO,
  type DatahubDatasourceTypeStatVO,
  type DatahubQualityResultStatVO,
  type DatahubRecentApiPublishVO,
  type DatahubRecentQualityResultVO,
  type DatahubSensitiveTypeStatVO
} from '../../api/datahub/dashboard'
import {useRouter} from "vue-router";
const router = useRouter()
const loading = ref(false)
const summary = ref<DatahubDashboardSummaryVO>({
  datasourceCount: 0,
  metadataTableCount: 0,
  metadataColumnCount: 0,
  qualityRuleCount: 0,
  qualityResultCount: 0,
  sensitiveFieldCount: 0,
  maskRuleCount: 0,
  maskResultCount: 0,
  apiPublishCount: 0,
  onlineApiCount: 0
})
const datasourceTypeStats = ref<DatahubDatasourceTypeStatVO[]>([])
const qualityResultStats = ref<DatahubQualityResultStatVO[]>([])
const sensitiveTypeStats = ref<DatahubSensitiveTypeStatVO[]>([])
const recentQualityResults = ref<DatahubRecentQualityResultVO[]>([])
const recentApis = ref<DatahubRecentApiPublishVO[]>([])

const statCards = computed(() => [
  { label: '数据源数量', value: summary.value.datasourceCount || 0 },
  { label: '元数据表数量', value: summary.value.metadataTableCount || 0 },
  { label: '元数据字段数量', value: summary.value.metadataColumnCount || 0 },
  { label: '质量规则数量', value: summary.value.qualityRuleCount || 0 },
  { label: '质量结果数量', value: summary.value.qualityResultCount || 0 },
  { label: '敏感字段数量', value: summary.value.sensitiveFieldCount || 0 },
  { label: '脱敏规则数量', value: summary.value.maskRuleCount || 0 },
  { label: '脱敏结果数量', value: summary.value.maskResultCount || 0 },
  { label: 'API发布数量', value: summary.value.apiPublishCount || 0 },
  { label: '已上线 API', value: summary.value.onlineApiCount || 0 }
])
function goDashboard() {
  router.push('/dashboard')
}
async function loadDashboard() {
  loading.value = true
  try {
    const [summaryRes, datasourceStatsRes, qualityStatsRes, sensitiveStatsRes, qualityRecentRes, apiRecentRes] = await Promise.all([
      getDatahubDashboardSummary(),
      getDatasourceTypeStats(),
      getQualityResultStats(),
      getSensitiveTypeStats(),
      getRecentQualityResults(),
      getRecentApis()
    ])
    summary.value = summaryRes || summary.value
    datasourceTypeStats.value = Array.isArray(datasourceStatsRes) ? datasourceStatsRes : []
    qualityResultStats.value = Array.isArray(qualityStatsRes) ? qualityStatsRes : []
    sensitiveTypeStats.value = Array.isArray(sensitiveStatsRes) ? sensitiveStatsRes : []
    recentQualityResults.value = Array.isArray(qualityRecentRes) ? qualityRecentRes : []
    recentApis.value = Array.isArray(apiRecentRes) ? apiRecentRes : []
  } catch (error) {
    console.error(error)
    ElMessage.error('加载数据治理看板失败，请先查 F12 Network，再查 ApiResult 解包，再查 Docker 后端日志')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDashboard()
})
</script>

<style scoped>
.datahub-dashboard-page {
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

.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card {
  min-height: 96px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  line-height: 1.4;
}

.stat-label {
  color: #666;
  margin-top: 6px;
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.table-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
}

@media (max-width: 768px) {
  .page-header {
    grid-template-columns: 1fr;
  }
}
</style>
