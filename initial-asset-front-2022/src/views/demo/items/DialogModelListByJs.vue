<template>
    <div class="padding bg-white">
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------<br/>
        <el-button @click="showTable()">测试弹窗表格</el-button>
    </div>
</template>
<script>
    const id = 'modelListId'
    // import xx from ./xx
    export default {
        components: {},
        data () { // 定义页面变量
            return {
                btnList: [
                    { 
                        name: '保存', handler: function () {
                            // getDialogConObj:全局封装，3为指定模型表单，返回该页面作用域
                            let listObj = this.getDialogConObj(id, 3)
                            this.list = listObj.selection
                            alert(`选择的列表数据为：${JSON.stringify(listObj.selection)}`)
                        } 
                    }, {
                        name: '取消', handler: function () {
                            this.$confirm('取消将不保存表单数据, 是否继续?', '提示', {          
                                confirmButtonText: '确定',          
                                cancelButtonText: '取消',          
                                type: 'warning'
                            }).then(() => {          
                                // 关闭弹窗表单
                                this.$dialog.close()
                            }).catch(() => {          
                                // 取消关闭        
                            })
                        }
                    }
                ],
            }
        },
        computed: {},
        methods: { // 定义函数
            showTable () {
                // let id = 'modelListId'
                // let dataObj = { mdCode: 'filter', symbol: 'placeholder', query: {} }
                // let btnList = [
                //     { 
                //         name: '保存', 
                //         handler: function () {
                //             // getDialogConObj:全局封装，3为指定模型表单，返回该页面作用域
                //             let listObj = this.getDialogConObj(id, 3)
                //             this.list = listObj.selection
                //             alert(`选择的列表数据为：${JSON.stringify(listObj.selection)}`)
                //         } 
                //     }, {
                //         name: '取消', 
                //         click: function () {
                //             this.$confirm('取消将不保存表单数据, 是否继续?', '提示', {          
                //                 confirmButtonText: '确定',          
                //                 cancelButtonText: '取消',          
                //                 type: 'warning'
                //             }).then(() => {          
                //                 // 关闭弹窗表单
                //                 this.$dialog.close()
                //             }).catch(() => {          
                //                 // 取消关闭        
                //             })
                //         }
                //     }
                // ]

                // // 调用弹窗
                // this.$dialog.init({
                //     type: 'standerList',
                //     id: id,
                //     title: '表格标题',
                //     mdCode: dataObj.mdCode,
                //     symbol: dataObj.symbol,
                //     query: dataObj.query,
                //     handlerList: btnList
                // })


                let dataObj = { mdCode: 'filter', symbol: 'placeholder', query: {} }
                this.$dialog.init({
                    type: 'standerList',
                    id: id,
                    // 弹窗内容过多时使用
                    isFixedDialog: true,
                    title: '表格标题',
                    mdCode: dataObj.mdCode,
                    symbol: dataObj.symbol,
                    query: dataObj.query,
                    handlerList: this.btnList
                })
            },
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {}
    }
</script>
<style lang='scss' scoped>

</style>