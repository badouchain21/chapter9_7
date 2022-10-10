import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import qs from 'qs'
// 操作提醒事件间隔,warnTime时间间隔后允许再次提醒,避免多个接口同时报错带来一系列提醒
const warnTime = 1000
// 提醒窗口是否正在运行 false-当前无活跃提醒窗
let tipRunStatus = false

// 项目中拥有明确接口地址使用,默认不需要带请求地址域名部分
export function request (requestObj, selfConfig = {}) {
    return new Promise((resolve, reject) => {
        requestObj.url = process.env.VUE_APP_BASE_API + requestObj.url
        finalRequest(requestObj, selfConfig).then(res => resolve(res))
    })
}

// 请求地址是接口数据传回,并且传回完整地址(含域名)
export function finalRequest (requestObj, selfConfig = {}) {
    return new Promise((resolve, reject) => {
        let config = {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }
        // 合并默认config与自定义config属性，更新config的值
        Object.assign(config, selfConfig)
        let params = requestObj.params
        if (params) {
            Object.keys(params).filter(key => params[key] === null).forEach(key => params[key] = '')
        }
        axios.interceptors.response.use(
            response => {
                const res = response
                // if the custom code is not 20000, it is judged as an error.
                if (res.status === 200) {
                    return res
                } else {
                    Message({
                        message: res.message || 'Error',
                        type: 'error',
                        duration: 5 * 1000
                    })
                    return Promise.reject(new Error(res.message || 'Error'))
                }
            },
            error => {
                let errorStatus = error.response ? error.response.status : 500
                console.log('err' + error) // for debug
                if (errorStatus === 401) {

                    if (!tipRunStatus) {
                        tipRunStatus = true
                        setTimeout(() => { 
                            tipRunStatus = false
                        }, warnTime)
                        MessageBox.confirm('当前页面已过期失效,你可以继续留在当前页面,或者选择重新登录', '页面过期', {
                            confirmButtonText: '重新登录',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            store.dispatch('user/resetToken').then(() => {
                                location.reload()
                            })
                        })
                    }
                    
                } else if (errorStatus === 400 || errorStatus === 404) {
                    if (!tipRunStatus) {
                        tipRunStatus = true
                        setTimeout(() => { 
                            tipRunStatus = false
                        }, warnTime)
                        MessageBox.alert('请求资源找不到,请联系管理员', '资源缺失', {
                            type: 'error'
                        })
                    }
                } else {
                    if (!tipRunStatus) {
                        tipRunStatus = true
                        setTimeout(() => { 
                            tipRunStatus = false
                        }, warnTime)
                        MessageBox.confirm('服务器状态异常,你可以继续留在当前页面,或者选择重新登录', '异常状态', {
                            confirmButtonText: '重新登录',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            store.dispatch('user/resetToken').then(() => {
                                location.reload()
                            })
                        })
                    }
                }
                return Promise.reject(error)
            }
        )
        if (requestObj.method && requestObj.method.toUpperCase() === 'POST') {
            axios.post(requestObj.url, qs.stringify(params), config).then(res => {
                resolve(res.data)
            })
        } else {
            axios({
                method: 'get',
                url: requestObj.url,
                params: params,
                ...config
            }).then(res => {
                resolve(res.data)
            })
        }
    })
}
