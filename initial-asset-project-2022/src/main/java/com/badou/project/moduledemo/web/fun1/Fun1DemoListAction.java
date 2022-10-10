package com.badou.project.moduledemo.web.fun1;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.SortOrderEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.project.moduledemo.model.Fun1DemoEntity;
import com.badou.project.moduledemo.service.IFun1DemoChildService;
import com.badou.project.moduledemo.service.IFun1DemoService;
import com.badou.project.moduledemo.web.form.Fun1DemoForm;
import com.badou.tools.common.util.DateUtils;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.param.ParamDateUtils;
/**
 * 功能1示例查询事件
 * <p>对应的请求路径是：${context}/moduledemo/fun1/fun1demolist/方法名.do
 * <p>对应的请求默认页面是：${context}/WEB-INF/jsp/moduledemo/fun1/fun1demolist_list.jsp
 * @author xiangsf 2013-1-18
 *
 */
@RestController
@RequestMapping("/project/moduledemo/fun1demolist")
public class Fun1DemoListAction extends JsonListTemplateAction<Fun1DemoEntity, Serializable, Fun1DemoForm> {

	/**
	 * http://blog.csdn.net/heyutao007/article/details/5981555
	 * http://blog.csdn.net/guo_love_peng/article/details/6853728
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
	public void setFun1DemoService(IFun1DemoService fun1DemoService) {
		this.fun1DemoService = fun1DemoService;
		super.setBaseService(fun1DemoService);
	}
	@Override
	protected void exeBeforeList() throws Exception {
		HttpServletRequest req = request ;
		String code = ParamUtils.getParameter(req, "code");
		String name = ParamUtils.getParameter(req, "name");
		String creator = ParamUtils.getParameter(req, "creator");
		Date createTimeStart = ParamDateUtils.getParameter(req, "createTimeStart",DateUtils.getDateFmtCnYmd());
		Date createTimeEnd = ParamDateUtils.getParameter(req, "createTimeEnd",DateUtils.getDateFmtCnYmd());
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("code",code,null,QueryOperSymbolEnum.rlike))
			.addParam(new QueryHibernatePlaceholderParam("name",name,null,QueryOperSymbolEnum.rlike))
			.addParam(new QueryHibernatePlaceholderParam("creator",creator,null,QueryOperSymbolEnum.eq))
			.addParam(new QueryHibernatePlaceholderParam("createTime",createTimeStart,null,QueryOperSymbolEnum.ge))
			.addParam(new QueryHibernatePlaceholderParam("createTime",createTimeEnd,null,QueryOperSymbolEnum.le))
			.addOrder(new QueryOrderby(1,"priority",SortOrderEnum.ase))
			.addOrder(new QueryOrderby(2,"updateTime",SortOrderEnum.desc));
		req.setAttribute(Request_Criterion, queryCriterion);
	}
	
}
