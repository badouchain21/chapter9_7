package com.bdc.api.intermanage.tokenmanage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.badou.brms.base.support.hibernate.BaseHibernateEntity;


@javax.persistence.Entity
@Table(name = "I_TOKEN")
@Cache(usage = CacheConcurrencyStrategy.NONE)	//缓存配置
public class TokenManageEntity extends BaseHibernateEntity {


	/**
	 *
	 */
	private static final long serialVersionUID = -309210345920063691L;
	/**
	 * appId
	 */
	@Column(name = "appId", unique = false, nullable = true, insertable = true, updatable = true)
	private String appId;
	/**
	 * appKey
	 */
	@Column(name = "appKey", unique = false, nullable = true, insertable = true, updatable = true)
	private String appkey;
	/**
	 * token
	 */
	@Column(name = "token", unique = false, nullable = true, insertable = true, updatable = true)
	private String token;
	/**
	 * 起始时间
	 */
	@Column(name = "CREATE_TIME", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createTime;
	/**
	 * 结束时间
	 */
	@Column(name = "END_TIME", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date endTime;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


}
