import Vue from 'vue'
import { findByClassName } from '@/utils/find-dom'
import globalStyle from '@/styles/global.scss'
Vue.directive('setFormHeight', {
    inserted: function (el, binding) {
        let formElement = findByClassName(el, 'base-form')
        formElement.style.height = `${el.clientHeight -
                                    parseInt(globalStyle.formTitleHeight) -
                                    parseInt(globalStyle.footerHeight)}px`
    }
})

