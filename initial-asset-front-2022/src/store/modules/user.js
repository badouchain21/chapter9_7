import { login, logout, getInfo, getUserRoutes } from '@/api/frame/user'
import { getToken, setToken, removeToken } from '@/service/auth'
import { resetRouter } from '@/router'
import storage from '@/utils/storage'
import CryptoUtils from '@/service/crypto-utils.js'

const state = {
    token: getToken(),
    userInfo: null,
    name: '',
    avatar: '',
    // 首页地址
    homeUrl: ''
}

const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token
    },
    SET_USER_INFO: (state, info) => {
        state.userInfo = info
    },
    SET_NAME: (state, name) => {
        state.name = name
    },
    SET_AVATAR: (state, avatar) => {
        state.avatar = avatar
    },
    SET_HOME_URL: (state, url) => {
        state.homeUrl = url
    }
}

const actions = {
    // user login
    login({ commit }, userInfo) {
        return new Promise((resolve, reject) => {
            login({data: CryptoUtils.aesEncrypt(JSON.stringify(userInfo))}).then(res => {
                let message = res ? res.message : ''
                // TODO: token逻辑在前端没有使用,使用的是后台直接操作cookies
                commit('SET_TOKEN', message)
                setToken(message)
                let result = {
                    status: res.result,
                    message: res.tip || '登录异常'
                }
                resolve(result)
            }).catch(error => {
                reject(error)
            })
        })
    },

    // get user info
    getInfo({ commit, state }) {
        return new Promise((resolve, reject) => {
            getInfo(state.token).then(res => {
                if (!res) {
                    reject('获取用户信息失败, 请重新登录')
                }
                commit('SET_USER_INFO', res)
                const { name, headImg, indexUrl:homeUrl } = res
                // 设置姓名
                commit('SET_NAME', name)
                // 设置头像
                commit('SET_AVATAR', headImg)
                // 设置主页面
                commit('SET_HOME_URL', homeUrl || '/admin/overview')
                resolve(res)
            }).catch(error => {
                reject(error)
            })
        })
    },

    setUserInfo({ commit, state }, info) {
        return new Promise((resolve, reject) => {
            commit('SET_USER_INFO', info)
            const { name, headImg } = info
            // 设置姓名
            commit('SET_NAME', name)
            // 设置头像
            commit('SET_AVATAR', headImg)
            resolve()
        })
    },

    // user logout
    logout({ state, dispatch, commit, getters, rootGetters }) {
        return new Promise((resolve, reject) => {
            logout(state.token).then(() => {
                commit('SET_TOKEN', '')
                // 路由守卫那边是根据这个name值来判断是否需要请求用户数据,退出时,应该清空,确保下次进入重新请求
                commit('SET_NAME', '')
                removeToken()
                resetRouter()
                // 注销清除session数据
                storage.removeItem('routesActiveIndex')
                storage.removeItem('sidebarStatus')
                // 设置活跃状态为0
                commit('permission/CLEAR_ROUTES_ACTIVE', null, { root: true })
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    // remove token
    resetToken({ commit }) {
        return new Promise(resolve => {
            commit('SET_TOKEN', '')
            removeToken()
            resolve()
        })
    },

    // 获取用户权限路由菜单数据
    getUserRoutes ({ commit }) {
        return new Promise( resolve => {
            getUserRoutes().then( response => {
                resolve(response)
            })
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}

