<template>
    <div class="padding">
        <!-- <m-filter :formItemList="formItemList">
            <el-button type="primary" @click="search">
                <bd-icon name="search"></bd-icon>搜索
            </el-button>
            <el-button type="primary" @click="reset">
                <bd-icon name="reset"></bd-icon>重置
            </el-button>
        </m-filter> -->
        
        <m-tab
            class="marginB"
            border
            :tabData="tabData"
            :activeTab.sync="activeTab"
            @tab-click="handleClick">
            <div slot="fun">
                <el-button type="primary">上一年</el-button>
            </div>
            <div slot="first">第一个模块</div>
            <div slot="second">第二个模块</div>
            <div slot="third">第三个模块</div>
            <div slot="fourth">第四个模块</div>
        </m-tab>   
        <m-card
            border
            title="测试卡片嵌套tab"
            icon="pillar-fill">  
            <m-tab
                :tabData="tabData"
                :activeTab.sync="activeTab"
                @tab-click="handleClick">
                <div slot="first">第一个模块</div>
                <div slot="second">第二个模块</div>
                <div slot="third">第三个模块</div>
                <div slot="fourth">第四个模块</div>
            </m-tab>
        </m-card>
    </div>
</template>

<script>
import MFilter from '@/components/frame/Common/MFilter'
import MTable from '@/components/frame/Common/MTable'
import MList from '@/components/frame/Common/MList/index'
import MTab from '@/components/frame/Common/MTab'
import MCard from '@/components/frame/Common/MCard'
export default {
    components: {
        MFilter,
        MTable,
        MTab,
        MCard,
        MList
    },
    data () {
        return {
            formItemList: [
                { type: 'text', name: 'searchWord', label: '', value: '', placeholder: '请输入内容', style: '' },
                { type: 'date', name: 'startTime', label: '', value: '2019-12-22', placeholder: '选择起始日期', style: '' },
                { type: 'date', name: 'endTime', label: '', value: '2020-01-01', placeholder: '选择结束日期', style: '' },
                { type: 'select', name: 'searchType', label: '', value: '', placeholder: '请选择类型', style: '' },
                { type: 'daterange', name: 'timeRange', label: '', value: '', placeholder: '时间范围', style: {width: "240px"} }
            ],
            tabData: [
                { label: '用户管理', name: 'first' },
                { label: '配置管理', name: 'second' },
                { label: '角色管理', name: 'third' },
                { label: '定时任务补偿', name: 'fourth' },
            ],
            activeTab: 'first'
        }
    },
    methods: {
        // 搜索函数
        search () {
            alert(JSON.stringify(this.formItemList))
        },
        reset () {
            this.formItemList.forEach(i => i.value='')
        },
        // 切换tab事件
        handleClick (tab, event) {
            console.log(tab.name)
        }
    }
}
</script>

<style lang="scss" scoped>
    .fixPosition {
        display: flex;
        align-items: center;
        & > div {
            & + div {
                margin-left: $space;
            }
            & + .el-button {
                margin-left: $space;
            }
        }
    }
</style>
