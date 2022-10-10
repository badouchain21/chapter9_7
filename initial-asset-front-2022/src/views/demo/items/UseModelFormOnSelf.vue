<template>
    <div class="padding bg-white">
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------<br/>
        <m-stander-edit
            class="noPadding"
            :mdCode="mdCodeVal"
            :detailId="detailIdVal">
        </m-stander-edit>
    </div>
</template>
<script>
    import MStanderEdit from '@/views/module/stander/Edit'
    export default {
        components: {
            MStanderEdit
        },
        data () { // 定义页面变量
            return {
                mdCodeVal: 'flwViewDictionary',
                detailIdVal: ''
            }
        },
        computed: {},
        methods: { // 定义函数
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {}
    }
</script>
<style lang='scss' scoped>

</style>