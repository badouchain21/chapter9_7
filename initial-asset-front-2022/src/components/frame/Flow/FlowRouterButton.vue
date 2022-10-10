<template>
    <div class="route-but">
        <button
                v-for="r in flowBean.routes" :key="r.id"
                @click="_dealRoute(r.id)"
                class='btn'
                :class="$store.getters['SysStyle/btnClass']">
            <i class="icon iconfont bd-location-arrow-o"></i> {{r.routeName}}
        </button>
    </div>
</template>

<script>
    import FlowUtils from '@/js/FlowUtils'

    export default {
        name: "flow_router_button",
        props: {
            flowInfo: {
                type: Object
            },
            mdCode: {
                type: String
            }
        },
        data() {
          return {
              showBtn: false,
              flowBean: {}
          }
        },
        methods: {
            // 添加流程按钮
            async renderBusiBtn () {
                if (!this.flowInfo.worklistItemId && !this.flowInfo.isEdit) { // 待办已办和修改
                    return
                }

                let _flowBean = {}
                if (this.flowInfo.worklistItemId) { // 从待办进来
                    _flowBean = await FlowUtils.getBusiBtn(this.flowInfo.worklistItemId, null)
                } else if (this.flowInfo.isEdit) { // 点击修改进来
                    let _flow = await FlowUtils.getFlow(this.mdCode, this.flowInfo.boId)
                    if (_flow.flow) {
                        _flowBean = await FlowUtils.getBusiBtn(null, _flow.flow.flowId)
                        _flowBean = Object.assign({}, _flow, _flowBean)
                    }
                }
                this.flowBean = Object.assign({}, this.flowBean, _flowBean)
                this.flowBean.boId = this.flowInfo.boId

                this.flowBean.isStart = !this.flowInfo.worklistItemId

                if (this.flowBean && this.flowBean.routes.length > 0) {
                    this.showBtn = true
                }
            },
            // 处理路由事件
            _dealRoute (routeId) {
                this.submitProcess({
                    boId: this.flowBean.boId,
                    flowId: this.flowBean.flowId,
                    busiType: this.flowBean.busiType,
                    worklistId: this.flowBean.worklistId,
                    routeId: routeId,
                    isStart: this.flowBean.isStart
                }).then(() => {
                    this.$emit('closed')
                })
            }
        },
        watch: {
            flowInfo: {
                deep: true,
                handler() {
                    this.showBtn = false
                    this.$nextTick(() => {
                        this.renderBusiBtn()
                    })
                }
            }
        }
    }
</script>

<style scoped>
    .route-but {
        display: inline-block;
    }
    .route-but .btn {
        margin-right: 10px;
    }
</style>
