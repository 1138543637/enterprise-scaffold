import request from '../request'

export interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

export interface RiskDashboardSummaryVO {
  transactionTotal: number
  riskTransactionTotal: number
  ruleTotal: number
  ruleHitTotal: number
  pendingReviewTotal: number
  approvedReviewTotal: number
  rejectedReviewTotal: number
}

export interface RiskChannelStatVO {
  channel: string
  channelName: string
  total: number
  riskTotal: number
}

export interface RiskTransactionTypeStatVO {
  transactionType: string
  transactionTypeName: string
  total: number
  riskTotal: number
}

export interface RiskLevelStatVO {
  riskLevel: number
  riskLevelName: string
  total: number
}

export interface RiskRecentTransactionVO {
  id: number
  transactionNo: string
  accountNo: string
  customerName: string
  merchantName: string
  transactionType: string
  transactionTypeName: string
  channel: string
  channelName: string
  amount: number
  currency: string
  location: string
  riskFlag: number
  riskFlagName: string
  transactionTime: string
}

export interface RiskRecentRuleHitVO {
  id: number
  hitCode: string
  transactionNo: string
  accountNo: string
  customerName: string
  ruleCode: string
  ruleName: string
  ruleType: string
  ruleTypeName: string
  riskLevel: number
  riskLevelName: string
  riskScore: number
  hitTime: string
}

export interface RiskRecentReviewOrderVO {
  id: number
  reviewOrderCode: string
  transactionNo: string
  customerName: string
  amount: number
  currency: string
  totalScore: number
  riskLevel: number
  riskLevelName: string
  riskResult: string
  riskResultName: string
  reviewStatus: number
  reviewStatusName: string
  createTime: string
}

function unwrapApiResult<T>(response: any): T {
  if (response && typeof response === 'object') {
    if ('code' in response && 'data' in response) {
      return response.data as T
    }

    if ('data' in response) {
      const data = response.data
      if (data && typeof data === 'object' && 'code' in data && 'data' in data) {
        return data.data as T
      }
      return data as T
    }
  }

  return response as T
}

export async function getRiskDashboardSummaryApi(): Promise<RiskDashboardSummaryVO> {
  const response = await request.get<any, RiskDashboardSummaryVO>(
    '/api/risk/dashboard/summary',
  )
  return unwrapApiResult<RiskDashboardSummaryVO>(response)
}

export async function getRiskChannelStatsApi(): Promise<RiskChannelStatVO[]> {
  const response = await request.get<any, RiskChannelStatVO[]>(
    '/api/risk/dashboard/channel-stats',
  )
  return unwrapApiResult<RiskChannelStatVO[]>(response)
}

export async function getRiskTransactionTypeStatsApi(): Promise<RiskTransactionTypeStatVO[]> {
  const response = await request.get<any, RiskTransactionTypeStatVO[]>(
    '/api/risk/dashboard/transaction-type-stats',
  )
  return unwrapApiResult<RiskTransactionTypeStatVO[]>(response)
}

export async function getRiskLevelStatsApi(): Promise<RiskLevelStatVO[]> {
  const response = await request.get<any, RiskLevelStatVO[]>(
    '/api/risk/dashboard/risk-level-stats',
  )
  return unwrapApiResult<RiskLevelStatVO[]>(response)
}

export async function getRiskRecentTransactionsApi(): Promise<RiskRecentTransactionVO[]> {
  const response = await request.get<any, RiskRecentTransactionVO[]>(
    '/api/risk/dashboard/recent-transactions',
  )
  return unwrapApiResult<RiskRecentTransactionVO[]>(response)
}

export async function getRiskRecentRuleHitsApi(): Promise<RiskRecentRuleHitVO[]> {
  const response = await request.get<any, RiskRecentRuleHitVO[]>(
    '/api/risk/dashboard/recent-rule-hits',
  )
  return unwrapApiResult<RiskRecentRuleHitVO[]>(response)
}

export async function getRiskRecentReviewOrdersApi(): Promise<RiskRecentReviewOrderVO[]> {
  const response = await request.get<any, RiskRecentReviewOrderVO[]>(
    '/api/risk/dashboard/recent-review-orders',
  )
  return unwrapApiResult<RiskRecentReviewOrderVO[]>(response)
}
