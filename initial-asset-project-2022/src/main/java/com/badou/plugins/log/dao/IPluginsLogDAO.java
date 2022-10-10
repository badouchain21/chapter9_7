package  com.badou.plugins.log.dao;

import java.io.Serializable;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.plugins.log.model.PluginsLogEntity;

/**
 *	sys_plugins_logsdao接口
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:16.853
 * @version v1.0
 * @revision 
 */
public interface IPluginsLogDAO extends IBaseHibernateDAO<PluginsLogEntity, Serializable> {

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:52:11
	 * @todo 清空插件日志表
	 */
	public void updateTruncateTable();
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:52:56
	 * @todo 更新插件状态
	 * @param code
	 * @param id
	 * @param beanName
	 */
	public void updatePluginsState(String code , String id ,String beanName );
}
