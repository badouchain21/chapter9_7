package com.bdc.api.intermanage.interfacelogger.service.impl;

import java.io.Serializable;

import com.bdc.api.intermanage.interfacelogger.dao.IInterFaceLoggerDao;
import com.bdc.api.intermanage.interfacelogger.model.InterFaceLoggerEntity;
import com.bdc.api.intermanage.interfacelogger.service.IInterFaceLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;

@Service
public class InterFaceLoggerServiceImpl extends BaseSpringService<InterFaceLoggerEntity, Serializable>
implements IInterFaceLoggerService {
	@Autowired
	private IInterFaceLoggerDao interFaceLoggerDAO;

	@Autowired
	public void setInterFaceLoggerDAO(IInterFaceLoggerDao interFaceLoggerDAO) {
		this.interFaceLoggerDAO = interFaceLoggerDAO;
		this.setBaseDAO(interFaceLoggerDAO);

	}


}
