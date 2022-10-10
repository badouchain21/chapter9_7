import { getDicList } from '@/api/frame/dic'
import GlobalConst from '@/service/global-const'

export default function getDic({url, params}) {
    return new Promise((resolve, reject) => {
        if (!(url || params[GlobalConst.dic.codeName])) {
            resolve([])
        } else {
            getDicList(url, params).then(res => {
                if (!(res instanceof Array)) {
                    console.error(`获取到的数据字典格式错误，不为数组，组件需要的格式为[{id:'', text:''}]`)
                    return
                }
                resolve(res)
            }).catch(err => {
                reject(err)
            })
        }
    })
    
}