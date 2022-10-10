<template>
    <div class="address-book-single"
        @mouseover="handerMouseover"
        @mouseout="handerMouseout">
        <div class="address-book-input-div">
            <el-input
                :name="name"
                v-bind="$attrs"
                v-model="editValue">
                <i slot="suffix" 
                    class="el-input__icon" 
                    :class="{'el-icon-circle-close': removeIcon}"
                    @click="handerRemove">
                </i>
            </el-input>
        </div>
        <el-button type="primary" class="address-book-but" @click="choose">选择</el-button>
    </div>
</template>
<!--2;600-2-20-1-->
<script>
    export default {
        name: "form-addressBook-single",
        props: {
            // 地址本的类型 格式为 2-1-20-2 对应的配置项为type-singleChoose-selectType-indexType
            addressType: String,
            field: Object, // 模型 field
            name: String,
            valueId: [String, Number],
            valueText: [String, Number]
        },
        computed: {
            value() {
                this.editValue = this.valueText || this.valueId
                return this.editValue
            }
        },
        data() {
            return {
                editValue: '',
                removeIcon: false
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
        },
        watch: {
            /**
             * el-input如果直接使用props中value的值,其使用v-model双向数据绑定会修改到父组件
             * 由于vue单向数据流,上述操作是禁止的,所以需要构造新对象数据进行绑定el-input值,再触发emit修改父亲的值
             * editValue为绑定的值,初始进入页面需要对其赋值,当其值变动时需要触发父亲值修改
             */
            value (val) {
                this.editValue = val
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
        width: calc(100% - 52px);
        margin-right: $space / 2;
    }

    .address-book-but {
        max-width: 70px;
    }
</style>
