package com.bdc.api.intermanage.interfacepermission.web;

import com.bdc.api.ApiConst;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.designer.jdbc.common.web.BaseCommonSaveAction;
import com.badou.tools.common.Globals;
import com.badou.tools.common.util.StringUtils;


@RestController
@Api(value = "InterFacePermissionSaveAction")
@RequestMapping(value="/api/intermanage/interfacepermission/interfacepermissionsave/")
public class InterFacePermissionSaveAction extends BaseCommonSaveAction {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */

	@Autowired
	private IInterFaceDetailService interfacedetailService;
	@Autowired
	private INetInformationService NetInformationService;
	@Autowired
	private IInterFacePermissionService interFacePermissionService;

	@RequestMapping(value="/savePermission")
	@BaseMdJsonStack
	public JsonReturnBean savePermission() throws Exception {
		InterFaceDetailEntity faceDetailEntity = new InterFaceDetailEntity();
		NetInformationEntity netInformationEntity = new NetInformationEntity();
		InterFacePermissionEntity permissionEntity = new InterFacePermissionEntity();
		try{

			String ids = request.getParameter("ids");
			String netId = request.getParameter("netId");
			String interfaceId = request.getParameter("interfaceId");

			//保存外网权限
			if(StringUtils.isNotBlank(netId)){
				if(!StringUtils.isNotBlank(ids)){
					returnBean.setHasOk(false);
					returnBean.setTip(JsonReturnBean.TIP_FAIL);
					returnBean.setMessage("数据缺失,请重试");
					return returnBean;
				}

				netInformationEntity = NetInformationService.find(netId);
				if(netInformationEntity==null){
					returnBean.setHasOk(false);
					returnBean.setTip(JsonReturnBean.TIP_FAIL);
					returnBean.setMessage("未知错误");
                    return returnBean;
				}

				String[] values = ids.split(Globals.SEPARATOR_COMMA);
				for(String value: values){
					permissionEntity = new InterFacePermissionEntity();
					faceDetailEntity = interfacedetailService.get(value);
					permissionEntity.setInterfacePermission(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "1")));
					if(faceDetailEntity!=null){
						permissionEntity.setInterFaceDetailEntity(faceDetailEntity);
						permissionEntity.setNetInformationEntity(netInformationEntity);
						permissionEntity.setInterfaceId(faceDetailEntity.getId());
						permissionEntity.setNetInformationEntity(netInformationEntity);
						interFacePermissionService.create(permissionEntity);
					}
				}
				returnBean = new JsonReturnBean();
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
				returnBean.setBean(custForm);
			}

			//保存接口权限
			if(StringUtils.isNotBlank(interfaceId)){
				if(!StringUtils.isNotBlank(ids)){
					returnBean.setHasOk(false);
					returnBean.setTip(JsonReturnBean.TIP_FAIL);
					returnBean.setMessage("数据缺失,请重试!");
                    return returnBean;
				}

				faceDetailEntity = interfacedetailService.find(interfaceId);
				if(faceDetailEntity==null){
					returnBean.setHasOk(false);
					returnBean.setTip(JsonReturnBean.TIP_FAIL);
					returnBean.setMessage("数据缺失,请重试!");
                    return returnBean;
				}

				String[] values = ids.split(Globals.SEPARATOR_COMMA);
				for(String value: values){
					permissionEntity = new InterFacePermissionEntity();
					netInformationEntity = NetInformationService.find(value);
                    permissionEntity.setInterfacePermission(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "1")));
					if(netInformationEntity!=null){
						permissionEntity.setInterFaceDetailEntity(faceDetailEntity);
						permissionEntity.setNetInformationEntity(netInformationEntity);
						interFacePermissionService.create(permissionEntity);
					}
				}
				returnBean = new JsonReturnBean();
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
				returnBean.setBean(custForm);
			}

		}catch(Exception e){
			logger.error("error:",e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
        return returnBean;
	}



}
