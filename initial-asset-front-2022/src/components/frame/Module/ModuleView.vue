<template>
    <div :class="module.code+'_view'" class="_view formPage m-module-view">
        <div class="fixBottomBtn" 
            v-if="buttons.length>0 && module.code"
            v-setRouterHeight="isCoverPage && module.code && buttons.length>0">
            <flow-router-button
                :flowInfo="flowInfo"
                :mdCode="module.code"
                @closed="_handerClosed">
            </flow-router-button>
            <el-button
                v-for='(btn, index) in buttons'
                :key="index"
                v-btnBg="btn"
                v-if="!btn.isHide" 
                type="primary"
                @click='exeMethod(btn.id)'>
                <bd-icon :name="comp_codeFromBtn(btn)"></bd-icon>
                {{ btn.name }}
            </el-button>
        </div>

        <!-- 有父子关系时，使用tab -->
        <el-tabs
            v-if="useTab"
            type="border-card"
            v-model="activeTabName">
            <!-- 主表数据 -->
            <el-tab-pane :label="module.name" name="main">
                <template>
                    <div>
                        <div class="showSpan form-title" v-if="module.name && !isDialogForm">
                            <span class="sign"></span> 
                            <span>{{module.name}}</span>
                        </div>
                        <div class="form-main">
                            <el-form class="m-form">
                                <el-row
                                    v-for="(i, i_index) in showFields"
                                    :key="i_index">
                                    <div class="m-form-item" 
                                        :class="[item.isFullLine? 'labelOneLine':'', 
                                                'el-col el-col-' + item.widthPer, 
                                                item.className ? item.className : '']" 
                                        v-for="(item, index) in i" :key="index">
                                        <label class="m-form-item-label"> 
                                            <span class="text-o-1" :title="item.display">{{item.display}}</span>
                                        </label>
                                        <div  class="m-form-item-content">
                                            <!-- 只读 -->
                                            <fg-input 
                                                v-if="item.showType === 'readonly'" 
                                                disabled
                                                :isView="true"
                                                :label="item.display"
                                                :placeholder="item.display"
                                                v-model="dataModel[item.name]">
                                            </fg-input>

                                            <!-- 密码 -->
                                            <fg-input 
                                                v-else-if="item.showType === 'password'" 
                                                type="password"
                                                readonly
                                                :isView="true"
                                                :label="item.display"
                                                :placeholder="item.display" 
                                                v-model="dataModel[item.name]">
                                            </fg-input>

                                            <!-- 下拉框 -->
                                            <fg-select
                                                v-else-if="item.showType === 'select'"
                                                :label="item.display"
                                                :moduleField="item.field"
                                                :isView="true"
                                                :options="module.dic[item.dic]"
                                                :placeholder="item.display"
                                                v-model="dataModel[item.name]"
                                                :name="item.name">
                                            </fg-select>

                                            <!-- 日期 -->
                                            <fg-input
                                                v-else-if="item.showType === 'date'"
                                                v-model="dataModel[item.name]"
                                                :label="item.display"
                                                readonly
                                                :name="item.name"
                                                :isView="true">
                                            </fg-input>

                                            <!-- 年份 -->
                                            <fg-input
                                                v-else-if="item.showType === 'year'"
                                                v-model="dataModel[item.name]"
                                                :label="item.display"
                                                :name="item.name"
                                                :isView="true"
                                                :required="item.required">
                                            </fg-input>

                                            <!-- 颜色选择器-多选 -->
                                            <fg-input
                                                v-else-if="item.showType === 'color-multiple'"
                                                :label="item.display"
                                                :name="dataModel[item.name]"
                                                v-model="dataModel[item.name]"
                                                :isView="true"
                                                :required="item.required">
                                                <l-color-picker
                                                    :fieldName="item.name"
                                                    :colors="dataModel[item.name]"
                                                    :isSingle="false"
                                                    @setDataFromChild="setDataFromChild">
                                                </l-color-picker>
                                            </fg-input>
                                            <!-- 颜色选择器-单选 -->
                                            <fg-input
                                                v-else-if="item.showType === 'color-single'"
                                                :label="item.display"
                                                :name="item.name"
                                                v-model="dataModel[item.name]"
                                                :isView="true">
                                                <l-color-picker
                                                    :fieldName="item.name"
                                                    :colors="dataModel[item.name]"
                                                    :isSingle="true"
                                                    @setDataFromChild="setDataFromChild">
                                                </l-color-picker>
                                            </fg-input>

                                            <!-- 图片-多选 -->
                                            <fg-input
                                                v-else-if="item.showType === 'img'"
                                                :label="item.display">
                                                <l-attach
                                                    type='img'
                                                    :disable="true"
                                                    :resourceId="id"
                                                    :fileType='item.fileType'
                                                    :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                                    :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                                    :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                                    :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                                    :isSingle="false">
                                                </l-attach>
                                            </fg-input>
                                            <!-- 图片-单选 -->
                                            <fg-input
                                                v-else-if="item.showType === 'imgSingle'"
                                                :label="item.display">
                                                <l-attach
                                                    type='img'
                                                    :resourceId="id"
                                                    :fileType='item.fileType'
                                                    :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                                    :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                                    :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                                    :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                                    :isSingle="true">
                                                </l-attach>
                                            </fg-input>

                                            <!-- 附件 -->
                                            <fg-input
                                                v-else-if="item.showType === 'attach'"
                                                :label="item.display">
                                                <l-attach
                                                    type='attach'
                                                    :resourceId="id"
                                                    :fileType='item.fileType'
                                                    :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                                    :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                                    :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                                    :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                                    :isSingle="true">
                                                </l-attach>
                                            </fg-input>

                                            <!-- 地址本 单选 -->
                                            <fg-input
                                                v-else-if="item.showType === 'addressbook' && isSingleAddressBook(item.addressType)"
                                                v-model="dataModel[item.valueFieldText]"
                                                :label="item.display"
                                                :isView="true"
                                                :name="item.name">
                                            </fg-input>

                                            <!-- 地址本 多选 -->
                                            <fg-input
                                                v-else-if="item.showType === 'addressbook' && !isSingleAddressBook(item.addressType)"
                                                v-model="dataModel[item.valueFieldText]"
                                                :label="item.display"
                                                :name="item.name"
                                                :isView="true">
                                            </fg-input>

                                            <!-- 弹出框列表选择 单选 -->
                                            <fg-input
                                                v-else-if="item.showType === 'selectListData'"
                                                v-model="dataModel[item.valueFieldText]"
                                                :label="item.display"
                                                :name="item.name"
                                                :isView="true">
                                            </fg-input>


                                            <!-- 弹出框列表选择 多选 -->
                                            <fg-input
                                                v-else-if="item.showType === 'selectListDataMulti'"
                                                v-model="dataModel[item.valueFieldText]"
                                                :label="item.display"
                                                :isView="true"
                                                :name="item.name">
                                            </fg-input>

                                            <!-- CheckboxList -->
                                            <fg-input
                                                v-else-if="item.showType === 'checkboxlist'"
                                                v-model="dataModel[item.name]"
                                                :label="item.display"
                                                :name="item.name">
                                                <checkbox-list
                                                    :options="module.dic[item.dic]"
                                                    :moduleField="item.field"
                                                    :isView="true"
                                                    :valueId="dataModel[item.valueFieldId]">
                                                </checkbox-list>
                                            </fg-input>

                                            <!-- radio -->
                                            <fg-input
                                                v-else-if="item.showType === 'radio'"
                                                v-model="dataModel[item.name]"
                                                :label="item.display"
                                                :isView="true"
                                                :name="item.name">
                                                <radio-list
                                                    :options="module.dic[item.dic]"
                                                    :moduleField="item.field"
                                                    :isView="true"
                                                    :valueId="dataModel[item.valueFieldId]">
                                                </radio-list>
                                            </fg-input>

                                            <!-- checkbox -->
                                            <fg-input
                                                v-else-if="item.showType === 'checkbox'"
                                                v-model="dataModel[item.name]"
                                                :label="item.display"
                                                :name="item.name"
                                                :required="item.required">
                                                <div>
                                                    <l-switch :isView="true" v-model="dataModel[item.name]"></l-switch>
                                                </div>
                                            </fg-input>

                                            <!-- textarea -->
                                            <fg-input
                                                v-else-if="item.showType === 'textarea'"
                                                :isView="true"  
                                                :label="item.display"
                                                :placeholder="item.display" :name="item.name"
                                                v-model="dataModel[item.name]">
                                                <textarea
                                                    class="form-control"
                                                    readonly
                                                    :isView="true"
                                                    :placeholder="item.display"
                                                    v-model="dataModel[item.name]">
                                                </textarea>
                                            </fg-input>

                                            <!-- 富文本 -->
                                            <fg-input 
                                                v-else-if="item.showType === 'richText'" 
                                                :label="item.display"
                                                :placeholder="item.display" :name="item.name"
                                                v-model="dataModel[item.name]">
                                                <rich-text v-model="dataModel[item.name]" :isView="true"></rich-text>
                                            </fg-input>

                                            <!-- 文本框 -->
                                            <fg-input 
                                                v-else 
                                                :isView="true" 
                                                :label="item.display" 
                                                :placeholder="item.display" :name="item.name"
                                                v-model="dataModel[item.name]">
                                            </fg-input>
                                        </div>
                                    </div>
                                </el-row>
                            </el-form>
                        </div>

                        <card v-if="attachMultiFields.length > 0">
                            <attach-list
                                v-for="f in attachMultiFields"
                                :key="f.id"
                                :resourceId="id"
                                :title="f.display"
                                v-model="dataModel[f.name]">
                            </attach-list>
                        </card>
                    </div>
                </template>
            </el-tab-pane>
            <el-tab-pane
                v-for="(item, index) in childrenTab" :key="index"
                :label="item.displayName || item.moduleName"
                :name="index + ''">
                <child-module-view
                    :moduleLink="item"
                    :recordId="recordId">
                </child-module-view>
            </el-tab-pane>
        </el-tabs>

        <template v-else>
            <div>
                <div class="showSpan form-title" v-if="module.name && !isDialogForm">
                    <span class="sign"></span> 
                    <span>{{module.name}}</span>
                </div>
                <div class="form-main">
                    <el-form class="m-form">
                        <el-row
                            v-for="(i, i_index) in showFields"
                            :key="i_index">
                            <div class="m-form-item" 
                                :class="[item.isFullLine? 'labelOneLine':'', 
                                        'el-col el-col-' + item.widthPer,
                                        item.className ? item.className : '']" 
                                v-for="(item, index) in i" :key="index">
                                <label class="m-form-item-label">
                                    <skeleton :loading="skeleton" :widthAdjust="-10"></skeleton>
                                    <span class="text-o-1" :title="item.display">{{item.display}}</span>
                                </label>
                                <div  class="m-form-item-content">
                                    <skeleton :loading="skeleton"></skeleton>
                                    <!-- 只读 -->
                                    <fg-input 
                                        v-if="item.showType === 'readonly'" 
                                        disabled 
                                        :label="item.display" 
                                        :placeholder="item.display"
                                        v-model="dataModel[item.name]"
                                        :isView="true">
                                    </fg-input>

                                    <!-- 密码 -->
                                    <fg-input 
                                        v-else-if="item.showType === 'password'"
                                        readonly  
                                        :isView="true"
                                        type="password" 
                                        :label="item.display"
                                        :placeholder="item.display" 
                                        v-model="dataModel[item.name]">
                                    </fg-input>

                                    <!-- 下拉框 -->
                                    <fg-select
                                        v-else-if="item.showType === 'select'"
                                        :label="item.display"
                                        :moduleField="item.field"
                                        :isView="true"
                                        :options="module.dic[item.dic]"
                                        :placeholder="item.display"
                                        v-model="dataModel[item.name]"
                                        :name="item.name">
                                    </fg-select>

                                    <!-- 日期 -->
                                    <fg-input
                                        v-else-if="item.showType === 'date'"
                                        v-model="dataModel[item.name]"
                                        :label="item.display"
                                        :title="dataModel[item.name]"
                                        readonly
                                        :isView="true"
                                        :name="item.name">
                                    </fg-input>

                                    <!-- 年份 -->
                                    <fg-input
                                        v-else-if="item.showType === 'year'"
                                        v-model="dataModel[item.name]"
                                        :label="item.display"
                                        :name="item.name"
                                        readonly
                                        :isView="true"
                                        :required="item.required">
                                    </fg-input>

                                    <!-- 颜色选择器-多选 -->
                                    <fg-input
                                        v-else-if="item.showType === 'color-multiple'"
                                        :label="item.display"
                                        :name="dataModel[item.name]"
                                        v-model="dataModel[item.name]"
                                        readonly
                                        :isView="true"
                                        :required="item.required">
                                        <l-color-picker
                                            :fieldName="item.name"
                                            :colors="dataModel[item.name]"
                                            :isSingle="false"
                                            @setDataFromChild="setDataFromChild">
                                        </l-color-picker>
                                    </fg-input>
                                    <!-- 颜色选择器-单选 -->
                                    <fg-input
                                        v-else-if="item.showType === 'color-single'"
                                        :label="item.display"
                                        :name="item.name"
                                        v-model="dataModel[item.name]"
                                        :isView="true"
                                        readonly>
                                        <l-color-picker
                                            :fieldName="item.name"
                                            :colors="dataModel[item.name]"
                                            :isSingle="true"
                                            @setDataFromChild="setDataFromChild">
                                        </l-color-picker>
                                    </fg-input>

                                    <!-- 图片-多选 -->
                                    <fg-input
                                        v-else-if="item.showType === 'img'"
                                        :label="item.display">
                                        <l-attach
                                            type='img'
                                            :disable="true"
                                            :resourceId="id"
                                            :fileType='item.fileType'
                                            :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                            :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                            :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                            :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                            :isSingle="false">
                                        </l-attach>
                                    </fg-input>
                                    <!-- 图片-单选 -->
                                    <fg-input
                                        v-else-if="item.showType === 'imgSingle'"
                                        :label="item.display">
                                        <l-attach
                                            type='img'
                                            :resourceId="id"
                                            :fileType='item.fileType'
                                            :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                            :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                            :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                            :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                            :isSingle="true">
                                        </l-attach>
                                    </fg-input>

                                    <!-- 附件 -->
                                    <fg-input
                                        v-else-if="item.showType === 'attach'"
                                        :label="item.display">
                                        <l-attach
                                            type='attach'
                                            :resourceId="id"
                                            :fileType='item.fileType'
                                            :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                            :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                            :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                            :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                            :isSingle="true">
                                        </l-attach>
                                    </fg-input>

                                    <!-- 地址本 单选 -->
                                    <fg-input
                                        v-else-if="item.showType === 'addressbook' && isSingleAddressBook(item.addressType)"
                                        v-model="dataModel[item.valueFieldText]"
                                        :label="item.display"
                                        readonly
                                        :isView="true"
                                        :name="item.name">
                                    </fg-input>

                                    <!-- 地址本 多选 -->
                                    <fg-input
                                        v-else-if="item.showType === 'addressbook' && !isSingleAddressBook(item.addressType)"
                                        v-model="dataModel[item.valueFieldText]"
                                        :label="item.display"
                                        :name="item.name"
                                        readonly
                                        :isView="true">
                                    </fg-input>

                                    <!-- 弹出框列表选择 单选 -->
                                    <fg-input
                                        v-else-if="item.showType === 'selectListData'"
                                        v-model="dataModel[item.valueFieldText]"
                                        :label="item.display"
                                        :name="item.name"
                                        readonly
                                        :isView="true">
                                    </fg-input>

                                    <!-- 弹出框列表选择 多选 -->
                                    <fg-input
                                        v-else-if="item.showType === 'selectListDataMulti'"
                                        v-model="dataModel[item.valueFieldText]"
                                        :label="item.display"
                                        readonly
                                        :name="item.name"
                                        :isView="true">
                                    </fg-input>

                                    <!-- CheckboxList -->
                                    <fg-input
                                        v-else-if="item.showType === 'checkboxlist'"
                                        v-model="dataModel[item.name]"
                                        :label="item.display"
                                        :name="item.name"
                                        :isView="true">
                                        <checkbox-list
                                            :options="module.dic[item.dic]"
                                            :moduleField="item.field"
                                            :isView="true"
                                            :valueId="dataModel[item.valueFieldId]">
                                        </checkbox-list>
                                    </fg-input>

                                    <!-- radio -->
                                    <fg-input
                                        v-else-if="item.showType === 'radio'"
                                        v-model="dataModel[item.name]"
                                        :label="item.display"
                                        readonly
                                        :name="item.name"
                                        :isView="true">
                                        <radio-list
                                            :options="module.dic[item.dic]"
                                            :moduleField="item.field"
                                            :isView="true"
                                            :valueId="dataModel[item.valueFieldId]">
                                        </radio-list>
                                    </fg-input>

                                    <!-- checkbox -->
                                    <fg-input
                                        v-else-if="item.showType === 'checkbox'"
                                        v-model="dataModel[item.name]"
                                        :label="item.display"
                                        :name="item.name">
                                        <div>
                                            <l-switch :isView="true" v-model="dataModel[item.name]"></l-switch>
                                        </div>
                                    </fg-input>

                                    <!-- textarea -->
                                    <fg-input 
                                        v-else-if="item.showType === 'textarea'"
                                        readonly  
                                        :label="item.display"
                                        :placeholder="item.display" :name="item.name"
                                        v-model="dataModel[item.name]"
                                        :isView="true">
                                        <textarea
                                            class="form-control"
                                            readonly
                                            :isView="true"
                                            :placeholder="item.display"
                                            v-model="dataModel[item.name]">
                                        </textarea>
                                    </fg-input>

                                    <!-- 富文本 -->
                                    <fg-input 
                                        v-else-if="item.showType === 'richText'" 
                                        :label="item.display"
                                        :placeholder="item.display" 
                                        :name="item.name"
                                        v-model="dataModel[item.name]">
                                        <rich-text v-model="dataModel[item.name]" :isView="true"></rich-text>
                                    </fg-input>

                                    <!-- 文本框 -->
                                    <fg-input 
                                        v-else 
                                        readonly 
                                        :label="item.display" 
                                        :placeholder="item.display" 
                                        :name="item.name"
                                        :title="dataModel[item.name]"
                                        v-model="dataModel[item.name]"
                                        :isView="true">
                                        <!-- 格式化JSON数据 -->
                                        <slot>
                                            <span></span>
                                           <pre class="viewSection formatJson" v-if="item.className === 'json'" width=80>{{formatJson(dataModel[item.name])}}</pre> 
                                           <span class="viewSection" v-else>{{dataModel[item.name]}}</span>
                                        </slot>
                                    </fg-input>
                                </div>
                            </div>
                        </el-row>
                    </el-form>
                </div>

                <card v-if="attachMultiFields.length > 0">
                    <attach-list
                        v-for="f in attachMultiFields"
                        :key="f.id"
                        :resourceId="id"
                        :title="f.display"
                        v-model="dataModel[f.name]">
                    </attach-list>
                </card>
            </div>
        </template>
    </div>
</template>

<script>
    import {Tabs, TabPane} from 'element-ui'
    import ColorPickerJq from '../Inputs/ColorPickerJq'
    import {Select, Option, DatePicker} from 'element-ui'
    import formGroupSelect from '../Inputs/formGroupSelect'
    import CheckBoxList from '../Inputs/CheckboxList'
    import Attach from '../Inputs/Attach'
    import AttachList from './AttachList'
    import RadioList from '../Inputs/RadioList'
    import Switch from '../Switch'
    import ChildModuleView from './ChildModuleView'
    import RichText from '../Inputs/RichText'
    import FlowRouterButton from '../Flow/FlowRouterButton'
    import GlobalConst from '@/service/global-const'
    import { makeFormShowList } from '@/service/deal-form'

    import Skeleton from '@/components/frame/Skeleton/Skeleton'
    import iconHandler from '@/components/frame/Icon/index.js'

    export default {
        name: 'module-edit',
        components: {
            [Tabs.name]: Tabs,
            [ColorPickerJq.name]: ColorPickerJq,
            [TabPane.name]: TabPane,
            [Option.name]: Option,
            [Select.name]: Select,
            [formGroupSelect.name]: formGroupSelect,
            [CheckBoxList.name]: CheckBoxList,
            [Attach.name]: Attach,
            [AttachList.name]: AttachList,
            [RadioList.name]: RadioList,
            [Switch.name]: Switch,
            [ChildModuleView.name]: ChildModuleView,
            [RichText.name]: RichText,
            FlowRouterButton,

            Skeleton
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
                type: String
            },
            flowInfo: {
                type: Object
            },
            // 是否铺满页面，单纯模型页面时需要设置为铺满，多模块组块不需要
            isCoverPage: {
                type: Boolean,
                default: false
            },
            // 当前查看页是否dialog中被使用，默认false
            isDialogForm: {
                type: Boolean,
                default: false
            },
        },
        data() {
            return {
                activeTabName: 'main',
                buttons: [],
                childrenTab: [],
                column: 2,
                dataModel: {},
                tagCode: '',
                id: '',
                afterViewJSON: {},

                skeleton: false // 骨架
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
            showFields() {
                let fields = this.module.fields.filter(f => f.type !== 'hidden' && f.isShowInPage === 1 && f.type !== 'attachMulti')

                if (!fields) {
                    return []
                }
                let result = JSON.parse(JSON.stringify(fields))
                // TODO
                // 待删，对接模型构造数据
                // 定义展示数据列表
                let showList = []
                result.forEach (i => {
                    // 获取默认展示字段占比：1/4或者业务内部设置的比例
                    let widthPer = GlobalConst.formItem.columnWidth
                    // 判断字段是否为单独一行
                    if (i.isFullLine) {
                        widthPer = 1
                    }
                    // widthPer = Math.random() > 0.5 ? 1: widthPer
                    // 修改为elementUI 排版支持的数据占比数，用于类使用
                    this.$set(i, 'widthPer', widthPer * 24)
                })
                if (result.length !== 0) {
                    // 返回展示的数据列表
                    makeFormShowList(showList, result, 'widthPer')
                }
                return showList
            },
            attachMultiFields() {
                let fields = this.module.fields.filter(f => f.type !== 'hidden' && f.isShowInPage === 1 && f.type === 'attachMulti')
                return fields
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
            exeMethod(method) {
                this.btnMethod[method].call(this)
            },
            randerBtn(newButtons) {
                // 获取默认按钮
                let btns = Object.assign([], this.defaultButtons)

                // 如果外部JS参数没有配置按钮，那么按钮为原来的按钮
                if (typeof newButtons === 'undefined') {
                    this.buttons = btns
                    return
                }
            },
            setChildrenTab(children) {
                this.childrenTab = []
                if (!children || children.length === 0) {
                    return
                }
                children.filter(child => child.showTab === 1).forEach(child => this.childrenTab.push(child))
            },
            /**
             * @return {boolean} 如果有 tab 返回 true，否则返回 false
             */
            hasTab() {
                return this.childrenTab.length > 0
            },
            setDefaultValue() {
                if (this.module && this.module.fields) {
                    this.module.fields.filter(f => f.defaultValue).forEach(f => this.setData(f.name, f.defaultValue))
                }
            },
            editJSON(recordId) {
                this.id = this.recordId || this.$route.params.id || this.$route.query.id
                if (this.id === 'add' || this.id === 'placeholder') {
                    this.id = ''
                    if(typeof this.afterViewJSON === 'function') {
                        this.afterViewJSON();
                    }
                    return
                }
                if (this.id && this.id.toLowerCase() === 'nullstr') {
                    this.id = ''
                }
                let params = {
                    id: this.id,
                    mdCode: this.module.code
                }

                let _url = `${this.BASEURL}/jdbc/common/basecommonedit/editJSON.do`
                let customUrl = false
                if (this.module.editedJsObj.editJSONUrl) {
                    _url = this.BASEURL + this.module.editedJsObj.editJSONUrl
                    customUrl = true
                }
                // 显示骨架
                this.skeleton = true
                this.get(_url, params).then(res => {
                    // TODO
                    // let a = JSON.stringify(res)
                    // let b = JSON.stringify(this.module.fields)
                    this.module.fields.forEach(f => {
                        let value = ''
                        if (customUrl) {
                            if (res[f.name] && typeof res[f.name] === 'object' && res[f.name].hasOwnProperty('value')) {
                                value = res[f.name].value
                            } else {
                                value = res[f.name]
                            }
                        } else {
                            if (typeof res[f.name] !== 'undefined' && res[f.name].value !== null) {
                                // 数据字典类型的数据需要在这里转化为值，用于展示，值字段为【name + 'Desc'】该字段值
                                if (f.showType && f.showType === 'dic') {
                                    value = res[f.name + 'Desc'].value
                                } else {
                                    value = res[f.name].value
                                }  
                            }
                        }
                        // 关闭骨架
                        this.skeleton = false
                        this.$set(this.dataModel, f.name, String(value || '')) // 数字也需要转成字符,不然select的可能不回显,底层判断可能为===
                    })
                    this.$set(this.dataModel, 'deleteAttach', '{}')
                    if (!this.id) {
                        this.id = this.dataModel['id']
                    }
                    if(typeof this.afterViewJSON === 'function') {
                        this.afterViewJSON();
                    }
                    console.log(this.dataModel)
                })
            },
            setData(key, value) {
                this.$set(this.dataModel, key, value)
            },
            handerCheckboxAndRadioListChange(f, result) {
                this.setData(f.valueFieldText, result.text)
                this.setData(f.valueFieldId, result.id)
            },
            /**
             * 判断是否为单选的地址本，是返回true，否则返回false
             */
            isSingleAddressBook(addressType) {
                if (!addressType) {
                    return false
                }
                let arr = addressType.split('-')
                if (arr.length < 2) {
                    return false
                }
                return arr[1] === '1'
            },
            /*
            给子组件赋值用,适用于那些触发不了input和change的特殊子组件
            json: {fieldName: value}
            */
            setDataFromChild(json) {
                if (!json) return
                for (var key in json) {
                    this.dataModel[key] = json[key];
                }
            },
            _handerClosed() {
                this.$parent.close(true)
            },
            _colClass(clumnNum) {
                if (!clumnNum) {
                    clumnNum = this.column
                }
                if (clumnNum === 2) {
                    return 'col-lg-6'
                } else if (clumnNum === 3) {
                    return 'col-lg-4'
                } else {
                    return 'col-lg-12'
                }
            },
            //json格式化
            formatJson(json, options) {
                //判断是不是JSON格式
                if (typeof json == 'string') {
                    try {
                        var obj=JSON.parse(json);
                        if(typeof obj == 'object' && obj ){
                            
                        }else{
                            return json;
                        }
                    } catch(e) {
                        return json;
                    }
                } else {
                    return json;
                }
                var reg = null,
                    formatted = '',
                    pad = 0,
                    PADDING = '    '; // one can also use '\t' or a different number of spaces
                // optional settings
                options = options || {};
                // remove newline where '{' or '[' follows ':'
                options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
                // use a space after a colon
                options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

                // begin formatting...

                // make sure we start with the JSON as a string
                if (typeof json !== 'string') {
                    json = JSON.stringify(json);
                }
                // parse and stringify in order to remove extra whitespace
                json = JSON.parse(json);
                json = JSON.stringify(json);

                // add newline before and after curly braces
                reg = /([\{\}])/g;
                json = json.replace(reg, '\r\n$1\r\n');

                // add newline before and after square brackets
                reg = /([\[\]])/g;
                json = json.replace(reg, '\r\n$1\r\n');

                // add newline after comma
                reg = /(\,)/g;
                json = json.replace(reg, '$1\r\n');

                // remove multiple newlines
                reg = /(\r\n\r\n)/g;
                json = json.replace(reg, '\r\n');

                // remove newlines before commas
                reg = /\r\n\,/g;
                json = json.replace(reg, ',');

                // optional formatting...
                if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
                    reg = /\:\r\n\{/g;
                    json = json.replace(reg, ':{');
                    reg = /\:\r\n\[/g;
                    json = json.replace(reg, ':[');
                }
                if (options.spaceAfterColon) {
                    reg = /\:/g;
                    json = json.replace(reg, ': ');
                }

                $.each(json.split('\r\n'), function(index, node) {
                    var i = 0,
                        indent = 0,
                        padding = '';
                    if (node.match(/\{$/) || node.match(/\[$/)) {
                        indent = 1;
                    } else if (node.match(/\}/) || node.match(/\]/)) {
                        if (pad !== 0) {
                            pad -= 1;
                        }
                    } else {
                        indent = 0;
                    }

                    for (i = 0; i < pad; i++) {
                        padding += PADDING;
                    }
                    formatted += padding + node + '\r\n';
                    pad += indent;
                });
                return formatted;
            }
        },
        updated() {
            $('.el-upload--picture-card').hide()
            $('.attach-btn').hide()
            $('.el-upload-list__item-delete').hide()
            $('.el-upload-list__item-status-label').hide()
            $('.el-icon-close').hide()
            $('.el-icon-close-tip').hide()
            $('.colorSelect-span').hide()
            $('.colorSelect-img').hide()
            $('.colorSelect-input').attr('readonly', 'readonly')
        },
        watch: {
            module: {
                deep: true,
                immediate: true,
                handler: function () {
                    // 渲染页面按钮
                    this.randerBtn()
                    // 获取label值
                    this.setDefaultValue()
                    this.setChildrenTab(this.module.childTab)
                    this.editJSON(this.recordId)
                    if (this.module.editedJsObj.afterViewJSON) {
                        this.afterViewJSON = this.module.editedJsObj.afterViewJSON
                    }
                }
            }
        }
    }
</script>

<style scoped>

    .formatJson {
        display: block;
        font-family: inherit;
        white-space: pre-wrap;
        white-space: -moz-pre-wrap;
        white-space: -o-pre-wrap;
        word-wrap: break-word;
        padding: 0;
        margin: 0;
        font-size: 15px;
        line-height: inherit;
        color: inherit;
        word-break: break-all;
        background-color: transparent;
        border: none;
        border-radius: 0;
    }

</style>
