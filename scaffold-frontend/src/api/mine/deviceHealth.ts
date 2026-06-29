import request from '../request'

interface ApiResult<T> {
  code: number
  msg: string
  data: T
}

function unwrapApiData<T>(response: T | ApiResult<T>): T {
  const value = response as any

  if (value && typeof value === 'object' && 'code' in value && 'data' in value) {
    return value.data as T
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

export interface MineDeviceHealthPageQuery {
  pageNo?: number
  pageSize?: number
  deviceCode?: string
  deviceName?: string
  deviceType?: string
  areaName?: string
  riskLevel?: number
  status?: number
}

export interface MineDeviceHealthVO {
  id: number
  deviceCode: string
  deviceName: string
  deviceType: string
  areaName: string
  location: string
  healthScore: number
  riskLevel: number
  riskLevelName: string
  alarmCount24h: number
  severeUnhandledAlarmCount: number
  unclosedWorkOrderCount: number
  sensorTotal: number
  offlineSensorCount: number
  lastReportTime: string
  status: number
  createTime: string
  remark: string
}

export interface MineDeviceHealthSummaryVO {
  deviceTotal: number
  healthyTotal: number
  attentionTotal: number
  riskTotal: number
  highRiskTotal: number
  averageHealthScore: number
  severeUnhandledAlarmTotal: number
  unclosedWorkOrderTotal: number
}

export async function getMineDeviceHealthPageApi(
    params: MineDeviceHealthPageQuery
): Promise<MinePageResult<MineDeviceHealthVO>> {
  const response = await request.get<
      any,
      MinePageResult<MineDeviceHealthVO> | ApiResult<MinePageResult<MineDeviceHealthVO>>
  >('/api/mine/device-health/page', {
    params
  })

  return unwrapApiData<MinePageResult<MineDeviceHealthVO>>(response)
}

export async function getMineDeviceHealthSummaryApi(): Promise<MineDeviceHealthSummaryVO> {
  const response = await request.get<
      any,
      MineDeviceHealthSummaryVO | ApiResult<MineDeviceHealthSummaryVO>
  >('/api/mine/device-health/summary')

  return unwrapApiData<MineDeviceHealthSummaryVO>(response)
}