package com.badou.project.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.badou.brms.system.container.InterfaceRegisterContainer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.badou.brms.quartz.server.QuartzPropertiesLoader;
import com.badou.brms.quartz.service.IQwTemplateService;
import com.badou.brms.util.InstanceFactory;
import com.badou.tools.common.cfg.ConfigContainer;

/**
 * @author chenjiabao
 * @date 2019年7月1日下午12:16:02
 * @todo Quartz启动监听器
 */
public class QuartzStartupLoaderListener implements ApplicationListener<ApplicationReadyEvent> , ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("监听器初始化");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ConfigContainer.getInstance().destroy();
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		// 加载properties配置文件
		ConfigContainer.getInstance().register(QuartzPropertiesLoader.class);
		// 启动全部的定时任务
		IQwTemplateService qwTemplateService = InstanceFactory
				.getInstance(IQwTemplateService.class);
		qwTemplateService.startAllQwJobs();

		System.out.println("系统启动完成监听器启动.....");
	}

}
