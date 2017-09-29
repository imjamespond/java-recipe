package com.metasoft.kafka.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Commons {
	public static String GetRegularDate(Long millis){
		if(millis<=0)
			millis = System.currentTimeMillis();
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return f.format(new Date(millis));
	}
	
	public static <T> T FromJson(String json){
		T rs = new Gson().fromJson(json, new TypeToken<T>(){}.getType());
		return rs;
	}
	
	public static Object InvokeMethod(String clazzName, String methodName, 
			Object obj, List<String> argClasses, List<Object> argObjs) 
	 throws NoSuchMethodException, SecurityException, IllegalAccessException, 
	 		IllegalArgumentException, InvocationTargetException, ClassNotFoundException{
		List<Class<?>> arg_classes = new ArrayList<Class<?>>();
		for(String argClass : argClasses){
			Class<?> clazz = Class.forName(argClass);
			arg_classes.add(clazz);
		}  
		Class<?>[] argclasses = arg_classes.toArray(new Class<?>[0]);
		Object[] args = argObjs.toArray();
		Class<?> clazz = Class.forName(clazzName);
		Method method = clazz.getMethod(methodName, argclasses );
		return method.invoke(obj, args);
	}
	
	public static void main(String[] args) throws Exception {
		List<String> argClasses = new ArrayList<String>(Arrays.asList("java.lang.Long"));
		List<Object> argObjs = new ArrayList<Object>(Arrays.asList(new Long(99999L)));

    	//helper.setArgClasses(Arrays.asList("[Ljava.lang.String;"));
    	//helper.setArgObjs(new ArrayList<Object>(Arrays.asList(new String[1])));
		
		Map<String, Object> map2json = new HashMap<String, Object>();
		map2json.put("arg-classes", argClasses);
		map2json.put("arg-objs", argObjs);
		
		String json = new Gson().toJson(map2json);
		System.out.println(json);
		
		//one way deal with casting
		Map<String, Object> mapFromJson = Commons.FromJson(json);
		Object objArgClasses = mapFromJson.get("arg-classes");
		if(objArgClasses instanceof List<?>) {
//			for(Object obj : (List<?>) objArgClasses){
//				
//			}
		}
		//another way
		@SuppressWarnings("unchecked")
		List<String> argClasses_ = (List<String>) objArgClasses;
		//FIXME gson has converted Long to Double which will not match the type
		//Object objArgObjs = mapFromJson.get("arg-objs");
		//@SuppressWarnings("unchecked")
		//List<Object> argObjs_ = (List<Object>) objArgObjs;
		
		System.out.println(Commons.InvokeMethod("com.metasoft.util.Commons", "GetRegularDate", null, argClasses_, argObjs));
	}
}
