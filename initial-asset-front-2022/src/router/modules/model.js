// 模型路由: 所有模型菜单都应该在这里统一管理起来
/* Layout */
const routes = [
    {
        path: '/',
        component: () => import('@/layout'),
        children: [
            // stander
            {
                path: 'module/stander/list/:mdCode/:symbol',
                component: () => import('@/views/module/stander/List')
            }, {
                path: 'module/stander/edit/:mdCode/:id',
                component: () => import('@/views/module/stander/Edit')
            },
            // tree
            {
                path: 'module/tree/list/:mdCode/:symbol',
                component: () => import('@/views/module/tree/List')
            }, {
                path: 'module/tree/edit/:mdCode/:id',
                component: () => import('@/views/module/tree/Edit')
            }, {
                //没有ROOT的tree列表
                path: 'module/tree/listWithoutROOT/:mdCode/:symbol',
                component: () => import('@/views/module/tree/ListWithoutROOT.vue')
            },
            // dic
            {
                path: 'module/dic/list/:mdCode/:symbol',
                component: () => import('@/views/module/dic/List')
            }, {
                path: 'module/dic/edit/:mdCode/:id',
                component: () => import('@/views/module/dic/Edit')
            },
            // chain
            {
              path: 'module/chain/list/:mdCode/:symbol',
              component: () => import('@/views/module/chain/List')
            }, {
              path: 'module/chain/edit/:mdCode/:id',
              component: () => import('@/views/module/chain/Edit')
            },
            // view
            {
                path: 'module/view/view/:mdCode/:id',
                component: () => import('@/views/module/view/View')
            },
            //edit
            {
                path: 'module/edit/edit/:mdCode/:symbol',
                component: () => import('@/views/module/edit/Edit')
            }
            // 42
        ]
    }
]
export default routes
