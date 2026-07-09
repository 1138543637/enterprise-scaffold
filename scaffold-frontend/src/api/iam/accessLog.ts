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

export interface IamAccessLogPageQuery {
  pageNo: number
  pageSize: number
  requestUri?: string
  requestMethod?: string
  username?: string
  clientIp?: string
  accessStatus?: number | ''
  beginTime?: string
  endTime?: string
}

export interface IamAccessLogPageVO {
  id: number
  traceId: string
  requestUri: string
  requestMethod: string
  moduleName: string
  operationName: string
  userId: number
  username: string
  clientIp: string
  userAgent: string
  responseCode: number
  responseMsg: string
  accessStatus: number
  accessStatusName: string
  costMs: number
  accessTime: string
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

export async function getIamAccessLogPage(
  params: IamAccessLogPageQuery
): Promise<PageResult<IamAccessLogPageVO>> {
  const response = await request.get<any, PageResult<IamAccessLogPageVO>>(
    '/api/iam/access-logs/page',
    { params }
  )
  return normalizePage<IamAccessLogPageVO>(
    unwrapApiResult<PageResult<IamAccessLogPageVO>>(response)
  )
}
