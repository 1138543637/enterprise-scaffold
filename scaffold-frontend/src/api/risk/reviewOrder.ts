import request from '../request'

export interface PageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

export interface RiskReviewOrderPageQuery {
  pageNo?: number
  pageSize?: number
  reviewOrderCode?: string
  transactionNo?: string
  accountNo?: string
  customerName?: string
  merchantName?: string
  transactionType?: string
  channel?: string
  riskLevel?: number
  riskResult?: string
  reviewStatus?: number
  status?: number
  beginTime?: string
  endTime?: string
}

export interface RiskReviewOrderPageVO {
  id: number
  reviewOrderCode: string
  transactionId: number
  transactionNo: string
  accountNo?: string
  customerId?: string
  customerName?: string
  merchantId?: string
  merchantName?: string
  transactionType?: string
  channel?: string
  amount?: number
  currency?: string
  totalScore: number
  riskLevel: number
  riskResult: string
  reviewStatus: number
  reviewerUserId?: number
  reviewerUsername?: string
  reviewTime?: string
  reviewResult?: string
  status: number
  createTime?: string
  remark?: string
}

export interface RiskReviewSummaryVO {
  totalOrderCount: number
  pendingCount: number
  approvedCount: number
  rejectedCount: number
  lowRiskCount: number
  mediumRiskCount: number
  highRiskCount: number
  reviewResultReviewCount: number
  reviewResultRejectCount: number
  todayOrderCount: number
  averageScore: number
}

export interface RiskReviewOrderCreateRequest {
  transactionId?: number
  limit?: number
}

export interface RiskReviewApproveRequest {
  reviewerUserId?: number
  reviewerUsername?: string
  reviewResult: string
}

export interface RiskReviewRejectRequest {
  reviewerUserId?: number
  reviewerUsername?: string
  reviewResult: string
}

function unwrapApiResult<T>(response: unknown): T {
  const maybeResponse = response as any

  if (maybeResponse && typeof maybeResponse === 'object') {
    if ('code' in maybeResponse && 'data' in maybeResponse) {
      return maybeResponse.data as T
    }

    if (
      'data' in maybeResponse &&
      maybeResponse.data &&
      typeof maybeResponse.data === 'object' &&
      'code' in maybeResponse.data &&
      'data' in maybeResponse.data
    ) {
      return maybeResponse.data.data as T
    }
  }

  return response as T
}

export async function getRiskReviewOrderPageApi(
  params: RiskReviewOrderPageQuery,
): Promise<PageResult<RiskReviewOrderPageVO>> {
  const response = await request.get<
    any,
    PageResult<RiskReviewOrderPageVO> | ApiResult<PageResult<RiskReviewOrderPageVO>>
  >('/api/risk/review-orders/page', { params })
  return unwrapApiResult<PageResult<RiskReviewOrderPageVO>>(response)
}

export async function getRiskReviewSummaryApi(): Promise<RiskReviewSummaryVO> {
  const response = await request.get<any, RiskReviewSummaryVO | ApiResult<RiskReviewSummaryVO>>(
    '/api/risk/review-orders/summary',
  )
  return unwrapApiResult<RiskReviewSummaryVO>(response)
}

export async function createRiskReviewOrderFromTransactionApi(
  data: RiskReviewOrderCreateRequest = {},
): Promise<RiskReviewOrderPageVO[]> {
  const response = await request.post<
    any,
    RiskReviewOrderPageVO[] | ApiResult<RiskReviewOrderPageVO[]>
  >('/api/risk/review-orders/create-from-transaction', data, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
  const orders = unwrapApiResult<RiskReviewOrderPageVO[]>(response)
  return Array.isArray(orders) ? orders : []
}

export async function approveRiskReviewOrderApi(
  id: number,
  data: RiskReviewApproveRequest,
): Promise<RiskReviewOrderPageVO> {
  const response = await request.post<
    any,
    RiskReviewOrderPageVO | ApiResult<RiskReviewOrderPageVO>
  >(`/api/risk/review-orders/${id}/approve`, data, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
  return unwrapApiResult<RiskReviewOrderPageVO>(response)
}

export async function rejectRiskReviewOrderApi(
  id: number,
  data: RiskReviewRejectRequest,
): Promise<RiskReviewOrderPageVO> {
  const response = await request.post<
    any,
    RiskReviewOrderPageVO | ApiResult<RiskReviewOrderPageVO>
  >(`/api/risk/review-orders/${id}/reject`, data, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
  return unwrapApiResult<RiskReviewOrderPageVO>(response)
}
