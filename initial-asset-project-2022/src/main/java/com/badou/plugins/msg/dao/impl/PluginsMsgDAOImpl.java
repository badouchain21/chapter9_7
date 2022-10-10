package com.badou.plugins.msg.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.plugins.msg.dao.IPluginsMsgDAO;
import com.badou.plugins.msg.model.PluginsMsgEntity;

/**
 *	sys_plugins_msgdao接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:39.504
 * @version v1.0
 * @revision 
 */
@Repository
public class PluginsMsgDAOImpl extends
		BaseHibernateDAO<PluginsMsgEntity, Serializable> implements IPluginsMsgDAO {
	
}
 
 
 
 