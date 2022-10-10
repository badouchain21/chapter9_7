import dialogVue from './index.vue'
import router from '@/router'
import store from '@/store'

const MDialog = {}
MDialog.install = function (Vue, option) {
    if (typeof window !== 'undefined' && window.Vue) {
        Vue = window.Vue
    }
    // 使用vue.extend的组件无法获取$router,$route,$store，需要在这里塞进去
    const MDialogInstance = Vue.extend({...dialogVue, router, store}) // 创建构造器（子类）
    let dialog
    const initInstance = () => {
        // 创建实例
        dialog = new MDialogInstance()
        // 手动挂载，此时模版渲染为文档之外元素
        let dialogEl = dialog.$mount()
        // 插入文档
        document.body.appendChild(dialogEl.$el)
    }
    const close = () => {
        let vm = dialog.$mount()
        let dialogEl = vm.$el
        document.body.removeChild(dialogEl)
        dialog.$destroy()
        vm = null
    }
    Vue.prototype.$dialog = {
        init (option) {
            return new Promise((resolve, reject) => {
                try {
                    initInstance()
                    if (typeof option === 'object') {
                        // 传递参数中默认添加dialog的触发属性，设置visible值为true
                        option.visible = true
                        // 设置弹窗中内容的参考父级类名，用以计算固定内容高度
                        option.parentElClass = 'el-dialog__body'
                        // 将option各属性拷贝给dialog对象
                        Object.assign(dialog, option, {option: option})
                        resolve(option)
                    }
                } catch (e) {
                    console.log(`启用全局注册弹窗异常：${JSON.stringify(e)}`)
                    reject(e)
                }
            })
            
        },
        close () {
            return new Promise((resolve, reject) => {
                try {
                    // 关闭事件
                    close()
                    resolve()
                } catch (e) {
                    console.log(`关闭全局注册弹窗异常：${JSON.stringify(e)}`)
                    reject(e)
                }
                
            })
            
        }
    }
}
export default MDialog