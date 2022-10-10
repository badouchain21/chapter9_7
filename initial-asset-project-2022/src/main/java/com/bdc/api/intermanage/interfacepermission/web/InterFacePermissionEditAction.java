package com.bdc.api.intermanage.interfacepermission.web;

import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.designer.jdbc.common.web.BaseCommonEditAction;
import com.badou.tools.common.util.ParamUtils;

@RestController
@Api(value = "InterFacePermissionEditAction")
@RequestMapping(value="/api/intermanage/interfacepermission/interfacepermissionedit")
public class InterFacePermissionEditAction extends BaseCommonEditAction  {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */

	protected String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Autowired
	private IInterFacePermissionService interFacePermissionService;


	@RequestMapping(value="/updateInterfacaPermission")
	@BaseMdJsonStack
	public JsonReturnBean updateInterfacaPermission() throws Exception {
		returnBean = new JsonReturnBean();
		try {
		    String ids = ParamUtils.getParameter(request, "ids");
			String msg = null;
			if (ids != null) {
				msg = interFacePermissionService.updateInterfacaPermission(ids.toString());
			}
			if(StringUtils.isNotBlank(msg)){
				returnBean.setHasOk(false);
				returnBean.setTip(JsonReturnBean.TIP_FAIL);
				returnBean.setMessage(msg);
			}else{
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("error:",e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}
}
