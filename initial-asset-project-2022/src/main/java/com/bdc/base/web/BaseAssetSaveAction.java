package com.bdc.base.web;

import com.badou.core.standard.enums.SystemBoolean;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import com.bdc.blockchainconfig.service.IBlockchainConfigService;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.UploadTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;
import com.badou.designer.jdbc.common.web.BaseCommonSaveAction;
import com.badou.designer.jdbc.core.vo.BaseVO;


/**
 * 通用资产列表实现类
 * @author lps
 *
 */
@Controller
@RequestMapping("/bdc/baseAssetSave")
public class BaseAssetSaveAction extends BaseCommonSaveAction{
	    @Autowired
	    private IBaseAssetService baseAssetService;
	    @Autowired
		private IAssetDefinedService assetDefinedService;
	    @Autowired
		private BaseUploadAssetFactory baseUploadAssetFactory;

	    @Override
	    protected void exeBeforeSave() throws Exception {
//	    	设置未上链
	       this.custForm.getDetails().put("uploadStatus", new String[]{UploadStatusEnum.UNUPLOAD.getValue().toString()});
	       this.custForm.getDetails().put("bdcBaasLogId", new String[]{null});

	       if(this.create){
			   String assetCode = 	baseUploadAssetFactory.getCodeByTableName(this.moduleBean.getDbTable());
			   if(StringUtils.isBlank(assetCode)){
				   throw new Exception("资产 NOT FOUND");
			   }
			   this.custForm.getDetails().put("assetCode", new String[]{assetCode});
			   this.custForm.getDetails().put("assetName", new String[]{UploadTypeEnum.getUploadTypeEnumByCode(assetCode).name});
			   this.custForm.getDetails().put("isCreate", new String[]{SystemBoolean.NO.getKey().toString()});
		   }else{
			   BaseVO assetVO = this.baseModuleService.get(this.custForm.getId());
			   AssetDefinedEntity assetDefinedEntity = assetDefinedService.findByAssetCode(assetVO.getFieldValueByEntityName("assetCode")==null?null:assetVO.getFieldValueByEntityName("assetCode").toString());
			   if(!assetVO.getFieldValueByEntityName(assetDefinedEntity.getAssetKey()).equals(custForm.getFieldValue(assetDefinedEntity.getAssetKey()))){
				   throw new Exception("资产主键不可修改");
			   }
		   }

		}
}
