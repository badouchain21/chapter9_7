package com.bdc.api.intermanage.interfacepermission.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.core.standard.base.extend.ILogicDelete;


@javax.persistence.Entity
@Table(name = "I_INTER_PERMISSION")
@Cache(usage = CacheConcurrencyStrategy.NONE)	//缓存配置
@Where(clause = "FLG_DELETED=0")
public class InterFacePermissionEntity  extends AppBaseEntity implements ILogicDelete{

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -1640992943459634502L;

	/**
	 * 接口Id
	 */
	@Column(name = "INTERFACE_ID", unique = false, nullable = true, insertable = false, updatable = false)
	private String interfaceId;
	/**
	 * 外网信息Id
	 */
	@Column(name = "NET_INFORMAT_ID", unique = false, nullable = true, insertable = false, updatable = false)
	private String netInformatId;
	/**
	 * 接口访问权限
	 */
	@Column(name = "INTERFACE_PERMISSION", unique = false, nullable = true, insertable = true, updatable = true)
	private Integer interfacePermission;

	/**
	 * 接口信息对象
	 */
	@JoinColumn(name = "INTERFACE_ID", referencedColumnName = "ID")
	@ManyToOne
	private InterFaceDetailEntity interFaceDetailEntity;

	/**
	 * 外网信息对象
	 */
	@JoinColumn(name = "NET_INFORMAT_ID", referencedColumnName = "ID")
	@ManyToOne
	private NetInformationEntity netInformationEntity;

	public InterFaceDetailEntity getInterFaceDetailEntity() {
		return interFaceDetailEntity;
	}

	public void setInterFaceDetailEntity(InterFaceDetailEntity interFaceDetailEntity) {
		this.interFaceDetailEntity = interFaceDetailEntity;
	}

	public NetInformationEntity getNetInformationEntity() {
		return netInformationEntity;
	}

	public void setNetInformationEntity(NetInformationEntity netInformationEntity) {
		this.netInformationEntity = netInformationEntity;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getNetInformatId() {
		return netInformatId;
	}

	public void setNetInformatId(String netInformatId) {
		this.netInformatId = netInformatId;
	}

	public Integer getInterfacePermission() {
		return interfacePermission;
	}

	public void setInterfacePermission(Integer interfacePermission) {
		this.interfacePermission = interfacePermission;
	}



}
