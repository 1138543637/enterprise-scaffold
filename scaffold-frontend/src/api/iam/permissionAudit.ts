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

export interface IamPermissionAuditPageQuery {
  pageNo?: number
  pageSize?: number
  auditCode?: string
  auditType?: string
  targetType?: string
  targetName?: string
  changeAction?: string
  riskLevel?: string
  reviewStatus?: string
  operatorName?: string
  beginTime?: string
  endTime?: string
}

export interface IamPermissionAuditPageVO {
  id: number
  auditCode: string
  auditType: string
  auditTypeName: string
  targetType: string
  targetTypeName: string
  targetId?: number
  targetName?: string
  changeAction: string
  changeActionName: string
  beforeValue?: string
  afterValue?: string
  riskLevel: string
  riskLevelName: string
  reviewStatus: string
  reviewStatusName: string
  reviewBy?: string
  reviewTime?: string
  requestIp?: string
  operatorName?: string
  status: number
  statusName: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface IamPermissionAuditSimulateRequest {
  auditType: string
  targetType: string
  targetId?: number
  targetName?: string
  changeAction: string
  beforeValue?: string
  afterValue?: string
  riskLevel?: string
  requestIp?: string
  operatorName?: string
  remark?: string
}

export interface IamPermissionAuditReviewRequest {
  reviewStatus: string
  reviewBy?: string
  remark?: string
}

function unwrapApiResult<T>(response: any): T {
  if (response && typeof response === 'object' && 'code' in response && 'data' in response) {
    return response.data as T
  }
  if (
    response &&
    typeof response === 'object' &&
    response.data &&
    typeof response.data === 'object' &&
    'code' in response.data &&
    'data' in response.data
  ) {
    return response.data.data as T
  }
  return response as T
}

function normalizePage<T>(value: any): PageResult<T> {
  const page = unwrapApiResult<PageResult<T>>(value)
  const records = Array.isArray((page as any)?.records) ? (page as any).records : []
  return {
    pageNo: Number((page as any)?.pageNo ?? 1),
    pageSize: Number((page as any)?.pageSize ?? 10),
    total: Number((page as any)?.total ?? 0),
    pages: Number((page as any)?.pages ?? 0),
    records
  }
}

export async function getIamPermissionAuditPage(
  query: IamPermissionAuditPageQuery
): Promise<PageResult<IamPermissionAuditPageVO>> {
  const response = await request.get<any, any>('/api/iam/permission-audits/page', {
    params: query
  })
  return normalizePage<IamPermissionAuditPageVO>(response)
}

export async function simulateIamPermissionAudit(
  data: IamPermissionAuditSimulateRequest
): Promise<IamPermissionAuditPageVO> {
  const response = await request.post<any, any>('/api/iam/permission-audits/simulate', data)
  return unwrapApiResult<IamPermissionAuditPageVO>(response)
}

export async function reviewIamPermissionAudit(
  id: number,
  data: IamPermissionAuditReviewRequest
): Promise<IamPermissionAuditPageVO> {
  const response = await request.post<any, any>(`/api/iam/permission-audits/${id}/review`, data)
  return unwrapApiResult<IamPermissionAuditPageVO>(response)
}
