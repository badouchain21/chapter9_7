package com.badou.project.moduledemo.web.fun2;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.used.json.BaseJsonDeleteStrutsAction;
import com.badou.project.moduledemo.model.Fun2DemoEntity;
import com.badou.project.moduledemo.service.IFun2DemoService;
/**
 * 功能1示例删除事件
 * <p>对应的请求路径是：${context}/moduledemo/fun2/fun2demodelete/方法名.do
 * @author xiangsf 2013-1-18
 *
 */
@RestController
public class Fun2DemoDeleteAction extends BaseJsonDeleteStrutsAction<Fun2DemoEntity, Serializable> {

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
