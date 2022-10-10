package com.bdc.api.intermanage.interfacedetail.model;

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
import com.badou.core.standard.base.extend.ILogicDelete;


/**
 *
 * @ClassName InterFaceDetailEntity
 * @Description TODO(接口详情表)
 * @author zzk
 * @Date 2017年9月14日 上午11:33:20
 * @Updator zzk
 * @UpdateDate 2017年9月14日 上午11:33:20
 * @UpdateDesc (新增接口详情表)
 * @version 1.0.0
 */

@javax.persistence.Entity
@Table(name = "I_INTERFACE_DETAIL")
@Cache(usage = CacheConcurrencyStrategy.NONE)	//缓存配置
@Where(clause = "FLG_DELETED=0")
public class InterFaceDetailEntity  extends AppBaseEntity implements ILogicDelete{

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -6906533940460568156L;


	/**
	 * 接口名称
	 */
	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 64)
	private String name;

	/**
	 * 接口使用状态
	 * 数据字典：INTERFACE_STATUS
	 * 0：启用
	 * 1：停用
	 */
	@Column(name = "STATUS", unique = false, nullable = true, insertable = true, updatable = true)
	private Integer status;

	/**
	 * 接口描述
	 */
	@Column(name = "INTERFACE_DETAIL", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	private String InterfaceDetail;

	/**
	 * 接口交互类型
	 * 数据字典：INTERFACE_RESPON_TYPE
	 * 0：同步
	 * 1：异步
	 */
	@Column(name = "TYPE", unique = false, nullable = true, insertable = true, updatable = true)
	private Integer type;

	/**
	 * 接口所属系统
	 *
	 * 数据字典：Subordinate_System
	 * 如：
	 * 	01：风险系统
	 * 	02：质检系统
	 */
	@Column(name = "belong_sys", unique = false, nullable = true, insertable = true, updatable = true)
	private String belong_sys;


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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getInterfaceDetail() {
		return InterfaceDetail;
	}
	public void setInterfaceDetail(String interfaceDetail) {
		InterfaceDetail = interfaceDetail;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getBelong_sys() {
		return belong_sys;
	}
	public void setBelong_sys(String belong_sys) {
		this.belong_sys = belong_sys;
	}



}
