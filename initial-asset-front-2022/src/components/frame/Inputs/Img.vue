<template>
  <div class="form-check form-check-img">
    <el-upload
      ref="upload"
      action=""
      :name="fieldName"
      :accept="accept"
      :file-list='fileListFun'
      :multiple='multiple'
      :show-file-list='showFileList'
      :drag='drag'
      :list-type='listType'
      :auto-upload="autoUpload"
      :limit="limit"
      :on-exceed="onExceed"

      :on-preview="handlePictureCardPreview"
      :before-remove="beforeRemove"
      :on-change="onChange"
      >
      <i class="el-icon-plus"></i>
      <div slot="tip" class="el-upload__tip"><slot name="el-upload__tip"></slot></div>
    </el-upload>

    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>
<script>
// 旧版本,未优化,现在用 Attach.vue
  import Vue from 'vue'
  import { Upload, Dialog } from 'element-ui'

  export default {
    name: 'l-img', 
    components: {
      [Upload.name]: Upload,
      [Dialog.name]: Dialog
    }, 
    props: {
      fieldName: {
        type: String
      },
      imgId: { // 可以放置附件id或者附件数组
        type: [String, Object, Array]
      },
      imgName: {
        type: [String, Object, Array]
      },
      resourceId : {
        type: String 
      },
      fileType: {
        type: String,
        default: 'img'
      },
      isSingle: {
        type: Boolean,
        default: false
      }
    },
    data () {
      return {
        imgDownloadURL: Vue.prototype.BASEURL + '/attach/action/attachsupport/downloadAttach.do?attachId=',
        imgListURL: Vue.prototype.BASEURL + '/attach/action/attachsupport/listJSON.do', 
        fileList: [],

        accept: "image/png,image/gif,image/jpg,image/jpeg", // 允许上传的类型
        drag: false,   // 是否可以拖拽上传
        listType: 'picture-card', // 列表展示类型  text/picture/picture-card
        autoUpload: false,  // 是否自动上传附件
        showFileList: true, // 是否展示文件列表
        multiple: this.isSingle ? false : true,   // 是否可以多选

        dialogImageUrl: '',
        dialogVisible: false
      }
    },
    computed: {
      fileListFun() {
        let _fileList = []
        if (this.imgId) {
          // 可能是字符串/数组
          if (typeof this.imgId === 'object' && this.imgId) { // 数组形式
            for (let i in this.imgId) {
              let imgObj;
              if (typeof this.imgId[i] === 'string') {
                imgObj = {url: this.imgDownloadURL + this.imgId[i]}
              } else if (typeof this.imgId[i] === 'object') {
                var _url = window.URL.createObjectURL(this.imgId[i])
                imgObj = {name : this.imgId[i].name, url: _url, raw: this.imgId[i]}
              }
              _fileList.push(imgObj)
            }
          } else if (typeof this.imgId === 'string') { // 字符串形式
            let imgObj = {name: this.imgName, url: this.imgDownloadURL + this.imgId}
            _fileList.push(imgObj)
          }
        }
        return _fileList
      },
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
      // 点击放大图片
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true
      },
      // 删除前(先返回false,再通过数据操作是否删除)
      beforeRemove(file, fileList) {
        this.$confirm('你确定要删除所选的图片吗？', '删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then((action) => { // 确定的回调
          // 判断是否为数据库数据(图片分为本地图片和已保存数据图片)
          if (file.url.indexOf(this.imgDownloadURL) != -1) {
            // 通知父组件记录下要删除的id
            let rmId = file.url.substring(file.url.indexOf("=") + 1)
            this.$emit('removeAttach', [rmId]);
          }
          // 删除对应的图片
          for (let i = 0; i < fileList.length; i++) {
            if (fileList[i].url == file.url) {
              fileList.splice(i, 1)
              break
            }
          }
        }).catch((err) => { //取消的回调

        });
        return false;
      },
      // 上传文件时
      onChange(file, fileList) {
        let _fileList = []
        for (let i in fileList) {
          if (typeof fileList[i].url === 'undefined') {
            _fileList.push(fileList[i])
            continue
          }
          if (fileList[i].url.indexOf(this.imgDownloadURL) == -1) {
            _fileList.push(fileList[i].raw)
          } else {
            _fileList.push(fileList[i].url.substring(fileList[i].url.indexOf("=") + 1))
          }
        }
        let json = {}
        json[this.fieldName] = _fileList 
        this.$emit('setAttach', json);  // 把附件设置进父组件
      }
    },
    watch: {
      resourceId: {
        immediate: true,
        handler: function (val, oldVal) {
          if (!val || this.isSingle) return
          Vue.prototype.get(this.imgListURL, {'resourceId': this.resourceId, 'fileType': this.fileType}, (isSuccess, res) => {
            if (res.Total > 0) {
              let json = {}
              let _fileList = []
              for (var i in res.Rows) {
                _fileList.push(res.Rows[i].attachId)
              }
              json[this.fieldName] = _fileList 
              this.$emit('setAttach', json);  // 把附件设置进父组件
            }
          }, true)
        }
      }
    }
  }
</script>
