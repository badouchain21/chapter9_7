{
    listDataUrl: function() {
        console.log(this)
        return `${this.BASEURL}/api/intermanage/interfacepermission/interfacepermissionlist/listJSON?mdCode=${this.module.code}&netId=`+this.queryParams.data.netId+`&isOther=`+this.queryParams.data.isOther
    },
    buttons: [
        {
            id: 'editPermission',
            name: '修改访问权限',
            icon: 'edit',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('至少选择一行！')
                    return
                }
                let params = {
                    mdCode: this.module.code,
                    ids: selection.map(e => e.id).join(','),
                    netId:this.queryParams.data.netId
                }
                this.$confirm('你确定要修改访问权限吗？', '请确认', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then((action) => { // 确定的回调
                    this.post(`${this.BASEURL}/api/intermanage/interfacepermission/interfacepermissionedit/updateInterfacaPermission`,  params).then(res => {
                        if (res.hasOk) {
                            this.$refs.list.refresh()
                            this.alert('修改成功', 'success')
                        } else {
                            this.alert('修改失败', 'error')
                        }
                    }, true)
                }).catch((err) => { //取消的回调
                });
            }
        },
        {
            id: 'addPerimission',
            name: '添加权限',
            icon: 'add',
            click: function () {
                // 定义弹窗唯一标识
                let signId = 'modelFormDialog'
                // 定义ModuleList.vue中this作用域
                let that = this
                // 定义弹窗所需按钮
                let btnList = [
                    {
                        name: '添加权限',
                        icon: 'save',
                        click: function () {
                            let modelList = this.getDialogConObj(this.query.data.netId, 3) // 获取模型列表页面作用域，id为dialog属性id的值
                            let selectionList = modelList.selection // 获取多选列表的选择数据

                            if (selectionList.length === 0) {
                                this.alert('至少选择一行！')
                                return
                            }
                            let params = {
                                mdCode: this.mdCode,
                                ids: selectionList.map(e => e.id).join(','),
                                netId: this.query.data.netId
                            }


                            this.post(`${this.BASEURL}/api/intermanage/interfacepermission/interfacepermissionsave/savePermission`, params).then(res => {
                                if (res.hasOk) {
                                    that.$parent.$refs.moduleList.refresh()
                                    this.alert('添加成功', 'success')
                                    this.$dialog.close()
                                } else {
                                    this.alert('添加失败', 'error')
                                }
                            }, true)
                        }
                    }, {
                        name: '取消', click: function () {
                            // 关闭弹窗表单
                            this.$dialog.close()
                        }
                    }
                ]

                let routerObj =  { mdCode: 'netPermission', symbol: 'placeholder', query: {data: {
                            netId: this.queryParams.data.netId,isOther:true
                        }} }
                this.$dialog.init({
                    // 弹窗内容类型
                    type: 'standerList',
                    // 弹窗唯一标识，注意唯一标识与上面出现的函数getModelListObj的参数值保持一致
                    id: this.queryParams.data.netId,
                    // 弹窗标题
                    title: '添加权限',
                    // 弹窗内容过多时使用
                    isFixedDialog: true,
                    // 模型编码
                    mdCode: routerObj.mdCode,
                    // TODO symbol值不知是什么的默认写‘placeholder’
                    symbol: routerObj.symbol,
                    // 默认查询参数对象
                    query: routerObj.query,
                    // 弹窗中按钮组
                    handlerList: btnList

                })
            }
        },
        {
            id: 'delete',
            name: '删除',
            icon: 'delete',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('至少选择一行！')
                    return
                }

                this.$confirm('确定删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = {
                        mdCode: this.module.code,
                        ids: selection.map(e => e.id).join(',')
                    }
                    this.post(`${this.BASEURL}/interfacePermissonDelete/delete.do`, params).then(res => {
                        if (res.hasOk) {
                            this.$refs.list.refresh()
                            this.alert('删除成功', 'success')
                            this.$emit('deleteAfter', selection)
                        } else {
                            this.alert('删除失败', 'error')
                        }
                    })
                })
            }
        }
    ], delDefalutButtons:[{id:"add"},{id:"edit"},{id:"view"}]
}
