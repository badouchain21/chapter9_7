<!--
  年月选择器
  该搜索器暂不能使用
  JSON配置格式：{"id":"date", "formate":"yyyy-MM","valueText":"全部","name":"日期","selectType":10,"isExtended":false,"value":"2017-9"}
-->
<template>
  <div style="display: inline;">
    <div class="selector_name">{{selector.name}}:</div>
    <div class="search-value">
      <el-date-picker
        v-model="value"
        type="month"
        placeholder="选择日期">
      </el-date-picker>
    </div>
  </div>
</template>

<script>
  import {DatePicker} from 'element-ui'

  export default {
    name: "s-year-month",
    components: {
      [DatePicker.name]:DatePicker,
    },
    props: {
      selector: {
        type: Object,
        required: true
      }
    },
    data () {
      return {
        value: ''
      }
    }
  }
</script>

<style scoped>

</style>
