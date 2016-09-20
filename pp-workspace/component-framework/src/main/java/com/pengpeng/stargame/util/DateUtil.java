package com.pengpeng.stargame.util;

import java.text.DateFormat;
import
        java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日期处理工具类
 */
public class DateUtil {
	private static Log log = LogFactory.getLog(DateUtil.class);
	private static String datePattern = "yyyy-MM-dd";
	private static String timePattern = "HH:mm";
	private static int updateHours;

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String DEFAULT_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE = "yyyy-MM-dd";

	/**
	 * yyyy年MM月dd日 HH:mm
	 */
	public static final String CHINESE_DATE_TIME = "yyyy年MM月dd日 HH:mm:ss.SSS";

	/**
	 * HH:mm:ss.SSS
	 */
	public static String FALSHE_TIME = "HH:mm:ss.SSS";

	/**
	 * 将日期转换成指定字符串的显示格式 date默认是当前时间 S默认是"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            日期
	 * @param s
	 *            要显示的格式
	 * @return 转换显示后的字符串
	 */
	public static String getDateFormat(Date date, String s) {
		if (date == null)
			date = new Date();
		if (s == null)
			s = "yyyy-MM-dd HH:mm:ss";
		StringBuffer s1 = new StringBuffer();
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(s);
			s1.append(simpledateformat.format(date));
		} catch (Exception exception) {
			return "";
		}
		return s1.toString();
	}
	/**
	 * 将日期字符串 转换成日期
	 * 
	 * @param aMask
	 *            字符串格式
	 * @param strDate
	 *            日期字符串
	 * @return 日期
	 */
	public static final Date convertStringToDate(String aMask, String strDate) {
		if (aMask == null) {
			aMask = DateUtil.datePattern + " " + DateUtil.timePattern;
		}
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		try {
			date = df.parse(strDate);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return (date);
	}

    public static  Date   convertMsecondToString(long mSecond) {
        Date date=new Date(mSecond);
        return  date;
    }
	/**
	 * 获取下次刷新时间
	 */
	public static  Date getNextCountTime() {
		Date current = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(current);
		if (c.get(Calendar.HOUR_OF_DAY) >= updateHours) {
			c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
			c.set(Calendar.HOUR_OF_DAY, updateHours);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
		} else {
			c.set(Calendar.HOUR_OF_DAY, updateHours);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
		}
		return c.getTime();
	}

    /**
     *
     * @param week 1 星期天 2星期一
     * @return
     */
    public static Date getNextWeekTime(int week){
        Calendar c = Calendar.getInstance();
        int currentWeek=c.get(Calendar.DAY_OF_WEEK) ;
        if(currentWeek==week){
          c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+7);
        }else if(currentWeek<week){
           c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+week-currentWeek);
        }else if(currentWeek>week){
            c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+7-(currentWeek-week));
        }
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();

    }
    /**
     * 当前 时间 加上 指定的天
     * @return
     */
    public static Date getAddDay(int day){
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+day);
        return c.getTime();
    }

    /**
     * 当前 时间 加上 指定的小时数
     * @param hours
     * @return
     */
    public static Date getAddHours(int hours){
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY)+hours);
        return c.getTime();
    }

	/**
	 * 在指定的时间上加上多分钟。
	 *
	 * @param date    指定时间
	 * @param minutes 分钟数
	 * @return 新时间
	 */
	public static Date addMinute(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	/**
	 * 在指定的时间上加上多少秒。
	 *
	 * @param date    指定时间
	 * @param second  秒数
	 * @return 新时间
	 */
	public static Date addSecond(Date date, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}

	/**
	 * 时间比较
	 * @param sourceTime 源时间
	 * @param targetTime 目标时间
	 * @return   0  : sourceTime == targetTime ;<br/>
	 * 		     1  : sourceTime > targetTime  ;<br/>
	 * 			-1  : sourceTime < targetTime  ;<br/>
	 */
	public static int compareDate(Date sourceTime, Date targetTime){
		if(sourceTime.after(targetTime)){
			return 1;
		}

		if(sourceTime.before(targetTime)){
			return -1;
		}
		return 0;
	}

	/**
	 * 字符串 转 日期
	 *
	 * @param dateStr 日期格式字符串
	 * @param dateFmt 转换格式
	 * @return
	 * @throws ParseException If the string and the format is not match, exception will be thrown
	 */
	public static Date toUtilDate(String dateStr, String dateFmt) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(dateFmt);
		return format.parse(dateStr.trim());
	}

	/**
	 * 日期 转 字符串
	 *
	 * @param date   日期
	 * @param patten 字符串格式
	 * @return str
	 */
	public static String parseDateString(Date date, String patten) {
		if (date == null || StringUtils.isBlank(patten)) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(patten);
		return format.format(date);
	}

	/**
	 * 计算时间差 精确到秒
	 *
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @return 秒数
	 */
	public static int callSecond(Date beginTime, Date endTime) {
		return (int) (endTime.getTime() - beginTime.getTime()) / 1000;
	}

	public int getUpdateHours() {
		return updateHours;
	}
	public void setUpdateHours(int updateHours) {
		this.updateHours = updateHours;
	}

	public static void main(String[] args) throws ParseException {
                                                  System.out.println(getDateFormat(new Date(),"yyyyMMdd"));
//		System.out.println(getDateFormat(getAddHours(2), null));
//		System.out.println(DateUtil.parseDateString(DateUtil.toUtilDate("14:10:04.656",DateUtil.FALSHE_TIME),DateUtil.CHINESE_DATE_TIME));
//
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String d1 = "2009-05-25 10:21:21";
//		String d2 = "2009-05-25 10:21:00";
//
//		Date date1;
//		Date date2;
//		try {
//			date1 = df.parse(d1);
//			date2 = df.parse(d2);
//			System.out.println("相差"+(date1.getTime() - date2.getTime())/1000+"秒");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
        Calendar calendar=Calendar.getInstance();
        System.out.print(calendar.get(Calendar.HOUR_OF_DAY));

	}

    public static int getDayOfYear(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int getWeekOfYear(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    public static int getMonthOfYear(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }


    public static Date getZeroTime(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }
}
