{
    toolbarParams: {
        buttons: [
            {id: "add", isHide: true},
            {id: "modify", isHide: true},
            {id: "delete", isHide: true},
            {id: "view", isHide: true},
            {id: "import", isHide: true},
            {id: "export", isHide: true},
            {id: "unfreeze",name:"启动",onClick:function(){
            	var selections = $("#sys_plugins_logsTable").bootstrapTable('getSelections');
    			if(selections.length == 1){ 
    				if(selections[0].state == 0)
    					BT.showConfirm("启动","将会替换其他同编码的插件！",function(){
        					$.ajax({
        						type: "POST",
        						url:baseURL+"/log/pluginslogsave/updatePluginsState.do",
        						dataType:'json',
        						data:{code:selections[0].code , id:selections[0].id , beanName:selections[0].beanName},
        						success:function(data){
        							if(data && data.hasOk == true){
        								BT.showSuccess("提交成功!",function(){
        									$("#sys_plugins_logsTable").bootstrapTable("refresh");
        								});
        							}else{
        								BT.showError("提交出错!"+data.message);
        							}
        						}
        					});
        				}) 
					else{
						BT.showWarning("当前插件已启动！"); 
					}
     			}else{
    				BT.showWarning("请选中一行数据！"); 
    			}
            },icon:"icon iconfont bd-caret-right"} 
            ]
    },
    
 // 前端工程按钮
    buttons: [
        {id: "add", isHide: true},
        {id: "edit", isHide: true},
        {id: "delete", isHide: true},
        {id: "unfreeze",name:"启动",icon:"icon iconfont bd-caret-right", click:function(){
        	let selections = this.$refs.list.selection
            if (selections.length !== 1) {
              this.alert('请选中一行数据！')
              return
            }
        	if (selections[0].state !== 0) {
              this.alert('当前插件已启动！')
              return
            }
        	
        	this.$confirm('将会替换其他同编码的插件！', '启动', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.post(this.BASEURL+"/log/pluginslogsave/updatePluginsState.do", 
                		{code:selections[0].code , id:selections[0].id , beanName:selections[0].beanName}, 
                		(isSuccess, res) => { 
                  if (isSuccess) {
                	if(res && res.hasOk == true){
                		this.alert('提交成功!', 'success');
                		this.$refs.list.refresh();
					}else{
						this.alert('提交出错!'+res.message, 'error');
					}
                  }
                }, true)
              }).catch(() => {
                        
              });
        }}
    ]
}