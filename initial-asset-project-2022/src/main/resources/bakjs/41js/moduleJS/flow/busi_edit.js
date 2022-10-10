{
	editButtons: [
          {
              id: 'save',
              name: '保存',
              icon: 'icon iconfont bd-save-o',
              click: function () {
                  let _this = this;
                  this.$refs.mainForm.validate(function () {
                      let url = `${_this.BASEURL}/busi/busioperationsave/saveservice.do?id=${_this.$refs.mainForm.dataModel.idd}`
                      _this.postFile(url, _this.$refs.mainForm.dataModel, (isSuccess, res) => {
                          if (res.hasOk) {
                              _this.alert('保存成功', 'success')
                              _this.$parent.close(false, res.bean.id)
                          } else {
                              _this.alert(`保存失败！${res.message}`)
                          }
                      }, true)
                  });
              }
          }
	]
}