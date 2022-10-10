<template>
    <div class="p-r">
        <el-table
            v-loading="loading"
            v-setTableHeight="{ parentClassName: parentElClass, loadingEndStatus: loading }"
            ref="listTable"
            class="w-per-100 m-table-list"
            :data="rows"
            show-overflow-tooltip = "true"
            @selection-change="selection = $event"
            @row-click="rowClick">

            <el-table-column
                v-if="showSelection"
                type="selection"
                width="55">
            </el-table-column>
            <el-table-column
                v-if="lineNumber"
                type="index"
                label="序号"
                :width="indexWidth"
                :index="indexMethod">
            </el-table-column>
            <template v-for="column in columns">
                <slot :name="column.prop">
                    <el-table-column 
                        :key="column.name"
                        :min-width="column.minWidth || minWidth"
                        :prop="column.prop"
                        :label="column.label"
                        show-overflow-tooltip
                        :formatter="column.formatter">
                        <template slot-scope="scope" >
                            <div class="text-o-1" @click="handColumnClick(column, scope.row, scope.column, scope.$index)" v-html="formatterColum(scope.row, scope.column, scope.$index)"></div>
                        </template>
                    </el-table-column>
                </slot>
            </template>
        </el-table>
        <div slot="footer" class="padding m-module-list-pagination" v-if="rows.length > 0">
            <el-pagination
                background
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pagination.pageNo"
                :page-sizes="[10, 20, 30, 40]"
                :page-size="pagination.perPageSize"
                :pager-count="pageCount"
                layout="total, sizes, jumper, next, pager, prev"
                :total="total">
            </el-pagination>
        </div>
        <!-- sign：使用了el-table -->
        <div class="s-noData" v-if="rows.length === 0 && !loading">
            <img class="s-noData-img" :src='publicPath + "static/img/noData.png"'/>
            <div class="s-noData-text">{{noDataTip}}</div>
        </div>
    </div>
</template>

<script>
    import { finalRequest } from '@/service/request'
    import {Table, TableColumn, Select, Option} from 'element-ui'
    import GlobalConst from '@/service/global-const'
    import { findParentByClassName, findByClassName } from '@/utils/find-dom'
    import globalStyle from '@/styles/global.scss'
    function setTableHeightDirective (el, binding) {
        let { parentClassName, loadingStatus } = binding.value
        if (loadingStatus) {
            return false
        }
        setTimeout(function () {
            // 定义表格所处在模块的展示高度
            let panelHeight = 0
            // 模块最顶级父亲，高度参照峰值
            let ROOTEl = findParentByClassName(el, parentClassName)
            // 搜索组件dom对象
            let searchbarEl = findByClassName(ROOTEl, 'm-search')
            // 获取操作列表按钮DOM
            let btnEl = findByClassName(ROOTEl, 'm-module-list-btn')
            // 获取分组组件DOM
            let paginationEl = findByClassName(ROOTEl, 'm-module-list-pagination')
            // 获取表格的默认最小高度
            let tableHeight = parseInt(GlobalConst.table.height)
            // 设置展示高度，兼容弹窗出现模型列表获取高度异常，所以需要使用最小高度
            panelHeight = ROOTEl.clientHeight > tableHeight ? 
                ROOTEl.clientHeight : tableHeight
            // 表格header-dom对象
            let tableHeaderEl = el.children[1]
            // // 表格body-dom对象
            let tableBodyEl = el.children[2]
            let borderHeight = 1
            // 设置列表展示高度
            tableBodyEl.style.cssText = `
                height: ${
                    panelHeight - 
                    2 * parseInt(globalStyle.padding) -
                    (searchbarEl.clientHeight || 0) -
                    (btnEl.clientHeight || 0) -
                    (tableHeaderEl.clientHeight || 0) - 
                    (paginationEl.clientHeight || 0) -
                    borderHeight * 2}px;
                overflow: auto
            `
        }, 400)
    }
    export default {
        name: "pagelist",
        components: {
            [Select.name]: Select,
            [Option.name]: Option,
            [Table.name]: Table,
            [TableColumn.name]: TableColumn
        },
        directives: {
            setTableHeight: function (el, binding) {
                setTableHeightDirective(el, binding)
            }
        },
        props: {
            url: {
                type: String,
                required: true
            },
            queryParams: {
                type: Object,
                default() {
                    return {}
                }
            },
            columns: {
                type: Array,
                required: true
            },
            title: {
                type: String
            },
            pageInfo: {
                type: Boolean,
                default: true
            },
            sortname: {
                type: String,
                default: ''
            },
            sortorder: {
                type: String,
                default: 'desc'
            },
            showSelection: {
                type: Boolean,
                default: true
            },
            lineNumber: {
                type: Boolean,
                default: true
            },
            // 整个列表页面的高度参照物，当一般页面时为父级，弹窗中的列表支持传入参照物实现高度自适应
            parentElClass: {
                type: String,
                default: 'm-module-list'
            },
            // 页码按钮数目峰值
            pageCount: {
                type: Number,
                default: GlobalConst.pagination.maxBtnCount
            },
        },
        data() {
            return {
                publicPath: process.env.BASE_URL,
                // 无数据文本提醒
                noDataTip: GlobalConst.searchBar.noDataTip,
                // 序号列宽度
                indexWidth: GlobalConst.table.indexWidth,
                // 表格数据请求数据状态
                loading: false,
                rows: [],
                pagination: {
                    perPageSize: GlobalConst.pagination.size,
                    pageNo: 1,
                    perPageOptions: [10, 25, 50, 100],
                    total: 0
                },
                // 存储选中的数据，在父组件中直接获取即可
                selection: []
            }
        },
        computed: {
            total() {
                return this.pagination.total
            },
            // 获取表格列最小宽度
            minWidth () {
                return GlobalConst.table.minWidth
            }
        },
        methods: {
            // 自定义列序号
            indexMethod (index) {
                return ((this.pagination.pageNo - 1) * this.pagination.perPageSize) + index + 1
            },
            adjustTableHeight (listTableEl) {
                setTableHeightDirective(listTableEl, { value: { parentClassName: this.parentElClass, loadingStatus: false } })
            },
            handleSizeChange (val) {
                this.pagination.perPageSize = val
                this.refresh()
            },
            handleCurrentChange (val) {
                this.pagination.pageNo = val
                this.refresh()
            },
            /**
             * 刷新列表
             */
            refresh() {
                // 设置数据请求状态--正在请求
                this.loading = true
                let params = {
                    pageNo: this.pagination.pageNo,
                    perPageSize: this.pagination.perPageSize
                }

                if (this.queryParams) {
                    Object.assign(params, this.queryParams)
                }

                if (this.sortname) {
                    params.sortname = this.sortname
                    params.sortorder = this.sortorder
                }
                finalRequest({
                    url: this.url,
                    method: 'post',
                    params: params
                }).then(res => {
                    this.rows = res.Rows || []
                    this.pagination.total = res.Total
                    /*
                        * 如果获取到的数据为空，并且当前页面不是第一页时，加载上一页的数据
                        * 删除当前页所有记录的时候会出现这个情况
                        */
                    if (this.rows.length === 0 && this.pagination.pageNo > 1) {
                        this.pagination.pageNo--
                        this.refresh()
                    }
                    // 设置数据请求状态--请求结束
                    this.loading = false
                }).catch(err => {
                    // 设置数据请求状态--请求结束
                    this.loading = false
                })
            },
            /**
             * 重置分页，所选数据的信息
             */
            reset() {
                this.pagination.perPageSize = GlobalConst.pagination.size
                this.pagination.pageNo = 1
                this.selection = []
                this.refresh()
            },
            rowClick(row) {
                if (this.showSelection) {
                    this.$refs.listTable.toggleRowSelection(row)
                }
            },
            formatterColum(row, column, index) {
                let value = row[column.property]
                if (column.formatter) {
                    value = column.formatter(row, column, value, index, this) 
                }
                return (value || GlobalConst.table.value)
            },
            handColumnClick(columnDef, row, column, index) {
                if (columnDef.c_hander) {
                    columnDef.c_hander(row, row[column.property], index, this)
                }
            }
        },
        created() {
            this.refresh()
        }
    }
</script>

<style scoped lang="scss">
.s-noData {
    width: 100%;
    position: absolute;
    text-align: center;
    bottom: 0;
    left: 0;
    right: 0;
    height: 55%;
    margin: auto;
    .s-noData-img {
        width: auto;
        height: 80%;
        min-height: 100px;
    }
    .s-noData-text {
        font-size: $fontS;
        color: $fontCS;
    }
}
.m-table-list >>> {
    .el-table__empty-text {
        display: none;
    }
}
</style>
