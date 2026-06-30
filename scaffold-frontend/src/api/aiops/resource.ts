import request from '../request'

export interface AiopsResourcePageQuery {
  pageNo?: number
  pageSize?: number
  resourceCode?: string
  resourceName?: string
  resourceType?: string
  ipAddr?: string
  envType?: string
  systemName?: string
  ownerName?: string
  collectEnabled?: number
  status?: number
}

export interface AiopsResourcePageVO {
  id: number
  resourceCode: string
  resourceName: string
  resourceType: string
  ipAddr: string
  port: number
  envType: string
  systemName: string
  ownerName: string
  collectEnabled: number
  lastCollectTime: string
  status: number
  createTime: string
  remark: string
}

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

function unwrapApiResult<T>(response: any): T {
  if (response && typeof response === 'object' && 'code' in response && 'data' in response) {
    return (response as ApiResult<T>).data
  }

  if (
    response &&
    typeof response === 'object' &&
    response.data &&
    typeof response.data === 'object' &&
    'code' in response.data &&
    'data' in response.data
  ) {
    return (response.data as ApiResult<T>).data
  }

  if (response && typeof response === 'object' && 'data' in response) {
    return response.data as T
  }

  return response as T
}

export async function getAiopsResourcePageApi(
  params: AiopsResourcePageQuery
): Promise<PageResult<AiopsResourcePageVO>> {
  const response = await request.get<any, PageResult<AiopsResourcePageVO>>(
    '/api/aiops/resources/page',
    {
      params
    }
  )
  return unwrapApiResult<PageResult<AiopsResourcePageVO>>(response)
}
