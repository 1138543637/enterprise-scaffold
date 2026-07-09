import request from '../request'

export interface PageResult<T> {
  records: T[]
  total: number
  pageNo?: number
  pageSize?: number
  current?: number
  size?: number
}

export interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

export interface IamLoginRiskPageQuery {
  pageNo: number
  pageSize: number
  riskCode?: string
  username?: string
  clientIp?: string
  riskType?: string
  riskLevel?: number | ''
  handleStatus?: number | ''
  beginTime?: string
  endTime?: string
}

export interface IamLoginRiskPageVO {
  id: number
  riskCode: string
  username: string
  clientIp: string
  riskType: string
  riskTypeName: string
  riskLevel: number
  riskLevelName: string
  failCount: number
  firstFailTime: string
  lastFailTime: string
  detectTime: string
  handleStatus: number
  handleStatusName: string
  status: number
  createTime: string
  remark: string
}

function unwrapApiResult<T>(response: any): T {
  if (response == null) {
    return response as T
  }

  if (typeof response === 'object' && 'code' in response && 'data' in response) {
    return response.data as T
  }

  if (
    response.data &&
    typeof response.data === 'object' &&
    'code' in response.data &&
    'data' in response.data
  ) {
    return response.data.data as T
  }

  if (response.data && typeof response.data === 'object') {
    return response.data as T
  }

  return response as T
}

function normalizePage<T>(page: any): PageResult<T> {
  const records = Array.isArray(page?.records) ? page.records : []
  return {
    records,
    total: Number(page?.total || 0),
    pageNo: Number(page?.pageNo || page?.current || 1),
    pageSize: Number(page?.pageSize || page?.size || 10)
  }
}

export async function getIamLoginRiskPage(
  params: IamLoginRiskPageQuery
): Promise<PageResult<IamLoginRiskPageVO>> {
  const response = await request.get<any, PageResult<IamLoginRiskPageVO>>(
    '/api/iam/login-risks/page',
    { params }
  )
  return normalizePage<IamLoginRiskPageVO>(
    unwrapApiResult<PageResult<IamLoginRiskPageVO>>(response)
  )
}

export async function detectIamLoginRisks(): Promise<IamLoginRiskPageVO[]> {
  const response = await request.post<any, IamLoginRiskPageVO[]>(
    '/api/iam/login-risks/detect',
    {}
  )
  const data = unwrapApiResult<IamLoginRiskPageVO[]>(response)
  return Array.isArray(data) ? data : []
}
