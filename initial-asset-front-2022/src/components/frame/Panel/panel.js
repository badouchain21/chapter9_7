var Vue = require("vue");
var echarts = require("echarts");
async function init(defaults) {
    var chartsObj = null;

    // 加载到图表js后的处理函数
    var callback = function (data) {
        if (defaults.panelFilterReportId != null && defaults.panelFilterReportId != undefined && defaults.panelFilterReportId != "") {
            defaults.dataSourceId = defaults.panelFilterReportId;
            defaults.dataSourceType = "filter";
        } else if (defaults.reportId != null && defaults.reportId != undefined && defaults.reportId != "") {
            defaults.dataSourceId = defaults.reportId;
            defaults.dataSourceType = "report";
        }

        chartsObj = data.type.func({
            $element: defaults.$element,
            dataSourceId: defaults.dataSourceId,
            dataSourceType: defaults.dataSourceType,
            reportShowTypeId: defaults.reportShowTypeId,
            dataContent: defaults.dataContent,
            chartOptions: defaults.chartOptions,
            reportId: defaults.reportId,
            dataDef: defaults.dataDef
        });
    }

    /*
         * 如果有dataDef.chartDefJS这个属性，为自定义图表，就是dataDef中已经有定义图表的js，
         * 不需要去后台加载js
         */
    if (defaults.dataDef) {
        var chartJs = genChartDefJS(defaults.dataDef, defaults.chartOptions);
        if (chartJs) {
            callback(chartJs)
            return chartsObj;
        }
    }

    var jsObj = await loadJS(defaults.reportShowTypeId)
    callback(jsObj);
    

    return chartsObj;
}

// 加载json格式的js
async function loadJS(reportShowTypeId) {
    let data = await loadJSPath(reportShowTypeId);
    let jsObj = {};
    if (data.hasOk) {
        jsObj = await loadScriptJSON(data.bean);
    } else {
        Vue.prototype.alert(data.message);
    }
    return jsObj;
}
// 加载js路径
function loadJSPath(reportShowTypeId) {
    return new Promise(resolve => {
        Vue.prototype.get(Vue.prototype.INTERFACE.packJsPath, {
            reportShowTypeId: reportShowTypeId, isVue: true
        }, (isSuccess, res) => {
            resolve(res)
        }, true)
    })
}

//动态加载json格式的js脚本 js文件里面的内容必须为json格式
function loadScriptJSON(url, callback) {
    let baseUrl = '';
    // 针对生产环境
    if(process.env.NODE_ENV === 'production'){
        baseUrl = Vue.prototype.BASEURL + '/dist';
    }
    return new Promise(resolve => {
        Vue.prototype.get(baseUrl + url, {}, (isSuccess, res) => {
            let obj = eval("(" + res + ")");
            resolve(obj)
        }, true)
    })
}

/*
 * dataDef.chartDefJS[0].fieldName 		为图表定义，相当于图表的options
 * dataDef.renderChartJS[0].fieldName   为渲染图表的方法
 *
 * 生成报表的处理方法，返回的是一个Object，格式与 panel.plugin.line.js 等
 * 一致，如下：
 * {
 *     type: {
 *         func: function(data){
 *             ...
 *         }
 *     }
 * }
 */
function genChartDefJS(dataDefStr, optionsChartOptions) {
    var dataDef = JSON.parse(dataDefStr)
    if (typeof dataDef.chartDefJS == 'undefined') {
        return null;
    }
    var chartOptions = eval("(" + dataDef.chartDefJS[0].fieldName + ")");
    if (optionsChartOptions)
        $.extend(chartOptions, optionsChartOptions);
    /*增加功能 优先级最高 重新定义报表设置*/
    evalJs(chartOptions, false);

    var renderChartObj = eval("(" + dataDef.renderChartJS[0].fieldName + ")");

    return {
        type: {
            func: function (data) {
                var base = {
                    $element: null,
                    reportId: null,
                    echart: null,
                    dataDef: null,
                    panelFilterReportId: null,
                    options: chartOptions,
                    dataSourceId: null,
                    dataSourceType: null,
                    reportShowTypeId: null,
                    load: function (params) {
                        var _this = this;
                        _this.echart = echarts.init(data.$element);
                        Vue.prototype.post(
                            Vue.prototype.INTERFACE.packData,
                            {
                                dataSourceId: this.dataSourceId,
                                dataSourceType: this.dataSourceType,//[filter||report]
                                reportShowTypeId: this.reportShowTypeId,
                                dataDef: this.dataDef,
                                params: params
                            },
                            (isSuccess, res) => {
                                if (res.bean.data != null && res.bean.data != undefined) {
                                    g.renderChartsData(res.bean.data);
                                    renderChartObj.renderChart.call(_this, res.bean.data)
                                } else {
                                    options.$element.html('<div class="noDataIconArea"><i class="fa fa-frown-o noDataIcon"></i><div class="noData">暂无数据</div></div>');
                                }
                            },
                            true
                        );
                    }
                }
                $.extend(base, data);
                return base;
            }
        }
    }
}

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
    async genChartsObj(options) {
        var defaults = {
            $element: null,
            reportId: null,
            panelFilterReportId: null,
            dataSourceId: null,
            dataSourceType: null,
            reportShowTypeId: null,
            dataContent: null,
            chartOptions: null,
            dataDef: null
        },
            defaults = $.extend({}, defaults, options);
        //启动方法请求加载数据
        return await init(defaults);
    }
}
