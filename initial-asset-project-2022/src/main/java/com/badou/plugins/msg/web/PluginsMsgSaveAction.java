package com.badou.plugins.msg.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.common.web.BaseCommonSaveAction;
import com.badou.plugins.msg.service.IPluginsMsgService;
import com.badou.tools.common.Globals;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.param.ParamIntegerUtils;

/**
 *	sys_plugins_msg 保存实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:39.504
 * @version v1.0
 * @revision 
 */
@RestController
public class PluginsMsgSaveAction extends BaseCommonSaveAction {

	@Autowired
	private IPluginsMsgService msgService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:08:15
	 * @todo 更新插件状态
	 * @return 统一接口封装api
	 */
	@RequestMapping
	public JsonReturnBean updatePluginsStatus() {
		String ids = ParamUtils.getParameter(request, "ids");
		Integer status = ParamIntegerUtils.getParameter(request, "status");
		String[] values = ids.split(Globals.SEPARATOR_COMMA);
		returnBean = new JsonReturnBean();
		try{
			for(String id : values){
				msgService.updatePluginsStatus(id, status);
			}
			returnBean.setHasOk(true);
			returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}
	
	 
}
