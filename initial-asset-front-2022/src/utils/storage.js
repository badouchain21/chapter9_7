const storage = {
    setItem: function (key, val) {
        sessionStorage.setItem(key, val)
    },
    getItem: function (key) {
        return sessionStorage.getItem(key)
    },
    setObj: function (key, obj) {
        let val = JSON.stringify(obj)
        sessionStorage.setItem(key, val)
    },
    getObj: function (key) {
        let obj = sessionStorage.getItem(key)
        return JSON.parse(obj)
    },
    removeItem: function (key) {
        sessionStorage.removeItem(key)
    },
    clear: function () {
        sessionStorage.clear()
    }
}
export default storage