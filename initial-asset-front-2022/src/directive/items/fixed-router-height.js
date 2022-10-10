import Vue from 'vue'
import GlobalConst from '@/service/global-const'

// 计算children-router核心页面body的高度
Vue.directive('setRouterHeight', {
    inserted: function (el, binding) {
        if (!binding.value) {
            return
        }
        setTimeout(function () {
            // 获取children-router页面content元素
            let contentElement = el.parentElement.parentElement
            // 获取除开底部按钮区域的页面内容高度
            let contentHeight = contentElement.clientHeight - parseInt(GlobalConst.footer.height)
            // 设置高度
            contentElement.style.height = `${contentHeight}px`
        })
    }
})