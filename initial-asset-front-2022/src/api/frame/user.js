/* ================用户数据权限相关接口================ */
import { request } from '@/service/request'

export function login (data) {
    // data为参数对象
    return request({
        url: '/system/security/logon/logon.do',
        method: 'post',
        params: data
    })
}
// 获取用户基本信息
export function getInfo (data) {
    return request({
        url: '/system/security/logon/userInfo.do',
        method: 'post',
        params: data
    })
}
// 更新用户基本信息
export function updateInfo (data) {
    return request({
        url: '/org/employee/employeesave/updateMyInfo.do',
        method: 'post',
        params: data
    })
}
// 更新用户密码
export function updatePwd (data) {
    return request({
        url: '/system/security/logon/updatePassword.do',
        method: 'post',
        params: data
    })
}
// 获取用户历史头像
export function getUserHistoryAvatarList (data) {
    return request({
        url: '/org/employee/employeeedit/getHistoricalHeadImg.do',
        method: 'post',
        params: data
    })
}

// 获取用户权限菜单
export function getUserRoutes (token) {
    return request({
        url: '/system/security/resource/userResource.do',
        method: 'post',
        params: { token }
    })
}

export function logout () {
  return request({
    url: '/system/security/logout/logout.do',
    method: 'post'
  })
}
