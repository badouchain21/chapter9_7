<template>
    <div class="padding">
        <el-pagination
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNo"
            :page-sizes="pageSizes"
            :pager-count="pageCount"
            :page-size="pageSize"
            layout="total, sizes, jumper, next, pager, prev"
            :total="total">
        </el-pagination>
    </div>
</template>
<script>
    import GlobalConst from '@/service/global-const'
    export default {
        components: {},
        data () { // 定义页面变量
            return {
                num: 1,
            }
        },
        props: {
            // 当前页码
            pageNo: {
                type: Number,
                default: 1
            },
            // pageSize与pageSizes的使用规则注意
            //     原则上pageSize应该与pageSizes中的某一项相等，当同时出现pageSize与pageSizes,
            //     且pageSize与pageSizes中任意项不相等时，此时使用pageSizes数组中的第一项
            //     作为pageSize的值

            // 单页数据条数
            pageSize: {
                type: Number,
                default: GlobalConst.pagination.size
            },
            // 下拉可选单页数据条数数组 例如：[10, 20, 30, 40],非必须
            pageSizes: {
                type: Array,
                default: GlobalConst.pagination.sizes
            },
            // 页码按钮数目峰值
            pageCount: {
                type: Number,
                default: GlobalConst.pagination.maxBtnCount
            },
            // 总数
            total: {
                type: Number,
                default: 0
            }
        },
        computed: {},
        methods: { // 定义函数
            // 单页数据条数切换函数
            handleSizeChange (val) {
                this.$emit('size-change', val)
            },
            // 页码数切换函数
            handleCurrentChange (val) {
                this.$emit('current-change', val)
            },
            /**
             * 刷新列表
             */
            // refresh() {
            //     // 设置数据请求状态--正在请求
            //     this.loading = true
            //     let params = {
            //         pageNo: this.pagination.pageNo,
            //         perPageSize: this.pagination.perPageSize
            //     }

            //     if (this.queryParams) {
            //         Object.assign(params, this.queryParams)
            //     }

            //     if (this.sortname) {
            //         params.sortname = this.sortname
            //         params.sortorder = this.sortorder
            //     }
            //     finalRequest({
            //         url: this.url,
            //         method: 'post',
            //         params: params
            //     }).then(res => {
            //         this.rows = res.Rows
            //         this.pagination.total = res.Total
            //         /*
            //             * 如果获取到的数据为空，并且当前页面不是第一页时，加载上一页的数据
            //             * 删除当前页所有记录的时候会出现这个情况
            //             */
            //         if (this.rows.length === 0 && this.pagination.pageNo > 1) {
            //             this.pagination.pageNo--
            //             this.refresh()
            //         }
            //         // 设置数据请求状态--请求结束
            //         this.loading = false
            //     }).catch(err => {
            //         // 设置数据请求状态--请求结束
            //         this.loading = false
            //     })
            // },
            // /**
            //  * 重置分页，所选数据的信息
            //  */
            // reset() {
            //     this.pagination.perPageSize = DEFAULT_PER_PAGE_SIZE
            //     this.pagination.pageNo = 1
            //     this.selection = []
            //     this.refresh()
            // },
            // rowClick(row) {
            //     if (this.showSelection) {
            //         this.$refs.listTable.toggleRowSelection(row)
            //     }
            // },
            // formatterColum(row, column, index) {
            //     let value = row[column.property]
            //     if (column.formatter) {
            //         return column.formatter(row, column, value, index, this)
            //     }
            //     return value
            // },
            // handColumnClick(columnDef, row, column, index) {
            //     if (columnDef.c_hander) {
            //         columnDef.c_hander(row, row[column.property], index, this)
            //     }
            // }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {}
    }
</script>
<style lang='scss' scoped>

</style>