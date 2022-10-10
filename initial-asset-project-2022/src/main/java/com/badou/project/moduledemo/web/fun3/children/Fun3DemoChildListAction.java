package com.badou.project.moduledemo.web.fun3.children;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.SortOrderEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.project.moduledemo.model.Fun3DemoChildEntity;
import com.badou.project.moduledemo.service.IFun3DemoChildService;
import com.badou.project.moduledemo.service.IFun3DemoService;
import com.badou.project.moduledemo.web.form.Fun3DemoChildForm;
import com.badou.tools.common.util.ParamUtils;
/**
 * 功能1示例查询事件
 * <p>对应的请求路径是：${context}/moduledemo/Fun3/Fun3demolist/方法名.do
 * <p>对应的请求默认页面是：${context}/WEB-INF/jsp/moduledemo/Fun3/Fun3demolist_list.jsp
 * @author xiangsf 2013-1-18
 *
 */
@RestController
public class Fun3DemoChildListAction extends JsonListTemplateAction<Fun3DemoChildEntity, Serializable, Fun3DemoChildForm> {

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
	@Override
	protected void exeBeforeList() throws Exception {
		HttpServletRequest req = request ;
		String menuId = ParamUtils.getParameter(req, "fun3DemoId");
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("MENU_ID",menuId,null,QueryOperSymbolEnum.eq))
			.addOrder(new QueryOrderby(1,"priority",SortOrderEnum.ase))
			.addOrder(new QueryOrderby(2,"createTime",SortOrderEnum.desc));
		req.setAttribute(Request_Criterion, queryCriterion);
		req.setAttribute(Request_Criterion, queryCriterion);
	}
	
}
