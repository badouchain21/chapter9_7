{
    listDataUrl: function() {
        return `${this.BASEURL}/interfacedetail/listByNetId?mdCode=${this.module.code}&netId=`+this.queryParams.data.netId+`&isOther=`+this.queryParams.data.isOther
    },
    delDefalutButtons:[{id:"add"},{id:"edit"},{id:"view"},{id:"delete"}],
    isShowSelection: true
}
