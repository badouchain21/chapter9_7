package com.badou.plugins.helper;

import com.badou.tools.common.util.SpringHelper;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:32:58
 * @todo 插件帮助类
 */
public class PluginsHelper {
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:37:04
	 * @todo 获取spring IOC对象
	 * @param code
	 * @return spring ioc容器对象
	 */
	public static Object getBean(String code){
		return SpringHelper.getBean(code);
	}
}
