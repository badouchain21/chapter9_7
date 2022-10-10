<!--
  日期时间选择器
  JSON 配置格式：{"id":"createTime", "name":"创建时间", "selectType":5, "isExtended":false, "valueText":"全部", "formate":"yyyy-MM-dd", "value":""}
-->
<template>
    <div class="search-value">
        <m-search-option
            ref="datePopover"
            :name="selector.name"
            :chooseText="showText"
            :isShowClear="false"
            width="400"
            trigger="click"
            :useShowMethod="true"
            :isPopoverShow="showPopover"
            @show="echoData">
            <template v-slot:panel>
                <el-radio v-model="tmpData.dateType" label="0" class="font-12 marB-5">
                    <el-date-picker
                        v-model="tmpData.startTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
                    </el-date-picker>至
                    <el-date-picker
                        v-model="tmpData.endTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-radio>
                <el-radio v-model="tmpData.dateType" label="1" class="font-12 marB-5">
                    在过去的
                    <el-input type="text" v-model="tmpData.afterTime"></el-input>
                    <el-dropdown>
                        <span class="el-dropdown-link">
                            {{tmpData.afterDateMeas}}
                            <i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item
                                v-for="item in measurements"
                                :key="item.text"
                                @click.native="changeMeasurement('after', item.text)">
                                {{item.text}}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    之内
                </el-radio>
                <el-radio v-model="tmpData.dateType" label="2" class="font-12 marB-5">
                    超过
                    <el-input type="text" v-model="tmpData.beforeTime"></el-input>
                    <el-dropdown>
                        <span class="el-dropdown-link">
                            {{tmpData.beforeDateMeas}}
                            <i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item
                                v-for="item in measurements"
                                :key="item.text"
                                @click.native="changeMeasurement('before', item.text)">
                                {{item.text}}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    之前
                </el-radio>
                <el-button type="primary" size="small" @click="search">
                    <bd-icon name="search"></bd-icon>搜索
                </el-button>
            </template>
        </m-search-option>

    </div>
</template>

<script>
import {
    Dropdown,
    DropdownMenu,
    DropdownItem,
    Popover,
    DatePicker,
    Radio
} from "element-ui"
import MSearchOption from '@/components/frame/Common/MSearchOption'

export default {
    name: "s-date",
    components: {
        [Dropdown.name]: Dropdown,
        [DropdownMenu.name]: DropdownMenu,
        [DropdownItem.name]: DropdownItem,
        [Popover.name]: Popover,
        [DatePicker.name]: DatePicker,
        [Radio.name]: Radio,
        MSearchOption
    },
    props: {
        selector: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            showText: "全部",
            measurements: [
                { value: "d", text: "天" },
                { value: "w", text: "周" },
                { value: "month", text: "个月" },
                { value: "y", text: "年" }
            ],
            showPopover: false,
            /*
             * tmpData 与 effectiveData 中的 dataType 类型如下：
             * 0 为日期区间搜索 如：在 2019-02-01 到 2019-02-14 之间
             * 1 在过去 xx [天，周，个月，年] 之内
             * 2 超过 xx [天，周，个月，年] 之前
             */
            // 与弹出框中的输入进行数据绑定
            tmpData: {
                dateType: "0",
                startTime: "",
                startTimeFormat: "",
                endTime: "",
                endTimeFormat: "",
                afterDateMeas: "天",
                afterTime: "",
                beforeDateMeas: "天",
                beforeTime: ""
            },
            // 存储真实搜索的数据，弹出框的搜索按钮，才会把值放到这里
            effectiveData: {
                dateType: "",
                startTime: "",
                endTime: "",
                afterDateMeas: "",
                afterTime: "",
                beforeDateMeas: "",
                beforeTime: ""
            }
        }
    },
    methods: {
        changeMeasurement (type, value) {
            if (type === "after") {
                this.tmpData.afterDateMeas = value
            } else {
                this.tmpData.beforeDateMeas = value
            }
        },
        search () {
            this.$refs.datePopover.temp_isPopoverShow = false
            if (!this.isEffectiveData()) {
                this.effectiveData.dateType = ""
                this.setShowText()
                return
            }
            this.effectiveData.dateType = this.tmpData.dateType;
            if (this.tmpData.dateType === "0") {
                this.effectiveData.startTime = this.tmpData.startTime || "";
                this.effectiveData.endTime = this.tmpData.endTime || "";
            } else if (this.tmpData.dateType === "1") {
                this.effectiveData.afterTime = this.tmpData.afterTime;
                this.effectiveData.afterDateMeas = this.tmpData.afterDateMeas;
            } else if (this.tmpData.dateType === "2") {
                this.effectiveData.beforeTime = this.tmpData.beforeTime;
                this.effectiveData.beforeDateMeas = this.tmpData.beforeDateMeas;
            }
            this.setShowText();
            this.$emit("change");
        },
        setShowText () {
            if (this.effectiveData.dateType === "0") {
                this.showText = `在${this.effectiveData.startTime}-${this.effectiveData.endTime}之间`;
            } else if (this.effectiveData.dateType === "1") {
                this.showText = `在过去的${this.effectiveData.afterTime}${this.effectiveData.afterDateMeas}之内`;
            } else if (this.effectiveData.dateType === "2") {
                this.showText = `超过${this.effectiveData.beforeTime}${this.effectiveData.beforeDateMeas}之前`;
            } else {
                this.showText = "全部";
            }
        },
        /**
         * 判断用户是否填写了数据，true 是，false 否
         * @returns {Boolean} 路由对象
         */
        isEffectiveData () {
            // dataType为0时，开始时间和结束时间只要有一个有数据，就是有效
            if (
                this.tmpData.dateType === "0" &&
                (this.tmpData.startTime || this.tmpData.endTime)
            ) {
                return true;
            } else if (
                this.tmpData.dateType === "1" &&
                this.tmpData.afterTime
            ) {
                return true;
            } else if (
                this.tmpData.dateType === "2" &&
                this.tmpData.beforeTime
            ) {
                return true;
            }
            return false;
        },
        /**
         * 恢复 tmpData 的初始值
         */
        setTmpDataToDefault () {
            this.tmpData.dateType = "0";
            this.tmpData.startTime = "";
            this.tmpData.endTime = "";
            this.tmpData.afterDateMeas = "天";
            this.tmpData.afterTime = "";
            this.tmpData.beforeDateMeas = "天";
            this.tmpData.beforeTime = "";
        },
        setEffectiveDataToDefault () {
            this.effectiveData.dateType = "";
            this.effectiveData.startTime = "";
            this.effectiveData.endTime = "";
            this.effectiveData.afterDateMeas = "";
            this.effectiveData.afterTime = "";
            this.effectiveData.beforeDateMeas = "";
            this.effectiveData.beforeTime = "";
        },
        echoData () {
            this.setTmpDataToDefault();
            // 将有效值设置到tmpData中
            if (this.effectiveData.dateType === "0") {
                this.tmpData.dateType = this.effectiveData.dateType;
                this.tmpData.startTime = this.effectiveData.startTime;
                this.tmpData.endTime = this.effectiveData.endTime;
            } else if (this.effectiveData.dateType === "1") {
                this.tmpData.dateType = this.effectiveData.dateType;
                this.tmpData.afterTime = this.effectiveData.afterTime;
                this.tmpData.afterDateMeas = this.effectiveData.afterDateMeas;
            } else if (this.effectiveData.dateType === "2") {
                this.tmpData.dateType = this.effectiveData.dateType;
                this.tmpData.beforeTime = this.effectiveData.beforeTime;
                this.tmpData.beforeDateMeas = this.effectiveData.beforeDateMeas;
            }
        },
        getMeasurementValue(text) {
            for (let key in this.measurements) {
                if (this.measurements[key].text == text) {
                    return this.measurements[key].value;
                }
            }
        },
        getMeasurementText(value) {
            for (let key in this.measurements) {
                if (this.measurements[key].value == value) {
                    return this.measurements[key].text;
                }
            }
        },
        getSelectorValue() {
            if (this.effectiveData.dateType === "") {
                return;
            }
            let result = { name: this.selector.id, type: "date-query" };
            if (this.effectiveData.dateType === "0") {
                result.value = {
                    dateType: this.effectiveData.dateType,
                    startTime: this.effectiveData.startTime,
                    endTime: this.effectiveData.endTime
                };
            } else if (this.effectiveData.dateType === "1") {
                result.value = {
                    dateType: this.effectiveData.dateType,
                    afterTime: this.effectiveData.afterTime,
                    measurement: this.getMeasurementValue(
                        this.effectiveData.afterDateMeas
                    )
                };
            } else if (this.effectiveData.dateType === "2") {
                result.value = {
                    dateType: this.effectiveData.dateType,
                    beforeTime: this.effectiveData.beforeTime,
                    measurement: this.getMeasurementValue(
                        this.effectiveData.beforeDateMeas
                    )
                };
            }
            return result;
        },
        reset() {
            this.setEffectiveDataToDefault();
            this.setTmpDataToDefault();
            this.showText = "全部";
        },
        /**
         * 设置搜索值
         * @param value
         */
        setValue(val) {
            if (val.dateType == 0 && (val.startTime || val.endTime)) {
                this.effectiveData.dateType = "0";
                this.effectiveData.startTime = val.startTime;
                this.effectiveData.endTime = val.endTime;
            } else if (val.dateType == 1 && val.afterTime) {
                this.effectiveData.dateType = "1";
                this.effectiveData.afterTime = val.afterTime;
                this.effectiveData.afterDateMeas = this.getMeasurementText(
                    val.measurement
                );
            } else if (val.dateType == 2 && val.beforeTime) {
                this.effectiveData.dateType = "2";
                this.effectiveData.beforeTime = val.beforeTime;
                this.effectiveData.beforeDateMeas = this.getMeasurementText(
                    val.measurement
                );
            }
            this.setShowText();
            this.echoData();
        },
        getSelectorInfo() {
            return this.selector;
        }
    }
};
</script>

<style scoped>
.el-popover .el-date-editor.el-input {
    width: 40% !important;
}

.el-popover .el-input {
    width: 20%;
}
</style>
