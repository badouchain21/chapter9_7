{
    delDefalutButtons:[{id:"delete"}],
    listDataUrl: function() {
        return `${this.BASEURL}/assetDefined/listJSON?mdCode=${this.module.code}`
    },
    buttons: [
           {
			id: 'init',
            name: '生成接口',
            icon: 'plus',
  		    click: function () {
  		    	let selection = this.$refs.list.selection
                if (selection.length != 1) {
                    this.alert('请选择一行！')
                    return
                }
                let params = {
  		    			"mdCode" : `${this.module.code}`,
  		    			"id" : `${selection[selection.length - 1].id}`
                };
	        	this.post(`${this.BASEURL}/api/intermanage/interfacedetail/interfacedetaillist/init?id=${selection[selection.length - 1].id}`, params).then(res => {
        	  		if (res.hasOk) {
        	  			this.$refs.list.refresh();
        	  			this.alert('生成成功', 'success')
        	  		} else {
        	  			this.alert(res.message, 'error')
        	  		}
        	  	}, true)
  		    }
  		}, {
            id: 'viewApi',
            name: '接口文档',
            icon: 'document',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length != 1) {
                    this.alert('请选择一行！')
                    return
                }
                let params = {
  		    			"mdCode" : `${this.module.code}`,
  		    			"id" : `${selection[selection.length - 1].id}`
                };
                const { href } = this.$router.resolve({
                    name: "Api",
                    params: {
  		    			"mdCode" : `${this.module.code}`,
  		    			"id" : `${selection[selection.length - 1].id}`
                }
                });
                window.open(href, '_blank');
            }
  		},
        {
            id: 'edit',
            name: '修改',
            icon: 'edit',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length !== 1) {
                    this.alert('请选择一行！')
                    return
                }
                let row = selection[0]
                this.get(`${this.BASEURL}/assetDefined/isBaseAsset?assetCode=`+row.assetCode).then(res => {
                    if (res){
                        this.alert('基础资产不能修改！')
                        return
                    } else {
                        let listPath = this.$parent.$route.path
                        this.$router.push({
                            path: `/module/stander/edit/${this.module.code}/${selection[selection.length - 1].id}`,
                            query: {
                                data: {
                                    returnPath: listPath
                                }
                            }
                        })
                    }
                })
            }
        }
  		]
}
