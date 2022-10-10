<template>
    <div class="">
        <template v-if="!isView">
            <el-select
                :disabled="disabled"
                v-model="tempValue" 
                filterable
                :multiple="multiple"
                :collapse-tags="isCollapse"
                :placeholder="placeholder || defaultPlaceholder"
                :remote="isOptionsFromRequest"
                :clearable="clearable"
                :loading ="loading && isOptionsFromRequest"
                @change="change"
                @visible-change="visibleChange"
                popper-class="m-form-select-popper">
                <template>
                    <el-option
                        v-for="(i, index) in tempOptions"
                        :key="index"
                        :label="i.text"
                        :value="i.id">
                    </el-option>
                </template>
            </el-select>
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
    import GlobalConst from '@/service/global-const'
    import completeValue from '@/filter/items/complete-value'
    export default {
        components: {},
        data () { // 定义页面变量
            return {
                // 临时options数据，用于页面使用，避免改动prop报错
                tempOptions: [],
                loading: false,
                isOptionsFromRequest: false
            }
        },
        props: {
            // 设置组件是否可编辑，默认可编辑Boolean(false)，非必须
            disabled: {
                type: Boolean,
                default: false
            },
            // 可能传入值形式 [1,2] 或者 '1,2'
            value: {
                type: [String, Number, Array],
            },
            // 是否支持多选
            multiple: {
                type: Boolean,
                default: false
            },
            // 组件使用标签名称，非必须
            label: {
                type: String,
                default: GlobalConst.form.label
            },
            // 预输入文本，非必须
            placeholder: {
                type: String,
                default: GlobalConst.form.placeholder.select
            },
            // 组件下拉数据源，若已知，可直接传入，若需要接口动态请求，请看下一个request参数
            // 数据格式为[{text:'', id:''}]
            options: {
                type: Array,
                default: () => []
            },
            // 当下拉数据来源接口时使用，使用这个则不需要使用options属性
            // 数据格式为｛url: '', params: {}｝
            request: {
                type: Object,
                default: () => {
                    return {
                        url: '',
                        params: ''
                    }
                }
            },
            // option数据字典编码
            dicCode: {
                type: String,
                default: ''
            },
            // 是否为查看状态
            isView: {
                type: Boolean,
                default: false
            },
            // 多选时是否以文本折叠展示
            isCollapse: {
                type: Boolean,
                default: true
            },
            // 选项是否可清空
            clearable: {
                type: Boolean,
                default: true
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
                        // 非查看模式
                        if (this.multiple) {
                            // 多选时，组件需要接收值类型为Array，如果值为字符串则需要转化为数组格式
                            if (this.value && !(this.value instanceof Array)) {
                                return this.value.split(',')
                            }
                            return this.value || []
                        }
                    }
                    return this.value
                },
                set (val) {
                    this.$emit('input', val)
                }
            }
        },
        methods: { // 定义函数
            /**
             * 选中值发生变化时触发
             */
            change (value) {
                this.$emit('change', value)
            },
            /**
             * 下拉框出现/隐藏时触发
             */
            visibleChange (visibleStatus) {
                this.$emit('visible-change', visibleStatus)
            },
            /**
             * 多选模式下移除tag时触发
             * TODO:目前无效
             */
            // removeTag1 (removeTagVal) {
            //     this.$emit('remove-tag', removeTagVal)
            // },
            /**
             * 可清空的单选模式下用户点击清空按钮时触发
             */
            clear () {
                this.$emit('clear')
            },
            /**
             * 当 input 失去焦点时触发
             */
            blur (event) {
                this.$emit('blur', event)
            },
            /**
             * 当 input 获得焦点时触发
             */
            focus (event) {
                this.$emit('focus', event)
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {
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
            },
            // 监听请求对象是否存在，存在即使用其请求options数据
            request: {
                immediate: true,
                handler: function (newVal, oldVal) {
                    // 查看状态不需要执行此逻辑
                    if (!this.isView && newVal && newVal.url) {
                        // 使用接口数据作为选项数组来源
                        this.isOptionsFromRequest = true
                        this.loading = true
                        getDic(newVal).then(res => {
                            this.tempOptions = res
                            this.loading = false
                        }).catch(err => {
                            this.loading = false
                        })
                    }
                }
            },
            // 监听数据字典编码，存在即使用其请求options数据
            dicCode: {
                immediate: true,
                handler: function (newVal, oldVal) {
                    // 查看状态不需要执行此逻辑
                    if (!this.isView && newVal) {
                        // 使用接口数据作为选项数组来源
                        this.isOptionsFromRequest = true
                        this.loading = true
                        let requestObj = { params: { [GlobalConst.dic.codeName]: newVal } }
                        getDic(requestObj).then(res => {
                            this.tempOptions = res
                            this.loading = false
                        }).catch(err => {
                            this.loading = false
                        })
                    }
                }
            }
        }
    }
</script>
<style lang='scss' scoped>

</style>