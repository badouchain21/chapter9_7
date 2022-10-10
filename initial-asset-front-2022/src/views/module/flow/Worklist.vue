<template>
    <div>
        <module-list
            ref="moduleList"
            :module="module"
            :queryParams="queryParams"
            :defaultButtons="defaultButtons"
            :url="url"
            :colsFormatter="colsFormatter"
            >
        </module-list>
    </div>
</template>

<script>
/* 待办已办列表 
待办:module/flow/worklist?mdCode=myRecWlItem&symbol=undo&type=undo
已办:module/flow/worklist?mdCode=myRecWlItem&symbol=done&type=done
*/
    import {ModuleList} from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'

    let default_btn = [
    ]

    export default {
        components: {
            ModuleList
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
                url: '',
                colsFormatter: {}
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
                this.url = this.getUrl()
                this.colsFormatter = this.getColsFormatter()
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
            },
            getUrl() {
                var url = '/process/gtasks/worklist/listUndoByUser.do'
                switch (this.$route.query.type) {
                    case 'undo':
                        url = '/process/gtasks/worklist/listUndoByUser.do'
                        break
                    case 'done':
                        url = '/process/gtasks/worklist/listDoneByUser.do'
                        break
                    case 'unread':
                        break
                    case 'read':
                        break
                }
                return this.BASEURL + url
            },
            getColsFormatter() {
                let f = {
                    title: { 
                        formatter: function(row, column, value, index, vue) {
                            return "<a href='javascript:;'>"+value+"</a>"
                        }, 
                        hander: function(row, value, index, vue) {
                            var url = '/process/gtasks/frontworks/undo.do'
                            switch (vue.$route.query.type) {
                                case 'undo':
                                    url = '/process/gtasks/frontworks/undo.do'
                                    break
                                case 'done':
                                    url = '/process/gtasks/frontworks/done.do'
                                    break
                                case 'unread':
                                    break
                                case 'read':
                                    break
                            }
                            vue.get(vue.BASEURL + url,
                                {worklistItemId: row.id}, (isSuccess, res) => {
                                    if (res && res.hasOk) {
                                        let listPath = vue.$parent.$route.path
                                        vue.$router.push({
                                            path: res.data.url + res.data.boId,
                                            query: {
                                                returnPath: listPath,
                                                worklistItemId: row.id,
                                                workType: vue.$route.query.type
                                            }
                                        })
                                    } else {
                                        this.alert('删除失败', res.errMsg)
                                    }
                                }, true)

                        }
                    }
                } 
                if (this.$route.query.type == 'undo') {
                    f['dealDate'] = false
                }
                return f
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
