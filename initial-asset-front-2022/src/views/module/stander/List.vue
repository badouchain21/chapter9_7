<template>
    <div class="padding defaultBg m-module-list">
        <module-list
            :parentElClass="parentElClass"
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
            id: 'add',
            name: '新增',
            icon: 'add',
            click: function () {
                let listPath = this.$parent.$route.path
                // console.log(`============================${this.$parent.$route}`)
                this.$router.push({
                    path: `/module/stander/edit/${this.module.code}/add`,
                    query: {
                        data: {
                            returnPath: listPath
                        }
                    }
                })
            }
        }, {
            id: 'edit',
            name: '修改',
            icon: 'edit',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length !== 1) {
                    this.alert('请选择一行！')
                    return
                }
                let listPath = this.$parent.$route.path
                this.$router.push({
                    path: `/module/stander/edit/${this.module.code}/${selection[selection.length - 1].id}`,
                    query: {
                        data: {
                            returnPath: listPath
                        }
                    }
                })
            }
        }, {
            id: 'delete',
            name: '删除',
            icon: 'delete',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('至少选择一行！')
                    return
                }

                this.$confirm('确定删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = {
                        mdCode: this.module.code,
                        ids: selection.map(e => e.id).join(',')
                    }
                    this.post(`${this.BASEURL}/jdbc/common/basecommondelete/delete.do`, params).then(res => {
                        if (res.hasOk) {
                            this.$refs.list.refresh()
                            this.alert('删除成功', 'success')
                            this.$emit('deleteAfter', selection)
                        } else {
                            this.alert('删除失败', 'error')
                        }
                    })
                })
            }
        },
        {
            id: "view",
            name: '查看',
            icon: 'view',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length !== 1) {
                    this.alert('请选择一行！')
                    return
                }
                let listPath = this.$parent.$route.path
                this.$router.push({
                    path: `/module/view/view/${this.module.code}/${selection[selection.length - 1].id}`,
                    query: {
                        data: {
                            returnPath: listPath
                        }
                    }
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
            },
            // 整个列表页面的高度参照物，当一般页面时为父级，弹窗中的列表支持传入参照物实现高度自适应
            parentElClass: {
                type: String
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
                    this.$refs.moduleList.updateQueryParam(key, queryObj[key])
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
        // 暂时注释，这块是监听同个页面组件被重复使用时触发其更新数据，从目前项目看即使被重复使用依然可以数据更新，暂时不用这个
        // 避免重复请求
        // beforeRouteUpdate(to, from, next) {
        //     this.newModuleInfo(to)
        //     next()
        // }
    }
</script>
