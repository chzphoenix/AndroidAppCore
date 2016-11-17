package com.hcz.core.utils;

import java.text.DecimalFormat;

/**
 * 字符串处理类
 */
public class StringUtils {


	/**
	 * 将字节转换成kb\mb形式，保留n位小数，四舍五入
	 * @param size
	 * @param digit 保留位数
     * @return
     */
	public static String parseFileSize(long size, int digit){
		StringBuffer format = new StringBuffer();
		format.append("#");
		if(digit > 0){
			format.append(".");
			for (int i = 0; i < digit; i++){
				format.append("#");
			}
		}
		DecimalFormat df = new DecimalFormat(format.toString());
		double sizeF = 0;
		StringBuffer sb = new StringBuffer();
		if(size > 1024 * 1024 * 1024){
			sizeF = size / 1024f / 1024f / 1024f;
			sb.append(df.format(sizeF));
			sb.append("GB");
		}
		else if(size > 1024 * 1024){
			sizeF = size / 1024f / 1024f;
			sb.append(df.format(sizeF));
			sb.append("MB");
		}
		else if(size > 1024){
			sizeF = size / 1024f;
			sb.append(df.format(sizeF));
			sb.append("KB");
		}
		else{
			sb.append(size);
		}
		return sb.toString();
	}


	/**
	 * 将价格转化为人民币千分位格式，保留n位小数，四舍五入
	 * @param price
	 * @param digit
     * @return
     */
	public static String parseRMB(double price, int digit){
		return parsePrice(price, "¥", digit);
	}

	/**
	 * 将价格转化为美元千分位格式，保留n位小数，四舍五入
	 * @param price
	 * @param digit
     * @return
     */
	public static String parseDollar(double price, int digit){
		return parsePrice(price, "$", digit);
	}

	/**
	 * 将价格转化为千分位格式，保留n位小数，四舍五入
	 * @param price
	 * @param currency 货币符号
	 * @param digit 保留位数
     * @return
     */
	public static String parsePrice(double price, String currency, int digit){
		StringBuffer format = new StringBuffer();
		format.append("###,###");
		if(digit > 0){
			format.append(".");
			for (int i = 0; i < digit; i++){
				format.append("#");
			}
		}
		DecimalFormat df = new DecimalFormat(format.toString());
		StringBuffer sb = new StringBuffer();
		sb.append(currency);
		sb.append(df.format(price));
		return sb.toString();
	}

}
