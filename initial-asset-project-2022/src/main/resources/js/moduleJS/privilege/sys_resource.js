{
	treeInfoCfg: function (parent) {
    	if (parent) {
            return {
                url: `${this.BASEURL}/system/security/resourcetree/ptreeByUser.do`,
                params: {pid: parent.id}
            }
        }
        return {
            url: `${this.BASEURL}/system/security/resourcetree/getTreeNode.do`,
            params: {}
        }
    },
    defaultParam: {
        /**
         * 返回初始的 defaultSearchParam
         */
        init: function (symbol,node) {
        	if(node.currentNodeId){
        		return {pid: node.currentNodeId}
        	}else{
        		return {pid: ''}
        	}
        },
        /**
         * 更新 defaultSearchParam
         * 在带树的列表页会用到该方法，点击树节点时，进行调用，传入树的结点数据
         * 会把返回的数据放到查询参数中
         * 该方法仅用于带有树的模型列表
         */
        getUpdateData: function (data){
            return {pid: data.id}
        }
    },
    listDataUrl: function() {
        return `${this.BASEURL}/system/security/resourcetree/listJSON.do`
    },
	buttons: [
		{
            id: 'delete',
            name: '删除',
            icon: '#bd-trash-alt-o',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('请选择一行数据')
                    return
                }
                if (selection.length > 1) {
                    this.alert('一次只能删除一条数据')
                    return
                }

                this.$confirm('确定删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = {
                        mdCode: this.module.code,
                        id: selection.map(e => e.id).join(',')
                    }
                    this.post(`${this.BASEURL}/project/resource/roleresource/delete.do`, params).then(res => {
                    	if (res.hasOk) {
                            this.$refs.list.refresh()
                            this.alert('删除成功', 'success')
                            this.$emit('deleteAfter', selection)
                        } else {
                            this.alert('删除失败', 'error')
                        }
                    })
                })
            }
        }
	],
	diyMethods: {
		delete: function(){
			let selection = this.$refs.list.selection
            if (selection.length === 0) {
                this.alert('请选择一行数据')
                return
            }
            if (selection.length > 1) {
                this.alert('一次只能删除一条数据')
                return
            }

            this.$confirm('确定删除吗？', '删除', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let params = {
                    mdCode: this.module.code,
                    id: selection.map(e => e.id).join(',')
                }
                this.post(`${this.BASEURL}/project/resource/roleresource/delete.do`, params).then(res => {
                	if (res.hasOk) {
                        this.$refs.list.refresh()
                        this.alert('删除成功', 'success')
                        this.$emit('deleteAfter', selection)
                    } else {
                        this.alert('删除失败', 'error')
                    }
                })
            })
		}
	}
}