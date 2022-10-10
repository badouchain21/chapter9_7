<template>
    <div class="padding bg-white">
        js触发打开dialog-表单查看
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------<br/>
        <el-button type="primary" @click="showDialogForm">通过js逻辑弹出表单查看，不需要引入dialog跟form标签</el-button>
    </div>
</template>
<script>
    const formId = 'formHello'
    export default {
        components: {
        },
        data () { // 定义页面变量
            return {
                isShow: false,
                formInfo: {
                    id: formId,
                    title: '表单标题',
                    labelWidth: '110px',
                    columnNum: 3
                },
                formHelloList: [
                    {
                        title: '总标题',
                        list: [
                            { type: 'text', label: '普通标签', name: 'common', value: '1222' },
                            { type: 'text', label: '占比变宽标签', name: 'largePer', value: '占比变宽标签', columnPer: 16 },
                            { type: 'hidden', label: '隐藏元素', name: 'hidden', value: '1222' },
                            { type: 'text', label: '密码', name: 'password', value: null },
                            { type: 'text', label: '不可编辑', name: 'noEdit', value: '不可编辑' },
                            { type: 'text', label: '字数限制', name: 'numLint', value: '字数限制' },
                            { type: 'text', label: '前置元素', name: 'frontEl', value: '12' },
                            { type: 'text', label: '后置元素', name: 'endEl', value: '后置元素' },
                            { type: 'text', label: '必填标签', name: 'requireEl', value: '12' },
                            { type: 'text', label: '非必填检验', name: 'noReVaEl', value: null },
                            { type: 'text', label: '必填带校验', name: 'reVaEl', value: '1065502552@qq.com' }, 
                            { type: 'select', label: '下拉框', name: 'select', value: '1' },
                            { type: 'textarea', label: '文本域', name: 'textarea', value: '说到文本域，那这里肯定是有很多文本，这里的文本超级多，如果过要问有多多，有多多就有多多', columnPer:12 },
                            { type: 'radio', label: '单选框', name: 'radio', value: '单选框' },
                            { type: 'checkbox', label: '复选框', name: 'checkbox', value: '复选框1， 复选框2' },
                            { type: 'date', label: '年月日-日期框', name: 'date1', value: '1994-12-24' },
                            { type: 'date', label: '年-日期框', name: 'date2', value: '2004' },
                            { type: 'autoComplete', label: '输入建议文本', name: 'autoComplete', value: '三圈视频' },
                            { type: 'file', dataType: 'text', label: '文件', name: 'allFile', value: [
                                { name: 'p1.jpg', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946251190&di=a7f9c5a9c9275e528df0e9616fcf7549&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201402%2F20%2F20140220220726_YvyRR.thumb.700_0.jpeg' },
                                { name: 'jquery.js', url: 'http://code.jquery.com/jquery-1.4.1.min.js' }
                            ], columnPer: 24, isOneLine: true },
                            { type: 'file', dataType: 'picture-card', label: '图片', name: 'pictFile', value: [
                                { name: 'p1', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946251190&di=a7f9c5a9c9275e528df0e9616fcf7549&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201402%2F20%2F20140220220726_YvyRR.thumb.700_0.jpeg'},
                                { name: 'p2', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946251190&di=8219a413b9ea2ea001d826ce1d409722&imgtype=0&src=http%3A%2F%2Fwww.jituwang.com%2Fuploads%2Fallimg%2F160405%2F257858-160405000g246.jpg' },
                                { name: 'p3', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946251190&di=6b018ba8577bcf3e5342148b4d8bdc2f&imgtype=0&src=http%3A%2F%2Fpic41.nipic.com%2F20140601%2F18681759_143805185000_2.jpg' },
                                { name: 'p4', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946251189&di=1dfc458ada0c42f0dee9541f1eeba014&imgtype=0&src=http%3A%2F%2Fdesk-fd.zol-img.com.cn%2Ft_s960x600c5%2Fg2%2FM00%2F00%2F0E%2FCg-4WVVB5iWID7fMAB-lSVvDa4oAACnXgEVbFsAH6Vh286.jpg' },
                                { name: 'p5', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946467985&di=63caf3a6623f2a6436c50ee7d04c6a0f&imgtype=0&src=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu%3D1263550701%2C2004494872%26fm%3D214%26gp%3D0.jpg' },
                                { name: 'p6', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946492376&di=4a2473baa039809ba19dc1cef6f7b25c&imgtype=0&src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-c017345dddc4c11b1f6c9c4137d15a41_hd.jpg' },
                                { name: 'p7', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946492376&di=1dad89626f4e26ec49aaaefccf330358&imgtype=0&src=http%3A%2F%2Fbbs.jooyoo.net%2Fattachment%2FMon_1204%2F27_498206_d1356d0ba2ea535.jpg' },
                                { name: 'p8', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946492376&di=e837725518ed8ed64c45219b644697a5&imgtype=0&src=http%3A%2F%2Fpic.haixia51.com%2Fpic%2F%3Fp%3D%2Fqianqianhua%2F20180519%2F04%2F1526673904-wmCAFxRzri.jpg' },
                                { name: 'p9', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946492375&di=71345d3aa552904eec83fb6c51905aeb&imgtype=0&src=http%3A%2F%2Fm.tuniucdn.com%2Ffilebroker%2Fcdn%2Folb%2Fc2%2F4a%2Fc24accbe9b33e7d764df048f8c51b41f_w755_h0_c0_t0.jpg' },
                                { name: 'p10', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946492375&di=1bc60b12c220e316aa6b37d9354416e0&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170312%2F3e3134e6831b448785b1dd6cf0c50ffb_th.jpeg' },
                                { name: 'p11', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946492375&di=4faae2e794925120f019d0f654766064&imgtype=0&src=http%3A%2F%2Fpic23.nipic.com%2F20120827%2F8960079_215244364351_2.jpg' },
                                { name: 'p12', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946492374&di=2fafa318a715c594dca224a6ba0fe3fe&imgtype=0&src=http%3A%2F%2Fpic30.nipic.com%2F20130607%2F3872579_133241474393_2.jpg' },
                                { name: 'p13', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946492374&di=5f2059d510eecf7aff77550a37805ebb&imgtype=0&src=http%3A%2F%2Fimg02.tooopen.com%2Fimages%2F20140711%2Fsy_65874915662.jpg' },
                            ], columnPer:24, isOneLine: true },
                            { type: 'richText', label: '富文本', name: 'richText', value: '<p>观察输出结果发现，<span style="color: #ff0000;">number, string, undefined, function，boolean</span>类型均能通过typeof方法判断，</p>', columnPer: 24, isOneLine: true},
                        ]
                    }, {
                        title: '标题1-2',
                        list: [
                            { type: 'text', label: '普通标签', name: 'common', value: '1222' },
                            { type: 'text', label: '占比变宽标签', name: 'largePer', value: '占比变宽标签', columnPer: 16 }
                        ],
                        labelWidth: '80px',
                        columnNum: 2
                    }
                ],
                btnList: [
                    {
                        name: '取消', click: function () {
                            this.$dialog.close()
                        }
                    }
                ],
            }
        },
        computed: {},
        methods: { // 定义函数
            showDialogForm () {
                this.$dialog.init({
                    type: 'form',
                    // 表单是否为查看状态
                    isView: true,
                    id: formId,
                    // 弹窗内容过多时使用
                    isFixedDialog: true,
                    title: this.formInfo.title,
                    labelWidth: this.formInfo.labelWidth,
                    columnNum: this.formInfo.columnNum,
                    dataList: this.formHelloList,
                    handlerList: this.btnList,
                })
            }
        },
        // 可访问当前this实例
        created () {},
        // 挂载完成，可访问DOM元素
        mounted () {}
    }
</script>
<style lang='scss' scoped>

</style>