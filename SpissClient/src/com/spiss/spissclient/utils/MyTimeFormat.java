package com.spiss.spissclient.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTimeFormat {
	
	public static String dateFormat(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static String timeFormat(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String dateTimeFormat(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String noYTimeFormat(String date){
		String[] strs = date.split("-",2);
		return strs[1];
	}

}
