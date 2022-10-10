// 公用路由, 项目初始存在路由
import Layout from '@/layout'

const routes = [
    {
        path: '/login',
        component: () => import(/* webpackChunkName: "login" */ '@/views/default/Login')
    }, {
        path: '/404',
        component: () => import('@/views/default/404')
    },
    {
        path: '/',
        component: Layout,
        redirect: '/admin/overview',
        children: [
            {
                path: 'admin/overview',
                component: () => import(/* webpackChunkName: "base" */ '@/views/default/indexchart')
            }, {
                // 用于测试例子
                path: '/text',
                component: () => import(/* webpackChunkName: "base" */ '@/views/default/Text')
            }, {
                path: '/Personal/UserData',
                component: () => import(/* webpackChunkName: "user" */ '@/views/personal/UserData')
            }, {
                path: '/system/RolePermission/:mdCode/:symbol',
                component: () => import('@/views/system/RolePermission')
            }, {
                path: '/default/iconTool/Base',
                component: () => import('@/views/default/iconTool/Base')
            }, {
                path: '/default/iconTool/Business',
                component: () => import('@/views/default/iconTool/Business')
            }, {
                path: '/personal/UserAvatar',
                component: () => import('@/views/personal/UserAvatar')
            }, {
                path: '/personal/ModifyPwd',
                component: () => import('@/views/personal/ModifyPwd')
            }
        ]
    }, 
    // demo例子路由
    {
        path: '/demo',
        component: Layout,
        children: [
            {
                path: '/',
                component: () => import('@/views/demo/index')
            }, {
                path: 'formJSON',
                component: () => import('@/views/demo/items/FormJSON')
            }, {
                path: 'formJSONSlot',
                component: () => import('@/views/demo/items/FormJSONSlot')
            }, {
                path: 'formStatic',
                component: () => import('@/views/demo/items/FormStatic')
            }, {
                path: 'dialogFormByImport',
                component: () => import('@/views/demo/items/DialogFormByImport')
            }, {
                path: 'dialogFormByJs',
                component: () => import('@/views/demo/items/DialogFormByJs')
            }, {
                path: 'useModelFormOnSelf',
                component: () => import('@/views/demo/items/UseModelFormOnSelf')
            }, {
                path: 'dialogModelFormByJs',
                component: () => import('@/views/demo/items/DialogModelFormByJs')
            }, {
                path: 'dialogModelFormByJsOnModel/:mdCode/:symbol',
                component: () => import('@/views/demo/items/DialogModelFormByJsOnModel')
            },
            // 列表模块
            {
                path: 'list',
                component: () => import('@/views/demo/items/List')
            }, {
                path: 'dialogListByJs',
                component: () => import('@/views/demo/items/DialogListByJs')
            }, {
                path: 'useModelListOnSelf',
                component: () => import('@/views/demo/items/UseModelListOnSelf')
            }, {
                path: 'dialogModelListByJs/',
                component: () => import('@/views/demo/items/DialogModelListByJs')
            }, {
                path: 'dialogModelListByJsOnModel/:mdCode/:symbol',
                component: () => import('@/views/demo/items/DialogModelListByJsOnModel')
            },
            // 查看模块
            {
                path: 'viewStatic',
                component: () => import('@/views/demo/items/ViewStatic')
            }, {
                path: 'viewJSON',
                component: () => import('@/views/demo/items/ViewJSON')
            }, , {
                path: 'dialogViewByJs',
                component: () => import('@/views/demo/items/DialogViewByJs')
            }
        ]
    },
    { 
        name: 'LoadingResource',
        path: '/loadingResource',
        component: () => import('@/views/default/LoadingResource'),
        meta: {
            title: '资源下载'
        }
    }
]

export default routes

 