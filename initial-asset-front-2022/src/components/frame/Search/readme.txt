搜索器编辑规范

搜索器组件文件存放目录结构：
  search
    -- component
      -- MultipleSelect.vue   多选搜索器 selectType = 0
      -- Text.vue             文本搜索器 selectType = 1
      -- Date.vue             日期搜索器 selectType = 5
      -- SingleSelect.vue     单选搜索器 selectType = 9
      -- YearMonth.vue        年月搜索器 selectType = 10（该搜索器还没完成，2019-2-18）
    -- filter
      -- filter.vue           过滤器的 vue 文件
    -- SearchBar.vue          搜索组件主体，外部使用也是引用这个文件
    -- readme.txt             说明文件，就是本文件

添加一个新的搜索器
  添加一个新的搜索器需要在 component 目录中建立一个对应的 vue 文件，组件名称以"s-"开头，如：s-text
  而搜索器的配置JSON需要符合以下格式：
  {"id":"title", "name":"标题", "selectType":1, "isExtended":false, "value":""}
  id:          搜索器的id，也是作为查询数据的属性名。
  name：       搜索器的label，也就是这个查询属性的中文名称。
  selectType： 搜索器类型，不能与目前已有的搜索器类型相同。
  isExtended： 是否为扩展搜索器，扩展搜索器需要在更多哪里选择之后才出来。
  value：      查询的初始值。
  以上为必须的属性，如果搜索器需要一些额外配置，可自行添加。

  搜索器需要对外提供下面的方法
  getSelectorValue: 方法返回搜索器的值，该值需要符合模型查询条件的写法，
                    即 {name: 字段名, type: 查询类型, value: 查询值}
                    如：
                    getSelectorValue () {
                       return {name: this.selector.id, type: 'text-query', value: this.value}
                    }

  reset: 重置搜索器的值，方法没有参数，只需把搜索器的值恢复到初始状态

  setValue: 设置搜索器的值，接收一个 value 参数，参数格式与 getSelectorValue 方法返回的参数中的 value 属性一致

  getSelectorInfo：获取搜索器配置信息，在父组件初始化子组件时，会传入搜索器配置信息，一般通过一个 名为 selector
                   的 props 接收，如：props: { selector: { type: Object, required: true } }，方法直接返回该值
                   即可

  当搜索器的值发生变化时，应该抛出 change 事件，以通知 SearchBar 搜索器值发生了变化，如：this.$emit('change')

  上面具体实现可参考 Text.vue组件，这个是最简单的搜索器了，方便看


把新的搜索器配置到 SearchBar 中
  在 SearchBar 中引入组件，如：import SText from './component/Text'

  在 SearchBar 中使用组件，components: { [SText.name]: SText }

  在 SearchBar 中有两个 class 为 search-box 的div，第一个存放非扩展的搜索器，isExtended == false，
  第二个存放扩展的搜索器，isExtended == true。
  非扩展搜索器固定写法，在 第一个 class 为 search-box 的div 的 template 标签中写下如下内容：
  <s-text
    ref="selectors"
    v-if="item.selectType === 1"
    :selector="item"
    @change="search">
  </s-text>
  其中：
  ref="selectors" 为固定写法
  v-if="item.selectType === 1" 用于判断是否为对应的搜索器，是的时候才进行渲染
  :selector="item" 传入搜索器配置
  @change="search" 监听change 事件

  扩展搜索器固定写法，在 第二个 class 为 search-box 的div 的 template 标签中写下如下内容：
  <s-text
    ref="extraSelectors"
    v-if="item.selectType === 1"
    v-show="extraCheckedList[index]"
    :selector="item"
    @change="search">
  </s-text>
  其中：
  ref="extraSelectors" 为固定写法
  v-if="item.selectType === 1" 用于判断是否为对应的搜索器，是的时候才进行渲染
  v-show="extraCheckedList[index]" 用于判断是否显示，固定写法
  :selector="item" 传入搜索器配置
  @change="search" 监听change 事件








