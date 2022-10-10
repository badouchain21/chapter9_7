<template>
    <card>
        <div slot="header" class="col-12 d-flex justify-content-center justify-content-sm-between flex-wrap"
             style="padding-left: 0;padding-right: 0;padding-top:30px;padding-bottom: 20px;">
            <span style="font-size:15px">{{title}}</span> <a v-show="isShow" href="javascript:void(0);" style="margin-left:10px;font-size:15px;color:blue" @click="goPrePage">返回上一页</a>
        </div>
        <div>
            <div class="col-sm-12" style="padding-left: 0;padding-right: 0;">
                <el-table stripe
                          class="my-table-head"
                          ref="listTable"
                          style="width: 100%;"
                          :data="rows"
                          @selection-change="selection = $event"
                          @row-click="rowClick"
                          border
                          max-height="420">
                    <el-table-column
                        v-if="showSelection"
                        type="selection"
                        width="55">
                    </el-table-column>

                    <el-table-column
                        v-if="lineNumber"
                        type="index"
                        label="序号"
                        width="65"
                        :index="from + 1">
                    </el-table-column>

                    <template v-for="column in columns">
                        <el-table-column :key="column.name"
                                         :min-width="column.minWidth"
                                         :prop="column.prop"
                                         :label="column.label"
                                         :formatter="column.formatter"
                                         >
                            <template slot-scope="scope">
                                <div class="text-handle" @click="handColumnClick(column, scope.row, scope.column, scope.$index)" v-html="formatterColum(scope.row, scope.column, scope.$index)"></div>
                            </template>
                        </el-table-column>
                    </template>
                </el-table>
            </div>
        </div>
        <div slot="footer" class="col-12 d-flex justify-content-center justify-content-sm-between flex-wrap"
             style="padding-left: 0;padding-right: 0;padding-top:30px">
            <div class="page-info">
                <el-pagination v-if="pageInfo" 
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pagination.pageNo"
                    :page-sizes="pagination.perPageOptions"
                    :page-size="pagination.perPageSize"
                    layout=" jumper, next, pager, prev,slot"
                    ref="pagination"
                    :total="pagination.total">
                        <span>显示第 {{from + 1}} 到第 {{to}} 条记录，总共 {{total}} 条记录</span>
                        <span v-if="pageInfo" style="margin-left: 10px;">
                            每页显示
                            <el-select
                                class="select-default"
                                style="width:115px"
                                v-model="pagination.perPageSize"
                                @change="handleSizeChange"
                                placeholder="Per page">
                                <el-option
                                class="width:30px"
                                    v-for="item in pagination.perPageOptions"
                                    :key="item"
                                    :label="item"
                                    :value="item">
                                </el-option>
                            </el-select>
                            条记录
                        </span>
                </el-pagination>
            </div>
        </div>
    </card>
</template>

<script>

    import {Table, TableColumn, Select, Option} from 'element-ui'
    import LPagination from '@/components/frame/Pagination.vue'

    const DEFAULT_PER_PAGE_SIZE = 10

    export default {
        name: "pagelist",
        components: {
            LPagination,
            [Select.name]: Select,
            [Option.name]: Option,
            [Table.name]: Table,
            [TableColumn.name]: TableColumn
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
            /*
             * 是否加载数据，用于父组件控制子组件是否可以进行数据的加载。
             * 例如 在模型渲染列表的场景下，需要先获取到模型数据，才可以进行列表数据的查询，所以在页面渲染的时候
             * 把这个属性设置为false，等到获取到模型数据后，将该属性设置为true，并且调用获取数据的方法
             */
            loadData: {
                type: Boolean,
                default: true
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
            }
        },
        data() {
            return {
                rows: [],
                pagination: {
                    perPageSize: DEFAULT_PER_PAGE_SIZE,
                    pageNo: 1,
                    perPageOptions: [10, 25, 50, 100],
                    total: null,
                },
                // 存储选中的数据，在父组件中直接获取即可
                selection: [],
                isShow:false,
                prePageNo:0,
                oldPerPageSize:0,
            }
        },
        computed: {
            to() {
                let highBound = this.from + this.pagination.perPageSize
                if (this.total < highBound) {
                    highBound = this.total
                }
                return highBound
            },
            from() {
                return this.pagination.perPageSize * (this.pagination.pageNo - 1)
            },
            total() {
                return this.pagination.total
            }
        },
        methods: {
            /**
             * 刷新列表
             */
            refresh() {
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

                this.post(this.url, params).then(res => {
                    for(var i = 0; i < res.datas.length; i++){
                        // 若存在 url，说明该记录属于已上链，为其设置超链接
                        if(res.datas[i].jumpUrl != null && res.datas[i].jumpUrl != ""){
                            res.datas[i].hash = "<a style='color:blue' target='_blank' href='" + res.datas[i].jumpUrl + "'>" + res.datas[i].hash + "</a>"
                        }
                        res.datas[i].assetName = "<a style='color:blue' href='javascript:void(0);'>"+res.datas[i].assetName+"</a>"
                    }
                    this.rows = res.datas
                    this.pagination.total = res.totalCount

                    if (res.totalCount < res.perPageSize || this.pagination.pageNo > res.totalPageCount){
                        this.pagination.pageNo = res.totalPageCount
                    }

                    /*
                        * 如果获取到的数据为空，并且当前页面不是第一页时，加载上一页的数据
                        * 删除当前页所有记录的时候会出现这个情况
                        */
                    if (this.rows.length === 0 && this.pagination.pageNo > 1) {
                        this.pagination.pageNo--
                        this.refresh()
                    }
                })
            },
            /**
             * 重置分页，所选数据的信息
             */
            reset() {
                this.pagination.perPageSize = DEFAULT_PER_PAGE_SIZE
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
                    return column.formatter(row, column, value, index, this)
                }
                return value
            },
            handColumnClick(columnDef, row, column, index) {
                if (columnDef.c_hander) {
                    columnDef.c_hander(row, row[column.property], index, this)
                }
                if (columnDef.prop == "assetName"){
                    //保存当前分页
                    if (this.prePageNo == 0 || this.oldPerPageSize == 0){
                        this.prePageNo = this.pagination.pageNo
                        this.oldPerPageSize = this.pagination.perPageSize
                        this.isShow = true
                    
                        //设置后台请求参数为第一页
                        this.pagination.pageNo = 1
                        this.queryParams.assetId = row.assetId
                        this.refresh()
                    }
                }
            },
            handleCurrentChange (val) {
                this.pagination.pageNo = val
                this.refresh()
                this.$emit("updateAmount")
            },
            handleSizeChange (val) {
                this.pagination.perPageSize = val;
                this.pagination.pageNo = 1;
                this.refresh()
                this.$emit("updateAmount")
            },
            goPrePage (){
                this.isShow = false
                this.pagination.perPageSize = this.oldPerPageSize

                this.queryParams.assetId = null
                this.handleCurrentChange(this.prePageNo)

                this.prePageNo = 0
                this.oldPerPageSize = 0
            }
        },
        created() {
            if (!this.loadData) {
                return
            }
            this.refresh()
            this.columns[1].minWidth = '220'
        },
        watch:{
            prePageNo(newVal,oldVal){
                if(newVal == 0) {
                    //设置页面页码高亮显示
                    this.$nextTick(() => {
                        this.$refs.pagination.internalCurrentPage = oldVal;
                    })
                }
            }
            
        }

    }
  
</script>

<style scoped>
    .col-sm-12 {
        padding: 0;
    }
    .page-info >>> .select-default.el-select .el-input .el-input__icon {
        background-color: rgba(0,0,0,0);
    }
    .text-handle {
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
    }
    .el-table__body .el-table__row td {
        padding-left: 0;
    }
    .el-table__body .el-table__row td:nth-child(2) {
        padding-left: 8px;
    }
    .el-table__body .el-table__row td:nth-child(4) {
        padding: 0;
    }
    .el-table__body .el-table__row td:nth-child(7) {
        padding: 23px;
    }
</style>

