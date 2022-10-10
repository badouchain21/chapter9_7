package com.badou.project.moduledemo.help;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:14:28
 * @todo 附件图片缓存
 */
public class AttachImgCaches {
	private static Logger logger = Logger.getLogger(AttachImgCaches.class);
	private static String defaultSuffix = ".png";

	private static Map<String, AttachImg> imgCaches = new HashMap<>();

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:46:56
	 * @todo 存储图片
	 * @param img
	 * @return 主键标识
	 */
	public static String storeImg(AttachImg img) {
		return storeImg(null, img);
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:46:56
	 * @todo 存储图片
	 * @param img
	 * @return 主键标识
	 */
	public static String storeImg(String imgId, AttachImg img) {
		if (imgCaches.size() > 100) {
			imgCaches.clear();
		}
		if (StringUtils.isNotEmpty(imgId)) {
			imgCaches.put(imgId, img);
			return imgId;
		} else {
			String key = getUid();
			imgCaches.put(key, img);
			return key;
		}
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:47:57
	 * @todo 获取图片
	 * @param imgId
	 * @return 图片对象
	 */
	public static AttachImg getImg(String imgId) {
		return imgCaches.get(imgId);
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:48:26
	 * @todo 生成随机数主键
	 * @return 随机数主键
	 */
	private static String getUid() {
		return System.currentTimeMillis() + "" + (int) (Math.random() * 10000);
	}

	/**
	 * @author chenjiabao
	 * @date 2019年7月2日上午11:16:57
	 * @todo 附件实体类
	 */
	public static class AttachImg {

		private String name = "";

		private long size = 0L;

		private String suffix = "";

		private InputStream is;

		private byte[] content = {};

		/**
		 * @auth chenjiabao
		 * @date 2019年7月2日下午7:38:23
		 * @todo 无参构造函数
		 */
		public AttachImg() {

		}

		/**
		 * @auth chenjiabao
		 * @date 2019年7月2日下午5:49:06
		 * @todo 有参构造函数
		 * @param name
		 * @param suffix
		 * @param size
		 * @param is
		 */
		public AttachImg(String name, String suffix, long size, InputStream is) {
			this.name = name;
			this.suffix = suffix;
			this.size = size;
			this.is = is;
			this.content = getContent();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @auth chenjiabao
		 * @date 2019年7月2日下午5:49:19
		 * @todo 获取文件大小
		 * @return 文件大小
		 */
		public long getSize() {
			if (size == 0 && is != null) {
				try {
					size = is.available();
				} catch (IOException e) {
					logger.error("获取文件大小错误：" + e.getMessage());
				}
			}
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		/**
		 * @auth chenjiabao
		 * @date 2019年7月2日下午5:49:39
		 * @todo 获取文件后缀
		 * @return 文件后缀
		 */
		public String getSuffix() {
			if (StringUtils.isEmpty(suffix) && StringUtils.isNotEmpty(name)) {
				suffix = name.substring(name.lastIndexOf("\\.") + 1);
			} else {
				suffix = defaultSuffix;
			}
			return suffix;
		}

		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}

		/**
		 * @auth chenjiabao
		 * @date 2019年7月2日下午5:49:51
		 * @todo 获取文件流
		 * @return 文件流
		 */
		public InputStream getIs() {
			try {
				if (is == null || is.available() == 0) {
					is = new ByteArrayInputStream(content);
				}
			} catch (IOException e) {
				logger.error(e);
			}
			return is;
		}

		public void setIs(InputStream is) {
			this.is = is;
		}

		/**
		 * @auth chenjiabao
		 * @date 2019年7月2日下午5:50:03
		 * @todo 获取文件内容（byte[]）
		 * @return 文件内容
		 */
		public byte[] getContent() {
			if (content.length == 0 && is != null) {
				try {
					content = new byte[is.available()];
					is.read(content);
				} catch (IOException e) {
					logger.error("获取文件错误：" + e.getMessage());
				}
			}
			return content;
		}

		public void setContent(byte[] content) {
			this.content = content;
		}

	}
}
