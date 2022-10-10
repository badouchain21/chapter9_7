package com.badou.bpms.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.badou.brms.base.support.BaseAction;
import com.badou.tools.common.util.ParamUtils;
/**
 * @author chenjiabao
 * @date 2019年7月2日下午3:37:13
 * @todo 流程相关的Action基类
 */
public class BaseStrutsBpmsAction extends BaseAction {

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:36:57
	 * @todo 获得流程中的业务对象ID
	 * @return 对象id
	 */
	protected String getBusinessObjectId(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String boId = (String)request.getAttribute("boId");
		if(StringUtils.isBlank(boId)){
			boId = ParamUtils.getParameter(request, "boId");
		}
		return boId;
	}
}
