<template>
    <div class="padding bg-white">
        页面引入标签dialog-表单
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------<br/>
        <el-button type="primary" @click="showDialogForm">有感情的dialog，我是标签引入，使用的时候需要先引入dialog标签</el-button>

        <m-dialog
            :ref="formInfo.id"
            :visible.sync="isShow"
            type="form"
            :isFixedDialog = "true"
            :id="formInfo.id"
            :title="formInfo.title"
            :labelWidth="formInfo.labelWidth"
            :columnNum="formInfo.columnNum"
            :dataList="formHelloList"
            :handlerList="btnList">
        </m-dialog>
    </div>
</template>
<script>
    const formId = 'formHello'
    import MDialog from '@/components/frame/Common/MDialog/index.vue'
    export default {
        components: {
            MDialog
        },
        data () { // 定义页面变量
            return {
                isShow: false,
                formInfo: {
                    id: formId,
                    title: '表单标题',
                    labelWidth: '110px',
                    columnNum: 3
                },
                formHelloList: [
                    {
                        title: '总标题',
                        list: [
                            { type: 'text', label: '普通标签', name: 'common', value: '1222', placeholder: '请输入' },
                            { type: 'text', label: '占比变宽标签', name: 'largePer', value: '12', placeholder: '请输入', columnPer: 16 },
                            { type: 'hidden', label: '隐藏元素', name: 'hidden', value: '1222' },
                            { type: 'text', label: '密码', name: 'password', value: null, placeholder: '请输入密码', isPassword: true },
                            { type: 'text', disabled: true, label: '不可编辑', name: 'noEdit', value: '12', placeholder: '请输入' },
                            { type: 'text', label: '字数限制', name: 'numLint', value: '12', placeholder: '请输入', maxlength: 10 },
                            { type: 'text', label: '前置元素', name: 'frontEl', value: '12', placeholder: '请输入', prepend: 'Http://' },
                            { type: 'text', label: '后置元素', name: 'endEl', value: '12', placeholder: '请输入', append: '.com' },
                            { type: 'text', label: '必填标签', name: 'requireEl', value: '12', placeholder: '请输入', rules: [
                                { required: true, message: '请输入', trigger: 'blur' },
                            ] },
                            { type: 'text', label: '非必填检验', name: 'noReVaEl', value: null, placeholder: '请输入邮箱', rules: [
                                { type: 'email', message: '请输入邮箱', trigger: 'blur' },
                            ] },
                            { type: 'text', label: '必填带校验', name: 'reVaEl', value: '1065502552@qq.com', placeholder: '请输入邮箱', rules: [
                                { required: true, message: '必填项', trigger: 'blur' },
                                { type: 'email', message: '请输入邮箱', trigger: 'blur' }
                            ] }, 
                            { type: 'textarea', label: '文本域', name: 'textarea', value: '12', placeholder: '请输入', maxlength: 40 },
                            { type: 'radio', status: 1, label: '单选框', name: 'radio', value: '1', options: [
                                { label: '单选1', value: '1' },
                                { label: '单选2', value: '2' },
                                { label: '单选3', value: '3' },
                            ], columnPer: 12},
                            { type: 'checkbox', label: '复选框', name: 'checkbox', value: ['1'], options: [
                                { label: '复选1', value: '1' },
                                { label: '复选2', value: '2' },
                                { label: '复选3', value: '3' },
                            ], columnPer: 12},
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
                            { type: 'date', label: '年月日-日期框', name: 'date1', value: null, placeholder: '请选择日期', columnPer: 8, dateType: 'date', showFormat: 'yyyy年MM月dd日', valueFormat: 'yyyy-MM-dd'},
                            { type: 'date', label: '年-日期框', name: 'date2', value: null, placeholder: '请选择日期', columnPer: 8, dateType: 'year', showFormat: 'yyyy年', valueFormat: 'yyyy'},
                            { type: 'autoComplete', label: '输入建议文本', name: 'autoComplete', value: null, placeholder: '请输入' },
                            { type: 'file', dataType: 'text', label: '文件', name: 'allFile', value: [], columnPer: 12, isMultiple: true, limit: 3, isOneLine: true },
                            { type: 'file', dataType: 'picture-card', label: '图片', name: 'pictFile', value: [], columnPer: 12, isMultiple: true, limit: 3, isOneLine: true },
                            { type: 'richText', label: '富文本', name: 'richText', value: '', columnPer: 24, isOneLine: true},
                        ]
                    }
                ],
                btnList: [
                    { 
                        name: '保存', isLoad: false, click: (btnObj) => {
                            btnObj.isLoad = true
                            let formObj = this.$refs[formId].$refs[formId].$refs[formId]
                            setTimeout(() => {
                                formObj.validateForm().then(res => {
                                    btnObj.isLoad = false
                                    alert(JSON.stringify(res))
                                })
                            }, 1000)                 
                        } 
                    }, {
                        name: '取消', click: () => {
                            this.$confirm('取消将不保存表单数据, 是否继续?', '提示', {          
                                confirmButtonText: '确定',          
                                cancelButtonText: '取消',          
                                type: 'warning'
                            }).then(() => {          
                                // 关闭弹窗表单
                                this.isShow = false
                            }).catch(() => {          
                                // 取消关闭        
                            })
                        }
                    }
                ],
            }
        },
        computed: {},
        methods: { // 定义函数
            showDialogForm () {
                this.isShow = true
            },
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {}
    }
</script>
<style lang='scss' scoped>

</style>