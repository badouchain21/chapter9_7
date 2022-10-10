<template>
    <div class="view-div">
        <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="imgURL" alt="">
            <span @click="dialogVisible = false" class="close"><i class="icon iconfont bd-times"></i></span>
        </el-dialog>
    </div>
</template>

<script>
    import {Dialog} from 'element-ui'
    export default {
        name: "img-view",
        components: {
            [Dialog.name]: Dialog
        },
        data() {
            return {
                dialogVisible: true, // 控制弹窗是否显示
                attachId: '',   // 图片 attach 记录的id，由外部传入
                url: '',        // 图片 url 由外部传入
                imgURL: ''      // 显示图片的url
            }
        },
        created() {
            if (this.url) {
                // 有 url，直接显示url
                this.imgURL = this.url
            } else if (this.attachId) {
                // 存在 attachId 去后台获取大图
                this.get(this.INTERFACE.bigImgInfo, {attachId: this.attachId}).then(res => {
                    let id = this.attachId
                    if (res.hasOk && res.bean) {
                        id = res.bean.attachId
                    }
                    this.imgURL = this.INTERFACE.downloadAttach + '?attachId=' + id
                })
            }
        }
    }
</script>

<style scoped>
    .view-div >>> .el-dialog__header {
        display: none;
    }

    .view-div >>> .el-dialog__body {
        padding: 0;
    }

    .view-div .close {
        position: absolute;
        top: 0;
        right: -80px;
        font-size: 30px;
        color: #ffffff;
        cursor: pointer;
    }
</style>
