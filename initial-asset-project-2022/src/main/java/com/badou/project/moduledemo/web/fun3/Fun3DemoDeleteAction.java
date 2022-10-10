package com.badou.project.moduledemo.web.fun3;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.used.json.BaseJsonDeleteStrutsAction;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.project.moduledemo.service.IFun3DemoService;
/**
 * 功能3示例删除事件
 * <p>对应的请求路径是：${context}/moduledemo/fun3/fun3demodelete/方法名.do
 * @author xiangsf 2013-3-18
 *
 */
@RestController
public class Fun3DemoDeleteAction extends BaseJsonDeleteStrutsAction<Fun3DemoEntity, Serializable> {

	@Autowired
	private IFun3DemoService fun3DemoService;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:53:36
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun3DemoService(IFun3DemoService fun3DemoService) {
		this.fun3DemoService = fun3DemoService;
		super.setBaseService(fun3DemoService);
	}
	
	
}
