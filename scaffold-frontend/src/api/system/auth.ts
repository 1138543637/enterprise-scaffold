import request from '../request'
import type { ApiResult } from '../../types/api'

export interface LoginRequest {
    username: string
    password: string
}

export interface LoginResponse {
    token: string
    tokenType: string
    expiresIn: number
    userId: number
    username: string
    nickname: string
}

export interface LoginUser {
    userId: number
    username: string
    nickname: string
}

export function loginApi(data: LoginRequest) {
    return request.post<ApiResult<LoginResponse>, ApiResult<LoginResponse>>(
        '/api/auth/login',
        data
    )
}

export function getMeApi() {
    return request.get<ApiResult<LoginUser>, ApiResult<LoginUser>>('/api/auth/me')
}