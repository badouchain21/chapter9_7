package com.bdc.api.intermanage.interfacelogger.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;


@Getter
@Setter
@javax.persistence.Entity
@Table(name = "I_INTERFACE_LOG")
@Cache(usage = CacheConcurrencyStrategy.NONE)	//缓存配置
@Where(clause = "FLG_DELETED=0")
public class InterFaceLoggerEntity extends AppBaseEntity {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -6415962229342195956L;

	/**
	 * appId
	 */
	@Column(name = "app_id", unique = false, nullable = true, insertable = true, updatable = false)
	private String appId;

	/**
	 * appkey
	 */
	@Column(name = "appkey", unique = false, nullable = true, insertable = true, updatable = false)
	private String appkey;

	/**
	 * 外网Id
	 */
	@Column(name = "NET_INFORMAT_ID", unique = false, nullable = true, insertable = true, updatable = false)
	private String netInformatId;

	/**
	 * 接口名称
	 */
	@Column(name = "INTERFACE_NAME", unique = false, nullable = true, insertable = true, updatable = false)
	private String interfacename;
	/**
	 * 接口Id
	 */
	@Column(name = "INTERFACE_ID", unique = false, nullable = true, insertable = true, updatable = false)
	private String interfaceId;
	/**
	 * 请求时间
	 */
	@Column(name = "REQUEST_TIME", unique = false, nullable = true, insertable = true, updatable = false)
	private Timestamp requestTime;
	/**
	 * 返回结果（是否成功）
	 * 数据字典:INTERFACE_LOG_TYPE
	 * 0:成功
	 * 1：失败
	 */
	@Column(name = "IS_SUCCESS", unique = false, nullable = true, insertable = true, updatable = true)
	private Integer isSuccess;
	/**
	 * 失败原因
	 */
	@Column(name = "FAILL_REASON", unique = false, nullable = true, insertable = true, updatable = true)
	private String failreason;

	/**
	 * token
	 */
	@Column(name = "token", unique = false, nullable = true, insertable = true, updatable = false)
	private String token;
	/**
	 * 结束时间
	 */
	@Column(name = "endtime", unique = false, nullable = true, insertable = true, updatable = true)
	private Timestamp endTime;
	/**
	 * 耗时
	 */
	@Column(name = "latetime", unique = false, nullable = true, insertable = true, updatable = true)
	private String lateTime;

	/**
	 * 来源URL
	 */
    @Column(name = "source_url", unique = false, nullable = true, insertable = true, updatable = false)
	private String sourceUrl;

    /**
     * 请求参数
     */
    @Column(name = "REQUEST_PARAM", unique = false, nullable = true, insertable = true, updatable = false)
    private String requestParam;

	/**
	 * 返回结果，拦截器拦截的方法的返回结果
	 */
    @Column(name = "json_result", unique = false, nullable = true, insertable = true, updatable = true)
	private String jsonResult;

	/**
	 * 请求类型 GET | POST | PUT | DELETE
	 */
	@Column(name = "request_type",updatable = false)
    private String requestType;

}
