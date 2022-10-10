<template>
    <div>
        <div class="m-tree-list limited">
            <div class="m-tree-main" :class="{'is-hide': hideTree}">
                <div class="hideTree-title">
                    <span class="titleWord" v-for="(i, index) in module.name" :key="index">{{i}}</span>
                </div>
                <div class="tree-title"  @click="toggleMenu">
                    <span class="name text-o-1"><i class="fa fa-sitemap"></i>{{module.name}}</span> 
                    <bd-icon :name='hideTree?"unfoldHor":"foldHor"'></bd-icon>   
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
                            <!-- <i :id="node.id"
                            :class="node.isLeaf ? 'last-node fa  fa-list-alt' : 'folder fa  fa-folder'"></i> -->
                            <bd-icon :id="node.id"
                                :name='node.isLeaf?"file":"folder-fill"'></bd-icon>
                            <span>{{node.label}}</span>
                        </div>
                    </el-tree>
                </el-scrollbar>
            </div>
            <div class="m-treeList" v-loading="isLoadingRightTree">
                <!-- <el-scrollbar wrap-class="scrollbar-wrapper"> -->
                    <permission_tree
                        ref="moduleList"
                        :module="module"
                        :queryParams="queryParams"
                        :defaultButtons="defaultButtons"
                        @changeLoadingStatus="changeLoadingStatus"
                        @deleteAfter="deleteAfter">
                    </permission_tree>
                <!-- </el-scrollbar> -->
            </div>
        </div>
    </div>
</template>

<script>
    import {Tree} from 'element-ui'
    import ModuleUtils from '@/js/ModuleUtils'
    import Permission from './Permission.vue'
    import GlobalConst from '@/service/global-const'
    let default_btn = [
        {
            id: 'add',
            name: '保存',
            icon: '#bd-save-o',
            isLoad: false,
            click: function (btnObj) {
                btnObj.isLoad = true
                // let aimObj = this.$parent.$parent  // 使用了e-scrollbar才需要
                let aimObj = this.$parent
                let params = {
                    "roleId": aimObj.currentNodeId,
                    "mids": this.getCheckedNodeKeys()
                }
                this.post(`${this.BASEURL}/auth/assign/roleresourceassignmenu/save.do`, params).then(res => {
                    if (res.hasOk) {
                        this.reset()
                        this.alert('操作成功', 'success')
                    } else {
                        this.alert('操作失败', 'error')
                    }
                    btnObj.isLoad = false
                }).catch(e => {
                    btnObj.isLoad = false
                })
            }
        }
    ]

    export default {
        components: {
            [Tree.name]: Tree,
            [Permission.name]: Permission,
        },
        data() {
            return {
                // 是否正在加载页面右侧菜单树状态
                isLoadingRightTree: true,
                // 树文件操作icon:打开或者关闭
                treeIconList: ["folderOpen-fill", "folder-fill", "file"],
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
                currentNodeId: 'ROOT', // 默认加载列表的树节点id
                menuPath: '',
            };
        },
        methods: {
            /**
             * 切换右侧菜单树的请求状态
             * status: 状态：默认false
             */
            changeLoadingStatus (status) {
                this.isLoadingRightTree = status
            },
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
                    this.$refs.moduleList.updateQueryParam(key, value)
                    return true
                }
                return false
            },
            setListTitleByTreeData (data) {
                if (typeof data === 'object') {
                    if (data.id === this.currentNodeId) {
                        this.listTitle = data.name
                    }
                } else {
                    for (let key in data) {
                        if (data[key].id === this.currentNodeId) {
                            this.listTitle = data[key].name
                            break
                        }
                    }
                }
            },
            /**
             * 加载节点
             */
            loadNode (node, resolve) {
                let c = this.treeInfoCfg(node.data)
                this.get(c.url, c.params).then(res => {
                    this.setListTitleByTreeData(res)
                    if (node.level > 0) {
                        resolve(res)
                        return
                    }
                    /**
                    * 加载根节点时，返回的数据会包含根节点的子节点，这是老版本的UI数据
                    * 因为目前（2019-2-13）后台获取数据的方法没有修改，所以这里进行过滤，
                    * 只返回根节点
                    */
                    resolve([res])
                    this.defaultExpandedKeys.push(res.id)
                })
            },
            nodeClick(data) {
                this.isLoadingRightTree = true
                this.currentNodeId = data.id
                // 如果模型 js 没有定义更新默认查询参数的方法，使用默认的配置
                if (this.updateDefaultSearchParam('parentId', data.id)) {
                    setTimeout(this.$refs.moduleList.reset)
                }
            },
            async newModuleInfo(mdCode, symbol, query) {
                this.module = await ModuleUtils.listModuleCfg(mdCode)
                this.setDefaultParams(symbol, query)

                // 销毁树再重新渲染
                this.hackReset = false
                this.$nextTick(() => {
                    this.hackReset = true
                })
            },
            setDefaultParams(symbol, query) {
                // 重置搜索条件
                this.$refs.moduleList.resetQueryParam()
                this.defaultExpandedKeys = []
                if (query.data && query.data.fullPath && query.data.fullPath !== 'ROOT') {
                    let arr = query.data.fullPath.split('-')
                    this.currentNodeId = arr.splice(arr.length - 1, 1)[0]
                    this.defaultExpandedKeys.push(...arr)
                } else {
                    this.defaultExpandedKeys.push('ROOT')
                }
                // 判断模型js是否有设置默认查询参数的方法，如果有通过该方法设置默认查询参数
                if (this.module.listJsObj.defaultParam && this.module.listJsObj.defaultParam.init) {
                    this.sortname = ''
                    this.sortorder = ''
                    let result = this.module.listJsObj.defaultParam.init.call(this, symbol, {currentNodeId: this.currentNodeId});
                    for (let key in result) {
                        this.$refs.moduleList.updateQueryParam(key, result[key])
                    }
                    this.customDefaultParams = result
                    return
                }

                // 如果没有使用，使用标准的默认查询参数
                this.sortname = 'priority'
                this.sortorder = 'asc'
                this.defaultSearchParam = [{"name": "parentId", "type": "exact-match", "value": this.currentNodeId}]
                this.$refs.moduleList.updateQueryParam('roleId', this.currentNodeId)
            },
            deleteAfter(data) {
                data.forEach(d => this.$refs.tree.remove(d.id))
            },
            nodeExpand(data) { //节点展开时图标替换
                document.getElementById(data.$treeNodeId).childNodes[0].attributes['xlink:href'].nodeValue = `${GlobalConst.icon.prefix}folderOpen-fill`
            },
            nodeClose(data) { //节点关闭时图标替换
                document.getElementById(data.$treeNodeId).childNodes[0].attributes['xlink:href'].nodeValue = `${GlobalConst.icon.prefix}folder-fill`
            },
            toggleMenu () {
                this.hideTree = !this.hideTree
            },
        },
        created() {
            if (this.$route.query.roleId) {
                this.currentNodeId = this.$route.query.roleId
            }
            this.$nextTick(() => {
                this.newModuleInfo(this.$route.params.mdCode, this.$route.params.symbol, this.$route.query)
            })
        },
        beforeRouteUpdate(to, from, next) {
            let mdCode = to.params.mdCode
            this.newModuleInfo(mdCode, to.params.symbol, to.query)
            next()
        }
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
