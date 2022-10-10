<template>
    <div>
        <form-text 
            v-if="formType ==='text'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-text>
        <form-textarea 
            v-else-if="formType ==='textarea'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-textarea>
        <form-radio 
            v-else-if="formType ==='radio'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-radio>
        <form-checkbox 
            v-else-if="formType ==='checkbox'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-checkbox>
        <form-select 
            v-else-if="formType ==='select'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-select>
        <form-date
            v-else-if="formType ==='date'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-date>
        <form-autoComplete
            v-else-if="formType ==='autoComplete'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-autoComplete>
        <form-richText 
            v-else-if="formType ==='richText'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-richText>
        <form-file
            v-else-if="formType ==='file'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-file>
        <form-image
            v-else-if="formType ==='image'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-image>
        <form-cascader
            v-else-if="formType ==='cascader'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-cascader>
        <form-addressPicker
            v-else-if="formType ==='addressPicker'"
            v-bind="$attrs"
            v-on="$listeners">
        </form-addressPicker>
        

        <!-- 啥也没匹配到，就用text -->
        <form-text
            v-else
            v-bind="$attrs"
            v-on="$listeners">
        </form-text>
    </div>
</template>

<script>
import {
    FormText,
    FormTextarea,
    FormRadio,
    FormCheckbox,
    FormSelect,
    FormDate,
    FormRichText,
    FormFile,
    FormImage,
    FormAutoComplete,
    FormCascader,
    FormAddressPicker
} from '@/components/frame/Common/MForm/components/items/index'
export default {
    components: {
        FormText,
        FormTextarea,
        FormRadio,
        FormCheckbox,
        FormSelect,
        FormDate,
        FormRichText,
        FormFile,
        FormImage,
        FormAutoComplete,
        FormCascader,
        FormAddressPicker
    },
    props: {
        formType: {
            type: String,
            default: 'text'
        }
    },
    data () {
        return {
        }
    },
    methods: {},
    created () {
    },
    watch: {
    },
}
</script>

<style lang="scss" scoped>

</style>
