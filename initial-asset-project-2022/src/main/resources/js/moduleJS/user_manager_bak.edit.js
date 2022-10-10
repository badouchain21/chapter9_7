{
    editJSONUrl: '/org/employee/employeeedit/editJSON.do',
    // 前端写法
    editButtons: [
        {
            id: 'save',
            name: '保存',
            icon: '#bd-save-o',
            click: function () {
                let _this = this;
                this.$refs.mainForm.validate(function() {
                    let url = `${_this.BASEURL}/org/employee/employeesave/save.do`
                    _this.postFile(url, _this.$refs.mainForm.dataModel, (isSuccess, res) => {
                        if (res.hasOk) {
                            _this.alert('保存成功', 'success')
                            _this.$parent.close()
                        } else {
                            _this.alert(`保存失败！${res.message}`)
                        }
                    }, true)
                })
            }
        }
    ],
    /**
     * 会把返回的对象放到 表单的 dataModel 中，根据编辑页的不同，传入的参数可能不一样，
     * 例如：树形列表的编辑页传入的参数为树节点的数据
     */
    initDataModel: function (data) {
        console.log('tree', data)
        return {
            departmentId: data.id,
            departmentName: data.name
        }
    }
}