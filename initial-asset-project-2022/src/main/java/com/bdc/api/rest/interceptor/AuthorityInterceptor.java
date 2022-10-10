package com.bdc.api.rest.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.badou.brms.util.InstanceFactory;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;

import com.badou.core.standard.enums.SystemBoolean;
import com.badou.security.ssl.token.pojo.Token;
import com.badou.security.ssl.token.service.ITokenService;
import com.badou.tools.common.util.ParamUtils;
import com.bdc.api.intermanage.interfacelogger.model.InterFaceLoggerEntity;
import com.bdc.api.intermanage.interfacelogger.service.IInterFaceLoggerService;
import com.bdc.api.rest.ReturnCodeEnum;
import com.bdc.api.rest.container.TokenContainer;
import com.bdc.api.rest.service.IBaseRestService;
import com.bdc.api.rest.vo.ApiReturnVO;
import com.bdc.common.utils.BdLoginUtil;
import com.bdc.common.utils.SpringContextUtil;
import com.bdc.rabbitmq.config.RabbitMqConfig;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:10:59
 * @todo 认证拦截器
 */
public class AuthorityInterceptor  extends HandlerInterceptorAdapter {

	//日志
	protected Logger logger = Logger.getLogger(AuthorityInterceptor.class);
	/**
	 * 返回对象
	 */
	protected ApiReturnVO returnVO;
	@Autowired
	private IBaseRestService baseRestService;
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private IInterFaceLoggerService interFaceLoggerService;

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
    	System.out.println("START");
        ITokenService tokenService = (ITokenService) SpringContextUtil.getBean("tokenServiceImpl");
        IBaseRestService baseRestService = (IBaseRestService)SpringContextUtil.getBean("baseRestService");
    	returnVO = new ApiReturnVO();
    	response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");//这句话是解决乱码的
    	String token = ParamUtils.getParameter(request, "access_token");
    	/*com.alibaba.fastjson.JSONObject userInfo = TokenContainer.getInstance().get(token);
        Token tokenEntity = (Token) userInfo.get("tokenEntity");*/
		Token tokenEntity = tokenService.getTokenEntityByToken(token);
		if(tokenEntity==null)
        	return false;
      	Map<String,Object> result = baseRestService.authPermission(tokenEntity, request);
    	boolean isSuccess = (boolean) result.get("isSuccess");
    	String message =  (String) result.get("message");
    	if(!isSuccess)
    	{
    		returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+message);
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
			response.getWriter().write(JSONObject.fromObject(returnVO).toString());
    		return false;
    	}
    	request.setAttribute("logId", result.get("logId"));
    	return true;
    }
	public ApiReturnVO getReturnVO() {
		return returnVO;
	}
	public void setReturnVO(ApiReturnVO returnVO) {
		this.returnVO = returnVO;
	}


	/**
	 * 切割ip地址
	 * @param ipaddress
	 * @return ip地址集合
	 */
	private List<String> getIpsFromStr(String ipaddress) {
		if(ipaddress==null){
			return null;
		}
		String[] strs = ipaddress.split("，");
		List<String> ips = new ArrayList<String>();
		for(int i=0; i<strs.length; i++){
			ips.add(strs[i].trim());
		}
		return ips;
	}
	 /**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:46:28
	 * @todo 获取ip地址
	 * @param request
	 * @return IP地址
	 */
	public static String getIpAddress(HttpServletRequest request) {
	        String ip = request.getHeader("x-forwarded-for");
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("Proxy-Client-IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("WL-Proxy-Client-IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("HTTP_CLIENT_IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getRemoteAddr();
	        }
	        return ip;
	    }

	/**
	 * 保存日志的返回结果和请求的结束时间
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		Object responseBody = request.getAttribute("responseBody");
		com.alibaba.fastjson.JSONObject resBodyJOSN = com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSONObject.toJSON(responseBody).toString());
		JSONArray jArr = new JSONArray();
		for(String key: resBodyJOSN.keySet()) {
            JSONObject jObj = new JSONObject();
            jObj.put("name", key);
            jObj.put("value", String.valueOf(resBodyJOSN.get(key)));
            jArr.add(jObj);
		}
		InterFaceLoggerEntity loggerEntity = (InterFaceLoggerEntity) request.getAttribute("loggerEntity");
		loggerEntity.setIsSuccess(!ApiReturnVO.SUCCESS.equals(resBodyJOSN.get("returnCode"))? SystemBoolean.YES.getKey():SystemBoolean.NO.getKey());
     	if(!loggerEntity.getIsSuccess().equals(SystemBoolean.NO.getKey()) && resBodyJOSN.get("returnMsg")!=null){
			loggerEntity.setFailreason(resBodyJOSN.get("returnMsg").toString());
		}
		if(!request.getMethod().equals(RequestMethod.GET.name())){
			loggerEntity.setJsonResult(jArr.toJSONString());
		}else{
			loggerEntity.setJsonResult(resBodyJOSN.get("returnMsg")==null?(loggerEntity.getIsSuccess().equals(SystemBoolean.YES.getKey())?ApiReturnVO.SUCCESS:ApiReturnVO.FAIL):resBodyJOSN.get("returnMsg").toString());
		}
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		loggerEntity.setEndTime(Timestamp.valueOf(time));
		loggerEntity.setLateTime(new Date().getTime() - (Long)request.getAttribute("requestDate")+"ms");
		com.alibaba.fastjson.JSONObject userInfo = TokenContainer.getInstance().get(loggerEntity.getToken());
		String userId = userInfo.getString("userId");
		BdLoginUtil.setLogin(userId);
//		loggerService.update(loggerEntity);
		//使用rabbitTemplate发送消息

		RabbitTemplate rabbitTemplate = InstanceFactory.getInstance(RabbitTemplate.class);
		String logger = JSON.toJSONString(loggerEntity);
		rabbitTemplate.convertAndSend(logger);
		LogonCertificateHolder.clear();
	}

}
