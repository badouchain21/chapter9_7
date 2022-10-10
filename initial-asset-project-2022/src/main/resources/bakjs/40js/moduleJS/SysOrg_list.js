{
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
					this.alert('请先选择需要冻结的部门!');
					return
				}
				this.$prompt('', '您确定要冻结选定的部门吗?', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					inputPlaceholder: '部门冻结必须填写冻结意见...',
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
		        	this.post(`${this.BASEURL}/org/department/departmentdelete/delete.do`, params, (isSuccess, res) => {
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
  			id: "panelSetting",
  			name: "设置默认面板",
  			icon : "fa fa-pencil-square-o",
  			isHide: false,
  			click: function () {
  				let selection = this.$refs.list.selection
				if (selection.length <= 0) {
					this.alert('请先选择要配置面板的部门!')
					return
				} else if (selection.length !== 1) {
					this.alert('一次只能配置一个部门的面板')
					return
				}
				this.CustomBusi.PanelChooseList({
					type: '0',
					id: selection[0].id
				}).then(data => {
                    let params = {
                    	panelId: data[0].id,
                    	groupId: selection[0].id,
                    	type: '0'
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