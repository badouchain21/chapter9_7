<template>
    <div class="simple-edit">
        <el-dialog
            width="910px"
            :title="editDialogTitle"
            :visible.sync="dialogVisible"
            @opened="handerDialogOpened"
            class="edit-dialog">
            <div class="dialog-form">
                <module-form v-if="editModule" ref="dialogForm" :module="editModule"></module-form>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEditData">保 存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

    import ModuleUtils from '@/js/ModuleUtils'
    import {Dialog} from 'element-ui'
    import {ModuleForm} from '@/components/frame/index'

    export default {
        name: "simple-edit",
        components: {
            ModuleForm,
            [Dialog.name]: Dialog
        },
        props: {
            recordId: {
                type: String
            },
            mdCode: {
                type: String
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
                // 编辑页模型配置
                editModule: null,
                // 是否显示弹窗
                dialogVisible: false,
                // 显示 diolog 后的回调方法，在 showEditDialog 方法中赋值，在 handerDialogOpened 中调用
                afterEditDialogOpen: null,
                showList: false
            }
        },
        methods: {
            async loadModuleEditCfg(mdCode) {
                this.editModule = await ModuleUtils.editModuleCfg(mdCode)
            },
            showDialog(updateId) {
                if (!this.editModule) {
                    this.loadModuleEditCfg(this.moduleLink.module)
                }
                this.dialogVisible = true
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
                this.postFile(`${this.INTERFACE.saveIncludeFile}?mdCode=${this.editModule.code}`, form.dataModel, (isSuccess, res) => {
                    if (res.hasOk) {
                        this.alert('保存成功', 'success')
                        this.dialogVisible = false
                        this.$emit('saveSuccess')
                    } else {
                        this.alert(`保存失败！${res.message}`)
                    }
                }, true)
            },
            handerDialogOpened() {
                this.afterEditDialogOpen()
            }
        },
        watch: {
            recordId () {
                this.$nextTick(() => {
                    this.setDefaultParams()
                    this.$refs.moduleList.refresh()
                })
            },
            mdCode() {
                this.loadModuleEditCfg(this.mdCode)
            }
        }
    }
</script>

<style scoped>
    .edit-dialog >>> .el-dialog__footer {
        text-align: center;
    }
    .simple-edit > div {
        width: 100%;
    }
    .simple-edit >>> .el-dialog__body {
        padding: 0;
    }
    .simple-edit >>> .card {
        border: none;
    }

    .simple-edit >>> .el-dialog__body {
        padding: 0;
    }

    .simple-edit .dialog-form {
        padding: 6px;
    }
</style>
