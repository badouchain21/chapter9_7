<template>
    <div class="m-module-radio">
        <el-radio-group v-model="value" @change="handerChange">
            <el-radio
                v-for="o in optionArr" :key="o.id" :disabled="isView"
                :label="o.id">
                {{o.text}}
            </el-radio>
        </el-radio-group>
    </div>
</template>

<script>
    import {Radio, RadioGroup} from 'element-ui'

    export default {
        name: "radio-list",
        components: {
            [Radio.name]: Radio,
            [RadioGroup.name]: RadioGroup
        },
        props: ['moduleField', 'options', 'valueId', 'isView'],
        data() {
            return {
                optionArr: [],
                valueIdChange: false,
                value: ''
            }
        },
        methods: {
            handerChange(chooseData) {
                for (let key in this.optionArr) {
                    let o = this.optionArr[key]
                    if (o.id === chooseData) {
                        this.$emit('change', {id: o.id, text: o.text})
                        return
                    }
                }
            }
        },
        watch: {
            moduleField: {
                deep: true,
                immediate: true,
                handler: function () {
                    this.optionArr = this.options
                    let dataUrl = this.moduleField.fun
                    if ((!this.selectOptions || this.selectOptions.length === 0) && dataUrl) {
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
                    this.value = this.valueId
                }
            }
        }
    }
</script>

<style scoped lang="scss">
    .card .card-category, .card label {
        color: #606266;
    }
    .m-module-radio {
        line-height: $inputHeight;
    }
</style>
