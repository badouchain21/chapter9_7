package com.badou.project.moduledemo.web.fun6;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.SortOrderEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.project.moduledemo.model.Fun6DemoEntity;
import com.badou.project.moduledemo.service.IFun6DemoService;
import com.badou.project.moduledemo.web.form.Fun6DemoForm;
import com.badou.tools.common.util.ParamUtils;
/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:28:06
 * @todo demo6列表接口类
 */
@RestController
public class Fun6DemoListAction extends JsonListTemplateAction<Fun6DemoEntity, Serializable, Fun6DemoForm> {

	@Autowired 
	private IFun6DemoService customerService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setCustomerService(IFun6DemoService customerService) {
		this.customerService = customerService;
		super.baseService = customerService;
	}
	
	
	/**
	 * 
	 * 描述：跳转到全部客户页面并分页
	 * 
	 * @return
	 * @throws Exception 
	 *
	 */
	/*@Action(interceptorRefs = @InterceptorRef("baseJsonStack"))
	@DispatcherResult
	public String listAllPage() throws Exception {
		super.list();
		return "listAllPage";
	}*/
	
	/**
	 * 执行list前方法，可以添加查询条件
	 */
	@Override
	protected void exeBeforeList() throws Exception {
		QueryCriterion queryCriterion = new QueryCriterion();
		//获取参数
		String name = ParamUtils.getParameter(request, "name");
		String phone = ParamUtils.getParameter(request, "phone");
		
		//添加查询条件
		QueryCriterion addParam = queryCriterion
				.addOrder(new QueryOrderby(1,"createTime",SortOrderEnum.desc))
				.addParam(new QueryHibernatePlaceholderParam("name",name,null,QueryOperSymbolEnum.like))
				.addParam(new QueryHibernatePlaceholderParam("phone",phone,null,QueryOperSymbolEnum.like));
		
		String isPrivate =  ParamUtils.getParameter(request, "isPrivate");
		//判断私有标志是否存在，存在则说明是查询我的客户，否则是查询全部客户
		if(StringUtils.isNotBlank(isPrivate)) {
			//从线程中获取当前用户名
			String creatorName = LogonCertificateHolder.getLogonCertificate().getUserName();
			queryCriterion.addParam(new QueryHibernatePlaceholderParam("creatorName",creatorName,null,QueryOperSymbolEnum.eq));
		}
		request.setAttribute(Request_Criterion, queryCriterion);
	}
	
	
}
