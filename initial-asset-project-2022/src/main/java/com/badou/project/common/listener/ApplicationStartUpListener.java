package com.badou.project.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.badou.brms.system.container.InterfaceRegisterContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.badou.brms.cfg.syscache.SystemCacheContainer;
import com.badou.brms.dictionary.DictionaryCacheContainer;
import com.badou.brms.system.security.UserRoleResourceCacheContainer;
import com.badou.brms.system.security.service.ILogonLogService;
import com.badou.brms.util.InstanceFactory;
import com.badou.logs.syslog.performace.ResourceCacheContainer;
import com.badou.logs.syslog.performace.config.ConfigFactory;
import com.badou.project.common.util.ProjectPropertiesLoader;
import com.badou.tools.common.cache.CacheFactory;
import com.badou.tools.common.cfg.ConfigContainer;
import com.badou.tools.common.cfg.system.DefaultPropertiesLoader;
import com.badou.tools.common.properties.CacheProperties;
import com.badou.tools.common.util.SpringHelper;
import com.badou.uniapp.cache.AppUserRoleResourceCacheContainer;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author chenjiabao
 * @date 2019年7月1日下午12:19:51
 * @todo 应用程序启动监听类
 * ApplicationEnvironmentPreparedEvent 在应用启动时进行监听
 * ApplicationReadyEvent在应用启动完成后进行监听
 * 取代原先框架中的APPstartuplistener
 */
@Component
public class ApplicationStartUpListener implements ServletContextListener {

//    @Autowired
//    WebApplicationContext webApplicationContext;
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//
//        InstanceFactory.setServletContent(webApplicationContext.getServletContext());
//        ConfigContainer.getInstance().register(CacheProperties.class);
//        CacheFactory.getInstance().init();
//
//        ConfigContainer.getInstance().register(ProjectPropertiesLoader.class);
//        ConfigContainer.getInstance().register(DefaultPropertiesLoader.class);
//
//        DictionaryCacheContainer.getInstance().init();
//        SystemCacheContainer.getInstance().init();
//
//        // 注销未注销的登录日志
//        //ILogonLogService logonLogService = SpringHelper.getBean(ILogonLogService.class);
//        /*logonLogService.updateLogoutData(null);        */
//
//        //PC菜单 按钮加载
//        UserRoleResourceCacheContainer.getInstance().init();
//        //APP菜单加载
//        AppUserRoleResourceCacheContainer.getInstance().init();
//
//        ConfigFactory.configure(this.getClass().getClassLoader().getResource("").getPath());
//        //   DatabaseCacheContainer.getInstance().init();
////        ResourceCacheContainer.getInstance().init();
//
//        //扫描系统中所有接口，并在数据库中进行注册
//
//        InterfaceRegisterContainer.getInstance().register(webApplicationContext);
//        System.out.println("初始化完成");
//
//    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());        InstanceFactory.setServletContent(webApplicationContext.getServletContext());
        ConfigContainer.getInstance().register(CacheProperties.class);
        SpringHelper springHelper= (SpringHelper) webApplicationContext.getBean("springHelper");
        springHelper.setApplicationContext(webApplicationContext);
        InstanceFactory.setServletContent(webApplicationContext.getServletContext());
        CacheFactory.getInstance().init();

        ConfigContainer.getInstance().register(ProjectPropertiesLoader.class);
        ConfigContainer.getInstance().register(DefaultPropertiesLoader.class);


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


}
