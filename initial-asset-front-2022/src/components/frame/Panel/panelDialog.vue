<template>
  <el-dialog title="面板设置" :visible="setDialogVisible" @close="changeStatus">
    <el-form
      :model="panel"
      label-width="100px"
      label-position="left"
      :rules="panelRules"
      ref="panelForm"
    >
      <el-form-item label="标题" prop="name">
        <el-input v-model="panel.name"></el-input>
      </el-form-item>
      <el-form-item label="编码">
        <el-input v-model="panel.code"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <label v-if="panel.type === '3'">首页工作台</label>
        <el-select v-else v-model="panel.type">
          <el-option
            v-for="layoutType in layoutTypelist"
            :label="layoutType.name"
            :value="layoutType.id"
            :key="layoutType.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="模板">
        <el-select v-model="panel.pageId">
          <el-option
            v-for="template in templatelist"
            :label="template.name"
            :value="template.id"
            :key="template.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图表整体颜色">
        <l-color-picker
          fieldName="chartColor"
          :colors="colors2.chartColor"
          :isSingle="true"
          @setDataFromChild="setDataFromChild2"
        ></l-color-picker>
      </el-form-item>
      <el-form-item label="图表图例字体颜色">
        <l-color-picker
          fieldName="chartLegendFontColor"
          :colors="colors2.chartLegendFontColor"
          :isSingle="true"
          @setDataFromChild="setDataFromChild2"
        ></l-color-picker>
      </el-form-item>
      <el-form-item label="是否启用自动刷新">
        <el-select v-model="panel.isUseReload">
          <el-option label="否" value="0"></el-option>
          <el-option label="是" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" rows="3" v-model="panel.remark"></el-input>
      </el-form-item>
      <el-form-item label="背景图">
        <input type="file" ref="file">
        <span class="del-button" @click="delFile">删除</span>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="changeStatus">取 消</el-button>
      <el-button type="primary" @click="savePanel">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { Dialog, Form, FormItem, Select, Option } from 'element-ui'
import ColorPickerJq from '../Inputs/ColorPickerJq'
export default {
    props: [
        'setDialogVisible', 
        'layoutTypelist', 
        'templatelist'
    ],
    components: {
        [Dialog.name]: Dialog,
        [Form.name]: Form,
        [FormItem.name]: FormItem,
        [Select.name]: Select,
        [Option.name]: Option,
        [ColorPickerJq.name]: ColorPickerJq
    },
    data: function() {
        return {
            layoutId: '',
            panel: {},
            colors2: {},
            panelRules: {
                name: [
                    { required: true, message: '请填写标题', trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '请选择类型', trigger: 'change' }
                ]
            }
        }
    },
    methods: {
        /* 关闭弹框 */
        changeStatus () {
            this.$emit('dialogContro')
        },

        /* 删除文件 */
        delFile () {
            this.$refs.file.value = ''
        },

        /* 获取面板信息 */
        initDialog (layoutId) {
            if (layoutId !== '') {
                this.layoutId = layoutId
                this.post(this.INTERFACE.editPanel, { id: this.layoutId }).then(res => {
                    let data = res
                    data.type = res.type.toString()
                    data.isUseReload = res.isUseReload.toString()
                    this.panel = data
                    this.colors2.chartColor = this.panel.chartColor
                    this.colors2.chartLegendFontColor = this.panel.chartLegendFontColor
                })
            } else {
                this.panel = {}
                this.colors2 = {}
            }  
        },

        /* 保存面板信息 */
        savePanel () {
            this.$refs['panelForm'].validate(valid => {
                if (valid) {
                    this.panel.id = this.layoutId
                    this.panel.chartColor = this.colors2.chartColor
                    this.panel.chartLegendFontColor = this.colors2.chartLegendFontColor
                    this.post(this.INTERFACE.savePanel, this.panel).then(res => {
                        this.alert('保存成功', 'success')
                        this.$emit('dialogContro')
                        if (this.layoutId === '') {
                            this.$emit('setPanelInfo',{id:res.bean.id,name:res.bean.name})
                        }
                    })
                } else {
                    return false
                }
            })
        },

        /* 颜色选择器设置数据 */
        setDataFromChild2 (json) {
            if (!json) return
            for (let key in json) {
                this.colors2[key] = json[key]
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
.del-button {
  color: #2684ce;
}
.del-button:hover {
  color: #f36100;
}
</style>
