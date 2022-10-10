package com.badou.project.moduledemo.web.fun3.children;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.used.json.BaseJsonDeleteStrutsAction;
import com.badou.project.moduledemo.model.Fun3DemoChildEntity;
import com.badou.project.moduledemo.service.IFun3DemoChildService;
import com.badou.project.moduledemo.service.IFun3DemoService;
/**
 * 功能1示例删除事件
 * <p>对应的请求路径是：${context}/moduledemo/Fun3/children/Fun3demodelete/方法名.do
 * @author xiangsf 2013-1-18
 *
 */
@RestController
public class Fun3DemoChildDeleteAction extends BaseJsonDeleteStrutsAction<Fun3DemoChildEntity, Serializable> {

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
