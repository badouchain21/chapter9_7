package com.badou;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
 
/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:44:58
 * @todo 会话工厂配置类
 */
@Configuration
public class SessionFactoryConfig {

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:52:33
	 * @todo 配置hibernate事务管理实例bean
	 * @param hemf
	 * @return hibernate事务管理
	 */
	@Bean
	 public HibernateTransactionManager transactionManager(HibernateEntityManagerFactory hemf) {
		return new HibernateTransactionManager(hemf.getSessionFactory());
		 
	 }
	 
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:53:34
	 * @todo 配置会话工厂
	 * @param hemf
	 * @return SessionFactory
	 */
	@Bean
	public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
		return hemf.getSessionFactory();
	}
	
}
