{
    buttons:[],
    listDataUrl: function() {
    return `${this.BASEURL}/bdc/uploadAssetLogList/listJSON?mdCode=${this.module.code}`
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
                        return `<font color="green">已上链</font>`
                    } else if (row.uploadStatus == 3){
                        return `<font color="red">上链失败</font>`
                    }
                }else {
                    return `-`
                }
            }
        }
    }
}
