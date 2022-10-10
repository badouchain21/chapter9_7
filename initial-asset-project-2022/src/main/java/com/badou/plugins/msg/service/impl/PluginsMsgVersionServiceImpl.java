package com.badou.plugins.msg.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.plugins.msg.dao.IPluginsMsgVersionDAO;
import com.badou.plugins.msg.model.PluginsMsgVersionEntity;
import com.badou.plugins.msg.service.IPluginsMsgVersionService;


/**
 *	sys_plugins_msg_version Service接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:53.816
 * @version v1.0
 * @revision 
 */
@Service
public class PluginsMsgVersionServiceImpl extends
		BaseSpringService<PluginsMsgVersionEntity, Serializable> implements IPluginsMsgVersionService {
		
	@Autowired
	private IPluginsMsgVersionDAO pluginsMsgVersionDAO;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:31:08
	 * @todo 注入dao
	 * @param pluginsMsgVersionDAO
	 */
	@Autowired
	public void setPluginsMsgVersionDAO(IPluginsMsgVersionDAO pluginsMsgVersionDAO) {
		this.pluginsMsgVersionDAO = pluginsMsgVersionDAO;
		super.setBaseDAO(pluginsMsgVersionDAO);
	}
	
}
 
 
 
 