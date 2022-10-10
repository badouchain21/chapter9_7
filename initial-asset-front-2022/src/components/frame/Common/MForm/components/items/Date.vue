// 组件接收值：
    // disabled: Boolean(false) - 是否可编辑
    // value： String - 值
    // label： String - 标签
    // placeholder：String - 预输入文本
    // dateType: String - 日期类型
    //     1. date: 默认返回yyyy-mm-dd，显示为年月日
    //     2. year：默认返回yyyy，显示为年
    //     3. month： yyyy第几月
    //     4. week： yyyy第几周
    //     5. dates: 多日期
    // showFormat: String - 页面展示数据形式（如：yyyy年MM月dd日）
    // valueFormat: String - 值数据格式（如： yyyy-mm-dd）
<template>
    <div class="">
        <template v-if="!isView">
            <el-date-picker
                :disabled="disabled"
                v-model="tempValue"
                :type="dateType"
                :placeholder="placeholder || defaultPlaceholder"
                :format="showFormat"
                :value-format="valueFormat">
            </el-date-picker>
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
            disabled: {
                type: Boolean,
                default: false
            },
            value: {
                type: String,
                default: ''
            },
            label: {
                type: String,
                default: GlobalConst.form.label
            },
            placeholder: {
                type: String,
                default: GlobalConst.form.placeholder.text
            },
            dateType: {
                type: String,
                default: ''
            },
            showFormat: {
                type: String,
                default: ''
            },
            valueFormat: {
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
                type: String,
                default: 'date-fill'
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
        }
    }
</script>
<style lang='scss' scoped>

</style>