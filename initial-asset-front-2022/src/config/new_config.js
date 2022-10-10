// 引入键盘事件编码列表
import keyboardList from '@/service/keyboard-list'
import { finalRequest } from '@/service/request'
import storage from '@/utils/storage'
import returnSrc from '@/service/return-src'
import { MessageBox, Message, Loading } from 'element-ui'
export default {
    install (Vue, options) {
        Vue.prototype.DEFAULT_AVATAR = 'this.src="' + require('../assets/image/frame/avatar.png') + '"'
        // 请求地址域名
        Vue.prototype.BASEURL = process.env.VUE_APP_BASE_API
        /** 监听页面键盘事件
         * @param {*} keyDownObj={'监听键盘事件类型':触发函数} = {'Enter': () => {console.log(123)}}
         * 监听多种类型使用逗号,操作 {'Enter':fun1, 'Space':fun2}
         */
        Vue.prototype.keyDown = function (keyDownObj) {
            document.onkeydown = (e) => {
                // 获取当前点击键盘的编码
                let key = e.keyCode || e.key || e.which
                // 对传入的多个键盘事件类型进行遍历处理
                Object.keys(keyDownObj).forEach(i => {
                    // 根据键盘编码,找到当前需要触发的键盘事件
                    if (Vue.prototype.getValueByKey(i, keyboardList) === key) {
                        // 触发函数 函数名keyDownObj[i],触发则为keyDownObj[i]()
                        keyDownObj[i]()
                    }
                })
            }
        }

        /** 根据键在对象中找到该键对应的值并返回.
         * 例如key为name,obj:{name:'xiao', sex:'man},则函数意义为返回'xiao'
         * @param {*} key 需要查找的键
         * @param {*} obj 对象数据
         * @returns
         */
        Vue.prototype.getValueByKey = function (key, obj) {
            if (key === null || key === undefined || key === '') {
                console.error(`使用getValueByKey函数:请确保键有值,不要使用空值`)
                return false
            }
            if (typeof (key) === 'number') {
                console.error(`键盘事件专用:请使用提供的键盘编码code,不要使用数字,例如13`)
            }
            let signKey = Object.keys(obj).find(i => i === key)
            if (!signKey) {
                console.error(`找不到你要的指令:${key},现有指令为: ${Object.keys(obj).join(' / ')},请检查类型是否写错或者拼写错误`)
                return false
            }
            return obj[signKey]
        }

        /** post请求
         * @param {String} url:请求地址
         * @param {Object} params:请求参数
         * @returns 响应数据
         */
        Vue.prototype.post = function (url, params) {
            return new Promise((resolve, reject) => {
                finalRequest({
                    method: 'post',
                    url: url,
                    params: params
                }).then(res => {
                    resolve(res)
                }).catch(err => {
                    reject(err)
                })
            })
        }
        /** get请求
         * @param {String} url:请求地址
         * @param {Object} params:请求参数
         * @returns 响应数据
         */
        Vue.prototype.get = function (url, params) {
            return new Promise((resolve, reject) => {
                finalRequest({
                    method: 'get',
                    url: url,
                    params: params
                }).then(res => {
                    resolve(res)
                }).catch(err => {
                    reject(err)
                })
            })
        }
        // session操作  --start
        Vue.prototype.setItem = storage.setItem
        Vue.prototype.getItem = storage.getItem
        Vue.prototype.setObj = storage.setObj
        Vue.prototype.getObj = storage.getObj
        Vue.prototype.removeItem = storage.removeItem
        Vue.prototype.clear = storage.clear
        // session操作  --end

        // 弹窗信息提醒 --start
        Vue.prototype.$message = Message
        Vue.prototype.$msgbox = MessageBox
        Vue.prototype.$alert = MessageBox.alert
        Vue.prototype.$confirm = MessageBox.confirm
        Vue.prototype.$prompt = MessageBox.prompt
        // 弹窗信息提醒 --end
        /** 根据图片id,与图片使用类型返回相关地址
         * @param {string} id:图片id
         * @param {string} [type='']:图片使用类型
         * @returns 相关图片引用地址
         */
        Vue.prototype.makeImg = function (id, type = '') {
            return returnSrc(id, type)
        }

        // loading  --start
        Vue.prototype.$new_loading = Loading.service
        Vue.prototype.new_loading = {}
        // 显示loading
        Vue.prototype.showLoading = function () {
            Vue.prototype.new_loading = this.$new_loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            })
        },
        // 关闭loading
        Vue.prototype.closeLoading = function () {
            setTimeout(() => {
                Vue.prototype.new_loading.close()
            }, 800)
        }
        // loading  --end

        // echarts配色
        Vue.prototype.colorScheme = function (index) {
            let colorList = []
            switch (index) {
                // 默认配色
                case 'default':
                    colorList = ['#0473CE', '#F9BD16', '#8A7AFB', '#16C1C4', '#F5CFB5', '#27B78D', '#77ADFF', '#0EE5E8', '#C7B2FC',
                        '#FC7575', '#B357FB']
                    break
                default:
                    // 规范原因 注释一下
            }
            return colorList
        }

        // 获取弹窗中模型列表对象 --start
        Vue.prototype.getModelListObj = function (signId, type = 1) {
            let refObj
            switch (type) {
                case 1:
                    refObj = this.$refs[signId].$refs[signId].$refs['moduleList'].$refs['list']
                    break
                default:
                    // do something
            }
            return refObj
            
        }
        // 获取弹窗中模型列表对象 --end
        // 获取弹窗中模型表单对象 --start
        Vue.prototype.getModelFormObj = function (signId, type = 1) {
            let refObj
            switch (type) {
                case 1:
                    refObj = this.$refs[signId].$refs[signId].$refs['editElem'].$refs['mainForm']
                    break
                default:
                    // do something
            }
            return refObj
            
        }
        // 获取弹窗中模型表单对象 --end

        /**
         * 根据类型获取dialog内容所在页面作用域
         * @param {String} signId js触发弹窗属性id值
         * @param {Number} type 弹窗内容类型：
         *      1-自定义列表
         *      2-自定义表单
         *      3-模型列表
         *      4-模型表单
         * @return 弹窗内容所在页面作用域
         */
        Vue.prototype.getDialogConObj = function (signId, type) {
            let refObj = ''
            switch (type) {
                // 自定义列表-表格
                case 1:
                    refObj = this.$refs[signId].$refs[signId].$refs['mList']
                    break
                // 自定义表单编辑
                case 2:
                    refObj = this.$refs[signId].$refs[signId]
                    break
                // 模型列表
                case 3:
                    refObj = this.$refs[signId].$refs[signId].$refs['moduleList'].$refs['list']
                    break
                // 模型表单
                case 4:
                    refObj = this.$refs[signId].$refs[signId].$refs['editElem'].$refs['mainForm']
                    break
                default:
                    // do something
                    console.error('无效弹窗类型，请传入弹窗内容类型')
            }
            return refObj
        }
    }
}