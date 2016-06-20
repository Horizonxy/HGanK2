package com.horizon.gank.hgank.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具
 * @author mrh
 *
 */
public class DateUtils{
	
	public static String TIME_PATTERN_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static String TIME_PATTERN_YMDHMS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static String TIME_PATTERN_YMDHM = "yyyy-MM-dd HH:mm";
	public static String TIME_PATTERN_MD = "MM-dd";
	public static String TIME_PATTERN_HM = "HH:mm";
	public static String TIME_PATTERN_HMS = "HH:mm:ss";
	
	public static String DATE_PATTERN = "yyyy-MM-dd";
	public static String DATE_PATTERN_DIRECTORY = "yyyy/MM/dd";
	public static String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";
	public static String DATE_PATTERN_YYYYMM = "yyyyMM";
	public static String DATE_PATTERN_YYYYMMDDHMS = "yyyyMMddHHmmss";

	public static String DATE_PATTERN_YYYYMMTDDHMSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	public static enum TimeInterval {
		YEAR,
		MONTH,
		WEEK,
		DAY,
		HOUR,
		MINUTE,
		SECOND,
		MILLISECOND
	}
	
	/**
	 * 返回指定格式的系统时间字符串
	 * @return 返回指定格式的系统时间字符串
	 */
	public static String sysDate() {
		return dateToString(new Date(), TIME_PATTERN_YMDHMS);
	}
	
	/**
	 * 返回指定格式的系统时间字符串
	 * @param pattern 显示格式，如果输入null则显示yyyy-MM-dd HH:mm:ss
	 * @return 返回指定格式的系统时间字符串
	 */
	public static String sysDate(String pattern) {
		if(pattern==null||"".equals(pattern)){
			return dateToString(new Date(), TIME_PATTERN_YMDHMS);
		}else{
			return dateToString(new Date(), pattern);
		}
	}
	
	/**
	 * 指定两个日期的时间差
	 * @param interval 时间类型（年、月、日、周、时、分、秒）
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @return 返回date2-date1的时间差值
	 */
	public static long dateDiff(TimeInterval interval,Date date1,Date date2){
		long result=-1,time1, time2;
		time1 = date1.getTime();
		time2 = date2.getTime();
		int time=0;
		Calendar calendar;
		
		switch(interval){
		case YEAR:
			calendar = Calendar.getInstance();
			calendar.setTime(date2);
			time = calendar.get(Calendar.YEAR);
			calendar.setTime(date1);
			result = time - calendar.get(Calendar.YEAR);
			break;
		case MONTH:
			 calendar = Calendar.getInstance();
			 calendar.setTime(date2);
			 time = calendar.get(Calendar.YEAR) * 12;
			 calendar.setTime(date1);
			 time -= calendar.get(Calendar.YEAR) * 12;
			 calendar.setTime(date2);
			 time += calendar.get(Calendar.MONTH);
			 calendar.setTime(date1);
			 result = time - calendar.get(Calendar.MONTH);
			break;
		case WEEK:
			calendar = Calendar.getInstance();
			calendar.setTime(date2);
			time = calendar.get(Calendar.YEAR) * 52;
			calendar.setTime(date1);
			time -= calendar.get(Calendar.YEAR) * 52;
			calendar.setTime(date2);
			time += calendar.get(Calendar.WEEK_OF_YEAR);
			calendar.setTime(date1);
			result = time - calendar.get(Calendar.WEEK_OF_YEAR);
			break;
		case DAY:
			result=(time2-time1)/1000/3600/24;
			break;
		case HOUR:
			result=(time2-time1)/1000/3600;
			break;
		case MINUTE:
			result=(time2-time1)/1000/60;
			break;
		case SECOND:
			result=(time2-time1)/1000;
			break;
		case MILLISECOND:
			result=time2-time1;
			break;
		default:
			result=time2-time1;
			break;
		}
		return result;
	}
	
	/**
	 * 在一个日期上加上一个时间
	 * @param interval 时间类型（年、月、日、周、时、分、秒）
	 * @param date 日期
	 * @param add 加上的数值
	 * @return 返回相加后的日期
	 */
	public static Date dateAdd(TimeInterval interval,Date date,int add){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		switch(interval){
		case YEAR:
			calendar.add(Calendar.YEAR, add);
			break;
		case MONTH:
			calendar.add(Calendar.MONTH, add);
			break;
		case WEEK:
			calendar.add(Calendar.WEEK_OF_YEAR, add);
			break;
		case DAY:
			calendar.add(Calendar.DAY_OF_YEAR, add);
			break;
		case HOUR:
			calendar.add(Calendar.HOUR, add);
			break;
		case MINUTE:
			calendar.add(Calendar.MINUTE, add);
			break;
		case SECOND:
			calendar.add(Calendar.SECOND, add);
			break;
		default:
			break;
		}
		return calendar.getTime();
	}
	
	/**
	 * 获取当前时间的年月日时分秒
	 * @param interval
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static int getTime(TimeInterval interval,String date,String pattern){
		return getTime(interval,stringToDate( date,pattern));
	}
	
	/**
	 * 获取当前时间的年月日时分秒
	 * @param interval
	 * @param date
	 * @return
	 */
	public static int getTime(TimeInterval interval,Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		int retVal = 0;
		switch(interval){
		case YEAR:
			retVal = calendar.get(Calendar.YEAR);
			break;
		case MONTH:
			retVal = calendar.get(Calendar.MONTH);
			break;
		case DAY:
			retVal = calendar.get(Calendar.DAY_OF_MONTH);
			break;
		case HOUR:
			retVal = calendar.get(Calendar.HOUR_OF_DAY);
			break;
		case MINUTE:
			retVal = calendar.get(Calendar.MINUTE);
			break;
		case SECOND:
			retVal = calendar.get(Calendar.SECOND);
			break;
		default:
			break;
		}
		return retVal;
	}
	
	/**
	 * 格式化日期
	 * @param date 日期
	 * @return 返回显示格式为yyyy-MM-dd HH:mm:ss的日期
	 */
	public static Date dateFormat(Date date){
		if(date==null) date=new Date();
		return dateFormat(date,TIME_PATTERN_YMDHMS);
	}
	
	/**
	 * 格式化日期
	 * @param date 日期
	 * @param pattern 显示格式
	 * @return 返回格式化后的日期，如果格式化失败则返回null
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date dateFormat(Date date,String pattern){
		if (date == null) date=new Date();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date result = null;
		try {
			result = format.parse(format.format(date));
		} catch (ParseException e) {
			//e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 取指定日期的周数
	 * @param date 指定日期（如果为null则取当前时间）
	 * @return 返回第几周
	 */
	public static int weekNumOfYear(Date date){
		if(date==null) date=new Date();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 字符串转换成日期
	 * @param date 要转换的日期字符串
	 * @param pattern 显示格式
	 * @return 返回格式化后的日期，如果转换失败则返回null
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date stringToDate(String date, String pattern) {
		if (date == null || date.equals("")) return null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date result = null;
		try {
			result = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 日期转换成字符串
	 * @param date 指定日期
	 * @param pattern 显示格式
	 * @return 返回格式化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date date, String pattern) {
		if (pattern != null && !pattern.equals(""))
			return (new SimpleDateFormat(pattern)).format(date);
		else
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
	}
	
	/**
	 * 判断给定日期是否为当天、距离当前时间七天之内的日期、七天之外的日期 
	 * @param dt
	 * @param type 0:当天 1:7天之内的 2:7天之外的
	 * @return
	 */
	public static boolean getDayDiffFromToday(String dt,int type){
		if(dt == null || "".equals(dt)) return false;
		return getDayDiffFromToday(stringToDate(dt,TIME_PATTERN_YMDHMS),type);
	}
	
	/**
	 * 判断给定日期是否为当天、距离当前时间七天之内的日期、七天之外的日期 
	 * @param dt
	 * @param type 0:当天 1:7天之内的 2:7天之外的
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean getDayDiffFromToday(Date dt,int type){ 
		if(dt == null) return false;
		Date today = new Date(); 
		today.setHours(23); 
		today.setMinutes(59); 
		today.setSeconds(59); 

		long diff = today.getTime() - dt.getTime(); 
		if(diff<0) diff=0; 
		long days = diff/(1000*60*60*24); 

		if(type==0 && days==0)return true; 
		if(type==1 && days>0 && days<=7)return true; 
		if(type==2 && days>7)return true; 

		return false; 
	} 
	
	
	/**
	 * 根据时间戳获得一个指定格式的时间字符串
	 * 
	 * @param millSeconds
	 * @param format  格式，1：yyyy-MM-dd HH:mm，2：yyyy/MM/dd
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static String getFormatTime(long millSeconds, int format) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		if(format == 1) {
			sdf.applyPattern(TIME_PATTERN_YMDHM);
		}else if(format == 2) {
			sdf.applyPattern(DATE_PATTERN_DIRECTORY);
		}else if(format == 3) {
			sdf.applyPattern(TIME_PATTERN_MD);
		}
		
		return sdf.format(new Date(millSeconds));
	}
	
	
	/**
	 *@description 将毫秒时间转为 "yyyy-MM-dd HH:mm:ss";格式
	 * @author 陈永昌
	 * @date 2015年10月29日 上午10:56:04
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatTimeByLong(long millSeconds,int format){
		SimpleDateFormat sdf = new SimpleDateFormat();
		if (format == 1) {
			sdf.applyPattern(TIME_PATTERN_YMDHMS);
		}else if(format == 2){
			sdf.applyPattern("yyyy.MM.dd");
		}
		
		return sdf.format(new Date(millSeconds));
		
	}
	/**
	 * 把gmt时间格式化成long型
	 * lvke
	 * 2015年11月24日下午5:21:29
	 * @param gmtTime
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static long formatGmt(String gmtTime){
		long time = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date ftime = null;
	    try {
		    ftime = sdf.parse(gmtTime);
		    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String s2 = sdf2.format(ftime);
		    Date dt = sdf2.parse(s2);
		    time = dt.getTime();
		} catch (ParseException e) {
	       // TODO Auto-generated catch block
		   e.printStackTrace();
	    }
	    
		return time;
	}
	
	/**
	 * 将字符串时间转为long
	 * @param user_time
	 * @param format
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static long formatStringToLong(String user_time,int format){		  
		SimpleDateFormat sdf = new SimpleDateFormat();  
		if(format == 1) {
			sdf.applyPattern(TIME_PATTERN_YMDHMS_SSS);
		}else if(format == 2) {
			sdf.applyPattern(DATE_PATTERN);
		}
		
		Date d;  
		try {  
			d = sdf.parse(user_time);  
			long l = d.getTime();  
			return l;
		} catch (ParseException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace(); 
			return System.currentTimeMillis();
		}  
	} 
}


