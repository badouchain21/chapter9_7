<template>
    <div class="defaultBg">
        <div class="m-tree-list limited">
            <div class="m-tree-main" :class="{'is-hide': hideTree}">
                <div class="hideTree-title">
                    <span class="titleWord" v-for="(i, index) in module.name" :key="index">{{i}}</span>
                </div>
                <div class="tree-title"  @click="toggleMenu">
                    <span class="name text-o-1"><i class="fa fa-sitemap"></i>{{module.name}}</span>
                    <bd-icon :name='hideTree?"#bd-dakai":"#bd-shouqicaidan"'></bd-icon>
                </div>
                <el-scrollbar wrap-class="scrollbar-wrapper" class="flex-1">
                    <el-tree
                        class="m-tree"
                        v-if="hackReset"
                        ref="tree"
                        :props="props1"
                        :load="loadNode"
                        node-key="id"
                        :default-expanded-keys="defaultExpandedKeys"
                        lazy
                        :expand-on-click-node="false"
                        @node-expand="nodeExpand"
                        @node-collapse="nodeClose"
                        @node-click="nodeClick">
                        <div slot-scope="{ node, props1 }" class="tree-lable" :title="node.label">
                            <bd-icon :id="node.id"
                                :name='node.isLeaf?"#bd-ziwenjian":"#bd-wenjianguanbi"'></bd-icon>
                            <span>{{node.label}}</span>
                        </div>
                    </el-tree>
                </el-scrollbar>
            </div>
            <!-- <div class="spread" v-show="hideTree">
                <i @click="showMenu" class="fa fa-angle-double-right"></i>
            </div> -->

            <div class="m-treeList">
                <!-- <el-scrollbar wrap-class="scrollbar-wrapper"> -->
                    <module-list
                        ref="moduleList"
                        :listTitle="listTitle"
                        :url="url"
                        :module="module"
                        :queryParams="queryParams"
                        :sortname="sortname"
                        :sortorder="sortorder"
                        :defaultButtons="defaultButtons"
                        @deleteAfter="deleteAfter">
                    </module-list>
                <!-- </el-scrollbar> -->
            </div>
        </div>
    </div>
</template>

<script>
    import {Tree} from 'element-ui'
    import {ModuleList} from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'

    let default_btn = [
        {
            id: 'add',
            name: '新增',
            icon: '#bd-plus',
            click: function () {
                // 获取按钮所在页面对象域
                // let pageObj = this.$parent.$parent //使用了e-scrollbar才需要
                let pageObj = this.$parent
                // 动态按钮依托调用对象所在的页面结构
                let tree = pageObj.$refs.tree
                // let tree = this.$parent.$refs.tree  原有页面结构下使用,无滚动框
                let nodeId = pageObj.currentNodeId
                let node = tree.getNode(nodeId)
                let listPath = this.$parent.$route.path
                this.$router.push({
                    path: `/module/tree/edit/${this.module.code}/add`,
                    query: {
                        data: {
                            returnPath: listPath,
                            parent: node.data,
                            fullPath: node.data.fullPath
                        }
                    }
                })
            }
        }, {
            id: 'edit',
            name: '修改',
            icon: '#bd-edit',
            click: function () {
                // 获取按钮所在页面对象域
                // let pageObj = this.$parent.$parent //使用了e-scrollbar才需要
                let pageObj = this.$parent
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('请选择一行！')
                    return
                }
                if (selection.length > 1) {
                    this.alert('最多只能选择一行数据！')
                    return
                }
                let id = selection[selection.length - 1].id
                let tree = pageObj.$refs.tree
                let nodeId = pageObj.currentNodeId
                let node = tree.getNode(nodeId)
                let parent = node.data
                let fullPath = parent.fullPath
                // 如果编辑的数据id，等于当前选中的树节点id，那么 parent 的数据为当前节点的父节点
                if (nodeId === id) {
                    parent = node.parent.data
                }
                let listPath = this.$parent.$route.path
                this.$router.push({
                    path: `/module/tree/edit/${this.module.code}/${id}`,
                    query: {
                        data: {
                            returnPath: listPath,
                            parent: parent,
                            fullPath: fullPath
                        }
                    }
                })
            }
        }, {
            id: 'delete',
            name: '删除',
            icon: '#bd-trash-alt-o',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('至少选择一行！')
                    return
                }

                this.$confirm('确定删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = {
                        mdCode: this.module.code,
                        ids: selection.map(e => e.id).join(',')
                    }
                    this.post(`${this.BASEURL}/jdbc/common/basecommondelete/delete.do`, params).then(res => {
                        if (res.hasOk) {
                            this.$refs.list.refresh()
                            this.alert('删除成功', 'success')
                            this.$emit('deleteAfter', selection)
                        } else {
                            this.alert('删除失败', 'error')
                        }
                    })
                })
            }
        },
        {
            id: "view",
            name: '查看',
            icon: '#bd-eye-o',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('请选择一行！')
                    return
                }
                if (selection.length > 1) {
                    this.alert('最多只能选择一行数据！')
                    return
                }
                let listPath = this.$parent.$route.path
                this.$router.push({
                    path: `/module/view/view/${this.module.code}/${selection[selection.length - 1].id}`,
                    query: {
                        data: {
                            returnPath: listPath
                        }
                    }
                })
            }
        }
    ]

    export default {
        components: {
            [Tree.name]: Tree,
            ModuleList
        },
        computed: {
        },
        data() {
            return {
                // 树文件操作icon:打开或者关闭
                treeIconList: [
                    "#bd-wenjian",
                    "#bd-wenjianguanbi",
                    "#bd-ziwenjian"
                ],
                hideTree: false,
                hackReset: false,
                props1: {
                    label: 'name',
                    isLeaf(data) {
                        return !(data.isParent || data.hasChild)
                    }
                },
                module: {
                    id: '',
                    code: '',
                    name: '',
                    moduleDisplay: [],
                    listJs: '',
                    listJsObj: {},
                    selectorData: [],
                    searchCondition: [],
                },
                defaultSearchParam: [],
                queryParams: {},
                sortname: '',
                sortorder: '',
                customDefaultParams: {},
                defaultButtons: default_btn,
                defaultExpandedKeys: [],  // 默认展开的树节点
                currentNodeId: '', // 默认加载列表的树节点id
                url: '',
                listTitle: '', // 列表title
            };
        },
        methods: {
            treeInfoCfg(parent) {
                if (this.module.listJsObj.treeInfoCfg) {
                    return this.module.listJsObj.treeInfoCfg.call(this, parent)
                }
                let mdCode = this.$route.params.mdCode
                let url = `${this.BASEURL}/jdbc/common/basecommontree/tree.do`
                let params = {mdCode: mdCode}
                if (parent) {
                    params.nodeId = parent.id
                }
                return {url: url, params: params}
            },
            /**
             * 成功更新了默认搜索条件返回true，否则返回false
             */
            updateDefaultSearchParam(key, value) {
                let param = this.defaultSearchParam.filter(p => p.name === key)
                if (param.length > 0 && param[0].value != value) {
                    param[0].value = value
                    this.queryParams = {defaultSearchParam: JSON.stringify(this.defaultSearchParam)}
                    this.$refs.moduleList.updateQueryParam('defaultSearchParam', JSON.stringify(this.defaultSearchParam))
                    return true
                }
                return false
            },
            setListTitleByTreeData(data) {
                if(typeof data === 'object' ) {
                    if(data.id == this.currentNodeId){
                        this.listTitle = data.name
                    }
                }else{
                    for (let key in data) {
                        if (data[key].id == this.currentNodeId) {
                            this.listTitle = data[key].name
                            break;
                        }
                    }
                }
            },
            loadNode(node, resolve) {
                let c = this.treeInfoCfg(node.data)
                this.get(c.url, c.params).then(res => {
                    this.setListTitleByTreeData(res)
                    if (node.level > 0) {
                        resolve(res)
                        return
                    }
                    
                    /*
                     * 加载根节点时，返回的数据会包含根节点的子节点，这是老版本的UI数据
                     * 因为目前（2019-2-13）后台获取数据的方法没有修改，所以这里进行过滤，
                     * 只返回根节点
                     */
                    resolve([res])
                    this.defaultExpandedKeys.push(res.id)
                    // if(node.level == 0){
                    //     this.currentNodeId = res.id
                    //     this.defaultExpandedKeys = res.id
                    //     this.nodeClick(res)
                    // }
                    // for (let key in res) {
                    //     resolve([res[key]])
                    //     break
                    //     // if (res[key].id === 'ROOT') {
                    //     //     resolve([res[key]])
                    //     //     break
                    //     // }
                    // }
                })
            },
            nodeClick(data) {
                this.listTitle = data.name
                this.currentNodeId = data.id
                // 模型列表 js 定义了更新默认查询参数的方法，通过方法的返回值更新
                if (this.module.listJsObj.defaultParam && this.module.listJsObj.defaultParam.getUpdateData) {
                    let result = this.module.listJsObj.defaultParam.getUpdateData.call(this, data)
                    for (let key in result) {
                        this.$refs.moduleList.updateQueryParam(key, result[key])
                    }
                    setTimeout(this.$refs.moduleList.reset)
                    return
                }

                // 如果模型 js 没有定义更新默认查询参数的方法，使用默认的配置
                if (this.updateDefaultSearchParam('parentId', data.id)) {
                    setTimeout(this.$refs.moduleList.reset)
                }
            },
            async newModuleInfo($router) {
                this.module = await ModuleUtils.listModuleCfg($router.params.mdCode)
                this.url = `${this.BASEURL}/jdbc/common/basecommontreelist/listJSON.do?mdCode=${this.module.code}&addParent=false`
                this.setDefaultParams($router)

                // 销毁树再重新渲染
                this.hackReset = false
                this.$nextTick(() => {
                    this.hackReset = true
                })
            },
            setDefaultParams($router) {
                // 重置搜索条件
                this.$refs.moduleList.resetQueryParam()

                let query = $router.query
                this.defaultExpandedKeys = []
                if (query.editData && query.editData.fullPath && query.editData.fullPath !== 'ROOT') {
                    let arr = query.editData.fullPath.split('-')
                    this.currentNodeId = arr.splice(arr.length - 1, 1)[0]
                    this.defaultExpandedKeys.push(...arr)
                }

                // 设置url上的默认查询参数
                for (let key in $router.query) {
                    // editData 是从编辑页返回列表页时带上的一些页面所需参数，不许提交到列表查询的请求中
                    if (key !== 'editData') {
                        this.$refs.moduleList.updateQueryParam(key, $router.query[key])
                    }
                }

                // 判断模型js是否有设置默认查询参数的方法，如果有通过该方法设置默认查询参数
                if (this.module.listJsObj.defaultParam && this.module.listJsObj.defaultParam.init) {
                    this.sortname = ''
                    this.sortorder = ''
                    let result = this.module.listJsObj.defaultParam.init.call(this, $router.params.symbol, {currentNodeId: this.currentNodeId});
                    for (let key in result) {
                        this.$refs.moduleList.updateQueryParam(key, result[key])
                    }
                    this.customDefaultParams = result
                } else {
                    // 如果没有使用，使用标准的默认查询参数
                    this.sortname = 'layerNum,priority'
                    this.sortorder = 'asc,asc'
                    this.defaultSearchParam = [{"name": "parentId", "type": "exact-match", "value": this.currentNodeId}]
                    this.$refs.moduleList.updateQueryParam('defaultSearchParam', JSON.stringify(this.defaultSearchParam))
                }

            },
            deleteAfter(data) {
                data.forEach(d => this.$refs.tree.remove(d.id))
            },
            nodeExpand(data) { //节点展开时图标替换
                document.getElementById(data.$treeNodeId).childNodes[0].attributes['xlink:href'].nodeValue = "#bd-wenjian"
            },
            nodeClose(data) { //节点关闭时图标替换
                document.getElementById(data.$treeNodeId).childNodes[0].attributes['xlink:href'].nodeValue = "#bd-wenjianguanbi"
            },
            toggleMenu () {
                this.hideTree = !this.hideTree
                if (!this.hideTree) {
                    this.$refs.moduleList.reset()
                }
            },
            /**
             * 获取当前的树节点数据
             */
            getCurrentNodeData() {
                let tree = this.$refs.tree
                let node = tree.getNode(this.currentNodeId)
                return node.data
            },
        },
        created() {
            this.$nextTick(() => {
                this.newModuleInfo(this.$route)
            })
        },
        // 暂时注释，这块是监听同个页面组件被重复使用时触发其更新数据，从目前项目看即使被重复使用依然可以数据更新，暂时不用这个
        // 避免重复请求
        // beforeRouteUpdate(to, from, next) {
        //     this.newModuleInfo(to)
        //     next()
        // }
    };
</script>

<style lang="scss" scoped>
    .hideTree-title {
        .titleWord {
            position: absolute;
            z-index: 1;
            color: $primary;
            width: 26px;
            font-size: 16px;
            margin: 0px auto;
            text-align: center;
            writing-mode: horizontal-tb;
            transition: all 0.4s;
            line-height: 24px;
            left: -24px;
            @for $i from 1 through 10 {
                &:nth-child(#{$i}) {
                    top: 40px + 10px + ($i - 1) * 26;
                }
            }
        }
    }
    .is-hide {
        .titleWord {
            left: 6px;
            @for $i from 1 through 10 {
                &:nth-child(#{$i}) {
                    transition-delay: $i * 0.12s;
                }
            }
        }
    }
</style>
