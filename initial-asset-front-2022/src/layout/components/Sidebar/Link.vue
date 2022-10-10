
<template>
  <!-- eslint-disable vue/require-component-is -->
  <component v-bind="linkProps(to)">
    <slot />
  </component>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  props: {
    to: {
      type: Object,
      required: true
    }
  },
  methods: {
    linkProps({path, query}) {
        if (isExternal(path)) {
            return {
                is: 'a',
                href: path,
                target: '_blank',
                rel: 'noopener'
            }
        }
        if (query) {
            return {
                is: 'router-link',
                to: {path: path, query: query},
            }
        }
        return {
            is: 'router-link',
            to: {path: path}
        }
    }
  }
}
</script>
