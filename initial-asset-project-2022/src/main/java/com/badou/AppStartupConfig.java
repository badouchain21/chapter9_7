package com.badou;

import com.badou.brms.cfg.syscache.SystemCacheContainer;
import com.badou.brms.dictionary.DictionaryCacheContainer;
import com.badou.brms.system.container.InterfaceRegisterContainer;
import com.badou.brms.system.security.UserRoleResourceCacheContainer;
import com.badou.logs.syslog.performace.config.ConfigFactory;
import com.badou.uniapp.cache.AppUserRoleResourceCacheContainer;
import com.bdc.api.rest.container.InterfaceDetailContainer;
import com.bdc.api.rest.container.InterfacePermissionContainer;
import com.bdc.api.rest.container.NetInformationContainer;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:44:58
 * @todo 会话工厂配置类
 */
@Configuration
public class AppStartupConfig {
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Bean
	 public int initApp() {
		DictionaryCacheContainer.getInstance().init();
		SystemCacheContainer.getInstance().init();

		// 注销未注销的登录日志
		//ILogonLogService logonLogService = SpringHelper.getBean(ILogonLogService.class);
		/*logonLogService.updateLogoutData(null);        */

		//PC菜单 按钮加载
		UserRoleResourceCacheContainer.getInstance().init();
		//APP菜单加载
		AppUserRoleResourceCacheContainer.getInstance().init();

		ConfigFactory.configure(this.getClass().getClassLoader().getResource("").getPath());
		//   DatabaseCacheContainer.getInstance().init();
//        ResourceCacheContainer.getInstance().init();

		//扫描系统中所有接口，并在数据库中进行注册
		InterfaceRegisterContainer.getInstance().register(webApplicationContext);
		//缓存初始化
		NetInformationContainer.getInstance().init();
		InterfaceDetailContainer.getInstance().init();
		InterfacePermissionContainer.getInstance().init();

		System.out.println("初始化完成");
		return 0;
	 }


}
