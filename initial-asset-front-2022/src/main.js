// 兼容ie浏览器
import '@babel/polyfill'
if (Number.parseInt === undefined) Number.parseInt = window.parseInt;
if (Number.parseFloat === undefined) Number.parseFloat = window.parseFloat;

import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import LightBootstrap from './light-bootstrap-main'
import ElementUI from 'element-ui'
// 去除elementUI样式文件引入
// import 'element-ui/lib/theme-chalk/index.css'

import Config from './config/config.js'
import NewConfig from './config/new_config.js'
// global css
import '@/styles/index.scss' 
import App from './App'
import store from './store'
import router from './router'
import CustomValidateRules from './js/CustomValidateRules/index'
import SelectSearch from '@/components/frame/SelectSearch/index'
import Card from '@/components/frame/Cards/index'
 // permission control
import '@/permission'
// 全局注册dialog js触发
import MDialog from '@/components/frame/Common/MDialog/index.js'
// 引入图标
// 引入字体svg格式文件
// import '@/assets/svgFont/frame/iconfont.js' // 即将废弃 
// import '@/assets/svgFont/business/iconfont.js' // 即将废弃
import '@/icons'
// 引入指令-start
import '@/directive/index'
// 引入指令-end
// 过滤器不需要在main.js中引入，只需在filter/index.js中引用即可全局  --start
// 过滤器不需要在main.js中引入，只需在filter/index.js中引用即可全局  --end

Vue.use(MDialog)
Vue.use(ElementUI, {
    size: 'small'
})
// Vue.prototype.$ELEMENT = { size: 'small', zIndex: 3000 }

Vue.use(Config)
Vue.use(NewConfig)
Vue.use(LightBootstrap)
Vue.use(CustomValidateRules)


// 注册全局组件 --start
const components = [
    SelectSearch,
    Card
]
components.forEach(component => {
    Vue.component(component.name, component)
})
// 注册全局组件 --end

Vue.config.productionTip = false
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})

