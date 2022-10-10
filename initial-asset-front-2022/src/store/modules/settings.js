import defaultSettings from '@/settings'
import { getProjectSetting } from '@/api/frame/systemSetting'
import { getPublicitySetting } from '@/api/business/publicitySetting'

const { showSettings, fixedHeader, sidebarLogo } = defaultSettings
const state = {
    theme: '',
    showSettings: showSettings,
    fixedHeader: fixedHeader,
    sidebarLogo: sidebarLogo,
    hasSettingStatus: false,
    projectSetting: {
        title: '',
        headBgImg: '',
        logoImg: '',
        loginBgImg: '',
        // logo图片
        logo: '',
        // 标签页显示项目主名称
        rootTitle: '',
        // 标签页显示icon
        favicon: '',
        // 是否启用验证码
        isUseVerifyCode: false
    },
    // 面包屑地址
    location: [],
    publicitySetting: {
        title: '',
        background: ''
    }
}

const mutations = {
    CHANGE_SETTING: (state, { key, value }) => {
        if (state.hasOwnProperty(key)) {
            state[key] = value
        }
    },
    SET_PROJECT_SETTING: (state, obj) => {
        state.projectSetting = obj
    },
    SET_SETTING_STATUS: (state, status) => {
        state.hasSettingStatus = status
    },
    SET_LOCATION: (state, value) => {
        let locationList = []
        if (typeof value === 'string') {
            locationList = value.split(',').map(i => {
                return {
                    title: i
                }
            })
            state.location = locationList
        } else {
            state.location = value
        }
    },
    SET_PUBLICITY_SETTING: (state, obj) => {
        state.publicitySetting = obj
    }
}

const actions = {
    changeSetting({ commit }, data) {
        commit('CHANGE_SETTING', data)
    },
    getProjectSetting ({ commit }) {
        return new Promise((resolve, reject) => {
            getProjectSetting({}).then(res => {
                // let textImg = 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577285208258&di=e65f8fb2913d2f0fdbec286f168896ba&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170601%2Fb9f35bff9a4a4d3fafe735e09a45cff0_th.jpg'
                let data = {
                    title: res.systemTitle,
                    headBgImg: res.headBgImg,
                    loginBgImg: res.loginBackground,
                    logoImg: res.loginLogo,
                    // logo图片
                    logo: res.logo,
                    // 标签页显示项目主名称
                    rootTitle: res.rootTitle,
                    // 标签页显示icon
                    favicon: res.favicon,
                    // 是否启用验证码
                    isUseVerifyCode: res.isVerify || false

                }
                commit('SET_SETTING_STATUS', true)
                commit('SET_PROJECT_SETTING', data)
                resolve(res)
            }).catch(err => {
                reject(err)
            })
        })
    },
    getPublicitySetting ({commit}){
        return new Promise((resolve, reject) => {
            getPublicitySetting({}).then(res => {
                let data = {
                    title: res.title,
                    background: res.background,
                }
                commit('SET_PUBLICITY_SETTING', data)
                resolve(res)
            }).catch(err => {
                reject(err)
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

