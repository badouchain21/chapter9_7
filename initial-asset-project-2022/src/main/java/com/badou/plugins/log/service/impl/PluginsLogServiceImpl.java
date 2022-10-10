package com.badou.plugins.log.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.core.plugin.PluginsCache;
import com.badou.plugins.log.dao.IPluginsLogDAO;
import com.badou.plugins.log.model.PluginsLogEntity;
import com.badou.plugins.log.service.IPluginsLogService;


/**
 *	sys_plugins_logs Service接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:16.853
 * @version v1.0
 * @revision 
 */
@Service
public class PluginsLogServiceImpl extends
		BaseSpringService<PluginsLogEntity, Serializable> implements IPluginsLogService {
		
	@Autowired
	private IPluginsLogDAO pluginsLogDAO;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:00:22
	 * @todo 注入dao
	 * @param pluginsLogDAO
	 */
	@Autowired
	public void setPluginsLogDAO(IPluginsLogDAO pluginsLogDAO) {
		this.pluginsLogDAO = pluginsLogDAO;
		super.setBaseDAO(pluginsLogDAO);
	}
	
	@Override
	public void updateTruncateTable(){
		this.pluginsLogDAO.updateTruncateTable();
	}

	@Override
	public void updatePluginsState(String code , String id ,String beanName ) {
		pluginsLogDAO.updatePluginsState(code, id , beanName);
		PluginsCache.codeMap.put(code,PluginsCache.beanMap.get(beanName));
	}
}
 
 
 
 