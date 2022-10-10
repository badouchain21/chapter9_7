package com.badou.platform.model;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.badou.brms.base.support.hibernate.used.AppEntityOnlyId;

/**
 * 资源操作记录相关附件实体
 * @author chenjiabao
 *
 */
@javax.persistence.Entity
@Table(name = "resource_oper_attach")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class ResourceOperAttachEntity extends AppEntityOnlyId{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8715553078343233322L;

	/**
	 * 操作id
	 */
	@Column(name="oper_id")
	protected String operId ;
	
	/**
	 * 类型
	 */
	@Column(name="type")
	protected Integer type ;
	
	/**
	 * 附件id
	 */
	@Column(name="attach_id")
	protected String attachId ;

	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	@Override
	public String toString() {
		return "ResourceOperAttachEntity [operId=" + operId + ", type=" + type
				+ ", attachId=" + attachId + "]";
	}
	
}
