import Vue from 'vue'
/**
 * 颜色选择器渲染组件
 * Version v1.0
 * @auth cjb
 * @param $
 * 使用方法
 * $('#挂载对象').colorSelect({
 * 		container : $('#存放值的容器'),
 * 		imgURL : '图片URL' //不传入该参数时则使用默认值
 * });
 */
(function ($) {
  $.fn.extend({
    colorSelect: function (options) {
      var colorSelect = ColorSelect(options, this)
      return colorSelect
    }
  })

  var ColorSelect = function (options, e) {
    var _this = $(e)  // 挂载对象
    var defaults = {
        container: '', // 存放值的input域
        value: '', // 对应值，回显时传入
        width: '60%', // 宽度
        inputClass: '', // input样式
        ulClass: '', // ul样式
        liClass: '', // li样式
        divClass: '', // div样式
        imgClass: '', // img样式,
        iconBackground: '#FFFFFF', // 右上角关闭按钮背景颜色
        submitText: '确定', // 颜色选择框确认按钮文字
        isSingle: false, // 是否单选，默认为多选
        max: 0, // 最多可以选择多少个颜色。为0时表示不限制
        imgURL: Vue.prototype.BASEURL + '/img/colorRing.jpg',	// 颜色选择器图片地址
        vue: null
      },
      defaults = $.extend({}, defaults, options)
		// 增加展示用的面板
    var addShowPanel = function () {
			// 先移除元素中所有的色块装载ul
      $(_this).find('.colorSelect-ul').remove()
      var div = $('<div></div>')
      div.addClass('colorSelect-maxContainer')
			 if (defaults.width.indexOf('%') !== -1 || defaults.width.indexOf('px') !== -1) {
				 div.css('width', defaults.width)
 } else {
            	 div.css('width', defaults.width + 'px')
 }

			// 展示用ul面板
      var ul = $('<ul></ul>')
      ul.addClass('colorSelect-ul')
      if (defaults.ulClass != '') {
        ul.addClass(defaults.ulClass)
      }
      div.append(ul)
			// 选择图标
      var img = $('<img/>')
      img.attr('src', defaults.imgURL)
      img.addClass('colorSelect-img')
      if (defaults.imgClass != '') {
        img.addClass(defaults.imgClass)
      }
      img.click(showSelector)
      div.append(img)
			// h5颜色选择器
      var selector = $('<input type="text" />')
      selector.addClass('colorSelect-selector')
      div.append(selector)
      $(_this).append(div)
			// 获取旧色盘，如果存在色盘则不重新生成色盘
      var oldSelector = $(_this).find('.colpick')
      if (oldSelector.length == 0) {
        selector.colpick({
					// flat : true ,
          layout: 'rgbhex',
          submitText: defaults.submitText,
          polyfill: false,
          onSubmit: imgColorSubmit,
          onShow: function (el) {
            $(el).css('top', '100%')
            $(el).css('left', '50%')
            $(el).css('z-index', '99999')
          }
        })
      }
      if (defaults.value != '') {
        echoData()
      }
    }
		// 回显数据
    var echoData = function () {
      var value = defaults.value
      var ul = $(_this).find('.colorSelect-ul')
      if (value != '' && value != 'null' && value != undefined) {
        ul.empty()
				// 判断是否rgb或者rgba或者时16进制颜色以及是否全英文的字符正则
        var reg = RegExp(/([rR][gG][Bb][Aa]?[\(]((2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),){2}(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),?(0\.\d{1,2}|1|0)?[\)]{1})|(#([0-9a-fA-F]{6}|[0-9a-fA-F]{3}))|([a-zA-Z]+)/, 'g')
				// 判断是否符合规则
        var colors = value.match(reg)
        var colorNum = colors.length
        for (var key = 0; key < colorNum; key++) {
          var color = colors[key]
          var li = addShowLi(color)
          ul.append(li)
        }
      }
    }
		// 增加展示用的li
    var addShowLi = function (color) {
        var li = $('<li></li>')
        li.addClass('colorSelect-li')
        if (defaults.liClass != '') {
          li.addClass(defaults.liClass)
        }
        var span = $('<span></span>')
        var icon = $('<i class="menu-icon bd bd-times-circle-o"></i>')
        icon.css('background', defaults.iconBackground)
        icon.click(removeColor)
        span.append(icon)
        span.addClass('colorSelect-span')
        li.append(span)
			// 颜色块
        var div = $('<div></div>')
        div.addClass('colorSelect-div')
        div.css('background', color)
        if (defaults.divClass != '') {
          div.addClass(defaults.divClass)
        }
        li.append(div)
        div.colpick({
          layout: 'rgbhex',
          submitText: defaults.submitText,
          onSubmit: divColorSubmit,
          onShow: function (el) {
            $(el).css('top', '100%')
            $(el).css('left', '50%')
            $(el).css('z-index', '99999')
          }
        })
			// 对应色值
        var input = $('<input type="text"/>')
        input.keydown(colorInputKeyDown)
        input.addClass('colorSelect-input')
        if (defaults.inputClass != '') {
          input.addClass(defaults.inputClass)
        }
        input.click(colorInputClick)// 给input增加点击事件
        input.blur(checkColorChange)// 给input增加失去焦点事件
        var length
			// 计算长度然后赋值给input
        if (color.indexOf('rgb') != -1) {
          length = color.length * 8
        } else {
          length = color.length * 8
        }
        input.css('width', length + 'px')
        input.val(color)
        li.append(input)
        return li
      },
      removeColor = function () {
        $(this).parent().parent().remove()
        var inputs = $(_this).find('.colorSelect-ul').find('.colorSelect-input')
        setValueToContainer(inputs)
      },
		// 展示颜色选择器
      showSelector = function () {
        $(_this).find('.colorSelect-selector').click()
      },
		// 将值放进对应指定的容器中
      setValueToContainer = function (inputs) {
        if (defaults.container == '') {
          return false
        } else {
				// 获取ul下所有input
          var temp = ''
          var inputLen = inputs.length
          for (var i = 0; i < inputLen; i++) {
            var tempInput = inputs[i]
            if (temp == '') {
              temp = $(tempInput).val()
            } else {
              temp += ',' + $(tempInput).val()
            }
          }
          defaults.container.val(temp)
          defaults.vue.setColor(temp)
        }
      },
		// 选中颜色事件
      selectColor = function () {
			// 获取被选中的颜色值
        var color = $(_this).find('.colorSelect-selector').val()
			// 生成相应的li
        var li = addShowLi(color)
			// 将li添加进展示用的ul中
        $(_this).find('.colorSelect-ul').append(li)
        var inputs = $(_this).find('.colorSelect-ul').find('.colorSelect-input')
        setValueToContainer(inputs)
      },
		// 监听颜色input键盘事件
      colorInputKeyDown = function (e, element) {
        var keyCode = null
			 if (e.which)		         { keyCode = e.which }		     else if (e.keyCode)		         { keyCode = e.keyCode }
			 if (keyCode == 8) {
				 return true
		     } else {
		         if (keyCode == 13) {
		        	 checkColorChange(element)
		         }
		         return true
		     }
      },
		// 监听颜色改变事件，判断时否删除色块
      checkColorChange = function () {
        var val = $(this).val()
			// 如果文本值为空，则移除整个li
        if (val == '') {
          $(this).parent().remove()
        } else {
				// 计算宽度
          var length
          if (val.indexOf('rgb') != -1) { // 如果时rgb或者rgba则乘于6即可。因为’,‘占的位置小
            length = val.length * 8
          } else {
            length = val.length * 8
          }
          $(this).css('width', length + 'px')
          var div = $(this).parent().find('.colorSelect-div')
          if (/^[rR][gG][Bb][Aa]?[\(]((2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),){2}(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),?(0\.\d{1,2}|1|0)?[\)]{1}$/.test(val)) {
					// 验证是否合法rgb颜色
            $(this).parent().find('.colorSelect-div').css('background', val)
            div.show()
          } else if (/^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$/.test(val)) {
					// 验证是否合法16进制颜色
            $(this).parent().find('.colorSelect-div').css('background', val)
            div.show()
          } else if (/^[a-zA-Z]+$/.test(val)) {
					// 是否英文字母颜色
            if (colorMap[val] == '' || colorMap[val] == undefined) {
						// 在对应的颜色集合中找不到该值对应的颜色则移除整个li
              $(this).parent().remove()
            } else {
              $(this).parent().find('.colorSelect-div').css('background', colorMap[val])
              div.show()
            }
          } else {
					// 如果不符合上述三条正则，则说明该值不是一个颜色。直接移除整个li
            $(this).parent().remove()
          }
        }
        var inputs = $(_this).find('.colorSelect-ul').find('.colorSelect-input')
        setValueToContainer(inputs)
      },
		// 颜色输入框点击事件
      colorInputClick = function () {
        $(this).parent().find('.colorSelect-div').hide()
        $(this).select()
      },
		// 判断是否启用单选模式
      isSingle = function (el) {
        var inputs = $(_this).find('.colorSelect-ul').find('.colorSelect-input')
			// 判断是否启用单选功能
        if (defaults.isSingle) {
				// 如果inputs数组长度大于0,说明已存在色值。直接返回
          if (inputs.length != 0) {
            Vue.prototype.alert('单选模式下只允许选择一个颜色')
            return false
          }
        }
        return true
      },
		// 判断是否已达到最大数限制
      isMax = function (el) {
        var inputs = $(_this).find('.colorSelect-ul').find('.colorSelect-input')
			// 判断是否限制最高选择数
        if (defaults.max != 0) {
          if (inputs.length >= defaults.max) {
            Vue.prototype.alert('当前字段最多只能选择' + defaults.max + '个颜色')
            return false
          }
        }
        return true
      },
      divColorSubmit = function (hsb, hex, rgb, el, bySetColor) {
        if (rgb != '' && rgb != null && rgb != undefined) {
          var rgbVal = 'rgb(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ')'
          if (!isExist(rgbVal)) {
            $(el).parent().find('.colorSelect-input').val(rgbVal)
            $(el).parent().find('.colorSelect-input').css('width', rgbVal.length * 8 + 'px')
            $(el).parent().find('.colorSelect-div').css('background', rgbVal)
					// 获取当前ul下所有input
            var inputs = $(_this).find('.colorSelect-ul').find('.colorSelect-input')
            setValueToContainer(inputs)
          }
        }
        $(el).colpickHide()
      },
		// 判断色值是否已存在
      isExist = function (rgb) {
        var isExist = false // 判断颜色是否已存在
        var inputs = $(_this).find('.colorSelect-ul').find('.colorSelect-input')// 获取当前ul下所有input。用于判断是否已存在当前色值
			// 循环当期ul下所有input，判断是否存在该色值。已存在则不新增
        for (var key = 0; key < inputs.length; key++) {
          var input = inputs[key]
          if (input.value == rgb) {
            isExist = true
            break
          }
        }
        return isExist
      },
		// 点击图片颜色选择事件
      imgColorSubmit = function (hsb, hex, rgb, el, bySetColor) {
        if (rgb != '' && rgb != null && rgb != undefined) {
          var rgbVal = 'rgb(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ')'
				// 判断是否启用单选以及最大限制功能
          if (isSingle(el) && isMax(el) && !isExist(rgbVal)) {
            var li = addShowLi(rgbVal)
					// 将li添加进展示用的ul中
            $(_this).find('.colorSelect-ul').append(li)
					// 获取当前ul下所有input
            var inputs = $(_this).find('.colorSelect-ul').find('.colorSelect-input')
            setValueToContainer(inputs)
          }
        }
        $(el).colpickHide()
      }
    addShowPanel()

      var setValue = function (value) {
          defaults.value = value
          echoData()
      }
      return {setValue: setValue}
  }
})(jQuery)

// 对应英文的颜色
var colorMap = {
  'aliceblue': '#F0F8FF',
  'antiquewhite': '#FAEBD7',
  'aqua': '#00FFFF',
  'aquamarine': '#7FFFD4',
  'azure': '#F0FFFF',
  'beige': '#F5F5DC',
  'bisque': '#FFE4C4',
  'black': '#000000',
  'blanchedalmond': '#FFEBCD',
  'blue': '#0000FF',
  'blueviolet': '#8A2BE2',
  'brown': '#A52A2A',
  'burlywood': '#DEB887',
  'cadetblue': '#5F9EA0',
  'chartreuse': '#7FFF00',
  'chocolate': '#D2691E',
  'coral': '#FF7F50',
  'cornflowerblue': '#6495ED',
  'cornsilk': '#FFF8DC',
  'crimson': '#DC143C',
  'cyan': '#00FFFF',
  'darkblue': '#00008B',
  'darkcyan': '#008B8B',
  'darkgoldenrod': '#B8860B',
  'darkgray': '#A9A9A9',
  'darkgreen': '#006400',
  'darkkhaki': '#BDB76B',
  'darkmagenta': '#8B008B',
  'darkolivegreen': '#556B2F',
  'darkorange': '#FF8C00',
  'darkorchid': '#9932CC',
  'darkred': '#8B0000',
  'darksalmon': '#E9967A',
  'darkseagreen': '#8FBC8F',
  'darkslateblue': '#483D8B',
  'darkslategray': '#2F4F4F',
  'darkturquoise': '#00CED1',
  'darkviolet': '#9400D3',
  'deeppink': '#FF1493',
  'deepskyblue': '#00BFFF',
  'dimgray': '#696969',
  'dimgrey': '#696969',
  'dodgerblue': '#1E90FF',
  'firebrick': '#B22222',
  'floralwhite': '#FFFAF0',
  'forestgreen': '#228B22',
  'fuchsia': '#FF00FF',
  'gainsboro': '#DCDCDC',
  'ghostwhite': '#F8F8FF',
  'gold': '#FFD700',
  'goldenrod': '#DAA520',
  'gray': '#808080',
  'green': '#008000',
  'greenyellow': '#ADFF2F',
  'honeydew': '#F0FFF0',
  'hotpink': '#FF69B4',
  'indianred': '#CD5C5C',
  'indigo': '#4B0082',
  'ivory': '#FFFFF0',
  'khaki': '#F0E68C',
  'lavender': '#E6E6FA',
  'lavenderblush': '#FFF0F5',
  'lawngreen': '#7CFC00',
  'lemonchiffon': '#FFFACD',
  'lightblue': '#ADD8E6',
  'lightcoral': '#F08080',
  'lightcyan': '#E0FFFF',
  'lightgoldenrodyellow': '#FAFAD2',
  'lightgray': '#D3D3D3',
  'lightgreen': '#90EE90',
  'lightpink': '#FFB6C1',
  'lightsalmon': '#FFA07A',
  'lightseagreen': '#20B2AA',
  'lightskyblue': '#87CEFA',
  'lightslategray': '#778899',
  'lightsteelblue': '#B0C4DE',
  'lightyellow': '#FFFFE0',
  'lime': '#00FF00',
  'limegreen': '#32CD32',
  'linen': '#FAF0E6',
  'magenta': '#FF00FF',
  'maroon': '#800000',
  'mediumaquamarine': '#66CDAA',
  'mediumblue': '#0000CD',
  'mediumorchid': '#BA55D3',
  'mediumpurple': '#9370DB',
  'mediumseagreen': '#3CB371',
  'mediumslateblue': '#7B68EE',
  'mediumspringgreen': '#00FA9A',
  'mediumturquoise': '#48D1CC',
  'mediumvioletred': '#C71585',
  'midnightblue': '#191970',
  'mintcream': '#F5FFFA',
  'mistyrose': '#FFE4E1',
  'moccasin': '#FFE4B5',
  'navajowhite': '#FFDEAD',
  'navy': '#000080',
  'oldlace': '#FDF5E6',
  'olive': '#808000',
  'olivedrab': '#6B8E23',
  'orange': '#FFA500',
  'orangered': '#FF4500',
  'orchid': '#DA70D6',
  'palegoldenrod': '#EEE8AA',
  'palegreen': '#98FB98',
  'paleturquoise': '#AFEEEE',
  'palevioletred': '#DB7093',
  'papayawhip': '#FFEFD5',
  'peachpuff': '#FFDAB9',
  'peru': '#CD853F',
  'pink': '#FFC0CB',
  'plum': '#DDA0DD',
  'powderblue': '#B0E0E6',
  'purple': '#800080',
  'red': '#FF0000',
  'rosybrown': '#BC8F8F',
  'royalblue': '#041690',
  'saddlebrown': '#8B4513',
  'salmon': '#FA8072',
  'sandybrown': '#F4A460',
  'seagreen': '#2E8B57',
  'seashell': '#FFF5EE',
  'sienna': '#A0522D',
  'silver': '#C0C0C0',
  'skyblue': '#87CEEB',
  'slateblue': '#6A5ACD',
  'slategray': '#708090',
  'snow': '#FFFAFA',
  'springgreen': '#00FF7F',
  'steelblue': '#4682B4',
  'tan': '#D2B48C',
  'teal': '#008080',
  'thistle': '#D8BFD8',
  'tomato': '#FF6347',
  'turquoise': '#40E0D0',
  'violet': '#EE82EE',
  'wheat': '#F5DEB3',
  'white': '#FFFFFF',
  'whitesmoke': '#F5F5F5',
  'yellow': '#FFFF00',
  'yellowgreen': '#9ACD32'
}
