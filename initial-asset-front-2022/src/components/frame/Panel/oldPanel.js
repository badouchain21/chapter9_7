import Vue from 'vue'
var echarts = require("echarts");
function evalJs(options, isAfterRender) {
  try {
    if (options.tempChartOptions != null && options.tempChartOptions.indexOf("chartOptions") == 0) {
      var arr = options.tempChartOptions.split(";");
      for (var i = 0; i < arr.length; i++) {
        try {
          if (isAfterRender) {
            if (arr[i].indexOf("chartOptions.series") > -1) {
              eval(arr[i].replace("chartOptions", "g.options"));
            }
          } else {
            if (arr[i].indexOf("chartOptions.series") == -1) {
              eval(arr[i].replace("chartOptions", "options"));
            }
          }
        } catch (e) {
          Vue.prototype.alert("[" + arr[i] + "] 执行失败，失败原因如下：" + e.message);
        }
      }
    }
  } catch (e) {
    Vue.prototype.alert("执行失败，失败原因如下：" + e.message);
  }
}
export default {
  initBar(options) {
    var chartOptions = {
      tooltip: {
        trigger: "axis",
        axisPointer: {
          // 坐标轴指示器，坐标轴触发有效
          type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
        }
      },
      grid: {
        left: "3%",
        right: "4%",
        bottom: "3%",
        containLabel: true
      },
      legend: {
        data: []
      },
      label: {
        // 图图形上的文本标签
        normal: {
          show: true,
          position: "top", //标签的位置
          distance: 18,
          offset: [-8, 0],
          textStyle: {
            fontSize: 16 //文字的字体大小
          }
        }
      },
      xAxis: [
        {
          type: "category",
          data: [],
          axisTick: {
            alignWithLabel: true
          },
          axisLine: {
            lineStyle: {
              type: "dotted",
              color: "#000"
            }
          },
          axisLabel: {
            show: true,
            textStyle: {
              color: "#000"
            }
          },
          splitLine: {
            lineStyle: {
              type: "dotted",
              color: "#ccc"
            }
          }
        }
      ],
      yAxis: [
        {
          type: "value",
          axisLine: {
            lineStyle: {
              type: "dotted",
              color: "#000"
            }
          },
          axisLabel: {
            show: true,
            textStyle: {
              color: "#000"
            }
          },
          splitLine: {
            lineStyle: {
              type: "dotted",
              color: "#ccc"
            }
          }
        }
      ],
      series: []
    };
    if (options.chartOptions) $.extend(chartOptions, options.chartOptions);

    /*增加功能 优先级最高 重新定义报表设置*/
    evalJs(chartOptions, false);

    var base = {
      $element: null,
      reportId: null,
      xFieldName: null,
      yFieldName: null,
      staticsFieldName: null,
      yAxisUnit: null,
      echart: null,
      panelFilterReportId: null,
      options: chartOptions,
      load: function (params) {
        var g = this;
        this.echart = echarts.init(options.$element);
        Vue.prototype.post(
          Vue.prototype.INTERFACE.barReport,
          {
            reportId: options.reportId,
            xFieldName: options.xFieldName,
            yFieldName: options.yFieldName,
            staticsFieldName: options.staticsFieldName,
            panelFilterReportId: options.panelFilterReportId,
            params: params
          },
          (isSuccess, res) => {
            if (res.xAxis.length > 0) {
              if (options.yAxisUnit != null)
                res.yAxisUnit = options.yAxisUnit;
              g.renderChartsData(res);
            } else {
              obj.innerHTML =
                '<div class="noDataIconArea"><i class="fa fa-frown-o noDataIcon"></i><div class="noData">暂无数据</div></div>';
            }
          },
          true
        );
      },
      renderChartsData: function (data) {
        var g = this;
        if (data) {
          if ((g.options.yAxis[0]).type == 'value')
            (g.options.xAxis[0]).data = data.xAxis;
          else
            (g.options.yAxis[0]).data = data.xAxis;
          g.options.legend.data = data.legend;

          g.options.series = data.series;

          if (data.yAxisUnit) {
            (g.options.yAxis[0]).axisLabel = { formatter: '{value} ' + data.yAxisUnit };
          }

          evalJs(g.options, true);
          this.echart.setOption(g.options, true);
        }
      }
    }
    $.extend(base, options);
    return base;
  },

  initLine(options) {
    var chartOptions = {
      tooltip: {
        trigger: "axis"
      },
      legend: {
        data: []
      },
      grid: {
        left: "3%",
        right: "8%",
        bottom: "3%",
        containLabel: true
      },
      label: {
        //饼图图形上的文本标签
        normal: {
          show: true,
          position: "top", //标签的位置
          distance: 15,
          textStyle: {
            fontWeight: 300,
            fontSize: 22 //文字的字体大小
          }
        }
      },
      xAxis: {
        scale: false,
        type: "category",
        data: [],
        axisLine: {
          lineStyle: {
            type: "dotted",
            color: "#ADD8E6"
          }
        },
        axisLabel: {
          show: true,
          textStyle: {
            color: "#000"
          }
        },
        splitLine: { lineStyle: { color: "#e5e5e5" } }
      },
      yAxis: {
        type: "value",
        axisLine: {
          lineStyle: {
            type: "dotted",
            color: "#000"
          }
        },
        axisLabel: {
          show: true,
          textStyle: {
            color: "#000"
          }
        },
        splitLine: {
          lineStyle: {
            type: "dotted",
            color: "#ccc"
          }
        }
      },
      series: []
    };
    if (options.chartOptions) $.extend(chartOptions, options.chartOptions);
    /*增加功能 优先级最高 重新定义报表设置*/
    evalJs(chartOptions, false);
    var base = {
      $element: null,
      reportId: null,
      xFieldName: null,
      yFieldName: null,
      staticsFieldName: null,
      yAxisUnit: null,
      echart: null,
      options: chartOptions,
      panelFilterReportId: null,
      load: function (params) {
        var g = this;
        this.echart = echarts.init(options.$element);
        Vue.prototype.post(
          Vue.prototype.INTERFACE.lineReport,
          {
            reportId: options.reportId,
            xFieldName: options.xFieldName,
            yFieldName: options.yFieldName,
            staticsFieldName: options.staticsFieldName,
            params: params
          },
          (isSuccess, res) => {
            if (res.xAxis.length > 0) {
              if (options.yAxisUnit != null)
                res.yAxisUnit = options.yAxisUnit;
              g.renderChartsData(res);
            } else {
              obj.innerHTML =
                '<div class="noDataIconArea"><i class="fa fa-frown-o noDataIcon"></i><div class="noData">暂无数据</div></div>';
            }
          },
          true
        );
      },
      renderChartsData: function (data) {
        var g = this;
        if (data) {
          g.options.xAxis.data = data.xAxis;
          g.options.legend.data = data.legend;
          g.options.series = data.series;
          if (data.yAxisUnit) {
            g.options.yAxis.axisLabel = { formatter: '{value} ' + data.yAxisUnit };
          }

          evalJs(g.options, true);
          this.echart.setOption(g.options, true);
        }
      }
    }
    $.extend(base, options);
    return base;
  },

  initPie(options) {
    var chartOptions = {
      tooltip: {
        trigger: "item",
        formatter: "{a} <br/>{b}: {c} ({d}%)"
      },
      legend: {
        orient: "vertical",
        x: "left",
        itemWidth: 8,
        itemHeight: 8,
        data: []
      },
      series: [
        {
          name: "",
          type: "pie",
          radius: "55%",
          center: ["50%", "60%"],
          label: {
            //饼图图形上的文本标签
            normal: {
              show: true,
              position: "top", //标签的位置
              distance: 15,
              textStyle: {
                fontWeight: 300,
                fontSize: 22 //文字的字体大小
              },
              formatter: "{d}%"
            }
          },
          data: [],
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)"
            }
          }
        }
      ]
    };
    if (options.chartOptions) $.extend(chartOptions, options.chartOptions);
    /*增加功能 优先级最高 重新定义报表设置*/
    evalJs(chartOptions, false);
    var base = {
      $element: null,
      reportId: null,
      xFieldName: null,
      yFieldName: null,
      echart: null,
      options: chartOptions,
      panelFilterReportId: null,
      load: function (params) {
        var g = this;
        this.echart = echarts.init(options.$element);
        Vue.prototype.post(
          Vue.prototype.INTERFACE.pieReport,
          {
            reportId: options.reportId,
            xFieldName: options.xFieldName,
            yFieldName: options.yFieldName,
            params: params
          },
          (isSuccess, res) => {
            if (!$.isEmptyObject(res)) {
              g.renderChartsData(res);
            } else {
              obj.innerHTML =
                '<div class="noDataIconArea"><i class="fa fa-frown-o noDataIcon"></i><div class="noData">暂无数据</div></div>';
            }
          },
          true
        );
      },
      renderChartsData: function (data) {
        g = this;
        if (data) {
          g.options.legend.data = data.legend;
          (g.options.series)[0].data = data.series;

          evalJs(g.options, true);
          this.echart.setOption(g.options, true);
        }
      }
    }
    $.extend(base, options);
    return base;
  },

  initStackArea(options) {
    var chartOptions = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        }
      },
      legend: {
        data: [],
        textStyle: {
          color: '#000'
        }
      },
      label: {            //饼图图形上的文本标签
        normal: {
          show: true,
          position: 'top', //标签的位置
          distance: 15,
          textStyle: {
            fontWeight: 300,
            fontSize: 22    //文字的字体大小
          },
        }
      },
      grid: {
        left: '3%',
        right: '8%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          axisLine: {
            lineStyle: {
              type: 'dotted',
              color: "#000"
            }
          },
          axisLabel: {
            show: true,
            textStyle: {
              color: '#000'
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dotted',
              color: "#ccc"
            }
          },
          data: []
        }
      ],
      yAxis: [
        {
          type: 'value',
          axisLine: {
            lineStyle: {
              type: 'dotted',
              color: "#000"
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dotted',
              color: "#ccc"
            }
          }
        }
      ],
      series: []
    };
    if (options.chartOptions) $.extend(chartOptions, options.chartOptions);
    /*增加功能 优先级最高 重新定义报表设置*/
    evalJs(chartOptions, false);
    var base = {
      $element: null,
      reportId: null,
      xFieldName: null,
      yFieldName: null,
      echart: null,
      options: chartOptions,
      panelFilterReportId: null,
      load: function (params) {
        var g = this;
        this.echart = echarts.init(options.$element);
        Vue.prototype.post(
          Vue.prototype.INTERFACE.stackAreaReport,
          {
            reportId: options.reportId,
            xFieldName: options.xFieldName,
            yFieldName: options.yFieldName,
            params: params
          },
          (isSuccess, res) => {
            if (res.xAxis.length > 0) {
              if (options.yAxisUnit != null)
                res.yAxisUnit = options.yAxisUnit;
              g.renderChartsData(res);
            } else {
              obj.innerHTML =
                '<div class="noDataIconArea"><i class="fa fa-frown-o noDataIcon"></i><div class="noData">暂无数据</div></div>';
            }
          },
          true
        );
      },
      renderChartsData: function (data) {
        g = this;
        if (data) {
          if ((g.options.yAxis[0]).type == 'value')
            (g.options.xAxis[0]).data = data.xAxis;
          else
            (g.options.yAxis[0]).data = data.xAxis;
          g.options.legend.data = data.legend;
          g.options.series = data.series;
          if (data.yAxisUnit) {
            (g.options.yAxis[0]).axisLabel = { formatter: '{value} ' + data.yAxisUnit };
          }

          evalJs(g.options, true);
          this.echart.setOption(g.options, true);
        }
      }
    }
    $.extend(base, options);
    return base;

  },

  initRadar(options) {
    var chartOptions = {
      tooltip: {
        show: true,
        axisPointer: {
          type: 'shadow'
        },
        position: function (point, params, dom) {
          return [point[0], '10%'];
        }
      },
      legend: {
        data: []
      },
      radar: {
        name: {
          textStyle: {
            color: '#000',
            backgroundColor: '#999',
            borderRadius: 3,
            padding: [3, 5]
          }
        },
        indicator: []
      },
      series: [{
        type: 'radar',
        data: []
      }]
    };
    if (options.chartOptions) $.extend(chartOptions, options.chartOptions);
    /*增加功能 优先级最高 重新定义报表设置*/
    evalJs(chartOptions, false);
    var base = {
      $element: null,
      reportId: null,
      xFieldName: null,
      yFieldName: null,
      echart: null,
      options: chartOptions,
      panelFilterReportId: null,
      load: function (params) {
        var g = this;
        this.echart = echarts.init(options.$element);
        Vue.prototype.post(
          Vue.prototype.INTERFACE.radarReport,
          {
            reportId: options.reportId,
            xFieldName: options.xFieldName,
            yFieldName: options.yFieldName,
            params: params
          },
          (isSuccess, res) => {
            if (!$.isEmptyObject(res)) {
              g.renderChartsData(res);
            } else {
              obj.innerHTML =
                '<div class="noDataIconArea"><i class="fa fa-frown-o noDataIcon"></i><div class="noData">暂无数据</div></div>';
            }
          },
          true
        );
      },
      renderChartsData: function (data) {
        g = this;
        if (data) {
          g.options.legend.data = data.legend;
          g.options.radar.indicator = data.indicator;
          (g.options.series)[0].data = data.data;

          evalJs(g.options, true);
          this.echart.setOption(g.options, true);
        }
      }
    }
    $.extend(base, options);
    return base;
  },

  initStackBar(options) {
    var chartOptions = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
          type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '5%',
        containLabel: true
      },
      legend: {
        data: []
      },
      label: {            //图形上的文本标签
        normal: {
          show: true,
          position: 'top', //标签的位置
          distance: 15,
          position: ['100%', '0%'],
          textStyle: {
            fontWeight: 300,
            fontSize: 22    //文字的字体大小
          },
          rich: {
            z: 999
          },
        }
      },
      xAxis: [
        {
          type: 'category',
          data: [],
          axisTick: {
            alignWithLabel: true
          },
          axisLine: {
            lineStyle: {
              type: 'dotted',
              color: "#000"
            }
          },
          splitLine: { lineStyle: { color: "#000" } },

        }
      ],
      yAxis: [
        {
          type: 'value',
          axisLine: {
            lineStyle: {
              type: 'dotted',
              color: "#000"
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dotted',
              color: "#ccc"
            }
          },
        }
      ],
      series: []
    };
    if (options.chartOptions) $.extend(chartOptions, options.chartOptions);
    /*增加功能 优先级最高 重新定义报表设置*/
    evalJs(chartOptions, false);
    base = {
      $element: null,
      reportId: null,
      xFieldName: null,
      yFieldName: null,
      yAxisUnit: null,
      echart: null,
      options: chartOptions,
      panelFilterReportId: null,
      load: function (params) {
        var g = this;
        this.echart = echarts.init(options.$element);
        Vue.prototype.post(
          Vue.prototype.INTERFACE.stackBarReport,
          {
            reportId: options.reportId,
            xFieldName: options.xFieldName,
            yFieldName: options.yFieldName,
            params: params
          },
          (isSuccess, res) => {
            if (res.xAxis.length > 0) {
              if (options.yAxisUnit != null)
                res.yAxisUnit = options.yAxisUnit;
              g.renderChartsData(res);
            } else {
              obj.innerHTML =
                '<div class="noDataIconArea"><i class="fa fa-frown-o noDataIcon"></i><div class="noData">暂无数据</div></div>';
            }
          },
          true
        );
      },
      renderChartsData: function (data) {
        g = this;
        if (data) {
          if ((g.options.yAxis[0]).type == 'value')
            (g.options.xAxis[0]).data = data.xAxis;
          else
            (g.options.yAxis[0]).data = data.xAxis;
          g.options.legend.data = data.legend;
          g.options.series = data.series;
          if (data.yAxisUnit) {
            (g.options.yAxis[0]).axisLabel = { formatter: '{value} ' + data.yAxisUnit };
          }

          evalJs(g.options, true);
          this.echart.setOption(g.options, true);
        }
      }
    }
    $.extend(base, options);
    return base;
  },
}