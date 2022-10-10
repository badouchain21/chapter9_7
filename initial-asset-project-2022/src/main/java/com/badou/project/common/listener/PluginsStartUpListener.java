package com.badou.project.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.badou.core.plugin.builder.PluginBuilderThread;

/**
 * @author chenjiabao
 * @date 2019年7月1日下午3:26:36
 * @todo 插件相关启动监听器
 */
public class PluginsStartUpListener implements ApplicationListener<ApplicationReadyEvent> , ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		new PluginBuilderThread().run();
	}

}
