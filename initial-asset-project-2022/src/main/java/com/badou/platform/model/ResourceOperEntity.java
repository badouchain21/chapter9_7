package com.badou.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.badou.brms.base.support.hibernate.used.AppEntityOnlyId;
import com.badou.platform.PlatformConst;

/**
 * 资源操作记录实体
 * @author cjb
 *
 */
@javax.persistence.Entity
@Table(name = "resource_oper")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class ResourceOperEntity extends AppEntityOnlyId{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7905326874950645273L;

	/**
	 * 云中心资源表id
	 */
	@Column(name="platform_resource_link_id")
	protected String platformResourceId ;
	
	/**
	 * 资源类型
	 * <ul>
	 * 	<li>0：模型</li>
	 * </ul>
	 */
	@Column(name="type")
	protected Integer type ;
	
	/**
	 * 操作类型
	 * <ul>
	 * 	<li>0：上传</li>
	 *  <li>1：下载，更新</li>
	 * </ul>
	 */
	@Column(name="oper_type")
	protected Integer operType ;
	
	/**
	 * 该字段存储 上传、下载、更新的状态，上传的状态值与下载、更新的状态值存在重复，
	 * 所以该字段是上传状态还是下载、更新状态需要先判断 operType，operType = 0时，
	 * 字段存储的是上传的状态，operType = 1时，字段存储的是下载、更新的状态。状态
	 * 值如下：
	 * 
	 * <p>
	 * 
	 * 上传：
	 * <ul>
	 * 	<li>0：未上传，常量 {@link PlatformConst#UPLOAD_STATUS_UN_DO}</li>
	 * 	<li>1：上传中，常量 {@link PlatformConst#UPLOAD_STATUS_UN_DO}</li>
	 * 	<li>2：上传成功，常量 {@link PlatformConst#UPLOAD_STATUS_UN_DO}</li>
	 * 	<li>3：上传失败，常量 {@link PlatformConst#UPLOAD_STATUS_UN_DO}</li>
	 * </ul>
	 * 
	 * <p>
	 * 
	 * 下载：
	 * <ul>
	 * 	<li>0：未下载，常量 {@link PlatformConst#DOWNLOAD_STATUS_UN_DO}</li>
	 * 	<li>1：下载中，常量 {@link PlatformConst#DOWNLOAD_STATUS_DOING}</li>
	 * 	<li>2：下载完成，常量 {@link PlatformConst#DOWNLOAD_STATUS_SUCCESS}</li>
	 * 	<li>3：下载失败，常量 {@link PlatformConst#DOWNLOAD_STATUS_FAIL}</li>
	 * 	<li>4：下载更新中，常量 {@link PlatformConst#DOWNLOAD_STATUS_UPDATE}</li>
	 * 	<li>5：下载更新完成，常量 {@link PlatformConst#DOWNLOAD_STATUS_FINISH}</li>
	 * 	<li>6：下载更新失败，常量 {@link PlatformConst#DOWNLOAD_STATUS_UPDATE_FAIL}</li>
	 * </ul>
	 * 
	 */
	@Column(name="status")
	protected Integer status ;
	
	/**
	 * 失败原因
	 */
	@Column(name="fail_msg")
	protected String failMsg ;
	
	/**
	 * 备注
	 */
	@Column(name="remark")
	protected String remark ;
	
	/**
	 * 上传路径/下载路径
	 */
	@Column(name="url")
	protected String url ;
	
	/**
	 * 编码
	 */
	@Column(name="code")
	protected String code ;	
	
	/**
	 * 名称
	 */
	@Column(name="name")
	protected String name ;	
	
	/**
	 * 创建时间
	 */
	@Column(name="create_time")
	protected Date createTime ;	
	
	/**
	 * 版本编码
	 */
	@Column(name="version_code")
	protected String versionCode ;	
	
	/**
	 * 版本号1
	 */
	@Column(name="version1")
	protected String version1 ;	
	
	/**
	 * 版本号2
	 */
	@Column(name="version2")
	protected String version2 ;	
	
	/**
	 * 版本号3
	 */
	@Column(name="version3")
	protected String version3 ;
	
	/**
	 * 附件id
	 */
	@Column(name="attach_id")
	protected String attachId ;
	
	/**
	 * 更新类型
	 * <ul>
	 * 	<li>0：仅下载文件</li>
	 *  <li>1：覆盖更新</li>
	 *  <li>2：初次下载</li>
	 * </ul>
	 */
	@Column(name="update_type")
	protected Integer updateType ;
	
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getUpdateType() {
		return updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

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
	
	public String getFailMsg() {
		return failMsg;
	}

	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getVersion(){
		return versionCode+version1+version2+version3;
	}

	@Override
	public String toString() {
		return "ResourceOperEntity [platformResourceId=" + platformResourceId
				+ ", type=" + type + ", operType=" + operType + ", status="
				+ status + ", code=" + code + ", name=" + name
				+ ", createTime=" + createTime + ", versionCode=" + versionCode
				+ ", version1=" + version1 + ", version2=" + version2
				+ ", version3=" + version3 + "]";
	}	
	
	
}
