<template>
    <div>
        <m-form
            v-if="type ==='form'"
            :ref="id"
            v-bind="$attrs"
            v-on="$listeners">
        </m-form>
        <m-list
            v-if="type ==='list'"
            class="padding"
            :ref="id"
            v-bind="$attrs"
            v-on="$listeners">
        </m-list>
        <m-stander-list
            v-else-if="type ==='standerList'"
            :ref="id"
            v-bind="$attrs"
            v-on="$listeners">
        </m-stander-list>
        <m-stander-edit
            v-else-if="type ==='standerEdit'"
            :ref="id"
            v-bind="$attrs"
            v-on="$listeners">
        </m-stander-edit>
    </div>
</template>

<script>
import MForm from '@/components/frame/Common/MForm/index.vue'
import MList from '@/components/frame/Common/MList/index.vue'
import MStanderList from '@/views/module/stander/List'
import MStanderEdit from '@/views/module/stander/Edit'
export default {
    components: {
        MForm,
        MList,
        MStanderList,
        MStanderEdit
    },
    props: {
        type: {
            type: String,
            default: 'form'
        },
        id: {
            type: String,
            default: ''
        }
    }
}
</script>

<style lang="scss" scoped>

</style>
