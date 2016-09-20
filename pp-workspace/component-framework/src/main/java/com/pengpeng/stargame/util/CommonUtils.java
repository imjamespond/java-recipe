package com.pengpeng.stargame.util;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 */
public class CommonUtils {

	public static int random(int low, int high) {
		return (int) ((high - low) * Math.random() + low);
	}

    public static String random(int length){
        StringBuilder result=new StringBuilder();
        for(int i=0;i<length;i++){
            result.append(random(0,9));
        }
        return result.toString();
    }

	public static String format(float value) {
		DecimalFormat df = new DecimalFormat("00,000,000.00");
		return df.format(value);
	}

	/**
	 * 把Date类型转化为指定格试的字符串
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static String formatDate(Date value, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(value);
	}

	public static Date getNewDate(Date oldDate, int day) {
		Calendar c = Calendar.getInstance();
		if (oldDate != null) {
			c.setTime(oldDate);
		}
		c.add(Calendar.DATE, day);
		return c.getTime();
	}

	public static String formatDecimal(float value) {
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(value);
	}

	public static String md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (byte aB : b) {
				i = aB;
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int[] toIntArray(String src, String spliter) {
		if (src == null)
			return null;
		String[] ary = src.split(spliter);
		int[] values = new int[ary.length];
		for (int i = 0; i < ary.length; i++) {
			if (StringUtils.isBlank(ary[i]))
				continue;
			values[i] = Integer.valueOf(ary[i].trim());
		}
		return values;
	}

	public static double[] toDoubleArray(String src, String spliter) {
		if (src == null)
			return null;
		String[] ary = src.split(spliter);
		double[] values = new double[ary.length];
		for (int i = 0; i < ary.length; i++) {
			values[i] = Double.valueOf(ary[i].trim());
		}
		return values;
	}

    public static void main(String[] args){
        //System.out.print(CommonUtils.md5(System.currentTimeMillis()+""));
        //System.out.print(CommonUtils.md5("1000"+"12345"+"tws1"+"GTHJ24F6S90ADFss2bAfAKDP"));
        System.out.print(CommonUtils.md5("12345"+"tws1"+"F6AB0GTdJ24ADFPcS902bKDP"+"1000"+"12345"+"10"+"0"));
    }
}
