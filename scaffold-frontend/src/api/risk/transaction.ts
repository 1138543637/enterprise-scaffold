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

export interface RiskTransactionKafkaMessage {
  transactionNo?: string
  accountNo?: string
  customerId?: number
  customerName?: string
  merchantId?: string
  merchantName?: string
  transactionType?: string
  channel?: string
  amount?: number
  currency?: string
  ipAddr?: string
  deviceId?: string
  location?: string
  transactionTime?: string
  transactionStatus?: number
  remark?: string
}

export interface RiskKafkaBatchSimulateRequest {
  count?: number
  transactionType?: string
  channel?: string
  minAmount?: number
  maxAmount?: number
  location?: string
  remark?: string
}

export interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

function unwrapApiResult<T>(response: any): T {
  if (response && typeof response === 'object' && 'code' in response && 'data' in response) {
    return response.data as T
  }

  if (
      response &&
      response.data &&
      typeof response.data === 'object' &&
      'code' in response.data &&
      'data' in response.data
  ) {
    return response.data.data as T
  }

  if (response && response.data !== undefined) {
    return response.data as T
  }

  return response as T
}

export function simulateRiskKafkaPublishApi(
    data?: RiskTransactionKafkaMessage,
): Promise<RiskTransactionKafkaMessage> {
  return request
      .post<any, RiskTransactionKafkaMessage>('/api/risk/kafka/simulate-publish', data || {})
      .then((res) => unwrapApiResult<RiskTransactionKafkaMessage>(res))
}

export function simulateRiskKafkaBatchApi(data?: RiskKafkaBatchSimulateRequest): Promise<number> {
  return request
      .post<any, number>('/api/risk/kafka/simulate-batch', data || { count: 10 })
      .then((res) => unwrapApiResult<number>(res))
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
