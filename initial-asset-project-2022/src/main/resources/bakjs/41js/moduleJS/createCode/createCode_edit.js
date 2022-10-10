{
    selectChange: [
        {
            name: 'energyCodeChange', //下拉框事件名称.格式为name+Change\
            click: (event, target) => {//触发事件.event为目标code值
                let energyIndicatorsType = event
                let _this = target
                let url = target.BASEURL+'/project/createcode/energyindicatorslist/queryEnergyIndicatorsByType.do'
                let params= {
                    energyIndicatorsType:energyIndicatorsType
                }
                
                target.post(url,params, (isSuccess, res) => {
                	_this.$parent.$parent.moduleDics.EnergyIndicatorsCode = res;
                },true)
            }
        }
    ],
    editButtons: [{
        id: 'save',
        name: '保存',
        icon: 'icon iconfont bd-save-o',
        click: function () {
            let _this = this;
            this.$refs.mainForm.validate(function () {
                let url = `${_this.BASEURL}/project/createcode/energyindicatorssave/saveIncludeFile.do?mdCode=${_this.module.code}`
                _this.postFile(url, _this.$refs.mainForm.dataModel, (isSuccess, res) => {
                    if (res.hasOk) {
                        _this.alert('保存成功', 'success')
                        // 有子表的时候，保持后，设置recordId，不然编辑页子表数据无法正常显示，需要重新进入编辑页才能正常显示
                        _this.$parent.recordId = res.bean.id
                        _this.$parent.close(false, res.bean.id)
                    } else {
                        _this.alert(`保存失败！${res.message}`)
                    }
                }, true)
            });
        }
    }],
	afterEditJSON:function(){
		let url = this.BASEURL+'/project/createcode/energyindicatorslist/queryEnergyIndicatorsByType.do'
      	let params= {
                    energyIndicatorsType:this.dataModel.energyCode
                }
                
        this.post(url,params, (isSuccess, res) => {
             this.moduleDics.EnergyIndicatorsCode = res;
        },true)
	}
}