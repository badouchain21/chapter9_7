package com.bdc.api.intermanage.interfacedetail.service.impl;

import java.io.Serializable;
import java.util.List;

import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.bdc.api.ApiConst;
import com.bdc.api.intermanage.interfacedetail.dao.IInterFaceDetailDao;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.common.utils.BdLoginUtil;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.service.IModuleDesignService;


@Service
public class InterFaceDetailServiceImpl extends BaseSpringService<InterFaceDetailEntity, Serializable>
implements IInterFaceDetailService {

	@Autowired
	private IInterFaceDetailDao interFaceDetailDao;

	@Autowired
	private IModuleDesignService moduleDesignService;

	@Autowired
	private IInterFacePermissionService interFacePermissionService;

	@Autowired
	private IAssetDefinedService assetDefinedService;

	@Autowired
	private INetInformationService netInformationService;

	@Autowired
	public void setInterFaceDetailDao(IInterFaceDetailDao interFaceDetailDao) {
		this.interFaceDetailDao = interFaceDetailDao;
		this.setBaseDAO(interFaceDetailDao);
	}

	@Override
	public Pagelet findOtherByNetId(String netId,String searchHql,Boolean isOther) {
		NetInformationEntity netInformationEntity = netInformationService.get(netId);
		return interFaceDetailDao.findOtherByNetId(netId,searchHql,isOther,netInformationEntity.getCreator());
	}

	@Override
	public void initInterFaceDetail(@NonNull String id) throws Exception {
	    AssetDefinedEntity assetDefined = assetDefinedService.get(id);
        Integer status = Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_STATUS, "0"));
        Integer type = Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_RESPON_TYPE, "0"));
        String belongSys = DictionaryLib.getItemValueByItemCode(ApiConst.SUBORDINATE_SYSTEM, "0");
        String userId = null;
        if (BdLoginUtil.isSa()){
        	userId = assetDefined.getCreator();
		} else {
        	userId = LogonCertificateHolder.getLogonCertificate().getUserId();
		}
		List<InterFaceDetailEntity> interFaceDetailEntitysDetailEntities = interFaceDetailDao.getEntityByName(assetDefined.getAssetCode(),userId);
		if(interFaceDetailEntitysDetailEntities == null || interFaceDetailEntitysDetailEntities.size() == 0){
			InterFaceDetailEntity interFaceDetailEntity = new InterFaceDetailEntity();
			interFaceDetailEntity.setName(assetDefined.getAssetCode());
			interFaceDetailEntity.setInterfaceDetail(assetDefined.getAssetName());
			interFaceDetailEntity.setStatus(status);
			interFaceDetailEntity.setType(type);
			interFaceDetailEntity.setBelong_sys(belongSys);
			interFaceDetailDao.create(interFaceDetailEntity);
			// 赋予所有企业该接口的权限
			interFacePermissionService.initFacePermission(interFaceDetailEntity);
		} else {
			throw new Exception("接口已存在");
		}
	}

}
