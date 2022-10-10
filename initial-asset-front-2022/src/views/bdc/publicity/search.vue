<template>
  <div class="div-background" :style="{backgroundImage: makeImg(publicityBackground, 'bg')}">
    <el-row :gutter="10" type="flex" class="top-button">
<!--       <el-button type="primary" class="button-class" @click="toApiPage">API 对接</el-button>
 -->      <el-button type="primary" class="button-class" @click="login">登录</el-button>
    </el-row>
    <el-row type="flex" justify="center">
      <div class="search-box-title">{{publicityTitle}}</div>
    </el-row>
    <el-form :model="formInline">
      <el-row :gutter="40" class="row-top">
        <el-col :span="12" :offset="6">
          <el-input v-model="formInline.searchParam" placeholder="资产类型名称/资产名称"></el-input>
        </el-col>

        <el-col :span="4">
          <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { Table, TableColumn, Select, Option } from "element-ui";
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      formInline: {
        searchParam: ""
      }
    };
  },
  computed: {
    ...mapGetters([
        'publicityTitle',
        'publicityBackground'
    ])
  },
  methods: {
    onSubmit() {
      this.$router.push({path: '/platform/show', query: {searchParam: this.formInline.searchParam,queryType:'keyword'}})
    },
    toApiPage() {
      this.$router.push('api')
    },
    login(){
      this.$router.push({path: '/login'})
    },
    getPublicityInfo(){
      this.get(`${this.BASEURL}/platform/search/findPublicityInfo`).then(res => {
          this.title = res.title;
          this.background = res.background;
      });
    }
  },
  mounted (){
    // 注册键盘事件
    this.keyDown({ 'Enter': this.onSubmit })
  },
  created(){
    this.getPublicityInfo();
  }
};
</script>
<style scoped>
.row-top {
  margin-top: 3%;
}
.div-background {
  height: 100vh;
  margin: 0;
  padding: 0;
  border: 0;
  /* background-image: url(../../../../public/static/img/publicity/background.png); */
  /* background-image: url(http://localhost:8080/platform/search/publicityBackground?attachId=ff80818176b308050176b31125530016); */
  background-repeat: no-repeat;
  background-position: 50% 50%;
  background-attachment: fixed;
  background-size: cover;
}
.search-box-title {
  padding-top: 10%;
  font-weight: bold;
  font-size: 24px;
  color: white;
}
.top-button{
  display: flex;
  flex-direction: row-reverse;
}
.button-class{
  margin: 20px;
}
</style>
