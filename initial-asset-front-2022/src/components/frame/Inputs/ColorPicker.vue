<template>
  <div class="form-color-picker colorinput_select">
    <div class="colorSelect-maxContainer">
        <ul class="colorSelect-ui">
            <div v-for="(value, key) in items" :key="key">
                <li class="colorSelect-li">
                    <span v-show="false" class="colorSelect-span" @click='removeItem(key)'><i class="menu-icon bd bd-times-circle-o" style="background: rgb(255, 255, 255);"></i></span>
                    <div v-show="false" class="colorSelect-div" style="background: value);"></div>
                    <input type="text" class="colorSelect-input" style="width: 96px;" :value="value">
                    <el-color-picker show-alpha></el-color-picker>
                </li>
            </div>
        </ul>
        <el-color-picker v-model="colors" show-alpha></el-color-picker>
    </div>
  </div>
</template>
<script>
// 颜色选择器,未完成,想改成可以多选,可以显示已选颜色,element-ui版
  import { ColorPicker } from 'element-ui'

  export default{
    name: 'l-color-picker',
    components: {
      [ColorPicker.name]: ColorPicker
    },
    props: {
      colors: {
          type: String
      },
      isSingle : {
          type: Boolean,
          default: false
      }
    },
    data () {
      return {
          items: {}
      }
    },
    computed: {

    },
    methods: {
        // 获取随机的字符串
        getRamdomId () {
            return Math.random().toString(16).slice(2)
        },
        // 把以逗号分隔的颜色初始化为json
        initItems () { 
            if (!this.colors) return
            for (let c in this.colors.split(",")) {
                this.items[this.getRamdomId()] = c
            }
        },
        // 移除项,同时更新数据
        removeItem (key) {
            let color = this.items[key];
            this.items.remove(key);
            this.colors = this.colors.replace(color, "");
            this.colors = this.colors.replace(",,", ",");
        },
        addItem (color) {
            this.items[this.getRamdomId()] = color
            this.colors.split(",").push(color).join(",");
        },
        changeItem (key, color) {
            
        }
    },
    created () {
        this.initItems()
    }
  }
</script>
