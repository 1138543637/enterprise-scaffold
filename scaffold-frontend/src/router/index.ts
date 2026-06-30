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
        component: () => import('../views/dashboard/DashboardView.vue'),
        meta: {
            requiresAuth: true
        }
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
        component: () => import('../views/mine/MineDeviceHealthView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/mine/maintenance-tasks',
        name: 'MineMaintenanceTask',
        component: () => import('../views/mine/MineMaintenanceTaskView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/mine/maintenance-dashboard',
        name: 'MineMaintenanceDashboard',
        component: () => import('../views/mine/MineMaintenanceDashboardView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/aiops/resources',
        name: 'AiopsResource',
        component: () => import('../views/aiops/AiopsResourceView.vue'),
        meta: {
            requiresAuth: true
        }
    }

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router