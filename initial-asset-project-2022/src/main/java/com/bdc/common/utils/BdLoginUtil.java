package com.bdc.common.utils;


import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 八斗登录工具类
 * @author zzl
 *
 */
public class BdLoginUtil {
	
	private final static String SA_USER_ID = "U00001";
	
	/**
	 * 判断当前登录用户是不是超管
	 */
	public static boolean isSa() {
		LogonCertificate logonCertificate = LogonCertificateHolder.getLogonCertificate();
		return Objects.isNull(logonCertificate) ? false : Objects.equals(logonCertificate.getUserId(),SA_USER_ID);
	}

	public static boolean isSa(String userId) {
		return Objects.equals(userId,SA_USER_ID);
	}

	public static void setLogin (String userId){
		LogonCertificateHolder.setLogonCertificate(new LogonCertificate("",userId,""));
	}

}
