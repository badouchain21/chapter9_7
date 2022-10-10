<template>
  <el-dialog :title="item.title" :visible="dialogFormVisible" @close="changeStatus">
    <el-form :model="item" label-width="100px" label-position="left" :rules="itemRules" ref="itemForm">
      <el-tabs v-model="activeName" type="border-card">
        <el-tab-pane label="基础设置" name="basicSettings">
          <el-form-item label="标题" prop="name">
            <el-input v-model="item.name"></el-input>
          </el-form-item>
          <el-form-item label="预览图">
            <input type="file" ref="file">
            <span class="del-button" @click="delFile">删除</span>
          </el-form-item>
          <el-form-item label="内容类型" prop="contentType">
            <el-select v-model="item.contentType" @change="changeContentType">
              <el-option label="单一内容" value="0"></el-option>
              <el-option label="组合内容" value="1"></el-option>
              <el-option label="多图片" value="2"></el-option>
            </el-select>
          </el-form-item>
          <div v-if="imgVisible">
            <el-form-item label="展示类型" prop="shouType">
              <el-select v-model="item.shouType">
                <el-option label="切换展示" value="1"></el-option>
                <el-option label="并列展示" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="切换方式" prop="changeType">
              <el-select v-model="item.changeType">
                <el-option label="横向滚动" value="1"></el-option>
                <el-option label="淡入淡出" value="2"></el-option>
                <el-option label="消失显示" value="3"></el-option>
                <el-option label="旋转消失" value="4"></el-option>
              </el-select>
            </el-form-item>
            <!-- <el-form-item label="上传图片">
              <input type="file" ref="file">
              <span class="del-button" @click="delFile">删除</span>
            </el-form-item> -->
            <el-form-item label="上传图片">
              <el-upload :action="imageUrl">
                <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
              <!-- <span class="del-button" @click="delFile">删除</span> -->
            </el-form-item>
          </div>
          <div v-if="singleContentVisible">
            <el-form-item label="数据源类型" prop="dataSourceType">
              <el-select v-model="item.dataSourceType" @change="changeDataSourceType">
                <el-option label="报表" value="0"></el-option>
                <el-option label="页面" value="1"></el-option>
                <el-option label="过滤器" value="2"></el-option>
                <el-option label="视频" value="4"></el-option>
              </el-select>
            </el-form-item>
            <div v-if="isReport">
              <el-form-item label="选择报表" prop="reportId">
                <el-select v-model="item.reportId" @change="changeReport">
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
                <el-select v-model="item.chartSourceId" @change="changeFilter">
                  <el-option
                    v-for="filter in filterList"
                    :label="filter.name"
                    :value="filter.id"
                    :key="filter.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div v-if="item.isSingle && (isReport||isFilter)">
              <el-form-item label="图表类型" prop="type">
                <el-select v-model="item.type" @change="changeReportType">
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
                <el-select v-model="item.pageId" @change="changePage">
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
                  <el-input v-model="item.pageURL"></el-input>
              </el-form-item>
            </div>
            <div v-if="isVideo">
              <el-form-item label="视频地址" prop="videoURL">
                <el-input v-model="item.videoURL"></el-input>
              </el-form-item>
              <el-form-item label="视频宽度" prop="videoWidth">
                <el-input v-model="item.videoWidth"></el-input>
              </el-form-item>
              <el-form-item label="视频高度" prop="videoHeight">
                <el-input v-model="item.videoHeight"></el-input>
              </el-form-item>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="样式设置" name="styleSettings">
          <el-form-item label="块宽度">
            <el-input v-model="item.width"></el-input>
          </el-form-item>
          <el-form-item label="块高度">
            <el-input v-model="item.height"></el-input>
          </el-form-item>
          <el-form-item label="上边距">
            <el-input v-model="item.topDistance"></el-input>
          </el-form-item>
          <el-form-item label="右边距">
            <el-input v-model="item.rightDistance"></el-input>
          </el-form-item>
          <el-form-item label="下边距">
            <el-input v-model="item.bottomDistance"></el-input>
          </el-form-item>
          <el-form-item label="左边距">
            <el-input v-model="item.leftDistance"></el-input>
          </el-form-item>
          <el-form-item label="是否显示标题" prop="flgShowTitleBar">
            <el-select v-model="item.flgShowTitleBar" @change="changeFlgShowTitleBar">
              <el-option label="否" value="0"></el-option>
              <el-option label="是" value="1"></el-option>
            </el-select>
          </el-form-item>
          <div v-if="titleVisible">
            <el-form-item label="标题图标">
              <el-input v-model="item.blockIcon"></el-input>
            </el-form-item>
            <el-form-item label="标题背景颜色">
              <l-color-picker
                fieldName="blockTitleColor"
                :colors="colors.blockTitleColor"
                :isSingle="true"
                @setDataFromChild="setDataFromChild"
              ></l-color-picker>
            </el-form-item>
            <el-form-item label="标题字体颜色">
              <l-color-picker
                fieldName="blockTitleFontColor"
                :colors="colors.blockTitleFontColor"
                :isSingle="true"
                @setDataFromChild="setDataFromChild"
              ></l-color-picker>
            </el-form-item>
          </div>
          <el-form-item label="是否显示边框" prop="flgShowBorder">
            <el-select v-model="item.flgShowBorder" @change="changeFlgShowBorder">
              <el-option label="否" value="0"></el-option>
              <el-option label="是" value="1"></el-option>
            </el-select>
          </el-form-item>
          <div v-if="borderVisible">
            <el-form-item label="边框类型">
              <el-select v-model="item.borderType" prop="borderType">
                <el-option label="默认边框" value="0"></el-option>
                <el-option label="主题边框1" value="1"></el-option>
                <el-option label="主题边框2" value="2"></el-option>
                <el-option label="主题边框3" value="3"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="边框颜色">
              <l-color-picker
                fieldName="blockBorderColor"
                :colors="colors.blockBorderColor"
                :isSingle="true"
                @setDataFromChild="setDataFromChild"
              ></l-color-picker>
            </el-form-item>
          </div>
          <el-form-item label="图表内容js扩展显示">
            <el-input v-model="item.blockChartOptions" type="textarea" rows="3"></el-input>
          </el-form-item>
          <el-form-item label="背景图">
            <input type="file" ref="file">
            <span class="del-button" @click="delFile">删除</span>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane v-if="contentVisible" label="内容设置" name="contentSettings">
          <el-form-item label="展示类型" prop="shouType">
            <el-select v-model="item.shouType">
              <el-option label="切换展示" value="1"></el-option>
              <el-option label="并列展示" value="2"></el-option>
              <el-option label="组合展示" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="切换方式" prop="changeType">
            <el-select v-model="item.changeType">
              <el-option label="横向滚动" value="1"></el-option>
              <el-option label="淡入淡出" value="2"></el-option>
              <el-option label="消失显示" value="3"></el-option>
              <el-option label="旋转消失" value="4"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="时间间距(毫秒)" prop="pitchTime">
            <el-input v-model="item.pitchTime"></el-input>
          </el-form-item>
          <el-form-item label-width="0px">
            <panel-table
                :reportlist="reportlist"
                :typelist="typelist"
                :pagelist="pagelist"
                :filterList="filterList"
                :tableData="tableData"
                ref="content"
            ></panel-table>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="事件设置" name="eventSettings">
          <el-form-item label="刷新间隔(秒)">
            <el-input v-model="item.reloadTime"></el-input>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="分享设置" name="shareSettings">
          <el-form-item label="分享对象">
            <span v-for="(option,index) in shareSet" :id="option.id" :key="index">
              {{option.name}}
              <i @click="delShare(index)" class="del-button fa fa-trash-o"></i>
            </span>
          </el-form-item>
          <el-form-item label="分享给">
            <el-select v-model="shareObj">
              <el-option v-for="share in shareArray" :label="share.name" :value="share.id" :key="share.id"></el-option>
            </el-select>
            <span @click="addShare" class="addShare">添加共享</span>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="changeStatus">取 消</el-button>
      <el-button type="primary" @click="saveSingleBlock">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import {Dialog, Tabs, TabPane, Form, FormItem, Select, Option, Upload} from 'element-ui'
import panelTable from 'src/components/panel/panelTable.vue'
import ColorPickerJq from '../Inputs/ColorPickerJq'

export default {
    name: 'panel-form',
    props: [
        'dialogFormVisible',
        'reportlist',
        'typelist',
        'pagelist',
        'filterList',
        'shareArray'
    ],
    components: {
        [Dialog.name]: Dialog,
        [Tabs.name]: Tabs,
        [TabPane.name]: TabPane,
        [Form.name]: Form,
        [FormItem.name]: FormItem,
        [Select.name]: Select,
        [Option.name]: Option,
        [ColorPickerJq.name]: ColorPickerJq,
        panelTable
    },
    data: function() {
        return {
            item: {},
            activeName: 'basicSettings',
            titleVisible: false,
            borderVisible: false, 
            shareObj: '',
            shareSet: [],
            contentVisible: false,
            dialogAddVisible: false,
            imgVisible: false,
            singleContentVisible: true,
            reportSettings: [],
            isReport: false,
            isPage: false,
            isFilter: false,
            isVideo: false,
            reportField: {},
            chartSourceId: '',
            chartSourceType: '',
            selectOperation: [],
            colors: {},
            shareObjectId: '',
            shareObjectType: '',
            isPageURL: false,
            tableData: [],
            itemRules: {
                name: [
                    { required: true, message: '请填写标题', trigger: 'blur' }
                ],
                contentType: [
                    { required: true, message: '请选择内容类型', trigger: 'change' }
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
                ],
                flgShowTitleBar: [
                    { required: true, message: '请选择是否显示标题', trigger: 'change' }
                ],
                flgShowBorder: [
                    { required: true, message: '请选择是否显示边框', trigger: 'change' }
                ],
                borderType: [
                    { required: true, message: '请选择边框类型', trigger: 'change' }
                ],
                shouType: [
                    { required: true, message: '请选择展示类型', trigger: 'change' }
                ],
                changeType: [
                    { required: true, message: '请选择切换方式', trigger: 'change' }
                ],
                pitchTime: [
                    { required: true, message: '请填写时间间距', trigger: 'blur' }
                ]
            },
            src: [],
            imageUrl: 'http://www.baidu.com'
        }
    },
    methods: {
        handleAvatarSuccess(res, file) {
          let _this = this;
          _this.imageUrl = URL.createObjectURL(file.raw);
        },
        //上传之前的钩子函数
        beforeAvatarUpload(file) {
          let fd = new FormData();
          fd.append('file',file);//传文件
          // fd.append('srid',this.upLoadData.srid);//传其他参数
          axios.post('/api/up/file', fd).then(function(res){
                  console.log('成功');
          })
          return false//屏蔽了action的默认上传
        },
        /* 关闭弹框 */
        changeStatus () {
            this.$emit('changeVisible')
        },      

        /* 保存块 */
        saveSingleBlock () {
            this.$refs['itemForm'].validate((valid) => {
                if (valid) {
                    let tempItem = this.item
                    // 处理部分数据
                    tempItem.blockTitleColor = this.colors.blockTitleColor
                    tempItem.blockTitleFontColor = this.colors.blockTitleFontColor
                    tempItem.blockBorderColor = this.colors.blockBorderColor
                    tempItem.shareObjectId = this.shareObjectId
                    tempItem.shareObjectType = this.shareObjectType
                    tempItem.col = this.item.x
                    tempItem.row = this.item.y
                    if (tempItem.contentType === '0') {
                        if (tempItem.dataSourceType !== '1') {
                            tempItem.pageId = ''
                            tempItem.pageURL = ''
                        }
                        if (tempItem.dataSourceType !== '2') {
                            tempItem.panelFilterReportId = ''
                        }
                        
                        if (tempItem.pageId !== '0') {
                            tempItem.pageURL = ''
                        } else {
                            tempItem.pageId = ''
                        }
                        
                        if (tempItem.dataSourceType === '0' || tempItem.dataSourceType === '2') {
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
                            let contents = []
                            let content = {
                                blockId: tempItem.id,
                                type: tempItem.type,
                                reportId: tempItem.reportId,
                                dataSourceType: tempItem.dataSourceType,
                                dataDef: json
                            }
                            contents.push(content)
                            tempItem.contentsStr = JSON.stringify(contents)
                            //组装过滤器信息
                            if (tempItem.dataSourceType === '2') {
                                let panelFilterReport = {
                                    blockId: tempItem.id,
                                    filterId: tempItem.chartSourceId,
                                    id: tempItem.panelFilterReportId
                                }
                                tempItem.panelFilterReportFormStr = JSON.stringify(panelFilterReport)
                            }
                        }
                        if (tempItem.dataSourceType === '4') {

                        }
                    }
                    if (tempItem.contentType === '1') {
                        this.tableData = this.$refs['content'].tableData
                        let contents = []
                        for (let i in this.tableData) {
                            contents.push(JSON.parse(this.tableData[i].content))
                        }
                        tempItem.contentsStr = JSON.stringify(contents)
                    }
                    if (tempItem.contentType === '2') {

                    }
                    this.post(this.INTERFACE.saveBlock, 
                        { layoutId: this.item.layoutId, block: JSON.stringify(tempItem) }).then(res => {
                        this.alert('保存成功', 'success')
                        this.$emit('changeVisible')
                        this.$emit('reloadBlock')
                    })
                } else {
                    return false
                }
            })
        },

        /* 是否显示标题 监听处理 */
        changeFlgShowTitleBar (value) {
            if (value === '1') {
                this.titleVisible = true
            } else {
                this.titleVisible = false
            }
        },

        /* 是否显示边框 监听处理 */
        changeFlgShowBorder (value) {
            if (value === '1') {
                this.borderVisible = true
            } else {
                this.borderVisible = false
            }
        },

        /* 删除文件 */
        delFile () {
            this.$refs.file.value = ''
        },

        /* 删除分享 */
        delShare (index) {
            this.shareSet.splice(index, 1)
            let shareObjectIdArray = this.shareObjectId.split(',')
            let shareObjectTypeArray = this.shareObjectType.split(',')
            shareObjectIdArray.splice(index, 1)
            shareObjectTypeArray.splice(index, 1)
            // 先清空再处理
            this.shareObjectId = ''
            this.shareObjectType = ''
            for (let i in this.shareSet) {
                if (i === '0') {
                    this.shareObjectId = shareObjectIdArray[i]
                    this.shareObjectType = shareObjectTypeArray[i]
                } else {
                    this.shareObjectId += ',' + shareObjectIdArray[i]
                    this.shareObjectType += ',' + shareObjectTypeArray[i]
                }
            }
        },

        /* 添加分享 */
        addShare () {
            if (this.shareObj !== '') {
                // 非所有用户
                if (this.shareObj !== 0) {
                    let type = this.shareObj
                    let selectType = ''
                    if (type === 20) {
                        type = 2
                        selectType = '20'
                    }
                    this.addressBook({
                        type: type,
                        selectType: selectType
                    }).then(data => {
                        // 在选择了所有用户后又选择其他则清空
                        if (this.shareSet.length === 1 && this.shareSet[0].id === '0') {
                            this.shareSet = []
                            this.shareObjectId = ''
                            this.shareObjectType = ''
                        }
                        // 将shareSet进行封装处理
                        let shareSetJson = {}
                        for (let j in this.shareSet) {
                            shareSetJson[this.shareSet[j].id] = this.shareSet[j]
                        }
                        for (let i in data) {
                            if (shareSetJson[data[i].id] === undefined) {
                                //显示
                                let share = {
                                    id: data[i].id,
                                    name: data[i].name
                                }
                                this.shareSet.push(share)
                                //组装shareObjectId和shareObjectType
                                if (this.shareObjectType === '') {
                                    this.shareObjectId = data[i].id + '!@!' + data[i].name
                                    this.shareObjectType = data[i].type 
                                } else {
                                    this.shareObjectId += ',' + data[i].id + '!@!' + data[i].name
                                    this.shareObjectType += ',' + data[i].type 
                                }
                            }
                        }
                    })
                } else {
                    this.shareSet = [
                        {
                            id: '0',
                            name: '所有用户'
                        }
                    ]
                    this.shareObjectId = '0!@!所有用户'
                    this.shareObjectType = '0'
                }
            }
        },

        /* 内容类型 监听处理 */
        changeContentType (value) {
            switch (value) {
                case '1':
                this.contentVisible = true
                this.singleContentVisible = false
                this.imgVisible = false
                this.genTableData()
                break
                case '2':
                this.imgVisible = true
                this.singleContentVisible = false
                this.contentVisible = false
                break
                default:
                this.singleContentVisible = true
                this.contentVisible = false
                this.imgVisible = false
            }
            this.selectOperation = []
        },
        
        /* 展示类型 监听处理 */
        changeReportType (value) {
            this.genReportField(value, true)
            this.selectOperation = []
        },

        /* 生成动态字段 */
        genReportField (value, isChange) {
            let chartSourceId = this.item.reportId
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

        /* 数据源类型 监听处理 */
        changeDataSourceType (value) {
            this.watchDataSourceType(value, true)
        },

        /* 选择报表 监听处理 */
        changeReport (value) {
            this.chartSourceId = value
            this.chartSourceType = 'report'
            this.genReportField(this.item.type, true)
            this.selectOperation = []
        },

        /* 选择过滤器 监听处理 */
        changeFilter (value) {
            this.chartSourceId = this.findMdId(value)
            this.chartSourceType = 'filter'
            this.genReportField(this.item.type, true)
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
                    this.item.reportId = null
                    this.reportSettings = []
                }
            }
            this.selectOperation = []
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

        /* 颜色选择器设置数据 */
        setDataFromChild (json) {
            if (!json) return
            for (let key in json) {
                this.colors[key] = json[key]
            }
        },

        /* 获取块信息 */
        genItem (item) {
            this.item = item
            this.chartSourceType = this.item.chartSourceType
            if (this.chartSourceType === 'filter') {
                this.chartSourceId = this.findMdId(this.item.chartSourceId)
            } 
            this.changeContentType(this.item.contentType)
            this.watchDataSourceType(this.item.dataSourceType, false)
            this.reportField = this.item.reportFields
            this.genReportField(this.item.type, false)
            this.changeFlgShowTitleBar(this.item.flgShowTitleBar)
            this.changeFlgShowBorder(this.item.flgShowBorder)
            this.changePage(this.item.pageId)

            this.colors.blockTitleColor = this.item.blockTitleColor
            this.colors.blockTitleFontColor = this.item.blockTitleFontColor
            this.colors.blockBorderColor = this.item.blockBorderColor

            if(this.item.id !== undefined ) {
                this.genShareArray(this.item.id)
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

        /* 生成分享对象 */
        genShareArray (blockId) {
            this.post(this.INTERFACE.findShareArray, { blockId: blockId }).then(res => {
                for(let i in res) {
                    //显示
                    let share = {
                        id: res[i].id,
                        name: res[i].name
                    }
                    this.shareSet.push(share)
                    //组装shareObjectId和shareObjectType
                    if (this.shareObjectType === '') {
                        this.shareObjectId = res[i].id + '!@!' + res[i].name
                        this.shareObjectType = res[i].type + ''
                    } else {
                        this.shareObjectId += ',' + res[i].id + '!@!' + res[i].name
                        this.shareObjectType += ',' + res[i].type 
                    }
                }
            })
        },

        /* 生成表格对象 */
        genTableData () {
            this.tableData = []
            let comboContentData = this.item.comboContentData
            for (let i in comboContentData) {
                let reportId = comboContentData[i].reportId
                if (reportId === '') {
                    reportId = null
                }
                let pageId = comboContentData[i].pageId
                if (comboContentData[i].pageId === '' && comboContentData[i].pageURL !== '') {
                    pageId = '0'
                }
                let tempContent = {
                    id: comboContentData[i].id,
                    dataSourceType: comboContentData[i].dataSourceType,
                    name: comboContentData[i].name,
                    chartsGroup: comboContentData[i].chartsGroup,
                    priority: comboContentData[i].priority,
                    reportId: reportId,
                    type: comboContentData[i].type,
                    dataDef: comboContentData[i].dataDef,
                    pageId: pageId,
                    pageURL: comboContentData[i].pageURL,
                    filterReportId: comboContentData[i].filterReportId,
                    filterReportStr: comboContentData[i].filterReportStr,
                    videoURL: comboContentData[i].videoURL,
                    videoHeight: comboContentData[i].videoHeight,
                    videoWidth: comboContentData[i].videoWidth
                }
                let data = {
                    id: comboContentData[i].id,
                    contentName: comboContentData[i].name,
                    chartsGroup: comboContentData[i].chartsGroup,
                    priority: comboContentData[i].priority,
                    content: JSON.stringify(tempContent)
                }
                this.tableData.push(data)
            }
        }
    }
}
</script>
<style>
/* 针对打包后样式发生变化 */
.el-dialog__body {
  padding: 30px 20px;
}
.el-select-dropdown__item.selected {
  background-color: transparent;
}

.el-select .el-input:hover input {
  color: unset;
}
.del-button {
  color: #2684ce;
}
.del-button:hover {
  color: #f36100;
}
.el-table .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: #409eff;
}
.previewBox {
  border: 1px solid rgba(0, 0, 0, 0.5);
  width: 100%;
  padding: 2% 0;
  margin-top: 10px;
  border-radius: 5px;
}
.imgContainer {
  height: 50px;
  width: 80px;
  position: relative;
  display: inline-block;
  margin: 0 5px;
}
.imgContainer img {
  width: 100%;
  height: 100%;
  /*position: absolute;*/
}
.imgContainer i {
  float: right;
}
.addShare {
  cursor: pointer;
  color: #2684ce;
}
</style>
