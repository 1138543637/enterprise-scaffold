import request from '../request'

export interface PageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface DatahubSensitiveScanRequest {
  targetTableId?: number
  sensitiveType?: string
  limit?: number
}

export interface DatahubSensitiveFieldPageVO {
  id: number
  fieldCode: string
  datasourceId?: number
  datasourceName?: string
  tableId: number
  schemaName?: string
  tableName: string
  columnId: number
  columnName: string
  columnComment?: string
  dataType?: string
  sensitiveType: string
  sensitiveLevel: number
  detectRule?: string
  confidence?: number
  confirmStatus: number
  maskType: string
  status: number
  createTime?: string
  remark?: string
}

export interface DatahubMaskRulePageVO {
  id: number
  ruleCode: string
  ruleName: string
  sensitiveType: string
  maskMethod: string
  keepPrefix: number
  keepSuffix: number
  maskChar: string
  status: number
  createTime?: string
  remark?: string
}

export interface DatahubMaskPreviewRequest {
  fieldId: number
  maskRuleId?: number
  rawValue?: string
}

export interface DatahubMaskResultPageVO {
  id: number
  resultCode: string
  fieldId: number
  ruleId?: number
  datasourceId?: number
  datasourceName?: string
  tableId: number
  tableName: string
  columnId: number
  columnName: string
  sensitiveType: string
  maskMethod: string
  sampleBefore?: string
  sampleAfter?: string
  maskStatus: number
  maskTime?: string
  status: number
  createTime?: string
  remark?: string
}

function unwrapData<T>(res: any): T {
  if (res && typeof res === 'object' && 'code' in res && 'data' in res) {
    return res.data as T
  }
  return res as T
}

function emptyPage<T>(): PageResult<T> {
  return {
    pageNo: 1,
    pageSize: 10,
    total: 0,
    pages: 0,
    records: []
  }
}

function unwrapPage<T>(res: any): PageResult<T> {
  const data = unwrapData<PageResult<T>>(res)

  if (!data || typeof data !== 'object') {
    return emptyPage<T>()
  }

  return {
    pageNo: Number(data.pageNo || 1),
    pageSize: Number(data.pageSize || 10),
    total: Number(data.total || 0),
    pages: Number(data.pages || 0),
    records: Array.isArray(data.records) ? data.records : []
  }
}

export async function scanSensitiveFields(data: DatahubSensitiveScanRequest) {
  const res = await request.post<any, any>('/api/datahub/sensitive-fields/scan', data)
  const unwrapped = unwrapData<DatahubSensitiveFieldPageVO[]>(res)
  return Array.isArray(unwrapped) ? unwrapped : []
}

export async function getSensitiveFieldPage(params: any) {
  const res = await request.get<any, any>('/api/datahub/sensitive-fields/page', { params })
  return unwrapPage<DatahubSensitiveFieldPageVO>(res)
}

export async function getMaskRulePage(params: any) {
  const res = await request.get<any, any>('/api/datahub/mask-rules/page', { params })
  return unwrapPage<DatahubMaskRulePageVO>(res)
}

export async function previewMask(data: DatahubMaskPreviewRequest) {
  const res = await request.post<any, any>('/api/datahub/mask-results/preview', data)
  return unwrapData<DatahubMaskResultPageVO>(res)
}

export async function getMaskResultPage(params: any) {
  const res = await request.get<any, any>('/api/datahub/mask-results/page', { params })
  return unwrapPage<DatahubMaskResultPageVO>(res)
}