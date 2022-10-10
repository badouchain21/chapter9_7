<template>
    <div class="login-container" :style="{backgroundImage: makeImg(settingLoginBgImg, 'bg')}">
        <el-row  class="top-button">
            <el-button type="default" class="button-class" @click="publicity">公示平台</el-button>
        </el-row>
        <p class="subject-title">{{settingTitle}}</p>
        <div class="login-panel">
            <div class="titlt-area">
                <img v-if="settingLogoImg" :src="makeImg(settingLogoImg)" alt="">
            </div>
            <el-form class="login-form" ref="loginForm" :model="loginForm" :rules="loginRules">
                <el-form-item prop="username">
                    <bd-icon name="user" class="iconSvg"></bd-icon>
                    <el-input
                        ref="username"
                        v-model="loginForm.username"
                        placeholder="请输入账户"
                        name="username"
                        type="text"
                        tabindex="1"
                        auto-complete="on">
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <bd-icon name="password" class="iconSvg"></bd-icon>
                    <el-input
                        :key="passwordType"
                        ref="password"
                        v-model="loginForm.password"
                        :type="passwordType"
                        placeholder="请输入密码"
                        name="password"
                        tabindex="2"
                        auto-complete="on">
                    </el-input>
                    <!-- <span class="show-pwd" @click="showPwd">
                        <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                    </span> -->
                </el-form-item>
                <el-form-item prop="imageCode" v-if="isUseVerifyCode == 1">
                    <bd-icon name="verifiCode" class="iconSvg"></bd-icon>
                    <el-input
                        ref="imageCode"
                        v-model="loginForm.imageCode"
                        placeholder="请输入验证码"
                        name="imageCode"
                        type="text"
                        tabindex="3"
                        auto-complete="on">
                    </el-input>
                    <check-code 
                        ref="checkCode" 
                        :auto="false"
                        :code="imageCode"
                        @refresh="getCode"/>
                </el-form-item>
                <el-button 
                    :loading="loading" 
                    type="primary" 
                    size="large"
                    class="w-per-100" 
                    @click.native.prevent="handleLogin">
                    登录
                </el-button>
                <!-- <div class="textR forgetBtn">
                    <span>忘记密码?</span>
                </div> -->
            </el-form>
        </div>
    </div>
</template>

<script>
import { isStrEmpty } from '@/utils/index'
import CheckCode from '@/components/frame/Common/CheckCode'
import { Table } from 'element-ui'
import { mapGetters } from 'vuex'
import { getVerificationCode } from '@/api/frame/index'
export default {
    name: 'Login',
    components: {
        CheckCode,
    },
    computed: {
        ...mapGetters([
            'settingTitle',
            'settingLogoImg',
            'settingLoginBgImg',
            'settingHeadBgImg',
            'isUseVerifyCode'
        ]),
    },
    data () {
        window.imgCode = ''
        const validateUsername = (rule, value, callback) => {
            if (value.length === 0) {
                callback(new Error('用户名无效,请重新输入'))
            } else {
                callback()
            }
        }
        const validatePassword = (rule, value, callback) => {
            if (value.length < 6) {
                callback(new Error('密码不少于6位'))
            } else {
                callback()
            }
        }
        const validateImageCode = (rule, value, callback) => {
            if (isStrEmpty(value)) {
                callback(new Error('请输入图形验证码'))
            }
            if (value.toUpperCase() !== window.imgCode.toUpperCase()) {
                // 验证码输入错误,重置验证码
                callback(new Error('图形验证码错误'))
            } else {
                callback()
            }
        }
        return {
            imageCode: '',
            loginForm: {
                username: '',
                password: '',
                imageCode: '',
                uuid: ''
            },
            loginRules: {
                username: [{ required: true, trigger: 'blur', validator: validateUsername }],
                password: [{ required: true, trigger: 'blur', validator: validatePassword }],
                imageCode: [{ required: true, trigger: 'blur', validator: validateImageCode }]
            },
            loading: false,
            passwordType: 'password',
            redirect: undefined
        }
    },
    watch: {
        $route: {
            handler: function (route) {
                // 判断是否存在重定向模块,登录成功之后将跳转到指定位置,一般用于token失效后重新登录回到之前浏览页面
                // route.query可能值为 {
                //     redirect: '',
                //     其他参数键: 值
                // }
                // 需要将redirect属性与其他属性拼装返回完整重定向地址-->makeAllQueryParams函数作用
                this.redirect = route.query && this.makeAllQueryParams(route.query)
            },
            immediate: true
        }
    },
    methods: {
        // 根据路由query参数对象拼装完整路径
        makeAllQueryParams (routeQueryObject) {
            // 获取重定向地址--此时未获取其余参数
            let redirect = routeQueryObject.redirect
            // 删除数据源中reirect属性,剩下均为其余参数
            delete routeQueryObject['redirect']
            // 获取参数键数组
            let keyList = Object.keys(routeQueryObject)
            keyList.forEach((i, index) => {
                // 拼装完整重定向地址(含参数)
                redirect +=`&${i}=${routeQueryObject[i]}`
            })
            return redirect
        },
        // 图形校验码绘制成功后,回调函数赋值
        getCode (data) {
            // 有回调值表示当前为验证码组件自动生成验证码
            if (data) {
                window.imgCode = data
                this.imageCode = data
            } else {
                // 验证码来自接口
                getVerificationCode({}).then(res => {
                    if (res) {
                        let { code, uuid } = res
                        window.imgCode = code || ''
                        this.imageCode = code || ''
                        this.$set(this.loginForm, 'uuid', uuid)
                    }
                })
            }
        },
        showPwd () {
            if (this.passwordType === 'password') {
                this.passwordType = ''
            } else {
                this.passwordType = 'password'
            }
            this.$nextTick(() => {
                this.$refs.password.focus()
            })
        },
        handleLogin () {
            var that = this
            this.loading = true
            setTimeout(() => {
                this.$refs.loginForm.validate(valid => {
                    if (valid) {
                        let form = {
                            username: this.loginForm.username,
                            password: this.loginForm.password,
                            CAPTCHA: this.loginForm.imageCode,
                            uuid: this.loginForm.uuid
                        }
                        this.$store.dispatch('user/login', form).then((res) => {
                            if (res.status) {
                                this.$router.push({ path: this.redirect || '/' })
                            } else {
                                this.$message({
                                    message: res.message,
                                    type: 'warning'
                                })
                                that.getCode()
                            }
                        }).catch((err) => {
                            console.error(err)
                        }).finally(() => {
                            setTimeout(() => {
                                this.loading = false
                            }, 1000)
                        })
                    } else {
                        that.getCode()
                        this.loading = false
                        console.log('error submit!!')
                        return false
                    }
                })
            })
        },
        publicity () {
            this.$router.push('/search');
        }
    },
    created () {
        // 获取验证码
        this.getCode()
    },
    mounted () {
        // 注册键盘事件
        this.keyDown({ 'Enter': this.handleLogin })
    }
  }
</script>
<style lang="scss" scoped>
.iconSvg {
    margin: 0px 10px;
}
.login-container {
    background-color: #2e3138;
    // background: url("../../assets/project/bg.png");
    background-size: 100% 100%;
    background-attachment: fixed;
    min-height: 100%;
    width: 100%;
    overflow: hidden;
    .subject-title {
        padding: 10px 30px;
        font-size: 28px;
        letter-spacing: 1px;
        color: #fff;
        animation: sliceTop 0.9s ease ;
    }
    .login-panel {
        width: 24%;
        background: #fff;
        min-width: 400px;
        margin: auto;
        margin-top: 8%;
        border-radius: 4px;
    }
    .titlt-area {
        width: 100%;
        text-align: center;
        padding: 15px 0px;
        img {
            height: 80px;
        }
    }
    .login-form {
        padding: 0px 10% 20px;
        margin: auto;
        .el-form-item {
            border-bottom: 1px solid #eee;
            color: #999;
            margin-bottom:  20px;
            &:last-type-of-child {
                margin-bottom: 30px;
            }
        }
        .forgetBtn {
            padding: 10px 0px;
            color: #666;
            font-size: 12px;
            cursor: pointer;
        }
        .show-pwd {
            position: absolute;
            right: 10px;
            top: 0px;
            font-size: 16px;
            color: #eee;
            cursor: pointer;
            user-select: none;
        }
    }
    @keyframes sliceTop {
        0% {
            margin-top: 2em;
        }
        100% {
            margin-top: 1em;
        }
    }
    .top-button {
        float: right;
        margin-top: 2%;
        margin-right: 2%;
    }
}

</style>

<style lang="scss">
.login-container {
    .el-input {
        display: inline-block;
        height: 30px;
        width: 85%;
        input {
            border: 0px;
            border-radius: 0px;
            padding: 5px;
            height: 30px;
        }
    }
}
</style>