<template>
    <div class="child-module-edit">
        <module-list
            v-if="recordId !== 'add'"
            ref="moduleList"
            :module="listModule"
            :defaultButtons="defaultButtons">
        </module-list>

        <el-dialog
            width="910px"
            :title="editDialogTitle"
            :visible.sync="editDialogVisible"
            @opened="handerDialogOpened"
            class="edit-dialog">
            <div class="dialog-form">
                <module-form 
                    v-if="editModule" 
                    ref="dialogForm" 
                    :module="editModule"
                    :recordId="recordId">
                </module-form>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEditData">保 存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

    import ModuleUtils from '@/js/ModuleUtils'
    import ModuleList from './ModuleList'
    import {Dialog} from 'element-ui'
    import ModuleForm from './ModuleForm'

    export default {
        name: "child-module-edit",
        components: {
            ModuleList,
            ModuleForm,
            [Dialog.name]: Dialog
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
        computed: {
            editDialogTitle() {
                if (this.moduleLink) {
                    return this.moduleLink.displayName + ' - 编辑'
                }
                return '编辑'
            }
        },
        data() {
            return {
                // 列表页模型配置
                listModule: {},
                // 编辑页模型配置
                editModule: null,
                // 默认按钮
                defaultButtons: default_btn,
                // 控制编辑弹窗是否显示
                editDialogVisible: false,
                // 显示 diolog 后的回调方法，在 showEditDialog 方法中赋值，在 handerDialogOpened 中调用
                afterEditDialogOpen: null,
                showList: false
            }
        },
        methods: {
            async loadModuleListCfg(mdCode) {
                this.listModule = await ModuleUtils.listModuleCfg(mdCode)
                this.setDefaultParams()
            },
            async loadModuleEditCfg(mdCode) {
                this.editModule = await ModuleUtils.editModuleCfg(mdCode)
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
            showEditDialog(updateId) {
                if (!this.editModule) {
                    this.loadModuleEditCfg(this.moduleLink.module)
                }
                this.editDialogVisible = true

                // 清除已有的数据
                if (this.$refs.dialogForm) {
                    this.$refs.dialogForm.clearData()
                }

                this.afterEditDialogOpen = () => {
                    this.$refs.dialogForm.clearData()
                    // 更新
                    if (updateId) {
                        this.$refs.dialogForm.editJSON(updateId)
                    }
                }
            },
            saveEditData() {
                let form = this.$refs.dialogForm
                form.setData(this.moduleLink.relationField, this.recordId);
                this.postFile(`${this.INTERFACE.saveIncludeFile}?mdCode=${this.editModule.code}`, form.dataModel, (isSuccess, res) => {
                    if (res.hasOk) {
                        this.alert('保存成功', 'success')
                        this.$refs.moduleList.refresh()
                        this.editDialogVisible = false
                    } else {
                        this.alert(`保存失败！${res.message}`)
                    }
                }, true)
            },
            handerDialogOpened() {
                this.afterEditDialogOpen()
            }
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

    let default_btn = [
        {
            id: 'add',
            name: '新增',
            icon: 'icon iconfont bd-plus',
            click: function () {
                this.$parent.showEditDialog()
            }
        }, {
            id: 'edit',
            name: '修改',
            icon: 'icon iconfont bd-edit',
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
            icon: 'icon iconfont bd-trash-alt-o',
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
</script>

<style scoped>
    .edit-dialog >>> .el-dialog__footer {
        text-align: center;
    }
    .child-module-edit > div {
        width: 100%;
    }
    .child-module-edit >>> .el-dialog__body {
        padding: 0;
    }
    .child-module-edit >>> .card {
        border: none;
    }

    .child-module-edit >>> .el-dialog__body {
        padding: 0;
    }

    .child-module-edit .dialog-form {
        padding: 6px;
    }
</style>
