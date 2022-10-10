{
	editButtons: [{
        id: 'save',
        name: '保存',
        icon: 'icon iconfont bd-save-o',
        click: function () {
            let _this = this;
            this.$refs.mainForm.validate(function () {
                let url = `${_this.BASEURL}/netInformationSave/save?mdCode=${_this.module.code}`
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
    }, {
        id: 'close',
        name: '关闭',
        icon: 'icon iconfont bd-times',
        click: function () {
            this.$parent.close()
        }
    }],
	renderColumn: {
		// �ֶ�ʵ����
		operationDesc: {
            // ��ʽ������
            formatter : function (row, column, cellValue, index, vue) {
            	if (cellValue) {
            		return `<a style="color: #2B8ADA">${cellValue}</a>`
            	}
            },
            // �����ص��ĺ���
            hander : function (row, value, index, vue) {
            	
            }
		}
	}
}