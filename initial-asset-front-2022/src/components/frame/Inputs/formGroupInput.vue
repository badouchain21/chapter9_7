<template>
    <div
        :class="[
        {'input-group': hasIcon},
        {'has-right-slot': addonRightIcon || this.$slots.addonRight},
        {'has-error': error},
        {'input-group-focus': focused},
        {'has-success': !error && touched && hadError}]">
        <span v-if="addonLeftIcon || $slots.addonLeft" class="input-group-addon input-group-prepend">
            <slot name="addonLeft">
                <i :class="addonLeftIcon"></i>
            </slot>
        </span>
        <slot>
            <template v-if="!isView">
                <el-input
                    :name="name"
                    v-model="editValue"
                    v-on="listeners"
                    v-bind="$attrs"
                    :class="[{valid: value && !error}, inputClasses]"
                    aria-describedby="addon-right addon-left">
                </el-input>
            </template>
            <template v-else >
                <span class="viewSection">
                    {{editValue | completeValue}}
                </span>
            </template>
        </slot>
        <span v-if="addonRightIcon || $slots.addonRight" class="input-group-addon input-group-append">
            <slot name="addonRight">
                <i :class="addonRightIcon"></i>
            </slot>
        </span>

        <slot name="infoBlock"></slot>
        <slot name="helpBlock">
            <div class="text-danger invalid-feedback text-o-1" :class="{'mt-2': hasIcon}" v-if="error">
                {{ error }}
            </div>
        </slot>
    </div>
</template>
<script>
    import completeValue from '@/filter/items/complete-value'
    export default {
        inheritAttrs: false,
        name: 'fg-input',
        props: {
            required: Boolean,
            label: String,
            error: String,
            labelClasses: String,
            inputClasses: String,
            value: [String, Number],
            addonRightIcon: String,
            addonLeftIcon: String,
            isView: Boolean,
            name: String,
            inputType: String
        },
        data () {
            return {
                editValue: '',
                touched: false,
                focused: false,
                hadError: false
            }
        },
        computed: {
            listeners () {
                return {
                    ...this.$listeners,
                    input: this.updateValue,
                    focus: this.onFocus,
                    blur: this.onBlur
                }
            },
            hasIcon () {
                const {addonRight, addonLeft} = this.$slots
                return addonRight !== undefined || addonLeft !== undefined || this.addonRightIcon !== undefined || this.addonLeftIcon !== undefined
            }
        },
        methods: {
            updateValue (evt) {
                let value = evt
                if (!this.touched && value) {
                    this.touched = true
                }
                this.$emit('input', value)
            },
            onFocus (value) {
                this.focused = true
                this.$emit('focus', value)
            },
            onBlur (value) {
                this.focused = false
                this.$emit('blur', value)
            }
        },
        mounted () {
            this.editValue = this.value
        },
        watch: {
            error (value) {
                if (value) {
                    this.hadError = true
                }
            },
            /**
             * el-input??????????????????props???value??????,?????????v-model???????????????????????????????????????
             * ??????vue???????????????,????????????????????????,?????????????????????????????????????????????el-input???,?????????emit??????????????????
             * editValue???????????????,????????????????????????????????????,?????????????????????????????????????????????
             */
            value: {
                handler (newVal, oldVal) {
                    this.editValue = newVal
                },
                deep: true
            }
        }
    }
</script>
<style>

</style>
