import axios, { type AxiosError, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

const TOKEN_KEY = 'enterprise_scaffold_token'

const request = axios.create({
    baseURL: '',
    timeout: 10000
})

request.interceptors.request.use((config) => {
    const token = localStorage.getItem(TOKEN_KEY)

    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }

    return config
})

request.interceptors.response.use(
    (response: AxiosResponse) => {
        const result = response.data

        if (result.code !== 0) {
            if (result.code === 401) {
                localStorage.removeItem(TOKEN_KEY)
            }

            ElMessage.error(result.msg || '请求失败')
            return Promise.reject(new Error(result.msg || '请求失败'))
        }

        return result
    },
    (error: AxiosError) => {
        ElMessage.error('后端服务异常，请检查后端是否启动')
        return Promise.reject(error)
    }
)

export { TOKEN_KEY }
export default request