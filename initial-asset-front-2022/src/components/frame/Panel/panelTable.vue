<template>
  <div>
    <el-dialog append-to-body :title="extraDialogTitle" :visible="dialogAddVisible">
      <el-form
        :model="content"
        label-width="100px"
        label-position="left"
        :rules="contentRules"
        ref="contentForm"
      >
        <el-form-item label="内容名称" prop="contentName">
          <el-input v-model="content.contentName"></el-input>
        </el-form-item>
        <el-form-item label="分组类别" prop="chartsGroup">
          <el-input v-model="content.chartsGroup"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="priority">
          <el-input v-model="content.priority"></el-input>
        </el-form-item>
        <el-form-item label="数据源类型" prop="dataSourceType">
          <el-select v-model="content.dataSourceType" @change="changeDataSourceType">
            <el-option label="报表" value="0"></el-option>
            <el-option label="页面" value="1"></el-option>
            <el-option label="过滤器" value="2"></el-option>
            <el-option label="视频" value="4"></el-option>
          </el-select>
        </el-form-item>
        <div v-if="isReport">
          <el-form-item label="选择报表" prop="reportId">
            <el-select v-model="content.reportId" @change="changeReport">
              <el-option
                v-for="report in reportlist"
                :label="report.name"
                :value="report.id"
                :key="report.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div v-if="isFilter">
          <el-form-item label="选择过滤器" prop="chartSourceId">
            <el-select v-model="content.chartSourceId" @change="changeFilter">
              <el-option
                v-for="filter in filterList"
                :label="filter.name"
                :value="filter.id"
                :key="filter.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div v-if="isReport||isFilter">
          <el-form-item label="图表类型" prop="type">
            <el-select v-model="content.type" @change="changeReportType">
              <el-option
                v-for="type in typelist"
                :label="type.text"
                :value="type.id"
                :key="type.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            v-for="(setting,index) in reportSettings"
            :key="index"
            :label="setting.dimensionName"
          >
            <el-input v-if="setting.type === 'input'" v-model="reportField[setting.dimensionCode]"></el-input>
            <el-input
              v-if="setting.type ==='textarea'"
              type="textarea"
              rows="3"
              v-model="reportField[setting.dimensionCode]"
            ></el-input>
            <el-select
              v-if="setting.type === 'select'"
              v-model="reportField[setting.dimensionCode]"
            >
              <el-option
                v-for="content in setting.contents"
                :label="content.displayName"
                :value="content.fieldName"
                :key="content.fieldName"
              ></el-option>
            </el-select>
            <div v-if="isReport">
              <el-select
                v-if="setting.type === 'multi_select'"
                multiple
                v-model="reportField[setting.dimensionCode]"
              >
                <el-option
                  v-for="content in setting.contents"
                  :label="content.displayName"
                  :value="content.fieldName"
                  :key="content.fieldName"
                ></el-option>
              </el-select>
            </div>
            <div v-if="isFilter">
              <el-select
                v-if="setting.type === 'multi_select' && setting.maxSelectCount === 1"
                v-model="reportField[setting.dimensionCode]"
                @change="genSelectOperation($event, setting.dimensionCode, setting.dimensionName, setting.sqlField)"
              >
                <el-option
                  v-for="content in setting.contents"
                  :label="content.displayName"
                  :value="content.fieldName"
                  :key="content.fieldName"
                ></el-option>
              </el-select>
              <el-select
                v-if="setting.type === 'multi_select' && setting.maxSelectCount === 0"
                multiple
                v-model="reportField[setting.dimensionCode]"
                @change="genSelectOperation($event, setting.dimensionCode, setting.dimensionName, setting.sqlField)"
              >
                <el-option
                  v-for="content in setting.contents"
                  :label="content.displayName"
                  :value="content.fieldName"
                  :key="content.fieldName"
                ></el-option>
              </el-select>
            </div>
          </el-form-item>
          <div>
            <el-form-item
              v-for="(operation,index) in selectOperation"
              :key="index"
              :label="operation.label"
            >
              <el-select v-model="operation.value">
                <el-option label="count" value="count"></el-option>
                <el-option label="sum" value="sum"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div v-if="isPage">
          <el-form-item label="选择页面" prop="pageId">
            <el-select v-model="content.pageId" @change="changePage">
              <el-option
                v-for="page in pagelist"
                :label="page.name"
                :value="page.id"
                :key="page.id"
              ></el-option>
              <el-option label="==手动输入==" value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="页面地址" v-if="isPageURL" prop="pageURL">
            <el-input v-model="content.pageURL"></el-input>
          </el-form-item>
        </div>
        <div v-if="isVideo">
          <el-form-item label="视频地址" prop="videoURL">
            <el-input v-model="content.videoURL"></el-input>
          </el-form-item>
          <el-form-item label="视频宽度" prop="videoWidth">
            <el-input v-model="content.videoWidth"></el-input>
          </el-form-item>
          <el-form-item label="视频高度" prop="videoHeight">
            <el-input v-model="content.videoHeight"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAddVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveContent">确 定</el-button>
      </div>
    </el-dialog>
    <el-button @click="addContent('添加内容')" plain>添加</el-button>
    <el-button @click="editContent('修改内容')" plain>修改</el-button>
    <el-button @click="deleteContent" plain>删除</el-button>
    <!-- sign：使用了el-table -->
    <el-table
        :data="tableData" 
        style="width: 100%;top: 10px;" 
        borderm 
        @selection-change="selections = $event">
        <el-table-column type="selection" width="60"></el-table-column>
        <el-table-column type="index" label="序号" width="100"></el-table-column>
        <el-table-column prop="contentName" label="内容名称" :min-width="minWidth"></el-table-column>
        <el-table-column prop="chartsGroup" label="分组类别" :min-width="minWidth"></el-table-column>
        <el-table-column prop="priority" label="排序" :min-width="minWidth"></el-table-column>
    </el-table>
  </div>
</template>
<script>
import { Dialog, Form, FormItem, Select, Option } from 'element-ui'
import GlobalConst from '@/service/global-const'

export default {
    name: 'panel-table',
    props: [
        'reportlist',
        'typelist',
        'pagelist',
        'filterList',
        'tableData'
    ],
    components: {
        [Dialog.name]: Dialog,
        [Form.name]: Form,
        [FormItem.name]: FormItem,
        [Select.name]: Select,
        [Option.name]: Option
    },
    computed: {
        // 获取表格列最小宽度
        minWidth () {
            return GlobalConst.table.minWidth
        }
    },
    data: function() {
        return {
            dialogAddVisible: false,
            extraDialogTitle: '',
            isReport: false,
            isPage: false,
            isFilter: false,
            isVideo: false,
            chartSourceId: '',
            chartSourceType: '',
            selectOperation: [],
            reportField: {},
            reportSettings: [],
            isPageURL: false,
            selections: [],
            content: {},
            contentRules: {
                contentName: [
                    { required: true, message: '请填写内容名称', trigger: 'blur' }
                ],
                chartsGroup: [
                    { required: true, message: '请选择分组类别', trigger: 'blur' }
                ],
                priority: [
                    { required: true, message: '请选择排序', trigger: 'blur' }
                ],
                dataSourceType: [
                    { required: true, message: '请选择数据源类型', trigger: 'change' }
                ],
                reportId: [
                    { required: true, message: '请选择报表', trigger: 'change' }
                ],
                chartSourceId: [
                    { required: true, message: '请选择过滤器', trigger: 'change' }
                ],
                type: [
                    { required: true, message: '请选择图表类型', trigger: 'change' }
                ],
                pageId: [
                    { required: true, message: '请选择页面', trigger: 'change' }
                ],
                pageURL: [
                    { required: true, message: '请填写页面地址', trigger: 'blur' }
                ],
                videoURL: [
                    { required: true, message: '请填写视频地址', trigger: 'blur' }
                ],
                videoWidth: [
                    { required: true, message: '请填写视频宽度', trigger: 'blur' }
                ],
                videoHeight: [
                    { required: true, message: '请填写视频高度', trigger: 'blur' }
                ]
            },
        }
    },
    methods: {
        /* 数据源类型 监听处理 */
        changeDataSourceType (value) {
            this.watchDataSourceType(value, true)
        },

        /* 数据源类型修改之后部分数据重新赋值 */
        watchDataSourceType (value, isChange) {
            switch (value) {
                case '1':
                this.isReport = false
                this.isPage = true
                this.isFilter = false
                this.isVideo = false
                break
                case '2':
                this.isReport = false
                this.isPage = false
                this.isFilter = true
                this.isVideo = false
                if (isChange) {
                    this.reportSettings = []
                }
                break
                case '4':
                this.isReport = false
                this.isPage = false
                this.isFilter = false
                this.isVideo = true
                break
                default:
                this.isReport = true
                this.isPage = false
                this.isFilter = false
                this.isVideo = false
                if (isChange) {
                    this.content.reportId = null
                    this.reportSettings = []
                }
            }
            this.selectOperation = []
        },

        /* 选择报表 监听处理 */
        changeReport (value) {
            this.chartSourceId = value
            this.chartSourceType = 'report'
            this.genReportField(this.content.type, true)
            this.selectOperation = []
        },

        /* 选择过滤器 监听处理 */
        changeFilter (value) {
            this.chartSourceId = this.findMdId(value)
            this.chartSourceType = 'filter'
            this.genReportField(this.content.type, true)
            this.selectOperation = []
        },

        /* 根据过滤器id查找对应的模型id */
        findMdId (filterId) {
            let mdId = ''
            for (let i in this.filterList) {
                if (this.filterList[i].id === filterId) {
                    mdId = this.filterList[i].mdId
                    break
                }
            }
            return mdId
        },

        /* 展示类型 监听处理 */
        changeReportType (value) {
            this.genReportField(value, true)
            this.selectOperation = []
        },

        /* 生成动态字段 */
        genReportField (value, isChange) {
            let chartSourceId = this.content.reportId
            if (this.chartSourceType === 'filter') {
                chartSourceId = this.chartSourceId
            }
            this.post(this.INTERFACE.packSettings,
                { dataSourceId: chartSourceId, dataSourceType: this.chartSourceType, reportShowTypeId: value }).then(res => {
                if (res.hasOk) {
                    this.reportSettings = res.bean
                    //重新组装报表字段
                    if (isChange) {
                        let rf = {}
                        for (let i in this.reportSettings) {
                            let key = this.reportSettings[i].dimensionCode
                            if (this.reportSettings[i].type === 'multi_select' && this.reportSettings[i].maxSelectCount === 0) {
                                rf[key] = []
                            } else {
                                rf[key] = ''
                            }
                        }
                        this.reportField = rf
                    } else {
                        //数据回显
                        for (let i in this.reportSettings) {
                            let key = this.reportSettings[i].dimensionCode
                            if (this.reportSettings[i].type === 'multi_select' && this.reportSettings[i].maxSelectCount === 0) {
                                let temp = this.reportField[key]
                                //将单个元素转成数组
                                if (!(temp instanceof Array)) {
                                    this.reportField[key] = new Array(temp)
                                }
                            }
                        }
                        //组装selectOperation
                        for (let j in this.reportField) {
                            if (j.indexOf('_oper') !== -1) {
                                //拼装label
                                let label = this.genLabel(j)
                                let oper = {
                                    label: label,
                                    name: j,
                                    value: this.reportField[j]
                                }
                                this.selectOperation.push(oper)
                            }
                        }
                    }
                } else {
                    this.alert(res.message)
                }
            })
        },

        /* 生成操作（count/sum）字段 */
        genLabel (name) {
            // 根据fieldName_name_oper生成显示字段名称操作
            let index1 = name.indexOf('_')
            let index2 = name.lastIndexOf('_')
            let str1 = name.substring(0, index1)
            let str2 = name.substring(index1 + 1, index2)
            let label1 = ''
            let label2 = ''
            let isExit = false
            for (let i in this.reportSettings) {
                if (this.reportSettings[i].dimensionCode === str1) {
                    label1 = this.reportSettings[i].dimensionName
                    for (let j in this.reportSettings[i].contents) {
                        if (this.reportSettings[i].contents[j].fieldName === str2) {
                            label2 = this.reportSettings[i].contents[j].displayName
                            isExit = true
                            break
                        }
                    }
                }
                if (isExit) {
                    break
                }
            }
            return label1 + label2 + '操作'
        },

        /* 生成操作（count/sum）数据 */
        genSelectOperation ($event, dimensionCode, dimensionName, sqlField) {
            if (sqlField) {
                // 重新生成selectOperation
                let tempSelectOperation = []
                // 根据当前下拉框重新组装相关的selectOperation元素
                if ($event instanceof Array) {
                    for (let j in $event) {
                        let name = dimensionCode + '_' + $event[j] + '_oper'
                        let label = this.genLabel(name)
                        let oper = {
                            label: label,
                            name: name,
                            value: ''
                        }
                        tempSelectOperation.push(oper)
                    }
                } else {
                    let name = dimensionCode + '_' + $event + '_oper'
                    let label = this.genLabel(name)
                    let oper = {
                        label: label,
                        name: name,
                        value: ''
                    }
                    tempSelectOperation.push(oper)
                }
                for (let i in this.selectOperation) {
                    if (this.selectOperation[i].name.indexOf(dimensionCode) === -1) {
                        // 保存其他下拉框相关的selectOperation元素
                        tempSelectOperation.push(this.selectOperation[i])
                    }
                }
                this.selectOperation = tempSelectOperation
            }
        },

        /* 选择页面 监听处理 */
        changePage (value) {
            if (value === '0') {
                this.isPageURL = true
            } else {
                this.isPageURL = false
            }
        },

        /* 生成表单信息 */
        genContent () {
            this.chartSourceType = this.content.chartSourceType
            if (this.chartSourceType === 'filter') {
                this.chartSourceId = this.findMdId(this.content.chartSourceId)
            } 
            this.watchDataSourceType(this.content.dataSourceType, false)
            this.reportField = this.content.reportFields
            this.genReportField(this.content.type, false)
            this.changePage(this.content.pageId)
        },

        addContent (title) {
            this.dialogAddVisible = true
            this.extraDialogTitle = title
            this.content = {
                contentName: null,
                chartsGroup: null,
                priority: null,
                dataSourceType: null,
                type: null,
                reportId: null,
                chartSourceId: null,
                chartSourceType: null,
                reportFields: null,
                pageId: null,
                filterReportId: null,
                videoURL: null,
                videoWidth: null,
                videoHeight: null,
                pageURL: null
            }
            this.genContent()
        },
        editContent (title) {
            if (this.selections.length === 1) {
                this.dialogAddVisible = true
                this.extraDialogTitle = title
                let reportId = null
                let chartSourceId = ''
                let chartSourceType = ''
                let reportFields = {}
                
                let tempContent = JSON.parse(this.selections[0].content)
                if (tempContent.filterReportId !== null && tempContent.filterReportId !== undefined && tempContent.filterReportId !== '') {
                    chartSourceId = JSON.parse(tempContent.filterReportStr).filterId
                    chartSourceType = 'filter'
                } else if (tempContent.reportId !== null && tempContent.reportId !== undefined && tempContent.reportId !== '') {
                    reportId = tempContent.reportId
                    chartSourceType = 'report'
                }
                //根据dataDef生成报表字段
                if (tempContent.dataDef !== null && tempContent.dataDef !== undefined && tempContent.dataDef !== '') {
                    let obj = JSON.parse(tempContent.dataDef)
                    for (let key in obj) {
                        if(obj[key].length === 0){
                            reportFields[key] = ''
                        }else if (obj[key].length === 1) {
                            reportFields[key] = obj[key][0]['fieldName']
                            //组装操作（count/sum）数据
                            if(obj[key][0]['oper'] !== undefined){
                                let longKey = key + '_'+ reportFields[key] + '_oper'
                                reportFields[longKey] = obj[key][0]['oper']
                            }
                        } else {
                            let list = obj[key]
                            let subList = []
                            for (let i in list) {
                                //组装操作（count/sum）数据
                                if(list[i]['oper'] !== undefined){
                                    let longKey = key + '_'+ list[i]['fieldName'] + '_oper'
                                    reportFields[longKey] = list[i]['oper']
                                }
                                subList.push(list[i]['fieldName'])
                            }
                            reportFields[key] = subList
                        }
                    }
                }

                let pageId = tempContent.pageId
                if (tempContent.pageId === '' && tempContent.pageURL !== '') {
                    pageId = '0'
                }
                
                this.content = {
                    id: this.selections[0].id,
                    contentName: this.selections[0].contentName,
                    chartsGroup: this.selections[0].chartsGroup,
                    priority: this.selections[0].priority,
                    dataSourceType: tempContent.dataSourceType,
                    type: tempContent.type,
                    reportId: reportId,
                    chartSourceId: chartSourceId,
                    chartSourceType: chartSourceType,
                    reportFields: reportFields,
                    pageId: pageId,
                    filterReportId: tempContent.filterReportId,
                    videoURL: tempContent.videoURL,
                    videoWidth: tempContent.videoWidth,
                    videoHeight: tempContent.videoHeight,
                    pageURL: tempContent.pageURL
                }
                this.genContent()
            } else {
                this.alert('请选择一行！')
            } 
        },
        deleteContent () {
            if (this.selections.length > 0) {
                this.$confirm('确定删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    for (let i in this.selections) {
                        for (let j in this.tableData) {
                            if (this.selections[i].id === this.tableData[j].id) {
                                this.tableData.splice(j, 1)
                                break
                            }
                        }
                    }
                })
            } else {
                this.alert('请至少选择一行！')
            }
        },
        saveContent () {
            this.$refs['contentForm'].validate((valid) => {
                if (valid) {
                    let id = ''
                    if (this.content.id !== undefined) {
                        id = this.content.id
                    } else {
                        id = Math.random()
                    }
                    if (this.content.pageId !== '0') {
                        this.content.pageURL = ''
                    } else {
                        this.content.pageId = ''
                    }
                    let tempContent = {
                        id: id,
                        contentName: this.content.contentName,
                        chartsGroup: this.content.chartsGroup,
                        priority: this.content.priority,
                        content: this.serializeJSONString()
                    }
                    if (this.content.id !== undefined) {
                        for (let i in this.tableData) {
                            if (this.tableData[i].id === this.content.id) {
                                this.tableData.splice(i, 1)
                                break
                            }
                        }
                    } 
                    this.tableData.push(tempContent)
                    this.dialogAddVisible = false
                } else {
                    return false
                }
            })
        },

        serializeJSONString () {
            let content = {}
            if (this.content.dataSourceType === '1') {
                content = {
                    dataSourceType: this.content.dataSourceType,
                    name: this.content.contentName,
                    chartsGroup: this.content.chartsGroup,
                    priority: this.content.priority,
                    reportId:null, 
					type: null,
				 	pageURL: this.content.pageURL,
				 	pageId: this.content.pageId  
				}
            } else if (this.content.dataSourceType === '2') {
                let panelFilterReport =  {
			 		 filterId: this.content.chartSourceId
			    }
			    content = {
					dataSourceType: this.content.dataSourceType,
					name: this.content.contentName,
                    chartsGroup: this.content.chartsGroup,
                    priority: this.content.priority,
					reportId:null, 
					type: this.content.type,
					
					filterReportId:this.content.filterReportId,
					filterReportStr: JSON.stringify(panelFilterReport),
					dataDef:this.genDataDef()
				}
            } else if (this.content.dataSourceType === '4') {
                content = {
                    dataSourceType: this.content.dataSourceType,
                    name: this.content.contentName,
                    chartsGroup: this.content.chartsGroup,
                    priority: this.content.priority,
                    reportId:null, 
					type: null,
					videoURL: this.content.videoURL,
				 	videoHeight: this.content.videoHeight,
					videoWidth: this.content.videoWidth
			    }
            } else {
                content = {
                    dataSourceType: this.content.dataSourceType,
                    name: this.content.contentName,
                    chartsGroup: this.content.chartsGroup,
                    priority: this.content.priority,
					reportId: this.content.reportId, 
                    type: this.content.type,
                    dataDef: this.genDataDef()
                }
            }
		    return JSON.stringify(content);
        },

        genDataDef () {
            // 组装dataDef
            let json = {}
            for (let i in this.reportSettings) {
                let key = this.reportSettings[i].dimensionCode
                let type = this.reportSettings[i].type
                let sqlField = this.reportSettings[i].sqlField
                let arr = []
                if (type === 'multi_select' || type === 'select') {
                    // 下拉框
                    let tempValue = this.reportField[key]
                    let contents = this.reportSettings[i].contents
                    if (tempValue instanceof Array) {
                        for (let j in tempValue) {
                            let jsonObj = {}
                            jsonObj['fieldName'] = tempValue[j]
                            for (let k in contents) {
                                if (contents[k].fieldName === tempValue[j]) {
                                    jsonObj['text'] = contents[k].displayName
                                    if (sqlField) {
                                            // 添加oper
                                        let name = key + '_' + tempValue[j] + '_oper'
                                        for (let n in this.selectOperation) {
                                            if (this.selectOperation[n].name === name) {
                                                jsonObj['oper'] = this.selectOperation[n].value
                                                break
                                            }
                                        }
                                        break
                                    } else {
                                        jsonObj['sqlField'] = false
                                    }
                                }
                            }
                            arr.push(jsonObj)
                        }
                    } else {
                        let jsonObj = {}
                        jsonObj['fieldName'] = tempValue
                        for (let k in contents) {
                            if (contents[k].fieldName === tempValue) {
                                jsonObj['text'] = contents[k].displayName
                                if (sqlField) {
                                    // 添加oper
                                    let name = key + '_' + tempValue + '_oper'
                                    for (let n in this.selectOperation) {
                                        if (this.selectOperation[n].name === name) {
                                            jsonObj['oper'] = this.selectOperation[n].value
                                            break
                                        }
                                    }
                                    break
                                } else {
                                    jsonObj['sqlField'] = false
                                }
                            }
                        }
                        arr.push(jsonObj)
                    }
                } else {
                    // 文本
                    if (this.reportField[key] !== '') {
                        let jsonObj = {
                            fieldName: this.reportField[key],
                            sqlField: false
                        }
                        arr.push(jsonObj)
                    }
                }
                json[key] = arr
            }
            return JSON.stringify(json)
        }
    }
}
</script>
<style>
.el-table .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: #409eff;
}
</style>
