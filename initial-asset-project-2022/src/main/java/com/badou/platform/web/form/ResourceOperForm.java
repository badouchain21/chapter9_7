package com.badou.platform.web.form;

import java.util.Date;

import javax.persistence.Column;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.platform.model.ResourceOperEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:09:13
 * @todo 资源操作form表单
 */
public class ResourceOperForm extends BaseStrutsEntityForm<ResourceOperEntity> {
	
	/**
	 * 云中心资源表id
	 */
	protected String platformResourceId ;
	
	/**
	 * 资源类型
	 */
	protected Integer type ;
	
	/**
	 * 操作类型
	 */
	protected Integer operType ;
	
	/**
	 * 状态
	 */
	protected Integer status ;
	
	/**
	 * 失败原因
	 */
	protected String failMsg ;
	
	/**
	 * 备注
	 */
	protected String remark ;
	
	/**
	 * 上传路径/下载路径
	 */
	protected String url ;
	
	/**
	 * 编码
	 */
	protected String code ;	
	
	/**
	 * 名称
	 */
	protected String name ;	
	
	/**
	 * 创建时间
	 */
	protected Date createTime ;	
	
	/**
	 * 版本编码
	 */
	protected String versionCode ;	
	
	/**
	 * 版本号1
	 */
	protected String version1 ;	
	
	/**
	 * 版本号2
	 */
	protected String version2 ;	
	
	/**
	 * 版本号3
	 */
	protected String version3 ;
	
	/**
	 * 附件id
	 */
	@Column(name="attach_id")
	protected String attachId ;
	
	/**
	 * 更新类型
	 * 0仅下载文件
	 * 1覆盖更新
	 * 2初次下载
	 */
	protected Integer updateType ;

	public String getPlatformResourceId() {
		return platformResourceId;
	}

	public void setPlatformResourceId(String platformResourceId) {
		this.platformResourceId = platformResourceId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFailMsg() {
		return failMsg;
	}

	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public Integer getUpdateType() {
		return updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}
}
