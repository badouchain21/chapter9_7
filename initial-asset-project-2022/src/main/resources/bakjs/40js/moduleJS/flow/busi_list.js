{
	// 前端写法
	buttons: [
		{
		    id: 'edit',
		    name: '修改',
		    icon: 'icon iconfont bd-edit',
		    click: function () {
		        let selection = this.$refs.list.selection
		        if (selection.length === 0) {
		            this.alert('请选择一行！')
		            return
		        }
		        let listPath = this.$parent.$route.path
		        this.$router.push({
		            path: `/module/stander/edit/${this.module.code}/${selection[selection.length - 1].idd}`,
		            query: {
		                data: {
		                    returnPath: listPath
		                }
		            }
		        })
		    }
		},
		{
			id: "delete",
			name: "删除",
			icon : "icon iconfont bd-trash-alt-o",
			isHide: false,
			click : function () {
				let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要删除的记录行（流程文档实例）!');
					return
				}
				this.$confirm('您确定删除选定的记录行吗?' , '删除', {
		            confirmButtonText: '确定',
		            cancelButtonText: '取消',
		            type: 'warning'
		        }).then(() => {
		        	let params = {
		        		mdCode	:	this.module.code,
		        		ids		:	selection.map(e => e.idd).join(',')
		        	};
		        	this.post(`${this.BASEURL}/jdbc/common/basecommondelete/delete.do`, params, (isSuccess, res) => {
	        	  		if (res.hasOk) {
	        	  			this.$refs.list.refresh();
	        	  			this.alert('删除成功', 'success')
	        	  		} else {
	        	  			this.alert('删除失败', 'error')
	        	  		}
	        	  	}, true)
		        }).catch(() => {})
			}
		}
	]
}