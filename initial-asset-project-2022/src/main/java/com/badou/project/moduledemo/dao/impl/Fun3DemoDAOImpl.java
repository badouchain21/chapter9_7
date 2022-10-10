package com.badou.project.moduledemo.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.project.moduledemo.dao.IFun3DemoDAO;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
/**
 * 功能1示例DAO实现类
 * @author xiangsf 2013-1-18
 *
 */
@Repository
public class Fun3DemoDAOImpl extends
		BaseHibernateDAO<Fun3DemoEntity, Serializable> implements IFun3DemoDAO {
	
}
