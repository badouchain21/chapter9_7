<template>
    <div>
        <template v-if="!isView">
            <!-- textarea rows属性至少为2，为1样式有问题，并且1与普通input输入一致，那就没必要使用textarea -->
            <el-input 
                type="textarea"
                v-model="tempValue" 
                :disabled="disabled"
                :placeholder="placeholder || defaultPlaceholder"
                :rows="rows"
                :autosize="autoSizeFun"
                :maxlength="maxlength"
                :show-word-limit="Boolean(maxlength)"
                :class="{'noResize': !noResize}">
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
                num: 1,
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
            // 文本域高度
            rows: {
                type: Number,
                default: GlobalConst.textarea.rows
            },
            // // 最小行数
            // minRows: {
            //     type: Number,
            //     default: this.rows
            // }, 
            // // 最大行数
            // maxRows: {
            //     type: Number,
            //     default: GlobalConst.textarea.maxRows
            // },
            // 根据输入内容自动撑高高度
            autoSize: {
                type: Boolean,
                default: true
            },
            // 是否显示可自由拖动尺寸按钮
            noResize: {
                type: Boolean,
                default: false
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
            },
            // 根据输入内容自动撑高高度
            autoSizeFun () {
                // 设置了el-textarea属性autoSize为true时，rows属性会失效，需要做处理
                if (this.autoSize) {
                    if (this.autoSize.constructor === Boolean) {
                        // 传入autoSize属性为boolean值且为true时，使用rows属性
                        return { 
                            minRows: this.rows
                        }
                    }
                    if (this.autoSize.constructor === Object) {
                        // 当传入autoSize属性为对象时，则使用该对象进行渲染
                        return this.autoSize
                    }
                }
                return this.autoSize
            },
        },
        methods: { // 定义函数
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {
        }
    }
</script>
<style lang='scss' scoped>
</style>