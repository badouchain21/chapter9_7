<template>
    <div class="card">
        <div class="card-image" v-if="$slots.image">
            <slot name="image"></slot>
        </div>
        <div class="card-header" v-if="$slots.header || title">
            <slot name="header">
                <h4 class="card-title">{{title}}</h4>
                <p class="card-category">{{subTitle}}</p>
            </slot>
        </div>
        <div class="card-body">
            <slot></slot>
        </div>
        <div class="card-footer" v-if="$slots.footer">
            <slot name="footer"></slot>
        </div>
    </div>
</template>
<script>
  export default {
    name: 'card',
    props: {
      title: String,
      subTitle: String
    }
  }
</script>
<style>
</style>
