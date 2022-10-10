<template>
    <div>
        <el-checkbox-group v-model="checkList" @change="handerChange">
            <el-checkbox
                v-for="o in optionArr" :key="o.id" :disabled="isView"
                :label="o.id">{{o.text}}</el-checkbox>
        </el-checkbox-group>
    </div>
</template>

<script>
    import {Checkbox, CheckboxGroup} from 'element-ui'


    const SEPARATOR = ','

    export default {
        name: "checkbox-list",
        components: {
            [CheckboxGroup.name]: CheckboxGroup,
            [Checkbox.name]: Checkbox
        },
        props: ['moduleField', 'options', 'valueId', 'isView'],
        data() {
            return {
                checkList: [],
                optionArr: [],
                valueIdChange: false
            }
        },
        methods: {
            handerChange(chooseData) {
                let obj = {}
                chooseData.forEach(o => obj[o] = true)
                let idArr = []
                let textArr = []
                this.optionArr.filter(o => obj[o.id]).forEach(o => {
                    idArr.push(o.id)
                    textArr.push(o.text)
                })
                this.$emit('change', {id: idArr.join(SEPARATOR), text: textArr.join(SEPARATOR)})
            }
        },
        watch: {
            moduleField: {
                deep: true,
                immediate: true,
                handler: function () {
                    this.optionArr = this.options
                    let dataUrl = this.moduleField.fun
                    if ((!this.selectOptions || this.selectOptions.length == 0) && dataUrl) {
                        this.get(this.BASEURL + dataUrl, {}).then(res => {
                            this.optionArr = res
                        })
                    }
                }
            },
            valueId: {
                deep: true,
                immediate: true,
                handler: function () {
                    if (!this.valueId || this.valueIdChange) {
                        return
                    }
                    this.valueIdChange = true
                    this.checkList.push(...this.valueId.split(SEPARATOR))
                }
            }
        }
    }
</script>

<style scoped>
    .card .card-category, .card label {
        color: #606266;
    }
</style>
