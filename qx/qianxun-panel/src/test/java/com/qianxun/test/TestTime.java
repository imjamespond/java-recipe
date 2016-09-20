package com.qianxun.test;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateUtils;

public class TestTime {
	private static final long OFFSET = DateUtils.MILLIS_PER_DAY-5400*1000l;
	public static void main(String[] args) {
		

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis()-OFFSET);
			String year = String.valueOf(c.get(Calendar.YEAR));
			String day = String.valueOf(c.get(Calendar.DAY_OF_YEAR));
			
			System.out.println("match-rank:"+year+"-"+c.get(Calendar.DAY_OF_MONTH)+"-"+c.get(Calendar.HOUR_OF_DAY));


	}

}
