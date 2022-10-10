<template>
    <div class="address-book-dialog">
        <el-dialog
            width="950px"
            :title="config.title"
            :visible.sync="dialogVisible"
            class="scorpion">
            <div>
                <search-bar
                    v-show="showSelector"
                    :use-filter="useFilter"
                    :mdCode="this.config.dataSrc"
                    :selectorConfig="selectorConfig"
                    @search="search">
                </search-bar>
                <!-- sign：使用了el-table -->
                <el-table
                    ref="chooseList"
                    :data="chooseListData"
                    tooltip-effect="dark"
                    style="width: 100%"
                    @row-click="rowClick"
                    @selection-change="chooseData = $event">
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column type="index" label="序号" width="65"></el-table-column>
                    <template v-for="column in columns">
                        <el-table-column
                            :key="column.name"
                            :prop="column.prop"
                            :label="column.label"
                            :min-width="minWidth">
                        </el-table-column>
                    </template>
                </el-table>
            </div>
            <div
                slot="footer"
                v-if="isShowPage"
                class="col-12 d-flex justify-content-center justify-content-sm-between flex-wrap"
                style="padding-left: 0;padding-right: 0;">
                <div class>
                    <p class="card-category">
                        <span>显示第 {{from + 1}} 到第 {{to}} 条记录，总共 {{total}} 条记录</span>
                    </p>
                </div>
                <l-pagination
                    class="pagination-no-border"
                    v-model="pagination.pageNo"
                    :per-page="pagination.perPageSize"
                    :total="pagination.total"
                    @input="loadModuleData">
                </l-pagination>
            </div>

            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleSubmitClick">提 交</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
import GlobalConst from '@/service/global-const'
import LPagination from "@/components/frame/Pagination"
import SearchBar from "@/components/frame/Search/SearchBar"
import { Dialog, Table, TableColumn, Select, Option } from "element-ui"

export default {
    components: {
        SearchBar,
        LPagination,
        [Dialog.name]: Dialog,
        [Table.name]: Table,
        [TableColumn.name]: TableColumn,
        [Select.name]: Select,
        [Option.name]: Option
    },
    props: {
        useFilter: {
            type: Boolean,
            default: false
        },
        pageInfo: {
            type: Boolean,
            default: true
        }
    },
    data() {
        return {
            defaultSearchParams: [] /* 默认查询阐述 */,
            config: {} /* 配置参数 */,
            columns: [] /* 列表上的列 */,
            chooseListData: [] /* 列表数据 */,
            isShowPage: false /* 是否显示分页栏 */,
            showSelector: false /* 是否显示搜索栏 */,
            selectorData: null /* 搜索框配置数据 */,
            searchCondition: null /* 搜索框配置的条件 */,
            queryParams: [] /* 查询参数 */,
            pagination: {
                perPageSize: 6,
                pageNo: 1,
                perPageOptions: [10, 25, 50, 100],
                total: 0
            },
            chooseData: [] /* 用户选择的数据，最终结果就是包含该对象的所有 value 值的数组 */,
            dialogVisible: true /* 对话框显示控制 */
        };
    },
    computed: {
        to () {
            let highBound = this.from + this.pagination.perPageSize
            if (this.total < highBound) {
                highBound = this.total
            }
            return highBound
        },
        from () {
            return this.pagination.perPageSize * (this.pagination.pageNo - 1)
        },
        total () {
            return this.pagination.total;
        },
        selectorConfig () {
            return {
                selectorData: this.selectorData,
                searchCondition: this.searchCondition
            }
        },
        // 获取表格列最小宽度
        minWidth () {
            return GlobalConst.table.minWidth
        }
    },
    methods: {
        loadTableList () {
            let field = this.config.field;
            /* 数据来源，数据字典 */
            if (field.dataSourceType === 1 || field.dataSourceType === "1") {
                /* 列表行列 */
                this.columns = [
                    { label: "文本", prop: "text", name: "text" },
                    { label: "字典值", prop: "id", name: "id" }
                ];
                /* 列表数据 */
                this.chooseListData = this.config.dicData[field.dic];
            } else if (
            /* 自定义数据 */
                field.dataSourceType === 2 ||
                field.dataSourceType === "2"
            ) {
                return
                let obj = this.module.editedJsObj
                if (obj && obj[field.fun]) {
                    let result = obj[field.fun].call(this)
                    this.chooseListData = result.data
                    this.columns = result.columns
                }
            } else if (
            /* 模型数据 */
                field.dataSourceType === 3 ||
                field.dataSourceType === "3"
            ) {
                this.defaultSearchParams = Object.assign(
                    [],
                    this.config.defaultParams
                )
                /* 加载列 */
                this.loadModule()
                /* 获取模型数据 */
                this.loadModuleData()
                /* 显示搜索框 */
                this.showSelector = true
                /* 显示分页 */
                this.isShowPage = true
            }
        },
        /* 加载模型的列 */
        loadModule () {
            let params = { mdCode: this.config.field.mdCode }
            this.post(this.INTERFACE.moduleListConfig, params).then(res => {
                let arr = []
                let moduleDisplay = JSON.parse(res.moduleDisplay)
                moduleDisplay
                    .filter(f => !f.isHidden)
                    .forEach(f => {
                        arr.push({
                            prop: f.name,
                            label: f.display,
                            minWidth: f.listWidth
                        })
                    })
                this.columns = arr
                this.selectorData = JSON.parse(res.selectorData)
                this.searchCondition = JSON.parse(res.searchCondition)
            })
        },
        search (searchParam) {
            if (searchParam) {
                this.queryParams = searchParam
            }
            this.loadModuleData()
        },
        reset () {
            (this.queryParams = []), this.loadModuleData()
        },
        /* 加载模型数据 */
        loadModuleData() {
            let params = {
                searchParam: JSON.stringify(this.queryParams),
                defaultSearchParam: JSON.stringify(this.defaultSearchParams),
                mdCode: this.config.field.mdCode,
                pageNo: this.pagination.pageNo,
                perPageSize: this.pagination.perPageSize
            };
            this.post(this.INTERFACE.moduleListData, params).then(res => {
                this.chooseListData = res.Rows
                this.pagination.total = res.Total
                /*
                 * 如果获取到的数据为空，并且当前页面不是第一页时，加载上一页的数据
                 * 删除当前页所有记录的时候会出现这个情况
                 */
                if (
                    this.chooseListData.length === 0 &&
                    this.pagination.pageNo > 1
                ) {
                    this.pagination.pageNo--
                    this.loadModuleData()
                }
            })
        },
        /* 点击提交 */
        handleSubmitClick() {
            if (this.chooseData.length <= 0) {
                this.alert("请选择一条数据!")
                return
            }
            this.callback(this.chooseData)
            this.dialogVisible = false
        },
        /* 点击表格选中行 */
        rowClick (row) {
            if (this.config.singleChoose) {
                this.chooseData = [row]
                this.$refs.chooseList.clearSelection()
            }
            this.$refs.chooseList.toggleRowSelection(row)
        }
    },
    created () {
        this.loadTableList()
    }
};
</script>
<style scoped>
.dialog-customize-content {
    padding: 0 25px 25px 25px;
    display: flex;
    width: 100%;
}

.tree-block {
    width: 248px;
    display: inline-block;
    border: 1px solid #efecec;
    height: 348px;
    border-radius: 4px;
    overflow: scroll;
    border-top: none;
}

.tree-block p {
    width: 100%;
    height: 35px;
    font-size: 16px;
    text-align: center;
    background-color: #409eff;
    color: white;
    border: 1px solid #efecec;
    margin-bottom: 5px;
    padding-top: 4px;
}

.dialog-transfer {
    display: inline-block;
    margin-left: 10px;
    position: relative;
}

.address-book-dialog >>> .el-dialog__footer {
    text-align: center;
}

.address-book-dialog >>> .el-collapse-item__header {
    height: 30px;
    line-height: 30px;
    padding-left: 8px;
    font-size: 15px;
}

.address-book-dialog >>> .el-collapse-item__content {
    padding-bottom: 0;
}

.address-book-dialog >>> .el-transfer__buttons {
    padding: 0 10px;
}

.address-book-dialog >>> .el-transfer-panel {
    width: 230px;
}

.address-book-dialog >>> .el-dialog__body {
    padding: 0;
}
</style>