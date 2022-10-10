<template>
    <div class="wrap" style="color: #666;">
        <!-- 顶部五个统计数据 -->
        <el-form class="el-form">
            <div class="icons-box">
                <div class="icons-item">
                    <div class="item-top">
                        <div class="item-top-left">
                            <img class="left-img locked-img" src="static/img/index/locked.png" />
                            <p class="left-title">已上链数据</p>
                        </div>
                        <div class="item-top-right fon-color1" v-html="uploadedAssetCount"></div>
                    </div>
                    <div class="item-bottom">
                        <p class="bottom-line fon-color1"></p>
                    </div>
                </div>
                <div class="icons-item">
                    <div class="item-top">
                        <div class="item-top-left">
                            <img class="left-img" src="static/img/index/locking.png" />
                            <p class="left-title">正在上链数据</p>
                        </div>
                        <div class="item-top-right fon-color2" v-html="uploadingAssetCount"></div>
                    </div>
                    <div class="item-bottom">
                        <p class="bottom-line fon-color2"></p>
                    </div>
                </div>
                <div class="icons-item">
                    <div class="item-top">
                        <div class="item-top-left">
                            <img class="left-img" src="static/img/index/tolock.png" />
                            <p class="left-title">待上链数据</p>
                        </div>
                        <div class="item-top-right fon-color3" v-html="unUnploadAssetCount"></div>
                    </div>
                    <div class="item-bottom">
                        <p class="bottom-line fon-color3"></p>
                    </div>
                </div>
                <div class="icons-item">
                    <div class="item-top">
                        <div class="item-top-left">
                            <img class="left-img" src="static/img/index/24hours.png" />
                            <p class="left-title">24小时数据</p>
                        </div>
                        <div class="item-top-right fon-color4" v-html="uploadedAssetIn24Hours"></div>
                    </div>
                    <div class="item-bottom">
                        <p class="bottom-line fon-color4"></p>
                    </div>
                </div>
                <div class="icons-item">
                    <div class="item-top">
                        <div class="item-top-left">
                            <img class="left-img" src="static/img/index/date.png" />
                            <p class="left-title">近一个月数据</p>
                        </div>
                        <div class="item-top-right fon-color5" v-html="uploadedAssetInOneMonth"></div>
                    </div>
                    <div class="item-bottom">
                        <p class="bottom-line fon-color5"></p>
                    </div>
                </div>
            </div>
        </el-form>
        <div class="chart">
            <div class="chart-bar">
                <div class="chart-search">
                    <!-- 选择年份 -->
                    年度查询
                    <div class="el-date-picker my-picker">
                        <el-date-picker
                            v-model="startYear"
                            type="year"
                            placeholder="选择起始年份"
                            :picker-options="datePickerOptions"
                            @change="datePickerChanagHandl"
                        ></el-date-picker>
                    </div>至
                    <div class="el-date-picker my-picker">
                        <el-date-picker
                            v-model="endYear"
                            type="year"
                            placeholder="选择终止年份"
                            :picker-options="datePickerOptions"
                            @change="datePickerChanagHandl"
                        ></el-date-picker>
                    </div>
                    <!-- <div class="el-date-picker">
                        <el-button @click="getAssetByYear">确 定</el-button>
                    </div> -->
                </div>

                <!-- 左下角的按年度统计的条形图 -->
                <div id="barChart" class="bar-chart" :style="{width: '100%'}"></div>
            </div>
            
            <div class="chart-pie-out">
              <div class="chart-pie-title">
              当前统计年度  <div class="chart-pie-title-text" v-html="selectYear"></div>

            </div>
              <div class="chart-pie">
                <div class="chart-box-right">
                    <div id="pieChart" class="pie-chart" :style="{width: '100%'}"></div>
                </div>
                <div class="chart-box-right">
                    <div id="pieChartforType" class="pie-chart" :style="{width: '100%'}"></div>
                </div>
              </div>
            </div>
        </div>
    </div>
</template>
<script>
// 引入echarts
import echarts from "echarts";
import Vue from "vue";
import { DatePicker } from "element-ui";

Vue.prototype.$echarts = echarts;
export default {
    name: "hello",
    components: {
        [DatePicker.name]: DatePicker
    },
    data() {
        return {
            unUnploadAssetCount: 0,
            uploadingAssetCount: 0,
            uploadedAssetCount: 0,
            uploadedAssetIn24Hours: 0,
            uploadedAssetInOneMonth: 0,
            // 五年数据，包括起始年份和终止年份的全年数据
            startYear: new Date(
                new Date().getTime() - 3 * 365 * 24 * 60 * 60 * 1000
            ).toString(),
            endYear: new Date().toString(),
            selectYear: new Date().getFullYear().toString(),
            barChart: null,
            circleChart: null,
            circleChartForType: null,
            datePickerOptions: {
                disabledDate: time => {
                    return (
                        time > new Date() ||
                        time <
                            new Date(
                                new Date().getTime() -
                                    9 * 365 * 24 * 60 * 60 * 1000
                            )
                    );
                }
            }
        };
    },
    computed: {
        startYearText() {
            return this.startYear.toString().split(" ", 4)[3];
        },
        endYearText() {
            return this.endYear.toString().split(" ", 4)[3];
        }
    },
    mounted() {
        this.getAssetByStatus();
        this.getAssetByYear(); // 这里面拿到数据后已经调用了this.drwaBar
        // this.drawBar([], []);
        this.drawPie(this.selectYear);
        this.drawPieForType(this.selectYear);
        window.onresize = ev => {
            this.barChart.resize();
            this.circleChart.resize();
            this.circleChartForType.resize();
        };
    },
    methods: {
        // 获取顶部五个统计数据
        getAssetByStatus() {
            this.post(`${this.BASEURL}/bdc/baseAssetStat/getAssetStat`,"").then(res => {
                      for (let i in res) {
                            res[i] = res[i] + "";
                            /* 如果数量大等于万 设置单位 */
                            if (res[i].length >= 5) {
                                let val = res[i];
                                val =
                                    val.substring(0, val.length - 4) +
                                    "." +
                                    val.substring(
                                        val.length - 4,
                                        val.length - 3
                                    ) +
                                    '<span style="font-size: 14px">万</span>';
                                res[i] = val;
                            }
                        }

                    this.unUnploadAssetCount = res.unUnploadAssetCount;
                    this.uploadingAssetCount = res.uploadingAssetCount;
                    this.uploadedAssetCount = res.uploadedAssetCount;
                    this.uploadedAssetIn24Hours = res.uploadedAssetIn24Hours;
                    this.uploadedAssetInOneMonth = res.uploadedAssetInOneMonth;
                })
        },
        // 获取按年度统计数据
        getAssetByYear() {
            if (this.startYear == null || this.endYear == null) {
                // alert("请选择时间");
            } else {
                let startYear = this.startYear.toString().split(" ", 4)[3];
                let endYear = this.endYear.toString().split(" ", 4)[3];
                if (startYear > endYear) {
                    alert("时间选择错误，起始年份不能大于终止年份");
                } else {
                    this.post(
                        `${this.BASEURL}/bdc/baseAssetStat/getAssetByYear`,
                        { startYear: startYear, endYear: endYear }
                    ).then(
                        res => {
                            var xAxisData = [];
                            var seriesData = [];
                            res.forEach(element => {
                                // 年份
                                xAxisData.push(element[0]);
                                // 数据数
                                seriesData.push(element[1]);
                            });
                            this.drawBar(xAxisData, seriesData);
                        },
                    );
                }
            }
        },
        //绘制条形图
        drawBar(xAxisData, seriesData) {
            // 基于准备好的dom，初始化echarts实例
            // debugger;
            let barChart = (this.barChart = this.$echarts.init(
                document.getElementById("barChart")
            ));
            // 绘制条形图
            barChart.setOption({
                title: {
                    text: `资产数据按年度统计报表（统计年份: ${this.startYearText}-${this.endYearText}）`,
                    textStyle: {
                        fontSize: 15,
                        fontWeight: 500
                    },
                    subtextStyle: {
                        fontSize: 14
                    },
                    left: "5%"
                },
                grid: {
                    left: "4%",
                    // 宽度包含刻度
                    containLabel: true
                },
                tooltip: {},
                xAxis: {
                    data: xAxisData,
                    axisLine: {
                        // 隐藏刻度线
                        show: false
                    },
                    axisTick: {
                        lineStyle: {
                            // 设置刻度的颜色为白色，视觉上可以做到隐藏
                            color: "#fff"
                        }
                    },
                    axisLabel: {
                        color: "#333",
                        //  显示所有刻度
                        interval: 0
                    }
                },
                yAxis: {
                    minInterval: 1,
                    axisLine: {
                        // 隐藏刻度线
                        show: false
                    },
                    axisTick: {
                        lineStyle: {
                            // 设置刻度的颜色为白色，视觉上可以做到隐藏
                            color: "#fff"
                        }
                    },
                    splitLine: {
                        lineStyle: {
                            type: "dashed"
                        }
                    },
                    axisLabel: {
                        color: "#333"
                    }
                },
                series: [
                    {
                        name: "数据数",
                        type: "bar",
                        data: seriesData,
                        itemStyle: {
                            normal: {
                                color: params => {
                                    let colorList = [
                                        "6F2D75",
                                        "1679DD",
                                        "F9BD16",
                                        "16C1C3"
                                    ];
                                    let index = params.dataIndex % 4;
                                    return "#" + colorList[index];
                                }
                            }
                        }
                    }
                ]
            });
            var that = this;
            // 点击条形图，生产对应的按月度统计饼图
            barChart.on("click", function(params) {
                that.selectYear = params.name;
                that.drawPie(params.name.toString().slice(0, 4));
                that.drawPieForType(params.name.toString().slice(0, 4));
            });
        },
        // 绘制饼图
        drawPie(selectYear) {
            var seriesData = [];

            this.post(
                `${this.BASEURL}/bdc/baseAssetStat/getAssetByMonth`,
                { year: selectYear }
            ).then(
                res => {
                    var legendArr = [];
                    res.forEach(element => {
                        var dataObj = new Object();
                        dataObj["name"] = element[0] + "月";
                        dataObj["value"] = element[1];
                        seriesData.push(dataObj);
                        legendArr.push(element[0] + "月");
                    });
                    let pieChart = (this.circleChart = this.$echarts.init(
                        document.getElementById("pieChart")
                    ));
                    pieChart.setOption({
                            tooltip: {
                            trigger: 'item',
                            formatter: '{b}: {c} ({d}%)'
                        },
                        title: {
                            text: "资产数据按月份统计报表",
                            textStyle: {
                                fontSize: 15,
                                fontWeight: 500
                            },
                            left: "61"
                        },
                        series: {
                            type: "pie",
                            data: seriesData,
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '20',
                                        // fontWeight: 'bold'
                                    }
                                }
                                },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: params => {
                                        // build a color map as your need.
                                        var colorList = [
                                            "#2986CE",
                                            "#B5C334",
                                            "#FCCE10",
                                            "#E87C25",
                                            "#27727B",
                                            "#FE8463",
                                            "#9BCA63",
                                            "#FAD860",
                                            "#F3A43B",
                                            "#60C0DD",
                                            "#D7504B",
                                            "#C6E579",
                                            "#F4E001",
                                            "#F0805A",
                                            "#26C0C0"
                                        ];
                                        return colorList[params.dataIndex];
                                    }
                                }
                            },
                            radius: ["38%","65%"],
                            center: ["45%", "50%"]
                        },
                        legend: {
                            data: legendArr,
                            orient: "horizontal",
                            right: "0",
                            x: "left",
                            y: "bottom",
                            icon: "circle"
                        }
                    });
                }
            );
        },

        // 绘制饼图
        drawPieForType(selectYear) {
            var seriesData = [];

            this.post(
                `${this.BASEURL}/bdc/baseAssetStat/getAssetByType`,
                { year: selectYear }
            ).then(
                res => {
                    var legendArr = [];
                    res.forEach(element => {
                        var dataObj = new Object();
                        dataObj["name"] = element[0];
                        dataObj["value"] = element[1];
                        seriesData.push(dataObj);``
                        legendArr.push(element[0]);
                    });
                    // debugger;
                    let pieChartForType = (this.circleChartForType = this.$echarts.init(
                        document.getElementById("pieChartforType")
                    ));
                    pieChartForType.setOption({
                        tooltip: {
                            trigger: 'item',
                            formatter: '{b}: {c} ({d}%)'
                        },
                        title: {
                            text: "资产数据按数据类型统计报表",
                            textStyle: {
                                fontSize: 15,
                                fontWeight: 500
                            },
                            left: '25',
                        },
                        series: {
                            type: "pie",
                            data: seriesData,
                            avoidLabelOverlap: false,

                            label: {
                                normal: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '20',
                                        // fontWeight: 'bold'
                                    }
                                }
                                },
                                labelLine: {
                                    normal: {
                                        show: false
                                    }
                                },
                            itemStyle: {
                                normal: {
                                    color: params => {
                                        // build a color map as your need.
                                        var colorList = [
                                            "#2986CE",
                                            "#B5C334",
                                            "#FCCE10",
                                            "#E87C25",
                                            "#27727B",
                                            "#FE8463",
                                            "#9BCA63",
                                            "#FAD860",
                                            "#F3A43B",
                                            "#60C0DD",
                                            "#D7504B",
                                            "#C6E579",
                                            "#F4E001",
                                            "#F0805A",
                                            "#26C0C0"
                                        ];
                                        return colorList[params.dataIndex];
                                    }
                                }
                            },
                            radius: ["38%","65%"],
                            center: ["38%", "50%"]
                        },
                        legend: {
                            data: legendArr,
                            orient: "horizontal",
                            right: "0",
                            x: "left",
                            y: "bottom",
                            icon: "circle"
                        }
                    });
                }
            );
        },
        datePickerChanagHandl(val) {
            this.getAssetByYear()
        }
    }
};
</script>
<style lang="scss" scoped>
body {
    background-color: #f4f8fd;
}
.wrap {
    position: relative;
    width: 100%;
    * {
        box-sizing: border-box;
        -moz-box-sizing: border-box;
        -webkit-box-sizing: border-box;
    }
}
.icons-box {
    display: flex;
    display: -webkit-flex;
    justify-content: space-around;
    .icons-item {
        display: flex;
        flex-direction: column;
        width: calc(20% - 20px);
        padding: 16px 12px;
        background: #fff;
        border-radius: 4px;
        .item-top {
            display: flex;
            align-items: center;
        }
    }
}
.icons-item {
    .fon-color1 {
        color: #6f2d75;
    }
    p.fon-color1 {
        background: #6f2d75;
    }
    .fon-color2 {
        color: #1679dd;
    }
    p.fon-color2 {
        background: #1679dd;
    }
    .fon-color3 {
        color: #f9bd16;
    }
    p.fon-color3 {
        background: #f9bd16;
    }
    .fon-color4 {
        color: #16c1c3;
    }
    p.fon-color4 {
        background: #16c1c3;
    }
    .fon-color5 {
        color: #f84f1f;
    }
    p.fon-color5 {
        background: #f84f1f;
    }
}
.item-top {
    .item-top-left {
        .left-img {
            width: 28px;
            height: 28px;
        }
        .locked-img {
            width: 30px;
            height: 27px;
        }
        .left-title {
            font-size: 14px;
        }
        p {
            margin-bottom: 0;
        }
    }
    .item-top-right {
        flex: 1;
        font-size: 40px;
        text-align: right;
    }
}
.item-bottom {
    height: 4px;
    background: #f2f2f2;
    .bottom-line {
        width: 50%;
        background: #2970d5;
        height: 4px;
    }
}
// echart图表样式
.chart {
    display: flex;
    width: 100%;
    margin-top: 20px;
    padding-top: 20px;
    background-color: #fff;
    border-radius: 4px;
    .chart-bar {
        width:40%;
        color: #333;
        flex: 3;
        .chart-search {
            padding-left: 12px;
            .el-date-picker button {
                background-color: #6f2d75;
                color: #fff;
                &:hover {
                    background-color: rgba(111, 45, 117, 0.5);
                    color: #6f2d75;
                }
            }
        }
    }
    .chart-pie {
        flex: 3;
        display: flex;
        display: -webkit-flex;
        width: calc(100% - 600px);
        height: min-content;
        margin-top: 50px;
        // border: 1px solid #ccc;
        // border-radius: 4px;
        .chart-box-right {
            flex: 1;
            margin-top: 20px;
        }
    }
}

.el-date-picker {
    margin: 0 10px;
    width: 33%;
    display: inline-block;
}
.bar-chart {
    margin-top: 30px;
    height: 500px;
    width: 100%;
}
.pie-chart {
    width: 100%;
    height: 400px;
}
@media screen and (max-height: 700px) {
    .bar-chart {
        height: 400px;
    }
}
@media screen and (min-height: 700px) {
    .bar-chart {
        height: 400px;
    }
}
@media screen and (min-height: 900px) {
    .bar-chart {
        height: 500px;
    }
}
@media screen and (min-height: 1100px) {
    .bar-chart {
        height: 700px;
    }
}
@media screen and (min-height: 1300px) {
    .bar-chart {
        height: 800px;
    }
}
@media screen and (min-height: 1700px) {
    .bar-chart {
        height: 900px;
    }
}
.chart .chart-bar{
  margin-top:30px;
  padding-left:20px;

}
.chart .chart-pie {
    -webkit-box-flex: 3;
    display: -webkit-flex;
    height: min-content;
    flex: 3 1 0%;
    // border-width: 1px;
    // border-style: solid;
    // border-color: rgb(204, 204, 204);
    // border-image: initial;
    // border-radius: 4px;
    width:90%;
    text-align: center;
    // margin:0 auto;
    margin-left: 30px;
    margin-top: 10px;
}
 .chart-pie-out {
 
        width:60%;
        height: min-content;
        margin-top: 32px;
        .chart-box-right {
            flex: 1;
            margin-top: 20px;
        }
        
 }


 .chart-pie-title{
   height: min-content;
   text-align: center;
   padding-right: 7.5%;
 }
 .chart-pie-title-text{
   color: #6f2d75;
   font-weight:bold;
   font-size:20px;
   margin-top: 5px;

 }
 .el-date-editor--year{
     width: 100%;
 }
</style>