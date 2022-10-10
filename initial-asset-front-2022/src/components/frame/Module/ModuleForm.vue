<template>
    <div :class="'module-form '+module.code " class="formPage">
        <!-- <card> -->
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
                            <label class="m-form-item-label" v-setLabelWidth="labelWidth">
                                <skeleton :loading="skeleton" :width="comp_labelSkelWidth(labelWidth)"></skeleton>
                                <span class="require-sign" v-if="item.required">*</span>    
                                <span :title="item.display">{{item.display}}</span>
                            </label>
                            <div class="m-form-item-content">
                                <skeleton :loading="skeleton" :width="comp_valueSkelWidth(labelWidth)"></skeleton>
                                <!-- 只读 -->
                                <fg-input v-if="item.type === 'readonly'" disabled :label="item.display"
                                        :placeholder="item.display"
                                        v-model="dataModel[item.name]"></fg-input>

                                <!-- 密码 -->
                                <fg-input v-else-if="item.type === 'password'" type="password" :label="item.display"
                                        :placeholder="item.display" v-model="dataModel[item.name]"></fg-input>

                                <!-- 下拉框 -->
                                <fg-select
                                    v-else-if="item.type === 'select' && 
                                        (!String(item.className).includes('mulitSelect'))"
                                    :label="item.display"
                                    :placeholder="item.display"
                                    v-model="dataModel[item.name]"
                                    @text-change="dataModel[item.valueFieldText] = $event"
                                    :selectChange="selectChange"
                                    @updateAttr="updateAttr"
                                    :options="tempModuleDic[item.dic]"
                                    :moduleField="item"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    @change="handerCheckboxAndRadioListChange(item, $event)"
                                    :error="getError(item.name)">
                                </fg-select>

                                <!-- 下拉框多选 -->
                                <fg-select
                                    v-else-if="item.type === 'select' && 
                                        (String(item.className).includes('mulitSelect'))"
                                    :label="item.display"
                                    :placeholder="item.display"
                                    v-model="dataModel[item.name]"
                                    @text-change="dataModel[item.valueFieldText] = $event"
                                    :selectChange="selectChange"
                                    @updateAttr="updateAttr"
                                    :options="tempModuleDic[item.dic]"
                                    :moduleField="item"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    @change="handerCheckboxAndRadioListChange(item, $event)"
                                    :multiple="true"
                                    :error="getError(item.name)">
                                </fg-select>

                                <!-- 日期时间 -->
                                <fg-input
                                    v-else-if="item.type === 'dateTime'"
                                    v-model="dataModel[item.name]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <el-date-picker
                                        v-model="dataModel[item.name]"
                                        type="datetime"
                                        size="medium"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        placeholder="请选择"
                                        prefix-icon="el-icon-date"
                                        @change="reValidateField(item.name)">
                                    </el-date-picker>
                                </fg-input>

                                <!-- 日期 -->
                                <fg-input
                                    v-else-if="item.type === 'date'"
                                    v-model="dataModel[item.name]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <el-date-picker
                                        v-model="dataModel[item.name]"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        placeholder="请选择"
                                        @change="reValidateField(item.name)">
                                    </el-date-picker>
                                </fg-input>

                                <!-- 年份 -->
                                <fg-input
                                    v-else-if="item.type === 'year'"
                                    v-model="dataModel[item.name]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <el-date-picker
                                        v-model="dataModel[item.name]"
                                        type="year"
                                        value-format="yyyy"
                                        placeholder="请选择"
                                        @change="reValidateField(item.name)">
                                    </el-date-picker>
                                </fg-input>

                                <!-- 颜色选择器-多选 -->
                                <fg-input
                                    v-else-if="item.type === 'color-multiple'"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <!-- <l-color-picker
                                        :fieldName="item.name"
                                        :colors="dataModel[item.name]"
                                        :isSingle="false"
                                        @setDataFromChild="setDataFromChild"
                                    >
                                    </l-color-picker> -->
                                </fg-input>
                                <!-- 颜色选择器-单选 -->
                                <fg-input
                                    v-else-if="item.type === 'color-single'"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <l-color-picker
                                        :fieldName="item.name"
                                        :colors="dataModel[item.name]"
                                        :isSingle="true"
                                        @setDataFromChild="setDataFromChild"
                                    >
                                    </l-color-picker>
                                </fg-input>

                                <!-- 图片-多选 -->
                                <fg-input
                                    v-else-if="item.type === 'img'"
                                    :label="item.display">
                                    <l-attach
                                        type='img'
                                        :resourceId="id"
                                        :fileType='item.fileType'
                                        :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                        :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                        :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                        :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                        :isSingle="false"
                                        @removeAttach="removeAttach(item.valueFieldId ? item.valueFieldId : item.name, $event)"
                                        @setAttach="setAttach"
                                    >
                                    </l-attach>
                                </fg-input>
                                <!-- 图片-单选 -->
                                <fg-input
                                    v-else-if="item.type === 'imgSingle'"
                                    :label="item.display">
                                    <l-attach
                                        type='img'
                                        :resourceId="id"
                                        :fileType='item.fileType'
                                        :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                        :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                        :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                        :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                        :isSingle="true"
                                        @removeAttach="removeAttach(item.valueFieldId ? item.valueFieldId : item.name, $event)"
                                        @setAttach="setAttach"
                                    >
                                    </l-attach>
                                </fg-input>

                                <!-- 附件 -->
                                <fg-input
                                    v-else-if="item.type === 'attach'"
                                    :label="item.display">
                                    <l-attach
                                        type='attach'
                                        :resourceId="id"
                                        :fileType='item.fileType'
                                        :fieldName="item.valueFieldId ? item.valueFieldId : item.name"
                                        :fieldTextName="item.valueFieldText ? item.valueFieldText : ''"
                                        :attach="dataModel[item.valueFieldId ? item.valueFieldId : item.name]"
                                        :attachName="item.valueFieldText ? dataModel[item.valueFieldText] : ''"
                                        :isSingle="true"
                                        @removeAttach="removeAttach(item.valueFieldId ? item.valueFieldId : item.name, $event)"
                                        @setAttach="setAttach"
                                    >
                                    </l-attach>
                                </fg-input>

                                <!-- 地址本 单选 -->
                                <fg-input
                                    v-else-if="item.type === 'addressbook' && isSingleAddressBook(item.addressType)"
                                    v-model="dataModel[item.valueFieldText]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <form-addressBook-single
                                        :name="item.valueFieldText"
                                        :addressType="item.addressType"
                                        :field="item"
                                        :valueId="dataModel[item.valueFieldId]"
                                        :valueText="dataModel[item.valueFieldText]"
                                        @change="handerAddressBookChange(item, $event)">
                                    </form-addressBook-single>
                                </fg-input>

                                <!-- 地址本 多选 -->
                                <fg-input
                                    v-else-if="item.type === 'addressbook' && !isSingleAddressBook(item.addressType)"
                                    v-model="dataModel[item.name]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <form-addressBook-mult
                                        :name="item.valueFieldText"
                                        :addressType="item.addressType"
                                        :field="item"
                                        :valueId="dataModel[item.valueFieldId]"
                                        :valueText="dataModel[item.valueFieldText]"
                                        @change="handerAddressBookChange(item, $event)">
                                    </form-addressBook-mult>
                                </fg-input>

                                <!-- 弹出框列表选择 单选 -->
                                <fg-input
                                    v-else-if="item.type === 'selectListData'"
                                    v-model="dataModel[item.valueFieldText]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <form-chooseList-single
                                        :name="item.valueFieldText"
                                        :field="item"
                                        :dicData="tempModuleDic"
                                        :editedJsObj="module.editedJsObj"
                                        :valueId="dataModel[item.valueFieldId]"
                                        :valueText="dataModel[item.valueFieldText]"
                                        @change="handerChooseListChange(item, $event)">
                                    </form-chooseList-single>
                                </fg-input>

                                <!-- 弹出框列表选择 多选 -->
                                <fg-input
                                    v-else-if="item.type === 'selectListDataMulti'"
                                    v-model="dataModel[item.valueFieldText]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <form-chooseList-mult
                                        :name="item.valueFieldText"
                                        :field="item"
                                        :dicData="tempModuleDic"
                                        :editedJsObj="module.editedJsObj"
                                        :valueId="dataModel[item.valueFieldId]"
                                        :valueText="dataModel[item.valueFieldText]"
                                        @change="handerChooseListChange(item, $event)">
                                    </form-chooseList-mult>
                                </fg-input>

                                <!-- CheckboxList -->
                                <fg-input
                                    v-else-if="item.type === 'checkboxlist'"
                                    v-model="dataModel[item.name]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <checkbox-list
                                        :options="tempModuleDic[item.dic]"
                                        :moduleField="item"
                                        :valueId="dataModel[item.valueFieldId]"
                                        @change="handerCheckboxAndRadioListChange(item, $event)">
                                    </checkbox-list>
                                </fg-input>

                                <!-- radio -->
                                <fg-input
                                    v-else-if="item.type === 'radio'"
                                    v-model="dataModel[item.name]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <radio-list
                                        :options="tempModuleDic[item.dic]"
                                        :moduleField="item"
                                        :valueId="dataModel[item.valueFieldId]"
                                        @change="handerCheckboxAndRadioListChange(item, $event)">
                                    </radio-list>
                                </fg-input>

                                <!-- checkbox -->
                                <fg-input
                                    v-else-if="item.type === 'checkbox'"
                                    v-model="dataModel[item.name]"
                                    :label="item.display"
                                    :name="item.name"
                                    :required="item.required"
                                    v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)">
                                    <div>
                                        <l-switch v-model="dataModel[item.name]"></l-switch>
                                    </div>
                                </fg-input>

                                <!-- textarea -->
                                <fg-input v-else-if="item.type === 'textarea'" :label="item.display"
                                    :placeholder="item.display" :name="item.name"
                                    :required="item.required" v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)"
                                    v-model="dataModel[item.name]">
                                    <!-- 添加类名noResize去除自由拖动尺寸 -->
                                    <el-input 
                                        type="textarea" 
                                        class="form-control noResize"
                                        :autosize="{ minRows: textareaRows ,maxRows: textareaRows*2}"
                                        :placeholder="item.display"
                                        v-model="dataModel[item.name]"
                                        @keydown="reValidateField(item.name)"></el-input>
                                </fg-input>

                                <fg-input v-else-if="item.type === 'richText'" :label="item.display"
                                    :placeholder="item.display" :name="item.name" :inputType="inputType.richText"
                                    :required="item.required" v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)"
                                    v-model="dataModel[item.name]">
                                    <rich-text v-model="dataModel[item.name]"
                                        :customCfg="_UECustomCfg(item.name)"
                                        @input="reValidateField(item.name)">
                                    </rich-text>
                                </fg-input>

                                <span v-else-if="item.type==='textEnent'" @click="textEnent(item.editTip)">{{item.display}}</span>


                                <fg-input v-else :label="item.display"
                                    :name="item.name"
                                    :required="item.required" v-validate="modelValidations[item.name]"
                                    :error="getError(item.name)"
                                    v-model="dataModel[item.name]">
                                </fg-input>
                            </div>
                        </div>
                    </el-row>
                </el-form>
            </div>
        </div>
        <!-- </card> -->

        <card v-if="attachMultiFields.length > 0">
            <attach-list
                v-for="f in attachMultiFields"
                :key="f.id"
                :resourceId="id"
                :title="f.display"
                :config="getAttachConfig(f.name)"
                v-model="dataModel[f.name]"
                @removeAttach="removeAttach(f.name, $event)">
            </attach-list>
        </card>
    </div>
</template>

<script>
    import {Select, Option, DatePicker} from 'element-ui'
    import formGroupSelect from '../Inputs/formGroupSelect'
    // import ColorPickerJq from '../Inputs/ColorPickerJq'
    import Attach from '../Inputs/Attach'
    import AttachList from './AttachList'
    import {Validator} from 'vee-validate'
    import FormAddressBookSingle from "../Inputs/formAddressBookSingle"
    import FormAddressBookMult from "../Inputs/formAddressBookMult"
    import FormChooseListSingle from "../Inputs/formChooseListSingle"
    import FormChooseListMult from "../Inputs/formChooseListMult"
    import CheckBoxList from '../Inputs/CheckboxList'
    import RadioList from '../Inputs/RadioList'
    import Switch from '../Switch'
    import RichText from '../Inputs/RichText'
    import GlobalConst from '@/service/global-const'
    import { makeFormShowList } from '@/service/deal-form'

    import Skeleton from '@/components/frame/Skeleton/Skeleton'

    export default {
        name: "module-form",
        components: {
            [Option.name]: Option,
            [Select.name]: Select,
            [formGroupSelect.name]: formGroupSelect,
            [DatePicker.name]: DatePicker,
            // [ColorPickerJq.name]: ColorPickerJq,
            [Attach.name]: Attach,
            [AttachList.name]: AttachList,
            [FormAddressBookSingle.name]: FormAddressBookSingle,
            [FormAddressBookMult.name]: FormAddressBookMult,
            [FormChooseListSingle.name]: FormChooseListSingle,
            [FormChooseListMult.name]: FormChooseListMult,
            [Validator.name]: Validator,
            [CheckBoxList.name]: CheckBoxList,
            [RadioList.name]: RadioList,
            [Switch.name]: Switch,
            [RichText.name]: RichText,

            Skeleton
        },
        props: {
            module: {
                type: Object,
                required: true
            },
            column: {
                type: Number,
                default: 2
            },
            // 当前编辑页是否dialog中被使用，默认false
            isDialogForm: {
                type: Boolean,
                default: false
            },
            recordId: {
                type: String,
                default: ''
            },
            labelWidth: {
                type: [Number, String],
                default: GlobalConst.form.labelWidth
            }
        },
        directives: {
            setLabelWidth: function (el, binding) {
                setTimeout(() => {
                    el.style.width = `${parseInt(binding.value)}px`                    
                    el.nextElementSibling.style.width = `calc(100% - ${parseInt(binding.value)}px)`
                })
                
            }
        },
        data() {
            return {
                textareaRows: GlobalConst.textarea.rows,
                // 临时模型数据字典
                tempModuleDic: {},
                inputType: {
                    richText : 'richText'
                },
                dataModel: {},
                tagCode: '',
                id: '',
                modelValidations: {},
                editorOption: {
                    modules: {
                        toolbar: [
                            ['bold', 'italic', 'underline', 'strike'],
                            ['blockquote', 'code-block'],
                            [{'header': 1}, {'header': 2}],
                            [{'list': 'ordered'}, {'list': 'bullet'}],
                            [{'script': 'sub'}, {'script': 'super'}],
                            [{'indent': '-1'}, {'indent': '+1'}],
                            [{'direction': 'rtl'}],
                            [{'size': ['small', false, 'large', 'huge']}],
                            [{'header': [1, 2, 3, 4, 5, 6, false]}],
                            [{'font': []}],
                            [{'color': []}, {'background': []}],
                            [{'align': []}],
                            ['clean'],
                            ['link', 'image', 'video']
                        ],
                        history: {
                            delay: 1000,
                            maxStack: 50,
                            userOnly: false
                        },
                        imageDrop: true,
                        imageResize: {
                            displayStyles: {
                                backgroundColor: 'black',
                                border: 'none',
                                color: 'white'
                            },
                            modules: ['Resize', 'DisplaySize', 'Toolbar']
                        }
                    }
                },
                afterEditJSON: {},
                selectChange: [],

                skeleton: false // 骨架
            }
        },
        computed: {
            showFields() {
                let fields = this.module.fields.filter(f => f.type !== 'hidden' && f.type !== 'attachMulti')

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
                    // TODO: 测试，待删
                    // widthPer = 1/3
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
                let fields = this.module.fields.filter(f => f.type === 'attachMulti')
                return fields
            },
            comp_labelSkelWidth() {
                return function(labelWidth) {
                    return `${parseInt(labelWidth) - 10}px`
                }
            },
            comp_valueSkelWidth() {
                return function(labelWidth) {
                    return `calc(100% - ${labelWidth} - 20px)`
                }
            },
        },
        methods: {
            /**
             * 修改当前页面域属性
             * @params obj 对象[Object]
             * @params attrName 属性名[String]
             * @params value 值[*]
             */
            updateAttr (obj, attrName, value) {    
                this.$set(this[obj], attrName, value)
            },
            editJSON(recordId) {
                this.id = recordId || (this.$route && (this.$route.params.id || this.$route.query.id))
                if (this.id === 'add' || this.id === 'placeholder') {
                    this.id = ''
                    if(typeof this.afterEditJSON === 'function') {
                        this.afterEditJSON();
                    }
                    return
                }

                if (this.id && this.id.toLowerCase() === 'nullstr') {
                    this.id = ''
                }

                let _url = `${this.BASEURL}/jdbc/common/basecommonedit/editJSON.do`
                let customUrl = false
                if (this.module.editedJsObj.editJSONUrl) {
                    _url = this.BASEURL + this.module.editedJsObj.editJSONUrl
                    customUrl = true
                }

                // 没有id，并且不是自定义url时，不需要查询数据
                if (!this.id && !customUrl) {
                    return
                }

                let params = {
                    id: this.id,
                    mdCode: this.module.code
                }
                // 显示骨架
                this.skeleton = true
                this.get(_url, params).then(res => {
                    let data = res
                    this.module.fields.forEach(f => {
                        let value = ''
                        if (customUrl) {
                            // write by cjb, waiting checking
                            if (res[f.name] && typeof res[f.name] === 'object' && res[f.name].hasOwnProperty('value')) {
                                value = res[f.name].value
                            } else {
                                value = res[f.name]
                            }
                        } else {
                            value = typeof data[f.name] !== 'undefined' && data[f.name].value !== null ? data[f.name].value : ''
                        }
                        //下拉框数据处理
                        if(f.type==='select' &&
                            (String(f.className).includes('mulitSelect')) &&
                            value){
                            let vals=value.split(',')
                            let newVals=[]
                            for(let k in vals){
                                newVals.push(String(vals[k]))
                            }
                            this.$set(this.dataModel, f.name, newVals) // 下拉多选
                        }else{
                           // 数据字典类型的数据需要在这里转化为值，用于展示，值字段为【name + 'Desc'】该字段值
                            if (f.type==='select'&&f.dic) {
                              if(res[f.name + 'Desc'])
                                value = res[f.name + 'Desc'].value
                            } 
                         this.$set(this.dataModel, f.name, String(value || '')) // 数字也需要转成字符,不然select的可能不回显,底层判断可能为===
                        }
                    })
                    // 关闭骨架
                    this.skeleton = false
                    this.$set(this.dataModel, 'deleteAttach', '{}')
                    if (!this.id) {
                        this.id = this.dataModel['id']
                    }
                    if (this.id && !this.dataModel['id']) {
                        this.dataModel['id'] = this.id
                    }
                    if(typeof this.afterEditJSON === 'function') {
                        this.afterEditJSON();
                    }
                })
            },
            setData(key, value) {
                this.$set(this.dataModel, key, value)
            },
            clearData() {
                for (let key in this.dataModel) {
                    this.dataModel[key] = ''
                }
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
            /**
             *
             * @param {String} field 附件属性的属性名
             * @param {Array} attachIds 需要删除的附件id
             */
            removeAttach(field, attachIds) {
                let deleteAttach = JSON.parse(this.dataModel['deleteAttach']);
                attachIds.forEach(id => {
                    if (deleteAttach[field]) deleteAttach[field] += `,${id}`
                    else deleteAttach[field] = id
                })
                this.dataModel['deleteAttach'] = JSON.stringify(deleteAttach)
            },
            /*
             附件上传类调用的方法:记录下要上传的附件
             json: {fieldName: attachArr}
             */
            setAttach(json) {
                if (!json) return
                for (var key in json) {
                    this.dataModel[key] = json[key];
                }
            },
            renValidator() {
                let attributes = {}, hasField = false;
                this.module.fields.forEach((f, index) => {
                    let rules = {}
                    // 处理字段的验证规则
                    let unique = false
                    if (f.validationRule) {
                        let arr = f.validationRule.split(';')
                        arr.forEach(r => {
                            if (r === 'unique') {
                                unique = true
                            } else if (r === 'required') {
                                // f 中的 required 属性控制是否显示星号，这里把它更新为true
                                f.required = true
                            } else if (r.indexOf('=') === -1) {
                                rules[r] = true
                            } else {
                                let tmp = r.split('=')
                                rules[tmp[0]] = tmp[1]
                            }
                        })
                    }

                    // 必填
                    if (f.required) {
                        rules.required = true
                    }

                    // 字符串最大长度
                    if (!rules.max && f.maxlength) {
                        rules.max = f.maxlength
                    }

                    // 唯一校验
                    if (unique || f.unique) {
                        let vName = 'unique-' + index
                        this.addUniqueValidator(vName, f)
                        rules[vName] = true
                    }

                    attributes[f.name] = f.display
                    this.modelValidations[f.name] = rules
                })
                Validator.localize('zh_CN', {'attributes': attributes})
            },
            addUniqueValidator(vName, field) {
                let url = this.INTERFACE.validUnique
                let params = {
                    id: this.id,
                    mdCode: this.module.code,
                    key: field.name
                }
                Validator.extend(vName, {
                    getMessage: fieldName => fieldName + '已存在',
                    validate: value => {
                        let resolve
                        params.value = value
                        this.get(url, params).then(res => {
                            resolve({
                                valid: !res
                            })
                        })
                        return new Promise(r => resolve = r)
                    }
                })
            },
            getError(fieldName) {
                return this.errors.first(fieldName)
            },
            validate(callback) {
                this.$validator.validateAll().then(valid => {
                    if (valid) {
                        callback()
                    }
                })
            },
            getAttachConfig(fieldName) {
                let fieldOptions = this.module.editedJsObj.fieldOptions
                if (!fieldOptions || !fieldOptions[fieldName] || !fieldOptions[fieldName].config) {
                    return {}
                }
                return fieldOptions[fieldName].config
            },
            /**
             * 处理单选地址本 change 事件
             */
            handerAddressBookChange(field, result) {
                if(result.text){
                    this.setData(field.valueFieldText, result.text)
                }else{
                     this.setData(field.valueFieldText, result.data[0].name)
                }
                if(result.id){
                    this.setData(field.valueFieldId, result.id)
                }else{
                     this.setData(field.valueFieldId, result.data[0].id)
                }

                let obj = this.module.editedJsObj
                if (obj && obj.fieldOptions && obj.fieldOptions[field.name]
                    && obj.fieldOptions[field.name].addressBookCallback) {
                    obj.fieldOptions[field.name].addressBookCallback.call(this, this.dataModel, result)
                }
                this.reValidateField(field.name)
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
            /**
             * 处理单选列表选择框 change 事件
             */
            handerChooseListChange(field, result) {
                this.setData(field.valueFieldId, result.id)
                this.setData(field.valueFieldText, result.text)

                let obj = this.module.editedJsObj
                if (obj && obj.fieldOptions && obj.fieldOptions[field.name]
                    && obj.fieldOptions[field.name].chooseListCallback) {
                    obj.fieldOptions[field.name].chooseListCallback.call(this, this.dataModel, result)
                }
                this.reValidateField(field.name)
            },
            /**
             * 触发字段的验证
             */
            reValidateField(field) {
                setTimeout(() => this.$validator.validate(field))
            },
            setDefaultValue() {
                if (this.module && this.module.fields) {
                    this.module.fields.filter(f => f.defaultValue).forEach(f => this.setData(f.name, f.defaultValue))
                }
            },
            handerCheckboxAndRadioListChange(f, result) {
                this.setData(f.valueFieldText, result.text)
                this.setData(f.valueFieldId, result.id)
                this.reValidateField(f.name)
            },
            _UECustomCfg(fieldName) {
                let obj = this.module.editedJsObj
                if (obj.fieldOptions && obj.fieldOptions[fieldName] && obj.fieldOptions[fieldName].ueditorCfg) {
                    return obj.fieldOptions[fieldName].ueditorCfg
                }
                return null
            },
            textEnent(method){
                eval(method);
            }
        },
        watch: {
            module: {
                deep: true,
                immediate: true,
                handler: function (newVal, oldVal) {
                    if (!this.module || !this.module.code) {
                        return
                    }
                    if (this.module.editedJsObj.afterEditJSON) {
                        this.afterEditJSON = this.module.editedJsObj.afterEditJSON
                    }
                    if (this.module.editedJsObj.selectChange) {
                        this.selectChange = this.module.editedJsObj.selectChange
                    }
                    // 需求：动态改变数据字典
                    // BUG：更改 module.dic 触发 module 监听事件导致页面刷新数据（重新去后台获取数据赋值到当前页面）
                    // 解决方案：将原本数据字典存放的 module.dic 位置更改为 tempModuleDic
                    if (this.module.dic) {
                        // 深拷贝：为了更改数据字典时不再触发 module 的监听事件
                        this.tempModuleDic = JSON.parse(JSON.stringify(this.module.dic))
                    }
                    this.setDefaultValue()
                    if (!oldVal || (newVal.code !== oldVal.code)) {
                        this.editJSON(this.recordId)
                    }
                    this.renValidator()
                }
            }
        },
    }
</script>

<style lang="scss" scoped>

</style>
