package com.bdc.base.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 公示平台关注栏目数据对象定义
 *
 * @author zhongzhilong
 * @date 2020-11-11 11:04:30
 */
@Getter
@Setter
public class BaseAssetPublicityAttentionVO {

    /**
     * 资产类型总数
     */
    private int assetTypeAmount;
    /**
     * 资产总数
     */
    private int assetAmount;
    /**
     * 24小时上链资产
     */
    private int assetAmount24hours;

}
