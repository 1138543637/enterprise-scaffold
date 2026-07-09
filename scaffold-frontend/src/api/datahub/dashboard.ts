import request from '../request'

export interface DatahubDashboardSummaryVO {
  datasourceCount: number
  metadataTableCount: number
  metadataColumnCount: number
  qualityRuleCount: number
  qualityResultCount: number
  sensitiveFieldCount: number
  maskRuleCount: number
  maskResultCount: number
  apiPublishCount: number
  onlineApiCount: number
}

export interface DatahubDatasourceTypeStatVO {
  datasourceType: string
  typeName: string
  count: number
}

export interface DatahubQualityResultStatVO {
  checkStatus: number
  statusName: string
  count: number
}

export interface DatahubSensitiveTypeStatVO {
  sensitiveType: string
  typeName: string
  count: number
}

export interface DatahubRecentQualityResultVO {
  id: number
  resultCode: string
  ruleCode?: string
  ruleName?: string
  datasourceCode?: string
  tableCode?: string
  columnName?: string
  checkTotal?: number
  errorTotal?: number
  errorRate?: number
  checkStatus?: number
  checkTime?: string
  status?: number
  createTime?: string
  remark?: string
}

export interface DatahubRecentApiPublishVO {
  id: number
  apiCode: string
  apiName: string
  datasourceName?: string
  tableName?: string
  apiPath: string
  requestMethod: string
  onlineStatus: number
  publishTime?: string
  createTime?: string
}

function unwrapData<T>(response: any): T {
  if (response && typeof response === 'object' && 'code' in response && 'data' in response) {
    return response.data as T
  }
  if (response && typeof response === 'object' && response.data && typeof response.data === 'object' && 'code' in response.data) {
    return response.data.data as T
  }
  return response as T
}

export function getDatahubDashboardSummary(): Promise<DatahubDashboardSummaryVO> {
  return request
    .get<any, any>('/api/datahub/dashboard/summary')
    .then((res) => unwrapData<DatahubDashboardSummaryVO>(res))
}

export function getDatasourceTypeStats(): Promise<DatahubDatasourceTypeStatVO[]> {
  return request
    .get<any, any>('/api/datahub/dashboard/datasource-type-stats')
    .then((res) => unwrapData<DatahubDatasourceTypeStatVO[]>(res))
}

export function getQualityResultStats(): Promise<DatahubQualityResultStatVO[]> {
  return request
    .get<any, any>('/api/datahub/dashboard/quality-result-stats')
    .then((res) => unwrapData<DatahubQualityResultStatVO[]>(res))
}

export function getSensitiveTypeStats(): Promise<DatahubSensitiveTypeStatVO[]> {
  return request
    .get<any, any>('/api/datahub/dashboard/sensitive-type-stats')
    .then((res) => unwrapData<DatahubSensitiveTypeStatVO[]>(res))
}

export function getRecentQualityResults(): Promise<DatahubRecentQualityResultVO[]> {
  return request
    .get<any, any>('/api/datahub/dashboard/recent-quality-results')
    .then((res) => unwrapData<DatahubRecentQualityResultVO[]>(res))
}

export function getRecentApis(): Promise<DatahubRecentApiPublishVO[]> {
  return request
    .get<any, any>('/api/datahub/dashboard/recent-apis')
    .then((res) => unwrapData<DatahubRecentApiPublishVO[]>(res))
}
