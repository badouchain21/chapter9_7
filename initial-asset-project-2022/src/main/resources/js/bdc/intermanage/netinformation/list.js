{
    listDataUrl: function() {
        return `${this.BASEURL}/api/intermanager/netinformation/netinformationlist/listJSON?mdCode=${this.module.code}`
    },
    buttons: [
        {
            id: 'permission',
            name: '查看权限',
            icon: 'view',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length != 1) {
                    this.alert('请选择一行！')
                    return
                }
                let listPath = this.$parent.$route.path
                this.$router.push({
                    path: `/module/stander/list/I_INTER_PERMISSION/placeholder`,
                    query: {
                        data: {
                            returnPath: listPath,
                            netId: `${selection[selection.length - 1].id}`,
                            isOther:false
                        }
                    }
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
                    this.post(`${this.BASEURL}/netInformationDelete/delete.do`, params).then(res => {
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
       ],
        renderColumn: {
            isCallInterface: {
                // 格式化参数
                formatter : function (row, column, cellValue, index, vue) {
                    if (row.isCallInterface != null && row.isCallInterface != "" && row.isCallInterface != undefined) {
                        if (row.isCallInterface == 0){
                            return `<font color="green">是</font>`
                        } else if (row.isCallInterface == 1){
                            return `<font color="red">否</font>`
                        }
                    }else {
                        return `-`
                    }
                }
            }
        }
}
