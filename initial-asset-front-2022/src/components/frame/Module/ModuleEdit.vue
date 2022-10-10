<template>
    <div>
        <div class="fixBottomBtn" 
            v-if="module.code && buttons.length>0" 
            v-setRouterHeight="isCoverPage && module.code && buttons.length>0">
            <flow-router-button
                :flowInfo="flowInfo"
                :mdCode="module.code"
                @closed="_handerClosed">
            </flow-router-button>
            <!-- 测试数据，待删 -->
            <!-- <el-button>
                <bd-icon name="save"></bd-icon>
                <bd-icon name="close"></bd-icon>
            </el-button> -->
            <!-- isLoad：状态表示该按钮项存在loading状态，点击时会启用 -->
            <el-button
                v-for='(i, index) in buttons' 
                :key="index"
                :class="'diy' + i.id"
                v-btnBg="i"
                v-if="!i.isHide"
                :loading="i.isLoad !== undefined && i.isLoad"
                @click='exeMethod(i)'>
                <bd-icon
                    v-if="(i.isLoad === undefined) || (i.isLoad !== undefined && !i.isLoad)"
                    :name="comp_codeFromBtn(i)">
                </bd-icon>
                {{ i.name }}
            </el-button>
        </div>
        <card>
            <!-- 有父子关系时，使用tab -->
            <template v-if="useTab">            
                <m-tab
                    noBorder
                    :tabData="tabData"
                    :activeTab.sync="activeTabName"
                    :beforeLeave="handerBeforeLeave">
                    <!-- <template :slot="activeTabName"> -->
                    <!-- 主表数据 -->
                        <div slot="main">
                            <module-form 
                                ref="mainForm" 
                                :module="module"
                                :recordId="recordId">
                            </module-form>
                        </div>
                        <div
                            :slot="index + ''"
                            v-for="(item, index) in childrenTab" :key="index"
                            :label="item.displayName"
                            :name="index + ''">
                            <child-module-edit
                                :moduleLink="item"
                                :recordId="recordId">
                            </child-module-edit>
                        </div>
                    <!-- </template> -->
                </m-tab>
            </template>
            
            <!-- 没有父子关系使用默认表单 -->
            <!-- 添加isDialogForm：用于判断是否展示表单标题；在dialog中不需要标题 -->
            <module-form 
                v-else 
                ref="mainForm" 
                :module="module"
                :recordId="recordId"
                :isDialogForm="defaultButtons.length === 0">
            </module-form>
        </card>
    </div>
</template>

<script>
    import {Tabs, TabPane} from 'element-ui'
    import ModuleForm from './ModuleForm'
    import ChildModuleEdit from './ChildModuleEdit'
    import FlowRouterButton from '../Flow/FlowRouterButton'
    import {Validator} from 'vee-validate'
    import GlobalConst from '@/service/global-const'
    import MTab from '@/components/frame/Common/MTab'
    import iconHandler from '@/components/frame/Icon/index.js'

    export default {
        name: 'module-edit',
        components: {
            ModuleForm,
            FlowRouterButton,
            ChildModuleEdit,
            [Tabs.name]: Tabs,
            [TabPane.name]: TabPane,
            MTab
        },
        props: {
            module: {
                type: Object,
                default() {
                    return {}
                }
            },
            defaultButtons: {
                type: Array
            },
            recordId: {
                type: String,
                default: ''
            },
            flowInfo: {
                type: Object
            },
            // 是否铺满页面，单纯模型页面时需要设置为铺满，多模块组块不需要
            isCoverPage: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                isBtnLoading: false,
                activeTabName: 'main',
                buttons: [],
                childrenTab: []
            }
        },
        computed: {
            btnMethod() {
                let obj = {}
                this.buttons.forEach(btn => obj[btn.id] = btn.click)
                return obj
            },
            useTab() {
                return this.childrenTab && this.childrenTab.length > 0
            },
            tabData () {
                let arr = []
                if (this.useTab) {
                    arr = [{ label: this.module.name, name: 'main' }]
                    arr.push(...this.childrenTab.map((item, index) => { return { label: item.displayName || item.title || item.moduleName, name: index + '' } }))
                }
                return arr
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
        methods: {
            exeMethod(btnObj) {
                let method = btnObj.id
                this.btnMethod[method].call(this, btnObj)  
            },
            randerBtn(newButtons) {
                // 获取默认按钮
                let btns = Object.assign([], this.defaultButtons)
                // 如果外部JS参数没有配置按钮，那么按钮为原来的按钮
                if (typeof newButtons === 'undefined') {
                    this.buttons = btns
                    return
                }

                // 如果外部JS参数按钮是一个空数组，表示清空按钮，上面复制参数后已经把空数组复制过去，这里直接返回
                if (newButtons.length <= 0) {
                    this.buttons = []
                    return;
                }

                // 暂时存储新的按钮
                let arr = [];
                // 遍历按钮，替换需要替换的
                for (let nKey in newButtons) {
                    let newBtn = newButtons[nKey];
                    for (let key in btns) {
                        if (newBtn.id == btns[key].id) {
                            btns[key] = newBtn;
                            newBtn = null;
                            break;
                        }
                    }
                    if (newBtn) {
                        arr.push(newBtn);
                    }
                }

                // 添加新的按钮
                arr.forEach(b => btns.push(b))
                console.info(btns)
                this.buttons = btns
            },
            setData (key, value) {
                this.$refs.mainForm.setData(key, value)
            },
            setChildrenTab (children) {
                this.childrenTab = []
                if (!children || children.length === 0) {
                    return
                }
                children.filter(child => child.showTab === 1).forEach(child => this.childrenTab.push(child))
            },
            handerBeforeLeave (e) {
                if (this.recordId === 'add') {
                    e && this.alert('请先保存数据')
                    return false
                }
                return true
            },
            /**
             * @return {boolean} 如果有 tab 返回 true，否则返回 false
             */
            hasTab() {
                return this.childrenTab.length > 0
            },
            _handerClosed() {
                this.$parent.close(true)
            }
        },
        watch: {
            module: {
                deep: true,
                immediate: true,
                handler: function (newVal) {
                    if (this.module.editedJsObj.init) {
                        this.module.editedJsObj.init.call(this, Validator)
                    }
                    // 渲染按钮的事件这里会进来两次，一次是初始，第二次是module异步请求成功后
                    // 第一次触发会使页面首先渲染默认按钮，然后等module异步加载数据后再渲染需要的按钮
                    // 现在需求是等待module加载完再显示按钮，所以第一次不需要渲染，通过module.code判断module加载状态
                    if (newVal.code) {
                        this.randerBtn(newVal.editedJsObj.editButtons)
                    }
                    this.setChildrenTab(this.module.childTab)
                }
            },
            // 默认按钮传入不需要触发渲染按钮事件，渲染按钮只在module异步回调后去处理展示
            // defaultButtons: {
            //     immediate: true,
            //     handler: function () {
            //         this.randerBtn()
            //     }
            // }
        }
    }
</script>

<style scoped>
    .fade-enter-active, .fade-leave-active {
        transition: opacity 0.28s
    }
    .fade-enter, .fade-leave-to /* .fade-leave-active, 2.1.8 版本以下 */ {
        opacity: 0
    }
</style>
