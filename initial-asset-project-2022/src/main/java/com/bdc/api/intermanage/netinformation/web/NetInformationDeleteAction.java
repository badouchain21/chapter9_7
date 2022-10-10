package com.bdc.api.intermanage.netinformation.web;

import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import com.bdc.api.rest.container.InterfacePermissionContainer;
import com.bdc.api.rest.container.NetInformationContainer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.designer.jdbc.common.web.BaseCommonDeleteAction;
import com.badou.tools.common.Globals;

@Controller
@RequestMapping("/netInformationDelete")
public class NetInformationDeleteAction extends BaseCommonDeleteAction {

	@Autowired
	private INetInformationService netInformationService;

	@Override
	protected void exeBeforeDelete() throws Exception {
		String idStr = this.request.getParameter("ids");
		//清除缓存
		if (StringUtils.isNotEmpty(idStr)){
			if (idStr.indexOf(Globals.SEPARATOR_COMMA) == -1){
				NetInformationEntity netInformationEntity = netInformationService.get(idStr);
				NetInformationContainer.getInstance().remove(netInformationEntity);
				InterfacePermissionContainer.getInstance().removeAll(idStr);
			} else {
				String[] ids = idStr.split(Globals.SEPARATOR_COMMA);
				for (String id : ids) {
					NetInformationEntity netInformationEntity = netInformationService.get(id);
					NetInformationContainer.getInstance().remove(netInformationEntity);
					InterfacePermissionContainer.getInstance().removeAll(id);
				}
			}
		}
	}
}
