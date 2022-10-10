package com.bdc.apple.model;

import com.bdc.base.model.BaseAssetEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 *	苹果管理类
 *
 * @author zhongzhilong
 * @date 2020-11-04 11:35:28
 */
@SuppressWarnings("serial")
@Getter
@Setter
@javax.persistence.Entity
@Table(name = "bdc_asset_apple")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class AppleEntity extends BaseAssetEntity {

	/**
     * 苹果id
     */
	@Column(name = "APPLE_ID")
    protected String appleId;

	/**
     * 苹果名称
     */
	@Column(name = "APPLE_NAME")
    protected String appleName;


}
