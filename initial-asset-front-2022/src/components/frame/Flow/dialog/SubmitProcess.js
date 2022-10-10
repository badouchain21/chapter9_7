/**
 * 提交流程对话框
 */
import Vue from 'vue'

const Constructor = Vue.extend(require('./SubmitProcess.vue').default)

/**
 * 使用例子
 * // 处理路由事件
            dealRoute (routeId) {
                this.submitProcess({
                    boId: this.flowBean.boId,
                    flowId: this.flowBean.flowId,
                    busiType: this.flowBean.busiType,
                    worklistId: this.flowBean.worklistId,
                    routeId: routeId,
                    isStart: this.$route.query.worklistItemId ? false : true
                }).then((result) => {
                    this.close(true)
                }).catch((err) => {
                    
                });
            }
 */

/**
 * 返回默认的 options 配置
 */
function getDefaultConfig () {
  return {
    title: '提交流程对话框',  // 弹窗标题
    boId: null,             /* 业务实体id */
    flowId: null,           /* 流程id */
    busiType: null,         /* 流程类型 */
    worklistId: null,       /* 待办ID */
    routeId: null,          /* 路由ID */
    isStart: false,         /* 是否为发起流程 */
    opinion: null,
    handler: null,          /* 用于提交的处理人 */
    handlers: null,         /* 后台返回的默认处理人 */
    handlerValue: null,
    handlerText: null,
    parentNodeInstanceId: null,
    myOpinions: [],         /* 我的常用意见 */
    routeSelect: null,
    orgType: null
  }
}

export default function (config) {
  let resolve
  function callback (data) {
    resolve(data)
  }

  let component = new Constructor({
    data: {
      config: Object.assign({}, getDefaultConfig(), config),
      callback: callback
    }
  }).$mount()
  document.querySelector('body').appendChild(component.$el)
  return new Promise(r => resolve = r)
}
