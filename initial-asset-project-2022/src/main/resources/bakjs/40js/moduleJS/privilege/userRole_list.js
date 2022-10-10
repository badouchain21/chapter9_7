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
    listDataUrl: function() {
        return `${this.BASEURL}/auth/assign/roleuserassignlist/listJSON.do`
    },
    defaultParam: {
        /**
         * 返回初始的 defaultSearchParam
         */
        init: function (symbol,node) {
        	if(node.currentNodeId){
        		return {roleId: node.currentNodeId, searchParam: "[]"}
        	}else{
        		return {roleId: this.currentNodeId, searchParam: "[]"}
        	}
        },
        /**
         * 更新 defaultSearchParam
         * 在带树的列表页会用到该方法，点击树节点时，进行调用，传入树的结点数据
         * 会把返回的数据放到查询参数中
         * 该方法仅用于带有树的模型列表
         */
        getUpdateData: function (data){
            return {roleId: data.id, searchParam: "[]"}
        }
    },
    // 按钮
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
        	id: "delete", 
        	isHide: true
        },
        {
        	id: "view",
        	isHide: true
        },
        {
        	id: "distributeUser",
        	name: "分配新用户",
        	type : "success",
        	icon : "#bdb-fenpei",
        	isHide: false,
            click: function () {
        	    let _this = this
        	    this.addressBook({
                    type: '2',
                    selectType: 20
                }).then(data => {
                    let roleData = _this.$parent.getCurrentNodeData()
                    let userInfos = data.map(d => d.value).join(',')
                    let params = {
                        roleId: roleData.id,
                        userInfos: userInfos
                    }
                    this.post(`${this.BASEURL}/auth/assign/roleuserassignsave/save.do`, params).then(res =>{
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
        	id: "deleteUser",
        	name: "删除用户",
        	type : "danger" ,
        	icon : "#bd-trash-alt-o",
        	isHide: false,
        	click : function () {
        		let selection = this.$refs.list.selection;
				if (selection.length <= 0) {
					this.alert('请先选择需要删除的员工!');
					return
				}
				this.$confirm('删除用户后该用户将不再具有该角色的权限操作，请慎重！您确定要删除选定的用户吗?' , '删除', {
		            confirmButtonText: '确定',
		            cancelButtonText: '取消',
		            type: 'warning'
		        }).then(() => {
		        	let params = {
		        		ids		:	selection.map(e => e.id).join(',')
		        	};
		        	this.post(`${this.BASEURL}/auth/assign/roleuserassigndelete/delete.do`, params).then(res =>{
		        		if (res.hasOk) {
	        	  			this.$refs.list.refresh();
	        	  			this.alert('删除成功', 'success')
	        	  		} else {
	        	  			this.alert('删除失败', 'error')
	        	  		}
		        	})
		        })
        	}
        }
    ]
}