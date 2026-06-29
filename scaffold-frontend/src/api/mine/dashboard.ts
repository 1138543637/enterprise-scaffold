import request from '../request'

interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

function unwrapApiData<T>(response: T | ApiResult<T> | { data: ApiResult<T> } | { data: T }): T {
  const value = response as any

  if (value && typeof value === 'object') {
    if ('code' in value && 'data' in value) {
      return value.data as T
    }

    if (value.data && typeof value.data === 'object' && 'code' in value.data && 'data' in value.data) {
      return value.data.data as T
    }

    if ('data' in value && !Array.isArray(value)) {
      return value.data as T
    }
  }

  return response as T
}

export interface MineDashboardSummaryVO {
  deviceTotal: number
  sensorTotal: number
  sensorDataTotal: number
  alarmRuleTotal: number
  alarmEventTotal: number
  unhandledAlarmTotal: number
  workOrderTotal: number
  pendingWorkOrderTotal: number
  closedWorkOrderTotal: number
}

export interface MineAlarmLevelStatVO {
  alarmLevel: number
  alarmLevelName: string
  total: number
}

export interface MineSensorTypeStatVO {
  sensorType: string
  sensorTypeName: string
  total: number
}

export interface MineWorkOrderStatusStatVO {
  orderStatus: number
  orderStatusName: string
  total: number
}

export interface MineRecentAlarmVO {
  id: number
  eventCode: string
  sensorCode: string
  sensorName: string
  sensorType: string
  areaName: string
  location: string
  dataValue: number
  thresholdValue: number
  alarmLevel: number
  alarmLevelName: string
  handleStatus: number
  handleStatusName: string
  alarmTime: string
}

export interface MineRecentWorkOrderVO {
  id: number
  workOrderCode: string
  eventCode: string
  sensorCode: string
  sensorName: string
  sensorType: string
  areaName: string
  location: string
  alarmLevel: number
  alarmLevelName: string
  orderStatus: number
  orderStatusName: string
  createTime: string
}

export interface MineSensorDataSimulateRequest {
  sensorId?: number
  sensorType?: string
  count?: number
}

export interface MineAlarmGenerateRequest {
  sensorDataId?: number
  sensorType?: string
  limit?: number
}

export async function getMineDashboardSummaryApi(): Promise<MineDashboardSummaryVO> {
  const response = await request.get('/api/mine/dashboard/summary')
  return unwrapApiData<MineDashboardSummaryVO>(response)
}

export async function getMineAlarmLevelStatsApi(): Promise<MineAlarmLevelStatVO[]> {
  const response = await request.get('/api/mine/dashboard/alarm-level-stats')
  return unwrapApiData<MineAlarmLevelStatVO[]>(response)
}

export async function getMineSensorTypeStatsApi(): Promise<MineSensorTypeStatVO[]> {
  const response = await request.get('/api/mine/dashboard/sensor-type-stats')
  return unwrapApiData<MineSensorTypeStatVO[]>(response)
}

export async function getMineWorkOrderStatusStatsApi(): Promise<MineWorkOrderStatusStatVO[]> {
  const response = await request.get('/api/mine/dashboard/work-order-status-stats')
  return unwrapApiData<MineWorkOrderStatusStatVO[]>(response)
}

export async function getMineRecentAlarmsApi(): Promise<MineRecentAlarmVO[]> {
  const response = await request.get('/api/mine/dashboard/recent-alarms')
  return unwrapApiData<MineRecentAlarmVO[]>(response)
}

export async function getMineRecentWorkOrdersApi(): Promise<MineRecentWorkOrderVO[]> {
  const response = await request.get('/api/mine/dashboard/recent-work-orders')
  return unwrapApiData<MineRecentWorkOrderVO[]>(response)
}

export async function simulateMineSensorDataApi(data: MineSensorDataSimulateRequest): Promise<unknown[]> {
  const response = await request.post('/api/mine/sensor-data/simulate', data)
  return unwrapApiData<unknown[]>(response)
}

export async function generateMineAlarmEventsApi(data: MineAlarmGenerateRequest): Promise<unknown[]> {
  const response = await request.post('/api/mine/alarm-events/generate', data)
  return unwrapApiData<unknown[]>(response)
}