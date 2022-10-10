/* ================数据字典请求相关接口================ */
import { request } from '@/service/request'
// 获取数据字典
export function getAttach (data) {
    let url = `${process.env.VUE_APP_BASE_API}/attach/action/attachsupport/downloadAttach.do`
    return `${url}?attachId=${data}`
}
// 获取验证码
export function getVerificationCode (data) {
    return request({
        url: '/system/security/logon/createCode.do',
        method: 'post',
        params: data
    })
}
 