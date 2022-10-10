{
    delDefalutButtons:[{id:"delete"}],
    listDataUrl: function() {
        return `${this.BASEURL}/bdc/baseAssetList/listJSON?mdCode=${this.module.code}`
    },
    renderColumn: {
        //字段实体名
        hash: {
            // 格式化参数
            formatter : function (row, column, cellValue, index, vue) {
                if (row.jumpUrl != null && row.jumpUrl != "" && row.jumpUrl != undefined) {
                    return `<a class="drill">${cellValue}</a>`
                }else {
                    return `${cellValue}`
                }
            },
            // 点击后回调的函数
            hander : function (row, value, index, vue) {
                console.log(row.jumpUrl )
                if (row.jumpUrl != null && row.jumpUrl != "" && row.jumpUrl != undefined) {
                    window.open(row.jumpUrl);
                }
            }
        },
        uploadStatus: {
            // 格式化参数
            formatter : function (row, column, cellValue, index, vue) {
                if (row.uploadStatus != null && row.uploadStatus != "" && row.uploadStatus != undefined) {
                    if (row.uploadStatus == 0){
                        return `未上链`
                    } else if (row.uploadStatus == 1){
                        return `上链中`
                    } else if (row.uploadStatus == 2){
                        return `已上链`
                    } else if (row.uploadStatus == 3){
                        return `上链失败`
                    }
                }else {
                    return `-`
                }
            }
        }
    },
    buttons:[
        {
            id: 'reupload',
            name: '重新上链',
            icon: 'reset',
            click: function () {
                let selection = this.$refs.list.selection
                if (selection.length == 0) {
                    this.alert('至少选择一行!')
                    return
                }
                var ids = "";
                for (var i = 0;i<selection.length;i++){
                    var row = selection[i];
                    if(row.uploadStatus != 3){
                        this.alert('只能选择失败状态的资产!')
                        return
                    }
                    ids += row.id + ",";
                }
                ids = ids.substring(0,ids.lastIndexOf(","))

                this.$confirm('你确定要重新上链吗？', '请确认', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then((action) => { // 确定的回调
                    let params = {
                        "assetCode" : `${selection[selection.length - 1].assetCode}`,
                        "ids" : ids
                    };
                    this.post(`${this.BASEURL}/bdc/baseAssetList/assetReUpload`, params).then(res => {
                        if (res.hasOk) {
                            this.$refs.list.refresh();
                            this.alert('操作成功', 'success')
                        } else {
                            this.alert(res.message, 'error')
                        }
                    }, true)
                }).catch((err) => { //取消的回调
                });
            }
        }
    ]
}
