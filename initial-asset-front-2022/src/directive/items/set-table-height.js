import Vue from 'vue'
import GlobalConst from '@/service/global-const'


Vue.directive('setTableHeight', function (el, binding) {
    let height = binding.value
    if (!height) {
        el.style.height = GlobalConst.table.height
    }
    if (height.includes('-')) {
        // 项目头部高度
        let titleHeight = 42
        // 一级菜单高度
        let firstMenuheight = 36
        // 面包屑高度
        let breadcrumbHeight = 36
        // padding，总共4个
        let padding = 10
        // 表格头部高度
        let tableHeaderHeight = 38
        // 分页高度
        let paginationHeight = this.showPagination ? 52 : 0
        // 过渡，避免传入值不精确，偏小时仍能出现效果
        let transitionHeight = 15
        el.style.cssText=`height: calc(100vh - ${
            titleHeight +
            firstMenuheight + 
            breadcrumbHeight + 
            padding * 4 +
            tableHeaderHeight + 
            transitionHeight +
            paginationHeight -
            parseInt(height)   // 该值为负数所以前面需要-号
        }px)`
    } else {
        el.style.height = parseInt(height) - 38 - 52 + 'px'
    }
})

