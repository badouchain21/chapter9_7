{
    toolbarParams: {
        buttons: [
            {id: "add", isHide: true},
            {id: "modify", isHide: true},
            {id: "delete"},
            {id: "view", isHide: true},
            {id: "import", isHide: true},
            {id: "export", isHide: true}]
    },



    // 前端写法
    buttons: [
        {id: "add", isHide: true},
        {id: "edit", isHide: true}
    ],
    defaultParam: {
        /**
         * 返回初始的 defaultSearchParam
         */
        init: function (symbol) {
            if (symbol === 'self') {
                return {defaultSearchParam: '[{"name":"userId","value":"currentUserId()","type":"exact-match"}]'}
            } else {
                return {}
            }
        }
    }
}