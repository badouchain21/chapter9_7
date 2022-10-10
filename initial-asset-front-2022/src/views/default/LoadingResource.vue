<template>
    <div class="textC project-panel">
        <div>
            <img class="_img" src="../../assets/image/frame/loadingResource.gif" alt="">
            <div class="_word">
                项目资源准备中,请耐心等候...
                <!-- <div style="height:2px;width:10px;background:#fff;position:relative"></div> -->
            </div>
        </div>
        
    </div>
</template>

<script>
export default {
    name: 'LoadingResource',
    computed: {
    },
    created () {
        setTimeout(() => {
            this.$router.push({
                name: 'Dashboard'
            })
        }, 2000)
    }
}
</script>

<style lang="scss" scoped>
.project-panel {
    // background: url("../../assets/project/bg.png");
    // background-size:100% 100%;
    // background-attachment: fixed;
    background-color: #2e3138;
    height: 100vh;
    ._img {
        margin-top: 10%;
        width: 300px;
    }
    ._word {
        font-size: 18px;
        font-weight: 600;
        color: #fff;
    }
}

</style>
