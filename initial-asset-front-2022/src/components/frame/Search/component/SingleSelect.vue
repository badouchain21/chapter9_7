<!--
  下拉单选框
  JSON 配置格式：{"id":"typeText", "name":"类型", "selectType":9, "valueText":"全部", "isExtended":false}
-->
<template>
    <div class="search-value">
        <m-search-option
            :name="selector.name"
            :valueText="valueText"
            :isShowSearch="selectOption.length > 0"
            :isShowClear="Boolean(value)"
            @change="change"
            @reset="reset">
            
            <template v-slot:content="scope">
                <div class="m-single-area">
                    <div 
                        v-for="item in selectOption.filter(i => i.text.includes(scope.searchOptionWord))"
                        :key="item.id"
                        class="s-item"
                        :class="{'is-select': item.isSelect }"
                        @click="setActive(item)">
                        {{item.text}}
                    </div>
                </div>
                <!-- 原本使用radio-group，但性能异常，使用卡顿，所以暂时使用上面的自定义 -->
                <!-- <el-radio-group
                    v-model="value"
                    @change="change">
                    <el-radio
                        class="d-b"
                        v-for="(item, index) in selector.searchCondition.filter(i => i.text.includes(scope.searchOptionWord))"
                        :key="index"
                        :label="item.id">
                        {{item.text}}
                    </el-radio>
                </el-radio-group> -->
            </template>
        </m-search-option>

    </div>
</template>

<script>
import MSearchOption from '@/components/frame/Common/MSearchOption'
import { isStrEmpty } from '@/utils/index'

export default {
    name: "s-single-select",
    components: {
        MSearchOption
    },
    props: {
        selector: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            value: '',
            text: '',
            selectOption: []
        }
    },
    computed: {
        valueText() {
            return this.text || "全部";
        }
    },
    methods: {
        // 设置活跃项
        setActive (itemObj) {
            this.selectOption.forEach(i => {
                this.$set(i, 'isSelect', false)
            })
            //拼装选择器对象，将旧的选择项和新的选择项以及对应的id传到上一层change事件中，方便做下拉框监听事件
            let selectorObj = {
                id: this.selector.id,
                old: {
                    value: this.value,
                    text: this.text
                },
                new: {
                    value: itemObj.id,
                    text: itemObj.text
                }
            }
            this.$set(itemObj, 'isSelect', true)
            this.value = itemObj.id
            this.text = itemObj.text
            this.change(selectorObj)
        },
        change (selectorObj) {
            this.$emit('change', selectorObj)
        },
        // searchBar组件会调用该函数获取当前单选组件的搜索参数值
        getSelectorValue () {
            if (!isStrEmpty(this.value)) {
                return {
                    name: this.selector.id,
                    type: "exact-match",
                    value: this.value
                };
            }
        },
        reset () {
            this.value = ''
            this.text = ''
            this.selectOption.forEach(i => {
                this.$set(i, 'isSelect', false)
            })
        },
        setValue (value) {
            if (isStrEmpty(value)) {
                this.value = ''
                this.text = ''
                return
            }
            let condition = this.selector.searchCondition
            if (!condition) return

            for (let i in condition) {
                if (condition[i].id == value) {
                    this.value = condition[i]
                }
            }
        },
        getSelectorInfo () {
            return this.selector
        },
    },
    watch: {
        selector: {
            immediate: true,
            handler: function (newVal) {
                // 考虑值为0的情况，不直接判断状态位
                if (newVal.value === '' || newVal.value === undefined) {
                    // 值为空时，清空已有数据
                    this.reset()
                }
                let options = newVal.searchCondition || []
                this.selectOption = JSON.parse(JSON.stringify(options))
                this.selectOption.forEach(i => {
                    this.$set(i, 'isSelect', false)
                })
                // 有value默认值时，触发选中该值
                if (newVal.value !== '' && newVal.value !== undefined) {
                    let activeVal = newVal.value.toString()
                    let selectObj = this.selectOption.find(i => i.id === activeVal)
                    this.setActive(selectObj)
                }               
            }
        },
        /**
         * 监听下拉列表
         * @param val
         */
        'selector.searchCondition' (val) {
            this.selectOption = val
        },
        /**
         * 监听选中值
         * @param val
         */
        'selector.value' (val) {
            // 问题：自定义页面的 val 不是一个对象，没有 .value 属性
            // 解决：直接交给 PFilter 处理
            if (typeof val !== 'object' && val.value == null && val.value === undefined) {
                return
            }
            if (val.value === '' || val.value === undefined) {
                // 值为空时，清空已有数据
                this.reset()
            }
            // 有value默认值时，触发选中该值
            if (val.value !== '' && val.value !== undefined) {
                let activeVal = val.value.toString()
                let selectObj = this.selectOption.find(i => i.id === activeVal)
                this.setActive(selectObj)
            }
        }
    }
}
</script>

<style scoped lang="scss">
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
