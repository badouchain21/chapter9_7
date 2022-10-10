package com.badou.project.moduledemo.web.fun1.children;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.brms.base.support.struts.struts2.JsonEditTemplateAction;
import com.badou.project.moduledemo.model.Fun1DemoChildEntity;
import com.badou.project.moduledemo.service.IFun1DemoChildService;
import com.badou.project.moduledemo.service.IFun1DemoService;
import com.badou.project.moduledemo.web.form.Fun1DemoChildForm;
/**
 * 功能1示例编辑
 * <p>对应的请求路径是：${context}/moduledemo/children/fun1/fun1demoedit/方法名.do
 * <p>对应的请求默认页面是：${context}/WEB-INF/jsp/moduledemo/children/fun1/fun1demoedit_edit.jsp
 * @author xiangsf 2013-1-18
 *
 */
@Controller
public class Fun1DemoChildEditAction extends JsonEditTemplateAction<Fun1DemoChildEntity, Serializable, Fun1DemoChildForm> {

	@Autowired
	private IFun1DemoService fun1DemoService;
	@Autowired
	private IFun1DemoChildService fun1DemoChildService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun1DemoChildService(IFun1DemoChildService fun1DemoChildService) {
		this.fun1DemoChildService = fun1DemoChildService;
		super.setBaseService(fun1DemoChildService);
	}
	
	
}
