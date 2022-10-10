<template>
    <div class="">
        <m-search-option
            :name="label"
            :isShowSearch="option.length > 0"
            :valueText="valueText"
            :isShowClear="Boolean(showValue) && isAllowClearAll"
            @clear="clear">
            <template v-slot:content="scope">
                <el-checkbox-group
                    v-model="checkList"
                    @change="setActive">
                    <el-checkbox
                        class="d-b"
                        v-for="item in option.filter(i => i.text.includes(scope.searchOptionWord))"
                        :key="item.id"
                        :title="item.text"
                        :label="item.id">
                        {{item.text}}
                    </el-checkbox>
                </el-checkbox-group>
            </template>
        </m-search-option>
    </div>
</template>
<script>
    import MSearchOption from '@/components/frame/Common/MSearchOption'
    import { isStrEmpty } from '@/utils/index'
    import { request } from '@/service/request'

    export default {
        components: {
            MSearchOption
        },
        props: {
            // label标签字段
            label: {
                type: String,
                default: ''
            },
            // 字段名称
            id: {
                type: String,
                default: ''
            },
            // 字段值
            value: {
                default: ''
            },
            // 值对应显示文本
            defaultText: {
                type: String,
                default: '全部'
            },
            // 下拉数据源数组
            option: {
                type: Array,
                default: () => []
            },
            // 多选数据的分割符号，用于最终值转换与值还原
            divisionMark: {
                type: String,
                default: ';'
            },
            // 是否允许清空所有已选项：默认显示允许清空按钮
            isAllowClearAll: {
                type: Boolean,
                default: true
            }
        },
        data () { // 定义页面变量
            return {
                checkList: []
            }
        },
        computed: {
            valueText () {
                return this.getText(this.value) || this.defaultText
            },
            // 页面展示value
            showValue: {
                get: function () {
                    // 获取父级组件传入值
                    return this.value
                },
                set: function (val) {
                    // 调用父级update事件进行value值更新
                    this.$emit('input', val)
                }
            }
        },
        methods: { // 定义函数
            // 文本的展示需要关联value，这样value的变更才会直接影响展示文本内容
            getText (val = '') {
                if (!val) return ''
                return this.getChooseText(this.getChooseValList(val))
            },
            getChooseValList (val) {               
                if (!val) return ''
                return val.split(this.divisionMark)
            },
            getChooseText (valList) {
                if (!valList) return ''
                let textList = []
                valList.forEach(i => {
                    this.option.forEach(j => {
                        if (i === j.id) {
                            textList.push(j.text)
                        }
                    })
                })
                return textList.join(this.divisionMark)
            },
            // setText (list) {
            //     // 定义选中文本
            //     let textList = []
            //     list.forEach(i => {
            //         this.option.forEach(j => {
            //             if (i === j.id) {
            //                 textList.push(j.text)
            //             }
            //         })
            //     })
            //     // 更新展示文本
            //     this.text = textList.join(this.divisionMark)
            // },
            setActive (val, isUpdateSearchbar = false) {
                let tempValue = val.join(this.divisionMark)
                // this.setText(val)
                // 更新值
                this.updateSearchbar(tempValue)
            },
            // 更新父组件searchbar值以及刷新列表数据
            updateSearchbar (val = '') {
                // 更新父组件值
                this.$emit('input', val)
                // 触发父组件请求列表
                this.$emit('search', val)
            },
            // 清空值
            clear () {
                // 重置所有状态
                this.checkList = []
                // 更新父组件searchbar值
                this.updateSearchbar()
            }
        },
        watch: {
            value: {
                immediate: true,
                handler: function (newVal) {
                    // 这里只处理默认传入值的展示
                    if (newVal) {
                        this.checkList = newVal.split(this.divisionMark)
                    } else {
                        // 重置所有状态
                        this.checkList = []
                    }
                }
            }
        }
    }
</script>
<style lang='scss' scoped>
</style>