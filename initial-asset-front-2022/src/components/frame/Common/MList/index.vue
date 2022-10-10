<template>
    <div class="mList self-module-list ">
        <div class="self-module-list-main">
            <div class="tableContent" v-setTableHeight='{ tableHeight:tableHeight, parentClassName: parentElClass }'>
                <el-scrollbar class="h-per-100" ref="mTable">
                    <!-- sign：使用了el-table -->
                    <el-table
                        ref="mList"
                        v-loading="loading"
                        :data="tempDataList"
                        class="w-per-100 module-list-table"
                        @cell-click="cellClick">
                        <!-- 是否带有选择框 -->
                        <el-table-column
                            v-if="isSelection"
                            type="selection"
                            :width="selectionWidth">
                        </el-table-column>
                        <!-- 是否带顺序标识 -->
                        <!-- 默认配置顺序，当传入属性hasIndex为false时去除顺序 -->
                        <el-table-column
                            v-if="hasIndex && Boolean(indexWidth)"
                            type="index"
                            :label="indexLabel"
                            :width="indexWidth"
                            :index="indexMethod">
                        </el-table-column>
                        <template 
                            v-for="(i, index) in tempFieldList">
                            <el-table-column
                                :key="index"
                                :prop="i.name"
                                :label="i.label"
                                :width="i.width"
                                :min-width="i.minWidth || minWidth">
                                <template slot-scope="scope">
                                    <bd-icon 
                                        :name="i.iconName" 
                                        v-if="columnFormatter(i, scope.row, scope.column, scope.row[scope.column.property], scope.$index) && i.iconName" 
                                        class="fill icon-small"
                                        :class="i.iconClass"
                                        :style="i.iconStyle">
                                    </bd-icon>
                                    <!-- class-pointer手势类，当事件存在时使用 -->
                                    <span 
                                        class="text-o-1" 
                                        :class="{'pointer':i.handler}"  
                                        @click.stop="columnHandler(i, scope.row, scope.column, scope.row[scope.column.property], scope.$index)"
                                        v-html="columnFormatter(i, scope.row, scope.column, scope.row[scope.column.property], scope.$index) || defaultCellValue">
                                    </span>
                                </template>
                            </el-table-column>
                        </template>
                    </el-table>
                </el-scrollbar>
            </div> 
            <!-- v-if="showPagination && isDataFromRequest" -->
            <m-pagination
                class="module-list-pagination"
                v-if="showPagination"
                :pageNo="this.tempParams[this.paginationNameObj.no]"
                :pageSize="this.tempParams[this.paginationNameObj.size]"
                :pageSizes="pageSizes"
                :total="total"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange">
            </m-pagination>
        </div>
    </div>
</template>

<script>
import GlobalConst from '@/service/global-const'
import MPagination from './components/Pagination'
import { finalRequest } from '@/service/request'
import globalStyle from '@/styles/global.scss'
import { findParentByClassName, findByClassName } from '@/utils/find-dom'
export default {
    components: {
        MPagination
    },
    props: {
        // hasIndex、indexLabel、indexWidth使用关系
        // 1. 只传hasIndex，就会默认有顺序列
        // 2. 当传递indexLabel或者indexWidth时，可以不用传hasIndex进来

        // 是否带有顺序列，默认无
        hasIndex: {
            type: Boolean,
            default: true
        },
        // 顺序列标签名称
        indexLabel: {
            type: String,
            default: GlobalConst.table.indexLabel
        },
        // 顺序列标签宽度
        indexWidth: {
            type: String,
            default: GlobalConst.table.indexWidth
        },
        // 是否为可选表格
        isSelection: {
            type: Boolean,
            default: false
        },
        // 表头数据源：格式为[{{ name: '字段名', label: '展示文本', width: 120 }}]
        fieldList: {
            type: Array,
            default: () => []
        },
        // 表头数据源-通过ajax请求获取
        fieldRequest: {
            type: Object,
            default: () => {
                return {
                    // 请求地址
                    url: '',
                    // 请求参数
                    params: {}
                }
            }
        },
        // 列表数据源（可直接传入数据源）
        dataList: {
            type: Array,
            default: () => []
        },
        // 列表数据请求地址
        url: {
            type: String,
            default: ''
        },
        // 列表数据请求参数对象
        params: {
            type: Object,
            default: () => {}
        },
        // 分页参数对应名称
        paginationNameObj: {
            type: Object,
            default: () => {
                return {
                    no: GlobalConst.pagination.noName,
                    size: GlobalConst.pagination.sizeName
                }
                
            }
        },
        // 是否显示分页组件--默认显示
        // 当不显示分页组件时要留意，默认请求全部数据，需要设置页码为1，单页数据条数为1000（你细品）
        showPagination: {
            type: Boolean,
            default: true
        },
        // 是否一次性请求所有数据
        isLoadAll: {
            type: Boolean,
            default: false
        },
        // 表格所占高度：
        //     支持传入像素px：可在一般页面的列表组件以及弹窗中使用
        //     支持传入负数（传入负数值为children-router页面中其他元素的总高度）：只能在一般页面，不可在弹窗中使用
        tableHeight: {
            type: String,
            // default: GlobalConst.table.height
        },
        // 整个列表页面的高度参照物，当一般页面时为父级，弹窗中的列表支持传入参照物实现高度自适应
        parentElClass: {
            type: String,
            default: 'self-module-list'
        },
    },
    directives: {
        setTableHeight: function (el, binding) {
            let { parentClassName, tableHeight } = binding.value
            setTimeout(function () {
                // 定义表格所处在模块的展示高度
                let panelHeight = tableHeight ? parseInt(tableHeight) : 0
                // 模块最顶级父亲，高度参照峰值
                let ROOTEl = findParentByClassName(el, parentClassName)
                // 获取分组组件DOM
                let paginationEl = findByClassName(ROOTEl, 'module-list-pagination')
                // 获取表格模块对象
                let tableEl = findByClassName(ROOTEl, 'module-list-table')
                // 表格header-dom对象
                let tableHeaderEl = tableEl.children[1]
                // // 表格body-dom对象
                let tableBodyEl = tableEl.children[2]
                // 最小表格高度，最低要求了
                let minTableHeight = parseInt(GlobalConst.table.height)
                // 判断是否有传入值限定当前模块高度，有就使用
                panelHeight = panelHeight || ROOTEl.clientHeight
                // 模块高度与最小高度比较，比最小高度小将直接使用最小高度
                panelHeight = panelHeight > minTableHeight ? panelHeight : minTableHeight
                let borderHeight = 1
                // 设置列表展示高度
                tableBodyEl.style.cssText = `
                    height: ${
                        panelHeight - 
                        2 * parseInt(globalStyle.padding) -
                        (tableHeaderEl.clientHeight || 0) - 
                        (paginationEl.clientHeight || 0) -
                        8}px;
                    overflow: auto`
            }, 400)
        }
    },
    computed: {
        // 选择列宽度
        selectionWidth () {
            return GlobalConst.table.selectionWidth
        },
        // 获取表格列最小宽度
        minWidth () {
            return GlobalConst.table.minWidth
        }
    },
    data () {
        return {
            // 请求数据状态
            loading: false,
            total: 0,
            // 临时表头数组
            tempFieldList: [],
            // 临时表格数组
            tempDataList: [],
            // 临时参数对象
            tempParams: {},
            // 分页参数对象
            paginationObj: {
                no: GlobalConst.pagination.no,
                size: GlobalConst.pagination.size
            },
            // 下拉可选请求数据条数
            pageSizes: [],
            // 列表数据是否来自请求获取
            isDataFromRequest: true,
            // 默认cell值
            defaultCellValue: GlobalConst.table.value
        }
    },
    methods: {
        /** 
         * 字段值处理函数
         * @params item [Object]: 表头列字段对象
         * @params row [Object]: 该行数据对象
         * @params column [String]: 列对象
         * @params value [String/NUmber]: 传入值
         * @params index [Number]: 该行下角标
         * @return [Function]
        */
        columnFormatter (item, row, column, value, index) {
            // 判断是否存在值自定义操作
            if (!item.formatter) {
                return value
            }
            return item.formatter(row, column, value, index, this)
        },
        /** 
         * 字段操作函数
         * @params item [Object]: 表头列字段对象
         * @params row [Object]: 该行数据对象
         * @params column [String]: 列对象
         * @params value [String/NUmber]: 传入值
         * @params index [Number]: 该行下角标
        */
        columnHandler (item, row, column, cellValue, index) {
            // 判断是否存在操作事件
            if (!item.handler) {
                // 当前cell无事件，则设置点击后该行数据被选中；有事件则使用stop冒泡阻止（已在html中添加）
                this.cellClick(row)
                return 
            }
            return item.handler(row, column, cellValue, index, this)
            
        },
        cellClick (row, column, cell, event) {
            this.$refs.mList.toggleRowSelection(row)
        },
        initTable () {
            this.loading = true
            if (this.isLoadAll) {
                // 如果设置为一次请求完，则修改单页请求数为设定峰值，一次请求完
                this.tempParams[this.paginationNameObj.size] = GlobalConst.pagination.maxSizeNum
            }
            finalRequest({
                url: this.url,
                method: 'post',
                params: this.tempParams
            }).then(res => {
                this.tempDataList = res.Rows
                try {
                    let ROOTEl = this.$refs.mTable.$refs['wrap']
                    let tableBodyEl = findByClassName(ROOTEl, 'el-table__body-wrapper')
                    tableBodyEl.scrollTop = 0
                } catch (e) {
                    console.log(`设置列表滚动高度重置失败：${JSON.stringify(e)}`)
                }
                this.total = res.Total
                // 设置数据请求状态--请求结束
                this.loading = false
            }).catch(err => {
                // 设置数据请求状态--请求结束
                this.loading = false
            })
        },
        // 分页组件单页可请求数据条数数组
        paginationSizes () {
            // 定义并获取下拉选择单页请求数的类型总数，[10,20,30,40]这为4种
            let sizeSelectNum = GlobalConst.pagination.sizeSelectNum
            // 定义并获取可选单页请求数的增幅，[5,15,25,35]增幅为10
            let sizeAddNum = GlobalConst.pagination.sizeAddNum
            // 定义分页单页请求数据条数数组
            let sizesArray = []
            // 获取默认单页请求数
            let startSize = parseInt(this.paginationObj.size)
            // 默认单页请求数据条数作为数组第一项，默认选中
            sizesArray.push(startSize)
            for (let i = 0; i < sizeSelectNum - 1; i++) {
                startSize += sizeAddNum
                sizesArray.push(startSize)
            }
            return sizesArray
        },
        // 单页数据条数切换函数
        handleSizeChange (val) {
            this.$set(this.tempParams, this.paginationNameObj.size, val)
            this.initTable()
        },
        // 页码数切换函数
        handleCurrentChange (val) {
            this.tempParams[this.paginationNameObj.no] = val
            this.initTable()
        },
        // 自定义列序号
        indexMethod (index) {
            if (!this.isDataFromRequest) {
                return index + 1
            }
            return (this.tempParams[this.paginationNameObj.no] - 1 ) *
                   this.tempParams[this.paginationNameObj.size] + 
                   index + 
                   1
        }
    },
    mounted () {
        // 存在请求url时再执行请求，否则逻辑为使用传入的列表数据
        if (this.url) {
            this.pageSizes = this.paginationSizes()
            this.initTable()
        } else {
            // 列表数据来自传入
            this.isDataFromRequest = false  // 设置数据来源状态

        }
    },
    watch: {
        dataList: {
            immediate: true,
            handler: function (newVal, oldVal) {
                this.tempDataList = newVal
            }
        },
        fieldList: {
            immediate: true,
            handler: function (newVal, oldVal) {                
                this.tempFieldList = newVal
            }
        },
        fieldRequest: {
            immediate: true,
            handler: function (newVal, oldVal) {
                let { url, params } = newVal
                if (!url) {
                    return
                }
                finalRequest({
                    url: url,
                    method: 'post',
                    params: params
                }).then(res => {
                    // TODO: 这里有待商榷字段名称如何统一问题
                    this.tempFieldList = res.map(i => {
                        return {
                            ...i,
                            label: i.display || i.label,
                            name: i.name,
                        }
                    })
                }).catch(err => {
                    this.tempFieldList = []
                })
            }
        },
        params: {
            immediate: true,
            handler: function (newVal, oldVal) {
                if (newVal) {
                    this.tempParams = JSON.parse(JSON.stringify(newVal))
                    this.paginationObj.no = newVal[this.paginationNameObj.no] || this.paginationObj.no
                    this.paginationObj.size = newVal[this.paginationNameObj.size] || this.paginationObj.size
                    this.$set(this.tempParams, this.paginationNameObj.no, this.paginationObj.no)
                    this.$set(this.tempParams, this.paginationNameObj.size, this.paginationObj.size)
                }
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.mList >>> {
    .self-module-list-main {
        border: 1px solid $tableLineColor;
        border-radius: $borderRadius;
    }
    .el-scrollbar__wrap {
        width: calc(100% + 17px);
        height: calc(100% + 17px); 
    }
    .el-table__empty-block {
        width: 100% !important;
    }
}
.tableContent >>> {
    // .el-scrollbar__wrap {
    //     padding-bottom: 18px;
    // }
    .el-loading-mask {
        height: 300px;
    }
}
</style>
