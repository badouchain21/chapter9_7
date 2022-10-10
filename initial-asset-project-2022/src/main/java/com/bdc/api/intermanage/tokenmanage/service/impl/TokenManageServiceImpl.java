package com.bdc.api.intermanage.tokenmanage.service.impl;

import java.io.Serializable;

import com.bdc.api.intermanage.tokenmanage.dao.ITokenManageDao;
import com.bdc.api.intermanage.tokenmanage.model.TokenManageEntity;
import com.bdc.api.intermanage.tokenmanage.service.ITokenManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.badou.brms.base.support.spring.BaseSpringService;

@Service
public class TokenManageServiceImpl extends BaseSpringService<TokenManageEntity, Serializable>
		implements ITokenManageService {

	@Autowired
	private ITokenManageDao tokenManageDao;

	@Autowired
	public void setTokenManageDao(ITokenManageDao tokenManageDao){
		this.tokenManageDao = tokenManageDao;
	}
}
