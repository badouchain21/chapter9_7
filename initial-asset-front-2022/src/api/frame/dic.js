/* ================数据字典请求相关接口================ */
import { request } from '@/service/request'
import GlobalConst from '@/service/global-const'
// 获取数据字典
export function getDicList (url, data) {
    return request({
        url: url || GlobalConst.dic.url,
        method: 'get',
        params: data
    })
}
 