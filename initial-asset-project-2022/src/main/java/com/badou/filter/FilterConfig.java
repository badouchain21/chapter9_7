package com.badou.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjiabao
 * @date 2019年7月1日下午8:59:21
 * @todo 过滤器配置类
 */
public class FilterConfig {

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:04:54
	 * @todo 注册相应过滤器
	 * @return 过滤器注册相关bean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean registFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		/*registration.setFilter(new LogonInfoFilter());
		registration.addUrlPatterns("/*");
		registration.setName("LogonInfoFilter");
		registration.setOrder(1);*/
		return registration;
	}
}
