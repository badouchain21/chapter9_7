{
	// 前端写法
	buttons: [
		{
			id: "delete",
			name: "删除",
			icon : "#bd-trash-alt-o",
			isHide: true
		},
		{
			id: "view",
			name: '查看',
			icon: '#bd-guankan',
			bgColor:'#23CCEF',
			linkBgColor:'#23CCEF',
			activeBgColor:'#11bfe3',
			click: function () {
				let selection = this.$refs.list.selection
				if (selection.length === 0) {
					this.alert('请选择一行！')
					return
				}
				let listPath = this.$parent.$route.path
				this.$router.push({
					path: `/module/view/view/${this.module.code}/${selection[selection.length - 1].id}`,
					query: {
						data: {
							returnPath: listPath
						}
					}
				})
			}
		},
		{
			id: "start",
			name: "启动",
			icon : "#bd-caret-right",
			isHide: false,
			click : function () {
				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请至少选中一行！');
					return
				}
				this.$confirm('您确定启动选定的记录行吗?' , '启动', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					let params = {
						ids		:	selection.map(e => e.id).join(',')
					};
					this.post(`${this.BASEURL}/quartz/qwtemplatesave/batchStart.do`, params).then(res => {
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('启动成功', 'success')
						} else {
							this.alert(res.message, 'error')
						}
					})
				})
			}
		},
		{
			id: "stop",
			name: "停止",
			icon : "#bd-times-circle-o",
			isHide: false,
			click : function () {
				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请至少选中一行！');
					return
				}
				this.$confirm('您确定停止选定的记录行吗?' , '停止', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					let params = {
						ids		:	selection.map(e => e.id).join(',')
					};
					this.post(`${this.BASEURL}/quartz/qwtemplatesave/batchStop.do`, params).then(res => {
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('停止成功', 'success')
						} else {
							this.alert('停止失败', 'error')
						}
					})
				})
			}
		},
		{
			id: "execute",
			name: "立即执行",
			icon : "#bd-exchange",
			isHide: false,
			click : function () {
				let selection = this.$refs.list.selection;
				if (selection.length == 1) {
					this.$confirm('您确定立即执行选定的记录行吗?' , '执行', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						let params = {
							id		:	selection[0].id
						};
						this.post(`${this.BASEURL}/quartz/qwtemplatesave/startQwJobIgnoredTime.do`, params).then(res => {
							if (res.hasOk) {
								this.$refs.list.refresh();
								this.alert('执行成功', 'success')
							} else {
								this.alert('执行失败', 'error')
							}
						})
					})
				} else {
					this.alert('请选中一行！');
					return
				}
			}
		}
	]
}