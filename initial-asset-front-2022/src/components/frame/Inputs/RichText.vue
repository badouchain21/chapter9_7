<template>
    <div class="richText">
        <template v-if="!isView">
            <vue-ueditor-wrap v-model="editData" :config="config"></vue-ueditor-wrap>
        </template>
        <template v-else>
            <div class="viewSection" v-html="editData"></div>
        </template>
    </div>
</template>

<script>
    import VueUeditorWrap from 'vue-ueditor-wrap'
    import GlobalConst from '@/service/global-const'
    export default {
        name: "rich-text",
        components: {
            VueUeditorWrap
        },
        props: ['value', 'isView', 'customCfg'],
        data () {
            return {
                editData: '',
                ready: false, // 编辑器是否已经准备好
                initValue: false, // 是不是已经设置过初始值
                containerId: '',
                editor: null,
                config: {
                    UEDITOR_HOME_URL: '/UEditor/',
                    toolbars: [
                        GlobalConst.richText.toolbars
                    ],
                    // 编辑器宽度
                    initialFrameWidth: '100%',
                    // 自动保存
                    enableAutoSave: false,
                    // 元素路径
                    elementPathEnabled: false,
                    // 字数统计
                    wordCount:false,
                },
            }
        },
        methods: {
            
        },
        created() {
            
        },
        watch: {
            editData (val) {
                this.$emit('input', val)
            },
            value (val) {
                this.editData = val
            },
        }
    }
</script>

<style scoped lang="scss">
.richText >>> {
    .viewSection {
        p {
            margin: 0;
            padding: 0;
        }
    }
}
</style>





