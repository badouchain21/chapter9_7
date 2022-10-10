<template>
    <div class="address-book-single"
        @mouseover="handerMouseover"
        @mouseout="handerMouseout">
        <div class="address-book-input-div">
        <input
            :name="name"
            :value="value"
            v-bind="$attrs"
            class="form-control address-book-input"
            aria-describedby="addon-right addon-left"/>
        <i class="el-input__icon"
           :class="{'el-icon-circle-close': removeIcon}"
            @click="handerRemove"></i>
        </div>
        <button type="button" class="btn btn-primary address-book-but" @click="choose">选择</button>
    </div>
</template>
<!--2;600-2-20-1-->
<script>
    export default {
        name: "form-chooseList-single",
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
            value() {
                return this.valueText || this.valueId
            }
        },
        data() {
            return {
                removeIcon: false
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
                    singleChoose: true,
                    field: this.field,
                    defaultParams: defaultParam
                }).then(data => {
                    let f = this.field
                    let d = data[0]
                    let result = {
                        id: d[f.valueFieldIdSrc],
                        text: d[f.valueFieldTextSrc],
                        data: data
                    }
                    this.$emit('change', result)
                })
            },
            handerMouseover() {
                this.removeIcon = this.valueText || this.valueId
            },
            handerMouseout() {
                this.removeIcon = false
            },
            handerRemove() {
                this.$emit('change', {id: '', text: ''})
            }
        }
    }
</script>

<style scoped lang="scss">
    .address-book-single {
        font-size: 0;
        position: relative;
    }

    .address-book-single .el-input__icon {
        position: absolute;
        top: 0;
        right: 0;
        transition: all .3s;
        color: #c0c4cc;
        font-size: 14px;
        cursor: pointer;
    }

    .address-book-input-div {
        position: relative;
        display: inline-block;
        width: calc(100% - 74px);
        margin-right: $space / 2;
    }

    .address-book-but {
        max-width: 70px;
    }
</style>
