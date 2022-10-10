package com.badou.plugins.log.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.common.web.BaseCommonSaveAction;
import com.badou.plugins.log.service.IPluginsLogService;
import com.badou.tools.common.util.ParamUtils;

/**
 *	sys_plugins_logs 保存实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:56:16.853
 * @version v1.0
 * @revision 
 */
@RestController
public class PluginsLogSaveAction extends BaseCommonSaveAction {

	@Autowired
	private IPluginsLogService logService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:58:36
	 * @todo 更新插件状态
	 * @return 统一接口封装api
	 */
	@RequestMapping
	public JsonReturnBean updatePluginsState() {
		String id = ParamUtils.getParameter(request, "id");
		String code = ParamUtils.getParameter(request, "code");
		String beanName = ParamUtils.getParameter(request, "beanName");
		  
		returnBean = new JsonReturnBean();
		try{
			logService.updatePluginsState(code, id ,beanName);
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
