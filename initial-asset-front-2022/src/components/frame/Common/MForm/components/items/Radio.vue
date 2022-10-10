<template>
    <div class="">
        <template v-if="!isView">
            <el-radio-group 
                v-model="tempValue"
                :disabled="disabled">
                <el-radio 
                    v-for="(i, index) in tempOptions" 
                    :key="index"
                    :label="i.id">
                    {{i.text}}
                </el-radio>
            </el-radio-group>
        </template>
        <template v-else>
            <bd-icon 
                :name="iconName" 
                v-if="tempValue && iconName" 
                class="fill icon-small">
            </bd-icon>
            <span>{{tempValue | completeValue}}</span>
        </template>
    </div>
</template>
<script>
    import getDic from '@/service/get-dic'
    import { request } from '@/service/request'
    export default {
        components: {},
        data () { // 定义页面变量
            return {
                // 临时options数据，用于页面使用，避免改动prop报错
                tempOptions: [],
            }
        },
        props: {
            // 设置组件是否可编辑，默认可编辑Boolean(false)，非必须
            disabled: {
                type: Boolean,
                default: false
            },
            // 组件默认值传入
            value: {
                type: String,
                default: ''
            },
            options: {
                type: Array,
                default: () => []
            },
            request: {
                type: Object,
                default: () => {
                    return {
                        url: '',
                        params: ''
                    }
                }
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
        methods: { // 定义函数
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {
            if (!this.isView) {
                // 当前不为查看状态
                if (this.options.length === 0) {
                    let requestObj = this.request
                    getDic(requestObj).then(res => {
                        this.tempOptions = res
                    })
                }
            }
        },
        watch: {
            options: {
                immediate: true,
                handler: function (newVal, oldVal) {
                    if (newVal && newVal.length > 0) {
                        // TODO:兼容写法，现在修改为id/text（推荐使用），旧版期望传入值为label/value（废弃使用）
                        this.tempOptions = newVal.map(i => {
                            return {
                                id: i.value || '',
                                text: i.label || '',
                                ...i,
                            }
                        })
                    }
                }
            }
        }
    }
</script>
<style lang='scss' scoped>

</style>