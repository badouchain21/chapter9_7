import Vue from 'vue'

/**
 * 获取路由按钮
 * @param {*} worklistItemId 待办ID
 * @param {*} fid 流程ID
 */
function getBusiBtn (worklistItemId, fid) {
    return new Promise(resolve => {
        if (!worklistItemId && !fid) {
            resolve({})
            return
        }

        let url = Vue.prototype.BASEURL + '/bpms/bpmsflowinstance/getRoutes.do'
        Vue.prototype.get(url, {worklistItemId: worklistItemId, fid: fid}, (isSuccess, res) => {
            resolve(res)
        }, true)
    })
}

function getFlowBean (mdCode, id) {
    return new Promise(resolve => {
        if (!mdCode || !id) {
            resolve({})
            return
        }
            let url = Vue.prototype.BASEURL + '/jdbc/common/frontbasemoduleedit/toFlowEdit.do'
        Vue.prototype.get(url, {mdCode: mdCode, id: id}, (isSuccess, res) => {
            resolve(res)
        }, true)
    })
}

export default {
    async getBusiBtn (worklistItemId, fid) {
        let busiBtn = await getBusiBtn(worklistItemId, fid)
        let btn = {}
        btn.flgFullScreen = busiBtn.flgFullScreen
        btn.handleWay = busiBtn.handleWay
        btn.nodeInstanceId = busiBtn.nodeInstanceId
        btn.worklistId = busiBtn.worklistId
        btn.flowId = busiBtn.flowId
        btn.busiType = busiBtn.busiType
        btn.routes = busiBtn.routes
        return btn
    },
    async getFlow (mdCode, id) {
        let flow = await getFlowBean(mdCode, id)
        let flowBean = {}
        flowBean.boId = flow.id
        flowBean.flow = flow.flow
        flowBean.module = flow.module
        return flowBean
    }
}
