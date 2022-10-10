<!--
  文本框搜索器
  JSON配置格式：{"id":"title", "name":"标题", "selectType":1, "isExtended":false, "value":""}
-->
<template>
    <div class="search-value">
        <!-- <label>{{selector.name}}:</label> -->
        <el-input
            class="showArea"
            v-model="value" 
            :placeholder="'请输入' + selector.name"
            clearable
            :title="selector.name">
            <!-- 暂时去除change事件@change="change"
            输入框输入后需要手动点击搜索按钮进行搜索 -->
        </el-input>
    </div>
</template>

<script>
export default {
    name: "s-text",
    props: {
        selector: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            value: ""
        }
    },
    methods: {
        change () {
            this.$emit("change")
        },
        getSelectorValue () {
            if (this.value) {
                return {
                    name: this.selector.id,
                    type: "text-query",
                    value: this.value
                }
            }
        },
        reset () {
            this.value = ""
        },
        setValue (value) {
            this.value = value
        },
        getSelectorInfo () {
            return this.selector
        }
    }
};
</script>

<style scoped>
</style>
