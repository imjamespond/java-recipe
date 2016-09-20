package test;

import java.util.Calendar;

import com.EpochUtil;

public class DateTest {

	public static void main(String[] args) {
		final long EXPIRE_TIME_OFFSET = 5*3600*1000l;
		long today = System.currentTimeMillis() - EXPIRE_TIME_OFFSET;
		long time = System.currentTimeMillis();//1418835219287l;
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(time);
		System.out.println(ca.toString());
		System.out.println(EpochUtil.getEpochDay(time));
		System.out.println(EpochUtil.getEpochDay(today));
		System.out.println(ca.get(Calendar.DAY_OF_MONTH));
		System.out.println(ca.getTime());
	}

}
