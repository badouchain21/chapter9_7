<template>
  <div class="background">
    <div class="navigation-bar">
      <div class="navigation-bar-text">{{publicityTitle}}</div>
      <div class="navigation-bar-right"><bd-icon name="people-fill"></bd-icon>
        <div class="navigation-bar-right-block" @click="login">登录</div>
      </div>
    </div>

    <el-form>
      <el-row :gutter="10" class="row-bottom">
        <el-col :span="12" :offset="6">
          <el-input v-model="searchParam" placeholder="资产类型名称/资产名称" @keyup.enter.native="onSubmit"/>
        </el-col>

        <el-col :span="4">
          <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>


    <el-row type="flex" justify="space-around">
      <el-col :span="4">
        <div class="data-box">
          <div class="data-text-box">
            <img class="data-text-box-icon" src="static/img/publicity/comNum.png">
            <div class="data-text">资产类型总数</div>
          </div>
          <div class="data-num1" v-html="assetTypeAmount"></div>
          <hr class="hr1">
        </div>
      </el-col>
      <el-col :span="4">
        <div class="data-box">
          <div class="data-text-box">
            <img class="data-text-box-icon" src="static/img/publicity/goodsAmount.png">
            <div class="data-text">资产总数</div>
          </div>
          <div class="data-num2" v-html="assetAmount"></div>
          <hr class="hr2">
        </div>
      </el-col>
      <el-col :span="4">
        <div class="data-box">
          <div class="data-text-box">
            <img class="data-text-box-icon" src="static/img/publicity/24hours.png">
            <div class="data-text">24小时上链次数</div>
          </div>
          <div class="data-num3" v-html="assetAmount24hours"></div>
          <hr class="hr3">
        </div>
      </el-col>
      <el-col :span="4">
        <div class="data-box">
          <div class="data-text-box">
            <img class="data-text-box-icon" src="static/img/publicity/blockHeight.png">
            <div class="data-text">区块链高度</div>
          </div>
          <div class="data-num4" v-html="blockchainHeight"></div>
          <hr class="hr4">
        </div>
      </el-col>
    </el-row>
    <pagelist
      ref="list"
      title="最新发布"
      :url="getUrl()"
      :columns="getColumns()"
      :queryParams="queryParams"
      sortname="sortname"
      sortorder="sortorder"
      class="pagelist"
      :showSelection=false
      @updateAmount="getAmount"
    ></pagelist>
  </div>
</template>
<script>
import Pagelist from "@/views/bdc/publicity/Pagelist.vue"; 
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      assetTypeAmount: "",
      assetAmount: "",
      assetAmount24hours: "",
      searchParam: "",
      queryParams:{},
      blockchainHeight:0,
    };
  },
  computed: {
    ...mapGetters([
        'publicityTitle'
    ])
  },
  components: {
    Pagelist
  },
  methods: {
    login(){
      this.$router.push({path: '/login'})
    },
    onSubmit() {
      this.queryParams.keyword = this.searchParam
      this.queryParams.assetId = null;
      this.getAmount();
      this.getBlockchainHeight()
      this.$refs.list.isShow = false;
      this.$refs.list.prePageNo = -1;
      this.$refs.list.oldPerPageSize = 0;
      setTimeout(this.$refs.list.reset)
    },
    getUrl() {
      return `${this.BASEURL}/platform/search/listJSON`;
    },
    setQueryParams() {
      this.queryParams = {
        keyword: this.$route.query.searchParam
      };
      this.searchParam = this.$route.query.searchParam
    },
    getColumns() {
      return [
        {
          label: "资产类型名称",
          prop: "assetTypeName",
          minWidth: "70",
          formatter: undefined
        },
        {
          label: "资产名称",
          prop: "assetName",
          minWidth: "70",
          formatter: undefined
        },
        {
          label: "资产登记 hash",
          prop: "hash",
          minWidth: "100",
          formatter: undefined
        },
        {
          label: "登记上链时间",
          prop: "uploadTime",
          minWidth: "",
          formatter: undefined
        }
      ];
    },
    getAmount() {
      this.post(`${this.BASEURL}/platform/search/getAmount`).then(res => {
          this.assetTypeAmount = this.convertData(res.assetTypeAmount);
          this.assetAmount = this.convertData(res.assetAmount);
          this.assetAmount24hours = this.convertData(res.assetAmount24hours);
      });
    },
    getBlockchainHeight(){
       this.post(`${this.BASEURL}/platform/search/getBlockHeight`,{}).then(res => {
          this.blockchainHeight = this.convertData(res);
       });
    },
    convertData (data){
      data = data + "";
      /* 如果数量大等于万 设置单位 */
      if (data.length >= 5) {
          let val = data;
          val =
              val.substring(0, val.length - 4) +
              "." +
              val.substring(
                  val.length - 4,
                  val.length - 3
              ) + '<span style="font-size: 14px">万</span>';
          return val;
      }else{
        return data;
      }
    }
  },
  created() {
    this.setQueryParams();
    this.getAmount();
    this.getBlockchainHeight();
  }
};
</script>

<style scoped>
body{
  background-color: #F4F8FD;
}
.navigation-bar{
  height: 50px;
  background-color: #2970D5;
}
.navigation-bar-text{
  margin-left: 15px;
  color: white;
  font-weight: bolder;
  font-size: 30px;
  display: inline-block;
  margin-top: 0.5%;
}
.navigation-bar-right{
  float: right;
  color: white;
  font-size: 18px;
  padding: 12px 0px 0px 0px;
}
.navigation-bar-right-block{
  margin-right: 20px;
  display: inline;
  cursor: pointer;
}
.data-box {
  width: 100%;
  height: 100px;
  background-color: white;
  border-radius: 10px;
  padding: 10px;
}
.data-text-box-icon {
  width: 32px;
  height: 32px;
}
.data-text-box {
  float: left;
}
.data-num1 {
  font-size: 40px;
  color: blue;
  float: right;
}
.data-num2 {
  font-size: 40px;
  color: red;
  float: right;
}
.data-num3 {
  font-size: 40px;
  color: purple;
  float: right;
}
.data-num4 {
  font-size: 40px;
  color: gold;
  float: right;
}
.hr1 {
  background-color: blue;
  margin-top: 70px;
  margin-right: 100px;
  height: 3px;
  border-radius: 10px;
}
.hr2 {
  background-color: red;
  margin-top: 70px;
  margin-right: 100px;
  height: 3px;
  border-radius: 10px;
}
.hr3 {
  background-color: purple;
  margin-top: 70px;
  margin-right: 100px;
  height: 3px;
  border-radius: 10px;
}
.hr4 {
  background-color: gold;
  margin-top: 70px;
  margin-right: 100px;
  height: 3px;
  border-radius: 10px;
}
.pagelist {
  margin:20px auto;
  width: 95%;
}
.row-bottom{
  margin-top: 30px;
  margin-bottom: 20px;
}
</style>