<template>
<div>
    <form-cascader
        v-bind="$attrs"
        v-on="listenerEvents"
        v-model="tempValue"
        :options="addressJson"
        valueField="label">
    </form-cascader>
</div>
    
</template>
<script>
    import province from './data/province'
    import city from './data/city'
    import area from './data/area'
    import FormCascader from '../Cascader'
    import GlobalConst from '@/service/global-const'

    export default {
        components: {
            FormCascader
        },
        data () { // 定义页面变量
            return {
            }
        },
        props: {
            value: {
                type: [Array, String, Object],
                default: ''
            }
        },
        computed: {
            // 更新自定义【字段关联】事件-设置地址组件的关联键为省市区
            listenerEvents () {
                return {
                    ...this.$listeners,
                    setRelatedField: this.setRelatedField
                }
            },
            // 单项数据流原因，不可直接修改值，需要用临时值做过渡使用
            tempValue: {
                get () {
                    return this.value
                },
                set (val) {
                    this.$emit('input', val)
                }
            },
            // 省市区地址数据
            addressJson () {
                // 方案一：递归：用时 1.746826171875ms
                let typeList = [city, area]
                let result =  this.getAddressList(province,  { label: 'name', value: 'code' }, typeList, 0)
                return result

                // 方案二：区优先级 > 市优先级 > 省优先级处理，用时2.76416015625ms
                // console.time('new')
                // let result = []
                // province.forEach(i => {
                //     let children = []
                //     let _city = city[i.code]
                //     if (_city && _city.length > 0) {
                //         _city.forEach(j => {
                //             let children2 = []
                //             let _area = area[j.code]
                //             if (_area && _area.length > 0) {
                //                 _area.forEach(k => {
                //                     children2.push({
                //                         label: k.name,
                //                         value: k.code,
                //                         full: `${k.code}-${k.name}`,
                //                     })
                //                 })
                //             }
                //             children.push({
                //                 label: j.name,
                //                 value: j.code,
                //                 full: `${j.code}-${j.name}`,
                //                 children: children2
                //             })
                //         })
                //     }
                //     result.push({
                //         label: i.name,
                //         value: i.code,
                //         full: `${i.code}-${i.name}`,
                //         children: children
                //     })
                // })
                // return result
                // console.timeEnd('new')


                // 方案三：先处理省，后处理市，再处理县区，用时 3.31396484375ms
                // console.time('old')
                // let result = []
                // province.forEach(i => {
                //     result.push({
                //         label: i.name,
                //         value: i.code,
                //         full: `${i.code}-${i.name}`,
                //         children: []
                //     })
                // })
                // result.forEach(i => {
                //     let _city = city[i.value]
                //     if (_city && _city.length > 0) {
                //         _city.forEach(j => {
                //             i.children.push({
                //                 label: j.name,
                //                 value: j.code,
                //                 full: `${j.code}-${j.name}`,
                //                 children: []
                //             })
                //         })
                //     }   
                // })
                // result.forEach(i => {
                //     i.children.forEach(j => {
                //         let _area = area[j.value]
                //         if (_area && _area.length > 0) {
                //             _area.forEach(k => {
                //                 j.children.push({
                //                     label: k.name,
                //                     value: k.code,
                //                     full: `${k.code}-${k.name}`,
                //                 })
                //             })
                //         } else {
                //             delete j.children
                //         }
                //     })
                // })
                // console.log(result)
                // return result
                // console.timeEnd('old')  // 3.31396484375ms
                
            }
        },
        methods: { // 定义函数
            /**
             * 更新关联键转化为对应数组数据
             * @param [Boolean, String] key: 关联键
             * @param [Array] value: 关联键对应值
             */
            setRelatedField (key, value) {
                let keyFiels = []
                if (key && key.constructor === Boolean) {
                    // 使用默认配置的关联字段键进行更新
                    keyFiels = GlobalConst.address.relatedField
                } else {
                    if (key && key.constructor === String) {
                        // 按照指定字符进行切割成数组
                        keyFiels = key.split(GlobalConst.address.separator)
                    }
                }
                // keyFiels [Array] 关联字段-键-数组
                // value [Array] 关联字段-值-数组
                this.$emit('setRelatedField', keyFiels, value)
            },
            /**
             * 将多个数组以一定关联字段值合并为一组数组
             * @param [Array] dataList: 被遍历列表
             * @param [Object] props: 指定页面展示字段与值字段
             * @param [Array] childArray: 需要处理的关联子数据数组
             * @param [Number] index: 当前正在处理的关联子数据顺序下角标
             */
            getAddressList (dataList, props = { label: 'label', value: 'value' }, childArray, index) {
                // 获取级联组件的页面显示名称字段与值字段键名
                let { label, value } = props
                // 定义结果列表
                let result = []
                if (dataList && dataList.length > 0) {
                    dataList.forEach(i => {
                        let item = {
                            label: i[label],
                            value: i[value]
                        }
                        let endStatus = index === childArray.length
                        // 当前处理的子数组为最后一种时，不再为子数组对象设置children属性
                        if (!endStatus) {
                            let children = []
                            // 找到当前处理项关联的下一个列表数据
                            let nextList = childArray[index][i[value]]
                            // 使用递归，将下一个子关联数组处理后合并到当前
                            children = this.getAddressList(nextList, props, childArray, index + 1)
                            if (children && children.length > 0) {
                                // 若children数组的长度为0时，不为对象设置children属性，在级联展示上选择才不会异常（总规则为：children无数据则不要设置children属性）
                                item.children = children
                            }   
                        }
                        result.push(item)
                    })
                }
                return result
                
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