package com.chitu.chess.model;

import java.util.Calendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ChessUtils {

	public static Log chessLog = LogFactory.getLog("chess.log");

	public static int Str2Array(String str, int[] arr) {

		String[] arrStr = str.split(",");

		for (int i = 0; i < arr.length; i++) {
			if (i < arrStr.length) {
				arr[i] = Integer.valueOf(arrStr[i]);
			}
		}

		return arrStr.length;
	}

	public static int Str2ArraySetLength(String str, int[] arr, int len) {

		String[] arrStr = str.split(",");
		if (arrStr.length > 3) {
			return 0;
		}
		for (int i = 0; i < arr.length; i++) {
			if (i < arrStr.length && len < arr.length) {// 数组i小于临时数组长度,len要小于数组长度prevent
														// from overrun
				arr[len++] = Integer.valueOf(arrStr[i]);
			}
		}
		return arrStr.length;
	}

	public static boolean isToday(long oldTime) {
		if(oldTime == 0){
			return false;
		}
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(oldTime);
		// chessLog.info(oldTime +"_"+ ca.get(Calendar.DAY_OF_YEAR) +"_"+
		// Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
		return ca.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}
	
	public static boolean timeHasCome(int hour,int minute){
		if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)==hour){
		if(Calendar.getInstance().get(Calendar.MINUTE)>=minute){
			return true;
		}
		}
		if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>hour){
			return true;
		}
		return false;
	}

	public static String md5(String str) {

        if (str== null) {  
            return null;  
        }  
        return DigestUtils.md5Hex(str);  
		//System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密

	}

	public static String point2Title(int point) {
		if (point < 10)
			return "包身工";
		else if (point < 40)
			return "短工";
		else if (point < 80)
			return "佃户";
		else if (point < 140)
			return "贫农";
		else if (point < 230)
			return "渔夫";
		else if (point < 365)
			return "猎人";
		else if (point < 500)
			return "中农";
		else if (point < 700)
			return "富农";
		else if (point < 1000)
			return "掌柜";
		else if (point < 1500)
			return "商人";
		else if (point < 2200)
			return "衙役";
		else if (point < 3000)
			return "小财主";
		else if (point < 4000)
			return "大财主";
		else if (point < 5500)
			return "小地主";
		else if (point < 7700)
			return "大地主";
		else if (point < 10000)
			return "知县";
		else if (point < 14000)
			return "通判";
		else if (point < 20000)
			return "知府";
		else if (point < 30000)
			return "总督";
		else if (point < 45000)
			return "巡抚";
		else if (point < 70000)
			return "丞相";
		else
			return "帝王";
	}

}
