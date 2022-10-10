package com.bdc.assetdefine.service;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.badou.brms.base.support.spring.IBaseSpringService;
import com.bdc.assetdefine.model.AssetDefinedEntity;


/**
 * 描述：资产建模定义 服务接口
 * @author linxiaoqing
 * @Date 2020年1月6日 上午10:49:10
 */
public interface IAssetDefinedService extends IBaseSpringService<AssetDefinedEntity, Serializable> {

    /**
     *
     * 描述：根据资产编码获取资产建模定义
     * @author linxiaoqing
     * @Date 2020年1月7日 下午2:47:30
     * @param assetCode
     * @return
     * @throws Exception
     */
    AssetDefinedEntity findByAssetCode(String assetCode) throws Exception;
    /**
	 *  返回所有资产类型 {aasetCode:assetName}
	 * @return
	 */
	Map<String,String> getAllAssetType();

	/**
	 *
	 * 描述：模型编码下拉框
	 * @author linxiaoqing
	 * @Date 2020年1月8日 下午4:54:32
	 * @return
	 */
    JSONArray listMd();
	/**
	 *
	 * 描述：资产建模定义 下拉框
	 * @author linxiaoqing
	 * @Date 2020年1月8日 下午4:54:32
	 * @return
	 */
	JSONArray listDefined();
	/**
	 *
	 * 描述：根据资产Nmae获取资产建模定义
	 * @param assetName
	 * @return
	 * @throws Exception
	 */
	AssetDefinedEntity findByAssetName(String assetName) throws Exception;

	/**
	 * 根据资产模型编码查询字段
	 * @param assetMdCode
	 * @return
	 */
	JSONArray listMdField (String assetMdCode);

}
