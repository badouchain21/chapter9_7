package com.bdc.api.intermanage.netinformation.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;

@javax.persistence.Entity
@Table(name = "I_NET_INFORMATION")
@Cache(usage = CacheConcurrencyStrategy.NONE)	//缓存配置
@Where(clause = "FLG_DELETED=0")
public class NetInformationEntity extends AppBaseEntity {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -5221445211485085500L;

	/**
	 * appId
	 */
	@Column(name = "app_Id", unique = false, nullable = true, insertable = true, updatable = true)
	private String appId;
	/**
	 * appKey
	 */
	@Column(name = "appkey", unique = false, nullable = true, insertable = true, updatable = true)
	private String appkey;
	/**
	 * 是否能访问接口
	 * 数据字典：IS_CALL_INTERFACE
	 * 0：是
	 * 1：否
	 */
	@Column(name = "IS_CALL_INTERFACE", unique = false, nullable = true, insertable = true, updatable = true)
	private Integer IsCallInterface;
	/**
	 * 部门编号
	 */
	@Column(name = "DEPA_NUM", unique = false, nullable = true, insertable = true, updatable = true)
	private String depanum;
	/**
	 * 企业名称
	 */
	@Column(name = "COMPANY_NAME", unique = false, nullable = true, insertable = true, updatable = true)
	private String companyName;

	/**
	 * 权限表（中间表）
	 */
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@OneToMany( fetch = FetchType.LAZY, mappedBy = "interFaceDetailEntity")
	@Cache(usage = CacheConcurrencyStrategy.NONE)
	protected Set<InterFacePermissionEntity> interFacePermissionEntities = new HashSet<InterFacePermissionEntity>();



	public Set<InterFacePermissionEntity> getInterFacePermissionEntities() {
		return interFacePermissionEntities;
	}
	public void setInterFacePermissionEntities(
			Set<InterFacePermissionEntity> interFacePermissionEntities) {
		this.interFacePermissionEntities = interFacePermissionEntities;
	}

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public Integer getIsCallInterface() {
		return IsCallInterface;
	}
	public void setIsCallInterface(Integer isCallInterface) {
		IsCallInterface = isCallInterface;
	}
	public String getDepanum() {
		return depanum;
	}
	public void setDepanum(String depanum) {
		this.depanum = depanum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
