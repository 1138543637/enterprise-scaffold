import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const TOKEN_KEY = 'enterprise_scaffold_token'

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
    },
    {
        path: '/aiops/metrics',
        name: 'AiopsMetricData',
        component: () => import('../views/aiops/AiopsMetricDataView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/aiops/alerts',
        name: 'AiopsAlerts',
        component: () => import('../views/aiops/AiopsAlertView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/aiops/work-orders',
        name: 'AiopsWorkOrders',
        component: () => import('../views/aiops/AiopsWorkOrderView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/aiops/root-causes',
        name: 'AiopsRootCauses',
        component: () => import('../views/aiops/AiopsRootCauseView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/aiops/dashboard',
        name: 'AiopsDashboard',
        component: () => import('../views/aiops/AiopsDashboardView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/risk/transactions',
        name: 'RiskTransactions',
        component: () => import('../views/risk/RiskTransactionView.vue'),
        meta: {
            requiresAuth: true,
            title: '银行交易模拟' }
    },
    {
        path: '/risk/rules',
        name: 'RiskRules',
        component: () => import('../views/risk/RiskRuleView.vue'),
        meta: {
            requiresAuth: true,
            title: '风控规则引擎',
        },
    },
    {
        path: '/risk/review-orders',
        name: 'riskReviewOrders',
        component: () => import('../views/risk/RiskReviewOrderView.vue'),
        meta: {
            requiresAuth: true,
            title: '风险人工审核'
        },
    },
    {
        path: '/risk/dashboard',
        name: 'RiskDashboard',
        component: () => import('../views/risk/RiskDashboardView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/datahub/datasources',
        name: 'DatahubDatasources',
        component: () => import('../views/datahub/DatahubDatasourceView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/datahub/metadata',
        name: 'DatahubMetadata',
        component: () => import('../views/datahub/DatahubMetadataView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/datahub/quality',
        name: 'DatahubQuality',
        component: () => import('../views/datahub/DatahubQualityView.vue'),
        meta: {
            requiresAuth: true,
            title: '数据质量检测'
        }
    },
    {
        path: '/datahub/sensitive',
        name: 'DatahubSensitive',
        component: () => import('../views/datahub/DatahubSensitiveView.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/datahub/apis',
        name: 'DatahubApis',
        component: () => import('../views/datahub/DatahubApiPublishView.vue'),
        meta: {
            title: 'API共享发布',
            requiresAuth: true }
    },
    {
        path: '/datahub/dashboard',
        name: 'DatahubDashboard',
        component: () => import('../views/datahub/DatahubDashboardView.vue'),
        meta: {
            title: '数据治理看板' ,
            requiresAuth: true}
    },
    {
        path: '/iam/access-logs',
        name: 'IamAccessLogs',
        component: () => import('../views/iam/IamAccessLogView.vue'),
        meta: {
            title: '接口访问日志',
            requiresAuth: true
        }
    },
    {
        path: '/iam/login-risks',
        name: 'IamLoginRisks',
        component: () => import('../views/iam/IamLoginRiskView.vue'),
        meta: {
            title: '异常登录检测',
            requiresAuth: true }
    }


]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, _from, next) => {
    const token = localStorage.getItem(TOKEN_KEY)

    if (to.meta.requiresAuth && !token) {
        next({
            path: '/login',
            query: {
                redirect: to.fullPath
            }
        })
        return
    }

    if (to.path === '/login' && token) {
        next('/dashboard')
        return
    }

    next()
})

export default router