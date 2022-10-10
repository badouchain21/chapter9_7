<template>
    <div class="defaultBg">
        <module-edit
            ref="editElem"
            v-if="hackReset"
            :module="module"
            :defaultButtons="!showBtnStatus? defaultButtons : []"
            :recordId="recordId"
            :isCoverPage="true">
        </module-edit>
    </div>
</template>

<script>
    import { ModuleEdit } from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'

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
                            // 有子表的时候，保持后，设置recordId，不然编辑页子表数据无法正常显示，需要重新进入编辑页才能正常显示
                            _this.$parent.recordId = res.bean.id
                            _this.$router.go(-1)
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
            Location
        },
        props: {
            // 详情数据id，用于获取页面详细数据
            detailId: {
                type: String,
                default: ''
            },
            // 模型编码
            mdCode: {
                type: String,
                default: ''
            }
        },
        computed: {
            // 表单按钮的展示状态，目前用于弹窗中的表单使用，将禁止表单原有表单按钮，交由dialog按钮进行业务操作
            showBtnStatus () {
                return Boolean(this.mdCode)
            }
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
                defaultButtons: default_btn,
                recordId: ''
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
                // 添加this.detailId，同步支持作为组件使用传入属性值
                this.recordId = this.mdCode ? this.detailId : this.$route.params.id
            },
            close(force, newRecordId) {
                // 关闭时，返回的路径
                let returnPath = this.$route.query.data.returnPath
                let closeFun = () => {
                    this.$router.push({
                        path: returnPath
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
                    path: `/module/stander/edit/${this.module.code}/${newRecordId}`,
                    query: {
                        data: {
                            returnPath: returnPath
                        }
                    }
                })
            },
        },
        created() {
            // 添加this.mdCode，同步支持作为组件使用传入属性值
            this.newModuleInfo(this.mdCode || this.$route.params.mdCode)
        },
        mounted(){
            let _this=this
            setTimeout(function(){
                let isView=_this.$route && _this.$route.query.isView
                if(isView==true||isView=="true"){
                    let name=_this.module.name
                    name=name.substring(0,name.length-3)
                    $(".diysave").hide()
                    $(".diysubmit").hide()
                    $(".titleClass").text(name+"查看页")
                    $(".titleShow").text("查看"+name+"记录")
                    $(".showSpan").hide()
                }
            },300)
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
