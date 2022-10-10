package com.badou.project.moduledemo.web.fun6;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.used.json.BaseJsonDeleteStrutsAction;
import com.badou.project.moduledemo.model.Fun6DemoEntity;
import com.badou.project.moduledemo.service.IFun6DemoService;
/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:28:26
 * @todo demo6删除接口类
 */
@RestController
public class Fun6DemoDeleteAction extends BaseJsonDeleteStrutsAction<Fun6DemoEntity, Serializable> {

	@Autowired 
	private IFun6DemoService customerService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setCustomerService(IFun6DemoService customerService) {
		this.customerService = customerService;
		super.baseService = customerService;
	}
	
	
	
}
