package com.metasoft.flying.test;

import java.util.Calendar;

import com.metasoft.flying.util.EpochUtil;

public class TestEpoch {

	public static void main(String[] args) {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 15);
		System.out.println(EpochUtil.getEpochDay(ca.getTimeInMillis()));
		ca.set(Calendar.HOUR_OF_DAY, 0);
		System.out.println(EpochUtil.getEpochDay(ca.getTimeInMillis()));
		ca.set(Calendar.HOUR_OF_DAY, 24);
		System.out.println(EpochUtil.getEpochDay(ca.getTimeInMillis()));
		System.out.println(EpochUtil.getEpochDay());
	}

}
