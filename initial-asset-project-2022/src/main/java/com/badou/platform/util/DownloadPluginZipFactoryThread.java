package com.badou.platform.util;

import com.badou.platform.model.ResourceOperEntity;
import org.apache.log4j.Logger;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:54:37
 * @todo 插件zip下载线程类
 */
public class DownloadPluginZipFactoryThread implements Runnable{

	protected static Logger logger = Logger.getLogger(DownloadPluginZipFactoryThread.class);

	ResourceOperEntity oper;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:24:28
	 * @todo 有参构造
	 */
	public DownloadPluginZipFactoryThread(ResourceOperEntity oper){
		this.oper = oper;
	}
	@Override
	public void run() {
		try {
			DownloadPluginZipFactory.getInstance().downloadFile(oper);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
