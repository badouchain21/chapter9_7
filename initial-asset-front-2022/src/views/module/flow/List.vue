<template>
    <div>
        <module-list
            ref="moduleList"
            :module="module"
            :queryParams="queryParams"
            :defaultButtons="defaultButtons"
            >
        </module-list>
    </div>
</template>

<script>
/* 业务数据列表页 */
    import {ModuleList} from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'

    let default_btn = [
        {
            id: 'add',
            name: '新增',
            icon: 'add',
            click: function () {
                let listPath = this.$parent.$route.path
                this.$router.push({
                    path: `/module/flow/edit/${this.module.code}/add`,
                    query: {
                        returnPath: listPath
                    }
                })
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

                var row = selection[0];
                if (row.boStatus === 1 || row.boStatus === 2) {
                    this.alert("所选信息已发起，请重新选择！");
                    return 
                } else {
                    let listPath = this.$parent.$route.path
                    this.$router.push({
                        path: `/module/flow/edit/${this.module.code}/${selection[selection.length - 1].id}`,
                        query: {
                            returnPath: listPath,
                            isEdit: true
                        }
                    })
                }
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
                for (var i = 0; i < selection.length; i++) {
                    if (selection[i].boStatus === 1 || selection[i].boStatus === 2) {
                        this.alert('所选信息包含已发起的信息，请重新选择！')
                        return
                    }
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
            Location
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
                defaultButtons: JSON.parse(JSON.stringify(default_btn)),
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
                this.initDefaultButtons()
            },
            initDefaultButtons() {
                this.defaultButtons.forEach(btn => {
                    default_btn.forEach(_btn => {
                        if (btn.id === _btn.id) {
                            btn.click = _btn.click
                        }
                    })
                });
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
            }
        },
        created() {
            this.$nextTick(() => {
                this.newModuleInfo(this.$route)
            })
        },
        beforeRouteUpdate(to, from, next) {
            this.newModuleInfo(to)
            next()
        }
    }
</script>
