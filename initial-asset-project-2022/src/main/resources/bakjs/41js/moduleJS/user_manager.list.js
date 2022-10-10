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
  			icon : "fa fa-minus-square-o",
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
		        	this.post(`${this.BASEURL}/org/employee/employeedelete/delete.do`, params, (isSuccess, res) => {
	        	  		if (res.hasOk) {
	        	  			this.$refs.list.refresh();
	        	  			this.alert('冻结成功', 'success')
	        	  		} else {
	        	  			this.alert('冻结失败', 'error')
	        	  		}
	        	  	}, true)
		        })
  			}
  		},
  		{
  			id: "unfreeze",
  			name: "解冻",
  			icon : "fa fa-pencil-square-o",
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
	        	this.post(`${this.BASEURL}/org/employee/employeesave/unFreeze.do`, params, (isSuccess, res) => {
        	  		if (res.hasOk) {
        	  			this.$refs.list.refresh();
        	  			this.alert('解冻成功', 'success')
        	  		} else {
        	  			this.alert('解冻失败', 'error')
        	  		}
        	  	}, true)
  			}
  		},
  		{
  			id: "transfer",
  			name: "调动",
  			icon : "fa fa-pencil-square-o",
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
                    this.post(`${this.BASEURL}/org/employee/employeesave/move.do`, params, (isSuccess, res) => {
                        if (res.hasOk) {
                            this.$refs.list.refresh();
                            this.alert('操作成功', 'success')
                        } else {
                            this.alert('操作失败', 'error')
                        }
                    }, true)
                })
  			}
  		},
  		{
  			id: "resetErrorLogon",
  			name: "重置非法登录次数",
  			icon : "fa fa-pencil-square-o",
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
	        	this.post(`${this.BASEURL}/org/employee/employeesave/resetFlgPwdError.do`, params, (isSuccess, res) => {
        	  		if (res.hasOk) {
        	  			this.$refs.list.refresh();
        	  			this.alert('重置成功', 'success')
        	  		} else {
        	  			this.alert('重置失败', 'error')
        	  		}
        	  	}, true)
  			}
  		},
  		{
  			id: "panelSetting",
  			name: "面板配置",
  			icon : "fa fa-pencil-square-o",
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
				this.CustomBusi.PanelChooseList({
					type: '20',
					id: selection[0].id
				}).then(data => {
                    let params = {
                    	panelId: data[0].id,
                    	groupId: selection[0].id,
                    	type: '20'
                    }
                    this.post(`${this.BASEURL}/panel/grouppanel/grouppanelsave/setDefultPanel.do`, params, (isSuccess, res) => {
                        if (res.hasOk) {
                            this.$refs.list.refresh();
                            this.alert('操作成功', 'success')
                        } else {
                            this.alert('操作失败', 'error')
                        }
                    }, true)
				})
  			}
  		},
  		{
  			id: 'delete',
  			isHide: true
  		}
  	]
}