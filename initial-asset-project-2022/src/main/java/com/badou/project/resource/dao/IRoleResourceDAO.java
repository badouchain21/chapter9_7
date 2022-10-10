package com.badou.project.resource.dao;

import java.io.Serializable;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.brms.system.security.vo.RoleResource;

/**
 * 角色菜单dao层接口
 * @author xiaowange
 *
 */
public interface IRoleResourceDAO extends IBaseHibernateDAO<RoleResource, Serializable>{

	/**
	 * 根据菜单id删除角色和菜单的关联关系
	 * @param id
	 */
	void deleteByResourceId(String id);

}
