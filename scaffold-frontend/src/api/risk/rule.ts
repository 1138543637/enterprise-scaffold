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

export interface RiskRulePageQuery {
  pageNo?: number
  pageSize?: number
  ruleCode?: string
  ruleName?: string
  ruleType?: string
  riskLevel?: number
  status?: number
}

export interface RiskRulePageVO {
  id: number
  ruleCode: string
  ruleName: string
  ruleType: string
  conditionType: string
  compareOperator: string
  thresholdValue?: number
  riskLevel: number
  riskScore: number
  ruleContent?: string
  status: number
  createTime?: string
  remark?: string
}

export interface RiskRuleHitGenerateRequest {
  transactionId?: number
  limit?: number
}

export interface RiskRuleHitPageQuery {
  pageNo?: number
  pageSize?: number
  hitCode?: string
  transactionNo?: string
  accountNo?: string
  customerName?: string
  ruleCode?: string
  ruleName?: string
  ruleType?: string
  riskLevel?: number
  status?: number
  beginTime?: string
  endTime?: string
}

export interface RiskRuleHitPageVO {
  id: number
  hitCode: string
  transactionId: number
  transactionNo: string
  accountNo: string
  customerId: string
  customerName: string
  ruleId: number
  ruleCode: string
  ruleName: string
  ruleType: string
  hitValue?: string
  thresholdValue?: number
  riskLevel: number
  riskScore: number
  hitTime?: string
  status: number
  createTime?: string
  remark?: string
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

export async function getRiskRulePageApi(
  params: RiskRulePageQuery,
): Promise<PageResult<RiskRulePageVO>> {
  const response = await request.get<any, PageResult<RiskRulePageVO> | ApiResult<PageResult<RiskRulePageVO>>>(
    '/api/risk/rules/page',
    { params },
  )
  return unwrapApiResult<PageResult<RiskRulePageVO>>(response)
}

export async function generateRiskRuleHitsApi(
  data: RiskRuleHitGenerateRequest = {},
): Promise<RiskRuleHitPageVO[]> {
  const response = await request.post<any, RiskRuleHitPageVO[] | ApiResult<RiskRuleHitPageVO[]>>(
    '/api/risk/rule-hits/generate',
    data,
    {
      headers: {
        'Content-Type': 'application/json',
      },
    },
  )
  const hits = unwrapApiResult<RiskRuleHitPageVO[]>(response)
  return Array.isArray(hits) ? hits : []
}

export async function getRiskRuleHitPageApi(
  params: RiskRuleHitPageQuery,
): Promise<PageResult<RiskRuleHitPageVO>> {
  const response = await request.get<any, PageResult<RiskRuleHitPageVO> | ApiResult<PageResult<RiskRuleHitPageVO>>>(
    '/api/risk/rule-hits/page',
    { params },
  )
  return unwrapApiResult<PageResult<RiskRuleHitPageVO>>(response)
}
