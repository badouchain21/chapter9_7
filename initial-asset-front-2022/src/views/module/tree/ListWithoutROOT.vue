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
            name: '??????',
            icon: '#bd-plus',
            click: function () {
                // ?????????????????????????????????
                // let pageObj = this.$parent.$parent //?????????e-scrollbar?????????
                let pageObj = this.$parent
                // ???????????????????????????????????????????????????
                let tree = pageObj.$refs.tree
                // let tree = this.$parent.$refs.tree  ???????????????????????????,????????????
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
            name: '??????',
            icon: '#bd-edit',
            click: function () {
                // ?????????????????????????????????
                // let pageObj = this.$parent.$parent //?????????e-scrollbar?????????
                let pageObj = this.$parent
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('??????????????????')
                    return
                }
                if (selection.length > 1) {
                    this.alert('?????????????????????????????????')
                    return
                }
                let id = selection[selection.length - 1].id
                let tree = pageObj.$refs.tree
                let nodeId = pageObj.currentNodeId
                let node = tree.getNode(nodeId)
                let parent = node.data
                let fullPath = parent.fullPath
                // ?????????????????????id?????????????????????????????????id????????? parent ????????????????????????????????????
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
            name: '??????',
            icon: '#bd-trash-alt-o',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('?????????????????????')
                    return
                }

                this.$confirm('??????????????????', '??????', {
                    confirmButtonText: '??????',
                    cancelButtonText: '??????',
                    type: 'warning'
                }).then(() => {
                    let params = {
                        mdCode: this.module.code,
                        ids: selection.map(e => e.id).join(',')
                    }
                    this.post(`${this.BASEURL}/jdbc/common/basecommondelete/delete.do`, params).then(res => {
                        if (res.hasOk) {
                            this.$refs.list.refresh()
                            this.alert('????????????', 'success')
                            this.$emit('deleteAfter', selection)
                        } else {
                            this.alert('????????????', 'error')
                        }
                    })
                })
            }
        },
        {
            id: "view",
            name: '??????',
            icon: '#bd-eye-o',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('??????????????????')
                    return
                }
                if (selection.length > 1) {
                    this.alert('?????????????????????????????????')
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
                // ???????????????icon:??????????????????
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
                defaultExpandedKeys: [],  // ????????????????????????
                currentNodeId: '', // ??????????????????????????????id
                url: '',
                listTitle: '', // ??????title
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
             * ???????????????????????????????????????true???????????????false
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
                     * ???????????????????????????????????????????????????????????????????????????????????????UI??????
                     * ???????????????2019-2-13????????????????????????????????????????????????????????????????????????
                     * ??????????????????
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
                // ???????????? js ???????????????????????????????????????????????????????????????????????????
                if (this.module.listJsObj.defaultParam && this.module.listJsObj.defaultParam.getUpdateData) {
                    let result = this.module.listJsObj.defaultParam.getUpdateData.call(this, data)
                    for (let key in result) {
                        this.$refs.moduleList.updateQueryParam(key, result[key])
                    }
                    setTimeout(this.$refs.moduleList.reset)
                    return
                }

                // ???????????? js ?????????????????????????????????????????????????????????????????????
                if (this.updateDefaultSearchParam('parentId', data.id)) {
                    setTimeout(this.$refs.moduleList.reset)
                }
            },
            async newModuleInfo($router) {
                this.module = await ModuleUtils.listModuleCfg($router.params.mdCode)
                this.url = `${this.BASEURL}/jdbc/common/basecommontreelist/listJSON.do?mdCode=${this.module.code}&addParent=false`
                this.setDefaultParams($router)

                // ????????????????????????
                this.hackReset = false
                this.$nextTick(() => {
                    this.hackReset = true
                })
            },
            setDefaultParams($router) {
                // ??????????????????
                this.$refs.moduleList.resetQueryParam()

                let query = $router.query
                this.defaultExpandedKeys = []
                if (query.editData && query.editData.fullPath && query.editData.fullPath !== 'ROOT') {
                    let arr = query.editData.fullPath.split('-')
                    this.currentNodeId = arr.splice(arr.length - 1, 1)[0]
                    this.defaultExpandedKeys.push(...arr)
                }

                // ??????url????????????????????????
                for (let key in $router.query) {
                    // editData ????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (key !== 'editData') {
                        this.$refs.moduleList.updateQueryParam(key, $router.query[key])
                    }
                }

                // ????????????js?????????????????????????????????????????????????????????????????????????????????????????????
                if (this.module.listJsObj.defaultParam && this.module.listJsObj.defaultParam.init) {
                    this.sortname = ''
                    this.sortorder = ''
                    let result = this.module.listJsObj.defaultParam.init.call(this, $router.params.symbol, {currentNodeId: this.currentNodeId});
                    for (let key in result) {
                        this.$refs.moduleList.updateQueryParam(key, result[key])
                    }
                    this.customDefaultParams = result
                } else {
                    // ??????????????????????????????????????????????????????
                    this.sortname = 'layerNum,priority'
                    this.sortorder = 'asc,asc'
                    this.defaultSearchParam = [{"name": "parentId", "type": "exact-match", "value": this.currentNodeId}]
                    this.$refs.moduleList.updateQueryParam('defaultSearchParam', JSON.stringify(this.defaultSearchParam))
                }

            },
            deleteAfter(data) {
                data.forEach(d => this.$refs.tree.remove(d.id))
            },
            nodeExpand(data) { //???????????????????????????
                document.getElementById(data.$treeNodeId).childNodes[0].attributes['xlink:href'].nodeValue = "#bd-wenjian"
            },
            nodeClose(data) { //???????????????????????????
                document.getElementById(data.$treeNodeId).childNodes[0].attributes['xlink:href'].nodeValue = "#bd-wenjianguanbi"
            },
            toggleMenu () {
                this.hideTree = !this.hideTree
                if (!this.hideTree) {
                    this.$refs.moduleList.reset()
                }
            },
            /**
             * ??????????????????????????????
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
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????
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
