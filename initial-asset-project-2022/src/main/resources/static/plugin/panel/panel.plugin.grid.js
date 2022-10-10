{
	type : {
		func : function (data){
			base = {
					$element:null,
 	 				reportId:null,
 	 				echart:null,
 	 				dataDef : null,
 	 				panelFilterReportId: null,
 	 				options:null,
 	 				dataSourceId : null,
 		 			dataSourceType :null,
 		 			reportShowTypeId: null,
					//生成列表
					genColumns : function(data){
				  	 	if(data){
				  		   var columns = new Array();
					       columns.push({field: 'number',title: '序号',formatter: function (value, row, index) {return (index+1);}});
				       
				      	   for(var key in data){
				      		   if(!data[key].isHidden){
				      			  	columns.push({title:data[key].display,field:data[key].name, type:data[key].type,align:"center" });
				      		   }
				      	    }
				      	 	return columns;
				  		}
				  	 	return null;
				    },
					initTable: function($obj,url,columns,queryParams,data){
						var chartOptions = {
 				       		method: 'post',
				       		sidePagination: "server",
				       		dataType:'json',
				       		dataField: 'Rows',
				       		totalField: 'Total',
				       		contentType:'application/x-www-form-urlencoded',
				       		queryParamsType:"undefined",
				            striped: true,                      //是否显示行间隔色
				            pagination: false,
				       		queryParams: function(params) {
				                var param = {
			                        pageNo: params.pageNumber,
			                        perPageSize: params.pageSize,
			                        usePage: true,
			                        searchParam:queryParams
				                };
				                return param;
		                    },
				            pageNumber:1,                       //初始化加载第一页，默认第一页
			                pageSize: 20,                       //每页的记录行数（*）
			                pageList: [10, 20, 50, 100],        //可供选择的每页的行数（*）
				   	        clickToSelect: true,                //是否启用点击选中行
				            uniqueId: "batchNum",                 //每一行的唯一标识，一般为主键列
				       		columns:  columns,
 						}
						if(url == null && data != null && data != undefined)
							$.extend(chartOptions,{data:data});
						else if(url != null)
							$.extend(chartOptions,{url:url});
						console.info(chartOptions);
						$obj.bootstrapTable(chartOptions);
					},
					load: function(params){
 		 				var g = this;
						if(this.dataSourceType == 'report'){
							$.ajax({
								 type: "POST",
								 url: baseURL + "/report/commonreport/gridReport.do?reportId="+g.dataSourceId,
								 dataType: 'json',
								 data:{
									 dataSourceId : this.dataSourceId,
									 dataSourceType : this.dataSourceType,//[filter||report]
									 reportShowTypeId: this.reportShowTypeId,
									 dataDef : this.dataDef,
	 								 params:params
								 },
	 							 success: function(data){
	 								 	 options.$element.empty();
	 								 	 options.$element.append('<div class="common-table tableScroll"><table class="table table-hover"></table></div>');
										 var $table = options.$element.find(".table");
										 var columns = g.genColumns(data.moduleFields);
										 var url = baseURL+data.listJSONURL+'?csId='+g.dataSourceId;
										 g.initTable($table,url,columns,params);
	 								 }
	 					 	});
						}else if(this.dataSourceType == 'filter'){ 
 							$.ajax({
								 type: "POST",
								 url: baseURL + "/panel/cfg/reportshowtype/reportshowtypeengine/packData.do?",
								 dataType: 'json',
								 data:{
									 dataSourceId : this.dataSourceId,
									 dataSourceType : this.dataSourceType,//[filter||report]
									 reportShowTypeId: this.reportShowTypeId,
									 dataDef : this.dataDef,
	 								 params:params
								 },
	 							 success: function(data){
	 								 	 options.$element.empty();
	 								 	 options.$element.append('<div class="common-table tableScroll"><table class="table table-hover"></table></div>');
										 var $table = options.$element.find(".table");
										 var columns =  data.bean.columns;
										 var datas = data.bean.datas;
										 g.initTable($table,null,columns,params,datas);
	 								 }
	 					 	});
						}
						
					}
				}
				$.extend(base,data);
				return base;
			}
	}
}