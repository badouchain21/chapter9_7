package com.bdc.base.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 首页数据关注栏目数据对象定义
 * @author lps
 *
 */
@Getter
@Setter
public class BaseAssetIndexAttentionVO {
	/**
	 * 已上链的文化馆资产
	 */
	private int  uploadedAssetCount;
	/**
	 * 待上链的文化馆资产
	 */
	private int uploadingAssetCount;
	/**
	 * wei上链的文化馆资产
	 */
	private int unUnploadAssetCount;
	/**
	 * 24小时内上链的文化馆资产
	 */
	private int uploadedAssetIn24Hours;
	/**
	 * 一个月内上链的文化馆资产
	 */
	private int uploadedAssetInOneMonth;



}
