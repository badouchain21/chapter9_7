package com.badou.project.common.util;

import java.util.Date;

import com.badou.tools.common.util.DateUtils;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:17:43
 * @todo 日期帮助类
 */
public class DateUtil {
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:07:22
	 * @todo 获得指定月份头一天，最后一天
	 * @param date
	 * @return 日期数组
	 */
	public static Date[] getMonth(Date date){
			
		Date[] dates = new Date[2];	
		dates[0] = DateUtils.getMonthFirstDate(date);
		dates[1] = DateUtils.getMonthEndDate(date);
		return dates;
		
		
	}
	
}
