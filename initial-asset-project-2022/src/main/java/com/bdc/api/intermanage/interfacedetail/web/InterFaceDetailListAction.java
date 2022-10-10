package com.bdc.api.intermanage.interfacedetail.web;

import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import com.bdc.common.utils.BdLoginUtil;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.CommonConsts;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;
import com.badou.tools.common.cfg.PropertyUtils;
import com.badou.tools.common.cfg.system.DefaultPropertiesLoader;
import com.badou.tools.common.util.ParamUtils;

@RestController
@Api(value="interfactDetail")
@RequestMapping(value="/api/intermanage/interfacedetail/interfacedetaillist")
public class InterFaceDetailListAction extends BaseCommonListAction {

	@Autowired
	private IInterFaceDetailService interFaceDetailService;

	@Autowired
	public void setInterFaceDetailService(IInterFaceDetailService interFaceDetailService){
		this.interFaceDetailService = interFaceDetailService;
	}

	public void detailList() throws Exception {
		pageSize = PropertyUtils.getIntProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_PAGESIZE,
				DefaultPropertiesLoader.CONFIG_FILE);
		pageOptions = PropertyUtils.getProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_OPTIONS_PAGESIZE,
				DefaultPropertiesLoader.CONFIG_FILE);
		//request.setAttribute("module", getModuleForList(true,CommonConsts.STRATEGY_SINGEL));
		request.setAttribute("strategy", CommonConsts.STRATEGY_SINGEL);
		request.getRequestDispatcher("/WEB-INF/jsp/template/interfacedetail/interfacedetaillist_list.jsp").forward(request,response);
	}

	@RequestMapping(value="/init")
	@BaseMdJsonStack
	public JsonReturnBean init() throws Exception {
		returnBean = new JsonReturnBean();
		try{
		    String id = ParamUtils.getParameter(request, "id");
			interFaceDetailService.initInterFaceDetail(id);
			returnBean.setHasOk(true);
			returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
		}catch(Exception e){
			logger.error("error:",e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
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
