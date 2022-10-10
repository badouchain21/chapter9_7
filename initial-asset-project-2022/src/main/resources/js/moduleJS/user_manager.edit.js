{
    // 模型数据更新的时候调用该方法，在该方法中可以放一些页面上只需执行一次的代码
    // 如自定义验证规则, 方法中的 this 指向 ModuleEdit.vue 组件
    init: function(Validator) {
        let id = this.recordId !== 'add' ? this.recordId : ''
        Validator.extend('uniqueLogonId', {
            getMessage: () => '登录账号已经存在',
            validate: (value) => {
                let url =  this.BASEURL + '/org/employee/employeesave/uniqueLoginId.do'
                let params = {
                    logonId: value,
                    id: id
                }
                let resolve
                this.get(url, params).then(res => {
                    resolve({
                        valid: res.hasOk
                    })
                })
                // this.get(url, params, (isSuccess, res) => {
                //     resolve({
                //         valid: res.hasOk
                //     })
                // }, true)
                return new Promise(r => resolve = r)
            }
        })
        Validator.extend('uniqueUserCode', {
            getMessage: () => '编号已经存在',
            validate: (value) => {
                let url =  this.BASEURL + '/org/employee/employeesave/uniquecode.do'
                let params = {
                    code: value,
                    id: id
                }
                let resolve
                this.get(url, params).then(res => {
                    resolve({
                        valid: res.hasOk
                    })
                })
                // this.get(url, params, (isSuccess, res) => {
                //     resolve({
                //         valid: res.hasOk
                //     })
                // }, true)
                return new Promise(r => resolve = r)
            }
        })
    },
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
                    debugger
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
        return {
            departmentId: data.id,
            departmentName: data.name
        }
    }
}