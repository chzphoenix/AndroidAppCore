package com.hcz.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间管理辅助功能类
 */
public class DateUtil {
	public static final String HH_MM = "HH:mm";


	/**
	 * 获取当前日期时间
	 * @return
	 */
	public static String getCurrentData(String formatStr) throws ParseException {
		String dateStr = "";
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
		dateStr = simpleDateFormat.format(date);
		return dateStr;
	}


	/**
	 * 时间对象转换成字符串
	 * @param date 
	 */
	public static String date2string(Date date, String formatStr) {  
		String strDate = "";  
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);  
		strDate = sdf.format(date);  
		return strDate;  
	}  


	/**
	 * 字符串转换成时间对象
	 * @param dateString 
	 * @param formatStr 
	 */
	public static Date string2date(String dateString, String formatStr) {  
		Date formateDate = null;  
		DateFormat format = new SimpleDateFormat(formatStr);  
		try {  
			formateDate = format.parse(dateString);  
		} catch (ParseException e) {  
			return null;  
		}  
		return formateDate;  
	}  


	/**
	 * 获取与当前时间距离
	 * @param time 
	 */
	public static String parseTimeToDistance(long time) {
		Calendar cal = Calendar.getInstance();  
		long timel = cal.getTimeInMillis() - time;  
		if (timel / 1000 < 60) {  
			return "1分钟以内";  
		} else if (timel / 1000 / 60 < 60) {  
			return timel / 1000 / 60 + "分钟前";  
		} else if (timel / 1000 / 60 / 60 < 24) {  
			return timel / 1000 / 60 / 60 + "小时前";  
		} else {  
			return timel / 1000 / 60 / 60 / 24 + "天前";  
		}  
	}

	/**
	 * 将Timestamp转化成今天、昨天和具体日期的时间
	 * @param timestamp
	 * @return
	 */
	public static String parseTimeToTodayYesterday(long timestamp, String dateFormat) {
		Calendar nowCal = Calendar.getInstance();
		Calendar targetCal = Calendar.getInstance();
		targetCal.setTimeInMillis(timestamp);
		SimpleDateFormat format = new SimpleDateFormat(HH_MM);
		SimpleDateFormat format2=new SimpleDateFormat(dateFormat);
		if(nowCal.get(Calendar.DATE) - targetCal.get(Calendar.DATE) == 0) {
			//今天
			return format.format(timestamp);
		} else if(nowCal.get(Calendar.DATE) - targetCal.get(Calendar.DATE) == 1) {
			//昨天
			return "昨天" + format.format(timestamp);
		} else {
			//昨天以前
			return format2.format(timestamp);
		}
	}


	/**
	 * 获取该时间所在月份的月初时间。
	 * @param time
	 * @return
	 */
	public static long  getMouthStartTime(long time){
		Date date = new Date(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}


	/**
	 * 获取该时间当天开始时间。
	 * @param time
	 * @return
	 */
	public static long getDayStart(long time){
		Date date = new Date(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}


	/**
	 * 获取该时间当天结束。
	 * @param time
	 * @return
	 */
	public static long getDayEnd(long time){
		Date date = new Date(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

}
