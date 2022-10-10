
package com.badou;

import com.bdc.zeromq.ZeroMqConst;
import com.bdc.zeromq.ZeroMqServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

/**
 * @author chenjiabao
 * @date 2019年7月1日下午12:20:04
 * @todo 启动主程序类
 */
@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableJpaRepositories(basePackages={"com.badou","com.bdc"})//扫描指定包下所有dao层bean
@EntityScan(basePackages={"com.badou","com.bdc"})//扫描所有实体
@ComponentScan(basePackages={"com.badou","com.bdc"})
@EnableCaching
@EnableTransactionManagement(proxyTargetClass = true)
public class InitialApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(InitialApplication.class);
	}


	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:36:22
	 * TODO 程序主入口方法
	 * @param args
	 */
	public static void main (String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
 		SpringApplication.run(InitialApplication.class, args);
		ZeroMqServer.openZeroMqService(ZeroMqConst.BDC_PORT);
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:39:07
	 * @todo 绑定线程到JPA中的entityManager
	 * @return 绑定线程过滤器
	 */
	@Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
            return new OpenEntityManagerInViewFilter();
    }

}
