<!--
搜索组件，点击搜索按钮，或者搜索项有改变时，抛出 search 事件，参数为搜索条件
-->
<template>
    <div class="searchBar m-search">
        <!-- 过滤器 -->
        <!-- <s-filter
            v-if="useFilter"
            :mdCode="mdCode"
            @click="filterChick"
            @cancel="resetAndSearch"
            @save="filterSave">
        </s-filter> -->

        <!-- 搜索框 -->
        <div class="search-box">
            <template v-for="item in selectorCfg">
                <!-- 下拉多选 -->
                <s-multiple-select
                    ref="selectors"
                    v-if="item.selectType === 0"
                    :selector="item"
                    @change="search">
                </s-multiple-select>

                <!-- 文本框 -->
                <s-text
                    ref="selectors"
                    v-if="item.selectType === 1"
                    :selector="item"
                    @change="search">
                </s-text>

                <!-- 日期选择 -->
                <s-date
                    ref="selectors"
                    v-if="item.selectType === 5"
                    :selector="item"
                    @change="search">
                </s-date>

                <!-- 单选 -->
                <s-single-select
                    ref="selectors"
                    v-if="item.selectType === 9"
                    :selector="item"
                    @change="singleSearch">
                </s-single-select>
            </template>

            <!-- 更多搜索框 -->
            <!-- <div class="search-box"> -->
            <template v-for="(item, index) in extraSelectorCfg">
                <!-- 下拉多选 -->
                <s-multiple-select
                    ref="extraSelectors"
                    v-if="item.selectType === 0"
                    v-show="extraCheckedList[index]"
                    :selector="item"
                    @change="search">
                </s-multiple-select>

                <!-- 文本框 -->
                <s-text
                    ref="extraSelectors"
                    v-if="item.selectType === 1"
                    v-show="extraCheckedList[index]"
                    :selector="item"
                    @change="search">
                </s-text>

                <!-- 日期选择 -->
                <s-date
                    ref="extraSelectors"
                    v-if="item.selectType === 5"
                    v-show="extraCheckedList[index]"
                    :selector="item"
                    @change="search">
                </s-date>

                <!-- 单选 -->
                <s-single-select
                    ref="extraSelectors"
                    v-if="item.selectType === 9"
                    v-show="extraCheckedList[index]"
                    :selector="item"
                    @change="singleSearch">
                </s-single-select>
            </template>
            <!-- </div> -->

            <!-- 更多条件选择框 -->
            <div class="search-value" v-if="hasExtra">
                <el-popover
                    placement="bottom"
                    width="150"
                    trigger="click">
                    <el-checkbox v-for="(item,index) in extraSelectorCfg" :key="index"
                                 v-model="extraCheckedList[index]">
                        {{item.name}}
                    </el-checkbox>
                    <el-button slot="reference" plain>更多</el-button>
                </el-popover>
            </div>
            <div class="d-ib">
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
    </div>
</template>
<script>

    import {Dialog, Dropdown, DropdownMenu, DropdownItem, Popover, Checkbox, DatePicker, Radio} from 'element-ui'

    import SFilter from './filter/filter'
    import SDate from './component/Date'
    import SText from './component/Text'
    import SMultipleSelect from './component/MultipleSelect'
    import SSingleSelect from './component/SingleSelect'
    import GlobalConst from '@/service/global-const'

    export default {
        name: 'search-bar',
        components: {
            [Dialog.name]: Dialog,
            [Dropdown.name]: Dropdown,
            [DropdownMenu.name]: DropdownMenu,
            [DropdownItem.name]: DropdownItem,
            [Popover.name]: Popover,
            [Checkbox.name]: Checkbox,
            [DatePicker.name]: DatePicker,
            [Radio.name]: Radio,
            [SDate.name]: SDate,
            [SText.name]: SText,
            [SMultipleSelect.name]: SMultipleSelect,
            [SSingleSelect.name]: SSingleSelect,
            [SFilter.name]: SFilter,
        },
        props: {
            // 模型编码，用于过滤器
            mdCode: '',
            // 模型对象
            module: null,
            // 是否使用过滤器
            useFilter: {
                default: true
            },
            // 搜索框配置
            selectorConfig: {
                type: Object,
                default() {
                    return {
                        selectorData: [],
                        searchCondition: [],
                    }
                }
            },
        },
        computed: {
            hasExtra() {
                return this.extraSelectorCfg.length > 0
            }
        },
        data() {
            return {
                // 搜索框的配置
                selectorCfg: [],
                //下拉框监听事件集合
                selectChange: [],
                // 更多搜索框的配置
                extraSelectorCfg: [],
                // 更多搜索框下拉选择列表对应的 是否选中数组
                extraCheckedList: [],
                btnList: [
                    { id: '', name: '搜索', icon: 'search', method: 'search' },
                    { id: '', name: '重置', icon: 'reset', method: 'resetAndSearch' }
                ]
            }
        },
        methods: {
            //下拉框监听事件，用于实现联动
            singleSearch (selectorObj) {
                //暴露出自定义的钩子事件。可在js中自定义指定的下拉框监听事件
                if (this.selectChange.length > 0) {
                    let clickChange = this.selectChange.find(item => {
                        return item.name == selectorObj.id + 'Change'
                    })
                    // 问题：clickChange 和 clickChange.click 为空的时候，无法向下 this.search()
                    // 解决：添加判断
                    if (clickChange && 
                        clickChange.click &&
                        typeof clickChange.click ==='function') {
                        clickChange.click.call(this, selectorObj.old, selectorObj.new)
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
                this.selectorCfg.forEach((item, index) => {
                    if (item.id === target) {
                        this.$set(this.selectorCfg[index], properties, val)
                    }
                })
                this.search()
            },
            // 获取搜索器的值
            getSelectorValue() {
                let searchParam = []

                let selectors = this.$refs.selectors || []
                selectors.forEach(s => {
                    let val = s.getSelectorValue()
                    if (val) {
                        searchParam.push(val)
                    }
                })
                // 更多的搜索器的搜索条件
                let extraSelectors = this.$refs.extraSelectors
                if (extraSelectors) {
                    extraSelectors.filter((s, i) => this.extraCheckedList[i]).forEach(s => {
                        let val = s.getSelectorValue()
                        if (val) {
                            searchParam.push(val)
                        }
                    })
                }
                return searchParam
            },
            /**
             * 获取搜索框的值，并抛出 search 事件
             */
            search() {
                setTimeout(() => {
                    this.$emit('search', this.getSelectorValue())
                })
            },
            /**
             * 执行动态函数
             */
            exeMethod (obj) {
                this[obj.method]()
            },
            /**
             * 重置搜索框
             */
            resetSearchParam() {
                let selectors = this.$refs.selectors
                if (selectors) {
                    selectors.forEach(s => s.reset())
                }

                let extraSelectors = this.$refs.extraSelectors
                if (extraSelectors) {
                    extraSelectors.forEach(s => s.reset())
                }
            },
            // 重置搜索框并抛出 search 事件
            resetAndSearch() {
                this.resetSearchParam()
                this.search()
            },
            filterChick(filterInfo) {
                this.resetSearchParam()

                let searchValArr = JSON.parse(filterInfo.content)
                searchValArr.forEach(v => {
                    let selector = this.getSelector(v.name)
                    if (selector) {
                        selector.setValue(v.value)
                        return
                    }

                    // 如果上面没有找到，就在扩展搜索器中查找
                    let extra = this.getExtraSelector(v.name)
                    if (extra) {
                        extra.selector.setValue(v.value)
                        this.$set(this.extraCheckedList, extra.index, true)
                    }
                })
                this.search()
            },
            filterSave(filterInfo) {
                console.log(filterInfo)
            },
            /**
             * 根据搜索器ID获取搜索器
             */
            getSelector(selectorId) {
                if (this.$refs.selectors) {
                    let arr = this.$refs.selectors
                    for (let i in arr)
                        if (arr[i].getSelectorInfo().id == selectorId)
                            return arr[i]
                }
                return null
            }
            ,
            /**
             * 根据搜索器ID获取扩展搜索器
             */
            getExtraSelector(selectorId) {
                if (this.$refs.extraSelectors) {
                    let arr = this.$refs.extraSelectors
                    for (let i in arr)
                        if (arr[i].getSelectorInfo().id == selectorId)
                            return {selector: arr[i], index: i}
                }
                return null
            },
            getSearchCondition (searchObj) {
                return new Promise((resolve, reject) => {
                    if (searchObj.url) {
                        this.post(`${this.BASEURL}` + searchObj.url, {}).then(res => {
                            if (res) {
                                resolve(res)
                            } else {
                                resolve([])
                            }
                        }).catch(err => {
                            resolve([])
                        })
                    } else {
                        resolve(searchObj.data || [])
                    }
                })
            },
            /**
             * 排序查询条件
             * @param cfgList[Array] 原条件
             * @returns {*}
             */
            sortCondition (cfgList) {
                if (!cfgList) return []
                // 获取搜索组件默认排序序号
                let defaultIndex = GlobalConst.searchBar.sortIndex
                /**
                 * 排序函数专用
                 * @param prop[String] 排序所依赖的对象字段名
                 */
                function sortByProp (prop) {
                    return function (obj1, obj2) {
                        return (obj1[prop] || defaultIndex) - (obj2[prop] || defaultIndex)
                    }
                }
                // 深拷贝，避免修改cfgList数据使得重复触发selectorConfig监听函数
                let _cfgList = JSON.parse(JSON.stringify(cfgList))
                // 排序
                _cfgList.sort(sortByProp('sortIndex'))
                return _cfgList
            }
        },
        watch: {
            selectorConfig: {
                deep: true,
                handler: function () {
                    // 每当搜索配置修改时，重置搜索条件
                    this.resetSearchParam()
                    let cfg = this.sortCondition(this.selectorConfig.selectorData)
                    // let cfg = this.selectorConfig.selectorData
                    let condition = this.selectorConfig.searchCondition
                    this.selectorCfg = []
                    this.extraSelectorCfg = []

                    let promiseList = []
                    // 把多选，单选等搜索项放到搜索配置中，这里目前(2019-2-14)只处理了搜索项数据来源为数据字典的情况
                    for (let i in cfg) {
                        for (let j in condition) {
                            if (cfg[i].id === condition[j].id) {
                                promiseList.push(this.getSearchCondition(condition[j]))
                                break
                            }
                        }
                    }
                    Promise.all(promiseList).then(res => {
                        for (let i in cfg) {
                            for (let j in condition) {
                                if (cfg[i].id === condition[j].id) {
                                    cfg[i].searchCondition = res[i]
                                    // 扩展的搜索器与非扩展的搜索器分开存放
                                    if (cfg[i].isExtended) {
                                        this.extraSelectorCfg.push(cfg[i])
                                    } else {
                                        this.selectorCfg.push(cfg[i])
                                    }
                                }
                            }
                        }
                        // 存在搜索组件默认值的情况，这里获取组件值重新获取列表数据
                        setTimeout(() => {
                            this.search()
                        })
                    })
                }
            },
            module: {
                deep: true,
                immediate: true,
                handler: function () {
                    this.selectChange = this.module.listJsObj.selectChange || []
                }
            }
        }
    }
</script>
<style scoped lang="scss">
    .searchBar {
        width: 100%;
        height: auto;
    }

    .searchBar >>> .filter {
        background-color: #fdfdfd
    }

    .searchBar >>> .filter p {
        background-color: #fdfdfd;
        padding: 0 0 10px 0;
        display: inline-block;
        margin-right: 5px;
    }

    .searchBar >>> .filterName {
        color: #5d5d5d;
    }

    .searchBar >>> .alert-btn {
        color: #007FBE;
        margin: 0 5px;
        cursor: pointer;
    }

    .searchBar >>> .alert-btn:hover {
        color: #f36100;
    }

    .searchBar >>>  p span:hover {
        color: #f36100;
    }

    .searchBar >>> .fiterName i {
        color: #bbb;
    }

    .searchBar >>> .fiterName i:hover {
        color: #333;
    }

    .searchBar >>> .search-box {
        display: inline-block;
        height: auto;
        font-size: 14px;
    }

    .searchBar >>> .search-value {
        width: auto;
        height: auto;
        display: inline-block;
    }

    .searchBar >>> .el-picker-panel__content .el-month-table td .cell {
        font-size: 12px;
    }


    .searchBar >>> .search-box button {
        outline: none;
    }

    .searchBar >>> .dialog-title {
        border-bottom: 1px solid #D8D8D8;
        margin: 0 20px;
        padding: 10px 0;
    }

    .searchBar >>> .dialog-span {
        float: right;
        margin-left: 10px;
        color: #9191e8;
    }

    .searchBar >>> .dialog-content {
        display: block;
        padding: 0 10px;
        margin: 0 20px;
        cursor: pointer;
    }

    .searchBar >>> .dialog-item {
        display: inline-block;
        background-color: #c2e2f2;
        color: #e2f0f7;
        margin: 5px 5px;
        text-align: center;
        width: 100px;
        height: 35px;
        border-radius: 50px;
        overflow: hidden;
        position: relative;
        white-space: nowrap;
        text-overflow: ellipsis;
        padding: 7px 10px 0px 10px;
    }

    .searchBar >>> .dialog-item i {
        color: white;
    }

    .searchBar >>> .dialog-item i:hover {
        color: unset;
    }

    .searchBar >>> .selected {
        background-color: #52b3df;
        color: white;
    }

    .searchBar >>> .el-dialog__body {
        padding: 0;
    }

    .searchBar >>> .dialog-user {
        display: inline-block;
        width: 70px;
        height: 80px;
        margin: 3px 10px;
        padding-top: 5px;
        text-align: center;
        color: #333;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        position: relative;
    }

    .searchBar >>> .dialog-user i {
        display: block;
        font-size: 30px;
        margin: 0 auto;
        color: #333;
    }

    .searchBar >>> .dialog-user img {
        position: absolute;
        font-size: 18px;
        color: #49b1e0;
        left: 0;
    }

    .searchBar >>> .el-dialog__footer {
        border-top: 1px solid #e7e4f0
    }

    .searchBar >>> .dialog-text {
        padding: 20px 20px 10px 20px;
    }

    .searchBar >>> .search-value el-dropdown {
        height: 35px;
        padding-top: 6px;
        font-size: 16px;
        outline: none;
    }

    .searchBar >>> .el-checkbox {
        display: block;
    }

    .searchBar >>> .text-style {
        width: 150px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .searchBar >>> .search-value span {
        display: inline-block;
    }

    .searchBar >>> .selector_name {
        display: inline-block;
        margin-right: -15px;
        margin-left: 10px;
    }

    // ==========================================wjx start
    .search-box .search-value{
        vertical-align: top;
        margin-right: $space;
        margin-bottom: $space;
    } 
    .searchBar >>> .search-value {
        .el-input {
            width: $searchBarItemWidth;
            .el-input__inner {
                height: $buttonHeight;
                line-height: $buttonHeight;
            }
        }
    }
    
    // ==========================================wjx end
</style>
