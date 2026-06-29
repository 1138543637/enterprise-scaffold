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

export interface MineSensorDataVO {
  id: number
  sensorId: number
  sensorCode: string
  sensorName: string
  sensorType: string
  deviceId: number
  areaName: string
  location: string
  dataValue: number
  unit: string
  alarmThreshold: number
  alarmFlag: number
  collectTime: string
  status: number
  createTime: string
  remark: string
}

export interface MinePageResult<T> {
  pageNo: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export interface MineSensorDataPageQuery {
  pageNo?: number
  pageSize?: number
  sensorType?: string
  alarmFlag?: number
}

export interface MineMqttBatchSimulateRequest {
  sensorType?: string
  count?: number
  intervalMillis?: number
  remark?: string
}

export interface MineSensorMqttMessage {
  sensorCode: string
  dataValue: number
  collectTime: string
  remark: string
}

export function getMineSensorDataPageApi(
    params: MineSensorDataPageQuery
): Promise<MinePageResult<MineSensorDataVO>> {
  return request.get<any, MinePageResult<MineSensorDataVO>>('/api/mine/sensor-data/page', {
    params
  })
}

export function simulateMineMqttBatchApi(
    data: MineMqttBatchSimulateRequest
): Promise<MineSensorMqttMessage[]> {
  return request.post<any, MineSensorMqttMessage[]>('/api/mine/mqtt/simulate-batch', data)
}