package com.bdc.base.service.impl;

import java.io.Serializable;
import java.util.List;

import com.badou.core.standard.enums.SystemBoolean;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseUploadAssetService;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.UploadTypeEnum;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.SortOrderEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;

/**
 * 描述：基础资产上链服务
 * @author linxiaoqing
 * @Date 2020年1月9日 上午9:51:20
 */
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseUploadAssetServiceImpl implements IBaseUploadAssetService {
	@Autowired
	private BaseUploadAssetFactory baseAssetUploadAssetFactory;

    @Override
    public BaseAssetEntity get(@NonNull UploadTypeEnum assetType, @NonNull String id) {
        IBaseSpringService<? extends BaseAssetEntity, Serializable> assetService = baseAssetUploadAssetFactory.getBusinessService(assetType.getCode());
        return assetService.get(id);
    }

    @Override
    public List<BaseAssetEntity> findUnUpload(@NonNull UploadTypeEnum assetType) {
        IBaseSpringService assetService = baseAssetUploadAssetFactory.getBusinessService(assetType.getCode());
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("upload_status", UploadStatusEnum.UNUPLOAD.getValue(), null, QueryOperSymbolEnum.eq));
        queryCriterion.addOrder(new QueryOrderby(1,"update_time",SortOrderEnum.ase));
        queryCriterion.setMaxResults(50);
        return assetService.find(queryCriterion);
    }

    @Override
    public List<BaseAssetEntity> findUploadingAndLogId(@NonNull UploadTypeEnum assetType) {
        IBaseSpringService assetService = baseAssetUploadAssetFactory.getBusinessService(assetType.getCode());
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("upload_status", UploadStatusEnum.UPLOADING.getValue(), null, QueryOperSymbolEnum.eq));
//        queryCriterion.addParam(new QueryHibernatePlaceholderParam("bdc_baas_log_id", null, null, QueryOperSymbolEnum.isnotnull));
        queryCriterion.addOrder(new QueryOrderby(1,"update_time",SortOrderEnum.ase));
        queryCriterion.setMaxResults(50);
        return assetService.find(queryCriterion);
    }

    @Override
    public void updateStatus(@NonNull UploadTypeEnum assetType, @NonNull BaseAssetEntity asset, @NonNull UploadStatusEnum status) {
        IBaseSpringService assetService = baseAssetUploadAssetFactory.getBusinessService(assetType.getCode());
        asset.setUploadStatus(status.getValue());
        assetService.update(asset);
    }

    @Override
    public void saveLogId(@NonNull UploadTypeEnum assetType, @NonNull BaseAssetEntity asset, @NonNull String logId) {
        IBaseSpringService assetService = baseAssetUploadAssetFactory.getBusinessService(assetType.getCode());
        asset.setBdcBaasLogId(logId);
        assetService.update(asset);
    }

    @Override
    public void saveHash(@NonNull UploadTypeEnum assetType, @NonNull BaseAssetEntity asset, @NonNull String hash) {
        IBaseSpringService assetService = baseAssetUploadAssetFactory.getBusinessService(assetType.getCode());
        asset.setHash(hash);
        assetService.update(asset);
    }

}
