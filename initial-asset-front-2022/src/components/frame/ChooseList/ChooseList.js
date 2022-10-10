import Vue from 'vue'

const Constructor = Vue.extend(require('./index.vue').default);

/**
 * 返回默认的 options 配置
 */
function getDefaultConfig() {
    return {
        title: "列表选择框",         // 弹窗标题
        type: '',                   // 显示的数据类型：数据字典、模型、自定义
        field: null,                // 模型字段
        dicData: null,              // 数据字典数据
        defaultParams: [],          // 默认带的查询参数
        singleChoose: false         // 单选或多选 默认为多选
    }
}

export default function (config) {
    let resolve
    function callback(data) {
        resolve(data)
    }

    let component = new Constructor({
        data: {
            config: Object.assign({}, getDefaultConfig(), config),
            callback: callback,
        }
    }).$mount()
    document.querySelector('body').appendChild(component.$el)
    return new Promise(r => resolve = r)
}
