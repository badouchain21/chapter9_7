// 全局框架常量
const noValText = '--'
let GlobalConst = {
    form: {
        title: '未命名表单',
        label: '未命名',
        labelWidth: '110px',
        columnNum: 3,
        placeholder: {
            text: '请输入',
            select: '请选择'
        },
        // 一行拆分格子数（24等份）
        gridNum: 24
    },
    // 表单子项
    formItem: {
        // 默认占比
        columnWidth: 1/3
    },
    // 表单查看页面无数据时展示文本
    view: {
        value: noValText
    },
    dialog: {
        title: '未命名模块',
        width: '70%',
        height: '88%',
        marginTop: '15vh'
    },
    textarea: {
        rows: 5,
        maxRows: 999999
    },
    // 级联框
    cascader: {
        // 分割符
        separator: '-'
    },
    // 地址级联
    address: {
        // 关联字段
        relatedField: ['province', 'city', 'area'],
        separator: '-'
    },
    table: {
        // 序号列标签
        indexLabel: '序号',
        // 序号列宽度
        indexWidth: '50',
        // 可选框所占宽度
        selectionWidth: '55',
        // 列最小宽度
        minWidth: '120',
        // 表格默认最小高度
        minHeight: '340px',
        // 表格默认高度
        height: '340px',
        // 表格是否启用斑马纹
        stripe: true,
        // 表格无数据时展示文本
        value: noValText
    },
    pagination: {
        no: 1,
        // 单页请求数据量
        size: 10,
        // 页码字段名
        noName: 'pageNo',
        // 单页数目字段名
        sizeName: 'perPageSize',
        // 下拉选择单页请求数的类型总数，[10,20,30,40]这为4种
        sizeSelectNum: 4,
        // 可选单页请求数的增幅，[5,15,25,35]增幅为10
        sizeAddNum: 10,
        // (暂不使用)可选单页请求数据量数组：目前会根据size，sizeSelectNum，sizeAddNum动态计算返回
        sizes: [30, 40, 50, 60],
        // 一次请求完所有数据默认请求数
        maxSizeNum: 100000,
        // 目前作用于弹窗页面列表，设置允许放置的页码按钮的最大值
        // api规定为大于等于 5 且小于等于 21 的奇数
        maxBtnCount: 5

    },
    // 滚动条
    scrollbar: {
        width: '18px'
    },
    // 数据字典相关
    dic: {
        url: '/common/commoninterface/listDicJSON',
        codeName: 'dicCode',
    },
    // 搜索组件
    searchBar: {
        // 搜索组件块触发方式
        trigger: 'hover',
        // 搜索条件组件默认展示顺序
        sortIndex: 9999999999,
        noDataTip: '暂无数据'
    },
    // 页面底部操作区域
    footer: {
        height: '44px'
    },
    // 富文本
    richText: {
        // 工具栏
        toolbars: ['fullscreen', 'source', '|', 'undo', 'redo', '|',
            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
            'directionalityltr', 'directionalityrtl', 'indent', '|',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
            'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
            'simpleupload', 'insertimage', 'emotion', 'scrawl', 'insertvideo', 'music', 'attachment', 'map', 'gmap', 'insertframe', 'insertcode', 'pagebreak', 'template', 'background', '|',
            'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
            'print', 'preview', 'searchreplace', 'drafts', 'help'
        ]
    },
    // 提醒信息模块
    message: {
        tip: {
            success: '操作成功',
            fail: '操作失败'
        }
    },
    // 图标模块
    icon: {
        // 默认图标前缀
        prefix: '#bd-',
        default: 'default'
    },
    // TODO: 检查有无使用的地方
    theme: {
        // disabled颜色设置：原有色的60%弱化展示
        disableOpacity: 0.6
    }
}
export default GlobalConst