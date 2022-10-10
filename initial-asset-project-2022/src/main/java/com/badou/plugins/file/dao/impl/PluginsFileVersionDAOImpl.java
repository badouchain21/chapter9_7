package com.badou.plugins.file.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.plugins.file.dao.IPluginsFileVersionDAO;
import com.badou.plugins.file.model.PluginsFileVersionEntity;

/**
 *	插件文件版本dao接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:55:59.359
 * @version v1.0
 * @revision 
 */
@Repository
public class PluginsFileVersionDAOImpl extends
		BaseHibernateDAO<PluginsFileVersionEntity, Serializable> implements IPluginsFileVersionDAO {
	
}
 
 
 
 