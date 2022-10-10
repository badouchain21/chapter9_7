{
    /**
     * 树的配置
     */
    treeInfoCfg: function (parent) {
        if (parent) {
            return {
                url: `${this.BASEURL}/org/department/departmenttree/ptree.do`,
                params: {pid: parent.id}
            }
        }

        return {
            url: `${this.BASEURL}/org/department/departmenttree/tree.do`,
            params: {}
        }
    },
    listDataUrl: function() {
        return `${this.BASEURL}/org/employee/employeelist/listJSON.do`
    },
    defaultParam: {
        /**
         * 返回初始的 defaultSearchParam，
         * param：
         * {
         *     defaultNodeId：第一次加载列表时的树节点id
         * }
         */
        init: function (symbol, param) {
            return {departmentId: param.currentNodeId}
        },
        /**
         * 更新 defaultSearchParam
         * 在带树的列表页会用到该方法，点击树节点时，进行调用，传入树的结点数据
         * 会把返回的数据放到查询参数中
         * 该方法仅用于带有树的模型列表
         */
        getUpdateData: function (data){
            return {departmentId: data.id}
        }
    },
    // 按钮
    buttons: [
  		{
  			id: "freeze",
  			name: "冻结",
  			icon : "#bd-cuiban",
  			isHide: false,
  			click : function () {
  				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要冻结的员工!');
					return
				}
				this.$prompt('', '员工冻结意见', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					inputPlaceholder: '员工冻结必须填写冻结意见...',
					inputValidator: value => {
						if (value === null || value.trim() === '') {
							return '请输入冻结意见'
						}
					},
		        }).then(result => {
		        	let params = {
		        		opinion	:	result.value.trim(),
		        		ids		:	selection.map(e => e.id).join(',')
		        	};
		        	this.post(`${this.BASEURL}/org/employee/employeedelete/delete.do`, params).then(res => {
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('冻结成功', 'success')
						} else {
							this.alert('冻结失败', 'error')
						}
					})
		        })
  			}
  		},
  		{
  			id: "unfreeze",
  			name: "解冻",
  			icon : "#bd-clock-o",
  			isHide: false,
  			click : function () {
  				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要解冻的员工!');
					return
				}
				let needReset = false;
		     	for (let index in selection) {
					if (selection[index].flgActive == 1) continue;
					needReset = true;
		     	}
		     	if (!needReset) {
		     		this.alert('解冻成功！', 'success');
		     		return;
		     	}
				let params = {
	        		ids		:	selection.map(e => e.id).join(',')
	        	};
	        	this.post(`${this.BASEURL}/org/employee/employeesave/unFreeze.do`, params).then(res => {
					if (res.hasOk) {
						this.$refs.list.refresh();
						this.alert('解冻成功', 'success')
					} else {
						this.alert('解冻失败', 'error')
					}
				})
  			}
  		},
  		{
  			id: "transfer",
  			name: "调动",
  			icon : "#bd-redo-square",
  			isHide: false,
  			click: function () {
  				let selection = this.$refs.list.selection
				if (selection.length <= 0) {
					this.alert('请先选择需要调动的员工!')
					return
				}
  				let _this = this
        	    this.addressBook({
                    type: '2',
                    selectType: 0,
                    singleChoose: true
                }).then(data => {
                    let params = {
                        ids: selection.map(e => e.id).join(','),
                        target: data[0].value
                    }
                    this.post(`${this.BASEURL}/org/employee/employeesave/move.do`, params).then(res=>{
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('操作成功', 'success')
						} else {
							this.alert('操作失败', 'error')
						}
					})
                })
  			}
  		},
  		{
  			id: "resetErrorLogon",
  			name: "重置非法登录次数",
  			icon : "#bd-zhongzhi",
  			isHide: false,
  			click : function () {
  				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要重置的员工!');
					return
				}
				let needReset = false;
		     	for (let index in selection) {
					if (selection[index].flgPwdError == 0) continue;
					needReset = true;
		     	}
		     	if (!needReset) {
		     		this.alert('重置成功！', 'success');
		     		return;
		     	}
				let params = {
	        		ids		:	selection.map(e => e.id).join(',')
	        	};
	        	this.post(`${this.BASEURL}/org/employee/employeesave/resetFlgPwdError.do`, params).then(res => {
					if (res.hasOk) {
						this.$refs.list.refresh();
						this.alert('重置成功', 'success')
					} else {
						this.alert('重置失败', 'error')
					}
				})
  			}
  		},
  		{
  			id: "panelSetting",
  			name: "面板配置",
  			icon : "#bd-file-bar-o",
  			isHide: false,
  			click: function () {
				let selection = this.$refs.list.selection
				if (selection.length <= 0) {
					this.alert('请先选择要配置面板的用户!')
					return
				} else if (selection.length !== 1) {
					this.alert('一次只能配置一个用户的面板')
					return
				}
				let dialogId = 'layoutDialog'
				let _this = this;
				this.$dialog.init({
					type: 'list',
					id: dialogId,
					// 弹窗内容过多时使用
//  	                    isFixedDialog: true,
					title: '设置【' + selection[0].name + '】默认面板',
					tableHeight: '430px',
					isSelection: true,
					fieldList: [
						{name: 'name', label: '名称'},
						{name: 'code', label: '编码'},
						{name: 'isDefaultDesc', label: '当前是否启用'},
						{name: 'remark', label: '备注'}
					],
					// 列表数据请求地址
					url: `${this.BASEURL}/panel/layout/layoutlist/panelListJSON.do`,
					// 列表数据请求参数
					params: {
						type: '20',
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
  		},
  		{
  			id: 'delete',
  			isHide: true
  		}
  	]
}