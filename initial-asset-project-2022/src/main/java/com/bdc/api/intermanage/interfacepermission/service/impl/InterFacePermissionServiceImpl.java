package com.bdc.api.intermanage.interfacepermission.service.impl;

import java.io.Serializable;
import java.util.List;

import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.bdc.api.ApiConst;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import com.bdc.api.intermanage.interfacepermission.dao.IInterFacePermissionDAO;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import com.bdc.api.rest.container.InterfacePermissionContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.dboperation.query.ICriterion;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.tools.common.Globals;

@Service
public class InterFacePermissionServiceImpl extends BaseSpringService<InterFacePermissionEntity, Serializable>
					implements IInterFacePermissionService {

	@Autowired
	private IInterFacePermissionDAO interFacePermissionDAO;
	@Autowired
	private INetInformationService netInformationService;
	@Autowired
	private IInterFaceDetailService interfacedetailService;
	@Autowired
	public void setInterFacePermissionDAO(
			IInterFacePermissionDAO interFacePermissionDAO) {
		this.setBaseDAO(interFacePermissionDAO);
	}

	@Override
	public Pagelet findPages(ICriterion criterion){
	    return interFacePermissionDAO.findPages(criterion);
	}

	@Override
	public void delByInterAndNet(String interfaceId, String netInformationId) {

		interFacePermissionDAO.delByInterAndNet(interfaceId, netInformationId);
	}

	@Override
	public String updateInterfacaPermission(String ids) throws Exception {
		String[] idArr = ids.split(Globals.SEPARATOR_COMMA);
		StringBuilder builder = new StringBuilder();
		for (String id : idArr) {
			try{
				InterFacePermissionEntity entity =  interFacePermissionDAO.get(id);
				InterfacePermissionContainer.getInstance().remove(entity);
				if(entity.getInterfacePermission() == Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "0"))){
					entity.setInterfacePermission(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "1")));
				}else{
					entity.setInterfacePermission(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "0")));
				}
				interFacePermissionDAO.update(entity);
				InterfacePermissionContainer.getInstance().put(entity.getNetInformatId(),entity);
			}catch(Exception e){
				builder.append(e.getMessage());
			}
		}
		return builder.toString();
	}

	@Override
	public void initFacePermission(String netId){
		NetInformationEntity netInformationEntity = new NetInformationEntity();
		InterFacePermissionEntity permissionEntity = new InterFacePermissionEntity();
		netInformationEntity = netInformationService.find(netId);
		List<InterFaceDetailEntity> faceDetailEntitys = interfacedetailService.listAll();
		for(InterFaceDetailEntity faceDetailEntity: faceDetailEntitys){
			permissionEntity = new InterFacePermissionEntity();
			permissionEntity.setInterfacePermission(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "1")));
			if(faceDetailEntity!=null){
				permissionEntity.setInterFaceDetailEntity(faceDetailEntity);
				permissionEntity.setNetInformationEntity(netInformationEntity);
				interFacePermissionDAO.create(permissionEntity);
			}
		}
	}

	@Override
	public void initFacePermission(InterFaceDetailEntity interFaceDetailEntity) {
        InterFacePermissionEntity permissionEntity =null;
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("creator", LogonCertificateHolder.getLogonCertificate().getUserId(),null, QueryOperSymbolEnum.eq));
        List<NetInformationEntity> netInformationEntitys = netInformationService.find(queryCriterion);
        Integer permission = Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "1"));
        for (NetInformationEntity netInformationEntity : netInformationEntitys) {
            permissionEntity = new InterFacePermissionEntity();
            permissionEntity.setInterfacePermission(permission);
            permissionEntity.setInterFaceDetailEntity(interFaceDetailEntity);
            permissionEntity.setNetInformationEntity(netInformationEntity);
            interFacePermissionDAO.create(permissionEntity);
        }
	}

}
