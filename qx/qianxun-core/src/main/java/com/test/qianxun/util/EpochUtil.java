package com.test.qianxun.util;

import java.util.TimeZone;

public class EpochUtil {
	public static final long SEC_MILLISECS = 1000l;
	public static final long HOUR_MILLISECS = 3600 * SEC_MILLISECS;
	public static final long DAY_MILLISECS = 24 * HOUR_MILLISECS;
	public static final long WEEK_MILLISECS = 7 * DAY_MILLISECS;
	public static final long WEEK_OFFSET = 3 * DAY_MILLISECS;//19700101IsTHIRSDAY
	private static final TimeZone TIMEZONE = TimeZone.getTimeZone("Asia/Shanghai");//TimeZone.getDefault();
	private static final long TZ_OFFSET = Long.valueOf(TIMEZONE.getRawOffset());

	public static int getEpochDay() {
		long current = System.currentTimeMillis();
		long now = current + TZ_OFFSET;
		int epochDay = (int) (now / DAY_MILLISECS);//System.out.println("epochDay:"+epochDay);
		return epochDay;
	}
	
	public static int getEpochDay(long current) {
		long now = current + TZ_OFFSET;
		int epochDay = (int) (now / DAY_MILLISECS);//System.out.println("epochDay:"+epochDay);
		return epochDay;
	}

	public static int getEpochWeek() {
		long current = System.currentTimeMillis();
		long now = current + TZ_OFFSET-WEEK_OFFSET;
		int epochDay = (int) (now / WEEK_MILLISECS);//System.out.println("epochDay:"+epochDay);
		return epochDay;
	}
	
	public static int getEpochWeek(long current) {
		long now = current + TZ_OFFSET-WEEK_OFFSET;
		int epochDay = (int) (now / WEEK_MILLISECS);//System.out.println("epochDay:"+epochDay);
		return epochDay;
	}
	
	public static int getHour() {
		long current = System.currentTimeMillis();
		long now = current + TZ_OFFSET;
		long nowSec = now % DAY_MILLISECS;
		return (int) (nowSec / HOUR_MILLISECS);
	}

	public static int getSec() {
		long current = System.currentTimeMillis();
		long now = current + TZ_OFFSET;
		long nowSec = now % DAY_MILLISECS;
		int hour = (int) (nowSec / HOUR_MILLISECS);
		return (int) (nowSec / SEC_MILLISECS - hour * 3600l);
	}
/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TimeZone tz = TimeZone.getDefault();//TimeZone.getTimeZone("America/New_York");// TimeZone.getDefault();//System.out.println(Arrays.toString(TimeZone.getAvailableIDs()));
		long tz_offset = Long.valueOf(tz.getRawOffset());
		System.out.println("TimeZone:" + tz_offset);
		long current = System.currentTimeMillis();
		long now = current + tz_offset;
		long nowSec = now % EpochUtil.DAY_MILLISECS;
		int hour = (int) (nowSec / EpochUtil.HOUR_MILLISECS);
		int sec = (int) (nowSec / EpochUtil.SEC_MILLISECS - hour * 3600l);
		System.out.println("time stamp:" + now + ",hour:" + hour + ",sec:"
				+ sec + ",day:"
				+ ((double) now / (double) EpochUtil.DAY_MILLISECS) + ",tz:"
				+ tz.getDisplayName());
		Calendar ca = Calendar.getInstance();
		// ca.setTimeInMillis(DAY_MICROSECS);
		Calendar caEpoch = Calendar.getInstance();
		caEpoch.setTimeInMillis(0);
		int caDay = ca.get(Calendar.HOUR_OF_DAY);
		int caMin = ca.get(Calendar.MINUTE);
		int caSec = ca.get(Calendar.SECOND) + caMin * 60;
		int caDays = 0;
		while (caEpoch.getTimeInMillis() < ca.getTimeInMillis()) {
			if (caEpoch.get(Calendar.YEAR) < ca.get(Calendar.YEAR)) {
				caDays++;
			} else if (caEpoch.get(Calendar.YEAR) == ca.get(Calendar.YEAR)) {
				if (caEpoch.get(Calendar.DAY_OF_YEAR) < ca
						.get(Calendar.DAY_OF_YEAR)) {
					caDays++;
				}
			}
			caEpoch.add(Calendar.DAY_OF_YEAR, 1);
		}
		System.out.println("time stamp:" + ca.getTimeInMillis() + ",hour:"
				+ caDay + ",sec:" + caSec + ",day:" + caDays);
		long before = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			EpochUtil.getEpochDay();
		}
		now = System.currentTimeMillis();
		System.out.println("EpochUtil:" + (now - before));
		before = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			Calendar tmp = Calendar.getInstance();
			tmp.get(Calendar.DAY_OF_YEAR);
		}
		now = System.currentTimeMillis();
		System.out.println("Calendar DAY_OF_YEAR:" + (now - before));
	}*/
}