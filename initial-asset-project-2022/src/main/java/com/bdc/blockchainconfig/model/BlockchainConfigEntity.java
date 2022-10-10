package com.bdc.blockchainconfig.model;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.core.standard.enums.SystemBoolean;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author badousoft
 * @date 2020-10-13 18:35:08.968
 * @todo bdc_blockchain_config类
 */
@Entity
@Table(name = "bdc_blockchain_config")
@Getter
@Setter
@Where(clause = "FLG_DELETED=0")
public class BlockchainConfigEntity extends AppBaseEntity {
	/**
	 * configName
	 */
	@Column(name = "config_name", unique = false, nullable = true, insertable = true, updatable = true)
	protected String configName;

	/**
     * 监控URL
     */
	@Column(name = "Monitor_Url", unique = false, nullable = true, insertable = true, updatable = true)
    protected String monitorUrl;


	/**
     * 日志URL
     */
	@Column(name = "LOG_URL", unique = false, nullable = true, insertable = true, updatable = true)
    protected String logUrl;


	/**
     * 应用appkey
     */
	@Column(name = "App_key", unique = false, nullable = true, insertable = true, updatable = true)
    protected String appKey;

	/**
     * TokenURL
     */
	@Column(name = "TOKEN_URL", unique = false, nullable = true, insertable = true, updatable = true)
    protected String tokenUrl;

	/**
     * 业务网络名称
     */
	@Column(name = "Network_Name", unique = false, nullable = true, insertable = true, updatable = true)
    protected String networkName;


	/**
     * 应用appid
     */
	@Column(name = "App_id", unique = false, nullable = true, insertable = true, updatable = true)
    protected String appId;

	/**
     * 区块链应用前缀URL
     */
	@Column(name = "Chain_Pre_Url", unique = false, nullable = true, insertable = true, updatable = true)
    protected String chainPreUrl;

	/**
     * 通道名称
     */
	@Column(name = "Channel_Name", unique = false, nullable = true, insertable = true, updatable = true)
    protected String channelName;

	/**
	 * config status
	 *  0 启用
	 *  1 禁用
	 */
	@Column(name = "STATUS")
	protected Integer status = SystemBoolean.NO.getKey();

}
