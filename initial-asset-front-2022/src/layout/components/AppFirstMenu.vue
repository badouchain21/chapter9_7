<template>
    <div class="project-menu">
        <div class="user-info" :class="{'hideUser': isCollapse}">
            <el-popover
                placement="top-start"
                trigger="click"
                width="100"
                popper-class="pad-0 marT-5 marL--3">
                <div v-for="(i, index) in operateList" :key="index">
                    <el-button type="primary" plain class="w-per-100" @click="operate(i.path)">
                        <bd-icon class="operaIcon" :name="i.icon"></bd-icon>
                        {{i.title}}
                    </el-button>
                </div>
                <div slot="reference">
                    <img class="headImg pointer" :src="makeImg(avatar, 'img')" :onerror="DEFAULT_AVATAR" alt="avater">
                    <div class="userName text-o-1 pointer" :title="name">
                        {{name}}
                    </div>
                </div>
                
            </el-popover>
        </div>
        <div class="first-menu font0">
            <div class="opeToggleSider d-ib v-t" @click="toggleSideBar">
                <bd-icon :name="isCollapse?operateIconList[0]:operateIconList[1]"></bd-icon>
            </div>
            <div class="menu-list bd-nav d-ib h-per-100" id="scrollDiv">
                <template v-if="permissionRoutes.length !== 0">
                    <div v-for="(i, index) in permissionRoutes" 
                        ref="firMenuItem"
                        @mouseover="hoverInMenuStyle(i, index)"
                        @mouseout="hoverOutMenuStyle(i, index)"
                        :key="index" 
                        class="menu-list-item bd-nav-item d-ib" 
                        :class="{'is-active': i.isActive}"
                        
                        @click="changeFirMenu(i, index)" >
                        <bd-icon :name="i.icon"></bd-icon>
                        <span class="bold">{{i.name}}</span>
                    </div>
                </template>
                <span 
                    class="bd-nav-bar" 
                    :style="firMenuStyle" 
                    @mouseover="hoverInMenuStyle()"
                    @mouseout="hoverOutMenuStyle()">
                </span>
            </div>
        </div>
    </div>  
</template>
<script>
import { mapGetters } from 'vuex'
export default {
    name: 'AppHeader',
    data () {
        return {
            memuList: [],
            firMenuStyle: {},
            hoverModel: {
                obj: null,
                index: 0
            },
            operateIconList: ["unfoldHor", "foldHor"],
            operateList: [
                { icon: "document-fill", path: '/Personal/UserData', title: "我的资料" },
                { icon: "myAvatar-fill", path: '/Personal/UserAvatar', title: "我的头像" },
                { icon: "password-fill", path: '/Personal/ModifyPwd', title: "修改密码" },
            ]
        }
    },
    props: {
    },
    computed: {
        ...mapGetters([
            'sidebar',
            'permissionRoutes',
            'name',
            'avatar'
        ]),
        isCollapse() {
            return !this.sidebar.opened
        }
    },
    methods: {
        operate (path) {
            this.$router.push({
                path: path
            })
        },
        toggleSideBar() {
            this.$store.dispatch('app/toggleSideBar')
        },
        changeFirMenu (item, index) {
            this.$store.dispatch('app/openSider', { withoutAnimation: false })
            this.$store.dispatch('permission/setRoutesActiveIndex', index)
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
            let item = this.$refs.firMenuItem[index]
            let left = item.offsetLeft
            let width = item.offsetWidth
            this.firMenuStyle = {
                'left': `${left - 2}px`,
                'bottom': 0,
                'width': `${width + 3}px`,
                'opacity': 1,
                'z-index': 1000
            }
        },
        hoverOutMenuStyle (obj, index) {
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
            let item = this.$refs.firMenuItem[index]
            let left = item.offsetLeft
            let width = item.offsetWidth
            this.firMenuStyle = {
                'left': `${left - 2 + width/2}px`,
                transform: 'translateX(-50%)',
                width: 0,
                bottom: 0,
            }
        }
    },
    created () {
    },
    mounted () {
        // 通过鼠标处理一级菜单的水平滚动 --start
        let scrollDiv = document.getElementById("scrollDiv")
        let element =  document.documentElement
        if (navigator.userAgent.indexOf("Chrome") > 0) {
            element = document.body
        }
        scrollDiv.addEventListener('DOMMouseScroll', handler, false)
        scrollDiv.addEventListener('mousewheel', handler, false)
        function handler (event) {
            let detail = event.wheelDelta || event.detail
            let moveForwardStep = 1
            let moveBackStep = -1
            let step = 0
            if (detail > 0) {
                    step = moveForwardStep*100
            } else {
                step = moveBackStep * 100
            } 
            scrollDiv.scrollLeft += step
            // console.log(scrollDiv.scrollLeft)
            if (parseInt(scrollDiv.scrollLeft) < 0) {
                scrollDiv.scrollLeft = 0
            }
        }
        // 通过鼠标处理一级菜单的水平滚动 --end
    }
}
</script>

<style lang='scss' scoped>
    @import "@/styles/variables.scss";
    $avatarWidth: 25px;
    $toggleSiderBtnWidth: 38px;
    $collapseTime: 0.3s;
    .project-menu {
        height: $appHeaderFirstMenuHeight;
        line-height: $appHeaderFirstMenuHeight;
        background-color: $primary;
        display: flex;
        color: #fff;
    }
    .user-info {
        width: $sideBarWidth;
        min-width: $sideBarWidth;
        transition: width $collapseTime;
        height: $appHeaderFirstMenuHeight;
        background: $firMenuBg;
        position: relative;
        align-items: center;
        padding: 0px $headTitlePadding;
        line-height: $appHeaderFirstMenuHeight;
        border-bottom: 1px solid rgba($white, 0.1);
        &.hideUser {
            width: $sideBarHideWidth;
            min-width: 0;
            // .userName {
            //     display: none;
            // }
            & + .first-menu{
                width: calc(100% - #{$sideBarHideWidth});
            }
        }
        .headImg{
            width: $avatarWidth;
            height: $avatarWidth;
            background: rgba($white, 0.5);
            border-radius: 50%;
            position: absolute;
            top: 0;
            bottom: 0;
            margin: auto;
            left: $headTitlePadding;
            border: 1px solid rgba($white, 0.3);
            border-radius: 50%;
        }
    }
    .first-menu {
        overflow: hidden;
        width: calc(100% - #{$sideBarWidth});
        transition: width $collapseTime;
    }
    
    .userName{
        color: #fff;
        width: 100%;
        padding-left: $avatarWidth + 5px;
    }
    .opeToggleSider {
        background: $firMenuBg;
        padding: 0px 10px;
        margin-left: 1px;
        font-size: $font;
        width: $toggleSiderBtnWidth;
    }
    .menu-list {
        font-size: $font;
        position: relative;
        width: calc(100% - #{$toggleSiderBtnWidth} - 1px);
        white-space: nowrap;
        overflow-x: hidden; // auto -> hidden
        overflow-y: hidden;
        // height: $appHeaderFirstMenuHeight + 18px;
        height: $appHeaderFirstMenuHeight;
        .menu-list-item {
            background: $firMenuBg;
            padding: 0px 25px;
            @media screen and (max-width: $screen-middle) {
                padding: 0px 18px;
            }
            margin-left: 1px;
            cursor: pointer;
            color: $menuText;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            &:hover {
                color: $menuActiveText;
            }
            &.is-active {
                background: $firMenuActiveBg;
                color: $menuActiveText;
            }
        }
    }
    .bd-nav-bar {
        position: absolute;
        left: 0;
        bottom: 0;
        width: 0;
        height: 4px;
        background-color: #fff;
        transition: all .2s linear, left .2s linear, width .6s linear, transform .2s linear;
        
    }
</style>
