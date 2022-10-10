<template>
    <div class="m-height">
        <div class="m-height-top">
            <el-button
                v-for='(i, index) in buttons' 
                :key="index" 
                v-btnBg="i"
                v-if="!i.isHide" 
                :loading="i.isLoad !== undefined && i.isLoad"
                @click='exeMethod(i)'>
                <bd-icon
                    v-if="(i.isLoad === undefined) || (i.isLoad !== undefined && !i.isLoad)"
                    :name="i.icon">
                </bd-icon>
                {{ i.name }}
            </el-button>
        </div>
        <div class="m-height-main">
            <!-- v-if="module.id" 存在module时才去加载数据 -->
            <el-tree
                :data="permissionDatas"
                ref="perTree"
                show-checkbox
                default-expand-all
                node-key="id"
                highlight-current
                :props="defaultProps">
                <span slot-scope="{ node, props2 }">
                    <bd-icon class="fill" :id="node.id"
                        :name='node.isLeaf?"file":"folder-fill"'>
                    </bd-icon>
                    <span>{{node.label}}</span>
                </span>
            </el-tree>
        </div> 
        
    </div>
</template>

<script>
    import { Tree } from 'element-ui'
    import GlobalConst from '@/service/global-const'
    export default {
        name: 'permission_tree',
        components: {
        [Tree.name]: Tree,
        },
        props: {
            module: {
                type: Object,
                default() {
                    return {
                        id: '',
                        code: '',
                        name: '',
                        moduleDisplay: [],
                        listJs: '',
                        listJsObj: {},
                        selectorData: [],
                        searchCondition: [],
                    }
                }
            },
            defaultButtons: {
                type: Array
            }
        },
        data() {
            return {
                queryParams: {},
                searchParam: '',
                buttons: [],
                permissionDatas: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                checkedKeys: [],
                props1: {
                label: 'name',
                isLeaf(data) {
                    return !(data.isParent || data.hasChild)
                }
            },
        }
    },
    computed: {
        url() {
            return `${this.BASEURL}/auth/assign/roleresourceassignmenu/tree.do`
        },
        btnMethod() {
            let obj = {}
            this.buttons.forEach(btn => obj[btn.id] = btn.click)
            return obj
        },
    },
    methods: {
        getCheckedNodeKeys() {
            let arr = this.$refs.perTree.getHalfCheckedKeys().concat(this.$refs.perTree.getCheckedKeys());
            return arr.join(',')
        },
        exeMethod(i) {
            this.btnMethod[i.id].call(this, i)
        },
        randerBtn(newButtons) {
            // 获取默认按钮
            let btns = Object.assign([], this.defaultButtons)
            this.buttons = btns
        },
        refresh () {
            this.checkedKeys = []
            let params = {
                "roleId": this.queryParams.parentId ? this.queryParams.parentId : this.$route.query.roleId ? this.$route.query.roleId :'ROOT'
            }
            this.post(this.url, params).then(res => {
                this.permissionDatas = res
                this.permissionDatas.forEach(t => {this.setCheckedTree(t)})
                this.$refs.perTree.setCheckedKeys(this.checkedKeys)
                /*
                * 如果获取到的数据为空，并且当前页面不是第一页时，加载上一页的数据
                * 删除当前页所有记录的时候会出现这个情况
                */
                if (this.permissionDatas.length === 0) {
                    this.refresh()
                } else {
                    try {
                        // 请求完数据时回调设置请求状态为false
                        this.$emit('changeLoadingStatus', false)
                    } catch (e) {
                        console.error('Permission.vue : refresh() -- error')
                    }
                    
                }

            })
        },
        setCheckedTree (data) {
            if (data.ischecked && data.children.length == 0) {
                this.checkedKeys.push(data.id)
            }
            if (data.children.length > 0) {
                data.children.forEach(tt => this.setCheckedTree(tt));
            }
        },
        reset() {
            this.refresh()
        },
        updateQueryParam(key, value) {
            this.queryParams[key] = value
        },
        removeQueryParam(key) {
            delete this.queryParams[key]
        },
        resetQueryParam() {
            this.queryParams = {}
        },
        nodeExpand(data) { //节点展开时图标替换
            document.getElementById(data.$treeNodeId).childNodes[0].attributes['xlink:href'].nodeValue = `${GlobalConst.icon.prefix}folderOpen-fill`
        },
        nodeClose(data) { //节点关闭时图标替换
            document.getElementById(data.$treeNodeId).childNodes[0].attributes['xlink:href'].nodeValue = `${GlobalConst.icon.prefix}folder-fill`
        },
    },
    watch: {
        module: {
            deep: true,
            handler: function () {
                this.randerBtn(this.module.listJsObj.buttons)
                if (this.$refs.list) {
                    Promise.resolve().then(this.$refs.list.reset)
                }
                    
            }
        }
    },
    created () {
        this.refresh()
    }
}
</script>

<style lang="scss" scoped>
.m-height {
    height: 100%;
    .m-height-top {
        height: 40px;
    }
    .m-height-main {
        height: calc(100% - 40px);
        overflow: auto;
    }
}
</style>
