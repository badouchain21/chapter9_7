<template>
    <div :class="{'has-logo':showLogo}">
        <logo v-if="showLogo" :collapse="isCollapse" ></logo>
        <el-scrollbar wrap-class="scrollbar-wrapper">
            <el-menu
                :default-active="activeMenu"
                :collapse="isCollapse"
                :unique-opened="true"
                :collapse-transition="false"
                mode="vertical">
                <sidebar-item  
                    ref="menuItem" 
                    v-for="(route, index) in (permissionRoutes[routesActiveIndex]).children" 
                    :key="index" 
                    :item="route" 
                    :base-path="route.path" 
                    @mouseover.native="hoverInMenuStyle(route, index)"
                    @mouseout.native="hoverOutMenuStyle(route, index)"></sidebar-item>
                <div 
                    class="bd-nav-bar-ver" 
                    :style="firMenuStyle"
                    @mouseover="hoverInMenuStyle()">
                </div>
            </el-menu>
        </el-scrollbar>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
    components: { SidebarItem, Logo },
    computed: {
        ...mapGetters([
            'sidebar',
            'permissionRoutes',
            'routesActiveIndex'
        ]),
        activeMenu() {
            const route = this.$route
            const { meta, path } = route
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
                return meta.activeMenu
            }
            return this.dealByModule(path)
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo
        },
        variables() {
            return variables
        },
        isCollapse() {
            return !this.sidebar.opened
        }
    },
    data () {
        return {
            firMenuStyle: {},
            hoverModel: {
                obj: null,
                index: 0
            },
        }
    },
    methods: {
        /** 
         * 左侧菜单使用的是elemntui的navMenu组件,组件中每个子菜单拥有标识index,在组件的最顶级统一控制下拉展示选中的子菜单
         * 这里默认的子菜单使用的标识为path路径,所以顶级使用的是完整的路径path进行控制
         * path是用于匹配左侧菜单的,但是由左侧菜单跳转出去的页面(例如模型编辑页面)将无法标识
         * 例如:左侧菜单树中有列表页面,会选中左侧的某个模块,但是从列表页面跳转去的编辑页以及详情页都无法对应左侧菜单任何一个,会导致无法选中
         * 实际: 从列表页面出去的编辑,详情与新增,在左侧应该对应为列表模块的选中,表示该页面是由此模块引申的
        */
        dealByModule (path) {
            let lastIndex = path.lastIndexOf('\/')
            let lastPath = path.slice(lastIndex + 1)
            return path
            
        },
        hoverInMenuStyle (obj, index) {
            // 特殊处理，避免鼠标在菜单白色选中条忽闪忽闪 --start
            if (obj) {
                this.hoverModel = {
                    obj: obj,
                    index: index
                }
            } else {
                obj = this.hoverModel.obj
                index = this.hoverModel.index
            }
            // 特殊处理，避免鼠标在菜单白色选中条忽闪忽闪 --end
            let item = this.$refs.menuItem[index].$el.offsetTop
            this.firMenuStyle = {
                'top': `${item}px`,
                'opacity': 1,
                'height': '56px'
            }
        },
        hoverOutMenuStyle (obj, index) {
            let item = this.$refs.menuItem[index].$el.offsetTop
            this.firMenuStyle = {
                'height': 0,
                'opacity': 0,
                'top': `${item + 56/2}px`,
                transform: 'translateY(-50%)',
            }
        }
        
    }
}
</script>
<style lang="scss" scoped>
    .bd-nav-bar-ver {
        width: 4px;
        height: 0;
        background: #fff;
        position: absolute;
        z-index: 2;
        top: 0;
        transition: all .2s linear, transform .2s linear;
    }
</style>
