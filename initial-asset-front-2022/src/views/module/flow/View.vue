<template>
    <div>
        <module-view
            ref="editElem"
            v-if="hackReset"
            :module="module"
            :flowInfo="flowInfo"
            :defaultButtons="defaultButtons"
            :recordId="recordId"
            :isCoverPage="true">
        </module-view>
        <flowtrace-list v-if="recordId" ref="traceList" :boId="recordId" :usePage="false"></flowtrace-list>
    </div>
</template>

<script>
    import {ModuleView} from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'
    import FlowUtils from '@/js/FlowUtils'
    import FlowTraceList from '@/components/frame/CustomBusi/FlowTraceList'

    let default_btn = [
        {
            id: 'close',
            name: '关闭',
            icon: 'close',
            click: function () {
                this.$router.go(-1)
            }
        }
    ]

    export default {
        components: {
            ModuleView,
            FlowUtils,
            [FlowTraceList.name]: FlowTraceList
        },
        data() {
            return {
                hackReset: true,
                module: {
                    id: '',
                    code: '',
                    name: '',
                    fields: [],
                    dic: {},
                    editedJs: '',
                    editedJsObj: {},
                    childTab: []
                },
                defaultButtons: JSON.parse(JSON.stringify(default_btn)),
                recordId: '',
                flowInfo: null
            }
        },
        methods: {
            async newModuleInfo(mdCode) {
                // 销毁再重新渲染 防止edit之间跳转出错
                this.hackReset = false
                this.$nextTick(() => {
                    this.hackReset = true
                })
                this.module = await ModuleUtils.editModuleCfg(mdCode)
                this.recordId = this.$route.params.id
                this.initDefaultButtons()
                this.flowInfo = {
                    worklistItemId: this.$route.query.worklistItemId,
                    isEdit: this.$route.query.isEdit,
                    workType: this.$route.query.workType,
                    boId: this.$route.params.id
                }
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
            close(force, newRecordId) {
                // 关闭时，返回的路径
                let returnPath = this.$route.query.returnPath
                let workType = this.$route.query.workType || ''
                let closeFun = () => {
                    this.$router.push({
                        path: returnPath,
                        query: {
                            type: workType
                        }
                    })
                }

                // 强制关闭
                if (force) {
                    closeFun()
                    return
                }

                let hasTab = this.$refs.editElem.hasTab()
                // 如果没有 tab 关闭编辑页
                if (!hasTab) {
                    closeFun()
                    return
                }

                // 如果编辑的id，与保存后的id一致，为更新，不需要做任何操作
                if (this.recordId === newRecordId) {
                    return
                }

                // 有tab的时候，带上记录id再次跳转到编辑页
                this.$router.push({
                    path: `/module/flow/view/${this.module.code}/${newRecordId}`,
                    query: {
                        data: {
                            returnPath: returnPath
                        }
                    }
                })
            },
        },
        created() {
            this.newModuleInfo(this.$route.params.mdCode)
        },
        beforeRouteUpdate(to, from, next) {
            let mdCode = to.params.mdCode
            this.newModuleInfo(mdCode)
            next()
        }
    }
</script>

<style scoped>

</style>
