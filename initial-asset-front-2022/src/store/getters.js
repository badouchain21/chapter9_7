const getters = {
    sidebar: state => state.app.sidebar,
    device: state => state.app.device,
    token: state => state.user.token,
    userInfo: state => state.user.userInfo,
    avatar: state => state.user.avatar,
    name: state => state.user.name,
    // 获取首页地址
    homeUrl: state => state.user.homeUrl,
    permissionRoutes: state => {
        state.permission.routes[state.permission.routesActiveIndex].isActive = true
        return state.permission.routes
    },
    routesActiveIndex: state => state.permission.routesActiveIndex,
    settingTitle: state => state.settings.projectSetting.title,
    settingHeadBgImg: state => state.settings.projectSetting.headBgImg,
    settingLoginBgImg: state => state.settings.projectSetting.loginBgImg,
    settingLogoImg: state => state.settings.projectSetting.logoImg,
    settingLogo: state => state.settings.projectSetting.logo,
    hasSettingStatus: state => state.settings.hasSettingStatus,
    location: state => state.settings.location,
    // 启用验证码状态
    isUseVerifyCode: state => state.settings.projectSetting.isUseVerifyCode,
    publicityTitle: state => state.settings.publicitySetting.title,
    publicityBackground: state => state.settings.publicitySetting.background,
}
export default getters
