// 组件接收值：
//     disabled: Boolean(false) - 是否可编辑
//     value: String - 值
//     name: String - 字段键名
//     options: Array([{text:'', id:''}]) - 下拉选择数组，对象下text为键，id为值,如果已知数据源则使用这个
//     request: Object({url:'', params: {}}) - options数据源，提供请求的url与params参数，如果需要请求数据则使用这个
<template>
    <div class="">
        <template v-if="!isView">
            <el-checkbox-group 
                v-model="tempValue" 
                :disabled="disabled">
                <el-checkbox
                    v-for="(i, index) in tempOptions" 
                    :key="index"
                    :label="i.id"
                    :name="name">
                    {{i.text}}
                </el-checkbox>
            </el-checkbox-group>
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
    import completeValue from '@/filter/items/complete-value'
    export default {
        components: {},
        data () { // 定义页面变量
            return {
                // 临时options数据，用于页面使用，避免改动prop报错
                tempOptions: [],
            }
        },
        props: {
            disabled: {
                type: Boolean,
                default: false
            },
            // 可能传入值形式 [1,2] 或者 '1,2'
            value: {
                type: [Array, String],
                default: () => []
            },
            name: {
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
                    if (!this.isView) {
                        // 组件需要接收值类型为Array，如果值为字符串则需要转化为数组格式
                        if (this.value && !(this.value instanceof Array)) {
                            return this.value.split(',')
                        }
                        return this.value || []
                    }
                    return this.value
                },
                set (val) {
                    this.$emit('input', val)
                }
            },
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