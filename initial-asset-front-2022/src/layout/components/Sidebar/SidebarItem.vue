<template>
    <div v-if="!item.hidden" class="menu-wrapper">
        <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
            <app-link v-if="onlyOneChild.meta" :to="{path:resolvePath(onlyOneChild.path, onlyOneChild), query:onlyOneChild.query}">
                <el-menu-item :index="resolvePath(onlyOneChild.path, onlyOneChild)" :class="{'submenu-title-noDropdown':!isNest}">
                    <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="onlyOneChild.meta.title" />
                </el-menu-item>
            </app-link>
        </template>
        
        <el-submenu v-else ref="subMenu" :index="item.code" popper-append-to-body>
            <template slot="title">
                <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title" />
            </template>
            <sidebar-item
                v-for="(child, index) in item.children"
                :key="index"
                :is-nest="true"
                :item="child"
                :base-path="resolvePath(child.path, child)"
                class="nest-menu">
            </sidebar-item>
        </el-submenu>
    </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import Item from './Item'
import AppLink from './Link'
import FixiOSBug from './FixiOSBug'

export default {
    name: 'SidebarItem',
    components: { Item, AppLink },
    mixins: [FixiOSBug],
    props: {
        // route object
        item: {
            type: Object,
            required: true
        },
        isNest: {
            type: Boolean,
            default: false
        },
        basePath: {
            type: String,
            default: ''
        }
    },
    data() {
        // To fix https://github.com/PanJiaChen/vue-admin-template/issues/237
        // TODO: refactor with render function
        this.onlyOneChild = null
        return {}
    },
    methods: {
        hasOneShowingChild(children = [], parent) {
            const showingChildren = children.filter(item => {
                if (item.hidden) {
                    return false
                } else {
                    // Temp set(will be used if only has one showing child)
                    this.onlyOneChild = item
                    return true
                }
            })
            // When there is only one child router, the child router is displayed by default
            if (showingChildren.length === 1) {
                return true
            }
            // Show parent if there are no child router to display 
            if (showingChildren.length === 0) {
                this.onlyOneChild = { ... parent, noShowingChildren: true }
                return true
            }
            return false
        },
        resolvePath(routePath, obj) {
            if (isExternal(routePath)) {
                return routePath
            }
            if (isExternal(this.basePath)) {
                return this.basePath
            }
            let finaPath = routePath ? path.resolve(this.basePath, routePath) : this.basePath
            if (this.hasOneShowingChild(this.item.children,this.item) && (!this.onlyOneChild.children||this.onlyOneChild.noShowingChildren)&&!this.item.alwaysShow) {
                finaPath = this.basePath
            }
            return finaPath
        }
    },
}
</script>
<style lang="scss" scoped>
// 解决样式异常问题,可以选中菜单，但是样式穿透不了子元素
.router-link-active {
    background: $subMenuActiveBg !important;
}
</style>
