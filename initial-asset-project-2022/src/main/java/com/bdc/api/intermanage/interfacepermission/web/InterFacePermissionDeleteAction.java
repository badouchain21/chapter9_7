package com.bdc.api.intermanage.interfacepermission.web;


import com.badou.designer.jdbc.common.web.BaseCommonDeleteAction;
import com.badou.tools.common.Globals;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import com.bdc.api.rest.container.InterfacePermissionContainer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/interfacePermissonDelete")
public class InterFacePermissionDeleteAction extends BaseCommonDeleteAction {

	@Autowired
	private IInterFacePermissionService interFacePermissionService;

	@Override
	protected void exeBeforeDelete() throws Exception {
		String idStr = this.request.getParameter("ids");
		//清除缓存
		if (StringUtils.isNotEmpty(idStr)){
			if (idStr.indexOf(Globals.SEPARATOR_COMMA) == -1){
				InterFacePermissionEntity interFacePermissionEntity = interFacePermissionService.get(idStr);
				InterfacePermissionContainer.getInstance().remove(interFacePermissionEntity);
			} else {
				String[] ids = idStr.split(Globals.SEPARATOR_COMMA);
				for (String id : ids) {
					InterFacePermissionEntity interFacePermissionEntity = interFacePermissionService.get(id);
					InterfacePermissionContainer.getInstance().remove(interFacePermissionEntity);
				}
			}
		}
	}
}
