

// ！！！！！！
// 该组件暂不使用，需要使用上传图片的时候可以使用File组件，file组件支持文件与图片
// ！！！！！！










<template>
    <div class="">
        <!-- <el-upload
            ref="imgUpload"
            action=""
            list-type="picture-card"
            multiple
            :limit="3"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-change="handleChange"
            :file-list="formItem.value"
            :auto-upload="false">
            <i class="el-icon-plus"></i>
        </el-upload>
        <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog> -->
    </div>
</template>
<script>
    export default {
        components: {},
        data () { // 定义页面变量
            return {
                dialogImageUrl: '',
                dialogVisible: false
            }
        },
        props: {
            formItem: {
                type: Object,
                default: () => {}
            }
        },
        computed: {},
        methods: { // 定义函数
            handleRemove (file, fileList) {
                console.log(file, fileList)
            },
            handlePictureCardPreview (file) {
                this.dialogImageUrl = file.url
                this.dialogVisible = true
            },
            handleChange (file, fileList) {
                let uploadRef = this.$refs.imgUpload
                this.formItem.value = fileList
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {
        }
    }
</script>
<style lang='scss' scoped>

</style>