package com.badou.project.moduledemo.web.fun2;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.SortOrderEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.brms.dictionary.service.IDictionaryService;
import com.badou.project.moduledemo.model.Fun2DemoEntity;
import com.badou.project.moduledemo.service.IFun2DemoService;
import com.badou.project.moduledemo.web.form.Fun2DemoForm;
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
@RestController
@RequestMapping("/project/moduledemo/fun2demolist")
public class Fun2DemoListAction extends JsonListTemplateAction<Fun2DemoEntity, Serializable, Fun2DemoForm> {
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
	
	/**
	 * 参数名称, 分页请求的每页数
	 */
	public static final String PAGE_REQ_PARAMS_PERPAGESIZE = "perPageSize";
	/**
	 * 参数名称，分页请求的第几页
	 */
	public static final String PAGE_REQ_PARAMS_PAGENO = "pageNo";

	/**
	 * 参数名称，页排序列名
	 */
	public static final String PAGE_REQ_PARAMS_SORTNAME = "sortName";
	/**
	 * 参数名称，页排序方向
	 */
	public static final String PAGE_REQ_PARAMS_SORTORDER = "sortOrder";
	@Override
	protected void exeBeforeList() throws Exception {
		HttpServletRequest req = request ;
		String fieldName1 = ParamUtils.getParameter(req, "fieldName1");
		String fieldName2=request.getParameter("fieldName2");
		String creator = ParamUtils.getParameter(req, "creator");
		Date createTimeStart = ParamDateUtils.getParameter(req, "createTimeStart",DateUtils.getDateFmtCnYmd());
		Date createTimeEnd = ParamDateUtils.getParameter(req, "createTimeEnd",DateUtils.getDateFmtCnYmd());
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("fieldName1",fieldName1,null,QueryOperSymbolEnum.rlike))
			.addParam(new QueryHibernatePlaceholderParam("fieldName2",fieldName2,null,QueryOperSymbolEnum.rlike))
			.addParam(new QueryHibernatePlaceholderParam("creator",creator,null,QueryOperSymbolEnum.eq))
			.addParam(new QueryHibernatePlaceholderParam("createTime",createTimeStart,null,QueryOperSymbolEnum.ge))
			.addParam(new QueryHibernatePlaceholderParam("createTime",createTimeEnd,null,QueryOperSymbolEnum.le))
			.addOrder(new QueryOrderby(2,"createTime",SortOrderEnum.desc));
		req.setAttribute(Request_Criterion, queryCriterion);
	}
	
	/**
	 * @auth chenjiabo
	 * @date 2019年7月2日下午5:59:28
	 * @todo 获取所有数据
	 * @return 数据结合对象（分页）
	 * @throws Exception if has error(包装好错误信息给前台提示用户)
	 */
	public LigeruiListVO<Fun2DemoForm> listJSONAll() throws Exception {
		Fun2DemoEntity entity = this.fun2DemoService.get("402881e557b703a60157b706e95d0001");
		LigeruiListVO<Fun2DemoForm> formvo = new LigeruiListVO<Fun2DemoForm>() ;
		Fun2DemoForm form = convert(entity);
		formvo.getRows().add(form);
		formvo.setTotal(1);
		return formvo;
	} 
	
	@Autowired
	private IDictionaryService dicService ;
	
}
