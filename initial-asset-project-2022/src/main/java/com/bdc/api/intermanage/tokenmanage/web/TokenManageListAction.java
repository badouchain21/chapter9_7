package com.bdc.api.intermanage.tokenmanage.web;


import com.bdc.api.intermanage.tokenmanage.service.ITokenManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.designer.jdbc.common.web.BaseCommonListAction;

@Controller
public class TokenManageListAction extends BaseCommonListAction {

	/**
	 *
	 */

	@Autowired
	private ITokenManageService tokenManageService;

	@Autowired
	public void setTokenManageService(ITokenManageService tokenManageService){
		this.tokenManageService = tokenManageService;
	}

}
