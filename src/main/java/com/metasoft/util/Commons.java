package com.metasoft.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Commons {
	public static String GetRegularDate(long millis){
		if(millis<=0)
			return null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return f.format(new Date(millis));
	}
	
	public static <T> T FromJson(String json){
		T rs = new Gson().fromJson(json, new TypeToken<T>(){}.getType());
		return rs;
	}
}
