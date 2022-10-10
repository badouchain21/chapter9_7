package com.bdc.assetdefine.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

/**
 * 描述：资产建模定义实体
 * @author linxiaoqing
 * @Date 2020年1月6日 上午10:36:23
 */
@Getter
@Setter
@javax.persistence.Entity
@Table(name = "bdc_asset_defined")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@Where(clause = "FLG_DELETED=0")
@SuppressWarnings("all")
public class AssetDefinedEntity extends AppBaseEntity {

    /**
     * 资产名称
     */
    @Column(name = "ASSET_NAME")
    private String assetName;

    /**
     * 资产编码
     */
    @Column(name = "ASSET_CODE")
    private String assetCode;

    /**
     * 资产主键
     */
    @Column(name = "ASSET_KEY")
    private String assetKey;

    /**
     * 资产描述
     */
    @Column(name = "ASSET_DESC")
    private String assetDesc;

    /**
     * 模型编码
     */
    @Column(name = "asset_mdcode")
    private String assetMdCode;
    /**
     * blockchainconfig referto BlockchainconfigEntity
     */
    @Column(name = "config_id", unique = false, nullable = true, insertable = false, updatable = false)
    protected String configId;

    @ManyToOne
    @JoinColumn(name="config_id")//外键字段
    private BlockchainConfigEntity blockchainConfig;

    /**
     * 资产Name
     */
    @Column(name = "asset_name_field")
    protected String assetNameField;
}
