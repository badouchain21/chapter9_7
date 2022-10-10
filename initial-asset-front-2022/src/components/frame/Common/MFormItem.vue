<template>
    <div>
        <el-input 
            v-if="formItem.type==='text'"
            type="text"
            v-model="formItem.value" 
            :placeholder="formItem.placeholder">
        </el-input>
        <el-date-picker
            v-else-if="formItem.type==='date'"
            v-model="formItem.value"
            type="date"
            :placeholder="formItem.placeholder">
        </el-date-picker>
        <el-date-picker
            v-else-if="formItem.type==='daterange'"
            v-model="formItem.value"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
        </el-date-picker>
        <el-select 
            v-else-if="formItem.type==='select'"
            v-model="formItem.value" 
            :placeholder="formItem.placeholder">
            <el-option label="1" value="1"></el-option>
            <el-option label="2" value="2"></el-option>
            <el-option label="3" value="3"></el-option>
        </el-select>
        <el-input 
            v-else
            type="text"
            v-model="formItem.value" 
            :placeholder="formItem.placeholder" 
            :style="formItem.style">
        </el-input>
    </div>
</template>

<script>
export default {
    components: {
    },
    props: {
        formItem: {
            type: Object,
            default: {}
        }
    },
    data () {
        return {
        }
    },
    methods: {
    },
    created () {
    },
    watch: {
    },
}
</script>

<style lang="scss" scoped>

</style>
