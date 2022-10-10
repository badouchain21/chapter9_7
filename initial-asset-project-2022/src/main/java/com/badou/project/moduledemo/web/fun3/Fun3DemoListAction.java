package com.badou.project.moduledemo.web.fun3;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.brms.base.support.struts.struts2.JsonListTreeTemplateAction;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.SortOrderEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.project.moduledemo.service.IFun3DemoService;
import com.badou.project.moduledemo.web.form.Fun3DemoForm;
import com.badou.tools.common.Globals;
import com.badou.tools.common.util.DateUtils;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.param.ParamDateUtils;
/**
 * 功能1示例查询事件
 * <p>对应的请求路径是：${context}/moduledemo/Fun2/Fun2demolist/方法名.do
 * <p>对应的请求默认页面是：${context}/WEB-INF/jsp/moduledemo/Fun2/Fun2demolist_list.jsp
 * @author xiangsf 2013-1-18
 *
 */
@Controller
public class Fun3DemoListAction extends JsonListTreeTemplateAction<Fun3DemoEntity, Serializable, Fun3DemoForm> {

	@Autowired
	private IFun3DemoService fun3DemoService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun3DemoService(IFun3DemoService fun3DemoService) {
		this.fun3DemoService = fun3DemoService;
		super.setBaseService(fun3DemoService);
	}
	
	

	@Override
	protected void exeBeforeList() throws Exception {
		HttpServletRequest req = request ;
		String pid = ParamUtils.getParameter(req, "pid", Globals.RESOURCE_ROOT_ID);
		String code = ParamUtils.getParameter(req, "code");
		String name = ParamUtils.getParameter(req, "name");
		String creator = ParamUtils.getParameter(req, "creator");
		Date createTimeStart = ParamDateUtils.getParameter(req, "createTimeStart",DateUtils.getDateFmtCnYmd());
		Date createTimeEnd = ParamDateUtils.getParameter(req, "createTimeEnd",DateUtils.getDateFmtCnYmd());
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("parent.id",pid, null, QueryOperSymbolEnum.eq))
			.addParam(new QueryHibernatePlaceholderParam("code",code,null,QueryOperSymbolEnum.rlike))
			.addParam(new QueryHibernatePlaceholderParam("name",name,null,QueryOperSymbolEnum.rlike))
			.addParam(new QueryHibernatePlaceholderParam("creator",creator,null,QueryOperSymbolEnum.eq))
			.addParam(new QueryHibernatePlaceholderParam("createTime",createTimeStart,null,QueryOperSymbolEnum.ge))
			.addParam(new QueryHibernatePlaceholderParam("createTime",createTimeEnd,null,QueryOperSymbolEnum.le))
			.addOrder(new QueryOrderby(2,"createTime",SortOrderEnum.desc));
		req.setAttribute(Request_Criterion, queryCriterion);
	}
	
	/*@Action
	@DispatcherResult
	public String frame() throws Exception {
		return "frame";
	}*/
}
