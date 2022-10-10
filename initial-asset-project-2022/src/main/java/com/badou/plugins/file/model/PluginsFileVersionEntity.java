/*
 * 
 * date： 2019-01-30 15:55:59.359
 * author:badousoft
 */
package com.badou.plugins.file.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *	插件文件版本类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:55:59.359
 * @version v1.0
 * @revision 
 */
@javax.persistence.Entity
@Table(name = "sys_plugins_file_version")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class PluginsFileVersionEntity extends  com.badou.designer.module.design.base.BaseModuleEntity{ 

	/**
     * 创建者
     */
	@Column(name = "Creator", unique = false, nullable = true, insertable = true, updatable = true)
    protected String creator;
	/**
     * 文件ID
     */
	@Column(name = "File_id", unique = false, nullable = true, insertable = true, updatable = true)
    protected String fileId;
	/**
     * 创建者名称
     */
	@Column(name = "Creator_name", unique = false, nullable = true, insertable = true, updatable = true)
    protected String creatorName;
	/**
     * 创建时间
     */
	@Column(name = "Create_date", unique = false, nullable = true, insertable = true, updatable = true)
    protected Date createDate;
	
	/**
	 * 文件唯一编码值
	 */
	@Column(name = "file_md5", unique = false, nullable = true, insertable = true, updatable = true)
	protected String fileMd5;
	
	/**
     * 系统默认版本号
     */
	@Column(name = "sys_version", unique = false, nullable = true, insertable = true, updatable = true)
    protected String sysVersion;

	/**
     * 获取文件唯一编码值
     * @return fileMd5
     */
   	public String getFileMd5() {
		return fileMd5;
	}

	/**
     * 设置文件唯一编码值
     */
	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	

    /**
     * 获取系统默认版本号
     * @return sysVersion
     */
	public String getSysVersion() {
		return sysVersion;
	}
	/**
     * 设置系统默认版本号
     */
	public void setSysVersion(String sysVersion) {
		this.sysVersion = sysVersion;
	}
	
    /**
     * 获取创建者
     * @return creator
     */
    public String getCreator() {
        return creator;
    }

	/**
     * 设置创建者
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     * 获取文件ID
     * @return fileId
     */
    public String getFileId() {
        return fileId;
    }

	/**
     * 设置文件ID
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    /**
     * 获取创建者名称
     * @return creatorName
     */
    public String getCreatorName() {
        return creatorName;
    }

	/**
     * 设置创建者名称
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    /**
     * 获取创建时间
     * @return createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

	/**
     * 设置创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
	
}




