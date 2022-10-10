package com.badou.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.badou.brms.base.support.hibernate.used.AppEntityOnlyId;

/**
 * 资源备份实体类
 * @author chenjiabao
 *
 */
@javax.persistence.Entity
@Table(name = "resource_bak")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class ResourceBakEntity extends AppEntityOnlyId{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1249191463950373169L;

	/**
	 * 云中心资源id
	 */
	@Column(name="platform_resource_link_id")
	protected String plaformResourceId ;
	
	/**
	 * 资源类型
	 */
	@Column(name="type")
	protected Integer type ;
	
	/**
	 * 操作类型
	 */
	@Column(name="oper_type")
	protected Integer operType ;
	
	/**
	 * 状态
	 */
	@Column(name="status")
	protected Integer status ;
	
	/**
	 *  编码
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
	 * 版本号
	 */
	@Column(name="version")
	protected String version ;
	
	/**
	 * 操作id
	 */
	@Column(name="oper_id")
	protected String operId ;

	public String getPlaformResourceId() {
		return plaformResourceId;
	}

	public void setPlaformResourceId(String plaformResourceId) {
		this.plaformResourceId = plaformResourceId;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	@Override
	public String toString() {
		return "ResourceBakEntity [plaformResourceId=" + plaformResourceId
				+ ", type=" + type + ", operType=" + operType + ", status="
				+ status + ", code=" + code + ", name=" + name
				+ ", createTime=" + createTime + ", version=" + version
				+ ", operId=" + operId + "]";
	}
	
}
