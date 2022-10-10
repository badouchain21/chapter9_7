package com.bdc.api.rest.interceptor;

import com.badou.brms.base.support.struts.JsonReturnBean;

import com.badou.tools.common.util.ParamUtils;
import com.bdc.api.rest.ReturnCodeEnum;
import com.bdc.api.rest.service.IBaseRestService;
import com.bdc.api.rest.vo.ApiReturnVO;
import com.bdc.common.utils.SpringContextUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:11:15
 * @todo token拦截器
 */
public class TokenInterceptor extends HandlerInterceptorAdapter{

	protected static Logger logger = Logger.getLogger(TokenInterceptor.class);

	/**
	 * 返回对象
	 */
	protected ApiReturnVO returnVO;
	 /**
     * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
     * SpringMVC中的Interceptor拦截器是链式的，可以同时存在多个Interceptor，
     * 然后SpringMVC会根据声明的前后顺序一个接一个的执行，
     * 而且所有的Interceptor中的preHandle方法都会在Controller方法调用之前调用。
     * SpringMVC的这种Interceptor链式结构也是可以进行中断的，
     * 这种中断方式是令preHandle的返回值为false，当preHandle的返回值为false的时候整个请求就结束了。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String token = ParamUtils.getParameter(request, "access_token");
    	returnVO = new ApiReturnVO();
    	response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");//这句话是解决乱码的
    	if(StringUtils.isBlank(token))
    	{
    		returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+"access_token不能为空");
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
			response.getWriter().write(JSONObject.fromObject(returnVO).toString());
    		return false;
    	}
    	IBaseRestService baseRestService = (IBaseRestService) SpringContextUtil.getBean("baseRestService");
        JsonReturnBean returnBean = baseRestService.validToken(token);
    	if(!returnBean.isHasOk())
    	{
    		returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+returnBean.getMessage());
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
    		response.getWriter().write(JSONObject.fromObject(returnVO).toString());
    		return false;
    	}
        return true;
    }
	public ApiReturnVO getReturnVO() {
		return returnVO;
	}
	public void setReturnVO(ApiReturnVO returnVO) {
		this.returnVO = returnVO;
	}



}
