<template>
    <div class="m-search" v-if="data.length > 0">
        <div class="s-l" :class="{'isCover': extendList.length === 0}">
            <div
                class="m-search-item"
                v-for="(i, index) in dataList"
                :key="index"
                v-if="i.isSelect">
                <template>
                    <!-- 下拉多选 -->
                    <search-multiple-select
                        v-if="i.type === 0"
                        :ref="i.id"
                        :label="i.name"
                        :id="i.id"
                        v-model="i.value"
                        :option="i.option"
                        @search="search">
                    </search-multiple-select>
                    <!-- 文本框 -->
                    <search-text
                        v-else-if="i.type === 1"
                        :ref="i.id"
                        :label="i.name"
                        :id="i.id"
                        v-model="i.value"
                        @search="search">
                    </search-text>
                    <!-- 日期选择 -->
                    <search-date 
                        v-else-if="i.type === 5"
                        :ref="i.id"
                        :label="i.name"
                        :id="i.id"
                        v-model="i.value"
                        @search="search">
                    </search-date>
                    <!-- 单选框 -->
                    <search-single-select 
                        v-else-if="i.type === 9"
                        :ref="i.id"
                        :label="i.name"
                        :id="i.id"
                        v-model="i.value"
                        :option="i.option"
                        @search="singleSearch">
                    </search-single-select>
                </template>
            </div>
            <!-- 当搜索栏子项存在时才显示操作按钮 -->
            <div class="d-ib" v-if="dataList.length > 0">
                <el-button 
                    class="marginB"
                    v-btnBg="i"
                    v-for="(i, index) in btnList"
                    :key="index"
                    @click="exeMethod(i)">
                    <bd-icon :name="i.icon"></bd-icon>{{i.name}}
                </el-button>
            </div>
        </div>
        <div class="s-r" v-if="extendList.length !== 0">
            <el-popover
                placement="bottom-end"
                popper-class="m-popover-checkbox"
                trigger="hover">
                <el-checkbox
                    v-for="(i, index) in extendList" 
                    :key="index"
                    v-model="i.isSelect"
                    @change="changeExtendItem">
                    {{i.name}}
                </el-checkbox>
                <div class="s-label-primary" slot="reference">
                    更多
                    <i class="el-icon-arrow-down"></i>
                </div>
            </el-popover>
        </div>
    </div>
</template>
<script>
    import SearchText from './components/Text'
    import SearchSingleSelect from './components/SingleSelect'
    import SearchMultipleSelect from './components/MultipleSelect'
    import SearchDate from './components/Date'
    import GlobalConst from '@/service/global-const'
    export default {
        components: {
            SearchText,
            SearchSingleSelect,
            SearchMultipleSelect,
            SearchDate
        },
        data () { // 定义页面变量
            return {
            }
        },
        props: {
            // 搜索栏集合数据
            // [{
            //     id: 字段名称
            //     isMulti: 是否为更多项
            //     name: 字段label
            //     type: 搜索类型
            //     optionUrl: option地址，用于请求option数据（下拉数据）
            //     option: option数据
            // }]
            data: {
                type: Array,
                default: () => []
            },
            // 默认展示文本
            selectValueText: {
                type: String,
                default: "全部"
            },
            // 单选下拉联动事件集合Array
            selectChange: {
                type: Array,
                default: () => []
            }
        },
        data () {
            return {
                btnList: [
                    { id: 'search', name: '搜索', icon: 'search', method: 'search' },
                    { id: 'reset', name: '重置', icon: 'reset', method: 'resetAndSearch' }
                ]
            }
        },
        computed: {
            sortList () {
                let _list = this.data
                let defaultIndex = GlobalConst.searchBar.sortIndex
                /**
                 * 排序函数专用
                 * @param prop[String] 排序所依赖的对象字段名
                 */
                function sortByProp (prop) {
                    return function (obj1, obj2) {
                        return (parseInt(obj1[prop]) || defaultIndex) - parseInt((obj2[prop]) || defaultIndex)
                    }
                }
                _list.sort(sortByProp('sortIndex'))
                return _list
            },
            // 获取全部搜索项集合（实现排序，展示集合在前，更多集合在后）
            showList () {
                return this.sortList.filter(i => !i.isExtend)
            },
            // 获取更多中搜索项集合
            extendList () {
                return this.sortList.filter(i => i.isExtend)
            },
            dataList () {
                return this.showList.concat(this.extendList) || []
            },
        },
        methods: { // 定义函数
            // 更多项中选中状态变更函数，主要用于取消选项时，去除该选项数据并且刷新列表
            changeExtendItem (chooseStatus) {
                // 当执行取消更多中的选项时触发以下逻辑，此时chooseStatus值为false：因为取消选中
                if (!chooseStatus) {
                    // 由于组件没有提供多余参数，无法知道这个变更的更多项是那一项，所以需要将更多项中没有被选中的全部重置
                    this.data.filter(i => i.isExtend && !i.isSelect).forEach(i => {
                        // initValue字段为初始值字段，直接使用该值进行重置
                        this.$set(i, 'value', i.initValue)
                    })
                    // 重置值之后，通知请求列表刷新当前页面数据
                    this.search()
                }
                // 更多选项的展示会影响搜索组件的高度，需要提示重新检查列表固定高度的设值
                this.$emit('changeSearchPosition')
            },
            //下拉框监听事件，用于实现联动
            singleSearch (selectorObj) {
                let { id: id, old: oldSelector, new: newSelector } = selectorObj
                //暴露出自定义的钩子事件。可在js中自定义指定的下拉框监听事件
                if (this.selectChange.length > 0) {
                    let clickChange = this.selectChange.find(item => {
                        return item.name === id + 'Change'
                    })
                    // 问题：clickChange 和 clickChange.click 为空的时候，无法向下 this.search()
                    // 解决：添加判断
                    if (clickChange && 
                        clickChange.click &&
                        typeof clickChange.click === 'function') {
                        clickChange.click.call(this, oldSelector, newSelector)
                        // 问题：下拉框选中值修改为了 newValue，但是搜索的时候，搜索条件是 oldValue
                        // 解决：将 search() 放在 changeSingleSelect 方法内执行
                        return
                    }
                }
                // 没有监听事件的普通下拉框直接执行 search()
                this.search()
            },
            /**
             * 改变单选框属性值
             * @param target[String]        目标ID
             * @param properties[String]    属性名
             * @param val[Object]           期望值
             * @returns {*}
             */
            changeSingleSelect (target, properties, val) {
                if (!target) return
                this.dataList.forEach((item, index) => {
                    if (item.id === target) {
                        this.$set(this.dataList[index], properties, val)
                    }
                })
                this.search()
            },
            /**
             * 执行动态函数
             */
            exeMethod (obj) {
                this[obj.method]()
            },
            search () {
                this.$emit('search')
            },
            resetAndSearch () {
                // 日期选项重置提交父组件reset value值之外，还需要额外清除下拉面板中的数据
                this.clearAllDate()
                // 重置搜索条件值
                this.$emit('reset')
            },
            clearAllDate () {
                // 先使用isSelect状态判断是否展示，选出展示的模块进行处理
                let dateList = this.data.filter(i => i.isSelect).
                                        filter(i => i.type === 5)
                try {
                    dateList.forEach(i => {
                        let refList = this.$refs[i.id] || []
                        if (refList.length === 0) {
                            this.$message({
                                message: '找不到日期搜索组件，请自行检查',
                                type: 'error'
                            })
                            throw 'end' 
                        }
                        if (refList.length > 1) {
                            this.$message({
                                message: '日期搜索组件存在同名字段，请自行检查',
                                type: 'error'
                            })
                            throw 'end'
                        }
                        if (refList.length === 1) {
                            refList[0].clear()
                        }
                    })
                } catch (e) {
                    console.log(e)
                }
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {
            // console.log(1,JSON.stringify(this.showList))
            // console.log(2,JSON.stringify(this.extendList))
            // console.log(3,JSON.stringify(this.dataList))
        }
    }
</script>
<style lang='scss' scoped>
$moreBtnWidth: 70px;
.m-search {
    font-size: 0;
    .m-search-item {
        display: inline-block;
        width: $searchBarItemWidth;
        vertical-align: top;
        margin-right: $space;
        margin-bottom: $space;
    }
    .s-l {
        display: inline-block;
        vertical-align: top;
        font-size: $font;
        width: calc(100% - #{$moreBtnWidth});
        &.isCover {
            width: 100%;
        }
    }
    .s-r {
        display: inline-block;
        vertical-align: top;
        font-size: $fontS;
        letter-spacing: 2px;
        text-align: right;
        cursor: pointer;
        width: $moreBtnWidth;
    }
}
</style>