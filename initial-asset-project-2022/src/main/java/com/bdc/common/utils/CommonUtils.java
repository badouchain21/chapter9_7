package com.bdc.common.utils;

import java.math.BigDecimal;

/**
 *
 * @author lps
 *
 */
public class CommonUtils {
	/**
	 * BigDecimal转换为Int类型
	 * @param bigDecimal
	 * @return
	 */
	public static int parseInt(BigDecimal bigDecimal){
		int obj=Integer.parseInt(bigDecimal.toString());
		return obj;
	}
}
