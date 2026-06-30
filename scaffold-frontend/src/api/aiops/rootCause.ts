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

export interface AiopsRootCauseAnalyzeRequest {
  alertEventId?: number
  lookbackMinutes?: number
  remark?: string
}

export interface AiopsRootCausePageQuery {
  pageNo?: number
  pageSize?: number
  analysisCode?: string
  eventCode?: string
  resourceCode?: string
  resourceName?: string
  resourceType?: string
  ipAddr?: string
  rootCauseType?: string
  status?: number
}

export interface AiopsRootCauseVO {
  id: number
  analysisCode: string
  alertEventId: number
  eventCode: string
  resourceId: number
  resourceCode: string
  resourceName: string
  resourceType: string
  ipAddr: string
  rootCauseType: string
  rootCauseDesc: string
  evidence: string
  suggestion: string
  confidenceScore: number
  analysisTime: string
  status: number
  createTime: string
  remark: string
}

function unwrapApiResult<T>(response: any): T {
  const value = response?.data ?? response

  if (
    value &&
    typeof value === 'object' &&
    'code' in value &&
    'data' in value
  ) {
    if (value.code !== 0) {
      throw new Error(value.msg || '请求失败')
    }
    return value.data as T
  }

  return value as T
}

export function analyzeAiopsRootCauseApi(
  data: AiopsRootCauseAnalyzeRequest
): Promise<AiopsRootCauseVO> {
  return request
    .post<any, AiopsRootCauseVO>('/api/aiops/root-causes/analyze', data)
    .then((response) => unwrapApiResult<AiopsRootCauseVO>(response))
}

export function getAiopsRootCausePageApi(
  params: AiopsRootCausePageQuery
): Promise<PageResult<AiopsRootCauseVO>> {
  return request
    .get<any, PageResult<AiopsRootCauseVO>>('/api/aiops/root-causes/page', {
      params
    })
    .then((response) => unwrapApiResult<PageResult<AiopsRootCauseVO>>(response))
}

export function getAiopsRootCauseDetailApi(id: number): Promise<AiopsRootCauseVO> {
  return request
    .get<any, AiopsRootCauseVO>(`/api/aiops/root-causes/${id}`)
    .then((response) => unwrapApiResult<AiopsRootCauseVO>(response))
}
