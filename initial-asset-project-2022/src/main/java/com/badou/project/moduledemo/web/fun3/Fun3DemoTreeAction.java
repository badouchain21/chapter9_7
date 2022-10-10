package com.badou.project.moduledemo.web.fun3;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.badou.brms.base.support.struts.BaseStrutsEntityAction;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.project.moduledemo.service.IFun3DemoService;
import com.badou.project.moduledemo.web.form.Fun3DemoTreeForm;
import com.badou.tools.common.Globals;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:22:53
 * @todo demo3树结构相关接口类
 */
public class Fun3DemoTreeAction extends BaseStrutsEntityAction<Fun3DemoEntity, Serializable> {

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
	private List<Fun3DemoTreeForm> treeForms;
	
	public List<Fun3DemoTreeForm> getTreeForms() {
		return treeForms;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:54:48
	 * @todo 获取树结构数据集合
	 * @return 数据集合对象
	 * @throws Exception if has error(直接往外抛)
	 */
	public List<Fun3DemoTreeForm> tree() throws Exception {
		treeForms = fun3DemoService.getTreeByParentId(Globals.RESOURCE_ROOT_ID);
		return treeForms;
	}
}
