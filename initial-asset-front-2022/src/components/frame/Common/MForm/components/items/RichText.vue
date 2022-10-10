<template>
    <div class="richText">
        <template v-if="!isView">
            <vue-ueditor-wrap
                class="editStatus"
                v-model="tempValue" 
                :config="config">
            </vue-ueditor-wrap>
        </template>
        <template v-else>
            <bd-icon 
                :name="iconName" 
                v-if="tempValue && iconName" 
                class="fill icon-small">
            </bd-icon>
            <div class="viewSection" v-html="tempValue"></div>
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
        props: {
            value: {
                type: String,
                default: ''
            },
            // 是否为查看状态
            isView: {
                type: Boolean,
                default: false
            },
            // 图标名称：用于查看页面展示
            iconName: {
                type: String
            }
        },
        computed: {
            // 单项数据流原因，不可直接修改值，需要用临时值做过度使用
            tempValue: {
                get () {
                    return this.value
                },
                set (val) {
                    this.$emit('input', val)
                }
            }
        },
        data () {
            return {
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
                    // 初始容器高度
                    // initialFrameHeight: 240,
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