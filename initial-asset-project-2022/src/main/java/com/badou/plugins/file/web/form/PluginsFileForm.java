package  com.badou.plugins.file.web.form;

import java.util.Date;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.plugins.file.model.PluginsFileEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:33:56
 * @todo 插件文件form表单
 */
public class PluginsFileForm extends BaseStrutsEntityForm<PluginsFileEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3736416501012557654L;
	
	/**
     * 附件ID
     */
    protected String  attachId;
	/**
     * 文件名称
     */
    protected String  fileName;
	/**
     * 
     */
    protected Date  updateTime;
	/**
     * 
     */
    protected Integer  flgDeleted;
	/**
     * 
     */
    protected String  updator;
	/**
     * 
     */
    protected String  creator;
	/**
     * 
     */
    protected String  creatorName;
	/**
     * 
     */
    protected String  updatorName;
	/**
     * 
     */
    protected Date  createTime;
	/**
     * 配置文件内容
     */
    protected String  properties;
	/**
     * 文件唯一编码值
     */
    protected String  md5;
	/**
     * 是否启用状态位
     */
    protected Integer  status;

    /**
     *  获取附件ID
     *  @return attachId
     */
    public String getAttachId() {
        return attachId;
    }

	/**
     *  设置附件ID
     */
    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }
    /**
     *  获取文件名称
     *  @return fileName
     */
    public String getFileName() {
        return fileName;
    }

	/**
     *  设置文件名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     *  获取
     *  @return updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

	/**
     *  设置
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     *  获取
     *  @return flgDeleted
     */
    public Integer getFlgDeleted() {
        return flgDeleted;
    }

	/**
     *  设置
     */
    public void setFlgDeleted(Integer flgDeleted) {
        this.flgDeleted = flgDeleted;
    }
    /**
     *  获取
     *  @return updator
     */
    public String getUpdator() {
        return updator;
    }

	/**
     *  设置
     */
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    /**
     *  获取
     *  @return creator
     */
    public String getCreator() {
        return creator;
    }

	/**
     *  设置
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     *  获取
     *  @return creatorName
     */
    public String getCreatorName() {
        return creatorName;
    }

	/**
     *  设置
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    /**
     *  获取
     *  @return updatorName
     */
    public String getUpdatorName() {
        return updatorName;
    }

	/**
     *  设置
     */
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }
    /**
     *  获取
     *  @return createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

	/**
     *  设置
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     *  获取配置文件内容
     *  @return properties
     */
    public String getProperties() {
        return properties;
    }

	/**
     *  设置配置文件内容
     */
    public void setProperties(String properties) {
        this.properties = properties;
    }
    /**
     *  获取文件唯一编码值
     *  @return md5
     */
    public String getMd5() {
        return md5;
    }

	/**
     *  设置文件唯一编码值
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    /**
     *  获取是否启用状态位
     *  @return status
     */
    public Integer getStatus() {
        return status;
    }

	/**
     *  设置是否启用状态位
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}