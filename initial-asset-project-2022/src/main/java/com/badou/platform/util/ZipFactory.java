package com.badou.platform.util;

import java.io.File;
import java.text.SimpleDateFormat;

import com.badou.brms.attach.AttachConfig;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:00:20
 * @todo zip压缩包工厂类
 */
public class ZipFactory {
	
	/**
	 * 获取文件名称
	 * 
	 * @param zipTagName
	 * @param version
	 * @return 文件名
	 * @throws Exception if has error(直接往外抛)
	 */
	public static String getFileName(String zipTagName, String version)
			throws Exception {
		return zipTagName + version;
	}

	/**
	 * 获取文件缓存路径
	 * @return 文件临时存储路径
	 * @throws Exception if has error(直接往外抛)
	 */
	public static String getTempPath() throws Exception {
		String tempPath = AttachConfig.getInstance().getAttachSavePath() + getAttachBasePath();
		return tempPath;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:47:23
	 * @todo 创建文件夹名称
	 * @return 保存文件夹名称
	 */
	private static String createSavePath() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String savePath = sdf.format(new java.util.Date());
		savePath += Math.random();
		savePath = savePath.replace(".", "_");
		return savePath;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:47:53
	 * @todo 获取文件保存路径
	 * @return 文件存储路径
	 */
	private static String getAttachBasePath() {
		return AttachConfig.ATTACH_PREFIX + File.separator + createSavePath();
	}

}
