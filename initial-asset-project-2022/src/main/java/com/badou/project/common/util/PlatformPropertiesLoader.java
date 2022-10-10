package com.badou.project.common.util;

import com.badou.tools.common.cfg.ConfigLocalLoader;
import com.badou.tools.common.cfg.PropertiesConfigLocalLoader;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:18:03
 * @todo 平台配置读取类
 */
public class PlatformPropertiesLoader extends PropertiesConfigLocalLoader
		implements ConfigLocalLoader {

	/**
	 * 配置文件
	 */
	public static final String CONFIG_FILE = "platform.properties";
	
	@Override
	public String getFileName() {
		return CONFIG_FILE;
	}
	
	public static final String SYS_ORG_TEACHER = "SYS_ORG_TEACHER";
	public static final String SYS_ROLE_TEACHER ="SYS_ROLE_TEACHER";

}
