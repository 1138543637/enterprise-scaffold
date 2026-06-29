import request from '../request'

interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

type ApiResponse<T> = T | ApiResult<T>

function unwrapApiResult<T>(response: ApiResponse<T>): T {
  const result = response as ApiResult<T>

  if (result && typeof result === 'object' && 'code' in result && 'data' in result) {
    if (result.code !== 0) {
      throw new Error(result.msg || '请求失败')
    }

    return result.data
  }

  return response as T
}

export interface MinePageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface MineMaintenanceTaskPageQuery {
  pageNo?: number
  pageSize?: number
  taskCode?: string
  deviceCode?: string
  deviceName?: string
  deviceType?: string
  areaName?: string
  riskLevel?: number
  taskStatus?: number
  priority?: number
  status?: number
}

export interface MineMaintenanceTaskVO {
  id: number
  taskCode: string
  deviceId: number
  deviceCode: string
  deviceName: string
  deviceType: string
  areaName: string
  location: string
  healthScore: number
  riskLevel: number
  riskLevelName: string
  taskTitle: string
  taskContent: string
  taskType: number
  taskSource: number
  taskStatus: number
  taskStatusName: string
  priority: number
  priorityName: string
  planStartTime: string
  planEndTime: string
  maintainerUserId: number
  maintainerUsername: string
  handleTime: string
  handleResult: string
  closeTime: string
  closeResult: string
  status: number
  createTime: string
  remark: string
}

export interface MineMaintenanceTaskSummaryVO {
  taskTotal: number
  pendingTotal: number
  plannedTotal: number
  processingTotal: number
  closedTotal: number
  highRiskTaskTotal: number
  urgentTotal: number
}

export interface MineMaintenanceTaskCreateRequest {
  deviceId: number
  remark?: string
}

export interface MineMaintenanceTaskPlanRequest {
  planStartTime: string
  planEndTime: string
  maintainerUsername: string
  maintainerUserId?: number
  remark?: string
}

export interface MineMaintenanceTaskHandleRequest {
  handleResult: string
  remark?: string
}

export interface MineMaintenanceTaskCloseRequest {
  closeResult: string
  remark?: string
}

export async function getMineMaintenanceTaskPageApi(
    params: MineMaintenanceTaskPageQuery
): Promise<MinePageResult<MineMaintenanceTaskVO>> {
  const response = await request.get<any, ApiResponse<MinePageResult<MineMaintenanceTaskVO>>>(
      '/api/mine/maintenance-tasks/page',
      { params }
  )

  return unwrapApiResult(response)
}

export async function getMineMaintenanceTaskSummaryApi(): Promise<MineMaintenanceTaskSummaryVO> {
  const response = await request.get<any, ApiResponse<MineMaintenanceTaskSummaryVO>>(
      '/api/mine/maintenance-tasks/summary'
  )

  return unwrapApiResult(response)
}

export async function createMineMaintenanceTaskFromDeviceHealthApi(
    data: MineMaintenanceTaskCreateRequest
): Promise<MineMaintenanceTaskVO> {
  const response = await request.post<any, ApiResponse<MineMaintenanceTaskVO>>(
      '/api/mine/maintenance-tasks/create-from-device-health',
      data
  )

  return unwrapApiResult(response)
}

export async function planMineMaintenanceTaskApi(
    id: number,
    data: MineMaintenanceTaskPlanRequest
): Promise<MineMaintenanceTaskVO> {
  const response = await request.post<any, ApiResponse<MineMaintenanceTaskVO>>(
      `/api/mine/maintenance-tasks/${id}/plan`,
      data
  )

  return unwrapApiResult(response)
}

export async function handleMineMaintenanceTaskApi(
    id: number,
    data: MineMaintenanceTaskHandleRequest
): Promise<MineMaintenanceTaskVO> {
  const response = await request.post<any, ApiResponse<MineMaintenanceTaskVO>>(
      `/api/mine/maintenance-tasks/${id}/handle`,
      data
  )

  return unwrapApiResult(response)
}

export async function closeMineMaintenanceTaskApi(
    id: number,
    data: MineMaintenanceTaskCloseRequest
): Promise<MineMaintenanceTaskVO> {
  const response = await request.post<any, ApiResponse<MineMaintenanceTaskVO>>(
      `/api/mine/maintenance-tasks/${id}/close`,
      data
  )

  return unwrapApiResult(response)
}