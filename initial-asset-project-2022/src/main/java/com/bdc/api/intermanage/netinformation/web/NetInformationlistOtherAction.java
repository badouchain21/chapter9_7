package com.bdc.api.intermanage.netinformation.web;


import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.core.annotation.PageMdJsonStack;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;

@RestController
@Api(value="netinformationlistother")
public class NetInformationlistOtherAction extends BaseCommonListAction {

	/**
	 * @Field @serialVersionUID : TODO(自定义继承JsonEditTemplateAction))
	 */

	protected String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}


	@Autowired
	private INetInformationService informationService;



    @RequestMapping(value="listByInterfaceId")
    @BaseMdJsonStack
    @PageMdJsonStack
	public LigeruiListVO<JSONObject> listByInterfaceId(){
        try {
            String interfaceId = request.getParameter("interfaceId");
            Pagelet pagelet = informationService.listByInterfaceId(interfaceId);
            listvo = new LigeruiListVO<JSONObject>();
            listvo.setTotal(pagelet.getTotalCount());
            listvo.setRows(pagelet.getDatas());
        } catch (Exception e) {
            e.printStackTrace();
			logger.error("error:",e);
        }
		return listvo;
	}

    @RequestMapping
    @BaseMdJsonStack
	public String updateCallInterface() throws Exception {
		returnBean = new JsonReturnBean();
		try {
			String msg = null;
			if (ids != null) {
				msg = informationService.updateCallInterface(ids.toString());
			}
			if(StringUtils.isNotBlank(msg)){
				returnBean.setHasOk(false);
				returnBean.setTip(JsonReturnBean.TIP_FAIL);
				returnBean.setMessage(msg);
			}else{
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("error:",e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return "updateCallInterface";
	}

}
