<!--

-->
<template>
    <div class="child-module-view">
        <module-list
            v-if="recordId !== 'add'"
            ref="moduleList"
            :module="listModule"
            :defaultButtons="defaultButtons">
        </module-list>
    </div>
</template>

<script>

    import ModuleUtils from '@/js/ModuleUtils'
    import ModuleList from './ModuleList'

    export default {
        name: "child-module-view",
        components: {
            ModuleList
        },
        props: {
            moduleLink: {
                type: Object,
                required: true
            },
            recordId: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                // 列表页模型配置
                listModule: {},
                // 默认按钮
                defaultButtons: default_btn,
                showList: false
            }
        },
        methods: {
            async loadModuleListCfg(mdCode) {
                this.listModule = await ModuleUtils.listModuleCfg(mdCode)
                this.setDefaultParams()
            },
            setDefaultParams() {
                if (!this.$refs.moduleList) {
                    return
                }
                // 从列表页js中获取默认查询参数
                if (this.listModule.listJsObj.defaultParam && this.listModule.listJsObj.defaultParam.init) {
                    let result = this.listModule.listJsObj.defaultParam.init.call(this, 'childTab', {recordId: recordId})
                    for (let key in result) {
                        this.$refs.moduleList.updateQueryParam(key, result[key])
                    }
                    return
                }
                let defaultSearchParam = `[{"name":"${this.moduleLink.relationField}","value":"${this.recordId}","type":"exact-match"}]`
                this.$refs.moduleList.updateQueryParam('defaultSearchParam', defaultSearchParam)
            },
        },
        mounted() {
            this.loadModuleListCfg(this.moduleLink.module)
        },
        watch: {
            recordId () {
                this.$nextTick(() => {
                    this.setDefaultParams()
                    this.$refs.moduleList.refresh()
                })
            }
        }
    }

    let default_btn = []
</script>

<style scoped>
    .edit-dialog >>> .el-dialog__footer {
        text-align: center;
    }
    .child-module-view > div {
        width: 100%;
    }
    .child-module-view >>> .el-dialog__body {
        padding: 0;
    }
    .child-module-view >>> .card {
        border: none;
    }

    .child-module-view >>> .el-dialog__body {
        padding: 0;
    }

    .child-module-view .dialog-form {
        padding: 6px;
    }
</style>
