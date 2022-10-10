/** 判断是否为数组
 * @param {*} obj:需要判断的值
 * @returns {boolean} true:传入数据为数组类型
 */
function isArray (obj) {
    return Object.prototype.toString.call(obj)== '[object Array]';
}

/** 在firList数组中删除secList数组存在的项,并且返回新的数组
 * @export
 * @param {*} firList 目标数组
 * @param {*} secList 需要删除数组项
 * @returns 不含有secList中项的数组firList
 */
export function filterListWithoutSec (firList, secList) {
    if (!isArray(secList)) {
        if (!isArray(firList)) {
            return []
        }
        return firList
    }
    secList.forEach((i, i_index) => {
        firList.forEach((j, j_index) => {
            if (i === j) {
                firList.splice(j_index, 1)
            }
        })
    })
    return firList
}