<!--
 * @Description: 骨架
 * @Author: LXG
 * @Date: 2020-09-17
 * @LastEditTime: 2020-09-21
-->
<!--
 * @TIPS
 * 骨架的作用，主要用于需要从后台获取数据的页面（如 模型查看页/模型编辑页），在响应前，用纯色快遮挡数据渲染的区域，填补页面空白。
 * 在页面中引入该组件，通过状态位loading控制即可。
 * 目前版本允许两种用法，走不同的自定义指令：
 * 1、inner。默认绝对布局，撑满父元素，层级在上，参考 ModuleView.vue /ModuleForm.vue 。
 ****************************************************************************************************
<label class="m-form-item-label">
    <skeleton :loading="skeleton" :widthAdjust="-10"></skeleton>
    <span class=text-o-1" :title="item.display">{{item.display}}</span>
</label>
 ****************************************************************************************************
 * 2、outer。默认无布局，需指定宽高。
-->
<script>
export default {
    name: 'Skeleton',
    directives: {
        inner: {
            inserted: function (el, binding) {
                // console.log('v-innerSkeleton:', el, binding)
                const width = binding.value.width
                const height = binding.value.height
                const widthAdjust = binding.value.widthAdjust
                const heightAdjust = binding.value.heightAdjust
                const parentDOM = el.parentNode || el.parentElement
                el.style.position = 'absolute'
                el.style.width =
                    width || `${parentDOM.clientWidth + widthAdjust}px`
                el.style.height =
                    height || `${parentDOM.clientHeight + heightAdjust}px`
                el.style['z-index'] = '10'
            }
        },
        outer: {
            inserted: function (el, binding) {
                // console.log('v-outerSkeleton:', el, binding)
                const width = binding.value.width
                const height = binding.value.height
                if (width) {
                    el.style.width = width
                }
                if (height) {
                    el.style.height = height
                }
            }
        }
    },
    props: {
        // 加载中
        loading: {
            type: [Boolean, Number, String],
            default: true
        },
        // 渲染身份
        renderIdentify: {
            type: String,
            default: 'inner'
        },
        // 宽度
        // width: {
        //     type: [Number, String]
        // },
        // 宽度调整
        widthAdjust: {
            type: [Number, String],
            default: 0
        },
        // 高度
        // height: {
        //     type: [Number, String]
        // },
        // 高度调整
        heightAdjust: {
            type: [Number, String],
            default: 0
        }
    },
    render (h) {
        // console.log('Skeleton Render')
        if (this.loading) {
            let option = {
                class: 'skeleton',
                directives: [
                    {
                        name: this.renderIdentify,
                        value: {
                            width: this.$attrs.width,
                            height: this.$attrs.height,
                            widthAdjust: this.widthAdjust,
                            heightAdjust: this.heightAdjust
                        }
                    }
                ]
            }
            return h('div', option, '')
        } else {
            if (this.$slots.default) {
                return this.$slots.default[0]
            }
        }
    }
}
</script>

<style lang='scss' scoped>
.skeleton {
    background-color: $grey;
    border-radius: $borderRadius;
}
</style>