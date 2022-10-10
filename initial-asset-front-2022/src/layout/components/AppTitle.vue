<template>
    <div class="project-header">
        <img class="project-header-bg" :src="makeImg(settingHeadBgImg)" alt="">
        <div class="title p-r">
            <div class="s-logo" v-if="settingLogo">
                <img class="s-logo-img" :src="makeImg(settingLogo, 'img')" alt="">
            </div>
            {{settingTitle}}
        </div>
        <div class="project-header-r floatR d-f p-r">
            <!-- <select-search></select-search> -->
            <div class="d-ib operaIconArea">
                <div class="icon-item" v-for="(i, index) in operateList" :key="index" :title="i.title">
                    <bd-icon class="operaIcon" :name="i.name"  @click="operate(i.fun)"></bd-icon>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import variables from '@/styles/variables.scss'
import { mapGetters } from 'vuex'
export default {
    name: 'AppHeader',
    data () {
        return {
            operateList: [
                { name: "message-fill", fun: null, title: "我的消息" },
                { name: "help-fill", fun: null, title: "疑问" },
                { name: "logOut-fill", fun: 'logout', title: "注销"},
            ]
        }
    },
    computed: {
        ...mapGetters([
            'settingTitle',
            'settingHeadBgImg',
            'settingLogo'
        ])
    },
    props: {
    },
    methods: {
        async logout() {
            await this.$store.dispatch('user/logout')
            this.$router.push(`/login`)
            // 用户主动注销的时候,不携带当前页面路径,因为可能存在用户是要切换身份,应该要回到首页,而且不确定原有页面地址参数数据是否还有效
            // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
        },
        operate (methodName) {
            this[methodName]()
        }
    }
}
</script>

<style lang='scss' scoped>
    @import "@/styles/variables.scss";
    .project-header >>> {
        position: relative;
        padding: 0 $headTitlePadding - 4px;
        height: $appHeaderTitleHeight;
        @media screen and (max-width: $screen-middle) {
            height: $appHeaderTitleHeight - $screenDis
        }
        color: #fff;
        font-size: 30px;
        background: linear-gradient(to bottom, $primary, $appTitleGradientBg);
        // background: url("../../assets/project/topBg.png") no-repeat;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        background-position: center center;
        overflow: hidden;
        .project-header-bg {
            height: 100%;
            width: 100%;
            position: absolute;
            left: 0;
            top: 0;
            z-index: 0;
        }
        .title{
            font-size: 22px;
            font-weight: 600;
            letter-spacing: 1px;
            height: $appHeaderTitleHeight;
            line-height: $appHeaderTitleHeight;
            @media screen and (max-width: $screen-middle) {
                font-size: 21px;
                height: $appHeaderTitleHeight - $screenDis;
                line-height: $appHeaderTitleHeight - $screenDis;
            }
            display: inline-block;
            .s-logo {
                display: inline-block;
                height: $appHeaderTitleHeight;
                min-width: $appHeaderTitleHeight - 10px;
                vertical-align: top;
                position: relative;
                margin-right: -4px;
                .s-logo-img {
                    height: $appHeaderTitleHeight - 10px;
                    width: $appHeaderTitleHeight - 10px;
                    position: absolute;
                    top: 0;
                    bottom: 0;
                    margin: auto;
                }
            }
        }
        .operaIconArea{
            display: flex;
            align-items: center;
            height: $appHeaderTitleHeight;
            .icon-item {
                text-align: center;
                min-width: 40px;
                position: relative;
                .bd-icon {
                    font-size: 20px;
                    transition: all 0.6s;
                    vertical-align: -0.1em;
                    &:hover {
                        font-size: 25px;
                    }
                }
            }
        }
        
    }

</style>
