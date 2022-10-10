<template>
    <div class="">
        静态数据创建表单
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------
        <div class="bg-white m-base-form">
            <div
                class="form-title">
                <span class="sign"></span> 
                <span>静态数据表单</span>
            </div>
            <el-form
                class="base-form"
                :inline="true"
                :model="dataForm"
                :rules="formRules"
                ref="formRef" 
                label-width="110px"
                v-fixedFormWidth="'110px'">
                <div class="secForm">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="common">  
                                <span slot="label" title="普通标签">普通标签</span> 
                                <form-text v-model="dataForm.common"></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="16">
                            <el-form-item prop="largePer">  
                                <span slot="label" title="占比变宽标签">占比变宽标签</span>
                                <form-text v-model="dataForm.largePer"></form-text>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="password">  
                                <span slot="label" title="密码">密码</span>
                                <form-text v-model="dataForm.password" isPassword></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="noEdit">  
                                <span slot="label" title="不可编辑">不可编辑</span> 
                                <form-text v-model="dataForm.noEdit" disabled></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="numLint">  
                                <span slot="label" title="字数限制">字数限制</span>
                                <form-text v-model="dataForm.numLint" :maxlength="10"></form-text>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="frontEl">  
                                <span slot="label" title="前置元素">前置元素</span> 
                                <form-text v-model="dataForm.frontEl" prepend="Http://"></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="endEl">  
                                <span slot="label" title="后置元素">后置元素</span>
                                <form-text v-model="dataForm.endEl" append=".com"></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="requireEl">  
                                <span slot="label" title="必填标签">必填标签</span>
                                <form-text v-model="dataForm.requireEl"></form-text>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="noReVaEl">  
                                <span slot="label" title="非必填检验">非必填检验</span> 
                                <form-text v-model="dataForm.noReVaEl" placeholder="请输入邮箱"></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="reVaEl">  
                                <span slot="label" title="必填带校验">必填带校验</span>
                                <form-text v-model="dataForm.reVaEl" placeholder="请输入邮箱"></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="textarea">  
                                <span slot="label" title="文本域">文本域</span> 
                                <form-textarea v-model="dataForm.textarea" :maxlength="40"></form-textarea>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="select">  
                                <span slot="label" title="下拉单选">下拉单选</span>
                                <form-select  
                                    v-model="dataForm.select" 
                                    :options="demoSelectList">
                                    </form-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="selectDicCode">  
                                <span slot="label" title="下拉单选-编码">下拉单选-编码</span>
                                <form-select  v-model="dataForm.selectDicCode" dicCode="model_manage_type"></form-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="selectRequest">  
                                <span slot="label" title="下拉单选-请求对象">下拉单选-请求对象</span>
                                <form-select  v-model="dataForm.selectRequest" :request="selectRequest"></form-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="multiSelect">  
                                <span slot="label" title="下拉多选">下拉多选</span>
                                <form-select  v-model="dataForm.multiSelect" :options="demoSelectList" multiple></form-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="multiSelectDicCode">  
                                <span slot="label" title="下拉多选-编码">下拉多选-编码</span>
                                <form-select  v-model="dataForm.multiSelectDicCode" dicCode="model_manage_type" multiple></form-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="multiSelectRequest">  
                                <span slot="label" title="下拉多选-请求对象">下拉多选-请求对象</span>
                                <form-select  v-model="dataForm.multiSelectRequest" :request="selectRequest" multiple></form-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item prop="radio">  
                                <span slot="label" title="单选框">单选框</span>
                                <form-radio  v-model="dataForm.radio" :options="demoRadioList"></form-radio>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item prop="checkbox">  
                                <span slot="label" title="复选框">复选框</span>
                                <form-checkbox v-model="dataForm.checkbox" :options="demoCheckboxList"></form-checkbox>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="date1">  
                                <span slot="label" title="年月日-日期框">年月日-日期框</span>
                                <form-date
                                    v-model="dataForm.date1"
                                    placeholder="请选择日期"
                                    dateType="date"
                                    showFormat="yyyy年MM月dd日"
                                    valueFormat="yyyy-MM-dd">
                                </form-date>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="date2">  
                                <span slot="label" title="年-日期框">年-日期框</span>
                                <form-date
                                    v-model="dataForm.date2"
                                    placeholder="请选择日期"
                                    dateType="year"
                                    showFormat="yyyy年"
                                    valueFormat="yyyy">
                                </form-date>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="autoComplete">  
                                <span slot="label" title="输入建议文本">输入建议文本</span>
                                <form-autoComplete
                                    v-model="dataForm.autoComplete">
                                </form-autoComplete>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item prop="allFile">  
                                <span slot="label" title="文件">文件</span>
                                <form-file
                                    v-model="dataForm.allFile"
                                    name="allFile"
                                    dataType="text">
                                </form-file>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item prop="pictFile">  
                                <span slot="label" title="图片">图片</span>
                                <form-file
                                    v-model="dataForm.pictFile"
                                    dataType="picture-card"
                                    name="pictFile">
                                </form-file>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item prop="richText">  
                                <span slot="label" title="富文本">富文本</span>
                                <form-richText v-model="dataForm.richText"></form-richText>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
            </el-form>
            <div class="fixBottomBtn" v-fixBottom>
                <el-button 
                    v-for='(i, index) in opBtnList' 
                    :key="index"  
                    v-btnBg="i"
                    type="primary"
                    @click='exeMethod(i)'>
                    <bd-icon :name="i.icon"></bd-icon>
                    {{ i.name }}
                </el-button>
            </div>
        </div>
    </div>
</template>
<script>
    import {
        FormText,
        FormTextarea,
        FormRadio,
        FormCheckbox,
        FormSelect,
        FormDate,
        FormRichText,
        FormFile,
        FormImage,
        FormAutoComplete
    } from '@/components/frame/Common/MForm/components/items/index'
    export default {
        components: {
            FormText,
            FormTextarea,
            FormRadio,
            FormCheckbox,
            FormSelect,
            FormDate,
            FormRichText,
            FormFile,
            FormImage,
            FormAutoComplete
        },
        data () { // 定义页面变量
            return {
                dataForm: {
                    common: '',
                    largePer: '12',
                    hidden: '1222',
                    password: null,
                    noEdit: '12',
                    numLint: '12',
                    frontEl: '12',
                    endEl: '12',
                    requireEl: '1212',
                    noReVaEl: null,
                    reVaEl: '1065502552@qq.com',
                    textarea: '12',
                    radio: '1',
                    checkbox: ['1'],
                    select: '',
                    selectDicCode: '',
                    selectRequest: '',
                    multiSelect: '',
                    multiSelectDicCode: '',
                    multiSelectRequest: '',
                    date1: null,
                    date2: null,
                    autoComplete: null,
                    allFile: [],
                    pictFile: [],
                    richText: ''
                },
                selectRequest: {
                    url: '/common/commoninterface/listDicJSON', 
                    params: {
                        dicCode: 'model_manage_type'
                    }
                },
                multiSelectRequest: {
                    url: '/common/commoninterface/listDicJSON', 
                    params: {
                        dicCode: 'model_manage_type'
                    }
                },
                formRules: {
                    requireEl: [
                        { required: true, message: '请输入', trigger: 'blur' }
                    ],
                    noReVaEl: [
                        { type: 'email', message: '请输入邮箱', trigger: 'blur' }
                    ],
                    reVaEl: [
                        { required: true, message: '必填项', trigger: 'blur' },
                        { type: 'email', message: '请输入邮箱', trigger: 'blur' }
                    ],
                },
                demoRadioList: [
                    { label: '单选1', value: '1' },
                    { label: '单选2', value: '2' },
                    { label: '单选3', value: '3' }
                ],
                demoCheckboxList: [
                    { label: '复选1', value: '1' },
                    { label: '复选2', value: '2' },
                    { label: '复选3', value: '3' }
                ],
                demoSelectList: [
                    { label: '下拉1', value: '1' },
                    { label: '下拉2', value: '2' },
                    { label: '下拉3', value: '3' }
                ],
                demoAutoCompleteList: [
                    { "value": "三全鲜食（北新泾店）", "address": "长宁区新渔路144号" },
                    { "value": "Hot honey 首尔炸鸡（仙霞路）", "address": "上海市长宁区淞虹路661号" },
                    { "value": "新旺角茶餐厅", "address": "上海市普陀区真北路988号创邑金沙谷6号楼113" },
                    { "value": "泷千家(天山西路店)", "address": "天山西路438号" },
                    { "value": "胖仙女纸杯蛋糕（上海凌空店）", "address": "上海市长宁区金钟路968号1幢18号楼一层商铺18-101" },
                    { "value": "贡茶", "address": "上海市长宁区金钟路633号" },
                    { "value": "豪大大香鸡排超级奶爸", "address": "上海市嘉定区曹安公路曹安路1685号" },
                    { "value": "茶芝兰（奶茶，手抓饼）", "address": "上海市普陀区同普路1435号" },
                    { "value": "十二泷町", "address": "上海市北翟路1444弄81号B幢-107" },
                    { "value": "星移浓缩咖啡", "address": "上海市嘉定区新郁路817号" }
                ],
                opBtnList: [
                    { name: '保存', icon: 'save', method: 'saveForm', params: '' },
                    { name: '关闭', icon: 'close', method: 'popPage', params: '' }
                ]
            }
        },
        computed: {},
        methods: { // 定义函数
            validateForm () {
                return new Promise((resolve, reject) => {
                    this.$refs.formRef.validate((valid) => {
                        if (valid) {
                            resolve(this.dataForm)
                        } else {
                            reject('表单校验异常')
                            return false
                        }
                    })
                })
                
            },
            saveForm () {
                this.validateForm().then(res => {
                    alert(JSON.stringify(res))
                })
            },
            popPage () {
                this.$router.back(-1)
            },
            exeMethod (obj) {
                this[obj.method](obj.params)
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {}
    }
</script>
<style lang='scss' scoped>

</style>