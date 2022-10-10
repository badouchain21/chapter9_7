/**
 * 编辑页js
 * 	js内容为一个json对象，模型相关逻辑会处理
 * 	editButtons：编辑页的按钮，按钮可重写，只要保持id一致，不配置的话默认是【保存】【取消】按钮
 * 	editJSONUrl：我们都知道进入编辑页要回显数据，请求的地址可以通过editJSONUrl参数设置，不设置的话默认走模型的editJSON方法
 * 	fieldOptions：对编辑页面每个字段做出自定义配置，目前支持的配置为：
 * 			多附件配置：key值为该字段的实体名，value值为一个名为config（固定名）的json对象，对象里面可设置上传文件的限制，如：maxSize（文件最大字节），whiteList（白名单后缀数组），
 * 																														maxFileNameLengh（最大文件名长度），blackList（黑名单后缀数组）
 * 			地址本选完数据后的回调函数：addressBookCallback
 * 			弹窗列表选择数据后的回调函数：chooseListCallback
 * 			弹窗列表获取数据时传过去后台的默认参数：key值为该字段的实体名，value值为一个名为defaultParam（固定名）的json对象，返回查询参数数组
 * 			富文本配置：key值为该字段的实体名，value值为一个名为ueditorCfg（固定名）的json对象，对象里面可设置富文本的一些配置，如：toolbars（富文本工具集，数组类型），initialFrameWidth：编辑框宽度
 * 																									enableAutoSave（是否自动保存），elementPathEnabled（元素路径），wordCount（是否展示字数统计）
 */
{
	/**
	 * 编辑页面上的按钮，比如这样加的话，页面上就有三个按钮，【保存】，【取消】，【我新加入的按钮】
	 */
	editButtons: [
        {
            id: 'myBtn',						// 按钮唯一标识
            isHide: true,						// 是否隐藏，若为true，则按钮隐藏，为false，则按钮显示
            name: '我新加入的按钮',				// 按钮的名称
            icon: 'icon iconfont bd-save-o',	// 按钮的图标
            click: function () {				// 按钮点击事件
                // 处理逻辑
            }
        }
	],
	/**
	 * 我们都知道进入编辑页要回显数据，请求的地址可以通过editJSONUrl参数设置，不设置的话默认走模型BaseCommonEditAction类的editJSON方法
	 */
	// editJSONUrl: `${this.BASEURL}/xxx/xxx/myMethod.do`,
	/**
	 * 编辑页上的字段配置
	 * 这里以字段mulFiles，addressSingleId，chooselistSingleIds，richText为例
	 * 
	 */
    fieldOptions: {
    	/**
    	 * 字段实体名：mulFiles，编辑类型为【多附件】
    	 */
    	mulFiles: {
        	/**
        	 * 编辑类型为【多附件】的字段，可配置这个参数，表示文件的一些配置
        	 * key值为字段实体名，如这里表示我这里的配置都是针对files的，如果你的字段实体名叫myFile，请改成myFile
        	 * value为一个json对象，key值为config，这个config是固定写法，不能改变的，value值为json对象，存放一些参数，具体参数如下：
        	 * 		maxSize:				数字类型，表示允许上传的文件的最大大小
        	 * 		whiteList:				数组类型，表示允许上传的文件的后缀的集合，如这里表示我只能上传后缀为jpg格式的文件
        	 * 		maxFileNameLengh：		数字类型，表示允许上传的文件的名称的最大长度
        	 * 		blackList：				数组类型，表示禁止上传的文件的后缀的集合，如这里表示我禁止上传后缀为exe格式的文件
        	 */
            config: {
                maxSize: 10000000,
                whiteList: ['jpg'],
                maxFileNameLengh: 10000,
                blackList: ['exe']
            }
        },
        /**
         * 字段实体名，addressSinleId，编辑类型为【地址本】
         */
        addressSingleId: {
        	/**
        	 * 编辑类型为【地址本】的字段，可配置这个参数，表示选完地址本后的回调函数，addressBookCallback是固定写法，不能改成其他值
        	 * @param vue	vue对象
        	 * @param model	vue动态绑定的model对象
        	 * @param data	选中的地址本的数据
        	 */
            addressBookCallback: function (vue, model, data) {
            	// 我要将选完的数据怎么处理，在这里写
            }
        },
        /**
         * 字段实体名，chooselistSingleIds，编辑类型为【弹出框列表单选】
         */
        chooselistSingleIds: {
        	/**
        	 * 编辑类型为【弹出框列表单选】的字段，可配置这个参数，表示选完列表数据后的回调函数，chooselistSingleIds是固定写法，不能改成其他值
        	 * @param vue	vue对象
        	 * @param model	vue动态绑定的model对象
        	 * @param data	选中的列表数据
        	 */
        	chooseListCallback: function (vue, model, data) {
        		// 我要将选完的数据怎么处理，在这里写
        	},
        	/**
        	 * 编辑类型为【弹出框列表单选】的字段，可配置这个参数，表示打开弹出框加载数据时默认的查询参数，defaultParam是固定写法，不能改成其他值
        	 * 返回一个数组
        	 * @param vue	vue对象
        	 * @param model	vue动态绑定的model对象
        	 * 
        	 * @return Array
        	 */
        	defaultParam: function (vue, model) {
        		/**
        		 * 这里表示，我弹出框的列表数据，加载时，默认查询字段typeId的值为OTHRE的数据，精确查询
        		 */
        		return [{"name":"typeId","type":"other-query","value":"OTHRE"}]
        	}
        },
        /**字段实体名，richText，编辑类型为【富文本】
         * 
         */
        richText: {
        	/**
        	 * 编辑类型为【富文本】的字段，可配置这个参数，表示加载富文本时的一些配置，对应ueditor的配置，具体可查看ueditor的api，参照网址https://ueditor.baidu.com/doc/
        	 * ueditorCfg是固定写法，不能改成其他值
        	 * toolbars：表示富文本上的工具栏，比如这里表示富文本只显示一个“预览”的按钮
        	 * initialFrameWidth: 编辑器宽度
        	 * enableAutoSave：自动保存，true为开启自动保存，false则关闭自动保存
        	 * elementPathEnabled：是否启用元素路径，如果为true，则显示，即你焦点位置元素的层级：html>div>div>a，设为false则不显示
        	 * wordCount：是否开启字数统计
        	 * 还有其他配置请参考ueditor官方文档
        	 */
        	ueditorCfg: {
        		toolbars: [
        		    ['preview']
        		],
        		initialFrameWidth: '100%',
        		enableAutoSave: false,
        		elementPathEnabled: false,
        		wordCount: false
        	}
        }
    }
}