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
        name: "form-addressBook-mult",
        components: {
            [Tag.name]: Tag,
            [Button.name]: Button
        },
        props: {
            // 地址本的类型 格式为 2-1-20-2 对应的配置项为type-singleChoose-selectType-indexType
            addressType: String,
            field: Object, // 模型 field
            name: String,
            valueId: String,
            valueText: String
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
                let type = this.addressType.split('-')
                let indexType = type[3] || null
                if (indexType) {
                    indexType = parseInt(indexType)
                }
                this.addressBook({
                    type: type[0],
                    singleChoose: type[1] === '1',
                    selectType: type[2],
                    indexType: indexType
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
