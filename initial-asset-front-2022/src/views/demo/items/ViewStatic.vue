<template>
    <div class="">
        查看页面（静态标签渲染）
        <el-button type="primary" @click="$router.go(-1)">返回demo目录</el-button><br>
        -------------------------------------------------------------------------
        <div class="bg-white m-base-form">
            <div
                class="form-title">
                <span class="sign"></span> 
                <span>静态数据表单</span>
            </div>
            <el-form
                class="base-form view"
                :inline="true"
                :model="dataForm"
                ref="formRef" >
                <!-- label-width="110px"
                v-fixedFormWidth="'110px'" -->
                <div class="secForm">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="common">  
                                <span slot="label" title="普通标签">普通标签</span> 
                                <form-text v-model="dataForm.common" isView></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="16">
                            <el-form-item prop="largePer">  
                                <span slot="label" title="占比变宽标签">占比变宽标签</span>
                                <form-text v-model="dataForm.largePer" isView></form-text>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="password">  
                                <span slot="label" title="密码">密码</span>
                                <form-text v-model="dataForm.password" isView></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="noEdit">  
                                <span slot="label" title="不可编辑">不可编辑</span> 
                                <form-text v-model="dataForm.noEdit" isView></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="numLint">  
                                <span slot="label" title="字数限制">字数限制</span>
                                <form-text v-model="dataForm.numLint" isView></form-text>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="frontEl">  
                                <span slot="label" title="前置元素">前置元素</span> 
                                <form-text v-model="dataForm.frontEl" isView></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="endEl">  
                                <span slot="label" title="后置元素">后置元素</span>
                                <form-text v-model="dataForm.endEl" isView></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="requireEl">  
                                <span slot="label" title="必填标签">必填标签</span>
                                <form-text v-model="dataForm.requireEl" isView></form-text>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="noReVaEl">  
                                <span slot="label" title="非必填检验">非必填检验</span> 
                                <form-text v-model="dataForm.noReVaEl" isView></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="reVaEl">  
                                <span slot="label" title="必填带校验">必填带校验</span>
                                <form-text v-model="dataForm.reVaEl" isView></form-text>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="textarea">  
                                <span slot="label" title="文本域">文本域</span> 
                                <form-textarea v-model="dataForm.textarea" isView></form-textarea>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="radio">  
                                <span slot="label" title="单选框">单选框</span>
                                <form-radio  v-model="dataForm.radio" isView></form-radio>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="checkbox">  
                                <span slot="label" title="复选框">复选框</span>
                                <form-checkbox v-model="dataForm.checkbox" isView></form-checkbox>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="select">  
                                <span slot="label" title="下拉框">下拉框</span>
                                <form-select v-model="dataForm.select" isView></form-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item prop="date1">  
                                <span slot="label" title="年月日-日期框">年月日-日期框</span>
                                <form-date v-model="dataForm.date1" isView></form-date>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="date2">  
                                <span slot="label" title="年-日期框">年-日期框</span>
                                <form-date v-model="dataForm.date2" isView></form-date>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="autoComplete">  
                                <span slot="label" title="输入建议文本">输入建议文本</span>
                                <form-autoComplete v-model="dataForm.autoComplete" isView></form-autoComplete>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item prop="allFile">  
                                <span slot="label" title="文件">文件</span>
                                <form-file
                                    v-model="dataForm.allFile"
                                    name="allFile"
                                    dataType="text"
                                    isView>
                                </form-file>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item prop="pictFile">  
                                <span slot="label" title="图片">图片</span>
                                <form-file
                                    v-model="dataForm.pictFile"
                                    dataType="picture-card"
                                    name="pictFile"
                                    isView>
                                </form-file>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item prop="richText">  
                                <span slot="label" title="富文本">富文本</span>
                                <form-richText v-model="dataForm.richText" isView></form-richText>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
            </el-form>
            <div class="fixBottomBtn" v-fixBottom>
                <el-button 
                    v-for='(i, index) in opBtnList' 
                    :key="index"  
                    v-btnBg="i"
                    type="primary"
                    @click='exeMethod(i)'>
                    <bd-icon :name="i.icon"></bd-icon>
                    {{ i.name }}
                </el-button>
            </div>
        </div>
    </div>
</template>
<script>
    import {
        FormText,
        FormTextarea,
        FormRadio,
        FormCheckbox,
        FormSelect,
        FormDate,
        FormRichText,
        FormFile,
        FormImage,
        FormAutoComplete
    } from '@/components/frame/Common/MForm/components/items/index'
    export default {
        components: {
            FormText,
            FormTextarea,
            FormRadio,
            FormCheckbox,
            FormSelect,
            FormDate,
            FormRichText,
            FormFile,
            FormImage,
            FormAutoComplete
        },
        data () { // 定义页面变量
            return {
                dataForm: {
                    common: '',
                    largePer: '占比变宽标签',
                    hidden: '1222',
                    password: null,
                    noEdit: '不可编辑',
                    numLint: '字数限制',
                    frontEl: '12',
                    endEl: '后置元素',
                    requireEl: '1212',
                    noReVaEl: null,
                    reVaEl: '1065502552@qq.com',
                    textarea: '说到文本域，那这里肯定是有很多文本，这里的文本超级多，如果过要问有多多，有多多就有多多',
                    radio: '单选框1',
                    checkbox: '复选框1， 复选框2',
                    select: '1',
                    date1: '1994-12-24',
                    date2: '2004',
                    autoComplete: '三圈视频',
                    allFile: [
                        { name: 'p1.jpg', url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588946251190&di=a7f9c5a9c9275e528df0e9616fcf7549&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201402%2F20%2F20140220220726_YvyRR.thumb.700_0.jpeg' },
                        { name: 'jquery.js', url: 'http://code.jquery.com/jquery-1.4.1.min.js' }
                    ],
                    pictFile: [
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
                    ],
                    richText: '<p>观察输出结果发现，<span style="color: #ff0000;">number, string, undefined, function，boolean</span>类型均能通过typeof方法判断，</p>'
                },
                opBtnList: [
                    { name: '关闭', icon: 'close', method: 'popPage', params: '' }
                ]
            }
        },
        computed: {},
        methods: { // 定义函数
            popPage () {
                this.$router.back(-1)
            },
            exeMethod (obj) {
                this[obj.method](obj.params)
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