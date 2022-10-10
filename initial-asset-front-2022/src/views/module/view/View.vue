<template>
    <div class="defaultBg">
        <module-view
            ref="editElem"
            v-if="hackReset"
            :module="module"
            :defaultButtons="defaultButtons"
            :recordId="recordId"
            :isCoverPage="true">
        </module-view>
    </div>
</template>

<script>
    import {ModuleView} from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'

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
                this.module = await ModuleUtils.viewModuleCfg(mdCode)
                this.recordId = this.$route.params.id
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
                    path: `/module/view/view/${this.module.code}/${newRecordId}`,
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
