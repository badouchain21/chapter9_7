<template>
    <div>
        <el-dialog
            :id="id"
            custom-class="m-dialog noUsePaddding"
            v-setDialogSize = "{ 
                width: width || dialogWidth, 
                height: height || dialogHeight, 
                visibleStatus:visibleStatus,
                isAutoFix: isAutoFix }"
            @close="handleClose"
            @opened="addScrollListener"
            :before-close="beforeCloseDialog"
            :destroy-on-close = "true"
            :close-on-click-modal="false"
            :visible.sync="visibleStatus">
            <div slot="title" class="m-dialog-title">
                <bd-icon name="pillar-fill" class="pillar fill"></bd-icon>
                {{title}}
            </div>
            <el-scrollbar
                wrap-class="scrollbar-wrapper">
                <m-dialog-item
                    v-if="dialogItem"
                    v-bind="dialogItem"
                    :ref="id">
                </m-dialog-item>
            </el-scrollbar>
            <span slot="footer" v-if="btnList.length !== 0">
                <!-- isLoad：状态表示该按钮项存在loading状态，点击时会启用 -->
                <el-button
                    v-for='(i, index) in btnList' 
                    :key="index" 
                    v-btnBg="i"
                    :loading="i.isLoad !== undefined && i.isLoad"
                    @click='exeMethod(i)'>
                    <bd-icon
                        v-if="(i.isLoad === undefined) || (i.isLoad !== undefined && !i.isLoad)"
                        :name="comp_codeFromBtn(i)">
                    </bd-icon>
                    {{ i.name }}
                </el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import GlobalConst from '@/service/global-const'
import MForm from '@/components/frame/Common/MForm/index.vue'
import MDialogItem from '@/components/frame/Common/MDialog/components/MDialogItem'
import iconHandler from '@/components/frame/Icon/index.js'

export default {
    components: {
        MForm,
        MDialogItem
    },
    props: {
        visible: {
            type: Boolean,
            default: false
        },
        // // dialog与内容块的唯一标识，建议必须传递，以防存在多个时异常
        id: {
            type: String,
            default: 'dialogContentId'
        },
        // dialog标题
        title: {
            type: String,
            default: GlobalConst.dialog.title
        },
        // 弹窗宽度-即将废弃
        dialogWidth: {
            type: [String, Number],
            default: 0
        },
        // 弹窗宽度-推荐使用
        width: {
            type: [String, Number],
            default: 0
        },
        // 弹窗高度
        dialogHeight: {
            type: [String, Number],
            default: 0
        },
        height: {
            type: [String, Number],
            default: 0
        },
        // 是否根据内容自动撑高度
        isAutoFix: {
            type: Boolean,
            default: false
        },
        // 即将废弃-dialog中的操作按钮组，请使用handlerList
        btnMethods: {
            type: Array,
            default: () => []
        },
        // dialog中的操作按钮组
        handlerList: {
            type: Array,
            default: () => []
        },
        // 父组件传递给当前组件的所有属性对象(这里的父级指通过js触发弹窗的父级)
        option: {
            type: Object
        }
    },
    computed: {
        // TODO:兼容btnMethods参数，后续版本中将删除
        btnList () {
            if (this.handlerList.length > 0) {
                return this.handlerList
            }
            return this.btnMethods || []
        },
        // 获取弹窗中内容需要的属性，用于传入内容组件
        dialogItem () {
            // 当前组件属性来源两个：
            // 1. 通过js触发的弹窗，属性值来源 this.option
            // 2. 组件引入的弹窗，属性值来源 $attrs + $props
            let contentAttr = { ...this.option, ...this.$attrs, ...this.$props }
            if (contentAttr) {
                let {
                    width, 
                    dialogWidth,
                    height,
                    dialogHeight,
                    isAutoFix, 
                    handlerList,
                    btnMethods,
                    ...dialogItem
                } = contentAttr
                return dialogItem
            }
        },
        /**
         * @description: 匹配通用按钮图标
         * @param {Object} btn 按钮
         */
        comp_codeFromBtn () {
            return function (btn) {
                return iconHandler.codeFromBtn(btn)
            }
        }
    },
    data () {
        return {
            visibleStatus: false,
        }
    },
    inheritAttrs: false,
    methods: {
        exeMethod (itemObj) {
            // 绑定事件中的this为当前页面作用域
            // 另外将按钮对象itemObj扔回作为绑定事件的参数
            // TODO:click即将废弃，使用属性名handler
            (itemObj.click || itemObj.handler || itemObj.method).call(this, itemObj)
        },
        handleClose (done) {
            this.$emit('update:visible', false)
        },
        // 弹窗关闭前事件
        beforeCloseDialog (done) {
            this.addScrollListener (true)
            // 执行关闭
            done()
        },
        addDialogClass () {
            let dialogScrollWrap = document.querySelector(`#${this.id} .el-scrollbar__wrap`)
            let dialogHeader = document.querySelector(`#${this.id} .el-dialog__header`)
            if (dialogScrollWrap.scrollTop > 0) {
                dialogHeader.style.cssText = 'box-shadow: rgba(102, 102, 102, 0.1) 0px -2px 8px 0px inset'
            } else {
                dialogHeader.style.cssText = 'box-shadow: none'
            }
        },
        addScrollListener (listenStatus = false) {
            let dialogScrollWrap = document.querySelector(`#${this.id} .el-scrollbar__wrap`)
            if (!listenStatus) {
                dialogScrollWrap.addEventListener('scroll', this.addDialogClass)
            } else {
                dialogScrollWrap.removeEventListener('scroll', this.addDialogClass)
            }  
        }
    },
    mounted () {},
    watch: {
        visible: function (newVal, oldVal) {
            this.visibleStatus = newVal
        },
    }
}
</script>

<style scoped>

</style>

