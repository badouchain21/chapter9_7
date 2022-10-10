<template><div class="padding">
<div class="marginB fixPosition">
<el-input v-model="exampleData.input" placeholder="请输入内容" class="exampleInput"></el-input>
<el-date-picker
                v-model="exampleData.value1"
                type="date"
                placeholder="选择日期">
</el-date-picker>
<el-date-picker
                v-model="exampleData.value1"
                type="date"
                placeholder="选择日期">
</el-date-picker>
            <el-button type="primary">
<bd-icon name="search"></bd-icon>搜索
</el-button>
<el-button type="primary">
  <bd-icon name="reset"></bd-icon>重置
            </el-button>
  </div>
  <m-list></m-list>
  </div>
</template>

<script>
import MList from '@/components/frame/Common/MTable'
export default {
    components: {
        MTable
    },
    data () {
        return {
            exampleData: {
                input: '',
                value1: '2019-12-22'
            }  
        }
    },
    methods: {
        add(){
            let a=1
            let b=2
            let c=a+b
            if(a==b){
                console.log(1)
            }
        }
    }
}
</script>

<style lang="scss" scoped>
    .exampleInput {
        width: 150px;
    }
    .fixPosition {
        display: flex;
        align-items: center;
        & > div {
            & + div {
                margin-left: $space;
            }
            & + .el-button {
                margin-left: $space;
            }
        }
    }
</style>
