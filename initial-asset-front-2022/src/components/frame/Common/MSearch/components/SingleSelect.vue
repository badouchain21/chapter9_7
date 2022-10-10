<template>
    <div class="">
        <m-search-option
            :name="label"
            :isShowSearch="option.length > 0"
            :valueText="valueText"
            :isShowClear="Boolean(showValue) && isAllowClearAll"
            @clear="clear">
            <template v-slot:content="scope">
                <div class="m-single-area">
                    <div 
                        v-for="item in option.filter(i => i.text.includes(scope.searchOptionWord))"
                        :key="item.id"
                        class="s-item"
                        :class="{'is-select': item.isSelect }"
                        @click="setActive(item, true)">
                        {{item.text}}
                    </div>
                </div>
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
            // 是否允许清空所有已选项：默认显示允许清空按钮
            isAllowClearAll: {
                type: Boolean,
                default: true
            }
        },
        data () { // 定义页面变量
            return {
            }
        },
        computed: {
            valueText: {
                get: function () {
                    return this.getText(this.value) || this.defaultText
                }
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
                if (!val) {
                    return ''
                }
                return this.getActiveItem(val).text
            },
            /**
             * @desciption: 设置对象模块为选中状态
             * @param {object} itemObj option对象
             * @param {boolean} isUpdateSearchbar 是否触发父组件的搜索事件
             */
            setActive (itemObj, isUpdateSearchbar = false) {
                // 重置所有状态
                this.clearSelect()
                // 设置选中目标
                this.$set(itemObj, 'isSelect', true)
                // 需要触发父组件searchbar的搜索事件
                if (isUpdateSearchbar) {
                    //拼装选择器对象，将旧的选择项和新的选择项以及对应的id传到上一层change事件中，方便做下拉框监听事件
                    let selectorObj = {
                        id: this.id,  // 字段名称
                        old: {
                            value: this.value,
                            text: this.valueText
                        },
                        new: {
                            value: itemObj.id,
                            text: itemObj.text
                        }
                    }
                    this.updateSearchbar(itemObj.id, selectorObj)
                }
            },
            /**
             * @desciption: 根据value值返回对应的option对象模块
             * @param {String} val:val值
             * @return {Object} 匹配val值的option下对象
             */
            getActiveItem (val = '') {
                return this.option.find(i => i.id === val) || false
            },
            // 清空数据已选状态
            clearSelect () {
                this.option.forEach(i => {
                    this.$set(i, 'isSelect', false)
                })
            },
            // 清空值
            clear () {
                // 重置所有状态
                this.clearSelect()
                // 更新父组件searchbar值
                this.updateSearchbar()
            },
            // 更新父组件searchbar值以及刷新列表数据
            updateSearchbar (val = '', selectorObj = {}) {
                // 更新父组件值
                this.$emit('input', val)
                // 触发父组件请求列表
                this.$emit('search', selectorObj)
            }
        },
        // 可访问当前this实例
        created () {
        },
        // 挂载完成，可访问DOM元素
        mounted () {},
        watch: {
            value: {
                immediate: true,
                handler: function (newVal, oldVal) {
                    if (newVal) {
                        // 对于传入值的处理，只需要做渲染选中的状态，不需要根据值请求列表数据，分析如下：
                        // 1. 初始默认有值时searchbar会直接取默认值进行列表请求，所以这里不需要再次触发，只需要做简单UI处理，根据值设置被选中项的活跃状态
                        // 2. 如果是手动操作了下拉选中值，会在操作事件中触发searchbar请求最新数据，依旧会将最新值传回来，所以这里还是只做接收，不需要触发请求
                        let activeItem = this.getActiveItem(newVal)
                        if (activeItem) {
                            // 设置下拉数据的活跃选中项，false表示不需要触发父组件searchbar的请求数据函数，如果手动点击选择下拉数据时才需要触发
                            this.setActive(activeItem, false)
                        } else {
                            // 重置所有状态
                            this.clearSelect()
                        }
                    } else {
                        // 重置所有状态
                        this.clearSelect()
                    }
                }
            }
        }
    }
</script>
<style lang='scss' scoped>
.m-single-area {
    .s-item {
        padding: 4px 4px;
        cursor: pointer;
        
        &.is-select {
            background: $primaryLight;
            color: $primary;
        }
        &:hover {
            background: $primaryLight;
        }
    }
}
</style>