package com.badou.platform.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.platform.dao.IResourceBakDAO;
import com.badou.platform.model.ResourceBakEntity;
import com.badou.platform.service.IResourceBakService;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:25:04
 * @todo 资源备份service接口实现类
 */
@Service
public class ResourceBakServiceImpl extends BaseSpringService<ResourceBakEntity, Serializable> implements IResourceBakService {

	@Autowired
	private IResourceBakDAO resourceBakDAO;
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:35:19
	 * @todo 设置默认使用的dao
	 * @param resourceBakDAO
	 */
	@Autowired
	public void setResourceBakDAO(IResourceBakDAO resourceBakDAO) {
		this.resourceBakDAO = resourceBakDAO;
		super.setBaseDAO(resourceBakDAO);
	}
	
}
