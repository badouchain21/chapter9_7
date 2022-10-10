{
     listDataUrl: function() {
          return `${this.BASEURL}/api/intermanager/interfacelogger/interfaceloggerlist/listJSON?mdCode=${this.module.code}`
     },
     delDefalutButtons:[{id:"delete"},{id:"add"},{id:"edit"}],
     renderColumn: {
          isSuccess: {
               // 格式化参数
               formatter : function (row, column, cellValue, index, vue) {
                    if (row.isSuccess != null && row.isSuccess != "" && row.isSuccess != undefined) {
                         if (row.isSuccess == 0){
                              return `<font color="green">成功</font>`
                         } else if (row.isSuccess == 1){
                              return `<font color="red">失败</font>`
                         }
                    }else {
                         return `-`
                    }
               }
          }
     }
}
