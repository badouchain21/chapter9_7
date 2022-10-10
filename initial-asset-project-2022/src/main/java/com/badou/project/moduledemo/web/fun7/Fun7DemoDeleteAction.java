package com.badou.project.moduledemo.web.fun7;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.used.json.BaseJsonDeleteStrutsAction;
import com.badou.project.moduledemo.model.Fun7DemoEntity;
import com.badou.project.moduledemo.service.IFun7DemoService;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:27:26
 * @todo demo7删除接口类
 */
@RestController
public class Fun7DemoDeleteAction extends BaseJsonDeleteStrutsAction<Fun7DemoEntity, Serializable>{

	@Autowired
	private IFun7DemoService fun7DemoService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun7DemoService(IFun7DemoService fun7DemoService) {
		this.fun7DemoService = fun7DemoService;
		super.baseService=fun7DemoService;
	}
	
}
