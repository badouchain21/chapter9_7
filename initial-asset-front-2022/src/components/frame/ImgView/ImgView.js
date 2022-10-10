import Vue from 'vue'

const Constructor = Vue.extend(require('./ImgView.vue').default);

export default function (attachInfo) {
    let component = new Constructor({
        data: {
            attachId: attachInfo.attachId,
            url: attachInfo.url
        }
    }).$mount()
    document.querySelector('body').appendChild(component.$el)
}
