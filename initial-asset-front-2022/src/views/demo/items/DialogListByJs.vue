<template>
    <div class="padding bg-white">
        js触发打开dialog-列表
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------<br/>
        <el-button type="primary" @click="showDialogList">我是一个没有感情的按钮，通过js逻辑弹出表格，不需要引入dialog跟表格·标签</el-button>
        <!-- <el-button type="primary" @click="showDialogList2">简单js最少参数调用dialog</el-button> -->
    </div>
</template>
<script>
    const listId = 'baseList'
    const listId2 = 'baseList2'
    import router from '@/router'
    export default {
        components: {
        },
        data () { // 定义页面变量
            return {               
                btnList: [
                    { 
                        name: '保存', handler: function () {
                            // getDialogConObj:全局封装，1为指定自定义列表，返回该对象作用域
                            let list = this.getDialogConObj(listId, 1).selection
                            alert(JSON.stringify(list))
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
            showDialogList () {
                
                this.$dialog.init({
                    title: '弹窗标题',
                    isFixedDialog: false,
                    type: 'list',
                    // width: 1200,
                    // height: 300,
                    isSelection: true,
                    id: listId,
                    fieldList: [
                        { name: 'updateTime', label: '更新时间', minWidth: '400'  },
                        { 
                            name: 'name', 
                            label: '名称',
                            iconName: 'save',
                            iconClass: "dangerC",
                            iconStyle: {
                                color: '#000'
                            },
                            formatter: function (row, column, cellValue, index, scope) {
                                return `<span style="color:red">${cellValue}</span>`
                            }, 
                            handler: function (row, column, cellValue, index, scope) {
                                scope.$dialog.close()
                                scope.$router.push({path: '/demo'})
                            
                            }
                        },
                        { name: 'updatorName', label: '更新人' }
                    ],
                    btnMethods: this.btnList,
                    dataList: [
                        { updateTime: '2017-07-02', name: '王不留行1', updatorName: '管理员1' },
                        { updateTime: '2017-07-02', name: '王不留行2', updatorName: '管理员2' },                 	  { updateTime: '2017-07-02', name: '王不留行3', updatorName: '管理员3' },
                        { updateTime: '2017-07-02', name: '王不留行4', updatorName: '管理员4' },
                        { updateTime: '2017-07-02', name: '王不留行1', updatorName: '管理员1' },
                        { updateTime: '2017-07-02', name: '王不留行2', updatorName: '管理员2' },                 	  { updateTime: '2017-07-02', name: '王不留行3', updatorName: '管理员3' },
                        { updateTime: '2017-07-02', name: '王不留行4', updatorName: '管理员4' },
                        { updateTime: '2017-07-02', name: '王不留行1', updatorName: '管理员1' },
                        { updateTime: '2017-07-02', name: '王不留行2', updatorName: '管理员2' },                 	  { updateTime: '2017-07-02', name: '王不留行3', updatorName: '管理员3' },
                        { updateTime: '2017-07-02', name: '王不留行4', updatorName: '管理员4' },
                        { updateTime: '2017-07-02', name: '王不留行1', updatorName: '管理员1' },
                        { updateTime: '2017-07-02', name: '王不留行2', updatorName: '管理员2' },                 	  { updateTime: '2017-07-02', name: '王不留行3', updatorName: '管理员3' },
                        { updateTime: '2017-07-02', name: '王不留行4', updatorName: '管理员4' },
                        { updateTime: '2017-07-02', name: '王不留行2', updatorName: '管理员2' },                 	  { updateTime: '2017-07-02', name: '王不留行3', updatorName: '管理员3' },
                        { updateTime: '2017-07-02', name: '王不留行4', updatorName: '管理员4' },
                        { updateTime: '2017-07-02', name: '王不留行1', updatorName: '管理员1' },
                        { updateTime: '2017-07-02', name: '王不留行2', updatorName: '管理员2' },                 	  { updateTime: '2017-07-02', name: '王不留行3', updatorName: '管理员3' },
                        { updateTime: '2017-07-02', name: '王不留行4', updatorName: '管理员4' },
                        { updateTime: '2017-07-02', name: '王不留行1', updatorName: '管理员1' },
                        { updateTime: '2017-07-02', name: '王不留行2', updatorName: '管理员2' },                 	  { updateTime: '2017-07-02', name: '王不留行3', updatorName: '管理员3' },
                        { updateTime: '2017-07-02', name: '王不留行4', updatorName: '管理员4' },
                        { updateTime: '2017-07-02', name: '王不留行1', updatorName: '管理员1' },
                        { updateTime: '2017-07-02', name: '王不留行2', updatorName: '管理员2' },                 	  { updateTime: '2017-07-02', name: '王不留行3', updatorName: '管理员3' },
                        { updateTime: '2017-07-02', name: '王不留行4', updatorName: '管理员4' },
                    ],
                })

                // this.$dialog.init({
                //     type: 'list',
                //     id: listId,
                //     // width: '80%',
                //     // 弹窗内容过多时使用
                //     // isFixedDialog: true,
                //     isSelection: true,
                //     title: '列表弹窗',
                //     // 操作按钮数组
                //     handlerList: this.btnList,
                //     // tableHeight: '400px',
                //     // 表头请求对象（包括地址与参数）
                //     // fieldRequest: {
                //     //     url: `${this.BASEURL}/project/financebigdata/bibalance/citybibalancelist/columnDisplay.do`,
                //     //     params: { mdCode: 'city_bi_balance' }
                //     // },
                //     // dataList: [
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  },
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  },
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  },
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  },
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  },
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  },
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  },
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  },
                //     //     { updateTime: '2017-07-02', name: '王不留行-瓦力咔咔咔咔1', updatorName: '管理员管理员'  }
                //     // ],
                //     fieldList: [
                //         { name: 'updateTime', label: '更新时间', minWidth: '400'  },
                //         { name: 'updatorName', label: '更新人' }
                //     ],
                //     // // 列表数据请求地址
                //     url: `${this.BASEURL}/jdbc/common/basecommonlist/listJSON.do`,
                //     // 列表数据请求参数
                //     params: {
                //         mdCode: 'commDictionary',
                //         // perPageSize: 4,
                //     },
                //     // 是否一次请求所有数据
                //     // isLoadAll: true,
                //     // 是否展示分页
                //     showPagination: true,
                //     btnMethods: this.btnList
                // })
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