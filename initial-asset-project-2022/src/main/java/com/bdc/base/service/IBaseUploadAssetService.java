package com.bdc.base.service;

import com.bdc.base.model.BaseAssetEntity;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.UploadTypeEnum;

import java.util.List;


/**
 * 描述：基础资产上链接口
 * @author linxiaoqing
 * @Date 2020年1月8日 上午10:08:06
 */
public interface IBaseUploadAssetService {

    /**
     *
     * 描述：获取资产
     * @author linxiaoqing
     * @Date 2020年1月8日 上午10:33:34
     * @param AssetType
     * @param id
     * @return
     */

    BaseAssetEntity get(UploadTypeEnum AssetType, String id);

    /**
     *
     * 描述：更新上链状态
     * @author linxiaoqing
     * @Date 2020年1月8日 上午10:33:46
     * @param AssetType
     * @param asset
     * @param status
     */
    void updateStatus(UploadTypeEnum AssetType, BaseAssetEntity asset, UploadStatusEnum status);

    /**
     *
     * 描述：保存上链日志ID
     * @author linxiaoqing
     * @Date 2020年1月8日 上午10:33:58
     * @param AssetType
     * @param asset
     * @param logId
     */
    void saveLogId(UploadTypeEnum AssetType, BaseAssetEntity asset, String logId);

    /**
     *
     * 描述：保存交易Hash
     * @author linxiaoqing
     * @Date 2020年1月8日 下午3:59:48
     * @param AssetType
     * @param asset
     * @param hash
     */
    void saveHash(UploadTypeEnum AssetType, BaseAssetEntity asset, String hash);

    /**
     *
     * 描述：获取未上链数据
     * @author linxiaoqing
     * @Date 2020年1月9日 上午10:35:38
     * @param AssetType
     * @return
     */
    List<BaseAssetEntity> findUnUpload(UploadTypeEnum AssetType);

    /**
     *
     * 描述：获取上链中的数据
     * @author linxiaoqing
     * @Date 2020年1月9日 上午10:36:01
     * @param AssetType
     * @return
     */
    List<BaseAssetEntity> findUploadingAndLogId(UploadTypeEnum AssetType);

}
