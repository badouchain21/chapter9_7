 package com.bdc.api.template.web;

import com.bdc.api.template.service.ITemplateService;
import com.bdc.api.template.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.BaseAction;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.tools.common.util.ParamUtils;

 /**
  * 描述：接口文档
  * @author linxiaoqing
  * @date 2019年8月15日
  */
 @RestController
 @RequestMapping(value="/api/template/template/")
 public class TemplateAction extends BaseAction {

     @Autowired
     private ITemplateService templateService;

     @RequestMapping(value="/show")
     @BaseMdJsonStack
     public JsonReturnBean show() throws Exception {
         returnBean = new JsonReturnBean();
         try{
             String id = ParamUtils.getParameter(request, "id");
             TemplateVO data = templateService.getAssetInterfaceDetail(id);
             returnBean.setBean(data);
             returnBean.setHasOk(true);
             returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
         }catch(Exception e){
             e.printStackTrace();
             logger.error("error:",e);
             returnBean.setHasOk(false);
             returnBean.setTip(JsonReturnBean.TIP_FAIL);
             returnBean.setMessage(e.getMessage());
         }
         return returnBean;
     }

 }
