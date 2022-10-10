{
	editButtons: [{
        id: 'save',
        name: '保存',
        icon: 'icon iconfont bd-save-o',
        click: function () {
            let _this = this;
            this.$refs.mainForm.validate(function () {
                let url = `${_this.BASEURL}/interfaceDetailSave/saveIncludeFile.do?mdCode=${_this.module.code}`
                _this.postFile(url, _this.$refs.mainForm.dataModel, (isSuccess, res) => {
                    if (res.hasOk) {
                        _this.alert('保存成功', 'success')
                        _this.$parent.close()
                    } else {
                        _this.alert(`保存失败！${res.message}`)
                    }
                }, true)
            });
        }
    }]
}