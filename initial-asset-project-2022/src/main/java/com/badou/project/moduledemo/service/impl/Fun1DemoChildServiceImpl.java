package com.badou.project.moduledemo.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.project.moduledemo.dao.IFun1DemoChildDAO;
import com.badou.project.moduledemo.model.Fun1DemoChildEntity;
import com.badou.project.moduledemo.service.IFun1DemoChildService;
/**
 * 功能1示例子对象业务层接口实现
 * <p>该实现可以没有，将子对象业务实现纳入到父对象{@link Fun1DemoServiceImpl}中,,在父对象中注入{@link IFun1DemoChildDAO}
 * @author xiangsf 2013-1-18
 *
 */
@Service
public class Fun1DemoChildServiceImpl extends BaseSpringService<Fun1DemoChildEntity, Serializable>
		implements IFun1DemoChildService {
	@Autowired
	private IFun1DemoChildDAO fun1DemoChildDAO;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入dao
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun1DemoChildDAO(IFun1DemoChildDAO fun1DemoChildDAO) {
		this.fun1DemoChildDAO = fun1DemoChildDAO;
		super.setBaseDAO(fun1DemoChildDAO);
	}
	
	
}
