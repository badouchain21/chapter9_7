package com.badou.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.badou.brms.base.support.hibernate.used.AppEntityOnlyId;

/**
 * 云中心资源表
 * @author chenjiabao
 *
 */
@javax.persistence.Entity
@Table(name = "platform_resource_link")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class PlatformResourceEntity extends AppEntityOnlyId{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1919646227947495422L;

	/**
	 * 资源类型
	 */
	@Column(name = "type")
	protected Integer type ;
	
	/**
	 * 云中心资源id
	 */
	@Column(name = "platform_resource_id")
	protected String platformResourceId ;
	
	/**
	 * 版本标签
	 */
	@Column(name = "version_code")
	protected String versionCode ;
	
	/**
	 * 资源id
	 */
	@Column(name = "resource_id")
	protected String resourceId ;
	
	/**
	 * 当前版本号1
	 */
	@Column(name = "version1")
	protected String version1 ;
	
	/**
	 * 当前版本号2
	 */
	@Column(name = "version2")
	protected String version2 ;
	
	/**
	 * 当前版本号3
	 */
	@Column(name = "version3")
	protected String version3 ;
	
	/**
	 * 状态
	 */
	@Column(name = "status")
	protected Integer status ;
	
	/**
	 * 资源修改时间。该字段用于批量上传资源时，判断资源是否有修改过。
	 * 例如模型，该字段 < 模型的修改时间，就证明模型修改过后还没有上
	 * 传到云中心。只有在资源成功被修改（包括上传，下载，更新等操作）
	 * 后，才会更新该时间。
	 * 
	 * 目前(2018-11-9)，只有模型的上传，下载，更新才更新了这个字段
	 */
	@Column(name = "resource_modify_time")
	protected Date resourceModifyTime ;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getResourceModifyTime() {
		return resourceModifyTime;
	}

	public void setResourceModifyTime(Date resourceModifyTime) {
		this.resourceModifyTime = resourceModifyTime;
	}

	@Override
	public String toString() {
		return "PlatformResourceEntity [type=" + type + ", platformResourceId="
				+ platformResourceId + ", versionCode=" + versionCode
				+ ", resourceId=" + resourceId + ", version1=" + version1
				+ ", version2=" + version2 + ", version3=" + version3
				+ ", status=" + status + "]";
	}
	
}
