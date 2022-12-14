import fgInput from './components/frame/Inputs/formGroupInput.vue'
import DropDown from './components/frame/Dropdown.vue'
import Card from './components/frame/Cards/Card.vue'
import {Input, InputNumber} from 'element-ui'
/**
 * You can register global components here and use them as a plugin in your main Vue instance
 */

const GlobalComponents = {
    install (Vue) {
        Vue.component('fg-input', fgInput)
        Vue.component('drop-down', DropDown)
        Vue.component('card', Card)
        Vue.component(Input.name, Input)
        Vue.component(InputNumber.name, InputNumber)
    }
}

export default GlobalComponents
