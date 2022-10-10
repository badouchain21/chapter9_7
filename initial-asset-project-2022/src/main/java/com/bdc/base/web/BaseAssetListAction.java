package com.bdc.base.web;

import com.badou.brms.base.support.page.PaginationTheadLocal;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.tools.common.Globals;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.StringUtils;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.common.utils.BdLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;

import java.util.List;

/**
 * 通用资产列表实现类
 * @author lps
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/bdc/baseAssetList")
public class BaseAssetListAction extends BaseCommonListAction{

	@Autowired
	private IBaseAssetService baseAssetService;
	@Autowired
	private IAssetDefinedService assetDefinedService;

	@Override
	protected void exeAfterList() throws Exception {
		//循环每一条单证数据
		List<Object> objects = baseAssetService.fillAssetDataWithJumpUrl(this.getPagelet().getDatas(), true,true);
		this.pagelet.setDatas(objects);
	}

	@Override
	protected void exeBeforeList(Boolean isExport) throws Exception {
		super.exeBeforeList(isExport);
		PaginationTheadLocal.getDefaultPagelet().getDatas().clear();
		if (!BdLoginUtil.isSa()){
			QueryCriterion parameter = (QueryCriterion) this.request.getAttribute(Request_Criterion);
			parameter.addParam(new QueryHibernatePlaceholderParam("CREATOR", LogonCertificateHolder.getLogonCertificate().getUserId(),null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
		}
	}

	/**
	 * 资产重新上链
	 * @return
	 */
	@PostMapping("/assetReUpload")
	public JsonReturnBean assetReUpload (){
		JsonReturnBean returnBean = new JsonReturnBean();
		String idStr = ParamUtils.getParameter(this.request, "ids");
		String assetCode = ParamUtils.getParameter(this.request, "assetCode");
		try {
			List<String> ids = StringUtils.stringToList(idStr, Globals.SEPARATOR_COMMA);
			baseAssetService.assetReUpload(ids,assetCode);
			returnBean.setHasOk(true);
			returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
			return returnBean;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_ERROR);
			return returnBean;
		}
	}

}
