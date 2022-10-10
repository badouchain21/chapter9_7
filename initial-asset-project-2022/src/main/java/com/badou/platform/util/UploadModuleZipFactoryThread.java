package com.badou.platform.util;

import java.util.HashMap;
import java.util.Map;

import com.badou.platform.PlatformConst;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.project.common.util.ProjectPropertiesLoader;
import org.apache.log4j.Logger;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:01:55
 * @todo 上传模型zip包工厂线程
 */
public class UploadModuleZipFactoryThread implements Runnable{

	protected static Logger logger = Logger.getLogger(UploadModuleZipFactoryThread.class);

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
	 * @date 2019年7月2日下午4:51:03
	 * @todo 有参构造方法 
	 * @param oper
	 * @param resourceId
	 * @param dataMap
	 */
	public UploadModuleZipFactoryThread(ResourceOperEntity oper,String resourceId,Map<String,String> dataMap){
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
			dataMap.put("type", PlatformConst.RESOUTCE_TYPE_MODULE+"");
 			UploadModuleZipFactory.getInstance().uploadFile(oper,resourceId,dataMap);
		} catch (Exception e) {
			logger.error(e);
		}
		 
	}

}
