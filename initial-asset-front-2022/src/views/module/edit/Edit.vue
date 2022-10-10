<template>
    <div class="defaultBg">
        <module-edit
            v-if="hackReset"
            :module="module"
            :defaultButtons="defaultButtons"
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
                            _this.$parent.close()
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
                    editedJsObj: {}
                },
                defaultButtons: default_btn,
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
            },
            close() {
                let returnPath = this.$route.query.data.returnPath
                this.$router.push({
                    path: returnPath
                })
            },
        },
        created() {
            this.newModuleInfo(this.$route.params.mdCode)
        },
        // 暂时注释，这块是监听同个页面组件被重复使用时触发其更新数据，从目前项目看即使被重复使用依然可以数据更新，暂时不用这个
        // 避免重复请求
        // beforeRouteUpdate(to, from, next) {
        //     let mdCode = to.params.mdCode
        //     this.newModuleInfo(mdCode)
        //     next()
        // }
    }
</script>

<style scoped>

</style>
