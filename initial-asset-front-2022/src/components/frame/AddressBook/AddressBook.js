import Vue from 'vue'

const Constructor = Vue.extend(require('./index.vue').default);

/**
 * 返回默认的 options 配置
 */
function getDefaultConfig() {
    return {
        title: "人员选择", // 弹窗标题
        type: '', // 需要显示的地址本，不填的时候会加载全部，多个使用分号分割 如：2;600
        indexType: '',// 默认展开的地址本,根据type的值来填，不填默认展开第一个
        userPermission: false,  //权限校验，默认关闭
        selectType: null,// 确定数据范围，根据地址本的注册类有所区别，默认地址本注册类（BtAddressBookRegister）
        singleChoose: false, // 单选或多选 默认为多选
        includeParent: 1,// 点击节点时，是否包含父节点在待选框 1包含 0不包含 默认包含
        treeDefaultParams: null, // 树的默认查询参数，数据格式参考搜索组件的数据格式
        optionDefaultParams: null, // 选项的默认查询参数，数据格式参考搜索组件的数据格式
        registerDicCode: null,// 自定义注册bean的名称的数据字典值（如果需要自定义地址本注册类，模仿BtAddressBookRegister，并将改bean的名字保存在数据字典ADDRESS_BOOK_REGISTER中）
        treeLevels: null,// 待选框默认显示的层级 -1：全部  0：当前节点（默认） "2;3"则显示2 3级
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
