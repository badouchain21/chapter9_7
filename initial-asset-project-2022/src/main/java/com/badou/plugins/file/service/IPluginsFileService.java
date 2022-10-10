package com.badou.plugins.file.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.plugins.file.model.PluginsFileEntity;
/**
 *	$系统插件文件 service接口
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:55:40.739
 * @version v1.0
 * @revision 
 */
public interface IPluginsFileService extends IBaseSpringService<PluginsFileEntity, Serializable> {
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:43:53
	 * @todo 批量创建插件jar包
	 * @param pluginsPaths
	 * @return 结果集合
	 * @throws Exception if has error(直接往外抛)
	 */
	public Map<String, PluginsFileEntity> batchCreatePluginJar(List<String> pluginsPaths ) throws Exception;
	
}
