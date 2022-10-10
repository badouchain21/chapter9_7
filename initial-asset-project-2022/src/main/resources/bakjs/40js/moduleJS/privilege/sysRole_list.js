{
	/**
     * 树的配置
     */
    treeInfoCfg: function (parent) {
    	if (parent) {
            return {
                url: `${this.BASEURL}/auth/role/roletree/ptree.do`,
                params: {pid: parent.id}
            }
        }
        return {
            url: `${this.BASEURL}/auth/role/roletree/getTreeNodeByUser.do`,
            params: {}
        }
    },
	buttons: [
		{
			id: "resourceDistribute",
			name: "资源分配",
            icon : "#bdb-fenpei",
			type : "success" ,
			isHide: false,
			click: function () {
  				let selection = this.$refs.list.selection
				if (selection.length !== 1) {
					this.alert('请选择一条！', 'warning')
					return
				}
  				this.$router.push({
  					path: `/system/rolepermission/sysRole/placeholder`,
  					query: {
						roleId: selection[0].id
  					}
  				})
			}
		},
		{
			id: "userDistribute",
			name: "人员分配",
			type : "success" ,
			icon : "#bdb-fenpei",
			isHide: false,
			click: function () {
				this.$router.push({
					path: `/module/tree/list/userRole/placeholder`
				})
			}
		},
		{
			id: "panelDistribute",
			name: "面板分配",
			type: 'success',
			icon : "#bdb-fenpei",
			isHide: false,
			click: function () {
  				let selection = this.$refs.list.selection
				if (selection.length <= 0) {
					this.alert('请先选择要配置面板的角色!')
					return
				} else if (selection.length !== 1) {
					this.alert('一次只能配置一个角色的面板')
					return
				}
  				let dialogId = 'layoutDialog'
  				let _this = this ;
  				this.$dialog.init({
                    type: 'list',
                    id: dialogId,
                    // 弹窗内容过多时使用
//                    isFixedDialog: true,
                    title: '设置【' + selection[0].name + '】默认面板',
                    tableHeight: '430px',
                    isSelection: true,
                    fieldList: [
                        { name: 'name', label: '名称' },
                        { name: 'code', label: '编码' },
                        { name: 'isDefult', label: '当前是否启用' },
                        { name: 'remark', label: '备注' }
                    ],
                    // 列表数据请求地址
                    url: `${this.BASEURL}/panel/layout/layoutlist/panelListJSON.do`,
                    // 列表数据请求参数
                    params: {
                    	type: '10',
                    	id: selection[0].id
                    },
                    // 是否一次请求所有数据
                    isLoadAll: true,
                    // 是否展示分页
                    showPagination: false,
                    btnMethods: [
                      { 
                          name: '保存', icon: '#bd-save-o', click: function () {
                              let listObj = this.$refs[dialogId].$refs[dialogId].$refs['mList'].selection
                              this.params.panelId = listObj[0].id
                              this.params.groupId = this.params.id
                              this.post(`${this.BASEURL}/panel/grouppanel/grouppanelsave/setDefultPanel.do`, this.params).then(res => {
                              	if (res.hasOk) {
                              		// 关闭弹窗表单
                                    this.$dialog.close();
                                    _this.$refs.list.refresh();
                                    _this.alert('操作成功', 'success')
                                  } else {
                                	_this.alert('操作失败', 'error')
                                  }
                              })
                          } 
                      }, {
                          name: '取消', icon: '#bd-tuichu1', click: function () {
                              this.$confirm('取消将不保存表单数据, 是否继续?', '提示', {          
                                  confirmButtonText: '确定',          
                                  cancelButtonText: '取消',          
                                  type: 'warning'
                              }).then(() => {          
                                  // 关闭弹窗表单
                                  this.$dialog.close()
                              }).catch(() => {          
                                  // 取消关闭        
                              })
                          }
                      }
                  ]
                })
			}
		}
	]
}