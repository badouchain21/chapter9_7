package com.badou.platform.util;

import java.util.HashMap;
import java.util.Map;

import com.badou.platform.PlatformConst;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.project.common.util.ProjectPropertiesLoader;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:55:12
 * @todo 插件zip包上传工厂线程类
 */
public class UploadPluginZipFactoryThread implements Runnable{

	private ResourceOperEntity oper;
	
	private String resourceId;
	
	private Map<String,String>  dataMap;
	
	public ResourceOperEntity getOper() {
		return oper;
	}
	public void setOper(ResourceOperEntity oper) {
		this.oper = oper;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public Map<String, String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:25:13
	 * @todo 有参构造函数
	 */
	public UploadPluginZipFactoryThread(ResourceOperEntity oper,String resourceId,Map<String,String> dataMap){
		this.resourceId = resourceId;
		this.oper = oper;
		this.dataMap = dataMap;
		
	}
	@Override
	public void run() {
		try {
			Map<String , String> dataMap = new HashMap<String , String>();
			dataMap.put("projectId", ProjectPropertiesLoader.getProjectId());
			dataMap.put("token", ProjectPropertiesLoader.getToken());
			dataMap.put("userId", ProjectPropertiesLoader.getUserId());
			dataMap.put("type", PlatformConst.RESOUTCE_TYPE_PLUGIN+"");
 			UploadPluginZipFactory.getInstance().uploadFile(oper,resourceId,dataMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		 
	}

}
