import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const TOKEN_KEY = 'enterprise_scaffold_token'

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/dashboard'
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/login/LoginView.vue'),
        meta: {
            title: '登录'
        }
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/DashboardView.vue'),
        meta: {
            title: '企业级项目演示首页',
            requiresAuth: true
        }
    },

    {
        path: '/mine/dashboard',
        name: 'MineDashboard',
        component: () => import('../views/mine/MineDashboardView.vue'),
        meta: {
            title: '智能矿山综合看板',
            requiresAuth: true
        }
    },
    {
        path: '/mine/device-health',
        name: 'MineDeviceHealth',
        component: () => import('../views/mine/MineDeviceHealthView.vue'),
        meta: {
            title: '设备健康评分',
            requiresAuth: true
        }
    },
    {
        path: '/mine/maintenance-tasks',
        name: 'MineMaintenanceTask',
        component: () => import('../views/mine/MineMaintenanceTaskView.vue'),
        meta: {
            title: '预测性维护任务',
            requiresAuth: true
        }
    },
    {
        path: '/mine/maintenance-dashboard',
        name: 'MineMaintenanceDashboard',
        component: () => import('../views/mine/MineMaintenanceDashboardView.vue'),
        meta: {
            title: '维护看板与风险趋势',
            requiresAuth: true
        }
    },

    {
        path: '/aiops/dashboard',
        name: 'AiopsDashboard',
        component: () => import('../views/aiops/AiopsDashboardView.vue'),
        meta: {
            title: 'AIOps 综合看板',
            requiresAuth: true
        }
    },
    {
        path: '/aiops/resources',
        name: 'AiopsResource',
        component: () => import('../views/aiops/AiopsResourceView.vue'),
        meta: {
            title: '资源管理',
            requiresAuth: true
        }
    },
    {
        path: '/aiops/metrics',
        name: 'AiopsMetricData',
        component: () => import('../views/aiops/AiopsMetricDataView.vue'),
        meta: {
            title: '指标采集',
            requiresAuth: true
        }
    },
    {
        path: '/aiops/alerts',
        name: 'AiopsAlerts',
        component: () => import('../views/aiops/AiopsAlertView.vue'),
        meta: {
            title: '告警中心',
            requiresAuth: true
        }
    },
    {
        path: '/aiops/work-orders',
        name: 'AiopsWorkOrders',
        component: () => import('../views/aiops/AiopsWorkOrderView.vue'),
        meta: {
            title: '运维工单',
            requiresAuth: true
        }
    },
    {
        path: '/aiops/root-causes',
        name: 'AiopsRootCauses',
        component: () => import('../views/aiops/AiopsRootCauseView.vue'),
        meta: {
            title: '根因分析',
            requiresAuth: true
        }
    },

    {
        path: '/risk/dashboard',
        name: 'RiskDashboard',
        component: () => import('../views/risk/RiskDashboardView.vue'),
        meta: {
            title: '银行风控综合看板',
            requiresAuth: true
        }
    },
    {
        path: '/risk/transactions',
        name: 'RiskTransactions',
        component: () => import('../views/risk/RiskTransactionView.vue'),
        meta: {
            title: '银行交易模拟',
            requiresAuth: true
        }
    },
    {
        path: '/risk/rules',
        name: 'RiskRules',
        component: () => import('../views/risk/RiskRuleView.vue'),
        meta: {
            title: '风控规则引擎',
            requiresAuth: true
        }
    },
    {
        path: '/risk/review-orders',
        name: 'RiskReviewOrders',
        component: () => import('../views/risk/RiskReviewOrderView.vue'),
        meta: {
            title: '风险人工审核',
            requiresAuth: true
        }
    },

    {
        path: '/datahub/dashboard',
        name: 'DatahubDashboard',
        component: () => import('../views/datahub/DatahubDashboardView.vue'),
        meta: {
            title: '数据治理综合看板',
            requiresAuth: true
        }
    },
    {
        path: '/datahub/datasources',
        name: 'DatahubDatasources',
        component: () => import('../views/datahub/DatahubDatasourceView.vue'),
        meta: {
            title: '数据源管理',
            requiresAuth: true
        }
    },
    {
        path: '/datahub/metadata',
        name: 'DatahubMetadata',
        component: () => import('../views/datahub/DatahubMetadataView.vue'),
        meta: {
            title: '元数据采集',
            requiresAuth: true
        }
    },
    {
        path: '/datahub/quality',
        name: 'DatahubQuality',
        component: () => import('../views/datahub/DatahubQualityView.vue'),
        meta: {
            title: '数据质量检测',
            requiresAuth: true
        }
    },
    {
        path: '/datahub/sensitive',
        name: 'DatahubSensitive',
        component: () => import('../views/datahub/DatahubSensitiveView.vue'),
        meta: {
            title: '敏感数据识别与脱敏',
            requiresAuth: true
        }
    },
    {
        path: '/datahub/apis',
        name: 'DatahubApis',
        component: () => import('../views/datahub/DatahubApiPublishView.vue'),
        meta: {
            title: 'API 共享发布',
            requiresAuth: true
        }
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
            requiresAuth: true
        }
    },
    {
        path: '/iam/rate-limit-rules',
        name: 'IamRateLimitRules',
        component: () => import('../views/iam/IamRateLimitRuleView.vue'),
        meta: {
            title: '接口限流规则',
            requiresAuth: true
        }
    },
    {
        path: '/iam/security-policies',
        name: 'IamSecurityPolicy',
        component: () => import('../views/iam/IamSecurityPolicyView.vue'),
        meta: {
            title: '安全策略配置',
            requiresAuth: true
        }
    },
    {
        path: '/iam/permission-audits',
        name: 'IamPermissionAudits',
        component: () => import('../views/iam/IamPermissionAuditView.vue'),
        meta: {
            title: '权限审计增强',
            requiresAuth: true
        }
    },
    {
        path: '/iam/security-dashboard',
        name: 'IamSecurityDashboard',
        component: () => import('../views/iam/IamSecurityDashboardView.vue'),
        meta: {
            title: 'IAM 安全看板',
            requiresAuth: true
        }
    },
    {
        path: '/iam/risk-closures',
        name: 'IamRiskClosure',
        component: () => import('../views/iam/IamRiskClosureView.vue'),
        meta: {
            title: 'IAM 风险闭环处理',
            requiresAuth: true
        }
    },

    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        redirect: '/dashboard'
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

    if (to.meta.title) {
        document.title = `${String(to.meta.title)} - Enterprise Scaffold`
    } else {
        document.title = 'Enterprise Scaffold'
    }

    next()
})

export default router