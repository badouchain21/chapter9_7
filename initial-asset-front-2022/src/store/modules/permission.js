import MakeRouteAndMenu from '@/service/make-route'
import storage from '@/utils/storage'
import Vue from 'vue'
import router from '@/router'

const state = {
    routes: [],
    routesActiveIndex: storage.getItem('routesActiveIndex') ? storage.getItem('routesActiveIndex') : 0
}

const mutations = {
    SET_ROUTES: (state, routes) => {
        routes.forEach(i => {
            Vue.set(i, 'isActive', false)
        })
        state.routes = routes
    },
    SET_ROUTES_ACTIVE: (state, index) => {
        // 设置活跃一级菜单index
        state.routesActiveIndex = index
        // storage存储，避免刷新后选中状态消失
        storage.setItem('routesActiveIndex', index)
        state.routes.forEach(i => {
            Vue.set(i, 'isActive', false)
        })
        Vue.set((state.routes)[index], 'isActive', true)
        if ((state.routes)[index].path) {
            router.push({
                path: (state.routes)[index].path,
                query: (state.routes)[index].query
            })
        }
    }, 
    CLEAR_ROUTES_ACTIVE: (state) => {
        state.routesActiveIndex = 0
    } 
}


const actions = {
    /** 根据菜单树的数据构造出两个模块。1-项目菜单（点击可跳转对应界面）， 2-路由树
     * @param {*} menuTree： 接口菜单树数据
     * @returns
     */
    generateRoutes({ commit }, menuTree) {
        return new Promise(resolve => {
            let { routerList, menuList } = MakeRouteAndMenu(menuTree)
            // 存储菜单树(权限路由)
            commit('SET_ROUTES', menuList)
            resolve(routerList)
        })
    },
    setRoutesActiveIndex({ commit }, index) {
        return new Promise( resolve => {
            commit('SET_ROUTES_ACTIVE', index)
            resolve()
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}