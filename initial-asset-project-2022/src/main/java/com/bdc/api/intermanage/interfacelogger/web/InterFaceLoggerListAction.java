package com.bdc.api.intermanage.interfacelogger.web;

import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.bdc.common.utils.BdLoginUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.badou.designer.jdbc.CommonConsts;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;
import com.badou.tools.common.cfg.PropertyUtils;
import com.badou.tools.common.cfg.system.DefaultPropertiesLoader;

@Controller
@RequestMapping("/api/intermanager/interfacelogger/interfaceloggerlist")
public class InterFaceLoggerListAction extends BaseCommonListAction {

	@RequestMapping
	@BaseMdJsonStack
	public void loggerList() throws Exception {
		pageSize = PropertyUtils.getIntProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_PAGESIZE,
				DefaultPropertiesLoader.CONFIG_FILE);
		pageOptions = PropertyUtils.getProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_OPTIONS_PAGESIZE,
				DefaultPropertiesLoader.CONFIG_FILE);
		//request.setAttribute("module", getModuleForList(true,CommonConsts.STRATEGY_SINGEL));
		request.setAttribute("strategy", CommonConsts.STRATEGY_SINGEL);
		request.getRequestDispatcher("/WEB-INF/jsp/template/interfacelogger/interfaceloggerlist_list.jsp").forward(request,response);
	}

	@Override
	protected void exeBeforeList(Boolean isExport) throws Exception {
		super.exeBeforeList(isExport);
		if (!BdLoginUtil.isSa()){
			QueryCriterion parameter = (QueryCriterion) this.request.getAttribute(Request_Criterion);
			parameter.addParam(new QueryHibernatePlaceholderParam("CREATOR", LogonCertificateHolder.getLogonCertificate().getUserId(),null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
		}
	}

}
