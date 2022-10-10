package com.badou.project.moduledemo.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.project.moduledemo.dao.IFun7DemoDAO;
import com.badou.project.moduledemo.model.Fun7DemoEntity;
import com.badou.project.moduledemo.service.IFun7DemoService;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:13:32
 * @todo demo7service接口实现类
 */
@Service
public class Fun7DemoServiceImpl extends BaseSpringService<Fun7DemoEntity, Serializable> implements IFun7DemoService {
	@Autowired
	private IFun7DemoDAO fun7DemoDAO;
	/**
	 * 在注入service类或者dao类的时候
	 *描述：
	 * @param fun7DemoDAO
	 *
	 */
	@Autowired
	public void setFun7DemoDAO(IFun7DemoDAO fun7DemoDAO) {
		this.fun7DemoDAO = fun7DemoDAO;
		super.baseDAO=fun7DemoDAO;
	}
	
	
	
	
}
