package com.badou.project.moduledemo.web.fun6;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonEditTemplateAction;
import com.badou.project.moduledemo.model.Fun6DemoEntity;
import com.badou.project.moduledemo.service.IFun6DemoService;
import com.badou.project.moduledemo.web.form.Fun6DemoForm;
/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:28:52
 * @todo demo6编辑接口类
 */
@RestController
public class Fun6DemoEditAction extends JsonEditTemplateAction<Fun6DemoEntity, Serializable, Fun6DemoForm> {

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
