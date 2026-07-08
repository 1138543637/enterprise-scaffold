import request from '../request'

export interface PageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface DatahubMetadataCollectRequest {
  dataSourceId: number
}

export interface DatahubMetadataCollectResultVO {
  collectBatchNo: string
  dataSourceId: number
  dataSourceName: string
  tableTotal: number
  columnTotal: number
  costTime: number
}

export interface DatahubMetadataTablePageQuery {
  pageNo?: number
  pageSize?: number
  dataSourceId?: number
  schemaName?: string
  tableName?: string
  tableType?: string
  status?: number
}

export interface DatahubMetadataTablePageVO {
  id: number
  dataSourceId: number
  dataSourceName: string
  schemaName: string
  tableName: string
  tableComment: string
  tableType: string
  rowCount: number
  collectBatchNo: string
  collectTime: string
  status: number
  createTime: string
  remark: string
}

export interface DatahubMetadataColumnPageQuery {
  pageNo?: number
  pageSize?: number
  tableId?: number
  dataSourceId?: number
  schemaName?: string
  tableName?: string
  columnName?: string
  dataType?: string
  nullableFlag?: number
  primaryKeyFlag?: number
  status?: number
}

export interface DatahubMetadataColumnPageVO {
  id: number
  tableId: number
  dataSourceId: number
  dataSourceName: string
  schemaName: string
  tableName: string
  columnName: string
  columnComment: string
  columnType: string
  dataType: string
  columnLength: number
  numericPrecision: number
  numericScale: number
  nullableFlag: number
  primaryKeyFlag: number
  columnDefault: string
  ordinalPosition: number
  collectBatchNo: string
  collectTime: string
  status: number
  createTime: string
  remark: string
}

export interface DatahubMetadataCollectLogPageQuery {
  pageNo?: number
  pageSize?: number
  dataSourceId?: number
  collectBatchNo?: string
  collectStatus?: number
  status?: number
}

export interface DatahubMetadataCollectLogPageVO {
  id: number
  collectBatchNo: string
  dataSourceId: number
  dataSourceName: string
  collectStatus: number
  tableTotal: number
  columnTotal: number
  errorMsg: string
  startTime: string
  endTime: string
  costTime: number
  status: number
  createTime: string
  remark: string
}

export function collectMetadata(data: DatahubMetadataCollectRequest) {
  return request.post<any, DatahubMetadataCollectResultVO>('/api/datahub/metadata/collect', data)
}

export function getMetadataTablePage(params: DatahubMetadataTablePageQuery) {
  return request.get<any, PageResult<DatahubMetadataTablePageVO>>('/api/datahub/metadata/tables/page', {
    params
  })
}

export function getMetadataColumnPage(params: DatahubMetadataColumnPageQuery) {
  return request.get<any, PageResult<DatahubMetadataColumnPageVO>>('/api/datahub/metadata/columns/page', {
    params
  })
}

export function getMetadataCollectLogPage(params: DatahubMetadataCollectLogPageQuery) {
  return request.get<any, PageResult<DatahubMetadataCollectLogPageVO>>('/api/datahub/metadata/collect-logs/page', {
    params
  })
}
