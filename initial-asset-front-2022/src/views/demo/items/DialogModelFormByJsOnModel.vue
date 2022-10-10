<template>
    <div class="padding defaultBg">
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------<br/>
        <module-list
            ref="moduleList"
            :module="module"
            :queryParams="queryParams"
            :defaultButtons="defaultButtons">
        </module-list>
    </div>
</template>

<script>
    import { ModuleList } from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'

    let default_btn = [
        {
            id: 'showModelFormByDialog',
            name: '弹出模型表单(-------点击这个超级明显的按钮进行测试--------)',
            icon: 'add',
            click: function () {
                // 定义弹窗唯一标识
                let signId = 'modelFormDialog'
                // 定义ModuleList.vue中this作用域
                let that = this
                // 定义弹窗所需按钮
                let btnList = [
                    { 
                        name: '保存', icon: 'save', click: function () {
                            // 获取模型表单对象，getModelFormObj为框架封装，直接使用即可
                            let formObj = this.getModelFormObj(signId)
                            formObj.validate(function () {
                                alert(`表单对象为：${JSON.stringify(formObj.dataModel)}`)
                            })
                            // that是指代ModuleList.vue中的作用域，可以使用that获取该页面变量
                            // 例如： that.buttons(buttons为ModuleList.vue中变量)
                            // 使用that语法去进行选中值的进一步操作 do something you like
                            
                        } 
                    }, {
                        name: '取消', click: function () {
                            // 关闭弹窗表单
                            this.$dialog.close()
                        }
                    }
                ]
                let routerObj =  { mdCode: 'flwViewDictionary', detailId: 'ff80818167735f230167737b7733005e' }
                this.$dialog.init({
                    // 弹窗内容类型
                    type: 'standerEdit',
                    // 弹窗唯一标识，注意唯一标识与上面出现的函数getModelListObj的参数值保持一致
                    id: signId,
                    // 弹窗内容过多时使用
                    isFixedDialog: true,
                    // 弹窗标题
                    title: '弹出模型表单标题',
                    // 模型编码
                    mdCode: routerObj.mdCode,
                    // 详情数据id
                    detailId: routerObj.detailId,
                    // 弹窗中按钮组
                    handlerList: btnList,
                })
            }
        }
    ]

    export default {
        components: {
            ModuleList,
            Location
        },
        data() {
            return {
                list: [],
                module: {
                    id: '',
                    code: '',
                    name: '',
                    moduleDisplay: [],
                    listJs: '',
                    listJsObj: {},
                    selectorData: [],
                    searchCondition: [],
                },
                queryParams: {},
                defaultButtons: default_btn,
            }
        },
        // 优化逻辑，将List.vue同步修改为支持父属性值传入
        props: {
            // 模型编码code
            mdCode: {
                type: String,
                default: ''
            },
            // TODO 暂时不知这个含义
            symbol: {
                type: String,
                default: 'placeholder'
            },
            // 默认查询参数对象
            query: {
                type: Object,
                default: () => {}
            }
        },
        computed: {
            title() {
                return this.module.name
            },
        },
        methods: {
            async newModuleInfo($router) {
                this.module = await ModuleUtils.listModuleCfg($router.params.mdCode)
                this.setDefaultParams($router)
            },
            setDefaultParams($router) {
                // 重置搜索条件
                this.$refs.moduleList.resetQueryParam()

                // 从列表页js中获取默认查询参数
                if (this.module.listJsObj.defaultParam && this.module.listJsObj.defaultParam.init) {
                    let result = this.module.listJsObj.defaultParam.init.call(this, $router.params.symbol)
                    for (let key in result) {
                        this.$refs.moduleList.updateQueryParam(key, result[key])
                    }
                }
                // 设置url上的默认查询参数
                let queryObj = $router.query
                for (let key in queryObj) {
                    this.$refs.moduleList.updateQueryParam(key, queryObj)
                }
            }
        },
        created() {
            this.$nextTick(() => {
                // 优化：支持从地址栏获取数据渲染页面，同步支持从父组件接收数据渲染页面
                if (!this.mdCode) {
                    // 若不存在mdCode,表示从路由对象中获取相关值
                    this.newModuleInfo(this.$route)
                } else {
                    // 存在mdCode，表示从父组件属性获取值，构造与路由对象类似的数据结构并返回，便于逻辑统一操作
                    let routeObj = {
                        params: {
                            mdCode: this.mdCode,
                            symbol: this.symbol
                        },
                        query: this.query
                    }
                    this.newModuleInfo(routeObj)
                }
            })
            let _this=this
            setTimeout(function(){
                let isView=_this.$route && _this.$route.query.isView
                if(isView==true||isView=="true"){
                    let name=_this.module.name
                    name=name.substring(0,name.length-3)
                    $(".card-body button").hide()
                    $(".bd-times").parent().show()
                }
            },300)
        },
        beforeRouteUpdate(to, from, next) {
            this.newModuleInfo(to)
            next()
        }
    }
</script>
