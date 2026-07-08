import request from '../request'

export interface PageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

export interface DatahubDatasourcePageQuery {
  pageNo?: number
  pageSize?: number
  datasourceCode?: string
  datasourceName?: string
  datasourceType?: string
  host?: string
  databaseName?: string
  envType?: string
  ownerName?: string
  testStatus?: number
  status?: number
}

export interface DatahubDatasourcePageVO {
  id: number
  datasourceCode: string
  datasourceName: string
  datasourceType: string
  jdbcUrl: string
  host: string
  port: number
  databaseName: string
  username: string
  ownerName: string
  envType: string
  testStatus: number
  lastTestTime: string
  status: number
  createTime: string
  remark: string
}

export interface DatahubDatasourceTestRequest {
  datasourceType: string
  jdbcUrl: string
  username?: string
  password?: string
}

export interface DatahubDatasourceTestVO {
  datasourceType: string
  jdbcUrl: string
  testStatus: number
  testMessage: string
  costTime: number
  databaseProductName?: string
  databaseProductVersion?: string
  testTime: string
}

function unwrapApiResult<T>(response: any): T {
  const body = response && response.data !== undefined ? response.data : response

  if (body && typeof body === 'object' && 'code' in body && 'data' in body) {
    return body.data as T
  }

  return body as T
}

export async function getDatahubDatasourcePageApi(
  query: DatahubDatasourcePageQuery
): Promise<PageResult<DatahubDatasourcePageVO>> {
  const response = await request.get<any, PageResult<DatahubDatasourcePageVO>>(
    '/api/datahub/datasources/page',
    {
      params: query
    }
  )
  return unwrapApiResult<PageResult<DatahubDatasourcePageVO>>(response)
}

export async function testDatahubDatasourceConnectionApi(
  data: DatahubDatasourceTestRequest
): Promise<DatahubDatasourceTestVO> {
  const response = await request.post<any, DatahubDatasourceTestVO>(
    '/api/datahub/datasources/test-connection',
    data
  )
  return unwrapApiResult<DatahubDatasourceTestVO>(response)
}
