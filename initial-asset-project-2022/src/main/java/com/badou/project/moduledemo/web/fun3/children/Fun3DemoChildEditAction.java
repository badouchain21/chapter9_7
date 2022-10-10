package com.badou.project.moduledemo.web.fun3.children;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonEditTemplateAction;
import com.badou.project.moduledemo.model.Fun3DemoChildEntity;
import com.badou.project.moduledemo.service.IFun3DemoChildService;
import com.badou.project.moduledemo.service.IFun3DemoService;
import com.badou.project.moduledemo.web.form.Fun3DemoChildForm;
/**
 * 功能1示例编辑
 * <p>对应的请求路径是：${context}/moduledemo/children/Fun3/Fun3demoedit/方法名.do
 * <p>对应的请求默认页面是：${context}/WEB-INF/jsp/moduledemo/children/Fun3/Fun3demoedit_edit.jsp
 * @author xiangsf 2013-1-18
 *
 */
@RestController
public class Fun3DemoChildEditAction extends JsonEditTemplateAction<Fun3DemoChildEntity, Serializable, Fun3DemoChildForm> {

	@Autowired
	private IFun3DemoService fun3DemoService;
	@Autowired
	private IFun3DemoChildService fun3DemoChildService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:53:36
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun3DemoChildService(IFun3DemoChildService fun3DemoChildService) {
		this.fun3DemoChildService = fun3DemoChildService;
		super.setBaseService(fun3DemoChildService);
	}
	
	
}
