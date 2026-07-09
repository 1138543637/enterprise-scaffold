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

export interface IamSecurityPolicyPageQuery {
  pageNo?: number
  pageSize?: number
  policyCode?: string
  policyName?: string
  policyType?: string
  policyLevel?: number
  effectiveScope?: string
  enabled?: number
  status?: number
  beginTime?: string
  endTime?: string
}

export interface IamSecurityPolicyPageVO {
  id: number
  policyCode: string
  policyName: string
  policyType: string
  policyTypeName: string
  policyLevel: number
  policyLevelName: string
  policyValue: string
  policyUnit: string
  policyUnitName: string
  effectiveScope: string
  effectiveScopeName: string
  enabled: number
  enabledName: string
  status: number
  statusName: string
  createTime: string
  updateTime: string
  remark: string
}

export interface IamSecurityPolicyUpdateRequest {
  policyValue?: string
  policyUnit?: string
  effectiveScope?: string
  enabled?: number
  remark?: string
}

function unwrapApiResult<T>(response: any): T {
  if (response && typeof response === 'object' && 'code' in response && 'data' in response) {
    return response.data as T
  }
  if (response?.data && typeof response.data === 'object' && 'code' in response.data && 'data' in response.data) {
    return response.data.data as T
  }
  if (response?.data?.data !== undefined) {
    return response.data.data as T
  }
  if (response?.data !== undefined) {
    return response.data as T
  }
  return response as T
}

function normalizePage<T>(raw: any): PageResult<T> {
  const page = unwrapApiResult<PageResult<T>>(raw)
  const records = Array.isArray(page?.records) ? page.records : []
  const pageNo = Number(page?.pageNo || 1)
  const pageSize = Number(page?.pageSize || 10)
  const total = Number(page?.total || 0)
  const pages = Number(page?.pages || Math.ceil(total / Math.max(pageSize, 1)))
  return {
    pageNo,
    pageSize,
    total,
    pages,
    records
  }
}

export async function getIamSecurityPolicyPage(
  params: IamSecurityPolicyPageQuery
): Promise<PageResult<IamSecurityPolicyPageVO>> {
  const response = await request.get<any, any>('/api/iam/security-policies/page', { params })
  return normalizePage<IamSecurityPolicyPageVO>(response)
}

export async function enableIamSecurityPolicy(id: number): Promise<boolean> {
  const response = await request.post<any, any>(`/api/iam/security-policies/${id}/enable`)
  return Boolean(unwrapApiResult<boolean>(response))
}

export async function disableIamSecurityPolicy(id: number): Promise<boolean> {
  const response = await request.post<any, any>(`/api/iam/security-policies/${id}/disable`)
  return Boolean(unwrapApiResult<boolean>(response))
}

export async function updateIamSecurityPolicy(
  id: number,
  data: IamSecurityPolicyUpdateRequest
): Promise<IamSecurityPolicyPageVO> {
  const response = await request.post<any, any>(`/api/iam/security-policies/${id}/update-config`, data)
  return unwrapApiResult<IamSecurityPolicyPageVO>(response)
}
