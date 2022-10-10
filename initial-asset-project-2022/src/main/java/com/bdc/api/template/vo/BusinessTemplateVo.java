package com.bdc.api.template.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述：业务
 * @author linxiaoqing
 * @Date 2020年1月4日 上午10:47:28
 */
@Getter
@Setter
public class BusinessTemplateVo {

    /**
     * 资产建模定义编码
     */
    private String assetCode;

    /**
     * 模型编码
     */
    private String mdCode;
    /**
     * 资产name
     */
    private String assetName;

}
