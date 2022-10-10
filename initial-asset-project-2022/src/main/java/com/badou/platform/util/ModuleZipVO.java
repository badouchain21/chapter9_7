package com.badou.platform.util;

import java.util.Date;
import java.util.Map;

import com.badou.brms.dictionary.model.DictionaryEntity;
import com.badou.designer.module.design.model.MdModuleEntity;

/**
 * 模型压缩文件对象
 * @author zt
 *
 */
public class ModuleZipVO{
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
	private MdModuleEntity module;
	
	/**
	 * 数据字典
	 */
	private Map<String,DictionaryEntity> dicMap;

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

	public MdModuleEntity getModule() {
		return module;
	}

	public void setModule(MdModuleEntity module) {
		this.module = module;
	}

	public Map<String, DictionaryEntity> getDicMap() {
		return dicMap;
	}

	public void setDicMap(Map<String, DictionaryEntity> dicMap) {
		this.dicMap = dicMap;
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
}
