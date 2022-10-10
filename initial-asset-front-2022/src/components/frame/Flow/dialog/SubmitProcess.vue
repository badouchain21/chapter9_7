<template>
    <div class="submit-process-dialog">
        <el-dialog
            width="700px"
            :title="config.title"
            :visible.sync="dialogVisible"
            class="scorpion">
            <div class="cont-table">
                <div class="table-normal">
                    <form id="opinionform" method="post" v-if='config.routeSelect'>
                        <table class="tb_borderNone">
                            <tr>
                                <td class="text-right text-6">流程意见填写：</td>
                                <td class="buttons">
                                    <button type="button" id="btnSaveMyOpinion" @click="saveMyOpinion"
                                            class="btn btn-small btn-blue">保存为我的常用意见
                                    </button>
                                    <button type="button" id="btnDelMyOpinion" @click="delMyOpinion"
                                            class="btn btn-small btn-orange">删除我的常用意见
                                    </button>
                                    <button type="button" id="btnClearOpinion" @click="clearOpinion"
                                            class="btn btn-small btn-default">清空意见
                                    </button>
                                </td>
                            </tr>
                            <tr class="border-top" style="height: 60px;line-height:60px;">
                                <td class="text-right text-6">我的常用意见：</td>
                                <td>
                                    <el-select v-model="myOpinionValue" @change="updateOpinion" placeholder="--请选择--">
                                        <el-option
                                            v-for="item in config.myOpinions"
                                            :key="item.id"
                                            :label="item.opinion"
                                            :value="item.opinion">
                                        </el-option>
                                    </el-select>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-right text-6">意见填写：</td>
                                <td>
                            <textarea cols="100" rows="4" name="opinion" id="opinion"
                                      validate="{maxlength:200}" v-model="config.opinion"> </textarea>
                                </td>
                            </tr>

                            <tr>
                                <td class="text-right text-6">路由：</td>
                                <td>{{config.routeSelect.routeName}}&nbsp;&nbsp;->&nbsp;&nbsp;{{config.routeSelect.nextNodeName}}</td>
                            </tr>

                            <tr v-if="config.routeSelect.hasHandlerSelect "><!-- && config.orgType != 20 -->
                                <td class="text-right text-6">下一环节处理人：</td>
                                <td class="buttons">
                                    <button type="button" id="btnReselected" @click="reselected"
                                            class="btn btn-primary">选择
                                    </button>
                                </td>
                            </tr>
                            <tr v-if="config.routeSelect.hasHandlerSelect"><!--  && config.orgType != 20 -->
                                <td class="text-right text-6">处理人：</td>
                                <td align="left" id="handlerList">
                                    <el-tag
                                            v-for="(hander, index) in handerTag" :key="hander.id"
                                            type="info"
                                            :closable="true"
                                            @close="handerTagClose(index)">
                                        {{hander.name}}
                                    </el-tag>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="handleSubmitClick">提 交</el-button>
    </span>
        </el-dialog>
    </div>
</template>
<script>
    import Vue from 'vue'
    import {Dialog, Select, Option, Checkbox, Tag} from 'element-ui'

    const SEPARATOR_COMMA = ','
    const SEPARATOR_FOR_HANDER_NAME = '、'

    export default {
        name: 'submit-process',
        components: {
            [Dialog.name]: Dialog,
            [Select.name]: Select,
            [Option.name]: Option,
            [Checkbox.name]: Checkbox,
            [Tag.name]: Tag,
        },
        data() {
            return {
                /* 面板参数 */
                config: {},
                dialogVisible: true,
                myOpinionValue: '',
                handlerArr: []
            }
        },
        computed: {
            handerTag() {
                if (!this.config.handler) {
                    return []
                }
                let idArr = this.config.handler.split(SEPARATOR_COMMA)
                let nameArr = this.config.handlerText.split(SEPARATOR_FOR_HANDER_NAME)
                return idArr.map((v, index) => {
                    return {id: v, name: nameArr[index]}
                })
            }
        },
        methods: {
            async init() {
                let config = this.config
                let _config = {}
                if (config.isStart) {
                    _config = await this.beforeStart(config.boId, config.busiType, config.routeId)
                } else {
                    _config = await this.beforeNext(config.routeId, config.worklistId)
                }
                if (_config.routeSelect && _config.routeSelect.handlers) {
                    let idArr = []
                    let nameArr = []
                    _config.routeSelect.handlers.forEach(e => {
                        idArr.push(e.id)
                        nameArr.push(e.name)
                    });
                    this.config.handler = idArr.join(SEPARATOR_COMMA)
                    this.config.handlerText = nameArr.join(SEPARATOR_FOR_HANDER_NAME)
                }
                Object.assign(this.config, _config)
            },
            beforeNext(routeId, worklistId) {
                return new Promise(resolve => {
                    if (!routeId) {
                        resolve({})
                        return
                    }
                    Vue.prototype.get(Vue.prototype.BASEURL + '/process/gtasks/frontsubmit/beforeNext.do',
                        {routeId: routeId, worklistId: worklistId}, (isSuccess, res) => {
                            resolve(res)
                        }, true)
                })
            },
            beforeStart(boId, busiType, routeId) {
                return new Promise(resolve => {
                    if (!boId || !busiType || !routeId) {
                        resolve({})
                        return
                    }
                    Vue.prototype.get(Vue.prototype.BASEURL + '/process/gtasks/frontsubmit/beforeStart.do',
                        {boId: boId, busiType: busiType, routeId: routeId}, (isSuccess, res) => {
                            resolve(res)
                        }, true)
                })
            },
            handleSubmitClick() {
                if (this.config.hasHandlerSelect && (!this.config.handler || this.config.handler.length === 0)) {
                    this.alert('请选择处理人！')
                    return
                }

                this.$confirm('确定提交吗？', '提交', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let action = Vue.prototype.BASEURL + "/process/gtasks/submit/next.do";//提交
                    if (this.config.isStart) {
                        action = Vue.prototype.BASEURL + "/process/gtasks/submit/start.do";//发起
                    }
                    this.post(action, this.config).then(res => {
                        if (res.hasOk) {
                            this.alert('操作成功', 'success')
                            this.callback()
                            this.dialogVisible = false
                        } else {
                            this.alert(`操作失败:${res.message}`)
                        }
                    })
                })
            },
            updateOpinion(val) {
                this.config.opinion = val
            },
            saveMyOpinion() {
                let _opinion = this.config.opinion ? this.config.opinion.trim() : ''

                if (_opinion == '') {
                    this.alert("请先填写意见!")
                    return
                } else if (_opinion.length > 50) {
                    this.alert("我的常用意见长度不可超过50个字符!")
                    return
                }
                Vue.prototype.post(Vue.prototype.BASEURL + "/process/myopinion/personopinionsave/save.do",
                    {opinion: _opinion}, (isSuccess, res) => {
                        if (res && res.hasOk) {
                            this.alert("已保存", 'success')
                            this.config.myOpinions.push({id: res.bean.id, opinion: _opinion})
                        } else {
                            this.alert(res.message)
                        }
                    }, true)
            },
            delMyOpinion() {
                if (!this.myOpinionValue) {
                    this.alert("请先从下面的“我的常用意见”下拉选择要删除的意见项!")
                    return
                }
                let _index = 0;
                for (var i = 0; i < this.config.myOpinions; i++) {
                    if (this.config.myOpinions[i].opinion == this.myOpinionValue) {
                        _index = i;
                        break;
                    }
                }
                Vue.prototype.post(Vue.prototype.BASEURL + "/process/myopinion/personopiniondelete/delete.do",
                    {ids: this.config.myOpinions[_index].id}, (isSuccess, res) => {
                        if (res && res.hasOk) {
                            this.alert("已删除", 'success')
                            this.config.myOpinions.splice(_index, 1)
                            this.myOpinionValue = ''
                        } else {
                            this.alert(res.message)
                        }
                    }, true)
            },
            clearOpinion() {
                this.myOpinionValue = ''
                this.config.opinion = ''
            },
            reselected() {
                this.addressBook({
                    type: '2', // '64',
                    selectType: 20
                }).then(data => {
                    let notExist = id => !this.config.handler || this.config.handler.indexOf(id) === -1

                    if (data && data.length > 0) {
                        let idArr = []
                        let nameArr = []
                        data.filter(e => notExist(e.id)).forEach(e => {
                            idArr.push(e.id)
                            nameArr.push(e.name || e.id)
                        })
                        if (this.config.handler) {
                            this.config.handler += SEPARATOR_COMMA
                            this.config.handlerText += SEPARATOR_FOR_HANDER_NAME
                        }
                        this.config.handler += idArr.join(SEPARATOR_COMMA)
                        this.config.handlerText += nameArr.join(SEPARATOR_FOR_HANDER_NAME)
                    }
                })
            },
            handerTagClose(index) {
                let idArr = this.config.handler.split(SEPARATOR_COMMA)
                let nameArr = this.config.handlerText.split(SEPARATOR_FOR_HANDER_NAME)
                idArr.splice(index, 1)
                nameArr.splice(index, 1)
                this.config.handler = idArr.join(SEPARATOR_COMMA)
                this.config.handlerText = nameArr.join(SEPARATOR_FOR_HANDER_NAME)
            }
        },
        created() {
            this.init()
        }
    }
</script>
<style scoped>
    .submit-process-dialog /deep/ .el-dialog__header {
        background-color: #66b0ea;
    }

    .submit-process-dialog /deep/ .el-dialog__title {
        color: #fff;
    }

    .submit-process-dialog /deep/ .el-dialog__body {
        padding: 10px 0px;
    }

    .submit-process-dialog /deep/ .el-dialog__footer {
        text-align: center;
    }

    .tb_borderNone {
        width: 100%;
    }

    .btn {
        border: 1px solid transparent;
    }

    .btn:hover {
        opacity: 0.8;
    }

    #btnSaveMyOpinion {
        background: #2684ce;
        color: #fff;
    }

    #btnDelMyOpinion {
        background: #f66429;
        color: #fff;
    }

    #btnClearOpinion {
        background-color: #fff;
        color: #666;
        border-color: #dce0e9;
    }

    #btnClearOpinion:hover {
        border-color: #555;
    }

    #opinion {
        width: 350px;
        min-height: 80px;
        resize: none;
        border: 1px solid #DCE0E9;
        border-radius: 4px;
        padding: 0 5px;
    }

    .submit-process-dialog >>> .el-tag {
        margin-left: 0;
        margin-right: 10px;
    }
</style>
