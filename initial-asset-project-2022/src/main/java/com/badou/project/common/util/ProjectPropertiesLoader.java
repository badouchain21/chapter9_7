package com.badou.project.common.util;

import java.io.IOException;
import java.util.Properties;

import com.badou.tools.common.cfg.ConfigContainer;
import com.badou.tools.common.cfg.ConfigLocalLoader;
import com.badou.tools.common.cfg.PropertiesConfigLocalLoader;
import com.badou.tools.common.cfg.PropertyUtils;
/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:16:41
 * @todo 读取项目配置信息
 */
public class ProjectPropertiesLoader extends PropertiesConfigLocalLoader
		implements ConfigLocalLoader{

	public final static String FILENAME = "project.properties";
	
	public final static String USER_ID = "project.userid";
	
	public final static String TOKEN = "project.token";
	
	public final static String PROJECT_ID = "project.id";
	
	public final static String PLATFORM_URL = "platform.url";

	@Override
	public String getFileName() {
		return FILENAME;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:06:42
	 * @todo 无参构造
	 */
	public ProjectPropertiesLoader(){
		try {
			if(prop == null) {
				prop = new Properties();
				prop.load(PropertiesConfigLocalLoader.class.getClassLoader().getResourceAsStream(this.getFileName()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	private Properties prop = null;
	
	@Override
	public void load(){
		prop = null;
		try {
			prop = new Properties();
			prop.load(PropertiesConfigLocalLoader.class.getClassLoader().getResourceAsStream(this.getFileName()));
		} catch (IOException e) {
			logger.error(String.format("加载配件文件[%s]出错!", this.getFileName()), e);
		}
		if(prop != null){
			ConfigContainer.getInstance().registerCache(this.getFileName(), prop);
		}
		
		this.setHasLoad(true);
	}

	public static String getUserId() {
		return PropertyUtils.getProperty(USER_ID, FILENAME);
	}


	public static String getToken() {
		return PropertyUtils.getProperty(TOKEN, FILENAME);
	}

	public static String getProjectId() {
		return PropertyUtils.getProperty(PROJECT_ID, FILENAME);
	}
	
	public static String getPlatformUrl() {
		return PropertyUtils.getProperty(PLATFORM_URL, FILENAME);
	}
}
