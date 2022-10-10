package com.bdc.base.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.page.model.Pagelet;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.vo.BaseAssetIndexAttentionVO;
import com.bdc.base.vo.BaseAssetPublicityAttentionVO;
import com.bdc.common.UploadStatusEnum;
import lombok.NonNull;

/**
 * base asset interface defined
 * @author lps
 *
 */
public interface IBaseAssetService {
	/**
	 * 填充跳转路径
	 * @param objects
	 * @param isBaseAssetEntity 用于区分资产实体和日志
	 * @return
	 * @throws Exception
	 */
	List<Object> fillAssetDataWithJumpUrl(List<Object> objects, boolean isBaseAssetEntity,boolean displayTxAttr)throws Exception;
	/**
	 * 统计已上链的文化馆资产、正在上链的文化馆资产、待上链的文化馆资产、24小时内上链的文化馆资产、一个月内上链的文化馆资产。
	 */
	BaseAssetIndexAttentionVO getBaseAssetIndexAttention();
	/**
	 * 资产数据按年度统计 支持切换不同年度、不同数据类型
	 * @param startYear
	 * @param endYear
	 * @param assetsCode
	 * @return
	 */
	List getAssetsByYear(String startYear, String endYear, String assetsCode);

	/**
	 * 按月份统计文化馆资产数据的上链数据量在各个月份的分布
	 * @param year
	 * @param assetCode
	 * @return
	 */
	List getAssetsByMonth(String year, String assetCode);
	/**
	 * 资产数据按数据类型统计
	 * @param year
	 * @return
	 */
	List getAssetsByType(String year);

	/**
	 * get chinese field
	 * @param id
	 * @param assetCode
	 * @return
	 * @throws Exception
	 */
	JSONObject getCZTField(String id, String assetCode) throws Exception;

	/**
	 * 取出资产主键对应的值
	 * @param keyType 类型 （assetKey || assetNameField）
	 * @param baseAssetEntity
	 * @return
	 * @throws Exception
	 */
	Object getFieldValue (String keyType, BaseAssetEntity baseAssetEntity);

	/**
	 * 获取 已上链资产类型数量 已上链资产总数 24已上链资产数量
	 * @return
	 */
	BaseAssetPublicityAttentionVO getBaseAssetPublicityAttention ();

	/**
	 * 获取所有配置的区块链高度并累加到一起
	 * @return
	 */
	Integer getBlockHeight ();

	/**
	 * 返回表对应的资产编码
	 * @param assetCode 资产编码
	 * @return
	 */
	String getAssetCode (String assetCode);

	/**
	 * 通过资产主键ID获取实体
	 * @param assetCode 资产编码
	 * @param assetKeyId 资产主键ID
	 * @return
	 */
	BaseAssetEntity getByAssetId (String assetCode, Object assetKeyId);

	/**
	 * 判断是否存在该主键的资产
	 */
	public Boolean existByAssetKeyVal(@NonNull AssetDefinedEntity assetDefinedEntity,  @NonNull String keyVal) throws Exception;

	/**
	 * 输入String类型的资产主键ID转换成对应的数据类型
	 * @param assetId 资产主键ID值
	 * @param assetDefinedEntity 资产定义实体
	 * @return
	 * @throws Exception
	 */
	Object converAssetId (String assetId,AssetDefinedEntity assetDefinedEntity) throws Exception;

	/**
	 * 资产上链
	 * @param baseAssetEntity
	 */
	void assetUpload (BaseAssetEntity baseAssetEntity);

	/**
	 * 重新上链失败的资产
	 * @param assetIds 资产ids
	 * @param assetCode 资产编码
	 */
	void assetReUpload (List<String> assetIds,String assetCode);

	/**
	 * 根据上链状态统计资产数
	 * @param uploadStatusEnum
	 * @return
	 */
	int statAssetCount(UploadStatusEnum uploadStatusEnum);

}
