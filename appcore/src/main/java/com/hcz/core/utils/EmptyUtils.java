package com.hcz.core.utils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;

/**
 * 校验是否为空工具类
 *
 */
public class EmptyUtils {

    /**
     * 校验数据是否为空
     * 
     * @param sourceArray
     * @return
     */
    public static <V> boolean isEmpty(V[] sourceArray) {
	return (sourceArray == null || sourceArray.length == 0);
    }

    /**
     * 校验List是否为空
     * 
     * @param sourceList
     * @return
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
	return (sourceList == null || sourceList.size() == 0);
    }

    /**
     * 校验Map是否为空
     * 
     * @param sourceMap
     * @return
     */
    public static <K, V> boolean isEmpty(Map<K, V> sourceMap) {
	return (sourceMap == null || sourceMap.size() == 0);
    }

    /**
     * 校验String是否为空
     * 
     * @param sourceString
     * @return
     */
    public static boolean isEmpty(String sourceString) {
	return (sourceString == null || sourceString.length() == 0);
    }

    /**
     * 校验String是由空格组成
     * 
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
	return (str == null || str.trim().length() == 0);
    }

    /**
     * 校验JSONArray是否为空
     * 
     * @param sourceJSONArray
     * @return
     */
    public static boolean isEmpty(JSONArray sourceJSONArray) {
	return (sourceJSONArray == null || sourceJSONArray.length() == 0);
    }

    /**
     * 获取非空,非空格,非"null"字符串
     * 
     * @param str 源字符串
     * @return trim后的字符串或者空串
     */
    public static String getUnNullString(String str) {
	if (isBlank(str)) {
	    return "";
	} else if (str.trim().toLowerCase(Locale.ENGLISH).equals("null")) {
	    return "";
	} else
	    return str.trim();
    }

}