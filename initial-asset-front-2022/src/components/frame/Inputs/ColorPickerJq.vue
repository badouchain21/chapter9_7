<template>
    <div class="form-color-picker">
        <div :id="id + '_div'" class="mdiv"/>
        <input
            v-model="colors"
            :id="id"
            type="hidden"
            style="float: left; position: relative; width: 100%; background: #fff;"
            colspan="8"
        >
    </div>
</template>
<script>
    // 颜色选择器,从后台工程搬移过来的,jQuery版
    import jquery from 'jquery'
    // import jquery from './color_picker/jquery-1.12.1.min.js'
    import colpick_js from './color_picker/colpick/js/colpick.js'
    import colpick_css from './color_picker/colpick/css/colpick.css'
    import color_select_js from './color_picker/color_select/js/color_select.js'
    import color_select_css from './color_picker/color_select/css/color_select.css'

    export default {
        name: "l-color-picker",
        props: {
            fieldName: {
                type: String
            },
            colors: {
                type: String
            },
            isSingle: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                id: Math.random().toString(16).slice(2),
                colorSelect: null,
            }
        },
        methods: {
            init() {
                let _vue = this
                this.colorSelect = $('#' + this.id + '_div').colorSelect({
                    container: $('#' + this.id),
                    imgClass: "mimg",
                    width: '100%',
                    isSingle: this.isSingle,
                    // value : this.colors,
                    vue: _vue
                })
            },
            setColor(val) {
                var json = {}
                json[this.fieldName] = val
                this.$emit('setDataFromChild', json)
            },
        },
        mounted() {
            this.init()
        },
        watch: {
            colors () {
                this.colorSelect.setValue(this.colors)
            }
        }
    };
</script>
