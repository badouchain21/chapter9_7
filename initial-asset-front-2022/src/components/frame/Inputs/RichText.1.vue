<template>
    <div>
        <script :id="containerId" type="text/plain"></script>
    </div>
</template>

<script>

    import $ from 'jquery'

    export default {
        name: "rich-text",
        props: ['value', 'isView', 'customCfg'],
        data () {
            return {
                ready: false, // 编辑器是否已经准备好
                initValue: false, // 是不是已经设置过初始值
                containerId: '',
                editor: null,
                config: {
                    toolbars: [
                        ['fullscreen', 'source', '|', 'undo', 'redo', '|',
                            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
                            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
                            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
                            'directionalityltr', 'directionalityrtl', 'indent', '|',
                            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
                            'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
                            'simpleupload', 'insertimage', 'emotion', 'scrawl', 'insertvideo', 'music', 'attachment', 'map', 'gmap', 'insertframe', 'insertcode', 'pagebreak', 'template', 'background', '|',
                            'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
                            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
                            'print', 'preview', 'searchreplace', 'drafts', 'help'
                        ]
                    ],
                    // 编辑器宽度
                    initialFrameWidth: '100%',
                    // 自动保存
                    enableAutoSave: false,
                    // 元素路径
                    elementPathEnabled: false,
                    // 字数统计
                    wordCount:false,
                },
            }
        },
        methods: {
            _UECfg() {
                if (this.customCfg) {
                    return Object.assign(this.config, this.customCfg)
                }
                return this.config
            }
        },
        created() {
            let random = Math.floor(Math.random()*1000000)
            this.containerId = 'UE-CONTAINER-' + random
        },
        mounted() {
            this.editor = UE.getEditor(this.containerId + '', this._UECfg()) // 初始化UE
            const _this = this
            const editor = this.editor

            const change = function(){
                _this.$emit('input', editor.getContent())
            }
            // 多附件，或者视频
            editor.addListener("contentChange", change);
            // 单图片
            editor.addListener("afterinsertsingleimage", change)

            editor.ready(() => {
                // ueditor 的 contentChange 时间是通过 键盘时间触发的
                // 一些特殊字符，如 @ 不会触发键盘时间，这里使用 input 时间来做
                let $textarea = $('#' + this.containerId).find('iframe').contents()
                if (window.navigator.userAgent.indexOf("MSIE") !== -1) { // document.all
                    $textarea.get(0).attachEvent('onpropertychange', change);
                }else{
                    $textarea.on('input', function () {
                        change()
                    });
                }

                _this.ready = true
                if (_this.value) {
                    editor.setContent(_this.value)
                    _this.initValue = true
                }
                if (this.isView) {
                    editor.setDisabled('preview')
                }
            })
        },
        beforeDestroy() {
            // 组件销毁的时候，要销毁 UEditor 实例
            if(this.editor !== null && this.editor.destroy) {
                this.editor.destroy();
            }
        },
        watch: {
            value: {
                immediate: true,
                handler: function () {
                    if (!this.ready || this.initValue) {
                        return
                    }
                    this.editor.setContent(this.value)
                    this.initValue = true
                }
            }
        }
    }
</script>

<style scoped>

</style>