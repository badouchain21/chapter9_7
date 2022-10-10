<template>
    <div class="defaultBg">
        <div>
            <div class="fixBottomBtn"  v-show="buttons.length>0" >
                <el-button 
                    :class="'diy' + btn.id"
                    v-for='(btn, index) in buttons' 
                    :key="index" 
                    v-btnBg="btn"
                    v-if="!btn.isHide" 
                    type="primary"
                    @click='exeMethod(btn.id)'>
                    <bd-icon :name="btn.icon"></bd-icon>
                    {{ btn.name }}
                </el-button>
            </div>

            <module-form ref="mainForm" :module="module"></module-form>
            <div ref='childForm' id='childForm' class="childForm">
                <div class="childFormTitle">
                    <span>{{module.childTab.name}}</span>
                    <el-button type="primary" @click="addItem" class="marL-15">新增项</el-button>
                </div>
                <table class="el-table">
                    <thead>
                        <tr>
                            <th class="textC">操作</th>
                            <th v-for='field in module.childTab.fields' v-if='!(field.type == "hidden")'
                                :key='field.id' class="textC">
                                <span :class="[`${field.required}-required`]">{{field.display}}</span>
                                <!-- <span v-if='field.required' style="color: red;">*</span> -->
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for='(item, index) in dicItems' :key ="index">
                            <td>
                                <el-button type="danger" @click.prevent="deleteItem(index, $event)">删除</el-button>
                            </td>
                            <td v-for='(field, index) in module.childTab.fields' :key ="index" v-if='!(field.type == "hidden")'>
                                <el-input
                                    v-if="field.type === 'hidden'"
                                    type='hidden'
                                    :placeholder="field.display"
                                    :name="field.name"
                                    v-model="item[field.name]">
                                </el-input>
                                <fg-input
                                    v-if="field.type === 'text'"
                                    :placeholder="field.display"
                                    :name="genField(field.name, index)"
                                    v-model="item[field.name]"
                                    :required="field.required"
                                    v-validate='genModelValid(field.name, index)'
                                    :error="getError(field.name, index)">
                                </fg-input>
                                <!-- 下拉框 -->
                                <el-select 
                                    :error="getError(field.name)" 
                                    :required="field.required"
                                    v-validate='childModelValidations[field.name]' 
                                    v-if="field.type === 'select'"
                                    v-model="item[field.name]" :name="field.name" placeholder="请选择">
                                    <el-option
                                        v-for="d in module.childTab.dic[field.dic]"
                                        :key="d.id"
                                        :label="d.text"
                                        :value="d.id"
                                        class="select-default">
                                    </el-option>
                                </el-select>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
    import {ModuleEdit} from '@/components/frame/index'
    import ModuleUtils from '@/js/ModuleUtils'
    import ModuleForm from '@/components/frame/Module/ModuleForm.vue'
    import {Select, Option, DatePicker} from 'element-ui'
    import {Validator} from 'vee-validate'

    let default_btn = [
        {
            id: 'save',
            name: '保存',
            icon: 'save',
            click: function () {
                let _this = this;
                _this.$refs.mainForm.dataModel['child'] = JSON.stringify(this.dicItems);
                this.validate(function () {
                    _this.$refs.mainForm.validate(function () {
                        let url = `${_this.BASEURL}/jdbc/common/basecommonsave/saveDic.do?mdCode=${_this.module.code}`
                        _this.postFile(url, _this.$refs.mainForm.dataModel, (isSuccess, res) => {
                            if (res.hasOk) {
                                _this.alert('保存成功', 'success')
                                _this.close()
                            } else {
                                _this.alert(`保存失败！${res.message}`)
                            }
                        }, true)
                    });
                });
            }
        }, {
            id: 'close',
            name: '关闭',
            icon: 'close',
            click: function () {
                this.$router.go(-1)
            }
        }
    ]

    export default {
        components: {
            ModuleEdit,
            Location,
            ModuleForm,
            [Option.name]: Option,
            [Select.name]: Select,
            [DatePicker.name]: DatePicker,
            [Validator.name]: Validator,
        },
        data() {
            return {
                module: {
                    id: '',
                    code: '',
                    name: '',
                    fields: [],
                    dic: {},
                    editedJs: '',
                    editedJsObj: {},
                    childTab: {},
                    hasChild: false,
                },
                defaultButtons: default_btn,
                buttons: [],
                childModelValidations: {},
                dicItems: [],
                dicItem: {},
                text: 123
            }
        },
        computed: {
            btnMethod() {
                let obj = {}
                this.buttons.forEach(btn => obj[btn.id] = btn.click)
                return obj
            },
            hasChild() {
                return this.module.hasChild
            },
        },
        methods: {
            genModelValid(field, index) {
                return this.childModelValidations[this.genField(field, index)]
            },
            genField(field, index) {
                return field + index
            },
            validate(callback) {
                this.$validator.validateAll().then(valid => {
                    if (valid) {
                        callback()
                    }
                })
            },
            async newModuleInfo(mdCode) {
                this.module = await ModuleUtils.editModuleCfg(mdCode)
                if (this.module.childTab.length > 0) {
                    this.module.hasChild = true
                    let child = await ModuleUtils.editModuleCfg(this.module.childTab[0].module)
                    child.relatedField = this.module.childTab[0].relationEntityField
                    this.module.childTab = child;
                    this.module.childTab.fields.forEach(f => {
                        if (f.dic) {
                            this.dicItem[f.name] = 0
                        } else {
                            this.dicItem[f.name] = ''
                        }
                    })
                    this.childEditJSON()
                }
            },
            childEditJSON() {
                let id = this.$route.params.id
                if (id === 'add') {
                    return
                }

                if (id === 'placeholder') {
                    id = ''
                }
                let params = {
                    defaultSearchParam: JSON.stringify([{
                        'name': this.module.childTab.relatedField,
                        'value': id,
                        'type': 'exact-match'
                    }]),
                    mdCode: this.module.childTab.code,
                    usePage: false
                }

                let _url = `${this.INTERFACE.moduleListData}`
                this.post(_url, params).then(res => {
                    res.Rows.forEach(d => {
                        let item = new Object()
                        this.module.childTab.fields.forEach(f => {
                            item[f.name] = d[f.name]
                        })
                        this.dicItems.push(item)
                    })
                    console.log(this.module.childTab)
                    console.log(this.dicItems)
                })
            },
            close() {
                let returnPath = this.$route.query.data.returnPath
                this.$router.push({
                    path: returnPath
                })
            },
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

                this.buttons = btns
            },
            setData(key, value) {
                this.$refs.mainForm.setData(key, value)
            },
            addItem() {
                let newItem = {}
                newItem = Object.assign(newItem, this.dicItem)
                this.$set(this.dicItems, this.dicItems.length, newItem)
            },
            deleteItem(index, event) {
                this.dicItems.splice(index, 1);
            },
            getError(fieldName, index) {
                return this.errors.first(this.genField(fieldName, index))
            },
        },
        created() {
            this.newModuleInfo(this.$route.params.mdCode)
        },
        beforeRouteUpdate(to, from, next) {
            let mdCode = to.params.mdCode
            this.newModuleInfo(mdCode)
            next()
        },
        watch: {
            module: {
                deep: true,
                handler: function (newVal) {
                    // 渲染按钮的事件这里会进来两次，一次是初始，第二次是module异步请求成功后
                    // 第一次触发会使页面首先渲染默认按钮，然后等module异步加载数据后再渲染需要的按钮
                    // 现在需求是等待module加载完再显示按钮，所以第一次不需要渲染，通过module.code判断module加载状态
                    if (newVal.code) {
                        this.randerBtn(newVal.editedJsObj.editButtons)
                    }
                }
            },
            dicItems: {
                deep: false,
                handler: function () {
                    // return
                    let attributes = {}
                    this.dicItems.forEach((d, index) => {
                        this.module.childTab.fields.forEach(f => {
                            let fn = f.name + index;
                            this.childModelValidations[fn] = {};
                            if (f.required) {
                                this.childModelValidations[fn]['required'] = true
                                attributes[fn] = f.display
                            }
                            if (f.unique) {
                                this.childModelValidations[fn]['unique'] = true
                                attributes[fn] = f.display
                            }
                            if (f.validationRule) {
                                this.childModelValidations[fn][f.validationRule] = true
                                attributes[fn] = f.display
                            }
                        })
                    });
                    Validator.localize('zh_CN', {'attributes': attributes})
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
.childForm {
    margin: 10px;
    border: 1px solid $lineColor;
    padding: 10px;
    border-radius: $borderRadius;
    .childFormTitle {
        margin-bottom: $space;
    }
    .el-table {
        tr {
            display: flex;
            th, td {
                flex-shrink: 1;
                flex-grow: 1;
                padding: 7px 5px;
                border-bottom: none !important;
                &:first-child {
                    width: 80px;
                    flex-shrink: 0;
                    flex-grow: unset;
                    display: inline-table;
                }
                div {
                    width: 100%;
                }
            }
        }
        &::before {
            content: none!important;
        }
    }
}
</style>
