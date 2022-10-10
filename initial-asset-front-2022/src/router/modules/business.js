// 公用路由, 项目初始存在路由
import Layout from '@/layout'
import Api from '@/views/bdc/api/Api.vue'

const routes = [
  {
    path: '/api/:id',
    name: 'Api',
    component: Api
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/views/bdc/publicity/search.vue')
  },
  {
    path: '/platform/show',
    name: 'show',
    component: () => import('@/views/bdc/publicity/show.vue')
  }
]

export default routes

 