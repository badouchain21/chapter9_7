<template>
    <div class="module-list">
        <!-- TODO:旧版searchbar,即将废弃不推荐使用 -->
        <!-- <search-bar
            v-show="showSelector"
            :use-filter="useFilter"
            :mdCode="module.code"
            :module="module"
            :selectorConfig="selectorConfig"
            @search="search">
        </search-bar> -->

        <!-- searchbar搜索组件 -->
        <m-search
            :data="selectorData"
            @search="searchFun"
            @reset="resetFun"
            @changeSearchPosition="resetTablePosition"
            :selectChange="selectChange">
        </m-search>
        <div class="m-op-list module-list-main" 
            v-if="module.code">
            <card v-if="showBtn" class="m-module-list-btn">
                <el-button
                    v-btnBg="i"
                    v-for='(i, index) in buttons' 
                    :key="index"
                    @click='exeMethod(i)'
                    :loading="i.isLoad !== undefined && i.isLoad">
                    <bd-icon
                        v-if="(i.isLoad === undefined) || (i.isLoad !== undefined && !i.isLoad)"
                        :name="comp_codeFromBtn(i)">
                    </bd-icon>
                    {{ i.name }}
                </el-button>
            </card>
            <!-- v-if="module.id" 存在module时才去加载数据 -->
            <!-- 当模型列表操作按钮组长度为0时，取消table的数据选择框 -->
            <pagelist
                class="m-module-list-panel"
                v-if="module.code && isParamsReady"
                ref='list'
                :parentElClass="parentElClass"
                :title="listTitle"
                :showSelection="buttons.length !== 0 ||isShowSelection"
                :url='getUrl()'
                :columns='columns'
                :queryParams="queryParams"
                :sortname="sortname"
                :sortorder="sortorder">
                <!-- demo: 支持列表项插槽-参考例子
                            selfColumn为字段名 
                            scope.row.selfColumn 值可以用来自定义-->
                <!-- <el-table-column slot="selfColumn" prop="selfColumn" label='操作列' width="120">
                    <div slot-scope="scope">
                        {{scope.row.selfColumn}}
                    </div> 
                </el-table-column> -->
            </pagelist>
        </div>
    </div>
</template>

<script>
    import SearchBar from '@/components/frame/Search/SearchBar'
    import Pagelist from '@/components/frame/List/Pagelist.vue'
    import CustomBusi from '@/components/frame/Module/CustomBusi'
    import GlobalConst from '@/service/global-const'
    import MSearch from '@/components/frame/Common/MSearch/index'
    import { hasValue } from '@/utils/index'
    import { request } from '@/service/request'
    import iconHandler from '@/components/frame/Icon/index.js'

    export default {
        name: 'module-list',
        components: {
            Pagelist,
            SearchBar,
            MSearch
        },
        props: {
            module: {
                type: Object,
                default() {
                    return {
                        id: '',
                        code: '',
                        name: '',
                        moduleDisplay: [],
                        listJs: '',
                        listJsObj: {},
                        selectorData: [],
                        searchCondition: [],
                    }
                }
            },
            useFilter: {
                type: Boolean,
                default: true
            },
            useSelector: {
                type: Boolean,
                default: true
            },
            sortname: {
                type: String,
                default: ''
            },
            sortorder: {
                type: String,
                default: 'desc'
            },
            defaultButtons: {
                type: Array
            },
            url: {
                type: String,
            },
            listTitle: String,
            colsFormatter: {
                type: Object
            },
            // 整个列表页面的高度参照物（样式类形式），当一般页面时为父级，弹窗中的列表支持传入参照物实现高度自适应
            parentElClass: {
                type: String,
            },
            
        },
        data() {
            return {
                optionData: [],  // 搜索栏options数据源集合
                selectorData: [], // 搜索栏数据集合
                queryParams: {},
                searchParam: '',
                buttons: [],
                CustomBusi: CustomBusi,
                diySearch: {},
                // 初始搜索默认值，用于reset时还原值使用
                defaultSearchValue: {},
                // 单选框下拉联动事件array
                selectChange: [],
                // 请求参数是否已准备好，在参数准备好时再触发列表请求，避免多次请求
                isParamsReady: false,
                delDefalutButtons:[],
                isShowSelection: false
            }
        }, 
        computed: {
            columns() {
                let arr = []
                this.module.moduleDisplay.filter(f => !f.isHidden).forEach(f => {
                    let cf = this.getColumnFormatter(f)
                    if (cf === false) return
                    arr.push({
                        prop: f.name,
                        label: f.display,
                        minWidth: f.listWidth,
                        formatter: cf.formatter,
                        c_hander: cf.hander
                    })
                })
                return arr
            },
            btnMethod() {
                let obj = {}
                this.buttons.forEach(btn => obj[btn.id] = btn.click)
                return obj
            },
            selectorConfig() {
                return {selectorData: this.module.selectorData, searchCondition: this.module.searchCondition}
            },
            showSelector() {
                return this.useSelector && this.module.selectorData && this.module.selectorData.length > 0
            },
            showBtn() {
                let arr = this.buttons.filter(b => !b.isHide)
                return arr.length > 0
            },
            /**
             * @description: 匹配通用按钮图标
             * @param {Object} btn 按钮
             */
            comp_codeFromBtn () {
                return function (btn) {
                    return iconHandler.codeFromBtn(btn)
                }
            }
        },
        methods: {
            exeMethod(btnObj) {
                let method = btnObj.id
                this.btnMethod[method].call(this, btnObj)  
            },
            // 重新计算表格高度是否做变更，确保页面排版与表格固定内容区域高度展示
            resetTablePosition () {
                let listRef = this.$refs.list
                let listTableEl = listRef.$refs.listTable
                listRef.adjustTableHeight(listTableEl.$el)
            },
            //自定义的搜索方法
            customSearch(diySearch) {
                // 如果外部JS参数没有配置自定义搜索事件，那么使用默认搜索事件
                if (typeof diySearch === 'undefined') {
                    return
                }
                this.diySearch = diySearch
            },
            // 返回需要显示在页面的按钮，按钮对象状态位isHide不为true
            returnShowBtn (btnList) {
                return btnList.filter(i => !i.isHide)
            },
            randerBtn(newButtons) {
                // 获取默认按钮
                let btns = Object.assign([], this.defaultButtons)
                 // 遍历按钮，delete需要delete的
                for (let nKey in this.delDefalutButtons) {
                    let delBtn = this.delDefalutButtons[nKey]
					        	for (var i = 0; i < btns.length; i++) {
                        if (delBtn.id == btns[i].id) {
                           btns.splice(i,1)
                        }
                    }
                   
                }
                // 如果外部JS参数没有配置按钮，那么按钮为原来的按钮
                if (typeof newButtons === 'undefined') {
                    this.buttons = this.returnShowBtn(btns)
                    return
                }

                // 如果外部JS参数按钮是一个空数组，表示清空按钮，上面复制参数后已经把空数组复制过去，这里直接返回
                if (newButtons.length <= 0) {
                    this.buttons = []
                    return
                }

                // 暂时存储新的按钮
                let arr = []
                // 遍历按钮，替换需要替换的
                for (let nKey in newButtons) {
                    let newBtn = newButtons[nKey]
                    for (let key in btns) {
                        if (newBtn.id == btns[key].id) {
                            btns[key] = newBtn
                            newBtn = null
                            break
                        }
                    }
                    if (newBtn) {
                        arr.push(newBtn)
                    }
                }
          

                // 添加新的按钮
                arr.forEach(b => btns.push(b))
                
                this.buttons = this.returnShowBtn(btns)
            },
            /**
             * @desciption: 根据搜索类型返回搜索关键词
             * @param {Number} type:搜索类型
             * @return: {String} 搜索类型下对应的搜索词
             */
            searchType (type) {
                let typeWord = ''
                switch (type) {
                    // 下拉多选
                    case 0:
                        typeWord = 'other-query'
                        break
                    // 文本框
                    case 1:
                        typeWord = 'text-query'
                        break
                    // 日期选择
                    case 5:
                        typeWord = 'date-query'
                        break
                    // 单选框
                    case 9:
                        typeWord = 'exact-match'
                        break
                    // 默认值-文本框
                    default:
                        typeWord = 'text-query'
                }
                return typeWord
            },
            // 参数字段转换
            formatParams () {
                let params = this.selectorData.filter(i => hasValue(i.value))
                    .map(i => {
                        return {
                            name: i.id,
                            value: i.value,
                            type: this.searchType(i.type)
                        }
                    })
                return params
            },
            // 旧版：即将废弃，新版见searchFun()
            search(searchParam) {
                if(typeof this.diySearch ==='function'){
                    this.diySearch(searchParam)
                }else{
                    this.updateQueryParam('searchParam', JSON.stringify(searchParam))
                    // 通过 setTimeout 让出控制流，使 VUE 更新子组件的 searchParam
                    setTimeout(this.$refs.list.reset)
                }
            },
            searchFun () {
                if (typeof this.diySearch ==='function') {
                    // 使用js中的自定义的search方法
                } else {
                    // 默认搜索方法
                    this.updateQueryParam('searchParam', JSON.stringify(this.formatParams()))
                    // 通过 setTimeout 让出控制流，使 VUE 更新子组件的 searchParam
                    setTimeout(this.$refs.list.reset)
                }
            },
            // 重置参数为初始数据状态，非清空
            resetFun () {
                this.selectorData.forEach((i, index) => {
                    this.$set(i, 'value', i.initValue) 
                })
                this.searchFun()
            },
            refresh() {
                this.$refs.list.refresh()
            },
            // TODO 旧版：即将废弃,新版使用resetFun
            reset() {
                this.$refs.list.reset()
            },
            updateQueryParam(key, value) {
                this.queryParams[key] = value
            },
            removeQueryParam(key) {
                delete this.queryParams[key]
            },
            resetQueryParam() {
                this.queryParams = {}
            },
            getUrl() {
                // 在列表页JS 中定义的 URL
                if (this.module.listJsObj.listDataUrl) {
                    return this.module.listJsObj.listDataUrl.call(this)
                }
                // 父组件传进来的 URL
                if (this.url) {
                    return this.url
                }
                // 默认的 URL
                return `${this.INTERFACE.moduleListData}?mdCode=${this.module.code}`
            },
            /**
             * 获取钻取列的格式化方法
             * @param {string} drillUrl 钻取的url
             * @param {number} drillUrlOpenType 钻取页面的打开方式 1新tab，2新窗口，目前（2019-3-6）还没有tab，为1是在当前窗口打开
             * @param {string} drillPageTitle 钻取页面的title，目前（2019-3-6）还没有什么用
             */
            getDrillFormatter (drillUrl, drillUrlOpenType, drillPageTitle) {
                // 格式化参数
                let formatter = (row, column, cellValue, index, vue) => {
                    if (cellValue) {
                        return `<a class="drill">${cellValue}</a>`
                    }
                    return ''
                }

                // 点击后回调的函数
                let hander = (row, value, index, vue) => {
                    if (!value) {
                        return
                    }

                    // 替换drillUrl 中的变量
                    let reg = /VAR-(.*?)-VAR/g
                    while (reg.exec(drillUrl)) {
                        let key = RegExp.$1 ? RegExp.$1 : RegExp.$2
                        drillUrl =  drillUrl.replace('VAR-' + key + '-VAR', row[key])
                    }

                    // 以 http 开头的不是路由，使用原生的跳转方式
                    if (drillUrl.startsWith('http')) {
                        if (drillUrlOpenType === 2) {
                            window.open(drillUrl)
                        }
                        window.location.href = drillUrl
                        return
                    }

                    // 跳转路由
                    vue.$router.push({path: drillUrl,query:{row:row}})
                }

                return {formatter, hander}
            },
            getImgSingleFormatter() {
                // 格式化参数
                let formatter = (row, column, cellValue, index, vue) => {
                    if (cellValue) {
                        let url = vue.INTERFACE.downloadAttach + '?attachId=' + cellValue
                        return `<img class="list-img" src="${url}" />`
                        // return `<a class="drill">${cellValue}</a>`
                    }
                    return ''
                }

                return {formatter}
            },
            /**
             *  格式化单附件
             *
             */
            getAttachFormatter(field) {
                // 格式化参数
                let formatter = (row, column, cellValue, index, vue) => {
                    if (field.valueFieldText) {
                        return `<a class="drill">${row[field.valueFieldText]}</a>`
                    } 
                    if ((row[field.valueFieldId] && !field.valueFieldText) || cellValue) {
                        return `<a class="drill">下载附件</a>`
                    }
                    return ''
                }
                // 点击后回调的函数
                let hander = (row, value, index, vue) => {
                    if (field.valueFieldId && row[field.valueFieldId]) {
                        window.open(vue.INTERFACE.downloadAttach + '?attachId=' + row[field.valueFieldId])
                        return
                    }
                    if (value) {
                        window.open(vue.INTERFACE.downloadAttach + '?attachId=' + value)
                        return
                    }
                    return
                }

                return {formatter, hander}
            },
            /* 金额格式化 */
            getMoneyFormatter() {
                // 格式化参数
                let formatter = (row, column, cellValue, index, vue) => {
                    if (cellValue) {
                        return vue.toMoney(cellValue)
                    }
                    return ''
                }
                return {formatter}
            },
            /**
             * 返回false时表示不渲染
             * 返回的数据需要符合 {formatter: function(row, column, value, index, vue), hander: function(row, value, index, vue)} 这样的格式。
             * 其中 formatter 为格式化数据的方法，hander 为点击时触发的方法
             */
            getColumnFormatter(field) {
                if (this.module.listJsObj &&
                    this.module.listJsObj.renderColumn &&
                    this.module.listJsObj.renderColumn[field.name] !== undefined) {
                    return this.module.listJsObj.renderColumn[field.name]
                }
                if (this.colsFormatter && this.colsFormatter[field.name] !== undefined) {
                    return this.colsFormatter[field.name]
                }
                if (field.type === 'imgSingle') {
                    return this.getImgSingleFormatter()
                } else if (field.drillUrl) {
                    return this.getDrillFormatter(field.drillUrl, field.drillUrlOpenType, field.drillPageTitle);
                } else if (field.type === 'attach') {
                    return this.getAttachFormatter(field)
                }
                return {}
            },
            /**
             * 添加
             */
            addOptionStatus (list) {
                let _list = list
                _list.forEach(i => {
                    this.$set(i, 'isSelect', false)
                })
                return _list
            },
            /**
             * 设置搜索组件的option值（根据url或者数组）
             */
            setOption (itemObj) {
                let {url, data} = itemObj
                return new Promise((resolve, reject) => {
                    if (data && data.length > 0) {
                        resolve({
                            option: this.addOptionStatus(data),
                            obj: itemObj
                        })
                    } else if (url) {
                        request({
                            url: url,
                            method: 'post',
                            params: {}
                        }).then(res => {
                            resolve({
                                option: this.addOptionStatus(res || []),
                                obj: itemObj
                            })
                        })
                    } else {
                        resolve({
                            option: [],
                            obj: itemObj
                        })
                    }
                })
            },
            /**
             * 设置搜索组件项的值（根据value是否使用函数）
             * @params [Array] option:数据列表项
             * @params [String] method:函数名或者值
             */
            setValue (option, value) {
                let _value = ''
                switch (value) {
                    case 'getFirst':
                        _value = option[0].id || ''
                        break
                    case 'getLast':
                        _value = (option.length > 0 && option[option.length - 1].id) || ''
                        break
                    case 'getCurrentYear':
                        _value = new Date().getFullYear().toString()
                        break
                    case 'getLastYear':
                        _value = (new Date().getFullYear() - 1).toString()
                        break
                    default:
                        _value = value || ''
                }
                return _value
            }
        },
        watch: {
            module: {
                deep: true,
                immediate: true,
                handler: function (newVal) {
                    // 核心模型code必须存在，才可以执行后续逻辑
                    if (!newVal.code) {
                        return
                    }
                    let optionData = JSON.parse(JSON.stringify(newVal.searchCondition))
                    let selectorData = JSON.parse(JSON.stringify(newVal.selectorData))
                    // 定义option承诺列表
                    let optionPromiseList = []
                    selectorData.forEach(i => {
                        optionData.forEach(j => {
                            if (i.id === j.id) {
                                // 合并对象数据，生成新对象
                                let obj = {...i, ...j}
                                // 原有的考虑是将option的逻辑在下拉组件中去实现（使用静态资源或者动态请求）
                                //     问题1： 可能需要额外传入其他参数需要开放属性入口
                                //     问题2： 响应的数据结构可能不一致，包括结果集的结构与数据字典中键名不一致
                                //     问题3： 冗余逻辑，单选与多选的处理逻辑一致，但是各自需要写一份在组件中
                                //     问题4： 初始时允许函数默认值（即支持自定义的默认第一个或者最后一个），
                                //             列表需要拿到这个函数最终值才能请求正确数据，但是option又是在组件
                                //             中获取的话逻辑繁琐了，而且可能随着数据更新重复触发请求列表
                                // --------------------------------------------------------------------------------
                                // 现有做法：不纠结option多种数据来源，组件只设置option字段，并且只接收列表数据
                                //         初始是接口地址的需要先请求获取数据后再传入，这样即使值采用函数方式（例如默认取第一个）表示的也能第一时间获取
                                //         整体search中，需要优先请求所有带url的option（注意是异步请求），获取数据后再进行渲染
                                optionPromiseList.push(this.setOption(obj))
                            }
                        })
                    })
                    // 所有option使用接口地址的进行异步请求数据，并返回生成最新searchbar数据
                    Promise.all(optionPromiseList).then(res => {
                        res.forEach(i => {
                            // 定义并赋值变量
                            let { option, obj } = i
                            // 删除对象下多余属性 isExtended, url, data, selectType，生成新对象itemObj
                            let { isExtended, url, data, selectType,  ...itemObj } = {
                                // 枚举所有属性，后面重复出现的属性将覆盖这里，放置后面的优先级高
                                ...obj,
                                // 字段名
                                id: obj.id,
                                // 可扩展的,决定展示的位置（是否作为右侧更多中的选项状态，这一状态不会受后续逻辑影响，放在更多中就是更多中，无关选中与否）
                                isExtend: !!obj.isExtended,
                                // 已选中展示的；扩展项默认初始不选中
                                isSelect: !obj.isExtended,
                                // 字段文本名称
                                name: obj.name,
                                // 搜索类型
                                type: obj.selectType,
                                // 初始值,重置时将value字段值变更为该字段值
                                initValue: this.setValue(option, obj.value),
                                // 值
                                value: this.setValue(option, obj.value),
                                // searchbar中展示排序字段
                                sortIndex: obj.sortIndex,
                                // 下拉option数据源；不需要数据源的类型字段值为[]
                                option: option
                            }
                            // 添加搜索条件子项
                            this.selectorData.push(itemObj)
                            // 添加搜索子项值，用于reset还原调用
                            // this.defaultSearchValue[obj.id] = obj.value || ''
                        })
                        // console.log('--', JSON.stringify(this.selectorData))
                        // 更新参数
                        this.updateQueryParam('searchParam', JSON.stringify(this.formatParams()))
                        // 更新参数状态完成，列表v-if状态变更true，列表组件显示，内部触发列表请求
                        this.isParamsReady = true
                    })
                    let customBtns
                    let diySearch
                    if (newVal.listJsObj) {
                        customBtns = newVal.listJsObj.buttons
                        diySearch = newVal.listJsObj.diySearch
                        this.selectChange = newVal.listJsObj.selectChange || []
                        this.delDefalutButtons=newVal.listJsObj.delDefalutButtons
                        this.isShowSelection=newVal.listJsObj.isShowSelection || false
                    }
                    // 渲染按钮的事件这里会进来两次，一次是初始，第二次是module异步请求成功后
                    // 第一次触发会使页面首先渲染默认按钮，然后等module异步加载数据后再渲染需要的按钮
                    // 现在需求是等待module加载完再显示按钮，所以第一次不需要渲染，通过module.code判断module加载状态
                    if (newVal.code) {
                        this.randerBtn(customBtns)
                    }
                    this.customSearch(diySearch)
                    if (this.$refs.list)
                        Promise.resolve().then(this.$refs.list.reset)
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .module-list >>> .drill {
        color: #2c6cf6;
        cursor: pointer;
    }
    .module-list >>> .list-img {
        width: 50px
    }
    // ==========================================wjx start
    .module-list {
        height: 100%;
    }
    // ==========================================wjx end
</style>
