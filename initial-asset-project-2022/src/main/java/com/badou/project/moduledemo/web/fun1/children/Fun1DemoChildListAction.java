package com.badou.project.moduledemo.web.fun1.children;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.SortOrderEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.project.moduledemo.model.Fun1DemoChildEntity;
import com.badou.project.moduledemo.service.IFun1DemoChildService;
import com.badou.project.moduledemo.service.IFun1DemoService;
import com.badou.project.moduledemo.web.form.Fun1DemoChildForm;
import com.badou.tools.common.util.ParamUtils;
/**
 * 功能1示例查询事件
 * <p>对应的请求路径是：${context}/moduledemo/fun1/fun1demolist/方法名.do
 * <p>对应的请求默认页面是：${context}/WEB-INF/jsp/moduledemo/fun1/fun1demolist_list.jsp
 * @author xiangsf 2013-1-18
 *
 */
@Controller
public class Fun1DemoChildListAction extends JsonListTemplateAction<Fun1DemoChildEntity, Serializable, Fun1DemoChildForm> {

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
	@Override
	protected void exeBeforeList() throws Exception {
		HttpServletRequest req = request;
		String fun1DemoId = ParamUtils.getParameter(req, "fun1DemoId");
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("fun1Demo.id",fun1DemoId,null,QueryOperSymbolEnum.eq))
			.addOrder(new QueryOrderby(1,"priority",SortOrderEnum.ase))
			.addOrder(new QueryOrderby(2,"createTime",SortOrderEnum.desc));
		
		req.setAttribute(Request_Criterion, queryCriterion);
	}
	
}
