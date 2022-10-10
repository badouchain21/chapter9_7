{
    type : {
        func : function (data){
            var chartOptions = {
                title: {
                    text: '',
                },
                tooltip: {
                    trigger: 'item'
                },
                visualMap: {
                    min: 0,
                    max: 200,
                    realtime: false,
                    calculable: true,
                    inRange: {
                        color: ['#14da2c','yellow', 'red']
                    }
                },
                series: [
                    {
                        name: '',
                        type: 'map',
                        itemStyle:{
                            normal:{
                                show:true,
                                position:'top', //标签的位置
                                // distance : 15 ,
                                textStyle : {
                                    //color:"#fff",
                                    fontWeight : 300 ,
                                    fontSize  :22    //文字的字体大小
                                }
                            }
                            ,
                            emphasis:{label:{show:true}}
                        }
                    }
                ]
            };
            if(options.chartOptions)
                $.extend(chartOptions, options.chartOptions);
            /*增加功能 优先级最高 重新定义报表设置*/
            charts.evalJs(chartOptions,false);

            var base = {
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
                    this.echart = echarts.init(options.$element[0]);
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

                    return g;
                },
                renderChartsData: function(data) {
                    (this.options.series)[0].data = data.series;
                    this.options.visualMap.max = this.maxValue(data.series);
                    (this.options.series)[0].mapType = data.mapChartType;
                    this.echart.setOption(this.options);
                },
                maxValue: function(seriesData) {
                    if (seriesData && seriesData.length > 0) {
                        var max = seriesData[0].value;
                        for (var i = 1; i < seriesData.length; i++) {
                            if (max < seriesData[i].value)
                                max = seriesData[i].value;
                        }
                        return max;
                    }
                    return 200;
                }
            }
            $.extend(base,data);
            return base;
        }
    }
}