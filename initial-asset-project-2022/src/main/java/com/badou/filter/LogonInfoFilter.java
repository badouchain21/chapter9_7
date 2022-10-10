package com.badou.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.badou.bpms.engine.flow.FlowContext;
import com.badou.brms.auth.AuthConsts;
import com.badou.brms.system.CommonLoginUser;
import com.badou.brms.system.LoginAuthentication;
import com.badou.core.runtime.global.Globals;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.core.standard.base.message.UIRespose;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.StringUtils;

/**
 * @author chenjiabao
 * @date 2019年7月1日下午8:59:04
 * @todo 登录过滤器
 */
public class LogonInfoFilter implements Filter {

	protected FilterConfig filterConfig;
	protected Logger logger = Logger.getLogger(getClass());

	/**
	 * @auth chenjiabao
	 * @date 2019年7月1日下午5:42:16
	 * @todo 过滤页面校验
	 * @return true:成功 false:失败
	 */
	public boolean doExclude(ServletRequest request, ServletResponse response,
			FilterChain filterChain) {
		try {
			// String excludeUrls = getInitParameter("excludeUrls");
			String excludeUrls = "/v2/**;*/ueditor*;*swagger*;/detect;/detect/*;*.js;*.jpg;*.htm;*.html;*.gif;*.png;*.css;*.swf;/web/*jsp;*/login.jsp;*/login;*/login;*/logout;*/loginAccredit;*/403.jsp;*/500.jsp;*/404.jsp;/fileupload/attachupload/*;/validate/*/*;/exportcompany/exportcompanylist/search;/exportcompany/exportcompanyedit/detail;/validate/*/*;/security/ssl/token/*;*/change_browser.jsp;/cfg/basedefineedit/downloadImgCache;/jdbc/common/appmodule*/*;/system/security/userregister/*;/attach/action/attachsupport/downloadAttach;/system/security/logon/*;*/api/v1/**";
			String[] excludes = excludeUrls.split(";");
			HttpServletRequest httprequest = (HttpServletRequest) request;
			String path = httprequest.getServletPath();
			String regx = "";
			for (int i = 0; i < excludes.length; i++) {
				regx = excludes[i].replaceAll("\\.", "\\\\.");
				regx = regx.replaceAll("\\*", "\\.*");
				regx = regx.replaceAll("[\\s\t\n\r]", "");
				if (excludes[i].endsWith("/")){
					regx = regx + ".*";
				}
				if (path.matches(regx)){
					return true;
				}
			}
			if (path.matches("/getUndoForOA.do")){
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 获取fifter参数
	 * @author keman, 2017-12-25
	 * @param name
	 * @return 返回相关参数
	 */
	public String getInitParameter(String name) {
		return filterConfig.getInitParameter(name);
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
		filterConfig = fc;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		String loginUserName;
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		HttpServletResponse res = (HttpServletResponse) response;
		boolean isCheckLogin = true; // 默认要检测是否登陆
		if (req.getRequestURI().contains("barReport")){
			logger.info(req.getRequestURI());
		}
		if (doExclude(request, response, chain)) { // 过滤页面验证
			isCheckLogin = false;
			//*** 如果可以在session中找到登陆信息，则获取当前登录信息 **//*
			LogonCertificate certificate = (LogonCertificate) session
					.getAttribute(Globals.LOGON_BDUSER);
			if (certificate != null) {
				com.badou.brms.system.CommonLoginUser.addCurrentLoginID(certificate.getLoginId());
				LogonCertificateHolder.setLogonCertificate(certificate);
			} else {
				certificate = new LogonCertificate("sa", "U00001", "超级管理员", "",
						AuthConsts.SUPERADMIN_ROLE_CODE);
				LogonCertificateHolder.setLogonCertificate(certificate);
			}
		}
		try {
			if (isCheckLogin) {
				//**** 登陆验证开始 ****//
				String uuvId = ParamUtils.getDefaultStringValue(req,
						"UserName", null);
				if (!StringUtils.isEmpty(uuvId)) {
					loginUserName = LoginAuthentication.ssoAuthentication(req);
					// SSO验证失败，重定向URL
					if (StringUtils.isEmpty(loginUserName)|| !LoginAuthentication.logon(session, req, res,loginUserName)) {
						LoginAuthentication.redirectUrl(req, res);
						logger.info("sso验证不通过,需要重新验证");
						return;
					}
					//** 判断当前请求的URL是否已经进行过登录 **//*
				} else if (session.getAttribute(Globals.LOGON_BDUSER) == null) {
					logger.info("在session中找不到登陆信息......");
					//*** 如果在session中找不到登陆信息，则在cookie中找登录信息，找到则直接登录 **//*
					loginUserName = LoginAuthentication.getUserFromCookies(req);
					if (StringUtils.isEmpty(loginUserName)) {
						logger.info("在cookie中已经过期或找不到登陆信息......");
						LoginAuthentication.redirectUrl(req, res);
						return;
					} else if (!LoginAuthentication.logon(session, req, res,
							loginUserName)) {
						res.sendRedirect(req.getContextPath()
								+ "/Unauthorization.htm");
						logger.info("通过cookie中用户系统登录失败,用户不存在或帐户被冻结!");
						return;
					}
				} else {
					//*** 如果可以在session中找到登陆信息，则获取当前登录信息 **//*
					LogonCertificate certificate = (LogonCertificate) session.getAttribute(Globals.LOGON_BDUSER);
					CommonLoginUser.addCurrentLoginID(certificate.getLoginId());
					System.out.println(CommonLoginUser.getCurrentDefaultUserHolder().isSa());
					LogonCertificateHolder.setLogonCertificate(certificate);
				}
				//**** 登陆验证结束 ****/
			}
			chain.doFilter(request , response);
		} finally {
			UIRespose.destroy();
			FlowContext.destroy();
			LogonCertificateHolder.clear();
		}
	}

	@Override
	public void destroy() {
		com.badou.brms.system.CommonLoginUser.destroy();
	}
}
