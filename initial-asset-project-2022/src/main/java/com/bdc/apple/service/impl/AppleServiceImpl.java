package com.bdc.apple.service.impl;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.bdc.apple.dao.IAppleDAO;
import com.bdc.apple.model.AppleEntity;
import com.bdc.apple.service.IAppleService;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.annotate.UploadAssetTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;


/**
 *	苹果管理service服务实现
 * @author zhongzhilong
 * @date 2020-11-04 11:34
 */
@Service
@UploadAssetTypeHandler(UploadTypeEnum.APPLE)
public class AppleServiceImpl extends
		BaseSpringService<AppleEntity, Serializable> implements IAppleService {

	@Autowired
	private IAppleDAO appleDAO;

	@Autowired
	public void setAppleDAO(IAppleDAO appleDAO) {
		this.appleDAO = appleDAO;
		super.setBaseDAO(appleDAO);
	}

}



