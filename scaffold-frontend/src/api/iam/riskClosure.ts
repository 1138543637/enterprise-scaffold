import request from '../request'

export interface ApiResult<T> {
  code?: number
  msg?: string
  message?: string
  data?: T
}

export interface PageResult<T> {
  records: T[]
  total: number
  pages?: number
  pageNo?: number
  pageSize?: number
  current?: number
  size?: number
}

export interface IamRiskClosureSummaryVO {
  unhandledLoginRiskCount: number
  confirmedLoginRiskCount: number
  ignoredLoginRiskCount: number
  closedLoginRiskCount: number
  pendingAuditCount: number
  reviewedAuditCount: number
  ignoredAuditCount: number
}

export interface IamLoginRiskPageQuery {
  pageNo?: number
  pageSize?: number
  riskCode?: string
  username?: string
  clientIp?: string
  riskType?: string
  riskLevel?: number | string
  handleStatus?: number | string
}

export interface IamLoginRiskPageVO {
  id: number
  riskCode?: string
  username?: string
  clientIp?: string
  riskType?: string
  riskTypeName?: string
  riskLevel?: number
  riskLevelName?: string
  failCount?: number
  firstFailTime?: string
  lastFailTime?: string
  detectTime?: string
  handleStatus?: number
  handleStatusName?: string
  handleBy?: string
  handleTime?: string
  handleRemark?: string
  status?: number
  createTime?: string
  remark?: string
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
}

export interface IamPermissionAuditPageVO {
  id: number
  auditCode?: string
  auditType?: string
  auditTypeName?: string
  targetType?: string
  targetTypeName?: string
  targetId?: number
  targetName?: string
  changeAction?: string
  changeActionName?: string
  beforeValue?: string
  afterValue?: string
  riskLevel?: string
  riskLevelName?: string
  reviewStatus?: string
  reviewStatusName?: string
  reviewBy?: string
  reviewTime?: string
  requestIp?: string
  operatorName?: string
  createTime?: string
  remark?: string
}

export interface IamLoginRiskHandleRequest {
  handleBy?: string
  handleRemark?: string
}

export interface IamPermissionAuditClosureRequest {
  reviewBy?: string
  remark?: string
}

function unwrapApiResult<T>(response: any): T {
  const first = response?.data ?? response
  if (first && typeof first === 'object' && 'data' in first && ('code' in first || 'msg' in first || 'message' in first)) {
    return first.data as T
  }
  return first as T
}

function normalizePage<T>(input: any): PageResult<T> {
  const data = unwrapApiResult<any>(input) || {}
  const records = Array.isArray(data.records) ? data.records : []
  const total = Number(data.total ?? 0)
  const pages = Number(data.pages ?? 0)
  const pageNo = Number(data.pageNo ?? data.current ?? 1)
  const pageSize = Number(data.pageSize ?? data.size ?? 10)
  return {
    records,
    total,
    pages,
    pageNo,
    pageSize,
    current: pageNo,
    size: pageSize
  }
}

function normalizeSummary(input: any): IamRiskClosureSummaryVO {
  const data = unwrapApiResult<any>(input) || {}
  return {
    unhandledLoginRiskCount: Number(data.unhandledLoginRiskCount ?? 0),
    confirmedLoginRiskCount: Number(data.confirmedLoginRiskCount ?? 0),
    ignoredLoginRiskCount: Number(data.ignoredLoginRiskCount ?? 0),
    closedLoginRiskCount: Number(data.closedLoginRiskCount ?? 0),
    pendingAuditCount: Number(data.pendingAuditCount ?? 0),
    reviewedAuditCount: Number(data.reviewedAuditCount ?? 0),
    ignoredAuditCount: Number(data.ignoredAuditCount ?? 0)
  }
}

export async function getIamRiskClosureSummary(): Promise<IamRiskClosureSummaryVO> {
  const response = await request.get<any, IamRiskClosureSummaryVO>('/api/iam/risk-closures/summary')
  return normalizeSummary(response)
}

export async function getIamLoginRiskPage(params: IamLoginRiskPageQuery): Promise<PageResult<IamLoginRiskPageVO>> {
  const response = await request.get<any, PageResult<IamLoginRiskPageVO>>('/api/iam/login-risks/page', { params })
  return normalizePage<IamLoginRiskPageVO>(response)
}

export async function getIamPermissionAuditPage(params: IamPermissionAuditPageQuery): Promise<PageResult<IamPermissionAuditPageVO>> {
  const response = await request.get<any, PageResult<IamPermissionAuditPageVO>>('/api/iam/permission-audits/page', { params })
  return normalizePage<IamPermissionAuditPageVO>(response)
}

export async function confirmIamLoginRisk(id: number, data: IamLoginRiskHandleRequest): Promise<boolean> {
  const response = await request.post<any, boolean>(`/api/iam/risk-closures/login-risks/${id}/confirm`, data)
  return Boolean(unwrapApiResult<boolean>(response))
}

export async function ignoreIamLoginRisk(id: number, data: IamLoginRiskHandleRequest): Promise<boolean> {
  const response = await request.post<any, boolean>(`/api/iam/risk-closures/login-risks/${id}/ignore`, data)
  return Boolean(unwrapApiResult<boolean>(response))
}

export async function closeIamLoginRisk(id: number, data: IamLoginRiskHandleRequest): Promise<boolean> {
  const response = await request.post<any, boolean>(`/api/iam/risk-closures/login-risks/${id}/close`, data)
  return Boolean(unwrapApiResult<boolean>(response))
}

export async function reviewIamPermissionAudit(id: number, data: IamPermissionAuditClosureRequest): Promise<boolean> {
  const response = await request.post<any, boolean>(`/api/iam/risk-closures/permission-audits/${id}/review`, data)
  return Boolean(unwrapApiResult<boolean>(response))
}

export async function ignoreIamPermissionAudit(id: number, data: IamPermissionAuditClosureRequest): Promise<boolean> {
  const response = await request.post<any, boolean>(`/api/iam/risk-closures/permission-audits/${id}/ignore`, data)
  return Boolean(unwrapApiResult<boolean>(response))
}
