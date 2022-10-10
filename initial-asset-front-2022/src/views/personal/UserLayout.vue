<template>
    <div>
        <card>
            <button @click="setDefault" class='btn btn-primary'>设为默认</button>
            <button @click="viewPanel" class='btn btn-primary'>预览</button>
            <button @click="editPanel" class='btn btn-primary'>编辑</button>
        </card>
        <pagelist
            ref='mainList'
            :url='url'
            :columns='columns'
            :pageInfo='false'>
        </pagelist>
    </div>
</template>

<script>
import {Pagelist} from '@/components/frame/index'
export default {
    components: {
        Pagelist
    },
    data() {
        return {
            url: `${this.BASEURL}/panel/userlayout/userlayoutlist/listJSON.do`,
            columns: [
                {prop: 'layoutName', label: '面板名称'},
                {prop: 'layoutTypeStr', label: '面板类型'},
                {prop: 'createTime', label: '创建时间'},
                {prop: 'updateTime', label: '最后修改时间'},
                {
                    prop: 'status',
                    label: '状态',
                    formatter: function (row, column, cellValue) {
                        return cellValue === 1 ? '启用' : '未启用'
                    }
                }, {
                    prop: 'editable',
                    label: '是否允许修改',
                    formatter: function (row, column, cellValue) {
                        return cellValue === 1 ? '允许' : '不允许'
                    }
                }, {
                    prop: 'isDefult',
                    label: '是否默认',
                    formatter: function (row, column, cellValue) {
                    return cellValue === 1 ? '是' : '否'
                    }
                }, {prop: 'sourceDesc', label: '来源'}
            ]
        }
    },
    methods: {
        setDefault () {
            let selection = this.$refs.mainList.selection
            if (selection.length === 0) {
                this.alert('请选择一行！')
                return
            }
            let url = `${this.BASEURL}/panel/userlayout/userlayoutedit/setDefult.do`
            this.post(url, {id: selection[selection.length - 1].layoutId}).then(res => {
                if (res.hasOk) {
                    this.$refs.mainList.refresh()
                    this.alert('操作成功', 'success')
                } else {
                    this.alert('操作成功', 'error')
                }
            })
        },
        viewPanel () {
            let selection = this.$refs.mainList.selection
            if (selection.length === 0) {
                this.alert('请选择一行！')
                return
            }
            let layoutId = selection[selection.length - 1].layoutId
            this.$router.push({ name: 'Overview', params: { layoutId: layoutId }})
        },
        editPanel () {
            let selection = this.$refs.mainList.selection
            if (selection.length === 0) {
                this.alert('请选择一行！')
                return
            }
            let layoutId = selection[selection.length - 1].layoutId
            this.$router.push({ path: '/panel/panelEdit', query: { layoutId: layoutId }})
        }
    },
    created: function () {
    }
}
</script>

<style lang="scss" scoped>
</style>
