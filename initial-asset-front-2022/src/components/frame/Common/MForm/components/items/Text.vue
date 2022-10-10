<template>
    <div class="">
        <template v-if="!isView">
            <el-input
                type="text"
                :disabled="disabled"
                v-model="tempValue"
                :placeholder="placeholder || defaultPlaceholder" 
                :show-password="isPassword"
                :maxlength="maxlength"
                :show-word-limit="Boolean(maxlength)"
                clearable>
                <template slot="prepend" v-if="prepend">{{prepend}}</template>
                <template slot="append" v-if="append">{{append}}</template>
            </el-input>
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
    import GlobalConst from '@/service/global-const'
    import completeValue from '@/filter/items/complete-value'
    export default {
        components: {},
        data () { // 定义页面变量
            return {
                // num: 1,
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
            // 标签名称
            label: {
                type: String,
                default: GlobalConst.form.label
            },
            // 预输入文本
            placeholder: {
                type: String,
                default: GlobalConst.form.placeholder.text
            },
            // 最大允许输入长度
            maxlength: {
                type: Number
            },
            // 是否为密码
            isPassword: {
                type: Boolean,
                default: false
            },
            // 前置元素
            prepend: {
                type: String,
                default: ''
            },
            // 后置元素
            append: {
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
            // 默认预输入文本
            defaultPlaceholder () {
                return this.placeholder + this.label
            },
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
        },
    }
</script>
<style lang='scss' scoped>

</style>