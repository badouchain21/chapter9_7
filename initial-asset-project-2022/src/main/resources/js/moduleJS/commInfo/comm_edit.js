{
    editButtons:[
        {
            id: 'saveAndPush',
            name: '保存并发布',
            icon: '#bd-save-o',
            click: function (btnObj) {
                let _this = this;
                this.$refs.mainForm.validate(function () {
                    // 启用按钮加载状态-兼容4.0需要优先判断该按钮对象
                    if (btnObj && btnObj.isLoad !== undefined) {
                        btnObj.isLoad = true
                    }
                    let url = `${_this.BASEURL}/jdbc/common/basecommonsave/saveIncludeFile.do?mdCode=${_this.module.code}`
                    _this.$refs.mainForm.dataModel.status = 1;
                    _this.postFile(url, _this.$refs.mainForm.dataModel, (isSuccess, res) => {
                        if (res.hasOk) {
                        _this.alert('保存成功', 'success')
                        // 有子表的时候，保持后，设置recordId，不然编辑页子表数据无法正常显示，需要重新进入编辑页才能正常显示
                        _this.$parent.recordId = res.bean.id
                        _this.$router.go(-1)
                    } else {
                        _this.alert(`保存失败！${res.message}`)
                    }
                    // 兼容4.0需要优先判断该按钮对象
                    if (btnObj && btnObj.isLoad !== undefined) {
                        // 请求结束，结束按钮加载状态
                        btnObj.isLoad = false
                    }
                }, true)
                });
            }
        }
    ]
}