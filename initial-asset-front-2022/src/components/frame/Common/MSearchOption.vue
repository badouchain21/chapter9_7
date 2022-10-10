<template>
    <el-popover
        class="m-search-multi"
        placement="bottom-start"
        :trigger="trigger"
        :width="width"
        popover-class="m-search-content"
        v-model="temp_isPopoverShow"
        @show="popoverShow"
        @hide="popoverHide">
        <slot name="panel">
            <el-input
                v-if="isShowSearch"
                class="marginB"
                :placeholder="'请输入' + name"
                v-model="searchOptionWord"
                :clearable="true"
                size="mini">
                <i slot="suffix" class="el-input__icon el-icon-search" v-show="isSearchWordEmpty"></i>
            </el-input>
            <div class="marginB" v-show="isShowClear">
                <div class="clearConBtn d-ib" @click="clearChoose">清除所选内容</div>
            </div>
            <div class="m-search-content marginB">
                <el-scrollbar class="hiddenX">
                    <slot 
                        v-if="isShowSearch" 
                        name="content" 
                        :searchOptionWord="searchOptionWord">
                    </slot>
                    <div v-else class="s-noData">
                        <img class="s-noData-img" :src='publicPath + "static/img/noData.png"'/>
                        <div class="s-noData-text">{{noDataTip}}</div>
                    </div>
                </el-scrollbar>
            </div>
        </slot>
        <div slot="reference" class="m-search-multi-btn" :class="{'is-choose': isShow}">
            <div class="s-type" v-setSearchWidth>{{name}}</div>
            <div class="s-val" :title="valueText">{{valueText}}</div>
            <div class="s-icon">
                <i class="el-icon-arrow-up" :class="{'is-reverse': isShow}"></i>
            </div>
        </div>
    </el-popover>
</template>
<script>
    import { isStrEmpty } from '@/utils/index'
    import GlobalConst from '@/service/global-const'
    export default {
        components: {},
        data () { // 定义页面变量
            return {
                isShow: false,
                // 用于搜索下拉选项的输入搜索词
                searchOptionWord: '',
                temp_isPopoverShow: false,
                publicPath: process.env.BASE_URL,
                // 无数据文本提醒
                noDataTip: GlobalConst.searchBar.noDataTip,
            }
        },
        computed: {
            // 返回option搜索词是否为空的状态
            isSearchWordEmpty () {
                return isStrEmpty(this.searchOptionWord)
            }
        },
        props: {
            // 搜索类型名称
            name: {
                type: String,
                default: ''
            },
            isShowSearch: {
                type: Boolean,
                defalut: true
            },
            // 是否展示清空按钮
            isShowClear: {
                type: Boolean,
                default: false
            },
            // 已选择展示的文本
            valueText: {
                type: String
            },
            // popover窗口宽度
            width: {
                type: [String, Number]
            },
            // 弹出popover时显示是否触发传入的自定义函数
            useShowMethod: {
                type: Boolean,
                default: false
            },
            // 是否显示popover
            isPopoverShow: {
                type: Boolean
            },
            // popover触发方式
            trigger: {
                type: String,
                default: GlobalConst.searchBar.trigger
            }
        },
        directives: {
            setSearchWidth: function (el, binding) {
                // 定义字体大小
                let fontSize = 14
                // 定义label区域最大宽度
                let maxWidth = 90
                // 定义当前模块宽度
                let itemWidth = 180
                // 定义模块中右侧图标宽度
                let iconWidth = 12
                // 定义容错范围宽度
                let rangeWidth = 28
                setTimeout(function() {
                    let content = el.innerText.length * fontSize > maxWidth ? maxWidth : el.innerText.length * fontSize
                    let e = `${itemWidth - iconWidth - rangeWidth - content}px`
                    el.nextElementSibling.style.width = e
                }) 
            }
        },
        methods: { // 定义函数
            clearChoose () {
                this.$emit('clear')
                // this.$emit('change')
            },
            popoverShow () {
                if (this.useShowMethod) {
                    this.$emit('show')
                }
                this.isShow = true
            },
            popoverHide () {
                this.isShow = false
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {},
        watch: {
            isPopoverShow: {
                deep: true,
                immediate: true,
                handler: function (newVal) {
                    this.temp_isPopoverShow = newVal
                }
            }
        }
    }
</script>
<style lang='scss' scoped>
$searchContentHeight: 150px;
.m-search-content >>> {
    height: $searchContentHeight;
    overflow: auto;
    .el-checkbox {
        padding: 2px 0px;
    }
    .s-noData {
        text-align: center;
        .s-noData-img {
            height: $searchContentHeight - 30px;
        }
        .s-noData-text {
            font-size: $fontS;
            color: $fontCS;
        }
    }
}
.m-search-multi >>> {
    .m-search-multi-btn {
        height: $buttonHeight;
        line-height: 18px;
        border-radius: $borderRadius;
        border: 1px solid #DCDFE6;
        width: $searchBarItemWidth;
        padding: 6px;
        position: relative;
        &:hover {
            border-color: #C0C4CC;
        }
        
        &.is-choose {
            border: 1px solid $primary;
        }
        .s-type {
            border-right: 1px solid $lineColor;
            display: inline-block;
            padding-right: 4px;
            max-width: 90px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            color: $fontCS;
        }
        .s-val {
            display:inline-block;
            vertical-align: top;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .s-icon {
            float: right;
            height: 100%;
            vertical-align: top;
            .el-icon-arrow-up {
                transition: transform .3s;
                transform: rotate(180deg);
                &.is-reverse {
                    transform: rotate(0deg);
                }
            }
        }
    }
}
</style>