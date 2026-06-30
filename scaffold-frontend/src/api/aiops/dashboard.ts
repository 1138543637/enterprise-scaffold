import request from '../request'

export interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

export interface AiopsDashboardSummaryVO {
  resourceTotal: number
  abnormalResourceTotal: number
  metricDataTotal: number
  alertMetricTotal: number
  alertRuleTotal: number
  alertEventTotal: number
  unhandledAlertTotal: number
  workOrderTotal: number
  pendingWorkOrderTotal: number
  rootCauseTotal: number
  highConfidenceRootCauseTotal: number
}

export interface AiopsResourceTypeStatVO {
  resourceType: string
  resourceTypeName: string
  total: number
}

export interface AiopsAlertLevelStatVO {
  alertLevel: number
  alertLevelName: string
  total: number
}

export interface AiopsWorkOrderStatusStatVO {
  orderStatus: number
  orderStatusName: string
  total: number
}

export interface AiopsMetricTrendVO {
  statDate: string
  metricTotal: number
  alertMetricTotal: number
  avgMetricValue: number
}

export interface AiopsRecentAlertVO {
  id: number
  eventCode: string
  resourceCode: string
  resourceName: string
  resourceType: string
  ipAddr: string
  metricType: string
  metricValue: number
  thresholdValue: number
  alertLevel: number
  alertLevelName: string
  handleStatus: number
  handleStatusName: string
  alertTime: string
}

export interface AiopsRecentWorkOrderVO {
  id: number
  workOrderCode: string
  eventCode: string
  resourceCode: string
  resourceName: string
  resourceType: string
  ipAddr: string
  metricType: string
  alertLevel: number
  alertLevelName: string
  orderStatus: number
  orderStatusName: string
  createTime: string
}

export interface AiopsRecentRootCauseVO {
  id: number
  analysisCode: string
  eventCode: string
  resourceCode: string
  resourceName: string
  resourceType: string
  ipAddr: string
  rootCauseType: string
  rootCauseTypeName: string
  confidenceScore: number
  analysisTime: string
}

function unwrapApiResult<T>(response: any): T {
  const payload = response?.data !== undefined ? response.data : response

  if (
    payload &&
    typeof payload === 'object' &&
    'code' in payload &&
    'msg' in payload &&
    'data' in payload
  ) {
    return payload.data as T
  }

  return payload as T
}

export async function getAiopsDashboardSummaryApi(): Promise<AiopsDashboardSummaryVO> {
  const response = await request.get<any, AiopsDashboardSummaryVO>(
    '/api/aiops/dashboard/summary'
  )
  return unwrapApiResult<AiopsDashboardSummaryVO>(response)
}

export async function getAiopsResourceTypeStatsApi(): Promise<AiopsResourceTypeStatVO[]> {
  const response = await request.get<any, AiopsResourceTypeStatVO[]>(
    '/api/aiops/dashboard/resource-type-stats'
  )
  return unwrapApiResult<AiopsResourceTypeStatVO[]>(response)
}

export async function getAiopsAlertLevelStatsApi(): Promise<AiopsAlertLevelStatVO[]> {
  const response = await request.get<any, AiopsAlertLevelStatVO[]>(
    '/api/aiops/dashboard/alert-level-stats'
  )
  return unwrapApiResult<AiopsAlertLevelStatVO[]>(response)
}

export async function getAiopsWorkOrderStatusStatsApi(): Promise<AiopsWorkOrderStatusStatVO[]> {
  const response = await request.get<any, AiopsWorkOrderStatusStatVO[]>(
    '/api/aiops/dashboard/work-order-status-stats'
  )
  return unwrapApiResult<AiopsWorkOrderStatusStatVO[]>(response)
}

export async function getAiopsMetricTrendApi(): Promise<AiopsMetricTrendVO[]> {
  const response = await request.get<any, AiopsMetricTrendVO[]>(
    '/api/aiops/dashboard/metric-trend'
  )
  return unwrapApiResult<AiopsMetricTrendVO[]>(response)
}

export async function getAiopsRecentAlertsApi(): Promise<AiopsRecentAlertVO[]> {
  const response = await request.get<any, AiopsRecentAlertVO[]>(
    '/api/aiops/dashboard/recent-alerts'
  )
  return unwrapApiResult<AiopsRecentAlertVO[]>(response)
}

export async function getAiopsRecentWorkOrdersApi(): Promise<AiopsRecentWorkOrderVO[]> {
  const response = await request.get<any, AiopsRecentWorkOrderVO[]>(
    '/api/aiops/dashboard/recent-work-orders'
  )
  return unwrapApiResult<AiopsRecentWorkOrderVO[]>(response)
}

export async function getAiopsRecentRootCausesApi(): Promise<AiopsRecentRootCauseVO[]> {
  const response = await request.get<any, AiopsRecentRootCauseVO[]>(
    '/api/aiops/dashboard/recent-root-causes'
  )
  return unwrapApiResult<AiopsRecentRootCauseVO[]>(response)
}
