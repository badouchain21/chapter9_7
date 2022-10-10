package com.badou.platform.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.platform.dao.IResourceBakDAO;
import com.badou.platform.model.ResourceBakEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:48:12
 * @todo 资源备份dao层相关接口实现类
 */
@Repository
public class ResourceBakDAOImpl extends BaseHibernateDAO<ResourceBakEntity, Serializable> implements IResourceBakDAO {

}
