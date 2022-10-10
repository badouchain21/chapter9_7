{
	// 前端写法
	buttons: [
	    {
			id: "add",
			isHide: true
		},
		{
			id: "edit",
			isHide: true
		},
		{
			id: "monitor",
			name: "监控",
			icon : "#bd-guankan",
			isHide: false,
			click: function() {
  				let selection = this.$refs.list.selection
				if (selection.length <= 0) {
					this.alert('请先选择需要监控的实例行!')
					return
				} else if (selection.length !== 1) {
					this.alert('请选中一条!')
					return
				}
				this.CustomBusi.FlowMonitor({
					fiId: selection[0].id,
					boId: selection[0].boId,
					flow: selection[0]
				})
			}
		},
		{
			id: "reset",
			name: "重置未归档",
			icon : "#bd-zhongzhi",
			isHide: false,
			click: function () {
				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要恢复的记录行（流程文档实例）!');
					return
				}
				let titles = "";
				for (let i = 0; i < selection.length; i++) {
					if (selection[i].status == 0 || selection[i].status == 1 || selection[i].status == 3) {
						this.alert("您选择的记录行中包含未归档数据，未归档数据不需要[重置未归档]操作!");
						return;
					}
					if (titles.length > 0 ) titles += ",";  
					titles += selection[i].title;
				}
				this.$confirm('您确定重置选定的记录行（流程文档实例）吗? \n' +titles , '重置', {
		            confirmButtonText: '确定',
		            cancelButtonText: '取消',
		            type: 'warning'
		        }).then(() => {
		        	let params = {
		        		ids	: selection.map(e => e.id).join(',')
		        	};
		        	this.post(`${this.BASEURL}/instance/flow/flowinstancemonitor/resume.do`, params).then(res=>{
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('重置成功', 'success')
						} else {
							this.alert('重置失败', 'error')
						}
					})
		        }).catch(() => {})
			}
		},
		{
			id: "pigeonhole",
			name: "归档",
			icon : "#bd-redo-square",
			isHide: false,
			click: function () {
				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要归档的记录行（流程文档实例）!');
					return
				}
				let titles = "";
				for (let i = 0; i < selection.length; i++) {
					if (selection[i].status == 2) {
						this.alert("您选择的记录行中包含已归档数据，已归档数据不需要[归档]操作!");
						return;
					}
					if (titles.length > 0 ) titles += ",";  
					titles += selection[i].title;
				}
				this.$confirm('您确定归档选定的记录行（流程文档实例）吗? \n' +titles , '归档', {
		            confirmButtonText: '确定',
		            cancelButtonText: '取消',
		            type: 'warning'
		        }).then(() => {
		        	let params = {
		        		ids	: selection.map(e => e.id).join(',')
		        	};
		        	this.post(`${this.BASEURL}/instance/flow/flowinstancemonitor/archive.do`, params).then(res=>{
						if (res.hasOk) {
							this.$refs.list.refresh();
							this.alert('归档成功', 'success')
						} else {
							this.alert('归档失败', 'error')
						}
					})
		        }).catch(() => {})
			}
		},
		{
			id: "delete",
			name: "删除",
			icon : "#bd-trash-alt-o",
			isHide: false,
			click: function () {
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
	]
}