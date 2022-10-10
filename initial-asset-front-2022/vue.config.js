'use strict'
const path = require('path')
const defaultSettings = require('./src/settings.js')
const webpack = require('webpack')
// 引入js分析插件
let BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const CompressionPlugin = require('compression-webpack-plugin') // 引入gzip压缩插件

function resolve(dir) {
    return path.join(__dirname, dir)
}

const name = defaultSettings.title || 'vue Admin Template' // page title

// If your port is set to 80,
// use administrator privileges to execute the command line.
// For example, Mac: sudo npm run
// You can change the port by the following methods:
// port = 9528 npm run dev OR npm run dev --port = 9528
const port = process.env.port || process.env.npm_config_port || 8000 // dev port
// 环境区分主要为开发环境与其他环境（其他：生产，uat，测试等等）
const isNotDevelopMentEnv = process.env.NODE_ENV !== 'development'
// 非正式环境：包括开发环境与测试环境
const isNotRegularEnv = process.env.VUE_APP_ENV === 'development' ||
                        process.env.VUE_APP_ENV === 'test'
const cdnData = {
    css: [
        'https://cdn.bootcdn.net/ajax/libs/element-ui/2.13.0/theme-chalk/index.css'
    ],
    js: [
        'https://cdn.bootcdn.net/ajax/libs/vue/2.6.10/vue.min.js',
        'https://cdn.bootcdn.net/ajax/libs/axios/0.19.2/axios.min.js',
        'https://cdn.bootcdn.net/ajax/libs/vuex/3.1.0/vuex.min.js',
        'https://cdn.bootcdn.net/ajax/libs/vue-router/3.0.6/vue-router.min.js',
        'https://cdn.bootcdn.net/ajax/libs/element-ui/2.13.0/index.js',
        'https://cdn.bootcdn.net/ajax/libs/jquery/1.12.1/jquery.min.js',
        'https://cdn.bootcdn.net/ajax/libs/vee-validate/2.0.0-rc.21/vee-validate.min.js',
        'https://cdn.bootcdn.net/ajax/libs/vee-validate/2.0.0-rc.21/locale/zh_CN.js'
    ],
    externals: {
        'vue': 'Vue',
        'vuex': 'Vuex',
        'vue-router': 'VueRouter',
        'element-ui': 'ELEMENT',
        'vuex': 'Vuex',
        'axios': 'axios',
        'vee-validate': 'VeeValidate',
        'jQuery':"jquery",
        'jquery': 'window.$'
    }
}
// All configuration item explanations can be find in https://cli.vuejs.org/config/
module.exports = {
  /**
   * You will need to set publicPath if you plan to deploy your site under a sub path,
   * for example GitHub Pages. If you plan to deploy your site to https://foo.github.io/bar/,
   * then publicPath should be set to "/bar/".
   * In most cases please use '/' !!!
   * Detail: https://cli.vuejs.org/config/#publicpath
   */
    publicPath: '/zydb_front',
    outputDir: 'zydb_front',
    assetsDir: 'static',
    lintOnSave: !isNotDevelopMentEnv,
    // TODO 是否启动问题源码追踪
    productionSourceMap: isNotRegularEnv,
    css: {
        loaderOptions: {
            sass: {
                data: `@import "@/styles/global.scss";`
            }
        }
    },
    // 兼容ie浏览器
    // entry: ['babel-polyfill', './app/js'],

    devServer: {
        port: port,
        open: false,
        compress: true,
        overlay: {
            warnings: false,
            errors: true
        },
        proxy: {
            // 跨域处理：可用于访问本地文件
            [process.env.VUE_APP_BASE_API]: {
                target: `http://localhost:${port}/`,
                changeOrigin: true,
                pathRewrite: {
                    ['^' + process.env.VUE_APP_BASE_API]: ''
                }
            }
        },
    },
    configureWebpack: {
        // provide the app's title in webpack's name field, so that
        // it can be accessed in index.html to inject the correct title.
        name: name,
        resolve: {
            alias: {
                '@': resolve('src')
            }
        },
        plugins: [
            new webpack.ProvidePlugin({
                $:"jquery",
                jQuery:"jquery",
                "windows.jQuery":"jquery"
            }),
        ],
        // 性能提醒
        performance: {
            // 提醒方式
            hints: "warning",
            // 文件大小峰值控制（单位：kb）
            maxAssetSize: 10000
        },
        externals: {}
    },
    chainWebpack (config) {
        config.plugins.delete('preload') // TODO: need test
        config.plugins.delete('prefetch') // TODO: need test
        // 配置启用打包文件分析
        if (isNotDevelopMentEnv) {
            // 开启图片压缩-使用异常，暂时注释
            // config.module.rule('images')
            //     .test(/\.(png|jpe?g|gif|svg)(\?.*)?$/)
            //     .use('image-webpack-loader')
            //     .loader('image-webpack-loader')
            //     .options({ bypassOnDebug: true })
            // js文件包分析
            if (process.env.npm_config_report) {
                config
                    .plugin('webpack-bundle-analyzer')
                    .use(BundleAnalyzerPlugin)
                    .end()
            }
            // 配置html文件引入变量
            // config.plugin('html')
            //     .tap(args => {
            //         args[0].cdn = cdnData
            //         return args
            //     })
            // gzip压缩
            config.plugin('compression').use(CompressionPlugin, [
                {
                  algorithm: 'gzip',
                  test: new RegExp('\\.(js|css)$'),
                  threshold: 10240, //超过多少字节进行压缩
                  minRatio: 0.8 //至少压缩到原来体积的0.8,才会进行压缩
                }
            ])
        }
        // set svg-sprite-loader
        config.module
            .rule('svg')
            .exclude.add(resolve('src/icons'))
            .end()
        config.module
            .rule('icons')
            .test(/\.svg$/)
            .include.add(resolve('src/icons'))
            .end()
            .use('svg-sprite-loader')
                .loader('svg-sprite-loader')
                .options({
                    symbolId: 'bd-[name]'
                })
            .end()
        // 新增去除svg loader，去除svg默认fill
        config.module
            .rule('deleteSvgFill')
            .test(/\.svg$/)
            .include.add(resolve('src/icons'))
            .end()
            // 排除框架多色图标文件夹，保留fill属性
            .exclude.add(resolve('src/icons/svg/frame/mixed'))
            .end()
            // 排除业务多色图标文件夹，保留fill属性
            .exclude.add(resolve('src/icons/svg/business/mixed'))
            .end()
            .use('svgo-loader')
                .loader('svgo-loader')
                .tap(options => ({
                    ...options, plugins: [{removeAttrs: {attrs: 'fill'}}]
                }))
            .end()

        // set preserveWhitespace
        config.module
            .rule('vue')
            .use('vue-loader')
            .loader('vue-loader')
            .tap(options => {
                options.compilerOptions.preserveWhitespace = true
                return options
            })
            .end()

        config
            // https://webpack.js.org/configuration/devtool/#development
            .when(!isNotDevelopMentEnv,
                config => config.devtool('cheap-source-map')
            )

        config
            .when(isNotDevelopMentEnv,
                config => {
                    config
                        .plugin('ScriptExtHtmlWebpackPlugin')
                        .after('html')
                        .use('script-ext-html-webpack-plugin', [{
                            // `runtime` must same as runtimeChunk name. default is `runtime`
                            inline: /runtime\..*\.js$/
                        }])
                        .end()
                    config
                        .optimization.splitChunks({
                            chunks: 'all',
                            cacheGroups: {
                                libs: {
                                    name: 'chunk-libs',
                                    test: /[\\/]node_modules[\\/]/,
                                    priority: 10,
                                    chunks: 'initial' // only package third parties that are initially dependent
                                },
                                elementUI: {
                                    name: 'chunk-elementUI', // split elementUI into a single package
                                    priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                                    test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
                                },
                                commons: {
                                    name: 'chunk-commons',
                                    test: resolve('src/components'), // can customize your rules
                                    minChunks: 3, //  minimum common number
                                    priority: 5,
                                    reuseExistingChunk: true
                                }
                            }
                        })
                    config.optimization.runtimeChunk('single')
                }
            )
    }
}
