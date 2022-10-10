import Vue from 'vue'
import {Loading, Message, MessageBox} from 'element-ui'
import axios from 'axios'
import {AddressBook, ImgView, ChooseList, SubmitProcess} from '@/components/frame/index.js'

let development = process.env.NODE_ENV === 'development'
/**
 * UEditor 的文件位置，该配置不能写在 install 里面，否则在初始化 UEditor 文件时
 * 还没有设置 window.UEDITOR_HOME_URL 的值，导致 UEditor文件加载不成功
 */
if (development) {
    // 开发
    window.UEDITOR_HOME_URL = 'http://localhost:8000/static/UEditor/'
    // 系统后台地址 同Vue.prototype.BASEURL
    window.UEDITOR_SERVER_URL = 'http://localhost:8080'
} else {
    // 打包
    // window.UEDITOR_HOME_URL = 'http://localhost:8481/init/dist/static/UEditor/'
    // window.UEDITOR_HOME_URL = 'http://dangjian.gdaee.com.cn/cqdj/static/UEditor/'
    window.UEDITOR_HOME_URL = 'http://imwork.fadsii.cn:29999/cqdj/static/UEditor/'
    // 系统后台地址 同Vue.prototype.BASEURL
    // window.UEDITOR_SERVER_URL = 'http://dangjian.gdaee.com.cn/dj'
    window.UEDITOR_SERVER_URL = 'http://imwork.fadsii.cn:29999/dj'
}

Vue.use(Loading.directive)
export default {
    install (Vue, options) {
        Vue.prototype.BASEURL = process.env.VUE_APP_BASE_API
        // if (development) {
        //         // 开发
        //     //   Vue.prototype.BASEURL = 'http://localhost:8080'
        //     // Vue.prototype.BASEURL = 'http://imwork.fadsii.cn:29999/dj'
        //   Vue.prototype.BASEURL = 'http://192.168.1.230:8705/dj'
        // } else {
        //         // 打包
        //         // Vue.prototype.BASEURL = 'http://localhost:8481/init'
        //   Vue.prototype.BASEURL = 'http://imwork.fadsii.cn:29999/dj'
        //         // Vue.prototype.BASEURL = 'http://cloud.badousoft.com/init'
        //         // Vue.prototype.BASEURL = 'http://dangjian.gdaee.com.cn/dj'
        // }

        /* 访问接口 */
        Vue.prototype.INTERFACE = {
            findSystemInfo: Vue.prototype.BASEURL + '/system/security/logon/findSystemInfo.do',
            downloadImg: Vue.prototype.BASEURL + '/cfg/basedefineedit/downloadImgCache.do?tempId=',
            logon: Vue.prototype.BASEURL + '/system/security/logon/logon.do',
            register: Vue.prototype.BASEURL + '/system/security/userregister/create.do',
            logout: Vue.prototype.BASEURL + '/system/security/logout/logout.do',
            loadBlock: Vue.prototype.BASEURL + '/panel/layout/layoutlist/loadBlock.do',
            barReport: Vue.prototype.BASEURL + '/report/commonreport/barReport.do',
            lineReport: Vue.prototype.BASEURL + '/report/commonreport/lineReport.do',
            pieReport: Vue.prototype.BASEURL + '/report/commonreport/pieReport.do',
            stackAreaReport: Vue.prototype.BASEURL + '/report/commonreport/stackedAreaReport.do',
            radarReport: Vue.prototype.BASEURL + '/report/commonreport/radarReport.do',
            stackBarReport: Vue.prototype.BASEURL + '/report/commonreport/stackBarReport.do',
            packData: Vue.prototype.BASEURL + '/panel/cfg/reportshowtype/reportshowtypeengine/packData.do',
            packJsPath: Vue.prototype.BASEURL + '/panel/cfg/reportshowtype/reportshowtypeengine/packJsPath.do',
            getUrl: Vue.prototype.BASEURL + '/panel/page/pagelist/getUrl.do',
            findBlockSettingsInfo: Vue.prototype.BASEURL + '/panel/layout/layoutlist/findBlockSettingsInfo.do',
            packSettings: Vue.prototype.BASEURL + '/panel/cfg/reportshowtype/reportshowtypeengine/packSettings.do',
            saveBlock: Vue.prototype.BASEURL + '/panel/block/panelblocksave/saveSingleBlock.do',
            deleteBlock: Vue.prototype.BASEURL + '/panel/block/panelblockdelete/delete.do',
            editPanel: Vue.prototype.BASEURL + '/panel/layout/layoutedit/editJSON.do',
            savePanel: Vue.prototype.BASEURL + '/panel/layout/layoutsave/savePanel.do',
            loadFilterField: Vue.prototype.BASEURL + '/panel/filterreport/filterreport/loadFilterField.do',
            findShareArray: Vue.prototype.BASEURL + '/panel/layout/layoutlist/findShareArray.do',
            savePanelPos: Vue.prototype.BASEURL + '/panel/layout/layoutsave/savePanelPos.do',
            gridReport: Vue.prototype.BASEURL + '/report/commonreport/gridReport.do?reportId=',
            userLayouts: Vue.prototype.BASEURL + '/panel/userlayout/userlayoutlist/userLayouts.do',
            saveMultiImg: Vue.prototype.BASEURL + '/panel/block/panelblockattach/saveMultiImg.do',
            downloadFile: Vue.prototype.BASEURL + '/attach/action/attach/downloadFile.do?id=',

            userMenu: Vue.prototype.BASEURL + '/system/security/resource/userResource.do',
            updatePwd: Vue.prototype.BASEURL + '/system/security/logon/updatePassword.do',
            userInfo: Vue.prototype.BASEURL + '/system/security/logon/userInfo.do',
            downloadAttach: Vue.prototype.BASEURL + '/attach/action/attachsupport/downloadAttach.do',
            moduleListConfig: Vue.prototype.BASEURL + '/jdbc/common/basecommonlist/listModule.do',        // 获取模型列表数据的接口
            moduleListData: Vue.prototype.BASEURL + '/jdbc/common/basecommonlist/listJSON.do',              // 获取模型列表数据的接口
            moduleEditConfig: Vue.prototype.BASEURL + '/jdbc/common/basecommonedit/editModule.do',        // 获取模型列表数据的接口
            moduleViewConfig: Vue.prototype.BASEURL + '/jdbc/common/basecommonedit/viewModule.do',        // 获取查看模型数据的接口
            saveIncludeFile: Vue.prototype.BASEURL + '/jdbc/common/basecommonsave/saveIncludeFile.do',        // 模型保存数据的接口
            validUnique: Vue.prototype.BASEURL + '/jdbc/common/basecommonedit/validUnique.do',        // 验证数据是否唯一的接口
            bigImgInfo: Vue.prototype.BASEURL + '/attach/action/attachsupport/bigImg.do',        // 获取大图的接口  
            allApiUrl: Vue.prototype.BASEURL + '/api/template/template/show?id=',   // 接口页面的接口
        },
        /* 默认图片 */
        Vue.prototype.DEFAULT = 'this.src=""'
        Vue.prototype.$loading = Loading.service
        Vue.prototype.wrongTip = '请求数据异常,请稍后重试'
        Vue.prototype.tokenFalseTip = '会话失效,请重新登录'
        Vue.prototype.$message = Message
        Vue.prototype.$confirm = MessageBox.confirm
        Vue.prototype.$msgbox = MessageBox
        Vue.prototype.$alert = MessageBox.alert
        Vue.prototype.$prompt = MessageBox.prompt
        Vue.prototype.axios = axios
        Vue.prototype.addressBook = AddressBook
        Vue.prototype.imgView = ImgView
        Vue.prototype.chooseList = ChooseList
        Vue.prototype.submitProcess = SubmitProcess
        /* 静态变量 */
        Vue.prototype.BASEDATA = {
            /* 单页请求数-large */
            perPageSize_large: 15,
            /* 单页请求数-middle */
            perPageSize: 10,
            /* 单页请求数-small */
            perPageSize_small: 5,
            /* 最高一级父组织id */
            parent_org_id: 'ROOT'
        },
        /* 关闭loading */
        Vue.prototype.loading = {}
        Vue.prototype.loadingClose = function () {
            setTimeout(() => {
                try {
                    if (Vue.prototype.loading.close) {
                        Vue.prototype.loading.close()
                    }
                } catch (e) {
                    console.error(e)
                }
            }, 1000)
        }
        /**
         * 提交文件的post请求
         */
        Vue.prototype.postFile = function (url, params, callback, isloading = false) {
            if (!isloading) {
                Vue.prototype.loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
            }
            let formData = new FormData()
            Object.keys(params).forEach(key => {
                if (params[key] instanceof Array) {
                    params[key].forEach(e => formData.append(key, e)) // 用于处理附件上传,数组形式传过来,然后拆分成多个
                } else {
                    formData.append(key, params[key])
                }
            })
            let config = {
                withCredentials: process.env.NODE_ENV === 'development',
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }
            axios.post(url, formData, config).then(res => {
                callback(true, res.data)
            }).catch(error => {
                Vue.prototype.loadingClose()
            })
        },
        /* 封装警告信息 */
        Vue.prototype.alert = function (message, type) {
            if ((type === null) || (type === '') || (type === undefined)) {
                type = 'warning'
            }
            this.$message({
                message: message,
                type: type,
                center: true
            })
        },
        /* 根据子元素对象删除数组中数据 */
        Vue.prototype.remove = function (obj, item) {
            let index = obj.indexOf(item)
            if (index > -1) {
                obj.splice(index, 1)
            }
        }
        /* 表单清除空格 */
        Vue.prototype.trim = function (str) {
            return str.replace(/(^\s+)|(\s+$)/g, '')
        },
        /* 检测表单数据是否为空 */
        Vue.prototype.isCheckNull = function (str) {
            // str = str.replace(/(^\s+)|(\s+$)/g, "")
            if ((str === null) || (str === undefined) || (str === '')) {
                return false
            }
            return true
        },
        /* 检测11位手机号码格式正误 */
        Vue.prototype.isCheckPhone = function (str) {
            if (!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(str))) {
                return false
            }
            return true
        }
        /* 检测邮箱 */
        Vue.prototype.isCheckEmail = function (str) {
            if (!(/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(str))) {
                return false
            }
            return true
        }

        /* ===============================业务数据处理============================= */
        /* 让列表数据按照某一相同属性重新组装数据模块 */
        /* 参数:
                rule: 规则,按照什么进行划分,并且默认是string或者number,
                old_list: 数据源,默认list数组
                list: 最终数据源,默认list数组
            return:
                list: 最终数据源
        */
        Vue.prototype.listToGroup = function (rule, old_list, list, _other = '') {
            old_list.forEach((i, i_index) => {
                if (i_index === 0) {
                    if (list.length === 0) {
                        /* 数据源为空时直接获取首个 */
                        let item = {ilist: [old_list[0]]}
                        item[rule] = old_list[0][rule]
                        if (_other != '') {
                            item[_other] = old_list[0][_other]
                        }
                        list.push(item)
                    }
                } else {
                    try {
                        list.forEach((j, j_index) => {
                            if (i[rule] === j[rule]) {
                                list[j_index].ilist.push(i)
                                /* 使用try/catch用于跳出foreach循环 */
                                throw 'can stop'
                            }
                            /* 判断当前已经循环结束,并且没有一个与现有哦数据相同的情况,直接新增一个店铺模块数据 */
                            if ((i[rule] != j[rule]) &&
                                    (j_index === (list.length - 1))) {
                                let item = {ilist: [old_list[i_index]]}
                                item[rule] = old_list[i_index][rule]
                                if (_other != '') {
                                    item[_other] = old_list[i_index][_other]
                                }
                                list.push(item)
                            }
                        })
                    } catch (e) {
                        console.error(e)
                    }
                }
            })
            return list
        }
        /* 跳转页面,缓存参数 */
        Vue.prototype.goPage = function (page, params = {}) {
            if (Object.keys(params).length !== 0) {
                for (let key in params) {
                    this.setItem(key, params[key])
                }
                this.$router.push({
                    name: page
                })
            } else {
                this.$router.push({
                    name: page
                })
            }
        }
        /* 返回上级路由 */
        Vue.prototype.goBack = function () {
            let _this = this
            setTimeout(function () {
                _this.$router.go(-1)
            }, 300)
        }
        /* 获取当前日期 1990-12-22 */
        Vue.prototype.getNowTime = function () {
            let nowDate = new Date()
            let year = nowDate.getFullYear()
            let month = nowDate.getMonth() + 1
            if (month < 10) {
                month = '0' + month
            }
            let day = nowDate.getDate()
            if (day < 10) {
                day = '0' + day
            }
            return year + '-' + month + '-' + day
        }
        /* 时间戳转日期 1990-12-22 */
        Vue.prototype.convertDate = function (timestamp) {
            let date = new Date(timestamp)
            let year = date.getFullYear()
            let month = date.getMonth() + 1
            var day = date.getDate()
            if (month < 10) {
                month = '0' + month
            }
            var day = date.getDate()
            if (day < 10) {
                day = '0' + day
            }
            return year + '-' + month + '-' + day
        }
        /* 时间戳转日期 1990年12月22日 */
        Vue.prototype.convertDateCh = function (timestamp) {
            let date = new Date(timestamp)
            let year = date.getFullYear()
            let month = date.getMonth() + 1
            var day = date.getDate()
            if (month < 10) {
                month = '0' + month
            }
            var day = date.getDate()
            if (day < 10) {
                day = '0' + day
            }
            return year + '年' + month + '月' + day + '日'
        }
        /* 时间戳转日期时间 1990-12-22 00:00:00 */
        Vue.prototype.convertDateTime = function (timestamp) {
            let date = new Date(timestamp)
            let year = date.getFullYear()
            let month = date.getMonth() + 1
            if (month < 10) {
                month = '0' + month
            }
            let day = date.getDate()
            if (day < 10) {
                day = '0' + day
            }
            let hour = date.getHours()
            if (hour < 10) {
                hour = '0' + hour
            }
            let minute = date.getMinutes()
            if (minute < 10) {
                minute = '0' + minute
            }
            let second = date.getSeconds()
            if (second < 10) {
                second = '0' + second
            }
            return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second
        }
        /* 时间戳转日期 1990-12-22 */
        Vue.prototype.convertDates = function (timestamp) {
            let date = new Date(timestamp)
            let year = date.getFullYear()
            let month = date.getMonth() + 1
            if (month < 10) {
                month = '0' + month
            }
            let day = date.getDate()
            if (day < 10) {
                day = '0' + day
            }
            return year + '-' + month + '-' + day
        }
        /* 时间戳转日期 1990-12-22 00:00:00 */
        Vue.prototype.convertDatesToString = function (timestamp) {
            let date = new Date(timestamp) // 时间戳为10位需*1000，时间戳为13位的话不需乘1000
            let Y = date.getFullYear() + '-'
            let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
            let D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' '
            let h = (date.getHours() < 10 ? '0' + (date.getHours()) : date.getHours()) + ':'
            let m = (date.getMinutes() < 10 ? '0' + (date.getMinutes()) : date.getMinutes()) + ':'
            let s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds())
            return Y + M + D + h + m + s
        }
        /* 百分比转化为小数 */
        Vue.prototype.toPoint = function (percent) {
            let obj = {
                one: 0,
                other: 0
            }
            let str = percent.replace('%', '')
            str = parseFloat(str)
            obj.one = str
            obj.other = (10000 - str * 100) / 100
            return obj
        }
        /* 转换成金额，千分符 */
        Vue.prototype.toMoney = function (num) {
            if (!num) {
                return ''
            }
            // 将num中的$,去掉，将num变成一个纯粹的数据格式字符串
            num = num.toString().replace(/\$|\,/g, '')
            // 如果num不是数字，则将num置0，并返回
            if (num == '' || isNaN(num)) {
                return ''
            }
            // 如果num是负数，则获取她的符号
            let sign = num.indexOf('-') > 0 ? '-' : ''
            // 如果存在小数点，则获取数字的小数部分
            let cents = num.indexOf('.') > 0 ? num.substr(num.indexOf('.')) : ''
            cents = cents.length > 1 ? cents : ''   // 注意：这里如果是使用change方法不断的调用，小数是输入不了的
            // 获取数字的整数数部分
            num = num.indexOf('.') > 0 ? num.substring(0, (num.indexOf('.'))) : num
            // 如果没有小数点，整数部分不能以0开头
            if (cents == '') {
                if (num.length > 1 && num.substr(0, 1) == '0') {
                    return ''
                }
            }
            // 如果有小数点，且整数的部分的长度大于1，则整数部分不能以0开头
            else {
                if (num.length > 1 && num.substr(0, 1) == '0') {
                    return ''
                }
            }
            // 小数保留两位
            /* if (cents.length > 2) {
                cents = Math.round(cents * 100) / 100
            } */
            // 针对整数部分进行格式化处理，这是此方法的核心，也是稍难理解的一个地方，逆向的来思考或者采用简单的事例来实现就容易多了
            /*
              也可以这样想象，现在有一串数字字符串在你面前，如果让你给他家千分位的逗号的话，你是怎么来思考和操作的?
              字符串长度为0/1/2/3时都不用添加
              字符串长度大于3的时候，从右往左数，有三位字符就加一个逗号，然后继续往前数，直到不到往前数少于三位字符为止
             */
            for (let i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) {
                num = num.substring(0, num.length - (4 * i + 3)) + ',' + num.substring(num.length - (4 * i + 3))
            }
            // 将数据（符号、整数部分、小数部分）整体组合返回
            return (sign + num + (cents + '').substr((cents + '').indexOf('.')))
        },
        // 数字转英语,支持1-25的转换
        Vue.prototype.numberToEnglish = function (num) {
            if (num <= 0 || num > 24 || parseFloat(num).toString() == 'NaN') {
                return null
            }
            return this.BASEDATA.numEnglish[num - 1]
        }
    }
}
