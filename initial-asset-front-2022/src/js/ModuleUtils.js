import Vue from 'vue'
import { standardList, standardEdit, standardView } from '@/api/frame/module'
import { finalRequest } from '@/service/request'
function loadListCfg (mdCode) {
    return new Promise(resolve => {
        standardList({mdCode: mdCode}).then(res => {
            resolve(res)
        })
    })
}

function loadEditCfg (mdCode) {
    return new Promise(resolve => {
        standardEdit({mdCode: mdCode}).then(res => {
            resolve(res)
        })
    })
}

function loadViewCfg (mdCode) {
    return new Promise(resolve => {
        standardView({mdCode: mdCode}).then(res => {
            resolve(res)
        })
    })
}

function loadScript (jsPath) {
    return new Promise(resolve => {
        if (!jsPath) {
            resolve({})
            return
        }
        let url = Vue.prototype.BASEURL + jsPath
        finalRequest({
            method: 'get',
            url: url,
            params: {}
        }).then(res => {
            let obj = eval('(' + res + ')')
            resolve(obj)
        }).catch(err => {
            console.log(JSON.stringify(err))
        })
    })
}

/**
 * 处理有 valueFieldID, valueFieldText 之类的字段，确保其都有值
 */
function processValueIdAndText (fields) {
    if (!fields || fields.length === 0) {
        return
    }
    fields.filter(f => f.type === 'addressbook').forEach(f => {
        if (!f.valueFieldId) {
            f.valueFieldId = f.name
        }
        if (!f.valueFieldIdSrc) {
            f.valueFieldIdSrc = f.valueFieldId
        }
        if (!f.valueFieldText) {
            f.valueFieldText = f.name
        }
        if (!f.valueFieldTextSrc) {
            f.valueFieldTextSrc = f.valueFieldText
        }
    })
    return fields
}

export default {
    async listModuleCfg (mdCode) {
        let m = await loadListCfg(mdCode)
        m.listJsObj = await loadScript(m.listJs)
        let module = {}
        module.id = m.moduleId
        module.code = m.module
        module.name = m.moduleName
        module.moduleDisplay = JSON.parse(m.moduleDisplay)
        module.listJs = m.listJs
        module.listJsObj = m.listJsObj
        if (m.selectorData) {
            module.selectorData = JSON.parse(m.selectorData)
        }
        if (m.searchCondition) {
            module.searchCondition = JSON.parse(m.searchCondition)
        }
        return module
    },
    async editModuleCfg (mdCode) {
        let m = await loadEditCfg(mdCode)
        let module = {}
        module.id = m.moduleId
        module.code = m.module
        module.name = m.moduleName
        module.editedJs = m.editedJs
        module.editedJsObj = await loadScript(m.editedJs)
        module.childTab = m.childTab
        if (m.moduleFields) {
            module.fields = processValueIdAndText(JSON.parse(m.moduleFields))
        }
        if (m.dic) {
            module.dic = JSON.parse(m.dic)
        }
        return module
    },
    async viewModuleCfg (mdCode) {
        let m = await loadViewCfg(mdCode)
        let module = {}
        module.id = m.moduleId
        module.code = m.module
        module.name = m.moduleName
        module.editedJs = m.editedJs
        module.editedJsObj = await loadScript(m.editedJs)
        module.childTab = m.childTab
        if (m.moduleFields) {
            module.fields = processValueIdAndText(JSON.parse(m.moduleFields))
        }
        if (m.dic) {
            module.dic = JSON.parse(m.dic)
        }
        return module
    }
}
