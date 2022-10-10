package com.badou.project.moduledemo.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.project.moduledemo.dao.IFun1DemoChildDAO;
import com.badou.project.moduledemo.model.Fun1DemoChildEntity;

/**
 * 功能1子对象示例DAO实现类
 * 
 * @author xiangsf 2013-1-18
 * 
 */
@Repository
public class Fun1DemoChildDAOImpl extends
		BaseHibernateDAO<Fun1DemoChildEntity, Serializable> implements
		IFun1DemoChildDAO {

}
