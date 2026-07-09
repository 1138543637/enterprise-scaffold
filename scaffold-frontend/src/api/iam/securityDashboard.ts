import request from '../request'

export interface IamSecurityDashboardSummaryVO {
  todayAccessCount: number
  todayFailedAccessCount: number
  unhandledLoginRiskCount: number
  highRiskPermissionAuditCount: number
  pendingAuditCount: number
  enabledSecurityPolicyCount: number
  enabledRateLimitRuleCount: number
}

export interface IamRiskDistributionVO {
  riskLevel: string
  riskLevelName: string
  loginRiskCount: number
  permissionAuditCount: number
  totalCount: number
}

export interface IamReviewStatusStatVO {
  reviewStatus: string
  reviewStatusName: string
  totalCount: number
}

export interface IamPolicyStatusStatVO {
  enabled: number
  enabledName: string
  securityPolicyCount: number
  rateLimitRuleCount: number
  totalCount: number
}

export interface IamRecentSecurityEventVO {
  eventType: string
  eventTypeName: string
  eventCode: string
  eventTitle: string
  eventDetail: string
  riskLevel: string
  riskLevelName: string
  statusText: string
  operatorName: string
  requestIp: string
  eventTime: string
}

export interface IamSecurityDashboardVO {
  summary: IamSecurityDashboardSummaryVO
  riskDistributions: IamRiskDistributionVO[]
  reviewStatusStats: IamReviewStatusStatVO[]
  policyStatusStats: IamPolicyStatusStatVO[]
  recentEvents: IamRecentSecurityEventVO[]
}

interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

function unwrapApiResult<T>(response: T | ApiResult<T> | { data: ApiResult<T> } | { data: T }): T {
  const value = response as any

  if (value && typeof value === 'object' && 'code' in value && 'data' in value) {
    return value.data as T
  }

  if (value && typeof value === 'object' && 'data' in value) {
    const nested = value.data
    if (nested && typeof nested === 'object' && 'code' in nested && 'data' in nested) {
      return nested.data as T
    }
    return nested as T
  }

  return response as T
}

function normalizeDashboard(data: Partial<IamSecurityDashboardVO> | null | undefined): IamSecurityDashboardVO {
  return {
    summary: {
      todayAccessCount: Number(data?.summary?.todayAccessCount ?? 0),
      todayFailedAccessCount: Number(data?.summary?.todayFailedAccessCount ?? 0),
      unhandledLoginRiskCount: Number(data?.summary?.unhandledLoginRiskCount ?? 0),
      highRiskPermissionAuditCount: Number(data?.summary?.highRiskPermissionAuditCount ?? 0),
      pendingAuditCount: Number(data?.summary?.pendingAuditCount ?? 0),
      enabledSecurityPolicyCount: Number(data?.summary?.enabledSecurityPolicyCount ?? 0),
      enabledRateLimitRuleCount: Number(data?.summary?.enabledRateLimitRuleCount ?? 0)
    },
    riskDistributions: Array.isArray(data?.riskDistributions) ? data.riskDistributions : [],
    reviewStatusStats: Array.isArray(data?.reviewStatusStats) ? data.reviewStatusStats : [],
    policyStatusStats: Array.isArray(data?.policyStatusStats) ? data.policyStatusStats : [],
    recentEvents: Array.isArray(data?.recentEvents) ? data.recentEvents : []
  }
}

export async function getIamSecurityDashboard(): Promise<IamSecurityDashboardVO> {
  const response = await request.get<any, IamSecurityDashboardVO | ApiResult<IamSecurityDashboardVO> | { data: ApiResult<IamSecurityDashboardVO> }>(
    '/api/iam/security-dashboard/overview'
  )
  return normalizeDashboard(unwrapApiResult<IamSecurityDashboardVO>(response))
}
