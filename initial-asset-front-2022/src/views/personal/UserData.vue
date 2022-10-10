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
import { mapGetters } from 'vuex'
import GlobalConst from '@/service/global-const'
import { updateInfo } from '@/api/frame/user'
import '@/directive/items/set-form-height'
export default {
    components: {
        MForm
    },
    directives: {
        // 设置页面高度
        setHeight: {
            inserted: function (el, binding) {
                el.children[0].style.height = `${el.clientHeight  - 44}px`
            }
        }
    },
    data() {
        return {
            formInfo: {
                refId: 'form',
                title: '修改资料',
                labelWidth: '89px',
                columnNum: 4
            },
            formList: [
                {
                    list: [
                        { type: 'hidden', name: 'id', value: '' },
                        { type: 'text', label: '登录账号', name: 'logonId', value: '', disabled: true, placeholder: '请输入' },
                        { type: 'text', label: '编码', name: 'code', value: '', disabled: true, placeholder: '请输入' },
                        { type: 'text', label: '邮箱', name: 'email', value: '', placeholder: '请输入', rules: [
                            { type: 'email', message: '请输入正确邮箱', trigger: 'blur' },
                        ] },
                        { type: 'text', label: '名称', name: 'name', value: '', placeholder: '请输入', rules: [
                            { required: true, message: '请输入', trigger: 'blur' },
                        ] },
                        { type: 'text', label: '电话', name: 'tel', value: '', placeholder: '请输入' },
                        { type: 'text', label: 'QQ', name: 'qq', value: '', placeholder: '请输入' }
                    ]
                }
            ],
            btnList: [
                { name: '保存', icon: '#bd-save-o', click: this.saveForm, isLoad: false}
            ]
        }
    },
    computed: {
        ...mapGetters([
            'userInfo'
        ])
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
                // 启用按钮加载状态-兼容4.0需要优先判断该按钮对象
                if (btnObj && btnObj.isLoad !== undefined) {
                    btnObj.isLoad = true
                }
                updateInfo(resForm).then(res => {
                    if (res.hasOk) {
                        this.$store.dispatch('user/setUserInfo', resForm)
                        this.alert('操作成功', 'success')
                    } else {
                        let tip = res.message || ''
                        this.alert(`操作失败 ${tip}`)
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
    },
    created: function () {
        // 初始设置
        this.formList[0].list.forEach(i => {
            i.value = this.userInfo[i.name] || ''
        })
    }
}
</script>
<style lang="scss" scoped>
</style>
