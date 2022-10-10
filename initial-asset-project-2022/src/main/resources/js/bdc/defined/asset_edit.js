{
    // 前端写法
	editButtons: [
		{
	        id: 'save',
	        name: '保存',
	        icon: '#bd-save-o',
	        click: function () {
	           let _this = this;
	           this.$refs.mainForm.validate(function () {
	               let url = `${_this.BASEURL}/assetDefined/saveIncludeFile.do?mdCode=${_this.module.code}`
                   _this.postFile(url, _this.$refs.mainForm.dataModel, (isSuccess, res) => {
                    if (res.hasOk) {
                      _this.alert('保存成功', 'success')

                       _this.$router.go(-1)
                    } else {
                      _this.alert(`保存失败！` + res.message)
                    }
                  }, true)
               });
	        }
	      },
    ],
	selectChange: [
		{
			name : 'assetMdCodeChange',
			click : function (event,target) {
				let url = target.BASEURL+`/assetDefined/listMdField?assetMdCode=` + event;
				target.get(url).then(res => {
					target.$parent.$parent.$parent.tempModuleDic.ASSET_MD_FIELD = res
				})
			}
		}
	],
	afterEditJSON : function() {
		let mdCode = this.dataModel.assetMdCode
		let url = this.BASEURL+`/assetDefined/listMdField?assetMdCode=` + mdCode;
		this.get(url).then(res => {
			this.tempModuleDic.ASSET_MD_FIELD = res
		})
	}
}
