{
    listDataUrl: function() {
        return `${this.BASEURL}/api/intermanage/interfacedetail/interfacedetaillist/listJSON?mdCode=${this.module.code}`
    },
    delDefalutButtons:[{id:"delete"}],
    renderColumn: {
        status: {
            // 格式化参数
            formatter : function (row, column, cellValue, index, vue) {
                if (row.status != null && row.status != "" && row.status != undefined) {
                    if (row.status == "0"){
                        return `<font color="green">启用</font>`
                    } else if (row.status == "1"){
                        return `<font color="red">停用</font>`
                    }
                }else {
                    return `-`
                }
            }
        },
        type: {
            formatter : function (row, column, cellValue, index, vue) {
                if (row.type != null && row.type != "" && row.type != undefined) {
                    if (row.type == "0"){
                        return `同步`
                    } else if (row.type == "1"){
                        return `异步`
                    }
                }else {
                    return `-`
                }
            }
        }
    }
}
