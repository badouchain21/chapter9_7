import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/service/auth' // get token from cookie
// import getPageTitle from '@/utils/get-page-title'
import setPageFavicon from '@/utils/set-page-favicon'
// 根据类型返回资源路径函数
import returnSrc from '@/service/return-src'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login','/search','/platform/show'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
    // window.onload = function(){
    //     //屏蔽键盘事件
    //     document.onkeydown = function (){
    //         var e = window.event || arguments[0];
    //         //F12
    //         if(e.keyCode == 123){
    //             return false;
    //         //Ctrl+Shift+I
    //         }else if((e.ctrlKey) && (e.shiftKey) && (e.keyCode == 73)){
    //             return false;
    //         //Shift+F10
    //         }else if((e.shiftKey) && (e.keyCode == 121)){
    //             return false;
    //         //Ctrl+U
    //         }else if((e.ctrlKey) && (e.keyCode == 85)){
    //             return false;
    //         }
    //     };
    //     //屏蔽鼠标右键
    //     document.oncontextmenu = function (){
    //         return false;
    //     }
    // }

    // start progress bar
    NProgress.start()
    if (!store.state.settings.hasSettingStatus) {
        await store.dispatch('settings/getProjectSetting')
        await store.dispatch('settings/getPublicitySetting')
    }
    // set page title
    document.title = store.state.settings.projectSetting.rootTitle || '开发平台'
    // determine whether the user has logged in
    const hasToken = getToken()
    // console.log(`hasToken: ${hasToken}`)
    // 判断token是否失效:项目没有使用token,所以这里不需要
    if (hasToken) {
        if (to.path === '/login') {
            // 登录后还跳去登录，则强制返回首页
            next({ path: '/' })
            NProgress.done()
        } else {
            const hasGetUserInfo = store.getters.name
            if (hasGetUserInfo) {
                next()
            } else {
                try {
                    // 设置项目标签页favicon-修改为此位置，避免重复请求
                    setPageFavicon(returnSrc(store.state.settings.projectSetting.favicon, 'favicon'))
                    // 获取用户信息
                    await store.dispatch('user/getInfo')
                    // 获取该用户下菜单路由(权限管理)
                    let menuRoutes = await store.dispatch('user/getUserRoutes')
                    let routerList = await store.dispatch('permission/generateRoutes', menuRoutes ? menuRoutes : [])
                    let route404 = {
                        path: '*',
                        redirect: '/404'
                    }
                    router.addRoutes(routerList)
                    router.addRoutes([route404])
                    // 使用replace:true,避免刷新当前页面时重复添加路由,导致后退失效
                    next({...to, replace: true})
                } catch (error) {
                    console.error(`
                    =======================================================================================
                    =======================出现这个说明问题被捕获了,需要关掉try/catch才能看到问题 -wjx=============
                    =======================================================================================
                    `)
                    console.error(JSON.stringify(error))
                    // remove token and go to login page to re-login
                    await store.dispatch('user/resetToken')
                    Message.error(error || 'Has Error')
                    next(`/login?redirect=${to.path}`)
                    NProgress.done()
                }
            }
        }   
    } else {
        // 设置项目标签页favicon-修改为此位置，避免重复请求
        setPageFavicon(returnSrc(store.state.settings.projectSetting.favicon, 'favicon'))
        /* has no token*/
        if (whiteList.indexOf(to.path) !== -1) {
            // in the free login whitelist, go directly
            next()
        } else {
            // other pages that do not have permission to access are redirected to the login page.
            next(`/login?redirect=${to.path}`)
            NProgress.done()
        }
    }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
