package com.badou.project.moduledemo.web.fun1.children;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.brms.base.support.struts.used.json.BaseJsonDeleteStrutsAction;
import com.badou.project.moduledemo.model.Fun1DemoChildEntity;
import com.badou.project.moduledemo.service.IFun1DemoChildService;
import com.badou.project.moduledemo.service.IFun1DemoService;
/**
 * 功能1示例删除事件
 * <p>对应的请求路径是：${context}/moduledemo/fun1/children/fun1demodelete/方法名.do
 * @author xiangsf 2013-1-18
 *
 */
@Controller
public class Fun1DemoChildDeleteAction extends BaseJsonDeleteStrutsAction<Fun1DemoChildEntity, Serializable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5580776894637809336L;
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
