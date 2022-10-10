package com.badou.plugins.log.dao.impl;

import java.io.Serializable;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.plugins.log.dao.IPluginsLogDAO;
import com.badou.plugins.log.model.PluginsLogEntity;

/**
 *	sys_plugins_logsdao接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:16.853
 * @version v1.0
 * @revision 
 */
@Repository
public class PluginsLogDAOImpl extends
		BaseHibernateDAO<PluginsLogEntity, Serializable> implements IPluginsLogDAO {

	@Override
	public void updateTruncateTable() {
		this.getSession().createSQLQuery("TRUNCATE TABLE sys_plugins_logs").executeUpdate();
		
	}
	
	@Override
	public void updatePluginsState(String code , String id ,String beanName){
		Query sqlQuery1 = this.getSession().createQuery("update PluginsLogEntity set state = 0 where code = ?  ");
		Query sqlQuery2 = this.getSession().createQuery("update PluginsLogEntity set state = 1 where id = ?  ");
		
		Query sqlQuery3 = this.getSession().createQuery("update PluginsMsgEntity set beanName = 0 where code = ?  ");
		
		sqlQuery1.setParameter(0,code);
		sqlQuery2.setParameter(0, id);
		sqlQuery3.setParameter(0,code);
		
		sqlQuery1.executeUpdate();
		sqlQuery2.executeUpdate();
		sqlQuery3.executeUpdate();
	}
}
 
 
 
 