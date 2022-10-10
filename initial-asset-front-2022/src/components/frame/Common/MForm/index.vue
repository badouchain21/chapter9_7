<template>
    <div class="bg-white m-base-form" :class="{'firForm': title}">
        <div
            v-if="title"
            class="form-title">
            <bd-icon name="pillar-fill" class="pillar fill"></bd-icon>
            <span>{{title}}</span>
        </div>
        <el-form
            :class="{'view': isView}"
            class="base-form"
            :inline="true" 
            :model="dataForm" 
            :rules="formRules"
            :ref="id"
            v-fixedFormWidth="labelWidth">
            <div
                v-for="(i, i_index) in dataShowList"
                :key="i_index"
                class="secForm">
                <!-- 表单本身有大标题，如果子元素只有一个，那没必要再出现标题重复
                小标题的作用在于出现多个子元素时区分 -->
                <div class="title" v-if="i.title && dataShowList.length > 1">
                    <bd-icon name="point-fill" class="point fill"></bd-icon>
                    {{i.title}}
                </div>
                <el-row 
                    v-for="(j, j_index) in i.list"
                    :key="j_index"
                    :class="{'onlyOneColumn': i.columnNum===1}">
                    <el-col
                        v-for="(k, k_index) in j"
                        :key="k_index"
                        :span="columnSpan(k, k.columnPer, i.columnNum, columnNum).span">
                        <el-form-item
                            :class="{
                                'isBlock':k.isBlock, 
                                'isShowAllLabel':k.isShowAllLabel}"
                            :prop="k.name">  
                            <span slot="label" :title="k.label" class="s-label">{{k.label}}</span>
                            <!-- 表单编辑输入模块需要判断是否为插槽 -->
                            <!-- 非插槽 -->
                            <m-form-item
                                :style="k.isBlock? { width:  returnSpanPer(k.columnPer)} : ''"
                                :isView="isView"
                                v-if="k.type !== 'slot'"
                                :formType="k.type"
                                v-bind="k"
                                v-model="k.value"
                                @setRelatedField="setRelatedField">
                            </m-form-item>
                            <!-- 是插槽 使用插槽渲染 -->
                            <slot  v-else :name="k.name"></slot>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div> 
        </el-form>
    </div>
</template>
<script>
    import GlobalConst from '@/service/global-const'
    import MFormItem from '@/components/frame/Common/MForm/components/MFormItem'
    export default {
        components: {
            MFormItem
        },
        props: {
            // 表单id，用于区分唯一性，必填项（必填安全性高）
            id: {
                type: String,
                default: 'formRef'
            },
            // 表单标题
            title: {
                type: String,
                default: GlobalConst.form.title
            },
            // 标签文本宽度
            labelWidth: {
                type: String,
                default: GlobalConst.form.labelWidth
            },
            // 表单展示列数
            columnNum: {
                type: Number,
                default: GlobalConst.form.columnNum
            },
            // 表单项数据源--支持同个表单拆分多块进行展示，数组下每个对象为表单的一个子展示区
                // 默认数据格式[{
                //     title: '', --这一模块表单标题
                //     list: [{name: '', value: ''}], --这一模块表单项json数据
                //     labelWidth: String,  --这一模块表单的标签宽度
                //     column: Number --这一模块表单的展示列数
                // }]
            dataList: {
                type: Array,
                deafult: () => {
                    return []
                }
            },
            // 当前是否为查看状态，默认false（表示当前为编辑状态）
            isView: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            dataForm: function () {
                let formObject = {}
                this.dataList.forEach(i => {
                    i.list.forEach(j => {
                        formObject[j.name] = j.value
                    })
                })
                return formObject
            },
            formRules: function () {
                let rules = {}
                this.dataList.forEach(i => {
                    i.list.forEach(j => {
                        if (j.rules && j.rules.length > 0) {
                            rules[j.name] = j.rules
                        }
                    })
                })
                return rules
            },
            
            // 对于需要展示的列表，需要优化数据结构，按照row一组进行数组切割
            // 优化后的数据结构列表只作用于页面展示
            // 同时也为了上一行的样式不要影响到下一行（可自行测试）
            // <el-row>                                  <el-row>
            //     <el-col :span="6"></el-col>               <el-col :span="6"></el-col>
            //     <el-col :span="18"></el-col>  --->        <el-col :span="18"></el-col>
            //     <el-col :span="6"></el-col>           </el-row>
            //     <el-col :span="18"></el-col>          <el-row>
            // </el-row>                                     <el-col :span="6"></el-col>
            //                                               <el-col :span="18"></el-col>
            //                                           </el-row>
            dataShowList () {
                // 这里dataList会是从父级过来的数据数组，注意数组中每个对象下的list属性是需要双向数据绑定的
                // 才能编辑更改以及回显，所以其他属性只是单纯使用，不需要更改，使用深拷贝复制，list属性的数据
                // 需要使用浅拷贝，确保指向不变。注意这里只需要更改的是数据结构用于页面呈现，不需要动到数据引用地址
                let data = JSON.parse(JSON.stringify(this.dataList))
                // 清空数组，准备遍历原数据数组将对象添加进来，确保地址指向一致
                data.forEach(i => i.list = [])
                this.dataList.forEach((i, index) => {
                    this.makeShowList(data[index].list, i.list, i)
                })
                return data
            },
        },
        data () { // 定义页面变量
            return {
                num: 1,
            }
        },
        methods: { // 定义函数
            /**
             * 将字段值处理给赋值给关联键值
             * @param [Array] key 关联键数组
             * @param [Array] value 关联值
             */
            setRelatedField (key, value) {
                key.forEach((k, index) => {
                    try {
                        this.dataList.forEach(i => {
                            let itemObj = i.list.find(i => i.name === k)
                            this.$set(itemObj, 'value', value[index])
                            throw 'End' // 已找到指定值，结束当前遍历
                        })
                    } catch (e) {
                        console.log(`已找到匹配值，结束该环节遍历`)
                    }      
                })
            },
            /**
             * 返回一行所占比例%
             * @params num [Number/String] 栅格占比份数（总数24）
             */
            returnSpanPer (num) {
                if (num) {
                    return `${parseInt(num) / GlobalConst.form.gridNum * 100}%`
                }
            },
            /**
             * @params isBlock [Boolean] 是否块级展示，label与内容各占一行
             * @params isOneLine [Boolean] label跟内容是否独占一行（此时label与内容同在一行）
             * @params itemColumnSpan [Number] 表单子模块子项在一行中所占份数（共24份）
             * @params sectionColumnNum [Number] 表单子模块（一个表单支持多个模块）通用一行列数
             * @params formColumnNum [Number] 所有表单子模块通用一行列数
             * @return { span: Number 表单子模块子项在一行中所占份数,  oneLineStatus: Boolean 是否独占一行状态}
             */
            columnSpan ({isBlock, isOneLine}, itemColumnSpan, sectionColumnNum, formColumnNum) {
                // 定义返回结果
                let span = 0
                // 定义是否单独一行状态
                let oneLineStatus = false
                if (isBlock) {
                    // 若为块级，则模块位置铺满一行
                    // 注意：这里强调位置，这一行只会放一个子项元素，至于元素宽度由itemColumnSpan控制
                    //     itemColumnSpan有值则使用该值（通过样式控制，不在当前处理），无值则默认铺满
                    span = GlobalConst.form.gridNum
                    oneLineStatus = true
                } else if (isOneLine) {
                    // 若设置标签与内容同一行，同样只管理位置，确保该行只会有其一个子项，元素宽度由itemColumnSpan控制
                    // itemColumnSpan有值则使用该值，无值默认铺满
                    span = itemColumnSpan || GlobalConst.form.gridNum
                    oneLineStatus = true
                } else {
                    // 以下元素非独占一行
                    // 优先级： 各模块表单子项配置 > 各模块表单配置 > 表单配置
                    if (itemColumnSpan) {
                        span = itemColumnSpan
                    } else if (sectionColumnNum) {
                        span = GlobalConst.form.gridNum/parseInt(sectionColumnNum)
                    } else {
                        span = GlobalConst.form.gridNum/parseInt(formColumnNum)
                    }
                }
                return {
                    // 向下取整
                    span: Math.floor(span),
                    oneLineStatus: oneLineStatus
                }
                
            },
            /**
             * 表单保存检验事件
             */
            validateForm () {
                return new Promise((resolve, reject) => {
                    this.$refs[this.id].validate((valid) => {
                        if (valid) {
                            resolve(this.dataForm)
                        } else {
                            reject('表单校验异常')
                            return false
                        }
                    })
                })
                
            },
            makeShowList (showList, dataList, sectionObj) {
                // 定义临时存储列表数据
                let tempList = []
                // 定义当前列占比数
                let columnNum = 0
                // 定义最高列占比，参照elementUI，一行占比数峰值是24比例列
                let MaxcolumnNum = GlobalConst.form.gridNum
                let formShowList = dataList.filter(i => i.type !== 'hidden')
                formShowList.forEach((i, index) => {
                    // 按照优先级先后使用
                    let { span, oneLineStatus } = this.columnSpan(i, i.columnPer, sectionObj.columnPer, this.columnNum)
                    // 获取当前模块的占比份数（24等份维度）
                    let itemColumnNum = span
                    // 判断当前模块是否独占一行
                    if (oneLineStatus) {
                        // 遇到独占一行的模块，将之前的临时列表整理优先添加进结果列表
                        if (tempList.length > 0) {
                            showList.push(tempList)
                            // 清空临时数据
                            tempList = []
                            // 清空已有数
                            columnNum = 0
                        }
                        // 将独占一行的模块添加进去
                        showList.push([i])
                    } else {
                        // 计算当前行添加新增模块后后是否会超出一行（占比份数 <=24则不会）
                        if (columnNum + itemColumnNum <= MaxcolumnNum) {
                            // 若不会，则更新当前一行占比份数
                            columnNum += itemColumnNum
                            // 将数据添加进当前行
                            tempList.push(i)
                        } else {
                            // 若超出，表示新增模块需要添加到下一行，前一行模块可以结束
                            showList.push(tempList) // 将当前行添加进结果列表
                            tempList = []     // 开始下一行 
                            tempList.push(i)  // 将当前模块添加进下一行
                            columnNum = itemColumnNum  // 更新下一行已有占比份数
                        }
                        // 若当前模块为最后一个，则不管满不满一行都将临时数据行也添加进结果列表
                        if (index === formShowList.length - 1) {
                            showList.push(tempList)
                        }
                    }
                    
                })
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {
        },
        watch: {
        }
    }
</script>
<style lang='scss' scoped>
</style>
