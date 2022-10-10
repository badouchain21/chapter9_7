<template>
    <div>
        <module-list
            ref="moduleList"
            :module="module"
            :queryParams="queryParams"
            :defaultButtons="defaultButtons">
        </module-list>

        <simple-edit
            ref="simpleEdit"
            :mdCode="module.code"
            @saveSuccess="refreshList">
        </simple-edit>
    </div>
</template>

<script>
    import {ModuleList} from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'
    import SimpleEdit from './Edit'

    let default_btn = [
        {
            id: 'add',
            name: '新增',
            icon: 'add',
            click: function () {
                let listPath = this.$parent.$route.path
                this.$parent.showEditDialog()
            }
        }, {
            id: 'edit',
            name: '修改',
            icon: 'edit',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('请选择一行！')
                    return
                }
                this.$parent.showEditDialog(selection[selection.length - 1].id)
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
        }
    ]

    export default {
        components: {
            ModuleList,
            SimpleEdit
        },
        data() {
            return {
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
                editDialogVisible: false
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
                for (let key in $router.query) {
                    this.$refs.moduleList.updateQueryParam(key, $router.query[key])
                }
            },
            showEditDialog(id) {
                this.$refs.simpleEdit.showDialog(id)
            },
            refreshList() {
                this.$refs.moduleList.reset()
            }
        },
        created() {
            this.$nextTick(() => {
                this.newModuleInfo(this.$route)
            })
        },
        // 暂时注释，这块是监听同个页面组件被重复使用时触发其更新数据，从目前项目看即使被重复使用依然可以数据更新，暂时不用这个
        // 避免重复请求
        // beforeRouteUpdate(to, from, next) {
        //     this.newModuleInfo(to)
        //     next()
        // }
    }
</script>
