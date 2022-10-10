/*
 * @Author: your name
 * @Date: 2020-06-17 10:38:16
 * @LastEditTime: 2020-07-24 14:40:08
 * @LastEditors: your name
 * @Description: In User Settings Edit
 * @FilePath: \badouFrameWork-front\src\directive\items\fixed-form-width.js
 */ 
import Vue from 'vue'

Vue.directive('fixedFormWidth', {
    inserted: function (el, binging) {
        // 查看页面不需要设置label固定宽度
        if (!el.className.includes('view')) {
            // 表单元素：设置左侧label固定宽度
            el.querySelectorAll('.el-form-item__label').forEach(i => {
                i.style.width = `${binging.value}`
            })
            // 表单content： 分配除开固定label宽度的剩余空间
            el.querySelectorAll('.el-form-item__content').forEach(i => {
                i.style.width = `calc(100% - ${binging.value})`
            })
        }
    }
})