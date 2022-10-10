package com.badou.platform.util;

import org.apache.log4j.Logger;

import com.badou.platform.model.ResourceOperEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:58:33
 * @todo 下载模型zip包工厂线程类
 */
public class DownloadModuleZipFactoryThread implements Runnable{

	protected static Logger logger = Logger.getLogger(DownloadModuleZipFactoryThread.class);

	ResourceOperEntity oper;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:33:25
	 * @todo 有参构造
	 * @param oper 资源操作实体
	 */
	public DownloadModuleZipFactoryThread(ResourceOperEntity oper){
		this.oper = oper;
	}
	@Override
	public void run() {
		try {
			DownloadModuleZipFactory.getInstance().downloadFile(oper);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
