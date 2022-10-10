package com.badou.plugins.file.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.plugins.file.dao.IPluginsFileVersionDAO;
import com.badou.plugins.file.model.PluginsFileVersionEntity;
import com.badou.plugins.file.service.IPluginsFileVersionService;


/**
 *	插件文件版本 Service接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:55:59.359
 * @version v1.0
 * @revision 
 */
@Service
public class PluginsFileVersionServiceImpl extends
		BaseSpringService<PluginsFileVersionEntity, Serializable> implements IPluginsFileVersionService {
		
	@Autowired
	private IPluginsFileVersionDAO pluginsFileVersionDAO;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:48:29
	 * @todo 注入dao
	 * @param pluginsFileVersionDAO
	 */
	@Autowired
	public void setPluginsFileVersionDAO(IPluginsFileVersionDAO pluginsFileVersionDAO) {
		this.pluginsFileVersionDAO = pluginsFileVersionDAO;
		super.setBaseDAO(pluginsFileVersionDAO);
	}
	
}
 
 
 
 