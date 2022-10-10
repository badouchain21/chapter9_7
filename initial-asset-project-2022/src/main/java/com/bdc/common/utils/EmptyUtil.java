package com.bdc.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 通用判空工具类
 *
 * @author qiudao 2018年7月9日
 */
public class EmptyUtil {

	private EmptyUtil() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * 判断字符串是否为空，长度为0被认为是空字符串.
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断字符串是否为空，字符串前后空白被截断，截断后长度为0被认为是空字符串
	 *
	 * @param str
	 * @param isTrimed
	 *            是否去掉前后空格
	 * @return
	 */
	public static boolean isEmpty(String str, boolean isTrimed) {
		if (isTrimed) {
			return str == null || str.length() == 0 || str.trim().length() == 0;
		} else {
			return str == null || str.length() == 0;
		}
	}

	/**
	 * 判断列表是否为空，列表没有元素也被认为是空
	 *
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断数组是否为空
	 *
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		return null == array || array.length == 0;
	}

	/**
	 * 判断对象是否为空
	 *
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
	     // 先判断一下是否是null
        if (null == obj) {
            return true;
        }
        // 数组对象
        if (obj.getClass().isArray()) {
            return arrayIsEmpty(obj);
            // 集合
        } else if (obj instanceof Collection) {
            Collection<?> collection = (Collection<?>) obj;
            return collection.isEmpty();
            // String
        } else if (obj instanceof String) {
            String str = (String) obj;
            return str.length() == 0;
            // Map
        } else if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map.isEmpty();
        }
        return false;
	}

	/**
	 * 判断map是否为空
	 *
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 判断字符串是否不为空，长度为0被认为是空字符串.
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断字符串是否不为空，字符串前后空白被截断，截断后长度为0被认为是空字符串
	 *
	 * @param str
	 * @param isTrimed
	 *            是否去掉前后空格
	 * @return
	 */
	public static boolean isNotEmpty(String str, boolean isTrimed) {
		return !isEmpty(str, isTrimed);
	}

	/**
	 * 判断列表是否不为空，列表没有元素也被认为是空
	 *
	 * @param collection
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	/**
	 * 判断数组是否不为空
	 *
	 * @param array
	 * @return
	 */
	public static boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}

	/**
	 * 判断对象是否不为空
	 *
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 判断map是否不为空
	 *
	 * @param map
	 * @return
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
	 /**
     * 数组类型对象判空
     *
     * @param obj
     * @return
     */
    private static boolean arrayIsEmpty(Object obj) {
        if (obj instanceof Object[]) {
            Object[] array = (Object[]) obj;
            return array.length == 0;
        } else if (obj instanceof boolean[]) {
            boolean[] array = (boolean[]) obj;
            return array.length == 0;
        } else if (obj instanceof byte[]) {
            byte[] array = (byte[]) obj;
            return array.length == 0;
        } else if (obj instanceof char[]) {
            char[] array = (char[]) obj;
            return array.length == 0;
        } else if (obj instanceof double[]) {
            double[] array = (double[]) obj;
            return array.length == 0;
        } else if (obj instanceof float[]) {
            float[] array = (float[]) obj;
            return array.length == 0;
        } else if (obj instanceof int[]) {
            int[] array = (int[]) obj;
            return array.length == 0;
        } else if (obj instanceof long[]) {
            long[] array = (long[]) obj;
            return array.length == 0;
        } else if (obj instanceof short[]) {
            short[] array = (short[]) obj;
            return array.length == 0;
        }
        return false;
    }

}
