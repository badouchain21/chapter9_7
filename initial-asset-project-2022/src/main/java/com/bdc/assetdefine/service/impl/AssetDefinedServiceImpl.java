package com.bdc.assetdefine.service.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.badou.brms.dboperation.query.QueryParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.designer.jdbc.core.service.IBaseModuleService;
import com.badou.designer.module.design.model.MdFieldEntity;
import com.bdc.assetdefine.dao.IAssetDefinedDAO;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.common.utils.BdLoginUtil;
import com.bdc.common.utils.EmptyUtil;
import com.google.common.collect.Lists;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.service.IModuleDesignService;


/**
 * 描述：资产建模定义 服务实现
 * @author linxiaoqing
 * @Date 2020年1月6日 上午10:50:03
 */
@Service
public class AssetDefinedServiceImpl extends BaseSpringService<AssetDefinedEntity, Serializable> implements IAssetDefinedService {

    @Autowired
    private IAssetDefinedDAO assetDefinedDao;

    @Autowired
    private IModuleDesignService moduleDesignService;

    @Autowired
    private BaseUploadAssetFactory baseUploadAssetFactory;

    @Autowired
    public void setAssetDefineDAO(IAssetDefinedDAO assetDefinedDao) {
        this.assetDefinedDao = assetDefinedDao;
        super.setBaseDAO(assetDefinedDao);
    }

    @Override
    public AssetDefinedEntity findByAssetCode(@NonNull String assetCode) throws Exception {
        AssetDefinedEntity assetDefined = assetDefinedDao.findByAssetCode(assetCode);
        if (Objects.isNull(assetDefined)) {
            throw new Exception("获取资产建模定义失败！");
        }
        return assetDefined;
    }

	@Override
	public Map<String, String> getAllAssetType() {
		Map<String,String> assetsMap = new HashMap<String,String>();
		List<AssetDefinedEntity> assetDefineds = this.listAll();
		assetDefineds.forEach((assetdefined)->{
			assetsMap.put(assetdefined.getAssetCode(), assetdefined.getAssetName());
		});
		return assetsMap;
	}

	@Override
	public JSONArray listMd() {
	    // 找出数据库表类为单证表的模型
        List<String> dbTables = baseUploadAssetFactory.getTableMap().keySet().stream().collect(Collectors.toList());
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("base_class", "com.badou.designer.module.design.base.BaseModuleEntity", null, QueryOperSymbolEnum.like));
        if(dbTables!=null && dbTables.size()>0){
            queryCriterion.addParam(new QueryHibernatePlaceholderParam("db_table", dbTables, null, QueryOperSymbolEnum.in, QueryParam.PARAM_PLACEHOLDER_NAME));
        }
        List<MdModuleEntity> mds = moduleDesignService.find(queryCriterion);

        JSONArray selector = new JSONArray();
        for (MdModuleEntity md : mds) {
            JSONObject obj = new JSONObject();
            obj.put("id", md.getCode());
            selector.add(obj);
        }
        return selector;
    }
    @Override
    public JSONArray listDefined() {
        List<AssetDefinedEntity> mds = Lists.newArrayList();
        if (!BdLoginUtil.isSa()){
            QueryCriterion queryCriterion = new QueryCriterion();
            queryCriterion.addParam(new QueryHibernatePlaceholderParam("creator", LogonCertificateHolder.getLogonCertificate().getUserId(),null,QueryOperSymbolEnum.eq));
            mds.addAll(assetDefinedDao.find(queryCriterion));
        } else {
            mds.addAll(assetDefinedDao.findAll());
        }
        JSONArray selector = new JSONArray();
        for (AssetDefinedEntity md : mds) {
            JSONObject obj = new JSONObject();
            obj.put("id", md.getAssetName());
            selector.add(obj);
        }
        return selector;
    }
    @Override
    public AssetDefinedEntity findByAssetName(@NonNull String assetName) throws Exception {
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("assetName",assetName,null,QueryOperSymbolEnum.eq));

        List<AssetDefinedEntity> assetDefineds = assetDefinedDao.find(queryCriterion);
        if (assetDefineds!=null && assetDefineds.size()>0) {
           return assetDefineds.get(0);
        }
        return null;
    }

    @Override
    public JSONArray listMdField(String assetMdCode) {
        JSONArray jsonArray = new JSONArray();
        // 找出数据库表类为单证表的模型
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("base_class", "com.badou.designer.module.design.base.BaseModuleEntity", null, QueryOperSymbolEnum.like));
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("code", assetMdCode, null, QueryOperSymbolEnum.eq));
        List<MdModuleEntity> mds = moduleDesignService.find(queryCriterion);
        if (EmptyUtil.isEmpty(mds)){
            return jsonArray;
        }
        MdModuleEntity moduleEntity = mds.get(0);

        for (MdFieldEntity field : moduleEntity.getFields()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",field.getEntityName());
            jsonObject.put("text",field.getFieldName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
