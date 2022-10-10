package com.badou.validate.web;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.annotation.JSONField;
import com.badou.brms.base.support.BaseAction;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.StringUtils;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:27:39
 * @todo 检查验证码是否合法性相关类
 */
@RestController
public class CheckCodeAction extends BaseAction{
	
	protected JsonReturnBean returnBean;
	
	@JSONField(name="rbean")
	public JsonReturnBean getReturnBean() {
		return returnBean;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:12:16
	 * @todo 校验验证码是否正确
	 * @return JsonReturnBean 返回结果封装bean
	 */
	public JsonReturnBean checkCode(){
		try{
			returnBean = new JsonReturnBean();
			String sCodes = null;
			try {
				sCodes = request.getSession().getAttribute("codes").toString().toLowerCase();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			String codes = ParamUtils.getParameter(request,"codes").toLowerCase();
			if(StringUtils.isNotBlank(codes) && StringUtils.isNotBlank(sCodes) && codes.equals(sCodes)){
				//request.getSession().invalidate();
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
				returnBean.setMessage("验证成功！");
			}else{
				returnBean.setHasOk(false);
				returnBean.setTip(JsonReturnBean.TIP_FAIL);
				returnBean.setMessage("验证码有误，请刷新与重新输入");
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage("验证码有误，请刷新与输入");
		}
		return returnBean;
	}
}
