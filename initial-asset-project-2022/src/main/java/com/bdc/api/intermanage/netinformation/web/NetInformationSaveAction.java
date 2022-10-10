package com.bdc.api.intermanage.netinformation.web;

import java.util.UUID;

import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import com.bdc.api.rest.container.NetInformationContainer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.common.web.BaseCommonSaveAction;


@Controller
@RequestMapping("/netInformationSave")
public class NetInformationSaveAction extends BaseCommonSaveAction {

	@Autowired
	private INetInformationService netInformationService;

	/**
	 * 保存外网信息
	 */
	@RequestMapping("/save")
	public JsonReturnBean savePermission() throws Exception  {
		try {
			String id = request.getParameter("id");
			String companyName = request.getParameter("companyName");
			String isCallInterfaceStr = request.getParameter("isCallInterface");
			if(StringUtils.isBlank(companyName)) {
				// 企业名称不能为空
				returnBean = new JsonReturnBean();
				returnBean.setHasOk(false);
				returnBean.setTip(JsonReturnBean.TIP_FAIL);
				returnBean.setMessage("企业名称不能为空");
				return returnBean;
			}
			if(StringUtils.isBlank(isCallInterfaceStr)) {
				// 能否访问接口权限不能为空
				returnBean = new JsonReturnBean();
				returnBean.setHasOk(false);
				returnBean.setTip(JsonReturnBean.TIP_FAIL);
				returnBean.setMessage("访问接口权限不能为空");
				return returnBean;
			}
			else if(StringUtils.isBlank(id)) {
				// 新增
				Integer isCallInterface = Integer.valueOf(isCallInterfaceStr);
				NetInformationEntity netInformationEntity = new NetInformationEntity();
				String appId = UUID.randomUUID().toString().replaceAll("-","");
				String appkey = UUID.randomUUID().toString().replaceAll("-","");
				netInformationEntity.setAppId(appId);
				netInformationEntity.setAppkey(appkey);
				netInformationEntity.setCompanyName(companyName);
				netInformationEntity.setIsCallInterface(isCallInterface);
				netInformationService.create(netInformationEntity);
				NetInformationContainer.getInstance().put(netInformationEntity);
			}
			else {
				// 更新
				Integer isCallInterface = Integer.valueOf(isCallInterfaceStr);
				NetInformationEntity netInformationEntity = netInformationService.get(id);
				netInformationEntity.setCompanyName(companyName);
				netInformationEntity.setIsCallInterface(isCallInterface);
				netInformationService.update(netInformationEntity);
				//更新缓存
				NetInformationContainer.getInstance().put(netInformationEntity);
			}
			returnBean = new JsonReturnBean();
			returnBean.setHasOk(true);
			returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
			returnBean.setBean(custForm);
		}catch (Exception e) {
			logger.error("error:",e);
			returnBean = new JsonReturnBean();
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}

}
