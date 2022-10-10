package com.badou.project.moduledemo.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.project.moduledemo.dao.IFun2DemoDAO;
import com.badou.project.moduledemo.model.Fun2DemoEntity;
/**
 * 功能2示例DAO实现类
 * @author xiangsf 2013-1-18
 *
 */
@Repository
public class Fun2DemoDAOImpl extends
		BaseHibernateDAO<Fun2DemoEntity, Serializable> implements IFun2DemoDAO {
	
}
