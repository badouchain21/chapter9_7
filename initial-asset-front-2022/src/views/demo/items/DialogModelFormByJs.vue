<template>
    <div class="padding bg-white">
        js触发打开dialog-表单
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------<br/>
        <el-button type="primary" @click="showDialogModelForm">我是一个没有感情的按钮，通过js逻辑弹出模型表单，不需要引入dialog跟form标签</el-button>
    </div>
</template>
<script>
    // import xx from ./xx
    export default {
        components: {},
        data () { // 定义页面变量
            return {
            }
        },
        computed: {},
        methods: { // 定义函数
            showDialogModelForm () {
                // 定义弹窗唯一标识
                let signId = 'helloModelForm'
                // 定义ModuleList.vue中this作用域
                let that = this
                // 定义弹窗所需按钮
                let btnList = [
                    { 
                        name: '保存', isLoad: false, handler: function (btnObj) {
                            btnObj.isLoad = true  // 开启按钮加载状态
                            setTimeout(() => {
                                // getDialogConObj:全局封装，4为指定模型表单，返回该页面作用域
                                let formObj = this.getDialogConObj(signId, 4)
                                formObj.validate(function () {
                                    btnObj.isLoad = false // 重置按钮加载状态
                                    console.log(JSON.stringify(formObj.dataModel))
                                })
                            }, 300)
                            
                        } 
                    }, {
                        name: '取消', handler: function () {
                            // 关闭弹窗表单
                            this.$dialog.close()
                        }
                    }
                ]
                let routerObj =  { mdCode: 'qw_template', detailId: '408176566fbd31d5016fbd3eaaec0017' }
                this.$dialog.init({
                    // 弹窗内容类型
                    type: 'standerEdit',
                    // 弹窗唯一标识，注意唯一标识与上面出现的函数getModelListObj的参数值保持一致
                    id: signId,
                    // 弹窗标题
                    title: '弹出模型表单标题',
                    // 模型编码c
                    mdCode: routerObj.mdCode,
                    // 详情数据id
                    detailId: routerObj.detailId,
                    // 弹窗中按钮组
                    handlerList: btnList,
                })
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {}
    }
</script>
<style lang='scss' scoped>

</style>