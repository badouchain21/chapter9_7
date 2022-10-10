import Vue from 'vue'

Vue.directive('fixBottom', {
    inserted: function (el, binging) {
        // 获取按钮数据对象
        el.previousElementSibling.style.paddingBottom = binging.value || '44px'
    }
})