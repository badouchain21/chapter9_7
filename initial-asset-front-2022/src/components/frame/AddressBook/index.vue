<template>
    <div class="address-book-dialog">
        <el-dialog
        width="950px"
        :title="config.title"
        :visible.sync="dialogVisible"
        class="scorpion">
        <div class="dialog-customize-content">
            <div class="tree-block">

                <el-collapse v-model="config.indexType" accordion @change="handleCollapseChange">
                    <el-collapse-item
                            v-for="(item, index) in addressBookData" :key="index"
                            :title="item.name" :name="item.key">
                        <el-tree
                            v-if="showTree[item.key]"
                            lazy
                            node-key="id"
                            :props="treeProp"
                            :default-expanded-keys="defaultExpandedKeys"
                            :load="loadNode"
                            @node-click="handleNodeClick">
                        </el-tree>
                    </el-collapse-item>
                </el-collapse>
            </div>
            <div class="dialog-transfer">
                <div style="margin-bottom: 20px;">
                    <el-input placeholder="请输入内容" class="input-with-select" v-model="queryValue">
                        <el-button slot="append" icon="el-icon-search" @click="searchOption"></el-button>
                    </el-input>
                </div>
                <el-transfer
                    @left-check-change="leftCheckChange"
                    @change="handleChange"
                    v-model="value"
                    :data="transdata"
                    :titles="['待选列表', '已选列表']"
                    :props="{key: 'id', label: 'name'}">
                    <div slot-scope="{ option }">
                        <span :title="option.desc" @dblclick="dbClickOption(option.id)">{{ option.name }}</span>
                    </div>
                </el-transfer>

            </div>
        </div>

        <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="handleSubmitClick">提 交</el-button>
    </span>
    </el-dialog>
    </div>
</template>
<script>
    import {Dialog, Dropdown, DropdownMenu, DropdownItem, Popover, Tree, Transfer, Collapse, CollapseItem} from 'element-ui'

    export default {
        components: {
            [Dialog.name]: Dialog,
            [Dropdown.name]: Dropdown,
            [DropdownMenu.name]: DropdownMenu,
            [DropdownItem.name]: DropdownItem,
            [Popover.name]: Popover,
            [Tree.name]: Tree,
            [Transfer.name]: Transfer,
            [Collapse.name]: Collapse,
            [CollapseItem.name]: CollapseItem
        },
        data() {
            return {
                // 地址本的所有配置
                config: {},
                addressBookURL: `${this.BASEURL}/btaddressbook/btaddressbook/addressBookData.do`,
                addressBookData: [],
                // 加载树数据时用到的提交参数
                treeParams: {},
                // 加载选择数据时用到的提交参数
                dataParams: {},
                treeProp: {
                    label: 'name',
                    isLeaf(data) {
                        return !(data.isParent || data.hasChild)
                    }
                },
                defaultExpandedKeys: [],
                // input 框输入的搜索数据
                queryValue: '',
                /*
                 * 当前的数据，在点击树的节点后，触发 loadOptionData 方法获取数据，返回的数据是一个数组，
                 * 为了方便查找，会把数组转换为 {id: data} 的格式，暂存在该属性中
                 */
                currentData: {},
                /*
                 * 用户选择的数据，最终结果就是包含该对象的所有 value 值的数组
                 */
                chooseData: {},
                transdata: [], //穿梭框数据渲染
                value: [], //右边默认显示
                dialogVisible: true, //对话框显示控制,
                // 当前树节点的id
                currentTreeNodeId: '',
                // 控制是否渲染树
                showTree: [true]
            }
        },
        methods: {
            loadAddress() {
                this.post(this.addressBookURL, {type: this.config.type}).then(res => {
                    if (res.hasOk) {
                        let data = res.bean
                        if (!this.config.indexType) {
                            this.config.indexType = data[0].key
                        }
                        this.showTree[this.config.indexType] = true
                        this.addressBookData = data
                    } else {
                        this.alert(`获取地址本信息失败！${res.message}`)
                    }
                })

            },
            getTreeQeryParams() {
                return {
                    defaultParams: this.config.treeDefaultParams,
                    indexType: this.config.indexType,
                    userPermission: this.config.userPermission,
                    registerDicCode: this.config.registerDicCode,
                    selectType: this.config.selectType
                }
            },
            loadNode(node, resolve) {
                let url = `${this.BASEURL}/btaddressbook/btaddressbook/tree.do`
                this.treeParams.indexType = this.config.indexType
                let params = Object.assign({}, this.getTreeQeryParams())
                if (node.data) {
                    params.nodeId = node.data.id
                }
                this.post(url, params).then(res => {
                    resolve(res)
                    // 加载节点为根节点时，展开，并加载根节点的数据
                    if (node.level === 0) {
                        this.defaultExpandedKeys.push(res[0].id)
                        this.handleNodeClick(res[0]);
                    }
                })
            },
            loadOptionData(url, params) {
                this.post(url, params).then(res => {
                    this.currentData = {}
                    res.map(e => this.currentData[e.id] = e)
                    this.transdata = res
                })  
            },
            getOptionQueryParams() {
                return {
                    indexType: this.config.indexType,
                    includeParent: this.config.includeParent,
                    defaultParams: this.config.treeDefaultParams,
                    treeLevels: this.config.treeLevels,
                    registerDicCode: this.config.registerDicCode,
                    selectType: this.config.selectType
                }
            },
            searchOption() {
                let url = `${this.BASEURL}/btaddressbook/btaddressbookquery/search.do`
                let params = Object.assign({}, this.getOptionQueryParams(), {pid: this.currentTreeNodeId, value: this.queryValue.trim()})
                this.loadOptionData(url, params)
            },
            handleNodeClick(data) {
                this.currentTreeNodeId = data.id
                let url = `${this.BASEURL}/btaddressbook/btaddressbookquery/selectnode.do`
                let params = Object.assign({}, this.getOptionQueryParams(), {nodeId: data.id})
                this.loadOptionData(url, params)
            },
            handleSubmitClick() {
                let data = Object.keys(this.chooseData).map(key => this.chooseData[key])
                if (data.length === 0) {
                    this.alert('请选择数据')
                    return
                }
                this.callback(data)
                this.dialogVisible = false
            },
            handleChange(currentValue, to, changeValue) {
                let add = to === 'right'
                if (add) {
                    // 如果是单选，先清除现有数据
                    if (this.config.singleChoose) {
                        currentValue.splice(0, currentValue.length)
                        currentValue.push(...changeValue)
                        this.chooseData = {}
                    }
                    changeValue.forEach(id => this.chooseData[id] = this.currentData[id])
                } else {
                    changeValue.forEach(id => delete this.chooseData[id])
                }
            },
            /**
             * 实现双击选择的功能
             * @param {string} dataId 双击项对应的id
             */
            dbClickOption(dataId) {
                let add = typeof this.chooseData[dataId] === 'undefined'
                if (add) {
                    // 如果是单选，先清除现有数据
                    if (this.config.singleChoose) {
                        this.value.splice(0, this.value.length)
                        this.chooseData = {}
                    }
                    this.value.push(dataId)
                    this.chooseData[dataId] = this.currentData[dataId]
                } else {
                    delete this.chooseData[dataId]
                    for (let i = 0; i < this.value.length; i++) {
                        if (this.value[i] === dataId) {
                            this.value.splice(i, 1)
                            break
                        }
                    }
                }
            },
            /**
             * 实现页面的单选效果
             * @param chooseArr 已勾选的数组，通过改变改数组的值，实现单选
             * @param current   当前选择的数组
             */
            leftCheckChange(chooseArr, current) {
                if (!this.config.singleChoose) {
                    return
                }
                // 一个都没选或只选了一个，什么都不用做
                if (chooseArr.length === 0 || chooseArr.length === 1) {
                    return
                }

                /*
                 * current 在点击全选按钮的时候，很可能有多个，这种情况下，删除选中的，提示不能多选。
                 */
                if (current.length > 1) {
                    // 将数组转换为对象，方便后续查询
                    let obj = {}
                    chooseArr.forEach((e, i) => obj[e] = i)
                    // 获取 current 中的元素在 chooseArr 中的下标
                    let indexArr = current.map(e => obj[e])
                    /*
                     * 将下标从大到小排序，目的是为了可以从数组后面往前面删除，如果从前面删除的话，会导致
                     * 后面元素的下标发生改变
                     */
                    indexArr.sort((n1, n2) => n2 - n1)
                    indexArr.forEach(i => chooseArr.splice(i, 1))
                    this.alert('只能选择一个')
                    return
                }

                // current 只有1个，替换已有数据
                chooseArr.splice(0, chooseArr.length)
                chooseArr.push(...current)
            },
            handleCollapseChange(active) {
                this.showTree[active] = true
                this.config.indexType = active
            }
        },
        created() {
            this.loadAddress()
        }
    }
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
        background-color: #409EFF;
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
