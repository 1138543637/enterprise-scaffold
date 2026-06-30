import request from '../request'

export interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface AiopsMetricDataPageQuery {
  pageNo?: number
  pageSize?: number
  resourceId?: number
  resourceCode?: string
  resourceName?: string
  resourceType?: string
  ipAddr?: string
  metricCode?: string
  metricType?: string
  alarmFlag?: number
  status?: number
}

export interface AiopsMetricDataSimulateRequest {
  resourceId?: number
  resourceType?: string
  metricType?: string
  count?: number
  remark?: string
}

export interface AiopsMetricDataVO {
  id: number
  resourceId: number
  resourceCode: string
  resourceName: string
  resourceType: string
  ipAddr: string
  metricCode: string
  metricName: string
  metricType: string
  metricValue: number
  metricUnit: string
  thresholdValue: number
  alarmFlag: number
  collectTime: string
  status: number
  createTime: string
  remark: string
}

function isApiResult<T>(value: unknown): value is ApiResult<T> {
  return (
    typeof value === 'object' &&
    value !== null &&
    'code' in value &&
    'msg' in value &&
    'data' in value
  )
}

function unwrapApiResult<T>(response: unknown): T {
  if (isApiResult<T>(response)) {
    return response.data
  }

  const maybeAxiosData = (response as { data?: unknown })?.data

  if (isApiResult<T>(maybeAxiosData)) {
    return maybeAxiosData.data
  }

  if (maybeAxiosData !== undefined && (response as { status?: number })?.status !== undefined) {
    return maybeAxiosData as T
  }

  return response as T
}

export async function simulateAiopsMetricDataApi(
  data: AiopsMetricDataSimulateRequest
): Promise<AiopsMetricDataVO[]> {
  const response = await request.post<any, AiopsMetricDataVO[]>(
    '/api/aiops/metric-data/simulate',
    data
  )
  return unwrapApiResult<AiopsMetricDataVO[]>(response)
}

export async function getAiopsMetricDataLatestApi(
  params: AiopsMetricDataPageQuery
): Promise<AiopsMetricDataVO[]> {
  const response = await request.get<any, AiopsMetricDataVO[]>(
    '/api/aiops/metric-data/latest',
    {
      params
    }
  )
  return unwrapApiResult<AiopsMetricDataVO[]>(response)
}

export async function getAiopsMetricDataPageApi(
  params: AiopsMetricDataPageQuery
): Promise<PageResult<AiopsMetricDataVO>> {
  const response = await request.get<any, PageResult<AiopsMetricDataVO>>(
    '/api/aiops/metric-data/page',
    {
      params
    }
  )
  return unwrapApiResult<PageResult<AiopsMetricDataVO>>(response)
}
