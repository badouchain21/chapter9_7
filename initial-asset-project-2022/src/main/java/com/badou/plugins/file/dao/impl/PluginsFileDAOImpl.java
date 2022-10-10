package com.badou.plugins.file.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.plugins.file.dao.IPluginsFileDAO;
import com.badou.plugins.file.model.PluginsFileEntity;

/**
 *	系统插件文件dao接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:55:40.739
 * @version v1.0
 * @revision 
 */
@Repository
public class PluginsFileDAOImpl extends
		BaseHibernateDAO<PluginsFileEntity, Serializable> implements IPluginsFileDAO {
	
}
 
 
 
 