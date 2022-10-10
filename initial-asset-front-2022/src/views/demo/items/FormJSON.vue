<template>
    <div>
        json数据创建表单
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------<br/>
        <!-- {{JSON.stringify(formHelloList)}} -->
        <m-form
            ref="formHello"
            id="formHello"
            title="表单标题"
            labelWidth="110px"
            :columnNum="3"
            :dataList="formHelloList">
        </m-form>
        <div class="fixBottomBtn" v-fixBottom>
            <el-button 
                v-for='(i, index) in opBtnList' 
                :key="index"  
                v-btnBg="i"
                type="primary"
                @click='exeMethod(i.method)'>
                <bd-icon :name="i.icon"></bd-icon>
                {{ i.name }}
            </el-button>
        </div>
    </div>
</template>

<script>
let checkMobileNum = (rule, value, callback) => {
    if (!value) {
        // 不做必填校验
        callback()
    }
    let { isVerify, message } = verifyMobileNum(value)
    if (!isVerify) {
        callback(new Error(message))
    } else {
        callback()
    }
}
let changeFun = function (val) {
    alert(`当前选中值为：${JSON.stringify(val)}`)
    // this: 指向组件所在页面作用域
}
import { verifyMobileNum } from '@/utils/checkConcatNum'
import MForm from '@/components/frame/Common/MForm/index.vue'
export default {
    components: {
        MForm,
    },
    data() {
        return {
            formHelloList: [
                {
                    title: '模块1',
                    list: [
                        // { type: 'text', label: '这个一个普普通通标签', name: 'common', value: '1222', placeholder: '请输入' },
                        // { type: 'text', label: '占比2/3标签', name: 'largePer', value: '12', placeholder: '请输入', columnPer: 16 },
                        // { type: 'text', label: '当标签特别特别长或者内容需要区域大时的时候可以考虑使用这种，让标签与内容各占块', name: 'block1', value: '12122', isBlock: true },
                        // { type: 'text', label: '当标签特别特别长或者内容需要区域大时的时候，标签页面内容很长，但是不希望输入框的内容全部铺满时。现在输入框占12等份（总24等份）', name: 'blockAndPer', value: '12122', isBlock: true, columnPer: 12 },
                        // { type: 'text', label: '标签与内容一起独占一行', name: 'oneLine', value: '标签与内容一起独占一行，按照标准标签宽度排版', isOneLine: true },
                        // { type: 'text', label: '标签与内容一起独占一行(占比)', name: 'oneLine', value: '标签与内容一起独占一行，按照标准标签宽度排版（占比）', isOneLine: true, columnPer: 16 },
                        // { type: 'text', label: '标签与内容一起独占一行，标签全部展示', name: 'oneLineAsShowAllLabel', value: '标签与内容一起独占一行，标签内容全部展示，输入内容剩余空间自动铺满', isOneLine: true, isShowAllLabel: true },
                        // { type: 'text', label: '标签与内容一起独占一行, 标签全部展示,只铺满16等份（共24）', name: 'oneLine', value: '标签与内容一起独占一行, 标签全部展示,只铺满16等份（共24）', isOneLine: true, isShowAllLabel: true, columnPer: 16 },
                        // { type: 'hidden', label: '隐藏元素', name: 'hidden', value: '1222' },
                        { type: 'cascader', label: '级联', name: 'cascader1', value: '', separator: '+',options: [{
                            value: 'zhinan',
                            label: '指南',
                            children: [{
                                value: 'shejiyuanze',
                                label: '设计原则',
                                children: [
                                    { value: 'yizhi', label: '一致' }, 
                                    { value: 'fankui', label: '反馈'}, 
                                    { value: 'xiaolv', label: '效率' }, 
                                    { value: 'kekong', label: '可控' }]
                            }, {
                                value: 'daohang',
                                label: '导航',
                                children: [
                                    { value: 'cexiangdaohang', label: '侧向导航' }, 
                                    { value: 'dingbudaohang', label: '顶部导航' }
                                ]
                            }]
                        }, {
                            value: 'zujian',
                            label: '组件',
                            children: [{
                                value: 'basic',
                                label: 'Basic',
                                children: [
                                    { value: 'layout', label: 'Layout 布局' }, 
                                    { value: 'color', label: 'Color 色彩' }, 
                                    { value: 'typography',label: 'Typography 字体'}, 
                                    { value: 'icon', label: 'Icon 图标' }, 
                                    { value: 'button', label: 'Button 按钮'
                                }]
                            },{
                                value: 'notice',
                                label: 'Notice',
                                children: [{
                                    value: 'alert',
                                    label: 'Alert 警告'
                                }, {
                                    value: 'loading',
                                    label: 'Loading 加载'
                                }, {
                                    value: 'message',
                                    label: 'Message 消息提示'
                                }, {
                                    value: 'message-box',
                                    label: 'MessageBox 弹框'
                                }, {
                                    value: 'notification',
                                    label: 'Notification 通知'
                                }]
                            }]
                        }, {
                            value: 'ziyuan',
                            label: '资源',
                            children: [{
                                value: 'axure',
                                label: 'Axure Components'
                            }, {
                                value: 'sketch',
                                label: 'Sketch Templates'
                            }, {
                                value: 'jiaohu',
                                label: '组件交互文档'
                            }]
                        }] },
                        { type: 'addressPicker', label: '省市区', name: 'distPicker1', value: '天津市-天津市-和平区' },
                        { type: 'addressPicker', label: '省市区开关联', name: 'distPicker2', value: '天津市-天津市-和平区', relatedField: true, change: changeFun },
                        { type: 'addressPicker', label: '省市区配关联', name: 'distPicker3', value: '天津市-天津市-和平区', relatedField: 'province11-city11-area11' },
                        // 开启省市区关联字段-默认字段
                        { type: 'hidden', name: 'province', value: '' },
                        { type: 'hidden', name: 'city', value: '' },
                        { type: 'hidden', name: 'area', value: '' },
                        // 开启省市区关联字段-指定字段
                        { type: 'hidden', name: 'province11', value: '' },
                        { type: 'hidden', name: 'city11', value: '' },
                        { type: 'hidden', name: 'area11', value: '' },
                        { type: 'text', label: '密码', name: 'password', value: null, placeholder: '请输入密码', isPassword: true },
                        { type: 'text', disabled: true, label: '不可编辑', name: 'noEdit', value: '12', placeholder: '请输入' },
                        { type: 'text', label: '字数限制', name: 'numLint', value: '12', placeholder: '请输入', maxlength: 10 },
                        { type: 'text', label: '前置元素', name: 'frontEl', value: '12', placeholder: '请输入', prepend: 'Http://' },
                        { type: 'text', label: '后置元素', name: 'endEl', value: '12', placeholder: '请输入', append: '.com' },
                        { type: 'text', label: '必填标签', name: 'requireEl', value: '', placeholder: '请输入', rules: [
                            { required: true, message: '请输入', trigger: 'blur' },
                        ] },
                        { type: 'text', label: '最小长度4', name: 'requireElMin', value: '21211', placeholder: '请输入', rules: [
                            { required: true, message: '请输入', trigger: 'blur' },
                            { min: 4, message: '最小长度为4', trigger: 'blur' },
                        ] },
                        { type: 'text', label: '指定长度6', name: 'requireElLen', value: '323232', placeholder: '请输入', rules: [
                            { required: true, message: '请输入', trigger: 'blur' },
                            { min: 6, message: '指定长度为6', trigger: 'blur' },
                        ] },
                        { type: 'text', label: '最大长度8', name: 'requireElMax', value: '43', placeholder: '请输入', rules: [
                            { required: true, message: '请输入', trigger: 'blur' },
                            { max: 8, message: '最大长度为8', trigger: 'blur' },
                        ] },
                        { type: 'text', label: '指定角色范围内选取内容', name: 'requireRange', value: 'admin', placeholder: '请输入', rules: [
                            { required: true, message: '请输入', trigger: 'blur' },
                            { type: 'enum', enum: ['admin', 'user', 'guest'], message: '角色只能在admin/user/guest中选择' }
                        ] },
                        { type: 'text', label: '手机号码检验', name: 'phoneNumVerify', value: '', placeholder: '请输入手机号', rules: [
                            { validator: checkMobileNum, trigger: 'blur'}
                        ] },
                        { type: 'text', label: '非必填检验', name: 'noReVaEl', value: null, placeholder: '请输入邮箱', rules: [
                            { type: 'email', message: '请输入邮箱', trigger: 'blur' },
                        ] },
                        { type: 'text', label: '必填带校验', name: 'reVaEl', value: '1065502552@qq.com', placeholder: '请输入邮箱', rules: [
                            { required: true, message: '必填项', trigger: 'blur' },
                            { type: 'email', message: '请输入邮箱', trigger: 'blur' }
                        ] }, 
                        { type: 'textarea', label: '文本域', name: 'textarea', value: '12', placeholder: '请输入', maxlength: 40 },
                        { type: 'select', label: '下拉单选', name: 'select', value: '1', placeholder: '请选择xxx', options: [
                            { label: '下拉1', value: '1' },
                            { label: '下拉2', value: '2' },
                            { label: '下拉3', value: '3' },
                        ] },
                        { type: 'select', label: '下拉单选-编码', name: 'selectDicCode', value: '', placeholder: '请选择', dicCode: 'model_manage_type' },
                        { type: 'select', label: '下拉单选-请求对象', name: 'selectRequest', value: '', placeholder: '请选择', request: {
                            url: '/common/commoninterface/listDicJSON', 
                            params: {
                                dicCode: 'model_manage_type'
                            }
                        }},
                        { type: 'select', multiple: true, label: '下拉多选', name: 'multiSelect', value: [], placeholder: '请选择多个', options: [
                            { value: '1', label: '黄金糕'},
                            { value: '2', label: '双皮奶'},
                            { value: '3', label: '蚵仔煎'},
                            { value: '4', label: '龙须面'},
                            { value: '5', label: '北京烤鸭'}
                        ]},
                        { type: 'select', multiple: true, label: '下拉多选-编码', name: 'multiSelectDicCode', value: [], placeholder: '请选择多个', dicCode: 'model_manage_type' },
                        { type: 'select', multiple: true, label: '下拉多选-请求对象', name: 'multiSelectRequest', value: [], placeholder: '请选择多个', request: {
                            url: '/common/commoninterface/listDicJSON', 
                            params: {
                                dicCode: 'model_manage_type'
                            }
                        }},
                        { type: 'radio', status: 1, label: '单选框', name: 'radio', value: '1', options: [
                            { text: '单选1', id: '1' },
                            { text: '单选2', id: '2' },
                            { text: '单选3', id: '3' },
                        ], columnPer: 12},
                        { type: 'checkbox', label: '复选框', name: 'checkbox', value: ['1'], options: [
                            { text: '复选1', id: '1' },
                            { text: '复选2', id: '2' },
                            { text: '复选3', id: '3' },
                        ], columnPer: 12},

                        { type: 'date', label: '年月日-日期框', name: 'date1', value: null, placeholder: '请选择日期', columnPer: 8, dateType: 'date', showFormat: 'yyyy年MM月dd日', valueFormat: 'yyyy-MM-dd'},
                        { type: 'date', label: '年-日期框', name: 'date2', value: null, placeholder: '请选择日期', columnPer: 8, dateType: 'year', showFormat: 'yyyy年', valueFormat: 'yyyy'},
                        { type: 'autoComplete', label: '输入建议文本', name: 'autoComplete', value: null, placeholder: '请输入' },
                        { type: 'file', dataType: 'text', label: '文件', name: 'allFile', value: [], columnPer: 12, isMultiple: true, limit: 3, isOneLine: true },
                        { type: 'file', dataType: 'picture-card', label: '图片', name: 'pictFile', value: [], columnPer: 12, isMultiple: true, limit: 3, isOneLine: true },
                        { type: 'richText', label: '富文本', name: 'richText', value: '', columnPer: 24, isOneLine: true},
                    ]
                }, {
                    title: '模块2',
                    columnNum: 1,
                    list: [
                        { type: 'text', label: '普通标签', name: 'common2', value: '1222', placeholder: '请输入'},
                        { type: 'hidden', label: '隐藏元素', name: 'hidden2', value: '1222' },
                        { type: 'text', label: '密码', name: 'password2', value: null, placeholder: '请输入密码', isPassword: true }
                    ]
                }
            ],
            
            
            opBtnList: [
                { name: '保存', icon: 'save', method: 'saveForm', params: '' },
                { name: '关闭', icon: 'close', method: 'popPage', params: '' }
            ]
        }
    },
    computed: {
        // dataObject () {
        //     let dataObject = {}
        //     this.formHelloList.forEach(i => {
        //         i.list.forEach(j => {
        //             dataObject[j.name] = j.value
        //         })
        //     })
        //     return dataObject
        // },
    },
    methods: {
        exeMethod (id) {
            this[id]()
        },
        saveForm () {
            this.$refs.formHello.validateForm().then(res => {
                alert(JSON.stringify(res))
            })
        },
    }
}
</script>

<style scoped>

</style>




