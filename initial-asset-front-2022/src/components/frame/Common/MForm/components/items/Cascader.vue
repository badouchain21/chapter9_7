<template>
    <div class="">
        <template v-if="!isView">
            <el-cascader
                v-model="tempValue"
                :disabled="disabled"
                :placeholder="placeholder || defaultPlaceholder"
                :options="options"
                :separator="separator"
                :props="{ 
                    expandTrigger: trigger,
                    value: valueField,
                    checkStrictly: checkStrictly
                }"
                @change="handleChange"
                :clearable="clearable">
            </el-cascader>
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
            // 值拼接与分割使用分隔符
            valueSeparator: {
                type: String,
                default: GlobalConst.cascader.separator
            },
            // 展示分割符
            separator: {
                type: String,
                default: '/'
            },
            // 标签名称
            label: {
                type: String,
                default: GlobalConst.form.label
            },
            // 预输入文本
            placeholder: {
                type: String,
                default: GlobalConst.form.placeholder.select
            },
            // 可选项数据源
            options: {
                type: Array,
                default: () => []
            },
            // 指定options中字段作为最终选中返回值
            valueField: {
                type: String,
                default: 'value'
            },
            // 选项弹窗触发方式
            trigger: {
                type: String,
                default: 'click'
            },
            // 是否严格的遵守父子节点不互相关联
            // 简单理解：
            //     设置false:选中父亲则所有子节点被勾选； 
            //     设置true则取消该关联，选中父亲后子节点不会被勾选
            //     设置true则即使所有子节点被选中，父亲也不会被勾选
            checkStrictly: {
                type: Boolean,
                default: false
            },
            // 关联键-键名
            relatedField: {
                type: [String, Boolean],
                default: false
            },
            // change事件
            change: {
                type: Function
            },
            // 是否支持清空选项
            clearable: {
                type: Boolean,
                default: false
            },
            // 输入框中是否显示选中值的完整路径
            showAllLevels: {
                type: Boolean,
                default: true
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
                    if (!this.isView) {
                        // 编辑状态需要将值处理为组件期望接收的数据格式
                        if (this.value && this.value.constructor === String) {
                            return this.value.split(this.valueSeparator)
                        }
                        return []
                    }
                    // 查看状态下直接使用该值进行展示
                    return this.value
                    
                },
                set (val) {
                    this.$emit('input', val.join(this.valueSeparator))
                }
            }
        },
        methods: { // 定义函数
            handleChange (value) {
                // 判断是否存在自定义change事件，存在则使用，不存在则调用默认的change事件
                if (!this.change) {
                    this.$emit('change', value)
                }
                this.change(value)
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {
            // 模拟请求本地json数据
            // this.get('/mock.js', {}).then(res => {
            //     debugger
            // })
        },
        watch: {
            // 监听值变化，设置关联字段值
            tempValue: {
                immediate: true,
                handler: function (newVal, oldVal) {
                    if (newVal) {
                        if (this.relatedField) {
                            // 当前存在关联键值或者关联键字段boolean为true时，回调事件
                            // this.relatedField: [Boolean, String],存在值时格式为'aa-bb-cc'
                            // newVal: [Array] eg:['11','22', '33']
                            this.$emit('setRelatedField', this.relatedField, newVal)
                        }
                    }
                }
            }
        }
    }
</script>
<style lang='scss' scoped>

</style>