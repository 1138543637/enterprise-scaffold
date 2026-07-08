import request from '../request'

export type PageResult<T> = {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export type DatahubQualityRulePageQuery = {
  pageNo?: number
  pageSize?: number
  ruleName?: string
  ruleType?: string
  targetType?: string
  targetTableId?: number
  targetColumnId?: number
  status?: number
}

export type DatahubQualityRulePageVO = {
  id: number
  ruleCode: string
  ruleName: string
  ruleType: string
  targetType: string
  targetTableId?: number
  targetTableName?: string
  targetColumnId?: number
  targetColumnName?: string
  checkExpression?: string
  qualityLevel: number
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export type DatahubQualityCheckRequest = {
  ruleId?: number
  targetTableId?: number
}

export type DatahubQualityResultPageQuery = {
  pageNo?: number
  pageSize?: number
  ruleName?: string
  ruleCode?: string
  tableCode?: string
  columnName?: string
  checkStatus?: number
  tableId?: number
  columnId?: number
}

export type DatahubQualityResultPageVO = {
  id: number
  resultCode: string
  ruleId: number
  ruleCode: string
  ruleName: string
  datasourceId?: number
  datasourceCode?: string
  tableId?: number
  tableCode?: string
  columnId?: number
  columnName?: string
  checkTotal: number
  errorTotal: number
  errorRate: number
  checkStatus: number
  checkTime?: string
  status: number
  remark?: string
}

export type DatahubMetadataTableOption = {
  id: number
  tableName: string
  tableComment?: string
  dataSourceId?: number
  dataSourceName?: string
}

export type DatahubMetadataColumnOption = {
  id: number
  tableId: number
  tableName: string
  columnName: string
  columnComment?: string
  dataType?: string
  nullableFlag?: string
  primaryKeyFlag?: string
}

export function getQualityRulesPage(params: DatahubQualityRulePageQuery) {
  return request.get<any, PageResult<DatahubQualityRulePageVO>>('/api/datahub/quality-rules/page', { params })
}

export function checkQualityResults(data: DatahubQualityCheckRequest) {
  return request.post<any, DatahubQualityResultPageVO[]>('/api/datahub/quality-results/check', data)
}

export function getQualityResultsPage(params: DatahubQualityResultPageQuery) {
  return request.get<any, PageResult<DatahubQualityResultPageVO>>('/api/datahub/quality-results/page', { params })
}

export function getMetadataTablesForQuality(params: Record<string, any>) {
  return request.get<any, PageResult<DatahubMetadataTableOption>>('/api/datahub/metadata/tables/page', { params })
}

export function getMetadataColumnsForQuality(params: Record<string, any>) {
  return request.get<any, PageResult<DatahubMetadataColumnOption>>('/api/datahub/metadata/columns/page', { params })
}
