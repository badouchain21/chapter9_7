<template>
    <div class="card-count">
        <div class="m-count">
            <div class="m-count-item" 
                v-for="(i, index) in countList" 
                :key="index"
                v-if="countList.length > 0">
                <bd-icon class="card-bg" name="cardBg"></bd-icon>
                <div class="header-side">
                    <div class="l-side">
                        <div class="iconArea" 
                            :style="{background: colorRgba(i.bgColor, 0.2), color: i.bgColor}">
                            <bd-icon :name="i.iconName"></bd-icon>
                        </div>
                        <span class="itemlabel">{{i.label}}</span>
                    </div>
                    <div class="r-side">
                        <countTo :startVal='0' :endVal='i.num' :duration='2000'></countTo>
                    </div>
                </div>
                <div class="footer-side">
                    <div class="line" :style="{background: i.bgColor}"></div>
                </div>
            </div>

            <div class="m-count-item default" 
                v-for="(i, index) in default_countList" 
                :key="index"
                v-if="countList.length === 0">
                <bd-icon class="card-bg" name="cardBg"></bd-icon>
                <div class="header-side">
                    <div class="l-side">
                        <div class="iconArea" ></div>
                        <span class="itemlabel"></span>
                    </div>
                    <div class="r-side"></div>
                </div>
                <div class="footer-side"></div>
            </div>

        </div>
    </div>
</template>

<script>
import countTo from 'vue-count-to'
import globalStyle from '@/styles/global.scss'
export default {
    name: 'CardCount',
    components: {
        countTo
    },
    props: {
        list: Array,
        type: {}
    },
    data () {
        return {
            countList: [],
            default_countList: 4,
            colorList: ['default', '#F9BD16', '#8A7AFB', '#F95554'],
        }
    },
    computed: {
        // colorList () {
        //     let colorScheme = []
        //     let baseColorList = [globalStyle.primary, globalStyle.warning, globalStyle.operate, globalStyle.danger]
        //     switch (this.type) {
        //         case 'theme-full':
        //             break
        //         case 'theme-gradient':
        //             break
        //         default:
        //             colorScheme =  baseColorList
        //     }
        // }
    },
    methods: {
        /**
         * JS颜色十六进制转换为rgb或rgba,返回的格式为 rgba（255，255，255，0.5）字符串
         * sHex为传入的十六进制的色值
         * alpha为rgba的透明度
         */
        colorRgba: (sHex, alpha = 1) => {
            if (!sHex || sHex === 'default') {
                return 'default'
            }
            // 十六进制颜色值的正则表达式
            var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/
            /* 16进制颜色转为RGB格式 */
            let sColor = sHex.toLowerCase()
            if (sColor && reg.test(sColor)) {
            if (sColor.length === 4) {
                var sColorNew = '#'
                for (let i = 1; i < 4; i += 1) {
                    sColorNew += sColor.slice(i, i + 1).concat(sColor.slice(i, i + 1))
                }
                sColor = sColorNew
            }
            //  处理六位的颜色值
            var sColorChange = []
            for (let i = 1; i < 7; i += 2) {
                sColorChange.push(parseInt('0x' + sColor.slice(i, i + 2)))
            }
            // return sColorChange.join(',')
            // 或
            return 'rgba(' + sColorChange.join(',') + ',' + alpha + ')'
            } else {
                return sColor
            }
        }
    },
    watch: {
        list: {
            immediate: true,
            handler (newVal, oldVal) {
                // 模拟实际请求页面渲染效果
                setTimeout(() => {
                    this.countList = newVal
                    this.countList.forEach((i, index) => {
                        this.$set(i, 'bgColor', this.colorList[index])
                    })
                }, 1000)
                
            }
            
        }
    },
}
</script>

<style lang="scss" scoped>
    .m-count {
        display: flex;
        .m-count-item {
            position: relative;
            flex: 1;
            padding: $space $space $space $space+2px;
            min-height: 110px;
            margin-right: $space;
            background: $contentInBg;
            border-radius: $borderRadius;
            &:first-child {
                .header-side {
                    .iconArea {
                        background: rgba($primary, 0.2);
                        color: $primary;
                    }
                }
                .footer-side {
                    .line {
                        background: $primary;
                    }
                }
            }
            &:last-child {
                margin-right: 0;
            }
            .card-bg {
                position: absolute;
                right: 0;
                bottom: 0;
                font-size: 122px;
                fill: rgba($primary, 0.05);
            }
            .header-side {
                display: flex;
                align-items: center;
                justify-content: space-between;
                .l-side {
                    flex-grow: 1;
                    padding-right: 10px;
                    .iconArea {
                        width: 40px;
                        height: 40px;
                        position: relative;
                        font-size: 20px;
                        border-radius: 50%;
                        .bd-icon {
                            position: absolute;
                            top: 0;
                            right: 0;
                            bottom: 0;
                            left: 0;
                            margin: auto;
                        }
                    }
                    .itemlabel {
                        font-size: 18px;
                        font-weight: bold;
                        margin-top: 8px;
                        display: block;
                        margin-bottom: 14px;
                        min-height: 20px;
                    }
                }
                .r-side {
                    font-size: 42px;
                    color: $fontCL;
                }
            }
            .footer-side {
                width: 100%;
                height: 4px;
                background: rgba(204, 204, 204, 0.5);
                .line {
                    width: 40%;
                    height: 100%;
                }
            }
            &.default {
                .l-side {
                    .iconArea {
                        background: #eee;
                    }
                    .itemlabel {
                        background: #eee;
                    }
                }
                .r-side {
                    height: 60px;
                    width: 60px;
                    background: #eee;
                }
            }

        }

    }
</style>
