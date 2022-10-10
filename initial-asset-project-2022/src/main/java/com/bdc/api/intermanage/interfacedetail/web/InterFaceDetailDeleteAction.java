package com.bdc.api.intermanage.interfacedetail.web;

import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.common.web.BaseCommonDeleteAction;
import com.badou.tools.common.Globals;

@Controller
@Slf4j
public class InterFaceDetailDeleteAction extends BaseCommonDeleteAction {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */

	@Autowired
	private IInterFacePermissionService facePermissionService;

	@Autowired
	private IInterFaceDetailService interFaceDetailService;

	@RequestMapping
	@com.badou.designer.jdbc.common.annotations.BaseMdJsonStack
	public JsonReturnBean delete() {
		try{
			returnBean = new JsonReturnBean();
			if(ids != null){
				String vs = ids;
				String[] values = vs.toString().split(Globals.SEPARATOR_COMMA);
				for(String str : values){
					interFaceDetailService.delete(str);
					facePermissionService.delByInterAndNet(str,null);
				}

			}
			returnBean.setHasOk(true);
			returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("error:",e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}
}
