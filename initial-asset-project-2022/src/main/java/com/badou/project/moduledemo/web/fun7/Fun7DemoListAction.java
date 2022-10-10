package com.badou.project.moduledemo.web.fun7;

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
import com.badou.project.moduledemo.model.Fun7DemoEntity;
import com.badou.project.moduledemo.service.IFun7DemoService;
import com.badou.project.moduledemo.web.form.Fun7DemoForm;
import com.badou.tools.common.util.ParamUtils;
/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:27:00
 * @todo demo7列表接口类
 */
@RestController
public class Fun7DemoListAction extends JsonListTemplateAction<Fun7DemoEntity, Serializable, Fun7DemoForm> {

	/**
	 * 
	 */
	@Autowired
	private IFun7DemoService fun7DemoService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun7DemoService(IFun7DemoService fun7DemoService) {
		this.fun7DemoService = fun7DemoService;
		super.setBaseService(fun7DemoService);
	}
	private static final long serialVersionUID = -2448200243482150279L;
		
		@Override
		protected void exeBeforeList() throws Exception {
			HttpServletRequest req = request ;
			String name = ParamUtils.getParameter(req, "name");
			String goodsId = ParamUtils.getParameter(req, "goodsId");
			QueryCriterion queryCriterion = new QueryCriterion();
			queryCriterion
			.addParam(new QueryHibernatePlaceholderParam("name",name,null,QueryOperSymbolEnum.like))
			.addParam(new QueryHibernatePlaceholderParam("id",goodsId,null,QueryOperSymbolEnum.eq))
			.addOrder(new QueryOrderby(1,"name",SortOrderEnum.ase));
			req.setAttribute(Request_Criterion, queryCriterion);
		}
		
		/*@Action(interceptorRefs = @InterceptorRef("baseJsonStack"))
		@DispatcherResult
		public String details() {
			String goodsId = request.getParameter("goodsId");
			request.setAttribute("goodsId", goodsId);
			return "details";
		}*/
		
		
}
