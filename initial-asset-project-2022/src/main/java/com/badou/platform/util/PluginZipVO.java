package com.badou.platform.util;

import java.util.Date;

import com.badou.plugins.install.model.PluginsInstallEntity;

/**
 * 模型压缩文件对象
 * @author zt
 *
 */
public class PluginZipVO{
	/**
	 * 版本号
	 */
	private String version;
	
	/**
	 * 版本号
	 */
	private String versionCode;
	
	/**
	 * 版本号
	 */
	private String version1;
	
	/**
	 * 版本号
	 */
	private String version2;
	
	/**
	 * 版本号
	 */
	private String version3;
	
	/**
	 * 发布日期
	 */
	private Date publishDate = new Date();
	
	/**
	 * 模型
	 */
	private PluginsInstallEntity plugin;
	
	/**
	 * 配置文件路径
	 */
	private String xmlFilePath;
	
	/**
	 * jar包文件路径
	 */
	private String jarFilePath;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public PluginsInstallEntity getPlugin() {
		return plugin;
	}

	public void setPlugin(PluginsInstallEntity plugin) {
		this.plugin = plugin;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersion1() {
		return version1;
	}

	public void setVersion1(String version1) {
		this.version1 = version1;
	}

	public String getVersion2() {
		return version2;
	}

	public void setVersion2(String version2) {
		this.version2 = version2;
	}

	public String getVersion3() {
		return version3;
	}

	public void setVersion3(String version3) {
		this.version3 = version3;
	}

	public String getXmlFilePath() {
		return xmlFilePath;
	}

	public void setXmlFilePath(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	public String getJarFilePath() {
		return jarFilePath;
	}

	public void setJarFilePath(String jarFilePath) {
		this.jarFilePath = jarFilePath;
	}
	
}
