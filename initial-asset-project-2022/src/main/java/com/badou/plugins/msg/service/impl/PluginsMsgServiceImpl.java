package com.badou.plugins.msg.service.impl;

import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.core.plugin.PluginsCache;
import com.badou.core.plugin.PluginsTypeEnum;
import com.badou.core.plugin.agent.IPluginsAgent;
import com.badou.core.plugin.builder.basic.Plugin;
import com.badou.plugins.msg.dao.IPluginsMsgDAO;
import com.badou.plugins.msg.model.PluginsMsgEntity;
import com.badou.plugins.msg.service.IPluginsMsgService;
import com.badou.tools.common.util.bean.BeanUtils;


/**
 *	sys_plugins_msg Service接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:39.504
 * @version v1.0
 * @revision 
 */
@Service
public class PluginsMsgServiceImpl extends
		BaseSpringService<PluginsMsgEntity, Serializable> implements IPluginsMsgService {
		
	@Autowired
	private IPluginsMsgDAO pluginsMsgDAO;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:28:58
	 * @todo 注入dao
	 * @param pluginsMsgDAO
	 */
	@Autowired
	public void setPluginsMsgDAO(IPluginsMsgDAO pluginsMsgDAO) {
		this.pluginsMsgDAO = pluginsMsgDAO;
		super.setBaseDAO(pluginsMsgDAO);
	}

	@Override
	public void batchSave(List<PluginsMsgEntity> ts) throws IllegalAccessException, InvocationTargetException {
		List<PluginsMsgEntity> updateList = new ArrayList<PluginsMsgEntity>();
		List<PluginsMsgEntity> addList = new ArrayList<PluginsMsgEntity>();
		PluginsMsgEntity t = null;
		for(PluginsMsgEntity s : ts){
			t = this.findByCode(s.getCode());
			if( t != null ){
				//更新
				String useBeanName = t.getBeanName();
				int status = t.getStatus();
				 
				BeanUtils.copyPropertiesNotNull(t, s);
				t.setBeanName(useBeanName);
				t.setStatus(status);
				updateList.add(t);
				
				/*PluginsCache.codeMap.get(t.getCode()).setBeanName(useBeanName);
				PluginsCache.codeMap.get(t.getCode()).setStatus(status);*/
			}else{
				//更新
				addList.add(s);
			}
		}
		this.batchCreate(addList);
		this.batchUpdate(updateList);
		
	}

	@Override
	public void updatePluginsStatus(String id, Integer status) throws  Exception {
			PluginsMsgEntity msgEntity = this.find(id);
 			msgEntity.setStatus(status);
 			this.update(msgEntity);
 			PluginsCache.returnPlugins().get(msgEntity.getCode()).get(0).setStatus(status);
 			PluginsCache.beanMap.get(msgEntity.getCode()).setStatus(status);
 			String properties = msgEntity.getPluginsFileEntity().getProperties();
 			if(properties != null){
 				Integer agentType = msgEntity.getType();
 				Properties proper = null;
 				proper = new Properties();
	 	        proper.load(new StringReader(properties)); //把字符串转为reader
 	        	Class<?> c =  Class.forName(PluginsTypeEnum.getAgentByType(agentType).getAgentName());
 	     		Constructor<?> c1= c.getDeclaredConstructor(Plugin.class,Properties.class); 
 	     		IPluginsAgent agent = (IPluginsAgent) c1.newInstance(PluginsCache.beanMap.get(msgEntity.getCode()),proper);
 	      		agent.run();
 			}
 			
	}
	
}
 
 
 
 