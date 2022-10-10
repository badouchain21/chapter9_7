/* ================模型请求相关接口================ */
import { request } from '@/service/request'
// 模型列表数据
export function standardList (data) {
    return request({
        url: '/jdbc/common/basecommonlist/listModule.do',
        method: 'get',
        params: data
    })
}
// 模型编辑数据
export function standardEdit (data) {
    return request({
        url: '/jdbc/common/basecommonedit/editModule.do',
        method: 'get',
        params: data
    })
}
// 模型查看数据
export function standardView (data) {
    return request({
        url: '/jdbc/common/basecommonedit/viewModule.do',
        method: 'get',
        params: data
    })
} 