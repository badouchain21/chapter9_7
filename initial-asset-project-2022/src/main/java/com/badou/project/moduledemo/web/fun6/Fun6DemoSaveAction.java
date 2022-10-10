package com.badou.project.moduledemo.web.fun6;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.brms.base.support.struts.struts2.JsonSaveTemplateAction;
import com.badou.project.moduledemo.model.Fun6DemoEntity;
import com.badou.project.moduledemo.service.IFun6DemoService;
import com.badou.project.moduledemo.web.form.Fun6DemoForm;
/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:27:41
 * @todo demo6保存接口类
 */
@Controller
public class Fun6DemoSaveAction extends JsonSaveTemplateAction<Fun6DemoEntity, Serializable, Fun6DemoForm> {

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
