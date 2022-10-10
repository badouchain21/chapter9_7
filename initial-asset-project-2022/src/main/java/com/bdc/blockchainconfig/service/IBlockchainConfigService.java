package com.bdc.blockchainconfig.service;

import java.io.Serializable;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import lombok.NonNull;


/**
 * @author badousoft
 * @date 2020-10-13 18:35:08.968
 * @todo bdc_blockchain_config service接口
 **/
public interface IBlockchainConfigService extends IBaseSpringService<BlockchainConfigEntity, Serializable> {
    /**
     * get configBy Name
     * @param configName
     * @return
     * @throws Exception
     */
    public BlockchainConfigEntity findByConfigName(@NonNull String configName) throws Exception;

    /**
     * 修改配置状态 启用 | 禁用
     * @param ids
     * @return
     */
    String updateConfigStatus(String ids);

}
