package com.bdc.assetdefine.web;

import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import com.bdc.blockchainconfig.service.IBlockchainConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.designer.jdbc.common.web.BaseCommonSaveAction;

import java.util.Objects;

/**
 * 描述：资产建模定义 保存事件
 * @author linxiaoqing
 * @Date 2020年1月6日 上午10:54:57
 */
@RestController
@RequestMapping("assetDefined")
public class AssetDefinedSaveAction extends BaseCommonSaveAction {
    @Autowired
    private IBlockchainConfigService blockchainConfigService;
    @Autowired
    private IAssetDefinedService assetDefinedService;

    @Override
    protected void exeBeforeSave() throws Exception {
        super.exeBeforeSave();

        BlockchainConfigEntity blockchainConfigEntity = blockchainConfigService.findByConfigName(custForm.getFieldValue("configName"));
        if(blockchainConfigEntity==null){
            throw new Exception("CONFIG NOT FOUND");
        }
        this.custForm.getDetails().put("configId", new String[]{blockchainConfigEntity.getId()});

        if (!this.create){
            AssetDefinedEntity assetDefinedEntity = assetDefinedService.get(this.custForm.getId());
            String assetCode = this.custForm.getFieldValue("assetCode");
            if (!Objects.equals(assetCode,assetDefinedEntity.getAssetCode())){
                throw new Exception("资产编码不可以修改");
            }
        }
    }

}
