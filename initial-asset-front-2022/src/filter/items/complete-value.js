import Vue from 'vue'
import { isStrEmpty } from '@/utils/index'
import GlobalConst from '@/service/global-const'

Vue.filter('completeValue', function (value) {
    if (isStrEmpty(value)) {
        return GlobalConst.view.value
    }
    return value
})