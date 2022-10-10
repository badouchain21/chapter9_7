package com.badou.plugins.msg.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.plugins.msg.dao.IPluginsMsgVersionDAO;
import com.badou.plugins.msg.model.PluginsMsgVersionEntity;

/**
 *	sys_plugins_msg_versiondao接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:53.816
 * @version v1.0
 * @revision 
 */
@Repository
public class PluginsMsgVersionDAOImpl extends
		BaseHibernateDAO<PluginsMsgVersionEntity, Serializable> implements IPluginsMsgVersionDAO {
	
}
 
 
 
 