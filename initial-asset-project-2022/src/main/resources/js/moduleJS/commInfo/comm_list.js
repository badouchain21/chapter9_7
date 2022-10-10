{
    // 按钮
    buttons: [
        {
            id: "revoke",
            name: "撤销",
            icon: "#bd-back",
            isHide: false,
            click : function () {
                let selection = this.$refs.list.selection;
                if (selection.length <= 0) {
                    this.alert('请先选择需要撤销的消息!');
                    return
                }
                let statusFlag = selection.map(e => e.status).join(',')
                if(statusFlag.indexOf("0") != -1 ) {
                    this.alert('未发布消息不能撤回!');
                    return
                }
                this.$confirm('确定撤销吗？', '撤销', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                        let params = {
                            status : 0,
                            ids : selection.map(e => e.id).join(',')
                        };
                        this.post(`${this.BASEURL}/uniapp/comminfo/updateStatus.do`, params).then(res => {
                            if (res.hasOk) {
                            this.$refs.list.refresh();
                            this.alert('撤销成功', 'success')
                        } else {
                            this.alert('撤销失败', 'error')
                        }
                    })
                })
            }
        },
        {
            id: "push",
            name: "发布",
            icon: "#bd-cuiban",
            isHide: false,
            click: function () {
                let selection = this.$refs.list.selection;
                if (selection.length <= 0) {
                    this.alert('请先选择需要发布的消息!');
                    return
                }
                let statusFlag = selection.map(e => e.status).join(',')
                if(statusFlag.indexOf("1") != -1 ) {
                    this.alert('已发布消息不能再发布!');
                    return
                }
                let params = {
                    status : 1,
                    ids : selection.map(e => e.id).join(',')
                };
                this.post(`${this.BASEURL}/uniapp/comminfo/updateStatus.do`, params).then(res => {
                    if (res.hasOk) {
                    this.$refs.list.refresh();
                    this.alert('发布成功', 'success')
                } else {
                    this.alert('发布失败', 'error')
                }})
            }
        },
        {
            id: 'view',
            isHide: true
        }
    ]
}