<template>
    <div v-setFormHeight>
        <m-form
            :ref="formInfo.refId"
            :id="formInfo.refId"
            :title="formInfo.title"
            :labelWidth="formInfo.labelWidth"
            :columnNum="formInfo.columnNum"
            :dataList="formList">
        </m-form>
        <div class="fixBottomBtn">
            <el-button
                v-for='(i, index) in btnList' 
                :key="index" 
                v-btnBg="i"
                :loading="i.isLoad !== undefined && i.isLoad"
                @click='exeMethod(i)'>
                <bd-icon
                    v-if="(i.isLoad === undefined) || (i.isLoad !== undefined && !i.isLoad)"
                    :name="i.icon">
                </bd-icon>
                {{ i.name }}
            </el-button>
        </div>
    </div>
</template>

<script>
    import MForm from '@/components/frame/Common/MForm/index.vue'
    import CryptoUtils from '@/service/crypto-utils.js'
    import { updatePwd } from '@/api/frame/user'
    import '@/directive/items/set-form-height'
    export default {
    components: {
        MForm
    },
    data () {
        let validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'))
            } else {
                let hasType = 0
                var isBigEng = /[A-Z]+/
                var isSmallEng = /[a-z]+/
                var isNumber = /[0-9]+/
                var regEn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im
                if (value.match(isNumber) != null) {
                    hasType ++
                }
                if (value.match(isSmallEng) != null || value.match(isBigEng) != null) {
                    hasType ++ 
                }
                if (value.match(regEn) != null) {
                    hasType ++
                }
                if (hasType >= 2) {
                    callback()
                } else {
                    callback(new Error('密码必须包含字母、数字、特殊字符中的任意2个'))
                }
            }
        }
        let validatePassAgain = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'))
            } else if (value !== (this.formList[0].list)[1].value) {
                callback(new Error('两次输入密码不一致!'))
            } else {
                callback()
            }
        }
        return {
            formInfo: {
                refId: 'passForm',
                title: '修改密码',
                labelWidth: '120px',
            },
            formList: [
                {
                    columnNum: 1,
                    list: [
                        { type: 'text', label: '原密码', name: 'password', value: '', placeholder: '请输入', isPassword: true, rules: [
                            { required: true, message: '请输入密码', trigger: 'blur' },
                        ] },
                        { type: 'text', label: '新密码', name: 'passwordNew', value: '', placeholder: '请输入', isPassword: true, rules: [
                            { required: true, message: '请输入新密码', trigger: 'blur' },
                            { min: 8, message: '密码最少8位数', trigger: 'blur' },
                            { validator: validatePass, trigger: 'blur' }
                        ] },
                        { type: 'text', label: '确认新密码', name: 'passwordOk', value: '', placeholder: '请输入', isPassword: true, rules: [
                            { required: true, message: '请再次输入新密码', trigger: 'blur' },
                            { validator: validatePassAgain, trigger: 'blur' }
                        ] }
                    ]
                }
            ],
            btnList: [
                { name: '保存', icon: '#bd-save-o', click: this.saveForm, isLoad: false}
            ]
        }
    },
    methods: {
        exeMethod (itemObj) {
            // 绑定事件中的this为当前页面作用域
            // 另外将按钮对象itemObj扔回作为绑定事件的参数
            itemObj.click.call(this, itemObj)
        },
        saveForm (btnObj) {
            let formId = this.formInfo.refId
            this.$refs[formId].validateForm().then(resForm => {
                let params = {
                    password: CryptoUtils.aesEncrypt(resForm.password),
                    passwordNew: CryptoUtils.aesEncrypt(resForm.passwordNew),
                    passwordOk: CryptoUtils.aesEncrypt(resForm.passwordOk)
                }
                // 启用按钮加载状态-兼容4.0需要优先判断该按钮对象
                if (btnObj && btnObj.isLoad !== undefined) {
                    btnObj.isLoad = true
                }
                updatePwd(params).then(res => {
                    if (res.hasOk) {
                        this.alert('操作成功', 'success')
                    } else {
                        let tip = res.message || ''
                        this.alert(`操作失败 ${tip}`, 'error')
                    }
                }).catch(err => {

                }).finally(() => {
                    // 兼容4.0需要优先判断该按钮对象
                    if (btnObj && btnObj.isLoad !== undefined) {
                        // 请求结束，结束按钮加载状态
                        btnObj.isLoad = false
                    }
                })
            })
        }
    }
}
</script>
<style scoped>

</style>
