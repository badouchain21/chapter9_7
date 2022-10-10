/*
 * 
 * date： 2019-01-30 15:55:40.739
 * author:badousoft
 */
package com.badou.plugins.file.model;

import java.util.Date;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.badou.core.standard.enums.SystemBoolean;

/**
 *	系统插件文件类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:55:40.739
 * @version v1.0
 * @revision 
 */
@javax.persistence.Entity
@Table(name = "sys_plugins_file")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class PluginsFileEntity extends  com.badou.designer.module.design.base.BaseModuleEntity{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = -6939218657752486408L;
	/**
     * 附件ID
     */
	@Column(name = "Attach_id", unique = false, nullable = true, insertable = true, updatable = true)
    protected String attachId;
	/**
     * 文件名称
     */
	@Column(name = "File_name", unique = false, nullable = true, insertable = true, updatable = true)
    protected String fileName;
	
	/**
     * 
     */
	@Column(name = "UPDATE_TIME", unique = false, nullable = true, insertable = true, updatable = true)
    protected Date updateTime = new Date();
	  
	/**
     * 
     */
	@Column(name = "CREATE_TIME", unique = false, nullable = true, insertable = true, updatable = true)
    protected Date createTime;
	/**
     * 配置文件内容
     */
	@Column(name = "Properties", unique = false, nullable = true, insertable = true, updatable = true)
    protected String properties;
	/**
     * 是否启用状态位
     */
	@Column(name = "Status", unique = false, nullable = true, insertable = true, updatable = true)
    protected Integer status = SystemBoolean.YES.getKey();
	/**
	 * 文件唯一编码值
	 */
	@Column(name = "file_md5", unique = false, nullable = true, insertable = true, updatable = true)
	protected String fileMd5;
	
 

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
     * 获取附件ID
     * @return attachId
     */
    public String getAttachId() {
        return attachId;
    }

	/**
     * 设置附件ID
     */
    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }
    /**
     * 获取文件名称
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

	/**
     * 设置文件名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * 获取
     * @return updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

	/**
     * 设置
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
      
    /**
     * 获取
     * @return createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

	/**
     * 设置
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 获取配置文件内容
     * @return properties
     */
    public String getProperties() {
        return properties;
    }

	/**
     * 设置配置文件内容
     */
    public void setProperties(String properties) {
        this.properties = properties;
    }
    

    /**
     * 获取是否启用状态位
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

	/**
     * 设置是否启用状态位
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
 
 
    @Transient
    private Properties  property;

    @Transient
	public Properties getProperty() {
		return property;
	}

	public void setProperty(Properties property) {
		this.property = property;
	}
	
    @Transient
	private boolean needUpdate = false;


    @Transient
	public boolean getNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	 
}




