package com.test.qianxun;

import java.util.*;

import com.test.qianxun.util.EpochUtil;

public class EpochTest {

	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
		
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.MONTH, 7);
		c.set(Calendar.DAY_OF_MONTH, 29);
		c.set(Calendar.HOUR_OF_DAY, 23);
		System.out.println(c.get(Calendar.DAY_OF_MONTH));
		System.out.println(c.get(Calendar.HOUR_OF_DAY));
		System.out.println(c.get(Calendar.MINUTE));
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
		
		System.out.println(EpochUtil.getEpochWeek());
		System.out.println(EpochUtil.getEpochWeek(c.getTimeInMillis()));
	}

}
