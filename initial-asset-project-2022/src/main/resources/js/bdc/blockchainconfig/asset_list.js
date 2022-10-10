{
    listDataUrl: function() {
        return `${this.BASEURL}/blockchainconfig/listJSON?mdCode=${this.module.code}`
    },
    buttons: [
        {
            id: 'editStatus',
            name: '修改权限',
            icon: 'edit',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length === 0) {
                    this.alert('至少选择一行！')
                    return
                }
                let params = {
                    ids: selection.map(e => e.id).join(','),
                }
                this.$confirm('你确定要修改权限吗？', '请确认', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then((action) => { // 确定的回调
                    this.post(`${this.BASEURL}/blockchainconfig/updateStatus`,  params).then(res => {
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
        }
    ],
    renderColumn: {
        status: {
            // 格式化参数
            formatter : function (row, column, cellValue, index, vue) {
                if (row.status != null && row.status != "" && row.status != undefined) {
                    if (row.status == 0){
                        return "<font color='green'>启用</font>"
                    } else if (row.status == 1){
                        return "<font color='red'>禁用</font>"
                    }
                }else {
                    return `-`
                }
            }
        }
    }
}
