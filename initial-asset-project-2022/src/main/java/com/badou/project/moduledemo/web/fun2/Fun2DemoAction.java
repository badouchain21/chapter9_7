package com.badou.project.moduledemo.web.fun2;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.project.moduledemo.model.Fun2DemoEntity;
import com.badou.project.moduledemo.service.IFun2DemoService;
import com.badou.project.moduledemo.web.form.Fun2DemoForm;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:12:04
 * @todo demo2列表接口类
 */
@RestController
public class Fun2DemoAction extends JsonListTemplateAction<Fun2DemoEntity, Serializable, Fun2DemoForm> {
	
	@Autowired
	private IFun2DemoService fun2DemoService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun2DemoService(IFun2DemoService fun2DemoService) {
		this.fun2DemoService = fun2DemoService;
		super.setBaseService(fun2DemoService);
	}
	
	List<Fun2DemoEntity> list = null;
	  
	/* @Action
     public HttpHeaders index(){  
		 list =  Fun2DemoService.listAll();
    	 return  new DefaultHttpHeaders("index").disableCaching(); 
     }*/  
}
