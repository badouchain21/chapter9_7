/* ================项目系统配置相关接口================ */
import { request } from '@/service/request'
// 获取项目配置数据(比如背景图,主题,项目名称等配置信息)
export function getProjectSetting (params) {
    return request({
        url: '/system/security/logon/findSystemInfo.do',
        method: 'post',
        params: params
    })
}
