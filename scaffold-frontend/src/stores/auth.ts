import { defineStore } from 'pinia'
import { getMeApi, loginApi, type LoginRequest, type LoginUser } from '../api/system/auth'
import { TOKEN_KEY } from '../api/request'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem(TOKEN_KEY) || '',
        user: null as LoginUser | null
    }),

    actions: {
        async login(data: LoginRequest) {
            const result = await loginApi(data)

            this.token = result.data.token
            localStorage.setItem(TOKEN_KEY, this.token)

            await this.loadMe()
        },

        async loadMe() {
            const result = await getMeApi()
            this.user = result.data
        },

        logout() {
            this.token = ''
            this.user = null
            localStorage.removeItem(TOKEN_KEY)
        }
    }
})