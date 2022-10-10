package com.badou.project.moduledemo.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.project.moduledemo.dao.IFun6DemoDAO;
import com.badou.project.moduledemo.model.Fun6DemoEntity;
import com.badou.project.moduledemo.service.IFun6DemoService;
/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:13:18
 * @todo demo6service接口实现类
 */
@Service
public class Fun6DemoServiceImpl extends BaseSpringService<Fun6DemoEntity, Serializable> implements IFun6DemoService{
	@Autowired
	private IFun6DemoDAO customerDao;
	//要提供setter方法  把dao实现类赋给父类的baseDao 
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入dao
	 * @param fun3DemoService
	 */
	@Autowired
	public void setCustomerDao(IFun6DemoDAO customerDao) {
		this.customerDao = customerDao;
		super.baseDAO = customerDao;
	}
	
}
