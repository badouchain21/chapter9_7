{
	 tableParams: {
	        renderColumn: function(column) {
	            if (column.field == 'coverImages') {
	                column.formatter = function (value, row, index) {
 	                    return '<img style="height:40px;"src="'+baseURL+'/plugins_icons/' + row.code + '.png"/>'
	                }
	            };
	            if (column.field == 'statusDesc') {
	                 column.formatter = function (value, row, index) {
	                	var color = "green";
	                	if(row.status == 0)
	                		color = "red";
	                	return '<span style="color:'+color+'">'+value+'</span>';
	                } 
	            }
	        } 
	    },
    toolbarParams: {
        buttons: [
            {id: "add", isHide: true},
            {id: "modify", isHide: true},
            {id: "delete", isHide: true},
            {id: "view", isHide: true},
            {id: "import", isHide: true},
            {id: "export", isHide: true},
            {id: "unfreeze",name:"启用",onClick:function(){
            	var selections = $("#sys_plugins_msgTable").bootstrapTable('getSelections');
    			if(selections.length>0){ 
    				BT.showConfirm("启用","你确定要启用所选的插件吗？",function(){
    					var ids = "";
    					for(var i=0; i<selections.length; i++){
    						if(selections[i].status == 1){
    							BT.showWarning("您选择的数据里包括已被启用的插件!");
    							return false;
    						}
    						ids = ids + selections[i].id + ",";
    					}
    					ids = ids.substring(0,ids.length-1);
    					$.ajax({
    						type: "POST",
    						url:baseURL+"/msg/pluginsmsgsave/updatePluginsStatus.do",
    						dataType:'json',
    						data:{ids:ids, status:1},
    						success:function(data){
    							if(data && data.hasOk == true){
    								BT.showSuccess("提交成功!",function(){
    									$("#sys_plugins_msgTable").bootstrapTable("refresh");
    								});
    							}else{
    								BT.showError("提交出错!"+data.message);
    							}
    						}
    					});
    				}) 
     			}else{
    				BT.showWarning("请至少选中一行！"); 
    			}
            },icon:"icon iconfont bd-caret-right"},
            
            {id: "freeze",name:"停用",onClick:function(){
    			var selections = $("#sys_plugins_msgTable").bootstrapTable('getSelections');
    			 
    			if(selections.length>0){
    				BT.showConfirm("停用","你确定要停用所选的插件吗？",function(){
    					var ids = "";
    					for(var i=0; i<selections.length; i++){
    						ids = ids + selections[i].id + ",";
    						if(selections[i].status == 0){
    							BT.showWarning("您选择的数据里包括已被停用的插件!");
    							return false;
    						}
    					}
    					ids = ids.substring(0,ids.length-1);
     					 $.ajax({
    						type: "POST",
    						url:baseURL+"/msg/pluginsmsgsave/updatePluginsStatus.do",
    						dataType:'json',
    						data:{ids:ids, status:0 },
    						success:function(data){
    							if(data && data.hasOk == true){
    								BT.showSuccess("提交成功!",function(){
    									$("#sys_plugins_msgTable").bootstrapTable("refresh");
    								});
    							}else{
    								BT.showError("提交出错!"+data.message);
    							}
    						}
    					}); 
    				}) 
     			}else{
    				BT.showWarning("请至少选中一行！"); 
    			}
            },icon:"fa fa-close"},
            {id: "log",name:"日志",onClick:function(){
            	 parent.f_addTab("plugins_log","插件启动日志",baseURL+"/jdbc/common/basecommonlist/list.do?mdCode=sys_plugins_logs");
            },icon:"fa fa-file-text-o"}
            ]
    },
    // 前端工程按钮
    buttons: [
        {id: "add", isHide: true},
        {id: "edit", isHide: true},
        {id: "delete", isHide: true},
        {id: "unfreeze",name:"启用",icon:"icon iconfont bd-caret-right", click:function(){
        	let selections = this.$refs.list.selection
            if (selections.length === 0) {
              this.alert('请至少选中一行！')
              return
            }
        	this.$confirm('你确定要启用所选的插件吗？', '启用', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
            	let ids = "";
  				for(let i=0; i<selections.length; i++){
  					if(selections[i].status == 1){
  						this.alert("您选择的数据里包括已被启用的插件!");
  						return;
  					}
  					ids = ids + selections[i].id + ",";
  				}
  				ids = ids.substring(0,ids.length-1);  
                this.post(this.BASEURL+"/msg/pluginsmsgsave/updatePluginsStatus.do", 
                		{ids:ids,status:1}, (isSuccess, res) => { 
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
        }},
        
        {id: "freeze",name:"停用",icon:"fa fa-close",click:function(){
        	let selections = this.$refs.list.selection
            if (selections.length === 0) {
              this.alert('请至少选中一行！')
              return
            }
        	this.$confirm('你确定要停用所选的插件吗？', '停用', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
            	let ids = "";
  				for(let i=0; i<selections.length; i++){
  					if(selections[i].status == 0){
  						this.alert("您选择的数据里包括已被停用的插件!");
  						return;
  					}
  					ids = ids + selections[i].id + ",";
  				}
  				ids = ids.substring(0,ids.length-1);  
                this.post(this.BASEURL+"/msg/pluginsmsgsave/updatePluginsStatus.do", 
                		{ids:ids, status:0}, (isSuccess, res) => { 
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
        }},
        
        {id: "log",name:"日志",icon:"fa fa-file-text-o",click:function(){
        	let listPath = this.$parent.$route.path
            this.$router.push({
                path: `/module/stander/list/sys_plugins_logs/placeholder?defaultParams=placeholder`,
                query: {
                    data: {
                        returnPath: listPath
                    }
                }
            })
        }}
    ],
    
    /*
     * 前端工程-列表formatter
     * 返回格式：{
     * 		fieldName: {
     * 			formatter: function (row, column, value, index, vue) { }, 
     * 			hander: function (row, value, index, vue) { }
     * 		}
     * }
     * 其中 formatter 为格式化数据的方法，hander 为点击时触发的方法
     */
    renderColumn: {
        coverImages: {
        	formatter: function (row, column, value, index, vue) {
        		return '<img style="height:40px;"src="'+vue.BASEURL+'/plugins_icons/' + row.code + '.png"/>'
            }
        },
    	statusDesc: {
    		formatter: function (row, column, value, index, vue) {
             	var color = "green";
             	if(row.status == 0)
             		color = "red";
             	return '<span style="color:'+color+'">'+value+'</span>';
             }
    	}
    }
}
