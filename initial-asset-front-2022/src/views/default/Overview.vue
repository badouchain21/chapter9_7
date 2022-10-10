<template>
    <div class="overview">
        <!-- <card-count :list="countList" class="marginB" type="theme-gradient"></card-count>
        <card-count :list="countList" class="marginB" type="theme-full"></card-count> -->
        <card-count :list="countList" class="marginB"></card-count>
        <m-card
            :title="listObj.title"
            :icon="listObj.icon">  
            <el-button type="primary" class="cardBtn" slot="right">优先选择单位</el-button>
            <m-list
                class="padding"
                :hasIndex="false"
                :isSelection="true"
                :showPagination="true"
                :dataList="tableData"
                :fieldList="fieldList"
                tableHeight="400px">
            </m-list>
        </m-card>
    </div>
</template>

<script>
import CardCount from '@/components/frame/Common/CardCount'
import MCard from '@/components/frame/Common/MCard'
import MList from '@/components/frame/Common/MList/index'
import { mapGetters } from 'vuex'
export default {
    name: 'Overview',
    components: {
        CardCount,
        MCard,
        MList
    },
    // 路由守卫函数，即将进入页面前
    beforeRouteEnter (from, to, next) {
        next(vm => {
            // 通过 `vm` 访问组件实例
            let homeUrl = vm.$store.getters.homeUrl
            if (homeUrl) {
                // 当前项目存在配置的主页面，跳转该页面
                next({ path: homeUrl })
            } else {
                // 继续当前
                next()
            }
            
        })  
    },
    data () {
        return {
            // 顶部卡片
            countList: [
                { label: '当前用户数', iconName: "dangqianyonghu", num: 50 },
                { label: '今日登录数', iconName: "jinridenglu", num: 345 },
                { label: '本月登录数', iconName: "benyuedenglu", num: 135 },
                { label: '库中项目数', iconName: "kuzhongxiangmu", num: 1135 }
            ],
            // 列表模块
            listObj: {
                title: '10个最近登录系统用户日志',
                icon: 'pillar-fill',
                list: []
            },
            fieldList: [
                { name: 'date', label: '日期', width: 120 },
                { name: 'name', label: '姓名', width: 120 },
                { name: 'address', label: '地址', width: '' },
                { name: 'family', label: '户籍', width: '' },
                { name: 'remark', label: '备注', width: '' }
            ],
            tableData: [
                {
                    date: '2016-05-02',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1518 弄',
                    family: '广东汕头',
                    remark: '这里备注情况'
                }, {
                    date: '2016-05-04',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1517 弄',
                    family: '广东佛山',
                    remark: '备注点东西'
                }, {
                    date: '2016-05-01',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1519 弄',
                    family: '广东湛江',
                    remark: '备注'
                }, {
                    date: '2016-05-03',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1516 弄',
                    family: '广东佛山',
                    remark: '块备注'
                }, {
                    date: '2016-05-04',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1517 弄',
                    family: '广东佛山',
                    remark: '备注点东西'
                }, {
                    date: '2016-05-01',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1519 弄',
                    family: '广东湛江',
                    remark: '备注'
                }, {
                    date: '2016-05-01',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1519 弄',
                    family: '广东湛江',
                    remark: '备注'
                }, {
                    date: '2016-05-03',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1516 弄',
                    family: '广东佛山',
                    remark: '块备注'
                }, {
                    date: '2016-05-04',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1517 弄',
                    family: '广东佛山',
                    remark: '备注点东西'
                }, {
                    date: '2016-05-01',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1519 弄',
                    family: '广东湛江',
                    remark: '备注'
                }
            ]
        }
    },
    methods: {

    },
    // created () {
    //     if(this.userInfo.indexUrl){
    //       this.$router.push({
    //         path:  "/"+this.userInfo.indexUrl
    //       })
    //     }
    // }
}
</script>

<style lang="scss" scoped>
.overview {
    padding: 0px 2px;
}
</style>
