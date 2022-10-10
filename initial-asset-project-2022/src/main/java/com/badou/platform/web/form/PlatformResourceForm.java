package com.badou.platform.web.form;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.platform.model.PlatformResourceEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:08:58
 * @todo 平台资源form表单
 */
public class PlatformResourceForm extends BaseStrutsEntityForm<PlatformResourceEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1277373811347385875L;

	/**
	 * 资源类型
	 */
	protected Integer type ;
	
	/**
	 * 云中心资源id
	 */
	protected String platformResourceId ;
	
	/**
	 * 版本标签
	 */
	protected String versionCode ;
	
	/**
	 * 资源id
	 */
	protected String resourceId ;
	
	/**
	 * 当前版本号1
	 */
	protected String version1 ;
	
	/**
	 * 当前版本号2
	 */
	protected String version2 ;
	
	/**
	 * 当前版本号3
	 */
	protected String version3 ;
	
	/**
	 * 状态
	 */
	protected Integer status ;
	
	/**
	 * 备注
	 */
	protected String remark ;
	
	
	/**
	 * 是否为当前版本，true 是，false 否。
	 * 该属性用于模型更新页面。
	 */
	protected boolean currentVersion;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPlatformResourceId() {
		return platformResourceId;
	}

	public void setPlatformResourceId(String platformResourceId) {
		this.platformResourceId = platformResourceId;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean isCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(boolean currentVersion) {
		this.currentVersion = currentVersion;
	}

	@Override
	public String toString() {
		return "PlatformResourceForm [type=" + type + ", platformResourceId="
				+ platformResourceId + ", versionCode=" + versionCode
				+ ", resourceId=" + resourceId + ", version1=" + version1
				+ ", version2=" + version2 + ", version3=" + version3
				+ ", status=" + status + "]";
	}
	
}
