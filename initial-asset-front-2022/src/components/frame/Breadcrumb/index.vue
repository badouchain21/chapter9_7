<template>
    <div class="breadcrumb">
        <bd-icon name="location-fill" class="mainSvgColor"></bd-icon>
        <el-breadcrumb class="app-breadcrumb" separator="/">
        <transition-group name="breadcrumb">
            <el-breadcrumb-item v-for="(item, index) in location" :key="index">
                <span v-if="!item.path" class="no-redirect">{{item.title}}</span>
                <a v-else @click.prevent="handleLink(item.path)">{{item.title}}</a>
            </el-breadcrumb-item>
        </transition-group>
    </el-breadcrumb>
    </div>
    
</template>

<script>
import { mapGetters } from 'vuex'
import pathToRegexp from 'path-to-regexp'
import storage from '@/utils/storage'
import store from '@/store'

export default {
    data() {
        return {
            levelList: [],
            // 是否找到当前点击的匹配菜单，用于面包屑展示
            /**
             * 为什么需要这个字段
             * 1. 路径面包屑的展示依赖于路由菜单，只有存在与路由菜单中的路径匹配才可以展示
             * 2. 一旦由这些页面进入新增编辑等的页面，在路由菜单中就找不到
             * 3. 所以逻辑处理为：存储上一次的面包屑，匹配不到就使用历史面包屑
             * 4. 遍历的时候当找到匹配时使用该字段终止查找，避免其他设置再次干扰
             */
            hasFind: false
        }
    },
    computed: {
        ...mapGetters([
            'permissionRoutes',
            'routesActiveIndex',
            'location'
        ])
    },
    watch: {
        $route() {       
            this.getBreadcrumb()
        }
    },
    created () {
        this.getBreadcrumb()
    },
    methods: {
        addBreadcrumbItem () {
            this.levelList.push({
                path: '/module/stander/list/djMeetingInfo_zhongxinzulilunxuexi/placeholder?searchParam=%5B%7B"name"%3A"typeCode","type"%3A"exact-match","value"%3A"zhongxinzulilunxuexi"%7D%5D&resourceId=402881696bf8fdfe016c42e32a040dd1',
                title: '新增'
            })
        },
        handleLink (path) {
            this.$router.replace({path: path})
        },
        findMenuListByPath (list, findPath) {
            for (let index = 0; index < list.length; index++) {
                if (this.hasFind) {
                    break
                }
                let i = list[index]
                // 找到与当前页面路由匹配的页面模块,获取其级联地址,地址在前面已经构造好(menuPathName会拥有最初始到它这一级别的所有菜单路径)
                if (i.path === findPath) {
                    this.hasFind = true
                    this.makeLevelList(i.menuPathName)
                    break
                } else {
                    if (index === list.length - 1) {
                        // this.levelList = storage.getObj('menuLevelList')
                        this.levelList = store.getters.location
                        break
                    }
                }
                if (i.children.length > 0 && !this.hasFind) {
                    this.findMenuListByPath(i.children, findPath)
                }
            }
        },
        makeLevelList (namePath) {
            this.levelList = namePath.split('-').map(i => {
                return {
                    title: i,
                    path: '',
                    allowJump: false
                }
            })
            this.levelList[this.levelList.length - 1].path = this.$route.fullPath
            // this.addBreadcrumbItem()
        },
        getBreadcrumb() {
            // 获取当前活跃的一级菜单下面的子级模块
            let activeRootMenu = this.permissionRoutes[this.routesActiveIndex]
            // 获取活跃一级菜单名称
            let menuPathName = activeRootMenu.menuPathName
            // 设置一级菜单
            this.makeLevelList(menuPathName) 
            // 参数为活跃一级菜单下的子菜单数据 以及 当前页面路由地址
            if (activeRootMenu.hasOwnProperty('children') && activeRootMenu.children.length > 0) {
                this.findMenuListByPath(activeRootMenu.children, this.$route.path)
            }
            // 存储菜单面包屑
            // storage.setObj('menuLevelList', this.levelList)
            store.commit('settings/SET_LOCATION', this.levelList)
            this.hasFind = false
        },
        isDashboard(route) {
            const name = route && route.name
            if (!name) {
                return false
            }
            return name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
        },
        pathCompile(path) {
            // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
            const { params } = this.$route
            var toPath = pathToRegexp.compile(path)
            return toPath(params)
        },
        
    }
}
</script>

<style lang="scss" scoped>
@import "@/styles/variables.scss";
.breadcrumb {
    .mainSvgColor {
        color: $primary;
        width: 1.5em;
        height: 1.5em;
        margin-right: 5px;
    }
    .app-breadcrumb.el-breadcrumb {
        display: inline-block;
        font-size: 14px;
        
        .no-redirect {
            color: #97a8be;
            cursor: text;
        }
    }
}

</style>
