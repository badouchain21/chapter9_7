{
    type : {
        func : function (data){
            var chartOptions  = {
                title : {
                    text: ''
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    itemWidth:8,
                    itemHeight:8,
                    data:[]
                },
                series: [{
                    name: '',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    label:{            //饼图图形上的文本标签
                        normal:{
                            show:true,
                            position:'top', //标签的位置
                            distance : 15 ,
                            textStyle : {
                                //color:"#fff",
                                fontWeight : 300 ,
                                fontSize : 22    //文字的字体大小
                            },
                            formatter:'{d}%'


                        }
                    },
                    data:[],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }]
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
                    g = this;
                    if(data){
                        g.options.legend.data = data.legend;
                        (g.options.series)[0].data = data.series;

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