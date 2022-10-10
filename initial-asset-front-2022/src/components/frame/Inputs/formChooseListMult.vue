<template>
    <div class="tag-container">
        <el-tag
            v-for="(tag, index) in tags" :key="index"
            type="info"
            :closable="true"
            @close="handerTagClose(index)">
            {{tag}}
        </el-tag>
        <el-button class="button-new-tag" size="small" @click="choose">选择</el-button>
    </div>
</template>

<script>
    import {Tag, Button} from 'element-ui'

    const SEPARATOR = ','

    export default {
        name: "form-chooseList-mult",
        components: {
            [Tag.name]: Tag,
            [Button.name]: Button
        },
        props: {
            // 获取数据的类型：数据字典| 模型| 自定义
            type: String,
            field: Object, // 模型 field
            dicData: Object,    // 数据字典数据
            editedJsObj: Object,
            name: String,
            valueId: [String, Number],
            valueText: [String, Number]
        },
        computed: {
            tags() {
                if (!this.valueText) {
                    return []
                }
                return this.valueText.split(SEPARATOR)
            }
        },
        methods: {
            choose() {
                let obj = this.editedJsObj
                let defaultParam = []
                if (obj && obj.fieldOptions && obj.fieldOptions[this.field.name]
                        && obj.fieldOptions[this.field.name].defaultParam) {
                    defaultParam = obj.fieldOptions[this.field.name].defaultParam.call(this, this.dataModel)
                }
                this.chooseList({
                    type: this.type,
                    dicData: this.dicData,
                    singleChoose: false,
                    field: this.field,
                    defaultParams: defaultParam
                }).then(data => {
                    let f = this.field
                    let idArr = []
                    let textArr = []
                    data.forEach(d => {
                        idArr.push(d[f.valueFieldIdSrc])
                        textArr.push(d[f.valueFieldTextSrc])
                    })
                    let id = ''
                    if (this.valueId) {
                        id = this.valueId + SEPARATOR
                    }
                    let text = ''
                    if (this.valueText) {
                        text = this.valueText + SEPARATOR
                    }
                    id += idArr.join(SEPARATOR)
                    text += textArr.join(SEPARATOR)
                    this.$emit('change', {id, text, data})
                })
            },
            handerTagClose(index) {
                let idArr = this.valueId.split(SEPARATOR)
                let textArr = this.valueText.split(SEPARATOR)
                let deleteId = idArr.splice(index, 1)
                let deleteText = textArr.splice(index, 1)
                this.$emit('change', {id: idArr.join(SEPARATOR), text: textArr.join(SEPARATOR), deleteId, deleteText})
            }
        }
    }
</script>

<style scoped>
    .tag-container .el-tag, .el-tag.el-tag--info {
        margin-left: 0px;
        margin-right: 5px;
    }
</style>
