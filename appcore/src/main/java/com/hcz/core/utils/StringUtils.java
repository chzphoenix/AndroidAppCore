package com.hcz.core.utils;

import java.text.DecimalFormat;

/**
 * 字符串处理类
 */
public class StringUtils {


    /**
     * 将字节转换成kb\mb形式，保留两位小数
     * @param size
     * @return
     */
	public static String parseFileSize(int size){
		StringBuffer sb = new StringBuffer();
		DecimalFormat df = new DecimalFormat(".00");
		double sizeF = 0;
		if(size > 1024 * 1024){
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

}
