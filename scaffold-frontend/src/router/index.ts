import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/login/LoginView.vue')
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/DashboardView.vue')
    },
    {
        path: '/mine/dashboard',
        name: 'MineDashboard',
        component: () => import('../views/mine/MineDashboardView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/mine/device-health',
        name: 'MineDeviceHealth',
        component: () => import('../views/mine/MineDeviceHealthView.vue')
    }

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router