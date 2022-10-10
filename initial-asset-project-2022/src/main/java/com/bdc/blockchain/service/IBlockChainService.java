package com.bdc.blockchain.service;

import com.bdc.base.model.BaseAssetEntity;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import com.bdc.common.UploadTypeEnum;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;

import java.util.List;


/**
 * 描述：对接区块链服务接口
 *
 * @author linxiaoqing
 * @date 2019年9月3日
 */
public interface IBlockChainService {

	/**
	 * 获取区块链高度
	 * @return
	 */
	int getBlockHeight(BlockchainConfigEntity blockchainConfig);

	/**
	 *
	 * 描述：资产上链
	 * @author linxiaoqing
	 * @Date 2020年1月8日 上午10:43:09
	 * @param AssetType
	 * @param id
	 */
    void uploadAsset(UploadTypeEnum AssetType, String id);

    /**
     *
     * 描述：更新上链状态
     * @author linxiaoqing
     * @Date 2020年1月8日 下午4:01:14
     * @param logEntities 上链日志实体
     */
    void updateUploadStatus(List<UploadAssetLogEntity> logEntities);

}
