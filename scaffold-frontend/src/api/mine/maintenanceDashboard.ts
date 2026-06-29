import request from '../request'

export interface MineMaintenanceDashboardSummaryVO {
  taskTotal: number
  unclosedTaskTotal: number
  pendingTotal: number
  plannedTotal: number
  processingTotal: number
  closedTotal: number
  highRiskTaskTotal: number
  urgentTotal: number
  todayNewTaskTotal: number
  alarmTotal7d: number
  workOrderTotal7d: number
}

export interface MineMaintenanceTaskStatusStatVO {
  taskStatus: number
  taskStatusName: string
  total: number
}

export interface MineMaintenancePriorityStatVO {
  priority: number
  priorityName: string
  total: number
}

export interface MineMaintenanceRiskLevelStatVO {
  riskLevel: number
  riskLevelName: string
  total: number
}

export interface MineMaintenanceRiskTrendVO {
  statDate: string
  alarmTotal: number
  workOrderTotal: number
  maintenanceTaskTotal: number
  highRiskTaskTotal: number
}

export interface MineMaintenanceRecentTaskVO {
  id: number
  taskCode: string
  deviceCode: string
  deviceName: string
  deviceType: string
  areaName: string
  healthScore: number
  riskLevel: number
  riskLevelName: string
  taskStatus: number
  taskStatusName: string
  priority: number
  priorityName: string
  createTime: string
}

export interface MineMaintenanceHighRiskDeviceVO {
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
  taskStatus: number
  taskStatusName: string
  priority: number
  priorityName: string
  createTime: string
}

const defaultSummary: MineMaintenanceDashboardSummaryVO = {
  taskTotal: 0,
  unclosedTaskTotal: 0,
  pendingTotal: 0,
  plannedTotal: 0,
  processingTotal: 0,
  closedTotal: 0,
  highRiskTaskTotal: 0,
  urgentTotal: 0,
  todayNewTaskTotal: 0,
  alarmTotal7d: 0,
  workOrderTotal7d: 0
}

function isObject(value: unknown): value is Record<string, any> {
  return typeof value === 'object' && value !== null
}

function unwrapApiResult<T>(response: unknown, fallback: T): T {
  if (response === null || response === undefined) {
    return fallback
  }

  if (!isObject(response)) {
    return response as T
  }

  // 情况 1：AxiosResponse<ApiResult<T>>
  if (isObject(response.data) && 'code' in response.data && 'data' in response.data) {
    return (response.data.data ?? fallback) as T
  }

  // 情况 2：ApiResult<T>
  if ('code' in response && 'data' in response) {
    return (response.data ?? fallback) as T
  }

  // 情况 3：AxiosResponse<T>
  if ('data' in response) {
    return (response.data ?? fallback) as T
  }

  // 情况 4：已经是 T
  return response as T
}

export async function getMaintenanceDashboardSummaryApi(): Promise<MineMaintenanceDashboardSummaryVO> {
  const response = await request.get('/api/mine/maintenance-dashboard/summary')
  return unwrapApiResult<MineMaintenanceDashboardSummaryVO>(response, defaultSummary)
}

export async function getMaintenanceTaskStatusStatsApi(): Promise<MineMaintenanceTaskStatusStatVO[]> {
  const response = await request.get('/api/mine/maintenance-dashboard/task-status-stats')
  return unwrapApiResult<MineMaintenanceTaskStatusStatVO[]>(response, [])
}

export async function getMaintenancePriorityStatsApi(): Promise<MineMaintenancePriorityStatVO[]> {
  const response = await request.get('/api/mine/maintenance-dashboard/priority-stats')
  return unwrapApiResult<MineMaintenancePriorityStatVO[]>(response, [])
}

export async function getMaintenanceRiskLevelStatsApi(): Promise<MineMaintenanceRiskLevelStatVO[]> {
  const response = await request.get('/api/mine/maintenance-dashboard/risk-level-stats')
  return unwrapApiResult<MineMaintenanceRiskLevelStatVO[]>(response, [])
}

export async function getMaintenanceRiskTrendApi(): Promise<MineMaintenanceRiskTrendVO[]> {
  const response = await request.get('/api/mine/maintenance-dashboard/risk-trend')
  return unwrapApiResult<MineMaintenanceRiskTrendVO[]>(response, [])
}

export async function getMaintenanceRecentTasksApi(): Promise<MineMaintenanceRecentTaskVO[]> {
  const response = await request.get('/api/mine/maintenance-dashboard/recent-tasks')
  return unwrapApiResult<MineMaintenanceRecentTaskVO[]>(response, [])
}

export async function getMaintenanceHighRiskDevicesApi(): Promise<MineMaintenanceHighRiskDeviceVO[]> {
  const response = await request.get('/api/mine/maintenance-dashboard/high-risk-devices')
  return unwrapApiResult<MineMaintenanceHighRiskDeviceVO[]>(response, [])
}