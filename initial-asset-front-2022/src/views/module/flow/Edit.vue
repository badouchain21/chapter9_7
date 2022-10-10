<template>
    <div>
        <module-edit
            ref="editElem"
            v-if="hackReset"
            :module="module"
            :flowInfo="flowInfo"
            :defaultButtons="defaultButtons"
            :recordId="recordId"
            :isCoverPage="true">
        </module-edit>
        <flowtrace-list v-if="flowInfo.worklistItemId" ref="traceList" :boId="recordId" :usePage="false"></flowtrace-list>
    </div>
</template>

<script>
/* 可以发起流程和处理待办 */
    import {ModuleEdit} from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'
    import FlowTraceList from '@/components/frame/CustomBusi/FlowTraceList'

    let default_btn = [
        {
            id: 'save',
            name: '保存',
            icon: 'save',
            isLoad: false,
            // btnObj:当前按钮对象
            click: function (btnObj) {
                let _this = this;
                this.$refs.mainForm.validate(function () {
                    // 启用按钮加载状态-兼容4.0需要优先判断该按钮对象
                    if (btnObj && btnObj.isLoad !== undefined) {
                        btnObj.isLoad = true
                    }
                    let url = `${_this.BASEURL}/jdbc/common/basecommonsave/saveIncludeFile.do?mdCode=${_this.module.code}`
                    _this.postFile(url, _this.$refs.mainForm.dataModel, (isSuccess, res) => {
                        if (res.hasOk) {
                            _this.alert('保存成功', 'success')
                            _this.$parent.close(false, res.bean.id)
                        } else {
                            _this.alert(`保存失败！${res.message}`)
                        }
                        // 兼容4.0需要优先判断该按钮对象
                        if (btnObj && btnObj.isLoad !== undefined) {
                            // 请求结束，结束按钮加载状态
                            btnObj.isLoad = false
                        }
                    }, true)
                });
            }
        }, {
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
            ModuleEdit,
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
                dialogVisible: false,
                flowInfo: {}
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

                // 如果编辑的id，与保存后的id一致，为更新，不需要做任何操作
                if (this.recordId === newRecordId) {
                    return
                }

                // 有tab的时候，带上记录id再次跳转到编辑页
                this.$router.push({
                    path: `/module/flow/edit/${this.module.code}/${newRecordId}`,
                    query: {
                        returnPath: returnPath,
                        isEdit: true
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
