package com.badou.project.moduledemo.service.impl;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.project.moduledemo.dao.IFun2DemoDAO;
import com.badou.project.moduledemo.model.Fun2DemoEntity;
import com.badou.project.moduledemo.service.IFun2DemoService;
/**
 * 功能2示例业务层接口实现
 * @author xiangsf 2013-1-18
 *
 */
@Service
public class Fun2DemoServiceImpl extends BaseSpringService<Fun2DemoEntity, Serializable>
		implements IFun2DemoService {
	@Autowired
	private IFun2DemoDAO fun2DemoDAO;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入dao
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun2DemoDAO(IFun2DemoDAO fun2DemoDAO) {
		this.fun2DemoDAO = fun2DemoDAO;
		super.setBaseDAO(fun2DemoDAO);
	}
	
	
}
