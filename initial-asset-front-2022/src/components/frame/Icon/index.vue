<template>
    <div
        v-if="isExternal"
        :style="styleExternalIcon"
        class="svg-external-icon svg-icon"
        v-on="$listeners">
    </div>
    <svg
        v-else
        :class="svgClass" 
        aria-hidden="true" 
        v-on="$listeners">
        <use :xlink:href="iconName" />
    </svg>
    
</template>
<script>
import GlobalConst from '@/service/global-const'
import { isExternal } from "@/utils/validate";
import iconHandler from '@/components/frame/Icon/index.js'

export default {
    name: "BdIcon",
    props: {
        name,
        className: {
            type: String,
            default: ""
        },
        size: {
            type: [String, Number],
            default: 'default'
        }
    },
    data () {
        return {
            // 默认图标名称
            icon: 'default-fill'
        }
    },
    computed: {
        isExternal () {
            return isExternal(this.name)
        },
        iconName () {
            // 获取icon名称
            let name = this.name || this.icon
            /**
             * 默认图标的处理
             * 当不存在该图标文件时，为其显示默认图标。
             * 旧版的图标使用方法，不做处理，该有的有，该没的没。
             */
            if (!name.includes(GlobalConst.icon.prefix) &&
                !name.includes('bd ') &&
                !name.includes('iconfont')) {
                // console.log('new way:', name)
                if (!iconHandler.hasIcon(name)) {
                    name = this.icon
                }
                name = GlobalConst.icon.prefix + name
            }

            return name
        },
        svgClass () {
            if (this.className) {
                return `bd-icon ${this.className} icon-${this.size}`
            } else {
                return `bd-icon icon-${this.size}`
            }
        },
        styleExternalIcon () {
            return {
                mask: `url(${this.name}) no-repeat 50% 50%`,
                "-webkit-mask": `url(${this.name}) no-repeat 50% 50%`
            };
        }
    }
};
</script>

<style scoped lang="scss">
$size: 1.2em;
.bd-icon {
    width: $size;
    height: $size;
    vertical-align: -0.2em;
    fill: currentColor;
    overflow: hidden;
    cursor: pointer;
    &.icon-middle {
        width: 2 * $size;
        height: 2 * $size;
    }
    &.icon-large {
        width: 6 * $size;
        height: 6 * $size;
    }
    &.icon-small {
        width: $size - 0.4em;
        height: $size - 0.4em;
        vertical-align: 0;
    }
    &.fill { fill: $primary }
    
}
.svg-external-icon {
    background-color: currentColor;
    mask-size: cover !important;
    display: inline-block;
}
</style>
