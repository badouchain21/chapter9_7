<!--
 * @Author: your name
 * @Date: 2020-06-17 10:38:16
 * @LastEditTime: 2020-07-30 17:10:34
 * @LastEditors: your name
 * @Description: In User Settings Edit
 * @FilePath: \badouFrameWork-front\src\layout\components\AppMain.vue
--> 
<template>
    <section class="app-main">
        <transition name="fade-transform" mode="out-in">
            <el-scrollbar :key="key" wrap-class="scrollbar-wrapper" class="h-per-100 hideX">
                <router-view :key="key" class="children-router"/>
            </el-scrollbar>
        </transition>
    </section>
</template>

<script>
export default {
    name: "AppMain",
    computed: {
        key () {
            return this.$route.path
        }
    }
}
</script>

<style scoped lang="scss">
@import "@/styles/variables.scss";
.app-main {
    height: calc(100vh - #{$appHeaderFirstMenuHeight} - #{$appHeaderHeight});
    width: 100%;
    position: relative;
    overflow: hidden;
}
.fixed-header + .app-main {
    padding-top: 50px;
}
.children-router {
    margin: 0 $space $space !important;
    height: calc(100vh - #{$space} - #{$appHeaderHeight} - #{$locationHeight});
    // height: calc(100% - #{$space});
    border-radius: 4px;
    overflow: auto;
    &.defaultBg {
        box-shadow: 0px 0px 3px 2px rgba(16,16,16,0.1);
    }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
    .fixed-header {
        padding-right: 15px;
    }
}
</style>
