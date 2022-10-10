package com.badou.project.resource.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.system.security.service.IResourceService;
import com.badou.brms.system.security.vo.RoleResource;
import com.badou.project.resource.dao.IRoleResourceDAO;
import com.badou.project.resource.service.IRoleResourceService;

/**
 * 用户菜单权限service接口实现类
 * @author xiaowange
 *
 */
@Service
@Transactional
public class RoleResourceServiceImpl  extends BaseSpringService<RoleResource, Serializable>
implements IRoleResourceService{

	@Autowired
	private IRoleResourceDAO roleResourceDAO;
	
	@Autowired
	private IResourceService resourceService ;
	
	/**
	 * 设置基类中的baseDao
	 * @param roleResourceDAO
	 */
	@Autowired
	public void setRoleResourceDAO(IRoleResourceDAO roleResourceDAO) {
		this.roleResourceDAO = roleResourceDAO;
		super.baseDAO = roleResourceDAO ;
	}

	@Override
	public JsonReturnBean deleteResource(String id) {
		JsonReturnBean returnBean = new JsonReturnBean() ;
		try {
			roleResourceDAO.deleteByResourceId(id) ;
			resourceService.delete(id);
			returnBean.setHasOk(true);
			returnBean.setMessage(JsonReturnBean.TIP_SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			returnBean.setHasOk(false);
			returnBean.setMessage(JsonReturnBean.TIP_FAIL);
		}
		return returnBean;
	}
}
