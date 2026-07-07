import request from '../request'

export interface RiskPageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface RiskTransactionSimulateRequest {
  count?: number
  accountNo?: string
  customerName?: string
  transactionType?: string
  channel?: string
  minAmount?: number
  maxAmount?: number
  riskFlag?: number
  remark?: string
}

export interface RiskTransactionPageQuery {
  pageNo?: number
  pageSize?: number
  transactionNo?: string
  accountNo?: string
  customerName?: string
  merchantName?: string
  transactionType?: string
  channel?: string
  transactionStatus?: number
  riskFlag?: number
  status?: number
  beginTime?: string
  endTime?: string
}

export interface RiskTransactionVO {
  id: number
  transactionNo: string
  accountNo: string
  customerId: number
  customerName: string
  merchantId: string
  merchantName: string
  transactionType: string
  transactionTypeName: string
  channel: string
  channelName: string
  amount: number
  currency: string
  ipAddr: string
  deviceId: string
  location: string
  transactionTime: string
  transactionStatus: number
  transactionStatusName: string
  riskFlag: number
  riskFlagName: string
  status: number
  statusName: string
  createTime: string
  remark: string
}

export function simulateRiskTransactionsApi(
  data: RiskTransactionSimulateRequest
): Promise<RiskTransactionVO[]> {
  return request.post<any, RiskTransactionVO[]>('/api/risk/transactions/simulate', data)
}

export function getRiskTransactionLatestApi(): Promise<RiskTransactionVO[]> {
  return request.get<any, RiskTransactionVO[]>('/api/risk/transactions/latest')
}

export function getRiskTransactionPageApi(
  params: RiskTransactionPageQuery
): Promise<RiskPageResult<RiskTransactionVO>> {
  return request.get<any, RiskPageResult<RiskTransactionVO>>('/api/risk/transactions/page', {
    params
  })
}
