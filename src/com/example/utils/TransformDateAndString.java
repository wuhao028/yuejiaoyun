package com.example.utils;

import java.util.Locale;

/**
 * 实现Date与String 转换的 工具类
 */
public class TransformDateAndString {

	/**
	 * 将Unix时间戳字符串（精确到秒）转换为按参数formats格式显示的字符串
	 * @param timestampString
	 * @param formats
	 * @return formateDate
	 */
	public static String TimeStamp2Date(String timestampString, String formats){    
		  Long timestamp = Long.parseLong(timestampString)*1000;    
		  String formateDate = new java.text.SimpleDateFormat(formats,Locale.getDefault()).format(new java.util.Date(timestamp));    
		  return formateDate;    
		} 
}
