{
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
		        	this.post(`${this.BASEURL}/org/department/departmentdelete/delete.do`, params).then(res =>{
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
  			id: "panelSetting",
  			name: "设置默认面板",
  			icon : "#bd-file-bar-o",
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
				let dialogId = 'layoutDialog'
				let _this = this ;
				this.$dialog.init({
					type: 'list',
					id: dialogId,
					// 弹窗内容过多时使用
//  	                    isFixedDialog: true,
					title: '设置【' + selection[0].name + '】默认面板',
					tableHeight: '430px',
					isSelection: true,
					fieldList: [
						{ name: 'name', label: '名称' },
						{ name: 'code', label: '编码' },
						{ name: 'isDefaultDesc', label: '当前是否启用' },
						{ name: 'remark', label: '备注' }
					],
					// 列表数据请求地址
					url: `${this.BASEURL}/panel/layout/layoutlist/panelListJSON.do`,
					// 列表数据请求参数
					params: {
						type: '0',
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