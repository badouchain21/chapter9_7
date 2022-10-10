<!--
 * @Author: your name
 * @Date: 2020-06-17 10:38:16
 * @LastEditTime: 2020-07-24 14:35:46
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \badouFrameWork-front\src\components\frame\Search\component\MultipleSelect.vue
--> 
<!--
  下拉多选框
  JSON配置格式：{"id":"type", "name":"类型", "selectType":0, "valueText":"全部", "isExtended":false}
-->
<template>
    <div class="search-value">
        <m-search-option
            :name="selector.name"
            :valueText="valueText"
            :isShowSearch="selector.searchCondition && selector.searchCondition.length > 0"
            :isShowClear="checkList && checkList.length > 0"
            @change="change"
            @reset="reset">
            <template v-slot:content="scope">
                <el-checkbox-group
                    v-model="checkList"
                    @change="change">
                    <el-checkbox
                        class="d-b"
                        v-for="item in selector.searchCondition.filter(i => i.text.includes(scope.searchOptionWord))"
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
    import { Popover, Checkbox, CheckboxGroup } from 'element-ui'
    import MSearchOption from '@/components/frame/Common/MSearchOption'
    export default {
        name: "s-multiple-select",
        components: {
            [Popover.name]: Popover,
            [Checkbox.name]: Checkbox,
            [CheckboxGroup.name]: CheckboxGroup,
            MSearchOption
        },
        props: {
            selector: {
                type: Object,
                required: true
            }
        },
        data () {
            return {
                dataText: '',
                checkList: []
            }
        },
        computed: {
            valueText () {
                return this.dataText || '全部'
            },
        },
        methods: {
            change () {
                let condition = this.selector.searchCondition
                if (condition) {
                    let name = condition.filter(c => this.checkList.indexOf(c.id) > -1).map(c => c.text)
                    this.dataText = name.join(',')
                }
                this.$emit('change')
            },
            getSelectorValue () {
                if (this.checkList.length > 0) {
                    return {name: this.selector.id, type: 'other-query', value: this.checkList.join(';')}
                }
            },
            reset () {
                this.checkList = []
                this.dataText = '全部'
            },
            setValue (value) {
                if (!value) {
                    return
                }
                let condition = this.selector.searchCondition
                if (condition) {
                    let arr = value.split(';')
                    this.checkList.push(...arr)
                    this.dataText = condition.filter(c => this.checkList.indexOf(c.id) > -1)
                        .map(c => c.text).join(',')
                }
            },
            getSelectorInfo () {
                return this.selector
            }
        },
    }
</script>

<style scoped lang="scss">

</style>
