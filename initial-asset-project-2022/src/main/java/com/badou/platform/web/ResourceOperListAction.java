package com.badou.platform.web;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.SortOrderEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.service.IResourceOperService;
import com.badou.platform.web.form.ResourceOperForm;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.param.ParamIntegerUtils;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:10:26
 * @todo 资源操作列表接口类
 */
public class ResourceOperListAction extends JsonListTemplateAction<ResourceOperEntity, Serializable, ResourceOperForm> {
	
	@SuppressWarnings("unused")
	private IResourceOperService resourceOperService ;

	@Autowired
	private IPlatformResourceService platformResourceService;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:02:11
	 * @todo 设置默认service对象
	 * @param resourceOperService
	 */
	@Autowired
	public void setResourceOperService(IResourceOperService resourceOperService) {
		this.resourceOperService = resourceOperService;
		setBaseService(resourceOperService);
	}
	
	@Override
	protected void exeBeforeList() {
		QueryCriterion queryCriterion = new QueryCriterion();

		Integer type = ParamIntegerUtils.getParameter(request, "type");
		if (type != null) {
			queryCriterion.addParam(new QueryHibernatePlaceholderParam("type", type, null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
		}

		String resourceId = ParamUtils.getParameter(request, "resourceId");
		if (StringUtils.isNotBlank(resourceId) && type != null) {
			PlatformResourceEntity pr = platformResourceService.findByResourceId(resourceId, type);
			if (pr != null) {
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("platformResourceId", pr.getId(), null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
			}
		}

		queryCriterion.addOrder(new QueryOrderby(0, "createTime", SortOrderEnum.desc));
		request.setAttribute(Request_Criterion, queryCriterion);
	}
	
	
	/*@Action(interceptorRefs = @InterceptorRef("baseJsonStack"))
	@DispatcherResult(name="resourceOperForModule",location="/WEB-INF/jsp/platform/moduleresourceoper_list.jsp")
	public String resourceOperForModule() {
		pageSize = PropertyUtils.getIntProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_PAGESIZE,
				DefaultPropertiesLoader.CONFIG_FILE);
		pageOptions = PropertyUtils.getProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_OPTIONS_PAGESIZE, 
				DefaultPropertiesLoader.CONFIG_FILE);

		// 如果资源id不为空，列表只加载这个资源的同步记录
		String resourceId = ParamUtils.getParameter(request, "resourceId");
		request.setAttribute("resourceId", resourceId);

		return "resourceOperForModule";
	}*/
}
