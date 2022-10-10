{
	// 前端写法
	buttons: [{
			id: "add",
			isHide: true
		},
		{
			id: "edit",
			isHide: true
		},
		{
			id: "transfor",
			name: "转办",
			icon : "#bd-exchange",
			isHide: false,
			click: function() {
        		let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要转办的记录!', 'warning');
					return
				}
				let length = selection.length
				for (let i = 0; i < length; i++) {
					if(selection[i].status == 2 || selection[i].status == 12){
						this.alert('您选择的记录行中包含已办（已阅）记录，已办（已阅）不能进行转办操作!', 'warning')
						return
					}
				}
				let _this = this
        	    this.addressBook({
                    type: '2',
                    selectType: 20
                }).then(data => {
    				this.$confirm('您确定将选定的待办转办至[' + data.map(d => d.name).join(',') + ']吗?' , '转办', {
    		            confirmButtonText: '确定',
    		            cancelButtonText: '取消',
    		            type: 'warning'
    		        }).then(() => {
    		        	let params = {
    		        			ids: selection.map(e => e.id).join(','),
    		        			target: data.map(d => d.value).join(',')
    		        	}
    		        	this.post(`${this.BASEURL}/workmaintain/workmaintainsave/transfer.do`, params).then(res=>{
							if (res.hasOk) {
								this.$refs.list.refresh();
								this.alert('操作成功', 'success')
							} else {
								this.alert('操作失败', 'error')
							}
						})
    		        }).catch(() => {})
                })
			}
		},
		{
			id: "transforToDone",
			name: "转为已处理",
			icon : "#bd-yiwancheng",
			isHide: false,
			click : function () {
				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要操作的记录行!');
					return
				}
				for (let i = 0; i < selection.length; i++) {
					if (selection[i].status == 2 || selection[i].status == 12) {
						this.alert("您选择的记录行中包含已办（已阅）记录，已办（已阅）不需要再进行转为已处理操作!")
						return;
					}
				}
				this.$confirm('您确定将选定的记录行转为已处理吗?' , '转为已处理', {
		            confirmButtonText: '确定',
		            cancelButtonText: '取消',
		            type: 'warning'
		        }).then(() => {
		        	let params = {
		        		status	:	2,
		        		ids		:	selection.map(e => e.id).join(',')
		        	};
		        	this.post(`${this.BASEURL}/workmaintain/workmaintainsave/trunk.do`, params).then(res=>{
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('操作成功', 'success')
						} else {
							this.alert('操作失败', 'error')
						}
					})
		        }).catch(() => {})
			}
		},
		{
			id: "transforToUndo",
			name: "转为未处理",
			icon : "#bd-banlishibai",
			isHide: false,
			click : function () {
				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要操作的记录行!');
					return
				}
				for (let i = 0; i < selection.length; i++) {
					if (selection[i].status == 1 || selection[i].status == 11) {
						this.alert("您选择的记录行中包含待办（待阅）记录，待办（待阅）不需要再进行转为未处理操作!");
						return;
					}
				}
				this.$confirm('您确定将选定的记录行转为未处理吗?' , '转为未处理', {
		            confirmButtonText: '确定',
		            cancelButtonText: '取消',
		            type: 'warning'
		        }).then(() => {
		        	let params = {
		        		status	:	1,
		        		ids		:	selection.map(e => e.id).join(',')
		        	};
		        	this.post(`${this.BASEURL}/workmaintain/workmaintainsave/trunk.do`, params).then(res=>{
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('操作成功', 'success')
						} else {
							this.alert('操作失败', 'error')
						}
					})
		        }).catch(() => {})
			}
		},
		{
			id: "delete",
			name: "删除",
			icon : "#bd-trash",
			isHide: false,
			click : function () {
				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要删除的记录行（流程文档实例）!');
					return
				}
				this.$confirm('您确定删除选定的记录行（流程文档实例）吗?' , '删除', {
		            confirmButtonText: '确定',
		            cancelButtonText: '取消',
		            type: 'warning'
		        }).then(() => {
		        	let params = {
		        		mdCode	:	this.module.code,
		        		ids		:	selection.map(e => e.id).join(',')
		        	};
		        	this.post(`${this.BASEURL}/jdbc/common/basecommondelete/delete.do`, params).then(res=>{
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('删除成功', 'success')
						} else {
							this.alert('删除失败', 'error')
						}
					})
		        }).catch(() => {})
			}
		},
		{
			id: "refresh",
			name: "刷新",
			icon : "#bd-sync",
			isHide: false,
			click : function () {
				this.$refs.list.refresh();
			}
		}
	]
}