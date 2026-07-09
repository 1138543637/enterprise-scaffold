import request from '../request'

export interface PageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface DatahubApiPublishPageQuery {
  pageNo?: number
  pageSize?: number
  apiCode?: string
  apiName?: string
  datasourceName?: string
  tableName?: string
  requestMethod?: string
  onlineStatus?: number
  status?: number
}

export interface DatahubApiPublishCreateRequest {
  tableId: number
  apiName?: string
  apiPath?: string
  requestMethod?: string
  ownerName?: string
  remark?: string
}

export interface DatahubApiPublishPageVO {
  id: number
  apiCode: string
  apiName: string
  datasourceId?: number
  datasourceCode?: string
  datasourceName?: string
  tableId: number
  tableCode?: string
  tableName: string
  tableComment?: string
  apiPath: string
  requestMethod: string
  publishType: string
  onlineStatus: number
  authRequired: number
  ownerName?: string
  publishTime?: string
  lastOnlineTime?: string
  lastOfflineTime?: string
  status: number
  createTime?: string
  remark?: string
}

function unwrapData<T>(response: any): T {
  if (response && typeof response === 'object' && 'code' in response && 'data' in response) {
    return response.data as T
  }
  if (response && typeof response === 'object' && response.data && typeof response.data === 'object' && 'code' in response.data) {
    return response.data.data as T
  }
  return response as T
}

export function pageApiPublishes(params: DatahubApiPublishPageQuery): Promise<PageResult<DatahubApiPublishPageVO>> {
  return request
    .get<any, any>('/api/datahub/api-publishes/page', { params })
    .then((res) => unwrapData<PageResult<DatahubApiPublishPageVO>>(res))
}

export function publishApiFromTable(data: DatahubApiPublishCreateRequest): Promise<DatahubApiPublishPageVO> {
  return request
    .post<any, any>('/api/datahub/api-publishes/publish-from-table', data)
    .then((res) => unwrapData<DatahubApiPublishPageVO>(res))
}

export function onlineApiPublish(id: number): Promise<DatahubApiPublishPageVO> {
  return request
    .post<any, any>(`/api/datahub/api-publishes/${id}/online`)
    .then((res) => unwrapData<DatahubApiPublishPageVO>(res))
}

export function offlineApiPublish(id: number): Promise<DatahubApiPublishPageVO> {
  return request
    .post<any, any>(`/api/datahub/api-publishes/${id}/offline`)
    .then((res) => unwrapData<DatahubApiPublishPageVO>(res))
}
