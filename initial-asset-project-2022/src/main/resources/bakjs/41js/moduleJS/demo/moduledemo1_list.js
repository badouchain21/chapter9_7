/**
 * 
 * 列表js
 * 	js内容为一个json对象，模型相关逻辑会处理
 * 	buttons：		列表上的按钮，该属性为数组类型，不写则会渲染默认的三个按钮（增删改）
 * 	listDataUrl:	列表上的数据的获取url，不写会默认调用模型的listJSON()方法获取数据
 * 	renderColumn:	列表上字段的格式化，renderColumn为一个json对象，格式为{'字段名': '格式化对象'}
 * 
 */
{
	/**
	 * 这是一个数组
	 * 列表上的按钮，默认有增加（add），修改（edit），删除（delete）
	 * 这里可以重写，只要id一样就行，也可以添加自定义的按钮
	 */
	buttons: [
		{
		    id: 'view',
		    name: '查看',
		    icon: 'icon iconfont bd-edit',
		    click: function () {
		        // 点击按钮时的逻辑处理
		        let selection = this.$refs.list.selection
		        if (selection.length === 0) {
		            this.alert('请选择一行！')
		            return
		        }
		        let listPath = this.$parent.$route.path
		        this.$router.push({
		            path: `/module/view/view/${this.module.code}/${selection[selection.length - 1].id}`,
		            query: {
		                data: {
		                    returnPath: listPath
		                }
		            }
		        })
		    }
		}
	],
	/**
	 * 列表数据自定义获取URL，当这里定义了listDataUrl时，列表的数据会请求下面的地址获取数据，注意数据返回格式，一般是{Rows: [], Total}
	 */
	//listDataUrl:	`${this.BASEURL}/xxx/xxx/myMethod.do`,
	/**
	 * 这是一个json对象，里面key值为字段实体名（不是field_name，是entity_name），value值为一个json对象，格式为{formatter: fn1, hander: fn2}，存放格式化函数和点击后回调的函数，可以理解为点击事件触发的函数
	 * 比如这里我要列表上字段为input的属性，格式化成<a style="color: red">xxxx</a>，并且点击，会alert一条提示信息，可以这么写
	 */
	renderColumn: {
		//字段实体名
		input: {
            // 格式化参数
            formatter : function (row, column, cellValue, index, vue) {
            	if (cellValue) {
            		return `<a style="color: red">${cellValue}</a>`
            	}
            },
            // 点击后回调的函数
            hander : function (row, value, index, vue) {
                vue.alert('点击后会调用这个函数')
            }
		}
	}
}