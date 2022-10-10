{
	type : {
		func : function (data){
 				var chartOptions = {
 		 		    tooltip : {
 				        trigger: 'axis',
 				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
 				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
 				        }
 				    },
 				    grid: {
 				        left: '3%',
 				        right: '4%',
 				        bottom: '3%',
 				        containLabel: true
 				    },
 				    legend: {
 				        data:[]
 				    },
 				    label:{            // 图图形上的文本标签
 		                normal:{
 		                    show:true,
 		                    position:'top', //标签的位置
 		                    distance : 18 ,
 		                    offset:[-8,0],
 		                    textStyle : {
 		                        fontSize : 16    //文字的字体大小
 		                    }
 		                }
 		            },
 				    xAxis : [
 				        {
 				         //   type : 'category',
 				            type : 'category',
 				            data : [],
 				            axisTick: {
 				                alignWithLabel: true
 				            },
 				            axisLine:{
 					        	lineStyle:{ 
 					        		type:'dotted',
 						        	color:"#fff"
 						        }
 					        },
 					        axisLabel: {
 		                        show: true,
 		                        textStyle: {
 		                            color: '#fff'
 		                        }
 		                    },
 		                    splitLine:{
 				            	lineStyle:{
 				            		type:'dotted',
 				            		color:"#ccc"
 				            	}
 					        }
 				            
 				        }
 				    ],
 				    yAxis : [
 				        {
 				        	type : 'value',
 				        //    type : 'value',
 				            axisLine:{
 					        	lineStyle:{ 
 					        		type:'dotted',
 						        	color:"#fff"
 						        }
 					        },
 					        axisLabel: {
 		                        show: true,
 		                        textStyle: {
 		                            color: '#fff'
 		                        }
 		                    },
 				            splitLine:{
 				            	lineStyle:{
 				            		type:'dotted',
 				            		color:"#ccc"
 				            	}
 					        }
 				        }
 				    ],
 				    series : []
 				};
 			     if(data.chartOptions)
 	                $.extend(chartOptions, data.chartOptions);
 	            /*增加功能 优先级最高 重新定义报表设置*/
 	            charts.evalJs(chartOptions,false);
 	            
  		 		base = {
 		 			$element:null,
 	 				reportId:null,
 	 				echart:null,
 	 				dataDef : null,
 	 				panelFilterReportId: null,
 	 				options:chartOptions,
 	 				dataSourceId : null,
 		 			dataSourceType :null,
 		 			reportShowTypeId: null,
 					load:function(params){
 		 				var g = this;
  		 				this.echart = echarts.init(data.$element[0]);
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
   								 if( data.bean.data != null &&  data.bean.data != undefined){
  									 g.renderChartsData(data.bean.data);
 								 }else{
 									 options.$element.html('<div class="noDataIconArea"><i class="fa fa-frown-o noDataIcon"></i><div class="noData">暂无数据</div></div>');
 								 }
 							
 							 }
 					 	});
 					},
 					renderChartsData: function(data) {
 						g = this;
 						 if(data){
 							if((g.options.yAxis[0]).type == 'value')
 								(g.options.xAxis[0]).data = data.cate;
  							 var seriesArr = data.series;
   							 for(var i = 0 ; i < seriesArr.length ; i++ ){
 								g.options.series.push({	name : seriesArr[i].name,type:'bar',data : seriesArr[i].data});
 							 }
  		 	                 if (data.yAxisUnit) {
 			                 	(g.options.yAxis[0]).axisLabel = {formatter: '{value} ' + data.yAxisUnit};
 			                 }
  		 	                
 			                charts.evalJs(g.options,true);
 	                        this.echart.setOption(g.options,true);
 						 } 
 					}
 				}
 				$.extend(base,data);
 				return base;
		}
	}
}