/* ================公示平台配置相关接口================ */
import { request } from '@/service/request'

export function getPublicitySetting (params) {
    return request({
        url: `/platform/search/findPublicityInfo`,
        method: 'get',
        params: params
    })
}
