package com.bdc.uploadassetlog.model;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Table(name = "bdc_upload_asset_log")
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class UploadAssetLogEntity extends AppBaseEntity {

    /**
     * 资产通用列表ID
     */
    @Column(name = "ASSET_ID")
    private String assetId;
    /**
     * 资产类型名称
     */
    @Column(name = "ASSET_TYPE_NAME")
    private String assetTypeName;

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
     * 上链状态
     */
    @Column(name = "UPLOAD_STATUS")
    private Integer uploadStatus;

    /**
     * hash地址
     */
    @Column(name = "HASH")
    private String hash;

    /**
     * 日志
     */
    @Column(name = "LOG")
    private String log;

    /**
     * 上链时间
     */
    @Column(name = "UPLOAD_TIME")
    private Date uploadTime;

    /**
     * baas平台logID
     */
    @Column(name = "BDC_BAAS_LOG_ID")
    private String bdcBaasLogId;

    /**
     * 资产Name
     */
    @Column(name = "ASSET_NAME_FIELD")
    private String assetNameField;

}
