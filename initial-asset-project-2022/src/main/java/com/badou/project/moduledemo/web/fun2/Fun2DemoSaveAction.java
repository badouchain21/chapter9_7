package com.badou.project.moduledemo.web.fun2;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonSaveTemplateAction;
import com.badou.project.moduledemo.model.Fun2DemoEntity;
import com.badou.project.moduledemo.service.IFun2DemoService;
import com.badou.project.moduledemo.web.form.Fun2DemoForm;
/**
 * 功能1示例保存新增事件
 * <p>对应的请求路径是：${context}/moduledemo/Fun2/Fun2demosave/save.do
 * @author xiangsf 2013-1-18
 *
 */
@RestController
public class Fun2DemoSaveAction extends JsonSaveTemplateAction<Fun2DemoEntity, Serializable, Fun2DemoForm> {

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
	
	
}
