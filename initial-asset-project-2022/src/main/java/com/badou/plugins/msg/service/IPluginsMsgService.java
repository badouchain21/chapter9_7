package com.badou.plugins.msg.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.plugins.msg.model.PluginsMsgEntity;
/**
 *	$sys_plugins_msg service接口
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:39.504
 * @version v1.0
 * @revision 
 */
public interface IPluginsMsgService extends IBaseSpringService<PluginsMsgEntity, Serializable> {
	
	
	/**
	 * 批量保存对象
	 * @Title: batchSave
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void batchSave(List<PluginsMsgEntity> ts) throws IllegalAccessException, InvocationTargetException;
	
	
	/**
	 * 更新插件状态
	 * @param ids
	 * @throws Exception if has error(直接往外抛)
	 */
	public void updatePluginsStatus(String id,Integer status) throws Exception;
	
}
