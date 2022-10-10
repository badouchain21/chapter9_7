{
    // 前端写法
	editButtons: [
		{
	        id: 'save',
	        name: '保存',
	        icon: 'icon iconfont bd-save-o',
	        click: function () {
	          let url = `${this.BASEURL}/cfg/syscfgsafesave/save.do?mdCode=${this.module.code}`
	          this.postFile(url, this.$refs.mainForm.dataModel, (isSuccess, res) => {
	            if (res.hasOk) {
	              this.alert('保存成功', 'success')
	            } else {
	              this.alert(`保存失败！` + res.message)
	            }
	          }, true)
	        }
	      }, 
        {id: "close", isHide: true}
    ],
    
    editJSONUrl: `/cfg/syscfgsafeedit/editJSONFront.do`
}