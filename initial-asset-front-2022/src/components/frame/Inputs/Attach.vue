<template>
    <div class="form-check form-check-attach">
        <el-upload
            ref="upload"
            action=""
            :name="fieldName"
            :accept="accept"
            :file-list='fileList'
            :multiple='multiple'
            :show-file-list='showFileList'
            :drag='drag'
            :list-type='listType'
            :auto-upload="autoUpload"
            :limit="limit"
            :on-exceed="onExceed"

            :on-preview="handlePreview"
            :before-remove="beforeRemove"
            :on-change="onChange"
        >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
                <slot name="el-upload__tip"></slot>
            </div>
        </el-upload>
    </div>
</template>
<script>
    import Vue from 'vue'
    import {Upload, Dialog} from 'element-ui'

    export default {
        name: 'l-attach',
        components: {
            [Upload.name]: Upload,
            [Dialog.name]: Dialog
        },
        props: {
            type: { // 可选[img, attach],用于决定展示类型
                type: String
            },
            fieldName: { // attachId的字段名
                type: String
            },
            fieldTextName: { // attachName的字段名
                type: String
            },
            attach: { // 可以放置附件id或者附件数组
                type: [String, Array]
            },
            attachName: { // 可以放置附件名称或者附件名称数组
                type: [String, Array]
            },
            resourceId: { // 业务数据的ID
                type: String
            },
            fileType: { // 文件类型
                type: String,
            },
            isSingle: { // 是否单选
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                downloadURL: Vue.prototype.BASEURL + '/attach/action/attachsupport/downloadAttach.do?attachId=',
                listURL: Vue.prototype.BASEURL + '/attach/action/attachsupport/listJSON.do',
                fileList: [],
                hasInit: false,

                accept: "", // 允许上传的类型
                drag: false,   // 是否可以拖拽上传
                listType: this.type === 'img' ? 'picture-card' : 'text', // 列表展示类型  text/picture/picture-card
                autoUpload: false,  // 是否自动上传附件
                showFileList: true, // 是否展示文件列表
                multiple: this.isSingle ? false : true,   // 是否可以多选
            }
        },
        computed: {
            // 限制上传数量
            limit() {
                return this.isSingle ? 1 : 0
            }
        },
        methods: {
            // 超出上传数量限制时
            onExceed() {
                Vue.prototype.alert("只允许上传一个!")
            },
            handlePreview(file) {
                if (this.type === 'img') {
                    let obj = {}
                    if (file.attachId) {
                        obj.attachId = file.attachId
                    } else {
                        obj.url = file.url
                    }
                    this.imgView(obj)
                } else {
                    if (!file.raw) {
                        window.open(file.url);
                    }
                }
            },
            // 删除前(先返回false,再通过数据操作是否删除)
            beforeRemove(file, fileList) {
                this.$confirm('你确定要删除所选的图片吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then((action) => { // 确定的回调
                    // 判断是否为数据库数据(图片分为本地图片和已保存数据图片)
                    if (!file.raw) {
                        // 通知父组件记录下要删除的id
                        let rmId = file.url.substring(file.url.indexOf("=") + 1)
                        this.$emit('removeAttach', [rmId]);
                    }
                    // 删除对应的图片
                    for (let i = 0; i < fileList.length; i++) {
                        if (fileList[i].uid == file.uid) {
                            fileList.splice(i, 1)
                            break
                        }
                    }
                    // 更新父的值
                    this.setAttach(fileList)
                }).catch((err) => { //取消的回调

                });
                return false;
            },
            onChange(file, fileList) {
                this.setAttach(fileList)
            },
            //把文件设置给父
            setAttach(fileList) {
                var _file = []
                var _fileName = []
                for (var i in fileList) {
                    if (fileList[i].raw) {
                        _file.push(fileList[i].raw)
                        _fileName.push(fileList[i].raw.name)
                    }
                }
                if (_file.length === 0) _file = ''
                if (_fileName.length === 0) _fileName = ''
                var json = {}
                json[this.fieldName] = _file
                if (this.fieldTextName) {
                    json[this.fieldTextName] = _fileName
                }
                this.$emit('setAttach', json);  // 把附件设置进父组件
            }
        },
        watch: {
            resourceId: {
                immediate: true,
                handler: function (val) {
                    if (!val || this.isSingle) return  // 多选时触发
                    Vue.prototype.get(this.listURL, {
                        'resourceId': this.resourceId,
                        'fileType': this.fileType
                    }, (isSuccess, res) => {
                        if (res.Rows.length > 0) {
                            for (let i in res.Rows) {
                                let attach = res.Rows[i]
                                this.fileList.push({
                                    name: attach.attachName,
                                    url: this.downloadURL + attach.attachId,
                                    attachId: attach.attachId
                                })
                            }
                        }
                    }, true)
                }
            },
            attach: {
                immediate: true,
                handler: function (val) {
                    if (!val || typeof val !== 'string' || !this.isSingle || this.hasInit) return  // 单选时触发
                    this.hasInit = true
                    this.fileList.push({
                        name: this.attachName ? this.attachName : '',
                        url: this.downloadURL + this.attach
                    })
                }
            }
        }
    }
</script>
<style scoped>
    .form-check-attach {
        height: auto !important;
        margin-bottom: unset;
    }
</style>