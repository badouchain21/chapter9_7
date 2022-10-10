<template>
  <div>
    <div class="filter">
      <p>我的筛选:</p>
      <p
        v-for="(item, index) in filters"
        :key="index"
        class="fiterName">

        <span
          class="filter-text filter-text-unchecked"
          :class="{'filter-text-unchecked': currentFilter.id != item.id, 'filter-text-checked': currentFilter.id == item.id}"
          @click="filterChick(item)">

          {{item.name}} <span v-if="typeof countArr[index] != 'undefined'">({{countArr[index]}})</span>
        </span>
        <i class="fa fa-share" @click="openShareDialog(item)"></i>
        <i class="el-icon-error" @click="filterDelete(item, index)"></i>
      </p>
      <p class="alert-btn" @click="openSaveDialog">保存过滤器</p>
    </div>

    <!-- 分享过滤器 -->
    <el-dialog title="分享过滤器" :visible.sync="share_dialog_Visible" width="38%">
      <div class="dialog-title">
        <span class="">请选择分享的过滤器</span>
        <!--<span class="dialog-span">全部过滤器</span>-->
      </div>
      <div class="dialog-content">
        <div v-for="item in shareFilterData" :key="item.id"
             class="dialog-item"
             :class="{selected: shareFilterId[item.id]}"
             @click="selectShareFilter(item)">
          <i class="fa fa-filter"></i>
          {{item.name}}
        </div>
      </div>
      <div class="dialog-title">
        <span class="">请选择分享的用户</span>
        <span style="cursor: pointer;" class="dialog-span" @click="selectAllUser()">{{selectAllUserLabel}}</span>
        <!--<span class="dialog-span">全部用户</span>-->
      </div>
      <div class="dialog-content">
        <div class="dialog-user" v-for="item in userList" :key="item.id" @click="selectShareUser(item)">
          <img src="static/img/choose.png" v-if="shareUserId[item.id]">
          <i class="fa fa-user"></i>
          {{item.name}}
        </div>
      </div>
      <span slot="footer" class="dialog-footer foot-border">
          <el-button @click="share_dialog_Visible = false">取 消</el-button>
          <el-button type="primary" @click="shareFilter">分 享</el-button>
        </span>
    </el-dialog>

    <!-- 过滤器操作选择 -->
    <el-dialog title="保存过滤器" :visible.sync="filter_dialog_Visible" width="30%">
      <div class="dialog-text">
        <span>您是需要更新当前过滤器，还是新建过滤器？</span>
      </div>
      <span slot="footer" class="dialog-footer foot-border">
          <el-button type="primary" @click="updateFilter">更新</el-button>
          <el-button type="primary" @click="filter_dialog_Visible = false;addFilter()">新建</el-button>
          <el-button @click="filter_dialog_Visible = false">取消</el-button>
        </span>
    </el-dialog>

  </div>
</template>
<script>
  import {Dialog} from 'element-ui'

  export default {
    name: 's-filter',
    components: {
      [Dialog.name]: Dialog,
    },
    props: {
      mdCode: ''
    },
    computed: {
    },
    data() {
      return {
        filters: [],
        currentFilter: {},
        countArr: [],
        shareFilterData: [],
        shareFilterId: {},
        userList: [],
        shareUserId: {},

        valueProject: [], //过滤器删减控制数组
        share_dialog_Visible: false, //分享过滤器对话框显示控制
        filter_dialog_Visible: false, //过滤器添加弹框显示控制
        selectAllUserLabel: '全选', //全选按钮文本控制
      }
    },
    methods: {
      filterChick (filterInfo) {
        if (this.currentFilter.id !== filterInfo.id) {
          this.currentFilter = filterInfo
          this.$emit('click', filterInfo)
        } else {
          this.currentFilter = {}
          this.$emit('cancel')
        }
      },
      filterDelete (filterInfo, index) {
        this.$confirm(`确定要删除过滤器 [${filterInfo.name}] 吗？`, '删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            let url = `${this.BASEURL}/filter/filter/mdfilterdelete/delete.do`
            this.post(url, {ids: filterInfo.id}).then(res => {
                if (res.hasOk) {
                    this.filters.splice(index, 1)
                    this.alert(`已删除过滤器：${filterInfo.name}`, 'success')
                    // 如果删除的是正在使用的过滤器，抛出 cancel 事件
                    if (filterInfo.id === this.currentFilter.id) {
                        this.$emit('cancel')
                    }
                } else {
                    let tip = res.message || ''
                    this.alert(`操作失败 ${tip}`, 'error')
                }
            })
        })
      },

      openSaveDialog: function () { //打开过滤器添加弹框
        if (this.currentFilter.id) {
          this.filter_dialog_Visible = true
        } else {
          this.addFilter()
        }
      },
      addFilter: function () { //添加过滤器弹框
        this.$prompt('', '保存过滤器', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '请输入过滤器名称...',
          inputValidator: value => {
            if (value === null || value.trim() === '') {
              return '请输入过滤器名称'
            }
          },
        }).then(result => {
          this.filterSave({
            name: result.value.trim(),
            mdCode: this.mdCode
          }, newFilter => {
            this.filters.push(newFilter)
          })
        })
      },
      updateFilter () {
        this.filterSave({id: this.currentFilter.id}, newFilter => {
          this.currentFilter.content = newFilter.content
          this.filter_dialog_Visible = false
        })
      },
      filterSave(filterInfo, callback) {
        let searchParam = this.$parent.getSelectorValue()
        filterInfo.content = JSON.stringify(searchParam)
        let url = `${this.BASEURL}/filter/filter/mdfiltersave/saveFilter.do`
        this.post(url, filterInfo).then(res => {
            if (res.hasOk) {
                this.alert('操作成功', 'success')
                callback(res.bean)
            } else {
                let tip = res.message || ''
                this.alert(`操作失败 ${tip}`, 'error')
            }
        })
      },
      selectShareFilter(filter) {  //选择分享过滤器
        let selected = this.shareFilterId[filter.id]
        this.$set(this.shareFilterId, filter.id, !selected)
      },
      openShareDialog (clickFilter) {
        // 重置分享过滤器数据的数组
        this.shareFilterData = []
        this.shareFilterData.push(clickFilter)
        let otherFilter = this.filters.filter(f => f.id !== clickFilter.id)
        this.shareFilterData.push(...otherFilter)
        // 重置分享过滤器的过滤器选择数组
        this.$set(this.shareFilterId, clickFilter.id, true)
        // 重置分享过滤器的用户选择数组
        this.shareUserId = []
        // 如果没有用户，调用接口加载
        if (this.userList.length === 0) {
            let url = `${this.BASEURL}/org/employee/employeelist/listJSONAll.do`
            this.get(url, {}).then(res => {
                let currentUser = this.$store.getters['user/info']
                this.userList = res.Rows.filter(u => u.id != currentUser.id)
            })
        }
        // 显示分享弹窗
        this.share_dialog_Visible = true
      },
      selectShareUser(user) { //选择分享用户
        let selected = this.shareUserId[user.id]
        this.$set(this.shareUserId, user.id, !selected)
      },
      /**
       * 分享过滤器
       */
      shareFilter() {
        let filterIds = Object.keys(this.shareFilterId).filter(key => this.shareFilterId[key])
        if (filterIds.length === 0) {
          this.alert('点击过滤器选择需要分享的过滤器')
          return
        }

        let userIds = Object.keys(this.shareUserId).filter(key => this.shareUserId[key])
        if (userIds.length === 0) {
          this.alert('点击用户选择需要分享的用户')
          return
        }

        let url = `${this.BASEURL}/filter/filter/mdfiltersave/batchSave.do`
        this.post(url, {filterIds: filterIds.join(','), userIds: userIds.join(',')}).then(res => {
            if (res.hasOk) {
                this.alert('分享成功', 'success')
            } else {
                let tip = res.message || ''
                this.alert(`操作失败 ${tip}`, 'error')
            }
            this.share_dialog_Visible = false
        })
      },
      //分享用户全选
      selectAllUser: function () {
        if (this.selectAllUserLabel === "全选") {
          this.selectAllUserLabel = "取消全选"
          this.userList.forEach(user => this.$set(this.shareUserId, user.id, true))
        } else {
          this.selectAllUserLabel = "全选"
          Object.keys(this.shareUserId).forEach(key => this.$set(this.shareUserId, key, false))
        }
      },
    },
    watch: {
      mdCode () {
        if (!this.mdCode) {
          return
        }

        let url = `${this.BASEURL}/filter/filter/mdfilterlist/loadFilterJson.do`
        this.get(url, {mdCode: this.mdCode}).then(res => {
            this.filters = res.Rows
            this.filters.forEach((f, index) => {
                let countUrl = `${this.BASEURL}/jdbc/common/basecommonlist/filterCount.do`
                this.post(countUrl, {mdCode: this.mdCode, searchParam: f.content}).then(res => {
                    this.$set(this.countArr, index, res.count)
                })
            })
        })
      }
    }
  }
</script>
<style>
  .filter-text {
    cursor: pointer;
  }
  .filter-text-unchecked {
    color: #2684ce
  }
  .filter-text-checked {
    color: #000000
  }
</style>
