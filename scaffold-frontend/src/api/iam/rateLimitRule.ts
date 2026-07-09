import request from '../request'

export interface PageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

export interface IamRateLimitRulePageQuery {
  pageNo?: number
  pageSize?: number
  ruleCode?: string
  ruleName?: string
  requestUri?: string
  requestMethod?: string
  limitDimension?: string
  enabled?: number
  beginTime?: string
  endTime?: string
}

export interface IamRateLimitRulePageVO {
  id: number
  ruleCode: string
  ruleName: string
  requestUri: string
  requestMethod: string
  limitDimension: string
  limitDimensionName: string
  limitWindowSeconds: number
  maxRequests: number
  limitAction: string
  limitActionName: string
  enabled: number
  enabledName: string
  status: number
  createTime: string
  remark: string
}

export interface IamRateLimitRuleSimulateRequest {
  requestUri?: string
  requestMethod?: string
  username?: string
  clientIp?: string
  currentRequests?: number
}

export interface IamRateLimitRuleSimulateVO {
  matched: boolean
  allowed: boolean
  hitRuleCount: number
  ruleId?: number
  ruleCode?: string
  ruleName?: string
  requestUri: string
  requestMethod: string
  limitDimension?: string
  limitDimensionName?: string
  limitWindowSeconds?: number
  maxRequests?: number
  currentRequests?: number
  remainRequests?: number
  message: string
}

function unwrapApiResult<T>(response: any): T {
  if (response && typeof response === 'object' && 'code' in response && 'data' in response) {
    return response.data as T
  }
  if (response && response.data && typeof response.data === 'object' && 'code' in response.data && 'data' in response.data) {
    return response.data.data as T
  }
  if (response && response.data && !('code' in response)) {
    return response.data as T
  }
  return response as T
}

function normalizePage<T>(response: any): PageResult<T> {
  const data = unwrapApiResult<PageResult<T>>(response)
  return {
    pageNo: Number(data?.pageNo || 1),
    pageSize: Number(data?.pageSize || 10),
    total: Number(data?.total || 0),
    pages: Number(data?.pages || 0),
    records: Array.isArray(data?.records) ? data.records : []
  }
}

export async function getIamRateLimitRulePage(
  params: IamRateLimitRulePageQuery
): Promise<PageResult<IamRateLimitRulePageVO>> {
  const response = await request.get<any, any>('/api/iam/rate-limit-rules/page', { params })
  return normalizePage<IamRateLimitRulePageVO>(response)
}

export async function enableIamRateLimitRule(id: number): Promise<boolean> {
  const response = await request.post<any, any>(`/api/iam/rate-limit-rules/${id}/enable`)
  return Boolean(unwrapApiResult<boolean>(response))
}

export async function disableIamRateLimitRule(id: number): Promise<boolean> {
  const response = await request.post<any, any>(`/api/iam/rate-limit-rules/${id}/disable`)
  return Boolean(unwrapApiResult<boolean>(response))
}

export async function simulateIamRateLimitRule(
  payload: IamRateLimitRuleSimulateRequest
): Promise<IamRateLimitRuleSimulateVO> {
  const response = await request.post<any, any>('/api/iam/rate-limit-rules/simulate', payload)
  return unwrapApiResult<IamRateLimitRuleSimulateVO>(response)
}
